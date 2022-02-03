package fr.uha.jacquey.hospitalbed.management.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

import fr.uha.jacquey.hospitalbed.helper.CompareUtil;


@Entity(tableName = "Patients")
public class Patient {

    @PrimaryKey (autoGenerate = true)
    private long pid;
    private String firstName;
    private String lastName;
    private Gender gender;
    private Date incomingDate;
    private Date dischargeDate;

    public Patient() {
        this.pid = 0;
    }

    @Ignore
    public Patient(long pid, String firstName, String lastName, Gender gender, Date incomingDate, Date dischargeDate) {
        this.pid = pid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.incomingDate=incomingDate;
        this.dischargeDate=dischargeDate;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }


    public Date getIncomingDate() {
        return incomingDate;
    }

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setIncomingDate(Date incomingDate) {
        this.incomingDate = incomingDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    public String toString() {
        StringBuilder tmp = new StringBuilder();
        tmp.append("@");
        tmp.append(pid);
        tmp.append(" ");
        tmp.append(firstName);
        tmp.append(".");
        tmp.append(lastName);
        tmp.append(" ");
        tmp.append(gender != null ? gender.name() : "null");
        tmp.append(" ");
        tmp.append(incomingDate);
        tmp.append("  ");
        tmp.append(dischargeDate);
        return tmp.toString();
    }

    public static boolean compare(Patient newPatient, Patient oldPatient) {
        if (newPatient.getPid() != oldPatient.getPid()) return false;
        if (! CompareUtil.compare(newPatient.getFirstName(), oldPatient.getFirstName())) return false;
        if (! CompareUtil.compare(newPatient.getLastName(), oldPatient.getLastName())) return false;
        if (! CompareUtil.compare(newPatient.getGender(), oldPatient.getGender())) return false;
        if (! CompareUtil.compare(newPatient.getIncomingDate(), oldPatient.getIncomingDate())) return false;
        if (! CompareUtil.compare(newPatient.getDischargeDate(), oldPatient.getDischargeDate())) return false;
        return true;
    }
}
