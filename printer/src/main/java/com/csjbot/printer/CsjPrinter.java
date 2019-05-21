package com.csjbot.printer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Base64;
import android.util.Log;

import com.csjbot.printer.bean.BluetoothBean;
import com.csjbot.printer.callback.BlueSearchCallback;
import com.csjbot.printer.callback.PrinterConnectCallback;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;

import java.util.ArrayList;
import java.util.UUID;
import java.util.Vector;

/**
 * Created by jingwc on 2018/5/8.
 */

public class CsjPrinter {

    private static final String TAG = CsjPrinter.class.getSimpleName();

    private volatile static CsjPrinter INSTANCE;

    private volatile static boolean isConnectPrinter;

    private static final String ACTION_CONNECT_STATUS = "action.connect.status";

    private Context mContext;

    private GpService mGpService;

    private PrinterServiceConnection mServiceConn;

    private BluetoothAdapter mAdapter;

    private ArrayList<BluetoothBean> mBluetoothList;

    private BlueBroadcastReceiver mBlueReceiver;

    private BluetoothSocket mSocket;

    private ConnectThread mThread;

    private BlueSearchCallback mBlueSearchCallback;

    private PrinterConnectCallback mPrinterConnectCallback;

    public static CsjPrinter getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (CsjPrinter.class) {
                if (INSTANCE == null) {
                    INSTANCE = new CsjPrinter(context);
                }
            }
        }
        return INSTANCE;
    }


    public void setBlueSearchCallback(BlueSearchCallback callback){
        this.mBlueSearchCallback = callback;
    }
    public void setPrinterConnectCallback(PrinterConnectCallback callback){
        this.mPrinterConnectCallback = callback;
    }

    public boolean isConnectPrinter(){
        return mSocket != null && mSocket.isConnected() && isConnectPrinter;
    }

    private CsjPrinter(Context context) {

        this.mContext = context;
        Intent intent = new Intent(context, GpPrintService.class);
        context.startService(intent);

        mServiceConn = new PrinterServiceConnection();
        intent = new Intent();
        intent.setAction("com.gprinter.aidl.GpPrintService");
        intent.setPackage(context.getPackageName());
        mContext.bindService(intent, mServiceConn, Context.BIND_AUTO_CREATE);

    }

    private class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
        }
    }

    public void getBluetoothList(){

        mBluetoothList = new ArrayList<>();

        mAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mAdapter == null){
            Log.d(TAG,"当前设备不支持蓝牙!");
            return;
        }

        if(!mAdapter.isEnabled()){
            if(!mAdapter.enable()){
                return;
            }
        }

        if(mAdapter.isEnabled()){
            mAdapter.startDiscovery();

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
            intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);

            mBlueReceiver = new BlueBroadcastReceiver();
            mContext.registerReceiver(mBlueReceiver, intentFilter);

        }
    }



    public class BlueBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device == null) {
                    return;
                }
                if (device.getName() == null) {
                    return;
                }

                BluetoothBean bluetoothBean = new BluetoothBean();
                bluetoothBean.mBluetoothName = device.getName();
                bluetoothBean.mBluetoothAddress = device.getAddress();
                bluetoothBean.mBluetoothDevice = mAdapter.getRemoteDevice(bluetoothBean.mBluetoothAddress);
                mBluetoothList.add(bluetoothBean);

                Log.i(TAG, "onReceive--->>" + device.getName());
                Log.i(TAG, "onReceive--->> " + mBluetoothList.size());
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.i(TAG, "onReceive--->>" + "搜索完成");
                if (0 == mBluetoothList.size()) {
                    Log.i(TAG, "搜索不到蓝牙设备");
                    if(mBlueSearchCallback != null){
                        mBlueSearchCallback.empty();
                    }
                }else {
                    if(mBlueSearchCallback != null){
                        mBlueSearchCallback.complete(mBluetoothList);
                    }
                }
                mContext.unregisterReceiver(mBlueReceiver);
            }
        }
    }

    public synchronized void connect(BluetoothBean bluetoothBean){
        if (mThread != null) {
            mThread.interrupt();
            mThread = null;
        }
        if (mSocket != null) {
            try {
                mGpService.closePort(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mSocket = null;
        }
        mThread = new ConnectThread(bluetoothBean.mBluetoothAddress, bluetoothBean.mBluetoothDevice);
        mThread.start();
    }

    private class ConnectThread extends Thread {

        private BluetoothDevice mmDevice;

        public ConnectThread(String mac, BluetoothDevice device) {
            mmDevice = device;
            String SPP_UUID = "00001101-0000-1000-8000-00805f9b34fb";
            try {
                if (mSocket == null) {
                    mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(SPP_UUID));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            mAdapter.cancelDiscovery();
            try {
                Log.i(TAG, "run--->> " + "连接socket");
                if (mSocket.isConnected()) {
                    Log.i(TAG, "run--->> " + "已经连接过了");
                } else {
                    if (mSocket != null) {
                        try {
                            if (mGpService != null) {
                                int state = mGpService.getPrinterConnectStatus(0);
                                switch (state) {
                                    case GpDevice.STATE_CONNECTED:
                                        break;
                                    case GpDevice.STATE_LISTEN:
                                        Log.i(TAG, "run--->> " + "state:STATE_LISTEN");
                                        break;
                                    case GpDevice.STATE_CONNECTING:
                                        Log.i(TAG, "run--->> " + "state:STATE_CONNECTING");
                                        break;
                                    case GpDevice.STATE_NONE:
                                        Log.i(TAG, "run--->> " + "state:STATE_NONE");
                                        registerBroadcast();
                                        mGpService.openPort(0, 4, mmDevice.getAddress(), 0);
                                        break;
                                    default:
                                        Log.i(TAG, "run--->> " + "state:default");
                                        break;
                                }
                            } else {
                                Log.i(TAG, "run--->> " + "mGpService IS NULL");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } catch (Exception connectException) {
                Log.i(TAG, "run--->> " + "连接失败");
                try {
                    if (mSocket != null) {
                        mGpService.closePort(0);
                        mSocket = null;
                    }
                } catch (Exception closeException) {

                }
            }
        }
    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CONNECT_STATUS);
        mContext.registerReceiver(printerStatusBroadcastReceiver, filter);
    }

    private BroadcastReceiver printerStatusBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            isConnectPrinter = false;
            if (ACTION_CONNECT_STATUS.equals(intent.getAction())) {
                int type = intent.getIntExtra(GpPrintService.CONNECT_STATUS, 0);
                int id = intent.getIntExtra(GpPrintService.PRINTER_ID, 0);
                if (type == GpDevice.STATE_CONNECTING) {
                    Log.i(TAG, "onReceive--->> " + "STATE_CONNECTING");
                } else if (type == GpDevice.STATE_NONE) {
                    Log.i(TAG, "onReceive--->> " + "STATE_NONE");
                } else if (type == GpDevice.STATE_VALID_PRINTER) {
                    //打印机-有效的打印机
                    Log.i(TAG, "onReceive--->> " + "STATE_VALID_PRINTER");
                } else if (type == GpDevice.STATE_INVALID_PRINTER) {
                    Log.i(TAG, "onReceive--->> " + "STATE_INVALID_PRINTER");
                } else if (type == GpDevice.STATE_CONNECTED) {
                    //表示已连接可以打印
                    Log.i(TAG, "onReceive--->> " + "STATE_CONNECTED");
                    mContext.unregisterReceiver(printerStatusBroadcastReceiver);
                    isConnectPrinter = true;
                    if(mPrinterConnectCallback != null){
                        mPrinterConnectCallback.printerConnected();
                    }
                } else if (type == GpDevice.STATE_LISTEN) {
                    Log.i(TAG, "onReceive--->> " + "STATE_LISTEN");
                }
            }
        }
    };

    public void printer(String title,String number,String content){
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 3);


        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTB, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText(title+"\n"); // 打印文字
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText(number+"\n"); // 打印文字
        esc.addPrintAndLineFeed();

		/* 打印文字 */
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);// 设置打印左对齐
        esc.addText(content+"\n"); // 打印文字

        // 开钱箱
        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
        esc.addPrintAndFeedLines((byte) 6);

        // 加入查询打印机状态，打印完成后，此时会接收到GpCom.ACTION_DEVICE_STATUS广播
        esc.addQueryPrinterStatus();

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(0, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Log.d(TAG, GpCom.getErrorText(r));
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void printerProduct(){
        EscCommand esc = new EscCommand();
        esc.addInitializePrinter();
        esc.addPrintAndFeedLines((byte) 3);


        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);// 设置打印居中
        esc.addSelectPrintModes(EscCommand.FONT.FONTB, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("万达百货\n");
        esc.addText("购物凭证\n");
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addText("**********************\n");
        esc.addText("**********************\n");

        esc.addSelectPrintModes(EscCommand.FONT.FONTB, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("商品名称");
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addText("- - - - - - - - - - - - - - - -"); // 打印文字
        esc.addPrintAndLineFeed();


        esc.addText("名称");
        esc.addSetHorAndVerMotionUnits((byte) 7, (byte) 0);
        esc.addSetAbsolutePrintPosition((short) 4);
        esc.addText("数量");
        esc.addSetAbsolutePrintPosition((short) 8);
        esc.addText("单价");
        esc.addSetAbsolutePrintPosition((short) 12);
        esc.addText("金额");
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addText("- - - - - - - - - - - - - - - -"); // 打印文字
        esc.addPrintAndLineFeed();


        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText(" 兰蔻 精华眼霜 小黑瓶");
        esc.addPrintAndLineFeed();
        esc.addSetAbsolutePrintPosition((short) 5);
        esc.addText("1");
        esc.addSetAbsolutePrintPosition((short) 8);
        esc.addText("398.00");
        esc.addSetAbsolutePrintPosition((short) 12);
        esc.addText("398.00");
        esc.addPrintAndLineFeed();

        esc.addText(" 兰蔻 清莹嫩肤水");
        esc.addPrintAndLineFeed();
        esc.addSetAbsolutePrintPosition((short) 5);
        esc.addText("2");
        esc.addSetAbsolutePrintPosition((short) 8);
        esc.addText("289.00");
        esc.addSetAbsolutePrintPosition((short) 12);
        esc.addText("598.00");
        esc.addPrintAndLineFeed();


        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText("- - - - - - - - - - - - - - - -"); // 打印文字
        esc.addPrintAndLineFeed();


        esc.addSelectPrintModes(EscCommand.FONT.FONTB, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("合计");
        esc.addSetAbsolutePrintPosition((short) 12);
        esc.addText("2220.00");
        esc.addPrintAndLineFeed();

        esc.addText("现金");
        esc.addSetAbsolutePrintPosition((short) 12);
        esc.addText("2400.00");
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addText("找零");
        esc.addSetAbsolutePrintPosition((short) 12);
        esc.addText("10.00");
        esc.addPrintAndLineFeed();

        esc.addText("- - - - - - - - - - - - - - - -"); // 打印文字
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(EscCommand.FONT.FONTB, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("收据号:  ");
        esc.addText("0113");
        esc.addSetAbsolutePrintPosition((short) 10);
        esc.addText("机器号:  ");
        esc.addText("002");
        esc.addPrintAndLineFeed();


        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addText("收款员:  ");
        esc.addText("刘");
        esc.addPrintAndLineFeed();

        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectPrintModes(EscCommand.FONT.FONTB, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("2018-05-11 10:00:00 周五\n");

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 取消倍高倍宽
        esc.addText("*******************\n");
        esc.addText("*******************\n");

        esc.addSelectPrintModes(EscCommand.FONT.FONTB, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("消费者协会监制\n");
        esc.addSelectPrintModes(EscCommand.FONT.FONTB, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);// 设置为倍高倍宽
        esc.addText("谢谢惠顾,欢迎下次光临!\n");
        esc.addPrintAndLineFeed();



        // 开钱箱
        esc.addGeneratePlus(LabelCommand.FOOT.F5, (byte) 255, (byte) 255);
        esc.addPrintAndFeedLines((byte) 6);

        // 加入查询打印机状态，打印完成后，此时会接收到GpCom.ACTION_DEVICE_STATUS广播
        esc.addQueryPrinterStatus();

        Vector<Byte> datas = esc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String sss = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rs;
        try {
            rs = mGpService.sendEscCommand(0, sss);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rs];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Log.d(TAG, GpCom.getErrorText(r));
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
