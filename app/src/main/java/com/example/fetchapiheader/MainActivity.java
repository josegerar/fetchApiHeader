package com.example.fetchapiheader;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    TextView txtPagosVolley;
    TextView txtPagosRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPagosVolley = (TextView) findViewById(R.id.txtVolley);
        txtPagosRetrofit = (TextView) findViewById(R.id.txtRetrofit);

        getDataVolley();
        getDataRetrofit();
    }

    public void getDataRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api-uat.kushkipagos.com/transfer/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServiceApi serviceAPi = retrofit.create(IServiceApi.class);

        Call<List<BankModel>> listbankCall = serviceAPi.getListBank();

        listbankCall.enqueue(new Callback<List<BankModel>>() {
            @Override
            public void onResponse(Call<List<BankModel>> call, retrofit2.Response<List<BankModel>> response) {
                if (!response.isSuccessful()){
                    txtPagosRetrofit.setText("Error code: " + response.code() + response.errorBody().toString());
                    return;
                }
                List<BankModel> listBank = response.body();
                for (BankModel bank : listBank){
                        txtPagosRetrofit.append("code: " + bank.getCode() + ", name: " + bank.getName() + "\n");
                }
            }

            @Override
            public void onFailure(Call<List<BankModel>> call, Throwable t) {
                txtPagosRetrofit.setText("Error: " + t.getMessage());
            }
        });
    }

    public void getDataVolley(){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api-uat.kushkipagos.com/transfer/v1/bankList";

        JSONArrayRequestEdit jsonArrayRequest = new JSONArrayRequestEdit(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Do something with response
                        //mTextView.setText(response.toString());

                        // Process the JSON
                        try{
                            // Loop through the array elements
                            for(int i=0;i<response.length();i++){
                                // Get current json object
                                JSONObject student = response.getJSONObject(i);

                                // Get the current student (json object) data
                                String code = student.getString("code");
                                String name = student.getString("name");

                                // Display the formatted json data in text view
                                txtPagosVolley.append("Code: " + code +", name:  " + name );
                                txtPagosVolley.append("\n\n");
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        // Do something when error occurred
                        txtPagosVolley.setText("Error: " + error.toString());
                    }
                }
        );

        queue.add(jsonArrayRequest);
    }
}