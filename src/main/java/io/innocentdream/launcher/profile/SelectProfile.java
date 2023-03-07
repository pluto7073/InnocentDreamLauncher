package io.innocentdream.launcher.profile;

import io.innocentdream.launcher.LauncherWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SelectProfile extends JFrame {

    public final JPanel panel;
    public final JButton newProfile, delProfile, selProfile;

    public DefaultListModel<String> model;
    public JList<String> list;
    public JScrollPane listScrollPane;
    public GridBagConstraints constraints;

    public SelectProfile() {
        super("Select Profile");
        this.setIconImage(LauncherWindow.mainWindow.icon);
        this.setMinimumSize(new Dimension(200, 400));

        this.panel = new JPanel();
        panel.setLayout(new GridBagLayout());

        this.constraints = new GridBagConstraints();

        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(5, 5, 0, 5);
        constraints.gridwidth = 2;
        constraints.weightx = 1.0;

        this.newProfile = new JButton("New Profile");
        this.newProfile.addActionListener(this::newProfile);
        panel.add(this.newProfile, constraints);

        makeList();

        constraints.gridy++;
        constraints.weighty = GridBagConstraints.RELATIVE;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.gridheight = 1;
        constraints.insets = new Insets(5, 5, 5, 5);

        this.selProfile = new JButton("Select");
        this.selProfile.addActionListener(this::selectProfile);
        panel.add(this.selProfile, constraints);

        constraints.gridx = 1;

        this.delProfile = new JButton("Delete");
        this.delProfile.addActionListener(this::deleteProfile);
        panel.add(this.delProfile, constraints);

        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(LauncherWindow.mainWindow);
        this.setVisible(true);
    }

    public void deleteProfile(ActionEvent e) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this profile?", "Are You Sure?", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            ProfileManager.deleteProfile(list.getSelectedValue());
        }
    }

    public void selectProfile(ActionEvent e) {
        ProfileManager.CURRENT_PROFILE = ProfileManager.findProfile(list.getSelectedValue());
        dispose();
        LauncherWindow.mainWindow.selProfile = null;
        LauncherWindow.mainWindow.updateText();
    }

    public void newProfile(ActionEvent e) {
        String name = JOptionPane.showInputDialog(this, "Profile Name:", "New Profile", JOptionPane.PLAIN_MESSAGE);
        if (name != null && !name.equals("")) ProfileManager.newProfile(name);
        makeList();
        pack();
    }

    public void makeList() {
        int i = 0;
        int currentIndex = 0;
        String currentProfile = ProfileManager.CURRENT_PROFILE.name();
        this.model = new DefaultListModel<>();
        for (String profile : ProfileManager.getProfiles()) {
            this.model.addElement(profile);
            if (currentProfile.equals(profile)) {
                currentIndex = i;
            }
            i++;
        }
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.gridheight = GridBagConstraints.RELATIVE;
        constraints.weighty = 1.0;

        this.list = new JList<>(this.model);
        this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.list.setLayoutOrientation(JList.VERTICAL);
        this.list.setVisibleRowCount(10);
        this.list.setSelectedIndex(currentIndex);

        if (listScrollPane != null) {
            panel.remove(listScrollPane);
        }

        this.listScrollPane = new JScrollPane(list);
        this.listScrollPane.setWheelScrollingEnabled(true);
        this.panel.add(listScrollPane, constraints);
    }

}
