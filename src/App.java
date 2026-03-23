//import factories.CanvasEntityFactory;
import models.*;
import models.CanvasEntities.*;
import models.CanvasEntities.Point;
import models.CanvasEntities.Polygon;
import models.CanvasEntities.Rectangle;
import models.InterfaceEntities.*;
import models.InterfaceEntities.Button;
import models.InterfaceEntities.MenuBar;
import rasterizers.CanvasRasterizer;
import rasterizers.FillRasterizer;
import rasterizers.InterfaceRasterizer;
import rasters.Raster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serial;
import java.util.*;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class App {



    private Polygon polygon;


    private JFrame frame;
    private JPanel mainPanel;
    private JPanel previewOverlayPanel;

    private final int windowWidth;
    private final int windowHeight;
//
    private final CanvasRasterizer canvasRasterizer;
    private final InterfaceRasterizer interfaceRasterizer;
//
//    private final CanvasEntityFactory entityFactory;
    private final WindowCanvasMap canvasMap;
//
    private final ArrayList<ComplexCanvasEntity> canvasEntities;

    // ────────────────────────────────────────────────
    //  UI Structure
    // ────────────────────────────────────────────────
    private Root rootElement;
    private MenuBar modeBar;
    private MenuBar toolBar;

    private WidthSelector widthSelector;
    private ColorSelector colorSelector;

    // ────────────────────────────────────────────────
    //  State – Drawing & Editing
    // ────────────────────────────────────────────────
    private DrawingMode drawingMode = DrawingMode.None;
    private EditMode editMode = EditMode.Normal;

    private Point pointA;           // start / anchor point
    private Point tempPointB;       // current mouse position (live preview)
    private Point pointB;           // end point (after second click)

    private Polygon currentPolygon;

    // Dragging / editing state
    private Point draggedPoint;
    private Point previousRelativePoint;
    private Point originPoint;
    private ComplexCanvasEntity draggedEntity;

    // Modifiers
    private boolean isCtrlPressed;
    private boolean isShiftPressed;
    private Color selectedColor = Color.RED;
    private int selectedWidth = 1;

    // Line appearance defaults
//    private final int lineWidth = 8;
    private final int lineStep = 8;

    private int getLineSpace() {
        return isCtrlPressed ? 8 : 0;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App(1000, 600));
    }

    public void refreshRenderCanvas(){
        canvasRasterizer.clearRenderRaster();
        DrawInterface();

        ArrayList<FillEntity> fillEntities = new ArrayList<>();
        for(ComplexCanvasEntity canvasEntity : canvasEntities){
            if(canvasEntity instanceof FillEntity fill){
                fillEntities.add(fill);
            }else{
                canvasRasterizer.rasterize(canvasEntity, false);
            }
        };

        for(FillEntity fillEntity : fillEntities){
            canvasRasterizer.rasterize(fillEntity, false);
        }

        mainPanel.repaint();
    }

    public void transferIntoPreviewLayer(ComplexCanvasEntity canvasEntity){
        canvasEntities.remove(canvasEntity);
        for (Point point : canvasEntity.getVisiblePoints()) {
            canvasMap.removeCanvasEntityPoint(canvasEntity, point.getX(), point.getY());
        }

        refreshRenderCanvas();
        canvasRasterizer.rasterize(canvasEntity, true);
    }

    public void transferIntoRenderLayer(ComplexCanvasEntity canvasEntity){
        canvasEntities.add(canvasEntity);

        if(canvasEntity instanceof FillEntity fillEntity){
//            System.out.println("Points to be filled (Debug) after: " + canvasMap.getFloodPoints(fillEntity.getFillReferencePoint()).size());
//            FillRasterizer fillRasterizer = new FillRasterizer();
//            fillRasterizer.rasterize(fillEntity, this.canvasRasterizer.renderRaster, this.canvasMap);

        }
        canvasRasterizer.rasterize(canvasEntity, false);

        if(canvasEntity instanceof FillEntity fillEntity){
//            System.out.println("Points to be filled (Debug) after: " + canvasMap.getFloodPoints(fillEntity.getFillReferencePoint()).size());
        }
    }

    public void registerRenderCanvasEntity(ComplexCanvasEntity canvasEntity){
        if(canvasEntity instanceof FillEntity fillEntity){
//            System.out.println("Points to be filled (Debug) before transfer: " + canvasMap.getFloodPoints(fillEntity.getFillReferencePoint()).size());
        }
        transferIntoRenderLayer(canvasEntity);



        for (Point point : canvasEntity.getVisiblePoints()) {
            canvasMap.addCanvasEntityPoint(point.getX(), point.getY(), canvasEntity);
        }
    }

    public void present(Graphics graphics, Raster mesh) {
        mesh.repaint(graphics);
    }

    public App(int width, int height) {

        windowWidth = width;
        windowHeight = height;

        rootElement = new Root(windowWidth, windowHeight, new BoundingDimensions(1), new ColorSet(null, null, null), new BoundingDimensions(1));

        WindowInterfaceMap.map = new Element[height][width];
        canvasMap = new WindowCanvasMap(windowWidth, windowHeight);
        canvasRasterizer = new CanvasRasterizer(windowWidth, windowHeight, canvasMap);
        canvasEntities = new ArrayList<>();

//        entityFactory = new CanvasEntityFactory(canvasMap);


        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setBackground(Color.DARK_GRAY);
        frame.setTitle("Delta : " + this.getClass().getName());
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//

//        raster = new RasterBufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
//        rasterPreview = new RasterBufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        mainPanel = new JPanel() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g, canvasRasterizer.getRenderRaster());
            }
        };
        previewOverlayPanel = new JPanel() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g, canvasRasterizer.getPreviewRaster());
            }
        };

