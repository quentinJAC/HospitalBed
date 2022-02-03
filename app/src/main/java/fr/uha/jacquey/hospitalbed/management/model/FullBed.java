package fr.uha.jacquey.hospitalbed.management.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class FullBed {
    @Embedded
    public Bed bed;
    @Relation(
            parentColumn = "bId",
            entityColumn = "bId"
    )
    public List<BedPatientAssociation> patient;

}

