package app.qvdev.com.flightstat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import app.qvdev.com.flightstat.model.Flight;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteFragment extends BaseFragment implements View.OnClickListener {

    private static final String DEFAULT_DEPARTURE = "AMS";
    private static final String DEFAULT_DESTINATION = "CDG";

    private EditText mDeparturePicker;
    private EditText mDestinationPicker;

    private String mChosenDeparture;
    private String mChosenDestination;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setLogView(view);
        setSearchButton(view);
        setDeparturePicker(view);
        setDestinationPicker(view);

        return view;
    }

    protected int getLayoutId() {
        return R.layout.fragment_route;
    }

    private void setSearchButton(View view) {
        view.findViewById(R.id.search_button).setOnClickListener(this);
    }

    private void setDeparturePicker(View view) {
        mDeparturePicker = (EditText) view.findViewById(R.id.departurePicker);
        mDeparturePicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mChosenDeparture = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //;
            }
        });
        setChosenDeparture(null);
    }

    private void setDestinationPicker(View view) {
        mDestinationPicker = (EditText) view.findViewById(R.id.destinationPicker);
        mDestinationPicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mChosenDestination = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //;
            }
        });
        setChosenDestination(null);
    }

    private void setChosenDeparture(String departure) {
        if (departure == null) {
            departure = DEFAULT_DEPARTURE;
        }
        mChosenDeparture = departure;
        mDeparturePicker.setText(mChosenDeparture);
    }

    private void setChosenDestination(String destination) {
        if (destination == null) {
            destination = DEFAULT_DESTINATION;
        }
        mChosenDestination = destination;
        mDestinationPicker.setText(mChosenDestination);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                getRoute();
                break;
        }
    }

    private void getRoute() {
        if (isSearchPossible()) {
            Call<Flight> call = mFoxService.getRouteStatus(mChosenDeparture.toUpperCase(), mChosenDestination.toUpperCase());
            call.enqueue(new Callback<Flight>() {
                @Override
                public void onResponse(Call<Flight> flight, Response<Flight> response) {
                    if (response.isSuccessful()) {
                        mLogView.setText(response.body().toString());
                    } else {
                        mLogView.setText(getString(R.string.response_error));
                    }
                }

                @Override
                public void onFailure(Call<Flight> flight, Throwable t) {
                    Log.d(getClass().getSimpleName(), flight.toString());
                }
            });
        }
    }

    private boolean isSearchPossible() {
        if (TextUtils.isEmpty(mChosenDeparture)) {
            mLogView.setError("");
            mLogView.setText(R.string.departure_picker_error);
            return false;
        }
        if (TextUtils.isEmpty(mChosenDestination)) {
            mLogView.setError("");
            mLogView.setText(R.string.destination_picker_error);
            return false;
        }
        mLogView.setError(null);
        return true;
    }
}
