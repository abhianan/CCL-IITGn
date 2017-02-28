package com.example.cclcricketcombatleague;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.json.JSONArray;
import org.json.JSONException;
import com.example.cclcricketcombatleague.R.drawable;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class MainActivity extends android.support.v4.app.FragmentActivity
		implements TabListener {

	ActionBar actionBar;
	ProgressDialog loader;
	public Context con;
	private static Context context;
	String topBowlerUrl = "http://students.iitgn.ac.in/CCL/app/json.php?password=cclapp2014&type=players&orderby1=wicketsTaken%20DESC&limit=10";
	String topBatsmanUrl = "http://students.iitgn.ac.in/CCL/app/json.php?password=cclapp2014&type=players&orderby1=runsScored%20DESC&limit=10";
	String teamUrl = " http://students.iitgn.ac.in/CCL/app/json.php?password=cclapp2014&type=teamcompositions&orderby1=teamScore%20DESC&orderby2=,teamRate%20DESC";
	String fixturesUrl = "http://students.iitgn.ac.in/CCL/app/json.php?password=cclapp2014&type=fixtures&orderby1=startTime%20ASC";

	// ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		actionBar = getActionBar();

		// ViewPager viewPager = (ViewPager) findViewById(R.id.pager);

		// FragmentManager fManager = getFragmentManager();
		// viewPager.setAdapter(new MyAdapter( t));

		// try {
		// Thread.sleep(3000);
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// a.dismiss();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		ActionBar.Tab tab1 = actionBar.newTab();
		tab1.setText("Team Ranking");
		tab1.setTabListener(this);
		actionBar.addTab(tab1);
		Log.e("CCL", "Tab1");

		ActionBar.Tab tab2 = actionBar.newTab();
		tab2.setText("Ranking");
		tab2.setTabListener(this);
		actionBar.addTab(tab2);

		ActionBar.Tab tab3 = actionBar.newTab();
		tab3.setText("Schedule");
		tab3.setTabListener(this);
		actionBar.addTab(tab3);
		con = getAppContext();
		MainActivity.context = getApplicationContext();

	}

	public static Context getAppContext() {
		return context;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private boolean isNetworkAvailable() {
	    ConnectivityManager connectivityManager 
	          = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		Sync sync1 = new Sync();
		Sync sync2 = new Sync();
		Sync sync3 = new Sync();
		Log.d("CCl", "Page Selected" + tab.getPosition());
		if (tab.getPosition() == 0) {
			
			if(isNetworkAvailable()){
				setContentView(R.layout.fragment_team_ranking);
				loader = ProgressDialog.show(this, "Loading", "Please Wait");
				sync1.execute(teamUrl, "0");
			}
			else{
				setContentView(R.layout.activity_main);
				RelativeLayout tb = (RelativeLayout) findViewById(R.id.pager);
				TextView notConnected = new TextView(this);
				notConnected.setText("No Internet Connection");
				notConnected.setTextColor(Color.BLACK);
				tb.addView(notConnected);
			}
			
		} else if (tab.getPosition() == 1) {
			
			if(isNetworkAvailable()){
				setContentView(R.layout.fragment_ranking);
			loader = ProgressDialog.show(this, "Loading", "Please Wait");
			sync2.execute(topBatsmanUrl, "1", topBowlerUrl);
			}
			else{
				setContentView(R.layout.activity_main);
				RelativeLayout tb = (RelativeLayout) findViewById(R.id.pager);
				TextView notConnected = new TextView(this);
				notConnected.setText("No Internet Connection");
				notConnected.setTextColor(Color.BLACK);
				tb.addView(notConnected);
			}

		} else if (tab.getPosition() == 2) {
			
			if(isNetworkAvailable()){
				setContentView(R.layout.fragment_schedule);
			loader = ProgressDialog.show(this, "Loading", "Please Wait");
			sync3.execute(fixturesUrl, "2");
			}
			else{
				setContentView(R.layout.activity_main);
				RelativeLayout tb = (RelativeLayout) findViewById(R.id.pager);
				TextView notConnected = new TextView(this);
				notConnected.setText("No Internet Connection");
				notConnected.setTextColor(Color.BLACK);
				tb.addView(notConnected);
			}
		}

	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	class Sync extends AsyncTask<String, Void, JSONArray> {
		// ProgressDialog dialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected JSONArray doInBackground(String... params) {
			try {
				URL ccl = new URL(params[0]);
				URLConnection tc;
				tc = ccl.openConnection();
			
				BufferedReader in = new BufferedReader(new InputStreamReader(
						tc.getInputStream()));
				String line;
				line = in.readLine();
				JSONArray jsonArray = new JSONArray(line);

				if (params.length == 3) {
					URL ccl2 = new URL(params[2]);
					tc = ccl2.openConnection();
					in = new BufferedReader(new InputStreamReader(
							tc.getInputStream()));
					line = in.readLine();
					JSONArray second = new JSONArray(line);
					for (int i = 0; i < second.length() - 1; ++i) {
						jsonArray.put(second.getJSONObject(i));
					}
				}

				jsonArray.put(params[1]);
				Log.i("CCL", jsonArray.toString());
				return jsonArray;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONArray result) {
			// TODO Auto-generated method stub
			Context context = MainActivity.getAppContext();
			loader.dismiss();
			TableLayout tb = (TableLayout) findViewById(R.id.pager);
			int a = -1;

			try {
				a = result.getInt(result.length() - 1);
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if (a == 0) {
				for (int i = 0; i <= 8; ++i) {
					ImageView logo = new ImageView(context);
					TextView teamName = new TextView(context);
					TextView points = new TextView(context);
					TextView netRate = new TextView(context);
					TableRow row = new TableRow(context);
					LayoutParams lp = new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT);

					row.setMinimumHeight(70);
					row.setGravity(Gravity.CENTER_VERTICAL);

					try {
						logo.setImageResource(logoName(result.getJSONObject(i)
								.getString("teamName")));
						logo.setMaxHeight(10);
						logo.setMaxWidth(10);

						teamName.setText(result.getJSONObject(i).getString(
								"teamName"));
						teamName.setTextColor(Color.rgb(0, 0, 0));
						//teamName.setPadding(30, 0, 0, 0);
						teamName.setGravity(Gravity.LEFT);
						teamName.setMaxWidth(150);

						points.setText(result.getJSONObject(i).getString(
								"teamScore"));
						points.setTextColor(Color.rgb(0, 0, 0));
						points.setGravity(Gravity.CENTER);
						netRate.setText(result.getJSONObject(i).getString(
								"teamRate"));
						netRate.setTextColor(Color.rgb(0, 0, 0));
						netRate.setGravity(Gravity.LEFT);
						row.addView(logo);
						row.addView(teamName);
						row.addView(points);
						row.addView(netRate);
						tb.addView(row);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} else if (a == 1) {
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT);
				for (int i = 0; i <= 9; i++) {
					TextView rank = new TextView(context);
					TextView playerName = new TextView(context);
					ImageView logo = new ImageView(context);
					TextView runs = new TextView(context);
					TableRow row = new TableRow(context);
					row.setMinimumHeight(70);
					row.setGravity(Gravity.CENTER_VERTICAL);

					try {
						rank.setText(Integer.toString(i + 1));
						rank.setTextColor(Color.rgb(0, 0, 0));
						rank.setGravity(Gravity.CENTER);

						playerName.setText(result.getJSONObject(i).getString(
								"playerName"));
						playerName.setTextColor(Color.rgb(0, 0, 0));
						playerName.setPadding(30, 0, 0, 0);
						playerName.setGravity(Gravity.LEFT);

						logo.setImageResource(logoName(result.getJSONObject(i)
								.getString("teamName")));
						logo.setMaxHeight(10);
						logo.setMaxWidth(10);

						runs.setText(result.getJSONObject(i).getString(
								"runsScored"));
						runs.setTextColor(Color.rgb(0, 0, 0));
						runs.setPadding(30, 0, 0, 0);
						runs.setGravity(Gravity.CENTER);

						// row.addView(rank);
						if (i == 0) {
							row.setBackgroundColor(Color.parseColor("#ee7600"));
							rank.setTextColor(Color.rgb(255, 255, 255));
							playerName.setTextColor(Color.rgb(255, 255, 255));
							runs.setTextColor(Color.rgb(255, 255, 255));
						}
						row.addView(rank);
						row.addView(playerName);
						row.addView(logo);
						row.addView(runs);
						tb.addView(row, lp);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
				TableRow SpaceRow = new TableRow(context);
				SpaceRow.setMinimumHeight(70);
				tb.addView(SpaceRow, lp);

				TextView rankHeader = new TextView(context);
				TextView playerNameHeader = new TextView(context);
				ImageView logoHeader = new ImageView(context);
				TextView wicketsHeader = new TextView(context);
				TableRow rowHeader = new TableRow(context);

				rowHeader.setMinimumHeight(70);
				rowHeader.setGravity(Gravity.CENTER_VERTICAL);
				rowHeader.setBackgroundColor(Color.parseColor("#0198E1"));
				// rowHeader.setPadding(0, 40, 0, 0);

				rankHeader.setText("#");
				rankHeader.setTextSize(16);
				rankHeader.setTextColor(Color.rgb(255, 255, 255));
				rankHeader.setGravity(Gravity.CENTER);

				playerNameHeader.setText("Bowler");
				playerNameHeader.setTextSize(16);
				playerNameHeader.setTextColor(Color.rgb(255, 255, 255));
				playerNameHeader.setPadding(30, 0, 0, 0);
				playerNameHeader.setGravity(Gravity.LEFT);

				logoHeader.setImageResource(drawable.ic_launcher);
				logoHeader.setMaxHeight(10);
				logoHeader.setMaxWidth(10);

				wicketsHeader.setText("Wickets");
				wicketsHeader.setTextSize(16);
				wicketsHeader.setPadding(30, 0, 0, 0);
				wicketsHeader.setTextColor(Color.rgb(255, 255, 255));
				wicketsHeader.setPadding(30, 0, 0, 0);
				wicketsHeader.setGravity(Gravity.CENTER);

				rowHeader.addView(rankHeader);
				rowHeader.addView(playerNameHeader);
				rowHeader.addView(logoHeader);
				rowHeader.addView(wicketsHeader);
				tb.addView(rowHeader, lp);

				for (int i = 11; i <= 20; i++) {
					TextView rank = new TextView(context);
					TextView playerName = new TextView(context);
					ImageView logo = new ImageView(context);
					TextView wickets = new TextView(context);
					TableRow row = new TableRow(context);
					row.setMinimumHeight(70);
					row.setGravity(Gravity.CENTER_VERTICAL);

					try {
						rank.setText(Integer.toString(i - 10));
						rank.setTextColor(Color.rgb(0, 0, 0));
						rank.setGravity(Gravity.CENTER);

						playerName.setText(result.getJSONObject(i).getString(
								"playerName"));
						playerName.setTextColor(Color.rgb(0, 0, 0));
						playerName.setPadding(30, 0, 0, 0);
						playerName.setGravity(Gravity.LEFT);

						logo.setImageResource(logoName(result.getJSONObject(i)
								.getString("teamName")));
						logo.setMaxHeight(10);
						logo.setMaxWidth(10);

						wickets.setText(result.getJSONObject(i).getString(
								"wicketsTaken"));
						wickets.setTextColor(Color.rgb(0, 0, 0));
						wickets.setPadding(30, 0, 0, 0);
						wickets.setGravity(Gravity.CENTER);

						if (i == 11) {
							row.setBackgroundColor(Color.parseColor("#9B30FF"));
							rank.setTextColor(Color.rgb(255, 255, 255));
							playerName.setTextColor(Color.rgb(255, 255, 255));
							wickets.setTextColor(Color.rgb(255, 255, 255));
						}
						// row.addView(rank);
						row.addView(rank);
						row.addView(playerName);
						row.addView(logo);
						row.addView(wickets);
						tb.addView(row, lp);

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			} else if (a == 2) {
				Time now = new Time();
				SimpleDateFormat ab = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				now.setToNow();

				java.util.Date matchTime = null;
				java.util.Date previousMatchTime = new java.util.Date(40000000);
				//previousMatchTime.
				// Calendar matchTime = null;
				// ab.parse();
				Log.d("CCL", previousMatchTime.toString());
				for (int i = 0; i < result.length() - 2; ++i) {
					try {
						// Time matchTime = new
						// Time(result.getJSONObject(i).getString("startTime"));

						try {
							matchTime = ab.parse(result.getJSONObject(i)
									.getString("startTime"));
							// previousMatchTime = matchTime;
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.d("CCL", matchTime.toString());
						// Log.d("CCL",);
						// if(matchTime.getTime() - now.toMillis(true) >= 0){
						Log.d("CCL",
								previousMatchTime.toString().substring(0, 10));
						Log.d("CCL", matchTime.toString().substring(0, 10));
						if (!previousMatchTime.toString().substring(0, 10)
								.equals(matchTime.toString().substring(0, 10))) {
							TableRow timeRow = new TableRow(context);
							Log.d("CCL", "if");
							timeRow.setMinimumHeight(70);
							// timeRow.set
							timeRow.setGravity(Gravity.CENTER_VERTICAL);
							timeRow.setBackgroundColor(Color
									.parseColor("#0198E1"));
							timeRow.setGravity(Gravity.CENTER_HORIZONTAL);
							TextView timeNote = new TextView(context);
							timeNote.setText(matchTime.toString()
									.substring(0, 10).replaceFirst(" ", ", "));
							timeNote.setTextColor(Color.rgb(255, 255, 255));
							LayoutParams lp = new LayoutParams(
									LayoutParams.MATCH_PARENT,
									LayoutParams.MATCH_PARENT);
							timeRow.addView(timeNote);
							tb.addView(timeRow, lp);
							previousMatchTime = matchTime;
						}
						// TableRow timeStampRow = new TableRow(context);
						// Log.d("CCL", "if");
						// timeStampRow.setMinimumHeight(70);
						// //timeRow.set
						// timeStampRow.setGravity(Gravity.CENTER_HORIZONTAL);

						TextView timeStampNote = new TextView(context);
						timeStampNote.setText(matchTime.toString().substring(
								11, 16));
						timeStampNote.setGravity(Gravity.CENTER);
						//timeStampNote.setMaxWidth(1);
						timeStampNote.setTextColor(Color.rgb(0, 0, 0));
						// timeStampRow.addView(timeStampNote);
						LayoutParams lp = new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						// tb.addView(timeStampRow, lp);

						TableRow matchRow = new TableRow(context);
						matchRow.setMinimumHeight(70);
						matchRow.setGravity(Gravity.CENTER_VERTICAL);
						ImageView team1Logo = new ImageView(context);
						ImageView team2Logo = new ImageView(context);
						team1Logo.setImageResource(logoName(result
								.getJSONObject(i).getString("teamName1")));
						team2Logo.setImageResource(logoName(result
								.getJSONObject(i).getString("teamName2")));
						TextView matchNote = new TextView(context);
						matchNote.setText(result.getJSONObject(i).getString(
								"teamName1")
								+ " vs "
								+ result.getJSONObject(i)
										.getString("teamName2"));
						Display display = getWindowManager().getDefaultDisplay();
						Point size = new Point();
						display.getSize(size);
						int width = size.x;
						int height = size.y;
						Log.i("CCL", new Integer(width).toString());
						Log.i("CCL", new Integer(height).toString());
						if(width < 550){
						matchNote.setMaxWidth(250);
						}
						else{
							matchNote.setMaxWidth(400);
						}
						matchNote.setMinHeight(70);
						matchNote.setGravity(Gravity.LEFT);
						matchNote.setTextColor(Color.rgb(0, 0, 0));
						//matchNote.setMaxWidth(450);
						matchRow.addView(timeStampNote);
						matchRow.addView(team1Logo);
						matchRow.addView(matchNote);
						matchRow.addView(team2Logo);

						tb.addView(matchRow, lp);

						// }
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			super.onPostExecute(result);
		}

		int logoName(String teamName) {
			if (teamName.equals("Royal Blacks")) {
				return R.drawable.royal_blacks;
			} else if (teamName.equals("Blue Wings")) {
				return R.drawable.blue_wings;
			} else if (teamName.equals("Golden Eagles")) {
				return R.drawable.golden_eagles;
			} else if (teamName.equals("Green Cobras")) {
				return R.drawable.green_cobras;
			} else if (teamName.equals("Grey Wolverines")) {
				return R.drawable.grey_wolverines;
			} else if (teamName.equals("Maroon Gladiators")) {
				return R.drawable.maroon_gladiators;
			} else if (teamName.equals("Red Rebellions")) {
				return R.drawable.red_rebellions;
			} else if (teamName.equals("The Yellow Submarines")) {
				return R.drawable.the_yellow_submarines;
			} else if (teamName.equals("White Wolves")) {
				return R.drawable.white_wolves;
			}

			return 0;

		}

	}

}

/*
 * class MyAdapter extends FragmentPagerAdapter {
 * 
 * public MyAdapter(FragmentManager fm) { super(fm); // TODO Auto-generated
 * constructor stub }
 * 
 * @Override public Fragment getItem(int arg0) { Fragment fragment = null;
 * if(arg0 == 0) { fragment = new TeamRanking(); } else if(arg0 == 1) { fragment
 * = new Ranking(); } else if(arg0 == 2) { fragment = new Schedule(); } return
 * fragment; }
 * 
 * @Override public int getCount() { // TODO Auto-generated method stub return
 * 3; } }
 */

