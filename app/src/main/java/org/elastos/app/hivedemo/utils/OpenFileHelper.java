package org.elastos.app.hivedemo.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

public class OpenFileHelper {
    private static final String AUTHORITY = "org.elastos.hive.demo.provider";
    public static Intent openFile(Context context , String filePath){
        File file = new File(filePath);
        if(!file.exists()){
            return null;
        }

        String fileName = file.getName();
        int fileNameLength = fileName.length();
        String end= fileName.substring(fileName.lastIndexOf(".") + 1,fileNameLength).toLowerCase();
        if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
            return getAudioFileIntent(context,filePath);
        }else if(end.equals("3gp")||end.equals("mp4")){
            return getAudioFileIntent(context,filePath);
        }else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                end.equals("jpeg")||end.equals("bmp")){
            return getImageFileIntent(context,filePath);
        }else if(end.equals("apk")){
            return getApkFileIntent(context,filePath);
        }else if(end.equals("ppt")){
            return getPptFileIntent(context,filePath);
        }else if(end.equals("xls")){
            return getExcelFileIntent(context,filePath);
        }else if(end.equals("doc")){
            return getWordFileIntent(context,filePath);
        }else if(end.equals("pdf")){
            return getPdfFileIntent(context,filePath);
        }else if(end.equals("chm")){
            return getChmFileIntent(context,filePath);
        }else if(end.equals("txt")){
            return getTextFileIntent(context,filePath);
        }else{
            return getAllIntent(context,filePath);
        }
    }

    public static Intent getAllIntent(Context context , String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context,
                    AUTHORITY, new File(param));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(param));
        }
        intent.setDataAndType(uri,"*/*");
        return intent;
    }

    public static Intent getApkFileIntent(Context context , String param) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context,
                    AUTHORITY, new File(param));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(param));
        }
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        return intent;
    }

    public static Intent getVideoFileIntent(Context context , String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context,
                    AUTHORITY, new File(param));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(param));
        }
        intent.setDataAndType(uri, "video/*");
        return intent;
    }

    public static Intent getAudioFileIntent(Context context , String param){
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context,
                    AUTHORITY, new File(param));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(param));
        }
        intent.setDataAndType(uri, "audio/*");
        return intent;
    }

    public static Intent getHtmlFileIntent(String param){
        Uri uri = Uri.parse(param ).buildUpon().encodedAuthority("com.android.htmlfileprovider")
                .scheme("content").encodedPath(param ).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    public static Intent getImageFileIntent(Context context , String param) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context,
                    AUTHORITY, new File(param));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(param));
        }
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    public static Intent getPptFileIntent(Context context , String param){
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context,
                    AUTHORITY, new File(param));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(param));
        }
        intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        return intent;
    }

    public static Intent getExcelFileIntent(Context context , String param){
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context,
                    AUTHORITY, new File(param));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(param));
        }
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    public static Intent getWordFileIntent(Context context , String param){
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context,
                    AUTHORITY, new File(param));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(param));
        }
        intent.setDataAndType(uri, "application/msword");
        return intent;
    }

    public static Intent getChmFileIntent(Context context , String param){
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context,
                    AUTHORITY, new File(param));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(param));
        }
        intent.setDataAndType(uri, "application/x-chm");
        return intent;
    }

    public static Intent getTextFileIntent(Context context , String param){

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context,
                    AUTHORITY, new File(param));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(param));
        }
        intent.setDataAndType(uri, "text/plain");
        return intent;
    }

    public static Intent getPdfFileIntent(Context context , String param ){
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri ;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context,
                    AUTHORITY, new File(param));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(param));
        }
        intent.setDataAndType(uri, "application/pdf");
        return intent;
    }
}
