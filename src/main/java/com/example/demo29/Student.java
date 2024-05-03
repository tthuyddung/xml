package com.example.demo29;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Student {
    private final StringProperty studID;
    private final StringProperty name;
    private final StringProperty address;

    public Student(String studID, String name, String address) {
        this.studID = new SimpleStringProperty(studID);
        this.name = new SimpleStringProperty(name);
        this.address = new SimpleStringProperty(address);
    }

    public String getStudID() {
        return studID.get();
    }

    public StringProperty studIDProperty() {
        return studID;
    }

    public void setStudID(String studID) {
        this.studID.set(studID);
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }
}