//        initializeUI();
//
//        createInputListeners();
//
//        initializeInterface();

        frame.setVisible(true);
        mainPanel.requestFocusInWindow();

        mainPanel.setBounds(0, 0, width, height);
        mainPanel.setOpaque(true);

        previewOverlayPanel.setBounds(0, 0, width, height);
        previewOverlayPanel.setOpaque(false);


        mainPanel.setPreferredSize(new Dimension(width, height));
        previewOverlayPanel.setPreferredSize(new Dimension(width, height));

        frame.add(previewOverlayPanel, BorderLayout.CENTER);
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        mainPanel.requestFocus();
        mainPanel.requestFocusInWindow();
        mainPanel.setVisible(true);

        previewOverlayPanel.setVisible(false);





        interfaceRasterizer = new InterfaceRasterizer(canvasRasterizer.getRenderRaster());

        CreateMenu();

        DrawInterface();

        mainPanel.repaint();

        createInputListeners();



    }

    private void initializeUI() {
        frame = new JFrame("Delta – Canvas Editor");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setBackground(Color.DARK_GRAY);
        frame.setResizable(true);

        mainPanel = createPanel(canvasRasterizer.getRenderRaster());
        previewOverlayPanel = createPanel(canvasRasterizer.getPreviewRaster());
        previewOverlayPanel.setOpaque(false);

        frame.setLayout(new BorderLayout());
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(previewOverlayPanel, BorderLayout.CENTER);

        frame.pack();
    }

    private JPanel createPanel(Raster raster) {
        return new JPanel() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                raster.repaint(g);
            }
        };
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
        Button rectangleBtn = new Button(icons.getIconData(icons.rectangleIcon), polygonBtnPadding, polygonBtnColors, polygonBtnHoverColors, polygonBtnBorders, new Coordinates(0, 0));
        Button circleBtn = new Button(icons.getIconData(icons.circleIcon), polygonBtnPadding, polygonBtnColors, polygonBtnHoverColors, polygonBtnBorders, new Coordinates(0, 0));
        Button normalTool = new Button(icons.getIconData(icons.cursorFullIcon), new BoundingDimensions(2), polygonBtnColors, polygonBtnHoverColors, new BoundingDimensions(1),  new Coordinates(0, 0));
        Button vertexTool = new Button(icons.getIconData(icons.cursorHollowIcon), new BoundingDimensions(2), polygonBtnColors, polygonBtnHoverColors, new BoundingDimensions(1),  new Coordinates(0, 0));
        Button bucketTool = new Button(icons.getIconData(icons.bucketIcon), new BoundingDimensions(2), polygonBtnColors, polygonBtnHoverColors, new BoundingDimensions(1),  new Coordinates(0, 0));
        Button eraserTool = new Button(icons.getIconData(icons.eraserIcon), new BoundingDimensions(2), polygonBtnColors, polygonBtnHoverColors, new BoundingDimensions(1),  new Coordinates(0, 0));
        Button handTool = new Button(icons.getIconData(icons.handIcon), new BoundingDimensions(2), polygonBtnColors, polygonBtnHoverColors, new BoundingDimensions(1),  new Coordinates(0, 0));

        this.widthSelector = new WidthSelector(new BoundingDimensions(0), polygonBtnColors, modeBar);

        BiConsumer<ReactiveElement, Color> colorChangeAction = (ReactiveElement element, Color color)->{
            selectedColor = color;
            radio(element, true);
            System.out.println("Color change : " +  color.getClass().getName());
            refreshRenderCanvas();
        };

        this.colorSelector = new ColorSelector(polygonBtnPadding, polygonBtnColors, colorChangeAction, Color.RED, modeBar);


        Consumer<Element> lineBtnAction = (Element button) ->{
            buttonSwitchDrawingMode(button, DrawingMode.Line, true);
            if(editMode != EditMode.Normal) {
                toolBar.disableAll();
                switchEditMode(EditMode.Normal);
                normalTool.toggleColorState();
            }
            DrawInterface();
        };

        Consumer<Element> circleBtnAction = (Element button) ->{
            buttonSwitchDrawingMode(button, DrawingMode.Circle, true);
            if(editMode != EditMode.Normal) {
                toolBar.disableAll();
                switchEditMode(EditMode.Normal);
                normalTool.toggleColorState();
            }
            DrawInterface();
        };

        Consumer<Element> rectBtnAction = (Element button) ->{
            buttonSwitchDrawingMode(button, DrawingMode.Rectangle, true);
            if(editMode != EditMode.Normal) {
                toolBar.disableAll();
                switchEditMode(EditMode.Normal);
                normalTool.toggleColorState();
            }
            DrawInterface();
        };



        Consumer<Element> cancelPolygonDrawing = (Element btn) -> {
            modeBar.clear();
            canvasRasterizer.clearPreviewRaster();
            previewOverlayPanel.repaint();
            polygon = null;
            modeBar.addElement(lineBtn);
            modeBar.addElement(polygonBtn);
            modeBar.addElement(circleBtn);
            modeBar.addElement(rectangleBtn);

            modeBar.addElement(widthSelector);
            modeBar.addElement(colorSelector);

            drawingMode = DrawingMode.None;
            DrawInterface();
            DrawCanvas();
        };



        Consumer<Element> confirmButtonAction = (Element button) -> {
            modeBar.clear();
            // No points were drawn, thus no action is needed.
            if(polygon != null) {
                pointB = polygon.getPoints().getFirst();
                DrawCanvas();
            }

            mainPanel.repaint();
            cancelPolygonDrawing.accept(button);
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


        Consumer<Element> normalToolAction = (Element button) ->{
            buttonSwitchEditMode(button, EditMode.Normal, true);
            // Not functional, just to switch the color state.
            // Can be deleted, when a None state will be added to the EditMode status options
            if(button instanceof ReactiveElement reactiveElement && !reactiveElement.isActive()) {
                reactiveElement.toggleColorState();
            }
            DrawInterface();
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

            DrawCanvas();
        };

        Consumer<Element> bucketToolAction = (Element button) ->{
            buttonSwitchEditMode(button, EditMode.Bucket, true);
            // Not functional, just to switch the color state.
            // Can be deleted, when a None state will be added to the EditMode status options
            if(editMode == EditMode.Normal) {
                normalTool.toggleColorState();
            }

            // You can't use bucket tool with any drawing mode.
            modeBar.disableAll();
            drawingMode = DrawingMode.None;
            cancelPolygonDrawing.accept(button);

            DrawCanvas();
        };

        Consumer<Element> eraserToolAction = (Element button) ->{
            buttonSwitchEditMode(button, EditMode.Eraser, true);
            // Not functional, just to switch the color state.
            // Can be deleted, when a None state will be added to the EditMode status options
            if(editMode == EditMode.Normal) {
                normalTool.toggleColorState();
            }

            // You can't use eraser tool with any drawing mode.
            modeBar.disableAll();
            drawingMode = DrawingMode.None;
            cancelPolygonDrawing.accept(button);

            DrawCanvas();
        };

        Consumer<Element> handToolAction = (Element button) ->{
            buttonSwitchEditMode(button, EditMode.Move, true);
            // Not functional, just to switch the color state.
            // Can be deleted, when a None state will be added to the EditMode status options
            if(editMode == EditMode.Normal) {
                normalTool.toggleColorState();
            }

            // You can't use eraser tool with any drawing mode.
            modeBar.disableAll();
            drawingMode = DrawingMode.None;
            cancelPolygonDrawing.accept(button);

            DrawCanvas();
        };


        BitmapLabel label = new BitmapLabel(getWidthBitmap(polygonBtnColors.getFOREGROUND()), new BoundingDimensions(0), polygonBtnColors, new BoundingDimensions(0), new Coordinates(0, 0), widthSelector);


        Consumer<Element> increaseAction = (Element button) -> {
            if(selectedWidth >= 20)
                return;

            selectedWidth++;
            System.out.println("increase : " +  selectedWidth);


            label.UpdateBitmap(getWidthBitmap(label.getBitmap().getContentColor()));
            refreshRenderCanvas();
        };

        Consumer<Element> decreaseAction = (Element button) -> {
            if(selectedWidth <= 1)
                return;

            selectedWidth--;
            System.out.println("decrease : " +  selectedWidth);
            label.UpdateBitmap(getWidthBitmap(label.getBitmap().getContentColor()));
            refreshRenderCanvas();
        };





        Button increase = new Button(icons.getIconData(icons.arrowUpIcon), new BoundingDimensions(0), polygonBtnColors, polygonBtnColors, new BoundingDimensions(1), new Coordinates(0, 0), widthSelector);
        Button decrease = new Button(icons.getIconData(icons.arrowDownIcon), new BoundingDimensions(0), polygonBtnColors, polygonBtnColors, new BoundingDimensions(1), new Coordinates(0, 0), widthSelector);




        increase.setButtonConsumer(increaseAction);
        decrease.setButtonConsumer(decreaseAction);





        modeBar.addElement(lineBtn);
        modeBar.addElement(polygonBtn);
        modeBar.addElement(circleBtn);
        modeBar.addElement(rectangleBtn);

        modeBar.addElement(widthSelector);
        widthSelector.addElement(increase);
        widthSelector.addElement(label);
        widthSelector.addElement(decrease);

        modeBar.addElement(colorSelector);
        colorSelector.addColor(Color.ORANGE);
        colorSelector.addColor(Color.MAGENTA);
        colorSelector.addColor(Color.GREEN);
        colorSelector.addColor(Color.GRAY);
        colorSelector.addColor(Color.CYAN);
        colorSelector.addColor(Color.ORANGE);
        colorSelector.addColor(Color.DARK_GRAY);
        colorSelector.addColor(Color.PINK);
        colorSelector.addColor(Color.YELLOW);
        colorSelector.addColor(Color.WHITE);
        colorSelector.addColor(Color.RED);
        colorSelector.addColor(Color.BLUE);




        rootElement.addElement(modeBar);

        rootElement.addElement(toolBar);
        toolBar.addElement(normalTool);
        toolBar.addElement(vertexTool);
        toolBar.addElement(bucketTool);
        toolBar.addElement(eraserTool);
        toolBar.addElement(handTool);

        polygonBtn.setButtonConsumer(polygonBtnAction);
        confirm.setButtonConsumer(confirmButtonAction);
        cancel.setButtonConsumer(cancelPolygonDrawing);
        lineBtn.setButtonConsumer(lineBtnAction);
        circleBtn.setButtonConsumer(circleBtnAction);
        rectangleBtn.setButtonConsumer(rectBtnAction);


        normalTool.setButtonConsumer(normalToolAction);
        vertexTool.setButtonConsumer(vertexToolAction);
        bucketTool.setButtonConsumer(bucketToolAction);
        eraserTool.setButtonConsumer(eraserToolAction);
        handTool.setButtonConsumer(handToolAction);

        buttonSwitchEditMode(normalTool, EditMode.Normal, true);



        DrawInterface();
//        DrawCanvas();
    }

    private Bitmap getWidthBitmap(Color color){
        ArrayList<Bitmap> digits = Bitmap.ConvertInt(selectedWidth, color);
        Bitmap mergedBitmap = null;
        for(Bitmap digit : digits) {
            if(mergedBitmap == null){
                mergedBitmap = digit;
                continue;
            }
            mergedBitmap = Bitmap.MergeBitmaps(mergedBitmap, digit, color, 1);
        }
        return  mergedBitmap;
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
            registerRenderCanvasEntity(draggedEntity);
            canvasRasterizer.clearPreviewRaster();
            previewOverlayPanel.repaint();
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
        if(isShiftPressed){
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

        if(Math.abs(k) == 1e6 || Math.abs(k) > canvasRasterizer.getRenderRaster().getHeight()) {
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

        return new Line(a, newPoint, line.getBordersColor(), line.getBordersWidth(), line.getSpace(), line.getStep(), line.isSnapping());
    }

    public Line finalLine(Point a, Point b, boolean allowSnapping){
        boolean hasSnapping = allowSnapping&& isShiftPressed;

//        System.out.println(selectedWidth);

        return new Line(a, b, selectedColor, selectedWidth, getLineSpace(), lineStep, hasSnapping);
    }

    public Line previewLine(Point a, Point b, boolean allowSnapping){

        boolean hasSnapping = allowSnapping&& isShiftPressed;

//        System.out.println(selectedWidth);

        return new Line(a, b, Color.cyan, selectedWidth, getLineSpace(), lineStep, hasSnapping);
    }

    private void showPreviewLayer() {
        previewOverlayPanel.setVisible(true);
        previewOverlayPanel.requestFocusInWindow();
    }

    private void showRenderLayer() {
        previewOverlayPanel.setVisible(false);
        mainPanel.setVisible(true);
        mainPanel.requestFocusInWindow();
    }

    private void redrawPreviewIfNeeded() {
        if (tempPointB != null) {
            redrawPreviewCanvas();
            showPreviewLayer();
        }
    }

    private ArrayList<Point> findSurroundingPoints(Point coordinates, ArrayList<Point> parents){

        ArrayList<Point> unregisteredNeighbours = new ArrayList<>();

        int x = coordinates.getX()-1;
        int y = coordinates.getY()-1;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                int currentY = y + j;
                int currentX = x + i;
                if(i == 1 && j == 1){
                    continue;
                }


                if(parents!=null&&parents.stream().anyMatch((Point parentPoint) ->
                    parentPoint.getY() == currentY && parentPoint.getX() == currentX
                )){
                    continue;
                }

                if(canvasMap.peekCanvasEntityPoint(currentX,currentY) != null){
                    continue;
                }

                unregisteredNeighbours.add(new Point(currentX, currentY));

            }
        }

        ArrayList<Point> neighbours = new ArrayList<>();

        for(Point parentPoint : unregisteredNeighbours){
            neighbours.addAll(findSurroundingPoints(parentPoint, unregisteredNeighbours));
        }

        return neighbours;

    }




    private ArrayList<Point> findSurroundingPoints(Point coordinates){
        return findSurroundingPoints(coordinates, null);
    }

    private void createInputListeners() {

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e)  { handleMousePress(e);   }
            @Override public void mouseReleased(MouseEvent e) { handleMouseRelease(e); }
            @Override public void mouseDragged(MouseEvent e)  { handleMouseDrag(e);    }
            @Override public void mouseMoved(MouseEvent e)    { handleMouseMove(e);    }
        };

        KeyAdapter keyboardAdapter = new KeyAdapter() {
            @Override public void keyPressed(KeyEvent e)  { handleKeyPress(e);   }
            @Override public void keyReleased(KeyEvent e) { handleKeyRelease(e); }
        };

        previewOverlayPanel.addMouseListener(mouseAdapter);
        previewOverlayPanel.addMouseMotionListener(mouseAdapter);
        previewOverlayPanel.addKeyListener(keyboardAdapter);

        mainPanel.addMouseListener(mouseAdapter);
        mainPanel.addMouseMotionListener(mouseAdapter);
        mainPanel.addKeyListener(keyboardAdapter);
    }

    private void handleMousePress(MouseEvent e) {
        Point p = new Point(e.getX(), e.getY());

        Element el = WindowInterfaceMap.GetElement(e.getX(), e.getY());
        if (el instanceof ActionElement action) {
            action.RunAction();
            return;
        }

        if (pointA == null && (drawingMode != DrawingMode.None || editMode != EditMode.Normal)) {
            pointA = p;
        }

        showPreviewLayer();
        redrawAll();
    }

    private void handleMouseRelease(MouseEvent e) {

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
                pointB = ShiftSnapPoint(pointA, new Point(e.getX(), e.getY()));
            }
        }

        // Not necessary for the whole project, but essential to keep the line visible even when no origin point is set

        tempPointB = new Point(e.getX(), e.getY());
        DrawCanvas();
        DrawInterface();

        if(pointB != null) {
            pointA = null;
            pointB = null;
        }

        DrawCanvas();


        previewOverlayPanel.repaint();
        mainPanel.repaint();

