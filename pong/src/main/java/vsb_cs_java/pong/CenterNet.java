package vsb_cs_java.pong;

import java.io.Serializable;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

public class CenterNet extends GameEntity implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CenterNet(Game game) {
		super(game);
	}

	@Override
	public void simulate(double deltaT) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void drawInternal(GraphicsContext gc)
	{
		gc.setStroke(Color.WHITE);
        gc.setLineWidth(5);
        gc.setLineCap(StrokeLineCap.BUTT); 
        gc.setLineJoin(StrokeLineJoin.ROUND);
        gc.setLineDashes(5, 10); 
        gc.strokeLine(400, 0, 400, 600);
	}

}
