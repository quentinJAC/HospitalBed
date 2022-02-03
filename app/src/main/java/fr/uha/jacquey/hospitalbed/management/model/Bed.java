package fr.uha.jacquey.hospitalbed.management.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import fr.uha.jacquey.hospitalbed.helper.CompareUtil;

@Entity(tableName = "beds")
public class Bed {

    @PrimaryKey(autoGenerate = true)
    private long bId;
    private long numero;

    private Patient patient;

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }


    public Bed() {this.bId=0;}

    @Ignore
    public Bed(long numero, Patient patient)
    {
        this();
        this.numero=numero;
        this.patient = patient;
    }

    public long getBedid(){return bId;}
    public void setBedid(long id){bId=id;}

    public long getNumero(){return this.numero;}
    public void setNumero(long numero){this.numero=numero;}

    public String toString() {
        return "Lit n°" + this.numero  ;
    }

    public static boolean compare(Bed newBed, Bed oldBed) {
        if (newBed.getBedid() != oldBed.getBedid()) return false;
        if (! CompareUtil.compare(newBed.getNumero(), oldBed.getNumero())) return false;
        return true;
    }

}

