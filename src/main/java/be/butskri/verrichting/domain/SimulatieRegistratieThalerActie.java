package be.butskri.verrichting.domain;

import be.butskri.thaler.IdempotentieId;

public abstract class SimulatieRegistratieThalerActie extends ThalerActie {

	@Override
	protected final void doeAnnulatie(IdempotentieId id) {
	}

}
