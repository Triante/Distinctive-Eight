package jorgeandcompany.loveletter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


public class Game extends ActionBarActivity {
    public int deckcount = 16;
    public ImageButton discard, deck, firstPlayerRight, firstPlayerLeft, secondPlayerRight,
            secondPlayerLeft, thirdPlayerRight, thirdPlayerLeft, fourthPlayerRight, fourthPlayerLeft, outCard;
    private Button bPlay, bCancel;
    private ImageView expandedCardImage, backgroundOnPaused;
    private TextView cardDescriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        discard = (ImageButton) findViewById(R.id.discard);
        deck = (ImageButton) findViewById(R.id.deck);
        firstPlayerLeft = (ImageButton) findViewById(R.id.player1left);
        firstPlayerRight = (ImageButton) findViewById(R.id.player1right);
        secondPlayerRight = (ImageButton) findViewById(R.id.player2left);
        secondPlayerLeft = (ImageButton) findViewById(R.id.player2right);
        thirdPlayerLeft = (ImageButton) findViewById(R.id.player3left);
        thirdPlayerRight = (ImageButton) findViewById(R.id.player3right);
        fourthPlayerLeft = (ImageButton) findViewById(R.id.player4right);
        fourthPlayerRight = (ImageButton) findViewById(R.id.player4left);
        outCard = (ImageButton) findViewById(R.id.outCard);
        bPlay = (Button) findViewById(R.id.bPlay);
        bCancel = (Button) findViewById(R.id.bCancel);
        cardDescriptionText = (TextView) findViewById(R.id.card_description_text);
        expandedCardImage = (ImageView) findViewById(R.id.expanded_image);
        bCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageZoomToClose();
            }
        });
        backgroundOnPaused = (ImageView) findViewById(R.id.backGround);

        GameData.setContextMenu(this);
        GameData.newGame();
        handOutCards(GameData.TURN);
    }

    public void multiPlayerGame() {
        int turn = GameData.TURN;;
        final Player on = GameData.PlayerList[turn];
        CountDownTimer toMove = new CountDownTimer(3000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (on.hasLeftCard()) {
                    deckToRight(1);
                }
                else {
                    deckToLeft(1);
                }
                on.drawCard();
            }

            @Override
            public void onFinish() {
                playerMove(on);
            }
        };
        toMove.start();


        //player or ai does turn
