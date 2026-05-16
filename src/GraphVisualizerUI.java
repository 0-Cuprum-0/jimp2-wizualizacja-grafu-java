import javax.swing.*;
import java.awt.*;

public class GraphVisualizerUI extends JFrame {
    BackEnd engine;
    SidebarPanel sidebar;
    StatusBar statusBar;
    GraphCanvas canvas;

    public VisualizationSettings settings;

    public GraphVisualizerUI(BackEnd engine) {
        this.engine = engine;
        this.engine.setUI(this);
        this.settings = new VisualizationSettings();
        initFrame();
        initLayout();
        setJMenuBar(new TopMenuBar(this));
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
        sidebar = new SidebarPanel(this);
        statusBar = new StatusBar();
        canvas = new GraphCanvas(this);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, sidebar, canvas);
        splitPane.setDividerLocation(250);
        splitPane.setDividerSize(3);
        splitPane.setBorder(null);

        add(splitPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);
    }

    public void showErrorMessage(String message) { 
        this.statusBar.setStatus("BŁĄD: " + message);
        JOptionPane.showMessageDialog(new JFrame(), "BŁĄD: " +message, "Wystąpił błąd",
        JOptionPane.ERROR_MESSAGE);
    }
}