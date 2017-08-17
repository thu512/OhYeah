package com.changjoo.ohyeah.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;

public class ModifySettingActivity extends Activity {
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_setting);
        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
