import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
public class TopMenuBar extends JMenuBar {

    private String openedFileName;
    private GraphVisualizerUI ui;

    public TopMenuBar(GraphVisualizerUI ui) {
        this.ui = ui;
        setBackground(AppTheme.BG_COLOR);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.FG_COLOR));

        // --- Zakładka "Plik" ---
        JMenu menuFile = createStyledMenu("Plik");
        JMenuItem menuSavePng = createStyledMenuItem("Wyeksportuj jako .png");
        menuSavePng.addActionListener(e -> saveFilePngAction());
        JMenuItem menuSaveTxt = createStyledMenuItem("Wyeksportuj jako .txt");
        menuSaveTxt.addActionListener(e -> saveFileTxtAction());
        JMenuItem menuOpen  = createStyledMenuItem("Wczytaj .txt do obliczenia");
        menuOpen.addActionListener(e -> openFileAction());
	JMenuItem menuOpenComputed  = createStyledMenuItem("Wczytaj .txt do wizualizacji");
        menuOpenComputed.addActionListener(e -> openComputedFileAction());

	menuFile.add(menuOpenComputed);
        menuFile.add(menuSavePng);
        menuFile.add(menuSaveTxt);
        menuFile.add(menuOpen);
        add(menuFile);

        // --- Zakładka "Widok" ---

        JMenu menuView = createStyledMenu("Widok");
        
        // Tworzymy elementy menu
        JMenuItem fullScreenItem = createStyledMenuItem("Zmień widoczność paska bocznego");
        fullScreenItem.setAccelerator(KeyStroke.getKeyStroke("F12"));
        fullScreenItem.addActionListener(e -> toggleSideBar());
        
        JMenuItem toggleWeightsItem = createStyledMenuItem("Zmień widoczność wag");
        toggleWeightsItem.setAccelerator(KeyStroke.getKeyStroke("F1"));
        toggleWeightsItem.addActionListener(e -> toggleWeights());
        
        JMenuItem toggleLabelsItem = createStyledMenuItem("Zmień widoczność etykiet krawędzi");
        toggleLabelsItem.setAccelerator(KeyStroke.getKeyStroke("F2"));
        toggleLabelsItem.addActionListener(e -> toggleLabels());

        menuView.add(fullScreenItem);
        menuView.add(toggleWeightsItem);
        menuView.add(toggleLabelsItem);
        add(menuView);

        // --- Zakładka "O programie" ---
        JMenuItem menuAbout = createStyledMenuItem("O programie");
        menuAbout.addActionListener(e -> {
            AboutWindow aboutwindow = new AboutWindow(ui);
            aboutwindow.setVisible(true);
        });
        menuAbout.setMaximumSize(menuAbout.getPreferredSize());
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

    private void saveFilePngAction() {
        // Tworzymy okno dialogowe wyboru pliku
        JFileChooser fileChooser = new JFileChooser();

        // Ustawienie początkowego katalogu
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Wyeksportuj graf jako obraz PNG");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Pliki obrazów (*.png)", "png"));

        int result = fileChooser.showSaveDialog(this);

        // Sprawdzamy, czy użytkownik kliknął "Zapisz"
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try{
                ui.canvas.exportToPNG(selectedFile);
                ui.statusBar.setStatus("Plik został zapisany.");
            }
            catch (Exception e) {
                ui.statusBar.setStatus("Nie udało się zapisać pliku.");
            }
        } else {
            ui.statusBar.setStatus("Anulowano operację zapisu pliku.");
        }
    }
    private void saveFileTxtAction(){
        // Tworzymy okno dialogowe wyboru pliku
        JFileChooser fileChooser = new JFileChooser();

        // Ustawienie początkowego katalogu
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Wyeksportuj graf jako plik TXT");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Pliki tekstowe (*.txt)", "txt"));
        int result = fileChooser.showSaveDialog(this);
        // Sprawdzamy, czy użytkownik kliknął "Zapisz"
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try{
                ui.engine.writeToTxt(selectedFile);
                ui.statusBar.setStatus("Plik został zapisany.");
            }
            catch (Exception e) {
                ui.statusBar.setStatus("Nie udało się zapisać pliku.");
            }
        } else {
            ui.statusBar.setStatus("Anulowano operację zapisu pliku.");
        }

    }

    private void openFileAction(){
        // Tworzymy okno dialogowe wyboru pliku
        JFileChooser fileChooser = new JFileChooser();

        // Ustawienie początkowego katalogu
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Otwórz plik wejściowy grafu do obliczenia");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Pliki tekstowe (*.txt)", "txt"));

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
    public void openComputedFileAction(){
	  // Tworzymy okno dialogowe wyboru pliku
        JFileChooser fileChooser = new JFileChooser();

        // Ustawienie początkowego katalogu
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        fileChooser.setDialogTitle("Otwórz plik wejściowy grafu do wizualizacji");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Pliki tekstowe (*.txt)", "txt"));
        int result = fileChooser.showSaveDialog(this);
        // Sprawdzamy, czy użytkownik kliknął "Zapisz"
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try{
		    
		ui.engine.displayComputed(selectedFile);
                ui.statusBar.setStatus("Plik otwarty");
            }
            catch (Exception e) {
                ui.statusBar.setStatus("Nie udało się zapisać pliku.");
            }
        } else {
            ui.statusBar.setStatus("Anulowano operację zapisu pliku.");
        }


    }

    public String getOpenFileName() {
        return openedFileName;
    }

    public void toggleSideBar() {
        ui.sidebar.setVisible(!ui.sidebar.isVisible());
        
        if (ui.sidebar.isVisible() && ui.sidebar.getParent() instanceof JSplitPane) {
            ((JSplitPane) ui.sidebar.getParent()).setDividerLocation(250);
        }
        ui.revalidate();
        ui.repaint();
    }

    public void toggleWeights() {
        ui.settings.showWeights = !ui.settings.showWeights;
        ui.canvas.repaint();
        ui.sidebar.buildUI();
    }

    public void toggleLabels() {
        ui.settings.showLabels = !ui.settings.showLabels;
        ui.canvas.repaint();
        ui.sidebar.buildUI();
    }
}
