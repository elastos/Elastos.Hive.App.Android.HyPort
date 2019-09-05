package org.elastos.app.hivedemo.action;

public class PasteBean {
    public enum PasteActionType {
        ACTION_CUT,
        ACTION_COPY
    }

    private String realAbsPath;
    private String destAbsPath;
    private String destParentPath;
    private String fileName ;
    private boolean isFolder ;
    private PasteActionType actionType ;

    public PasteBean(){
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getRealAbsPath() {
        return realAbsPath;
    }

    public void setRealAbsPath(String realAbsPath) {
        this.realAbsPath = realAbsPath;
    }

    public String getDestAbsPath() {
        return destAbsPath;
    }

    public void setDestAbsPath(String destAbsPath) {
        this.destAbsPath = destAbsPath;
    }

    public PasteActionType getActionType() {
        return actionType;
    }

    public void setActionType(PasteActionType actionType) {
        this.actionType = actionType;
    }

    public boolean isFolder() {
        return isFolder;
    }

    public void setFolder(boolean folder) {
        isFolder = folder;
    }

    public String getDestParentPath() {
        return destParentPath;
    }

    public void setDestParentPath(String destParentPath) {
        this.destParentPath = destParentPath;
    }
}
