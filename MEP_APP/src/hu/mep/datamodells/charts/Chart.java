package hu.mep.datamodells.charts;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Chart {

	@SerializedName("elapse")
	private String elapse;
	
	@SerializedName("y")
	private String yAxisTitle;
	
	@SerializedName("charts")
	private List<SubChart> subCharts;

	public Chart(String elapse, String yAxisTitle) {
		super();
		this.elapse = elapse;
		this.yAxisTitle = yAxisTitle;
	}
	
	public String getElapse() {
		return elapse;
	}

	public String getyAxisTitle() {
		return yAxisTitle;
	}

	public List<SubChart> getSubCharts() {
		return subCharts;
	}
	
	public void setSubCharts(List<SubChart> subCharts) {
		this.subCharts = subCharts;
	}
	
	
	
}
