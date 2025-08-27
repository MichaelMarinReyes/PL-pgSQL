package frontend.tabs;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author michael
 */
public class TabPane extends JTabbedPane {
    public TabPane() {
        super();
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }
    
    public void addNewTab(String title, JPanel panel) {
        this.addTab(title, panel);
        this.setSelectedComponent(panel);
    }
    
    public void closeTab(int index) {
        if (index >= 0 && index < this.getTabCount()) {
            this.remove(index);
        }
    }
}
