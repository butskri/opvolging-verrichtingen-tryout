package be.butskri.verrichting.domain;

import java.util.List;

public abstract class VerrichtingImpl implements Verrichting {

	private VerrichtingId verrichtingId = new VerrichtingId();
	private VerrichtingStatus status = VerrichtingStatus.DRAFT;
	private List<Deelverrichting> deelverrichtingen;

	@Override
	public VerrichtingId getVerrichtingId() {
		return verrichtingId;
	}

	@Override
	public VerrichtingStatus getStatus() {
		return status;
	}

	@Override
	public void initieerVoorBevestiging() {
		deelverrichtingen = creeerDeelverrichtingen();
		for (Deelverrichting deelverrichting : deelverrichtingen) {
			deelverrichting.initieerVoorBevestiging();
		}
		setStatus(VerrichtingStatus.GEINITIEERD);
	}

	@Override
	public void rollbackInitierenBevestiging() {
		if (deelverrichtingen != null) {
			for (Deelverrichting deelverrichting : deelverrichtingen) {
				deelverrichting.initieerVoorBevestiging();
			}
		}
	}

	@Override
	public void bevestig() {
		for (Deelverrichting deelverrichting : deelverrichtingen) {
			deelverrichting.bevestig();
		}
		setStatus(VerrichtingStatus.GEREGISTREERD);
	}

	@Override
	public void annuleer() {
		if (deelverrichtingen != null) {
			for (Deelverrichting deelverrichting : deelverrichtingen) {
				deelverrichting.annuleer();
			}
		}
		setStatus(VerrichtingStatus.GEANNULEERD);
	}

	@Override
	public void onderteken() {
		for (Deelverrichting deelverrichting : deelverrichtingen) {
			deelverrichting.onderteken();
		}
		setStatus(VerrichtingStatus.VERWERKT);
	}

	protected abstract List<Deelverrichting> creeerDeelverrichtingen();

	private void setStatus(VerrichtingStatus status) {
		this.status = status;
	}
}
