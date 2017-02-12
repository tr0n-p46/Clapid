package souvik.hueapplication.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import souvik.hueapplication.Log.PHLog;

/**
 * Created by Souvik Das on 10-Feb-17.
 */

public class PHHttpConnection {

    private static final String TAG = "PHHttpConnection";

    private int timeout = 8000;
    private HttpURLConnection httpURLConnection;

    public enum RequestMethod {
        GET,
        PUT,
        POST,
        DELETE;

        @Override
        public String toString() {
            switch (this) {
                case GET:
                    return "GET";
                case PUT:
                    return "PUT";
                case POST:
                    return "POST";
            }
            return "DELETE";
        }
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    private void openConnection(String url, RequestMethod method) {
        PHLog.d(TAG, "openConnection : " + method + " -> " + url);
        try {
            URL uri = new URL(url);
            httpURLConnection = (HttpURLConnection) uri.openConnection();
            httpURLConnection.setRequestMethod(method.toString());
            httpURLConnection.setConnectTimeout(timeout);
        }
        catch (IOException e) {
            PHLog.e(TAG, "openConnection : " + e.getMessage());
        }
    }

    private void closeConnection() {
        PHLog.d(TAG, "closeConnection : " + "Connection closed.");
        try {
            if(httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            else {
                PHLog.w(TAG, "closeConection : " + "httpURLConnection is null.");
            }
        }
        catch (Exception e) {
            PHLog.e(TAG, "closeConnection : " + e.getMessage());
        }
    }

    public String getData(String url) {
        String response = "";

        try{
            openConnection(url, RequestMethod.GET);
            if(httpURLConnection == null) {
                PHLog.w(TAG, "getData : " + "httpURLConnection is null.");
                return null;
            }
            httpURLConnection.connect();

            Object content = null;
            if(httpURLConnection.getResponseCode() == 200) {
                content = httpURLConnection.getContent();
            }
            if(content == null) {
                PHLog.w(TAG, "getData : " + "Null content.");
                return null;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) content, "UTF-8"));

            StringBuffer sb = new StringBuffer("");
            String newLine = System.getProperty("line.separator");
            String line;
            while((line = in.readLine()) != null) {
                sb.append(line);
                sb.append(newLine);
                if(!in.ready()) {
                    break;
                }
            }
            in.close();
            response = sb.toString();
            PHLog.d(TAG, "getData : " + "Response -> " + response);
        }
        catch (Exception e) {
            PHLog.e(TAG, "getData : " + e.getMessage());
        }
        finally {
            closeConnection();
        }
        return response;
    }

    public String putData(String url, String data) {
        PHLog.d(TAG, "putData : " + data);
        String response = "";

        try{
            openConnection(url, RequestMethod.PUT);
            if(httpURLConnection == null) {
                PHLog.w(TAG, "putData : " + "httpURLConnection is null.");
                return null;
            }
            httpURLConnection.setDoOutput(true);
            httpURLConnection.connect();

            OutputStream os = httpURLConnection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
            writer.write(data);
            writer.close();

            Object content = httpURLConnection.getContent();

            if(content == null) {
                PHLog.w(TAG, "putData : " + "Null content.");
                return null;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) content, "UTF-8"));

            StringBuffer sb = new StringBuffer("");
            String newLine = System.getProperty("line.separator");
            String line;
            while((line = in.readLine()) != null) {
                sb.append(line);
                sb.append(newLine);
                if(!in.ready()) {
                    break;
                }
            }
            in.close();
            response = sb.toString();
            PHLog.d(TAG, "putData : " + "Response -> " + response);

        }
        catch (Exception e) {
            PHLog.e(TAG, "putData : " + e.getMessage());
        }
        finally {
            closeConnection();
        }
        return response;
    }

    public String postData(String url, String data) {
        PHLog.d(TAG, "postData : " + data);
        String response = "";

        try{
            openConnection(url, RequestMethod.POST);
            if(httpURLConnection == null) {
                PHLog.w(TAG, "postData : " + "httpURLConnection is null.");
                return null;
            }
            httpURLConnection.setDoOutput(true);

            OutputStream os = httpURLConnection.getOutputStream();
            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
            writer.write(data);
            writer.close();

            Object content = httpURLConnection.getContent();
            if(content == null) {
                PHLog.w(TAG, "postData : " + "Null content.");
                return null;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader((InputStream)content, "UTF-8"));
            StringBuffer sb = new StringBuffer("");
            String newLine = System.getProperty("line.separator");
            String line;
            while((line = in.readLine()) != null) {
                sb.append(line);
                sb.append(newLine);
                if(!in.ready()) {
                    break;
                }
            }
            in.close();
            response = sb.toString();
            PHLog.d(TAG, "postData : " + "Response -> " + response);
        }
        catch (Exception e) {
            PHLog.e(TAG, "postData : " + e.getMessage());
        }
        finally {
            closeConnection();
        }
        return response;
    }

    public String deleteData(String url) {
        String response = "";

        try{
            openConnection(url, RequestMethod.DELETE);
            if(httpURLConnection == null) {
                PHLog.w(TAG, "deleteData : " + "httpURLConnection is null.");
                return null;
            }
            httpURLConnection.connect();

            Object content = httpURLConnection.getContent();

            if(content == null) {
                PHLog.w(TAG, "deleteData : " + "Null content.");
                return null;
            }
            BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) content, "UTF-8"));

            StringBuffer sb = new StringBuffer("");
            String newLine = System.getProperty("line.separator");
            String line;
            while((line = in.readLine()) != null) {
                sb.append(line);
                sb.append(newLine);
                if(!in.ready()) {
                    break;
                }
            }
            in.close();
            response = sb.toString();
            PHLog.d(TAG, "deleteData : " + "Response -> " + response);
        }
        catch (Exception e) {
            PHLog.e(TAG, "deleteData : " + e.getMessage());
        }
        finally {
            closeConnection();
        }
        return response;
    }

}
