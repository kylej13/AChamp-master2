package achamp.project.org.achamp.ViewingEvents;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import achamp.project.org.achamp.AChampEvent;
import achamp.project.org.achamp.R;
import achamp.project.org.achamp.ViewingEvents.fragments.ListFrag;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewEvents_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewEvents_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewEvents_Fragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    private static final String TAG_LISTFRAG = ("list");
    private static final String TAG_MAP = ("map");

    private android.app.Fragment listFrag;

    private ListView events;
    private Button viewMap;
    private Button viewList;
    private EditText search;

    private Boolean mapChecked;
    private Boolean listChecked;
    private int miles;
    private LinearLayout l;
    private Button go;

    private View view;
    private ArrayList<AChampEvent> temp;
    private LatLng currLoc;

    private FragmentManager fm;

    private GoogleMap map; // Might be null if Google Play services APK is not available.
    private com.google.android.gms.maps.MapFragment mfrag;

    static final LatLng BLACKSBURG = new LatLng(37.23, -80.42);
    static final LatLng ELLICOTT_CITY = new LatLng(39.27, -76.8);
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewEvents_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewEvents_Fragment newInstance(String param1, String param2) {
        ViewEvents_Fragment fragment = new ViewEvents_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ViewEvents_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapChecked = true;
        listChecked = false;

        miles = 10;
        temp = new ArrayList<AChampEvent>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_view_events_, container, false);
        viewMap = (Button) v.findViewById(R.id.view_map);
        viewList = (Button) v.findViewById(R.id.view_list);
        go = (Button) v.findViewById(R.id.go);
        l = (LinearLayout) v.findViewById(R.id.container);
        search = (EditText) v.findViewById(R.id.search);
        viewMap.setOnClickListener(this);
        viewList.setOnClickListener(this);
        go.setOnClickListener(this);
        view = v;


        return v;
    }

    @Override
    public void onResume(){

        super.onResume();
        createMap();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

    public void onClick(View v) {

        if (v == viewMap && (!mapChecked)) {

            mapChecked = true;
            listChecked = false;
            viewMap.setBackgroundColor(getResources().getColor(R.color.Highlight));
            viewList.setBackgroundColor(getResources().getColor(R.color.Gray));
            createMap();

        }

        if (v == viewList && (!listChecked)) {

            mapChecked = false;
            listChecked = true;
            viewList.setBackgroundColor(getResources().getColor(R.color.Highlight));
            viewMap.setBackgroundColor(getResources().getColor(R.color.Gray));
            displayList();
        }

        if(v == go && !search.getText().toString().equals("")){
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            Address temp = getAddress(search.getText().toString());
            if(temp != null){

                LatLng c = new LatLng(temp.getLatitude(), temp.getLongitude());
                Log.d("finderror", "loc: " + c);
                if(map != null && c != null) {
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(c, 15));
                    map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
                }
            }
        }
    }

    public void createMap() {
        Log.d("main", "getActivity is null "+ (getActivity()==null));
        Log.d("main", "getFragmentManager is null "+ (getActivity().getFragmentManager()==null));
        listFrag = getActivity().getFragmentManager().findFragmentByTag(TAG_LISTFRAG);
        //mfrag = (MapFragment) fm.findFragmentByTag(TAG_MAP);
        if (listFrag != null) {
            getActivity().getFragmentManager().beginTransaction().remove(listFrag).commit();
        }

        mfrag = MapFragment.newInstance();
        Log.d("main", "mfrag is null: " + (mfrag == null));

        getActivity().getFragmentManager().beginTransaction().add(R.id.container, mfrag, TAG_MAP).commit();
        mfrag.getMapAsync(this);

        Geocoder geocoder = new Geocoder(l.getContext());
        /*List<Address> gotAddresses = null;
        try {
            gotAddresses = geocoder.getFromLocationName("", 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address address = (Address) gotAddresses.get(0);

        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

        String properAddress = String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName());

        map.addMarker(new MarkerOptions()
                .position(new LatLng(address.getLatitude(), address.getLongitude())).draggable(true)
                .title(properAddress));
    */
    }

    private Address getAddress(String s) {
        Geocoder geocoder = new Geocoder(getActivity().getBaseContext());
        List<Address> gotAddresses = null;
        try {
            gotAddresses = geocoder.getFromLocationName(s, 1);
            Log.d("finderror", "gotAddresses: " + gotAddresses);
            if(gotAddresses.size() > 0 && gotAddresses.get(0) != null) {
                Log.d("finderror", "gotAddresses: " + gotAddresses.get(0));
                return gotAddresses.get(0);
            }
            else{
                CharSequence text = "Please enter a valid location";
                Toast toast= Toast.makeText(getActivity().getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

            return null;
    }

    private void displayList() {
        listFrag = getActivity().getFragmentManager().findFragmentByTag(TAG_LISTFRAG);
        mfrag = (com.google.android.gms.maps.MapFragment) getActivity().getFragmentManager().findFragmentByTag(TAG_MAP);
        getActivity().getFragmentManager().beginTransaction().remove(mfrag).commit();

        if (listFrag == null) {

            listFrag = ListFrag.newInstance("a", "n");
        }
        getActivity().getFragmentManager().beginTransaction().add(R.id.container, listFrag, TAG_LISTFRAG).commit();

    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        Log.d("main", "is in the not null");
        Marker bburg = map.addMarker(new MarkerOptions().position(BLACKSBURG)
                .title("Blacksburg"));
        Marker ec = map.addMarker(new MarkerOptions().position(ELLICOTT_CITY).title("Ellicott City"));
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(BLACKSBURG, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
        Log.d("findNull", "map.getMyLocation: " + map.getMyLocation());
        Log.d("findNull", "map: " + map);
        if(map != null && map.getMyLocation() != null) {
            currLoc = new LatLng(map.getMyLocation().getLatitude(), map.getMyLocation().getLongitude());
            addMarkers();
        }

    }

    private void addMarkers() {
        LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
        for(int x = 0; x<temp.size(); x++){
            LatLng curr = new LatLng(temp.get(x).getAddressLoc().getLatitude(), temp.get(x).getAddressLoc().getLongitude());
            if(bounds.contains(curr)){
                Marker m = map.addMarker(new MarkerOptions().position(curr)
                        .title(temp.get(x).getTitle()));

            }
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                //marker.
                return true;
            }
        });

    }



}

