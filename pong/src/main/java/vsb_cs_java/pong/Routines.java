package vsb_cs_java.pong;

import java.io.Serializable;

public final class Routines implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Routines() {	}
	
	public static void sleep(int timeInMilisenonds) {
		try {
			Thread.sleep(timeInMilisenonds);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
	
	public static boolean isEndOfThreadRequestedByJavaVM() {
		return Thread.interrupted();
	}
}
