package atj;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

public class Main extends Application
{
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage)
    {
	this.primaryStage = primaryStage;
	this.primaryStage.setTitle("Kó³ko i krzy¿yk");
	showGame();
    }

    public static void main(String[] args)
    {
	launch(args);
    }

    private void showGame()
    {
	try
	{
	    FXMLLoader loader = new FXMLLoader(Main.class.getResource("GameWindow.fxml"));
	    AnchorPane root = loader.load();
	    Scene scene = new Scene(root);

	    primaryStage.setScene(scene);
	    primaryStage.show();
	} catch (Exception e)
	{
	    e.printStackTrace();
	}
    }
}
