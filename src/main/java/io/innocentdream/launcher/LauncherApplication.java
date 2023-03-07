package io.innocentdream.launcher;

import io.innocentdream.launcher.profile.ProfileManager;
import io.innocentdream.launcher.version.VersionManager;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

public class LauncherApplication {

    public static final String VERSION = "v1.0";
    public static boolean CONNECTED = testConnection();
    public static final String ID_NEWS_URL = "https://jades-innocent-dream.tumblr.com/";
    public static final String JAVA_HOME = System.getProperty("java.home");

    public static boolean testConnection() {
        try {
            URL url = new URL("https://www.example.com/");
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void main(String[] args) {
        String javaVersion = System.getProperty("java.runtime.version");
        String javaVendor = System.getProperty("java.vendor");
        System.out.printf("Java Version: %s, %s, %s%n", javaVendor, System.getProperty("java.runtime.name"), javaVersion);
        System.out.printf("System: %s, %s, %s\n", OS.OS, OS.VER, OS.ARCH);
        long timeNano = System.nanoTime();
        try {
            if (OS.isWindows()) UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            else if (OS.isLinux()) UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        VersionManager.loadVersions();
        ProfileManager.initProfiles();
        new LauncherWindow();
        LauncherWindow.mainWindow.loadNews(null);
        LauncherWindow.mainWindow.setVisible(true);
    }

}