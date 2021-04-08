package space.test.test1;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.transition.Slide;

import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    // count_money - количество основной валюты "денег"
    // total_score - общее количество очков заработанных за игру
    static public int count_money;
    static public int total_score;
    //Значения по умолчанию для количества денег и общего счета
    final int default_money_and_score = 0;

    // add_money_tap - кол-во денег за одно нажатие
    int add_money_tap;
    //Значения по умолчанию для количества денег за один клик
    final int default_add_money_tap = 1;

    //Общий множитель дохода
    static float total_multiplier;
    // множитель денег за одно нажатие
    float multiplier_add_money_tap;
    // множители улучшений кликов
    float multiplier_tap_1_lvl;
    float multiplier_tap_2_lvl;
    float multiplier_tap_3_lvl;
    //Значения по умолчанию для множителя денег за один клик
    final int default_multiplier = 1;

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
    //Количество таймеров для автокликера
    public static int count_timer = 0;

    //вариации сколько покупок за раз делается, привязанные к радиокнопкам
    final int buy_count_x1 = 1;
    final int buy_count_x5 = 5;
    final int buy_count_x10 = 10;
    final int buy_count_x100 = 100;

    // выбранное количество покупок за раз
    int buy_count;


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

    TextView TextView_total_multiplier;
    TextView TextView_multiplier_add_money_tap;
    TextView TextView_multiplier_tap_1_lvl;
    TextView TextView_multiplier_tap_2_lvl;
    TextView TextView_multiplier_tap_3_lvl;

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

    RadioGroup RadioGroup_buy_count_group;
    RadioButton RadioButton_buy_count_x1;
    RadioButton RadioButton_buy_count_x5;
    RadioButton RadioButton_buy_count_x10;
    RadioButton RadioButton_buy_count_x100;

    ImageButton shop_appearance;
    Button shop_hidde;
    ScrollView ScrollView;
    //FrameLayout FrameLayout_shop_test;
    //private Animation Animation_shop_appearance;
    //private Animation Animation_shop_hidde;
    ObjectAnimator ObjectAnimation_shop_appearance;
    ObjectAnimator ObjectAnimation_shop_hidde;

    Button ship;

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

        TextView_total_multiplier = (TextView)findViewById(R.id.TextView_total_multiplier);
        TextView_multiplier_add_money_tap = (TextView)findViewById(R.id.TextView_multiplier_add_money_tap);
        TextView_multiplier_tap_1_lvl = (TextView)findViewById(R.id.TextView_multiplier_tap_1_lvl);
        TextView_multiplier_tap_2_lvl = (TextView)findViewById(R.id.TextView_multiplier_tap_2_lvl);
        TextView_multiplier_tap_3_lvl = (TextView)findViewById(R.id.TextView_multiplier_tap_3_lvl);

        RadioGroup_buy_count_group = (RadioGroup)findViewById(R.id.RadioGroup_buy_count_group);
        RadioButton_buy_count_x1 = (RadioButton)findViewById(R.id.RadioButton_buy_count_x1);
        RadioButton_buy_count_x5 = (RadioButton)findViewById(R.id.RadioButton_buy_count_x5);
        RadioButton_buy_count_x10 = (RadioButton)findViewById(R.id.RadioButton_buy_count_x10);
        RadioButton_buy_count_x100 = (RadioButton)findViewById(R.id.RadioButton_buy_count_x100);

        shop_appearance = (ImageButton) findViewById(R.id.shop_appearance);
        shop_hidde = (Button) findViewById(R.id.shop_hidde);
        ScrollView = (ScrollView) findViewById(R.id.ScrollView);
        //FrameLayout_shop_test = (FrameLayout)findViewById(R.id.FrameLayout_shop_test);
        //Animation_shop_appearance = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation_shop_appearence);
        //Animation_shop_hidde = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation_shop_hidde);

        ship = (Button) findViewById(R.id.ship);
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

        TextView_total_multiplier.setText(total_multiplier+ "");
        TextView_multiplier_add_money_tap.setText(multiplier_add_money_tap + "");
        TextView_multiplier_tap_1_lvl.setText(multiplier_tap_1_lvl + "");
        TextView_multiplier_tap_2_lvl.setText(multiplier_tap_2_lvl + "");
        TextView_multiplier_tap_3_lvl.setText(multiplier_tap_3_lvl + "");

        int cost_buy_local;
        switch (buy_count) {
            default:
            case 1:
                RadioButton_buy_count_x1.setChecked(true);
                cost_buy_local = (int) (cost_buy_1_lvl * buy_count);
                TextView_cost_buy_1_lvl.setText(cost_buy_local + "");
                break;
            case 5:
                RadioButton_buy_count_x5.setChecked(true);
                cost_buy_local = (int) (cost_buy_1_lvl * buy_count);
                TextView_cost_buy_1_lvl.setText(cost_buy_local + "");
                break;
            case 10:
                RadioButton_buy_count_x10.setChecked(true);
                cost_buy_local = (int) (cost_buy_1_lvl * buy_count);
                TextView_cost_buy_1_lvl.setText(cost_buy_local + "");
                break;
            case 100:
                RadioButton_buy_count_x100.setChecked(true);
                cost_buy_local = (int) (cost_buy_1_lvl * buy_count);
                TextView_cost_buy_1_lvl.setText(cost_buy_local + "");
                break;
        }
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

        View.OnClickListener radioButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioButton rb = (RadioButton)v;
                switch (rb.getId()) {
                    default:
                    case R.id.RadioButton_buy_count_x1:
                        buy_count = buy_count_x1;
                        break;
                    case R.id.RadioButton_buy_count_x5:
                        buy_count = buy_count_x5;
                        break;
                    case R.id.RadioButton_buy_count_x10:
                        buy_count = buy_count_x10;
                        break;
                    case R.id.RadioButton_buy_count_x100:
                        buy_count = buy_count_x100;
                        break;
                }
                for (int i = 0; i < buy_count; i++) {
                    cost_buy_1_lvl *= 1.01;
                }
                TextView_cost_buy_1_lvl.setText(cost_buy_1_lvl + "");
            }
        };

        RadioButton_buy_count_x1.setOnClickListener(radioButtonClickListener);
        RadioButton_buy_count_x5.setOnClickListener(radioButtonClickListener);
        RadioButton_buy_count_x10.setOnClickListener(radioButtonClickListener);
        RadioButton_buy_count_x100.setOnClickListener(radioButtonClickListener);

        View.OnClickListener bottomMenuClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationBottomMenu.animationBottomMenu(ScrollView);
            }
        };

        shop_appearance.setOnClickListener(bottomMenuClickListener);
        shop_hidde.setOnClickListener(bottomMenuClickListener);

        ship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationPiwPiw();
            }
        });
    }

    public void animationPiwPiw() {
        ViewGroup sceneRoot = (ViewGroup) findViewById(R.id.shootingAnimationScene);
        // You can also inflate a generate a Scene from a layout resource file.
        final Scene scene1 = Scene.getSceneForLayout(sceneRoot, R.layout.ammo_1_scene_1, this);
        final Scene scene2 = Scene.getSceneForLayout(sceneRoot, R.layout.ammo_1_scene_2, this);
        final Scene scene3 = Scene.getSceneForLayout(sceneRoot, R.layout.ammo_1_scene_3, this);
        final Scene scene4 = Scene.getSceneForLayout(sceneRoot, R.layout.ammo_1_scene_4, this);
        final Scene sceneDefault = Scene.getSceneForLayout(sceneRoot, R.layout.default_shooting_scene, this);
        ImageView imageView = (ImageView) findViewById(R.id.piw_piw_line);
        final Handler handler = new Handler();

        // опишем свой аналог AutoTransition
        TransitionSet set = new TransitionSet();
        set.addTransition(new ChangeBounds());
        set.addTransition(new Fade());
        // выполняться они будут одновременно
        set.setOrdering(TransitionSet.ORDERING_TOGETHER);
        set.setDuration(75);
        set.setInterpolator(new AccelerateInterpolator());
        TransitionManager.go(scene1, set);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionSet set1 = new TransitionSet();
                set1.addTransition(new ChangeBounds());
                // выполняться они будут одновременно
                set1.setOrdering(TransitionSet.ORDERING_TOGETHER);
                set1.setDuration(200);
                set1.setInterpolator(new AccelerateInterpolator());
                TransitionManager.go(scene2, set1);
            }
        }, 80);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionSet set2 = new TransitionSet();
                set2.addTransition(new ChangeBounds());
                // выполняться они будут одновременно
                set2.setOrdering(TransitionSet.ORDERING_TOGETHER);
                set2.setDuration(75);
                set2.setInterpolator(new AccelerateInterpolator());
                TransitionManager.go(scene3, set2);
            }
        }, 330);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionSet set2 = new TransitionSet();
                set2.addTransition(new ChangeBounds());
                // выполняться они будут одновременно
                set2.setOrdering(TransitionSet.ORDERING_TOGETHER);
                set2.setDuration(120);
                set2.setInterpolator(new AccelerateInterpolator());
                TransitionManager.go(scene4, set2);
            }
        }, 500);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TransitionSet set2 = new TransitionSet();
                set2.addTransition(new ChangeBounds());
                set.addTransition(new Fade());
                // выполняться они будут одновременно
                set2.setOrdering(TransitionSet.ORDERING_TOGETHER);
                set2.setDuration(100);
                set2.setInterpolator(new AccelerateInterpolator());
                TransitionManager.go(sceneDefault, set2);
            }
        }, 620);




