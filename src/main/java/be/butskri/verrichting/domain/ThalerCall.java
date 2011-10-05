package be.butskri.verrichting.domain;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import be.butskri.thaler.FunctioneleThalerException;
import be.butskri.thaler.IdempotentieId;
import be.butskri.thaler.ThalerBereiktTechnischeException;
import be.butskri.thaler.ThalerMisschienBereiktTechnischeException;
import be.butskri.thaler.ThalerNietBereiktTechnischeException;

public abstract class ThalerCall {

	private ThalerStatus thalerStatus = ThalerStatus.INIT;
	private IdempotentieId id = new IdempotentieId();
	private IdempotentieId vorigGebruikteId = null;
	private String laatsteStackTrace;

	public final void voerUit() {
		if (thalerStatus == ThalerStatus.UITGEVOERD) {
			return;
		}
		try {
			doeVoerUit(id);
			thalerStatus = ThalerStatus.UITGEVOERD;
		} catch (FunctioneleThalerException exception) {
			setExceptionOnExecute(exception, ThalerStatus.UITGEVOERD_MET_THALER_FOUT);
			resetIdUitvoering();
		} catch (ThalerBereiktTechnischeException exception) {
			setExceptionOnExecute(exception, ThalerStatus.UITGEVOERD_MET_THALER_FOUT);
			resetIdUitvoering();
		} catch (ThalerMisschienBereiktTechnischeException exception) {
			setExceptionOnExecute(exception, ThalerStatus.MISSCHIEN_UITGEVOERD_NIET_THALER_FOUT);
		} catch (ThalerNietBereiktTechnischeException exception) {
			setExceptionOnExecute(exception, ThalerStatus.NIET_UITGEVOERD_NIET_THALER_FOUT);
		}
	}

	public boolean isSuccesvolUitgevoerd() {
		return thalerStatus == ThalerStatus.UITGEVOERD;
	}

	public boolean isNietZekerOfCallIsUitgevoerd() {
		return thalerStatus == ThalerStatus.MISSCHIEN_UITGEVOERD_NIET_THALER_FOUT;
	}

	public boolean isUitvoeringAangeroepen() {
		return thalerStatus != ThalerStatus.INIT;
	}

	protected ThalerStatus getThalerStatus() {
		return thalerStatus;
	}

	protected IdempotentieId getId() {
		return id;
	}

	public String getLaatsteStackTrace() {
		return laatsteStackTrace;
	}

	public IdempotentieId getVorigGebruikteId() {
		return vorigGebruikteId;
	}

	protected abstract void doeVoerUit(IdempotentieId id);

	private void setExceptionOnExecute(RuntimeException exception, ThalerStatus thalerStatus) {
		laatsteStackTrace = getStackTrace(exception);
		this.thalerStatus = this.thalerStatus.and(thalerStatus);
	}

	private void resetIdUitvoering() {
		this.vorigGebruikteId = id;
		this.id = new IdempotentieId();
	}

	private static String getStackTrace(Throwable aThrowable) {
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		aThrowable.printStackTrace(printWriter);
		return result.toString();
	}
}
