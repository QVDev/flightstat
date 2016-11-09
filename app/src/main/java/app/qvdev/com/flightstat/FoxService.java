package app.qvdev.com.flightstat;

import app.qvdev.com.flightstat.model.Flight;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface FoxService {
    @GET("flightstatuses")
    Call<Flight> getFlightStatus(@Query("flightNumber") String flightNumber, @Query("departureDate") String date);

    @GET("flightstatuses")
    Call<Flight> getRouteStatus(@Query("originAirportCode") String departureCode, @Query("destinationAirportCode") String destinationCode);
}
