package hu.mep.mep_app;

import hu.mep.datamodells.Session;
import hu.mep.datamodells.Topic;
import hu.mep.utils.others.FragmentLevel2EventHandler;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class ActivityLevel2NEW extends ActionBarActivity implements
		ActionBar.TabListener, FragmentLevel2EventHandler {

	ActionBar mActionBar;
	Tab tabTopics;
	Tab tabRemoteMonitorings;
	Tab tabChat;

	ActivityLevel2SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_secondlevel);

		mSectionsPagerAdapter = new ActivityLevel2SectionsPagerAdapter(
				getSupportFragmentManager());

		mViewPager = (ViewPager) findViewById(R.id.activity_secondlevel_pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						mActionBar.setSelectedNavigationItem(position);
					}
				});

		mActionBar = getSupportActionBar();
		mActionBar.setDisplayShowHomeEnabled(true);
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setDisplayShowTitleEnabled(false);
		mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		addTabsForActionBar();
		
		if (Session.getActualUser().isMekut()) {
			Session.getInstance(getApplicationContext())
					.getActualCommunicationInterface().getTopicList();
		}
	}

	private void addTabsForActionBar() {
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			mActionBar.addTab(mActionBar.newTab()
					// .setText(mSectionsPagerAdapter.getPageTitle(i))
					.setIcon(mSectionsPagerAdapter.getPageIcon(i))
					.setTabListener(this));
		}
	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTopicSelected(Topic selectedTopic) {
		Session.setActualTopic(selectedTopic);
		Intent i = new Intent(this, ActivityLevel3ShowTopic.class);
		startActivity(i);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_secondlevel_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_logoff) {
			onBackPressed();
			Session.stopContactRefresher();
			Session.stopMessageRefresher();
			
			Session.setActualChart(null);
			Session.setActualChartInfoContainer(null);
			Session.setAllChartInfoContainer(null);
			Session.setActualTopic(null);
			Session.setAllTopicsList(null);
			
			Session.setChatMessagesList(null);
			Session.setActualChatPartner(null);
			Session.setActualChatContactList(null);

			Session.setActualRemoteMonitoring(null);
			
			Session.setActualUser(null);
			
		}
		return super.onOptionsItemSelected(item);
	}

}
