package edu.skku.everycalendar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebasePost {
    public String id;
    public String name;
    public ArrayList<TimetableData> table;
    public FirebasePost(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }
    public FirebasePost(String id) {
        this.id = id;
        this.name = null;
        this.table = null;
    }
    public FirebasePost(String id, String name) {
        this.id = id;
        this.name = name;
        this.table = null;
    }
    public FirebasePost(String id, String name, ArrayList<TimetableData> table) {
        this.id = id;
        this.name = name;
        this.table = table;
    }
    public String getId(){
        return this.id;
    }
    public String getName(){
        return this.name;
    }
    public ArrayList<TimetableData> getTable(){
        return this.table;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("name", name);
        result.put("table",table);
        return result;
    }
}