//        new CountDownTimer(1000, 1000) {
//
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            public void onFinish() {
//
//            }
//        }.start();
    }


//    //Класс отвечающий за анимацию стрельбы
//    private static class animationPiwPiw {
//
//
//        //Настройка анимации
//        //Выборка в какое состояние должен перейти контейнер
//        public static void animationPiwPiw() {
//            TransitionManager.beginDelayedTransition(frameLayout, makeSlideTransition());
//            itemPiwVisible(frameLayout);
//            itemPiwMove(frameLayout);
//            TransitionManager.beginDelayedTransition(frameLayout, makeSlideTransition());
//            itemPiwHidde(frameLayout);
//        }
//
//        private static void itemPiwHidde(FrameLayout frameLayout) {
//            frameLayout.setVisibility(View.GONE);
//        }
//
//        private static void itemPiwMove(FrameLayout frameLayout) {
//            TranslateAnimation animation = new TranslateAnimation(0, 0, 0, -400);
//            animation.setDuration(200);
//            animation.setFillAfter(true);
//            frameLayout.startAnimation(animation);
//        }
//
//        private static void itemPiwVisible(FrameLayout frameLayout) {
//            frameLayout.setVisibility(View.VISIBLE);
//        }
//
//        //Присет slide анимации
//        private static Slide makeSlideTransition() {
//            Slide slide = new Slide();
//            slide.setSlideEdge(Gravity.BOTTOM);
//            slide.setInterpolator(new AccelerateDecelerateInterpolator());
//            slide.setDuration(200);
//            return slide;
//        }
//
//        private static Slide makeSlide1Transition() {
//            Slide slide = new Slide();
//            slide.setSlideEdge(Gravity.BOTTOM);
//            slide.setInterpolator(new LinearInterpolator());
//            slide.setDuration(200);
//            return slide;
//        }
//
//        private static Slide makeSlide2Transition() {
//            Slide slide = new Slide();
//            slide.setSlideEdge(Gravity.BOTTOM);
//            slide.setInterpolator(new DecelerateInterpolator());
//            slide.setDuration(200);
//            return slide;
//        }
//    }

    //Класс отвечающий за анимацию нижнего меню
    private static class animationBottomMenu {
        //Основной метод, в который передается ScrollView
        //Настройка анимации
        //Выборка в какое состояние должен перейти контейнер
        public static void animationBottomMenu(ScrollView scrollView) {
            TransitionManager.beginDelayedTransition(scrollView, makeSlideTransition());
            switch (scrollView.getVisibility()){
                case View.VISIBLE:
                        itemBottomMenuHidde(scrollView);
                    break;
                case View.GONE:
                case View.INVISIBLE:
                    itemBottomMenuVisible(scrollView);
                    break;
            }
        }

        private static void itemBottomMenuHidde(ScrollView scrollView) {
            scrollView.setVisibility(View.GONE);
        }

        private static void itemBottomMenuVisible(ScrollView scrollView) {
            scrollView.setVisibility(View.VISIBLE);
        }

        //Присет slide анимации
        private static Slide makeSlideTransition() {
            Slide slide = new Slide();
            slide.setSlideEdge(Gravity.BOTTOM);
            slide.setInterpolator(new LinearInterpolator());
            slide.setDuration(500);
            return slide;
        }
    }

    public static class MyTimerTaskAddMoneyAuto extends TimerTask {
        @Override
        public void run() {
            MainActivity.count_money += count_buy_1_lvl_auto * total_multiplier;
            MainActivity.total_score += count_buy_1_lvl_auto * total_multiplier;
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
                count_money += add_money_tap * multiplier_add_money_tap;

                //Пришлось сделать математическое округление
                int total_result = (int) Math.ceil(add_money_tap * multiplier_add_money_tap);
                total_score(total_result);
                output_count_money();
                save_data();
            }
        });
    }

    void button_buy_1_lvl() {


        button_buy_1_lvl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float cost_buy = (cost_buy_1_lvl * buy_count);

                if (count_money >= cost_buy) {
                    count_money -= cost_buy;
                    for (int i = 0; i < buy_count; i++) {
                        cost_buy_1_lvl *= 1.01;
                    }
                    add_money_tap += buy_count * multiplier_tap_1_lvl * total_multiplier;
                    TextView_cost_buy_1_lvl.setText(cost_buy + "");
                    count_buy_1_lvl += buy_count;
                    TextView_count_buy_1_lvl.setText(count_buy_1_lvl + "");

                    //Увеличиваем множитель нажатия взависимости от количества улучшений
                    switch (count_buy_1_lvl) {
                        case 10:
                            multiplier_add_money_tap += (float) 1.2;
                            break;
                        case 100:
                            multiplier_add_money_tap += (float) 2;
                            break;
                        case 150:
                            multiplier_add_money_tap += (float) 2.5;
                            break;
                        case 200:
                            multiplier_add_money_tap += (float) 5;
                            break;
                    }

                    //Увеличиваем множитель улучшения взависимости от количества улучшений
                    switch (count_buy_1_lvl) {
                        case 10:
                        case 100:
                            multiplier_tap_1_lvl += (float) 2;
                            add_money_tap += 2 * count_buy_1_lvl * total_multiplier;
                            break;
                        case 50:
                        case 200:
                            multiplier_tap_1_lvl += (float) 10;
                            add_money_tap += 10 * count_buy_1_lvl * total_multiplier;
                            break;
                        case 150:
                            multiplier_tap_1_lvl += (float) 5;
                            add_money_tap += 5 * count_buy_1_lvl * total_multiplier;
                            break;
                    }
                    TextView_add_count_money.setText(add_money_tap + "");
                    TextView_multiplier_add_money_tap.setText(multiplier_add_money_tap + "");
                    TextView_multiplier_tap_1_lvl.setText(multiplier_tap_1_lvl + "");
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
                    cost_buy_2_lvl *= 1.01;
                    add_money_tap += 5 * multiplier_tap_2_lvl * total_multiplier;
                    TextView_cost_buy_2_lvl.setText(cost_buy_2_lvl + "");
                    count_buy_2_lvl ++;
                    TextView_count_buy_2_lvl.setText(count_buy_2_lvl + "");

                    //Увеличиваем множитель нажатия взависимости от количества улучшений
                    switch (count_buy_2_lvl) {
                        case 10:
                            multiplier_add_money_tap += (float) 1.2;
                            break;
                        case 100:
                            multiplier_add_money_tap += (float) 2;
                            break;
                        case 150:
                            multiplier_add_money_tap += (float) 2.5;
                            break;
                        case 200:
                            multiplier_add_money_tap += (float) 5;
                            break;
                    }

                    //Увеличиваем множитель улучшения взависимости от количества улучшений
                    switch (count_buy_2_lvl) {
                        case 10:
                        case 100:
                            multiplier_tap_2_lvl += (float) 2;
                            add_money_tap += 2 * count_buy_2_lvl * 5 * total_multiplier;
                            break;
                        case 50:
                        case 200:
                            multiplier_tap_2_lvl += (float) 10;
                            add_money_tap += 10 * count_buy_2_lvl * 5 * total_multiplier;
                            break;
                        case 150:
                            multiplier_tap_2_lvl += (float) 5;
                            add_money_tap += 5 * count_buy_2_lvl * 5 * total_multiplier;
                            break;
                    }
                    TextView_add_count_money.setText(add_money_tap + "");
                    TextView_multiplier_add_money_tap.setText(multiplier_add_money_tap + "");
                    TextView_multiplier_tap_2_lvl.setText(multiplier_tap_2_lvl + "");
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
                    cost_buy_3_lvl *= 1.01;
                    add_money_tap += 10 * multiplier_tap_3_lvl * total_multiplier;
                    TextView_cost_buy_3_lvl.setText(cost_buy_3_lvl + "");
                    count_buy_3_lvl ++;
                    TextView_count_buy_3_lvl.setText(count_buy_3_lvl + "");

                    //Увеличиваем множитель нажатия взависимости от количества улучшений
                    switch (count_buy_3_lvl) {
                        case 10:
                            multiplier_add_money_tap += (float) 1.2;
                            total_multiplier = 2;
                            break;
                        case 100:
                            multiplier_add_money_tap += (float) 2;
                            break;
                        case 150:
                            multiplier_add_money_tap += (float) 2.5;
                            break;
                        case 200:
                            multiplier_add_money_tap += (float) 5;
                            break;
                    }

                    //Увеличиваем множитель улучшения взависимости от количества улучшений
                    switch (count_buy_3_lvl) {
                        case 10:
                        case 100:
                            multiplier_tap_3_lvl += (float) 2;
                            add_money_tap += 2 * count_buy_3_lvl * 10 * total_multiplier;
                            break;
                        case 50:
                        case 200:
                            multiplier_tap_3_lvl += (float) 10;
                            add_money_tap += 10 * count_buy_3_lvl * 10 * total_multiplier;
                            break;
                        case 150:
                            multiplier_tap_3_lvl += (float) 5;
                            add_money_tap += 5 * count_buy_3_lvl * 10 * total_multiplier;
                            break;
                    }
                    TextView_total_multiplier.setText(total_multiplier+ "");
                    TextView_add_count_money.setText(add_money_tap + "");
                    TextView_multiplier_add_money_tap.setText(multiplier_add_money_tap + "");
                    TextView_multiplier_tap_3_lvl.setText(multiplier_tap_3_lvl + "");
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

        editor.putFloat("total_multiplier", total_multiplier);
        editor.putFloat("multiplier_add_money_tap", multiplier_add_money_tap);
        editor.putFloat("multiplier_tap_1_lvl", multiplier_tap_1_lvl);
        editor.putFloat("multiplier_tap_2_lvl", multiplier_tap_2_lvl);
        editor.putFloat("multiplier_tap_3_lvl", multiplier_tap_3_lvl);

        editor.putInt("buy_count", buy_count);

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

        total_multiplier = preferences.getFloat("total_multiplier", default_multiplier);
        multiplier_add_money_tap = preferences.getFloat("multiplier_add_money_tap", default_multiplier);
        multiplier_tap_1_lvl = preferences.getFloat("multiplier_tap_1_lvl", default_multiplier);
        multiplier_tap_2_lvl = preferences.getFloat("multiplier_tap_2_lvl", default_multiplier);
        multiplier_tap_3_lvl = preferences.getFloat("multiplier_tap_3_lvl", default_multiplier);

        buy_count = preferences.getInt("buy_count", buy_count_x1);

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

        total_multiplier = default_multiplier;
        multiplier_add_money_tap = default_multiplier;
        multiplier_tap_1_lvl = default_multiplier;
        multiplier_tap_2_lvl = default_multiplier;
        multiplier_tap_3_lvl = default_multiplier;
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