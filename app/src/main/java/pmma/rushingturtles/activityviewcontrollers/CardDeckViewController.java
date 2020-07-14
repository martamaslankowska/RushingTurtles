package pmma.rushingturtles.activityviewcontrollers;

import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

import pmma.rushingturtles.R;
import pmma.rushingturtles.activities.GameActivity;
import pmma.rushingturtles.enums.TurtleColor;

public class CardDeckViewController {
    ImageView card1, card2, card3, card4, card5;
    List<ImageView> cards;
    ImageView outsideCard;

    float cardCoordinateXOrY;
    float cardMovedCoordinateXOrY;
    boolean cardCoordinatesHaveBeenSet;

    Button playCardButton;

    GameActivity gameActivity;
    int currentOrientation;

    public CardDeckViewController(GameActivity gameActivity, int currentOrientation) {
        this.gameActivity = gameActivity;
        this.currentOrientation = currentOrientation;
        initializeCardImageViews();
        cardCoordinatesHaveBeenSet = false;
        playCardButton = gameActivity.findViewById(R.id.buttonPlayCardOnDeck);
    }

    private void initializeCardImageViews() {
        card1 = gameActivity.findViewById(R.id.imageViewCard1);
        card2 = gameActivity.findViewById(R.id.imageViewCard2);
        card3 = gameActivity.findViewById(R.id.imageViewCard3);
        card4 = gameActivity.findViewById(R.id.imageViewCard4);
        card5 = gameActivity.findViewById(R.id.imageViewCard5);
        outsideCard = null;

        cards = Arrays.asList(card1, card2, card3, card4, card5);
        addCardImageViewsOnListeners();
    }

    private void addCardImageViewsOnListeners() {
        for (int i = 0; i < cards.size(); i++)
            cards.get(i).setOnClickListener(cardDeckClickListener);
    }

    private View.OnClickListener cardDeckClickListener = new View.OnClickListener() {
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
                if (gameActivity.gameActivityController.getCardColor(cards.indexOf(outsideCard)) == TurtleColor.RAINBOW)
                    playCardButton.setText(gameActivity.getResources().getString(R.string.pick_color_card));
                else
                    playCardButton.setText(gameActivity.getResources().getString(R.string.play_card_on_deck));
            }

            if (gameActivity.gameActivityController.isPlayerAnActivePlayer()) {
                gameActivity.setActivePlayerViews();
                if (gameActivity.colorPickerViewController.isColorPickerVisible()) {
                    gameActivity.colorPickerViewController.animatePathsForColorPickers(false);
                }
                if (outsideCard != null)
                    gameActivity.setActivenessOfPlayCardButton(true);
            }
        }
    };

    private void setCardCoordinates(float windowPercentage) {
        DisplayMetrics displayMetrics = gameActivity.getWindowDisplayMetrics();
        float windowSize, imgXOrY;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            windowSize = displayMetrics.widthPixels;
            imgXOrY = gameActivity.getImageViewCoordinates(card1)[0];
        } else {
            windowSize = displayMetrics.heightPixels;
            imgXOrY = gameActivity.getImageViewCoordinates(card1)[1] - gameActivity.getStatusBarHeight();
        }

        float movementValue = windowSize * windowPercentage;
        cardCoordinateXOrY = imgXOrY;
        cardMovedCoordinateXOrY = imgXOrY - movementValue;
    }

    public void moveCard(View view, boolean moveOutside) {
        int[] coordinates = gameActivity.getImageViewCoordinates(view);
        int imgX = coordinates[0];
        int imgY = coordinates[1] - gameActivity.getStatusBarHeight();
        float newImgCoordinate = moveOutside ? cardMovedCoordinateXOrY : cardCoordinateXOrY;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT)
            createAndAnimatePath(view, imgX, imgY, newImgCoordinate, imgY);
        else
            createAndAnimatePath(view, imgX, imgY, imgX, newImgCoordinate);
    }

    private void createAndAnimatePath(View view, float imgX, float imgY, float newImgX, float newImgY) {
        Path path = new Path();
        path.moveTo(imgX, imgY);
        path.lineTo(newImgX, newImgY);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        animator.setDuration(500);
        animator.start();
    }

    public ImageView getOutsideCard() {
        return outsideCard;
    }

    public void setOutsideCard(ImageView outsideCard) {
        this.outsideCard = outsideCard;
    }

    public List<ImageView> getCardsFromDeck() {
        return cards;
    }

    public void setCardsOnDeck(List<ImageView> cards) {
        this.cards = cards;
    }
}
