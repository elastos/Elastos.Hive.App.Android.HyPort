package org.elastos.app.hivedemo.action;

import android.os.AsyncTask;

import org.elastos.app.hivedemo.vendor.ipfs.IPFSDataCenter;
import org.elastos.app.hivedemo.base.ActionCallback;
import org.elastos.app.hivedemo.base.BaseDataCenter;

import java.util.concurrent.ExecutionException;

public class DeleteFileAction extends AsyncTask <Void,String,Void>{

    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    private String fileAbsPath ;
    public DeleteFileAction(BaseDataCenter dataCenter , ActionCallback actionCallback , String fileAbsPath){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
        this.fileAbsPath = fileAbsPath ;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        Void voidFeild = null;

        if (dataCenter instanceof IPFSDataCenter){
            try {
                ((IPFSDataCenter) dataCenter).doDeleteFile(fileAbsPath);
            } catch (ExecutionException e) {
                actionCallback.onFail(ActionType.ACTION_DELETE_FILE,e);
                e.printStackTrace();
            } catch (InterruptedException e) {
                actionCallback.onFail(ActionType.ACTION_DELETE_FILE,e);
                e.printStackTrace();
            } catch (Exception e){
                actionCallback.onFail(ActionType.ACTION_DELETE_FILE , e);
            }
        }
        return voidFeild;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_DELETE_FILE);
    }

    @Override
    protected void onPostExecute(Void voidFeild) {
        super.onPostExecute(voidFeild);
        actionCallback.onSuccess(ActionType.ACTION_DELETE_FILE,voidFeild);
    }
}
