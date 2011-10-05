package be.butskri.verrichting.domain;

public abstract class DbActie implements Actie {

	private boolean annulatieUitgevoerd;
	private boolean bevestigingUitgevoerd;
	private boolean ondertekenenUitgevoerd;

	@Override
	public final void annuleer() {
		if (!annulatieUitgevoerd) {
			doeAnnulatie();
			annulatieUitgevoerd = true;
		}
	}

	@Override
	public final void bevestig() {
		if (!bevestigingUitgevoerd) {
			doeBevestiging();
			bevestigingUitgevoerd = true;
		}
	}

	@Override
	public final void onderteken() {
		if (!ondertekenenUitgevoerd) {
			doeOndertekening();
			ondertekenenUitgevoerd = true;
		}
	}

	protected void doeAnnulatie() {
	}

	protected void doeBevestiging() {
	}

	protected abstract void doeOndertekening();

}
