package org.elastos.app.hivedemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;

import org.elastos.app.hivedemo.action.ActionType;
import org.elastos.app.hivedemo.base.ActionCallback;
import org.elastos.app.hivedemo.base.BaseDataCenter;
import org.elastos.app.hivedemo.base.BasePresenter;
import org.elastos.app.hivedemo.config.ClientType;
import org.elastos.app.hivedemo.config.Config;
import org.elastos.app.hivedemo.utils.FileUtils;
import org.elastos.app.hivedemo.vendor.internal.InternalStorageDataCenter;
import org.elastos.app.hivedemo.vendor.ipfs.IPFSDataCenter;
import org.elastos.app.hivedemo.vendor.onedrive.OneDriveDataCenter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainPresenter extends BasePresenter {
    private Context context ;
    private String internalStorageCurrentPath = Config.INTERNAL_STORAGE_DEFAULT_PATH ;
    private String ipfsCurrentPath = Config.IPFS_DEFAULT_PATH ;
    private String oneDriveCurrentPath = Config.ONEDRIVE_DEFAULT_PATH ;

    private BaseDataCenter internalStorageDataCenter ,oneDriveDataCenter , ipfsDataCenter;

    private IView iView ;
    private ClientType currentClientType = ClientType.INTERNAL_STORAGE_TYPE;
    private ClientType lastClentType = ClientType.INTERNAL_STORAGE_TYPE;

    private final int MESSAGE_FAIL = 0;

    private BaseDataCenter getDataCenter(){
        switch (currentClientType){
            case IPFS_TYPE:
                if (ipfsDataCenter == null){
                    ipfsDataCenter = new IPFSDataCenter(context, new ActionCallback() {
                        @Override
                        public void onPreAction(ActionType type) {
                            iView.showProgressBar();
                        }

                        @Override
                        public void onFail(ActionType type, Exception e) {
                            Message message = new Message();
                            message.what = MESSAGE_FAIL;
                            handler.sendMessage(message);
                        }

                        @Override
                        public void onSuccess(ActionType type, Object body) {
                            iView.hideProgressBar();

                            switch (type){
                                case ACTION_GET_CHILDREN:
                                    ArrayList<FileItem> arrayList = (ArrayList<FileItem>) body;

                                    iView.refreshListView(arrayList);
                                    iView.refreshTitleView(getCurrentPath());
                                    iView.refreshListViewFinish();
                                    break;
                                case ACTION_CREATE_DIR:
                                case ACTION_CREATE_File:
                                case ACTION_UPLOAD_FILE:
                                case ACTION_DELETE_FILE:
                                case ACTION_MOVE_FILE:
                                    refreshData();
                                    break;
                            }
                        }
                    });

                }
                return ipfsDataCenter;
            case ONEDRIVE_TYPE:
                if (oneDriveDataCenter == null){
                    oneDriveDataCenter = new OneDriveDataCenter();
                }
                return oneDriveDataCenter;
            case INTERNAL_STORAGE_TYPE:
                if (internalStorageDataCenter == null){
                    internalStorageDataCenter = new InternalStorageDataCenter();
                }
                return internalStorageDataCenter;

            default:
                return null;
        }
    }

    public MainPresenter(IView iView , Context context){
        this.iView = iView ;
        this.context = context ;
    }

    public void setCurrentPath(String path){
        switch (currentClientType){
            case IPFS_TYPE:
                ipfsCurrentPath = path ;
                break;
            case ONEDRIVE_TYPE:
                oneDriveCurrentPath = path ;
                break;
            case INTERNAL_STORAGE_TYPE:
                internalStorageCurrentPath = path;
                break;
        }
    }

    public String getCurrentPath(){
        switch (currentClientType){
            case IPFS_TYPE:
                return ipfsCurrentPath;
            case ONEDRIVE_TYPE:
                return oneDriveCurrentPath;
            case INTERNAL_STORAGE_TYPE:
                return internalStorageCurrentPath;

            default:
                return null;
        }
    }

    public void refreshData(){
        ArrayList<FileItem> items = null;
        String currentPath = getCurrentPath();
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                items = ((InternalStorageDataCenter)getDataCenter()).getChildrenDetails(currentPath);
                break;
            case ONEDRIVE_TYPE:
                break;

            case IPFS_TYPE:
                items = ((IPFSDataCenter)getDataCenter()).getChildrenDetails(currentPath);
                break;
        }
        
        if (null==items){
            items = new ArrayList<>();
        }
        iView.refreshListView(items);
        iView.refreshTitleView(currentPath);
        iView.refreshListViewFinish();
    }

    public ArrayList<FileItem> getFileItemList(){
        ArrayList<FileItem> fileItems = new ArrayList<>();
        String currentPath = getCurrentPath();
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                fileItems = ((InternalStorageDataCenter)getDataCenter()).getChildrenDetails(currentPath);
                break;
            case IPFS_TYPE:
                fileItems = ((IPFSDataCenter)getDataCenter()).getChildrenDetails(currentPath);
                break;
        }
        return fileItems;
    }

    public void createDirectory(String fileAbsPath){
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                File file = new File(fileAbsPath);
                if (!file.exists()){
                    file.mkdir();
                    refreshData();
                }else{
                    iView.showSameFileDialog();
                }
                break;
            case IPFS_TYPE:
                ((IPFSDataCenter)getDataCenter()).createDirectory(fileAbsPath);
                break;
        }
    }

    public void createFile(String fileAbsPath){
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                File file = new File(fileAbsPath);
                if (!file.exists()){
                    try {
                        file.createNewFile();
                        refreshData();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else{
                    iView.showSameFileDialog();
                }

                break;
            case IPFS_TYPE:
                ((IPFSDataCenter)getDataCenter()).createFile(fileAbsPath);
                break;
        }
    }

    public void uploadFile(String uploadFileAbsPath){
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                break;
            case IPFS_TYPE:
                File file = new File(uploadFileAbsPath);
                String fileName = file.getName();
                String ipfsAbsFilePath = FileUtils.appendParentPath(getCurrentPath(),fileName);
                ((IPFSDataCenter)getDataCenter()).uploadFile(ipfsAbsFilePath , uploadFileAbsPath);
                break;
        }
    }

    public void downloadFile(String ipfsAbsPath , String internalFileAbsPath){
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                break;
            case IPFS_TYPE:
                ((IPFSDataCenter)getDataCenter()).downloadFile(ipfsAbsPath , internalFileAbsPath);
                break;
        }
    }

    public void excuteFile(FileItem item , String saveFileAbsPath){
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                break;
            case IPFS_TYPE:
                String fileName = item.getFileName();
                String appendAbsPath = FileUtils.appendParentPath(saveFileAbsPath,fileName);

                File file  = new File(appendAbsPath);
                if (file.exists()){
                    iView.showSameFileDialog();
                }else{
                    downloadFile(item.getFileAbsPath(),file.getAbsolutePath());
                }
                break;
        }
    }

    public void deleteFile(String absFilePath){
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                ((InternalStorageDataCenter)getDataCenter()).deleteFile(absFilePath);
                refreshData();
                break;
            case IPFS_TYPE:
                ((IPFSDataCenter)getDataCenter()).deleteFile(absFilePath);
                break;
        }
    }

    public void renameFile(String parentPath , String oldName , String newName , boolean isFolder){
        switch (currentClientType){
            case INTERNAL_STORAGE_TYPE:
                ((InternalStorageDataCenter)getDataCenter()).renameFile(parentPath,oldName,newName);
                refreshData();
                break;
            case IPFS_TYPE:
                ((IPFSDataCenter)getDataCenter()).renameFile(parentPath,oldName,newName,isFolder);
                break;
        }
    }

    public void returnParent(){
        String parentPath = FileUtils.getParent(getCurrentPath());
        setCurrentPath(parentPath);
        refreshData();
    }

    public void changeClientType(ClientType clientType){
        this.currentClientType = clientType ;

        if (lastClentType != currentClientType){
            refreshData();
            lastClentType = currentClientType;
        }
    }

    public ClientType getCurrentClientType(){
        return currentClientType ;
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_FAIL) {
                iView.hideProgressBar();
                iView.showConnectionWrong();
            }
        }
    };
    public interface IView{
        void refreshListView(ArrayList<FileItem> items);
        void refreshTitleView(String path);

        void refreshListViewFinish();

        void showProgressBar();

        void hideProgressBar();

        void showSameFileDialog();

        void showConnectionWrong();
    }
}
