package vsb_cs_java.pong;

import java.io.Serializable;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;


public class DrawingThread extends AnimationTimer implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	transient private final GraphicsContext gc;
	private final Game game;
	transient private final Canvas canvas;
	private static final int FPS = 100;
	
	private long lasttime = -1;
	
	public DrawingThread(Canvas canvas, Game game) 
	{
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		this.game = game;
	}


	@Override
	public void handle(long now) 
	{
		double deltaT = (now - lasttime) / 1e9;
		if (deltaT >= 1./FPS) {
			gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
			game.draw(gc);
			if (lasttime > 0) {
				//time are in nanoseconds and method simulate expects seconds
				
				game.simulate(deltaT);
			}
			lasttime = now;
		}
	}
}
