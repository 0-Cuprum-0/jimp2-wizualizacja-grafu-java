import javax.swing.*;
import java.awt.*;

public class GraphVisualizerUI extends JFrame {
    BackEnd engine;
    SidebarPanel sidebar;
    StatusBar statusBar;
    GraphCanvas canvas;

    public boolean showWeights = true;
    public boolean showLabels = true;

    public Color edgeColor = AppTheme.FG_COLOR;
    public Color bgColor = AppTheme.BG_COLOR;
    public Color vertexColor = AppTheme.ACCENT_COLOR;
    public Color vertexTextColor = AppTheme.FG_COLOR;

    public GraphVisualizerUI(BackEnd engine) {
        this.engine = engine;
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
}