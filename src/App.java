import models.*;
import models.CanvasEntities.Polygon;
import models.InterfaceEntities.*;
import models.CanvasEntities.Line;
import models.InterfaceEntities.Button;
import models.InterfaceEntities.MenuBar;
import models.CanvasEntities.Point;
import rasterizers.InterfaceRasterizer;
import rasterizers.LineRasterizer;
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

import java.util.function.Consumer;

public class App {

    private final JPanel panel;
    private final JFrame frame;
    private final JPanel transparentPanel;
    private final Raster raster;
    private final Raster rasterPreview;
    private final LineRasterizer rasterizer;
    private final LineRasterizer previewRasterizer;
    private final InterfaceRasterizer interfaceRasterizer;
    private MouseAdapter mouseAdapter;
    private KeyAdapter keyboardAdapter;
    private ArrayList<Line> canvasEntities;

    private MenuBar modeBar;
    private MenuBar toolBar;
    private Root rootElement;
    private DrawingMode drawingMode = DrawingMode.None;
    private EditMode editMode = EditMode.Normal;
    private Point pointA, tempPointB, pointB = null;

    private int windowWidth;
    private int windowHeight;


    // **** Line specs
    private boolean isControlDown;
    private boolean isShiftDown;
    private int lineStep = 8;
    private int getLineSpace(){
        return isControlDown?8:0;
    }
    private int lineWidth = 8;


    private Polygon polygon;

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

        windowWidth = width;
        windowHeight = height;

        rootElement = new Root(windowWidth, windowHeight, new BoundingDimensions(1), new ColorSet(null, null, null), new BoundingDimensions(1));

        WindowInterfaceMap.map = new Element[height][width];

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
        interfaceRasterizer = new InterfaceRasterizer(raster);

        CreateMenu();

        DrawInterface();

        panel.repaint();

        createAdapters();


