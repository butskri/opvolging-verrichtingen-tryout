package be.butskri.verrichting.domain;

import java.util.UUID;

public class VerrichtingId {

	private UUID internalId;

	public VerrichtingId() {
		this.internalId = UUID.randomUUID();
	}

	public UUID getInternalId() {
		return internalId;
	}
}
