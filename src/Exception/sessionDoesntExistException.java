package Exception;
/**
 * @author 
 *Cette  classe sert a gerer les differents constructeurs d'exceptions
 * en cas de session n'existant pas. 
 */
public class sessionDoesntExistException extends Exception {

    public sessionDoesntExistException() {
	super();
	// TODO Auto-generated constructor stub
    }

    public sessionDoesntExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
	// TODO Auto-generated constructor stub
    }

    public sessionDoesntExistException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    public sessionDoesntExistException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    public sessionDoesntExistException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

}
