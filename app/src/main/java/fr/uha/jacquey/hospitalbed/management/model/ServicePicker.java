package fr.uha.jacquey.hospitalbed.management.model;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.List;

public class ServicePicker {


    static public class smallService {
        public long sid;
        public String name;

        public long getSid() {
            return sid;
        }
    }

    @Embedded
    public ServicePicker.smallService service;
    public Bed bedService;

    @Relation(
            parentColumn = "pid",
            entityColumn = "pid"
    )
        public List<BedServiceAssociation> beds;

    @Ignore
    public BedServiceAssociation bed;

}
