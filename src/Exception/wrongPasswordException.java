package Exception;

/**
 * @author 
 *Cette  classe sert a gerer les differents constructeurs d'exceptions
 * en cas de mot de passe errone.
 */
public class wrongPasswordException extends Exception {

	public wrongPasswordException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public wrongPasswordException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public wrongPasswordException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public wrongPasswordException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public wrongPasswordException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
