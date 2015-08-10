package achamp.project.org.achamp.ViewingEvents;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import achamp.project.org.achamp.R;
import achamp.project.org.achamp.ViewingEvents.fragments.ListFrag;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ViewEvents_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ViewEvents_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewEvents_Fragment extends Fragment implements View.OnClickListener,
        ListFrag.OnFragmentInteractionListener, OnMapReadyCallback {

    private static final String TAG_LISTFRAG = ("list");
    private static final String TAG_MAP = ("map");

    private android.app.Fragment listFrag;

    private ListView events;
    private Button viewMap;
    private Button viewList;

    private Boolean mapChecked;
    private Boolean listChecked;
    private int miles;

    private View view;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_view_events_, container, false);
        viewMap = (Button) v.findViewById(R.id.view_map);
        viewList = (Button) v.findViewById(R.id.view_list);
        viewMap.setOnClickListener(this);
        viewList.setOnClickListener(this);
        view = v;

        createMap();
        return v;
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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d("main", "is in the not null");
        Marker bburg = map.addMarker(new MarkerOptions().position(BLACKSBURG)
                .title("Blacksburg"));
        Marker ec = map.addMarker(new MarkerOptions().position(ELLICOTT_CITY).title("Ellicott City"));
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.getUiSettings().setAllGesturesEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(BLACKSBURG, 15));
        map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
    }


}

