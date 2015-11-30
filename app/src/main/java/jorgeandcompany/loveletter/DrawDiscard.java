package jorgeandcompany.loveletter;

import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by kd on 11/13/15.
 */
public class DrawDiscard extends DrawDiscardDecorator {

    public DrawDiscard(Card c)
    {
        super(c);
    }

    public void drawAffect(Player player)
    {
        if(card.getValue() == 7) player.setSeven(true);
    }

    public void discardAffect(final Player player) {
        if (card.getValue() == 7) player.setSeven(false);
        else if (card.getValue() == 8) {
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

    }

    @Override
    public boolean equals(Object o) {
        return card.equals(o);
    }
}
