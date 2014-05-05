package com.geos.gestor.cerobaches.libs;

import java.util.ArrayList;

public class Datos {
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
