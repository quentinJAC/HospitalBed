package fr.uha.jacquey.hospitalbed.management.model;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class FullService {
    @Embedded
    public Service service;
    @Relation(parentColumn = "bId", entityColumn = "sId")
    public Bed bed;
    @Relation(
            parentColumn = "bId",
            entityColumn = "sId",
            associateBy = @Junction(BedServiceAssociation.class)
    )
    public List<Bed> beds;
}
