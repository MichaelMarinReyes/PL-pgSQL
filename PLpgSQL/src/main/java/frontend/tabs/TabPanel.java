package frontend.tabs;

import backend.analyzers.Parse;

import javax.swing.*;
import java.awt.*;

/**
 * @author michael
 */
public class TabPanel extends JPanel {

    private final JTextArea consoleTextArea = new JTextArea();
    private final JTextArea editorTextArea = new JTextArea();
    private final Parse parse = new Parse();
    private JTabbedPane tabsPanelTabbed;

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

        editorTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));

        LineNumber lineNumber = new LineNumber(editorTextArea);
        JScrollPane editorScrollPane = new JScrollPane(editorTextArea);
        editorScrollPane.setRowHeaderView(lineNumber);

        JButton newExecuteButton = new JButton("Ejecutar");
        JLabel newColumnLabel = new JLabel("Columna: 1");


        consoleTextArea.setEditable(false);
        consoleTextArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane consoleScrollPane = new JScrollPane(consoleTextArea);
        consoleScrollPane.setBorder(BorderFactory.createTitledBorder("Consola"));

        newExecuteButton.addActionListener(e -> {
            if (editorTextArea.getText().equals("")) {
                consoleTextArea.setText("No hay texto para ejecutar\n");
                consoleTextArea.append("Escriba en el área de texto");
            } else {
                parserText();
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

    private void parserText() {
        consoleTextArea.setText("");
        String code = editorTextArea.getText();
        String response = parse.analyzeText(code);
        consoleTextArea.setText(response);
    }
}
