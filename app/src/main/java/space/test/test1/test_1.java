package space.test.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class test_1 extends AppCompatActivity {

    Button button_anim;

    // ссылка на статью http://developer.alexanderklimov.ru/android/animation/tweenanimation.php
    //определяем переменную для анимации
    private Animation ButtonAnim;

    Button Point;
    Button button_anim_2;
    //Радиус окружности
    float radius = 300.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_1);

        //загружаем в переменную анимацию из файла res/anim/button_anim.xml
        ButtonAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_anim);
        button_anim = (Button) findViewById(R.id.button_anim);

        //загружаем внутрь кнопки анимацию и запускаем ее
        button_anim.startAnimation(ButtonAnim);


        //Дальше оч невнятная херня которую я нашел, на экране она сверху,
        //но теоретически тут можно разобраться как сделать так, чтобы объекты крутились
        //вокруг других объектов
        Point = (Button) findViewById(R.id.Point);

        button_anim_2 = (Button) findViewById(R.id.button_anim_2);
        button_anim_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rotate();
            }
        });
        Point.setX(button_anim_2.getX() + radius);
    }

    private void rotate(){

        final ValueAnimator animator = ValueAnimator.ofFloat(0.0f, 360.0f);
        animator.setDuration(5000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (Float) animator.getAnimatedValue();

                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);

                float cx = button_anim_2.getX() + radius;
                float cy = button_anim_2.getY();

                float x = (float) (cx + radius * Math.cos((float) Math.toRadians(value))); // center x in arc
                float y = (float) (cy + radius * Math.sin((float) Math.toRadians(value))); // center y in arc

                x -= (Point.getWidth() / 2);
                y -= (Point.getHeight() / 2);

                Point.setX(x);
                Point.setY(y);
            }
        });

        animator.start();
    }
}

