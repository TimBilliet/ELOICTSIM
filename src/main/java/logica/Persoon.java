package logica;

public abstract class Persoon {
    private int id;
    private String voornaam;
    private String achternaam;

    public Persoon(int id, String voornaam, String achternaam) {
        if (!voornaam.equals("") && !achternaam.equals("")) {
            this.id = id;
            this.voornaam = voornaam;
            this.achternaam = achternaam;
        } else {
            throw new IllegalArgumentException("Vul alle velden in");
        }
    }

    public Persoon(String voornaam, String achternaam) {
        if (!voornaam.equals("") && !achternaam.equals("")) {
            this.voornaam = voornaam;
            this.achternaam = achternaam;
        } else {
            throw new IllegalArgumentException("Vul alle velden in");
        }
    }

    public String getAchternaam() {
        return achternaam;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "id=" + id + ", " + voornaam + " " + achternaam;
    }
}
