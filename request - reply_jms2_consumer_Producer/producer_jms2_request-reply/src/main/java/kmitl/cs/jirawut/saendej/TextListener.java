package kmitl.cs.jirawut.saendej;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class TextListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage msg = (TextMessage) message;
                System.out.println("Reading message: " + msg.getText() + " " +
                        msg.getJMSCorrelationID());
            } else {
                System.err.println("Message is not a TextMessage");
            }
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage(): " + e);
        } catch (Throwable t) {
            System.err.println("Exception in onMessage(): " + t.getMessage());
        }
    }
}

