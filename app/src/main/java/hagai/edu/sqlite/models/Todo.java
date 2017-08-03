package hagai.edu.sqlite.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/*
 * A TODO MODEL CLASS
 */

public class Todo implements Parcelable, Comparable<Todo> {
    //Properties:
    private int id;
    private String mission;
    private String importance;

    //Constructor for INSERT:
    public Todo(String mission, String importance) {
        this.mission = mission;
        this.importance = importance;
    }

    //Constructor for the SELECT:
    public Todo(int id, String mission, String importance) {
        this.id = id;
        this.mission = mission;
        this.importance = importance;
    }

    //getters and setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
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

    @Override
    public String toString() {
        return "Todo{" +
                "id=" + id +
                ", mission='" + mission + '\'' +
                ", importance='" + importance + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.mission);
        dest.writeString(this.importance);
    }
    protected Todo(Parcel in) {
        this.id = in.readInt();
        this.mission = in.readString();
        this.importance = in.readString();
    }
    public static final Parcelable.Creator<Todo> CREATOR = new Parcelable.Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel source) {
            return new Todo(source);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    @Override
    public int compareTo(@NonNull Todo todo) {
        int idDiff = id - todo.id;
        if (idDiff != 0)
            return idDiff;

        int missionDiff = mission.compareTo(todo.mission);
        if (missionDiff!=0)
            return missionDiff;

        return importance.compareTo(todo.importance);
    }
}
