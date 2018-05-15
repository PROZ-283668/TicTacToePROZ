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

public class PlayerInfoConsumer
{
	GameStageController.GameLogic game;

	PlayerInfoConsumer(GameStageController.GameLogic game)
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
			JMSConsumer jmsConsumer = jmsContext.createConsumer(queue);

			System.out.println("Konsument czeka na info");
			TextMessage message;

			Thread.sleep(10000);
			while (game.getPlayerNotSet())
			{
				Message m = jmsConsumer.receive(1);
				if (m != null)
				{
					if (m instanceof TextMessage)
					{
						message = (TextMessage) m;
						game.setPlayer(message.getText().charAt(0));
					} else
					{
						break;
					}
				}
			}

			jmsConsumer.close();
		} catch (JMSException e)
		{
			e.printStackTrace();
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		jmsContext.close();
	}
}
