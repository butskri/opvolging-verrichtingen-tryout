package be.butskri.verrichting.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class DeelVerrichtingImpl implements Deelverrichting {

	private VerrichtingStatus status;
	private Acties acties;
	private List<DeelVerrichtingListener> listeners = new ArrayList<DeelVerrichtingListener>();
	private Long logboekId;

	@Override
	public void initieerVoorBevestiging() {
		this.acties = creeerActies();
		setStatus(VerrichtingStatus.GEINITIEERD);
	}

	protected void addListener(DeelVerrichtingListener listener) {
		listeners.add(listener);
	}

	public void rollbackInitierenBevestiging() {
		annuleer(VerrichtingStatus.DRAFT);
		this.acties = null;
	}

	public void bevestig() {
		acties.bevestig();
		setStatus(VerrichtingStatus.GEREGISTREERD);
	}

	public void annuleer() {
		annuleer(VerrichtingStatus.GEANNULEERD);
	}

	public void onderteken() {
		acties.onderteken();
		setStatus(VerrichtingStatus.GETEKEND);
	}

	public Long getLogboekId() {
		return logboekId;
	}

	public void setLogboekId(Long logboekId) {
		this.logboekId = logboekId;
	}

	protected void setStatus(VerrichtingStatus newStatus) {
		if (status != newStatus) {
			VerrichtingStatus oldStatus = this.status;
			this.status = newStatus;
			notifyStatusChanged(oldStatus, newStatus);
		}
	}

	protected abstract Acties creeerActies();

	private void annuleer(VerrichtingStatus status) {
		if (acties != null) {
			acties.annuleer();
		}
		setStatus(status);
	}

	private void notifyStatusChanged(VerrichtingStatus oldStatus, VerrichtingStatus newStatus) {
		for (DeelVerrichtingListener listener : listeners) {
			listener.statusChanged(this, oldStatus, newStatus);
		}
	}
}
