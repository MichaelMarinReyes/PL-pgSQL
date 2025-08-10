package frontend.principal;

import javax.swing.*;

public class MainWindow extends JFrame{
    private JPanel panel1;
    private JToolBar menuBar;
    private JButton archiveButton;
    private JPanel contentPane;
    private JButton reportsButton;

    public MainWindow() {
        initComponents();
        setTitle("Intérprete PL/pgSQL");
        changePanel(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


    private void initComponents() {
        setContentPane(panel1);
    }

    private void changePanel(JPanel panel) {
        panel.removeAll();
        panel.add(panel);
        panel.revalidate();
        panel.repaint();
    }


    private void createUIComponents() {

    }
}
