package io.innocentdream.launcher.launch;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class LogWindow extends JFrame implements Runnable {

    public static volatile BufferedReader out = null;
    public static volatile BufferedReader err = null;
    public static volatile boolean shouldRun = true;
    public static volatile Thread thread;

    public final JTextArea log;
    public JScrollPane logScrollPane;

    public LogWindow(String version) {
        super("Log - " + version);
        this.log = new JTextArea();
        this.log.setForeground(Color.LIGHT_GRAY);
        this.log.setBackground(Color.DARK_GRAY);
        this.log.setLineWrap(true);
        this.log.setWrapStyleWord(true);
        this.log.setCaretColor(Color.WHITE);
        this.log.setEditable(false);
        this.logScrollPane = new JScrollPane(log);
        this.logScrollPane.setWheelScrollingEnabled(true);
        this.logScrollPane.setPreferredSize(new Dimension(800, 500));
        this.getContentPane().add(logScrollPane, BorderLayout.CENTER);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        thread = new Thread(this);
        thread.start();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                shouldRun = false;
            }
        });
    }

    @Override
    public void run() {
        while (shouldRun) {
            if (out != null) {
                String logOut;
                try {
                    logOut = out.readLine();
                } catch (IOException e) {
                    continue;
                }
                if (logOut != null) {
                    try {
                        JsonObject data = Streams.parse(new JsonReader(new StringReader(logOut))).getAsJsonObject();
                        addRow(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(logOut);
                    }
                }
            }
            if (err != null) {
                String errOut;
                try {
                    errOut = err.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }
                if (errOut != null) {
                    try {
                        JsonObject data = Streams.parse(new JsonReader(new StringReader(errOut))).getAsJsonObject();
                        addRow(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println(errOut);
                    }
                }
            }
        }
        System.out.println("Thread Stopped");
    }

    public void addRow(JsonObject data) {
        if (data.get("basic").getAsBoolean()) {
            log.append(data.get("message").getAsString() + "\n");
            System.out.println(data.get("message").getAsString());
            return;
        }
        String time = data.get("time").getAsString();
        String thread = data.get("thread").getAsString();
        String level = data.get("level").getAsString();
        String logger = data.get("logger").getAsString();
        String message = data.get("message").getAsString();
        String logged = String.format("[%s] [%s/%s] (%s) %s", time, thread, level, logger, message);
        log.append(logged + "\n");
        System.out.println(logged);
    }

}
