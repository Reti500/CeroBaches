package com.geos.gestor.cerobaches.fragments;

import java.io.FileOutputStream;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback;

import com.geos.gestor.cerobaches.R;
import com.geos.gestor.cerobaches.interfaces.OrdenListener;
import com.geos.gestor.cerobaches.libs.Datos;
import com.geos.gestor.cerobaches.libs.DownloadImageTask;
import com.geos.gestor.cerobaches.libs.Files;
import com.geos.gestor.cerobaches.libs.Orden;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class OrdenFragment extends Fragment {
	// Context
	Context context;
	
	// Componentes de la UI
	private TextView idOrden;
	private TextView estado;
	private TextView direccion;
	private TextView referencia;
	private TextView descripcion;
	private Button reporte;
	private ImageButton imgButtonMapa;
	
	private Datos datos;
	private OrdenListener listener;
	
	private int orden_position;
	
	private Files files;
	
	private GoogleMap myMap;
	private FrameLayout frameMapa;
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_orden,container, false);
		
		context = rootView.getContext();
		datos = Datos.getInstance();
		files = new Files();
		
		frameMapa = (FrameLayout) rootView.findViewById(R.id.frameMapa);
		frameMapa.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				irMapa();
			}
		});
		
		idOrden = (TextView) rootView.findViewById(R.id.frag_orden_text_solicitud_id);
		estado = (TextView) rootView.findViewById(R.id.frag_orden_text_estado);
		direccion = (TextView) rootView.findViewById(R.id.frag_orden_text_direccion);
		referencia = (TextView) rootView.findViewById(R.id.frag_orden_text_referencia);
		descripcion = (TextView) rootView.findViewById(R.id.frag_orden_text_descripcion);
		reporte = (Button) rootView.findViewById(R.id.frag_orden_button_reporte);
//		imgButtonMapa = (ImageButton) rootView.findViewById(R.id.frag_orden_image_button_mapa);
		
		reporte.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(listener != null){
					listener.onClickReporte();
				}
			}
		});
		
		new DownloadImageTask(imgButtonMapa).execute("http://todoiphone.net/wp-content/uploads/2012/10/iOS6-Apple-Maps-icon.jpeg");
		
		return rootView;
	}
	
	public void setOrdenPosition(int position){
		this.orden_position = position;
		Orden or = datos.data_orden.get(position);
		
		estado.setText("Estatus: " + or.getEstatus());
		idOrden.setText("Solicitud: " + or.getIdSolicitud());
		direccion.setText("Direccion: " +
				or.getCalle() + " " + or.getNumExterior() + " " + or.getNumInterior()
				+ ", " + or.getColonia() + ", " + or.getMunicipio() + ", "
				+ or.getEntidad() + ", " + or.getCP());
		descripcion.setText("Referencia: " + or.getDescripcion());
		referencia.setText("Descripcion: " + or.getReferencia());
	}
	
	public void setListener(OrdenListener listener){
		this.listener = listener;
	}
	
	public void CaptureMapScreen(){
		SnapshotReadyCallback callback = new SnapshotReadyCallback() {
			Bitmap bitmap;
			
			@Override
			public void onSnapshotReady(Bitmap snapshot) {
				// TODO Auto-generated method stub
				bitmap = snapshot;
				
				try {
					FileOutputStream out = new FileOutputStream(
							files.BACHES_PHOTOS_DIRECTORY + "pothoMap" + System.currentTimeMillis() + ".png");
					bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		};
		
		myMap.snapshot(callback);
	}
	
	public void irMapa(){
		Toast.makeText(context, "CLICK", Toast.LENGTH_SHORT).show();
	}
}
