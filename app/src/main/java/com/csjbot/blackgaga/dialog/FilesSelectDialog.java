package com.csjbot.blackgaga.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.csjbot.blackgaga.R;
import com.csjbot.blackgaga.feature.navigation.NaviActivity;
import com.csjbot.blackgaga.feature.navigation.adapter.FileListAdapter;
import com.csjbot.blackgaga.feature.navigation.adapter.FileListNoImageAdapter;
import com.csjbot.blackgaga.global.Constants;
import com.csjbot.blackgaga.util.BlackgagaLogger;
import com.csjbot.blackgaga.util.FileUtil;
import com.csjbot.blackgaga.util.SharedKey;
import com.csjbot.blackgaga.util.SharedPreUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by 孙秀艳 on 2017/11/17.
 */

public class FilesSelectDialog extends Dialog {
    private ListView lv_file_list;
    private FileListNoImageAdapter adapter;
    private List<File> files = new ArrayList<>();
    private MyHandler mHandler;
    private Context mContext;
    private OnDialogClickListener mListener;
    public static String[] fileSuffix = new String[]{
            "txt",
            "mp3"
    };

    public FilesSelectDialog(@NonNull Context context) {
        super(context, R.style.NewRetailDialog);
        mContext = context;
        initView();
    }

    private void initView() {
        setContentView(R.layout.layout_files_dialog);
        setCanceledOnTouchOutside(false);
        lv_file_list = (ListView) findViewById(R.id.lv_file_list);
        mHandler = new MyHandler(this);
        initAdapter();

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = getContext().getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.4);
        lp.height = (int) (d.heightPixels * 0.5);
        dialogWindow.setAttributes(lp);
    }

    private void initAdapter() {
        adapter = new FileListNoImageAdapter();
        adapter.initData(files);
        File root = new File(Constants.USB_PATH);
        new Thread(() -> scanFile(root, mHandler, "txt")).start();
        lv_file_list.setAdapter(adapter);

        lv_file_list.setOnItemClickListener((parent, view, position, id) -> {
            File data = files.get(position);
            FileUtil.makeRootDirectory(Constants.NAV_DATA_PATH);
            File newData = new File(Constants.NAV_DATA_PATH + File.separator + data.getName());

            BlackgagaLogger.debug("原图片路径：" + data.getPath() +
                    "\n新文件图片路径：" + newData.getPath());

            Observable.just(1)
                    .map(integer -> FileUtil.copy(data, newData))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<Boolean>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        }

                        @Override
                        public void onNext(@io.reactivex.annotations.NonNull Boolean b) {
                            if (b) {
                                // 拷贝成功
                                BlackgagaLogger.debug("拷贝地图图片成功");
                                mListener.onItemClicked(newData.getPath());
                                SharedPreUtil.putString(SharedKey.NAVI_NAME, SharedKey.MAP_PATH, newData.getPath());
                            } else {
                                BlackgagaLogger.debug("拷贝地图图片失败");
                                Toast.makeText(mContext, mContext.getString(R.string.copy_image_fail), Toast.LENGTH_SHORT).show();
                            }
                            files.clear();
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            BlackgagaLogger.error(e);
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        });
    }

    public void addFile(File f) {
        files.add(f);
        adapter.notifyDataSetChanged();
    }

    public void scanFile(File root, Handler handler, String suffix) {
        File[] files = root.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                String fileName = file.getName();
                if (fileName.contains(suffix)) {
                    handler.obtainMessage(Constants.SEARCH_SUC, file).sendToTarget();
                } else {
                    if (file.isDirectory()) {
                        scanFile(file, handler, suffix);
                    }
                }
            }
        }
    }

    /**
     * 静态内部类Handler(避免内存泄露)
     */
    class MyHandler extends Handler {
        WeakReference<FilesSelectDialog> mContextWeakReference = null;
        public MyHandler(FilesSelectDialog usbFragment) {
            super();
            mContextWeakReference = new WeakReference<>(usbFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            FilesSelectDialog mapSelectDialog = mContextWeakReference.get();
            if (mapSelectDialog != null) {
                switch (msg.what) {
                    case Constants.SEARCH_SUC:// 搜索到视频文件消息
                        // 发送给recyclerview进行局部刷新
                        addFile((File) msg.obj);
                        break;
                    default:
                        BlackgagaLogger.debug("default???");
                        break;
                }
            }
        }
    }

    public void setListener(FilesSelectDialog.OnDialogClickListener listener){
        mListener = listener;
    }

    public interface OnDialogClickListener{
        void onItemClicked(String path);
    }
}