        transparentPanel.addKeyListener(keyboardAdapter);
        panel.addKeyListener(keyboardAdapter);

        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);
        transparentPanel.addMouseListener(mouseAdapter);
        transparentPanel.addMouseMotionListener(mouseAdapter);

    }

    private void CreateMenu(){
        ColorSet MenuColorSet = new ColorSet(new Color(0,100,117), new Color(13,53,63), null);

        modeBar = new MenuBar(windowWidth, 0, 10, new BoundingDimensions(5), new BoundingDimensions(10, 10, 10, 10), MenuColorSet);
        toolBar = new MenuBar(windowWidth, 0, 10, new BoundingDimensions(2), new BoundingDimensions(10, 2), MenuColorSet);

        ColorSet polygonBtnColors = new ColorSet(new Color(246,245,199), new Color(1,121,121) , new Color(245,125,108));
        ColorSet polygonBtnHoverColors = new ColorSet(polygonBtnColors.getBACKGROUND(), Color.MAGENTA, polygonBtnColors.getFOREGROUND());
        BoundingDimensions polygonBtnBorders = new BoundingDimensions(1, 1, 1, 1);
        BoundingDimensions polygonBtnPadding = new BoundingDimensions(10);

        Icons icons = new Icons();


        Button polygonBtn = new Button(icons.getIconData(icons.polygonIcon), polygonBtnPadding, polygonBtnColors, polygonBtnHoverColors, polygonBtnBorders, new Coordinates(0, 0));
        Button confirm = new Button(icons.getIconData(icons.tickIcon), polygonBtnPadding, polygonBtnColors, polygonBtnHoverColors, polygonBtnBorders, new Coordinates(0, 0));
        Button cancel = new Button(icons.getIconData(icons.closeIcon), polygonBtnPadding, polygonBtnColors, polygonBtnHoverColors, polygonBtnBorders, new Coordinates(0, 0));
        Button lineBtn = new Button(icons.getIconData(icons.lineIcon), polygonBtnPadding, polygonBtnColors, polygonBtnHoverColors, polygonBtnBorders, new Coordinates(0, 0));
        Button normalTool = new Button(icons.getIconData(icons.cursorFullIcon), new BoundingDimensions(2), polygonBtnColors, polygonBtnHoverColors, new BoundingDimensions(1),  new Coordinates(0, 0));
        Button vertexTool = new Button(icons.getIconData(icons.cursorHollowIcon), new BoundingDimensions(2), polygonBtnColors, polygonBtnHoverColors, new BoundingDimensions(1),  new Coordinates(0, 0));


        Consumer<Element> normalToolAction = (Element button) ->{
            switchEditMode(button, EditMode.Normal, true);
            // Not functional, just to switch the color state.
            // Can be deleted, when a None state will be added to the EditMode status options
            if(button instanceof ReactiveElement reactiveElement && !reactiveElement.isActive()) {
                reactiveElement.toggleColorState();
            }
        };



        Consumer<Element> lineBtnAction = (Element button) ->{
            switchDrawingMode(button, DrawingMode.Line, true);
            if(editMode != EditMode.Normal) {
                toolBar.disableAll();
                editMode = EditMode.Normal;
                normalTool.toggleColorState();
                DrawInterface();
            }
        };



        Consumer<Element> cancelPolygonDrawing = (Element btn) -> {
            modeBar.clear();
            rasterPreview.clear();
            transparentPanel.repaint();
            polygon = null;
            modeBar.addElement(lineBtn);
            modeBar.addElement(polygonBtn);
            drawingMode = DrawingMode.None;
            DrawInterface();
            DrawCanvas();
        };

        Consumer<Element> vertexToolAction = (Element button) ->{
            switchEditMode(button, EditMode.Vertex, true);
            // Not functional, just to switch the color state.
            // Can be deleted, when a None state will be added to the EditMode status options
            if(editMode == EditMode.Normal) {
                normalTool.toggleColorState();
            }

            // You can't use vertex tool with any drawing mode.
            modeBar.disableAll();
            drawingMode = DrawingMode.None;
            cancelPolygonDrawing.accept(button);

//            DrawInterface();
            DrawCanvas();
        };

        Consumer<Element> confirmButtonAction = (Element button) -> {
            modeBar.clear();
            // No points were drawn, thus no action is needed.
            if(polygon != null) {
                pointB = polygon.getPoints().getFirst();
                DrawCanvas();
            }

            panel.repaint();
            polygon = null;
            modeBar.addElement(lineBtn);
            modeBar.addElement(polygonBtn);
            drawingMode = DrawingMode.None;
            DrawInterface();

        };


        Consumer<Element> polygonBtnAction = (Element button) -> {
            switchDrawingMode(button, DrawingMode.Polygon, false);

            if(editMode != EditMode.Normal) {
                toolBar.disableAll();
                editMode = EditMode.Normal;
                normalTool.toggleColorState();
                DrawInterface();
            }

            modeBar.clear();

            modeBar.addElement(confirm);
            modeBar.addElement(cancel);

            DrawInterface();
        };








        modeBar.addElement(lineBtn);
        modeBar.addElement(polygonBtn);
        rootElement.addElement(modeBar);

        rootElement.addElement(toolBar);
        toolBar.addElement(normalTool);
        toolBar.addElement(vertexTool);

        polygonBtn.setButtonConsumer(polygonBtnAction);
        confirm.setButtonConsumer(confirmButtonAction);
        cancel.setButtonConsumer(cancelPolygonDrawing);
        lineBtn.setButtonConsumer(lineBtnAction);

        normalTool.setButtonConsumer(normalToolAction);
        vertexTool.setButtonConsumer(vertexToolAction);




        DrawInterface();
        DrawCanvas();
    }

    // Designed basically just to turn all currently active siblings off, except for the initializer,
    // whose state wil be negated, and optionally activate the initializer
    public void radio(Element button, boolean toggle){
        ArrayList<Element> siblings = button.getParent().getChildren();

        for(Element sibling : siblings) {

            if(sibling instanceof ReactiveElement reactiveElement) {

                if(reactiveElement.equals(button)) {
                    if(toggle || reactiveElement.isActive()) {
                        reactiveElement.toggleColorState();
                    }

                }else if(reactiveElement.isActive()){

                    reactiveElement.toggleColorState();
                }
            }
        }

    }


    public <T extends Enum<T>> T switchMode(Element button, T currentValue, T desiredValue, T defaultValue, boolean toggle){
        T newMode = defaultValue;

        radio(button, toggle);

        if(button instanceof ReactiveElement reactiveElement) {
            if(reactiveElement.isActive() || !toggle) {
                newMode = desiredValue;
            }
        }else{
            newMode = currentValue;
        }

        return newMode;
    }

    public void switchDrawingMode(Element button, DrawingMode desiredValue, boolean toggle){
        drawingMode = switchMode(button, drawingMode, desiredValue, DrawingMode.None, toggle);
        System.out.println("Switched drawing mode:" + drawingMode);
    }

    public void switchEditMode(Element button, EditMode desiredValue, boolean toggle){
        editMode = switchMode(button, editMode, desiredValue, EditMode.Normal, toggle);
        System.out.println("Switched edit mode:" + editMode);
    }

    public Line SnapPoints(Line line){

        Point a = new Point(line.getPointA());
        Point b = new Point(line.getPointB());

        int diffX = b.getX() - a.getX();
        int diffY = b.getY() - a.getY();

        double k = (diffX != 0) ? (double) diffY / diffX : (diffY > 0 ? 1e6 : -1e6);
        double factor = Math.abs(k);


        int ax = a.getX();
        int bx = b.getX();
        int ay = a.getY();
        int by = b.getY();

        int bX = bx, bY = by, aX, aY;

        if(line.isSnapping()) {


            if (factor < 0.5 && factor > 0) {
                // Snap to X axis
                k = 0;
            } else if (factor <= 1.5 && factor >= 1) {
                // Halve it
                k = (k > 0 ? 1 : -1);
            } else if (factor > 0.5 && factor <= 1) {
                // Halve it
                k = (k > 0 ? 1 : -1);
            } else if (factor > 1.5) {
                // Snap to Y axis
                bX = ax;
                return (new Line(new Point(ax, ay), new Point(bX, bY), line.getColor(), line.getWidth(), line.getSpace(), line.getStep(), line.isSnapping()));
            }
        }


        double q = a.getY() - a.getX() * k;

        if(Math.abs(k) == 1e6 || Math.abs(k) > raster.getHeight()) {
            return  (new Line(new Point(ax, ay), new Point(bX, bY), line.getColor(), line.getWidth(), line.getSpace(), line.getStep(), line.isSnapping()));
        }

        bX = (int) Math.round(((double) by - q) / k);

        bY = (int) Math.round(((k) * (double) bX + q));

        if(k == 0){
            bX = bx;
            bY = ay;
        }

        return  (new Line(new Point(ax, ay), new Point(bX, bY), line.getColor(), line.getWidth(), line.getSpace(), line.getStep(), line.isSnapping()));
    }

    public Line drawLine(Point a, Point b, Color color, boolean allowSnapping){

        boolean hasSnapping = allowSnapping&&isShiftDown;

        return new Line(a, b, color, lineWidth, getLineSpace(), lineStep, hasSnapping);
    }

    public void refreshPreviewLine(){



        if (pointA != null && tempPointB != null) {
            Line line = drawLine(pointA, tempPointB, Color.ORANGE, true);
            rasterPreview.clear();
            previewRasterizer.rasterize(line);

            transparentPanel.repaint();

        }
        DrawCanvas();

    }

    public void DrawInterface(){
        interfaceRasterizer.rasterize(rootElement);
        panel.repaint();
    }

    public void DrawCanvas() {

        Line line;


        switch(drawingMode) {
            case None:
                break;
            case Line:

                if (pointB == null && tempPointB != null && pointA != null) {
                    rasterPreview.clear();
                    line = SnapPoints(drawLine(pointA, tempPointB, Color.CYAN, true));
                    previewRasterizer.rasterize(line);
                }

                if (pointB != null && tempPointB != null) {

                    line = SnapPoints(drawLine(pointA, pointB, Color.ORANGE, true));

                    rasterPreview.clear();
                    rasterizer.rasterize(line);

                }

                if (pointB != null && pointA != null) {
                    panel.requestFocus();
                    panel.requestFocusInWindow();

                    transparentPanel.setVisible(false);

                    frame.add(panel, BorderLayout.CENTER);

                    line = SnapPoints(drawLine(pointA, pointB, Color.ORANGE, true));

                    rasterPreview.clear();
                    rasterizer.rasterize(line);


                }
                break;
            case Polygon:

                if (pointB == null ) {
                    if (this.polygon != null && polygon.getPoints().size() > 1 && tempPointB != null ) {
                        rasterPreview.clear();

                        switchTemporaryPanel();

                        line = SnapPoints(drawLine(polygon.getPoints().getLast(), tempPointB, Color.GREEN, true));
                        pointA = polygon.getPoints().getLast();

                        tempPointB = line.getPointB();

                        previewRasterizer.rasterize(line);

                        previewRasterizer.rasterize(drawLine(tempPointB, polygon.getPoints().getFirst(), Color.MAGENTA, false));

                        for(Line polygonSegment : polygon.getLines()){
                            previewRasterizer.rasterize(polygonSegment);

                        }

                    }
                    break;
                }


                if (this.polygon != null) {
                    polygon.constructPoint(pointB, Color.CYAN, lineWidth, getLineSpace(), lineStep, false);
                    if(polygon.getPoints().getFirst().equals(pointB) ) {
                        for(Line polygonSegment : polygon.getLines()){
                            polygonSegment.setColor(Color.GREEN);
                            rasterizer.rasterize(polygonSegment);

                        }
                        rasterPreview.clear();
                        panel.repaint();
                        transparentPanel.repaint();

                        break;
                    }

                } else {
                    polygon = new Polygon(pointA);
                    polygon.constructPoint(pointB, Color.CYAN, lineWidth, getLineSpace(), lineStep, false);
                }
                break;
        }




    }


    public void createAdapters(){
        keyboardAdapter = new KeyAdapter(){

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_CONTROL: {
                        isControlDown = true;

                        if(tempPointB != null) {
                            refreshPreviewLine();
                        }

                    }
                    break;
                    case KeyEvent.VK_SHIFT: {
                        isShiftDown = true;

                        if(tempPointB != null) {
                            refreshPreviewLine();
                        }

                    }
                    break;
                    case KeyEvent.VK_C: {
                        pointA = null;
                        pointB = null;
                        tempPointB = null;
                        raster.clear();
                        rasterPreview.clear();

                        DrawInterface();

                        transparentPanel.repaint();
                        panel.repaint();
                    }
                    break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch(e.getKeyCode()){
                    case KeyEvent.VK_CONTROL: {
                        isControlDown = false;

                        if(tempPointB != null) {
                            refreshPreviewLine();
                        }
                    }
                    break;
                    case KeyEvent.VK_SHIFT: {
                        isShiftDown = false;

                        if(tempPointB != null) {
                            refreshPreviewLine();
                        }
                    }
                    break;
                }
            }

        };

        mouseAdapter = new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent e) {
//                System.out.println("A : " + (pointA != null) + " |  B : " + (pointB != null) + " | tmpB : " + (tempPointB != null) + " # After");
                switchTemporaryPanel();

                tempPointB = new Point(e.getX(), e.getY());
                DrawCanvas();
                transparentPanel.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {


                int x = e.getX();
                int y = e.getY();

                tempPointB = new Point(x, y);

                DrawCanvas();



                transparentPanel.repaint();

            }

            @Override
            public void mouseReleased(MouseEvent e) {

                if(pointA == null){
                    return;
                }

                if(pointA.getY() == e.getY() && pointA.getX() == e.getX()){
                    Element element = WindowInterfaceMap.GetElement(e.getX(), e.getY());
                    if(element instanceof ActionElement){
                        pointA = null;
                        pointB = null;
                        tempPointB = null;

                        ((ActionElement)element).RunAction();
                    }
                }else{
                    if (pointB == null) {
                        pointB = SnapPoints(drawLine(pointA, new Point(e.getX(), e.getY()), Color.CYAN, true)).getPointB();
                    }
                }

                // Not necessary for the whole project, but essential to keep the line visible even when no origin point is set

                tempPointB = new Point(e.getX(), e.getY());
                DrawCanvas();
                DrawInterface();

                if(pointB != null) {
                    pointA = null;
                    pointB = null;
//                    tempPointB = null;
                }

                DrawCanvas();

                transparentPanel.repaint();
                panel.repaint();


            }

            @Override
            public void mousePressed(MouseEvent e) {


                DrawInterface();

                Element element = WindowInterfaceMap.GetElement(e.getX(), e.getY());
                if((pointA == null && drawingMode != DrawingMode.None )|| element instanceof ActionElement) {
                    pointA = new Point(e.getX(), e.getY());
                }


//                DrawCanvas();
                panel.repaint();


                switchTemporaryPanel();


            }
        };
    }
    public void switchTemporaryPanel(){
        transparentPanel.setVisible(true);
        transparentPanel.repaint();
        frame.repaint();

        transparentPanel.requestFocus();
        transparentPanel.requestFocusInWindow();
    }

}
