import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

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

    public static class AboutWindow extends JFrame{
        public AboutWindow (){
            createAboutWindow();
            initLayout();
        }

        private void createAboutWindow(){

            setTitle("Wizualizator grafów");
            setSize(450, 300);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setLocationRelativeTo(null);
            setBackground(AppTheme.BG_COLOR);


        }

        private void initLayout() {
            this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
            this.getContentPane().setBackground(AppTheme.BG_COLOR); // Keep the theme!

            add(Box.createVerticalStrut(20));

            ImageIcon icon = new ImageIcon("src/images/graph_logo.png");

            Image image = icon.getImage(); // transform it
            Image newimg = image.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
            icon = new ImageIcon(newimg);  // transform it back


            JLabel picLabel = new JLabel(icon);
            picLabel.setAlignmentX(CENTER_ALIGNMENT);
            add(picLabel);

            add(Box.createVerticalStrut(20));

            JLabel textLabel = new JLabel("Wizualizator Grafów v1.0");
            textLabel.setForeground(AppTheme.FG_COLOR);
            textLabel.setAlignmentX(CENTER_ALIGNMENT);
            add(textLabel);

            JLabel authors = new JLabel("<html><b>Autorzy:</b>Aleksander Józwik, Anastasiya Kryvetskaya</html>");
            authors.setForeground(AppTheme.FG_COLOR);
            authors.setAlignmentX(CENTER_ALIGNMENT);
            authors.setHorizontalAlignment(SwingConstants.CENTER);
            add(authors);

            JLabel hyperlink = new JLabel("<html><u><b>Repozytorium projektu na GitHub</b></u></html>");
            hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            hyperlink.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e) {
                    try {

                        Desktop.getDesktop().browse(new URI("https://github.com/0-Cuprum-0/jimp2-wizualizacja-grafu-java"));

                    } catch (IOException | URISyntaxException e1) {
                        e1.printStackTrace();
                    }
                }

            });
            hyperlink.setForeground(AppTheme.FG_COLOR);
            hyperlink.setAlignmentX(CENTER_ALIGNMENT);
            add(hyperlink);
            hyperlink.setHorizontalAlignment(SwingConstants.CENTER);

            add(Box.createVerticalGlue());
            JButton closeBtn = new JButton("Zamknij");
            closeBtn.setAlignmentX(CENTER_ALIGNMENT);
            closeBtn.addActionListener(e -> this.dispose());
            add(closeBtn);
            add(Box.createVerticalStrut(20));
        }





    }
}
