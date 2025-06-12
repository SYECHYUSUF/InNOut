module innout {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;  // Menambahkan Gson

    opens innout.controller to javafx.fxml; // Mengizinkan package controller diakses oleh JavaFX
    opens innout.model to com.google.gson; // Mengizinkan model untuk diakses oleh Gson (jika perlu)
    opens innout to javafx.fxml; // Membuka package innout untuk JavaFX

    exports innout; // Mengekspor package innout
}
