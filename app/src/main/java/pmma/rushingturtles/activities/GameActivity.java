package pmma.rushingturtles.activities;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Arrays;
import java.util.List;

import pmma.rushingturtles.R;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    ConstraintLayout mainLayout;
    PopupWindow popupWindowWinner;
    Button playCardButton, closePopupButton;

    ImageView blueTurtle, redTurtle, greenTurtle, yellowTurtle, purpleTurtle;

    ImageView blueColorPicker, redColorPicker, greenColorPicker, yellowColorPicker, purpleColorPicker;
    List<ImageView> colorPickers;
    ImageView blueColorTick, redColorTick, greenColorTick, yellowColorTick, purpleColorTick;
    List<ImageView> colorTicks;
    ImageView checkedColorPicker;

    ImageView card1, card2, card3, card4, card5;
    List<ImageView> cards;

    List<Integer> cardCoordinatesY;
    float cardCoordinateXOrY;
    float cardMovedCoordinateXOrY;
    int statusBarHeight;

    int currentOrientation;
    boolean cardCoordinatesHaveBeenSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mainLayout = findViewById(R.id.gameLayout);

        currentOrientation = getResources().getConfiguration().orientation;
        cardCoordinatesHaveBeenSet = false;

        initializeXmlViews();
        initializeTurtles();
        initializeCardImageViews();
        initializeColorPickerViews();

        Log.i("GameActivity", String.valueOf(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT));
    }

    private void initializeColorPickerViews() {
        blueColorPicker = findViewById(R.id.imageViewColorPickerBlue);
        redColorPicker = findViewById(R.id.imageViewColorPickerRed);
        greenColorPicker = findViewById(R.id.imageViewColorPickerGreen);
        yellowColorPicker = findViewById(R.id.imageViewColorPickerYellow);
        purpleColorPicker = findViewById(R.id.imageViewColorPickerPurple);
        colorPickers = Arrays.asList(blueColorPicker, redColorPicker, greenColorPicker, yellowColorPicker, purpleColorPicker);
        checkedColorPicker = null;
        for (int i=0; i<colorPickers.size(); i++)
            colorPickers.get(i).setOnClickListener(colorPickerClickListener);

        blueColorTick = findViewById(R.id.imageViewColorPickerBlueTick);
        redColorTick = findViewById(R.id.imageViewColorPickerRedTick);
        greenColorTick = findViewById(R.id.imageViewColorPickerGreenTick);
        yellowColorTick = findViewById(R.id.imageViewColorPickerYellowTick);
        purpleColorTick = findViewById(R.id.imageViewColorPickerPurpleTick);
        colorTicks = Arrays.asList(blueColorTick, redColorTick, greenColorTick, yellowColorTick, purpleColorTick);

        for (int i=0; i<colorTicks.size(); i++)
            colorTicks.get(i).setVisibility(View.GONE);
    }

    private View.OnClickListener colorPickerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkOrUncheckColorPicker(v);
        }
    };

    public void checkOrUncheckColorPicker(View view) {
        int touchedColorPickerIdx = colorPickers.indexOf(view);
        if (checkedColorPicker == null) {
            colorTicks.get(touchedColorPickerIdx).setVisibility(View.VISIBLE);
            checkedColorPicker = (ImageView) view;
        }
        else {
            if (checkedColorPicker == view) {
                colorTicks.get(touchedColorPickerIdx).setVisibility(View.GONE);
                checkedColorPicker = null;
            }
            else {
                colorTicks.get(colorPickers.indexOf(checkedColorPicker)).setVisibility(View.GONE);
                colorTicks.get(touchedColorPickerIdx).setVisibility(View.VISIBLE);
                checkedColorPicker = (ImageView) view;
            }
        }
    }

    private void initializeXmlViews() {
        playCardButton = findViewById(R.id.buttonPlayCardOnDeck);
//        playCardButton.setVisibility(View.INVISIBLE);
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
        DisplayMetrics displayMetrics = getWindowDisplayMetrics();
        float windowSize = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? displayMetrics.widthPixels : displayMetrics.heightPixels;
        float imgXOrY = getImageViewCoordinates(card1)[currentOrientation == Configuration.ORIENTATION_PORTRAIT ? 0 : 1];
        float movementValue = windowSize * windowPercentage;

        cardCoordinateXOrY = imgXOrY;
        cardMovedCoordinateXOrY = imgXOrY - movementValue;
    }

    private View.OnClickListener playCardButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            manageWinnerPopupWindow(v);
        }
    };

    private void manageWinnerPopupWindow(View view) {
        //instantiate the popup.xml layout file
        LayoutInflater layoutInflater = (LayoutInflater) GameActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_winner,null);

        closePopupButton = (Button) customView.findViewById(R.id.closePopupBtn);

        //instantiate popup window
        popupWindowWinner = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //display the popup window
        popupWindowWinner.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);

        //close the popup window on button click
        closePopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowWinner.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (!cardCoordinatesHaveBeenSet) {
            setCardCoordinates(currentOrientation == Configuration.ORIENTATION_PORTRAIT ? 0.3f : 0.2f);
            cardCoordinatesHaveBeenSet = true;
        }

        /* If we want to move card to the left, we need to move other cards to the right */
        if (!isCardOutside(view)) {
            int cardIdx = cards.indexOf((ImageView) view);
            for (int i = 0; i < cards.size(); i++)
                if (i != cardIdx && isCardOutside(cards.get(i)))
                    moveCard(cards.get(i));
        }
        /* Move our card to the left :) */
        moveCard(view);
    }

    public void moveCard(View view) {
        int[] coordinates = getImageViewCoordinates(view);
        int imgX = coordinates[0];
        int imgY = coordinates[1];

        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            float newImgX = calculateNewCoordinateXOrY(imgX);
            if (newImgX >= 0)
                createAndAnimatePath(view, imgX, imgY, newImgX, imgY);
        } else {
            float newImgY = calculateNewCoordinateXOrY(imgY);
            if (newImgY >= 0)
                createAndAnimatePath(view, imgX, imgY, imgX, newImgY);
        }
    }

    private boolean isCardOutside(View view) {
        int cardImgXOrY = getImageViewCoordinates(view)[currentOrientation == Configuration.ORIENTATION_PORTRAIT ? 0 : 1];
        return calculateNewCoordinateXOrY(cardImgXOrY) == cardCoordinateXOrY;
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

    private float calculateNewCoordinateXOrY(int imgXOrY) {
        float xOrY = -1;
        if (imgXOrY-1 < cardMovedCoordinateXOrY && cardMovedCoordinateXOrY < imgXOrY+1)
            xOrY = cardCoordinateXOrY;
        if (imgXOrY-1 < cardCoordinateXOrY && cardCoordinateXOrY < imgXOrY+1)
            xOrY = cardMovedCoordinateXOrY;
        return xOrY;
    }

    private void createAndAnimatePath(View view, float imgX, float imgY, float newImgX, float newImgY) {
        Path path = new Path();
        path.moveTo(imgX, imgY);
        path.lineTo(newImgX, newImgY);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        animator.setDuration(500);
        animator.start();
    }

}
