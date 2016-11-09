package app.qvdev.com.flightstat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.qvdev.com.flightstat.model.Flight;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteFragment extends BaseFragment {

    private TextView mLogView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        mLogView = (TextView) view.findViewById(R.id.section_label);
        mLogView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        return view;
    }

    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRoute();
    }

    private void getRoute() {
        Call<Flight> call = mFoxService.getRouteStatus("AMS", "CDG");
        call.enqueue(new Callback<Flight>() {
            @Override
            public void onResponse(Call<Flight> flight, Response<Flight> response) {
                mLogView.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<Flight> flight, Throwable t) {
                Log.d(getClass().getSimpleName(), flight.toString());
            }
        });
    }
}
