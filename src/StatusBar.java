import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class StatusBar extends JLabel{
    public StatusBar() {
        super(" Info: Wczytano plik \"graf.txt\"");
        setForeground(AppTheme.FG_COLOR);
        setOpaque(true);
        setBackground(new Color(20, 20, 30));
        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
        setPreferredSize(new Dimension(getWidth(), 25));
    }
}
