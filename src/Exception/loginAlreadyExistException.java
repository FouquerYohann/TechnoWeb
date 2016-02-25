package Exception;

/**
 * @author 
 *Cette  classe sert a gerer les differents constructeurs d'exceptions
 * en cas de login deja existant (à la creation d'un nouveau login)
 */
public class loginAlreadyExistException extends Exception {

	public loginAlreadyExistException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public loginAlreadyExistException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	public loginAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public loginAlreadyExistException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public loginAlreadyExistException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
