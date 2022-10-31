package app.bank.transaction;

import app.bank.model.TransactionRecord;
import app.bank.transaction.remote.RemoteAccount;

import java.util.Date;

public class TransactionDetails {

    private RemoteAccount source;
    private RemoteAccount destination;
    private String comment;
    private float amount;
    private Date timestamp;

    public TransactionDetails() {
    }

    public TransactionDetails(RemoteAccount source, RemoteAccount destination, String comment, float amount) {
        super();
        this.source = source;
        this.destination = destination;
        this.comment = comment;
        this.amount = amount;
    }

    public TransactionDetails(TransactionRecord transactionRecord) {
        this(new RemoteAccount(transactionRecord.getSource()),
                new RemoteAccount(transactionRecord.getDestination()),
                transactionRecord.getComment(),
                (float) transactionRecord.getAmount());
        this.timestamp = transactionRecord.getTimestamp();
    }

    public RemoteAccount getSource() {
        return source;
    }

    public void setSource(RemoteAccount source) {
        this.source = source;
    }

    public RemoteAccount getDestination() {
        return destination;
    }

    public void setDestination(RemoteAccount destination) {
        this.destination = destination;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
