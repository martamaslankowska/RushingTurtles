package pmma.rushingturtles.activityviewcontrollers;

import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.os.Handler;
import pmma.rushingturtles.R;
import pmma.rushingturtles.activities.GameActivity;
import pmma.rushingturtles.enums.TurtleColor;

public class ColorPickerViewController {
    ImageView colorPickerBackground;
    ImageView blueColorPicker, redColorPicker, greenColorPicker, yellowColorPicker, purpleColorPicker;
    ImageView blueColorTick, redColorTick, greenColorTick, yellowColorTick, purpleColorTick;
    ImageView checkedColorPicker;
    List<ImageView> colorTicks;
    List<ImageView> colorPickers;

    Button playCardButton;

    float colorPickerYOrX;
    float colorPickerHiddenYOrX;
    List<TurtleColor> colors;
    List<Integer> xOrYCoordinatesOfColorPickers;

    GameActivity gameActivity;
    int currentOrientation;
    boolean isColorPickerVisible;

    public ColorPickerViewController(GameActivity gameActivity, int currentOrientation) {
        this.gameActivity = gameActivity;
        this.currentOrientation = currentOrientation;
        initializeColorPickerViews();
        setColorPickerYOrX();

        playCardButton = gameActivity.findViewById(R.id.buttonPlayCardOnDeck);
        isColorPickerVisible = false;
        colors = Arrays.asList(TurtleColor.BLUE, TurtleColor.RED, TurtleColor.GREEN, TurtleColor.YELLOW, TurtleColor.PURPLE);
    }

    private void initializeColorPickerViews() {
        colorPickerBackground = gameActivity.findViewById(R.id.imageViewColorPickerBackground);
        colorPickerBackground.setVisibility(View.INVISIBLE);

        blueColorPicker = gameActivity.findViewById(R.id.imageViewColorPickerBlue);
        redColorPicker = gameActivity.findViewById(R.id.imageViewColorPickerRed);
        greenColorPicker = gameActivity.findViewById(R.id.imageViewColorPickerGreen);
        yellowColorPicker = gameActivity.findViewById(R.id.imageViewColorPickerYellow);
        purpleColorPicker = gameActivity.findViewById(R.id.imageViewColorPickerPurple);
        colorPickers = Arrays.asList(blueColorPicker, redColorPicker, greenColorPicker, yellowColorPicker, purpleColorPicker);
        checkedColorPicker = null;
        xOrYCoordinatesOfColorPickers = new ArrayList<>();
        for (int i = 0; i < colorPickers.size(); i++) {
            colorPickers.get(i).setOnClickListener(colorPickerClickListener);
            colorPickers.get(i).setVisibility(View.INVISIBLE);
        }

        blueColorTick = gameActivity.findViewById(R.id.imageViewColorPickerBlueTick);
        redColorTick = gameActivity.findViewById(R.id.imageViewColorPickerRedTick);
        greenColorTick = gameActivity.findViewById(R.id.imageViewColorPickerGreenTick);
        yellowColorTick = gameActivity.findViewById(R.id.imageViewColorPickerYellowTick);
        purpleColorTick = gameActivity.findViewById(R.id.imageViewColorPickerPurpleTick);
        colorTicks = Arrays.asList(blueColorTick, redColorTick, greenColorTick, yellowColorTick, purpleColorTick);

        for (int i = 0; i < colorTicks.size(); i++)
            colorTicks.get(i).setVisibility(View.INVISIBLE);
    }

