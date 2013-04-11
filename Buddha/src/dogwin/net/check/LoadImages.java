package dogwin.net.check;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.concurrent.ExecutionException;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
public class LoadImages{
	public Bitmap bitmap,rbitmap,bm;
	
	public Bitmap loadimgs(String url){
		AsyncjSONTask task = new AsyncjSONTask();
		try {
			rbitmap = task.execute(url).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rbitmap;
	}
	
	public class AsyncjSONTask extends AsyncTask<String , Void, Bitmap>{
		
		
		@Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	    }
	    @Override
	    protected Bitmap doInBackground(String... params) {
	    	String stringURL = params[0];
			try {
				URL url = new URL(stringURL);
				URI uri;
				try {
					uri = new URI(url.getProtocol(), url.getHost(),
					        url.getPath(), url.getQuery(), null);
					HttpURLConnection connection;
					try {
						connection = (HttpURLConnection) uri
						        .toURL().openConnection();
						connection.setDoInput(true);
				        connection.connect();
				        InputStream input = connection.getInputStream();
				        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

				        int bufferSize = 1024;
				        byte[] buffer = new byte[bufferSize];
				        int len = 0;
				        while ((len = input.read(buffer)) != -1) {
				            byteBuffer.write(buffer, 0, len);
				        }
				        byte[] img = byteBuffer.toByteArray();
				        byteBuffer.flush();
				        byteBuffer.close();
				        input.close();
				        bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
				        
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			        
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        return bitmap;
	    }
	    @Override
	    protected void onPostExecute(Bitmap result) {
	        super.onPostExecute(result);
	        /*
	        ImageButton image_btn = (ImageButton)findViewById(R.id.your_image_button_id);
	        image_btn.setImageBitmap(bitmap);*/
	    }
	}
}

