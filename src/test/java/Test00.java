import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import frc.robot.TestAprilTags;

/** Add your docs here. */
public class Test00 {

    @Test
    public void t00() throws IOException {
        AprilTagFieldLayout a = TestAprilTags.getAprilTagFieldLayout();
        System.out.println(a);

        System.out.println(a.getTagPose(0).orElse(null));
        Optional<Pose3d> o1 = a.getTagPose(1);
        System.out.println(o1.get());

    }

    @Test
    public void t01() {
        Rotation3d rAboutY = new Rotation3d (0, Units.degreesToRadians(-90), 0);
        System.out.println (rAboutY);
        Pose3d p0 = new Pose3d();
        System.out.println (p0.relativeTo(p0));
        Pose3d p1 = new Pose3d(0, 0, 0, rAboutY);
        System.out.println (p1.relativeTo(p0));
        Pose3d p2 = new Pose3d(1, 1, 1, rAboutY);
        System.out.println (p2);
        System.out.println (p2.toPose2d());
        System.out.println (p2.relativeTo(p0));
        System.out.println (p2.relativeTo(p1));
    }
}
