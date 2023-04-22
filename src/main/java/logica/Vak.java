package logica;

public class Vak {
    int id;
    String naam;
    int studiepunten;
    Persoon docent;
    String semesters;

    public Vak(int id, String naam, int studiepunten, String semesters, Persoon docent) {
        if (!naam.equals("") && studiepunten != 0 && studiepunten < 30 && !semesters.equals("")) {
            this.id = id;
            this.naam = naam;
            this.studiepunten = studiepunten;
            this.docent = docent;
            this.semesters = semesters;
        } else {
            throw new IllegalArgumentException("Vul alle velden in");
        }
    }

    public Vak(int id, String naam, int studiepunten, String semesters) {
        if (!naam.equals("") && studiepunten != 0 && studiepunten < 30 && !semesters.equals("")) {
            this.id = id;
            this.naam = naam;
            this.studiepunten = studiepunten;
            this.semesters = semesters;
        } else {
            throw new IllegalArgumentException("Vul alle velden in");
        }
    }

    public String getNaam() {
        return naam;
    }

    public int getStudiepunten() {
        return studiepunten;
    }

    public int getId() {
        return id;
    }

    public Persoon getDocent() {
        return docent;
    }

    @Override
    public String toString() {
        return naam + ", " + studiepunten + "stp, " + semesters;
    }
}
