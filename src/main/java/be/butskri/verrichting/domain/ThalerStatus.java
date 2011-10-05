package be.butskri.verrichting.domain;

public enum ThalerStatus {
	INIT(false), //
	UITGEVOERD(false), //
	UITGEVOERD_MET_THALER_FOUT(true), //
	NIET_UITGEVOERD_NIET_THALER_FOUT(false), //
	MISSCHIEN_UITGEVOERD_NIET_THALER_FOUT(false);

	private boolean nieuwIdNodigVoorVolgendeCall;

	private ThalerStatus(boolean nieuwIdNodigVoorVolgendeCall) {
		this.nieuwIdNodigVoorVolgendeCall = nieuwIdNodigVoorVolgendeCall;
	}

	public ThalerStatus and(ThalerStatus laatsteStatus) {
		if (this == MISSCHIEN_UITGEVOERD_NIET_THALER_FOUT) {
			if (laatsteStatus == NIET_UITGEVOERD_NIET_THALER_FOUT) {
				return MISSCHIEN_UITGEVOERD_NIET_THALER_FOUT;
			}
		}
		return laatsteStatus;
	}

	public boolean isNieuwIdNodigVoorVolgendeCall() {
		return nieuwIdNodigVoorVolgendeCall;
	}

}
