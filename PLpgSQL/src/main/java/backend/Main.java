package backend;

import frontend.principal.MainWindow;

import java.awt.*;

public class Main {
    public static void main(String[] args) {
        MainWindow mainWindow = new MainWindow();
        mainWindow.setSize((int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(), (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight());
        mainWindow.setLocationRelativeTo(null);
        mainWindow.setVisible(true);
    }
}