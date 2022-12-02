import org.junit.Assert;
import org.junit.Test;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.util.Units;
import frc.robot.FieldCalculations;

public class FieldCalculationsIntersectionTest {

    /* origin in LL corner. Increasing angle CCW. Heading 0 points right (high school trig coords) */
    Translation2d t00 = new Translation2d(0, 0);
    Translation2d t01 = new Translation2d(0, 1);
    Translation2d t10 = new Translation2d(1, 0);

    @Test
    public void test00() {
        /* we are at (1, 1) */
        Translation2d rv = triangulate(t10, Units.degreesToRadians(-90), t01, Units.degreesToRadians(180), new Translation2d(1, 1));
        System.out.println (rv);

        /* kaboom */
        rv = triangulate(t00, Units.degreesToRadians(-90), t10, Units.degreesToRadians(-90), null);
        System.out.println (rv);

        /* we are at (0.5, 0.5) */
        rv = triangulate(t00, Units.degreesToRadians(-135), t10, Units.degreesToRadians(-45), new Translation2d(0.5, 0.5));
        System.out.println (rv);
        rv = triangulate(t00, Units.degreesToRadians(-135), t01, Units.degreesToRadians(135), new Translation2d(0.5, 0.5));
        System.out.println (rv);

        for (double delta = -10; delta <= 10; delta += 1.0) {
            /* these should have same X, Y */
            rv = triangulate(t01, Units.degreesToRadians(135 + delta), t10, Units.degreesToRadians(-45 - delta), null);
            System.out.println (delta + " " + rv);
        }

    }


    Translation2d triangulate (Translation2d a, double heading_a, Translation2d b, double heading_b, Translation2d expected) {
        var rv = FieldCalculations.locateViaTriangulation(a, heading_a, b, heading_b);
        if (expected != null) {
            Assert.assertEquals("X bad", rv.getX(), expected.getX(), 0.01);
            Assert.assertEquals("Y bad", rv.getY(), expected.getY(), 0.01);
        }
        return rv;
    }
}
