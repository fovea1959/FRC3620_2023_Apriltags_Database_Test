package frc.robot;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;

public class TestAprilTags {

    static private AprilTagFieldLayout aprilTagFieldLayout = null;

    static Rotation3d r3d = new Rotation3d(0, Units.degreesToRadians(-90), 0);

    private static AprilTag makeOne(int id, double y, double z) {
        y = Units.inchesToMeters(y);
        z = Units.inchesToMeters(z);
        Pose3d pose3d = new Pose3d(0.0, y, z, r3d);
        return new AprilTag (id, pose3d);
    }

    private static AprilTagFieldLayout make() {
        List<AprilTag> rv = new ArrayList<>();
        rv.add (makeOne(1, 9.0, 50.25));
        rv.add (makeOne(2, 84.0, 51.5));
        rv.add (makeOne(3, 211.0, 50.5));

        return new AprilTagFieldLayout(rv, 20.0, 10.0);
    }

    public static AprilTagFieldLayout getAprilTagFieldLayout() {
        if (aprilTagFieldLayout == null) {
            aprilTagFieldLayout = make();
        }
        return aprilTagFieldLayout;
    }
}