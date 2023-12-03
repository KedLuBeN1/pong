package vsb_cs_java.pong;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Game implements Serializable
{
	private static final long serialVersionUID = 1L;
	private final double width;
	private final double height;
	private Score score;
	private Bat lBat;
	private Bat rBat;
	private Ball ball;
	private final String p1Name;
	private final String p2Name;
	boolean ballIn = false;
	private GameListener gameListener = new EmptyGameListener();
	private DrawableSimulable[] entities;
	
	public Game(double width, double height, String p1Name, String p2Name) 
	{
		this.width = width;
		this.height = height;
		this.p1Name = p1Name;
		this.p2Name = p2Name;

		entities = new DrawableSimulable[]{
			this.ball = new Ball(this, new Point2D(400, 300), new Point2D(200, 200)),
			this.lBat = new Bat(this, new Point2D(50, 100)),
			this.rBat = new Bat(this, new Point2D(740, 400)),
			new CenterNet(this),
		};
		
		this.score = new Score(p1Name, p2Name);
	}
	
	public void draw(GraphicsContext gc) 
	{
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, width, height);

		for (DrawableSimulable entity: entities) {
			entity.draw(gc);
		}
	}
	
	public void simulate(double deltaT) 
	{
		for (DrawableSimulable entity: entities) {
			entity.simulate(deltaT);
			if (entity instanceof Collisionable ca) {
				for (DrawableSimulable that: entities) {
					if (entity != that) {
						ca.checkCollision(that);
					}
				}
			}
		}
	}
	
	public void scored(int pl, int p2) 
	{
		score.setScore(pl, p2);
		gameListener.stateChanged(score.getPl1Score(), score.getPl2Score());
	}	
	
	public void setGameListener(GameListener gameListener) {
		this.gameListener = gameListener;
	}
	
	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
	
	
	public Score getScore() {
		return score;
	}
	
	public void setScore(Score score) {
		this.score = score;
	}
	
	public Bat getLBat() {
		return lBat;
	}
	
	public void setLBat(Bat bat) {
		this.lBat = bat;
	}
	
	public Bat getRBat() {
		return rBat;
	}
	
	public void setRBat(Bat bat) {
		this.rBat = bat;
	}
	
	public Ball getBall() {
		return ball;
	}
	
	public void setBall(Ball ball) {
		this.ball = ball;
	}

	public void setHeigtOfLBat(double newValue) {
		lBat.setHeight(newValue);
	}

	public void setHeigtOfRBat(double newValue) {
		rBat.setHeight(newValue);
	}


}