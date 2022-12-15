import org.junit.Assert;
import org.junit.Test;

import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import frc.robot.FieldCalculations;


public class FieldCalculationsTarget3dTest {
    static final double S2 = Math.sqrt(2.0);
    static final double S3 = Math.sqrt(3.0);

    /* origin in LL corner. Increasing angle CCW. North (heading 0) points to red alliance wall */
    Translation3d target_051 = new Translation3d(0, 5, 1);

    @Test
    public void test00() {
        /*
         * We are facing dead south, level, and we think target is 2 meters in
         * front of us. we should be at x=2, y=5, z=1.
         */
        Translation3d robot = test (target_051, 2, 0, 0, 180, 0, new Translation3d(2, 5, 1));
        System.out.println(robot);
    }

    @Test
    public void test01() {
        /*
         * We are facing dead south, up @ 45°, and we think target is √2 meters in
         * front of us. we should be at x=1, y=5, z=0.
         */
        Translation3d robot = test (target_051, S2, 0, 0, 180, 45, new Translation3d(1, 5, 0));
        System.out.println(robot);
    }

    @Test
    public void test02() {
        /*
         * We are facing W, up @ 45°, and we think target is √2 meters in
         * front of us. we should be at x=0, y=4, z=0.
         */
        Translation3d robot = test (target_051, S2, 0, 0, 90, 45, new Translation3d(0, 4, 0));
        System.out.println(robot);
    }

    @Test
    public void test03() {
        /*
         * We are facing W, down @ 45°, and we think target is √2 meters in
         * front of us. we should be at x=0, y=4, z=2.
         */
        Translation3d robot = test (target_051, S2, 0, 0, 90, -45, new Translation3d(0, 4, 2));
        System.out.println(robot);
    }

    @Test
    public void test04() {
        /*
         * We are facing SW, level, and we think target is √2 meters in
         * front of us. we should be at x=1, y=4, z=1.
         */
        Translation3d robot = test (target_051, S2, 0, 0, 135, 0, new Translation3d(1, 4, 1));
        System.out.println(robot);
    }

    @Test
    public void test05() {
        /*
         * We are facing SW, up @ 45°, and we think target is √2 meters in
         * front of us. we should be at x=√2/2, y=5-√2/2, z=0.
         */
        Translation3d robot = test (target_051, S2, 0, 0, 135, 45, new Translation3d(S2/2, 5-S2/2, 0));
        System.out.println(robot);
    }

    @Test
    public void test06() {
        /*
         * We are facing SW, up @ 35.266°, and we think target is √3 meters in
         * front of us. we should be at x=√2/2, y=5-√2/2, z=0.
         */
        Translation3d robot = test (target_051, S3, 0, 0, 135, 35.266, new Translation3d(1, 4, 0));
        System.out.println(robot);
    }

    @Test
    public void test10() {
        /*
         * We are facing dead south, level, and we think target is 2 meters in
         * front of us and 1 meter above us. we should be at x=2, y=5, z=0.
         */
        Translation3d robot = test (target_051, 2, 0, 1, 180, 0, new Translation3d(2, 5, 0));
        System.out.println(robot);
    }

    Translation3d test (Translation3d target, double x, double y, double z, double robotHeadingInDegrees, double cameraTiltUpInDegrees, Translation3d expected) {
        Translation3d targetFromCamera = new Translation3d(x, y, z);
        Translation3d rv = FieldCalculations.locateViaTarget(target, targetFromCamera, Units.degreesToRadians(robotHeadingInDegrees), Units.degreesToRadians(cameraTiltUpInDegrees));
        if (expected != null) {
            try {
                Assert.assertEquals("X bad", rv.getX(), expected.getX(), 0.01);
                Assert.assertEquals("Y bad", rv.getY(), expected.getY(), 0.01);
                Assert.assertEquals("Z bad", rv.getZ(), expected.getZ(), 0.01);
            } catch (AssertionError e) {
                System.err.println ("Bad result: " + rv + ", expected " + expected);
                throw e;
            }
        }
        return rv;
    }

}
