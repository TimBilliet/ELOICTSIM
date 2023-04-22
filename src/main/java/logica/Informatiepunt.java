package logica;

public class Informatiepunt {
    private int id;
    private Lokaal lokaal;
    private Persoon persoon;
    private int x;
    private int y;
    private String beschrijving;

    public Informatiepunt(int id, Lokaal lokaal, int x, int y, String beschrijving) {
        try {
            this.id = id;
            this.lokaal = lokaal;
            this.x = x;
            this.y = y;
            this.beschrijving = beschrijving;
        } catch (Exception exceptie) {
            System.out.println("Fout:" + exceptie.getMessage());
        }
    }

    public Informatiepunt(int id, Persoon persoon, int x, int y, String beschrijving) {
        try {
            this.id = id;
            this.persoon = persoon;
            this.x = x;
            this.y = y;
            this.beschrijving = beschrijving;
        } catch (Exception exceptie) {
            System.out.println("Fout:" + exceptie.getMessage());
        }
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public Persoon getPersoon() {
        return persoon;
    }

    public Lokaal getLokaal() {
        return lokaal;
    }
}
