package cs6250.benchmarkingsuite.imageprocessing.effects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.tzutalin.dlib.Constants;
import com.tzutalin.dlib.FaceDet;
import com.tzutalin.dlib.VisionDetRet;

public class GrayscaleEffect extends Effect {

	@Override
	public Mat applyTo(Mat frame) {
		float resizeRatio = 1;
		Bitmap bmp = Bitmap.createBitmap(frame.cols(), frame.rows(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(frame, bmp);
		FaceDet faceDet = new FaceDet(Constants.getFaceShapeModelPath());

		List<VisionDetRet> results = faceDet.detect(bmp);
		org.opencv.core.Point tl = new org.opencv.core.Point();
		org.opencv.core.Point br = new org.opencv.core.Point();
		org.opencv.core.Point center = new org.opencv.core.Point();
		Scalar lineColor = new Scalar(255, 0, 0, 255);
		Scalar textColor = new Scalar(255, 0, 0, 255);
		int lineWidth = 3;

		for (VisionDetRet ret : results) {
			tl = new org.opencv.core.Point(ret.getTop(), ret.getLeft());
			br = new org.opencv.core.Point(ret.getBottom(), ret.getRight());

			Imgproc.rectangle(frame, tl, br, new Scalar(0, 255, 0, 255), 8);
			Imgproc.putText(frame, " Face" , tl,
					Core.FONT_HERSHEY_PLAIN, 1.5 , textColor);

			ArrayList<Point> landmarks = ret.getFaceLandmarks();
			for (Point point : landmarks) {
				Log.e("Point", "" + point.x + " " + point.y);
				center = new org.opencv.core.Point(point.x, point.y);
				Imgproc.circle(frame, center, 10, new Scalar(0, 255, 0, 255), 2);

			}
		}

		for (final VisionDetRet ret : results) {
			Log.e("Face Detect", "Face Detect");
		}

		return frame;
//		Mat newFrame = new Mat();
//		Imgproc.cvtColor(frame, newFrame, Imgproc.COLOR_RGBA2GRAY);
//		Imgproc.cvtColor(newFrame, newFrame, Imgproc.COLOR_GRAY2RGBA);
//
//		return newFrame;
	}

	public String toString() {
		return "Grayscale";
	}
}
