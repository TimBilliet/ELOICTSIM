package presentatie;

import datalaag.Datalaag;
import logica.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;

public class EloictSimGui {

    //bron: https://huisstijl2.odisee.be/huisstijl/kleurenpalet-en-kleurbalans
    private static final Color SPELER_KLEUR = new Color(231, 63, 22);//Warmrood
    private static final Color LOKAAL_KLEUR = new Color(31, 65, 107);//Nachtblauw
    private static final Color DEUR_KLEUR = new Color(211, 221, 242);//Mistblauw

    private JPanel tekenPanel;
    private JPanel mainPanel;
    private JLabel icoonLabel;
    private JLabel titelLabel;
    private JPanel informatiePanel;
    private JLabel korteTekstLabel;
    private JLabel langereTekstLabel;
    private JPanel voorGrondInfoPanel;

    private Image background;
    private Image lokaalKlein, studentKlein, docentKlein;
    private ImageIcon lokaalGroot, studentGroot, docentGroot;
    private ArrayList<Informatiepunt> informatiepuntArrayList;
    private ArrayList<Lokaal> lokaalArrayList;
    private ArrayList<Deur> deurArrayList;
    private int bezoekerX = 500;
    private int bezoekerY = 270;
    private final int VERANDER_X_MET = 5;
    private final int VERANDER_Y_MET = 5;
    private final int BEZOEKER_GROOTTE = 24;
    private final int AFSTAND = 15;
    private boolean aanInfopunt;
    private Datalaag datalaag;

