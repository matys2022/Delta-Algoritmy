package rasterizers;

import models.InterfaceEntities.BitmapElement;
import models.InterfaceEntities.Element;
import models.WindowInterfaceMap;
import rasters.Raster;

import java.awt.*;


public class InterfaceRasterizer implements ElementRasterizer {

    private  Raster raster;

    public InterfaceRasterizer(Raster raster) {
        this.raster = raster;
    }




    public <T extends Element> void rasterize(T element, int index) {

        int startY = element.getY() + element.getMargin_top();
        int endY = element.getY() + element.getHeight() + element.getMargin_bottom();
        int startX = element.getX() + element.getMargin_left();
        int endX = element.getX() + element.getWidth() - element.getMargin_right();

        boolean hasBorder = element.getBORDER() != null && element.getBORDER() != element.getBACKGROUND();

        boolean hasBitmap = element instanceof BitmapElement;

        boolean hasForeground = hasBitmap
                    && ((BitmapElement)element).getFOREGROUND() != null
                    && ((BitmapElement)element).getFOREGROUND() != element.getBACKGROUND()
//                    && ((ContentElement)element).getBitmap() != null
                ;


//        System.out.println("Rasterizing : " + element.getClass().getSimpleName());
//        System.out.println("startY: " + startY);
//        System.out.println("endY: " + endY);
//        System.out.println("startX: " + startX);
//        System.out.println("endX: " + endX);


            for(int y = startY; y < endY; y++){

                for (int x = startX; x < endX; x++) {

                    Color color = element.getBACKGROUND();

                    int pixelX = x - startX - element.getPaddingLeft();
                    int pixelY = y - startY - element.getPaddingTop();

    //                System.out.println("X: " + pixelX + " Y: " + pixelY);

                    WindowInterfaceMap.RegisterElementPixel(element, x, y);

                    if(element.getBACKGROUND() != null) {
                        if (
                                hasForeground &&
                                        ( // Work only inside padding
                                                (
                                                        x >= startX + element.getPaddingLeft()
                                                                &&
                                                                x <= endX - element.getPaddingRight() - 1
                                                )
                                                        &&
                                                        (
                                                                y >= startY + element.getPaddingTop()
                                                                        &&
                                                                        y <= endY - element.getPaddingBottom() - 1
                                                        )
                                        )
                                        &&
                                        (
                                                ((BitmapElement) element).getBitmapContent().length > pixelY
                                                        &&
                                                        ((BitmapElement) element).getBitmapContent()[pixelY].length > pixelX
                                        )
                                        &&
                                        ( // Check whether the pixel in the content bitmap is lit up
                                                ((BitmapElement) element).getBitmapContent()[pixelY][pixelX] == 1
                                        )
                        ) {
                            color = ((BitmapElement) element).getFOREGROUND();
                        }


                        if ( // Exclude borders
                                hasBorder &&
                                        ( // Left and right borders
                                                x <= startX + element.getLeftBorderWidth() // Left border
                                                        ||
                                                        x >= endX - element.getRightBorderWidth() - 1// Right border
                                        )
                                        ||
                                        ( // Bottom and top borders
                                                y <= startY + element.getTopBorderWidth() // Top border
                                                        ||
                                                        y >= endY - element.getBottomBorderWidth() - 1// Bottom border
                                        )
                        ) {
                            color = element.getBORDER();
                        }

                        raster.setPixel(x, y, color.getRGB());
                    }
                }
            }


//        System.out.println("Has children? : " + (element.getChildren() != null));
//        System.out.println("Has more than 0 children? : " + (!element.getChildren().isEmpty()));
//        System.out.println("Has overflown? : " + (index >= element.getChildren().size()));
        if (element.getChildren() != null) {
            for (Element child : element.getChildren()) {
                rasterize(child);
            }
        }


    }

    @Override
    public <T extends Element> void rasterize(T element) {
        this.rasterize(element, 0);
    }

}
