package controller;

import domain.*;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.sql.Date;
import java.util.List;


public class VehicleOptionsController {

    @FXML
    private TextField addVehicleTypTextField;
    @FXML
    private TextField addVehicleMarkaTextField;
    @FXML
    private TextField addVehicleModelTextField;
    @FXML
    private TextField addVehicleUbezpieczenieTextField;
    @FXML
    private TextField addVehicleStanPojazduTextField;
    @FXML
    private DatePicker addVehicleTerminWaznosciBadaniaDatePicker;
    @FXML
    private TextField addVehiclePunktPostojuTextField;
    @FXML
    private TextField addInsuranceTypTextField;
    @FXML
    private TextField addInsuranceCenaTextField;
    @FXML
    private TextField addInsuranceDataWaznosciTextField;
    @FXML
    private DatePicker addInsuranceDataWaznosciDatePicker;
    @FXML
    private TextField deleteVehicleIdTextField;
    @FXML
    private TextField editVehicleIdTextField;
    @FXML
    private TextField editVehicleTypTextField;
    @FXML
    private TextField editVehicleMarkaTextField;
    @FXML
    private TextField editVehicleModelTextField;
    @FXML
    private TextField editVehicleUbezpieczenieTextField;
    @FXML
    private TextField editVehicleStanPojazduTextField;
    @FXML
    private TextField editVehicleDataWaznosciBadaniaTextField;
    @FXML
    private TextField editVehiclePunktPostojuTextField;
    @FXML
    private TextField editVehicleNewTypTextField;
    @FXML
    private TextField editVehicleNewMarkaTextField;
    @FXML
    private TextField editVehicleNewModelTextField;
    @FXML
    private TextField editVehicleNewUbezpieczenieTextField;
    @FXML
    private TextField editVehicleNewStanPojazduTextField;
    @FXML
    private DatePicker editVehicleNewDataWaznosciBadaniaDatePicker;
    @FXML
    private TextField editVehicleNewPunktPostojuTextField;
    @FXML
    private StackPane printVehicleStackPane;
    @FXML
    private StackPane printInsuranceStackPane;
    @FXML
    private TextField deleteInsuranceIdTextField;
    @FXML
    private TextField editInsuranceTextField;
    @FXML
    private TextField editInsuranceTypTextField;
    @FXML
    private TextField editInsuranceCenaTextField;
    @FXML
    private TextField editInsuranceDataWaznosciTextField;
    @FXML
    private TextField editInsuranceNewTypTextField;
    @FXML
    private TextField editInsuranceNewCenaTextField;
    @FXML
    private DatePicker editInsuranceDataWaznosciDatePicker;
    @FXML
    private TextField addServiceVehicleIdTextField;

    @FXML
    private TextField deleteServiceIdTextField;
    @FXML
    private TextField addReturnIdPojazduTextField;

    @FXML
    private TextField editServiceIdTextField;
    @FXML
    private TextField addServiceCenaTextField;
    @FXML
    private DatePicker addServiceDataRDatePicker;
    @FXML
    private DatePicker addServiceDataZDatePicker;
    @FXML
    private StackPane printServiceStackPane;


    public void addService() {
        if (addServiceCenaTextField.getText().equals("") ||
                addServiceDataRDatePicker.getValue().toString().equals("") ||
                addServiceDataZDatePicker.getValue().toString().equals("") ||
                addServiceVehicleIdTextField.getText().equals("")) {
            WindowSingleton.alert("Niepoprawne dane");
            return;
        }
        try {
            Float.parseFloat(addServiceCenaTextField.getText());
            Date.valueOf(addServiceDataRDatePicker.getValue());
            Date.valueOf(addServiceDataZDatePicker.getValue());
        } catch (NumberFormatException e) {
            WindowSingleton.alert("Niepoprawny format");
            return;
        }

        if (addServiceDataRDatePicker.getValue().isAfter(addServiceDataZDatePicker.getValue())) {
            WindowSingleton.alert("Data zakończenia nie może być przed datą rozpoczęcia");
            return;
        }

        Pojazd pojazd = DBConnector.getInstance().getEntityManager().find(Pojazd.class, Long.parseLong(addServiceVehicleIdTextField.getText()));
        List<Wypozyczenie> list = DBConnector.getInstance().getEntityManager().createQuery("SELECT a FROM Wypozyczenie a WHERE id_pojazdu='" + addServiceVehicleIdTextField.getText() + "'", Wypozyczenie.class).getResultList();
        if(list.size()>0){
            WindowSingleton.alert("Pojazd jest w trakcie wypożyczenia");
            return;
        }
        pojazd.setCzyDostepny("nie");

        DBConnector.getInstance().editPojazd(pojazd);
        DBConnector.getInstance().addService(
                new Serwis(
                        pojazd,
                        Date.valueOf(addServiceDataRDatePicker.getValue().toString()),
                        Date.valueOf(addServiceDataZDatePicker.getValue().toString()),
                        Float.parseFloat(addServiceCenaTextField.getText())
                )
        );

        WindowSingleton.alert("Dodano serwis");
    }

