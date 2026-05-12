import java.awt.Color;
import java.awt.Font;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

public class AppTheme {
    public static final Color BG_COLOR = new Color(220, 224, 232);
    public static final Color FG_COLOR = new Color(0, 0, 0);
    public static final Color ACCENT_COLOR = new Color(64, 160, 43);
    public static final Color VERTEX_DRAGGED_COLOR = new Color(202, 158, 230);

    public static final Font SECTION_FONT = new Font("Roboto", Font.BOLD, 16);
    public static final Font PLAIN_FONT = new Font("Roboto", Font.PLAIN, 13);

    public static void setupGlobalFont() {
        FontUIResource fontResource = new FontUIResource(PLAIN_FONT);
        java.util.Enumeration<Object> keys = UIManager.getDefaults().keys();
        
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            // Jeśli domyślną wartością jest czcionka, podmień ją na naszą
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontResource);
            }
        }
    }
}