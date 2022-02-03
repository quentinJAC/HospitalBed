package fr.uha.jacquey.hospitalbed.management.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class FullPatient {

    @Embedded
    public Patient patient;
    @Relation(
            parentColumn = "pid",
            entityColumn = "pid"
    )
    public List<BedPatientAssociation> beds;

}
