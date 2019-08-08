package sourabh.androdev.inclass05;

import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements AsyncUrl.urlListTransfer {
    TextView textView_search;
    Button button_Go;
    ImageView imageView_imageDisplay;
    ImageView imageView_next;
    ImageView imageView_previous;

    ArrayList<String> imageList=null;
    String url=null;
    int index=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_search=findViewById(R.id.textView_search);
        button_Go=findViewById(R.id.button_Go);
        imageView_imageDisplay=findViewById(R.id.imageView2_imageDisplay);
        imageView_next=findViewById(R.id.imageView_next);
        imageView_previous=findViewById(R.id.imageView_previous);
        imageList=new ArrayList<String>();
        button_Go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              new AsyncKeyword().execute("http://dev.theappsdr.com/apis/photos/keywords.php");
                asyncKeyword();
            }
        });
        imageView_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (index+1<imageList.size())
                new AyncImage(MainActivity.this, imageView_imageDisplay).execute(imageList.get(++index));
                else{
                    index=0;
                    new AyncImage(MainActivity.this, imageView_imageDisplay).execute(imageList.get(index));
                }


            }
        });
        imageView_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(index-1>=0){
                    new AyncImage(MainActivity.this, imageView_imageDisplay).execute(imageList.get(--index));
                }
                else {
                    index=imageList.size();
                    new AyncImage(MainActivity.this, imageView_imageDisplay).execute(imageList.get(imageList.size()-1));

                }
            }
        });


    }
    private boolean isConnected(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo==null || !networkInfo.isConnected()
                ||(networkInfo.getType() != ConnectivityManager.TYPE_WIFI
                && networkInfo.getType() != ConnectivityManager.TYPE_MOBILE)){
            return false;
        }
        return true;
    }

    @Override
    public void transferMethod(ArrayList<String> data)
    {
        if (data==null){
            Toast.makeText(this, "no images retrieved", Toast.LENGTH_SHORT).show();
        }else {
            this.imageList = data;
            new AyncImage(this, imageView_imageDisplay).execute(imageList.get(0));
        }
    }

    public void asyncKeyword()
    {
        Log.d("demo","here");
         OkHttpClient client = new OkHttpClient();

//          Request request = new Request.Builder()
//                    .url("http://dev.theappsdr.com/apis/photos/keywords.php")
//                    .build();
//
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Call call, IOException e) {
//                    e.printStackTrace();
//                }
//                @Override
//                public void onResponse(Call call, Response response) throws IOException {
//                    try (ResponseBody responseBody = response.body()) {
//                        if (!response.isSuccessful())
//                            throw new IOException("Unexpected code " + response);
//                         else
//                             Log.d("demo","value="+responseBody.string());
//                        }
//                }
//            });
    }
    public class AsyncKeyword extends AsyncTask<String,Void,String[]>{
        String[] itemsArray;
        @Override
        protected String[] doInBackground(String... strings) {
            StringBuilder sb=new StringBuilder();
            HttpURLConnection connection=null;
            BufferedReader bufferedReader=null;
            String result=null;
            try {
                URL url=new URL(strings[0]);
                connection= (HttpURLConnection) url.openConnection();
                connection.connect();
                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    // result= IOUtils.toString(connection.getInputStream(),"UTF8");

                    bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line="";
                    while ((line=bufferedReader.readLine()) != null){
                        sb.append(line);
                    }

                    result= sb.toString();
                    itemsArray=result.split(";");


                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if(connection !=null){
                    connection.disconnect();
                }
                if(bufferedReader !=null){
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            return itemsArray;
        }

        @Override
        protected void onPostExecute(final String[] strings) {
            final AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Choose a Keyword").setItems(strings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    url="http://dev.theappsdr.com/apis/photos/index.php?keyword="+strings[i];
                    new AsyncUrl(MainActivity.this).execute(url);

                }
            }).show();



        }
    }
}


