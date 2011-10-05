package be.butskri.thaler;

import java.util.UUID;

public class IdempotentieId {

	private String id;

	public IdempotentieId() {
		this.id = UUID.randomUUID().toString();
	}
}
