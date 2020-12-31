package com.applauded.meena.pincodevalidation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String NO_INTERNET_MESSAGE = "No internet connection.";
    private static final String REMOTE_SERVER_FAILED_MESSAGE = "Application server could not respond.";
    private static final String UNEXPECTED_ERROR_OCCURRED = "Something went wrong.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("Logging", "onCreate: Logging");

        EditText input = findViewById(R.id.pincode);
        Button   goB   = findViewById(R.id.getdetails);

        //TODO edittext numerical only, 6 digits input only

        TextView mCity = findViewById(R.id.city);
        TextView mState = findViewById(R.id.state);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://pintasticapi.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PinCodeAPI pinCodeService = retrofit.create(PinCodeAPI.class);

        goB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String qInput = input.getText().toString();
                Log.d("TAG", " "+qInput);
                Log.d("URL",pinCodeService.getArea(qInput).request().url().toString());
                pinCodeService.getArea(qInput).enqueue(new Callback<PinCode>() {
                    @Override
                    public void onResponse(Call<PinCode> call, Response<PinCode> response) {
                        PinCode pinCode = response.body();
                        Log.d("TAG", "City: "+pinCode.getCity());
                        Log.d("TAG", "State: "+pinCode.getState());
                        mCity.setText(pinCode.getCity());
                        mState.setText(pinCode.getState());

                        //if pincode is not valid
                        if(pinCode.getCity()==null)
                        {
                            Log.d("TAG", "onResponse: invalid pincode");
                            mCity.setText(pinCode.getStatus());
                            mState.setText(pinCode.getMessage());
                        }
                    }
                        //TODO output if pincode invalid
                    @Override
                    public void onFailure(Call<PinCode> call, Throwable t) {
                        //TODO failure messages

                        String exception = resolveException(t);
                        mCity.setText(exception);//reusing existing view
                        mState.setText(null);
                    }
                });
            }
        });

        pinCodeService.getmyArea().enqueue(new Callback<PinCode>() {
            @Override
            public void onResponse(Call<PinCode> call, Response<PinCode> response) {
                PinCode pinCode = response.body();
                Log.d("TAG", "City: "+pinCode.getCity());
                Log.d("TAG", "State: "+pinCode.getState());
                mCity.setText(pinCode.getCity());
                mState.setText(pinCode.getState());
            }

            @Override
            public void onFailure(Call<PinCode> call, Throwable t) {
                Log.d("Logging", "onFailure: Logging");
            }
        });
    }

    private String resolveException(Throwable throwable) {
      // no internet be checked way before even inserting the input?
        //TODO how to checkother errors manually or even by unit test case?
        if (throwable instanceof UnknownHostException) {
            Log.d(TAG, "resolveException: NO_INTERNET_MESSAGE");
            return NO_INTERNET_MESSAGE;
        } else if (throwable instanceof SocketTimeoutException) {
            Log.d(TAG, "resolveException: REMOTE_SERVER_FAILED_MESSAGE");
            return REMOTE_SERVER_FAILED_MESSAGE;
        } else if (throwable instanceof ConnectException) {
            Log.d(TAG, "resolveException: NO_INTERNET_MESSAGE");
            return NO_INTERNET_MESSAGE;
        } else {
            Log.d(TAG, "resolveException: UNEXPECTED_ERROR_OCCURRED");
            return UNEXPECTED_ERROR_OCCURRED;
        }

    }
}