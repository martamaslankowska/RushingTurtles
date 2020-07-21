package pmma.rushingturtles.activityviewcontrollers;

import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Path;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pmma.rushingturtles.R;
import pmma.rushingturtles.activities.GameActivity;
import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.objects.TurtleOnBoardPosition;

public class BoardViewController {
    ConstraintLayout gameLayout;

    ImageView blueTurtle, redTurtle, greenTurtle, yellowTurtle, purpleTurtle;
    List<ImageView> turtles;
    List<TurtleColor> turtleColors;
    ImageView board;

    double[] borderMargins;
    double boardSizePercentage;
    double boardRatio = 0.37;
    List<Double> xPercent, yPercent, xCoords, yCoords;
    List<Double> startRockShortCoords, startRockLongCoords;
    List<List<Double>> startRockShortPercents, startRockLongPercents;

    int turtleWidth, turtleHeight, turtleXShift, turtleYShift;
    int turtleTailShift;
    double singleTurtleRatio;

    GameActivity gameActivity;
    int currentOrientation;

    public BoardViewController(GameActivity gameActivity, int currentOrientation) {
        this.gameActivity = gameActivity;
        this.currentOrientation = currentOrientation;
        singleTurtleRatio = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? 0.644 : 0.73;
        initializeViews();
        initializeCoordinates();
        borderMargins = getBorderMarginCoordinates();
        initializeCoordinateLists();
        setFirstRowCoordinates();
//        setTurtlesInitialPositions();
        setTurtleImageViewHeight(0.12);
        setTurtlesCoordinatePositions();
        bringTurtlesToFront();

        Log.i("BoardViewController", "Working on coordinates :)");
    }

