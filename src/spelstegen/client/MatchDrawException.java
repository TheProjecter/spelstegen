package spelstegen.client;

public class MatchDrawException extends Exception {

	private static final long serialVersionUID = 1L;

	public MatchDrawException() {}

	public MatchDrawException(String message) {
		super(message);
	}

	public MatchDrawException(Throwable cause) {
		super(cause);
	}

	public MatchDrawException(String message, Throwable cause) {
		super(message, cause);
	}

}
