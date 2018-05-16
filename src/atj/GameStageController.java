package atj;

import java.io.File;
import java.util.concurrent.ThreadLocalRandom;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Pair;

public class GameStageController
{
    private final static int size = 3;
    GameLogic game;
    PlayerInfoConsumer PIConsumer;
    ImageView[][] imageBoard;

    @FXML
    Text txt;
    @FXML
    ImageView iv00;
    @FXML
    ImageView iv10;
    @FXML
    ImageView iv20;
    @FXML
    ImageView iv01;
    @FXML
    ImageView iv02;
    @FXML
    ImageView iv11;
    @FXML
    ImageView iv12;
    @FXML
    ImageView iv21;
    @FXML
    ImageView iv22;
    @FXML
    Button btnStart;

    @FXML
    public void initialize()
    {
	game = new GameLogic();
	game.setProducer(new Producer(game));
	PIConsumer = new PlayerInfoConsumer(game);

	imageBoard = new ImageView[size][size];
	imageBoard[0][0] = iv00;
	imageBoard[0][1] = iv01;
	imageBoard[0][2] = iv02;
	imageBoard[1][0] = iv10;
	imageBoard[1][1] = iv11;
	imageBoard[1][2] = iv12;
	imageBoard[2][0] = iv20;
	imageBoard[2][1] = iv21;
	imageBoard[2][2] = iv22;

	game.disableGrid();
    }

    @FXML
    private void iv00_clicked()
    {
	game.doMove(new Pair<Integer, Integer>(0, 0));
    }

    @FXML
    private void iv01_clicked()
    {
	game.doMove(new Pair<Integer, Integer>(0, 1));
    }

    @FXML
    private void iv02_clicked()
    {
	game.doMove(new Pair<Integer, Integer>(0, 2));
    }

    @FXML
    private void iv10_clicked()
    {
	game.doMove(new Pair<Integer, Integer>(1, 0));
    }

    @FXML
    private void iv11_clicked()
    {
	game.doMove(new Pair<Integer, Integer>(1, 1));
    }

    @FXML
    private void iv12_clicked()
    {
	game.doMove(new Pair<Integer, Integer>(1, 2));
    }

    @FXML
    private void iv20_clicked()
    {
	game.doMove(new Pair<Integer, Integer>(2, 0));
    }

    @FXML
    private void iv21_clicked()
    {
	game.doMove(new Pair<Integer, Integer>(2, 1));
    }

    @FXML
    private void iv22_clicked()
    {
	game.doMove(new Pair<Integer, Integer>(2, 2));
    }

    @FXML
    private void btnStart_clicked()
    {
	double ourRand = ThreadLocalRandom.current().nextDouble(0, 1);
	game.getProducer().sendPlayerInfo(ourRand);
	btnStart.setText("Czekam...");
	btnStart.setDisable(true);

	double theirRand = PIConsumer.recieveQueueMessages(ourRand);
	btnStart.setText("Gotowe");

	if (ourRand > theirRand)
	{
	    game.setPlayer('O');
	    btnStart.setText("O");
	    txt.setText("Twój ruch");
	    game.setConsumer(new Consumer(game));
	    game.enableGrid();
	} else
	{
	    btnStart.setText("X");
	    txt.setText("Przeciwnik wykonuje ruch...");
	    game.setPlayer('X');
	    game.setConsumer(new Consumer(game));
	}
    }

    public GameLogic getGameLogic()
    {
	return game;
    }

    private void draw(Pair<Integer, Integer> location, char player)
    {
	if (player == 'O')
	    drawO(location);
	else
	    drawX(location);
    }

    private void drawX(Pair<Integer, Integer> location)
    {
	File file = new File("X.png");
	Image image = new Image(file.toURI().toString());
	imageBoard[location.getKey()][location.getValue()].setImage(image);
    }

    private void drawO(Pair<Integer, Integer> location)
    {
	File file = new File("O.png");
	Image image = new Image(file.toURI().toString());
	imageBoard[location.getKey()][location.getValue()].setImage(image);
    }

