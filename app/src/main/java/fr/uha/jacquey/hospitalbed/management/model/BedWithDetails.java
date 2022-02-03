package fr.uha.jacquey.hospitalbed.management.model;

import androidx.room.Embedded;

public class BedWithDetails {

    @Embedded
    public Patient patient;

    public static boolean compare(Patient newPatient, Patient oldPatient) {
        if (! Patient.compare (newPatient, oldPatient)) return false;
        return true;
    }

}
