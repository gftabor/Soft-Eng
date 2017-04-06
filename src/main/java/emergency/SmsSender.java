package emergency;

// Install the Java helper library from twilio.com/docs/java/install
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import config.loadConfig;


import java.net.URISyntaxException;

/**
 * Created by jasonashton on 4/6/17.
 */

public class SmsSender {

    // Find your Account Sid and Auth Token at twilio.com/console
    loadConfig config = new loadConfig();


    public String sendSMS() throws URISyntaxException {
        Twilio.init(config.getTwilioAuth(), config.getTwilioPW());

        Message message = Message
                .creator(new PhoneNumber(config.getTwilioSendTo()),  // to
                        new PhoneNumber(config.getTwilioSendFrom()),  // from
                        "EMERGENCY - VISIT KIOSK")
                .create();
        System.out.println("Message SID: " + message.getSid());
        System.out.println("Message Status: " + message.getStatus());
        return message.getStatus().toString();
    }
}
