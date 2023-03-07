package io.innocentdream.launcher.profile;

import io.innocentdream.launcher.LauncherWindow;
import io.innocentdream.launcher.version.Version;
import io.innocentdream.launcher.version.VersionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EditProfile extends JFrame {

    public JPanel panel;
    public JButton removeProfile, saveProfile;
    public JLabel nameLabel, jvmArgsLabel, runDirLabel, runtimeLabel, keepOpenLabel, versionLabel;
    public JTextField name, jvmArgs, runDir, runtime;
    public JCheckBox showConsole;
    public JComboBox<String> keepOpen, version;

    private final Profile profile;

    public EditProfile(Profile profile) {
        super("Edit Profile");
        this.profile = profile;

        panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 2, 10, 10);
        constraints.gridy = 0;
        constraints.gridx = 0;

        removeProfile = new JButton("Remove Profile");
        removeProfile.addActionListener(this::removeProfile);
        panel.add(removeProfile, constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.gridx = 0;
        constraints.gridy = 1;

        nameLabel = new JLabel("Name: ");
        panel.add(nameLabel, constraints);

        constraints.gridx = 1;
        constraints.gridwidth = 2;

        name = new JTextField(profile.name());
        panel.add(name, constraints);

        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(2, 10, 0, 10);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 3;

        jvmArgsLabel = new JLabel("JVM Args");
        panel.add(jvmArgsLabel, constraints);

        constraints.insets = new Insets(0, 10, 20, 10);
        constraints.gridy++;

        jvmArgs = new JTextField();
        String[] args = profile.jvmArgs().toArray(new String[0]);
        if (args.length > 0) {
            StringBuilder builder = new StringBuilder(args[0]);
            for (int i = 1; i < args.length; i++) {
                builder.append(" ").append(args[i]);
            }
            jvmArgs.setText(builder.toString());
        }

        panel.add(jvmArgs, constraints);

        constraints.insets = new Insets(2, 10, 0, 10);
        constraints.gridy++;

        runDirLabel = new JLabel("Working Directory (leave blank for default)");
        panel.add(runDirLabel, constraints);

        constraints.insets = new Insets(0, 10, 20, 10);
        constraints.gridy++;

        runDir = new JTextField(profile.customDir().isPresent() ? profile.customDir().get() : "");
        panel.add(runDir, constraints);

        constraints.insets = new Insets(2, 10, 0, 10);
        constraints.gridy++;

        runtimeLabel = new JLabel("Java Runtime (Leave blank for default)");
        panel.add(runtimeLabel, constraints);

        constraints.insets = new Insets(0, 10, 20, 10);
        constraints.gridy++;

        runtime = new JTextField(profile.customRuntime().isPresent() ? profile.customRuntime().get() : "");
        panel.add(runtime, constraints);

        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.gridy++;

        showConsole = new JCheckBox("Show Console Log?", profile.showLog());
        panel.add(showConsole, constraints);

        constraints.gridy++;
        constraints.insets = new Insets(2, 10, 0, 10);

        keepOpenLabel = new JLabel("Launcher Action on Run Game");
        panel.add(keepOpenLabel, constraints);

        constraints.insets = new Insets(0, 10, 20, 10);
        constraints.gridy++;

        keepOpen = new JComboBox<>();
        keepOpen.addItem("Close Launcher");
        keepOpen.addItem("Close Launcher, Reopen when game closes");
        keepOpen.addItem("Keep Launcher Open");
        keepOpen.setSelectedIndex(profile.keepOpen());
        panel.add(keepOpen, constraints);

        constraints.insets = new Insets(2, 10, 0, 10);
        constraints.gridy++;

        versionLabel = new JLabel("Version");
        panel.add(versionLabel, constraints);

        constraints.insets = new Insets(0, 10, 20, 10);
        constraints.gridy++;

        version = new JComboBox<>();
        int i = 0;
        for (Version v : VersionManager.getVersions()) {
            version.addItem(v.getName());
            if (v.getName().equals(profile.version())) {
                version.setSelectedIndex(i);
            }
            i++;
        }
        panel.add(version, constraints);

        constraints.gridy++;

        saveProfile = new JButton("Save Profile");
        saveProfile.addActionListener(this::saveProfile);
        panel.add(saveProfile, constraints);

        panel.setPreferredSize(new Dimension(500, 400));

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(LauncherWindow.mainWindow);
        this.setVisible(true);
    }

    public void saveProfile(ActionEvent e) {
        close();
        Profile newProfile = this.profile.copy();
        newProfile.setName(this.name.getText().trim());
        newProfile.setVersion((String) this.version.getSelectedItem());
        newProfile.setJvmArgs(List.of(jvmArgs.getText().trim().split(" ")));
        newProfile.setKeepOpen(keepOpen.getSelectedIndex());
        newProfile.setShowLog(showConsole.isSelected());
        if (runtime.getText() != null && !"".equals(runtime.getText().trim())) {
            newProfile.setCustomRuntime(Optional.of(runtime.getText().trim()));
        }
        if (runDir.getText() != null && !"".equals(runDir.getText().trim())) {
            newProfile.setCustomDir(Optional.of(runDir.getText().trim()));
        }
        ProfileManager.editProfile(profile, newProfile);
        ProfileManager.saveProfiles();
        ProfileManager.CURRENT_PROFILE = newProfile;
        LauncherWindow.mainWindow.updateText();
    }

    public void removeProfile(ActionEvent e) {
        close();
        ProfileManager.deleteProfile(profile.name());
        ProfileManager.saveProfiles();
        ProfileManager.CURRENT_PROFILE = ProfileManager.DEFAULT_PROFILE;
        LauncherWindow.mainWindow.updateText();
    }

    private void close() {
        dispose();
        LauncherWindow.mainWindow.changeProfile = null;
    }

}
