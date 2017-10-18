package cs6250.benchmarkingsuite.imageprocessing.effects;

import java.io.ByteArrayOutputStream;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import android.graphics.Bitmap;

public class Frame 
{
	private Bitmap bmp;
	private Mat enc_mat;
	private byte[] byteArray;

	Frame(Mat m)
	{
		Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2BGRA);
		bmp = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(m, bmp);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byteArray = stream.toByteArray();
	}

	Bitmap getBitMap()
	{
		return bmp;
	}

	byte[] getByteArr()
	{
		return byteArray;
	}

	Mat getMat()
	{
		return enc_mat;
	}
}
