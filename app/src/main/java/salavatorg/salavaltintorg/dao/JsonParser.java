package salavatorg.salavaltintorg.dao;

import android.util.Log;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 *
 * @author Radan
 * JsonParser class get URL and list of value as Map parameter
 * then will send to server and return the JSON.
 *
 */

/**
 * Created by reza on 7/4/17.
 */

public class JsonParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "", tag = "JsonParser";

    private static final String SERVER = "http://172.20.30.209:8080/";
    private static final String WEB_SERVICE_SERVER_TEST = "http://daba.dabacenter.ir:8081";

    /*
     constructor
     */
    public JsonParser() {
    }


    public static boolean isWebServiceAvailable() {
        String url = SERVER;
        boolean result = false;
        Log.i(tag, " SERVER ADDRESS : " + url);

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();
            Log.i(tag, "response code : " + response.code());
            if (response.code() == 200)
                result = true;
        } catch (Exception e) {
            Log.i(tag, "Error in check web service : " + e);
        }

        return result;
    }
    //-------------------------------------------

    public static JSONObject getJSONFromUrl(String url, Map<String, String> params, String type,boolean AuthorizationFlag) {

        JSONObject jsonObject = null;
        String json = "", strParam = "";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        url = SERVER + url;
        Log.i(tag, " SERVER ADDRESS : " + url);


        try {
            if (params != null) {
                for (String key : params.keySet()) {
                    if (!strParam.isEmpty())
                        strParam += ", ";
                    strParam += "\"" + key + "\":\"" + params.get(key) + "\"";
                }
                strParam = "{" + strParam + "}";
                Log.i(tag, " Map Param after convert to string : " + strParam);
            }

            if (type.equalsIgnoreCase("POST")) {
                 /*
                   USE POST Method
                 */
                RequestBody body = RequestBody.create(JSON, strParam);
                Request request;

                if(AuthorizationFlag) {
                             request = new Request.Builder().
                             url(url)
                            .post(body)
//                            .addHeader("Authorization", SplashActivity.loginTokenValue)
                            .build();
                }else{
                             request = new Request.Builder().
                             url(url)
                            .post(body)
                            .build();
                }

                Response response = client.newCall(request).execute();
                json = response.body().string();
                Log.i(tag, "json get POST method response.body().string() : " + json);

            } else if (type.equalsIgnoreCase("GET")) {
                /*
                   USE GET Method
                 */
                Log.i(tag, " SERVER ADDRESS GET : " + url);
                Request request;
                if(AuthorizationFlag) {
                    request = new Request.Builder()
                                .url(url)
//                                .addHeader("Authorization", SplashActivity.loginTokenValue)
                                .build();
                }else{
                    request = new Request.Builder().url(url).build();

                }
                Response response = client.newCall(request).execute();
                json = response.body().string();
                Log.i(tag, "json get GET method  response.body().string() : " + json);

            }  else if (type.equalsIgnoreCase("PUT")) {

                /*
                   USE PUT Method
                 */
                Log.i(tag, " SERVER ADDRESS GET : " + url);
                RequestBody body = RequestBody.create(JSON, strParam);
                Request request;
                if(AuthorizationFlag) {
                    request = new Request.Builder()
                                .url(url)
                                .put(body)
//                                .addHeader("Authorization", SplashActivity.loginTokenValue)
                                .build();
                }else{
                    request = new Request.Builder().url(url).build();

                }
                Response response = client.newCall(request).execute();
                json = response.body().string();
                Log.i(tag, "json get GET method  response.body().string() : " + json);
            }
            /*
              Convert to json object 
             */
            jsonObject = new JSONObject(json);



        } catch (Exception e) {
            Log.e(tag, "Error in getting Json getJSONFromUrl " + e);
            return jsonObject;
        }

        Log.d(tag, " jsonObject before send : " + jsonObject);
        return jsonObject;
    }


    //-------------------------------------------


    public static JSONObject getJSONFromUrlInputStream(String url, Map<String, String> params, String type) {

        InputStream inputStream = null;
        JSONObject jsonObject = null;
        String json = "", strParam = "";

        url = SERVER + url;
        Log.i(tag, " InputStream SERVER ADDRESS : " + url);
        HttpClient httpClient = new DefaultHttpClient();

        StringEntity jsonParam = null;

        try {
            if (params != null) {

                for (String key : params.keySet()) {
                    if (!strParam.isEmpty())
                        strParam += ", ";
                    strParam += "\"" + key + "\":\"" + params.get(key) + "\"";
                }
                strParam = "{" + strParam + "}";

                jsonParam = new StringEntity(strParam, "UTF-8");
                Log.i(tag, "InputStream Map Param after convert : " + strParam);
            }

            if (type.equalsIgnoreCase("POST")) {

                Log.i(tag, " InputStream json get POST method");
                HttpPost httpPost = new HttpPost(url);
                httpPost.setHeader("Content-Type", "application/json");
                httpPost.setHeader("Accept", "application/json");
                httpPost.setEntity(jsonParam);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

                Log.i(tag, "InputStream json POST  method received inputStream  " + inputStream);


            } else if (type.equalsIgnoreCase("GET")) {

                Log.i(tag, "InputStream json get GET method ");
                HttpGet httpGet = new HttpGet(url);
                httpGet.setHeader("Content-Type", "application/json");
                httpGet.setHeader("Accept", "application/json");

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                inputStream = httpEntity.getContent();

                Log.i(tag, "InputStream json GET method received inputStream : " + inputStream);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(tag, "Error in encoding Json " + e);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(tag, "Error in I/O Json " + e);
        } catch (Exception e) {
            Log.e(tag, "Error in getting Json " + e);
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            json = sb.toString();
            jsonObject = new JSONObject(json);
        } catch (Exception e) {
            Log.e(tag, "Error in BufferedReader " + e);
        }
        Log.d(tag, " InputStream JsonObject before send : " + jsonObject);
        return jsonObject;
    }


    public static void uploadFile(String url, String fileName, String format, String token, File file, String UserID) {

        try {
            OkHttpClient client = new OkHttpClient();
            MediaType MEDIA_TYPE = MediaType.parse("txt/" + format); // e.g. "image/png"
            MultipartBody multipartBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("action", "upload")
                    .addFormDataPart("id", UserID)
                    .addFormDataPart("file", "fileName", RequestBody.create(MEDIA_TYPE, file))
                    .addFormDataPart("format", "json")
                    .addFormDataPart("filename", fileName + "." + format) //e.g. title.png --> imageFormat = png
                    .build();


            url = SERVER + url;
            Log.i(tag, " SERVER URL " + url);
            Request request = new Request.Builder().url(url).post(multipartBody).build();
            Response response = client.newCall(request).execute();
            json = response.body().string();
        } catch (Exception e) {
            Log.i(tag, " Error in : " + e);
        }
        Log.i(tag, "json get POST method response.body().string() : " + json);
    }

    public static File downloadFile(String url, String id, String path) {
        File file = null;
        try {
            url = SERVER + url;
            Log.i(tag, " SERVER URL " + url + " Path " + path);

            OkHttpClient client = new OkHttpClient();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            String strParam = "\"" + "id" + "\":\"" + id + "\"";
            strParam = "{" + strParam + "}";
            Log.i(tag, " file Id : " + strParam);

            RequestBody body = RequestBody.create(JSON, strParam);

            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();

            byte[] bytes = response.body().bytes();
            Log.i(tag, "InputStream lenghth " + bytes.length);
            if (bytes != null)
                file = inputStreamToFile(bytes, path);
            response.body().close();

            Log.i(tag, "File lenghth " + file.length());
            return file;
        } catch (Exception e) {
            Log.e(tag, " Error in downloadFile " + e);
            return file;
        }

    }

    public static File inputStreamToFile(byte[] bytes, String path) {

        Log.e(tag, " path " + path + " bytes " + bytes.length);
        File targetFile = null;
        try {
            targetFile = new File(path);
            OutputStream outStream = new FileOutputStream(targetFile);
            outStream.write(bytes);
            return targetFile;
        } catch (Exception e) {
            Log.e(tag, "Error in file convertion  " + e);
            return targetFile;
        }
    }


    //------------------------------------------------------------------
    public static JSONObject getJSONDirectlyGET(String url) {

        JSONObject jsonObject = null;
        String json = "";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        try {
                  /*
                   USE GET Method
                 */
            Log.i(tag, " SERVER ADDRESS getJSONDirectlyGET : " + url);
            Request request = new Request.Builder()
                    .url(url)
                    .addHeader("x-li-format", "json")
                    .build();
            Response response = client.newCall(request).execute();
            json = response.body().string();
            Log.i(tag, "json get GET method  response.body().string() : " + json);
            /*
              Convert to json object
             */
            jsonObject = new JSONObject(json);

        }  catch (Exception e) {
            Log.e(tag, "Error in getting Json " + e);
            return jsonObject;
        }

        Log.d(tag, " jsonObject before send getJSONDirectlyGET : " + jsonObject);
        return jsonObject;
    }


    public static boolean checkUrlAvailability(String url) {

        OkHttpClient client = new OkHttpClient();
        try {
            Log.i(tag, " checkUrlAvailability ADDRESS : " + url);
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            Log.i(tag, " checkUrlAvailability code : " + response.code());
            if (response.code() == 200)
                return true;
            else
                return false;
        } catch (Exception e) {
            Log.e(tag, "Error in getting Json " + e);
            return false;
        }
    }
    //-----------------------------------------------------------------------------------

    public static boolean checkUrlAvailabilityHttps(String url) {
        try {
            URL ur = new URL(url);
            HttpsURLConnection http = (HttpsURLConnection) ur.openConnection();
            http.setInstanceFollowRedirects(true);
            http.connect();
            Log.i("tag", "http.getResponseCode(): " + http.getResponseCode());
            if (http.getResponseCode() == 200)
                return true;
            else
                return false;
        } catch (Exception e) {
            Log.e(tag, "Error in getting Json in checkUrlAvailabilityHttps " + e);
            return false;
        }
    }
}
