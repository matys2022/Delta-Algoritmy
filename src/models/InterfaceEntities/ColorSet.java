package models.InterfaceEntities;

import java.awt.*;

public class ColorSet {
    private Color BACKGROUND;
    private Color BORDER;
    private Color FOREGROUND;

    public ColorSet(Color BACKGROUND, Color BORDER,  Color FOREGROUND) {
        this.BACKGROUND = BACKGROUND;
        this.BORDER = BORDER;
        this.FOREGROUND = FOREGROUND;
    }

    public Color getBACKGROUND() {
        return BACKGROUND;
    }

    public Color getBORDER() {
        return BORDER;
    }

    public Color getFOREGROUND() {
        return FOREGROUND;
    }
}
