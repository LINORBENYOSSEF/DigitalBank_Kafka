package app.bank.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ListField<T extends Field> implements Field {

    private final List<T> list = new ArrayList<>();
    private final Supplier<T> elementConstructor;

    public ListField(Supplier<T> elementConstructor) {
        this.elementConstructor = elementConstructor;
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public void writeInto(DataOutput dataOutput) throws IOException {
        dataOutput.writeInt(list.size());
        for (T t : list) {
            t.writeInto(dataOutput);
        }
    }

    @Override
    public void readFrom(DataInput dataInput) throws IOException {
        int size = dataInput.readInt();
        list.clear();

        for (int i = 0; i < size; i++) {
            T t = elementConstructor.get();
            t.readFrom(dataInput);
            list.add(t);
        }
    }
}
