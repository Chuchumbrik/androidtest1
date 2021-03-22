package space.test.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // count_money - количество основной валюты "денег"
    // total_score - общее количество очков заработанных за игру
    static public int count_money = 0;
    static public int total_score = 0;

    // add_money_tap - кол-во денег за одно нажатие
    int add_money_tap = 1;

    // cost_buy_1_lvl - стоимость увлечения клика 1 лвл
    // cost_buy_2_lvl - стоимость увлечения клика 2 лвл
    // cost_buy_2_lvl - стоимость увлечения клика 3 лвл
    float cost_buy_1_lvl = 10;
    float cost_buy_2_lvl = 100;
    float cost_buy_3_lvl = 1000;

    // count_buy_1_lvl - Количество купленных улучшений 1 лвл
    // count_buy_2_lvl - Количество купленных улучшений 2 лвл
    // count_buy_3_lvl - Количество купленных улучшений 3 лвл
    int count_buy_1_lvl = 0;
    int count_buy_2_lvl = 0;
    int count_buy_3_lvl = 0;

    public static boolean enabled_buy_1_lvl_auto = false;


    Button tap_button;
    Button button_buy_1_lvl;
    Button button_buy_2_lvl;
    Button button_buy_3_lvl;
    TextView text_money;
    TextView TextView_total_score;
    TextView TextView_cost_buy_1_lvl;
    TextView TextView_cost_buy_2_lvl;
    TextView TextView_cost_buy_3_lvl;

    TextView TextView_count_buy_1_lvl;
    TextView TextView_count_buy_2_lvl;
    TextView TextView_count_buy_3_lvl;
    TextView Shop;

    TextView TextView_add_count_money;

    Timer timer;
    TimerTask mTimerTask;

    static public SharedPreferences preferences;

    Button reset;

    Button test_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_data_reset();
            }
        });

        tap_button = (Button) findViewById(R.id.tap_tap);
        button_buy_1_lvl = (Button) findViewById(R.id.button_buy_1_lvl);
        button_buy_2_lvl = (Button) findViewById(R.id.button_buy_2_lvl);
        button_buy_3_lvl = (Button) findViewById(R.id.button_buy_3_lvl);

        TextView_cost_buy_1_lvl = (TextView)findViewById(R.id.TextView_cost_buy_1_lvl);
        TextView_cost_buy_1_lvl.setText(cost_buy_1_lvl + "");
        TextView_cost_buy_2_lvl = (TextView)findViewById(R.id.TextView_cost_buy_2_lvl);
        TextView_cost_buy_2_lvl.setText(cost_buy_2_lvl + "");
        TextView_cost_buy_3_lvl = (TextView)findViewById(R.id.TextView_cost_buy_3_lvl);
        TextView_cost_buy_3_lvl.setText(cost_buy_3_lvl + "");

        TextView_count_buy_1_lvl = (TextView)findViewById(R.id.TextView_count_buy_1_lvl);
        count_buy_1_lvl = 0;
        TextView_count_buy_1_lvl.setText(count_buy_1_lvl + "");
        TextView_count_buy_2_lvl = (TextView)findViewById(R.id.TextView_count_buy_2_lvl);
        count_buy_2_lvl = 0;
        TextView_count_buy_2_lvl.setText(count_buy_2_lvl + "");
        TextView_count_buy_3_lvl = (TextView)findViewById(R.id.TextView_count_buy_3_lvl);
        count_buy_3_lvl = 0;
        TextView_count_buy_3_lvl.setText(count_buy_3_lvl + "");

        TextView_add_count_money = (TextView)findViewById(R.id.add_count_money);
        TextView_add_count_money.setText(add_money_tap + "");

        Shop = (Button) findViewById(R.id.shop);
        Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_data();
                Intent intent = new Intent(MainActivity.this, Improve.class);
                startActivity(intent);
            }
        });

        test_1 = (Button) findViewById(R.id.test_1);
        test_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_data();
                Intent intent = new Intent(MainActivity.this, test_1.class);
                startActivity(intent);
            }
        });

        text_money = (TextView)findViewById(R.id.money);
        TextView_total_score = (TextView)findViewById(R.id.total_score);
        Tap();
        button_buy_1_lvl();
        button_buy_2_lvl();
        button_buy_3_lvl();

        load_save_data();
        if (enabled_buy_1_lvl_auto == true) {
            timer = new Timer();
            mTimerTask = new MyTimerTaskAddMoneyAuto();

            timer.schedule(mTimerTask, 1000, 1000);
        }

        class MyTimerTask extends TimerTask {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                    public void run() {
                        text_money.setText(count_money + "");
                        TextView_total_score.setText(total_score + "");
                        save_data();
                    }
                });
            }
        }

        timer = new Timer();
        mTimerTask = new MyTimerTask();

        timer.schedule(mTimerTask, 1000, 1000);
        save_data();
    }

    public static class MyTimerTaskAddMoneyAuto extends TimerTask {
        @Override
        public void run() {
            MainActivity.count_money += Improve.count_buy_1_lvl_auto;
            MainActivity.total_score += Improve.count_buy_1_lvl_auto;
        }
    }

    public void total_score(int add_score) {
        total_score += add_score;
        TextView_total_score.setText(total_score + "");
    }

    void Tap() {
        tap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count_money += add_money_tap;
                total_score(add_money_tap);
                text_money.setText(count_money + "");
                save_data();
            }
        });
    }

    void button_buy_1_lvl() {
        button_buy_1_lvl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count_money >= cost_buy_1_lvl) {
                    count_money -= cost_buy_1_lvl;
                    cost_buy_1_lvl *= 1.1;
                    add_money_tap += 1;
                    TextView_cost_buy_1_lvl.setText(cost_buy_1_lvl + "");
                    TextView_add_count_money.setText(add_money_tap + "");
                    count_buy_1_lvl ++;
                    TextView_count_buy_1_lvl.setText(count_buy_1_lvl + "");
                    save_data();
                }
            }
        });
    }

    void button_buy_2_lvl() {
        button_buy_2_lvl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count_money >= cost_buy_2_lvl) {
                    count_money -= cost_buy_2_lvl;
                    cost_buy_2_lvl *= 1.1;
                    add_money_tap += 5;
                    TextView_cost_buy_2_lvl.setText(cost_buy_2_lvl + "");
                    TextView_add_count_money.setText(add_money_tap + "");
                    count_buy_2_lvl ++;
                    TextView_count_buy_2_lvl.setText(count_buy_2_lvl + "");
                    save_data();
                }
            }
        });
    }

    void button_buy_3_lvl() {
        button_buy_3_lvl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count_money >= cost_buy_3_lvl) {
                    count_money -= cost_buy_3_lvl;
                    cost_buy_3_lvl *= 1.1;
                    add_money_tap += 10;
                    TextView_cost_buy_3_lvl.setText(cost_buy_3_lvl + "");
                    TextView_add_count_money.setText(add_money_tap + "");
                    count_buy_3_lvl ++;
                    TextView_count_buy_3_lvl.setText(count_buy_3_lvl + "");
                    save_data();
                }
            }
        });
    }

    void save_data() {
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("count_money", count_money);
        editor.putInt("total_score", total_score);
        editor.putInt("add_money_tap", add_money_tap);
        editor.putFloat("cost_buy_1_lvl", cost_buy_1_lvl);
        editor.putFloat("cost_buy_2_lvl", cost_buy_2_lvl);
        editor.putFloat("cost_buy_3_lvl", cost_buy_3_lvl);
        editor.putInt("count_buy_1_lvl", count_buy_1_lvl);
        editor.putInt("count_buy_2_lvl", count_buy_2_lvl);
        editor.putInt("count_buy_3_lvl", count_buy_3_lvl);
        editor.putBoolean("enabled_buy_1_lvl_auto", enabled_buy_1_lvl_auto);
        editor.putInt("count_buy_1_lvl_auto", Improve.count_buy_1_lvl_auto);
        editor.commit();
    }

    void load_save_data() {
        preferences = getPreferences(MODE_PRIVATE);
        count_money = preferences.getInt("count_money", 0);
        total_score = preferences.getInt("total_score", 0);
        add_money_tap = preferences.getInt("add_money_tap", 0);
        cost_buy_1_lvl = preferences.getFloat("cost_buy_1_lvl", 0);
        cost_buy_2_lvl = preferences.getFloat("cost_buy_2_lvl", 0);
        cost_buy_3_lvl = preferences.getFloat("cost_buy_3_lvl", 0);
        count_buy_1_lvl = preferences.getInt("count_buy_1_lvl", 0);
        count_buy_2_lvl = preferences.getInt("count_buy_2_lvl", 0);
        count_buy_3_lvl = preferences.getInt("count_buy_3_lvl", 0);
        enabled_buy_1_lvl_auto = preferences.getBoolean("enabled_buy_1_lvl_auto", false);
        Improve.count_buy_1_lvl_auto = preferences.getInt("count_buy_1_lvl_auto", 0);

        TextView_cost_buy_1_lvl.setText(cost_buy_1_lvl + "");
        TextView_cost_buy_2_lvl.setText(cost_buy_2_lvl + "");
        TextView_cost_buy_3_lvl.setText(cost_buy_3_lvl + "");
        TextView_count_buy_1_lvl.setText(count_buy_1_lvl + "");
        TextView_count_buy_2_lvl.setText(count_buy_2_lvl + "");
        TextView_count_buy_3_lvl.setText(count_buy_3_lvl + "");
        text_money.setText(count_money + "");
        TextView_total_score.setText(total_score + "");
        TextView_add_count_money.setText(add_money_tap + "");
    }

    void save_data_reset() {
        preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("count_money", 0);
        editor.putInt("total_score", 0);
        editor.putInt("add_money_tap", 0);
        editor.putFloat("cost_buy_1_lvl", 0);
        editor.putFloat("cost_buy_2_lvl", 0);
        editor.putFloat("cost_buy_3_lvl", 0);
        editor.putInt("count_buy_1_lvl", 0);
        editor.putInt("count_buy_2_lvl", 0);
        editor.putInt("count_buy_3_lvl", 0);
        editor.putBoolean("enabled_buy_1_lvl_auto", false);
        editor.putInt("count_buy_1_lvl_auto", 0);
        editor.putFloat("cost_buy_1_lvl_auto", 0);
        editor.commit();
        finishAffinity();
    }
}