    public void showAddPojazdList() {
        WindowSingleton.showVehicleTable(addServiceVehicleIdTextField);
    }


    public void printService() {
        final TableView<Serwis> table = WindowSingleton.createServiceTable();
        printServiceStackPane.getChildren().add(table);
    }


    public void showDeleteServiceList() {
        WindowSingleton.showServiceTable(deleteServiceIdTextField);
    }


    public void deleteService() {

        if (deleteServiceIdTextField.getText().equals("")) {
            WindowSingleton.alert("Niepoprawne ID");
            return;
        }
        Serwis serwis = DBConnector.getInstance().getEntityManager().find(Serwis.class, Long.parseLong(deleteServiceIdTextField.getText()));
        if (serwis == null) {
            WindowSingleton.alert("Nie ma serwisu o podanym ID");
            return;
        }
        Pojazd pojazd = DBConnector.getInstance().getEntityManager().find(Pojazd.class, serwis.getId_pojazdu().getId_pojazdu());
        pojazd.setCzyDostepny("tak");
        DBConnector.getInstance().editPojazd(pojazd);

        DBConnector.getInstance().deleteService(serwis);
        WindowSingleton.alert("Usunięto serwis o ID = " + deleteServiceIdTextField.getText());
        deleteServiceIdTextField.setText("");

    }


    public void addVehicle() {
        if (addVehicleTypTextField.getText().equals("") || addVehicleMarkaTextField.getText().equals("") || addVehicleModelTextField.getText().equals("") || addVehicleUbezpieczenieTextField.getText().equals("") || addVehicleStanPojazduTextField.getText().equals("") || addVehicleTerminWaznosciBadaniaDatePicker.getValue().toString().equals("") || addVehiclePunktPostojuTextField.getText().equals("")) {
            WindowSingleton.alert("Niepoprawne dane");
            return;
        }

        Ubezpieczenie ubezpieczenie = DBConnector.getInstance().getEntityManager().find(Ubezpieczenie.class, Long.parseLong(addVehicleUbezpieczenieTextField.getText()));
        Punkt_Wypozyczen punkt_postoju = DBConnector.getInstance().getEntityManager().find(Punkt_Wypozyczen.class, Long.parseLong(addVehiclePunktPostojuTextField.getText()));

        //DBConnector.getInstance().start();
        DBConnector.getInstance().addPojazd(new Pojazd(
                addVehicleTypTextField.getText(),
                addVehicleMarkaTextField.getText(),
                addVehicleModelTextField.getText(),
                ubezpieczenie,
                addVehicleStanPojazduTextField.getText(),
                Date.valueOf(addVehicleTerminWaznosciBadaniaDatePicker.getValue().toString()),
                punkt_postoju));
        //DBConnector.getInstance().stop();
        WindowSingleton.alert("Dodano pojazd");

        addVehicleTypTextField.setText("");
        addVehicleMarkaTextField.setText("");
        addVehicleModelTextField.setText("");
        addVehicleStanPojazduTextField.setText("");
        addVehicleTerminWaznosciBadaniaDatePicker.setAccessibleText("");
        addVehiclePunktPostojuTextField.setText("");
        addVehicleUbezpieczenieTextField.setText("");
        addVehicleTerminWaznosciBadaniaDatePicker.getEditor().clear();
    }

    public void showAddRentalPointList() {
        WindowSingleton.showRentalPointTable(addVehiclePunktPostojuTextField);
    }

    public void showDeleteVehicleList() {
        WindowSingleton.showVehicleTable(deleteVehicleIdTextField);
    }

    public void showEditVehicleList() {
        WindowSingleton.showVehicleTable(editVehicleIdTextField);
    }

    public void showMainMenu() throws IOException {
        WindowSingleton.getInstance().setLayout("/fxml/StartScreen.fxml");
    }

    public void addInsuranceSetDate() {
        addInsuranceDataWaznosciTextField.setText(String.valueOf(addInsuranceDataWaznosciDatePicker.getValue()));
    }