    private View.OnClickListener colorPickerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkOrUncheckColorPicker(v);
            if (checkedColorPicker == null) {
                gameActivity.setActivenessOfPlayCardButton(false);
            } else {
                gameActivity.setActivenessOfPlayCardButton(true);
                playCardButton.setText(gameActivity.getResources().getString(R.string.play_card_on_deck));
            }
        }
    };

    private void checkOrUncheckColorPicker(View view) {
        int touchedColorPickerIdx = colorPickers.indexOf(view);
        if (checkedColorPicker == null) {
            colorTicks.get(touchedColorPickerIdx).setVisibility(View.VISIBLE);
            checkedColorPicker = (ImageView) view;
        } else {
            if (checkedColorPicker == view) {
                colorTicks.get(touchedColorPickerIdx).setVisibility(View.GONE);
                checkedColorPicker = null;
            } else {
                colorTicks.get(colorPickers.indexOf(checkedColorPicker)).setVisibility(View.GONE);
                colorTicks.get(touchedColorPickerIdx).setVisibility(View.VISIBLE);
                checkedColorPicker = (ImageView) view;
            }
        }
    }

    public void animatePathsForColorPickers(boolean shouldBeVisible) {
        if (checkedColorPicker != null) {
            ImageView checkedColorTick = colorTicks.get(colorPickers.indexOf(checkedColorPicker));
            checkedColorTick.setVisibility(View.INVISIBLE);
            checkedColorPicker = null;
        }

        setXOrYCoordinatesOfColorPickers();

        setImagePath(colorPickerBackground, xOrYCoordinatesOfColorPickers.get(0), shouldBeVisible);
        for (int i=0; i<colorPickers.size(); i++)
            setImagePath(colorPickers.get(i), xOrYCoordinatesOfColorPickers.get(i + 1), shouldBeVisible);
        isColorPickerVisible = shouldBeVisible;
    }

    private void setImagePath(ImageView view, int xOrY, boolean shouldBeVisible) {
        if (shouldBeVisible)
            createAndAnimatePath(view, xOrY, colorPickerHiddenYOrX, colorPickerYOrX);
        else
            createAndAnimatePath(view, xOrY, colorPickerYOrX, colorPickerHiddenYOrX);
    }

    private void createAndAnimatePath(View view, float imgXOrY, float oldYOrX, float newYOrX) {
        Path path = new Path();
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            path.moveTo(imgXOrY, oldYOrX);
            path.lineTo(imgXOrY, newYOrX);
        } else {
            path.moveTo(oldYOrX, imgXOrY);
            path.lineTo(newYOrX, imgXOrY);
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        animator.setDuration(750);
        animator.start();
    }

    private void setColorPickerYOrX() {
        DisplayMetrics displayMetrics = gameActivity.getWindowDisplayMetrics();
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            colorPickerYOrX = (float) (displayMetrics.heightPixels * 0.01);
            colorPickerHiddenYOrX = - (float) (displayMetrics.heightPixels * 0.15);
        } else {
            colorPickerYOrX = (float) (displayMetrics.widthPixels * 0.84);
            colorPickerHiddenYOrX = (float) (displayMetrics.widthPixels * 1.01);
        }
    }

    public void setXOrYCoordinatesOfColorPickers() {
        if (xOrYCoordinatesOfColorPickers.isEmpty()) {
            int diff = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? 0 : - gameActivity.getStatusBarHeight();
            colorPickerBackground.setVisibility(View.VISIBLE);
            xOrYCoordinatesOfColorPickers.add(gameActivity.getImageViewCoordinates(colorPickerBackground)[currentOrientation - 1] + diff);
            for (int i = 0; i < colorPickers.size(); i++) {
                colorPickers.get(i).setVisibility(View.VISIBLE);
                xOrYCoordinatesOfColorPickers.add(gameActivity.getImageViewCoordinates(colorPickers.get(i))[currentOrientation - 1] + diff);
            }
        }
    }

    public boolean isColorPickerVisible() {
        return isColorPickerVisible;
    }

    public ImageView getCheckedColorPicker() {
        return checkedColorPicker;
    }

    public void setCheckedColorPicker(ImageView checkedColorPicker) {
        this.checkedColorPicker = checkedColorPicker;
    }

    public TurtleColor getCheckedColor() {
        return colors.get(colorPickers.indexOf(checkedColorPicker));
    }

    public void bringAllViewsToFront() {
        colorPickerBackground.bringToFront();
        for (ImageView colorPicker : colorPickers)
            colorPicker.bringToFront();
        for (ImageView tick : colorTicks)
            tick.bringToFront();
    }
}
