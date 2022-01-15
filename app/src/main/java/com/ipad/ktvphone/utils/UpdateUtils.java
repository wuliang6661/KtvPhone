package com.ipad.ktvphone.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ipad.ktvphone.config.FileConfig;
import com.ipad.ktvphone.entity.VersionBO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


/**
 * author : wuliang
 * e-mail : wuliang6661@163.com
 * date   : 2019/6/2111:14
 * desc   : App检查更新的工具类
 * version: 1.0
 */
public class UpdateUtils {


    private static final String TAG = "UpdateUtils";
    private Activity context;

    private boolean mIsCancel = false;
    private final String apkName = "box_project.apk";

    public void checkUpdate(Activity context, VersionBO versionBO) {
        this.context = context;
        if (Integer.parseInt(versionBO.getLatest_version()) > AppUtils.getAppVersionCode()) {
            //默认强制更新
            checkPrission(versionBO.getDownload_url());
        }
    }


    private void checkPrission(String url) {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(context,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    }, 1);
        } else {
            downloadAPK(url);
        }
    }


    /* 开启新线程下载apk文件
     */
    public void downloadAPK(String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mIsCancel = false;
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        File dir = new File(FileConfig.getApkFile());
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        // 下载文件
                        HttpsURLConnection conn = (HttpsURLConnection) new URL(url).openConnection();
                        conn.connect();
                        InputStream is = conn.getInputStream();
//                        int length = conn.getContentLength();

                        File apkFile = new File(FileConfig.getApkFile(), apkName);
                        FileOutputStream fos = new FileOutputStream(apkFile);

                        int count = 0;
                        byte[] buffer = new byte[1024];

                        while (!mIsCancel) {
                            int numread = is.read(buffer);
                            count += numread;
                            Message message = Message.obtain();
                            message.obj = count;
                            handler.sendMessage(message);
                            // 下载完成
                            if (numread < 0) {
                                handler.sendEmptyMessage(0x22);
//                                AppUtils.installApp(apkFile);
                                LogUtils.e(apkFile.getAbsolutePath());
                                startUpdate(apkFile.getAbsolutePath());
//                                InstallApkUtils.excuteSuCMD(apkFile.getAbsolutePath());
                                break;
                            }
                            fos.write(buffer, 0, numread);
                        }
                        fos.close();
                        is.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            LogUtils.e(Thread.currentThread().getName(), "2");
        }
    };


    // 适配android9.0之前的安装方法
    private void startUpdate(String apkPath) {
        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        try {
            // 7.0以后版本需要额外添加
            //           "-i", "当前应用包名",
            // 两个字段，并且需要应用支持 android.permission.INSTALL_PACKAGES 权限**
            process = new ProcessBuilder("pm", "install", "-i", "com.ipad.ktvphone", "-r", apkPath).start();
            successResult = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String s;
            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }
            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (Exception e) {
            ToastUtils.showShort(e.toString());
            Log.e(TAG, "Exception " + e.toString());
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (Exception e) {

            }
            if (process != null) {
                process.destroy();
            }
        }
        Log.e(TAG, "errorMsg " + errorMsg.toString());
        Log.d(TAG, "successMsg " + successMsg.toString());

    }

}
