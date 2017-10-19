package cs6250.benchmarkingsuite.imageprocessing.effects;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;
import android.graphics.Bitmap;

public class Frame 
{
	private Bitmap bmp;
	private Mat enc_mat;
	private byte[] byteArray;

	public Frame(Mat m)
	{
		enc_mat = m;
		Imgproc.cvtColor(m, m, Imgproc.COLOR_RGB2BGRA);
		bmp = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(m, bmp);
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		byteArray = stream.toByteArray();
	}

	public Frame(ByteBuffer pixels)
	{
		byteArray = pixels.array();
		MatOfByte m = new MatOfByte(pixels.array());
		enc_mat = Imgcodecs.imdecode(m, -1);
		bmp = Bitmap.createBitmap(m.cols(), m.rows(), Bitmap.Config.ARGB_8888);
		Utils.matToBitmap(m, bmp);
	}

	public ByteBuffer getAsByteBuffer()
	{
		return ByteBuffer.wrap(byteArray);
	}

	public ByteBuffer getAsPngByteBuffer()
	{
		MatOfByte m = new MatOfByte();
		Imgcodecs.imencode(".png", enc_mat, m);
		return ByteBuffer.wrap(m.toArray());
	}

	public Bitmap getBitMap()
	{
		return bmp;
	}

	public byte[] getByteArr()
	{
		return byteArray;
	}

	public Mat getMat()
	{
		return enc_mat;
	}
}
