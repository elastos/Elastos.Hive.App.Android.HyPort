package org.elastos.app.hivedemo.action;

import android.os.AsyncTask;

import org.elastos.hive.File;
import org.elastos.app.hivedemo.vendor.ipfs.IPFSDataCenter;
import org.elastos.app.hivedemo.base.ActionCallback;
import org.elastos.app.hivedemo.base.BaseDataCenter;

import java.util.concurrent.ExecutionException;

public class GetInfoAction extends AsyncTask<Void,String, File.Info> {
    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    private String filePath ;
    public GetInfoAction(BaseDataCenter dataCenter , ActionCallback actionCallback , String path){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
        this.filePath = path ;
    }


    @Override
    protected File.Info doInBackground(Void... voids) {
        File.Info fileInfo = null;


        if (dataCenter instanceof IPFSDataCenter){
            try{
                File file = ((IPFSDataCenter) dataCenter).doGetFile(filePath);
                fileInfo = ((IPFSDataCenter) dataCenter).doGetFileInfo(file);
            } catch (ExecutionException e) {
                actionCallback.onFail(ActionType.ACTION_FILE_INFO,e);
                e.printStackTrace();
            } catch (InterruptedException e) {
                actionCallback.onFail(ActionType.ACTION_FILE_INFO,e);
                e.printStackTrace();
            } catch (Exception e){
                actionCallback.onFail(ActionType.ACTION_FILE_INFO,e);
            }
        }
        return fileInfo;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_FILE_INFO);
    }

    @Override
    protected void onPostExecute(File.Info info) {
        super.onPostExecute(info);
        actionCallback.onSuccess(ActionType.ACTION_FILE_INFO,info);
    }
}
