package jkas.androidpe.translater;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.json.JSONArray;
import android.util.Log;
import org.json.JSONException;

/**
 * @ author JKas
 */
public class TranslateAPI {
    private OnTranslateListener listener;
    private String resp = null;
    private String url = null;
    public String langFrom = null;
    public String langTo = null;
    private ArrayList<String> listData = new ArrayList<>();

    public TranslateAPI(String langFrom, String langTo) {
        this.langFrom = langFrom;
        this.langTo = langTo;
    }

    public void translate() {
        if (listData.size() == 0) {
            if (listener != null) listener.onFailure("The list of data to translate is empty.");
        } else translateAllData();
    }

    private void translateAllData() {
        for (var data : listData) translate(data);
    }

    public synchronized void translate(String data) {
        try {
            resp = null;
            url =
                    "https://translate.googleapis.com/translate_a/single?"
                            + "client=gtx&"
                            + "sl="
                            + langFrom
                            + "&tl="
                            + langTo
                            + "&dt=t&q="
                            + URLEncoder.encode(data, "UTF-8");
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            resp = response.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String temp = "";
        if (resp == null) {
            listener.onFailure("Network Error");
        } else {
            try {
                JSONArray main = new JSONArray(resp);
                JSONArray total = (JSONArray) main.get(0);
                for (int i = 0; i < total.length(); i++) {
                    JSONArray currentLine = (JSONArray) total.get(i);
                    temp = temp + currentLine.get(0).toString();
                }
                if (temp.length() > 2) listener.onSuccess(temp);
                else listener.onFailure("Invalid Input String");
            } catch (JSONException e) {
                listener.onFailure(e.getLocalizedMessage());
                e.printStackTrace();
            }
        }
    }

    public void addListData(ArrayList<String> listData) {
        this.listData.addAll(listData);
    }

    public void addData(String dataToTranslate) {
        this.listData.add(dataToTranslate);
    }

    public void setTranslateListener(OnTranslateListener listener) {
        this.listener = listener;
    }

    public interface OnTranslateListener {
        public void onSuccess(String translatedText);

        public void onFailure(String ErrorText);
    }
}
