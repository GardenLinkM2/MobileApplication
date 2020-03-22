package com.gardenlink_mobile.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.adapters.CommentAdapter;
import com.gardenlink_mobile.entities.Score;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.utils.ImageMaster;
import com.gardenlink_mobile.wsconnecting.operations.GET_GARDEN_SCORES;
import com.gardenlink_mobile.wsconnecting.operations.GET_PHOTO;
import com.gardenlink_mobile.wsconnecting.operations.GET_USER;
import com.gardenlink_mobile.wsconnecting.operations.Operation;
import com.gardenlink_mobile.wsconnecting.operations.POST_SCORE;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentsFragment extends Fragment implements IWebConnectable {

    private static final String TAG = "CommentsFragment";
    public static final char RESPONSE_TAG = '@';
    private static final String COMMENTS_TITLE = "Commentaires";
    private List<Score> mComments;
    private HashMap<String, User> users = new HashMap<>();
    private HashMap<String, List<Score>> comments = new HashMap<>();
    private View mView;
    private String gardenId;
    private int usersToGet;

    public CommentsFragment(String gardenId) {
        this.gardenId = gardenId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comments_fragment, container, false);
        mView = view;
        mComments = new ArrayList<Score>();
        loadComments();
        displayList();
        return view;
    }

    private void loadComments() {
        new GET_GARDEN_SCORES(gardenId).perform(new WeakReference<>(this));
    }

    private void displayList() {
        CommentAdapter lAdapter = new CommentAdapter(getContext(), mComments, getResources().getColor(R.color.colorGreen_snackbar));
        ListView list = mView.findViewById(R.id.commentsList);
        list.setAdapter(lAdapter);
//        list.addOnLayoutChangeListener((v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
//            final int displayedHeight = bottom - top;
//            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) list.getLayoutParams();
//            lp.height = displayedHeight * 3;
//            list.setLayoutParams(lp);
//        });
        initFields();
    }


    private void initFields() {
        ((TextView) mView.findViewById(R.id.commentsTitle)).setText(COMMENTS_TITLE + "(" + mComments.size() + ")");
    }

    public void newComment(View view) {
        final EditText lMessage = new EditText(getContext());

        new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialogTheme)
                .setTitle("Nouveau commentaire")
                .setView(lMessage)
                .setPositiveButton("Valider", (dialog, which) -> {
                    Score lNewScore = new Score();
                    lNewScore.setComment(lMessage.getText().toString());
                    lNewScore.setRater(Session.getInstance().getCurrentUser().getId());
                    lNewScore.setRated(gardenId);
                    new POST_SCORE(lNewScore).perform(new WeakReference<>(this));
                })
                .setNegativeButton("Annuler", (dialog, which) -> {
                }).show();
    }


    public void answerComment(View view) {
        final EditText lMessage = new EditText(getContext());
        View lParent = (View) view.getParent();
        String lUsername = ((TextView) lParent.findViewById(R.id.commentUserName)).getText().toString();

        new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialogTheme)
                .setTitle("Répondre au commentaire")
                .setView(lMessage)
                .setPositiveButton("Valider", (dialog, which) -> {
                    Score lNewScore = new Score();
                    lNewScore.setComment(RESPONSE_TAG + lUsername + " " + lMessage.getText().toString());
                    lNewScore.setRater(Session.getInstance().getCurrentUser().getId());
                    lNewScore.setRated(gardenId);
                    new POST_SCORE(lNewScore).perform(new WeakReference<>(this));
                })
                .setNegativeButton("Annuler", (dialog, which) -> {
                }).show();
    }

    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        switch (operation.getName()) {
            case "GET_GARDEN_SCORES":
                switch (responseCode) {
                    case 200:
                        if (results == null) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            return;
                        }
                        List<Score> scores = (List<Score>) results;
                        usersToGet = scores.size();
                        for (Score s : scores) {
                            if (comments.get(s.getRater()) == null) {
                                comments.put(s.getRater(), new ArrayList<>());
                                comments.get(s.getRater()).add(s);
                                new GET_USER(s.getRater()).perform(new WeakReference<>(this));
                            } else{
                                decrementUsersToGet();
                                comments.get(s.getRater()).add(s);
                            }
                        }
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }
            case "GET_USER":
                switch (responseCode) {
                    case 200:
                        if (results == null || results.isEmpty()) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            return;
                        }
                        User user = (User) results.get(0);
                        if (user.getPhoto() == null || user.getPhoto().isEmpty()){
                            users.put(user.getId(), user);
                            decrementUsersToGet();
                            return;
                        }
                        String photoUrl = user.getPhoto();
                        users.put(photoUrl, user);
                        new GET_PHOTO(photoUrl).perform(new WeakReference<>(this));
                        return;
                }
            case "POST_SCORE":
                switch (responseCode) {
                    case 201:
                        if (results == null || results.isEmpty()) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            return;
                        }
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Score score = (Score) results.get(0);
                        score.setRatingUser(Session.getInstance().getCurrentUser());
                        score.getRatingUser().setDrawablePhoto(Session.getInstance().getAvatarDrawable());
                        mComments.add(score);
                        displayList();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        Snackbar snackbar = Snackbar.make(getView(), "Impossible d'envoyer le commentaire. Veuillez réessayer.", Snackbar.LENGTH_LONG);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.show();
                        return;
                }
            default:
                Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
                return;
        }
    }

    @Override
    public void receiveResults(int responseCode, HashMap<String, String> results, Operation operation) {
        switch (operation.getName()) {
            case "GET_PHOTO":
                switch (responseCode) {
                    case 200:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        User user = users.get(results.get("url"));
                        user.setDrawablePhoto(ImageMaster.byteStringToDrawable(results.get("photo")));
                        decrementUsersToGet();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        decrementUsersToGet();
                        return;
                }
            default:
                Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
                return;
        }
    }

    @Override
    public void receiveResults(int responseCode, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
        return;
    }

    private void decrementUsersToGet() {
        usersToGet -= 1;
        if (usersToGet == 0) {
            users.forEach((k, v) -> {
                for(Score s : comments.get(v.getId())) {
                    s.setRatingUser(v);
                    mComments.add(s);

                }
            });
            displayList();
        }
    }
}
