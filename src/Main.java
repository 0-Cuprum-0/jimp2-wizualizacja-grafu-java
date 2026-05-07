import javax.swing.*;
public class Main {
    public static void main(String[] args) {
        BackEnd engine = new BackEnd();
        SwingUtilities.invokeLater(() -> {
            AppTheme.setupGlobalFont();
            GraphVisualizerUI window = new GraphVisualizerUI(engine);
            window.setVisible(true);

        });
    }
}

//GraphVisualizerUI.setLookAndFeel(GraphVisualizerUI.getCrossPlatformLookAndFeelClassName());
//why not try this,huh?