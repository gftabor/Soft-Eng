package main.java;

/**
 * Created by griffincecil on 4/1/2017.
 */
public class MenuInterface {
    private boolean isShowing;
    private LoginButton login;
    private DirectoryButton directoryBtn;
    private DirectoryManagementTool directoryTool;

    public boolean getIsShowing() {
        return isShowing;
    }

    public void setIsShowing(boolean state) {
        this.isShowing = state;
    }
}
