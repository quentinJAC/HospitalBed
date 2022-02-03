package fr.uha.jacquey.hospitalbed.management.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "services")
public class Service {

    @PrimaryKey (autoGenerate = true)
    private long sId;
    private String name;

    public Service() {this.sId=0;}

    @Ignore
    public Service(String name)
    {
        this();

        this.name=name;
    }

    public long getsId(){return sId;}
    public void setsId(long id){sId=id;}

    public String getName(){return name;}
    public void setName(String newName){this.name=newName;}

    public String toString() {
        return "\nService " + this.name  ;
    }

    public static boolean compare(Service newService, Service oldService) {
        if (! Service.compare (newService, oldService)) return false;
        return true;
    }
}
