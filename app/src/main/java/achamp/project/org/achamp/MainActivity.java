package achamp.project.org.achamp;


import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

import achamp.project.org.achamp.AddingFriends.AddFriends_Fragment;
import achamp.project.org.achamp.CreatingEvents.CreateEvents_Fragment;
import achamp.project.org.achamp.CreatingEvents.PostingEvent_RetainedFragment;
import achamp.project.org.achamp.Login.LoginActivity;
import achamp.project.org.achamp.ViewingEvents.ViewEvents_Fragment;
import achamp.project.org.achamp.ViewingEvents.ViewEvents_Task;
import achamp.project.org.achamp.ViewingEvents.fragments.ListFrag;
import achamp.project.org.achamp.ViewingEvents.fragments.ViewEvents_RetainedFragment;

public class MainActivity extends FragmentActivity implements AddFriends_Fragment.OnFragmentInteractionListener, ViewEvents_Fragment.OnFragmentInteractionListener,
        CreateEvents_Fragment.OnFragmentInteractionListener,PostingEvent_RetainedFragment.OnFragmentInteractionListener, ViewEvents_RetainedFragment.OnFragmentInteractionListener, ListFrag.OnFragmentInteractionListener{

    private static final String DEMO_TAG = "demo_tag";
    private static final String POST_EVENT_TAG = "post_event_tag";
    private static final String VIEW_EVENT_RF_TAG = "view_event_rf_tag";
    private Demo_Fragment demo;

    private static AddFriends_Fragment addFriend;
    private static CreateEvents_Fragment createEvents;
    private static ViewEvents_Fragment viewEvents;

    private PostingEvent_RetainedFragment postEvent;
    private ViewEvents_RetainedFragment viewEvents_RF;

    private android.app.FragmentManager fm;

    private FragmentAdapter fAdapter;
    private ViewPager vPager;

    private final Handler handler = new Handler();


    private static final int NUM_ITEMS = 3;


    public static final String myurl = "http://" + "172.31.164.234" + ":3000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fm = getFragmentManager();

        LoadDemo();
        initialViewPager();
        initialPostEvent_RF();
        initialViewEvent_RF();

    }
    private void initialViewPager()
    {
        addFriend = AddFriends_Fragment.newInstance("a", "n");
        createEvents = CreateEvents_Fragment.newInstance("a", "n");
        viewEvents = ViewEvents_Fragment.newInstance("a", "n");

        fAdapter = new FragmentAdapter(getSupportFragmentManager());

        vPager = (ViewPager) findViewById(R.id.pager);
        vPager.setAdapter(fAdapter);
        PagerTitleStrip titleStrip = (PagerTitleStrip) findViewById(R.id.pager_tab_strip);
        titleStrip.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24);
    }

    private void initialPostEvent_RF() {
        postEvent = (PostingEvent_RetainedFragment) getFragmentManager().findFragmentByTag(POST_EVENT_TAG);

        if (postEvent == null)
        {
            postEvent = new PostingEvent_RetainedFragment();
            getFragmentManager().beginTransaction().add(postEvent, POST_EVENT_TAG).commit();
        }
    }

    private void initialViewEvent_RF()
    {
        viewEvents_RF = (ViewEvents_RetainedFragment) getFragmentManager().findFragmentByTag(VIEW_EVENT_RF_TAG);

        if(viewEvents_RF == null)
        {
            viewEvents_RF = new ViewEvents_RetainedFragment();
            getFragmentManager().beginTransaction().add(viewEvents_RF, VIEW_EVENT_RF_TAG).commit();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(requestCode == 5)
//        {
//            if(resultCode == RESULT_OK)
//            {
//                initMainActivityFragment();
//            }
//            else if(resultCode == RESULT_CANCELED){
//                finish();
//            }
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("usersession", MODE_PRIVATE);
        String username = prefs.getString("username", "");
        String password = prefs.getString("password", "");

        new ATask().execute(username, password);
    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    //getMenuInflater().inflate(R.menu.menu_main, menu);
    //return true;
    //    return false;
    //}


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void eventsUpdated(final ViewEvents_Task.EventsData data) {

        if(data.entries == null)
        {
            Toast.makeText(getApplication(), "Couldn't refresh", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplication(), "number of events = " + data.entries.size(), Toast.LENGTH_LONG).show();
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                ListFrag tempListFrag = (ListFrag) fm.findFragmentByTag("list");

                if(tempListFrag !=  null)
                {
                    tempListFrag.addNewData(data);
                }
            }
        });

    }

    private class ATask extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... params) {

            Log.d("Achamp", "(username, password) (" +params[0]+", "+params[1]);
            // TODO check to see if the credentials are correct, if yes then enter else have to enetr credentials
            try {
                Thread.sleep(1000);
                    if(login(params[0], params[1]).equals("Not Logged In"))
                    {
                        Intent newIntent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivityForResult(newIntent, 5);
                    }
                    else {
                        initMainActivityFragment();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

    }


    public void LoadDemo() {
        demo = Demo_Fragment.newInstance("a", "z");
        fm.beginTransaction().add(R.id.fragment_container, demo, DEMO_TAG).commit();
    }

    public void initMainActivityFragment() {

        demo = (Demo_Fragment) fm.findFragmentByTag(DEMO_TAG);

        if (demo != null) {
            fm.beginTransaction().remove(demo).commit();
        }

    }


    public static class FragmentAdapter extends FragmentPagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return viewEvents;
                case 1:
                    return createEvents;
                case 2:
                    return addFriend;
            }
            return null;
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "View AChamp";
                case 1:
                    return "Creat AChamp";
                case 2:
                    return "Add Friends";
            }

            return null;
        }
    }


    @Override
    public void uploadAchampEvent(AChampEvent event) {
        postEvent.PostTheEvent(event);
    }

    @Override
    public void ToastUploadingResult(boolean result) {
        if(result == true) {
            Toast.makeText(getApplicationContext(), "The AChamp was uploaded", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getApplicationContext(), "The AChamp couldn't be uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRefreshRequested(ArrayList<String> array) {
        Log.d("Achamp", "onRefreshRequested()");
        SharedPreferences prefs = getSharedPreferences("usersession", MODE_PRIVATE);
        String username = prefs.getString("username", "");
        viewEvents_RF.onRefreshEvents("",username, array);
    }

    private String login(String username, String password) throws IOException, JSONException {

        InputStream is = null;
        String cookie = "Not Logged In";
        try {
            HttpURLConnection conn = (HttpURLConnection) ((new URL(MainActivity.myurl+"/login").openConnection()));
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestMethod("POST");
            conn.connect();


            JSONObject cred = new JSONObject();
            cred.put("username", username);
            cred.put("password", password);
            Log.d("Achamp", "this is what gets sent JSON:" + cred.toString());
            // posting it
            Writer wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(cred.toString());
            wr.flush();
            wr.close();
            Log.d("Achamp", " response from the server is" + conn.getResponseCode());
            // handling the response
            StringBuilder sb = new StringBuilder();
            int HttpResult = conn.getResponseCode();
            is = conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream();

            if (HttpResult == HttpURLConnection.HTTP_OK) {
                Log.d("Achamp", "cookies: Login HTTP_OK");

                cookie = "Logged In Successfully";
//                Map<String, List<String>> headerFields = conn.getHeaderFields();
//                //COOKIES_HEADER
//                List<String> cookiesHeader = headerFields.get("Set-Cookie");
//
//                //  for (String s : cookiesHeader) {
//
//                Log.d("hw4", "cookies: " + cookiesHeader.get(0).substring(0, cookiesHeader.get(0).indexOf(";")));
//
//                cookie = cookiesHeader.get(0).substring(0, cookiesHeader.get(0).indexOf(";"));
//

            }

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } catch (Exception e) {

            Log.d("vt", " and the exception is " + e);
        } finally {
            if (is != null) {
                is.close();
            }
        }
        return cookie;
    }

}
