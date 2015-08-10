package achamp.project.org.achamp.ViewingEvents.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import achamp.project.org.achamp.R;
import achamp.project.org.achamp.AChampEvent;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

//import com.example.kylej.connectme.fragments.FragMap.OnFragmentInteractionListener;


public class ListFrag extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private EventEntryAdapter adapter;

    private ListView list;
    private SwipeRefreshLayout refreshList;

    private SeekBar seekBar;
    private int miles;
    private ArrayList<AChampEvent> temp;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFrag newInstance(String param1, String param2) {
        ListFrag fragment = new ListFrag();

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ListFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_list, container, false);

        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setProgress(21);
        miles = 10;

        refreshList = (SwipeRefreshLayout) view.findViewById(R.id.refresh_list);
        refreshList.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mListener.onRefreshRequested();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                miles = progress / 100;
                miles *= miles;
                miles *= 100;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        temp = new ArrayList<AChampEvent>();

        //linking  java side with the xml side for the ListView
        list = (ListView) view.findViewById(R.id.list);
        adapter = new EventEntryAdapter(getActivity().getApplicationContext(), temp);

        list.setAdapter(adapter);
        // Inflate the layout for this fragment

        return view;
    }

   // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        //if (mListener != null) {
        //    mListener.onFragmentInteraction(uri);
       // }
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
        //mListener = null;
    }

    public void addNewData(ViewEvents_Task.EventsData data) {
        if (data.entries != null) {
            adapter.clear();
            adapter.addAll((ArrayList<AChampEvent>)data.entries);
            list.invalidate();
        }
        refreshList.setRefreshing(false);
    }

    public class EventEntry{

        public String eventName;
        public String eventDate;
        public String time;
        public String address;
        public LatLng loc;

        public EventEntry(String eventName, String eventDate, String time, String address) {

            this.eventDate = eventDate;
            this.eventName = eventName;
            this.time = time;
            this.address = address;
        }

    }

    private class EventEntryAdapter extends ArrayAdapter<AChampEvent> implements
            View.OnClickListener {
        private final Context context;
        //values that will be displayed
        private final ArrayList<AChampEvent> values;


        public EventEntryAdapter(Context context, ArrayList<AChampEvent> values) {
            super(context, R.layout.event_list, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.event_list,
                    parent, false);

            TextView nameEntry = (TextView) rowView.findViewById(R.id.eventName);
            TextView dateEntry = (TextView) rowView.findViewById(R.id.date);
            TextView timeEntry = (TextView) rowView.findViewById(R.id.time);
            TextView addressEntry = (TextView) rowView.findViewById(R.id.address);


            // The code below sets tags to your buttons so that you can detect which one was pressed

            return rowView;
        }

        @Override
        public void onClick(View view) {
            // TODO Auto-generated method stub

            if (((String[]) view.getTag())[1] == "more") {


                //Send to event page

            }
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onRefreshRequested();
    }

}
