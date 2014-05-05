package com.geos.gestor.cerobaches.libs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

public class Files {
	// Private constants
	private static final String LOG_TAG = "Files";
	private static final String LOG_TAG_ERROR = "Files Error";
	private static final String BACHES_DIRECTORY_BASE = "/Android/data/com.geos.gestor.cerobaches/";
	
	//Public contants
	public final String BACHES_CACHE_DIRECTORY = BACHES_DIRECTORY_BASE + "cache/";
	public final String BACHES_PHOTOS_DIRECTORY = BACHES_DIRECTORY_BASE + "photos/";
	
	public Files(){
		super();
	}
	
	public void createDirectories(){
		File ext_dir = Environment.getExternalStorageDirectory();
		
		File file_cache = new File(ext_dir.getAbsoluteFile() + BACHES_CACHE_DIRECTORY);
		File file_photos = new File(ext_dir.getAbsoluteFile() + BACHES_PHOTOS_DIRECTORY);
		
		if(!file_cache.exists())
			file_cache.mkdirs();
		
		if(!file_photos.exists())
			file_photos.mkdirs();
	}
	
	public void saveFile(String file_name, String data, String path){
		try {
            File ext_dir = Environment.getExternalStorageDirectory();
            File file = new File(ext_dir.getAbsoluteFile() + path, file_name);
            
            OutputStreamWriter osw = new OutputStreamWriter(
                    new FileOutputStream(file));
            osw.write(data);
            osw.flush();
            osw.close();
            
            Log.i(LOG_TAG, "File created!!");
        } catch (IOException ioe) {
        	Log.e(LOG_TAG_ERROR, ioe.getMessage());
        }
	}
	
	public String readFile(String file_name, String path){
		String data = "";
    	
		try {
			File ext_dir = Environment.getExternalStorageDirectory();
			File file = new File(ext_dir.getAbsolutePath() + path, file_name);
        
            FileInputStream fIn = new FileInputStream(file);
            
            InputStreamReader archivo = new InputStreamReader(fIn);
            
            BufferedReader br = new BufferedReader(archivo);
            
            String line = br.readLine();
            
            while (line != null) {
                data += line;
                line = br.readLine();
            }
            
            br.close();
            archivo.close();
            
            Log.i(LOG_TAG, "File data -> " + data);
            
            return data;
        } catch (IOException e) {
        	Log.e(LOG_TAG_ERROR, e.getMessage());
        	
        	return null;
        }
//        return data;
	}
	
	public void saveImage(String image_name, String path, Bitmap img){
		try {
			File ext_dir = Environment.getExternalStorageDirectory();
            File file = new File(ext_dir.getAbsoluteFile() + path, image_name);
            
			FileOutputStream out = new FileOutputStream(file);
			
			img.compress(Bitmap.CompressFormat.JPEG, 90, out);
			
			Log.i(LOG_TAG, "Image SAVE!");
		} catch (Exception e) {
			Log.e(LOG_TAG_ERROR, e.getMessage());
		}
	}
	
	public boolean existsFile(String file_name, String path){
    	File ext_dir = Environment.getExternalStorageDirectory();
        File file = new File(ext_dir.getAbsolutePath() + path, file_name);
        
        return file.exists();
    }
}
