import java.io.*;
import java.util.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.Nat;
import edu.wpi.first.math.Num;
import edu.wpi.first.math.geometry.*;
import edu.wpi.first.math.numbers.N4;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.apriltag.*;

public class TestGenerateJsonAverageJoesFakeField {
    // the blue wall
    Rotation3d r3d_faces_east = new Rotation3d(0, 0, 0);
    Rotation3d r3d_faces_north = new Rotation3d(0, 0, Units.degreesToRadians(90));
    Rotation3d r3d_faces_south = new Rotation3d(0, 0, Units.degreesToRadians(-90));
    Rotation3d r3d_faces_west = new Rotation3d(0, 0, Units.degreesToRadians(180));

    @Test
    public void t_matrix() throws JsonProcessingException {
        // tested against https://ninja-calc.mbedded.ninja/calculators/mathematics/geometry/3d-rotations
        t_matrix1(new Quaternion(0.7071067811865476, 0, 0, 0.7071067811865476));
        t_matrix1(new Quaternion(1, 0, 0, 0));
    }

    public void t_matrix1 (Quaternion q) throws JsonProcessingException {
        Rotation3d r = new Rotation3d(q);
        Pose3d p = new Pose3d(0, 0, 0, r);
        System.out.println(q);
        System.out.println("matrix1");
        System.out.println (matrix1(p));
        // System.out.println (new ObjectMapper().writeValueAsString(j(matrix1(p))));
        System.out.println("matrix2");
        System.out.println (matrix2(p));
        // System.out.println (new ObjectMapper().writeValueAsString(j(matrix2(p))));
    }

    @Test
    public void makeAprilTagFieldLayout() throws IOException, JsonGenerationException, JsonMappingException {
        List<AprilTag> rv = new ArrayList<>();
        rv.add (new AprilTag(0, pose_in_inches(114, 0, 13.5, r3d_faces_north)));
        rv.add (new AprilTag(1, pose_in_inches(0, 80.25, 41, r3d_faces_east)));
        rv.add (new AprilTag(2, pose_in_inches(0, 80.25+74, 41, r3d_faces_east)));
        rv.add (new AprilTag(3, pose_in_inches(0, 80.25+74+134, 41, r3d_faces_east)));
        rv.add (new AprilTag(4, pose_in_inches(126, 80.25+74+134+50, 13.5, r3d_faces_south)));

        AprilTagFieldLayout a = new AprilTagFieldLayout(rv, 16.4592, 8.2296);
        a.serialize("average_joes_fake_2022.json");

        Matrix<N4, N4> m = matrix2(rv.get(0).pose);
        //System.out.println (m);
        ObjectMapper om = new ObjectMapper();
        om.enable(SerializationFeature.INDENT_OUTPUT);
        //System.out.println (om.writeValueAsString(m));
        //System.out.println (om.writeValueAsString(j(m)));

        TagTracker tt = new TagTracker();
        tt.tag_family = "tag16h5";
        for (AprilTag a1 : rv) {
            tt.tags.add(new TagTrackerTag(a1.ID, Units.inchesToMeters(6), a1.pose));
        }
        System.out.println (om.writeValueAsString(tt));
        om.writeValue(new File("average_joes_fake_2022_tagtracker.json"), tt);
    }

    Pose3d pose_in_inches (double x, double y, double z, Rotation3d r3d) {
        return new Pose3d(Units.inchesToMeters(x), Units.inchesToMeters(y), Units.inchesToMeters(z), r3d);
    }