    public EloictSimGui() {
        datalaag = new Datalaag();
        laadLijsten();
        mainPanel.setFocusable(true);
        voorGrondInfoPanel.setVisible(false);
        mainPanel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                super.keyPressed(e);
                switch (e.getKeyCode()) {
                    case 39 -> {
                        if (detecteerLokaal('r')) bezoekerX += VERANDER_X_MET;
                    }
                    case 37 -> {
                        if (detecteerLokaal('l')) bezoekerX -= VERANDER_X_MET;
                    }
                    case 38 -> {
                        if (detecteerLokaal('b')) bezoekerY -= VERANDER_Y_MET;
                    }
                    case 40 -> {
                        if (detecteerLokaal('o')) bezoekerY += VERANDER_Y_MET;
                    }
                }
                toonInfopunt();
            }
        });
    }

    private void createUIComponents() {
        background = laadAfbeelding("d-gang");
        lokaalKlein = laadAfbeelding("32px/lokaal");
        studentKlein = laadAfbeelding("32px/student");
        docentKlein = laadAfbeelding("32px/docent");
        lokaalGroot = laadIcoon("64px/lokaal");
        studentGroot = laadIcoon("64px/student");
        docentGroot = laadIcoon("64px/docent");

        tekenPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(background, 0, 0, null);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                tekenLokalen(g2);
                tekenDeuren(g2);
                tekenInformatiepunten(g2);
                tekenBezoeker(g2);
                repaint();
            }
        };
    }

    private void tekenLokalen(Graphics2D g) {
        g.setColor(LOKAAL_KLEUR);
        for (Lokaal lokaal : lokaalArrayList) {
            g.setStroke(new BasicStroke(6));
            g.drawRect(lokaal.getX(), lokaal.getY(), lokaal.getBreedte(), lokaal.getLengte());
        }
    }

    private void tekenDeuren(Graphics2D g) {
        g.setColor(DEUR_KLEUR);
        for (Deur deur : deurArrayList) {
            g.setStroke(new BasicStroke(6));
            g.drawLine(deur.getX1(), deur.getY1(), deur.getX2(), deur.getY2());
        }
    }

    private void tekenInformatiepunten(Graphics2D g) {
        for (Informatiepunt informatiepunt : informatiepuntArrayList) {
            if (informatiepunt.getPersoon() instanceof Docent) {
                g.drawImage(docentKlein, informatiepunt.getX() - 16, informatiepunt.getY() - 16, null);
            } else if (informatiepunt.getPersoon() instanceof Student) {
                g.drawImage(studentKlein, informatiepunt.getX() - 16, informatiepunt.getY() - 16, null);
            } else {
                g.drawImage(lokaalKlein, informatiepunt.getX() - 16, informatiepunt.getY() - 16,null);
            }
        }
    }

    private void tekenBezoeker(Graphics2D g) {
        g.setColor(SPELER_KLEUR);
        Ellipse2D bezoeker = new Ellipse2D.Double(bezoekerX - BEZOEKER_GROOTTE / 2.0, bezoekerY - BEZOEKER_GROOTTE / 2.0, BEZOEKER_GROOTTE, BEZOEKER_GROOTTE);
        g.fill(bezoeker);
    }

    private boolean detecteerLokaal(char richting) {
        int x = 0;
        int y = 0;
        switch (richting) {
            case 'b' -> {
                y = bezoekerY - 3;
                x = bezoekerX;
            }
            case 'o' -> {
                y = bezoekerY + 3;
                x = bezoekerX;
            }
            case 'l' -> {
                x = bezoekerX - 3;
                y = bezoekerY;
            }
            case 'r' -> {
                x = bezoekerX + 3;
                y = bezoekerY;
            }
        }
        if (!detecteerDeur(x, y)) {
            for (Lokaal lokaal : lokaalArrayList) {
                if (Meetkunde.cirkelOverlaptMetRechthoek(lokaal.getX(), lokaal.getY(), lokaal.getBreedte(), lokaal.getLengte(), x, y, BEZOEKER_GROOTTE / 2)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean detecteerDeur(int x, int y) {
        for (Deur deur : deurArrayList) {
            if (Meetkunde.cirkelOverlaptMetLijnstuk(deur.getX1(), deur.getY1(), deur.getX2(), deur.getY2(), x, y, BEZOEKER_GROOTTE / 2)) {
                return true;
            }
        }
        return false;
    }

    private void laadLijsten() {
        try {
            informatiepuntArrayList = datalaag.geefInformatiepuntenLijst();
            lokaalArrayList = datalaag.geefLokalenLijst();
            deurArrayList = datalaag.geefDeurenLijst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void toonInfopunt() {
        for (Informatiepunt informatiepunt : informatiepuntArrayList) {
            if (bezoekerX + AFSTAND > informatiepunt.getX() && bezoekerX - AFSTAND < informatiepunt.getX() && bezoekerY + AFSTAND > informatiepunt.getY() && bezoekerY - AFSTAND < informatiepunt.getY()) {
                if (informatiepunt.getPersoon() instanceof Docent) {
                    icoonLabel.setIcon(docentGroot);
                    titelLabel.setText(informatiepunt.getPersoon().getVoornaam() + " " + informatiepunt.getPersoon().getAchternaam());
                    korteTekstLabel.setText(((Docent) informatiepunt.getPersoon()).getRol());
                } else if (informatiepunt.getPersoon() instanceof Student) {
                    icoonLabel.setIcon(studentGroot);
                    titelLabel.setText(informatiepunt.getPersoon().getVoornaam() + " " + informatiepunt.getPersoon().getAchternaam());
                    korteTekstLabel.setText(((Student) informatiepunt.getPersoon()).getBeroepsprofiel() + " " + ((Student) informatiepunt.getPersoon()).getInschrijvingsjaar());
                } else {
                    icoonLabel.setIcon(lokaalGroot);
                    titelLabel.setText(informatiepunt.getLokaal().getNaam());
                    korteTekstLabel.setText(informatiepunt.getLokaal().getLokaalcode());
                }
                langereTekstLabel.setText("<html>" + informatiepunt.getBeschrijving() + "</html>");
                aanInfopunt = true;
            }
            voorGrondInfoPanel.setVisible(aanInfopunt);
        }
        aanInfopunt = false;
        tekenPanel.repaint();
    }

    private static Image laadAfbeelding(String bestand) {
        try {
            URL resource = EloictSimGui.class.getResource("/" + bestand + ".png");
            return ImageIO.read(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static ImageIcon laadIcoon(String bestand) {
        return new ImageIcon(laadAfbeelding(bestand));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("ELO-ICT SIM - Tim Billiet");
        frame.setContentPane(new EloictSimGui().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(laadAfbeelding("O-32x32"));
        frame.pack();
        frame.setVisible(true);
    }
}
