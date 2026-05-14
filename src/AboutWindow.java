import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutWindow extends JFrame {

    GraphVisualizerUI ui;

    public AboutWindow (GraphVisualizerUI ui){
        this.ui = ui;
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

    public void centerThisText(JLabel label){

        label.setForeground(AppTheme.FG_COLOR);
        label.setAlignmentX(CENTER_ALIGNMENT);
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void initLayout() {
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.getContentPane().setBackground(AppTheme.BG_COLOR); 

        add(Box.createVerticalStrut(20));

        add(createImageIcon());

        add(Box.createVerticalStrut(20));

        JLabel textLabel = new JLabel("Wizualizator Grafów v1.0");
        centerThisText(textLabel);
        add(textLabel);

        JLabel authors = new JLabel("<html><b>Autorzy:</b>Anastasiya Kryvetskaya, Aleksander Jóźwik</html>");
        centerThisText(authors);
        add(authors);

        JLabel hyperlink = new JLabel("<html><u><b>Repozytorium projektu na GitHub</b></u></html>");
        hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        hyperlink.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                        Desktop.getDesktop().browse(new URI("https://github.com/0-Cuprum-0/jimp2-wizualizacja-grafu-java"));
                    } else {
                        System.err.println("Otwieranie przeglądarki nie jest wspierane na tym urządzeniu.");
                        ui.showErrorMessage("Otwieranie przeglądarki nie jest wspierane na tym urządzeniu.");
                    }
                } catch (IOException | URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }

        });
        centerThisText(hyperlink);
        add(hyperlink);


        add(Box.createVerticalGlue());
        JButton closeBtn = new JButton("Zamknij");
        closeBtn.setAlignmentX(CENTER_ALIGNMENT);
        closeBtn.addActionListener(e -> this.dispose());
        add(closeBtn);
        add(Box.createVerticalStrut(20));
    }

    public Component createImageIcon(){
        ImageIcon icon = new ImageIcon("src/images/graph_logo.png");

        Image image = icon.getImage(); // transform it
        Image newimg = image.getScaledInstance(120, 120,  java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        icon = new ImageIcon(newimg);  // transform it back


        JLabel picLabel = new JLabel(icon);
        picLabel.setAlignmentX(CENTER_ALIGNMENT);
        return picLabel;


    }







}
