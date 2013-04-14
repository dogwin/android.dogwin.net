package dogwin.net.check;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitmapCreateNew {
	Bitmap bitmap;
	public BitmapCreateNew(Bitmap bm) {
		// TODO Auto-generated constructor stub
		this.bitmap = bm;
	}
	public Bitmap newimage(int newWidth,int newHeight){
		 
		// 获得图片的宽高
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		
	    // 计算缩放比例
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // 取得想要缩放的matrix参数
	    Matrix matrix = new Matrix();
	    matrix.postScale(scaleWidth, scaleHeight);
	    // 得到新的图片
	    Bitmap newbm = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix,true);
	    return newbm;
	}
}
