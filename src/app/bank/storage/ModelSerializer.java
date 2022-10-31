package app.bank.storage;

import app.bank.model.Model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

public class ModelSerializer {

    public <T extends Model> void writeList(DataOutput dataOutput, List<T> list) throws IOException {
        dataOutput.writeInt(list.size());
        for (T model : list) {
            model.writeInto(dataOutput);
        }
    }

    public <T extends Model> void readList(DataInput dataInput, List<T> list, Supplier<T> modelFactory) throws IOException {
        list.clear();

        int size = dataInput.readInt();
        for (int i = 0; i < size; i++) {
            T model = modelFactory.get();
            model.readFrom(dataInput);
            list.add(model);
        }
    }
}
