package cn.edu.hebtu.software.sharemateclient.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ImageTask extends AsyncTask {

    private String path = null;

    public ImageTask(String path) {
        this.path = path;
    }
    @Override
    protected Object doInBackground(Object[] objects) {

        HashMap<ImageView,Bitmap> map = new HashMap<>();

        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.connect();

            //判断响应码 成功响应
            if(conn.getResponseCode() == 200) {


                //获取服务器中响应的流
                InputStream is = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                map.put((ImageView) objects[0],bitmap);
                is.close();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        HashMap<ImageView,Bitmap> map = (HashMap<ImageView, Bitmap>) o;
        for(ImageView imageView : map.keySet())
            imageView.setImageBitmap(map.get(imageView));
    }
}

