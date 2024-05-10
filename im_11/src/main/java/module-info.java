module ru.vorotov.simulationslab11 {
    requires javafx.controls;
    requires javafx.fxml;
    requires commons.math3;


    opens ru.vorotov.simulationslab11 to javafx.fxml;
    exports ru.vorotov.simulationslab11;
    exports ru.vorotov.simulationslab11.NormalDist;
    opens ru.vorotov.simulationslab11.NormalDist to javafx.fxml;
}