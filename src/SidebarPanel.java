import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarPanel extends JPanel {
    GraphVisualizerUI ui;

    enum ColorSwatchesTypes {
        EDGE_COLOR,
        BG_COLOR,
        VERTEX_COLOR,
        VERTEX_TEXT_COLOR
    }

    public SidebarPanel(GraphVisualizerUI ui) {
        this.ui = ui;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(AppTheme.BG_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        buildUI();
    }

    public void buildUI() {
        removeAll();

        addSectionTitle("Parametry");
        addPlainText("Algorytm");

        JComboBox<String> algorithmsChoice = new JComboBox<>(new String[] { "Fruchterman-Reingold", "Triangulacja" });
        if(ui.engine.selectedAlgorithm != null) {
            algorithmsChoice.setSelectedItem(ui.engine.selectedAlgorithm);
        }
        algorithmsChoice.addActionListener(e -> {
            ui.engine.selectedAlgorithm = (String) algorithmsChoice.getSelectedItem();
        });

        algorithmsChoice.setAlignmentX(Component.CENTER_ALIGNMENT);
        algorithmsChoice.setMaximumSize(new Dimension(Integer.MAX_VALUE, algorithmsChoice.getPreferredSize().height));

        JCheckBox weightCheckBox = new JCheckBox();
        weightCheckBox.setSelected(ui.showWeights);
        weightCheckBox.setBackground(AppTheme.BG_COLOR);
        weightCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.showWeights = weightCheckBox.isSelected();
                ui.canvas.repaint();
            }
        });
        
        JCheckBox labelCheckBox = new JCheckBox();
        labelCheckBox.setSelected(ui.showLabels);
        labelCheckBox.setBackground(AppTheme.BG_COLOR);
        labelCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ui.showLabels = labelCheckBox.isSelected();
                ui.canvas.repaint();
            }
        });

        JButton resetBtn = new JButton("Resetuj ustawienia");
        resetBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetBtn.addActionListener(e -> {
            resetSettings();
        });

        JButton fullBtn = new JButton("Schowaj panel boczny");
        fullBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        fullBtn.addActionListener(e -> {
            ui.sidebar.setVisible(!ui.sidebar.isVisible());
            ui.revalidate();
            ui.repaint();
        });

        JButton runBtn = new JButton("Uruchom");
        runBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        runBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!ui.engine.checkIfInputFileExists()) {
                    ui.showErrorMessage("Nie wybrano pliku wejśćiowego.");
                    return;
                }

                
                System.out.println("Uruchomione wizualizację");
                ui.engine.launchC();
                int amount_of_read_vertices = ui.engine.readOutputFile();

                if(amount_of_read_vertices > 0) {
                    ui.statusBar.setStatus("Wczytano " + amount_of_read_vertices + " wierzchołków.");
                } else {
                   ui.showErrorMessage("Nie udało się wczytać pliku wyjściowego.");
                }
                ui.canvas.repaint();
                ui.canvas.resetCamera();
            }
        });

        JPanel edgeColorSwatch = createColorSwatch(ui.edgeColor, ColorSwatchesTypes.EDGE_COLOR);
        JPanel bgColorSwatch = createColorSwatch(ui.bgColor, ColorSwatchesTypes.BG_COLOR);
        JPanel vertexColorSwatch = createColorSwatch(ui.vertexColor, ColorSwatchesTypes.VERTEX_COLOR);
        JPanel vertexTextColorSwatch = createColorSwatch(ui.vertexTextColor, ColorSwatchesTypes.VERTEX_TEXT_COLOR);

        JSlider vertexRadius = new JSlider(0, 50, 10);
        vertexRadius.addChangeListener(e -> {
            ui.canvas.baseVertexRadius = vertexRadius.getValue();
            ui.canvas.repaint();
        });

        add(Box.createVerticalStrut(10));
        add(algorithmsChoice);
        add(Box.createVerticalStrut(10));
        
        add(createRowWithLabel("Kolor krawędzi:", edgeColorSwatch));
        add(Box.createVerticalStrut(10));
        add(createRowWithLabel("Kolor tła:", bgColorSwatch));
        add(Box.createVerticalStrut(10));
        add(createRowWithLabel("Kolor tła wierzchołków:", vertexColorSwatch));
        add(Box.createVerticalStrut(10));
        add(createRowWithLabel("Kolor tekstu wierzchołków:", vertexTextColorSwatch));
        add(Box.createVerticalStrut(10));
        
        add(createRowWithLabel("Pokaż wagi", weightCheckBox));
        add(Box.createVerticalStrut(10));
        add(createRowWithLabel("Pokaż etykiety", labelCheckBox));

        add(Box.createVerticalStrut(10));
        addPlainText("Promień wierzchołka");
        add(vertexRadius);
        
        add(Box.createVerticalGlue()); // Wypycha dolne przyciski na sam dół

        add(fullBtn);
        add(Box.createVerticalStrut(10));

        add(resetBtn);

        add(Box.createVerticalStrut(10));

        add(runBtn);


        Dimension size = fullBtn.getPreferredSize();
        runBtn.setMaximumSize(size);
        resetBtn.setMaximumSize(size);
        fullBtn.setMaximumSize(size);

        revalidate();
        repaint();
    }

    private JPanel createColorSwatch(Color initialColor, ColorSwatchesTypes type) {
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
                    switch (type) {
                        case EDGE_COLOR:
                            ui.edgeColor = selectedColor;
                            break;
                        case BG_COLOR:
                            ui.bgColor = selectedColor;
                            break;
                        case VERTEX_COLOR:
                            ui.vertexColor = selectedColor;
                            break;
                        case VERTEX_TEXT_COLOR:
                            ui.vertexTextColor = selectedColor;
                            break;
                    }
                    ui.canvas.repaint();
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

    private void resetSettings() {
        ui.showWeights = true;
        ui.showLabels = true;
        ui.edgeColor = AppTheme.FG_COLOR;
        ui.bgColor = AppTheme.BG_COLOR;
        ui.vertexColor = AppTheme.ACCENT_COLOR;
        ui.vertexTextColor = AppTheme.FG_COLOR;
        ui.sidebar.buildUI();
        ui.canvas.repaint();
    }
}
