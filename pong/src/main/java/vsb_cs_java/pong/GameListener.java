package vsb_cs_java.pong;

public interface GameListener
{
	void stateChanged(int p1Score, int p2Score);
	
	void gameOver();
}
