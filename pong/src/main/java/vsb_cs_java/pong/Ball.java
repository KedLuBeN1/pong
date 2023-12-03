package vsb_cs_java.pong;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


public class Ball extends GameEntity implements Collisionable, Serializable
{

	private static final long serialVersionUID = 1L;
	transient public  Point2D position;
	transient private  Point2D velocity;
	public int size = 20;
	public int xDirection = 1;
	public int yDirection = 1;
	private boolean ballIn = false;
    
	public Ball(Game game, Point2D position, Point2D velocity)
	{
		super(game);
		this.position = position;
		this.velocity = velocity;
    }
	
	@Override
	public void drawInternal(GraphicsContext gc) 
	{
		gc.setFill(Color.WHITE);
		gc.fillOval(position.getX(), position.getY(), size, size);
	}
	
	@Override
	public void simulate(double deltaT)
	{					
		position = position.add(velocity.getX() * deltaT * xDirection, velocity.getY() * deltaT * yDirection);
		
		Random rand = new Random();
		
		if(position.getY() >= 0  && (position.getY() + size) <= getGame().getHeight() && position.getX() >= 0  && (position.getX() + size) <= getGame().getWidth()) 
		{
			ballIn = true;
		}	
		
		if((position.getY() < 0  || (position.getY() + size) > getGame().getHeight()) && ballIn) 
		{
			yDirection *= -1;
			if(rand.nextBoolean()) 
			{
				xDirection *= -1;		
			}			
			ballIn = false;
		}
		
		if((position.getX() < 0  || (position.getX() + size) > getGame().getWidth()) && ballIn) 
		{
			xDirection *= -1;
			if(rand.nextBoolean()) 
			{
				yDirection *= -1;		
			}	
			ballIn = false;
			if(position.getX() < 0 ) {
				getGame().scored(0, 1);
			}
			else {
				getGame().scored(1, 0);
			}
			
			position = new Point2D(400, 300);
		}
					
	}
	
	protected Point2D getPosition() 
	{
		return position;
	}
	
	public Rectangle2D getBoundingBox() 
	{
		return new Rectangle2D(position.getX(), position.getY(), size, size);
	}
	
	@Override
	public void hitBy(Collisionable collisionable) {
		if (collisionable instanceof Bat) {
			xDirection *= -1;
			yDirection *= -1;	
		}
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException 
	{
        out.defaultWriteObject();
        out.writeDouble(position.getX());
        out.writeDouble(position.getY());
        out.writeDouble(velocity.getX());
        out.writeDouble(velocity.getY());
	}

    // Custom readObject method
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException 
    {
        in.defaultReadObject();
        double posX = in.readDouble();
        double posY = in.readDouble();
        double velX = in.readDouble();
        double velY = in.readDouble();
        position = new Point2D(posX, posY);
        velocity = new Point2D(velX, velY);
        System.out.println(position);
    }
}
