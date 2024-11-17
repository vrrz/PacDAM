// android/src/vrrz/pacdam/abstractions/FirestoreController.java
package vrrz.pacdam.abstractions;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import vrrz.pacdam.engine.abstractions.DatabaseInterface;
import vrrz.pacdam.engine.utils.variables.Pair;

public class FirestoreController implements DatabaseInterface {
    private final FirebaseFirestore db;

    public FirestoreController() {
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void saveScore(String playerName, int score) {
        // Crear un documento para guardar la puntuación en Firestore
        Map<String, Object> scoreData = new HashMap<>();
        scoreData.put("name", playerName);
        scoreData.put("score", score);

        db.collection("scores")
                .add(scoreData)
                .addOnSuccessListener(documentReference ->
                        System.out.println("Puntuación guardada exitosamente.")
                )
                .addOnFailureListener(e ->
                        System.err.println("Error al guardar la puntuación: " + e.getMessage())
                );
    }

    @Override
    public void getTopScores(@NonNull final ScoreCallback callback) {
        db.collection("scores")
                .orderBy("score", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Pair<String, Integer>> topScores = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String name = document.getString("name");
                            Integer score = Objects.requireNonNull(document.getLong("score")).intValue();
                            topScores.add(new Pair<>(name, score));
                        }
                        callback.onCallback(topScores);
                    } else {
                        System.err.println("Error al obtener las puntuaciones: " + task.getException());
                    }
                });
    }
}
