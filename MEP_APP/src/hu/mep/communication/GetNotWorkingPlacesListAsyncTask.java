package hu.mep.communication;

import hu.mep.datamodells.Place;
import hu.mep.datamodells.Session;
import hu.mep.mep_app.FragmentLevel2RemoteMonitorings;
import hu.mep.utils.deserializers.NotWorkingPlacesDeserializer;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map.Entry;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetNotWorkingPlacesListAsyncTask extends AsyncTask<Void, Void, Void> {

	private static final String TAG = "GetNotWorkingPlacesListAsyncTask";
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String resourceURI;
	
	public GetNotWorkingPlacesListAsyncTask() {
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		this.resourceURI = "ios_getHibasTf.php?userId=" + Session.getActualUser().getMepID();
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		Log.e(TAG, "doinbackground...");
		String response = "";
		response = RealCommunicator.dohttpGet(resourceURI);
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter( HashMap.class, new NotWorkingPlacesDeserializer());
		Gson gson = gsonBuilder.create();
		HashMap<String, String> container = gson.fromJson(response, HashMap.class);
		
		for (Place act : Session.getActualUser().getUsersPlaces().getPlaces()) {
			act.setWorkingProperly(true);
		}
		for (Entry<String, String> act : container.entrySet()) {
			Session.getActualUser().getUsersPlaces().findPlaceByID(act.getKey()).setWorkingProperly(false);
			if(act.getValue().equals("null")) {
				Session.getActualUser().getUsersPlaces().findPlaceByID(act.getKey()).setLastWorkingText(
						"A rendszer több mint 1 hónapja nem elérhető!");
				Log.e(TAG, "A rendszer több mint 1 hónapja nem elérhető!");
			} else {
				Session.getActualUser().getUsersPlaces().findPlaceByID(act.getKey()).setLastWorkingText(
					"A rendszer legutóbb ekkor volt elérhető: " + act.getValue());
				Log.e(TAG, "A rendszer legutóbb ekkor volt elérhető: " + act.getValue());
			}
		}		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		FragmentLevel2RemoteMonitorings.placeAdapter.notifyDataSetChanged();
	}
	
}