package space.test.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

public class test_1 extends AppCompatActivity {

    Button button_anim;


    // ссылка на статью http://developer.alexanderklimov.ru/android/animation/tweenanimation.php
    //определяем переменную для анимации
    private Animation ButtonAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_1);

        //загружаем в переменную анимацию из файла res/anim/button_anim.xml
        ButtonAnim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.button_anim);
        button_anim = (Button) findViewById(R.id.button_anim);

        //загружаем внутрь кнопки анимацию и запускаем ее
        button_anim.startAnimation(ButtonAnim);
    }
}

