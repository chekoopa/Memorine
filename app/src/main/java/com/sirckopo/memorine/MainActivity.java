package com.sirckopo.memorine;

import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetStones();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    final private int pairsCount = 8;
    private Integer stones[] = new Integer[pairsCount * 2];
    private int preClick = -1;
    private int postClick = -1;
    private int pairsLeft = pairsCount;
    private int tries = 0;

    private void resetStones() {
        preClick = -1;
        postClick = -1;
        pairsLeft = pairsCount;
        tries = 0;
        stones = new Integer[pairsCount * 2];
        for (int i = 0; i < pairsCount; i++) {
            stones[2 * i] = i + 1;
            stones[2 * i + 1] = i + 1;
        }
        shuffleArray(stones);
    }

    static void shuffleArray(Integer[] ar)
    {
        Random rnd = new Random();
        for (int i = ar.length - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    private Button StoneButton(View g, int index) {
        return (Button) g.findViewWithTag("s" + index);
    }

    public void StoneClick(View v) throws InterruptedException {
        int index = Integer.parseInt(v.getTag().toString().substring(1));
        if (stones[index] == 0) {
            return;
        }

        Button b = (Button) v;
        if (preClick != -1) {
            if ((postClick != -1) || (preClick == index))
                return;

            b.setText(stones[index].toString());
            postClick = index;
            tries++;
            TextView tw = (TextView) findViewById(R.id.textView1);
            tw.setText(Integer.toString(tries));

            if (stones[preClick].equals(stones[postClick])) {
                stones[preClick] = 0;
                stones[postClick] = 0;
                StoneButton(findViewById(R.id.gridLayout1), preClick).setEnabled(false);
                b.setEnabled(false);
                preClick = -1;
                postClick = -1;
                pairsLeft--;

                setAnimationIfWon();
            } else {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        StoneButton(findViewById(R.id.gridLayout1), preClick).setText("");
                        StoneButton(findViewById(R.id.gridLayout1), postClick).setText("");
                        preClick = -1;
                        postClick = -1;
                    }
                }, 500);
            }

        } else {
            preClick = index;
            b.setText(stones[index].toString());
        }
    }

    private void delayWinAnimation(int delay) {
        // stones numbers
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pairsLeft != 0)
                    return;
                for (int i = 0; i < pairsCount * 2; i++) {
                    Button b = StoneButton(findViewById(R.id.gridLayout1), i);
                    if (b.getText() == "") {
                        final String greeting = "WIENER";
                        b.setText(String.valueOf(greeting.charAt(i % greeting.length()))); // sic!
                    } else {
                        b.setText("");
                    }
                }
            }
        }, delay);
    }

    private void setAnimationIfWon() {
        final int animationDelay = 500;
        final int blinkDelay = 200;
        final int blinkCount = 5;
        if (pairsLeft == 0) {
            for (int i = 0; i <= blinkCount; i++) {
                delayWinAnimation(animationDelay + blinkDelay * i);
                delayWinAnimation(animationDelay + (blinkDelay + blinkDelay / 2) * i);
            }
        }
    }

    public void StonesRegenerate(View v) {
        resetStones();
        for (int i = 0; i < pairsCount * 2; i++) {
            Button b = StoneButton(findViewById(R.id.gridLayout1), i);
            b.setEnabled(true);
            b.setText("");
        }
        TextView tw = (TextView) findViewById(R.id.textView1);
        tw.setText("0");
    }

}
