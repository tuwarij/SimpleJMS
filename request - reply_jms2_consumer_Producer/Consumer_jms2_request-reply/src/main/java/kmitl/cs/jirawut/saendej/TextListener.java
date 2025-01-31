package kmitl.cs.jirawut.saendej;

import javax.jms.*;

public class TextListener implements MessageListener {
    private final JMSContext context;
    private final JMSProducer producer;

    public TextListener(JMSContext context) {
        this.context = context;
        this.producer = context.createProducer();
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage msg = (TextMessage) message;
                System.out.println("Received message: " + msg.getText());

                // Create and send the response message
                TextMessage response = context.createTextMessage("Hello back");
                response.setJMSCorrelationID(message.getJMSMessageID());
                System.out.println("Sending response: " + response.getText());

                Destination replyDestination = message.getJMSReplyTo();
                if (replyDestination != null) {
                    producer.send(replyDestination, response);
                } else {
                    System.err.println("No reply destination specified.");
                }
            } else {
                System.err.println("Received message is not a TextMessage.");
            }
        } catch (JMSException e) {
            System.err.println("JMSException in onMessage(): " + e.getMessage());
        }
    }
}