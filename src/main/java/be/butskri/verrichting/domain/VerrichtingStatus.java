package be.butskri.verrichting.domain;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;

import be.butskri.common.PreconditieNietVoldaanException;

public enum VerrichtingStatus {

	DRAFT, //
	GEINITIEERD, //
	GEREGISTREERD {
		@Override
		public VerrichtingStatus and(VerrichtingStatus other) {
			if (other == GEANNULEERD) {
				return REGISTRATIE_MISLUKT;
			}
			return super.and(other);
		}
	},
	REGISTRATIE_MISLUKT, //
	GETEKEND, //
	VERWERKT, //
	NIET_GETEKEND, //
	GEANNULEERD {
		@Override
		public VerrichtingStatus and(VerrichtingStatus other) {
			if (other == GEREGISTREERD) {
				return REGISTRATIE_MISLUKT;
			}
			return super.and(other);
		}
	};

	private static final List<VerrichtingStatus> DRAFT_GROUP = Arrays.asList(DRAFT);
	private static final List<VerrichtingStatus> REGISTRATIE_GROUP = Arrays.asList(REGISTRATIE_MISLUKT, GEINITIEERD, GEANNULEERD, GEREGISTREERD);
	private static final List<VerrichtingStatus> ANNULATIE_GROUP = Arrays.asList(NIET_GETEKEND, GEANNULEERD);
	private static final List<VerrichtingStatus> VERWERKING_GROUP = Arrays.asList(GETEKEND, VERWERKT);
	@SuppressWarnings("unchecked")
	private static final List<List<VerrichtingStatus>> ALL_GROUPS = Arrays.<List<VerrichtingStatus>>asList(DRAFT_GROUP, REGISTRATIE_GROUP, ANNULATIE_GROUP, VERWERKING_GROUP);

	public VerrichtingStatus and(VerrichtingStatus other) {
		List<VerrichtingStatus> group = findGroup(this, other);
		if (group == null) {
			throw new PreconditieNietVoldaanException("");
		}
		if (group.indexOf(this) <= group.indexOf(other)) {
			return this;
		}
		return other;
	}

	private List<VerrichtingStatus> findGroup(VerrichtingStatus oneStatus, VerrichtingStatus otherStatus) {
		for (List<VerrichtingStatus> group : ALL_GROUPS) {
			if (group.contains(oneStatus) && group.contains(otherStatus)) {
				return group;
			}
		}
		return null;
	}

	public boolean isEindStatus() {
		return isOneOf(VERWERKT, GEANNULEERD);
	}

	private boolean isOneOf(VerrichtingStatus... statussen) {
		return ArrayUtils.contains(statussen, this);
	}

}
