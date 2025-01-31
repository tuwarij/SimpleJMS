package kmitl.cs.jirawut.saendej;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InitialContext initialContext;
        ConnectionFactory connectionFactory;
        Queue requestQueue;
        Queue replyQueue;

        try {
            initialContext = new InitialContext();
            connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            requestQueue = (Queue) initialContext.lookup("RequestQueue");
            replyQueue = (Queue) initialContext.lookup("ReplyQueue");

            try (JMSContext context = connectionFactory.createContext()) {
                JMSProducer producer = context.createProducer();
                JMSConsumer responseConsumer = context.createConsumer(replyQueue);

                responseConsumer.setMessageListener(new TextListener());

                TextMessage message = context.createTextMessage("Hello friend");
                message.setJMSReplyTo(replyQueue);
                message.setJMSCorrelationID("12345");

                System.out.println("Sending message: " + message.getText());
                producer.send(requestQueue, message);
                System.out.println("Message ID: " + message.getJMSMessageID());

                Scanner inp = new Scanner(System.in);
                String ch;
                while (true) {
                    System.out.print("Press q to quit: ");
                    ch = inp.nextLine();
                    if (ch.equals("q")) {
                        break;
                    }
                }
            }
        } catch (NamingException | JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
