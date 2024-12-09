package vrrz.pacdam.abstractions;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vrrz.pacdam.engine.abstractions.DatabaseInterface;
import vrrz.pacdam.engine.utils.variables.Score;

public class FirestoreController implements DatabaseInterface {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void fetchUserScores(@NonNull String userId, @NonNull Function1<? super List<Score>, Unit> callback) {

    }

    @Override
    public void createUser(@NonNull String email, @NonNull Function1<? super Boolean, Unit> callback) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("email", email);
        userData.put("created_at", System.currentTimeMillis());
        db.collection("users")
                .add(userData)
                .addOnSuccessListener(documentReference -> {
                    System.out.println(documentReference.toString());
                    callback.invoke(true);
                })
                .addOnFailureListener(e -> {
                    System.err.println("Hubo un error y no se pudo crear el usuario con email " + email);
                    callback.invoke(false);
                });
    }

    @Override
    public void addScore(@NonNull String userId, int score, @NonNull Function0<Unit> callback) {
        Map<String, Object> scoreData = new HashMap<>();
        scoreData.put("score", score);
        scoreData.put("createdAt", System.currentTimeMillis());

        db.collection("users")
                .document(userId)
                .collection("points")
                .add(scoreData)
                .addOnSuccessListener(documentReference ->
                        System.out.println("Score added successfully"))
                .addOnFailureListener(e ->
                        System.err.println("Error adding score: " + e));
    }

    @Override
    public void checkUserExists(@NonNull String email, @NonNull Function1<? super Boolean, Unit> callback) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean exists = !task.getResult().isEmpty();
                        System.out.println("User exists with email: " + email + " -> " + exists);
                        callback.invoke(exists);
                    } else {
                        System.err.println("Error checking user existence: " + task.getException());
                        callback.invoke(false);
                    }
                });
    }
}