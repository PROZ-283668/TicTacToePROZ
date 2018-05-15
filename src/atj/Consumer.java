package atj;

import javax.jms.ConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.TextMessage;

import com.sun.messaging.ConnectionConfiguration;

import javafx.util.Pair;

public class Consumer implements MessageListener
{
	GameStageController.GameLogic game;

	Consumer(GameStageController.GameLogic game)
	{
		this.game = game;
	}

	public void recieveQueueMessages()
	{
		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		JMSContext jmsContext = connectionFactory.createContext();

		try
		{
			((com.sun.messaging.ConnectionFactory) connectionFactory)
					.setProperty(ConnectionConfiguration.imqAddressList, "localhost:7676/jms");
			Queue queue = new com.sun.messaging.Queue("ATJQueue");
			String selector = "PlayerID <> '" + game.getPlayer() + "'";
			JMSConsumer jmsConsumer = jmsContext.createConsumer(queue, selector);
			jmsConsumer.setMessageListener(this);
			
			Runnable idleRunnable = new Runnable()
					{
						@Override
						public void run()
						{
							while(true)
							{
								
							}
						}
					};
			
			Thread idleThread = new Thread(idleRunnable, "idleThread");
			idleThread.start();

			jmsConsumer.close();
		} catch (JMSException e)
		{
			e.printStackTrace();
		}

		jmsContext.close();
	}

	@Override
	public void onMessage(Message message)
	{
		TextMessage msg = (TextMessage) message;

		try
		{
			String text = msg.getText();
			String[] coordinates = text.split(":");
			Pair<Integer, Integer> location = new Pair<Integer, Integer>(Integer.parseInt(coordinates[0]),
					Integer.parseInt(coordinates[1]));

			game.registerMove(location);
		} catch (JMSException e)
		{
			e.printStackTrace();
		}
	}

}
