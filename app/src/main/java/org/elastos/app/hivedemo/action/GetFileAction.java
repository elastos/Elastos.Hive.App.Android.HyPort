package org.elastos.app.hivedemo.action;

import android.os.AsyncTask;

import org.elastos.hive.File;
import org.elastos.app.hivedemo.vendor.ipfs.IPFSDataCenter;
import org.elastos.app.hivedemo.base.ActionCallback;
import org.elastos.app.hivedemo.base.BaseDataCenter;

import java.util.concurrent.ExecutionException;

public class GetFileAction extends AsyncTask<Void,String, File> {
    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    private String path ;
    public GetFileAction(BaseDataCenter dataCenter , ActionCallback actionCallback , String path){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
        this.path = path ;
    }


    @Override
    protected File doInBackground(Void... voids) {
        File file = null;

        if (dataCenter instanceof IPFSDataCenter){
            try {
                ((IPFSDataCenter) dataCenter).doGetFile(path);
            } catch (ExecutionException e) {
                actionCallback.onFail(ActionType.ACTION_GET_FILE,e);
                e.printStackTrace();
            } catch (InterruptedException e) {
                actionCallback.onFail(ActionType.ACTION_GET_FILE,e);
                e.printStackTrace();
            } catch (Exception e){
                actionCallback.onFail(ActionType.ACTION_GET_FILE,e);
            }
        }
        return file;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_GET_FILE);
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        actionCallback.onSuccess(ActionType.ACTION_GET_FILE,file);
    }
}
