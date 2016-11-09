package app.qvdev.com.flightstat;

import android.os.Bundle;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        TextView textView = (TextView) view.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        return view;
    }

    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    private void getRoute() {
        Call<Flight> call = mFoxService.getRouteStatus("AMS", "CDG");
        call.enqueue(new Callback<Flight>() {
            @Override
            public void onResponse(Call<Flight> flight, Response<Flight> response) {
                Log.d(getClass().getSimpleName(), response.toString());
            }

            @Override
            public void onFailure(Call<Flight> flight, Throwable t) {
                Log.d(getClass().getSimpleName(), flight.toString());
            }
        });
    }
}
