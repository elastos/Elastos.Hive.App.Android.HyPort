package org.elastos.app.hivedemo.action;

import android.os.AsyncTask;

import org.elastos.app.hivedemo.base.ActionCallback;
import org.elastos.app.hivedemo.base.BaseDataCenter;
import org.elastos.app.hivedemo.vendor.ipfs.IPFSDataCenter;

import java.util.concurrent.ExecutionException;

public class CopyFileAction extends AsyncTask <Void,String,Void>{

    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    private String oldAbsPath , destParentPath;
    private boolean isFolder ;
    public CopyFileAction(BaseDataCenter dataCenter , ActionCallback actionCallback ,
                          String oldAbsPath , String newAbsPath , boolean isFolder){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
        this.oldAbsPath = oldAbsPath ;
        this.destParentPath = newAbsPath ;
        this.isFolder = isFolder ;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        Void voidFeild = null;

        if (dataCenter instanceof IPFSDataCenter){
            try {
                ((IPFSDataCenter) dataCenter).doCopyFile(oldAbsPath, destParentPath,isFolder);
            } catch (ExecutionException e) {
                actionCallback.onFail(ActionType.ACTION_COPY_FILE,e);
                e.printStackTrace();
            } catch (InterruptedException e) {
                actionCallback.onFail(ActionType.ACTION_COPY_FILE,e);
                e.printStackTrace();
            }
        }
        return voidFeild;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_COPY_FILE);
    }

    @Override
    protected void onPostExecute(Void voidFeild) {
        super.onPostExecute(voidFeild);
        actionCallback.onSuccess(ActionType.ACTION_COPY_FILE,voidFeild);
    }
}
