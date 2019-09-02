package org.elastos.app.hivedemo.action;

import android.os.AsyncTask;

import org.elastos.hive.Children;
import org.elastos.hive.File;
import org.elastos.hive.ItemInfo;
import org.elastos.app.hivedemo.FileItem;
import org.elastos.app.hivedemo.vendor.ipfs.IPFSDataCenter;
import org.elastos.app.hivedemo.base.ActionCallback;
import org.elastos.app.hivedemo.base.BaseDataCenter;
import org.elastos.app.hivedemo.utils.FileUtils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class GetChildrenAndInfoAction extends AsyncTask<Void, String, ArrayList<FileItem>> {
    private ActionCallback actionCallback ;
    private BaseDataCenter dataCenter ;
    private String parentPath ;
    public GetChildrenAndInfoAction(BaseDataCenter dataCenter , ActionCallback actionCallback ,
                                    String parentPath){
        this.actionCallback = actionCallback ;
        this.dataCenter = dataCenter ;
        this.parentPath = parentPath ;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        actionCallback.onPreAction(ActionType.ACTION_GET_CHILDREN);
    }

    @Override
    protected ArrayList<FileItem> doInBackground(Void... voids) {
        ArrayList<FileItem> arrayList = new ArrayList<>();

        if (dataCenter instanceof IPFSDataCenter){
            try{
                //getChildren
                Children children = ((IPFSDataCenter) dataCenter).doGetChildren(parentPath);
                ArrayList<ItemInfo> itemInfos = children.getContent();

                for (ItemInfo itemInfo : itemInfos){
                    String fileName = itemInfo.get(ItemInfo.name);
                    //create FileItem
                    FileItem fileItem = new FileItem(fileName);
                    //create file abs path
                    String fileAbsPath = FileUtils.appendParentPath(parentPath,fileName);

                    //get file
                    File file = ((IPFSDataCenter) dataCenter).doGetFile(fileAbsPath);

                    //get file info
                    File.Info fileInfo= ((IPFSDataCenter) dataCenter).doGetFileInfo(file);
                    String type = fileInfo.get(File.Info.type);
                    String size = fileInfo.get(File.Info.size);
                    String count =fileInfo.get(File.Info.childCount);


                    if (type.equals("file")){
                        fileItem.setFolder(false);
                        fileItem.setFileDetail("大小："+size);
                    }else{
                        fileItem.setFolder(true);
                        fileItem.setFileDetail("文件："+count);
                    }
                    fileItem.setFileAbsPath(fileAbsPath);

                    arrayList.add(fileItem);
                }
            } catch (ExecutionException e) {
                actionCallback.onFail(ActionType.ACTION_GET_CHILDREN,e);
                e.printStackTrace();
            } catch (InterruptedException e) {
                actionCallback.onFail(ActionType.ACTION_GET_CHILDREN,e);
                e.printStackTrace();
            } catch (Exception e){
                actionCallback.onFail(ActionType.ACTION_GET_CHILDREN,e);
            }
        }
        return arrayList;
    }

    @Override
    protected void onPostExecute(ArrayList<FileItem> drive) {
        super.onPostExecute(drive);
        actionCallback.onSuccess(ActionType.ACTION_GET_CHILDREN,drive);
    }

}
