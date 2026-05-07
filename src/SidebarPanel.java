import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarPanel extends JPanel {
    BackEnd engine;
    public SidebarPanel(BackEnd engine) {
        this.engine = engine;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppTheme.BG_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        buildUI();
    }

    private void buildUI() {
        
        addSectionTitle("Parametry");
        addPlainText("Algorytm");

        JComboBox<String> algorithmsChoice = new JComboBox<>(new String[] { "Fruchterman-Reingold", "Triangulacja" });

        algorithmsChoice.setAlignmentX(Component.CENTER_ALIGNMENT);
        algorithmsChoice.setMaximumSize(new Dimension(Integer.MAX_VALUE, algorithmsChoice.getPreferredSize().height));

        JButton runBtn = new JButton("URUCHOM");
        runBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        runBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("URUCHOM clicked");
                engine.launchC();
            }
        });

        JButton fullBtn = new JButton("Pokaż w pełnym ekranie");
        fullBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        JCheckBox weightCheckBox = new JCheckBox();
        weightCheckBox.setBackground(AppTheme.BG_COLOR);
        
        JCheckBox labelCheckBox = new JCheckBox();
        labelCheckBox.setBackground(AppTheme.BG_COLOR);

        add(Box.createVerticalStrut(10));
        add(algorithmsChoice);
        add(Box.createVerticalStrut(10));
        
        add(createRowWithLabel("Kolor krawędzi:", createColorSwatch(AppTheme.ACCENT_COLOR)));
        add(Box.createVerticalStrut(10));
        add(createRowWithLabel("Kolor tła:", createColorSwatch(AppTheme.ACCENT_COLOR)));
        add(Box.createVerticalStrut(10));
        add(createRowWithLabel("Kolor tła wierzchołków:", createColorSwatch(AppTheme.ACCENT_COLOR)));
        add(Box.createVerticalStrut(10));
        add(createRowWithLabel("Kolor tekstu wierzchołków:", createColorSwatch(AppTheme.ACCENT_COLOR)));
        add(Box.createVerticalStrut(10));
        
        add(createRowWithLabel("Pokaż wagi", weightCheckBox));
        add(Box.createVerticalStrut(10));
        add(createRowWithLabel("Pokaż etykiety", labelCheckBox));
        
        add(Box.createVerticalGlue()); // Wypycha dolne przyciski na sam dół

        add(fullBtn);
        add(Box.createVerticalStrut(10));
        add(runBtn);


        Dimension size = fullBtn.getPreferredSize();
        runBtn.setMaximumSize(size);
        fullBtn.setMaximumSize(size);
    }

    private JPanel createColorSwatch(Color initialColor) {
        JPanel swatch = new JPanel();
        swatch.setPreferredSize(new Dimension(24, 24)); 
        swatch.setBackground(initialColor);
        swatch.setBorder(BorderFactory.createLineBorder(new Color(80, 80, 100), 1));
        swatch.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        swatch.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Color selectedColor = JColorChooser.showDialog(swatch, "Wybierz kolor", swatch.getBackground());
                if (selectedColor != null) {
                    swatch.setBackground(selectedColor);
                }
            }
        });

        return swatch;
    }

    private void addPlainText(String text) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(AppTheme.FG_COLOR);
        label.setFont(AppTheme.SECTION_FONT);
        add(label);
    }

    private void addSectionTitle(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(AppTheme.FG_COLOR);
        label.setFont(AppTheme.SECTION_FONT);
        label.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.FG_COLOR)); 
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(label);
        add(Box.createVerticalStrut(10));
    }

    private JPanel createRowWithLabel(String string, JComponent element) {
        JPanel row = new JPanel(new BorderLayout(15, 0));

        JLabel label = new JLabel(string);

        element.setOpaque(true);
        element.setForeground(AppTheme.FG_COLOR);

        row.setBackground(AppTheme.BG_COLOR);
        label.setForeground(AppTheme.FG_COLOR);
        
        row.add(label, BorderLayout.CENTER);
        row.add(element, BorderLayout.EAST);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, element.getPreferredSize().height + 10));

        return row;
    }
}
