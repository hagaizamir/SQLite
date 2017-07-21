package hagai.edu.sqlite.models;

/**
 * A TODO MODEL CLASS
 */

public class Todo {
    private  String mission;
    private  String importance;
    private  int id;

    //constactort for insert
    public Todo(int id, String mission, String importance) {
        this.mission = mission;
        this.importance = importance;
    }

//constarctort for select
    public Todo(String mission, String importance, int id) {
        this.id = id;
        this.mission = mission;
        this.importance = importance;
    }

    public Todo(int id) {
        this.id = id;
    }

    public String getMission() {
        return mission;
    }

    public void setMission(String mission) {
        this.mission = mission;
    }

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }
}
