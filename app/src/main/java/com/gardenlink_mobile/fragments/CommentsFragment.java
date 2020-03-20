package com.gardenlink_mobile.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.gardenlink_mobile.R;
import com.gardenlink_mobile.adapters.CommentAdapter;
import com.gardenlink_mobile.entities.Score;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.session.Session;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends Fragment {

    public static final char RESPONSE_TAG='@';
    private static final String COMMENTS_TITLE = "Commentaires";
    private List<Score> mComments;
    private View mView;
    public CommentsFragment() {

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

    //TODO : mock datas, replace with real call
    private void loadComments() {
        Score comment1 = new Score();
        Score comment2= new Score();
        Score comment3= new Score();
        User pouglou = new User();
        pouglou.setFirstName("Pouglou");
        comment1.setRater(pouglou);
        User issou = new User();
        User Denis = new User();
        issou.setFirstName("Issou");
        Denis.setFirstName("Denis");
        comment2.setRater(issou);
        comment3.setRater(Denis);
        comment1.setComment("Très beau Jardin ! ");
        comment2.setComment("@Pouglou Merci beaucoup ! jojoefijoiezfjiezfjoizejfoizefjoiezfjofiezjoiefzjoiezfjofezjefzfjeefjizjfieiefjefzofjzeiofjDENIS dozjdijzidjoiadjoazaoidja BRAVO");
        comment3.setComment("Bonjour je suis denis ");
        mComments.add(comment1);
        mComments.add(comment2);
        mComments.add(comment3);
    }

    private void displayList()
    {
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


    private void initFields()
    {
        ((TextView)mView.findViewById(R.id.commentsTitle)).setText(COMMENTS_TITLE+"("+mComments.size()+")");
    }

    public void newComment(View view) {
        final EditText lMessage = new EditText(getContext());


        new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialogTheme)
                .setTitle("Nouveau commentaire")
                .setView(lMessage)
                .setPositiveButton("Valider", (dialog, which) -> {
                    Score lNewScore = new Score();
                    lNewScore.setComment(lMessage.getText().toString());
                    lNewScore.setRater(Session.getInstance().getCurrentUser());
                    mComments.add(lNewScore);
                    displayList();
                    //TODO : send real comment to the back
                })
                .setNegativeButton("Annuler", (dialog, which) -> {
                }).show();
    }


    public void answerComment(View view) {
        final EditText lMessage = new EditText(getContext());
        View lParent = (View)view.getParent();
        String lUsername = ((TextView)lParent.findViewById(R.id.commentUserName)).getText().toString();

        new MaterialAlertDialogBuilder(getContext(), R.style.AlertDialogTheme)
                .setTitle("Répondre au commentaire")
                .setView(lMessage)
                .setPositiveButton("Valider", (dialog, which) -> {
                    Score lNewScore = new Score();
                    lNewScore.setComment(RESPONSE_TAG+lUsername+" "+lMessage.getText().toString());
                    lNewScore.setRater(Session.getInstance().getCurrentUser());
                    mComments.add(lNewScore);
                    displayList();
                    //TODO : send real comment to the back
                })
                .setNegativeButton("Annuler", (dialog, which) -> {
                }).show();
    }
}
