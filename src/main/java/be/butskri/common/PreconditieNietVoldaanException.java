package be.butskri.common;

public class PreconditieNietVoldaanException extends RuntimeException {

	public PreconditieNietVoldaanException(String messageKey, Object... params) {
		super(messageKey);
	}

}
