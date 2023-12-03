package vsb_cs_java.pong;

import javafx.geometry.Rectangle2D;

public interface Collisionable {
	Rectangle2D getBoundingBox(); 
	
	void hitBy(Collisionable collisionable);

	default void checkCollision(Object that) {
		if (that instanceof Collisionable ca2 
				&& this.getBoundingBox().intersects(ca2.getBoundingBox())) {
				ca2.hitBy(this);
		}
	}
	
	
}
