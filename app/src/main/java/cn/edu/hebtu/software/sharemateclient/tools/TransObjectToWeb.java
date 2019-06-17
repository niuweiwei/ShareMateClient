package cn.edu.hebtu.software.sharemateclient.tools;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


import cn.edu.hebtu.software.sharemateclient.Bean.NoteBean;

public class TransObjectToWeb {
    private Boolean flag =false;
    private String path;

    public TransObjectToWeb(String path) {
        this.path = path;
    }

    public boolean sendToWeb(NoteBean noteBean){
        try{
            //链接服务器的访问路径
            Log.e("TAG","创建链接");
            URL url=new URL(path+"WinSockForAndroid");
            HttpURLConnection http=(HttpURLConnection)url.openConnection();
            http.setDoInput(true);//可读可写
            http.setDoOutput(true);
            http.setUseCaches(false);//不允许使用缓存
            Log.e("url","url");
            http.setRequestMethod("POST");//..........................................设置传输方式为post
            http.setRequestProperty("contentType","UTF-8");
            http.connect();

            Log.e("TAG","创建链接成功");
            //数据写入流发送至服务器
            OutputStream os=http.getOutputStream();
            OutputStreamWriter osw=new OutputStreamWriter(os);
            BufferedWriter bw=new BufferedWriter(osw);
            Log.e("TAG","创建json对象");
            JSONArray jsonArray=new JSONArray();//创建json对象
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("detial",noteBean.getNoteDetail());
            jsonObject.put("title",noteBean.getNoteTitle());
            jsonObject.put("userid",noteBean.getUser().getUserId());
            jsonObject.put("typeid",noteBean.getTypeid());
            jsonArray.put(jsonObject);
            bw.write(jsonArray.toString());
            bw.flush();
            //数据读取流接收数据
            InputStream is=http.getInputStream();
            InputStreamReader isr=new InputStreamReader(is);
            BufferedReader br=new BufferedReader(isr);
            String result=br.readLine();//获取web端返回的数据
            if(result.equals("succeed")){
                //如果返回数据为succeed处理成立
                flag=true;
            }
            if (os!=null)os.close();
            if (osw!=null)osw.close();
            if(is!=null)is.close();
            if(isr!=null)isr.close();
            if(br!=null)br.close();
            if(bw!=null)bw.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  flag;
    }
}
