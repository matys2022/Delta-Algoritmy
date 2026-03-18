package models;

import java.io.*;
import java.nio.*;


public class Icons{
    public Icons(){

    }


    private final String prefix = "/Icons/";

    // Shape icons
    // 50 x 50
    public String polygonIcon = "Polygon.bin";
    public String lineIcon = "Line.bin";
    public String tickIcon = "Tick.bin";
    public String closeIcon = "Close.bin";
    public String rectangleIcon = "RectangleThin.bin";
    public String circleIcon = "CircleThin.bin";

    // Tool icons - 30 x 30
    public String cursorHollowIcon = "HollowCursor.bin";
    public String cursorFullIcon = "FilledCursor.bin";
    public String bucketIcon = "Bucket.bin";
    public String eraserIcon = "Eraser.bin";
    public String handIcon = "Hand.bin";

    // Input icons
    public String arrowDownIcon = "ArrowDown.bin";
    public String arrowUpIcon = "ArrowUp.bin";

    // Color icons
    public String blockIcon = "Block.bin";

    // Digits
    public String zeroDigit = "0-Digit.bin";
    public String oneDigit = "1-Digit.bin";
    public String twoDigit = "2-Digit.bin";
    public String threeDigit = "3-Digit.bin";
    public String fourDigit = "4-Digit.bin";
    public String fiveDigit = "5-Digit.bin";
    public String sixDigit = "6-Digit.bin";
    public String sevenDigit = "7-Digit.bin";
    public String eightDigit = "8-Digit.bin";
    public String nineDigit = "9-Digit.bin";

    private int[][] getIconBits(String fileName) throws  java.io.IOException{

        ByteBuffer buffer;


        InputStream is = Icons.class.getResourceAsStream(prefix + fileName);

        if(is == null){
            throw new java.io.IOException("Icons/Close.bin not found");
        }

        byte[] fileData = is.readAllBytes();
        is.close();

        buffer = ByteBuffer.wrap(fileData);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        int rows = buffer.getInt();
        int cols = buffer.getInt();

        int[][] array = new int[rows][cols];
        int bitIndex = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int byteIndex = 8 + (bitIndex / 8); // Skip 8-byte header
                int bitPosition = 7 - (bitIndex % 8);

                array[i][j] = (fileData[byteIndex] & (1 << bitPosition)) != 0 ? 1 : 0;
                bitIndex++;
            }
        }

        return array;
    }

    public int[][] getIconData(String fileName){
        try{
            return this.getIconBits(fileName);
        }catch(Exception e){
            System.err.println("Icons not found");
            return null;
        }
    }
}
