package fr.uha.jacquey.hospitalbed.management.model;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.Date;
import java.util.List;

public class PatientPicker {

    static public class SmallPatient {
        public long pid;
        public String firstName;
        public String lastName;
        public Gender gender;
        public Date incomingDate;
        public Date dischargeDate;

        public long getPid() {
            return pid;
        }
    }

    @Embedded
    public SmallPatient patient;

    @Relation(
            parentColumn = "pid",
            entityColumn = "pid"
    )
    public List<BedPatientAssociation> beds;

    @Ignore
    public BedPatientAssociation bed;
}
