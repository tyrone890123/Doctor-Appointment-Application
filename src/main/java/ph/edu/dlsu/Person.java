package ph.edu.dlsu;

import javafx.beans.property.SimpleStringProperty;

public class Person {

    private final SimpleStringProperty time;
    private final SimpleStringProperty name;
    private final SimpleStringProperty symptoms;

    public Person(String time, String name,String symptoms) {
        this.time = new SimpleStringProperty(time);
        this.name = new SimpleStringProperty(name);
        this.symptoms = new SimpleStringProperty(symptoms);
    }

    public String getTime() {
        return time.get();
    }
    public void setTime(String sched) {
        time.set(sched);
    }

    public String getName() {
        return name.get();
    }
    public void setName(String myname) {
        name.set(myname);
    }

    public String getSymptoms() {
        return symptoms.get();
    }
    public void setSymptoms(String symp) {
        symptoms.set(symp);
    }
}
