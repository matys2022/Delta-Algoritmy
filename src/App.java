import factories.CanvasEntityFactory;
import models.*;
import models.CanvasEntities.*;
import models.CanvasEntities.Point;
import models.CanvasEntities.Polygon;
import models.InterfaceEntities.*;
import models.InterfaceEntities.Button;
import models.InterfaceEntities.MenuBar;
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
    private ArrayList<CanvasEntity> canvasEntities;

    // ***** Factories
    CanvasEntityFactory canvasEntityFactory;

    // *****  Window content maps
    WindowCanvasMap canvasMap;


    private MenuBar modeBar;
    private MenuBar toolBar;
    private Root rootElement;
    private DrawingMode drawingMode = DrawingMode.None;
    private EditMode editMode = EditMode.Normal;
    private Point pointA, tempPointB, pointB = null;


    // ***** Used to modify canvas entities
    Point draggedPoint;
    Point originPoint;
    CanvasEntity draggedEntity;

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

    public void refreshRenderCanvas(){
        raster.clear();

        DrawInterface();
        for(CanvasEntity canvasEntity : canvasEntities){
            renderIntoRenderLayer(canvasEntity);
        };

        panel.repaint();
    }

    public void transferCanvasEntity(CanvasEntity canvasEntity, LineRasterizer lineRasterizer){
        if(canvasEntity instanceof Line line){
            lineRasterizer.rasterize(line);
        }

        if(canvasEntity instanceof Polygon canvasPolygon){
            for(Line line : canvasPolygon.getLines()){
                lineRasterizer.rasterize(line);
            }
        }
    }

    public void renderIntoPreviewLayer(CanvasEntity canvasEntity){
        transferCanvasEntity(canvasEntity, previewRasterizer);
    }

    public void renderIntoRenderLayer(CanvasEntity canvasEntity){
        transferCanvasEntity(canvasEntity, rasterizer);
    }

    public void transferIntoPreviewLayer(CanvasEntity canvasEntity){
        canvasEntities.remove(canvasEntity);
        renderIntoPreviewLayer(canvasEntity);
    }

    public void transferIntoRenderLayer(CanvasEntity canvasEntity){
        canvasEntities.add(canvasEntity);
        renderIntoRenderLayer(canvasEntity);
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
        canvasMap = new WindowCanvasMap(windowWidth, windowHeight);
        canvasEntities = new ArrayList<>();

        canvasEntityFactory = new CanvasEntityFactory(canvasMap);


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
        panel.setOpaque(true);

        transparentPanel.setBounds(0, 0, width, height);
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



        panel.addKeyListener(keyboardAdapter);
        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);
        transparentPanel.addKeyListener(keyboardAdapter);
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
            buttonSwitchEditMode(button, EditMode.Normal, true);
            // Not functional, just to switch the color state.
            // Can be deleted, when a None state will be added to the EditMode status options
            if(button instanceof ReactiveElement reactiveElement && !reactiveElement.isActive()) {
                reactiveElement.toggleColorState();
            }
        };



        Consumer<Element> lineBtnAction = (Element button) ->{
            buttonSwitchDrawingMode(button, DrawingMode.Line, true);
            if(editMode != EditMode.Normal) {
                toolBar.disableAll();
                switchEditMode(EditMode.Normal);
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
            buttonSwitchEditMode(button, EditMode.Vertex, true);
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
            buttonSwitchDrawingMode(button, DrawingMode.Polygon, false);

            if(editMode != EditMode.Normal) {
                toolBar.disableAll();
                switchEditMode(EditMode.Normal);
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

        buttonSwitchEditMode(normalTool, EditMode.Normal, true);



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

    public void buttonSwitchDrawingMode(Element button, DrawingMode desiredValue, boolean toggle){
        drawingMode = switchMode(button, drawingMode, desiredValue, DrawingMode.None, toggle);
        System.out.println("Switched drawing mode:" + drawingMode);
    }

    public void runOnEditModeSwitch(){
        System.out.println("Running on edit mode switch");
        if(editMode == EditMode.Vertex && draggedEntity != null && draggedPoint != null && originPoint != null) {
            draggedEntity.modifyPoint(draggedPoint, originPoint.getX(), originPoint.getY());
            transferIntoRenderLayer(draggedEntity);
            rasterPreview.clear();
            transparentPanel.repaint();
            originPoint = null;
            draggedEntity = null;
            draggedPoint = null;
            refreshRenderCanvas();
        }
    }

    public void buttonSwitchEditMode(Element button, EditMode desiredValue, boolean toggle){
        switchEditMode(switchMode(button, editMode, desiredValue, EditMode.Normal, toggle));

        System.out.println("Switched edit mode:" + editMode);
    }
    public void switchEditMode(EditMode newValue){
        runOnEditModeSwitch();
        editMode = newValue;
    }

    public Point ShiftSnapPoint(Point pointA, Point pointB){
        if(isShiftDown){
            return SnapPoint(pointA, pointB);
        }else{
            return pointB;
        }
    }

    public Point SnapPoint(Point pointA, Point pointB){
        int diffX = pointB.getX() - pointA.getX();
        int diffY = pointB.getY() - pointA.getY();

        double k = (diffX != 0) ? (double) diffY / diffX : (diffY > 0 ? 1e6 : -1e6);
        double factor = Math.abs(k);

        int ax = pointA.getX();
        int bx = pointB.getX();
        int ay = pointA.getY();
        int by = pointB.getY();

        int bX = bx, bY = by;

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
            return new Point(bX, bY);
        }



        double q = pointA.getY() - pointA.getX() * k;

        if(Math.abs(k) == 1e6 || Math.abs(k) > raster.getHeight()) {
            return new Point(bX, bY);
        }

        bX = (int) Math.round(((double) by - q) / k);

        bY = (int) Math.round(((k) * (double) bX + q));

        if(k == 0){
            bX = bx;
            bY = ay;
        }
        return new Point(bX, bY);
    }

    public Line SnapLine(Line line){

        if(!line.isSnapping())
        {
            return new Line(line);
        }
        Point a = new Point(line.getPointA());
        Point b = new Point(line.getPointB());
        Point newPoint = SnapPoint(a, b);

        return new Line(a, newPoint, line.getColor(), line.getWidth(), line.getSpace(), line.getStep(), line.isSnapping());
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

    private boolean checkPoint(int x, int y) {

        if (x >= 0 && x < windowWidth && y >= 0 && y < windowHeight) { // Check window boundaries
            CanvasEntity entity = canvasMap.peekCanvasEntityPoint(x, y);
            if (entity != null) {
                this.draggedPoint = entity.getPoint(x, y);
                this.draggedEntity = entity;
                originPoint = new Point(draggedPoint);
                pointA = entity.getClosestChild(draggedPoint);
                return true;
            }
        }
        return false;
    }

    public void DrawCanvas() {



        Line line;


        switch(drawingMode) {
            case None: {
                    switch(editMode) {
                        case Normal: {

                            break;
                        }
                        case Vertex: {

                            if(draggedPoint != null && pointA == null) {
                                draggedPoint = null;
                            }
//
                            if(draggedPoint == null && pointA != null) { // Set dragged point
//                                System.out.println("A : " + (pointA != null) + " |  B : " + (pointB != null) + " | dragged : " + (draggedPoint != null) + " # After");
                                //        System.out.println("Drawing mode :"  + drawingMode + " | Edit mode :"  + editMode);
//                                System.out.println("Setting");

                                int centerX = pointA.getX();
                                int centerY = pointA.getY();

                                int maxRadius = 15;

                                if (canvasMap.peekCanvasEntityPoint(centerX, centerY) != null) {
                                    draggedPoint = new Point(centerX, centerY);
                                }

                                // Spiral through the defined bounds
                                for (int r = 1; r <= maxRadius && draggedPoint == null; r++) {
                                    for (int x = centerX - r; x <= centerX + r; x++) {
                                        if (checkPoint(x, centerY - r)) break; // Top edge
                                        if (checkPoint(x, centerY + r)) break; // Bottom edge
                                    }
                                    if (draggedPoint != null) break;

                                    for (int y = centerY - r + 1; y <= centerY + r - 1; y++) {
                                        if (checkPoint(centerX - r, y)) break; // Left edge
                                        if (checkPoint(centerX + r, y)) break; // Right edge
                                    }
                                }

                                if(draggedPoint != null) {
                                    System.out.println("Got a point : X(" + draggedPoint.getX() + "), Y(" + draggedPoint.getY() + ")");
                                    transferIntoPreviewLayer(draggedEntity);


                                }else{
                                    pointA = null;
                                    System.out.println("No point has been found : NULL");
                                }

                                refreshRenderCanvas();

                            }

//                            System.out.println("dragged point: " +  draggedPoint + " point B: " + pointB + " origin point: " + originPoint + " dragged entity: " +  draggedEntity);
                            if(draggedPoint != null && pointB != null && originPoint != null && draggedEntity != null) {
//                                System.out.println("Moved " + draggedEntity.getClass().getSimpleName() + " : X(" + draggedPoint.getX() + "), Y(" + draggedPoint.getY() + ")");
                                canvasMap.popCanvasEntityPoint(originPoint.getX(), originPoint.getY());
                                canvasMap.addCanvasEntityPoint(pointB, draggedEntity);
                                this.draggedPoint = null;
                                this.originPoint = null;
                                transferIntoRenderLayer(draggedEntity);
                                refreshRenderCanvas();
                                this.draggedEntity = null;

                                break;
                            }

                            if (tempPointB != null && draggedPoint != null && draggedEntity != null) { //

                                draggedEntity.modifyPoint(draggedPoint, tempPointB.getX(), tempPointB.getY());
                                rasterPreview.clear();
                                renderIntoPreviewLayer(draggedEntity);
                                transparentPanel.repaint();

                            }

                            break;
                        }
                    }
                break;
                }
            case Line: {
                if (pointB == null && tempPointB != null && pointA != null) {
//                    System.out.println("Click – Move – (Release / Click) ");

                    rasterPreview.clear();
                    line = SnapLine(drawLine(pointA, tempPointB, Color.CYAN, true));
                    previewRasterizer.rasterize(line);
                }


                if (pointB != null && pointA != null) { // Click – Move – Click

//                    System.out.println("Click – Move – Click");

                    panel.requestFocus();
                    panel.requestFocusInWindow();

                    transparentPanel.setVisible(false);

                    frame.add(panel, BorderLayout.CENTER);

                    line = canvasEntityFactory.createFinalLine(SnapLine(drawLine(pointA, pointB, Color.ORANGE, true)));

                    rasterPreview.clear();
                    transferIntoRenderLayer(line);


                }
                break;
            }
            case Polygon: {
                if (pointB == null) {
                    if (this.polygon != null && polygon.getPoints().size() > 1 && tempPointB != null) {
                        rasterPreview.clear();

                        switchTemporaryPanel();

                        line = canvasEntityFactory.createPreviewLine(SnapLine(drawLine(polygon.getPoints().getLast(), tempPointB, Color.GREEN, true)));

                        pointA = polygon.getPoints().getLast();

                        tempPointB = line.getPointB();

                        previewRasterizer.rasterize(line);

                        previewRasterizer.rasterize(drawLine(tempPointB, polygon.getPoints().getFirst(), Color.MAGENTA, false));

                        for (Line polygonSegment : polygon.getLines()) {
                            previewRasterizer.rasterize(polygonSegment);

                        }

                    }
                    break;
                }


                if (this.polygon != null) {
                    polygon.constructPoint(pointB, Color.CYAN, lineWidth, getLineSpace(), lineStep, false);

                    if (polygon.getPoints().getFirst().equals(pointB)) {
                        Polygon registeredPolygon = canvasEntityFactory.createPolygon(polygon);
                        for (Line polygonSegment : registeredPolygon.getLines()) {
                            polygonSegment.setColor(Color.GREEN);

//                            Line registeredSegment = canvasEntityFactory.createPreviewLine(polygonSegment);
//
//                            rasterizer.rasterize(registeredSegment);
                        }
                        transferIntoRenderLayer(registeredPolygon);
                        rasterPreview.clear();
                        panel.repaint();
                        transparentPanel.repaint();


                        break;
                    }

                } else {
                    polygon = canvasEntityFactory.createPolygon(pointA);
                    polygon.constructPoint(pointB, Color.CYAN, lineWidth, getLineSpace(), lineStep, false);
                }
                break;
            }
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

                        canvasEntities = new ArrayList<>();

                        originPoint = null;
                        draggedPoint = null;
                        draggedEntity = null;

                        switchEditMode(EditMode.Normal);

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
                Point newPoint = new Point(e.getX(), e.getY());

                tempPointB = pointA == null ? newPoint : ShiftSnapPoint(pointA, newPoint);

                DrawCanvas();
                transparentPanel.repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {


//                System.out.println("A : " + (pointA != null) + " |  B : " + (pointB != null) + " | tmpB : " + (tempPointB != null) + " # After");

                int x = e.getX();
                int y = e.getY();

                tempPointB = ShiftSnapPoint(pointA, new Point(x, y));

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
                    }else{
//                        if (pointB == null) {
//                            pointB = ShiftSnapPoint(pointA, new  Point(e.getX(), e.getY()));
//                        }
                    }
                }else{
                    if (pointB == null) {
                        pointB = SnapLine(drawLine(pointA, new Point(e.getX(), e.getY()), Color.CYAN, true)).getPointB();
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
                if((pointA == null && (drawingMode != DrawingMode.None || editMode != EditMode.Normal) )|| element instanceof ActionElement) {
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
