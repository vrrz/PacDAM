package vrrz.pacdam.abstractions;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vrrz.pacdam.engine.abstractions.DatabaseInterface;
import vrrz.pacdam.engine.utils.variables.Score;

public class FirestoreController implements DatabaseInterface {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void createUser(String email, String passwordHash, String username) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("passwordHash", passwordHash);
        userData.put("username", username);
        userData.put("createdAt", System.currentTimeMillis());

        db.collection("Usuarios")
                .add(userData)
                .addOnSuccessListener(documentReference ->
                        System.out.println("User created with ID: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        System.err.println("Error creating user: " + e));
    }

    @Override
    public void addScore(String userId, int levelId, int score) {
        Map<String, Object> scoreData = new HashMap<>();
        scoreData.put("levelId", levelId);
        scoreData.put("score", score);
        scoreData.put("createdAt", System.currentTimeMillis());

        db.collection("Usuarios")
                .document(userId)
                .collection("Puntuaciones")
                .add(scoreData)
                .addOnSuccessListener(documentReference ->
                        System.out.println("Score added successfully"))
                .addOnFailureListener(e ->
                        System.err.println("Error adding score: " + e));
    }

    @Override
    public void fetchUserScores(@NonNull String userId, @NonNull Function1<? super List<Score>, Unit> callback) {
        db.collection("Usuarios")
                .document(userId)
                .collection("Puntuaciones")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Score> scores = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        int levelId = document.getLong("levelId").intValue();
                        int score = document.getLong("score").intValue();
                        long createdAt = document.getLong("createdAt");
                        scores.add(new Score(levelId, score, createdAt));
                    }
                    callback.invoke(scores);
                })
                .addOnFailureListener(e -> {
                    callback.invoke(new ArrayList<>()); // Devuelve lista vacía en caso de error
                });
    }

    /*

    dbController.fetchUserScores("userId123", new Function1<List<Score>, Unit>() {
    @Override
    public Unit invoke(List<Score> scores) {
            for (Score score : scores) {
                System.out.println("Level: " + score.getLevelId() + ", Score: " + score.getScore());
            }
            return Unit.INSTANCE;
        }
    });

     */
}