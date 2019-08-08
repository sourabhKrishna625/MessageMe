package sourabh.androdev.inclass05;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AsyncUrl extends AsyncTask<String,Void,ArrayList<String>> {
    urlListTransfer urlDetails;

    public AsyncUrl(urlListTransfer urlDetails) {
        this.urlDetails = urlDetails;
    }

    ArrayList<String> urls=null;
    HttpURLConnection connection=null;
    BufferedReader bufferedReader=null;

    @Override
    protected ArrayList<String> doInBackground(String... strings) {
        try {
            URL url=new URL(strings[0]);
            connection= (HttpURLConnection) url.openConnection();
            connection.connect();
            if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                bufferedReader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line="";
                urls=new ArrayList<String>();
                while ((line=bufferedReader.readLine()) != null){
                    urls.add(line);
                }

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return urls;
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        urlDetails.transferMethod(strings);
    }

    public static interface urlListTransfer{
        public void transferMethod(ArrayList<String> data);
    }
}