    public void fillEditedVehicleFields() {
        try {
            long _id = Long.parseLong(editVehicleIdTextField.getText());
        } catch (NumberFormatException e) {
            WindowSingleton.alert("Niepoprawny format");
            return;
        }
        Pojazd pojazd = DBConnector.getInstance().getEntityManager().find(Pojazd.class, Long.parseLong(editVehicleIdTextField.getText()));
        if (pojazd == null) {
            WindowSingleton.alert("Nie ma pojazdu o tym ID");
            return;
        }

        editVehicleMarkaTextField.setText("MARKA - " + pojazd.getMarka());
        editVehicleModelTextField.setText("MODEL - " + pojazd.getModel());
        editVehicleStanPojazduTextField.setText("STAN POJAZDU - " + pojazd.getStan_pojazdu());
        editVehicleUbezpieczenieTextField.setText("UBEZPIECZENIE - " + pojazd.getId_ubezpieczenia());
        editVehicleTypTextField.setText("TYP - " + pojazd.getTyp());
        editVehicleDataWaznosciBadaniaTextField.setText("DATA WAŻNOŚCI BADANIA - " + pojazd.getTermin_waz_badania().toString());
        editVehiclePunktPostojuTextField.setText("PUNKT POSTOJU - " + pojazd.getPunkt_postoju());


    }

    public void addInsurance() {
        if (addInsuranceCenaTextField.getText().equals("") || addInsuranceDataWaznosciTextField.getText().equals("") || addInsuranceTypTextField.getText().equals("")) {
            WindowSingleton.alert("Niepoprawne dane");
            return;
        }

        try {
            float cena = Float.parseFloat(addInsuranceCenaTextField.getText());
        } catch (NumberFormatException e) {
            WindowSingleton.alert("Niepoprawna cena");
            return;
        }

        Ubezpieczenie ubezpieczenie = new Ubezpieczenie();
        ubezpieczenie.setCena(Float.parseFloat(addInsuranceCenaTextField.getText()));
        ubezpieczenie.setData_waznosci(Date.valueOf(addInsuranceDataWaznosciTextField.getText()));
        ubezpieczenie.setTyp(addInsuranceTypTextField.getText());

        DBConnector.getInstance().addInsurance(ubezpieczenie);
        WindowSingleton.alert("Dodano ubezpieczenie");

        addInsuranceCenaTextField.setText("");
        addInsuranceTypTextField.setText("");
        addInsuranceDataWaznosciTextField.setText("");
    }

    public void editInsurance() {
        if (editInsuranceTextField.getText().equals("") || editInsuranceNewCenaTextField.getText().equals("") || editInsuranceNewTypTextField.getText().equals("") || editInsuranceDataWaznosciDatePicker.getValue().toString().equals("")) {
            WindowSingleton.alert("Niepoprawne dane");
            return;
        }

        try {
            long _id = Long.parseLong(editInsuranceTextField.getText());
        } catch (NumberFormatException e) {
            WindowSingleton.alert("Niepoprawny format");
            return;
        }
        Ubezpieczenie ubezpieczenie = DBConnector.getInstance().getEntityManager().find(Ubezpieczenie.class, Long.parseLong(editInsuranceTextField.getText()));

        ubezpieczenie.setTyp(editVehicleNewTypTextField.getText());
        ubezpieczenie.setData_waznosci(Date.valueOf(editInsuranceDataWaznosciDatePicker.getValue().toString()));
        ubezpieczenie.setCena(Float.parseFloat(editInsuranceNewCenaTextField.getText()));

        DBConnector.getInstance().editUbezpieczenie(ubezpieczenie);
        WindowSingleton.alert("Zedytowano ubezpieczenie");
        editInsuranceNewCenaTextField.setText("");
        editInsuranceNewTypTextField.setText("");
        editInsuranceTextField.setText("");
        editInsuranceTypTextField.setText("");
        editInsuranceCenaTextField.setText("");
        editInsuranceDataWaznosciTextField.setText("");
    }

    public void deleteInsurance() {
        if (deleteInsuranceIdTextField.getText().equals("")) {
            WindowSingleton.alert("Niepoprawne ID");
        }
        Ubezpieczenie ubezpieczenie = DBConnector.getInstance().getEntityManager().find(Ubezpieczenie.class, Long.parseLong(deleteInsuranceIdTextField.getText()));
        if (ubezpieczenie == null) {
            WindowSingleton.alert("Nie ma ubezpieczenia o podanym ID");
            return;
        }

        if (DBConnector.getInstance().getEntityManager().createQuery("SELECT a FROM Pojazd a WHERE id_ubezpieczenia='" + deleteInsuranceIdTextField.getText() + "'", Pojazd.class).getResultList().size() > 0) {
            WindowSingleton.alert(" Nie można usunąć. Ubezpieczenie jest przypisane do pojazdu ");
            return;
        }


        DBConnector.getInstance().deleteUbezpieczenie(ubezpieczenie);
        WindowSingleton.alert("Usunięto ubezpieczenie o ID = " + deleteInsuranceIdTextField.getText());
        deleteInsuranceIdTextField.setText("");

    }

    public void showDeleteInsuranceList() {
        WindowSingleton.showInsuranceTable(deleteInsuranceIdTextField);
    }

    public void showEditInsuranceList() {
        WindowSingleton.showInsuranceTable(editInsuranceTextField);
    }

