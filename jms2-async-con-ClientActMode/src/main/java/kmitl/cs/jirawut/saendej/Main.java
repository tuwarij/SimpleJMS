package kmitl.cs.jirawut.saendej;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        //ConnectionFactory connectionFactory;
        InitialContext initialContext;
        Queue queue;
        Topic topic;
        String destType;
        Destination dest = null;
        if (args.length != 1) {
            System.err.println("Program takes one argument: <dest_type>");
            System.exit(1);
        }

        destType = args[0];
        System.out.println("Destination type is " + destType);

        if (!(destType.equals("queue") || destType.equals("topic"))) {
            System.err.println("Argument must be \"queue\" or \"topic\"");
            System.exit(1);
        }

        try {
            // Lookup the connection factory and destinations
            initialContext = new InitialContext();
            //connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory")


            if (destType.equals("queue")) {
                dest = (Queue) initialContext.lookup("jms/SimpleJMSQueue");
            } else {
                dest = (Topic) initialContext.lookup("jms/SimpleJMSTopic");
            }

            // Try-with-resources to manage the lifecycle of JMS objects
            try (ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory();
                 JMSContext context = connectionFactory.createContext(JMSContext.CLIENT_ACKNOWLEDGE)) {
                JMSConsumer consumer = context.createConsumer(dest);

                consumer.setMessageListener(message -> {
                    if (message instanceof TextMessage) {
                        try {
                            System.out.println("Received message: " + ((TextMessage) message).getText());
                            message.acknowledge();
                        } catch (JMSException e) {
                            System.err.println("Error processing message: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Received non-text message.");
                    }
                });

                System.out.println("To end program, type Q or q, then <return>");
                InputStreamReader inputStreamReader = new InputStreamReader(System.in);
                char answer = '\0';

                while (!((answer == 'q') || (answer == 'Q'))) {
                    try {
                        answer = (char) inputStreamReader.read();
                    } catch (IOException e) {
                        System.err.println("I/O exception: " + e.toString());
                    }
                }
            }
        } catch (NamingException e) {
            System.err.println("NamingException: " + e.getMessage());
        }
    }
}
