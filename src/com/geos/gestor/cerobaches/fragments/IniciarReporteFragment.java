package com.geos.gestor.cerobaches.fragments;

import com.geos.gestor.cerobaches.R;
import com.geos.gestor.cerobaches.interfaces.IniciarReporteListener;
import com.geos.gestor.cerobaches.libs.Datos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class IniciarReporteFragment extends Fragment{
	
	private static int TAKE_PICTURE = 1;
	
	private Context context;
	
	private EditText comentario;
	private ImageButton tomarFoto;
	private Button aceptar;
	
	private IniciarReporteListener listener;
	private Bitmap imageBitmap;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_iniciar_reporte,
				container, false);
		
		context = rootView.getContext();
		
		comentario = (EditText) rootView.findViewById(R.id.iniciar_reporte_edit_comentarios);
		tomarFoto = (ImageButton) rootView.findViewById(R.id.iniciar_reporte_image_foto);
		aceptar = (Button) rootView.findViewById(R.id.iniciar_reporte_button_aceptar);
		
		tomarFoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent i =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//				
//				startActivityForResult(i, TAKE_PICTURE);
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
			        startActivityForResult(takePictureIntent, TAKE_PICTURE);
			    }
			}
		});
		
		aceptar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listener != null){
					listener.onClickIniciarReporte(""+Datos.ESTATUS_INICIO_DE_LABORES
							, comentario.getText().toString(), imageBitmap);
				}
			}
		});
		
		return rootView;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//		if (requestCode == TAKE_PICTURE && resultCode== Activity.RESULT_OK && intent != null){
//		if (resultCode == Activity.RESULT_OK){    
//			// get bundle
//            Bundle extras = intent.getExtras();
// 
//            // get bitmap
//            Bitmap bitMap = (Bitmap) extras.get("data");
//            tomarFoto.setImageBitmap(bitMap);
//            img_url = intent.getData().toString();
//            
////            urlImagen = intent.getData();
////            tomarFoto.setEnabled(false);
//            aceptar.setEnabled(true);
//        }
		if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK) {
	        Bundle extras = intent.getExtras();
	        imageBitmap = (Bitmap) extras.get("data");
	        tomarFoto.setImageBitmap(imageBitmap);
	        aceptar.setEnabled(true);
//	        img_url = intent.getData().toString();
	    }
    }
	
	public void setListener(IniciarReporteListener listener){
		this.listener = listener;
	}
}
