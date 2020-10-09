package com.app.sociallogin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Util {

    public static Map<String, String> getQueryMap(String query) {
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();

        for (String param : params) {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, Util.decodeUrl(value));
        }
        return map;
    }

    public static String decodeUrl(String url){

        try {
            String previousUrl = "";
            String decodeUrl = url;
            while (!previousUrl.equals(decodeUrl)){

                previousUrl = decodeUrl;
                decodeUrl = URLDecoder.decode(decodeUrl, "UTF-8");

            }
            return decodeUrl;
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return url;
    }

    public static String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }


}
