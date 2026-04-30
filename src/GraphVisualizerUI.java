import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GraphVisualizerUI extends JFrame {
    private final Color BG_COLOR = new Color(17, 17, 27);
    private final Color FG_COLOR = new Color(205, 214, 244);
    private final Color ACCENT_COLOR = new Color(203, 166, 247);

    public GraphVisualizerUI() {
        setTitle("Wizualizator grafów");
        setSize(900,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setJMenuBar(createMenuBar());

        JPanel sidebar = createSideBar();

        JPanel canvas = new JPanel();
        canvas.setBackground(BG_COLOR);
        canvas.setBorder(new EmptyBorder(15, 15, 15, 15));

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,sidebar,canvas);
        splitPane.setDividerLocation(250);
        splitPane.setDividerSize(3);
        splitPane.setBorder(null);
        add(splitPane,BorderLayout.CENTER);

        JLabel statusBar = new JLabel(" Info: Wczytano plik \"graf.txt\"");
        statusBar.setForeground(FG_COLOR);
        statusBar.setOpaque(true);
        statusBar.setBackground(new Color(20, 20, 30));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        statusBar.setPreferredSize(new Dimension(getWidth(), 25));
        add(statusBar, BorderLayout.SOUTH);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(BG_COLOR);

        String[] menus = {"Plik", "Widok", "Opcje", "Pomoc"};
        for (String title : menus) {
            JMenu menu = new JMenu(title);
            menu.setForeground(FG_COLOR);
            menuBar.add(menu);
        }
        return menuBar;
    }

    private JPanel createSideBar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG_COLOR);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        addSectionTitle(panel, "Parametry");
        addFormRow(panel, "Algorytm:", new JComboBox<>(new String[]{"Fruchterman-Reingold", "Triangulacja"}));


        panel.add(Box.createVerticalStrut(20));

        JButton runBtn = new JButton("URUCHOM");
        runBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(runBtn);

        return panel;
    }

    private void addSectionTitle(JPanel parent, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(FG_COLOR);
        label.setFont(new Font("SansSerif", Font.BOLD, 16));
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, FG_COLOR)); // Podkreślenie
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        parent.add(label);
        parent.add(Box.createVerticalStrut(10));
    }

    private void addFormRow(JPanel parent, String labelText, JComponent inputComp) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 2));
        row.setBackground(BG_COLOR);
        row.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setForeground(FG_COLOR);
        row.add(label);
        row.add(inputComp);

        parent.add(row);
        parent.add(Box.createVerticalStrut(5));
    }
}
