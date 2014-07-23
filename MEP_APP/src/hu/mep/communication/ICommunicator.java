package hu.mep.communication;

import java.util.Calendar;

import android.app.Activity;

public interface ICommunicator {

	public void getChatPartners();

	public void getChatMessages();

	public void sendChatMessage(String text);

	public void getTopicList();

	public void getChartNames(boolean forRemoteMonitoring);

	public void authenticateUser(Activity activity, String username, String password);

	public void getGalleryURLsAndPictures();

	public void getActualChart(Calendar beginDate, Calendar endDate);

	public void getActualChart();
	
	public void registrateUser(String fullName, String email, String userName, String password);

	public void getActualRemoteMonitoringSettings();
	
	
}
