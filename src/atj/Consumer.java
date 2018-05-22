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

import javafx.application.Platform;
import javafx.util.Pair;

public class Consumer implements MessageListener
{
	GameStageController.GameLogic game;
	JMSConsumer jmsConsumer;
	JMSContext jmsContext;

	Consumer(GameStageController.GameLogic game)
	{
		this.game = game;

		ConnectionFactory connectionFactory = new com.sun.messaging.ConnectionFactory();
		jmsContext = connectionFactory.createContext();

		try
		{
			((com.sun.messaging.ConnectionFactory) connectionFactory)
					.setProperty(ConnectionConfiguration.imqAddressList, "localhost:7676/jms");
			Queue queue = new com.sun.messaging.Queue("ATJQueue");
			String selector = "PlayerID <> '" + game.getPlayer() + "' AND PlayerID IS NOT NULL";
			jmsConsumer = jmsContext.createConsumer(queue, selector);
			jmsConsumer.setMessageListener(this);

		} catch (JMSException e)
		{
			e.printStackTrace();
		}
	}

	public void close()
	{
		jmsConsumer.close();
		jmsContext.close();
	}

	@Override
	public void onMessage(Message message)
	{
		Platform.runLater(() -> {
			try
			{
				TextMessage msg = (TextMessage) message;
				String text = msg.getStringProperty("Move");
				System.out.println("Asynch odbior " + text);
				String[] coordinates = text.split(":");
				Pair<Integer, Integer> location = new Pair<Integer, Integer>(Integer.parseInt(coordinates[0]),
						Integer.parseInt(coordinates[1]));

				game.registerMove(location);
			} catch (JMSException e)
			{
				e.printStackTrace();
			}
		});
	}
}
