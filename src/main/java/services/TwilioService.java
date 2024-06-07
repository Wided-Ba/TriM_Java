package services;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;


public class TwilioService {
    public static final String ACCOUNT_SID = "ACfe64325c54abf3029c0a410fa2ad2a8a";
    public static final String AUTH_TOKEN = "bd36d30f0dbe30fbb804b4dd895f77bc";
    public void sendSMS(String recipient, String messageBody) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(  
                        new com.twilio.type.PhoneNumber(recipient),
                        new com.twilio.type.PhoneNumber("+17813718498"),
                        messageBody)
                .create();
    }


}
