import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppTheme.setupGlobalFont();
            GraphVisualizerUI window = new GraphVisualizerUI();
            window.setVisible(true);
        });
    }
}

//GraphVisualizerUI.setLookAndFeel(GraphVisualizerUI.getCrossPlatformLookAndFeelClassName());
//why not try this,huh?