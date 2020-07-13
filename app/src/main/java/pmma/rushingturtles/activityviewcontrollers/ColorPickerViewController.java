package pmma.rushingturtles.activityviewcontrollers;

import android.view.View;
import android.widget.ImageView;

import java.util.Arrays;
import java.util.List;

import pmma.rushingturtles.R;
import pmma.rushingturtles.activities.GameActivity;

public class ColorPickerViewController {
    ImageView colorPickerBackground;
    ImageView blueColorPicker, redColorPicker, greenColorPicker, yellowColorPicker, purpleColorPicker;
    ImageView blueColorTick, redColorTick, greenColorTick, yellowColorTick, purpleColorTick;
    ImageView checkedColorPicker;
    List<ImageView> colorTicks;
    List<ImageView> colorPickers;

    GameActivity gameActivity;

    public ColorPickerViewController(GameActivity gameActivity) {
        this.gameActivity = gameActivity;
        initializeColorPickerViews();
    }

    private void initializeColorPickerViews() {
        colorPickerBackground = gameActivity.findViewById(R.id.imageViewColorPickerBackground);
        colorPickerBackground.setVisibility(View.GONE);

        blueColorPicker = gameActivity.findViewById(R.id.imageViewColorPickerBlue);
        redColorPicker = gameActivity.findViewById(R.id.imageViewColorPickerRed);
        greenColorPicker = gameActivity.findViewById(R.id.imageViewColorPickerGreen);
        yellowColorPicker = gameActivity.findViewById(R.id.imageViewColorPickerYellow);
        purpleColorPicker = gameActivity.findViewById(R.id.imageViewColorPickerPurple);
        colorPickers = Arrays.asList(blueColorPicker, redColorPicker, greenColorPicker, yellowColorPicker, purpleColorPicker);
        checkedColorPicker = null;
        for (int i = 0; i < colorPickers.size(); i++) {
            colorPickers.get(i).setOnClickListener(colorPickerClickListener);
            colorPickers.get(i).setVisibility(View.GONE);
        }

        blueColorTick = gameActivity.findViewById(R.id.imageViewColorPickerBlueTick);
        redColorTick = gameActivity.findViewById(R.id.imageViewColorPickerRedTick);
        greenColorTick = gameActivity.findViewById(R.id.imageViewColorPickerGreenTick);
        yellowColorTick = gameActivity.findViewById(R.id.imageViewColorPickerYellowTick);
        purpleColorTick = gameActivity.findViewById(R.id.imageViewColorPickerPurpleTick);
        colorTicks = Arrays.asList(blueColorTick, redColorTick, greenColorTick, yellowColorTick, purpleColorTick);

        for (int i = 0; i < colorTicks.size(); i++)
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



}