    public void fillEditInsuranceField() {
        try {
            long _id = Long.parseLong(editInsuranceTextField.getText());
        } catch (NumberFormatException e) {
            WindowSingleton.alert("Niepoprawny format");
            return;
        }
        Ubezpieczenie ubezpieczenie = DBConnector.getInstance().getEntityManager().find(Ubezpieczenie.class, Long.parseLong(editInsuranceTextField.getText()));
        if (ubezpieczenie == null) {
            WindowSingleton.alert("Nie ma ubezpieczenia o tym ID");
            return;
        }
        editInsuranceCenaTextField.setText(String.valueOf(ubezpieczenie.getCena()));
        editInsuranceDataWaznosciTextField.setText(String.valueOf(ubezpieczenie.getData_waznosci()));
        editInsuranceTypTextField.setText(ubezpieczenie.getTyp());

    }

    public void deleteCar() {
        try {
            long _id = Long.parseLong(deleteVehicleIdTextField.getText());
        } catch (NumberFormatException e) {
            WindowSingleton.alert("Nie ma pojazdu o tym ID");
            return;
        }
//        DBConnector.getInstance().start();
        Pojazd pojazd = DBConnector.getInstance().getEntityManager().find(Pojazd.class, Long.parseLong(deleteVehicleIdTextField.getText()));
            List<Wypozyczenie> list = DBConnector.getInstance().getEntityManager().createQuery("SELECT a FROM Wypozyczenie a WHERE id_pojazdu='" + deleteVehicleIdTextField.getText() + "'", Wypozyczenie.class).getResultList();

            if (pojazd == null) {
                WindowSingleton.alert("Nie ma takiego pojazdu");
                DBConnector.getInstance().stop();
                return;
            }
            if (list.size() > 0) {
                WindowSingleton.alert("Pojazd jest w trakcie wypożyczenia");
                //DBConnector.getInstance().stop();
                return;
            }

        WindowSingleton.alert("Usunięto pojazd o id = " + deleteVehicleIdTextField.getText());
        DBConnector.getInstance().deletePojazd(pojazd);
//        DBConnector.getInstance().stop();
        deleteVehicleIdTextField.setText("");
    }

    public void showAddCarInsuranceList() {
        WindowSingleton.showInsuranceTable(addVehicleUbezpieczenieTextField);
    }

    public void editVehicle() {
        if (editVehicleMarkaTextField.getText().equals("") || editVehicleNewModelTextField.getText().equals("") || editVehicleNewTypTextField.getText().equals("") || editVehicleNewUbezpieczenieTextField.getText().equals("") || editVehicleNewStanPojazduTextField.getText().equals("") || editVehicleNewDataWaznosciBadaniaDatePicker.getValue().toString().equals("") || editVehicleNewPunktPostojuTextField.getText().equals("")) {
            WindowSingleton.alert("Niepoprawne dane");
            return;
        }

        Pojazd pojazd = DBConnector.getInstance().getEntityManager().find(Pojazd.class, Long.parseLong(editVehicleIdTextField.getText()));
        Ubezpieczenie ubezpieczenie = DBConnector.getInstance().getEntityManager().find(Ubezpieczenie.class, Long.parseLong(editVehicleNewUbezpieczenieTextField.getText()));
        Punkt_Wypozyczen punkt_wypozyczen = DBConnector.getInstance().getEntityManager().find(Punkt_Wypozyczen.class, Long.parseLong(editVehicleNewPunktPostojuTextField.getText()));


        pojazd.setTyp(editVehicleNewTypTextField.getText());
        pojazd.setMarka(editVehicleNewMarkaTextField.getText());
        pojazd.setModel(editVehicleNewModelTextField.getText());
        pojazd.setId_ubezpieczenia(ubezpieczenie);
        pojazd.setStan_pojazdu(editVehicleNewStanPojazduTextField.getText());
        pojazd.setTermin_waz_badania(Date.valueOf(editVehicleNewDataWaznosciBadaniaDatePicker.getValue().toString()));
        pojazd.setPunkt_postoju(punkt_wypozyczen);

        DBConnector.getInstance().editPojazd(pojazd);
        WindowSingleton.alert("Zedytowano pojazd");

    }

    public void showEditVehicleInsuranceList() {
        WindowSingleton.showInsuranceTable(editVehicleNewUbezpieczenieTextField);
    }

    public void editVehicleShowRentalPointList() {
        WindowSingleton.showRentalPointTable(editVehicleNewPunktPostojuTextField);
    }

    public void printVehicleList() {
        final TableView<Pojazd> table = WindowSingleton.createVehicleTable();
        printVehicleStackPane.getChildren().add(table);
    }

    public void printInsuranceList() {
        final TableView<Ubezpieczenie> table = WindowSingleton.createInsuranceTable();
        printInsuranceStackPane.getChildren().add(table);
    }

}
