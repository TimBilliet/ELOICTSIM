package logica;

public class Deur {
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    public Deur(int x1, int y1, int x2, int y2) {
        try {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
        } catch (Exception exceptie) {
            System.out.println("Fout:" + exceptie.getMessage());
        }
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }

    @Override
    public String toString() {
        return "Deur{" +
                "x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                '}';
    }
}
