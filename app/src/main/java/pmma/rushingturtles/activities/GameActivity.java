package pmma.rushingturtles.activities;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Handler;

import pmma.rushingturtles.R;
import pmma.rushingturtles.activityviewcontrollers.ColorPickerViewController;
import pmma.rushingturtles.controllers.GameActivityController;
import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.websocket.WSC;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    ConstraintLayout mainLayout;
    PopupWindow popupWindowWinner;
    Button playCardButton, closePopupButton;

    ImageView blueTurtle, redTurtle, greenTurtle, yellowTurtle, purpleTurtle;

    TextView currentPlayerText, currentPlayerName, nextPlayerName;

    ImageView card1, card2, card3, card4, card5;
    List<ImageView> cards;
    ImageView outsideCard;

    float cardCoordinateXOrY;
    float cardMovedCoordinateXOrY;
    int statusBarHeight;

    int currentOrientation;
    boolean cardCoordinatesHaveBeenSet;

    public GameActivityController gameActivityController;
    ColorPickerViewController colorPickerViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mainLayout = findViewById(R.id.gameLayout);
        gameActivityController = GameActivityController.getInstance();
        WSC.getInstance().setGameController(gameActivityController);

        currentOrientation = getResources().getConfiguration().orientation;
        cardCoordinatesHaveBeenSet = false;

        initializeXmlViews();
        initializeTurtles();
        initializeCardImageViews();
        colorPickerViewController = new ColorPickerViewController(this, currentOrientation);

        Log.i("GameActivity", String.valueOf(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT));
    }

    private void initializeXmlViews() {
        playCardButton = findViewById(R.id.buttonPlayCardOnDeck);
        playCardButton.setVisibility(View.GONE);
        playCardButton.setOnClickListener(playCardButtonOnClickListener);

        currentPlayerText = findViewById(R.id.textViewCurrentPlayerText);
        currentPlayerName = findViewById(R.id.textViewCurrentPlayerName);
        nextPlayerName = findViewById(R.id.textViewNextPlayerName);
    }

    private View.OnClickListener playCardButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pickedCardIdx = cards.indexOf(outsideCard);
            if (isPickedCardRainbow(pickedCardIdx)) {
                if (!colorPickerViewController.isColorPickerVisible()) {
                    setActivenessOfPlayCardButton(false);
                    colorPickerViewController.animatePathsForColorPickers(true);
                } else {
                    if (colorPickerViewController.getCheckedColorPicker() != null) {
                        sendPlayCardMessageToServer(pickedCardIdx, colorPickerViewController.getCheckedColor());
                        colorPickerViewController.animatePathsForColorPickers(false);
                    }
                }
            } else {
                sendPlayCardMessageToServer(pickedCardIdx, null);
            }
        }
    };

    private void sendPlayCardMessageToServer(int pickedCardIdx, TurtleColor cardColor) {
        WSC.getInstance().sendPlayCardMessage(pickedCardIdx, cardColor);
        playCardButton.setVisibility(View.GONE);
        moveCard(outsideCard, false);
        outsideCard = null;
    }

    public void setActivenessOfPlayCardButton(boolean isActive) {
        if (isActive) {
            playCardButton.setBackgroundColor(getResources().getColor(R.color.buttonActiveRed));
            playCardButton.setEnabled(true);
        } else {
            playCardButton.setBackgroundColor(getResources().getColor(R.color.buttonInactiveGray));
            playCardButton.setEnabled(false);
        }
    }

    private void manageWinnerPopupWindow(View view) {
        //instantiate the popup.xml layout file
        LayoutInflater layoutInflater = (LayoutInflater) GameActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_winner, null);

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
        outsideCard = null;

        cards = Arrays.asList(card1, card2, card3, card4, card5);
        addCardImageViewsOnListeners();
    }

    private void addCardImageViewsOnListeners() {
        for (int i = 0; i < cards.size(); i++)
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

    @Override
    public void onClick(View view) {
        if (!cardCoordinatesHaveBeenSet) {
            setCardCoordinates(currentOrientation == Configuration.ORIENTATION_PORTRAIT ? 0.3f : 0.23f);
            cardCoordinatesHaveBeenSet = true;
        }

        if (outsideCard != view) {  /* If clicked card is not outside */
            if (outsideCard != null)
                moveCard(outsideCard, false);
            moveCard(view, true);
            outsideCard = (ImageView) view;
        } else {  /* Clicked card is outside */
            moveCard(view, false);
            outsideCard = null;
        }

        if (outsideCard != null) {
            if (gameActivityController.getCardColor(cards.indexOf(outsideCard)) == TurtleColor.RAINBOW)
                playCardButton.setText(getResources().getString(R.string.pick_color_card));
            else
                playCardButton.setText(getResources().getString(R.string.play_card_on_deck));
        }

        if (gameActivityController.isPlayerAnActivePlayer()) {
            setActivePlayerViews();
            if (colorPickerViewController.isColorPickerVisible()) {
                colorPickerViewController.animatePathsForColorPickers(false);
            }
            if (outsideCard != null)
                setActivenessOfPlayCardButton(true);
        }
    }

    public void moveCard(View view, boolean moveOutside) {
        int[] coordinates = getImageViewCoordinates(view);
        int imgX = coordinates[0];
        int imgY = coordinates[1];
        float newImgCoordinate = moveOutside ? cardMovedCoordinateXOrY : cardCoordinateXOrY;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT)
            createAndAnimatePath(view, imgX, imgY, newImgCoordinate, imgY);
        else
            createAndAnimatePath(view, imgX, imgY, imgX, newImgCoordinate);
    }

    public int getStatusBarHeight() {
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
        if (imgXOrY - 1 < cardMovedCoordinateXOrY && cardMovedCoordinateXOrY < imgXOrY + 1)
            xOrY = cardCoordinateXOrY;
        if (imgXOrY - 1 < cardCoordinateXOrY && cardCoordinateXOrY < imgXOrY + 1)
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


    /* WEB SOCKET CLIENT METHODS */

    public void setActivePlayerViews() {
        currentPlayerText.setText(getResources().getString(R.string.your_turn));
        currentPlayerName.setText(gameActivityController.getCurrentPlayerName());
        nextPlayerName.setText(gameActivityController.getNextPlayerName());

        playCardButton.setVisibility(View.VISIBLE);
        playCardButton.setEnabled(false);
        playCardButton.setBackgroundColor(getResources().getColor(R.color.buttonInactiveGray));
    }

    private boolean isPickedCardRainbow(int cardIdx) {
        return gameActivityController.getCardColor(cardIdx) == TurtleColor.RAINBOW;
    }
}
