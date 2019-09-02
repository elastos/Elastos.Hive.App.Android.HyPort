package org.elastos.app.hivedemo.action;

import android.os.AsyncTask;

import org.elastos.app.hivedemo.vendor.ipfs.IPFSDataCenter;
import org.elastos.app.hivedemo.base.ActionCallback;
import org.elastos.app.hivedemo.base.BaseDataCenter;

import java.util.concurrent.ExecutionException;

public class UploadFileAction extends AsyncTask <Void,String,Void>{

    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    private String internalFileAbsPath , ipfsAbsPath;
    public UploadFileAction(BaseDataCenter dataCenter , ActionCallback actionCallback ,
                            String internalFileAbsPath , String ipfsAbsPath){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
        this.internalFileAbsPath = internalFileAbsPath ;
        this.ipfsAbsPath = ipfsAbsPath ;
    }


    @Override
    protected Void doInBackground(Void... voids) {
        Void voidField = null ;
        if (dataCenter instanceof IPFSDataCenter){
            try {
                ((IPFSDataCenter) dataCenter).doUploadFile(ipfsAbsPath,internalFileAbsPath);
            } catch (ExecutionException e) {
                actionCallback.onFail(ActionType.ACTION_UPLOAD_FILE , e);
                e.printStackTrace();
            } catch (InterruptedException e) {
                actionCallback.onFail(ActionType.ACTION_UPLOAD_FILE , e);
                e.printStackTrace();
            } catch (Exception e){
                actionCallback.onFail(ActionType.ACTION_UPLOAD_FILE , e);
            }
        }
        return voidField ;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_UPLOAD_FILE);
    }

    @Override
    protected void onPostExecute(Void voidField) {
        super.onPostExecute(voidField);
        actionCallback.onSuccess(ActionType.ACTION_UPLOAD_FILE,voidField);
    }
}
