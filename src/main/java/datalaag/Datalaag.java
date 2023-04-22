package datalaag;

import logica.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Datalaag {
    private final String gebruikersnaam = "";
    private final String wachtwoord = "";
    private final String dbName = "eloictsim";
    private Connection connectie;

    public Datalaag() {
        makeConnection();
    }

    private void makeConnection() {
        try {
            this.connectie = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
                    + dbName + "?serverTimezone=UTC&allowMultiQueries=true", gebruikersnaam, wachtwoord);
        } catch (SQLException ex) {
            Logger.getLogger(Datalaag.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<Lokaal> geefLokalenLijst() throws SQLException {
        Statement statement = null;
        ArrayList<Lokaal> lokalenLijst = new ArrayList<>();
        try {
            statement = this.connectie.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM lokalen;");
            while (rs.next()) {
                int id = rs.getInt("id");
                String naam = rs.getString("naam");
                String lokaalcode = rs.getString("lokaalcode");
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                int breedte = rs.getInt("breedte");
                int lengte = rs.getInt("lengte");
                lokalenLijst.add(new Lokaal(id, naam, lokaalcode, x, y, breedte, lengte));
            }
        } catch (SQLException exceptie) {
            Logger.getLogger(Datalaag.class.getName()).log(Level.SEVERE, null, exceptie);

        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return lokalenLijst;
    }

    public ArrayList<Deur> geefDeurenLijst() throws SQLException {
        Statement statement = null;
        ArrayList<Deur> deurenLijst = new ArrayList<>();
        try {
            statement = this.connectie.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM deuren;");
            while (rs.next()) {
                int x1 = rs.getInt("x1");
                int y1 = rs.getInt("y1");
                int x2 = rs.getInt("x2");
                int y2 = rs.getInt("y2");
                deurenLijst.add(new Deur(x1, y1, x2, y2));
            }
        } catch (SQLException exceptie) {
            Logger.getLogger(Datalaag.class.getName()).log(Level.SEVERE, null, exceptie);

        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return deurenLijst;
    }

    public ArrayList<Informatiepunt> geefInformatiepuntenLijst() throws SQLException {
        Statement statement = null;
        ArrayList<Informatiepunt> informatiepuntenLijst = new ArrayList<>();
        ArrayList<Persoon> personenLijst = geefPersonenLijst();
        ArrayList<Lokaal> lokalenLijst = geefLokalenLijst();
        try {
            statement = this.connectie.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM informatiepunten;");
            while (rs.next()) {
                int id = rs.getInt("id");
                int lokaalId = rs.getInt("lokaal_id");
                int persoonId = rs.getInt("persoon_id");
                int x = rs.getInt("x");
                int y = rs.getInt("y");
                String beschrijving = rs.getString("beschrijving");
                Informatiepunt informatiepunt = null;
                if (lokaalId != 0) {
                    for (Lokaal lokaal : lokalenLijst) {
                        if (lokaalId == lokaal.getId()) {
                            informatiepunt = new Informatiepunt(id, lokaal, x, y, beschrijving);
                        }
                    }
                } else if (persoonId != 0) {
                    for (Persoon persoon : personenLijst) {
                        if (persoonId == persoon.getId()) {
                            informatiepunt = new Informatiepunt(id, persoon, x, y, beschrijving);
                        }
                    }
                }
                informatiepuntenLijst.add(informatiepunt);
            }
        } catch (SQLException exceptie) {
            Logger.getLogger(Datalaag.class.getName()).log(Level.SEVERE, null, exceptie);

        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return informatiepuntenLijst;
    }

    public ArrayList<Persoon> geefPersonenLijst() throws SQLException {
        Statement statement = null;
        ArrayList<Persoon> personenLijst = new ArrayList<>();
        try {
            statement = this.connectie.createStatement();
            ResultSet rs = statement.executeQuery("SELECT personen.id, familienaam, voornaam, rol, beroepsprofielen.naam AS beroepsprofiel, inschrijvingsjaar FROM personen LEFT JOIN docenten ON docenten.id = personen.id  LEFT JOIN studenten ON studenten.id = personen.id LEFT JOIN beroepsprofielen ON beroepsprofielen.id = studenten.beroepsprofiel_id;");
            while (rs.next()) {
                int id = rs.getInt("personen.id");
                String achternaam = rs.getString("familienaam");
                String voornaam = rs.getString("voornaam");
                String rol = rs.getString("rol");
                String beroepsprofiel = rs.getString("beroepsprofiel");
                int inschrijvingsjaar = rs.getInt("inschrijvingsjaar");
                Persoon persoon;
                if (beroepsprofiel == null) {
                    persoon = new Docent(id, voornaam, achternaam, rol);
                } else {
                    persoon = new Student(id, voornaam, achternaam, beroepsprofiel, inschrijvingsjaar);
                }
                personenLijst.add(persoon);
            }
        } catch (SQLException exceptie) {
            Logger.getLogger(Datalaag.class.getName()).log(Level.SEVERE, null, exceptie);
        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return personenLijst;
    }

    public void voegDocentToe(Persoon nieuweDocent) throws SQLException {
        PreparedStatement insertInPersonen = null;
        PreparedStatement insertInDocenten = null;
        ResultSet rs;
        int id;
        try {
            insertInPersonen = connectie.prepareStatement("INSERT INTO personen (familienaam, voornaam) VALUES ( ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
            insertInPersonen.setString(1, nieuweDocent.getAchternaam());
            insertInPersonen.setString(2, nieuweDocent.getVoornaam());
            insertInPersonen.executeUpdate();
            rs = insertInPersonen.getGeneratedKeys();
            while (rs.next()) {
                id = rs.getInt(1);
                insertInDocenten = connectie.prepareStatement("INSERT INTO docenten (id, rol) VALUES (?, ?)");
                insertInDocenten.setInt(1, id);
                insertInDocenten.setString(2, ((Docent) nieuweDocent).getRol());
                insertInDocenten.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Datalaag.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (insertInPersonen != null) {
                insertInPersonen.close();
            }
            if (insertInDocenten != null) {
                insertInDocenten.close();
            }
        }
    }

    public void bewerkDocent(Persoon nieuweInfoDocent) throws SQLException {
        PreparedStatement updatePersonen = null;
        PreparedStatement updateDocenten = null;
        try {
            updateDocenten = connectie.prepareStatement("UPDATE docenten SET rol = ? where id = ?");
            updateDocenten.setString(1, ((Docent) nieuweInfoDocent).getRol());
            updateDocenten.setInt(2, nieuweInfoDocent.getId());
            updateDocenten.executeUpdate();
            updatePersonen = connectie.prepareStatement("UPDATE personen SET familienaam = ?, voornaam = ? WHERE id = ?");
            updatePersonen.setString(1, nieuweInfoDocent.getAchternaam());
            updatePersonen.setString(2, nieuweInfoDocent.getVoornaam());
            updatePersonen.setInt(3, nieuweInfoDocent.getId());
            updatePersonen.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Datalaag.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (updatePersonen != null) {
                updatePersonen.close();
            }
            if (updateDocenten != null) {
                updateDocenten.close();
            }
        }
    }

    public ArrayList<Vak> geefVakkenLijst() throws SQLException {
        Statement statement = null;
        ArrayList<Vak> vakkenLijst = new ArrayList<>();
        ArrayList<Persoon> personenLijst = geefPersonenLijst();
        try {
            statement = this.connectie.createStatement();
            ResultSet rs = statement.executeQuery("SELECT vakken.id, vakken.naam, aantal_studiepunten as stp, periode, docent_id FROM vakken INNER JOIN docenten_has_vakken ON vakken.id = docenten_has_vakken.vak_id INNER JOIN docenten ON docenten.id = docenten_has_vakken.docent_id;");
            while (rs.next()) {
                int id = rs.getInt("vakken.id");
                String naam = rs.getString("vakken.naam");
                int stp = rs.getInt("stp");
                String semesters = rs.getString("periode");
                int docentId = rs.getInt("docent_id");
                Vak vak = null;
                for (Persoon persoon : personenLijst) {
                    if (docentId == persoon.getId()) {
                        vak = new Vak(id, naam, stp, semesters, persoon);
                    }
                }
                vakkenLijst.add(vak);
            }
        } catch (SQLException exceptie) {
            Logger.getLogger(Datalaag.class.getName()).log(Level.SEVERE, null, exceptie);

        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return vakkenLijst;
    }

    public ArrayList<Vak> geefSoortenVakkenLijst() throws SQLException {
        Statement statement = null;
        ArrayList<Vak> vakkenLijst = new ArrayList<>();
        try {
            statement = this.connectie.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM vakken;");
            while (rs.next()) {
                int id = rs.getInt("id");
                String naam = rs.getString("naam");
                int stp = rs.getInt("aantal_studiepunten");
                String semesters = rs.getString("periode");
                vakkenLijst.add(new Vak(id, naam, stp, semesters));
            }
        } catch (SQLException exceptie) {
            Logger.getLogger(Datalaag.class.getName()).log(Level.SEVERE, null, exceptie);

        } finally {
            if (statement != null) {
                statement.close();
            }
        }
        return vakkenLijst;
    }

    public void verwijderVak(Persoon docent, Vak teVerwijderenVak) throws SQLException {
        PreparedStatement verwijderen = null;
        try {
            verwijderen = connectie.prepareStatement("DELETE FROM docenten_has_vakken WHERE docent_id = ? AND vak_id = ?");
            verwijderen.setInt(1, docent.getId());
            verwijderen.setInt(2, teVerwijderenVak.getId());
            verwijderen.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(Datalaag.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (verwijderen != null) {
                verwijderen.close();
            }
        }
    }

    public void voegVakToe(Persoon docent, Vak toeTeVoegenVak) throws SQLException {
        PreparedStatement verwijderen = null;
        try {
            verwijderen = connectie.prepareStatement("INSERT INTO docenten_has_vakken VALUES (?, ?)");
            verwijderen.setInt(1, docent.getId());
            verwijderen.setInt(2, toeTeVoegenVak.getId());
            verwijderen.executeUpdate();
        } catch (SQLException ex) {
            throw new IllegalArgumentException("Docent heeft vak al");
        } finally {
            if (verwijderen != null) {
                verwijderen.close();
            }
        }
    }
}
