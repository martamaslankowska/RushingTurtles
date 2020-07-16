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

import pmma.rushingturtles.R;
import pmma.rushingturtles.activityviewcontrollers.BoardViewController;
import pmma.rushingturtles.activityviewcontrollers.CardDeckViewController;
import pmma.rushingturtles.activityviewcontrollers.ColorPickerViewController;
import pmma.rushingturtles.controllers.GameActivityController;
import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.objects.Card;
import pmma.rushingturtles.websocket.WSC;

public class GameActivity extends AppCompatActivity {
    ConstraintLayout mainLayout;
    PopupWindow popupWindowWinner;
    Button playCardButton, closePopupButton;
    ImageView turtleColorTile;

    TextView currentPlayerText, currentPlayerName, nextPlayerName;
    ImageView turtleYellow;

    int statusBarHeight;
    int currentOrientation;

    public GameActivityController gameActivityController;
    public ColorPickerViewController colorPickerViewController;
    public CardDeckViewController cardDeckViewController;
    public BoardViewController boardViewController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mainLayout = findViewById(R.id.gameLayout);
        gameActivityController = GameActivityController.getInstance();
        WSC.getInstance().setGameController(gameActivityController);

        currentOrientation = getResources().getConfiguration().orientation;
        statusBarHeight = getStatusBarHeight();

        initializeXmlViews();
        colorPickerViewController = new ColorPickerViewController(this, currentOrientation);
        cardDeckViewController = new CardDeckViewController(this, currentOrientation);
        boardViewController = new BoardViewController(this, currentOrientation);

        cardDeckViewController.updateCardImages(gameActivityController.game.getMyPlayer().getCards());
        setTurtleTileColor();

//        turtleYellow = findViewById(R.id.imageViewTurtleYellow);
//        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(200, 300);
//        params.leftMargin = 400;
//        params.topMargin = 900;
//        turtleYellow.setLayoutParams(params);

        Log.i("GameActivity", String.valueOf(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT));
    }

    private void initializeXmlViews() {
        playCardButton = findViewById(R.id.buttonPlayCardOnDeck);
        playCardButton.setVisibility(View.GONE);
        playCardButton.setOnClickListener(playCardButtonOnClickListener);

        currentPlayerText = findViewById(R.id.textViewCurrentPlayerText);
        currentPlayerName = findViewById(R.id.textViewCurrentPlayerName);
        nextPlayerName = findViewById(R.id.textViewNextPlayerName);

        turtleColorTile = findViewById(R.id.imageViewTurtleColor);
    }

    private View.OnClickListener playCardButtonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int pickedCardIdx = cardDeckViewController.getCardsFromDeck().indexOf(cardDeckViewController.getOutsideCard());
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
        cardDeckViewController.moveCard(cardDeckViewController.getOutsideCard(), false);
        cardDeckViewController.setOutsideCard(null);
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

    public int getStatusBarHeight() {
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }

    public DisplayMetrics getWindowDisplayMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public int[] getImageViewCoordinates(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int[] coordinates = new int[2];
        coordinates[0] = rect.left;
        coordinates[1] = rect.top;
        return coordinates;
    }

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

    private void setTurtleTileColor() {
        String turtleColor = gameActivityController.game.getMyPlayer().getTurtle().toString().toLowerCase();
        String resourceName = "turtle_color_card_" + turtleColor;
        int resourceId = getResources().getIdentifier(resourceName, "drawable", getPackageName());
        turtleColorTile.setImageResource(resourceId);
    }
}
