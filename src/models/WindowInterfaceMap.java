package models;

import java.util.ArrayList;

public class WindowInterfaceMap {
    public static Element[][] map;

    public static void RegisterElement(Element element) {

        int startY = element.getY() + element.getMargin_top();
        int endY = element.getY() + element.getHeight() - element.getMargin_bottom();
        int startX = element.getX() + element.getMargin_left();
        int endX = element.getX() + element.getWidth() - element.getMargin_right();

        for(int y =  startY; y <= endY; y++) {
            for(int x =  startX; x <= endX; x++) {
                map[y][x] = element;
            }
        }
    }

    public static void RegisterElementPixel(Element element, int x, int y) {
        map[y][x] = element;
    }

    public static Element GetElement(int x, int y) {
        return map[y][x];
    }

}
