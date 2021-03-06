package controller;

//import com.sun.tools.javac.util.List;

import domain.*;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class ReservationController {

    @FXML
    private TextField addReservationIdPojazduTextField;
    @FXML
    private TextField addReservationPrzewidywanaCenaTextField;
    @FXML
    private TextField addReservationIdKlientaTextField;

    @FXML
    private TextField addReservationIdPracownikaTextField;
    @FXML
    private DatePicker addReservationDataRozpTextField;
    @FXML
    private DatePicker addReservationDataZakTextField;


    @FXML
    private TextField deleteReservationIdTextField;

    @FXML
    private StackPane printReservationStackPane;

    @FXML
    private TextField editReservationIdTextField;


    @FXML
    private TextField editReservationIdPojazduTextField;
    @FXML
    private TextField editReservationIdKlientaTextField;
    @FXML
    private DatePicker editReservationDataRTextField;
    @FXML
    private DatePicker editReservationDataZTextField;
    @FXML
    private TextField editReservationPrzewidywanaCenaTextField;
    @FXML
    private TextField editReservationPracownikTextField;

    @FXML
    private TextField editReservationNewIdPojazduTextField;
    @FXML
    private TextField editReservationNewIdKlientaTextField;
    @FXML
    private TextField editReservationNewDataRTextField;
    @FXML
    private TextField editReservationNewDataZTextField;
    @FXML
    private TextField editReservationNewPrzewidywanaCenaTextField;
    @FXML
    private TextField editReservationNewPracownikTextField;


    public void addReservation() {
        System.out.println("addReservationButton");


        if (addReservationPrzewidywanaCenaTextField.getText().equals("") || addReservationIdKlientaTextField.getText().equals("") || addReservationIdPojazduTextField.getText().equals("") || addReservationIdPracownikaTextField.getText().equals("") || addReservationDataRozpTextField.getValue().equals("") || addReservationDataZakTextField.getValue().equals("")) {
            WindowSingleton.alert("Niepoprawne dane");
            return;
        }
        //todo - daty
//        List<Rezerwacja> list = (List<Rezerwacja>) DBConnector.getInstance().getEntityManager().createQuery("SELECT a FROM Rezerwacja a WHERE id_pojazdu='" + addReservationIdPojazduTextField.getText() + "'", Rezerwacja.class).getResultList();
//        if (list.size() > 0) {
//            WindowSingleton.alert("Pojazd o podanym numerze jest już zarezerwowany");
//            return;
//        }

        try {
            Date startdate = Date.valueOf(addReservationDataRozpTextField.getValue());
            Date stopdate = Date.valueOf(addReservationDataZakTextField.getValue());
        } catch (Exception e) {
            System.out.println("Niepoprawny format");
        }

        if (addReservationDataRozpTextField.getValue().isAfter(addReservationDataZakTextField.getValue())) {
            WindowSingleton.alert("Data zakończenia nie może być przed datą rozpoczęcia");
            return;
        }


        Pojazd pojazd = DBConnector.getInstance().getEntityManager().find(Pojazd.class, Long.parseLong(addReservationIdPojazduTextField.getText()));
        Klient klient = DBConnector.getInstance().getEntityManager().find(Klient.class, Long.parseLong(addReservationIdKlientaTextField.getText()));
        Pracownik pracownik = DBConnector.getInstance().getEntityManager().find(Pracownik.class, Long.parseLong(addReservationIdPracownikaTextField.getText()));

        if (pojazd.getCzyDostepny().equals("nie")) {
            WindowSingleton.alert("Pojazd niedostępny");
            return;
        }

        List<Serwis> list = DBConnector.getInstance().getEntityManager().createQuery("SELECT a FROM Serwis a WHERE id_pojazdu_id_pojazdu='" + addReservationIdPojazduTextField.getText() + "'", Serwis.class).getResultList();
        if (list.size()>0) {
            WindowSingleton.alert("Pojazd jest w serwisie");
            return;
        }

        pojazd.setCzyDostepny("nie");
        DBConnector.getInstance().editPojazd(pojazd);

        //DBConnector.getInstance().start();
        DBConnector.getInstance().addRezerwacja(new Rezerwacja(pojazd, klient, Date.valueOf(addReservationDataRozpTextField.getValue()), Date.valueOf(addReservationDataZakTextField.getValue()), Float.parseFloat(addReservationPrzewidywanaCenaTextField.getText()), pracownik));
//        DBConnector.getInstance().stop();
        WindowSingleton.alert("Dodano rezerwacje");
        addReservationPrzewidywanaCenaTextField.setText("");
        addReservationIdKlientaTextField.setText("");
        addReservationIdPojazduTextField.setText("");
        addReservationDataRozpTextField.setValue(null);
        addReservationDataZakTextField.setValue(null);
        addReservationIdPracownikaTextField.setText("");
    }

    public void showAddPojazduList() {
        WindowSingleton.showVehicleTable(addReservationIdPojazduTextField);
    }

    public void showAddKlientaList() {
        WindowSingleton.showClientTable(addReservationIdKlientaTextField);
    }

    public void showAddPracownikList() {
        WindowSingleton.showEmployeeTable(addReservationIdPracownikaTextField);
    }


    public void deleteReservationShowReservationList() {
        WindowSingleton.showRezervationTable(deleteReservationIdTextField);
    }

    public void deleteReservation() {
        if (deleteReservationIdTextField.getText().equals("")) {
            WindowSingleton.alert("Nie ma rezerwacji o podanym ID");
        }

        long _id;
        try {
            _id = Long.parseLong(deleteReservationIdTextField.getText());
            //DBConnector.getInstance().start();
            Rezerwacja rezerwacja = DBConnector.getInstance().getEntityManager().find(Rezerwacja.class, _id);
            if (rezerwacja == null) {
                WindowSingleton.alert("Nie ma takiej rezerwacji");
                //DBConnector.getInstance().stop();
                return;
            }

            //List<Wypozyczenie> list = DBConnector.getInstance().getEntityManager().createQuery("SELECT a FROM Wypozyczenie a WHERE id='" + deleteVehicleIdTextField.getText() + "'", Wypozyczenie.class).getResultList();


            Pojazd pojazd = DBConnector.getInstance().getEntityManager().find(Pojazd.class, rezerwacja.getId_pojazdu().getId_pojazdu());
            pojazd.setCzyDostepny("tak");
            DBConnector.getInstance().editPojazd(pojazd);
            DBConnector.getInstance().deleteRezerwacja(rezerwacja);
            //DBConnector.getInstance().stop();
            deleteReservationIdTextField.setText("");
        } catch (NumberFormatException e) {
            System.out.println("zły format");
            return;
        }

        WindowSingleton.alert("Usunięto rezerwacje o id = " + _id);
        System.out.println("usunieto rezerwacje o id " + _id);
    }


    public void printReservationList() {
        final TableView<Rezerwacja> table = WindowSingleton.createReservationTable();
        printReservationStackPane.getChildren().add(table);
    }

    public void fillEditedReservationFields() {
        if (editReservationIdTextField.getText().equals("")) {
            WindowSingleton.alert("Nie ma rezerwacji o podanym ID");
        }
        try {
            long id = Long.parseLong(editReservationIdTextField.getText());
        } catch (NumberFormatException e) {
            WindowSingleton.alert("Niepoprawny format ID");
            return;
        }
        Rezerwacja rezerwacja = DBConnector.getInstance().getEntityManager().find(Rezerwacja.class, Long.parseLong(editReservationIdTextField.getText()));
        editReservationNewIdPojazduTextField.setText(String.valueOf(rezerwacja.getId_pojazdu()));
        editReservationNewIdKlientaTextField.setText(String.valueOf(rezerwacja.getId_klienta()));
        editReservationNewDataRTextField.setText(String.valueOf(rezerwacja.getData_r_rezerwacji()));
        editReservationNewDataZTextField.setText(String.valueOf(rezerwacja.getData_z_rezerwacji()));
        editReservationNewPrzewidywanaCenaTextField.setText(String.valueOf(rezerwacja.getPrzewidywana_cena()));
        editReservationNewPracownikTextField.setText(String.valueOf(rezerwacja.getId_pracownika()));
    }

    public void showEditVehicleList() {
        WindowSingleton.showVehicleTable(editReservationIdPojazduTextField);
    }

    public void showEditEmployeeList() {
        WindowSingleton.showEmployeeTable(editReservationPracownikTextField);
    }

    public void showEditClientList() {
        WindowSingleton.showClientTable(editReservationIdKlientaTextField);
    }

    public void editReservation() {
        if (editReservationNewIdPojazduTextField.getText().equals("") ||
                editReservationNewIdKlientaTextField.getText().equals("") ||
                editReservationNewDataRTextField.getText().equals("") ||
                editReservationNewDataZTextField.getText().equals("") ||
                editReservationNewPrzewidywanaCenaTextField.getText().equals("") ||
                editReservationNewPracownikTextField.getText().equals("")) {
            WindowSingleton.alert("Niepoprawne dane");
            return;
        }

        try {
            Date.valueOf(editReservationNewDataRTextField.getText());
        } catch (Exception e) {
            WindowSingleton.alert("Niepoprawna data");
            return;
        }
        try {
            Date.valueOf(editReservationNewDataZTextField.getText());
        } catch (Exception e) {
            WindowSingleton.alert("Niepoprawna data");
            return;
        }

        Rezerwacja rezerwacja = DBConnector.getInstance().getEntityManager().find(Rezerwacja.class, Long.parseLong(editReservationIdTextField.getText()));

        Pojazd pojazd = DBConnector.getInstance().getEntityManager().find(Pojazd.class, Long.parseLong(editReservationIdPojazduTextField.getText()));
        Klient klient = DBConnector.getInstance().getEntityManager().find(Klient.class, Long.parseLong(editReservationIdKlientaTextField.getText()));
        Pracownik pracownik = DBConnector.getInstance().getEntityManager().find(Pracownik.class, Long.parseLong(editReservationPracownikTextField.getText()));


        if (pojazd.getCzyDostepny() == "nie") {
            WindowSingleton.alert("Pojazd niedostępny");
            return;
        }
        List<Serwis> list = DBConnector.getInstance().getEntityManager().createQuery("SELECT a FROM Serwis a WHERE id_pojazdu_id_pojazdu='" + editReservationIdPojazduTextField.getText() + "'", Serwis.class).getResultList();
        if (list.size()>0) {
            WindowSingleton.alert("Pojazd jest w serwisie");
            return;
        }
        pojazd.setCzyDostepny("nie");

        rezerwacja.setId_pojazdu(pojazd);
        rezerwacja.setId_klienta(klient);
        rezerwacja.setData_r_rezerwacji(Date.valueOf(editReservationDataRTextField.getValue()));
        rezerwacja.setData_z_rezerwacji(Date.valueOf(editReservationDataZTextField.getValue()));
        rezerwacja.setPrzewidywana_cena(Float.parseFloat(editReservationPrzewidywanaCenaTextField.getText()));
        rezerwacja.setId_pracownika(pracownik);

        DBConnector.getInstance().editRezerwacja(rezerwacja);
        WindowSingleton.alert("Zedytowano rezerwcje");

        editReservationIdTextField.setText("");

        editReservationIdPojazduTextField.setText("");
        editReservationIdKlientaTextField.setText("");
        editReservationPracownikTextField.setText("");
        editReservationDataRTextField.getEditor().clear();
        editReservationDataZTextField.getEditor().clear();
        editReservationPrzewidywanaCenaTextField.setText("");

        editReservationNewIdPojazduTextField.setText("");
        editReservationNewIdKlientaTextField.setText("");
        editReservationNewPracownikTextField.setText("");
        editReservationNewDataRTextField.setText("");
        editReservationNewDataZTextField.setText("");
        editReservationNewPrzewidywanaCenaTextField.setText("");
    }

    public void showEditReservationList() {
        WindowSingleton.showRezervationTable(editReservationIdTextField);
    }


    public void showMainMenu() throws IOException {
        WindowSingleton.getInstance().setLayout("/fxml/StartScreen.fxml");
    }

}
