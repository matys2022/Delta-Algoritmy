package models;

import java.io.*;
import java.nio.*;
import java.nio.file.DirectoryStream;
import java.util.Iterator;


public class Icons{
    public Icons(){

    }

    // 50 x 50
    private final String prefix = "/Icons/";
    public String polygonIcon = "Polygon.bin";
    public String lineIcon = "Line.bin";
    public String tickIcon = "Tick.bin";
    public String closeIcon = "Close.bin";
    public String cursorHollowIcon = "HollowCursor.bin";
    public String cursorFullIcon = "FilledCursor.bin";
    public String bucketIcon = "Bucket.bin";
    public String rectangleIcon = "Rectangle.bin";
    public String circleIcon = "Circle.bin";
    public String circleThinIcon = "CircleThin.bin";
    public String rectangleThinIcon = "RectangleThin.bin";
    public String eraserIcon = "Eraser.bin";

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

    public int[][] getLineIcon() {
        return getIconData(lineIcon);
    }

    public int[][] getTickIcon() {
        return getIconData(tickIcon);
    }

    public int[][] getCloseIcon() {
        return getIconData(closeIcon);
    }

    public int[][] getCursorHollowIcon() {
        return getIconData(cursorHollowIcon);
    }

    public int[][] getCursorFullIcon() {
        return getIconData(cursorFullIcon);
    }
}
