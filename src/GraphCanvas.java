import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GraphCanvas extends JPanel {
    
    GraphVisualizerUI ui;

    // Parametry kamery
    private double zoomFactor = 1.0;
    private double offsetX = 0.0;
    private double offsetY = 0.0;

    // Zmienne do obsługi myszy
    private Point lastMousePos;
    private Vertex draggedVertex = null;
    
    private static final int BASE_VERTEX_RADIUS = 10;
    private double userScale = 1.0;

    public int baseVertexRadius = BASE_VERTEX_RADIUS;

    public GraphCanvas(GraphVisualizerUI ui) {
        this.ui = ui;
        setBackground(AppTheme.BG_COLOR);
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Inicjalizacja początkowa kamery
        offsetX = 400; 
        offsetY = 300;

        // Inicjalizacja nasłuchiwaczy myszy
        MouseAdapter mouseHandler = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastMousePos = e.getPoint();
                
                if (ui.engine.vertices != null) {
                    // Obliczamy aktualny promień na ekranie
                    int currentRadius = (int)Math.max(baseVertexRadius * userScale, 3);
                    
                    for (int i = ui.engine.vertices.size() - 1; i >= 0; i--) {
                        Vertex v = ui.engine.vertices.get(i);
                        
                        int screenX = (int)(v.x * zoomFactor + offsetX);
                        int screenY = (int)(v.y * zoomFactor + offsetY);
                        
                        // Sprawdzamy czy mysz znajduje się wewnątrz wierzchołka
                        if (Math.hypot(e.getX() - screenX, e.getY() - screenY) <= currentRadius) {
                            draggedVertex = v;
                            break;
                        }
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggedVertex = null;
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (draggedVertex != null) {
                    System.out.println("Before: " + draggedVertex.x + " "+ draggedVertex.y);
                    //draggedVertex.printCoordinates();
                    draggedVertex.x = (e.getX() - offsetX) / zoomFactor;
                    draggedVertex.y = (e.getY() - offsetY) / zoomFactor;
                        System.out.println("After: " + draggedVertex.x + " "+ draggedVertex.y);


                   // draggedVertex.printCoordinates();
                } else {
                    int dx = e.getX() - lastMousePos.x;
                    int dy = e.getY() - lastMousePos.y;
                    offsetX += dx;
                    offsetY += dy;
                }
                lastMousePos = e.getPoint();
                repaint();
            }

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                double zoomMultiplier = 1.1;
                double scaleChange = e.getWheelRotation() < 0 ? zoomMultiplier : 1.0 / zoomMultiplier;

                offsetX = e.getX() - (e.getX() - offsetX) * scaleChange;
                offsetY = e.getY() - (e.getY() - offsetY) * scaleChange;
                zoomFactor *= scaleChange;
                userScale *= scaleChange;

                repaint();
            }
        };

        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        addMouseWheelListener(mouseHandler);
    }

    // Metoda pozwalająca wycentrować widok
    public void resetCamera() {
        // Zabezpieczenie przed brakiem danych
        if (ui == null || ui.engine.vertices == null || ui.engine.vertices.isEmpty()) {
            return;
        }

        // Szukamy granic grafu
        double minX = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for (Vertex v : ui.engine.vertices) {
            if (v.x < minX) minX = v.x;
            if (v.x > maxX) maxX = v.x;
            if (v.y < minY) minY = v.y;
            if (v.y > maxY) maxY = v.y;
        }

        // Obliczamy wymiary wczytanego grafu
        double worldWidth = maxX - minX;
        double worldHeight = maxY - minY;

        // Minimalna wielkość obszaru
        if (worldWidth == 0) worldWidth = 10.0;
        if (worldHeight == 0) worldHeight = 10.0;

        // Obliczamy środek wczytanego grafu
        double worldCenterX = minX + (worldWidth / 2.0);
        double worldCenterY = minY + (worldHeight / 2.0);

        double padding = 0.8;
        int panelWidth = getWidth() > 0 ? getWidth() : 800;
        int panelHeight = getHeight() > 0 ? getHeight() : 600;

        double scaleX = panelWidth / worldWidth;
        double scaleY = panelHeight / worldHeight;
        zoomFactor = Math.min(scaleX, scaleY) * padding;

        offsetX = (panelWidth / 2.0) - (worldCenterX * zoomFactor);
        offsetY = (panelHeight / 2.0) - (worldCenterY * zoomFactor);

        userScale = 1.0;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(ui.settings.bgColor);
        
        if (ui == null || ui.engine.vertices == null || ui.engine.edges == null) {
            return; 
        }

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int currentRadius = (int) Math.max(baseVertexRadius * userScale, 3);
        
        // Skalowanie grubości krawędzi wraz z przybliżeniem
        float currentStroke = (float) Math.max(0.5f * userScale, 1.0f);
        g2d.setStroke(new BasicStroke(currentStroke));

        int edgeFontSize = (int) Math.max(10 * userScale, 8);
        g2d.setFont(new Font("SansSerif", Font.PLAIN, edgeFontSize));
        FontMetrics edgeMetrics = g2d.getFontMetrics();

        // Szybkie wyszukiwanie wierzchołków
        java.util.Map<Integer, Vertex> vertexMap = new java.util.HashMap<>();
        for (Vertex v : ui.engine.vertices) {
            vertexMap.put(v.id, v);
        }

        for (Edge e : ui.engine.edges) {
            Vertex u = vertexMap.get(e.u);
            Vertex v = vertexMap.get(e.v);
            
            if (u != null && v != null) {
                int x1 = (int)(u.x * zoomFactor + offsetX);
                int y1 = (int)(u.y * zoomFactor + offsetY);
                int x2 = (int)(v.x * zoomFactor + offsetX);
                int y2 = (int)(v.y * zoomFactor + offsetY);
                
                g2d.setColor(ui.settings.edgeColor);
                g2d.drawLine(x1, y1, x2, y2);

                if (ui.settings.showLabels || ui.settings.showWeights) {
                    // Budujemy tekst do wyświetlenia w zależności od włączonych opcji
                    StringBuilder edgeText = new StringBuilder();
                    if (ui.settings.showLabels && e.name != null) {
                        edgeText.append(e.name);
                    }
                    if (ui.settings.showWeights && e.weight != null) {
                        if (edgeText.length() > 0) edgeText.append(" (");
                        edgeText.append(e.weight);
                        if (ui.settings.showLabels) edgeText.append(")");
                    }

                    if (edgeText.length() > 0) {
                        String text = edgeText.toString();
                        int textW = edgeMetrics.stringWidth(text);
                        int textH = edgeMetrics.getAscent() - edgeMetrics.getDescent();

                        // Obliczamy środek linii
                        int midX = (x1 + x2) / 2;
                        int midY = (y1 + y2) / 2;

                        // Rysujemy małe tło, żeby linia nie przekreślała tekstu
                        g2d.setColor(ui.settings.bgColor);
                        g2d.fillRect(midX - (textW / 2) - 2, midY - (textH / 2) - 2, textW + 4, textH + 4);

                        g2d.setColor(ui.settings.edgeColor);
                        g2d.drawString(text, midX - (textW / 2), midY + (textH / 2));
                    }
                }
            }
        }

        // Wierzchołki
        for (Vertex v : ui.engine.vertices) {
            int screenX = (int)(v.x * zoomFactor + offsetX);
            int screenY = (int)(v.y * zoomFactor + offsetY);
            
            // Zmiana koloru jeżeli wierzchołek jest przeciągany
            if (v == draggedVertex) {
                g2d.setColor(AppTheme.VERTEX_DRAGGED_COLOR);
            } else {
                g2d.setColor(ui.settings.vertexColor);
            }
            
            g2d.fillOval(screenX - currentRadius, screenY - currentRadius, currentRadius * 2, currentRadius * 2);
            
            g2d.setColor(ui.settings.bgColor);
            g2d.drawOval(screenX - currentRadius, screenY - currentRadius, currentRadius * 2, currentRadius * 2);

            g2d.setColor(ui.settings.vertexTextColor);
            
            String text = String.valueOf(v.id);
            FontMetrics metrics = g2d.getFontMetrics(); // Pobieramy wymiary aktualnie ustawionej czcionki

            // Szerokość tekstu
            int textWidth = metrics.stringWidth(text);
            
            // Wysokość tekstu
            int textHeight = metrics.getAscent() - metrics.getDescent();
            
            // Obliczamy dokładny punkt startu rysowania tekstu
            int textX = screenX - (textWidth / 2);
            int textY = screenY + (textHeight / 2);
            
            g2d.drawString(text, textX, textY);
        }

        g2d.dispose();
    }

    public void exportToPNG(File file) throws IOException {
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        Graphics2D g2d = image.createGraphics();
        
        this.paint(g2d); 
        
        g2d.dispose();

        // Zapisujemy plik
        if (!file.getName().toLowerCase().endsWith(".png")) {
            file = new File(file.getAbsolutePath() + ".png");
        }
        ImageIO.write(image, "png", file);
    }
}