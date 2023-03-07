package io.innocentdream.launcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public class BottomPanel extends JPanel {

    private Image image = null;
    private Image img;

    public BottomPanel() {
        setLayout(new GridBagLayout());
        try {
            image = ImageIO.read(Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("assets/background.png")))
                    .getScaledInstance(32, 32, 16);
        } catch (IOException e) {
            System.err.println("Failed to load background");
            e.printStackTrace();
        }

        GridBagConstraints constraints = new GridBagConstraints();

        JPanel left = new JPanel();
        left.setLayout(new GridBagLayout());
        left.setOpaque(false);
        left.setAlignmentX(SwingConstants.CENTER);

        constraints.gridx = 0;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 0.0;
        constraints.weighty = 1.0;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.gridy = 1;
        constraints.insets = new Insets(25, 50, 2, 50);
        left.add(LauncherWindow.mainWindow.selectProfile, constraints);

        constraints.gridy = 2;
        constraints.insets = new Insets(0, 50, 0, 50);
        left.add(LauncherWindow.mainWindow.editProfile, constraints);

        JPanel mid = new JPanel();
        mid.setLayout(new GridBagLayout());
        mid.setOpaque(false);
        mid.setAlignmentX(SwingConstants.CENTER);

        constraints.gridy = 0;
        constraints.insets = new Insets(10, 2, 4, 0);
        constraints.weightx = 0.0;
        constraints.gridwidth = GridBagConstraints.RELATIVE;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = 2;
        mid.add(LauncherWindow.mainWindow.play, constraints);

        constraints.gridy = 1;
        constraints.gridx = 1;
        constraints.weightx = 0.0;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(0, 2, 0, 0);
        mid.add(LauncherWindow.mainWindow.settingsButton, constraints);

        JPanel right = new JPanel();
        right.setLayout(new GridBagLayout());
        right.setOpaque(false);
        right.setAlignmentX(SwingConstants.CENTER);

        constraints.gridx = 0;
        constraints.ipady = 0;
        constraints.gridy = 1;
        constraints.insets = new Insets(25, 50, 2, 50);
        constraints.gridwidth = 1;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.fill = GridBagConstraints.BOTH;
        right.add(LauncherWindow.mainWindow.currentProfile, constraints);

        constraints.gridy = 2;
        constraints.insets = new Insets(0, 50, 0, 50);
        //right.add(LauncherWindow.mainWindow.selectProfile, constraints);

        GridBagConstraints bottomConstraints = new GridBagConstraints();
        bottomConstraints.fill = GridBagConstraints.BOTH;
        bottomConstraints.gridwidth = 1;
        bottomConstraints.gridx = 0;
        bottomConstraints.gridy = 1;
        bottomConstraints.weightx = 1.0;
        bottomConstraints.weighty = 0.0;
        bottomConstraints.insets = new Insets(0, 0, 0, 0);
        this.add(left, bottomConstraints);

        bottomConstraints.gridx = 1;
        bottomConstraints.fill = GridBagConstraints.HORIZONTAL;
        this.add(mid, bottomConstraints);

        bottomConstraints.gridx = 2;
        this.add(right, bottomConstraints);
    }

    @Override
    public void update(Graphics g) {
        this.paint(g);
    }

    @Override
    protected void paintComponent(Graphics g) {
        final int w = this.getWidth() / 2 + 1;
        final int h = this.getHeight() / 2 + 1;
        if (this.img == null || this.img.getWidth(null) != w || this.img.getHeight(null) != h) {
            this.img = this.createImage(w, h);
            final Graphics graphics = this.img.getGraphics();
            for (int i = 0; i <= w / 32; ++i) {
                for (int j = 0; j <= h / 32; ++j) {
                    graphics.drawImage(this.image, i * 32, j * 32, null);
                }
            }
            if (graphics instanceof Graphics2D twoD) {
                //Come back to
            }
            graphics.dispose();
        }
        g.drawImage(this.img, 0, 0, w * 2, h * 2, null);
    }
}
