package be.butskri.logboek;

import java.util.Date;

public class LogboekBuilder {

	public Logboek build() {
		Logboek result = new Logboek();
		return result;
	}

	public LogboekBuilder withStatus(LogboekStatus status) {
		return this;
	}

	public LogboekBuilder withExterneReferentie(String externeReferentie) {
		return this;
	}

	public LogboekBuilder withDatumRegistratie(Date datumRegistratie) {
		return this;
	}
}
