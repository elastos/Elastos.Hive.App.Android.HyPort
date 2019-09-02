package org.elastos.app.hivedemo.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
}