//        showRenderLayer();
    }

    private void handleMouseMove(MouseEvent e) {
        tempPointB = ShiftSnapPoint(pointA, new Point(e.getX(), e.getY()));
        redrawPreviewCanvas();
    }

    private void handleMouseDrag(MouseEvent e) {
        handleMouseMove(e);
    }

    private void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL -> {
                isCtrlPressed = true;
                redrawPreviewIfNeeded(); }
            case KeyEvent.VK_SHIFT   -> {
                isShiftPressed = true;
                redrawPreviewIfNeeded();
            }
            case KeyEvent.VK_C       -> clearEverything();
        }
    }

    private void handleKeyRelease(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_CONTROL -> {
                isCtrlPressed = false;
                redrawPreviewIfNeeded();
            }
            case KeyEvent.VK_SHIFT -> {
                isShiftPressed = false;
                redrawPreviewIfNeeded();
            }
        }
    }

    private void clearEverything() {
        pointA = pointB = tempPointB = null;
        draggedPoint = originPoint = null;
        draggedEntity = null;
        canvasEntities.clear();
        currentPolygon = null;
        canvasMap.clear();

        switchEditMode(EditMode.Normal);
        canvasRasterizer.clearRenderRaster();
        canvasRasterizer.clearPreviewRaster();
        redrawAll();
    }

    private void redrawAll() {
        redrawInterface();
        redrawPreviewCanvas();
        redrawRenderCanvas();
    }

    private void redrawInterface() {
        interfaceRasterizer.rasterize(rootElement);
        mainPanel.repaint();
    }



    private void redrawPreviewCanvas() {
        DrawCanvas();
        previewOverlayPanel.repaint();
    }

    private void redrawRenderCanvas() {
        refreshRenderCanvas();
        mainPanel.repaint();
    }


    public void DrawInterface(){
        interfaceRasterizer.rasterize(rootElement);
        mainPanel.repaint();
    }



    private void setModifiedEntity(int x, int y){
        ComplexCanvasEntity foundEntity = canvasMap.peekCanvasEntityPoint(x, y);
        this.draggedPoint = foundEntity.getClosestPoint(x, y);
        this.draggedEntity = foundEntity;
        originPoint = new Point(draggedPoint);
        pointA = foundEntity.getClosestSibling(draggedPoint);
    }

    private void setMovedEntity(int x, int y){
        this.draggedEntity = canvasMap.peekCanvasEntityPoint(x, y);
        this.originPoint = new Point(x, y);
        System.out.println("New entity set");
        System.out.println(this.originPoint == null);
    }





    public ComplexCanvasEntity getMostPropableEntity(Map<ComplexCanvasEntity, Integer> surroundingEntities){
        int maxAppearance = Integer.MIN_VALUE;
        ComplexCanvasEntity foundEntity = null;
        for(ComplexCanvasEntity entity : surroundingEntities.keySet()){
            int appearance = surroundingEntities.get(entity);
            if(appearance > maxAppearance){
                maxAppearance = appearance;
                foundEntity = entity;
            }
        }
        return foundEntity;
    }

    public void DrawCanvas() {

        Line line;


        switch(drawingMode) {
            case None: {
                    switch(editMode) {
                        case Move: {

                            if(draggedEntity != null && pointA == null) {
                                draggedEntity = null;
                                System.out.println("Clear entity");
                            }
//
                            if(draggedEntity == null && pointA != null) { // Set dragged point

                                int centerX = pointA.getX();
                                int centerY = pointA.getY();

                                int maxRadius = 15;

                                if (canvasMap.hasEntity(centerX, centerY)) {
//                                    this.draggedEntity = canvasMap.peekCanvasEntityPoint(centerX, centerY);
                                    setMovedEntity(centerX, centerY);
                                }

                                // Spiral through the defined bounds
                                for (int r = 1; r <= maxRadius && draggedEntity == null; r++) {
                                    for (int x = centerX - r; x <= centerX + r; x++) {
                                        if (canvasMap.hasEntity(x, centerY - r)) {setMovedEntity(x, centerY - r); break;} // Top edge
                                        if (canvasMap.hasEntity(x, centerY + r)) {setMovedEntity(x, centerY + r);  break;} // Bottom edge
                                    }
                                    if (draggedEntity != null) break;

                                    for (int y = centerY - r + 1; y <= centerY + r - 1; y++) {
                                        if (canvasMap.hasEntity(centerX - r, y)) {setMovedEntity(centerX - r, y);  break;} // Left edge
                                        if (canvasMap.hasEntity(centerX + r, y)) {setMovedEntity(centerX + r, y);  break;} // Right edge
                                    }
                                }

                                if(draggedEntity != null) {
                                    System.out.println("Got an entity : " + draggedEntity.getClass().getSimpleName());
                                    transferIntoPreviewLayer(draggedEntity);

                                }else{
                                    pointA = null;
                                    System.out.println("No entity found : NULL");
                                }

                                System.out.println("Before refresh");
                                refreshRenderCanvas();
                                System.out.println("After refresh");


                            }


                            if(originPoint != null && draggedEntity != null){
                                int px = 0;
                                int py = 0;
                                Point tmpPoint = new Point(tempPointB.getX() - originPoint.getX(), tempPointB.getY() - originPoint.getY());
                                if(previousRelativePoint != null) {
                                    px = previousRelativePoint.getX();
                                    py = previousRelativePoint.getY();
                                }

//                                System.out.println((tempPointB.getX() - originPoint.getX()) + " " + (tempPointB.getY() - originPoint.getY()));
                                this.previousRelativePoint = new Point(tmpPoint);


                                // Vertex transformation ended
                                if(pointB != null) {

                                    this.draggedEntity.move(tmpPoint.getX() - px, tmpPoint.getY() - py);
                                    registerRenderCanvasEntity(draggedEntity);
                                    refreshRenderCanvas();
                                    this.draggedEntity = null;
                                    this.previousRelativePoint = null;
                                    originPoint = null;

                                    break;
                                }

                                // Mouse moved
                                if (tempPointB != null) {
                                    this.draggedEntity.move(tmpPoint.getX() - px, tmpPoint.getY() - py);
//                                    System.out.println((tempPointB.getX() - originPoint.getX()) + " " + (tempPointB.getY() - originPoint.getY()));
                                    canvasRasterizer.clearPreviewRaster();
                                    canvasRasterizer.rasterize(draggedEntity, true);
                                    this.previousRelativePoint = new Point(tmpPoint);
                                    previewOverlayPanel.repaint();
                                }



                            }



                        } break;
                        case Vertex: {

                            if(draggedPoint != null && pointA == null) {
                                draggedPoint = null;
                            }
//
                            if(draggedPoint == null && pointA != null) { // Set dragged point

                                int centerX = pointA.getX();
                                int centerY = pointA.getY();

                                int maxRadius = 15;

                                if (canvasMap.peekCanvasEntityPoint(centerX, centerY) != null) {
                                    setModifiedEntity(centerX, centerY);
                                }


                                // Spiral through the defined bounds
                                for (int r = 1; r <= maxRadius && draggedPoint == null; r++) {
                                    for (int x = centerX - r; x <= centerX + r; x++) {
                                        if (canvasMap.hasEntity(x, centerY - r)) {
                                            setModifiedEntity(x, centerY - r); break;} // Top edge
                                        if (canvasMap.hasEntity(x, centerY + r)) {
                                            setModifiedEntity(x, centerY + r); break;} // Bottom edge
                                    }
                                    if (draggedPoint != null) break;

                                    for (int y = centerY - r + 1; y <= centerY + r - 1; y++) {
                                        if (canvasMap.hasEntity(centerX - r, y)) {
                                            setModifiedEntity(centerX - r, y); break;} // Left edge
                                        if (canvasMap.hasEntity(centerX + r, y)) {
                                            setModifiedEntity(centerX + r, y); break;} // Right edge
                                    }
                                }

                                if(draggedPoint != null) {
                                    System.out.println("Got a point : X(" + draggedPoint.getX() + "), Y(" + draggedPoint.getY() + "): " + draggedEntity.toString());
                                    transferIntoPreviewLayer(draggedEntity);


                                }else{
                                    pointA = null;
                                    System.out.println("No point has been found : NULL");
                                }

                                refreshRenderCanvas();

                            }

                            // Vertex transformation ended
                            if(draggedPoint != null && pointB != null && originPoint != null && draggedEntity != null) {

                                this.draggedPoint = null;
                                this.originPoint = null;
                                registerRenderCanvasEntity(draggedEntity);
                                refreshRenderCanvas();
                                this.draggedEntity = null;

                                break;
                            }

                            // Mouse moved
                            if (tempPointB != null && draggedPoint != null && draggedEntity != null) { //
                                draggedEntity.modifyPoint(draggedPoint, tempPointB.getX(), tempPointB.getY());
                                canvasRasterizer.clearPreviewRaster();
                                canvasRasterizer.rasterize(draggedEntity, true);

                                previewOverlayPanel.repaint();
                            }


                        }break;
                        case Bucket: {
                            if (pointA != null) {
                                // Find valid boundary start
                                Point start = new Point(pointA);
                                List<ComplexCanvasEntity> pointEntities = canvasMap.getCanvasEntities(start.getX(), start.getY());
                                FillEntity fillEntity = null;
                                if(pointEntities != null){
                                    for (ComplexCanvasEntity pointEntity : pointEntities) {
                                        if(pointEntity instanceof FillEntity fill) {
                                            fillEntity = fill;
                                            break;
                                        }
                                    }
                                }

                                if(fillEntity != null) {
                                    fillEntity.setInfill(selectedColor);
                                    fillEntity.setFillReferencePoint(start);
                                    break;
                                }

                                ArrayList<Point> floodPoints = canvasMap.getFloodPoints(start);
                                FillEntity fill = new FillEntity(FilterBoundaryPoints(floodPoints), start, selectedColor);

                                registerRenderCanvasEntity(fill);

                                pointA = null;

                            }



                        } break;
                        case Eraser: {
                            if (pointA != null) {
                                Point tmpPoint = new Point(pointA);
                                pointA = null;
                                ComplexCanvasEntity foundEntity = canvasMap.peekCanvasEntityPoint(tmpPoint.getX(), tmpPoint.getY());
                                if (foundEntity != null) {
                                    canvasEntities.remove(foundEntity);

                                    for(Point visiblePoint : foundEntity.getVisiblePoints()){
                                        canvasMap.removeCanvasEntityPoint(foundEntity,  visiblePoint.getX(), visiblePoint.getY());
                                    }
                                }

                            }
                        }break;

                    }

            }break;
            case Line: {
                if (pointB == null && tempPointB != null && pointA != null) { // Click – Move – (Release / Click)
                    canvasRasterizer.clearPreviewRaster();
                    line  = previewLine(pointA, tempPointB, true);
                    System.out.println(line.getBordersWidth());
                    line = SnapLine(line);
                    System.out.println(line.getBordersWidth());
                    canvasRasterizer.rasterize(line, true);
                }


                if (pointB != null && pointA != null) { // Click – Move – Click

//                    previewOverlayPanel.setVisible(false);

                    frame.add(mainPanel, BorderLayout.CENTER);

                    line = SnapLine(finalLine(pointA, pointB, true));


                    registerRenderCanvasEntity(line);
                    canvasRasterizer.clearPreviewRaster();
                    mainPanel.repaint();
                    previewOverlayPanel.repaint();


                }

            }break;
            case Polygon: {
                if (pointB == null) {
                    if (this.polygon != null && polygon.getPoints().size() > 1 && tempPointB != null) {
                        canvasRasterizer.clearPreviewRaster();

                        switchTemporaryPanel();

                        line = SnapLine(finalLine(polygon.getPoints().getLast(), tempPointB, true));

                        pointA = polygon.getPoints().getLast();

                        tempPointB = line.getPointB(); // The point is snapped into place, if the snapping was enabled at the time

                        Polygon tmpPoly = new Polygon(polygon);
                        // Add the preview lines
                        tmpPoly.constructPoint(tempPointB, selectedWidth, getLineSpace(), lineStep, isShiftPressed);
                        tmpPoly.constructPoint(polygon.getPoints().getFirst(), selectedWidth, getLineSpace(), lineStep, isShiftPressed);

                        canvasRasterizer.rasterize(tmpPoly, true);
                    }
                    break;
                }


                if (this.polygon != null) {
                    polygon.constructPoint(pointB, selectedWidth, getLineSpace(), lineStep, false);

                    if (polygon.getPoints().getFirst().equals(pointB)) {
                        Polygon newPolygon = new Polygon(polygon);
//                        for (Line polygonSegment : registeredPolygon.getLines()) {
//                            polygonSegment.setColor(Color.GREEN);
//                        }

                        registerRenderCanvasEntity(newPolygon);
                        canvasRasterizer.clearPreviewRaster();
                        mainPanel.repaint();
                        previewOverlayPanel.repaint();


                    }

                } else {
                    polygon = new Polygon(pointA, selectedColor, selectedWidth, null);
                    polygon.constructPoint(pointB, selectedWidth, getLineSpace(), lineStep, false);
                }

            }break;
            case Rectangle: {
                if (pointB == null && tempPointB != null && pointA != null) { // Click – Move – (Release / Click)
                    canvasRasterizer.clearPreviewRaster();
                    line = SnapLine(previewLine(pointA, tempPointB, true));
                    Rectangle rect = new Rectangle(line, selectedColor, selectedWidth, null);

                    canvasRasterizer.rasterize(rect, true);
                }

                if (pointB != null && pointA != null) { // Click – Move – Click

                    mainPanel.requestFocus();
                    mainPanel.requestFocusInWindow();

                    previewOverlayPanel.setVisible(false);

                    frame.add(mainPanel, BorderLayout.CENTER);
                    line = SnapLine(finalLine(pointA, pointB, true));

                    Rectangle rect = new Rectangle(line, selectedColor, selectedWidth,null);

                    canvasRasterizer.clearPreviewRaster();

                    registerRenderCanvasEntity(rect);


                }

            }break;
            case Circle: {

                if (pointB == null && tempPointB != null && pointA != null) { // Click – Move – (Release /Click)
//                    canvasRasterizer.clearPreviewRaster();
                    line = previewLine(pointA, tempPointB, true);

                    Circle circle = new Circle(line, selectedColor, selectedWidth, lineStep, getLineSpace(), null);
                    System.out.println(circle.getBordersWidth());
                    canvasRasterizer.rasterize(circle, true);

                }else{

                    if (pointB != null && pointA != null) { // Click – Move – Click

                        mainPanel.requestFocus();
                        mainPanel.requestFocusInWindow();

                        previewOverlayPanel.setVisible(false);

                        frame.add(mainPanel, BorderLayout.CENTER);

                        line = finalLine(pointA, tempPointB, true);

                        Circle circle = new Circle(line, selectedColor, selectedWidth,  lineStep, getLineSpace(),  null);


//                        for (Point point : circle.getTransformationAffectedPoints(draggedPoint))
//                        {
//                            canvasMap.addCanvasEntityPoint(point, circle);
//                        }

                        canvasRasterizer.clearPreviewRaster();
                        registerRenderCanvasEntity(circle);

                    }
                }



            }break;
        }
    }

    public ArrayList<Point> FilterBoundaryPoints(Collection<Point> points){
        ArrayList<Point> result = new ArrayList<>();
        for (Point point : points){
            if(canvasMap.isBoundary(point)){
                result.add(point);
            }
        }
        return result;
    }

    public void switchTemporaryPanel(){
        previewOverlayPanel.setVisible(true);
        previewOverlayPanel.repaint();
        frame.repaint();

        previewOverlayPanel.requestFocus();
        previewOverlayPanel.requestFocusInWindow();
    }

}
