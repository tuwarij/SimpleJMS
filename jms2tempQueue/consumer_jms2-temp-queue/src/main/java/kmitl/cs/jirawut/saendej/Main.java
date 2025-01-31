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
        TextListener listener;
        try {
            initialContext = new InitialContext();
            connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            requestQueue = (Queue) initialContext.lookup("RequestQueue");


        } catch (NamingException e) {
            throw new RuntimeException("JNDI lookup failed", e);
        }
        try (JMSContext context = connectionFactory.createContext()) {
            JMSConsumer consumer = context.createConsumer(requestQueue);
            listener = new TextListener(context);
            consumer.setMessageListener(listener);
            System.out.println("Waiting for messages. Press 'q' to quit.");

            Scanner inp = new Scanner(System.in);
            while (true) {
                System.out.print("Press q to quit: ");
                if (inp.nextLine().equals("q")) {
                    break;
                }
            }
        }
    }
}
