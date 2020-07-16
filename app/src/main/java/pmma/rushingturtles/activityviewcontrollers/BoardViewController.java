package pmma.rushingturtles.activityviewcontrollers;

import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.graphics.Rect;
import android.media.Image;
import android.os.Handler;
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
    List<Double> firstRowCoords;
    List<List<Double>> firstRowPercents;

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
        setTurtleImageViewHeight(0.12);
        setTurtlesCoordinatePositions();
        bringTurtlesToFront();

        Log.i("BoardViewController", "Working on coordinates :)");
    }

    private void initializeCoordinates() {
        boardSizePercentage = currentOrientation == Configuration.ORIENTATION_PORTRAIT ? 0.66 : 0.7;
        xCoords = new ArrayList<>();
        yCoords = new ArrayList<>();
        firstRowCoords = new ArrayList<>();

        firstRowPercents = new ArrayList<>();
        firstRowPercents.add(Arrays.asList(0.6));
        firstRowPercents.add(Arrays.asList(0.4, 0.7));
        firstRowPercents.add(Arrays.asList(0.225, 0.5, 0.775));
        firstRowPercents.add(Arrays.asList(0.15, 0.375, 0.625, 0.85));
        firstRowPercents.add(Arrays.asList(0.1, 0.3, 0.5, 0.7, 0.9));

        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            yPercent = Arrays.asList(0.83, 0.71, 0.63, 0.54, 0.46, 0.37, 0.26, 0.17, 0.11, 0.02);
            xPercent = Arrays.asList(0.00, 0.47, 0.23, 0.50, 0.80, 0.60, 0.80, 0.55, 0.25, 0.99);
        } else {
            xPercent = Arrays.asList(0.85, 0.72, 0.63, 0.54, 0.46, 0.37, 0.26, 0.17, 0.11, 0.02);
            yPercent = Arrays.asList(0.00, 0.45, 0.23, 0.50, 0.80, 0.60, 0.80, 0.55, 0.25, 0.99);
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
            int x = firstRowCoords.get(turtlePosition.getPositionOnTheStartRock()[0]).intValue() + turtleXShift;
            int y = yCoords.get(turtlePosition.getRock()).intValue() + turtleTailShift * turtlePosition.getPositionOnTheStartRock()[1]; //+ turtleYShift;
            int[] imageViewCoordinates = gameActivity.getImageViewCoordinates(turtleImageView);
            createAndAnimatePath(turtleImageView, imageViewCoordinates[0], imageViewCoordinates[1] - gameActivity.getStatusBarHeight(), x, y);
        }
    }

    private void setTurtleInGamePositions(TurtleOnBoardPosition turtlePosition, int i) {
        final ImageView turtleImageView = turtles.get(i);
        if (currentOrientation == Configuration.ORIENTATION_PORTRAIT) {
            int x = xCoords.get(turtlePosition.getRock()).intValue() + turtleXShift;
            int y = yCoords.get(turtlePosition.getRock()).intValue() + turtleTailShift * turtlePosition.getPosition(); //+ turtleYShift;
            int[] imageViewCoordinates = gameActivity.getImageViewCoordinates(turtleImageView);
            createAndAnimatePath(turtleImageView, imageViewCoordinates[0], imageViewCoordinates[1] - gameActivity.getStatusBarHeight(), x, y);
        }
    }

    private void createAndAnimatePath(View view, int xOld, int yOld, int xNew, int yNew) {
        Path path = new Path();
        path.moveTo(xOld, yOld);
        path.lineTo(xNew, yNew);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        animator.setDuration(750);
        animator.start();
    }

    private void setFirstRowCoordinates() {
        int noOfTurtleStacks = gameActivity.gameActivityController.getNumberOfTurtleStacksOnStartRock();
        if (noOfTurtleStacks > 0) {
            List<Double> firstRowPercent = firstRowPercents.get(noOfTurtleStacks - 1);
            firstRowCoords = new ArrayList<>();
            initializeCoordinateList(borderMargins[0], borderMargins[1], firstRowPercent, firstRowCoords);
        }
    }

    private void setTurtlesInitialPositions() {

    }

    public void updateTurtlesPositions() {
        setFirstRowCoordinates();
        setTurtlesCoordinatePositions();
    }


}
