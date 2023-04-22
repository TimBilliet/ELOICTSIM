package logica;

public class Lokaal {
    private int id;
    private String naam;
    private String lokaalcode;
    private int x;
    private int y;
    private int breedte;
    private int lengte;

    public Lokaal(int id, String naam, String lokaalcode, int x, int y, int breedte, int lengte) {
        try {
            this.breedte = breedte;
            this.id = id;
            this.naam = naam;
            this.lengte = lengte;
            this.lokaalcode = lokaalcode;
            this.x = x;
            this.y = y;
        } catch (Exception exceptie) {
            System.out.println("Fout:" + exceptie.getMessage());
        }
    }

    public int getX() {
        return x;
    }

    public int getId() {
        return id;
    }

    public String getNaam() {
        return naam;
    }

    public int getY() {
        return y;
    }

    public int getBreedte() {
        return breedte;
    }

    public int getLengte() {
        return lengte;
    }

    public String getLokaalcode() {
        return lokaalcode;
    }

    @Override
    public String toString() {
        return "Lokaal{" +
                "id=" + id +
                ", naam='" + naam + '\'' +
                ", lokaalcode='" + lokaalcode + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", breedte=" + breedte +
                ", lengte=" + lengte +
                '}';
    }
}
