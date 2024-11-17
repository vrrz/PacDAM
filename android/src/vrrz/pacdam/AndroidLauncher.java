package vrrz.pacdam;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
// Las 2 sigueintes me fallan
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new PacDamMain(), config);
		System.loadLibrary("gdx");

		// Inicializar Firebase
		FirebaseApp.initializeApp(this);

		// Prueba de inicialización de Firestore
		FirebaseFirestore db = FirebaseFirestore.getInstance();
	}
}
