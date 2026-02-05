import models.Line;
import models.Point;
import rasterizers.Rasterizer;
import rasterizers.TrivialRasterizer;
import rasters.Raster;
import rasters.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;

public class App {

    private final JPanel panel;
    private final JFrame frame;
    private final JPanel transparentPanel;
    private final Raster raster;
    private final Raster rasterPreview;
    private final Rasterizer rasterizer;
    private final Rasterizer previewRasterizer;
    private MouseAdapter mouseAdapter;
    private KeyAdapter keyboardAdapter;
    private ArrayList<Line> canvasEntities;
    private boolean isControlDown;
    private boolean isShiftDown;

    private Point tempPointA, tempPointB;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App(1000, 600).start());
    }

    public void clear(int color) {
        raster.setClearColor(color);
        raster.clear();
        rasterPreview.setClearColor(color);
        rasterPreview.clear();
    }

    public void present(Graphics graphics, Raster mesh) {
        mesh.repaint(graphics);

    }

    public void start() {
        clear(Color.CYAN.getRGB());
        panel.repaint();
    }

    public App(int width, int height) {

        canvasEntities = new ArrayList<>();
        frame = new JFrame();

        frame.setLayout(new BorderLayout());

        frame.setBackground(Color.DARK_GRAY);

        frame.setTitle("Delta : " + this.getClass().getName());
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        rasterPreview = new RasterBufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        panel = new JPanel() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g, raster);
            }
        };
        transparentPanel = new JPanel() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g, rasterPreview);
            }
        };

        panel.setBounds(0, 0, width, height);
        transparentPanel.setBounds(0, 0, width, height);


        panel.setOpaque(true);
        transparentPanel.setOpaque(false);


        panel.setPreferredSize(new Dimension(width, height));
        transparentPanel.setPreferredSize(new Dimension(width, height));

        frame.add(transparentPanel, BorderLayout.CENTER);
        frame.add(panel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);

        panel.requestFocus();
        panel.requestFocusInWindow();

        panel.setVisible(true);
        transparentPanel.setVisible(false);


        rasterizer = new TrivialRasterizer( Color.CYAN, raster);
        previewRasterizer = new TrivialRasterizer( Color.CYAN, rasterPreview);

        createAdapters();


        transparentPanel.addKeyListener(keyboardAdapter);
        panel.addKeyListener(keyboardAdapter);

        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);
        transparentPanel.addMouseListener(mouseAdapter);
        transparentPanel.addMouseMotionListener(mouseAdapter);

    }

    int last_x = 0;
    int last_y = 0;

    public Line drawLine(Point startPoint, Point endPoint, Color color){
        return new Line(startPoint, endPoint, color, 1, isControlDown?8:0, 8, isShiftDown);
    }

    public void refreshPreviewLine(){
        rasterPreview.clear();

        if (tempPointA != null || tempPointB != null) {
            Line line = drawLine(tempPointA, tempPointB, Color.ORANGE);
            previewRasterizer.rasterize(line);
            transparentPanel.repaint();
        }
    }

    public void createAdapters(){
        keyboardAdapter = new KeyAdapter(){

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_CONTROL: {
                        System.out.println("Control pressed");

                        isControlDown = true;
                        refreshPreviewLine();

                    }
                    break;
                    case KeyEvent.VK_SHIFT: {
                        System.out.println("Shift pressed");

                        isShiftDown = true;

                        refreshPreviewLine();
                    }
                    break;
                    case KeyEvent.VK_C: {

                        raster.clear();

                        panel.repaint();
                    }
                    break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_CONTROL: {
                        System.out.println("Control released");
                        isControlDown = false;

                        refreshPreviewLine();
                    }
                    break;
                    case KeyEvent.VK_SHIFT: {
                        System.out.println("Shift released");
                        isShiftDown = false;

                        refreshPreviewLine();
                    }
                    break;
                }
            }

        };

        mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                rasterPreview.clear();

                int x = e.getX();
                int y = e.getY();

                tempPointB = new Point(x, y);

                Line line = drawLine(tempPointA, tempPointB, Color.ORANGE);

                previewRasterizer.rasterize(line);

                transparentPanel.repaint();

            }

            @Override
            public void mouseReleased(MouseEvent e) {

                panel.requestFocus();
                panel.requestFocusInWindow();

                transparentPanel.setVisible(false);

                frame.add(panel, BorderLayout.CENTER);

                tempPointB = new Point(e.getX(), e.getY());

                Line line = drawLine(tempPointA, tempPointB, Color.CYAN);

                rasterizer.rasterize(line);

                canvasEntities.add(line);

                rasterPreview.clear();

                transparentPanel.repaint();
                panel.repaint();


            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("Pressed");

                transparentPanel.setVisible(true);
                transparentPanel.repaint();

                frame.repaint();

                transparentPanel.requestFocus();
                transparentPanel.requestFocusInWindow();

                last_x = e.getX();
                last_y = e.getY();
                tempPointA = new Point(e.getX(), e.getY());

            }
        };
    }

}
