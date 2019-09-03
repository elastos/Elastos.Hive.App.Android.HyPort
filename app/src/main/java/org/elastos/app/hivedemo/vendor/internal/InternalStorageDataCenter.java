package org.elastos.app.hivedemo.vendor.internal;

import org.elastos.app.hivedemo.FileItem;
import org.elastos.app.hivedemo.action.DeleteFileAction;
import org.elastos.app.hivedemo.base.BaseDataCenter;
import org.elastos.app.hivedemo.utils.FileUtils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class InternalStorageDataCenter extends BaseDataCenter {
    @Override
    public ArrayList getChildrenDetails(String path) {
        ArrayList<FileItem> arrayList = new ArrayList<>();
//        FileFilter fileFilter = new HiveFileFilter(new String[]{});
        FileFilter fileFilter = null;
        boolean isGreater = true ;
        long targetSize = 0 ;

        List<File> list = FileUtils.getFileList(path,fileFilter,isGreater,targetSize);
        for (int i=0 ;i<list.size();i++){
            File file = list.get(i);
            FileItem fileItem = new FileItem(file.getName()) ;

            fileItem.setFileAbsPath(file.getAbsolutePath());
            if (FileUtils.isFile(file)){
                fileItem.setFolder(false);
                fileItem.setFileDetail("大小："+FileUtils.getReadableFileSize(FileUtils.getFileLength(file)));
            }else{
                fileItem.setFolder(true);
                fileItem.setFileDetail("文件："+FileUtils.getFileList(file.getAbsolutePath(),fileFilter,isGreater,targetSize).size());
            }

            if (file.isFile()){
                fileItem.setFolder(false);
            }else{
                fileItem.setFolder(true);
            }
            arrayList.add(fileItem);
        }
        return arrayList ;
    }

    public void deleteFile(String filePath){
        FileUtils.deleteFileAndDir(filePath);
    }

}
