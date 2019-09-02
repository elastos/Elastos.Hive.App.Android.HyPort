package org.elastos.app.hivedemo.action;

import android.os.AsyncTask;

import org.elastos.hive.Drive;
import org.elastos.app.hivedemo.vendor.ipfs.IPFSDataCenter;
import org.elastos.app.hivedemo.base.ActionCallback;
import org.elastos.app.hivedemo.base.BaseDataCenter;

public class InitAction extends AsyncTask<Void,String, Drive> {
    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    public InitAction(BaseDataCenter dataCenter , ActionCallback actionCallback){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_INIT);
    }

    @Override
    protected Drive doInBackground(Void... voids) {
        Drive drive = null ;
        if (dataCenter instanceof IPFSDataCenter){
            try{
                ((IPFSDataCenter) dataCenter).doInit();
            }catch (Exception e){
                actionCallback.onFail(ActionType.ACTION_INIT,e);
            }

        }
        return drive;
    }

    @Override
    protected void onPostExecute(Drive drive) {
        super.onPostExecute(drive);
        actionCallback.onSuccess(ActionType.ACTION_INIT,drive);
    }
}
