package frontend.tabs;

import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;

/**
 *
 * @author michael
 */
public class TabPanel extends JPanel {

    private javax.swing.JTabbedPane tabsPanelTabbed;

    public TabPanel() {
        initComponents();
        //addNewTab("Pestaña 1");
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        tabsPanelTabbed = new javax.swing.JTabbedPane();
        setLayout(new BorderLayout());
        add(tabsPanelTabbed, BorderLayout.CENTER);
    }

    public void addNewTab(String title) {
        JPanel newTab = new JPanel(new BorderLayout());

        JTextArea editorTextArea = new JTextArea();
        editorTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        LineNumber lineNumber = new LineNumber(editorTextArea);
        JScrollPane editorScrollPane = new JScrollPane(editorTextArea);
        editorScrollPane.setRowHeaderView(lineNumber);

        JButton newExecuteButton = new JButton("Ejecutar");
        JLabel newColumnLabel = new JLabel("Columna: 1");

        JTextArea consoleTextArea = new JTextArea();
        consoleTextArea.setEditable(false);
        consoleTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane consoleScrollPane = new JScrollPane(consoleTextArea);
        consoleScrollPane.setBorder(BorderFactory.createTitledBorder("Consola"));

        newExecuteButton.addActionListener(e -> {
            if (editorTextArea.getText().equals("")) {
                consoleTextArea.setText("No hay texto para ejecutar\n");
                consoleTextArea.append("Escriba en el área de texto");
            } else {
                consoleTextArea.setText("");
                String code = editorTextArea.getText();
                consoleTextArea.append("Ejecutando códe\n");
                consoleTextArea.append(code + "\n");
                consoleTextArea.append("Fin\n\n");
            }
        });

        editorTextArea.addCaretListener(e -> {
            int pos = editorTextArea.getCaretPosition();
            try {
                int line = editorTextArea.getLineOfOffset(pos);
                int col = pos - editorTextArea.getLineStartOffset(line) + 1;
                newColumnLabel.setText("Columna: " + col);
            } catch (Exception ex) {
                newColumnLabel.setText("Columna: 1");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        buttonPanel.add(newExecuteButton);
        buttonPanel.add(newColumnLabel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(editorScrollPane, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topPanel, consoleScrollPane);
        splitPane.setDividerLocation(400);
        splitPane.setResizeWeight(0.8);

        newTab.add(splitPane, BorderLayout.CENTER);

        tabsPanelTabbed.addTab(title, newTab);
        tabsPanelTabbed.setSelectedComponent(newTab);

        int index = tabsPanelTabbed.indexOfComponent(newTab);
        tabsPanelTabbed.setTabComponentAt(index, new ButtonTabComponent(tabsPanelTabbed));
        tabsPanelTabbed.setSelectedComponent(newTab);
    }

    public javax.swing.JTabbedPane getTabsPanelTabbed() {
        return tabsPanelTabbed;
    }
}
