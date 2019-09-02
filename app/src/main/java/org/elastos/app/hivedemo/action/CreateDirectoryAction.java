package org.elastos.app.hivedemo.action;

import android.os.AsyncTask;

import org.elastos.hive.Directory;
import org.elastos.app.hivedemo.vendor.ipfs.IPFSDataCenter;
import org.elastos.app.hivedemo.base.ActionCallback;
import org.elastos.app.hivedemo.base.BaseDataCenter;

public class CreateDirectoryAction extends AsyncTask <Void,String,Directory>{

    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    private String fileAbsPath ;
    public CreateDirectoryAction(BaseDataCenter dataCenter , ActionCallback actionCallback ,String fileAbsPath){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
        this.fileAbsPath = fileAbsPath ;
    }


    @Override
    protected Directory doInBackground(Void... voids) {
        Directory directory = null;

        if (dataCenter instanceof IPFSDataCenter){
            try{
                ((IPFSDataCenter) dataCenter).docreateDirectory(fileAbsPath);
            }catch (Exception e){
                actionCallback.onFail(ActionType.ACTION_CREATE_DIR , e);
            }

        }
        return directory;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_CREATE_DIR);
    }

    @Override
    protected void onPostExecute(Directory directory) {
        super.onPostExecute(directory);
        actionCallback.onSuccess(ActionType.ACTION_CREATE_DIR,directory);
    }
}
