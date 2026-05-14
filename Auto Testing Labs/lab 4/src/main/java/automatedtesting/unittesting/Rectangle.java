package automatedtesting.unittesting;

public final class Rectangle {
    private final int xMin, xMax, yMin, yMax;

    private static final PointTopology[] LOOKUP = {
            PointTopology.INSIDE,            // 0b00
            PointTopology.VERTICAL_BORDER,   // 0b01
            PointTopology.HORIZONTAL_BORDER, // 0b10
            PointTopology.CORNER             // 0b11
    };

    public Rectangle(final int xMin, final int xMax, final int yMin, final int yMax) {
        this.xMin = xMin;
        this.xMax = xMax;
        this.yMin = yMin;
        this.yMax = yMax;
    }

    public PointTopology getPointPosition(final int x, final int y) {
        if (((x - xMin) | (xMax - x) | (y - yMin) | (yMax - y)) < 0) {
            return PointTopology.OUTSIDE;
        }

        final int v = ((x == xMin) | (x == xMax)) ? 1 : 0;
        final int h = ((y == yMin) | (y == yMax)) ? 2 : 0;
        return LOOKUP[v | h];
    }
}
