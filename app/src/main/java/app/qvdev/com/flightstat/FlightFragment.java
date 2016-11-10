package app.qvdev.com.flightstat;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

import app.qvdev.com.flightstat.model.Flight;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlightFragment extends BaseFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final String DEFAULT_FLIGHT = "KL1699";
    private static final String DATE_PICKER_IDENTIFIER = "datePicker";

    private TextView mCalendarPicker;
    private EditText mFlightPicker;

    private String mChosenDate;
    private String mChosenFlight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        setLogView(view);
        setFlightPicker(view);
        setCalendarButton(view);
        setSearchButton(view);

        return view;
    }

    private void setFlightPicker(View view) {
        mFlightPicker = (EditText) view.findViewById(R.id.flightPicker);
        mFlightPicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //;
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mChosenFlight = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //;
            }
        });
        setChosenFlight(null);
    }

    private void setCalendarButton(View view) {
        mCalendarPicker = (TextView) view.findViewById(R.id.calendarPicker);
        mCalendarPicker.setOnClickListener(this);
        mCalendarPicker.requestFocus();
        setChosenDate(null);
    }

    private void setSearchButton(View view) {
        view.findViewById(R.id.search_button).setOnClickListener(this);
    }

    protected int getLayoutId() {
        return R.layout.fragment_flight;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void getFlight() {
        if (isSearchPossible()) {
            super.getRoute();
            Call<Flight> call = mFoxService.getFlightStatus(mChosenFlight, mChosenDate);
            call.enqueue(new Callback<Flight>() {
                @Override
                public void onResponse(Call<Flight> flight, Response<Flight> response) {
                    if (response.isSuccessful()) {
                        mLogView.setText(response.body().toString());
                        showFlights(response);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.calendarPicker:
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.setOnDateSetListener(this);
                datePicker.show(getFragmentManager(), DATE_PICKER_IDENTIFIER);
                break;
            default:
                getFlight();
                break;
        }
    }

    private boolean isSearchPossible() {
        if (TextUtils.isEmpty(mChosenFlight)) {
            mLogView.setError("");
            mLogView.setText(R.string.flight_picker_error);
            return false;
        }
        if (TextUtils.isEmpty(mChosenDate)) {
            mLogView.setError("");
            mLogView.setText(R.string.date_picker_error);
            return false;
        }
        mLogView.setError(null);
        return true;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        month++; // Increase as it starts counting from 0-11
        String date = getString(R.string.date_search, year, month, day);
        setChosenDate(date);
    }

    private void setChosenDate(String date) {
        if (date == null) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            month++;
            date = getString(R.string.date_search, year, month, day);
        }

        mChosenDate = date;
        mCalendarPicker.setText(date);
    }

    private void setChosenFlight(String flight) {
        if (flight == null) {
            flight = DEFAULT_FLIGHT;
        }
        mChosenFlight = flight;
        mFlightPicker.setText(mChosenFlight);
    }
}
