package presentatie;

import datalaag.Datalaag;
import logica.Docent;
import logica.Student;

import java.sql.SQLException;

public class Demo {
    public static void main(String[] args) throws SQLException {
        Datalaag test = new Datalaag();
        //System.out.println(test.geefInformatiepuntenLijst());
        //System.out.println(test.geefInformatiepuntenLijst().size());
        //System.out.println(test.geefPersonenLijst());
        //System.out.println(test.geefDeurenLijst());
        //System.out.println(((Student)test.geefInformatiepuntenLijst().get(1).getPersoon()).getBeroepsprofiel());
        //test.voegDocentToe(new Docent("jef", "test", "rol"));
        System.out.println(test.geefVakkenLijst());
    }
}
