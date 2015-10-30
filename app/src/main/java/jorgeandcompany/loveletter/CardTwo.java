package jorgeandcompany.loveletter;

import android.content.Context;
import android.content.res.Resources;
import android.os.CountDownTimer;

/**
 * Created by Firemon123 on 10/1/2015.
 */
public class CardTwo implements Card {

    private final int value = 2;

    @Override
    public void drawAffect(Player player) {
        return;
    }

    @Override
    public void cardEffect(final Player player) {
        CountDownTimer c = new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                GameData.out(player.getPlayerNumber());
                GameData.game.endOfTurn(player);
            }
        };
        c.start();
    }

    @Override
    public void discardAffect(Player player) {
        return;
    }

    @Override
    public int getValue() {
        return value;
    }

    @Override
    public String getDescription(Context c) {
        Resources res = c.getResources();
        String string = res.getString(R.string.Card_Two_Description);
        return string;
    }

    @Override
    public int getSkinRes(int skinId) {
        switch (skinId) {
            case 1:
                return R.drawable.alibabaver;
            case 2:
                return R.drawable.twofacever;
            case 3:
                return R.drawable.quicksilverver;
            default:
                return R.drawable.alibabaver;
        }
    }
}
