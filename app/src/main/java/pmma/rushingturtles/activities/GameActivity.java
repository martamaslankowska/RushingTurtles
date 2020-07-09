package pmma.rushingturtles.activities;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pmma.rushingturtles.R;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView card1;
    ImageView card2;
    ImageView card3;
    ImageView card4;
    ImageView card5;
    List<ImageView> cards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initializeCardImageViews();
        addCardImageViewsOnListeners();
    }

    private void initializeCardImageViews() {
        card1 = findViewById(R.id.imageViewCard1);
        card2 = findViewById(R.id.imageViewCard2);
        card3 = findViewById(R.id.imageViewCard3);
        card4 = findViewById(R.id.imageViewCard4);
        card5 = findViewById(R.id.imageViewCard5);

        cards = Arrays.asList(card1, card2, card3, card4, card5);
    }

    private void addCardImageViewsOnListeners() {
        for (int i=0; i<cards.size(); i++)
            cards.get(i).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        /* If we want to move card to the left, we need to move other cards to the right */
        if (movementCondition(view)) {
            int cardIdx = cards.indexOf((ImageView) view);
            for (int i = 0; i < cards.size(); i++) {
                if (i != cardIdx && !movementCondition(cards.get(i))) {
                    moveCard(cards.get(i));
                }
            }
        }
        /* Move our card to the left :) */
        moveCard(view);
    }

    public void moveCard(View view) {
        int statusBarHeight = getStatusBarHeight();
        int windowWidth = getWindowDisplayMetrics().widthPixels;

        int[] coordinates = getImageViewCoordinates(view, statusBarHeight);
        int imgX = coordinates[0];
        int imgY = coordinates[1];

        float movementValueX = windowWidth*0.3f;
        float movementX = calculateMovement(imgX, windowWidth, movementValueX);
        createAndAnimatePath(view, imgX, imgY, movementX);
    }

    private int getStatusBarHeight() {
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;
        return statusBarHeight;
    }

    private DisplayMetrics getWindowDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    private int[] getImageViewCoordinates(View view, int statusBarHeight) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int[] coordinates = new int[2];
        coordinates[0] = rect.left;
        coordinates[1] = rect.top - statusBarHeight;
        return coordinates;
    }

    private boolean movementCondition(int imgX, int windowWidth) {
        return imgX > windowWidth/2;
    }

    private boolean movementCondition(View view) {
        int statusBarHeight = getStatusBarHeight();
        int windowWidth = getWindowDisplayMetrics().widthPixels;
        int[] coordinates = getImageViewCoordinates(view, statusBarHeight);
        int imgX = coordinates[0];
        return imgX > windowWidth/2;
    }

    private float calculateMovement(int imgX, int windowWidth, float movementValueX) {
        float movementX = movementCondition(imgX, windowWidth) ? - movementValueX : movementValueX;
        return movementX;
    }

    private void createAndAnimatePath(View view, int imgX, int imgY, float movementX) {
        Path path = new Path();
        path.moveTo(imgX, imgY);
        path.lineTo(movementX + imgX, imgY);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        animator.setDuration(500);
        animator.start();
    }


}
