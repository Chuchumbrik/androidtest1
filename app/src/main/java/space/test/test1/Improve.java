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

    Button button_buy_1_lvl_auto;
    TextView TextView_cost_buy_1_lvl_auto;
    TextView TextView_count_buy_1_lvl_auto;

    Timer timer;
    TimerTask mTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improve);

        init();
        //load_save_data();
        output_to_the_screen();
        click_handler();

    }

    void init() {
        Close_shop = (Button) findViewById(R.id.Close_shop);

        TextView_cost_buy_1_lvl_auto = (TextView)findViewById(R.id.TextView_cost_buy_1_lvl);

        button_buy_1_lvl_auto = (Button) findViewById(R.id.button_buy_1_lvl_auto);
        TextView_count_buy_1_lvl_auto = (TextView)findViewById(R.id.TextView_count_buy_1_lvl_auto);
    }

    void output_to_the_screen() {
        TextView_cost_buy_1_lvl_auto.setText(MainActivity.cost_buy_1_lvl_auto + "");
        TextView_count_buy_1_lvl_auto.setText(MainActivity.count_buy_1_lvl_auto + "");
    }

    void click_handler() {
        Close_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save_data();
                Intent intent = new Intent(Improve.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button_buy_1_lvl_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.count_money >= MainActivity.cost_buy_1_lvl_auto) {
                    if (!(MainActivity.enabled_buy_1_lvl_auto) && (MainActivity.count_timer == 0)) {
                        timer = new Timer();
                        mTimerTask = new MainActivity.MyTimerTaskAddMoneyAuto();

                        timer.schedule(mTimerTask, 1000, 1000);
                        MainActivity.enabled_buy_1_lvl_auto = true;
                        MainActivity.count_timer ++;
                    }

                    MainActivity.count_money -= MainActivity.cost_buy_1_lvl_auto;
                    MainActivity.cost_buy_1_lvl_auto *= 1.1;
                    TextView_cost_buy_1_lvl_auto.setText(MainActivity.cost_buy_1_lvl_auto + "");
                    MainActivity.count_buy_1_lvl_auto ++;
                    TextView_count_buy_1_lvl_auto.setText(MainActivity.count_buy_1_lvl_auto + "");
                    save_data();
                }
            }
        });
    }

    void save_data() {
        MainActivity.preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = MainActivity.preferences.edit();
        editor.putFloat("cost_buy_1_lvl_auto", MainActivity.cost_buy_1_lvl_auto);
        editor.putInt("count_buy_1_lvl_auto", MainActivity.count_buy_1_lvl_auto);
        editor.commit();
    }

    void load_save_data() {
        MainActivity.preferences = getPreferences(MODE_PRIVATE);
        MainActivity.cost_buy_1_lvl_auto = MainActivity.preferences.getFloat("cost_buy_1_lvl_auto", MainActivity.default_cost_buy_1_lvl);
        MainActivity.count_buy_1_lvl_auto = MainActivity.preferences.getInt("count_buy_1_lvl_auto", MainActivity.default_count_buy);

        output_to_the_screen();
    }
}