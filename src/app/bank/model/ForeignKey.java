package app.bank.model;

import app.bank.storage.StorageException;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Optional;
import java.util.function.Function;

public class ForeignKey<T extends ModelWithId> implements Field {

    private T value;
    private String linkedId;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
        linkedId = value != null ? value.getId() : null;
    }

    @Override
    public void writeInto(DataOutput dataOutput) throws IOException {
        if (linkedId != null) {
            dataOutput.writeBoolean(true);
            dataOutput.writeUTF(linkedId);
        } else {
            dataOutput.writeBoolean(false);
        }
    }

    @Override
    public void readFrom(DataInput dataInput) throws IOException {
        if (dataInput.readBoolean()) {
            linkedId = dataInput.readUTF();
        } else {
            linkedId = null;
        }
    }

    public void postLoad(Function<String, Optional<? extends T>> valueFinder) throws StorageException {
        if (linkedId != null) {
            value = valueFinder.apply(linkedId)
                    .orElseThrow(()->new StorageException("Missing model expected: " + linkedId));
        }
    }
}
