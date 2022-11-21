import java.io.*;
import java.util.*;

import org.junit.Test;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.apriltag.*;

public class TestGenerateJsonForTagtrackerDevelopers {
    @Test
    public void t00() throws IOException {
        AprilTagFieldLayout a = getAprilTagFieldLayout();
        a.serialize ("test_apriltags.json");
        AprilTagFieldLayout a2 = new AprilTagFieldLayout("test_apriltags.json");
    }

    static Rotation3d r3d_westwall = new Rotation3d(0, Units.degreesToRadians(-90), 0);
    static Rotation3d r3d_southwall = new Rotation3d(0, Units.degreesToRadians(-90), Units.degreesToRadians(-90));

    static AprilTagFieldLayout getAprilTagFieldLayout() {
        List<AprilTag> rv = new ArrayList<>();
        rv.add (new AprilTag(1, new Pose3d (0, 1.0, 1.0, r3d_westwall)));
        rv.add (new AprilTag(2, new Pose3d (0, 2.0, 2.0, r3d_westwall)));
        rv.add (new AprilTag(11, new Pose3d (1.0, 0.0, 1.0, r3d_southwall)));
        rv.add (new AprilTag(12, new Pose3d (2.0, 0.0, 2.0, r3d_southwall)));
        return new AprilTagFieldLayout(rv, 20.0, 10.0);
    }
}