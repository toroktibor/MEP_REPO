package hu.mep.mep_app;

import hu.mep.charts.BarChartView;
import hu.mep.datamodells.charts.BarChart;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentLevel3ShowBarChart extends Fragment {
	
	public BarChart chartValues;
	private boolean forRemoteMonitoring;
	
	public FragmentLevel3ShowBarChart(BarChart barChartValues, boolean forRemoteMonitoring) {
		this.chartValues = barChartValues;
		this.forRemoteMonitoring = forRemoteMonitoring;
		}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		chartValues.sortChartValues();
		View rootView = new BarChartView(getActivity(), chartValues, forRemoteMonitoring);
		
		return rootView;
	}
	
}
