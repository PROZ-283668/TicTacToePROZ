package atj;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;

import com.sun.messaging.ConnectionConfiguration;

public class PlayerInfoConsumer
{
    GameStageController.GameLogic game;

    PlayerInfoConsumer(GameStageController.GameLogic game)
    {
	this.game = game;
    }

    public double recieveQueueMessages(double key)
    {
	ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
	JMSContext jmsContext = connectionFactory.createContext();
	double result = -1;

	try
	{
	    ((com.sun.messaging.ConnectionFactory) connectionFactory)
		    .setProperty(ConnectionConfiguration.imqAddressList, "localhost:7676/jms");
	    Queue queue = new com.sun.messaging.Queue("ATJQueue");
	    String selector = "Key <> '" + key + "' AND Key IS NOT NULL";
	    JMSConsumer jmsConsumer = jmsContext.createConsumer(queue, selector);

	    System.out.println("Konsument czeka na info");

	    System.out.println("odb");
	    Message msg;

	    msg = jmsConsumer.receive();
	    System.out.println("odb2");
	    double res = -1.0;
	    try
	    {
		if (msg != null)
		    res = Double.parseDouble(msg.getStringProperty("Key"));
	    } catch (JMSException e)
	    {
		e.printStackTrace();
	    }
	    System.out.println("!odbieram" + res);

	    result = res;

	    jmsConsumer.close();
	} catch (

	JMSException e)
	{
	    e.printStackTrace();
	}

	jmsContext.close();
	return result;
    }
}