    public class GameLogic
    {
	private char[][] board;
	int moveCount;
	private char player;
	private char otherPlayer;
	private Producer producer;
	private Consumer consumer;

	GameLogic()
	{
	    moveCount = 0;
	    board = new char[size][size];
	    for (int i = 0; i < size; i++)
		for (int j = 0; j < size; j++)
		    board[i][j] = '0';
	}

	public void registerMove(Pair<Integer, Integer> location)
	{
	    Boolean gameOver = false;
	    board[location.getKey()][location.getValue()] = otherPlayer;
	    draw(location, otherPlayer);
	    moveCount++;
	    if (moveCount > 4) // Win possible only after 4th move
		gameOver = checkVictoryCondition(otherPlayer, location);
	    if (!gameOver)
	    {
		enableGrid();
		txt.setText("Twój ruch");
	    }
	}

	public void doMove(Pair<Integer, Integer> location)
	{
	    Boolean gameOver = false;
	    board[location.getKey()][location.getValue()] = player;
	    draw(location, player);
	    producer.sendQueueMessage(location);
	    moveCount++;
	    if (moveCount > 4) // Win possible only after 4th move
		gameOver = checkVictoryCondition(player, location);
	    disableGrid();
	    if (!gameOver)
		txt.setText("Przeciwnik wykonuje ruch...");
	}

	public void setProducer(Producer producer)
	{
	    this.producer = producer;
	}

	public Producer getProducer()
	{
	    return producer;
	}

	public void setConsumer(Consumer consumer)
	{
	    this.consumer = consumer;
	}

	public Consumer getConsumer()
	{
	    return consumer;
	}

	public void setPlayer(char player)
	{
	    this.player = player;

	    if (player == 'O')
		this.otherPlayer = 'X';
	    else
		this.otherPlayer = 'O';
	}

	public String getPlayer()
	{
	    return Character.toString(player);
	}

	private void disableGrid()
	{
	    for (int i = 0; i < size; i++)
		for (int j = 0; j < size; j++)
		    imageBoard[i][j].setDisable(true);
	}

	private void enableGrid()
	{
	    for (int i = 0; i < size; i++)
		for (int j = 0; j < size; j++)
		    imageBoard[i][j].setDisable(false);
	}

	private Boolean checkVictoryCondition(char player, Pair<Integer, Integer> location)
	{
	    Boolean gameOver = false;
	    char winner = '0';
	    int x = location.getKey();
	    int y = location.getValue();

	    // check columns
	    for (int i = 0; i < size; i++)
	    {
		if (board[x][i] != player)
		    break;
		if (i == size - 1)
		{
		    gameOver = true;
		    winner = player;
		}
	    }

	    // check rows
	    for (int i = 0; i < size; i++)
	    {
		if (board[i][y] != player)
		    break;
		if (i == size - 1)
		{
		    gameOver = true;
		    winner = player;
		}
	    }

	    // check diagonal
	    if (x == y)
	    {
		for (int i = 0; i < size; i++)
		{
		    if (board[i][i] != player)
			break;
		    if (i == size - 1)
		    {
			gameOver = true;
			winner = player;
		    }
		}
	    }

	    // check anti diagonal
	    if (x + y == size - 1)
	    {
		for (int i = 0; i < size; i++)
		{
		    if (board[i][(size - 1) - i] != player)
			break;
		    if (i == size - 1)
		    {
			gameOver = true;
			winner = player;
		    }
		}
	    }

	    // check draw
	    if (!gameOver && moveCount == (Math.pow(size, 2)))
	    {
		gameOver = true;
	    }

	    if (gameOver)
	    {
		disableGrid();
		consumer.close();
		System.out.println("koniec gry");
		if (winner == '0')
		    txt.setText("Remis!");
		else
		    txt.setText("Wygrywa " + winner + "!");
	    }

	    return gameOver;
	}
    }
}
