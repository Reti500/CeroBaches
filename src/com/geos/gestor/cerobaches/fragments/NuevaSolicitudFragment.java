package com.geos.gestor.cerobaches.fragments;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.geos.gestor.cerobaches.R;
import com.geos.gestor.cerobaches.interfaces.NuevaSolicitudListener;
import com.geos.gestor.cerobaches.libs.BachesComunicador;
import com.geos.gestor.cerobaches.libs.Datos;
import com.geos.gestor.cerobaches.libs.Files;
import com.geos.gestor.cerobaches.libs.Comunicador.ResponseListener;
import com.geos.gestor.cerobaches.libs.Functions;
import com.geos.gestor.cerobaches.libs.GPSTracker;

public class NuevaSolicitudFragment extends Fragment {
	
	static int TAKE_PICTURE = 1;
	
	private Context context;
	
	private EditText calle;
	private EditText colonia;
	private EditText municipio;
	private EditText entidad;
	private EditText cp;
	private EditText descripcion;
	private EditText referencia;
	private EditText latitud;
	private EditText longitud;
	private ImageView foto;
	private Button crear;
	private Button tomarFoto;
	
	private GPSTracker gps;
	private ProgressDialog dialog;
	private String image_name;
	private String image_url;
	
	private ArrayList<String> listAddress;
	private NuevaSolicitudListener listener;
	private Bitmap imageBitmap;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_nueva_solicitud,
				container, false);
		
		context = rootView.getContext();
		listAddress = new ArrayList<String>();
		gps = new GPSTracker(context);
		
		calle = (EditText) rootView.findViewById(R.id.nueva_solicitud_edit_calle);
		colonia = (EditText) rootView.findViewById(R.id.nueva_solicitud_edit_colonia);
		municipio = (EditText) rootView.findViewById(R.id.nueva_solicitud_edit_municipio);
		entidad = (EditText) rootView.findViewById(R.id.nueva_solicitud_edit_entidad);
		cp = (EditText) rootView.findViewById(R.id.nueva_solicitud_edit_cp);
		descripcion = (EditText) rootView.findViewById(R.id.nueva_solicitud_edit_descripcion);
		referencia = (EditText) rootView.findViewById(R.id.nueva_solicitud_edit_referencia);
		latitud = (EditText) rootView.findViewById(R.id.nueva_solicitud_edit_latitud);
		longitud = (EditText) rootView.findViewById(R.id.nueva_solicitud_edit_longitud);
		foto = (ImageView) rootView.findViewById(R.id.nueva_solicitud_image_foto);
		crear = (Button) rootView.findViewById(R.id.nueva_solicitud_button_crear);
		tomarFoto = (Button) rootView.findViewById(R.id.nueva_solicitud_button_tomar_foto);
		
		
		crear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				dialog = ProgressDialog.show(context, "Please wait ...", "Creando la solicitud", true);
				
				final Handler run = new Handler();
				run.post(new Runnable() {

					@Override
					public void run() {
						crearSolicitud();
					}
				});
			}
		});
		
		tomarFoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// create intent with ACTION_IMAGE_CAPTURE action 
//		        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		        
//		        startActivityForResult(intent, TAKE_PICTURE);
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
			        startActivityForResult(takePictureIntent, TAKE_PICTURE);
			    }
			}
		});
		
		dialog = ProgressDialog.show(context, "Please wait ...", "Buscando tu ubicacion", true);
		
		final Handler run = new Handler();
		run.post(new Runnable() {

			@Override
			public void run() {
				parseLocation();
			}
		});
		
		return rootView;
	}
	
	public void setListener(NuevaSolicitudListener listener){
		this.listener = listener;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        if (requestCode == TAKE_PICTURE && resultCode== Activity.RESULT_OK && intent != null){
//		if (resultCode == Activity.RESULT_OK){
//			// get bundle
//            Bundle extras = intent.getExtras();
// 
//            // get bitmap
//            Bitmap bitMap = (Bitmap) extras.get("data");
//            foto.setImageBitmap(bitMap);
//            
//            urlImagen = intent.getData().toString();
//        }
		if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
	        Bundle extras = intent.getExtras();
	        imageBitmap = (Bitmap) extras.get("data");
	        foto.setImageBitmap(imageBitmap);
//	        urlImagen = intent.getData().toString();
	    }
    }
	
	public void crearSolicitud(){
		BachesComunicador sendData = BachesComunicador.getInstance();
		
		sendData.createSolicitud(
				calle.getText().toString(), colonia.getText().toString(), 
				municipio.getText().toString(), entidad.getText().toString(),
				cp.getText().toString(), latitud.getText().toString(), 
				longitud.getText().toString(), descripcion.getText().toString(),
				referencia.getText().toString(), new ResponseListener() {
					
					@Override
					public void success(String val) {
						// TODO Auto-generated method stub
						
//						if(progress.isShowing()){
//							progress.dismiss();
//						}
						
						try{
							JSONObject json = new JSONObject(val);
							String id = json.getString("id");
							String fecha = Functions.linuxFormat(json.getString("fecha"));
							
							Datos datos = Datos.getInstance();
					        Files files = new Files();
					        
					        image_name = datos.getOrdenName(id, Datos.TIPO_IMAGEN_NUEVA, "jpg");
					        image_url = datos.createOrdenName(id, Datos.TIPO_IMAGEN_NUEVA, "jpg");
					        files.saveImage(image_name, files.BACHES_PHOTOS_DIRECTORY, imageBitmap);
						}catch(JSONException e){
							e.printStackTrace();
						}
						
						if(listener != null){
							listener.onClickCrearSolicitud(image_name, image_url);
						}
						
						dialog.dismiss();
					}
					
					@Override
					public void onfinal() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void error(String msg) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	}
	
	public void parseLocation(){
		BachesComunicador sendData = BachesComunicador.getInstance();
		
		sendData.location(""+gps.getLatitude(), ""+gps.getLongitude(), new ResponseListener() {
			
			@Override
			public void success(String val) {
				// TODO Auto-generated method stub
				try{
					JSONObject json = new JSONObject(val);
					JSONArray results = json.getJSONArray("results");
					
//							for(int i=0; i<results.length(); i++){
					JSONObject result = results.getJSONObject(0);
					JSONArray address = result.getJSONArray("address_components");
					
					for(int j=0; j<address.length(); j++){
						JSONObject component = address.getJSONObject(j);
						listAddress.add(component.getString("short_name"));
					}
					
					calle.setText(listAddress.get(1));
					colonia.setText(listAddress.get(2));
					municipio.setText(listAddress.get(3));
					entidad.setText(listAddress.get(4));
					latitud.setText(""+gps.getLatitude());
					longitud.setText(""+gps.getLongitude());
						
				} catch (JSONException e){
					e.printStackTrace();
				}
				
				dialog.dismiss();
			}
			
			@Override
			public void onfinal() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void error(String msg) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
}
