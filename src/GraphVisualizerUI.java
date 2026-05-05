import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GraphVisualizerUI extends JFrame {

    public GraphVisualizerUI() {
        initFrame();
        setJMenuBar(new TopMenuBar());
        initLayout();
    }

    private void initFrame() {
        setTitle("Wizualizator grafów");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        ImageIcon logo = new ImageIcon("./images/graph_logo.png");
        setIconImage(logo.getImage());
    }

    private void initLayout() {
        JPanel sidebar = new SidebarPanel();
        JLabel statusBar = new StatusBar();
        JPanel canvas = new GraphCanvas();

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, canvas);
        splitPane.setDividerLocation(250);
        splitPane.setDividerSize(3);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }
}