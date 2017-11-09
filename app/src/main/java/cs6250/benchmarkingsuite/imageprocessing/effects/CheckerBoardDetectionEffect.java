package cs6250.benchmarkingsuite.imageprocessing.effects;
import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Core;
import org.opencv.core.TermCriteria;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;

import org.opencv.core.Size;
import org.opencv.core.TermCriteria;
import org.opencv.imgproc.Imgproc;
import org.opencv.calib3d.*;

import cs6250.benchmarkingsuite.imageprocessing.staticfiles.Storage;

import static org.opencv.imgproc.Imgproc.resize;

public class CheckerBoardDetectionEffect extends Effect {
    final int width = 5;
    final int height = 4;
    final int square_size = 20;
    Mat pic;

//    public CheckerBoardDetectionEffect(Mat pic) {
//        this.pic = pic;
//    }

    public CheckerBoardDetectionEffect() {
    }

    @Override
    public Mat applyTo(Mat frame) {
        Mat chessboard = Storage.getChessboard();

        Size size = new Size(frame.cols() * 0.2, frame.rows() * 0.2);
        resize(chessboard, chessboard, size);
        Mat pic = new Mat();
        pic = chessboard.clone();


        Mat gray = new Mat();
        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_RGBA2GRAY);

        Size pat_Size = new Size(width, height);

        MatOfPoint2f corners = new MatOfPoint2f(new Mat(square_size, 1,
                CvType.CV_32FC2));

        Calib3d.findChessboardCorners(frame, pat_Size, corners,
                Calib3d.CALIB_CB_ADAPTIVE_THRESH
                        | Calib3d.CALIB_CB_FILTER_QUADS);

        Size winSize = new Size(11, 11);
        Size zeroZone = new Size(-1, -1);
        TermCriteria criteria = new TermCriteria(TermCriteria.EPS
                + TermCriteria.MAX_ITER, 30, 0.1);
        if (0 == corners.checkVector(2))
            return frame;
        Imgproc.cornerSubPix(gray, corners, winSize, zeroZone, criteria);

        double[] picture = new double[8];
        double[] borders = new double[8];

        picture[0] = 0;
        picture[1] = 0;
        picture[2] = pic.width();
        picture[3] = 0;

        picture[4] = pic.width();
        picture[5] = pic.height();
        picture[6] = 0;
        picture[7] = pic.height();

        Point[] p_arr = corners.toArray();
        assert (p_arr.length == square_size);

        borders[0] = p_arr[0].x;
        borders[1] = p_arr[0].y;
        borders[2] = p_arr[4].x;
        borders[3] = p_arr[4].y;

        borders[4] = p_arr[19].x;
        borders[5] = p_arr[19].y;
        borders[6] = p_arr[15].x;
        borders[7] = p_arr[15].y;

        Mat pointsIn = new Mat(4, 1, CvType.CV_32FC2);
        Mat pointsRes = new Mat(4, 1, CvType.CV_32FC2);

        pointsIn.put(0, 0, picture);
        pointsRes.put(0, 0, borders);

        Mat warp_mat = Imgproc.getPerspectiveTransform(pointsIn, pointsRes);

        Mat blank = Mat.zeros(frame.size(), frame.type());
        // Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);
        // Mat blank = Mat.zeros(frame.size(),frame.type());
        // Mat blank = new Mat(frame.size(),frame.depth()); //NOTE: this causes
        // mat channel errors
        // Mat blank = frame.clone();
        //blank.setTo(new Scalar(255));

        Core.bitwise_not(blank,blank);
        Mat dst = Mat.zeros(frame.size(), frame.type());
        Imgproc.warpPerspective(pic, dst, warp_mat, pic.size());
        Imgproc.warpPerspective(blank, blank, warp_mat, blank.size());
        Core.bitwise_not(blank,blank);
        Core.bitwise_and(blank, frame,frame);
        //Core.add(frame, dst, frame);
        Core.bitwise_or(frame, dst, frame);

        //Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);
        // Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGB2RGBA);
        return frame;
    }

    public String toString() {
        return "Checker Detection";
    }

}