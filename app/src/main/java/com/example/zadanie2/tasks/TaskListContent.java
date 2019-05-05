package com.example.zadanie2.tasks;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class TaskListContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List <Task> ITEMS = new ArrayList <Task> ();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map <String, Task> ITEM_MAP = new HashMap <String, Task> ();

    private static final int COUNT = 5;

    static {
        addItem ( new Task ( String.valueOf ( 1 ), "Kuba", "Ktoś znany", "01.01.96" ) );
        addItem ( new Task ( String.valueOf ( 2 ), "Patryk", "Jakiś kolega", "17.05.97" ) );
        addItem ( new Task ( String.valueOf ( 3 ), "Kasia", "Jakaś koleżanka", "20.06.95" ) );
        addItem ( new Task ( String.valueOf ( 4 ), "Paulina", "Koleżanka jakiegoś kolegi", "31.12.96" ) );
    }

    public static void addItem(Task item) {
        ITEMS.add ( item );
        ITEM_MAP.put ( item.id, item );
    }
    /**
     * A dummy item representing a piece of content.
     */
    public static class Task implements Parcelable {
        public final String id;
        public final String title;
        public String picPath;
        public final String details;
        public final String data;

        public Task(String id, String title, String details, String data) {
            this.id = id;
            this.title=title;
            this.details=details;
            this.picPath="";
            this.data=data;
        }
        public Task(String id, String title, String details,String data, String picPath) {
            this.id = id;
            this.title=title;
            this.details=details;
            this.picPath=picPath;
            this.data=data;
        }

        protected Task(Parcel in) {
            id = in.readString ();
            title = in.readString ();
            picPath = in.readString ();
            details = in.readString ();
            data = in.readString ();
        }

        public static final Creator <Task> CREATOR = new Creator <Task> () {
            @Override
            public Task createFromParcel(Parcel in) {
                return new Task ( in );
            }

            @Override
            public Task[] newArray(int size) {
                return new Task[size];
            }
        };

        @Override
        public String toString() {
            return title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString ( id );
            dest.writeString ( title );
            dest.writeString ( picPath );
            dest.writeString ( details );
            dest.writeString ( data );
        }
        public void setPicPath(String path)
        {
            this.picPath = path;
        }
    }
    public static void removeItem(int position)
    {
        String itemId = ITEMS.get ( position ).id;

        ITEMS.remove ( position );

        ITEM_MAP.remove ( itemId );
    }
    public static void clearList()
    {
        ITEMS.clear ();
        ITEM_MAP.clear ();
    }
}
