package org.elastos.app.hivedemo.config;

import android.os.Environment;

public class Config {
    public static String INTERNAL_STORAGE_DEFAULT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String IPFS_DEFAULT_PATH = "/";
    public static String ONEDRIVE_DEFAULT_PATH = "/";
    public static String DEFAULT_UPLOAD_PICK_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String DEFAULT_DOWNLOAD_PICK_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();



    public static String getDefaultPath(ClientType clientType){
        String path = "";
        switch (clientType){
            case IPFS_TYPE:
                path = IPFS_DEFAULT_PATH ;
                break;
            case INTERNAL_STORAGE_TYPE:
                path = INTERNAL_STORAGE_DEFAULT_PATH;
                break;
            case ONEDRIVE_TYPE:
                path = ONEDRIVE_DEFAULT_PATH;
                break;
        }
        return path;
    }
}
