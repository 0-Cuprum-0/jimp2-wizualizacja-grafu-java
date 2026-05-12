import java.io.File;

import javax.swing.*;
public class TopMenuBar extends JMenuBar {

    private String openedFileName;
    private GraphVisualizerUI ui;

    public TopMenuBar(GraphVisualizerUI ui) {
        this.ui = ui;
        setBackground(AppTheme.BG_COLOR);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.FG_COLOR));

        // --- Zakładka "Plik" ---
        JMenu menuFile = createStyledMenu("Plik");
        JMenuItem menuSave = createStyledMenuItem("Zapisz jako...");
        JMenuItem menuOpen  = createStyledMenuItem("Otwórz");
        menuOpen.addActionListener(e -> openFileAction());

        menuFile.add(menuSave);
        menuFile.add(menuOpen);
        add(menuFile);

        // --- Zakładka "Widok" ---

        JMenu menuView = createStyledMenu("Widok");
        
        // Tworzymy elementy menu
        JMenuItem fullScreenItem = createStyledMenuItem("Pełen ekran");
        fullScreenItem.setAccelerator(KeyStroke.getKeyStroke("F12"));
        
        JMenuItem toggleWeightsItem = createStyledMenuItem("Zmień widoczność wag");
        toggleWeightsItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
        
        JMenuItem toggleLabelsItem = createStyledMenuItem("Zmień widoczność etykiet krawędzi");
        toggleLabelsItem.setAccelerator(KeyStroke.getKeyStroke("F2"));

        menuView.add(fullScreenItem);
        menuView.add(toggleWeightsItem);
        menuView.add(toggleLabelsItem);
        add(menuView);

        // --- Zakładka "O programie" ---
        JMenuItem menuAbout = createStyledMenuItem("O programie");
        menuAbout.addActionListener(e -> {
           // System.out.println("About was called!!!");
            AboutWindow aboutwindow = new AboutWindow();
            aboutwindow.setVisible(true);
        });
        menuAbout.setMaximumSize(menuAbout.getPreferredSize()); //nie daje ostatniemu elementowi topMenuBar rosciągać się  na całośc miejsca

        add(menuAbout);
    }

    private JMenu createStyledMenu(String title) {
        JMenu menu = new JMenu(title);
        menu.setForeground(AppTheme.FG_COLOR);
        return menu;
    }

    private JMenuItem createStyledMenuItem(String text){
        JMenuItem item = new JMenuItem(text);
        // Ustawienie ciemnego tła dla rozwijanej listy
        item.setBackground(AppTheme.BG_COLOR); 
        item.setForeground(AppTheme.FG_COLOR);
        return item;
    }

    private void openFileAction(){
        // Tworzymy okno dialogowe wyboru pliku
        JFileChooser fileChooser = new JFileChooser();

        // Ustawienie początkowego katalogu
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));

        int result = fileChooser.showOpenDialog(this);

        // Sprawdzamy, czy użytkownik kliknął "Otwórz"
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            openedFileName = selectedFile.getAbsolutePath();
            ui.engine.setInputFile(selectedFile);
            int amount_of_read_edges = ui.engine.readInputFile(selectedFile);

            if(amount_of_read_edges > 0) {
                ui.statusBar.setStatus("Wczytano " + amount_of_read_edges + " krawędzi.");
            } else {
                ui.statusBar.setStatus("Nie udało się wczytać pliku.");
            }
        } else {
            ui.statusBar.setStatus("Anulowano operację odczytu pliku.");
        }
    }

    public String getOpenFileName() {
        return openedFileName;
    }
}
