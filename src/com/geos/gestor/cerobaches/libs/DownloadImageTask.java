package com.geos.gestor.cerobaches.libs;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

	ImageView bmImage;
	ImageButton btImage;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
    }
    
    public DownloadImageTask(ImageButton bmImage) {
        this.btImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
//        Data data = new Data();
        
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        
//        data.guardarImagen("hola.jpg", Data.GESTOR_PHOTOS_DIRECTORY_NAME, mIcon11);
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
    	if(bmImage != null){
    		bmImage.setImageBitmap(result);
    	}else if(btImage != null){
    		btImage.setImageBitmap(result);
    	}
    }
    
}
