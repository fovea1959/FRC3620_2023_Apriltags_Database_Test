package frc.robot;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;

/** Add your docs here. */
public class FieldCalculations {

    /**
     * Given a couple of points on the field and our headings to them, determine our position.
     * https://stackoverflow.com/a/37090237/17887564
     * 
     * TODO: fix case where headings are too close to each other, or if they are almost in
     * opposite directions.
     * 
     * @param t1
     * @param t1_heading_radians heading to point a (in radians)
     * @param t2
     * @param t2_heading_radians heading to point b (in radians)
     * @param expected
     * @return
     */
    public static Translation2d locateViaTriangulation (Translation2d t1, double t1_heading_radians, Translation2d t2, double t2_heading_radians) {
        double c1 = Math.cos(t1_heading_radians);
        double s1 = Math.sin(t1_heading_radians);
        double c2 = Math.cos(t2_heading_radians);
        double s2 = Math.sin(t2_heading_radians);
        double dx = t2.getX() - t1.getX();
        double dy = t2.getY() - t1.getY();
        double t;
        if ( c1 > 0.01  ||  c1 < -0.01 ) {
            double k = s1 / c1;
            double den = c2 * k - s2;
            t = ( dy - dx * k) / den;
        } else {
            double k = c1 / s1;
            double den = s2 * k - c2;
            t = ( dx - dy * k) / den;
        }
        double x = t2.getX() + t * c2;
        double y = t2.getY() + t * s2;
        Translation2d rv = new Translation2d(x, y);
        return rv;
    }

    /**
     * https://www.thecivilengineer.org/education/professional-examinations-preparation/calculation-examples/calculation-example-find-the-coordinates-of-the-intersection-of-two-lines
     * @param ta
     * @param ta_heading_radians
     * @param tb
     * @param tb_heading_radians
     * @return
     */
    public static Translation2d locateViaTriangulation3 (Translation2d ta, double ta_heading_radians, Translation2d tb, double tb_heading_radians) {
        double tan_a = Math.tan(ta_heading_radians);
        if (tan_a > 10000) {
            tan_a = Double.POSITIVE_INFINITY;
        } else if (tan_a < -10000) {
            tan_a = Double.NEGATIVE_INFINITY;
        }
        double x_a = ta.getX();
        double y_a = ta.getY();
        double tan_b = Math.tan(tb_heading_radians);
        if (tan_b > 10000) {
            tan_b = Double.POSITIVE_INFINITY;
        } else if (tan_b < -10000) {
            tan_b = Double.NEGATIVE_INFINITY;
        }
        double x_b = tb.getX();
        double y_b = tb.getY();
        System.out.println ("tans " + tan_a + " " + tan_b);

        if (Double.isInfinite(tan_a) && Double.isInfinite(tan_b)) {
            return null;
        }
        if (tan_a == tan_b) {
            return null;
        }

        double x = 0.0;
        double y = 0.0;
        if (Double.isFinite(tan_b)) {
            // tan_b is usable
            if (Double.isInfinite(tan_a)) {
                // line to t_a is vertical
                x = x_a;
            } else {
                x = (y_a - y_b - tan_a*x_a + tan_b*x_b) / (tan_b - tan_a);
            }
            y = y_b + tan_b * (x - x_b);
        } else {
            // line to t_b is vertical
            x = x_b;
            y = y_a + tan_a * (x - x_a);
        }
        return new Translation2d(x, y);
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
        Translation2d targetToRobotTranslationAbsolute = robotToTargetTranslationAbsolute.unaryMinus();
        Translation2d rv = target.plus(targetToRobotTranslationAbsolute);
        return rv;
    }

    public static Translation3d locateViaTarget (Translation3d target, Translation3d targetFromCamera, double robotHeadingInRadians, double cameraTiltUpInRadians) {
        Translation3d robotToTargetTranslationRotated = targetFromCamera.rotateBy(new Rotation3d(0, -cameraTiltUpInRadians, robotHeadingInRadians));
        Translation3d targetToRobotTranslation = robotToTargetTranslationRotated.unaryMinus();
        Translation3d rv = target.plus(targetToRobotTranslation);
        return rv;
    }

}