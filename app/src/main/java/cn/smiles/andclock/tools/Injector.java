package cn.smiles.andclock.tools;

import java.io.DataOutputStream;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import cn.smiles.andclock.SmilesApplication;

/**
 * @author Juergen Punz
 *         Injects InputEvents to Android-Device
 *         Needs root-access to Device and App needs superuser-rights (you will be asked for that at execution-time).
 */
public class Injector {

    /**
     * Injects Swipe-Event from right to left
     *
     * @return If execution of shell-command was successful or not
     */
    public static boolean swipeRightLeft() {
        return executeCommand("input swipe 300 500 50 500 100");
    }

    /**
     * Injects Swipe-Event from left to right
     *
     * @return If execution of shell-command was successful or not
     */
    public static boolean swipeLeftRight() {
        return executeCommand("input swipe 50 500 300 500 100");
    }

    /**
     * Injects Touch-Event at x- and y-coordinates of screen
     *
     * @param x x-coordinate of screen
     * @param y y-coordinate of screen
     * @return If execution of shell-command was successful or not
     */
    public static boolean touch(int x, int y) {
        return executeCommand(String.format(Locale.getDefault(), "input tap %d %d", x, y));
    }

    /**
     * Injects Unlock-Event for unlocking the device's screen
     *
     * @return If execution of shell-command was successful or not
     */
    public static boolean unlockDevice() {
        return executeCommand("input keyevent 82");
    }

    /**
     * Injects Powerbutton-Event for locking or activate the device's screen
     *
     * @return If execution of shell-command was successful or not
     */
    public static boolean pressPowerButton() {
        return executeCommand("input keyevent 26");
    }

    /**
     * Injects Homebutton-Event
     *
     * @return If execution of shell-command was successful or not
     */
    public static boolean pressHomeButton() {
        return executeCommand("input keyevent 3");
    }

    /**
     * Injects Backbutton-Event
     *
     * @return If execution of shell-command was successful or not
     */
    public static boolean pressBackButton() {
        return executeCommand("input keyevent 4");
    }

    /**
     * Injects Swipe-Event (up to down) for opening the Notificationcenter
     *
     * @return If execution of shell-command was successful or not
     */
    public static boolean showNotificationCenter() {
        return executeCommand("input swipe 10 10 10 1000");
    }

    /**
     * 屏幕截图
     *
     * @return
     */
    public static boolean screenCapture() {
        return executeCommand("input keyevent 120");
    }

    /**
     * Runs given command in shell as superuser
     *
     * @param command Command to execute
     * @return If execution of shell-command was successful or not
     */
    private static boolean executeCommand(final String command) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Process suShell = Runtime.getRuntime().exec("su");
                    DataOutputStream commandLine = new DataOutputStream(suShell.getOutputStream());
                    commandLine.writeBytes(command + '\n');
                    commandLine.flush();
                    commandLine.writeBytes("exit\n");
                    commandLine.flush();
                    System.out.println("执行结果，" + (suShell.waitFor() == 0));
                    suShell.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                    String message = e.getMessage();
                    System.out.println(message);
                    SmilesApplication.showToast("此功能手机必须root");
                }
            }
        });
        return true;
    }

    private static ExecutorService executorService = Executors.newCachedThreadPool();

}