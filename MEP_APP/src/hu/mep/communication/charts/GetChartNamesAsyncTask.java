package hu.mep.communication.charts;

import hu.mep.communication.RealCommunicator;
import hu.mep.datamodells.Session;
import hu.mep.datamodells.charts.AllChartNames;
import hu.mep.datamodells.charts.ChartName;
import hu.mep.utils.deserializers.ChartNamesDeserializer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetChartNamesAsyncTask extends	AsyncTask<Void, Void, Void> {

	private Activity activity;
	private String resourceURI;
	private boolean forRemoteMonitoring;
	private ProgressDialog pd;
	//private static final String TAG = "GetChartNamesAsyncTask";

	public GetChartNamesAsyncTask(Activity activity, boolean forRemoteMonitoring) {
		super();
		this.activity = activity;
		this.forRemoteMonitoring = forRemoteMonitoring;
		
		this.pd = new ProgressDialog(this.activity);
		this.pd.setCancelable(false);
		this.pd.setMessage("Kapcsolódás a szerverhez...");
	}

	private String getSSZS() {
		String result = "";
		if (!forRemoteMonitoring) {
			Iterator<Integer> iterator = Session.getActualTopic()
					.getTabSerialNumbers().iterator();
			while (iterator.hasNext()) {
				result += String.valueOf(iterator.next());
				if (iterator.hasNext()) {
					result += ",";
				}
			}
		}
		return result;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		Session.setProgressDialog(pd);
		Session.showProgressDialog();
		
		if (forRemoteMonitoring) {
			resourceURI = "ios_getChartNames.php?tsz1_id="
					+ Session.getActualRemoteMonitoring().getID();
			} else {
			resourceURI = "ios_getChartNames.php?tsz1_id="
					+ Session.getActualTopic().getTopicID() + "&sszs="
					+ getSSZS();
		}
	}

	@Override
	protected Void doInBackground(Void... params) {
		String response = "";
		response = RealCommunicator.dohttpGet(resourceURI);
		//Log.e(TAG, "getChartNames response: " + response);
		
		if(response.equals("null")) {
			List<ChartName> allChartInfoContainer = new ArrayList<ChartName>();
			Session.setAllChartNames(allChartInfoContainer);
		} else {
			GsonBuilder gsonBuilder = new GsonBuilder();
			gsonBuilder.registerTypeAdapter(AllChartNames.class, new ChartNamesDeserializer());
			Gson gson = gsonBuilder.create();
			AllChartNames allChartInfo = gson.fromJson(response, AllChartNames.class);
			Session.setAllChartNames(allChartInfo.getAllChartNames());
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Session.dismissAndMakeNullProgressDialog();
		
		GetChartsAsyncTask chartGetter = new GetChartsAsyncTask(activity, forRemoteMonitoring, null, null);
		chartGetter.execute();
	}

}
