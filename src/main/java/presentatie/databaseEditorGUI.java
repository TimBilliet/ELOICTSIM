package presentatie;

import datalaag.Datalaag;
import logica.Docent;
import logica.Persoon;
import logica.Vak;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class databaseEditorGUI {
    private JPanel mainPanel;
    private JPanel docentPanel;
    private JButton docentToevoegenButton;
    private JList docentenList;
    private JTextField nieuweRolField;
    private JTextField nieuweAchternaamField;
    private JTextField nieuweVoornaamField;
    private JButton bewerkDocentButton;
    private JLabel geselecteerdeDocentLabel;
    private JLabel huidigeVoornaamLabel;
    private JLabel huidigeAchternaamLabel;
    private JLabel huidigeRolLabel;
    private JLabel voornaamLabel;
    private JLabel rolLabel;
    private JLabel achternaamLabel;
    private JLabel nieuweDataLabel;
    private JLabel feedbackLabel;
    private JList vakkenList;
    private JLabel vakkenLabel;
    private JButton verwijderHuidigVakButton;
    private JLabel vakdataLabel;
    private JButton vakToevoegenButton;
    private JList teKiezenVakkenList;
    private Datalaag datalaag;
    private ArrayList<Docent> docentenArraylist;
    private ArrayList<Vak> vakkenArraylist;
    private ArrayList<Vak> alleVakkenArraylist;
    private ArrayList<Vak> huidigeDocentVakkenArraylist;


    public databaseEditorGUI() {
        datalaag = new Datalaag();
        try {
            alleVakkenArraylist = datalaag.geefSoortenVakkenLijst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        geefDocentenLijstWeer();
        geefAlleVakkenWeer();
        docentToevoegenButton.addActionListener(e -> {
            try {
                if (!isInt(nieuweVoornaamField.getText()) && !isInt(nieuweAchternaamField.getText()) && !isInt(nieuweRolField.getText())) {
                    datalaag.voegDocentToe(new Docent(nieuweVoornaamField.getText(), nieuweAchternaamField.getText(), nieuweRolField.getText()));
                    feedbackLabel.setForeground(new Color(40, 180, 40));
                    feedbackLabel.setText("Succesvol toegevoegd");
                    geefDocentenLijstWeer();
                    nieuweVoornaamField.setText("");
                    nieuweAchternaamField.setText("");
                    nieuweRolField.setText("");
                } else {
                    throw new IllegalArgumentException("Ongeldige invoer");
                }
            } catch (Exception exceptie) {
                feedbackLabel.setForeground(Color.RED);
                feedbackLabel.setText(exceptie.getMessage());
            }
        });

        docentenList.addListSelectionListener(e -> {
            for (int i = 0; i < docentenArraylist.size(); i++) {
                if (docentenList.getSelectedIndex() == i) {
                    huidigeVoornaamLabel.setText(docentenArraylist.get(i).getVoornaam());
                    huidigeAchternaamLabel.setText(docentenArraylist.get(i).getAchternaam());
                    huidigeRolLabel.setText(docentenArraylist.get(i).getRol());
                    geefVakkenLijstWeer();
                }
            }
        });
        bewerkDocentButton.addActionListener(e -> {
            geefVakkenLijstWeer();
            try {
                int id = 0;
                for (int i = 0; i < docentenArraylist.size(); i++) {
                    if (docentenList.getSelectedIndex() == i) {
                        id =  docentenArraylist.get(i).getId();
                    }
                }
                if (id != 0) {
                    if (!isInt(nieuweVoornaamField.getText()) && !isInt(nieuweAchternaamField.getText()) && !isInt(nieuweRolField.getText())) {
                        datalaag.bewerkDocent(new Docent(id, nieuweVoornaamField.getText(), nieuweAchternaamField.getText(), nieuweRolField.getText()));
                        geefDocentenLijstWeer();
                        feedbackLabel.setForeground(new Color(40, 180, 40));
                        feedbackLabel.setText("Succesvol bewerkt");
                        huidigeVoornaamLabel.setText("");
                        huidigeAchternaamLabel.setText("");
                        huidigeRolLabel.setText("");
                        nieuweVoornaamField.setText("");
                        nieuweAchternaamField.setText("");
                        nieuweRolField.setText("");
                    } else {
                        throw new IllegalArgumentException("Ongeldige invoer");
                    }
                } else {
                    throw new IllegalArgumentException("Selecteer een docent");
                }
            } catch (Exception exceptie) {
                feedbackLabel.setForeground(Color.RED);
                feedbackLabel.setText(exceptie.getMessage());
            }
        });
        verwijderHuidigVakButton.addActionListener(e -> {
            huidigeVoornaamLabel.setText("");
            huidigeAchternaamLabel.setText("");
            huidigeRolLabel.setText("");
            try {
                Persoon docent = null;
                Vak vak = null;
                if (vakkenList.getSelectedIndex() > 0) {
                    for (int i = 0; i < docentenArraylist.size(); i++) {
                        if (docentenList.getSelectedIndex() == i) {
                            docent = docentenArraylist.get(i);
                        }
                    }
                    for (int i = 0; i < huidigeDocentVakkenArraylist.size(); i++) {
                        vak = huidigeDocentVakkenArraylist.get(i);
                    }
                    datalaag.verwijderVak(docent, vak);
                    feedbackLabel.setForeground(new Color(40, 180, 40));
                    feedbackLabel.setText("Succesvol verwijderd");
                } else {
                    throw new IllegalArgumentException("Selecteer een docent en vak");
                }
                geefDocentenLijstWeer();
                geefVakkenLijstWeer();
            } catch (Exception exceptie) {
                feedbackLabel.setForeground(Color.RED);
                feedbackLabel.setText(exceptie.getMessage());
            }
        });
        vakToevoegenButton.addActionListener(e -> {
            try {
                Persoon docent = null;
                Vak vak = null;
                for (int i = 0; i < docentenArraylist.size(); i++) {
                    if (docentenList.getSelectedIndex() == i) {
                        docent = docentenArraylist.get(i);
                    }
                }
                for (int i = 0; i < alleVakkenArraylist.size(); i++) {
                    if (teKiezenVakkenList.getSelectedIndex() == i) {
                        vak = alleVakkenArraylist.get(i);
                    }
                }
                if (docent != null && vak != null) {
                    datalaag.voegVakToe(docent, vak);
                    feedbackLabel.setForeground(new Color(40, 180, 40));
                    feedbackLabel.setText("Succesvol toegevoegd");
                } else {
                    throw new IllegalArgumentException("Selecteer een docent en vak");
                }
                geefVakkenLijstWeer();
            } catch (Exception exceptie) {
                feedbackLabel.setForeground(Color.RED);
                feedbackLabel.setText(exceptie.getMessage());
            }
        });
    }

    private void geefDocentenLijstWeer() {
        DefaultListModel<Docent> model = new DefaultListModel<>();
        try {
            docentenArraylist = new ArrayList<>();
            for (int i = 0; i < datalaag.geefPersonenLijst().size(); i++) {
                if (datalaag.geefPersonenLijst().get(i) instanceof Docent) {
                    model.addElement((Docent) datalaag.geefPersonenLijst().get(i));
                    docentenArraylist.add((Docent) datalaag.geefPersonenLijst().get(i));
                }
            }
            docentenList.setModel(model);
        } catch (SQLException e) {
            Logger.getLogger(Datalaag.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public void geefVakkenLijstWeer() {
        DefaultListModel<Vak> model = new DefaultListModel<>();
        huidigeDocentVakkenArraylist = new ArrayList<>();
        try {
            vakkenArraylist = datalaag.geefVakkenLijst();
        } catch (Exception ignored) {
        }
        for (int i = 0; i < docentenArraylist.size(); i++) {
            if (docentenList.getSelectedIndex() == i) {
                for (Vak vak : vakkenArraylist) {
                    if (vak.getDocent().getId() == docentenArraylist.get(i).getId()) {
                        model.addElement(vak);
                        huidigeDocentVakkenArraylist.add(vak);
                    }
                }
            }
        }
        vakkenList.setModel(model);
    }

    public void geefAlleVakkenWeer() {
        DefaultListModel<Vak> model = new DefaultListModel<>();
        try {
            alleVakkenArraylist = datalaag.geefSoortenVakkenLijst();
            for (Vak vak : alleVakkenArraylist) {
                model.addElement(vak);
            }
            teKiezenVakkenList.setModel(model);
        } catch (SQLException e) {
            Logger.getLogger(Datalaag.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static boolean isInt(String tekst) {
        try {
            Integer.parseInt(tekst);
        } catch (NumberFormatException | NullPointerException exceptie) {
            return false;
        }
        return true;
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

    public static void main(String[] args) {
        JFrame frame = new JFrame("Database Editor");
        frame.setContentPane(new databaseEditorGUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setIconImage(laadAfbeelding("O-32x32"));
        frame.pack();
        frame.setVisible(true);
    }
}
