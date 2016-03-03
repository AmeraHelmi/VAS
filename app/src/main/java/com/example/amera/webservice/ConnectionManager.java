package com.example.amera.webservice;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by amera on 3/1/16.
 */
public class ConnectionManager implements AsyncResponse {

    String jsonString = "";
    HttpURLConnection httpURLConnection;
    BufferedReader bufferedReader;
    String connectionFlag;
    String fileUplaodResponse = "";
    Context context;
    ProgressDialog pd;
    public GetJsonString getJsonString = null;

    public ConnectionManager(Context context)
    {
        this.context = context;
        pd = new ProgressDialog(context);
    }

    // for get method
    public void connectionManagerGet(String baseUrl, ArrayList<String> parameters, ArrayList<String> values, String connectionFlag) throws IOException
    {
        this.connectionFlag = connectionFlag;
        String[] asyncTaskParamemters = new String[5];
        String fullUrl = baseUrl + "?";
        Uri.Builder uriBuilder = Uri.parse(fullUrl).buildUpon();
        if (!isNetworkAvailable()) {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
        } else if (parameters.size() != values.size()) {
            Toast.makeText(context, "Parameters count must match values count", Toast.LENGTH_LONG).show();
            this.jsonString = "";
        } else {
            for (int i = 0; i < parameters.size(); i++) {
                uriBuilder.appendQueryParameter(parameters.get(i), values.get(i));
            }
            asyncTaskParamemters[0] = "get";
            asyncTaskParamemters[1] = uriBuilder.toString();
            ConnectionManagerAsyncTask connectionManagerAsyncTask = new ConnectionManagerAsyncTask();
            connectionManagerAsyncTask.delegate = this;
            connectionManagerAsyncTask.execute(asyncTaskParamemters);
        }
    }

