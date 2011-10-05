package be.butskri.verrichting.domain;

import java.util.Arrays;
import java.util.List;

public class Acties {

	private List<Actie> acties;

	public Acties(Actie... acties) {
		this.acties = Arrays.asList(acties);
	}

	public void bevestig() {
		for (Actie actie : acties) {
			actie.bevestig();
		}
	}

	public void annuleer() {
		for (int i = acties.size() - 1; i >= 0; i--) {
			acties.get(i).annuleer();
		}
	}

	public void onderteken() {
		for (Actie actie : acties) {
			actie.onderteken();
		}
	}
}
