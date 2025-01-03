package kmitl.cs.jirawut.saendej;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        JMSTextMessageProducer scoreProducer = getScoreProducer();
        Scanner inp = new Scanner(System.in);
        String scoreStr = null;
        while(true) {
            System.out.print("Enter Live Score ");
            scoreStr = inp.nextLine();
            if (!scoreStr.isEmpty()) {
                scoreProducer.sendTextMessage(scoreStr);
            }
            else {
                break;
            }
        }
        scoreProducer.close();
    }
    private static JMSTextMessageProducer getScoreProducer() {
        InitialContext initialContext;
        ConnectionFactory connectionFactory;
        Queue queue;
        Topic topic;
        try {
            initialContext = new InitialContext();
            connectionFactory = (ConnectionFactory) initialContext.lookup("ConnectionFactory");
            queue = (Queue) initialContext.lookup("jms/SimpleJMSQueue");
            //topic = (Topic) initialContext.lookup("jms/SimpleJMSQueue");
        }  catch (NamingException e) {
            throw new RuntimeException(e);
        }
        JMSTextMessageProducer scoreProducer;
        scoreProducer = new JMSTextMessageProducer(connectionFactory,queue,
                false, Session.CLIENT_ACKNOWLEDGE);
        return scoreProducer;
    }
}