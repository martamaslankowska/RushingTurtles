package pmma.rushingturtles.activityviewcontrollers;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import pmma.rushingturtles.R;
import pmma.rushingturtles.activities.GameActivity;
import pmma.rushingturtles.activities.MainActivity;
import pmma.rushingturtles.enums.TurtleColor;
import pmma.rushingturtles.websocket.WSC;

import static android.app.Activity.RESULT_OK;

public class WinnerPopupViewController {

    private static final String IMAGE = "TurtleImage";
    private static final String NAME = "PlayerName";

    List<HashMap<String,String>> listViewList;
    String winnerName;
    String[] from;
    int[] to;

    ConstraintLayout mainLayout;
    PopupWindow popupWindowWinner;
    TextView winnerNameTextView;
    Button closePopupButton;

    GameActivity gameActivity;
    int currentOrientation;

    public WinnerPopupViewController(GameActivity gameActivity, int currentOrientation) {
        this.gameActivity = gameActivity;
        this.currentOrientation = currentOrientation;
        initializeViews();
    }

    private void initializeViews() {
        mainLayout = gameActivity.findViewById(R.id.gameLayout);
    }

    public void manageWinnerPopupWindow(String winnerName, List<String> playersNamesPlaces, List<TurtleColor> playersTurtleColors) {
        LayoutInflater layoutInflater = (LayoutInflater) gameActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View customView = layoutInflater.inflate(R.layout.popup_winner, null);

        closePopupButton = (Button) customView.findViewById(R.id.closePopupBtn);
        winnerNameTextView = (TextView) customView.findViewById(R.id.textViewWinnerName);

        popupWindowWinner = new PopupWindow(customView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindowWinner.showAtLocation(mainLayout, Gravity.CENTER, 0, 0);
        winnerNameTextView.setText(winnerName.toUpperCase());

        initializeListView(playersNamesPlaces, playersTurtleColors);
        setAdapter(customView);

        closePopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindowWinner.dismiss();
                // TODO wysyłanie wiadomości do serwera o chęci ponownego zagrania w grę :)
//                WSC.getInstance().sendPlayAgainMsg();

                Intent output = new Intent();
                output.putExtra("play_again_msg", true);
                gameActivity.setResult(RESULT_OK, output);
                gameActivity.finish();
            }
        });
    }

    public void initializeListView(List<String> playersNamesPlaces, List<TurtleColor> playersTurtleColors) {
        listViewList = new ArrayList<HashMap<String, String>>();
        for (int i=0; i<playersNamesPlaces.size(); i++) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put(IMAGE, getResourceIdString(playersTurtleColors.get(i)));
            hm.put(NAME, playersNamesPlaces.get(i));
            listViewList.add(hm);
        }

        from = new String[]{IMAGE, NAME};
        to = new int[]{R.id.imageViewPlayersTurtle, R.id.textViewPlayersName};
    }

    public void setAdapter(View customView) {
        SimpleAdapter simpleAdapter = new SimpleAdapter(gameActivity.getBaseContext(), listViewList, R.layout.item_listview_winners, from, to);
        ListView simpleListView = (ListView) customView.findViewById(R.id.listViewOfPlayersFinalPlaces);
        simpleListView.setAdapter(simpleAdapter);
        simpleListView.setEnabled(false);
    }

    public String getResourceIdString(TurtleColor turtleColor) {
        String resourceName = "winner_turtle_" + turtleColor.toString().toLowerCase();
        int resourceId = gameActivity.getResources().getIdentifier(resourceName, "drawable", gameActivity.getPackageName());
        return String.valueOf(resourceId);
    }

}
