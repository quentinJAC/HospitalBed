package fr.uha.jacquey.hospitalbed.management.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import fr.uha.jacquey.hospitalbed.helper.CompareUtil;

@Entity(
        tableName = "bpas",
        indices = { @Index("bId") }
)
public class BedPatientAssociation implements Cloneable {

    @PrimaryKey(autoGenerate = true)
    private long bId;
    private long pid;
    private Patient patient;

    public BedPatientAssociation() {
        this.bId = 0;
    }

    @Ignore
    public BedPatientAssociation(long bId, long pid, Patient patient) {
        this.bId = bId;
        this.pid = pid;
        this.patient = patient;
    }

    public BedPatientAssociation clone () {
        try {
            return (BedPatientAssociation) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }

    public long getBid() {
        return bId;
    }

    public void setBid(long bid) {
        this.bId = bid;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }


    public static boolean compare(BedPatientAssociation newAssociation, BedPatientAssociation oldAssociation) {
        if (newAssociation.getBid() != oldAssociation.getBid()) return false;
        if (newAssociation.getPid() != oldAssociation.getPid()) return false;
        if (! CompareUtil.compare(newAssociation.getPatient(), oldAssociation.getPatient())) return false;
        return true;
    }
}
