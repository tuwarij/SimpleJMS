package kmitl.cs.jirawut.saendej;

import javax.jms.*;

public class TextListener implements MessageListener {
    private JMSProducer replyProducer;
    private JMSContext context;

    public TextListener(JMSContext context) {
        this.context = context;
        this.replyProducer = context.createProducer();
    }

    @Override
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage msg = (TextMessage) message;
                System.out.println("Received message: " + msg.getText());

                // Create reply message
                TextMessage response = context.createTextMessage("Hello back");
                response.setJMSCorrelationID(message.getJMSCorrelationID());
                System.out.println("Sending reply: " + response.getText());

                // Send reply if reply-to address is available
                if (message.getJMSReplyTo() != null) {
                    replyProducer.send(message.getJMSReplyTo(), response);
                } else {
                    System.out.println("No ReplyTo destination found.");
                }
            } else {
                System.err.println("Received non-text message.");
            }
        } catch (JMSException e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }
}
