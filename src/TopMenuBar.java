import javax.swing.*;
public class TopMenuBar extends JMenuBar {

    public TopMenuBar() {
        setBackground(AppTheme.BG_COLOR);
        setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.FG_COLOR));

        // --- Zakładka "Plik" ---
        JMenu menuFile = createStyledMenu("Plik");
        menuFile.add(createStyledMenuItem("Zapisz jako..."));
        menuFile.add(createStyledMenuItem("Otwórz"));
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


}
