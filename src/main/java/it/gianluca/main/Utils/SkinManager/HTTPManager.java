package it.gianluca.main.Utils.SkinManager;

import it.gianluca.main.Utils.ConsoleMessages.ConsoleMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class HTTPManager {
    public String get(String url, String arg){
        try{
            HttpURLConnection connection = (HttpURLConnection) new URL(String.format(url, arg)).openConnection();
            if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String output;
                while((output = bufferedReader.readLine()) != null){
                    stringBuilder.append(output);
                }
                return stringBuilder.toString();
            }else{
                return "error";
            }
        }catch(IOException e){
            e.printStackTrace();
            ConsoleMessage.send("Impossibile caricare.");
            return "error";
        }
    }
}
