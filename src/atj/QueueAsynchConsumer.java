package atj;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import javafx.util.Pair;

public class QueueAsynchConsumer implements MessageListener
{
    GameStageController.GameLogic game;
    
    QueueAsynchConsumer(GameStageController.GameLogic  game)
    {
	this.game = game;
    }

    @Override
    public void onMessage(Message message)
    {
	TextMessage msg = (TextMessage) message;
	
	try
	{
	    String text = msg.getText();
	    String[] coordinates = text.split(":");
	    Pair<Integer,Integer> location = new Pair<Integer,Integer>(Integer.parseInt(coordinates[0]), Integer.parseInt(coordinates[1]));
	    
	    game.registerMove(location);
	}
	catch (JMSException e)
	{
	    e.printStackTrace();
	}
    }

}
