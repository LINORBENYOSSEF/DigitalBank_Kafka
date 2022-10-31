package app.bank.transaction.remote;

import app.bank.model.BankInfo;
import app.bank.transaction.TransactionDetails;
import p2p.banking.BankingInterchange;
import p2p.banking.BankingListener;

public class TransactionSession implements AutoCloseable {

    private static final boolean DEBUG = true;

	private BankingInterchange<TransactionDetails> interchange;

	public TransactionSession(BankInfo bankInfo, BankingListener<TransactionDetails> listener, String... config) {
		if (!DEBUG) {
            this.interchange = new BankingInterchange<>(bankInfo.getBankId(), config,
                    listener, new TransactionDetails());
        }
	}
	
	public void executeTransaction(TransactionDetails transactionDetails) {
		if (!DEBUG) {
            this.interchange.sendBankingTransaction(transactionDetails);
        } else {
		    System.out.printf("send transfer: %s\n", transactionDetails);
        }
	}

	@Override
	public void close() throws Exception {
		if (!DEBUG) {
            this.interchange.close();
        }
	}
}
