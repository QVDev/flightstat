package app.qvdev.com.flightstat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.qvdev.com.flightstat.model.Flight_;

class FlightsAdapter extends RecyclerView.Adapter<FlightsAdapter.ViewHolder> {

    private final List<Flight_> mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView mFlightNumber;
        TextView mAircraft;
        TextView mRemainingFlightTime;

        ViewHolder(View v) {
            super(v);
            mFlightNumber = (TextView) v.findViewById(R.id.flight_number);
            mAircraft = (TextView) v.findViewById(R.id.aircraft);
            mRemainingFlightTime = (TextView) v.findViewById(R.id.remaining_flight_time);
        }
    }

    FlightsAdapter(List<Flight_> dataset) {
        mDataset = dataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.flight_card, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Flight_ flight = mDataset.get(position);
        Context context = holder.mAircraft.getContext();

        holder.mFlightNumber.setText(context.getString(R.string.flight_number, flight.getFlightNumber(), flight.getCarrier() != null ? flight.getCarrier().getCode() : "N/A"));
        holder.mAircraft.setText(context.getString(R.string.aircraft, flight.getAircraft() != null ? flight.getAircraft().getRegistrationCode() : "N/A"));
        holder.mRemainingFlightTime.setText(context.getString(R.string.remaining_time, flight.getRemainingFlyTime() != null ? flight.getRemainingFlyTime() : "N/A"));
    }

    @Override
    public int getItemCount() {
        if (mDataset == null) {
            return 0;
        }
        return mDataset.size();
    }
}
