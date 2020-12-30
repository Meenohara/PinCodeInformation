package com.applauded.meena.pincodevalidation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

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
                    }
                        //TODO output if pincode invalid
                    @Override
                    public void onFailure(Call<PinCode> call, Throwable t) {
                        //TODO failure messages
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
}