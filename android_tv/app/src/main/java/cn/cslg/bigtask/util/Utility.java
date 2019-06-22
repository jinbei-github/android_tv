package cn.cslg.bigtask.util;

import com.google.gson.Gson;

import org.json.JSONObject;

import cn.cslg.bigtask.bean.Channel;
import cn.cslg.bigtask.bean.Program;


public class Utility {



    /**
     * 将返回的JSON数据解析成频道实体类
     */
    public static Channel handleChannelResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String weatherContent = jsonObject.toString();
            return new Gson().fromJson(weatherContent, Channel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将返回的JSON数据解析成节目实体类
     */
    public static Program handlePorgramResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String weatherContent = jsonObject.toString();
            return new Gson().fromJson(weatherContent, Program.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}
