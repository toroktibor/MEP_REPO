package hu.mep.communication;

import hu.mep.datamodells.ChatContact;
import hu.mep.datamodells.ChatContactList;
import hu.mep.datamodells.ChatMessage;
import hu.mep.datamodells.ChatMessagesList;
import hu.mep.datamodells.Place;
import hu.mep.datamodells.PlaceList;
import hu.mep.datamodells.User;
import hu.mep.datamodells.Session;
import hu.mep.utils.ChatContactListDeserializer;
import hu.mep.utils.ChatMessagesListDeserializer;
import hu.mep.utils.MD5Encoder;
import hu.mep.utils.PlaceListDeserializer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Formatter.BigDecimalLayoutForm;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.AsyncTask.Status;
import android.util.Log;

public class RealCommunicator implements ICommunicator {

	private static final String TAG = "RealCommunicator.java";
	HttpClient httpclient;
	Context context;
	final String MainURL = "http://www.megujuloenergiapark.hu/";

	private static RealCommunicator instance = null;

	private RealCommunicator(Context context) {
		this.httpclient = new DefaultHttpClient(); // szükségtelen lesz az Asynctaskok után
		this.context = context;						//szükséges az AsyncTaskokhoz
	}

	public static synchronized RealCommunicator getInstance(Context ctx) {
		if (instance == null) {
			instance = new RealCommunicator(ctx);
		}
		return instance;
	}

	public String httpPost(String file, HashMap<String, String> post)
			throws ClientProtocolException, IOException {

		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

		Iterator it = post.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pairs = (Map.Entry) it.next();
			nameValuePairs.add(new BasicNameValuePair((String) pairs.getKey(),
					(String) pairs.getValue()));
		}

		HttpPost httppost = new HttpPost(MainURL + file);
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		HttpResponse response = httpclient.execute(httppost);
		String data = new BasicResponseHandler().handleResponse(response);
		return data;
	}

	@Override
	public void getChatMessages() {
		
		GetChatMessagesListAsyncTask getMessagesAsyncTask = new GetChatMessagesListAsyncTask(context, MainURL);

		try { // TODO! Ez a .get nem biztos, hogy kell a végére. Meg fogja fagyasztani az UIThread-et.
			// TODO! Ha a szálak onPostExecute() metódusa rendesen implementálva leszenk, a .get() nem kell!!!
			getMessagesAsyncTask.execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}

		/* Az aktuális üzenetlista kiíratása... tesztkiíratás */
		Log.e("RealCommunicator.getChatMessages()","Minden bejövő üzenet:");
		for (ChatMessage actMessage : Session.getInstance(context).getChatMessagesList().getChatMessagesList()) {
			Log.e("RealCommunicator.getChatMessages()", actMessage.toString());
		}
	}
	
	@Override
	public void authenticateUser(String username, String password) {
		/*HashMap<String, String> post = new HashMap<String, String>();
		String data = null;
		try {
			String link = "iphonelogin_do.php?username=" + username
					+ "&password=" + MD5Encoder.encodePasswordWithMD5(password);
			data = httpPost(link, post);

		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(PlaceList.class,
				new PlaceListDeserializer());
		Gson gson = gsonBuilder.create();
		User newUser = gson.fromJson(data, User.class);
		Session.getInstance(context).setActualUser(newUser);
		downloadProfilePictureForActualUser();*/
		if(NetThread.isOnline(context)) {
			Log.e("RealCommunicator", "ONLINE!!!!!!!!!");
		}
		else {
			Log.e("RealCommunicator", "OFFLINE!!!!!!!!!");
		}
		AuthenticationAsyncTask authenticationAsyncTask = new AuthenticationAsyncTask(context, username, password, MainURL);
		try {
			authenticationAsyncTask.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.e("RealCommunicator.authenticateUser()", "finished");
		
	}

	@Override
	public void getChatPartners() {
		/*HashMap<String, String> post = new HashMap<String, String>();

		String data = null;

		try {
			data = httpPost("ios_getContactList.php?userId="
					+ Session.getInstance(context).getActualUser().getMepID(), post);

			Log.e(TAG, MainURL + "ios_getContactList.php?userId="
					+ Session.getInstance(context).getActualUser().getMepID());

			Log.d(TAG, "getChatPartners() ==>" + data);
		} catch (ClientProtocolException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.registerTypeAdapter(ChatContactList.class,
				new ChatContactListDeserializer());
		Gson gson = gsonBuilder.create();
		ChatContactList contacts = gson.fromJson(data, ChatContactList.class);
		Session.getInstance(context).setActualChatContactList(contacts);

		for (ChatContact actContact : Session.getInstance(context)
				.getActualChatContactList().getContacts()) {
			//Log.e("CHATTÁRSAK!", actContact.getName());
			//Log.e("CHATTÁRSAK!", "Kép letöltése...");
			downloadProfilePictureForChatContact(actContact);
		}*/
		GetContactListAsyncTask getContactListAsyncTask = new GetContactListAsyncTask(context, MainURL);
		// TODO! Ide sem kell majd a .get() Most azért van ott, mert logolni akarom az eredményt, muszáj megvárni
		// a szál végrehajtódását!
		try {
			getContactListAsyncTask.execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Log.e("RealCommunicator.getChatPartners()","Minden chatpartner:");
		for (ChatContact actContacs : Session.getActualChatContactList().getContacts()) {
			Log.e("RealCommunicator.getChatPartners()", actContacs.toString());
		}
	}
/*


	public void downloadProfilePictureForChatContact(ChatContact contact) {
		try {
			Bitmap bmp;
			URL imgURL = new URL(contact.getImageURL());
			HttpURLConnection connection = (HttpURLConnection) imgURL
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			bmp = BitmapFactory.decodeStream(input);
			int fixSize = (bmp.getWidth() < bmp.getHeight() ? bmp.getWidth()
					: bmp.getHeight()); // Megnézzük, álló vagy fekvő
										// tájolású-e.
			bmp = Bitmap.createBitmap(bmp, 0, 0, fixSize, fixSize); // A
																	// rövidebb
																	// oldal
																	// szerint
																	// vágunk
																	// egy nagy
																	// négyzetre.
			bmp = Bitmap.createScaledBitmap(bmp, 200, 200, true); // Skálázás
																	// 200×200-as
																	// négyzetre.
			contact.setProfilePicture(bmp);

		} catch (IOException e) {
			e.printStackTrace();
			Log.e("getBmpFromUrl error: ", e.getMessage().toString());
			return;
		}
		return;
	}
*/
	
	public void downloadProfilePictureForActualUser() {
		try {

			HttpURLConnection connection = (HttpURLConnection) Session
					.getInstance(context).getActualUser().getImageURL()
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			Session.getInstance(context).getActualUser()
					.setProfilePicture(BitmapFactory.decodeStream(input));

		} catch (IOException e) {
			e.printStackTrace();
			Log.e("getBmpFromUrl error: ", e.getMessage().toString());
			return;
		}
		return;
	}
}
