package com.gogolab.ohyeah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gogolab.ohyeah.Activity;
import com.gogolab.ohyeah.R;

public class SendEmailActivity extends Activity {

    EditText contents;
    TextView length;
    Button submit;
    ImageButton back;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        contents = (EditText)findViewById(R.id.contents);
        length = (TextView)findViewById(R.id.length);
        submit = (Button)findViewById(R.id.submit);
        back = (ImageButton)findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //글자수 체크
        contents.addTextChangedListener(new TextWatcher() {
            String strCur;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                strCur = charSequence.toString();
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 100) {
                    contents.setText(strCur);
                    contents.setSelection(i);
                } else {
                    length.setText(String.valueOf(charSequence.length()));
                }
            }
        });


        //매일 보내기
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String to = "gogolab2017@gmail.com";
                String subject = "오예 문의 사항";
                String message = contents.getText().toString();

                Intent email = new Intent(Intent.ACTION_SEND);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ to});
                //email.putExtra(Intent.EXTRA_CC, new String[]{ to});
                //email.putExtra(Intent.EXTRA_BCC, new String[]{to});
                email.putExtra(Intent.EXTRA_SUBJECT, subject);
                email.putExtra(Intent.EXTRA_TEXT, message);

                //need this to prompts email client only
                email.setType("message/rfc822");

                startActivity(Intent.createChooser(email, "이메일을 전송할 앱을 선택하세요."));

            }
        });


    }

}
