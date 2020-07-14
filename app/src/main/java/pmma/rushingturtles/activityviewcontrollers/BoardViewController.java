package pmma.rushingturtles.activityviewcontrollers;

import android.content.res.Configuration;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import pmma.rushingturtles.R;
import pmma.rushingturtles.activities.GameActivity;

public class BoardViewController {

    ImageView blueTurtle, redTurtle, greenTurtle, yellowTurtle, purpleTurtle;
    ImageView board;

    double boardSizePercentage;
    double boardRatio = 0.37;
    List<Double> xPercent, yPercent, xCoords, yCoords;

    GameActivity gameActivity;
    int currentOrientation;

    public BoardViewController(GameActivity gameActivity, int currentOrientation) {
        this.gameActivity = gameActivity;
        this.currentOrientation = currentOrientation;
        initializeViews();
        initializeCoordinates();
        initializeCoordinateLists();

        Log.i("BoardViewController", "Working on coordinates :)");
    }

    private void initializeCoordinates() {
        boardSizePercentage = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? 0.66 : 0.7;
        xCoords = new ArrayList<>();
        yCoords = new ArrayList<>();

        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            yPercent = Arrays.asList(0.82, 0.72, 0.63, 0.54, 0.44, 0.33, 0.24, 0.17, 0.08);
            xPercent = Arrays.asList(0.05, 0.1, 0.05, 0.2, 0.3, 0.25, 0.2, 0.1, 0.2);
        } else {
            xPercent = Arrays.asList(0.82, 0.72, 0.63, 0.54, 0.44, 0.33, 0.24, 0.17, 0.08);
            yPercent = Arrays.asList(0.05, 0.1, 0.05, 0.2, 0.3, 0.25, 0.2, 0.1, 0.2);
        }
    }

    private void initializeViews() {
        board = gameActivity.findViewById(R.id.imageViewBoard);

        blueTurtle = gameActivity.findViewById(R.id.imageViewTurtleBlue);
        redTurtle = gameActivity.findViewById(R.id.imageViewTurtleRed);
        greenTurtle = gameActivity.findViewById(R.id.imageViewTurtleGreen);
        yellowTurtle = gameActivity.findViewById(R.id.imageViewTurtleYellow);
        purpleTurtle = gameActivity.findViewById(R.id.imageViewTurtlePurple);
    }

    private int[] getImageValues(ImageView view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int[] coordinates = new int[2];
        coordinates[0] = rect.right;
        coordinates[1] = rect.bottom;
        return coordinates;
    }

    private double[] getBoardImageViewSize() {
        DisplayMetrics displayMetrics = gameActivity.getWindowDisplayMetrics();
        int statusBarHeight = gameActivity.getStatusBarHeight();
        double boardImageViewShorterDim, boardImageViewLongerDim;
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            boardImageViewShorterDim = displayMetrics.widthPixels * boardSizePercentage;
            boardImageViewLongerDim = displayMetrics.heightPixels - statusBarHeight;
        } else {
            boardImageViewShorterDim = (displayMetrics.heightPixels - statusBarHeight) * boardSizePercentage;
            boardImageViewLongerDim = displayMetrics.widthPixels;
        }
        return new double[] {boardImageViewShorterDim, boardImageViewLongerDim};
    }

    private boolean boardHasMarginOnShorterDim(double[] boardImageViewSize) {
        double boardImageViewShorterDim = boardImageViewSize[0];
        double boardImageViewLongerDim = boardImageViewSize[1];
        boolean hasMarginOnShorterDim = true;
        if (boardImageViewShorterDim / boardImageViewLongerDim < boardRatio)
            hasMarginOnShorterDim = false;
        return hasMarginOnShorterDim;
    }

    private double getOneSideMarginValue(double[] boardImageViewSize) {
        double imageViewRatio = boardImageViewSize[0] / boardImageViewSize[1];
        return Math.abs(boardRatio - imageViewRatio)/2 * boardImageViewSize[1];
    }

    private double[] getBorderMargins(double[] boardImageViewSize, boolean hasMarginOnShorterDim) {
        double left, right, bottom, top;
        double boardImageViewShorterDim = boardImageViewSize[0];
        double boardImageViewLongerDim = boardImageViewSize[1];
        double oneSideMargin = getOneSideMarginValue(boardImageViewSize);
        if (hasMarginOnShorterDim) {
            bottom = 0;
            top = boardImageViewLongerDim;
            left = oneSideMargin;
            right = boardImageViewShorterDim - oneSideMargin;
        } else {
            left = 0;
            right = boardImageViewShorterDim;
            bottom = oneSideMargin;
            top = boardImageViewLongerDim - oneSideMargin;
        }
        return new double[] {left, right, bottom, top};
    }

    private void initializeCoordinateList(double firstCoord, double secondCoord, List<Double> percentages, List<Double> coordinates) {
        for (int i=0; i<percentages.size(); i++)
            coordinates.add(firstCoord + ((secondCoord - firstCoord) * percentages.get(i)));
    }

    private void initializeCoordinateLists() {
        double[] boardImageViewSize = getBoardImageViewSize();
        boolean hasMarginOnShorterDim = boardHasMarginOnShorterDim(boardImageViewSize);
        double[] borderMargins = getBorderMargins(boardImageViewSize, hasMarginOnShorterDim);
        double left = borderMargins[0];
        double right = borderMargins[1];
        double bottom = borderMargins[2];
        double top = borderMargins[3];

        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            initializeCoordinateList(bottom, top, yPercent, yCoords);
            initializeCoordinateList(left, right, xPercent, xCoords);
        } else {
            initializeCoordinateList(bottom, top, xPercent, xCoords);
            initializeCoordinateList(left, right, yPercent, yCoords);
        }
    }

}
