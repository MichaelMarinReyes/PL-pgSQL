package frontend.tabs;

import javax.swing.*;
import java.awt.*;

public class ButtonTabComponent extends JPanel {
    public ButtonTabComponent(final JTabbedPane tabbedPane) {
        setOpaque(false);
        FlowLayout layout = new FlowLayout(FlowLayout.LEFT, 0, 0);
        setLayout(layout);

        JLabel titleLabel = new JLabel() {
            @Override
            public String getText() {
                int i = tabbedPane.indexOfTabComponent(ButtonTabComponent.this);
                return i != -1 ? tabbedPane.getTitleAt(i) : "";
            }
        };

        add(titleLabel);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

        JButton closeButton = new JButton("x");
        closeButton.setMargin(new Insets(0, 0, 0, 0));
        closeButton.setPreferredSize(new Dimension(16, 16));
        closeButton.setBorderPainted(false);
        closeButton.setContentAreaFilled(false);
        closeButton.setFocusPainted(false);
        closeButton.setOpaque(false);
        closeButton.setFont(new Font("Arial", Font.BOLD, 12));
        closeButton.addActionListener(e -> {
            int i = tabbedPane.indexOfTabComponent(ButtonTabComponent.this);
            if (i != -1) {
                tabbedPane.remove(i);
            }
        });

        add(closeButton);
    }
}