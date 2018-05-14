package atj;

import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;

import com.sun.messaging.ConnectionConfiguration;

public class PTPConsumer
{
    private Boolean stop;
    private GameStageController.GameLogic game;
    
    PTPConsumer(GameStageController.GameLogic game)
    {
	this.game = game;
	this.stop = false;
    }

    public void setStop(Boolean stop)
    {
	this.stop = stop;
    }

   public void recieveQueueMessages() 
   {
       ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
       JMSContext jmsContext = connectionFactory.createContext();
       
       try
       {
	   ((com.sun.messaging.ConnectionFactory) connectionFactory).setProperty(ConnectionConfiguration.imqAddressList, "localhost:7676/jms");
	   Queue queue = new com.sun.messaging.Queue("ATJQueue");
	   String selector = "PlayerID <> '" + game.getPlayer() + "'";
	   JMSConsumer jmsConsumer = jmsContext.createConsumer(queue, selector);
	   jmsConsumer.setMessageListener(new QueueAsynchConsumer(game));
	   
	   while(!stop)
	   {
	       System.out.println("Konsument wykonuje zadanie");
	       try
	       {
		   Thread.sleep(1000);
	       }
	       catch (InterruptedException e)
	       {
		   e.printStackTrace();
	       }
	   }
	   
	   jmsConsumer.close();
       }
       catch (JMSException e)
       {
	   e.printStackTrace();
       }
       
       jmsContext.close();
   }
}
