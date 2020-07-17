package pmma.rushingturtles.activityviewcontrollers;

import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Path;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import pmma.rushingturtles.R;
import pmma.rushingturtles.activities.GameActivity;
import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.objects.Card;

public class CardDeckViewController {
    ImageView card1, card2, card3, card4, card5;
    List<ImageView> cards;
    ImageView outsideCard;
    ImageView cardOnDeck;

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
        cardOnDeck = gameActivity.findViewById(R.id.imageViewDeckOfCards);
        playCardButton = gameActivity.findViewById(R.id.buttonPlayCardOnDeck);
        bringAllViewsToFront();
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
            } else {
                gameActivity.setInActivePlayerViews();
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

    private int mapCardParametersToCardResourceId(Card card) {
        String turtleColor = card.getColor().toString().toLowerCase();
        String cardAction = card.getAction().toString().toLowerCase().replace("_", "");
        String resourceName = "card_" + turtleColor + "_" + cardAction;
        int resourceId = gameActivity.getResources().getIdentifier(resourceName, "drawable", gameActivity.getPackageName());
        return resourceId;
    }

    private void updateCardImage(ImageView view, Card card) {
        int resourceId = mapCardParametersToCardResourceId(card);
        view.setImageResource(resourceId);
    }

    public void updateCardImages(List<Card> deckOfCards) {
        for (int i=0; i<deckOfCards.size(); i++)
            updateCardImage(cards.get(i), deckOfCards.get(i));
    }

    public void updateCardImagesWithSound(List<Card> deckOfCards) {
        updateCardImages(deckOfCards);
        final MediaPlayer mp = MediaPlayer.create(gameActivity, R.raw.new_card_short);
        mp.start();
    }

    public void bringAllViewsToFront() {
        List<ImageView> reversedCards = new ArrayList<>(cards);
        Collections.reverse(reversedCards);
        for (ImageView card : reversedCards)
            card.bringToFront();
    }

    public void updateCardOnDeck(Card card) {
        cardOnDeck.setImageResource(mapCardParametersToCardResourceId(card));
    }

    public void setPlayedCardAsEmptyGray() {
        outsideCard.setImageResource(gameActivity.getResources().getIdentifier("card_gray_empty", "drawable", gameActivity.getPackageName()));
    }
}
