package be.butskri.verrichting.domain;

import static be.butskri.verrichting.domain.VerrichtingStatus.DRAFT;
import static be.butskri.verrichting.domain.VerrichtingStatus.GEANNULEERD;
import static be.butskri.verrichting.domain.VerrichtingStatus.GEINITIEERD;
import static be.butskri.verrichting.domain.VerrichtingStatus.GEREGISTREERD;
import static be.butskri.verrichting.domain.VerrichtingStatus.GETEKEND;
import static be.butskri.verrichting.domain.VerrichtingStatus.NIET_GETEKEND;
import static be.butskri.verrichting.domain.VerrichtingStatus.REGISTRATIE_MISLUKT;
import static be.butskri.verrichting.domain.VerrichtingStatus.VERWERKT;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

import org.junit.Test;

import be.butskri.common.PreconditieNietVoldaanException;

public class VerrichtingStatusTest {

	@Test
	public void draftEnAndereStatusGeeftDraft() {
		assertEquals(DRAFT, DRAFT.and(DRAFT));
		assertIncompatibleTypes(DRAFT, GEANNULEERD);
		assertIncompatibleTypes(DRAFT, GEINITIEERD);
		assertIncompatibleTypes(DRAFT, GEREGISTREERD);
		assertIncompatibleTypes(DRAFT, GETEKEND);
		assertIncompatibleTypes(DRAFT, NIET_GETEKEND);
		assertIncompatibleTypes(DRAFT, REGISTRATIE_MISLUKT);
		assertIncompatibleTypes(DRAFT, VERWERKT);
	}

	@Test
	public void geannuleerdEnAndereStatus() {
		assertIncompatibleTypes(GEANNULEERD, DRAFT);
		assertEquals(GEANNULEERD, GEANNULEERD.and(GEANNULEERD));
		assertEquals(GEINITIEERD, GEANNULEERD.and(GEINITIEERD));
		assertEquals(REGISTRATIE_MISLUKT, GEANNULEERD.and(GEREGISTREERD));
		assertIncompatibleTypes(GEANNULEERD, GETEKEND);
		assertEquals(NIET_GETEKEND, GEANNULEERD.and(NIET_GETEKEND));
		assertEquals(REGISTRATIE_MISLUKT, GEANNULEERD.and(REGISTRATIE_MISLUKT));
		assertIncompatibleTypes(GEANNULEERD, VERWERKT);
	}

	@Test
	public void geinitieerdEnAndereStatus() {
		assertIncompatibleTypes(GEINITIEERD, DRAFT);
		assertEquals(GEINITIEERD, GEINITIEERD.and(GEANNULEERD));
		assertEquals(GEINITIEERD, GEINITIEERD.and(GEINITIEERD));
		assertEquals(GEINITIEERD, GEINITIEERD.and(GEREGISTREERD));
		assertIncompatibleTypes(GEINITIEERD, GETEKEND);
		assertIncompatibleTypes(GEINITIEERD, NIET_GETEKEND);
		assertEquals(REGISTRATIE_MISLUKT, GEINITIEERD.and(REGISTRATIE_MISLUKT));
		assertIncompatibleTypes(GEINITIEERD, VERWERKT);
	}

	@Test
	public void geregistreerdEnAndereStatus() {
		assertIncompatibleTypes(GEREGISTREERD, DRAFT);
		assertEquals(REGISTRATIE_MISLUKT, GEREGISTREERD.and(GEANNULEERD));
		assertEquals(GEINITIEERD, GEREGISTREERD.and(GEINITIEERD));
		assertEquals(GEREGISTREERD, GEREGISTREERD.and(GEREGISTREERD));
		assertIncompatibleTypes(GEREGISTREERD, GETEKEND);
		assertIncompatibleTypes(GEREGISTREERD, NIET_GETEKEND);
		assertEquals(REGISTRATIE_MISLUKT, GEREGISTREERD.and(REGISTRATIE_MISLUKT));
		assertIncompatibleTypes(GEREGISTREERD, VERWERKT);
	}

	@Test
	public void getekendEnAndereStatus() {
		assertIncompatibleTypes(GETEKEND, DRAFT);
		assertIncompatibleTypes(GETEKEND, GEANNULEERD);
		assertIncompatibleTypes(GETEKEND, GEINITIEERD);
		assertIncompatibleTypes(GETEKEND, GEREGISTREERD);
		assertEquals(GETEKEND, GETEKEND.and(GETEKEND));
		assertIncompatibleTypes(GETEKEND, NIET_GETEKEND);
		assertIncompatibleTypes(GETEKEND, REGISTRATIE_MISLUKT);
		assertEquals(GETEKEND, GETEKEND.and(VERWERKT));
	}

	@Test
	public void nietGetekendEnAndereStatus() {
		assertIncompatibleTypes(NIET_GETEKEND, DRAFT);
		assertEquals(NIET_GETEKEND, NIET_GETEKEND.and(GEANNULEERD));
		assertIncompatibleTypes(NIET_GETEKEND, GEINITIEERD);
		assertIncompatibleTypes(NIET_GETEKEND, GEREGISTREERD);
		assertIncompatibleTypes(NIET_GETEKEND, GETEKEND);
		assertEquals(NIET_GETEKEND, NIET_GETEKEND.and(NIET_GETEKEND));
		assertIncompatibleTypes(NIET_GETEKEND, REGISTRATIE_MISLUKT);
		assertIncompatibleTypes(NIET_GETEKEND, VERWERKT);
	}

	@Test
	public void registratieMisluktEnAndereStatus() {
		assertIncompatibleTypes(REGISTRATIE_MISLUKT, DRAFT);
		assertEquals(REGISTRATIE_MISLUKT, REGISTRATIE_MISLUKT.and(GEANNULEERD));
		assertEquals(REGISTRATIE_MISLUKT, REGISTRATIE_MISLUKT.and(GEINITIEERD));
		assertEquals(REGISTRATIE_MISLUKT, REGISTRATIE_MISLUKT.and(GEREGISTREERD));
		assertIncompatibleTypes(REGISTRATIE_MISLUKT, GETEKEND);
		assertIncompatibleTypes(REGISTRATIE_MISLUKT, NIET_GETEKEND);
		assertEquals(REGISTRATIE_MISLUKT, REGISTRATIE_MISLUKT.and(REGISTRATIE_MISLUKT));
		assertIncompatibleTypes(REGISTRATIE_MISLUKT, VERWERKT);
	}

	@Test
	public void verwerktEnAndereStatus() {
		assertIncompatibleTypes(VERWERKT, DRAFT);
		assertIncompatibleTypes(VERWERKT, GEANNULEERD);
		assertIncompatibleTypes(VERWERKT, GEINITIEERD);
		assertIncompatibleTypes(VERWERKT, GEREGISTREERD);
		assertEquals(GETEKEND, VERWERKT.and(GETEKEND));
		assertIncompatibleTypes(VERWERKT, NIET_GETEKEND);
		assertIncompatibleTypes(VERWERKT, REGISTRATIE_MISLUKT);
		assertEquals(VERWERKT, VERWERKT.and(VERWERKT));
	}

	private void assertIncompatibleTypes(VerrichtingStatus status1, VerrichtingStatus status2) {
		try {
			status1.and(status2);
			fail();
		} catch (PreconditieNietVoldaanException expected) {

		}

	}
}
