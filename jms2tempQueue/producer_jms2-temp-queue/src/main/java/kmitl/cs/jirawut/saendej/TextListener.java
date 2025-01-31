package kmitl.cs.jirawut.saendej;


import javax.jms.*;

public class TextListener implements MessageListener {
    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                System.out.println("Reading message: " + textMessage.getText() + " " + textMessage.getJMSCorrelationID());
            } else {
                System.err.println("Message is not a TextMessage");
            }
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage(): " + e.getMessage());
        }
    }
}
