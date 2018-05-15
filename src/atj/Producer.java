package atj;

import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.JMSException;
import javax.jms.Queue;

import com.sun.messaging.ConnectionConfiguration;

import javafx.util.Pair;

public class Producer
{
    private GameStageController.GameLogic game;
    
    Producer(GameStageController.GameLogic game)
    {
	this.game = game;
    }
    
    public void sendQueueMessage(Pair<Integer,Integer> pair)
    {
	ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
	
	try
	{
	    ((com.sun.messaging.ConnectionFactory) connectionFactory).setProperty(ConnectionConfiguration.imqAddressList, "localhost:7676/jms");
	    JMSContext jmsContext = connectionFactory.createContext();
	    JMSProducer jmsProducer = jmsContext.createProducer();
	    Queue queue = new com.sun.messaging.Queue("ATJQueue");
	    
	    Message msg = jmsContext.createTextMessage(pair.getKey().toString() + ":" + pair.getValue().toString());
	    msg.setStringProperty("PlayerID", game.getPlayer());
	    jmsProducer.send(queue, msg);
	    
	    jmsContext.close();
	}
	catch (JMSException e)
	{
	    e.printStackTrace();
	}
    }
}
