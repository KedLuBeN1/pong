package vsb_cs_java.pong;

import java.io.Serializable;

public class EmptyGameListener implements GameListener, Serializable
{
	private static final long serialVersionUID = 1L;

	@Override
	public void stateChanged(int shoots, int hits) {
	}

	@Override
	public void gameOver() {
	}
}
