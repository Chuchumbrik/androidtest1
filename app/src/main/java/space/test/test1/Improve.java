package space.test.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class Improve extends AppCompatActivity {

    Button Close_shop;

    // cost_buy_1_lvl_auto - стоимость улучшения автоклика 1 лвл
    float cost_buy_1_lvl_auto = 10;
    // count_buy_1_lvl - Количество купленных улучшений автоклика 1 лвл
    public static int count_buy_1_lvl_auto = 0;

    Button button_buy_1_lvl_auto;
    TextView TextView_cost_buy_1_lvl_auto;
    TextView TextView_count_buy_1_lvl_auto;

    Timer timer;
    TimerTask mTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improve);

        Close_shop = (Button) findViewById(R.id.Close_shop);
        Close_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_data();
                Intent intent = new Intent(Improve.this, MainActivity.class);
                startActivity(intent);
            }
        });

        TextView_cost_buy_1_lvl_auto = (TextView)findViewById(R.id.TextView_cost_buy_1_lvl);
        TextView_cost_buy_1_lvl_auto.setText(cost_buy_1_lvl_auto + "");
        button_buy_1_lvl_auto = (Button) findViewById(R.id.button_buy_1_lvl_auto);
        TextView_count_buy_1_lvl_auto = (TextView)findViewById(R.id.TextView_count_buy_1_lvl_auto);
        TextView_count_buy_1_lvl_auto.setText(count_buy_1_lvl_auto + "");

        load_save_data();

        button_buy_1_lvl_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.count_money >= cost_buy_1_lvl_auto) {
                    MainActivity.count_money -= cost_buy_1_lvl_auto;
                    cost_buy_1_lvl_auto *= 1.1;
                    TextView_cost_buy_1_lvl_auto.setText(cost_buy_1_lvl_auto + "");
                    count_buy_1_lvl_auto ++;
                    TextView_count_buy_1_lvl_auto.setText(count_buy_1_lvl_auto + "");

                    if (MainActivity.enabled_buy_1_lvl_auto == false) {
                        timer = new Timer();
                        mTimerTask = new MainActivity.MyTimerTaskAddMoneyAuto();

                        timer.schedule(mTimerTask, 1000, 1000);
                        MainActivity.enabled_buy_1_lvl_auto = true;
                    }
                    save_data();
                }
            }
        });
    }

    void save_data() {
        MainActivity.preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = MainActivity.preferences.edit();
        editor.putFloat("cost_buy_1_lvl_auto", cost_buy_1_lvl_auto);
        editor.putInt("count_buy_1_lvl_auto", count_buy_1_lvl_auto);
        editor.commit();
    }

    void load_save_data() {
        MainActivity.preferences = getPreferences(MODE_PRIVATE);
        cost_buy_1_lvl_auto = MainActivity.preferences.getFloat("cost_buy_1_lvl_auto", 10);
        count_buy_1_lvl_auto = MainActivity.preferences.getInt("count_buy_1_lvl_auto", 0);

        TextView_count_buy_1_lvl_auto.setText(count_buy_1_lvl_auto + "");
        TextView_cost_buy_1_lvl_auto.setText(cost_buy_1_lvl_auto + "");
    }
}