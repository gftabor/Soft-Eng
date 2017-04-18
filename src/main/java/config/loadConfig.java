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
        BufferedReader reader = null;



        try{
            InputStream file = getClass().getResourceAsStream("/config/config.json");
            reader = new BufferedReader(new InputStreamReader(file));
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("CONFIGURE CONFIG.JSON");
        }
        /*

        if(Files.exists(Paths.get("/config/config.json"))){
            try {
                reader = new FileReader("/config/config.json");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if(Files.exists(Paths.get("/config/example-config.json"))){
            try {
                reader = new FileReader("/main/config/example-config.json");
                System.out.println("PLEASE CONFIGURE CONFIG.JSON");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        */

        return reader;
    }
}
