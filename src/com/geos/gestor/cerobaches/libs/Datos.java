package com.geos.gestor.cerobaches.libs;

import java.util.ArrayList;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Datos {
	
	// PUBLIC CONSTANTS
	public static final int ESTATUS_CANCELADA = 1;
	public static final int ESTATUS_DUPLICADA = 2;
	public static final int ESTATUS_PRE_SOLICITUD = 3;
	public static final int ESTATUS_LISTA_PARA_VALIDAR = 4;
	public static final int ESTATUS_SOLICITUD_VALIDA = 5;
	public static final int ESTATUS_REVISANDO_SOLICITUD = 6;
	public static final int ESTATUS_ORDEN_DE_TRABAJO_EMITIDA = 7;
	public static final int ESTATUS_INICIO_DE_LABORES = 8;
	public static final int ESTATUS_FINALIZADA_POSITIVAMENTE = 9;
	public static final int ESTATUS_FINALIZADA_NEGATIVAMENTE = 10;
	public static final int ESTATUS_SOLICITUD_FINALIZADA_ES_REABIERTA = 11;
	public static final int ESTATUS_ENTREGADO = 12;
	public static final int ESTATUS_TERMINO_DE_LABORES = 13;
	public static final int ESTATUS_REVISANDO_VALIDES = 14;
	public static final int ESTATUS_ORDEN_DE_TRABAJO_LEIDA = 15;
	public static final int ESTATUS_ESPERANDO_FIN_DE_CONVOCATORIA = 16;
	public static final int ESTATUS_CANALIZAR = 17;
	
	public static final String TIPO_IMAGEN_NUEVA = "nueva";
	public static final String TIPO_IMAGEN_INICIO = "inicio";
	public static final String TIPO_IMAGEN_FIN = "fin";
	
	public static final String PICTURE_BUCKET = "data.saltillo.gestorsocial.mx";
	public static final String PICTURE__BASE_DIRECTORY = "solicitudes/";
	
	public static final String REPORTES_INICIO_FILE_NAME = "reportes_inicio.txt";
	public static final String REPORTES_FIN_FILE_NAME = "reportes_inicio.txt";
	public static final String REPORTES_NUEVOS = "repostes_nuevos.txt";
	public static final String IMAGENES_FILE_NAME = "images.txt";
	
	public static final String RESPONSE_OK = "OK";
	public static final String RESPONSE_ERROR = "ERROR";
	//-----------------------------------------------------------------------
	
	private static Datos instance;
	
	public ArrayList<Orden> data_orden;
	public OrdenAdapter adapter;
	
	public Datos(){
		super();
	}
	
	public static final Datos getInstance(){
		if(instance == null){
			instance = new Datos();
		}
		
		return instance;
	}
	
	public String createOrdenName(Orden orden, String tipo, String ext){
		String separador = "_";
		String url =
				PICTURE__BASE_DIRECTORY +
				orden.getIdSolicitud() + "/" +
				orden.getIdSolicitud() + separador +
				orden.getFechaLinux() + separador +
				tipo + "." + ext;
		
		return url;
	}
	
	public String createOrdenName(String id, String fecha, String tipo){
		String separador = "_";
		String url = PICTURE__BASE_DIRECTORY + id + "/" 
				+ id + separador + fecha + separador + tipo + "." + "jpg";
		
		return url;
	}
	
	public String getOrdenName(Orden orden, String tipo, String ext){
		String separador = "_";
		String url =
				orden.getIdSolicitud() + separador +
				orden.getFechaLinux() + separador +
				tipo + "." + ext;
		
		return url;
	}
	
	public String getOrdenName(String id, String fecha, String tipo){
		String separador = "_";
		String url = id + separador + fecha + separador + tipo + "." + "jpg";
		
		return url;
	}
	
	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}
}
