package config;
import com.eclipsesource.json.*;
import java.io.*;

/**
 * Created by jasonashton on 4/6/17.
 */
public class loadConfig {
    Reader reader;
    JsonObject auth;

    public loadConfig() {
        reader = startReader();
        try{
            auth = Json.parse(reader).asObject();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public String getTwilioAuth(){
        return auth.get("accountID").asString();
    }

    public String getTwilioPW(){
        return auth.get("accountPW").asString();
    }

    public String getTwilioSendTo(){
        return auth.get("sendto").asString();
    }

    public String getTwilioSendFrom(){
        return auth.get("sendfrom").asString();
    }

    public Reader startReader(){
        Reader reader = null;
        try {
            reader = new FileReader("build/resources/main/config/config.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return reader;
    }
}
