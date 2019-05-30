package edu.skku.everycalendar;

public class FriendsListItem {
    private String friend_name;
    private String friend_major;
    private int friend_tag;

    public FriendsListItem(String name, String major, int tag){
        this.friend_name = name;
        this.friend_major = major;
        this.friend_tag = tag;
    }

    public String getFriend_name(){return friend_name;}
    public String getFriend_major(){return friend_major;}
    public int getFriend_tag(){return friend_tag;}
}
