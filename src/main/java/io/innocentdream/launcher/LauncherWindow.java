package io.innocentdream.launcher;

import io.innocentdream.launcher.launch.Launcher;
import io.innocentdream.launcher.launch.LogWindow;
import io.innocentdream.launcher.profile.EditProfile;
import io.innocentdream.launcher.profile.ProfileManager;
import io.innocentdream.launcher.profile.SelectProfile;
import org.jetbrains.annotations.Nullable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class LauncherWindow extends JFrame {

    public static LauncherWindow mainWindow;

    public BufferedImage icon;
    public SelectProfile selProfile;
    public EditProfile changeProfile;
    public LogWindow logWindow;

    public final JButton editProfile, play, changelog, selectProfile, settingsButton;
    public final JLabel currentVersion, currentProfile;
    public final BottomPanel bottomPanel;

    public LauncherWindow() {
        super("Innocent Dream Launcher - " + LauncherApplication.VERSION);
        mainWindow = this;

        try {
            InputStream stream = Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/icon-512.png"));
            icon = ImageIO.read(stream);
            this.setIconImage(icon);
        } catch (IOException | NullPointerException e) {
            System.err.println("Error occurred in loading window icon");
            e.printStackTrace();
        }

        this.setMinimumSize(new Dimension(800, 450));
        this.setPreferredSize(new Dimension(800, 450));
        this.setLayout(new BorderLayout());
        this.setLocationRelativeTo(null);
        this.editProfile = new JButton("Edit Profile");
        this.editProfile.addActionListener(this::editProfile);
        this.play = new JButton("Play");
        this.play.addActionListener(this::launch);
        this.changelog = new JButton("Changelog");
        this.changelog.addActionListener(this::loadNews);
        this.selectProfile = new JButton("Select Profile");
        this.selectProfile.addActionListener(this::selectProfile);
        this.currentProfile = new JLabel(ProfileManager.CURRENT_PROFILE.name() + " [" + ProfileManager.CURRENT_PROFILE.version() + "]");
        this.currentProfile.setForeground(Color.WHITE);
        this.currentProfile.setHorizontalAlignment(SwingConstants.CENTER);
        this.currentVersion = new JLabel("Version: " + ProfileManager.CURRENT_PROFILE.version());
        this.currentVersion.setForeground(Color.WHITE);
        this.currentVersion.setHorizontalAlignment(SwingConstants.CENTER);
        this.settingsButton = new JButton("Settings");
        this.settingsButton.addActionListener(this::settings);

        this.bottomPanel = new BottomPanel();
        this.add(this.bottomPanel, BorderLayout.SOUTH);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                ProfileManager.saveProfiles();
                System.exit(0);
            }
        });
    }

    @Override
    public void update(Graphics g) {
    }

    public void updateText() {
        this.currentProfile.setText(ProfileManager.CURRENT_PROFILE.name() + " [" + ProfileManager.CURRENT_PROFILE.version() + "]");
    }

    public void settings(ActionEvent e) {

    }

    public void selectProfile(ActionEvent e) {
        if (this.selProfile == null) this.selProfile = new SelectProfile();
        else this.selProfile.setVisible(true);
    }

    public void launch(ActionEvent e) {
        Launcher launcher = new Launcher(ProfileManager.CURRENT_PROFILE);
        launcher.initRunDir();
        Thread thread = new Thread(launcher::launch);
        thread.setName("LaunchThread");
        thread.start();
    }

    public void editProfile(ActionEvent e) {
        if (this.changeProfile != null) {
            this.changeProfile.dispose();
        }
        this.changeProfile = new EditProfile(ProfileManager.CURRENT_PROFILE);
    }

    public void loadNews(@Nullable ActionEvent e) {
        JScrollPane pane = new InternetPane().load(LauncherApplication.ID_NEWS_URL);
        this.add(pane, BorderLayout.CENTER);
    }
}
