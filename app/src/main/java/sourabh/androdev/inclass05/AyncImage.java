package sourabh.androdev.inclass05;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.LocaleDisplayNames;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class AyncImage extends AsyncTask<String,Integer, Bitmap> {
    ImageView imageView;
    HttpURLConnection connection=null;
    Bitmap image=null;
    Context context;
    ProgressDialog progressDialog;


    public AyncImage(Context context,ImageView imageView) {
        this.imageView = imageView;
        this.context=context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("loading image");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        HttpURLConnection connection=null;
        try {
            URL url=new URL(strings[0]);
            connection= (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                image=BitmapFactory.decodeStream(connection.getInputStream());

            }else{
                Log.d("demo","no resource found");
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(connection !=null){
                connection.disconnect();
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        progressDialog.dismiss();
        if(image !=null && imageView !=null){
            imageView.setImageBitmap(this.image);
        }else{
            Toast.makeText(context, "No resource found", Toast.LENGTH_SHORT).show();
}
    }
}
