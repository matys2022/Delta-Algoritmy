package models.InterfaceEntities;

import java.util.function.Consumer;

public class Button extends ReactiveElement implements ActionElement {
    private Consumer<Element> buttonConsumer;


    public Button(int[][] contentBitMap, BoundingDimensions padding, ColorSet colorSet, ColorSet secondaryColorSet, BoundingDimensions border, Coordinates coordinates, Consumer<Element> action, Element parent) {
        super(getMaxLength(contentBitMap), contentBitMap.length, new Bitmap(contentBitMap, colorSet.getFOREGROUND()), padding, colorSet, secondaryColorSet,  border, coordinates, parent);
        this.buttonConsumer = action;
    }
    public Button(int[][] contentBitMap, BoundingDimensions padding, ColorSet colorSet, ColorSet secondaryColorSet, BoundingDimensions border, Coordinates coordinates, Consumer<Element> action) {
        this(contentBitMap, padding, colorSet, secondaryColorSet, border, coordinates, action, null);
    }
    public Button(Button button){
        this(button.getBitmapContent(), button.getPadding(), button.colorSet, button.secondaryColorSet, button.border, button.getCoordinates(), button.buttonConsumer, button.getParent());
    }
//    public Button(int[][] contentBitMap, Coordinates coordinates){
//        this(contentBitMap, null, null, null, new BoundingDimensions(0), new Coordinates(0, 0), null);
//    }
    public Button(int[][] contentBitMap, BoundingDimensions padding, ColorSet colorSet, ColorSet secondaryColorSet, BoundingDimensions border, Coordinates coordinates, Element parent) {
        this(contentBitMap, padding, colorSet, secondaryColorSet, border, coordinates, null, parent);
    }
    public Button(int[][] contentBitMap, BoundingDimensions padding, ColorSet colorSet, ColorSet secondaryColorSet, BoundingDimensions border, Coordinates coordinates) {
        this(contentBitMap, padding, colorSet, secondaryColorSet, border, coordinates, null, null);
    }


    public void setButtonConsumer(Consumer<Element> buttonConsumer) {
        this.buttonConsumer = buttonConsumer;
    }

    private static int getMaxLength(int[][] matrix) {
        int max = 0;
        if (matrix == null) return 0;

        for (int[] row : matrix) {
            if (row != null && row.length > max) {
                max = row.length;
            }
        }



        return max;
    }

    @Override
    public void RunAction() {
        if(buttonConsumer != null){
            buttonConsumer.accept(this);
        }

    }
}
