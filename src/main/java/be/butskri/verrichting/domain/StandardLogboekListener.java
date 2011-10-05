package be.butskri.verrichting.domain;

import be.butskri.common.Locator;
import be.butskri.logboek.Logboek;
import be.butskri.logboek.LogboekBuilder;
import be.butskri.logboek.LogboekFactory;
import be.butskri.logboek.LogboekService;
import be.butskri.logboek.LogboekStatus;

public abstract class StandardLogboekListener implements DeelVerrichtingListener {

	private Long logboekId;

	@Override
	public void statusChanged(Deelverrichting deelVerrichting, VerrichtingStatus oldStatus, VerrichtingStatus newStatus) {
		if (newStatus == VerrichtingStatus.GEREGISTREERD) {
			createLogboekEntry(deelVerrichting);
		}
		if (newStatus == VerrichtingStatus.GEANNULEERD) {
			updateStatus(LogboekStatus.GEANNULEERD);
		}
		if (newStatus == VerrichtingStatus.GETEKEND) {
			updateStatus(LogboekStatus.VERWERKT);
		}
	}

	private void updateStatus(LogboekStatus status) {
		logboekService().updateStatus(logboekId, status);
	}

	private void createLogboekEntry(Deelverrichting deelVerrichting) {
		LogboekBuilder logboekBuilder = logboekBuilder();
		addSpecifiekeData(logboekBuilder, deelVerrichting);
		Logboek logboek = logboekBuilder.build();
		logboekService().save(logboek);
		this.logboekId = logboek.getId();
	}

	protected abstract void addSpecifiekeData(LogboekBuilder logboekBuilder, Deelverrichting deelVerrichting);

	private LogboekBuilder logboekBuilder() {
		return Locator.locate(LogboekFactory.class).createBuilder().withStatus(LogboekStatus.GEREGISTREERD);
	}

	private LogboekService logboekService() {
		return Locator.locate(LogboekService.class);
	}

}
