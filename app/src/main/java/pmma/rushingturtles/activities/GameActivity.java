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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pmma.rushingturtles.R;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    Button playCardButton;

    ImageView blueTurtle;
    ImageView redTurtle;
    ImageView greenTurtle;
    ImageView yellowTurtle;
    ImageView purpleTurtle;

    ImageView card1;
    ImageView card2;
    ImageView card3;
    ImageView card4;
    ImageView card5;
    List<ImageView> cards;
    float cardCoordinateX;
    float cardMovedCoordinateX;
    int statusBarHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initializeXmlViews();
        initializeTurtles();
        initializeCardImageViews();
    }

    private void initializeXmlViews() {
        playCardButton = findViewById(R.id.buttonPlayCardOnDeck);
        playCardButton.setOnClickListener(playCardButtonOnClickListener);
    }

    private void initializeTurtles() {
        blueTurtle = findViewById(R.id.imageViewTurtleBlue);
        redTurtle = findViewById(R.id.imageViewTurtleRed);
        greenTurtle = findViewById(R.id.imageViewTurtleGreen);
        yellowTurtle = findViewById(R.id.imageViewTurtleYellow);
        purpleTurtle = findViewById(R.id.imageViewTurtlePurple);
    }

    private void initializeCardImageViews() {
        card1 = findViewById(R.id.imageViewCard1);
        card2 = findViewById(R.id.imageViewCard2);
        card3 = findViewById(R.id.imageViewCard3);
        card4 = findViewById(R.id.imageViewCard4);
        card5 = findViewById(R.id.imageViewCard5);

        cards = Arrays.asList(card1, card2, card3, card4, card5);
        addCardImageViewsOnListeners();
    }

    private void addCardImageViewsOnListeners() {
        for (int i=0; i<cards.size(); i++)
            cards.get(i).setOnClickListener(this);
    }

    private void setCardCoordinates(float windowPercentage) {
        statusBarHeight = getStatusBarHeight();
        int windowWidth = getWindowDisplayMetrics().widthPixels;
        int imgX = getImageViewCoordinates(card1)[0];
        float movementValueX = windowWidth * windowPercentage;

        cardCoordinateX = (float) imgX;
        cardMovedCoordinateX = imgX - movementValueX;
    }

    private View.OnClickListener playCardButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(GameActivity.this, "Yeeey :)", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View view) {
        if (statusBarHeight == 0)
            setCardCoordinates(0.3f);

        /* If we want to move card to the left, we need to move other cards to the right */
        if (!isCardOnTheLeftSide(view)) {
            int cardIdx = cards.indexOf((ImageView) view);
            for (int i = 0; i < cards.size(); i++)
                if (i != cardIdx && isCardOnTheLeftSide(cards.get(i)))
                    moveCard(cards.get(i));
        }
        /* Move our card to the left :) */
        moveCard(view);
    }

    public void moveCard(View view) {
        int[] coordinates = getImageViewCoordinates(view);
        int imgX = coordinates[0];
        int imgY = coordinates[1];
        float newImgX = calculateNewCoordinateX(imgX);

        if (newImgX >= 0)
            createAndAnimatePath(view, imgX, imgY, newImgX);
    }

    private boolean isCardOnTheLeftSide(View view) {
        int cardImgX = getImageViewCoordinates(view)[0];
        return calculateNewCoordinateX(cardImgX) == cardCoordinateX;
    }

    private int getStatusBarHeight() {
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }

    private DisplayMetrics getWindowDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    private int[] getImageViewCoordinates(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int[] coordinates = new int[2];
        coordinates[0] = rect.left;
        coordinates[1] = rect.top - statusBarHeight;
        return coordinates;
    }

    private float calculateNewCoordinateX(int imgX) {
        float x = -1;
        if (imgX == cardMovedCoordinateX)
            x = cardCoordinateX;
        if (imgX == cardCoordinateX)
            x = cardMovedCoordinateX;
        return x;
    }

    private void createAndAnimatePath(View view, float imgX, float imgY, float newImgX) {
        Path path = new Path();
        path.moveTo(imgX, imgY);
        path.lineTo(newImgX, imgY);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        animator.setDuration(500);
        animator.start();
    }


}
