package com.example.mst.bonus;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;  // ADD THIS IMPORT
import java.util.*;
import java.util.List;
import com.example.mst.*;
public class GraphVisualizer extends JPanel {
    private Graph graph;
    private MSTResult mstResult;
    private Map<String, Point2D> positions;
    private String title;

    public GraphVisualizer(Graph graph, MSTResult mstResult, String title) {
        this.graph = graph;
        this.mstResult = mstResult;
        this.title = title;
        this.positions = calculatePositions();
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.WHITE);
    }

    private Map<String, Point2D> calculatePositions() {
        Map<String, Point2D> pos = new HashMap<>();
        List<String> vertices = graph.getVertices();
        int n = vertices.size();

        double centerX = 400;
        double centerY = 300;
        double radius = Math.min(centerX, centerY) - 80;

        for (int i = 0; i < n; i++) {
            double angle = 2 * Math.PI * i / n - Math.PI / 2;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            pos.put(vertices.get(i), new Point2D.Double(x, y));
        }

        return pos;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.setColor(Color.BLACK);
        g2.drawString(title, 20, 30);

        g2.setFont(new Font("Arial", Font.PLAIN, 14));
        g2.drawString(String.format("Vertices: %d | Edges: %d | MST Cost: %d",
                graph.getVertices().size(),
                graph.getEdges().size(),
                mstResult.getTotalCost()), 20, 55);

        Set<Edge> mstEdges = new HashSet<>(mstResult.getMstEdges());

        g2.setStroke(new BasicStroke(2));
        for (Edge e : graph.getEdges()) {
            Point2D p1 = positions.get(e.getU());
            Point2D p2 = positions.get(e.getV());

            if (mstEdges.contains(e)) {
                g2.setColor(new Color(220, 20, 60)); // Crimson for MST
                g2.setStroke(new BasicStroke(3));
            } else {
                g2.setColor(new Color(200, 200, 200)); // Light gray
                g2.setStroke(new BasicStroke(1));
            }

            g2.draw(new Line2D.Double(p1, p2));

            double midX = (p1.getX() + p2.getX()) / 2;
            double midY = (p1.getY() + p2.getY()) / 2;
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            g2.drawString(String.valueOf(e.getWeight()),
                    (float)midX - 10, (float)midY - 5);
        }

        for (String vertex : graph.getVertices()) {
            Point2D p = positions.get(vertex);

            g2.setColor(new Color(70, 130, 180)); // Steel blue
            g2.fill(new Ellipse2D.Double(p.getX() - 20, p.getY() - 20, 40, 40));

            g2.setColor(new Color(25, 25, 112)); // Midnight blue
            g2.setStroke(new BasicStroke(2));
            g2.draw(new Ellipse2D.Double(p.getX() - 20, p.getY() - 20, 40, 40));

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            FontMetrics fm = g2.getFontMetrics();
            int labelWidth = fm.stringWidth(vertex);
            g2.drawString(vertex,
                    (float)(p.getX() - labelWidth/2),
                    (float)(p.getY() + 5));
        }

        drawLegend(g2);
    }

    private void drawLegend(Graphics2D g2) {
        int x = 20;
        int y = getHeight() - 80;

        g2.setColor(Color.WHITE);
        g2.fillRect(x - 5, y - 20, 200, 70);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(1));
        g2.drawRect(x - 5, y - 20, 200, 70);

        g2.setColor(new Color(220, 20, 60));
        g2.setStroke(new BasicStroke(3));
        g2.drawLine(x, y, x + 40, y);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.PLAIN, 12));
        g2.drawString("MST Edge", x + 50, y + 5);

        y += 25;
        g2.setColor(new Color(200, 200, 200));
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(x, y, x + 40, y);
        g2.setColor(Color.BLACK);
        g2.drawString("Non-MST Edge", x + 50, y + 5);
    }

    public static void visualize(Graph graph, MSTResult prim, MSTResult kruskal) {
        JFrame frame = new JFrame("MST Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(1, 2));

        frame.add(new GraphVisualizer(graph, prim,
                "Prim's MST (Cost: " + prim.getTotalCost() + ")"));
        frame.add(new GraphVisualizer(graph, kruskal,
                "Kruskal's MST (Cost: " + kruskal.getTotalCost() + ")"));

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void saveImage(String filename) {
        int width = 800;
        int height = 600;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();

        setSize(width, height);

        paint(g2);
        g2.dispose();

        try {
            java.io.File output = new java.io.File(filename);
            javax.imageio.ImageIO.write(image, "PNG", output);
            System.out.println("✓ Saved: " + filename);
        } catch (java.io.IOException e) {
            System.err.println("✗ Failed to save: " + filename);
            e.printStackTrace();
        }
    }
}