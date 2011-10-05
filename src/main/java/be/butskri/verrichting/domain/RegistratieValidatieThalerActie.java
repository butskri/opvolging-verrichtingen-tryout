package be.butskri.verrichting.domain;

import be.butskri.common.Locator;
import be.butskri.logboek.LogboekService;
import be.butskri.thaler.IdempotentieId;
import be.butskri.thaler.ThalerReferentie;

public abstract class RegistratieValidatieThalerActie extends ThalerActie {

	private Deelverrichting deelverrichting;
	private ThalerReferentie thalerReferentie;

	protected RegistratieValidatieThalerActie(Deelverrichting deelverrichting) {
		this.deelverrichting = deelverrichting;
	}

	@Override
	protected final void doeAnnulatie(IdempotentieId id) {
		if (thalerReferentie != null) {
			annuleerRegistratie(id, thalerReferentie);
		}
	}

	@Override
	protected final void doeBevestiging(IdempotentieId id) {
		thalerReferentie = registreer(id);
		logboekService().update(deelverrichting.getLogboekId(), thalerReferentie.toString());
	}

	@Override
	protected final void doeOndertekening(IdempotentieId id) {
		valideer(id, thalerReferentie);
	}

	private LogboekService logboekService() {
		return Locator.locate(LogboekService.class);
	}

	protected abstract ThalerReferentie registreer(IdempotentieId id);

	protected abstract void annuleerRegistratie(IdempotentieId id, ThalerReferentie thalerReferentie);

	protected abstract void valideer(IdempotentieId id, ThalerReferentie thalerReferentie);

}
