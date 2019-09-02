package org.elastos.app.hivedemo.action;

import android.os.AsyncTask;

import org.elastos.hive.File;
import org.elastos.app.hivedemo.vendor.ipfs.IPFSDataCenter;
import org.elastos.app.hivedemo.base.ActionCallback;
import org.elastos.app.hivedemo.base.BaseDataCenter;

public class CreateFileAction extends AsyncTask <Void,String,File>{

    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    private String fileAbsPath ;
    public CreateFileAction(BaseDataCenter dataCenter , ActionCallback actionCallback , String fileAbsPath){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
        this.fileAbsPath = fileAbsPath ;
    }


    @Override
    protected File doInBackground(Void... voids) {
        File file = null;

        if (dataCenter instanceof IPFSDataCenter){
            try{
                file = ((IPFSDataCenter) dataCenter).doCreateFile(fileAbsPath);
            }catch (Exception e){
                actionCallback.onFail(ActionType.ACTION_CREATE_File , e);
            }

        }
        return file;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_CREATE_File);
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        actionCallback.onSuccess(ActionType.ACTION_CREATE_File,file);
    }
}
