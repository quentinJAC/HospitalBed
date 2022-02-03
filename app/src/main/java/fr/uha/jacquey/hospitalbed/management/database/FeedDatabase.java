package fr.uha.jacquey.hospitalbed.management.database;

import java.util.Date;

import fr.uha.jacquey.hospitalbed.management.model.Bed;
import fr.uha.jacquey.hospitalbed.management.model.Gender;
import fr.uha.jacquey.hospitalbed.management.model.Patient;
import fr.uha.jacquey.hospitalbed.management.model.Service;

public class FeedDatabase {

    Service radiologie = new Service("radiologie");
    Service cardiologie = new Service("cardiologie");
    Service pédiatrie = new Service("pédiatrie");
    Patient quentin = new Patient(10,"Quentin","Jacquey",Gender.BOY,new Date(2021,12,10,11,06),new Date(2021,12,20,11,06));
    Patient bruce = new Patient(20,"Bruce","Wayne",Gender.BOY,new Date(2021,11,10,11,06),new Date(2021,12,15,11,06));
    Patient diana = new Patient(30,"Diana","Prince",Gender.GIRL,new Date(2020,12,10,11,06),new Date(2021,12,18,11,06));

    private long [] feedPatients(){
        PatientDao dao =AppDatabase.get().getPatientDao();
        long [] ids = new long[3];
        ids[0]=dao.upsert(quentin);
        ids[1]=dao.upsert(bruce);
        ids[2]=dao.upsert(diana);
        return ids;
    }

    private long [] feedBeds(){
        BedDao dao = AppDatabase.get().getBedDao();
        long [] ids = new long[3];
        ids[0]=dao.upsert(new Bed(1000,diana));
        ids[1]=dao.upsert(new Bed(2000,bruce));
        ids[2]=dao.upsert(new Bed(3000,quentin));
        return ids;
    }

    private long [] feedServices(){
        ServiceDao dao = AppDatabase.get().getServiceDao();
        long [] ids = new long[3];
        ids[0]=dao.upsert(radiologie);
        ids[1]=dao.upsert(cardiologie);
        ids[2]=dao.upsert(pédiatrie);
        return ids;
    }

    public void feed(){
        feedPatients();
         feedServices();
        feedBeds();
    }
}
