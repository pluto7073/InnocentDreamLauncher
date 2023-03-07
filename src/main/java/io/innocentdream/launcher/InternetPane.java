package io.innocentdream.launcher;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class InternetPane extends JPanel {

    public static final HyperlinkListener EXTERNAL_HYPERLINK_LISTENER = hyperlinkEvent -> {
        if (hyperlinkEvent.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                LauncherWindow.mainWindow.add(new InternetPane().load(hyperlinkEvent.getURL().toString()), BorderLayout.CENTER);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    private JScrollPane scrollPane;

    public InternetPane() {}

    public JScrollPane load(String url) {
        final JEditorPane pane = new JEditorPane();
        pane.setContentType("text/html;charset=UTF-8");
        pane.setEditable(false);
        pane.setBackground(Color.BLACK);
        pane.addHyperlinkListener(EXTERNAL_HYPERLINK_LISTENER);
        if (!LauncherApplication.CONNECTED) {
            pane.setText("<html><body bgcolor=\"black\"><h1 style=\"color: red;\">No Connection</h1></body></html>");
        } else {
            pane.setText("<html><body bgcolor=\"black\"><h1> style=\"color: white;\">Loading..</h1></body></html>");
            new Thread(() -> {
                try {
                    HttpsURLConnection connection = (HttpsURLConnection) new URL(url).openConnection();
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    connection.setConnectTimeout(5000);
                    connection.connect();
                    Scanner scanner = new Scanner(connection.getInputStream(), StandardCharsets.UTF_8);
                    StringBuilder builder = new StringBuilder();
                    while (scanner.hasNextLine()) {
                        builder.append(scanner.nextLine());
                    }
                    pane.setText(builder.toString());
                    pane.setBackground(new Color(0, 0, 0, 225));
                    scanner.close();
                    connection.disconnect();
                } catch (IOException e) {
                    pane.setText("<html><body bgcolor=\"black\"><h1 style=\"color: red;\">No Connection</h1></body></html>");
                    System.err.println("Couldn't load news");
                    e.printStackTrace();
                }
            }).start();
        }
        this.scrollPane = new JScrollPane(pane);
        this.scrollPane.setBorder(null);
        this.scrollPane.setWheelScrollingEnabled(true);
        return this.scrollPane;
    }

}
