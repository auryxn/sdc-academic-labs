package automatedtesting.unittesting;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RectangleTest {

    private Rectangle taskRectangle;

    @BeforeEach
    void setUp() {
        taskRectangle = new Rectangle(-3, 3, -4, 4);
    }

    @ParameterizedTest(name = "Point({0}, {1}) -> {2}")
    @CsvSource({
            "0,   0,  INSIDE",
            "5,   0,  OUTSIDE",
            "0,   6,  OUTSIDE",
            "-4,  0,  OUTSIDE",
            "0,  -5,  OUTSIDE",
            "3,   4,  CORNER",
            "-3,  4,  CORNER",
            "3,  -4,  CORNER",
            "-3, -4,  CORNER",
            "3,   0,  VERTICAL_BORDER",
            "-3,  2,  VERTICAL_BORDER",
            "0,   4,  HORIZONTAL_BORDER",
            "1,  -4,  HORIZONTAL_BORDER"
    })
    void testTaskRectangle(int x, int y, PointTopology expected) {
        assertEquals(expected, taskRectangle.getPointPosition(x, y));
    }

    @Test
    void testDifferentRectangle() {
        Rectangle smallRect = new Rectangle(0, 1, 0, 1);
        assertEquals(PointTopology.CORNER, smallRect.getPointPosition(1, 1));
        assertEquals(PointTopology.OUTSIDE, smallRect.getPointPosition(3, 3));
    }
}
