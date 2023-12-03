package vsb_cs_java.pong;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.HBox;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;


public class GameController implements Serializable
{
	private static final long serialVersionUID = 1L;

	private Game game;
	
	@FXML
	transient private Canvas canvas;
	
	@FXML
	transient private Slider p1Slider;
	
	@FXML
	transient private Slider p2Slider;
	
	@FXML
	transient private TextField player1Name;

    @FXML
    transient private TextField player2Name;

    @FXML
    transient private Button startButton;
    
    @FXML
    transient private HBox hboxTop;
    
    @FXML
    transient private Label p1Score;
    
    @FXML
    transient private Label p2Score;
    
    @FXML
    transient private Label player1Label;
    
    @FXML
    transient private Label player2Label;
      
    @FXML
    transient private TableView<Score> highScores;
    
    @FXML
    transient private TableColumn<Score, String> pl1Name;

    @FXML
    transient private TableColumn<Score, Integer> pl1Score;

    @FXML
    transient private TableColumn<Score, String> pl2Name;

    @FXML
    transient private TableColumn<Score, Integer> pl2Score;
    
    @FXML
    transient private TableColumn<Score, Integer> id;
    
	private AnimationTimer animationTimer;
	
	private ScoreDAO scoreDAO;
	
	private String p1Name;
	private String p2Name;
	
	public void startGame() 
	{
		scoreDAO = new ScoreDAO();
		
		this.game = new Game(canvas.getWidth(), canvas.getHeight(), p1Name, p2Name);

		animationTimer = new DrawingThread(canvas, game);
		
		p1Slider.valueProperty().addListener( (obj, oldValue, newValue) -> game.setHeigtOfLBat(newValue.doubleValue()));
		p2Slider.valueProperty().addListener( (obj, oldValue, newValue) -> game.setHeigtOfRBat(newValue.doubleValue()));
		
		
		loadFromDb();
		
		game.setGameListener(new EmptyGameListener () {

			@Override
			public void stateChanged(int p1Score, int p2Score) {
				GameController.this.p1Score.setText("" + p1Score);
				GameController.this.p2Score.setText("" + p2Score);
				if(p1Score == 3 || p2Score == 3) {		
					Score newScore = new Score(GameController.this.p1Name, p1Score, GameController.this.p2Name, p2Score);
					save(GameController.this.p1Name, p1Score, GameController.this.p2Name, p2Score);
					scoreDAO.saveScore(newScore);
					saveTournamentsXML();
					stopGame();
				}
			}
			
		} );
		
		
		animationTimer.start();
	}

	public void stopGame() 
	{
		animationTimer.stop();
	}
	
	public void save(String p1Name, int p1Score, String p2Name, int p2Score) 
	{
		try (PrintWriter pw = new PrintWriter(new FileWriter("data.csv",true))) {
				pw.printf("%s;%d;%s;%d\n", p1Name, p1Score, p2Name, p2Score);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadFromDb() {
		var list = scoreDAO.load();
		id.setCellValueFactory(new PropertyValueFactory<>("id"));
		pl1Name.setCellValueFactory(new PropertyValueFactory<>("pl1Name"));
		pl1Score.setCellValueFactory(new PropertyValueFactory<>("pl1Score"));
		pl2Name.setCellValueFactory(new PropertyValueFactory<>("pl2Name"));
		pl2Score.setCellValueFactory(new PropertyValueFactory<>("pl2Score"));
        highScores.setItems(FXCollections.observableList(list));
	}
	
	private void load() {
		List<Score> scores = new LinkedList<>();
		scores.clear();
		try (BufferedReader br = new BufferedReader(new FileReader("data.csv"))){
			String line;
			while (null != (line = br.readLine())) {
				String [] token = line.split(";");
				Score score = new Score(token[0],token[2]);
				score.setScore(Integer.parseInt(token[1]), Integer.parseInt(token[3]));
				scores.add(score);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Collections.sort(scores,(o1, o2) ->  Integer.compare(o2.getLowerScore(), o1.getLowerScore()));
		highScores.setItems(FXCollections.observableList(scores));
	}
	

    @FXML
    void startPressed(ActionEvent event) 
    {
    	startGame();
    	p1Name = player1Name.getText();
    	p2Name = player2Name.getText();
    	player1Label.setText(p1Name);
    	player2Label.setText(p2Name);
    	hboxTop.setVisible(false);
    	hboxTop.setManaged(false);
    	p1Slider.setVisible(true);
    	p2Slider.setVisible(true);
    	p1Score.setVisible(true);
    	p2Score.setVisible(true);
    	highScores.setVisible(true);
    	player1Label.setVisible(true);
    	player2Label.setVisible(true);
    }

    @FXML
    void savePressed(ActionEvent event) 
    {
    	try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("score.obj"))) {
			os.writeObject(game.getScore());
		} catch (IOException e) {
			e.printStackTrace();
		}/*
    	try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("lBat.obj"))) {
			os.writeObject(game.getLBat());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("rBat.obj"))) {
			os.writeObject(game.getRBat());
		} catch (IOException e) {
			e.printStackTrace();
		}
    	try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("ball.obj"))) {
			os.writeObject(game.getBall());
		} catch (IOException e) {
			e.printStackTrace();
		}*/
    }

    @FXML
    void loadPressed(ActionEvent event) 
    {
    	try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(("score.obj")))) {
			game.setScore( (Score) ois.readObject());
			p1Score.setText("" + game.getScore().getPl1Score());
			p2Score.setText("" + game.getScore().getPl2Score());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}/*
    	try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(("lBat.obj")))) {
			game.setLBat( (Bat) ois.readObject());
			p1Slider.setValue(game.getLBat().getHeight());
			p1Slider.valueProperty().addListener( (obj, oldValue, newValue) -> game.setHeigtOfLBat(newValue.doubleValue()));
			p2Slider.valueProperty().addListener( (obj, oldValue, newValue) -> game.setHeigtOfRBat(newValue.doubleValue()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(("rBat.obj")))) {
			game.setRBat( (Bat) ois.readObject());
			p2Slider.setValue(game.getRBat().getHeight());
			p1Slider.valueProperty().addListener( (obj, oldValue, newValue) -> game.setHeigtOfLBat(newValue.doubleValue()));
			p2Slider.valueProperty().addListener( (obj, oldValue, newValue) -> game.setHeigtOfRBat(newValue.doubleValue()));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    	try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(("ball.obj")))) {
			game.setBall( (Ball) ois.readObject());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}*/
    }
    
	public List<Score> loadTournamentsXML() {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(ScoreArrayWrapper.class);
			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
			var c = (ScoreArrayWrapper)unmarshaller.unmarshal(new File("tournament.xml"));
			return (c.getScores());
		} catch (JAXBException e) {
			e.printStackTrace();
			return null;
		}
    }

    public void saveTournamentsXML() {
    	try {
    		var list = scoreDAO.load();
    		ScoreArrayWrapper sa = new ScoreArrayWrapper();
    		sa.setScores(list);
    		
    		JAXBContext jaxbContext = JAXBContext.newInstance(ScoreArrayWrapper.class);
	        Marshaller marshaller = jaxbContext.createMarshaller();
	        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);   
	        marshaller.marshal(sa, new File("tournament.xml")); // Appending to file	   		
    	} catch (JAXBException e) {
    		e.printStackTrace();
    	}
    }
      
	
}
