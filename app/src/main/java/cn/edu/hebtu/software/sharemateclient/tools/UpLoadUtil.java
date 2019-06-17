package cn.edu.hebtu.software.sharemateclient.tools;



import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

import cn.edu.hebtu.software.sharemateclient.Activity.MainActivity;
import cn.edu.hebtu.software.sharemateclient.Activity.PerPersonalActivity;
import cn.edu.hebtu.software.sharemateclient.Bean.UserBean;

public class UpLoadUtil extends AsyncTask{
    private String path;
    private Context context;
    private UserBean user;

    public UpLoadUtil(String path, Context context,UserBean user) {
        this.path = path;
        this.context = context;
        this.user = user;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        String BOUNDARY = UUID.randomUUID().toString();
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";
        File file = new File((String) objects[0]);
        try {
            Log.e("path",path);
            URL url = new URL(path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true); // 允许输入流
            con.setDoOutput(true); // 允许输出流
            con.setUseCaches(false); // 不允许使用缓存
            con.setRequestMethod("POST"); // 请求方式
            con.setRequestProperty("connection", "keep-alive");
            con.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            con.setRequestProperty("Charset", "UTF-8");
            Log.e("con","con");
            DataOutputStream dos = new DataOutputStream(con.getOutputStream());
            StringBuffer sb = new StringBuffer();
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINE_END);
            sb.append("Content-Disposition: form-data; name=\"img\"; filename=\""
                    + file.getName() + "\"" + LINE_END);
            sb.append("Content-Type: application/octet-stream; charset=utf-8" + LINE_END);
            sb.append(LINE_END);
            Log.e("sb","sb");
            dos.write(sb.toString().getBytes());
            Log.e("is","is");
            InputStream is = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = is.read(bytes)) != -1) {
                dos.write(bytes, 0, len);
            }
            is.close();
            dos.write(LINE_END.getBytes());
            Log.e("dos","dos");
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END)
                    .getBytes();
            dos.write(end_data);
            dos.flush();
            int res = con.getResponseCode();
            if (res == 200) {
                Log.e("test","上传成功");
            }
            InputStream inputStream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String result = reader.readLine();
            JSONObject userObject = new JSONObject(result);
            JSONObject userJson = (JSONObject) userObject.get("user");
            Gson gson = new Gson();
            user = gson.fromJson(userJson.toString(),UserBean.class);
            Log.e("UpLoadUtil",user.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        Intent intent = new Intent(context,MainActivity.class);
        intent.putExtra("flag","my");
        intent.putExtra("user",user);
        context.startActivity(intent);
    }
}
