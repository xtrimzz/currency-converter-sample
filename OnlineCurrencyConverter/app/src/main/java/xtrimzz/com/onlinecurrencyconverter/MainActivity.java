package xtrimzz.com.onlinecurrencyconverter;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private TextView eur, usd, inr;
    private Button btn;
    private EditText et;
    private Spinner spin;
    private int index;
    private double inputValue;
    private String result[] = new String[10];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et = (EditText) findViewById(R.id.et);
        usd = (TextView)findViewById(R.id.usd);
        eur = (TextView)findViewById(R.id.euro);
        inr = (TextView)findViewById(R.id.inr);
        btn = (Button)findViewById(R.id.btn);
        spin = (Spinner) findViewById(R.id.spin);

        //Populate the spinner values with string
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.currency, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spin.setAdapter(adapter);

        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
                index = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usd.setText("Wait...");
                eur.setText("Wait...");
                inr.setText("Wait...");

                //if user did not type a thing or type a dot
                if (et.getText().toString().trim().length() > 0 && !et.getText().toString().trim().equals(".")) {
                    String textValue = et.getText().toString();
                    //convert the string to double
                    inputValue = Double.parseDouble(textValue);

                    new Calculate().execute();
                }
            }
        });

    }//end OnCreate

    /**
     * A new Class to Calcuate the Class
     */
    public class  Calculate extends AsyncTask<String, String, String[]>{

        @Override
        protected String[] doInBackground(String... params) {
            if (index == 0) {
                String url;
                try {
                    url = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22USDEUR,USDINR%22)&format=json&env=store://datatables.org/alltableswithkeys");
                    JSONObject USDtoObj;
                    USDtoObj = new JSONObject(url);
                    JSONArray rateArray = USDtoObj.getJSONObject("query").getJSONObject("results").getJSONArray("rate");

                    result[0] = rateArray.getJSONObject(0).getString("Rate");
                    result[1] = rateArray.getJSONObject(1).getString("Rate");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (index == 1) {
                String url;
                try {
                    url = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22EURUSD,INRUSD%22)&format=json&env=store://datatables.org/alltableswithkeys");
                    JSONObject USDtoObj;
                    USDtoObj = new JSONObject(url);
                    JSONArray rateArray = USDtoObj.getJSONObject("query").getJSONObject("results").getJSONArray("rate");

                    result[0] = rateArray.getJSONObject(0).getString("Rate");
                    result[1] = rateArray.getJSONObject(1).getString("Rate");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (index == 2) {
                String url;
                try {
                    url = getJson("http://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20yahoo.finance.xchange%20where%20pair%20in%20(%22INRUSD,INREUR%22)&format=json&env=store://datatables.org/alltableswithkeys");
                    JSONObject USDtoObj;
                    USDtoObj = new JSONObject(url);
                    JSONArray rateArray = USDtoObj.getJSONObject("query").getJSONObject("results").getJSONArray("rate");

                    result[0] = rateArray.getJSONObject(0).getString("Rate");
                    result[1] = rateArray.getJSONObject(1).getString("Rate");
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] strings) {
            if (index == 0){
                double usdtoeuroVal, usdtoinrVal, usdtoeuroInp, usdtoinrInp, usdtousdInp;

                usdtousdInp = inputValue * 1;
                usd.setText(""+usdtousdInp);

                usdtoeuroVal = Double.parseDouble(result[0]);
                usdtoeuroInp = inputValue * usdtoeuroVal;
                eur.setText(""+usdtoeuroInp);

                usdtoinrVal = Double.parseDouble(result[1]);
                usdtoinrInp = inputValue * usdtoinrVal;
                inr.setText(""+usdtoinrInp);

            }else if (index == 1){
                double eurotousdVal, eurotoinrVal, eurotousdInp, eurotoinrInp, eurotoeurInp;

                eurotoeurInp = inputValue * 1;
                eur.setText(""+eurotoeurInp);

                eurotousdVal = Double.parseDouble(result[0]);
                eurotousdInp = inputValue * eurotousdVal;
                usd.setText(""+eurotousdInp);

                eurotoinrVal = Double.parseDouble(result[1]);
                eurotoinrInp = inputValue * eurotoinrVal;
                inr.setText(""+eurotoinrInp);
            }

            else if (index == 2){
                double inrtousdVal, inrtoeurVal, inrtousdInp, inrtoeurInp, inrtoinrInp;
                inrtoinrInp = inputValue * 1;
                inr.setText(""+inrtoinrInp);

                inrtousdVal = Double.parseDouble(result[0]);
                inrtousdInp = inputValue * inrtousdVal;
                usd.setText(""+inrtousdInp);

                inrtoeurVal = Double.parseDouble(result[1]);
                inrtoeurInp = inputValue * inrtoeurVal;
                eur.setText(""+inrtoeurInp);


            }
        }

        public String getJson(String url) throws ClientProtocolException , IOException{
            StringBuilder build = new StringBuilder();
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream content = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String con;

            while ((con = reader.readLine())!= null){
                build.append(con);
            }

            return build.toString();
        }
    }
}
