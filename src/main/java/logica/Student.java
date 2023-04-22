package logica;

import java.util.Scanner;

public class Student extends Persoon {
    private String beroepsprofiel;
    private int inschrijvingsjaar;

    public Student(int id, String voornaam, String achternaam, String beroepsprofiel, int inschrijvingsjaar) {
        super(id, voornaam, achternaam);
        this.beroepsprofiel = beroepsprofiel;
        this.inschrijvingsjaar = inschrijvingsjaar;
    }

    public int getInschrijvingsjaar() {
        return inschrijvingsjaar;
    }

    public String getBeroepsprofiel() {
        String beroepsprofiel = this.beroepsprofiel.toLowerCase();
        String uitkomst = "";
        Scanner scan = new Scanner(beroepsprofiel);
        scan.useDelimiter("_");
        while (scan.hasNext()) {
            String temp = scan.next();
            temp = temp.substring(0, 1).toUpperCase() + temp.substring(1);
            uitkomst = uitkomst + temp + " ";
        }
        return uitkomst;
    }

    @Override
    public String toString() {
        return "Student{" + super.toString() +
                "beroepsprofiel='" + beroepsprofiel + '\'' +
                ", inschrijvingsjaar=" + inschrijvingsjaar +
                '}';
    }
}
