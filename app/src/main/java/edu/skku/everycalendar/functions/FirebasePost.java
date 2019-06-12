package edu.skku.everycalendar.functions;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.skku.everycalendar.dataType.TimetableData;

public class FirebasePost {
    public String id;
    public String name;
    public ArrayList<TimetableData> table;

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

    public void postDB() {
        Map<String, Object> out = new HashMap<>();
        for(TimetableData td : table){
            Map<String, String> val = new HashMap<>();
            Map<String, Object> pack = new HashMap<>();
            val.put("name", name);
            val.put("id", id);
            out.put("/ClassData/" + td.getIdNum() + "/" + id + "/", val);
        }
        FirebaseDatabase.getInstance().getReference().updateChildren(out);

    }

}