package app.qvdev.com.flightstat;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.qvdev.com.flightstat.model.Flight;
import app.qvdev.com.flightstat.model.Flight_;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BaseFragment extends Fragment {

    private static final String FOX_BASE_URL = "http://fox.klm.com/fox/json/";

    protected static final String ARG_SECTION_NUMBER = "section_number";
    protected FoxService mFoxService;

    protected TextView mLogView;

    private FlightsAdapter mFlightsAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Flight_> mFlights = new ArrayList<>();

    public static BaseFragment newInstance(int sectionNumber) {
        BaseFragment fragment = getFragment(sectionNumber);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private static BaseFragment getFragment(int sectionNumber) {
        switch (sectionNumber) {
            case 1:
                return new FlightFragment();
            case 2:
                return new RouteFragment();
            default:
                return new BaseFragment();
        }
    }

    protected void getRoute() {
        mFlights.clear();
    }

    protected void setFlightList(View view) {
        RecyclerView mFlightList = (RecyclerView) view.findViewById(R.id.flights_list);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mFlightList.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mFlightList.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mFlightsAdapter = new FlightsAdapter(mFlights);
        mFlightList.setAdapter(mFlightsAdapter);
    }

    protected void showFlights(Response<Flight> response) {
        mFlights.addAll(response.body().getFlights());
        mFlightsAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);

        setFlightList(view);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initRetrofit();
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(FOX_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mFoxService = retrofit.create(FoxService.class);
    }

    /**
     * Get the fragment layout id. Override this method in subclasses
     *
     * @return The id of the fragment layout
     */
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    protected void setLogView(View view) {
        mLogView = (TextView) view.findViewById(R.id.section_label);
    }
}
