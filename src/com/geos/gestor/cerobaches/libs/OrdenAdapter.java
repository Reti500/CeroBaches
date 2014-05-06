package com.geos.gestor.cerobaches.libs;

import java.util.ArrayList;

import com.geos.gestor.cerobaches.R;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrdenAdapter extends ArrayAdapter<Orden> {
	private Context context = null;
	private Activity contextAc = null;
	private int layoutResourceId;
//	private Orden[] data = null;
	private ArrayList<Orden> data = null;
	OrdenHolder holder = null;
	
	public OrdenAdapter(Context context, int layoutResourceId, ArrayList<Orden> data){
		super(context, layoutResourceId, data);
		
		this.context = context;
		this.layoutResourceId = layoutResourceId;
		this.data = data;
	}
	
	public OrdenAdapter(Fragment context, ArrayList<Orden> data){
		super(context.getActivity(), R.layout.list_item_row, data);
		
		this.contextAc = context.getActivity();
		this.layoutResourceId = R.layout.list_item_row;
		this.data = data;
	}
	
	public View getView(int position, View contentView, ViewGroup parent){
		View row = contentView;
	
		if(row == null){
			if(context != null && contextAc == null){
				LayoutInflater inflater = ((Activity)context).getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);
			}else if(context == null && contextAc != null){
				LayoutInflater inflater = contextAc.getLayoutInflater();
				row = inflater.inflate(layoutResourceId, parent, false);
			}
			
			holder = new OrdenHolder();
			holder.txtDireccion = (TextView)row.findViewById(R.id.txtDireccion);
			holder.txtFecha = (TextView)row.findViewById(R.id.txtFecha);
			holder.imgStatus = (ImageView) row.findViewById(R.id.imgReporteStatus);
			
			
			row.setTag(holder);
		}else{
			holder = (OrdenHolder)row.getTag();
		}
		
//		Orden orden = data[position];
		Orden orden = data.get(position);
		
		holder.txtDireccion.setText(orden.getCalle() + ", " + orden.getColonia());
		holder.txtFecha.setText(orden.getFecha());
		
		int idresource = 1;
		
		switch(Integer.parseInt(orden.getEstatusId())){
			case 7:
				idresource = R.drawable.circle_grey;
				break;
			case 8:
				idresource = R.drawable.circle_yellow;
				break;
			case 9:
				idresource = R.drawable.circle_green;
				break;
			case 10:
				idresource = R.drawable.circle_red;
				break;
			default:
				idresource = R.drawable.circle_grey;
				break;
		}
		
		holder.imgStatus.setImageResource(idresource);
		
		return row;
	}
	
	public Orden getOrden(int pos){
		return data.get(pos);
	}
	
	public void update(ArrayList<Orden> new_data){
		this.data = new_data;
		
		notifyDataSetChanged();
	}
	
	static class OrdenHolder{
		TextView txtDireccion;
		TextView txtFecha;
		ImageView imgStatus;
	}
}
