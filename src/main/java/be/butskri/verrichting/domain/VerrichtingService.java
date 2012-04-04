package be.butskri.verrichting.domain;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import be.butskri.common.PreconditieNietVoldaanException;

public class VerrichtingService {

	private TransactionTemplate transactionTemplate;
	private VerrichtingRepository verrichtingRepository;

	public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
		this.transactionTemplate = transactionTemplate;
	}

	public void setVerrichtingRepository(VerrichtingRepository verrichtingRepository) {
		this.verrichtingRepository = verrichtingRepository;
	}

	public void bevestig(Verrichting verrichting) {
		try {
			doInTransaction(initieerVerrichting(verrichting));
		} catch (RuntimeException e) {
			doInTransactionCatchingAllExceptions(rollbackInitierenBevestiging(verrichting.getVerrichtingId()));
			throw new PreconditieNietVoldaanException("error.verrichting.bevestiging.intentie.failed");
		}
		VerrichtingStatus status = doInTransaction(doeRegistratie(verrichting.getVerrichtingId()));
		if (status != VerrichtingStatus.GEREGISTREERD) {
			doInTransaction(annuleerRegistratieIndienNodig(verrichting.getVerrichtingId()));
		}
	}

	private TransactionCallback rollbackInitierenBevestiging(final VerrichtingId verrichtingId) {
		return new TransactionCallback() {

			@Override
			public Object doInTransaction(TransactionStatus status) {
				Verrichting verrichting = verrichtingRepository.getVerrichting(verrichtingId);
				verrichting.rollbackInitierenBevestiging();
				verrichtingRepository.bewaar(verrichting);
				return null;
			}
		};
	}

	public void teken(VerrichtingId verrichtingId) {

	}

	private TransactionCallback annuleerRegistratieIndienNodig(VerrichtingId verrichtingId) {
		// TODO Auto-generated method stub
		return null;
	}

	private TransactionCallback doeRegistratie(VerrichtingId verrichtingId) {
		return new VerrichtingTransactionCallback<Object>(verrichtingId) {
			@Override
			protected Object voerUit(Verrichting verrichting) {
				verrichting.rollbackInitierenBevestiging();
				return null;
			}
		};
	}

	private TransactionCallback initieerVerrichting(final Verrichting verrichting) {
		return new TransactionCallback() {

			@Override
			public Object doInTransaction(TransactionStatus status) {
				verrichting.initieerVoorBevestiging();
				verrichtingRepository.bewaar(verrichting);
				return verrichting.getVerrichtingId();
			}
		};
	}

	private <T> T doInTransactionCatchingAllExceptions(final TransactionCallback transactionCallback) {
		try {
			return doInTransactionCatchingAllExceptions(transactionCallback);
		} catch (RuntimeException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private <T> T doInTransaction(final TransactionCallback transactionCallback) {
		ExceptionCatchingTransactionCallback exceptionCatchingTransactionCallback = new ExceptionCatchingTransactionCallback(transactionCallback);
		T result = (T) transactionTemplate.execute(exceptionCatchingTransactionCallback);
		if (exceptionCatchingTransactionCallback.getCaughtException() != null) {
			throw exceptionCatchingTransactionCallback.getCaughtException();
		}
		return result;
	}

	private static class ExceptionCatchingTransactionCallback implements TransactionCallback {

		private TransactionCallback transactionCallback;
		private PreconditieNietVoldaanException caughtException;

		public ExceptionCatchingTransactionCallback(TransactionCallback transactionCallback) {
			this.transactionCallback = transactionCallback;
		}

		public PreconditieNietVoldaanException getCaughtException() {
			return caughtException;
		}

		@Override
		public Object doInTransaction(TransactionStatus status) {
			try {
				return transactionCallback.doInTransaction(status);
			} catch (PreconditieNietVoldaanException e) {
				this.caughtException = e;
			}
			return null;
		}
	}

	private abstract class VerrichtingTransactionCallback<T> implements TransactionCallback {

		private VerrichtingId verrichtingId;

		public VerrichtingTransactionCallback(VerrichtingId verrichtingId) {
			this.verrichtingId = verrichtingId;
		}

		@Override
		public T doInTransaction(TransactionStatus status) {
			Verrichting verrichting = verrichtingRepository.getVerrichting(verrichtingId);
			T result = voerUit(verrichting);
			verrichtingRepository.bewaar(verrichting);
			return result;
		}

		protected abstract T voerUit(Verrichting verrichting);
	}

}
