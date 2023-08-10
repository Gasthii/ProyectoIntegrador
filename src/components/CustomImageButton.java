package components;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class CustomImageButton extends JButton {

    public CustomImageButton(ImageIcon icon) {
        setIcon(icon);
        setPreferredSize(new Dimension(300, 300));
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 80, 80));
        super.paintComponent(g2);
        g2.dispose();
    }
}

