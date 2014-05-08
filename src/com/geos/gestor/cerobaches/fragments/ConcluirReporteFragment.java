package com.geos.gestor.cerobaches.fragments;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.geos.gestor.cerobaches.R;
import com.geos.gestor.cerobaches.interfaces.ConcluirReporteListener;
import com.geos.gestor.cerobaches.libs.Datos;

public class ConcluirReporteFragment extends Fragment {
	
	static int TAKE_PICTURE = 1;
	
	// Componentes de la UI
	private EditText costales;
	private ImageButton tomarFoto;
	private Button aceptar;
	private Spinner spinnerFin;
	private EditText comentario;
	
	// Context
	private Context context;
	
	// 
	private String estatus;
	private String urlImagen;
	private ConcluirReporteListener listener;
	private Bitmap imageBitmap;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(
				R.layout.fragment_concluir_reporte, container, false);
		
		context = rootView.getContext();
		
		costales = (EditText) rootView.findViewById(R.id.concluir_reporte_fragment_edit_costales);
		tomarFoto = (ImageButton) rootView.findViewById(R.id.concluir_reporte_image_foto);
		aceptar = (Button) rootView.findViewById(R.id.concluir_reporte_button_aceptar);
		spinnerFin = (Spinner) rootView.findViewById(R.id.concluir_reporte_fragment_spiner_status_fin);
		comentario = (EditText) rootView.findViewById(R.id.concluir_reporte_edit_comentarios);
		
		addItemsToSpiner();
		
		tomarFoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//		        
//		        startActivityForResult(intent, TAKE_PICTURE);
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
					listener.onClickConcluirReporte(
							estatus, comentario.getText().toString(), 
							imageBitmap, costales.getText().toString());
				}
			}
		});
		
		spinnerFin.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(position == 0){
		        	estatus = "" + Datos.ESTATUS_FINALIZADA_POSITIVAMENTE;
		        }else{
		        	estatus = "" + Datos.ESTATUS_FINALIZADA_NEGATIVAMENTE;
		        }
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		
		return rootView;
	}
	
	public void setListener(ConcluirReporteListener listener){
		this.listener = listener;
	}
	
	public void addItemsToSpiner(){
		List<String> list = new ArrayList<String>();
		list.add("Fin. Positivamente");
		list.add("Fin. Negativamente");
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
			android.R.layout.simple_spinner_item, list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerFin.setAdapter(dataAdapter);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//		if (requestCode == TAKE_PICTURE && resultCode == Activity.RESULT_OK && intent != null){
//		if (resultCode == Activity.RESULT_OK){
//			// get bundle
//            Bundle extras = intent.getExtras();
// 
//            // get bitmap
////            Log.i("URL", intent.getData().toString());
//            Bitmap bitMap = (Bitmap) extras.get("data");
//            tomarFoto.setImageBitmap(bitMap);
//            
//            urlImagen = intent.getData().toString();
////            imgenButton.setEnabled(false);
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
}
