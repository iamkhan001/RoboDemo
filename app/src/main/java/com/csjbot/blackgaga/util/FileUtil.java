package com.csjbot.blackgaga.util;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.csjbot.blackgaga.global.Constants;
import com.csjbot.cosclient.utils.CosLogger;
import com.csjbot.coshandler.log.Csjlogger;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;

import okhttp3.ResponseBody;

/**
 * Created by xiasuhuei321 on 2017/8/14.
 * author:luo
 * e-mail:xiasuhuei321@163.com
 */

public class FileUtil {
    public static boolean copy(File from, File to) {
        long usableSpace = FileUtil.getSDFreeSize() * 1024 * 1024;
        if (from.length() > usableSpace) {
            CosLogger.debug("内存空间不足");
            return false;
        }

//        if (!to.exists()) to.mkdirs();

        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            if (!to.getParentFile().exists()) {
                // 如果父目录不存在，创建
                to.getParentFile().mkdirs();
            }
            if (!to.exists()) {
                // 如果文件不存在，创建
                to.createNewFile();
            }

            fis = new FileInputStream(from);
            fos = new FileOutputStream(to);
            byte[] data = new byte[1024];
            int len = 0;

            while ((len = fis.read(data)) != -1) {
                fos.write(data, 0, len);
                fos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Csjlogger.debug(e +"拷贝出错！");
            return false;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return true;
    }
    /**
     * 创建文件夹
     *
     * @param filePath
     */
    public static void makeRootDirectory(String filePath) {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
            } catch (Exception e) {

            }
        }
    }
    /**
     * SD卡剩余空间
     *
     * @return
     */
    public static long getSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }

    /**
     * 获取手机内部可用存储空间
     *
     * @param context
     * @return 以M, G为单位的容量
     */
    public static long getAvailableInternalMemorySize(Context context) {
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = 0;
        long blockSizeLong = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            availableBlocksLong = statFs.getAvailableBlocksLong();
            blockSizeLong = statFs.getBlockSizeLong();
        }

        return availableBlocksLong;
    }

    /**
     * 获取URL中的文件名
     * @param path
     * @return
     */
    public static String getFileName(String path) {
        String fileName = "";
        if (path != null && !path.trim().equals("")) {
            if (path.contains("/")) {
                String[] strArray = path.split("/");
                fileName = strArray[strArray.length - 1];
            }
        }
        return fileName;
    }

    /**
     * @param context
     * @return
     * @throws Exception
     *             获取当前缓存
     */
    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
        }
        return getFormatSize(cacheSize);
    }

    /**
     * @param context
     *            删除缓存
     */
    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            int size = 0;
            if (children != null) {
                size = children.length;
                for (int i = 0; i < size; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }

        }
        if (dir == null) {
            return true;
        } else {

            return dir.delete();
        }
    }

    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // 获取文件
    // Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
    // 目录，一般放一些长时间保存的数据
    // Context.getExternalCacheDir() -->
    // SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            int size2 = 0;
            if (fileList != null) {
                size2 = fileList.length;
                for (int i = 0; i < size2; i++) {
                    // 如果下面还有文件
                    if (fileList[i].isDirectory()) {
                        size = size + getFolderSize(fileList[i]);
                    } else {
                        size = size + fileList[i].length();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     * 计算缓存的大小
     * @param size
     * @return
     */
    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            // return size + "Byte";
            return "0K";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }

    /**
     * 判断文件大小是否超过sizeKB
     * @return
     */
    public static boolean isExceedSize(String path, int limit, String format) {
        boolean flag = false;
        File file = new File(path);
        Long size = file.length();
        if (file.exists()) {
            switch (format){
                case Constants.KB:
                    if (size/1024 > limit) {
                        flag = true;
                    }
                    break;
                case Constants.MB:
                    if (size/(1024*1024) > limit) {
                        flag = true;
                    }
                    break;
                case Constants.GB:
                    if (size/(1024*1024*1024) > limit) {
                        flag = true;
                    }
                    break;
                case Constants.TB:
                    if (size/(1024*1024*1024*1024) > limit) {
                        flag = true;
                    }
                    break;
                default:
                    break;
            }
        }
        return flag;
    }

    public static String getFilePathFromUri( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getRealPathFromUri_AboveApi19(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//
                projection, selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    public static String getPath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Csjlogger.debug("getPath", split[0]);
                String fileName = Constants.USB_PATH + "/" + split[1];
                String fileName2 = Environment.getExternalStorageDirectory() + "/" + split[1];
                if (new File(fileName).exists()) {
                    return fileName;
                } else if (new File(fileName2).exists()){
                    return fileName2;
                }
//                if ("primary".equalsIgnoreCase(type)) {
//                    return Environment.getExternalStorageDirectory() + "/" + split[1];
//                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     *
     * @return
     */
    public static Boolean isExistFile(String filename, String subfixx) {
        if(StrUtil.isBlank(filename) || !subfixx.contains(".txt")) {//没有文件名返回null
            return false;
        } else if (!new File(filename).exists()) {
            return false;
        }
        return true;
    }

    public static String readToString(String path, String fileName) {
        String filename = path + fileName;
        if(StrUtil.isBlank(fileName) || !fileName.contains(".txt")) {//没有文件名返回null
            return fileName;
        }
        FileInputStream file = null;
        BufferedReader reader = null;
        InputStreamReader inputFileReader = null;
        String content = "";
        String tempString = null;
        try {
            file = new FileInputStream(filename);
            inputFileReader = new InputStreamReader(file, checkCode(filename));
            reader = new BufferedReader(inputFileReader);
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                content += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return content;
    }


    /**
     * 以FileWriter方式写入txt文件。
     * @param file：要写入的文件
     * @param content： 要写入的内容
     */
    public static void writeToFile(File file,String content){

        if(file.exists()){
            FileOutputStream writerStream = null;
            try {
                writerStream = new FileOutputStream(file);
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));

                writer.write(content);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static String readToStringUTF8(String filename) {
        if(StrUtil.isBlank(filename) || !filename.contains(".txt")) {//没有文件名返回null
            return "";
        }
        FileInputStream file = null;
        BufferedReader reader = null;
        InputStreamReader inputFileReader = null;
        String content = "";
        String tempString = null;
        try {
            file = new FileInputStream(filename);
            inputFileReader = new InputStreamReader(file,"UTF-8");
            reader = new BufferedReader(inputFileReader);
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                content += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return content;
    }


    public static String readToString(String filename) {
        if(StrUtil.isBlank(filename) || !filename.contains(".txt")) {//没有文件名返回null
            return "";
        }
        FileInputStream file = null;
        BufferedReader reader = null;
        InputStreamReader inputFileReader = null;
        String content = "";
        String tempString = null;
        try {
            file = new FileInputStream(filename);
            inputFileReader = new InputStreamReader(file, checkCode(filename));
            reader = new BufferedReader(inputFileReader);
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                content += tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return content;
    }

    /**
     * 判断编码格式
     *
     * @return
     */
    public static String checkCode(String path) {
        InputStream inputStream;
        String code = "gbk";
        try {
            inputStream = new FileInputStream(path);
            byte[] head = new byte[3];
            inputStream.read(head);
            inputStream.close();
            if (head[0] == -1 && head[1] == -2) {
                code = "UTF-16";
                System.out.println(code);
            }
            if (head[0] == -2 && head[1] == -1) {
                code = "Unicode";
                System.out.println(code);
            }
            if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
                code = "UTF-8";
                System.out.println(code);
            }
            System.out.println(code);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 保存下载好的皮肤包
     *
     * @param body 资源
     * @param name 皮肤包的名字
     * @return
     */
    public static boolean writeResponseBodyToDisk(ResponseBody body, String name) {
        long usableSpace = FileUtil.getSDFreeSize() * 1024 * 1024;
        if (body.contentLength() > usableSpace) {
            CosLogger.debug("内存空间不足");
            return false;
        }
        try {
            // todo change the file location/name according to your needs
            File futureStudioIconFile = new File(Constants.SKIN_PATH + name);
            if (!new File(Constants.SKIN_PATH).exists()) {
                new File(Constants.SKIN_PATH).mkdirs();
            }
            else {
                futureStudioIconFile.delete();
            }

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {

                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    public static Bitmap getSDBitmap(String path) {
        if (!new File(path).exists()) {
            return null;
        }
        FileInputStream f = null;
        try {
            f = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;//图片的长宽都是原来的1/8
        BufferedInputStream bis = new BufferedInputStream(f);
        Bitmap bm = BitmapFactory.decodeStream(bis, null, options);
        return bm;
    }

    /**
     * 返回true是图片动画，返回false是视频
     */
    public static boolean isNaviImage(String filename) {
        boolean flag = false;
        if (StrUtil.isNotBlank(filename)) {
            for (int i=0; i<Constants.imageSuffixs.length; i++) {
                if (filename.contains(Constants.imageSuffixs[i])) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }
}
