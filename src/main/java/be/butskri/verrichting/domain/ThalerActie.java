package be.butskri.verrichting.domain;

import be.butskri.common.PreconditieNietVoldaanException;
import be.butskri.thaler.IdempotentieId;

public abstract class ThalerActie implements Actie {

	private ThalerCall bevestigenCall;
	private ThalerCall annulerenCall;
	private ThalerCall ondertekenenCall;

	public ThalerActie() {
		bevestigenCall = createBevestigenCall();
		annulerenCall = createAnnulerenCall();
		ondertekenenCall = createOndertekenenCall();
	}

	public final void bevestig(Deelverrichting deelverrichting) {
		checkHeeftGeldigeStatusVoorBevestigen();
		bevestigenCall.voerUit();
	}

	public final void annuleer(Deelverrichting deelverrichting) {
		if (bevestigenCall.isNietZekerOfCallIsUitgevoerd()) {
			bevestigenCall.voerUit();
		}
		checkHeeftGeldigeStatusVoorAnnuleren();
		if (bevestigenCall.isSuccesvolUitgevoerd()) {
			annulerenCall.voerUit();
		}
	}

	public final void onderteken(Deelverrichting deelverrichting) {
		checkHeeftGeldigeStatusVoorOndertekenen();
		ondertekenenCall.voerUit();
	}

	protected abstract void doeBevestiging(IdempotentieId id);

	protected abstract void doeAnnulatie(IdempotentieId id);

	protected abstract void doeOndertekening(IdempotentieId id);

	protected ThalerCall createBevestigenCall() {
		return new ThalerCall() {

			@Override
			protected void doeVoerUit(IdempotentieId id) {
				doeBevestiging(id);
			}
		};
	}

	protected ThalerCall createAnnulerenCall() {
		return new ThalerCall() {

			@Override
			protected void doeVoerUit(IdempotentieId id) {
				doeAnnulatie(id);
			}
		};
	}

	private ThalerCall createOndertekenenCall() {
		return new ThalerCall() {

			@Override
			protected void doeVoerUit(IdempotentieId id) {
				doeOndertekening(id);
			}
		};
	}

	private void checkHeeftGeldigeStatusVoorBevestigen() {
		if (annulerenCall.isUitvoeringAangeroepen()) {
			throw new PreconditieNietVoldaanException("error.thaler.bevestigen.ongeldige.status");
		}
	}

	private void checkHeeftGeldigeStatusVoorAnnuleren() {
		if (bevestigenCall.isNietZekerOfCallIsUitgevoerd()) {
			throw new PreconditieNietVoldaanException("error.thaler.annuleren.ongeldige.status");
		}
	}

	private void checkHeeftGeldigeStatusVoorOndertekenen() {
		if (!bevestigenCall.isSuccesvolUitgevoerd()) {
			throw new PreconditieNietVoldaanException("error.thaler.ondertekenen.bevestigen.mislukt");
		}
		if (annulerenCall.isUitvoeringAangeroepen()) {
			throw new PreconditieNietVoldaanException("error.thaler.ondertekenen.annuleren.gestart");
		}
	}

}
