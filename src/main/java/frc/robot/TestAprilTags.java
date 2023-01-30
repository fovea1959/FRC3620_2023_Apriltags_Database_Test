package frc.robot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;

public class TestAprilTags {

    static private AprilTagFieldLayout aprilTag2023FieldLayout = null;

    public static AprilTagFieldLayout getAprilTag2023FieldLayout() throws IOException {
        if (aprilTag2023FieldLayout == null) {
            aprilTag2023FieldLayout = AprilTagFieldLayout.loadFromResource(AprilTagFields.k2023ChargedUp.m_resourceFile);
        }
        return aprilTag2023FieldLayout;
    }
}