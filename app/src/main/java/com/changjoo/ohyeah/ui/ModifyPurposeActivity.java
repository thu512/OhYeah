package com.changjoo.ohyeah.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;

public class ModifyPurposeActivity extends Activity {
    RadioButton home;
    RadioButton car;
    RadioButton airplane;
    RadioButton gift;
    RadioButton heart;
    RadioButton etc;
    Button submit;
    ImageButton back;
    int selectedItem=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_purpose);
        home = (RadioButton)findViewById(R.id.home);
        car = (RadioButton)findViewById(R.id.car);
        airplane = (RadioButton)findViewById(R.id.airplane);
        gift = (RadioButton)findViewById(R.id.gift);
        heart = (RadioButton)findViewById(R.id.heart);
        etc = (RadioButton)findViewById(R.id.etc);
        back = (ImageButton)findViewById(R.id.back);
        submit = (Button)findViewById(R.id.submit);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyPurposeActivity.this.finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        RadioButton.OnClickListener optionOnClickListener
                = new RadioButton.OnClickListener() {

            public void onClick(View v) {
                if(v.equals(home)){
                    selectedItem=1;
                    home.setChecked(true);
                    car.setChecked(false);
                    airplane.setChecked(false);
                    gift.setChecked(false);
                    heart.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if(v.equals(car)){
                    selectedItem=2;
                    car.setChecked(true);
                    home.setChecked(false);
                    airplane.setChecked(false);
                    gift.setChecked(false);
                    heart.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if(v.equals(airplane)){
                    selectedItem=3;
                    airplane.setChecked(true);
                    car.setChecked(false);
                    home.setChecked(false);
                    gift.setChecked(false);
                    heart.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if(v.equals(gift)){
                    selectedItem=4;
                    gift.setChecked(true);
                    car.setChecked(false);
                    airplane.setChecked(false);
                    home.setChecked(false);
                    heart.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if(v.equals(heart)){
                    selectedItem=5;
                    heart.setChecked(true);
                    car.setChecked(false);
                    airplane.setChecked(false);
                    gift.setChecked(false);
                    home.setChecked(false);
                    etc.setChecked(false);
                    return;
                }
                if(v.equals(etc)){
                    selectedItem=6;
                    etc.setChecked(true);
                    car.setChecked(false);
                    airplane.setChecked(false);
                    gift.setChecked(false);
                    heart.setChecked(false);
                    home.setChecked(false);
                    return;
                }
            }
        };
        home.setOnClickListener(optionOnClickListener);
        car.setOnClickListener(optionOnClickListener);
        airplane.setOnClickListener(optionOnClickListener);
        gift.setOnClickListener(optionOnClickListener);
        heart.setOnClickListener(optionOnClickListener);
        etc.setOnClickListener(optionOnClickListener);

    }
}
