package vsb_cs_java.pong;


import java.io.Console;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Bat extends GameEntity implements Collisionable, Serializable
{
	private static final long serialVersionUID = 1L;
	transient private Point2D position;
	private int width = 10;
	private int height = 100;
	boolean batIn = false;
	
	public Bat(Game game ,Point2D posititon)
	{
		super(game);
		this.position = posititon;
	}
	
	@Override
	public void drawInternal(GraphicsContext gc) 
	{
		gc.setFill(Color.WHITE);
		gc.fillRect(position.getX(), position.getY(), width, height);
	}
	
	@Override
	public void simulate(double deltaT) 
	{
		/*if(position.getY() >= 0  && (position.getY() + height) <= getGame().getHeight()) 
		{
			batIn = true;
		}	
		
		if((position.getY() < 0  || (position.getY() + height) > getGame().getHeight()) && batIn) 
		{
			direction *= -1;
			batIn = false;
		}

		position = position.add(velocity.multiply(deltaT*direction));*/
	}
	
	public Rectangle2D getBoundingBox() 
	{
		return new Rectangle2D(position.getX(), position.getY(), width, height);
	}

	@Override
	public void hitBy(Collisionable collisionable) 
	{
		/*
		if (collisionable instanceof Ball) {
			System.out.println("Jsem palka a trefil me micek :(");
		}	*/
	}
	
	public void setHeight(double newValue) {
		position = new Point2D(position.getX(), newValue);
	}
	
	public double getHeight() {
		return position.getY();
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException 
	{
	        out.defaultWriteObject();
	        out.writeDouble(position.getX());
	        out.writeDouble(position.getY());
	}

    // Custom readObject method
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException 
    {
        in.defaultReadObject();
        double posX = in.readDouble();
        double posY = in.readDouble();
        position = new Point2D(posX, posY);
    }

}