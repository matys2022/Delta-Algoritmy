package models.InterfaceEntities;

import models.Icons;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Bitmap {
    int[][] contentBitmap;
    private Color contentColor;

    public Bitmap(int[][] contentBitmap, Color contentColor) {
        this.contentBitmap = contentBitmap;
        this.contentColor = contentColor;
    }

    public int[][] getContentBitmap() {
        return contentBitmap;
    }

    public Color getContentColor() {
        return contentColor;
    }

    public static ArrayList<Bitmap> ConvertInt(int number, Color contentColor) {
        Icons icons = new Icons();

        ArrayList<Bitmap> digits = new ArrayList<Bitmap>();
        char[] converted = String.valueOf(number).toCharArray();
        for(char c : converted){
            String  digitPath = switch (c) {
                case '1' -> icons.oneDigit;
                case '2' -> icons.twoDigit;
                case '3' -> icons.threeDigit;
                case '4' -> icons.fourDigit;
                case '5' -> icons.fiveDigit;
                case '6' -> icons.sixDigit;
                case '7' -> icons.sevenDigit;
                case '8' -> icons.eightDigit;
                case '9' -> icons.nineDigit;
                default -> icons.zeroDigit;
            };
            digits.add(new Bitmap(icons.getIconData(digitPath), contentColor));
        }
        return digits;
    }

    public static Bitmap MergeBitmaps(Bitmap bitmap1, Bitmap bitmap2, Color contentColor, int spacing) {
        Bitmap newBitmap;

        int[][] map1 = bitmap1.getContentBitmap();
        int[][] map2 = bitmap2.getContentBitmap();

        int h1 = map1.length;
        int h2 = map2.length;
        int w1 = Bitmap.getMaxLength(map1);
        int w2 = Bitmap.getMaxLength(map2);

        int maxY = Math.max(h1, h2);
        int maxX = w1 + spacing + w2;

        int[][] newMatrix = new int[maxY][maxX];

        for (int i = 0; i < maxY; i++) {
            // Copy from Map 1 (only if i is within map1's height)
            if (i < h1) {
                for (int j = 0; j < w1; j++) {
                    newMatrix[i][j] = map1[i][j];
                }
            }

            // Copy from Map 2 (only if i is within map2's height)
            if (i < h2) {
                for (int j = 0; j < w2; j++) {
                    // The X offset is: width of map1 + spacing
                    newMatrix[i][w1 + spacing + j] = map2[i][j];
                }
            }
        }

        return new Bitmap(newMatrix,  contentColor);

    }

    public static int getMaxLength(int[][] matrix) {
        int max = 0;
        if (matrix == null) return 0;

        for (int[] row : matrix) {
            if (row != null && row.length > max) {
                max = row.length;
            }
        }

        return max;
    }
}