//        if (turn == 1) {  // player
//            playerMove(on);
//        }
//        else { //ai
//            playerMove(on);
//        }
    }
    private void playerMove(final Player on) {
        final ImageButton left = firstPlayerLeft;
        flipCard(left);
        final ImageButton right = firstPlayerRight;
        left.setClickable(true);
        flipCard(right);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageZoomToOpen(on, 0, left);
            }
        });
        right.setClickable(true);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageZoomToOpen(on, 1, right);
            }
        });
    }
    public void endOfTurn(final Player on) {
        CountDownTimer toEnd = new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                firstPlayerLeft.setClickable(false);
                firstPlayerLeft.setOnClickListener(null);
                firstPlayerRight.setClickable(false);
                firstPlayerRight.setOnClickListener(null);
                secondPlayerLeft.setClickable(false);
                secondPlayerLeft.setOnClickListener(null);
                secondPlayerRight.setClickable(false);
                secondPlayerRight.setOnClickListener(null);
                thirdPlayerLeft.setClickable(false);
                thirdPlayerLeft.setOnClickListener(null);
                thirdPlayerRight.setClickable(false);
                thirdPlayerRight.setOnClickListener(null);
                fourthPlayerLeft.setClickable(false);
                fourthPlayerLeft.setOnClickListener(null);
                fourthPlayerRight.setClickable(false);
                fourthPlayerRight.setOnClickListener(null);

                GameData.nextTurn();
                if (GameData.getDeckCount() == 1) {
                    ImageButton deckDummy = (ImageButton) findViewById(R.id.deckDummy);
                    deckDummy.setVisibility(View.INVISIBLE);
                    deck.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onFinish() {
               if (GameData.FINISH_GAME) {
                   while (GameData.PlayerList[GameData.TURN].isOut()) {
                       GameData.nextTurn();
                   }
                   AlertDialog.Builder nextPlayerReady = new AlertDialog.Builder(Game.this);
                   nextPlayerReady.setTitle("END");
                   nextPlayerReady.setMessage("Player " + GameData.TURN + " has won!");
                   DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           GameData.newRound();
                           handOutCards(GameData.TURN);
                       }
                   };
                   nextPlayerReady.setPositiveButton("YAY", ok);
                   nextPlayerReady.setCancelable(false);
                   nextPlayerReady.show();
               }
                else {
                   while (GameData.PlayerList[GameData.TURN].isOut()) {
                       GameData.nextTurn();
                   }
                   AlertDialog.Builder nextPlayerReady = new AlertDialog.Builder(Game.this);
                   nextPlayerReady.setTitle("Next Player");
                   nextPlayerReady.setMessage("Player " + GameData.TURN + " is up.\nPlease pass to player and select OK when ready.");
                   DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           repaint();
                           multiPlayerGame();
                       }
                   };
                   nextPlayerReady.setPositiveButton("OK", ok);
                   nextPlayerReady.setCancelable(false);
                   nextPlayerReady.show();
               }
            }
        };
        toEnd.start();
     }


















    //animations

    public void deckToRight(int playerID) {
        switch (playerID){
            case 1:
                deckToFirstRight();
                break;
            case 2:
                deckToSecondRight();
                break;
            case 3:
                deckToThirdRight();
                break;
            case 4:
                deckToFourthRight();
                break;
        }
    }
    public void deckToLeft(int playerID) {
        switch (playerID){
            case 1:
                deckToFirstLeft();
                break;
            case 2:
                deckToSecondLeft();
                break;
            case 3:
                deckToThirdLeft();
                break;
            case 4:
                deckToFourthLeft();
                break;
        }
    }

    private void deckToFirstRight() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        Animation translate;
        AnimationSet set;
        firstPlayerRight.getLocationOnScreen(cardcoordinates);
        deck.getLocationOnScreen(deckcoordinates);
        translate = new TranslateAnimation(0, cardcoordinates[0] - deckcoordinates[0], 0, cardcoordinates[1] - deckcoordinates[1]);
        translate.setDuration(1000);
        set = new AnimationSet (true);
        set.addAnimation(translate);
        deck.startAnimation(set);
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                firstPlayerRight.setVisibility(firstPlayerRight.VISIBLE);
            }
        }.start();

    }
    private void deckToSecondRight() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        Animation translate;
        Animation rotate;
        AnimationSet set;
        secondPlayerRight.getLocationOnScreen(cardcoordinates);
        deck.getLocationOnScreen(deckcoordinates);
        translate = new TranslateAnimation(0, cardcoordinates[0] - deckcoordinates[0], 0, cardcoordinates[1] - deckcoordinates[1]);
        rotate = new RotateAnimation(0, -90, deck.getPivotX(), deck.getPivotY());
        translate.setDuration(1000);
        rotate.setDuration(1000);
        set = new AnimationSet (true);
        set.addAnimation(rotate);
        set.addAnimation(translate);
        deck.startAnimation(set);
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                secondPlayerRight.setVisibility(secondPlayerRight.VISIBLE);
            }
        }.start();


    }
    private void deckToThirdRight() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        Animation translate;
        Animation rotate;
        AnimationSet set;
        thirdPlayerRight.getLocationOnScreen(cardcoordinates);
        deck.getLocationOnScreen(deckcoordinates);
        translate = new TranslateAnimation(0, cardcoordinates[0] - deckcoordinates[0], 0, cardcoordinates[1] - deckcoordinates[1]);
        rotate = new RotateAnimation(0, 180, deck.getPivotX(), deck.getPivotY());
        translate.setDuration(1000);
        rotate.setDuration(1000);
        set = new AnimationSet (true);
        set.addAnimation(rotate);
        set.addAnimation(translate);
        deck.startAnimation(set);
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                thirdPlayerRight.setVisibility(thirdPlayerRight.VISIBLE);
            }
        }.start();


    }
    private void deckToFourthRight() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        Animation translate;
        Animation rotate;
        AnimationSet set;
        fourthPlayerRight.getLocationOnScreen(cardcoordinates);
        deck.getLocationOnScreen(deckcoordinates);
        translate = new TranslateAnimation(0, cardcoordinates[0] - deckcoordinates[0], 0, cardcoordinates[1] - deckcoordinates[1]);
        rotate = new RotateAnimation(0, 90, deck.getPivotX(), deck.getPivotY());
        translate.setDuration(1000);
        rotate.setDuration(1000);
        set = new AnimationSet (true);
        set.addAnimation(rotate);
        set.addAnimation(translate);
        deck.startAnimation(set);
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                fourthPlayerRight.setVisibility(fourthPlayerRight.VISIBLE);
            }
        }.start();

    }

    private void deckToFirstLeft() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        Animation translate;
        AnimationSet set;
        firstPlayerLeft.getLocationOnScreen(cardcoordinates);
        deck.getLocationOnScreen(deckcoordinates);
        translate = new TranslateAnimation(0, cardcoordinates[0] - deckcoordinates[0], 0, cardcoordinates[1] - deckcoordinates[1]);
        translate.setDuration(1000);
        set = new AnimationSet (true);
        set.addAnimation(translate);
        deck.startAnimation(set);
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                firstPlayerLeft.setVisibility(firstPlayerLeft.VISIBLE);
            }
        }.start();

    }
    private void deckToSecondLeft() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        Animation translate;
        Animation rotate;
        AnimationSet set;
        secondPlayerLeft.getLocationOnScreen(cardcoordinates);
        deck.getLocationOnScreen(deckcoordinates);
        translate = new TranslateAnimation(0, cardcoordinates[0] - deckcoordinates[0], 0, cardcoordinates[1] - deckcoordinates[1]);
        rotate = new RotateAnimation(0, -90, deck.getPivotX(), deck.getPivotY());
        translate.setDuration(1000);
        rotate.setDuration(1000);
        set = new AnimationSet (true);
        set.addAnimation(rotate);
        set.addAnimation(translate);
        deck.startAnimation(set);
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                secondPlayerLeft.setVisibility(secondPlayerLeft.VISIBLE);
            }
        }.start();


    }
    private void deckToThirdLeft() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        Animation translate;
        Animation rotate;
        AnimationSet set;
        thirdPlayerLeft.getLocationOnScreen(cardcoordinates);
        deck.getLocationOnScreen(deckcoordinates);
        translate = new TranslateAnimation(0, cardcoordinates[0] - deckcoordinates[0], 0, cardcoordinates[1] - deckcoordinates[1]);
        rotate = new RotateAnimation(0, 180, deck.getPivotX(), deck.getPivotY());
        translate.setDuration(1000);
        rotate.setDuration(1000);
        set = new AnimationSet (true);
        set.addAnimation(rotate);
        set.addAnimation(translate);
        deck.startAnimation(set);
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                thirdPlayerLeft.setVisibility(thirdPlayerLeft.VISIBLE);
            }
        }.start();


    }
    private void deckToFourthLeft() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        Animation translate;
        Animation rotate;
        AnimationSet set;
        fourthPlayerLeft.getLocationOnScreen(cardcoordinates);
        deck.getLocationOnScreen(deckcoordinates);
        translate = new TranslateAnimation(0, cardcoordinates[0] - deckcoordinates[0], 0, cardcoordinates[1] - deckcoordinates[1]);
        rotate = new RotateAnimation(0, 90, deck.getPivotX(), deck.getPivotY());
        translate.setDuration(1000);
        rotate.setDuration(1000);
        set = new AnimationSet (true);
        set.addAnimation(rotate);
        set.addAnimation(translate);
        deck.startAnimation(set);
        new CountDownTimer(1000, 1000) {
            public void onTick(long millisUntilFinished) {}

            public void onFinish() {
                fourthPlayerLeft.setVisibility(fourthPlayerLeft.VISIBLE);
            }
        }.start();

    }

    private void firstRightToDeck() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        deck.getLocationOnScreen(deckcoordinates);
        firstPlayerRight.getLocationOnScreen(cardcoordinates);
        Animation translate = new TranslateAnimation(0, deckcoordinates[0] - cardcoordinates[0], 0, deckcoordinates[1] - cardcoordinates[1]);
        translate.setDuration(1000);
        firstPlayerRight.startAnimation(translate);
        firstPlayerRight.setVisibility(firstPlayerRight.INVISIBLE);
    };
    private void secondRightToDeck() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        deck.getLocationOnScreen(deckcoordinates);
        secondPlayerRight.getLocationOnScreen(cardcoordinates);
        Animation rotate = new RotateAnimation(0, -90, secondPlayerRight.getPivotX(), secondPlayerRight.getPivotY());
        rotate.setDuration(1000);
        Animation translateleftrigt = new TranslateAnimation(0, deckcoordinates[0] - cardcoordinates[0], 0, deckcoordinates[1] - cardcoordinates[1]);
        translateleftrigt.setDuration(1000);
        AnimationSet rotateandmove = new AnimationSet(false);
        rotateandmove.addAnimation(rotate);
        rotateandmove.addAnimation(translateleftrigt);
        secondPlayerRight.startAnimation(rotateandmove);
        secondPlayerRight.setVisibility(secondPlayerRight.INVISIBLE);
    };
    private void thirdRightToDeck() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        deck.getLocationOnScreen(deckcoordinates);
        thirdPlayerRight.getLocationOnScreen(cardcoordinates);
        Animation translate = new TranslateAnimation(0, deckcoordinates[0] - cardcoordinates[0], 0, deckcoordinates[1] - cardcoordinates[1]);
        translate.setDuration(1000);
        thirdPlayerRight.startAnimation(translate);
        thirdPlayerRight.setVisibility(thirdPlayerRight.INVISIBLE);
    };
    private void fourthRightToDeck() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        deck.getLocationOnScreen(deckcoordinates);
        fourthPlayerRight.getLocationOnScreen(cardcoordinates);
        Animation rotate = new RotateAnimation(0, 90, fourthPlayerRight.getPivotX(), fourthPlayerRight.getPivotY());
        rotate.setDuration(1000);
        Animation translateleftrigt = new TranslateAnimation(0, deckcoordinates[0] - cardcoordinates[0], 0, deckcoordinates[1] - cardcoordinates[1]);
        translateleftrigt.setDuration(1000);
        AnimationSet rotateandmove = new AnimationSet(false);
        rotateandmove.addAnimation(rotate);
        rotateandmove.addAnimation(translateleftrigt);
        fourthPlayerRight.startAnimation(rotateandmove);
        fourthPlayerRight.setVisibility(fourthPlayerRight.INVISIBLE);
    };

    private void firstLeftToDeck() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        deck.getLocationOnScreen(deckcoordinates);
        firstPlayerLeft.getLocationOnScreen(cardcoordinates);
        Animation translate = new TranslateAnimation(0, deckcoordinates[0] - cardcoordinates[0], 0, deckcoordinates[1] - cardcoordinates[1]);
        translate.setDuration(1000);
        firstPlayerLeft.startAnimation(translate);
        firstPlayerLeft.setVisibility(firstPlayerLeft.INVISIBLE);
    };
    private void secondLeftToDeck() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        deck.getLocationOnScreen(deckcoordinates);
        secondPlayerLeft.getLocationOnScreen(cardcoordinates);
        Animation rotate = new RotateAnimation(0, -90, secondPlayerLeft.getPivotX(), secondPlayerLeft.getPivotY());
        rotate.setDuration(1000);
        Animation translateleftrigt = new TranslateAnimation(0, deckcoordinates[0] - cardcoordinates[0], 0, deckcoordinates[1] - cardcoordinates[1]);
        translateleftrigt.setDuration(1000);
        AnimationSet rotateandmove = new AnimationSet(false);
        rotateandmove.addAnimation(rotate);
        rotateandmove.addAnimation(translateleftrigt);
        secondPlayerLeft.startAnimation(rotateandmove);
        secondPlayerLeft.setVisibility(secondPlayerLeft.INVISIBLE);
    };
    private void thirdLeftToDeck() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        deck.getLocationOnScreen(deckcoordinates);
        thirdPlayerLeft.getLocationOnScreen(cardcoordinates);
        Animation translate = new TranslateAnimation(0, deckcoordinates[0] - cardcoordinates[0], 0, deckcoordinates[1] - cardcoordinates[1]);
        translate.setDuration(1000);
        thirdPlayerLeft.startAnimation(translate);
        thirdPlayerLeft.setVisibility(thirdPlayerLeft.INVISIBLE);
    };
    private void fourthLeftToDeck() {
        int[] cardcoordinates = new int[2];
        int[] deckcoordinates = new int[2];
        deck.getLocationOnScreen(deckcoordinates);
        fourthPlayerLeft.getLocationOnScreen(cardcoordinates);
        Animation rotate = new RotateAnimation(0, 90, fourthPlayerLeft.getPivotX(), fourthPlayerLeft.getPivotY());
        rotate.setDuration(1000);
        Animation translateleftrigt = new TranslateAnimation(0, deckcoordinates[0] - cardcoordinates[0], 0, deckcoordinates[1] - cardcoordinates[1]);
        translateleftrigt.setDuration(1000);
        AnimationSet rotateandmove = new AnimationSet(false);
        rotateandmove.addAnimation(rotate);
        rotateandmove.addAnimation(translateleftrigt);
        fourthPlayerLeft.startAnimation(rotateandmove);
        fourthPlayerLeft.setVisibility(fourthPlayerLeft.INVISIBLE);
    };

    //currently just does animation, from back to front.
    private void flipCard(final View toFlip) {

        new CountDownTimer(300, 100) {
            int a = 0;
            @Override
            public void onTick(long millisUntilFinished) {
                if (a == 0) {
                    final AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                            R.animator.flip_right_out);
                    setRightOut.setTarget(toFlip);
                    setRightOut.start();
                    a++;
                }
                else if (a ==1) {
                    toFlip.setBackgroundResource(R.drawable.background_trans);
                    final AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                            R.animator.flight_left_in);
                    setLeftIn.setTarget(toFlip);
                    setLeftIn.start();
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }
    //front to back
    private void flipCardToBack(final View toFlip) {
        new CountDownTimer(300, 100) {
            int a = 0;
            @Override
            public void onTick(long millisUntilFinished) {
                if (a == 0) {
                    final AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                            R.animator.flip_right_out);
                    setRightOut.setTarget(toFlip);
                    setRightOut.start();
                    a++;
                }
                else if (a ==1) {
                    toFlip.setBackgroundResource(R.drawable.background_up);
                    final AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                            R.animator.flight_left_in);
                    setLeftIn.setTarget(toFlip);
                    setLeftIn.start();
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private void cardToCenterSinglePlayer(Player on, int hand) {
        //left
        int playerNum = on.getPlayerNumber();
        if (hand == 0) {
            if (playerNum == 1) {
                firstLeftToDeck();
            }
            else if (playerNum == 2) {
                secondLeftToDeck();
            }
            else if (playerNum == 3) {
                thirdLeftToDeck();
            }
            else {
                fourthLeftToDeck();
            }
        }
        //right
        else {
            if (playerNum == 1) {
                firstRightToDeck();
            }
            else if (playerNum == 2) {
                secondRightToDeck();
            }
            else if (playerNum == 3) {
                thirdRightToDeck();
            }
            else {
                fourthRightToDeck();
            }
        }
    }
    private void cardToCenterMultiPlayer(final int hand) {
        if (hand == 0) firstLeftToDeck();
        else firstRightToDeck();
    }


    //also ends turn
    private void imageZoomToOpen(final Player on, final int hand, final View toFlip) {
        Animation zoomOutImage = AnimationUtils.loadAnimation(this, R.anim.anim_scale_up);
        Animation zoomOutImage1 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_up);
        Animation zoomOutImage2 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_up);
        Animation zoomOutImage3 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_up);
        backgroundOnPaused.setVisibility(View.VISIBLE);
        expandedCardImage.startAnimation(zoomOutImage);
        expandedCardImage.setImageResource(R.drawable.background_trans);
        bPlay.startAnimation(zoomOutImage1);
        bCancel.startAnimation(zoomOutImage2);
        cardDescriptionText.startAnimation(zoomOutImage3);
        expandedCardImage.setVisibility(View.VISIBLE);
        bPlay.setVisibility(View.VISIBLE);
        bPlay.setClickable(true);
        bPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageZoomToClose();
                CountDownTimer t = new CountDownTimer(2000, 500) {
                    boolean done = false;
                    boolean otherDone = false;
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (!done) {
                            //flipBack method here
                            if (hand == 0) {
                                flipCardToBack(firstPlayerRight);
                            }
                            else {
                                flipCardToBack(firstPlayerLeft);
                            }
                            done = true;
                        }
                        else if (!otherDone) {
                            cardToCenterMultiPlayer(hand);
                            otherDone = true;
                        }

                    }

                    @Override
                    public void onFinish() {
                        on.playCard(hand);
                        firstPlayerLeft.setBackgroundResource(R.drawable.background_up);
                        firstPlayerRight.setBackgroundResource(R.drawable.background_up);
                    }
                };
                t.start();



            }
        });
        bCancel.setVisibility(View.VISIBLE);
        bCancel.setClickable(true);
        cardDescriptionText.setText(on.getCard(hand).getDescription(this));
        cardDescriptionText.setVisibility(View.VISIBLE);

    }
    private void imageZoomToClose() {
        bPlay.setClickable(false);
        bCancel.setClickable(false);
        Animation zoomOut = AnimationUtils.loadAnimation(this, R.anim.anim_scale_down);
        Animation zoomOut1 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_down);
        Animation zoomOut2 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_down);
        Animation zoomOut3 = AnimationUtils.loadAnimation(this, R.anim.anim_scale_down);
        backgroundOnPaused.setVisibility(View.INVISIBLE);
        expandedCardImage.startAnimation(zoomOut);
        bPlay.startAnimation(zoomOut1);
        bCancel.startAnimation(zoomOut2);
        cardDescriptionText.startAnimation(zoomOut3);
        expandedCardImage.setVisibility(View.INVISIBLE);
        bPlay.setVisibility(View.INVISIBLE);
        bCancel.setVisibility(View.INVISIBLE);
        cardDescriptionText.setVisibility(View.INVISIBLE);
    }
    private void handOutCards(final int firstPlayer) {
        new CountDownTimer(7000, 1000) {
            int a = -1;
            public void onTick(long millisUntilFinished) {
                if (a == 0) {
                    int[] cardcoordinates = new int[2];
                    int[] deckcoordinates = new int[2];
                    Animation translate;
                    AnimationSet set;
                    outCard.getLocationOnScreen(cardcoordinates);
                    deck.getLocationOnScreen(deckcoordinates);
                    translate = new TranslateAnimation(0, cardcoordinates[0] - deckcoordinates[0], 0, cardcoordinates[1] - deckcoordinates[1]);
                    translate.setDuration(1000);
                    set = new AnimationSet (true);
                    set.addAnimation(translate);
                    deck.startAnimation(set);
                    new CountDownTimer(1000, 1000) {
                        public void onTick(long millisUntilFinished) {}

                        public void onFinish() {
                            outCard.setVisibility(firstPlayerLeft.VISIBLE);
                        }
                    }.start();
                    a = firstPlayer;
                }
                else if (a == 1) {
                    a = 2;
                    deckToFirstLeft();
                }
                else if (a == 2) {
                    a = 3;
                    deckToSecondLeft();
                }
                else if (a == 3) {
                    a = 4;
                    deckToThirdLeft();
                }
                else if (a == 4) {
                    a = 1;
                    deckToFourthLeft();
                }
                else {
                    a = 0;
                }

            }

            public void onFinish() {
                AlertDialog.Builder preGame = new AlertDialog.Builder(Game.this);
                preGame.setMessage("Player " + GameData.TURN + " is up.\nPlease pass to player and select OK when ready.");
                DialogInterface.OnClickListener ok = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        multiPlayerGame();
                    }
                };
                preGame.setPositiveButton("OK", ok);
                preGame.setCancelable(false);
                preGame.show();
            }
        }.start();
    }



    //other methods
    private void repaint() {
        int turn = GameData.TURN;
        //person who turn is now is set to main view. Scenario: turn = 1 therefore redraw player 1 to center.
        if (GameData.PlayerList[turn].hasLeft) {
            firstPlayerLeft.setVisibility(View.VISIBLE);
            firstPlayerRight.setVisibility(View.INVISIBLE);
        }
        else if (GameData.PlayerList[turn].hasRight){
            firstPlayerLeft.setVisibility(View.INVISIBLE);
            firstPlayerRight.setVisibility(View.VISIBLE);
        }
        else {
            firstPlayerLeft.setVisibility(View.INVISIBLE);
            firstPlayerRight.setVisibility(View.INVISIBLE);
        }
        turn++;
        if (turn == 5) {
            turn = 1;
        }
        //set next players location, scn: turn 1 therefore redraw player 2 to left side.
        if (GameData.PlayerList[turn].hasLeft) {
            secondPlayerLeft.setVisibility(View.VISIBLE);
            secondPlayerRight.setVisibility(View.INVISIBLE);
        }
        else if (GameData.PlayerList[turn].hasRight){
            secondPlayerLeft.setVisibility(View.INVISIBLE);
            secondPlayerRight.setVisibility(View.VISIBLE);
        }
        else {
            secondPlayerLeft.setVisibility(View.INVISIBLE);
            secondPlayerRight.setVisibility(View.INVISIBLE);
        }
        turn++;
        if (turn == 5) {
            turn = 1;
        }
        //set next players location, scn: turn 1 therefore redraw player 3 to top side.
        if (GameData.PlayerList[turn].hasLeft) {
            thirdPlayerLeft.setVisibility(View.VISIBLE);
            thirdPlayerRight.setVisibility(View.INVISIBLE);
        }
        else if (GameData.PlayerList[turn].hasRight){
            thirdPlayerLeft.setVisibility(View.INVISIBLE);
            thirdPlayerRight.setVisibility(View.VISIBLE);
        }
        else {
            thirdPlayerLeft.setVisibility(View.INVISIBLE);
            thirdPlayerRight.setVisibility(View.INVISIBLE);
        }
        turn++;
        if (turn == 5) {
            turn = 1;
        }
        //set next players location, scn: turn 1 therefore redraw player 4 to right side.
        if (GameData.PlayerList[turn].hasLeft) {
            fourthPlayerLeft.setVisibility(View.VISIBLE);
            fourthPlayerRight.setVisibility(View.INVISIBLE);
        }
        else if (GameData.PlayerList[turn].hasRight){
            fourthPlayerLeft.setVisibility(View.INVISIBLE);
            fourthPlayerRight.setVisibility(View.VISIBLE);
        }
        else {
            fourthPlayerLeft.setVisibility(View.INVISIBLE);
            fourthPlayerRight.setVisibility(View.INVISIBLE);
        }
    }

    public void clearTable() {
        firstPlayerLeft.setClickable(false);
        firstPlayerLeft.setOnClickListener(null);
        firstPlayerLeft.setVisibility(View.INVISIBLE);
        firstPlayerRight.setClickable(false);
        firstPlayerRight.setOnClickListener(null);
        firstPlayerRight.setVisibility(View.INVISIBLE);
        secondPlayerLeft.setClickable(false);
        secondPlayerLeft.setOnClickListener(null);
        secondPlayerLeft.setVisibility(View.INVISIBLE);
        secondPlayerRight.setClickable(false);
        secondPlayerRight.setOnClickListener(null);
        secondPlayerRight.setVisibility(View.INVISIBLE);
        thirdPlayerLeft.setClickable(false);
        thirdPlayerLeft.setOnClickListener(null);
        thirdPlayerLeft.setVisibility(View.INVISIBLE);
        thirdPlayerRight.setClickable(false);
        thirdPlayerRight.setOnClickListener(null);
        thirdPlayerRight.setVisibility(View.INVISIBLE);
        fourthPlayerLeft.setClickable(false);
        fourthPlayerLeft.setOnClickListener(null);
        fourthPlayerLeft.setVisibility(View.INVISIBLE);
        fourthPlayerRight.setClickable(false);
        fourthPlayerRight.setOnClickListener(null);
        fourthPlayerRight.setVisibility(View.INVISIBLE);
        outCard.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
