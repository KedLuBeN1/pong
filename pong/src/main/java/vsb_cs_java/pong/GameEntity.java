package vsb_cs_java.pong;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;

public abstract class GameEntity implements DrawableSimulable, Serializable
{
	 private static final long serialVersionUID = 1L;
	 private final Game game;
	
	public GameEntity(Game game) 
	{
		this.game = game;
	}
	
	@Override
	public final void draw(GraphicsContext gc) 
	{
		gc.save();
		drawInternal(gc);
		gc.restore();
	}
	
	protected Game getGame() 
	{
		return game;
	}
	
	protected abstract void drawInternal(GraphicsContext gc);
	
}
