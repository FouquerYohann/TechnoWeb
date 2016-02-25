package Exception;
/**
 * @author 
 *Cette  classe sert a gerer les differents constructeurs d'exceptions
 * en cas ou l'utilisateur demander n'existe pas.
 */
public class userDoesntExistException extends Exception {

	public userDoesntExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public userDoesntExistException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public userDoesntExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public userDoesntExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public userDoesntExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
