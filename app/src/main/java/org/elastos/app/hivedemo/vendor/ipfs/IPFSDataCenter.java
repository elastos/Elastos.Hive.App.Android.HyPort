package org.elastos.app.hivedemo.vendor.ipfs;

import android.content.Context;

import org.elastos.hive.Children;
import org.elastos.hive.Client;
import org.elastos.hive.Directory;
import org.elastos.hive.Drive;
import org.elastos.hive.File;
import org.elastos.hive.HiveException;
import org.elastos.hive.IPFSEntry;
import org.elastos.hive.Length;
import org.elastos.app.hivedemo.FileItem;
import org.elastos.app.hivedemo.action.CreateDirectoryAction;
import org.elastos.app.hivedemo.action.CreateFileAction;
import org.elastos.app.hivedemo.action.DeleteFileAction;
import org.elastos.app.hivedemo.action.DownloadFileAction;
import org.elastos.app.hivedemo.action.GetChildrenAndInfoAction;
import org.elastos.app.hivedemo.action.GetInfoAction;
import org.elastos.app.hivedemo.action.InitAction;
import org.elastos.app.hivedemo.action.UploadFileAction;
import org.elastos.app.hivedemo.base.ActionCallback;
import org.elastos.app.hivedemo.utils.FileUtils;
import org.elastos.hive.vendors.ipfs.IPFSParameter;

import org.elastos.app.hivedemo.base.BaseDataCenter;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class IPFSDataCenter extends BaseDataCenter {
    private String currentPath = "/";
    private static String catchPath ;
    private static final String[] rpcAddrs = {
            "52.83.119.110",
            "52.83.159.189",
            "3.16.202.140",
            "18.217.147.205",
            "18.219.53.133"
    };

    private static Client client ;
    private static Drive drive;

    private ActionCallback actionCallback ;

    private Context context ;
    public IPFSDataCenter(Context context , ActionCallback actionCallback){
        this.context = context ;
        this.actionCallback = actionCallback ;
        init();
    }

//    private Directory directory ;

    public void init(){
        new InitAction(this,actionCallback).execute();
    }

    public void doInit(){
        catchPath = context.getExternalCacheDir().getAbsolutePath();
        getClient();
        getDefaultDrive();
    }

    public static Client getClient(){
        if (client == null){
            String uid = null;
            IPFSParameter parameter = new IPFSParameter(new IPFSEntry(uid, rpcAddrs), catchPath);
            try {
                client = Client.createInstance(parameter);
                client.login(null);
            } catch (HiveException e) {
                e.printStackTrace();
            }
        }
        return client;
    }

    private Drive getDefaultDrive(){
        if (drive == null){
            try {
                Client client = getClient();
                drive = getClient().getDefaultDrive().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return drive;
    }

    public Children doGetChildren(String path){
        Children children = null;
        try {
            Directory directory = getDefaultDrive().getDirectory(path).get();
            children = directory.getChildren().get();
        } catch (ExecutionException e) {

            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return children ;
    }

    @Override
    public ArrayList getChildrenDetails(String path) {
        ArrayList<FileItem> arrayList = new ArrayList<>();
//        new GetChildrenAction(this , actionCallback).execute(null,null,null);
        new GetChildrenAndInfoAction(this,actionCallback,path).execute();
        return arrayList;
    }

    public void getChildrenAndInfo(){


    }

    public void dogetChildrenAndInfo(ArrayList<FileItem> fileItems){

    }


    public void docreateDirectory(String path){
        try {
            drive.createDirectory(path).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createDirectory(String fileAbsPath){
        new CreateDirectoryAction(this,actionCallback , fileAbsPath).execute();
    }

    public void getFileInfo(String path){
        new GetInfoAction(this,actionCallback,path).execute();
    }

    public File.Info doGetFileInfo(File file){
        File.Info fileInfo = null ;
        try {
            fileInfo = file.getInfo().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return fileInfo ;
    }

    public void getFile(String path){

    }

    public File doGetFile(String path) throws ExecutionException, InterruptedException {
        File file = drive.getFile(path).get();
        return file ;
    }


    public void createFile(String fileAbsPath){
        new CreateFileAction(this, actionCallback , fileAbsPath).execute();
    }

    public File doCreateFile(String fileAbsPath){
        File file = null ;
        try {
            file = drive.createFile(fileAbsPath).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return file ;
    }

    public void uploadFile(String ipfsAbsPath , String internalFileAbsPath){
        new UploadFileAction(this,actionCallback,internalFileAbsPath,ipfsAbsPath).execute();
    }

    public void doUploadFile(String ipfsAbsPath , String internalFileAbsPath) throws ExecutionException, InterruptedException {
        File file = drive.createFile(ipfsAbsPath).get();

        ByteBuffer writeBuffer = FileUtils.file2ByteBuffer(internalFileAbsPath);

        file.write(writeBuffer).get();
        file.commit().get();
    }

    public void downloadFile(String ipfsAbsPath , String internalFileAbsPath){
        new DownloadFileAction(this,actionCallback,internalFileAbsPath,ipfsAbsPath).execute();
    }

    public void doDownloadFile(String ipfsAbsPath , String internalFileAbsPath) throws ExecutionException, InterruptedException {
        File file = doGetFile(ipfsAbsPath);
        File.Info fileInfo = doGetFileInfo(file);
        int size = Integer.valueOf(fileInfo.get(File.Info.size));

        ByteBuffer readBuf = ByteBuffer.allocate(size);
        Length lenObj = new Length(0);

        long readLen = 0;
        while (lenObj.getLength() != -1) {
            ByteBuffer tmpBuf = ByteBuffer.allocate(100);
            lenObj = file.read(tmpBuf).get();

            int len = (int) lenObj.getLength();
            if (len != -1) {
                readLen += len;

                byte[] bytes = new byte[len];
                tmpBuf.flip();
                tmpBuf.get(bytes, 0, len);
                readBuf.put(bytes);

                //write the content to a file.
                FileUtils.byteBuffer2File(internalFileAbsPath, tmpBuf , false);
            }
        }
    }

    public void deleteFile(String ipfsAbsPath){
        new DeleteFileAction(this,actionCallback,ipfsAbsPath).execute();
    }

    public void doDeleteFile(String ipfsAbsPath) throws ExecutionException, InterruptedException {
        File file = doGetFile(ipfsAbsPath);
        file.deleteItem().get();
    }

}
