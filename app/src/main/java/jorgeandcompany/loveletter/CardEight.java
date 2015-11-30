package jorgeandcompany.loveletter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;

/**
 * Created by Firemon123 on 10/1/2015.
 */
public class CardEight implements Card {
    private final int value = 8;

    @Override
    public void cardEffect(final Player player) {
        ThemedDialog.Builder gameOver= new ThemedDialog.Builder(GameData.game);
        gameOver.setCancelable(false);
        gameOver.setTitle("Card 8 Effect");
        gameOver.setMessage("Player " + player.getPlayerNumber() + " lost card 8. Player " + player.getPlayerNumber() + " is out!");
        gameOver.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GameData.out(player.getPlayerNumber());
                GameData.game.endOfTurn();
            }
        });
        gameOver.show();
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getDescription(Context c) {
        Resources res = c.getResources();
        String string = res.getString(R.string.Card_Eight_Description);
        return string;
    }

    @Override
    public int getSkinRes(String orientation) {
        return SkinRes.skinRes(8, orientation);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Card)) return false;
        Card other = (Card) o;
        int thisCard = getValue();
        int otherCard = other.getValue();
        if (thisCard == otherCard) return true;
        else return false;
    }
}
