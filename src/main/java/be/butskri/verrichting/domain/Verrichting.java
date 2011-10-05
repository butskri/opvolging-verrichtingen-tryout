package be.butskri.verrichting.domain;

import java.io.Serializable;

public interface Verrichting extends Serializable {

	VerrichtingId getVerrichtingId();

	VerrichtingStatus getStatus();

	void bevestig();

	void annuleer();

	void onderteken();

	void initieerVoorBevestiging();

	void rollbackInitierenBevestiging();

}
