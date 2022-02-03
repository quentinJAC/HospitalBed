package fr.uha.jacquey.hospitalbed.management.model;


import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;

@Entity(
        tableName = "bsas",
        primaryKeys = {"sId", "bId"},
        indices = { @Index("sId"), @Index("bId")}
)
public class BedServiceAssociation {

    public long sId;
    public long bId;

    public BedServiceAssociation(){}

    @Ignore
    public BedServiceAssociation(long sId, long bId){
        this.sId=sId;
        this.bId=bId;
    }
}
