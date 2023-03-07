package io.innocentdream.launcher;

import javax.swing.*;
import java.io.File;

public class OS {
    public static String OS = System.getProperty("os.name").toLowerCase();
    public static String ARCH = System.getProperty("os.arch");
    public static String VER = System.getProperty("os.version").toLowerCase();

    public static boolean isWindows() {
        return OS.contains("win");
    }

    public static boolean isMac() {
        return OS.contains("mac");
    }

    public static boolean isLinux() {
        return OS.contains("nix") || OS.contains("nux") || OS.contains("aix");
    }

    public static String getPath() {
        return isWindows() ? windowsPath() : path();
    }

    public static String windowsPath() {
        File folder = new File(System.getenv("APPDATA") + "\\.innocentdream\\");
        folder.mkdirs();
        return folder.getAbsolutePath();
    }

    public static String path() {
        File folder;
        if (isLinux()) {
            folder = new File(System.getProperty("user.home") + "/.innocentdream/");
        } else if (isMac()) {
            folder = new File(System.getProperty("user.home") + "/Library/Application Support/.innocentdream/");
        } else {
            JOptionPane.showMessageDialog(LauncherWindow.mainWindow, "Unsupported Operating System", "Error", JOptionPane.WARNING_MESSAGE);
            System.exit(0);
            return null;
        }
        folder.mkdirs();
        return folder.getAbsolutePath();
    }

}
