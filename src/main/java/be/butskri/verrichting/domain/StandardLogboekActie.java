package be.butskri.verrichting.domain;

import be.butskri.common.Locator;
import be.butskri.logboek.Logboek;
import be.butskri.logboek.LogboekBuilder;
import be.butskri.logboek.LogboekFactory;
import be.butskri.logboek.LogboekService;
import be.butskri.logboek.LogboekStatus;

public abstract class StandardLogboekActie extends DbActie {

	private Long logboekId;
	private Deelverrichting deelverrichting;

	public StandardLogboekActie(Deelverrichting deelverrichting) {
		this.deelverrichting = deelverrichting;
	}

	@Override
	protected void doeAnnulatie() {
		updateStatus(LogboekStatus.GEANNULEERD);
	}

	@Override
	protected void doeBevestiging() {
		LogboekBuilder logboekBuilder = logboekBuilder();
		addSpecifiekeData(logboekBuilder, deelverrichting);
		Logboek logboek = logboekBuilder.build();
		logboekService().save(logboek);
		deelverrichting.setLogboekId(logboek.getId());
	}

	@Override
	protected void doeOndertekening() {
		updateStatus(LogboekStatus.VERWERKT);
	}

	private void updateStatus(LogboekStatus status) {
		logboekService().updateStatus(logboekId, status);
	}

	protected abstract void addSpecifiekeData(LogboekBuilder logboekBuilder, Deelverrichting deelVerrichting);

	private LogboekBuilder logboekBuilder() {
		return Locator.locate(LogboekFactory.class).createBuilder().withStatus(LogboekStatus.GEREGISTREERD);
	}

	private LogboekService logboekService() {
		return Locator.locate(LogboekService.class);
	}

}
