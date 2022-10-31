package app.bank.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class EnumField<T extends Enum<T>> implements Field {

    private final Class<T> enumType;
    private T value;

    public EnumField(Class<T> enumType) {
        this.enumType = enumType;
    }

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    @Override
    public void writeInto(DataOutput dataOutput) throws IOException {
        if (value != null) {
            dataOutput.writeInt(value.ordinal());
        } else {
            dataOutput.writeInt(-1);
        }
    }

    @Override
    public void readFrom(DataInput dataInput) throws IOException {
        int ordinal = dataInput.readInt();
        if (ordinal < 0) {
            value = null;
        } else {
            value = enumType.getEnumConstants()[ordinal];
        }
    }
}