    // for post method
    public void connectionManagerPost(String baseUrl, ArrayList<String> parameters, ArrayList<String> values, String connectionFlag) throws UnsupportedEncodingException
    {
        this.connectionFlag = connectionFlag;
        String[] asyncTaskParamemters = new String[5];
        String fullUrl = baseUrl;
        String urlParameters = "";
        if (!isNetworkAvailable()) {
            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_LONG).show();
        } else if (parameters.size() != values.size()) {
            Toast.makeText(context, "Parameters count must match values count", Toast.LENGTH_LONG).show();
            this.jsonString = "";
        } else {
            for (int i = 0; i < parameters.size(); i++) {
                if (i == 0) {
                    urlParameters += parameters.get(i) + "="
                            + URLEncoder.encode(values.get(i), "UTF-8");
                } else {
                    urlParameters += "&" + parameters.get(i) + "="
                            + URLEncoder.encode(values.get(i), "UTF-8");
                }

            }
            asyncTaskParamemters[0] = "post";
            asyncTaskParamemters[1] = baseUrl;
            asyncTaskParamemters[2] = urlParameters;
            ConnectionManagerAsyncTask connectionManagerAsyncTask = new ConnectionManagerAsyncTask();
            connectionManagerAsyncTask.delegate = this;
            connectionManagerAsyncTask.execute(asyncTaskParamemters);
        }
    }

    public void processFinish(String output, String connectionFlag) {
        try {
            getJsonString.getData(output, connectionFlag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public class ConnectionManagerAsyncTask extends AsyncTask<String, Integer, String>
    {
        public AsyncResponse delegate = null;

        @Override
        protected String doInBackground(String... params)
        {
            URL apiUrl = null;
            String jsonData = "";
            try
            {
                apiUrl = new URL(params[1]);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }

            StringBuffer stringBuffer = new StringBuffer();

            if (params[0].equalsIgnoreCase("get")) {

                try {
                    httpURLConnection = (HttpURLConnection) apiUrl.openConnection();
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.connect();

                    InputStream inputStream = httpURLConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line);
                    }
                    jsonData = stringBuffer.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (params[0].equalsIgnoreCase("post")) {
                String urlParameters = params[2];

                try {
                    httpURLConnection = (HttpURLConnection) apiUrl
                            .openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setRequestProperty("Content-Type",
                            "application/x-www-form-urlencoded");
                    httpURLConnection.setRequestProperty("Content-Length", ""
                            + Integer.toString(urlParameters.getBytes().length));
                    httpURLConnection.setRequestProperty("Content-Language",
                            "en-US");

                    httpURLConnection.setUseCaches(false);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.connect();
                    // Send request
                    DataOutputStream wr = new DataOutputStream(
                            httpURLConnection.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();

                    InputStream is = httpURLConnection.getInputStream();
                    BufferedReader rd = new BufferedReader(
                            new InputStreamReader(is));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        stringBuffer.append(line);
                        stringBuffer.append('\r');
                    }
                    rd.close();
                    jsonData = stringBuffer.toString();

                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (params[0].equalsIgnoreCase("fileUpload")) {

                String baseUrl = params[1];
                String parameter = params[2];
                String filePath = params[3];
                try {
                    // Set your file path here
                    FileInputStream fstrm = new FileInputStream(filePath);
                    // Set your server page url (and the file title/description)
                    HttpFileUpload hfu = new HttpFileUpload(baseUrl, parameter, filePath);
                    hfu.Send_Now(fstrm);

                } catch (FileNotFoundException e) {
                    Log.d("Error", "File not found");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                jsonData = fileUplaodResponse;
            }

            Log.d("a", "a");
            return jsonData;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("plz,wait ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            delegate.processFinish(result, connectionFlag);
            pd.hide();
        }
    }

    public class HttpFileUpload implements Runnable {
        URL connectURL;
        String parameter;
        String filePath;
        FileInputStream fileInputStream = null;

        HttpFileUpload(String urlString, String parameter, String filePath) {
            try {
                connectURL = new URL(urlString);
                this.filePath = filePath;
                this.parameter = parameter;
            } catch (Exception ex) {
            }
        }

        void Send_Now(FileInputStream fStream) throws UnsupportedEncodingException {
            fileInputStream = fStream;
            Sending();
        }

        void Sending() throws UnsupportedEncodingException {
            String[] segments = filePath.split("/");
            String iFileName = segments[segments.length - 1];
            String lineEnd = "\r\n";
            String twoHyphens = "--";
            String boundary = "*****";
            String Tag = "fSnd";
            try {
                Log.e(Tag, "Starting Http File Sending to URL");

                // Open a HTTP connection to the URL
                HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", iFileName);

                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);

                //Adding Parameter name
                dos.writeBytes("Content-Disposition: form-data; name=" + parameter + lineEnd);
                dos.writeBytes("Content-Type: text/plain;charset=UTF-8" + lineEnd);
                dos.writeBytes("Content-Length: " + filePath.length() + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(filePath + lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);

                dos.writeBytes("Content-Disposition: form-data; name=" + parameter + ";filename=\"" + iFileName + "\"" + lineEnd);
                dos.writeBytes(lineEnd);


                Log.e(Tag, "Headers are written");

                // create a buffer of maximum size
                int bytesAvailable = fileInputStream.available();

                int maxBufferSize = 1 * 1024 * 1024;
                int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                byte[] buffer = new byte[bufferSize];

                // read file and write it into form...
                int bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // close streams
                fileInputStream.close();

                dos.flush();

                Log.e(Tag, "File Sent, Response: " + String.valueOf(conn.getResponseCode()));

                InputStream is = conn.getInputStream();

                // retrieve the response from server
                int ch;

                StringBuffer b = new StringBuffer();
                while ((ch = is.read()) != -1) {
                    b.append((char) ch);
                }
                fileUplaodResponse = b.toString();

                dos.close();
            } catch (MalformedURLException ex) {
                Log.e(Tag, "URL error: " + ex.getMessage(), ex);
            } catch (IOException ioe) {
                Log.e(Tag, "IO error: " + ioe.getMessage(), ioe);
            }
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}