import java.io.*;
import java.util.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.apriltag.*;
import edu.wpi.first.apriltag.AprilTagFieldLayout.OriginPosition;

public class TestGenerateJson2022Field {
    static AprilTagFieldLayout official, mine;

    @BeforeAll
    public static void setup() throws IOException {
        try {
            official = new AprilTagFieldLayout("2022-rapidreact.json");
            mine = makeAprilTagFieldLayout();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Test
    public void t00_compareall() throws Exception {
        compare(Alliance.Red, 0);
        compare(Alliance.Blue, 10);
        compare(Alliance.Red, 2);
        compare(Alliance.Blue, 12);
        compare(Alliance.Red, 10);
        compare(Alliance.Blue, 0);
        compare(Alliance.Red, 12);
        compare(Alliance.Blue, 2);
    }

    void compare (Alliance a, int id) {
        OriginPosition origin = (a == Alliance.Blue) ? AprilTagFieldLayout.OriginPosition.kBlueAllianceWallRightSide : AprilTagFieldLayout.OriginPosition.kRedAllianceWallRightSide;
        official.setOrigin(origin);
        mine.setOrigin(origin);
        System.err.println (a.toString() + " " + id);
        System.err.println (official.getTagPose(id));
        System.err.println (mine.getTagPose(id));
    }

    // the blue wall
    static Rotation3d r3d_faces_east = new Rotation3d(0, 0, 0);
    static Rotation3d r3d_faces_north = new Rotation3d(0, 0, Units.degreesToRadians(90));
    static Rotation3d r3d_faces_south = new Rotation3d(0, 0, Units.degreesToRadians(-90));
    static Rotation3d r3d_faces_west = new Rotation3d(0, 0, Units.degreesToRadians(180));

    static AprilTagFieldLayout makeAprilTagFieldLayout() {
        List<AprilTag> rv = new ArrayList<>();
        rv.add (new AprilTag(0, new Pose3d (-0.004, 7.579, 0.886, r3d_faces_east)));
        rv.add (new AprilTag(2, new Pose3d (3.068, 5.331, 1.376, r3d_faces_south)));
        rv.add (new AprilTag(10, new Pose3d (16.463, 0.651, 0.886, r3d_faces_west)));
        rv.add (new AprilTag(12, new Pose3d (13.391, 2.90, 1.376, r3d_faces_north)));

        return new AprilTagFieldLayout(rv, 16.4592, 8.2296);
    }
}