    /**
     * https://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToMatrix/index.htm
     * and 
     * https://www.flipcode.com/documents/matrfaq.html#Q54
     * @param pose
     * @return
     */
    static Matrix<N4, N4> matrix1 (Pose3d pose) {
        var t = pose.getTranslation();
        var tx = t.getX();
        var ty = t.getY();
        var tz = t.getZ();

        Quaternion q = pose.getRotation().getQuaternion();

        var qw = q.getW();
        var qx = q.getX();
        var qy = q.getY();
        var qz = q.getZ();

        double invs = 1 / (qx*qx + qy*qy + qz*qz + qw*qw);
        
        //# First row of the rotation matrix
        var r00 = (1 - 2*qy*qy - 2*qz*qz) / invs;
        var r01 = (2 * (qx*qy - qz*qw)) / invs;
        var r02 = (2 * (qx*qz + qy*qw)) / invs;
        
        // # Second row of the rotation matrix
        var r10 = (2 * (qx*qy + qz*qw)) / invs;
        var r11 = (1 - 2*qx*qx - 2*qz*qz) / invs;
        var r12 = (2 * (qy*qz - qx*qw)) / invs;
        
        // # Third row of the rotation matrix
        var r20 = (2 * (qx*qz - qy*qw)) / invs;
        var r21 = (2 * (qy*qz + qx*qw)) / invs;
        var r22 = (1 - 2*qx*qx - 2*qy*qy) / invs;

        Matrix<N4, N4> m = Matrix.mat(Nat.N4(), Nat.N4()).fill (
            r00, r01, r02, tx,
            r10, r11, r12, ty,
            r20, r21, r22, tz,
            0, 0, 0, 1);
        
        return m;
    }

    /**
     * https://www.euclideanspace.com/maths/geometry/rotations/conversions/quaternionToMatrix/index.htm
     * @param pose
     * @return
     */
    static Matrix<N4, N4> matrix2 (Pose3d pose) {
        var t = pose.getTranslation();
        var tx = t.getX();
        var ty = t.getY();
        var tz = t.getZ();

        Quaternion q = pose.getRotation().getQuaternion();

        var qw = q.getW();
        var qx = q.getX();
        var qy = q.getY();
        var qz = q.getZ();

        double sqw = qw*qw;
        double sqx = qx*qx;
        double sqy = qy*qy;
        double sqz = qz*qz;
        
        double invs = 1 / (sqx + sqy + sqz + sqw);
        var m00 = ( sqx - sqy - sqz + sqw)*invs ; // since sqw + sqx + sqy + sqz =1/invs*invs
        var m11 = (-sqx + sqy - sqz + sqw)*invs ;
        var m22 = (-sqx - sqy + sqz + sqw)*invs ;
        
        double tmp1 = qx*qy;
        double tmp2 = qz*qw;
        var m10 = 2.0 * (tmp1 + tmp2)*invs ;
        var m01 = 2.0 * (tmp1 - tmp2)*invs ;
        
        tmp1 = qx*qz;
        tmp2 = qy*qw;
        var m20 = 2.0 * (tmp1 - tmp2)*invs ;
        var m02 = 2.0 * (tmp1 + tmp2)*invs ;
        tmp1 = qy*qz;
        tmp2 = qx*qw;
        var m21 = 2.0 * (tmp1 + tmp2)*invs ;
        var m12 = 2.0 * (tmp1 - tmp2)*invs ;
        
        Matrix<N4, N4> m = Matrix.mat(Nat.N4(), Nat.N4()).fill (
            m00, m01, m02, tx,
            m10, m11, m12, ty,
            m20, m21, m22, tz,
            0, 0, 0, 1);
        
        return m;
    }



    static public List<List<Double>> j (Matrix<? extends Num,? extends Num> m) {
        List<List<Double>> rv = new ArrayList<>();
        for (int i = 0; i < m.getNumRows(); i++) {
            List<Double> r = new ArrayList<>();
            rv.add(r);
            for (int j = 0; j < m.getNumCols(); j++) {
                r.add(m.get(i, j));
            }
        }
        return rv;
    }

    static public class TagTracker {
        public String tag_family;
        public List<TagTrackerTag> tags = new ArrayList<>();
    }

    static class TagTrackerTag {
        public double size;
        public int id;
        public List<List<Double>> transform1;
        public List<List<Double>> transform2;
        public Quaternion q;

        TagTrackerTag (int id, double size, Pose3d pose) {
            this.id = id;
            this.size = size;
            this.transform1 = j(matrix1(pose));
            this.transform2 = j(matrix2(pose));
            this.q = pose.getRotation().getQuaternion();
        }
    }

}