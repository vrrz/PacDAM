package vrrz.pacdam;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
// Las 2 sigueintes me fallan
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

import vrrz.pacdam.abstractions.FirestoreController;

public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.loadLibrary("gdx");
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        // Inicialziar Firebase
        FirebaseApp.initializeApp(this);
        // Instanciar abstracción controlador base de datos, en este caso, Firestore
        FirestoreController dbController = new FirestoreController();
        initialize(new PacDamMain(dbController), config);
    }
}
