package com.changjoo.ohyeah.ui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.model.FixModel;
import com.changjoo.ohyeah.model.Req_Fix;
import com.changjoo.ohyeah.model.Req_email;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ModifyFixActivity extends Activity {
    RecyclerView fix_list;
    ArrayList<FixModel> Fix_ex;
    FixAdapter fixAdapter;

    Button submit;
    ImageButton back;

    int item_cnt = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_fix);
        submit = (Button) findViewById(R.id.submit);
        fix_list = (RecyclerView) findViewById(R.id.fix_list);
        fix_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Fix_ex = new ArrayList<>();
        fixAdapter = new FixAdapter();
        fix_list.setAdapter(fixAdapter);
        fix_list.smoothScrollToPosition(fix_list.getAdapter().getItemCount() + 1);
        back = (ImageButton) findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyFixActivity.this.finish();
            }
        });

        //확인 버튼 눌를시 서버로 고정리스트 전송
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPd();
                U.getInstance().log("itemCnt: "+item_cnt);
                U.getInstance().log("size:" +Fix_ex.size());

                if(item_cnt > Fix_ex.size()){
                    FixViewHolder fixViewHolder = (FixViewHolder) fix_list.findViewHolderForLayoutPosition(fix_list.getChildCount()-1);
                    if ((!fixViewHolder.fix_name.getText().toString().equals("")) && (!fixViewHolder.fix_money.getText().toString().equals(""))) {
                        Fix_ex.add(new FixModel(fixViewHolder.fix_name.getText().toString(), Integer.parseInt(U.getInstance().removeComa(fixViewHolder.fix_money.getText().toString()))));
                    } else {

                    }
                }

                int sum = 0;
                for (FixModel fixModel : Fix_ex) {
                    sum += fixModel.getF_ex_money();
                }

                U.getInstance().setFix(ModifyFixActivity.this, sum);
                U.getInstance().log("총고정 지출 액 : " + sum);
                U.getInstance().log("총고정 지출 리스트 : " + Fix_ex.toString());


                //추가된 아이템이 1개이상일경우 서버로 리스트 전송 후 다음 화면으로
                if (Fix_ex.size() > 0) {
                    Req_Fix req_fix = new Req_Fix(U.getInstance().getEmail(ModifyFixActivity.this), Fix_ex);
                    Call<Res> res = SNet.getInstance().getAllFactoryIm().update_Fix(req_fix);
                    res.enqueue(new Callback<Res>() {
                        @Override
                        public void onResponse(Call<Res> call, Response<Res> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    if (response.body().getResult() == 1) {
                                        U.getInstance().log("고정지출" + response.body().toString());
                                        Toast.makeText(ModifyFixActivity.this, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        U.getInstance().log("고정 서버전송 에러");
                                    }

                                } else {
                                    U.getInstance().log("통신실패1");
                                }
                            } else {
                                try {
                                    U.getInstance().log("통신실패2" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            stopPd();
                        }

                        @Override
                        public void onFailure(Call<Res> call, Throwable t) {
                            U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                            stopPd();
                        }
                    });
                }
                //추가된 고정지출이 하나도 없을 경우 서버로 전송하지 않고 바로 다음 화면으로
                else {
                    finish();
                }
                stopPd();
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        Fix_ex.clear();
        item_cnt = Fix_ex.size()+1;
        fixAdapter.notifyDataSetChanged();

        Req_email req_email = new Req_email(U.getInstance().getEmail(ModifyFixActivity.this));
        Call<Res> res = SNet.getInstance().getAllFactoryIm().updateFix(req_email);
        res.enqueue(new Callback<Res>() {
            @Override
            public void onResponse(Call<Res> call, Response<Res> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (response.body().getResult() == 1) {
                            U.getInstance().log("고정지출 수정" + response.body().toString());

                            for(FixModel fixModel:response.body().getDoc().getAsset().getFix_ex() ){

                                if(fixModel != null){
                                    Fix_ex.add(fixModel);
                                }
                            }
                            item_cnt = Fix_ex.size()+1;
                            fixAdapter.notifyDataSetChanged();

                        } else {
                            U.getInstance().log("고정 서버전송 에러");
                        }

                    } else {
                        U.getInstance().log("통신실패1");
                    }
                } else {
                    try {
                        U.getInstance().log("통신실패2" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                stopPd();
            }

            @Override
            public void onFailure(Call<Res> call, Throwable t) {
                U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                stopPd();
            }
        });


    }

    //고정 지출 입력 뷰홀더=
    public class FixViewHolder extends RecyclerView.ViewHolder {
        EditText fix_name, fix_money;
        Button del;
        RelativeLayout add;
        Button add_btn;
        LinearLayout form;
        String result="";
        public FixViewHolder(final View itemView) {
            super(itemView);
            form = (LinearLayout)itemView.findViewById(R.id.form);
            fix_name = (EditText) itemView.findViewById(R.id.fix_name);
            fix_money = (EditText) itemView.findViewById(R.id.fix_money);
            del = (Button) itemView.findViewById(R.id.del);
            add = (RelativeLayout) itemView.findViewById(R.id.add);
            add_btn = (Button) itemView.findViewById(R.id.add_btn);
            fix_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b && !fix_name.getText().toString().equals("")){
                        del.setVisibility(View.VISIBLE);
                    }else{
                        del.setVisibility(View.GONE);
                    }
                }
            });
            fix_money.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if(b && !fix_money.getText().toString().equals("")){
                        del.setVisibility(View.VISIBLE);
                    }else{
                        del.setVisibility(View.GONE);
                    }
                }
            });
            fix_money.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    if(!charSequence.toString().equals(result)){     // StackOverflow를 막기위해,

                        result = U.getInstance().toNumFormat(charSequence.toString());
                        fix_money.setText(result);    // 결과 텍스트 셋팅.
                        fix_money.setSelection(result.length());     // 커서를 제일 끝으로 보냄.
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    U.getInstance().log("아이템개수 삭제 전: "+item_cnt);
                    if ((!fix_name.getText().toString().equals("")) && (!fix_money.getText().toString().equals("")) && Fix_ex.size() != 0) {
                        for (int i = 0; i < Fix_ex.size(); i++) {
                            if (Fix_ex.get(i).getF_ex_record().equals(fix_name.getText().toString()) && Fix_ex.get(i).getF_ex_money() == Integer.parseInt(U.getInstance().removeComa(fix_money.getText().toString()))) {
                                Fix_ex.remove(i);
                                if(item_cnt!=1){
                                    item_cnt--;
                                }
                                fixAdapter.notifyDataSetChanged();
                                U.getInstance().log("아이템개수 삭제 후: "+item_cnt);
                                return;
                            }
                        }
                        fix_name.setText("");
                        fix_money.setText("");
                    }else if(Fix_ex.size() == 0){
                        fix_name.setText("");
                        fix_money.setText("");
                    }
                    U.getInstance().log("아이템개수 삭제 후: "+item_cnt);
                }

            });

        }
    }


    //adapter
    class FixAdapter extends RecyclerView.Adapter<FixViewHolder> {
        @Override
        public FixViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ModifyFixActivity.this).inflate(R.layout.cell_fix_layout, parent, false);

            view.findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FixViewHolder fixViewHolder;
                    fixViewHolder = (FixViewHolder) fix_list.findViewHolderForLayoutPosition(fix_list.getChildCount()-1);
                    String name = fixViewHolder.fix_name.getText().toString();
                    String money =  U.getInstance().removeComa(fixViewHolder.fix_money.getText().toString());
                    if ((!name.equals("")) && (!money.equals(""))) {
                        Fix_ex.add(new FixModel(name, Integer.parseInt(money)));
                    } else {
                        return;
                    }
                    U.getInstance().log("고정지출리스트:" + Fix_ex.toString());
                    U.getInstance().log("고정지출 늘리기 버튼: 리스트 개수: " + Fix_ex.size());
                    U.getInstance().log("고정지출 늘리기 버튼: 리스트 개수: " + Fix_ex.size());
                    item_cnt++;
                    fixAdapter.notifyDataSetChanged();

                }
            });


            return new FixViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FixViewHolder holder, int position) {
            U.getInstance().log("포지션: " + position);
            U.getInstance().log(Fix_ex.toString());
            if (Fix_ex.size() > 0 && position < item_cnt - 1) {
                FixModel fixModel = Fix_ex.get(position);
                holder.fix_money.setText("" + U.getInstance().toNumFormat(Integer.toString(fixModel.getF_ex_money())));
                holder.fix_name.setText(fixModel.getF_ex_record());
            }
            if (item_cnt - 1 == position) {
                holder.fix_money.setText("");
                holder.fix_name.setText("");
                holder.add.setVisibility(View.VISIBLE);
            } else {
                holder.add.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return item_cnt;
        }
    }


}
