package org.elastos.app.hivedemo.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Dimorinny on 24.10.15.
 */
public class FileUtils {
    public static List<File> getFileListByDirPath(String path, FileFilter filter) {
        File directory = new File(path);
        File[] files = directory.listFiles(filter);
        List<File> result = new ArrayList<>();
        if (files == null) {
            return new ArrayList<>();
        }

        for (int i = 0; i < files.length; i++) {
            result.add(files[i]);
        }
        Collections.sort(result, new FileComparator());
        return result;
    }

    public static String cutLastSegmentOfPath(String path) {
        return path.substring(0, path.lastIndexOf("/"));
    }

    public static String getReadableFileSize(long size) {
        if (size <= 0) return "0";
        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    /**
     * 获取文件长度
     *
     * @param file 文件
     * @return 文件长度
     */
    public static long getFileLength(final File file) {
        if (!isFile(file)) return -1;
        return file.length();
    }

    /**
     * 判断是否是文件
     *
     * @param file 文件
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public static boolean isFile(final File file) {
        return file != null && file.exists() && file.isFile();
    }

    /**
     * 根据地址获取当前地址下的所有目录和文件，并且排序,同时过滤掉不符合大小要求的文件
     *
     * @param path
     * @return List<File>
     */
    public static List<File> getFileList(String path, FileFilter filter, boolean isGreater, long targetSize) {
        List<File> list = FileUtils.getFileListByDirPath(path, filter);
        //进行过滤文件大小
        Iterator iterator = list.iterator();
        while (iterator.hasNext()) {
            File f = (File) iterator.next();
            if (f.isFile()) {
                //获取当前文件大小
                long size = FileUtils.getFileLength(f);
                if (isGreater) {
                    //当前想要留下大于指定大小的文件，所以过滤掉小于指定大小的文件
                    if (size < targetSize) {
                        iterator.remove();
                    }
                } else {
                    //当前想要留下小于指定大小的文件，所以过滤掉大于指定大小的文件
                    if (size > targetSize) {
                        iterator.remove();
                    }
                }
            }
        }
        return list;
    }

    public static String getParent(String path) {
        int prefixLength = path.length();
        int index = path.lastIndexOf("/");
        if (index > 0 && index < prefixLength) {
            return path.substring(0, index);
        }
        return "/";
    }

    public static ByteBuffer file2ByteBuffer(String path) {
        FileInputStream fileInputStream = null;
        FileChannel inChannel = null;
        try {
            fileInputStream = new FileInputStream(path);
            inChannel = fileInputStream.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(fileInputStream.available());
            inChannel.read(buffer);
            buffer.flip();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inChannel != null) {
                    inChannel.close();
                }

                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public static void byteBuffer2File(String path, ByteBuffer buffer , boolean isAppend) {
        FileChannel outputChannel = null;
        FileOutputStream outputStream = null;
        try {
            buffer.flip();
            java.io.File file = new java.io.File(path);
            if (!file.exists()) {
                file.createNewFile();
            }

            outputStream = new FileOutputStream(path, isAppend);
            outputChannel = outputStream.getChannel();
            outputChannel.write(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                outputChannel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String appendParentPath(String parentPath , String fileName){
        String fileAbsPath = "";
        if (parentPath.equals("/")){
            fileAbsPath = parentPath+fileName ;
        }else{
            fileAbsPath = parentPath+"/"+fileName ;
        }
        return fileAbsPath ;
    }

    public static boolean deleteFileAndDir(String filePath){
        File file = new File(filePath);
        if (!file.exists()){
            return false ;
        }

        if (file.isDirectory()){
            return deleteDirectory(filePath);
        }

        return deleteFile(filePath);
    }
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean deleteDirectory(String dir) {
        if (!dir.endsWith(File.separator)){
            dir = dir + File.separator;
        }

        File dirFile = new File(dir);

        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return false;
        }
        boolean flag = true;
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                flag = FileUtils.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            else if (files[i].isDirectory()) {
                flag = FileUtils.deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            return false;
        }

        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean renameFile(String path, String oldFileName, String newFileName) {
        if (!oldFileName.equals(newFileName)) {
            File oldfile = new File(path + File.separator + oldFileName);
            File newfile = new File(path + File.separator + newFileName);
            if (newfile.exists()) {
                return false;
            } else {
                oldfile.renameTo(newfile);
                return true;
            }
        }
        return false;
    }

    public static boolean moveFile(String filename, String oldpath, String newpath, boolean cover) {
        if (!oldpath.equals(newpath)) {
            File oldfile = new File(oldpath + "/" + filename);
            File newfile = new File(newpath + "/" + filename);
            if (newfile.exists()) {
                if (cover) {
                    oldfile.renameTo(newfile);
                    return true ;
                } else {
                    return false;
                }
            } else {
                oldfile.renameTo(newfile);
                return true ;
            }
        }
        return false ;
    }

    public static void copyFile(String src, String dest) throws IOException {
        FileInputStream in = new FileInputStream(src);
        File file = new File(dest);
        if (!file.exists()){
            file.createNewFile();
        }else{}
        FileOutputStream out = new FileOutputStream(file);
        int c;
        byte buffer[] = new byte[1024];
        while ((c = in.read(buffer)) != -1) {
            for (int i = 0; i < c; i++){
                out.write(buffer[i]);
            }
        }
        in.close();
        out.close();
    }
}
