package com.geos.gestor.cerobaches.libs;

import java.util.ArrayList;

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
}
