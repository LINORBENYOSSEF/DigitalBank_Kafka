package app.bank.model;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public interface Field {

    void writeInto(DataOutput dataOutput) throws IOException;
    void readFrom(DataInput dataInput) throws IOException;
}
