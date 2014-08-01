package hu.mep.utils.adapters;

import hu.mep.datamodells.Place;
import hu.mep.datamodells.Session;
import hu.mep.mep_app.R;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceListAdapter extends ArrayAdapter<Place> {

	private LayoutInflater inflater;
	
	public PlaceListAdapter(Context context, int listviewID,
			List<Place> listOfPlaces) {
		super(context, listviewID, listOfPlaces);
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public long getItemId(int position) {
		return super.getItemId(position);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
	
		View newRow = inflater.inflate(R.layout.remote_monitorings_listitem, parent, false);
		TextView textview = (TextView) newRow.findViewById(R.id.remote_monitorings_listitem_textview_for_name);
		textview.setText(Session.getActualUser().getUsersPlaces().getPlaces().get(position).getName());
		
		ImageView imageView = (ImageView) newRow.findViewById(R.id.remote_monitorings_listitem_imageview_for_status);
		if( Session.getActualUser().getUsersPlaces().getPlaces().get(position).isWorkingProperly() ) {
			imageView.setVisibility(View.GONE);
		} else {
			imageView.setVisibility(View.VISIBLE);
		}
		return newRow;
	}

}
