package logica;

public class Docent extends Persoon {
    private String rol;

    public Docent(int id, String voornaam, String achternaam, String rol) {
        super(id, voornaam, achternaam);
        this.rol = rol;
    }

    public Docent(String voornaam, String achternaam, String rol) {
        super(voornaam, achternaam);
        this.rol = rol;
    }

    public String getRol() {
        return rol;
    }

    @Override
    public String toString() {
        if (rol != null && !rol.equals("")) return super.toString() + ", " + rol;
        else return super.toString();
    }
}
