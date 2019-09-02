package org.elastos.app.hivedemo.action;

import android.os.AsyncTask;

import org.elastos.hive.Length;
import org.elastos.app.hivedemo.vendor.ipfs.IPFSDataCenter;
import org.elastos.app.hivedemo.base.ActionCallback;
import org.elastos.app.hivedemo.base.BaseDataCenter;

import java.util.concurrent.ExecutionException;

public class DownloadFileAction extends AsyncTask <Void,String, Length>{

    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    private String internalFileAbsPath , ipfsAbsPath;
    public DownloadFileAction(BaseDataCenter dataCenter , ActionCallback actionCallback ,
                              String internalFileAbsPath , String ipfsAbsPath){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
        this.internalFileAbsPath = internalFileAbsPath ;
        this.ipfsAbsPath = ipfsAbsPath ;
    }


    @Override
    protected Length doInBackground(Void... voids) {
        Length length = null ;
        if (dataCenter instanceof IPFSDataCenter){
            try {
                ((IPFSDataCenter) dataCenter).doDownloadFile(ipfsAbsPath,internalFileAbsPath);
            } catch (ExecutionException e) {
                actionCallback.onFail(ActionType.ACTION_DOWNLOAD_FILE , e);
                e.printStackTrace();
            } catch (InterruptedException e) {
                actionCallback.onFail(ActionType.ACTION_DOWNLOAD_FILE , e);
                e.printStackTrace();
            } catch (Exception e){
                actionCallback.onFail(ActionType.ACTION_DOWNLOAD_FILE , e);
            }
        }
        return length ;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_DOWNLOAD_FILE);
    }

    @Override
    protected void onPostExecute(Length length) {
        super.onPostExecute(length);
        actionCallback.onSuccess(ActionType.ACTION_DOWNLOAD_FILE,length);
    }
}
