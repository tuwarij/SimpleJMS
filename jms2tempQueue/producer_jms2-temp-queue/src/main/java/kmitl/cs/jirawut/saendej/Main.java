package kmitl.cs.jirawut.saendej;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        InitialContext initialContext;
        ConnectionFactory connectionFactory = null;
        Queue requestQueue = null;
        TextMessage message;

        try {
            initialContext = new InitialContext();
            connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            requestQueue = (Queue) initialContext.lookup("RequestQueue");

        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        try (JMSContext context = connectionFactory.createContext()) {
            JMSProducer producer = context.createProducer();
            Queue tempDest = context.createTemporaryQueue();
            JMSConsumer responseConsumer = context.createConsumer(tempDest);

            responseConsumer.setMessageListener(new TextListener());

            message = context.createTextMessage("Hello friend");
            message.setJMSReplyTo(tempDest);
            message.setJMSCorrelationID("12345");

            System.out.println("Sending message: " + message.getText());
            producer.send(requestQueue, message);

            Scanner inp = new Scanner(System.in);
            while (true) {
                System.out.print("Press q to quit: ");
                if ("q".equals(inp.nextLine())) {
                    break;
                }
            }
        } catch (JMSException e) {
            throw new RuntimeException(e);
        }
    }
}