    private void initializeCoordinates() {
        boardSizePercentage = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? 0.66 : 0.7;
        xCoords = new ArrayList<>();
        yCoords = new ArrayList<>();
        startRockShortCoords = new ArrayList<>();
        startRockLongCoords = new ArrayList<>();

        startRockShortPercents = new ArrayList<>();
        startRockLongPercents = new ArrayList<>();
        startRockShortPercents.add(Arrays.asList(0.6));
        startRockShortPercents.add(Arrays.asList(0.4, 0.7));
        startRockShortPercents.add(Arrays.asList(0.275, 0.525, 0.775));
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            startRockShortPercents.add(Arrays.asList(0.165, 0.385, 0.615, 0.835));
            startRockShortPercents.add(Arrays.asList(0.13, 0.315, 0.5, 0.685, 0.87));
            startRockLongPercents.add(Arrays.asList(0.83));
            startRockLongPercents.add(Arrays.asList(0.83, 0.83));
            startRockLongPercents.add(Arrays.asList(0.83, 0.83, 0.83));
            startRockLongPercents.add(Arrays.asList(0.82, 0.84, 0.82, 0.84));
            startRockLongPercents.add(Arrays.asList(0.82, 0.84, 0.82, 0.84, 0.82));
        } else {
            startRockShortPercents.add(Arrays.asList(0.17, 0.38, 0.59, 0.80));
            startRockShortPercents.add(Arrays.asList(0.10, 0.28, 0.46, 0.64, 0.82));
            startRockLongPercents.add(Arrays.asList(0.08));
            startRockLongPercents.add(Arrays.asList(0.08, 0.08));
            startRockLongPercents.add(Arrays.asList(0.08, 0.08, 0.08));
            startRockLongPercents.add(Arrays.asList(0.09, 0.07, 0.09, 0.07));
            startRockLongPercents.add(Arrays.asList(0.09, 0.07, 0.09, 0.07, 0.09));
        }

        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            yPercent = Arrays.asList(0.83, 0.71, 0.63, 0.54, 0.46, 0.37, 0.26, 0.17, 0.11, 0.015);
            xPercent = Arrays.asList(0.00, 0.47, 0.23, 0.50, 0.80, 0.60, 0.80, 0.50, 0.25, 0.55);
        } else {
            xPercent = Arrays.asList(0.07, 0.19, 0.28, 0.37, 0.45, 0.56, 0.67, 0.76, 0.82, 0.93);
            yPercent = Arrays.asList(0.00, 0.45, 0.27, 0.50, 0.80, 0.60, 0.80, 0.52, 0.25, 0.50);
        }
    }

    private void initializeViews() {
        gameLayout = gameActivity.findViewById(R.id.gameLayout);
        board = gameActivity.findViewById(R.id.imageViewBoard);

        blueTurtle = gameActivity.findViewById(R.id.imageViewTurtleBlue);
        redTurtle = gameActivity.findViewById(R.id.imageViewTurtleRed);
        greenTurtle = gameActivity.findViewById(R.id.imageViewTurtleGreen);
        yellowTurtle = gameActivity.findViewById(R.id.imageViewTurtleYellow);
        purpleTurtle = gameActivity.findViewById(R.id.imageViewTurtlePurple);

        turtles = Arrays.asList(blueTurtle, redTurtle, greenTurtle, yellowTurtle, purpleTurtle);
        turtleColors = Arrays.asList(TurtleColor.BLUE, TurtleColor.RED, TurtleColor.GREEN, TurtleColor.YELLOW, TurtleColor.PURPLE);
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

    private double[] getBorderMarginCoordinates() {
        double[] boardImageViewSize = getBoardImageViewSize();
        boolean hasMarginOnShorterDim = boardHasMarginOnShorterDim(boardImageViewSize);
        double[] borderMargins = getBorderMargins(boardImageViewSize, hasMarginOnShorterDim);
        return borderMargins;
    }

    private void initializeCoordinateLists() {
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

    private void setTurtleImageViewHeight(double boardSizeRatio) {
        int turtleLongerSize = (int) ((borderMargins[3] - borderMargins[2]) * boardSizeRatio);
        for (int i=0; i<turtles.size(); i++)
            if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
                turtles.get(i).getLayoutParams().height = turtleLongerSize;
                turtles.get(i).getLayoutParams().width = (int) (turtleLongerSize * singleTurtleRatio);
                turtleXShift = - turtles.get(i).getLayoutParams().width / 2;
                turtleYShift = - turtleLongerSize / 2;
            } else {
                turtles.get(i).getLayoutParams().width = turtleLongerSize;
                turtles.get(i).getLayoutParams().height = (int) (turtleLongerSize * singleTurtleRatio);
                turtleYShift = - turtles.get(i).getLayoutParams().height / 2;
                turtleXShift = - turtleLongerSize / 2;
            }

        ConstraintLayout.LayoutParams turtleParams = (ConstraintLayout.LayoutParams) turtles.get(0).getLayoutParams();
        turtleWidth = turtleParams.width;
        turtleHeight = turtleParams.height;
        turtleTailShift = - (int) (turtleLongerSize * 0.12);
    }

    public void setTurtlesCoordinatePositions() {
        List<TurtleOnBoardPosition> turtlePositions = gameActivity.gameActivityController.getTurtlesOnBoardPositions(turtleColors);
        for (int i=0; i<turtlePositions.size(); i++) {
            TurtleOnBoardPosition turtlePosition = turtlePositions.get(i);
            if (turtlePosition.getRock() == 0)
                setTurtleOnStartRockPosition(turtlePosition, i);
            else
                setTurtleInGamePositions(turtlePosition, i);
        }
    }

    public void bringTurtlesToFront() {
        List<TurtleOnBoardPosition> turtlePositions = gameActivity.gameActivityController.getTurtlesOnBoardPositions(turtleColors);
        List<ImageView> turtleInGameOrder = gameActivity.gameActivityController.getTurtleImageViewSortedByStackPositionsInGame(turtles, turtlePositions);
        for (ImageView turtle : turtleInGameOrder)
            turtle.bringToFront();
        List<ImageView> turtleOnStartOrder = gameActivity.gameActivityController.getTurtleImageViewSortedByStackPositionsOnStartRock(turtles, turtlePositions);
        for (ImageView turtle : turtleOnStartOrder)
            turtle.bringToFront();
    }

    private void setTurtleOnStartRockPosition(TurtleOnBoardPosition turtlePosition, int i) {
        final ImageView turtleImageView = turtles.get(i);
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            int x = startRockShortCoords.get(turtlePosition.getPositionOnTheStartRock()[0]).intValue() + turtleXShift;
            int y = startRockLongCoords.get(turtlePosition.getPositionOnTheStartRock()[0]).intValue() + turtleTailShift * turtlePosition.getPositionOnTheStartRock()[1]; //+ turtleYShift;
            animatePath(turtleImageView, x, y);
        } else {
            int x = startRockLongCoords.get(turtlePosition.getPositionOnTheStartRock()[0]).intValue() + turtleXShift;
            int y = startRockShortCoords.get(turtlePosition.getPositionOnTheStartRock()[0]).intValue() + turtleTailShift * turtlePosition.getPositionOnTheStartRock()[1] + turtleYShift;
            animatePath(turtleImageView, x, y);
        }
    }

    private void setTurtleInGamePositions(TurtleOnBoardPosition turtlePosition, int i) {
        final ImageView turtleImageView = turtles.get(i);
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            int x = xCoords.get(turtlePosition.getRock()).intValue() + turtleXShift;
            int y = yCoords.get(turtlePosition.getRock()).intValue() + turtleTailShift * turtlePosition.getPosition(); //+ turtleYShift;
            animatePath(turtleImageView, x, y);
        } else {
            int x = xCoords.get(turtlePosition.getRock()).intValue() + turtleXShift;
            int y = yCoords.get(turtlePosition.getRock()).intValue() + turtleTailShift * turtlePosition.getPosition() + turtleYShift;
            animatePath(turtleImageView, x, y);
        }
    }

    private void animatePath(ImageView turtle, int x, int y) {
        int[] imageViewCoordinates = gameActivity.getImageViewCoordinates(turtle);
        createAndAnimatePath(turtle, imageViewCoordinates[0], imageViewCoordinates[1] - gameActivity.getStatusBarHeight(), x, y);
    }

    private void createAndAnimatePath(View view, int xOld, int yOld, int xNew, int yNew) {
        Path path = new Path();
        path.moveTo(xOld, yOld);
        path.lineTo(xNew, yNew);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        animator.setDuration(600);
        animator.start();
    }

    private void setFirstRowCoordinates() {
        int noOfTurtleStacks = gameActivity.gameActivityController.getNumberOfTurtleStacksOnStartRock();
        if (noOfTurtleStacks > 0) {
            List<Double> startRockShortPercent = startRockShortPercents.get(noOfTurtleStacks - 1);
            List<Double> startRockLongPercent = startRockLongPercents.get(noOfTurtleStacks - 1);
            startRockShortCoords = new ArrayList<>();
            startRockLongCoords = new ArrayList<>();
            initializeCoordinateList(borderMargins[0], borderMargins[1], startRockShortPercent, startRockShortCoords);
            initializeCoordinateList(borderMargins[2], borderMargins[3], startRockLongPercent, startRockLongCoords);
        }
    }

//    private void setTurtlesInitialPositions() {
//        float shorterCoord = (float) (borderMargins[0] + (borderMargins[1] - borderMargins[0]));
//        float longerCoord = (float) (borderMargins[2] + (borderMargins[3] - borderMargins[2]));
//        for (ImageView turtle : turtles) {
//            if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
//                turtle.setX(shorterCoord * 0.5f);
//                turtle.setY((float) (longerCoord * yCoords.get(0)));
//            } else {
//                turtle.setY(shorterCoord * 0.5f);
//                turtle.setX((float) (longerCoord * xCoords.get(0)));
//            }
//        }
//    }

    public void updateTurtlesPositions() {
        setFirstRowCoordinates();
        setTurtlesCoordinatePositions();
    }


}
