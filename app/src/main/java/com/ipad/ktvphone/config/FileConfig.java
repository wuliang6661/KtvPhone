package com.ipad.ktvphone.config;

import android.os.Environment;

import com.ipad.ktvphone.utils.AppManager;

import java.io.File;

/**
 * author : wuliang
 * e-mail : wuliang6661@163.com
 * date   : 2019/7/1910:29
 * desc   :  本地文件存储目录
 * version: 1.0
 */
public class FileConfig {


    /**
     * 所有文件的父级目录
     */
    public static String getBaseFile() {
        return AppManager.getAppManager().curremtActivity().getExternalFilesDir(null).getPath() + "/box_project";
    }


    /**
     * 存储崩溃日志的文件夹
     */
    public static String getLogFile() {
        return getBaseFile() + File.separator + "ExpLog";
    }


    /**
     * 存储上传头像的文件夹
     */
    public static String getImgFile() {
        return getBaseFile() + File.separator + "IMG";
    }


    /**
     * 存储目录文件的文件夹
     */
    public static String getMlFile() {
        return getBaseFile() + File.separator + "MUlU";
    }


    /**
     * 存储更新apk 的文件夹
     */
    public static String getApkFile() {
        return getBaseFile() + File.separator + "APKFile";
    }

}
