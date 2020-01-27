package org.jbeebr.test.domain;

public class DatabaseColumn {

    private String name;
    private int dataType;

    public DatabaseColumn(String name, int dataType) {
        this.name = name;
        this.dataType = dataType;
    }

    public String getName() {
        return this.name;
    }

    public int getDataType() {
        return this.dataType;
    }

}
