package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;

/** Add your docs here. */
public class FieldCalculations {

    /**
     * Given a couple of points on the field and our headings to them, determine our position.
     * https://stackoverflow.com/a/37090237/17887564
     * 
     * TODO: fix case where headings are too close to each other, or if they are almost in
     * opposite directions.
     * 
     * @param a
     * @param a_heading_radians heading to point a (in radians)
     * @param b
     * @param b_heading_radians heading to point b (in radians)
     * @param expected
     * @return
     */
    public static Translation2d locateViaTriangulation (Translation2d a, double a_heading_radians, Translation2d b, double b_heading_radians) {
        double ca = Math.cos(a_heading_radians);
        double sa = Math.sin(a_heading_radians);
        double cb = Math.cos(b_heading_radians);
        double sb = Math.sin(b_heading_radians);
        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double t;
        if ( ca > 0.01  ||  ca < -0.01 ) {
            double k = sa / ca;
            double den = cb * k - sb;
            t = ( dy - dx * k) / den;
        } else {
            double k = ca / sa;
            double den = sb * k - cb;
            t = ( dx - dy * k) / den;
        }
        double x = b.getX() + t * cb;
        double y = b.getY() + t * sb;
        Translation2d rv = new Translation2d(x, y);
        return rv;
    }

    /**
     * 
     * @param target
     * @param x
     * @param y
     * @param robotHeadingInRadians in radians
     * @return
     */
    public static Translation2d locateViaTarget (Translation2d target, double x, double y, double robotHeadingInRadians) {
        Translation2d robotToTargetTranslationRelative = new Translation2d(x, y);
        Translation2d robotToTargetTranslationAbsolute = robotToTargetTranslationRelative.rotateBy(Rotation2d.fromRadians(robotHeadingInRadians));
        Translation2d targetToRobotTranslationAbsolute = robotToTargetTranslationAbsolute.times(-1.0);
        Translation2d rv = target.plus(targetToRobotTranslationAbsolute);
        return rv;
    }

}