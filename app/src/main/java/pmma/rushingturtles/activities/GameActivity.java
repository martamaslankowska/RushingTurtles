package pmma.rushingturtles.activities;

import android.animation.ObjectAnimator;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pmma.rushingturtles.R;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView card1;
    ImageView card2;
    ImageView card3;
    ImageView card4;
    ImageView card5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        card1 = findViewById(R.id.imageViewCard1);
        card2 = findViewById(R.id.imageViewCard2);
        card3 = findViewById(R.id.imageViewCard3);
        card4 = findViewById(R.id.imageViewCard4);
        card5 = findViewById(R.id.imageViewCard5);

        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        card4.setOnClickListener(this);
        card5.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        /* Find statusBar height */
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;

        /* Get window height and width */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int windowHeight = displayMetrics.heightPixels;
        int windowWidth = displayMetrics.widthPixels;

        /* Get imageView coordinates */
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        int imgX = rect.left;
        int imgY = rect.top - statusBarHeight;

        /* Calculate direction of card movement */
        float movementX = (imgX < windowWidth/2) ? windowWidth*0.3f : - windowWidth*0.3f;

        /* Move card left or right */
        Path path = new Path();
        path.moveTo(imgX, imgY);
        path.lineTo(movementX + imgX, imgY);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, View.X, View.Y, path);
        animator.setDuration(500);
        animator.start();
    }
}
