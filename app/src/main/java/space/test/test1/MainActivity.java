package space.test.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
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
    //Значения по умолчанию для количества денег и общего счета
    final int default_money_and_score = 0;

    // add_money_tap - кол-во денег за одно нажатие
    int add_money_tap;
    //Значения по умолчанию для количества денег за один клик
    final int default_add_money_tap = 1;

    // cost_buy_1_lvl - стоимость увлечения клика 1 лвл
    // cost_buy_2_lvl - стоимость увлечения клика 2 лвл
    // cost_buy_3_lvl - стоимость увлечения клика 3 лвл
    float cost_buy_1_lvl;
    float cost_buy_2_lvl;
    float cost_buy_3_lvl;
    // cost_buy_1_lvl_auto - стоимость улучшения автоклика 1 лвл
    public static float cost_buy_1_lvl_auto;
    //Значения по умолчанию для цен увелечения клика
    final public static int default_cost_buy_1_lvl = 1;
    final public static int default_cost_buy_2_lvl = 10;
    final public static int default_cost_buy_3_lvl = 100;

    // count_buy_1_lvl - Количество купленных улучшений 1 лвл
    // count_buy_2_lvl - Количество купленных улучшений 2 лвл
    // count_buy_3_lvl - Количество купленных улучшений 3 лвл
    int count_buy_1_lvl;
    int count_buy_2_lvl;
    int count_buy_3_lvl;
    // count_buy_1_lvl - Количество купленных улучшений автоклика 1 лвл
    public static int count_buy_1_lvl_auto;
    //Значения по умолчанию для количества улучшений
    final public static int default_count_buy = 0;

    //Переключатель задачи по таймеру автоклика
    public static boolean enabled_buy_1_lvl_auto = false;

    public static int count_timer = 0;


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

    TextView TextView_cost_buy_auto;
    TextView TextView_count_buy_auto;
    TextView TextView_count_timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Объявление переменных для их объектов на экране
        init();

        //Загрузка данных
        load_save_data();

        //Вывод информации на экран
        output_to_the_screen();

        //Все функции связанные с отслеживанием кликов по экрану
        click_handler();

        //Запуск таймера автокликера
        if ((enabled_buy_1_lvl_auto) && (count_timer == 0)) {
            timer = new Timer();
            mTimerTask = new MyTimerTaskAddMoneyAuto();

            timer.schedule(mTimerTask, 1000, 1000);
            MainActivity.count_timer ++;
        }
        output_to_the_screen();

        class MyTimerTask extends TimerTask {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable(){
                    @Override
                    public void run() {
                        output_count_money();
                        output_total_score();
                        save_data();
                    }
                });
            }
        }

        //запуск таймера обновления каждую секунду общего счета и нынешнего количества денег
        timer = new Timer();
        mTimerTask = new MyTimerTask();

        timer.schedule(mTimerTask, 1000, 1000);
        save_data();
    }

    void init() {
        reset = (Button) findViewById(R.id.reset);

        tap_button = (Button) findViewById(R.id.tap_tap);
        button_buy_1_lvl = (Button) findViewById(R.id.button_buy_1_lvl);
        button_buy_2_lvl = (Button) findViewById(R.id.button_buy_2_lvl);
        button_buy_3_lvl = (Button) findViewById(R.id.button_buy_3_lvl);

        TextView_cost_buy_1_lvl = (TextView)findViewById(R.id.TextView_cost_buy_1_lvl);
        TextView_cost_buy_2_lvl = (TextView)findViewById(R.id.TextView_cost_buy_2_lvl);
        TextView_cost_buy_3_lvl = (TextView)findViewById(R.id.TextView_cost_buy_3_lvl);

        TextView_count_buy_1_lvl = (TextView)findViewById(R.id.TextView_count_buy_1_lvl);
        TextView_count_buy_2_lvl = (TextView)findViewById(R.id.TextView_count_buy_2_lvl);
        TextView_count_buy_3_lvl = (TextView)findViewById(R.id.TextView_count_buy_3_lvl);

        TextView_add_count_money = (TextView)findViewById(R.id.add_count_money);

        text_money = (TextView)findViewById(R.id.money);
        TextView_total_score = (TextView)findViewById(R.id.total_score);

        Shop = (Button) findViewById(R.id.shop);

        test_1 = (Button) findViewById(R.id.test_1);

        TextView_cost_buy_auto = (TextView)findViewById(R.id.cost_buy_auto);
        TextView_count_buy_auto = (TextView)findViewById(R.id.count_buy_auto);
        TextView_count_timer = (TextView)findViewById(R.id.count_timer);
    }



    void output_to_the_screen() {
        TextView_cost_buy_1_lvl.setText(cost_buy_1_lvl + "");
        TextView_cost_buy_2_lvl.setText(cost_buy_2_lvl + "");
        TextView_cost_buy_3_lvl.setText(cost_buy_3_lvl + "");

        TextView_count_buy_1_lvl.setText(count_buy_1_lvl + "");
        TextView_count_buy_2_lvl.setText(count_buy_2_lvl + "");
        TextView_count_buy_3_lvl.setText(count_buy_3_lvl + "");

        TextView_add_count_money.setText(add_money_tap + "");

        output_count_money();
        output_total_score();

        TextView_cost_buy_auto.setText(cost_buy_1_lvl_auto + "");
        TextView_count_buy_auto.setText(count_buy_1_lvl_auto + "");

        TextView_count_timer.setText(count_timer + "");
    }

    void output_total_score() {
        TextView_total_score.setText(total_score + "");
    }

    void output_count_money() {
        text_money.setText(count_money + "");
    }

    void click_handler() {
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_data_reset();
            }
        });

        Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_data();
                Intent intent = new Intent(MainActivity.this, Improve.class);
                startActivity(intent);
            }
        });

        test_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_data();
                Intent intent = new Intent(MainActivity.this, test_1.class);
                startActivity(intent);
            }
        });

        Tap();
        button_buy_1_lvl();
        button_buy_2_lvl();
        button_buy_3_lvl();
    }

    public static class MyTimerTaskAddMoneyAuto extends TimerTask {
        @Override
        public void run() {
            MainActivity.count_money += count_buy_1_lvl_auto;
            MainActivity.total_score += count_buy_1_lvl_auto;
        }
    }

    public void total_score(int add_score) {
        total_score += add_score;
        output_total_score();
    }

    void Tap() {
        tap_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count_money += add_money_tap;
                total_score(add_money_tap);
                output_count_money();
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
                    output_count_money();
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
                    output_count_money();
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
                    output_count_money();
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
        editor.putInt("count_buy_1_lvl_auto", count_buy_1_lvl_auto);
        editor.putFloat("cost_buy_1_lvl_auto", cost_buy_1_lvl_auto);
        editor.commit();
    }

    void load_save_data() {
        preferences = getPreferences(MODE_PRIVATE);
        count_money = preferences.getInt("count_money", default_money_and_score);
        total_score = preferences.getInt("total_score", default_money_and_score);
        add_money_tap = preferences.getInt("add_money_tap", default_add_money_tap);
        cost_buy_1_lvl = preferences.getFloat("cost_buy_1_lvl", default_cost_buy_1_lvl);
        cost_buy_2_lvl = preferences.getFloat("cost_buy_2_lvl", default_cost_buy_2_lvl);
        cost_buy_3_lvl = preferences.getFloat("cost_buy_3_lvl", default_cost_buy_3_lvl);
        count_buy_1_lvl = preferences.getInt("count_buy_1_lvl", default_count_buy);
        count_buy_2_lvl = preferences.getInt("count_buy_2_lvl", default_count_buy);
        count_buy_3_lvl = preferences.getInt("count_buy_3_lvl", default_count_buy);
        enabled_buy_1_lvl_auto = preferences.getBoolean("enabled_buy_1_lvl_auto", false);
        count_buy_1_lvl_auto = preferences.getInt("count_buy_1_lvl_auto", default_count_buy);
        cost_buy_1_lvl_auto = preferences.getFloat("cost_buy_1_lvl_auto", default_cost_buy_1_lvl);

        output_to_the_screen();
    }

    void save_data_reset() {
        count_money = default_money_and_score;
        total_score = default_money_and_score;
        add_money_tap = default_add_money_tap;
        cost_buy_1_lvl = default_cost_buy_1_lvl;
        cost_buy_2_lvl = default_cost_buy_2_lvl;
        cost_buy_3_lvl = default_cost_buy_3_lvl;
        count_buy_1_lvl = default_count_buy;
        count_buy_2_lvl = default_count_buy;
        count_buy_3_lvl = default_count_buy;
        enabled_buy_1_lvl_auto = false;
        count_buy_1_lvl_auto = default_count_buy;
        cost_buy_1_lvl_auto = default_cost_buy_1_lvl;
        save_data();
        restartApp();
    }

    private void restartApp() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        int mPendingIntentId = 12345;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
        System.exit(0);
    }
}