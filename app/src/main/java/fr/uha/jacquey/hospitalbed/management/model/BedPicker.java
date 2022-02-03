package fr.uha.jacquey.hospitalbed.management.model;

import androidx.room.Embedded;
import androidx.room.Ignore;
import androidx.room.Relation;

import java.util.Date;
import java.util.List;

public class BedPicker {

    static public class SmallBed {
        public long bid;
        public long numero;
        public Service service;
        public Patient patient;

        public long getBid() {
            return bid;
        }
    }

    @Embedded
    public BedPicker.SmallBed bed;
    public Bed bedBed;

    @Relation(
            parentColumn = "pid",
            entityColumn = "pid"
    )
    public List<Bed> beds;

    @Ignore
    public Bed bed02;

}

