import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class StatusBar extends JLabel{
    public StatusBar() {
        super(" Info: Wczytano plik \"graf.txt\"");
        setForeground(AppTheme.FG_COLOR);
        setOpaque(true);
        setBackground(AppTheme.BG_COLOR);
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, AppTheme.FG_COLOR));
        setPreferredSize(new Dimension(getWidth(), 25));
    }
}
