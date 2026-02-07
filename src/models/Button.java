package models;

public class Button extends ReactiveElement implements ActionElement{
    private Runnable buttonConsumer;

    public Button(int[][] contentBitMap, BoundingDimensions padding, ColorSet colorSet, ColorSet secondaryColorSet, BoundingDimensions border, Coordinates coordinates, Runnable action) {
        super(getMaxLength(contentBitMap), contentBitMap.length, new Bitmap(contentBitMap, colorSet.getFOREGROUND()), padding, colorSet, secondaryColorSet,  border, coordinates);
        this.buttonConsumer = action;
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
        buttonConsumer.run();
    }
}
