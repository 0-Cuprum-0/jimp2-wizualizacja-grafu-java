import java.awt.Color;

public class VisualizationSettings {
    public boolean showWeights;
    public boolean showLabels;

    public Color edgeColor;
    public Color bgColor;
    public Color vertexColor;
    public Color vertexTextColor;

    public VisualizationSettings(){
        this.showWeights = true;
        this.showLabels = true;
        this.edgeColor = AppTheme.FG_COLOR;
        this.bgColor = AppTheme.BG_COLOR;
        this.vertexColor = AppTheme.ACCENT_COLOR;
        this.vertexTextColor = AppTheme.FG_COLOR;
    }
}
