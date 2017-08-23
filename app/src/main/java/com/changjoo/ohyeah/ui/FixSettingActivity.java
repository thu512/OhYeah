package com.changjoo.ohyeah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.model.FixModel;
import com.changjoo.ohyeah.model.Req_Fix;
import com.changjoo.ohyeah.model.Res;
import com.changjoo.ohyeah.net.SNet;
import com.changjoo.ohyeah.utill.U;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FixSettingActivity extends Activity {
    RecyclerView fix_list;
    ArrayList<FixModel> Fix_ex;
    FixAdapter fixAdapter;
    int item_cnt = 4; //고정지출 입력 리스트 기본 개수
    ItemTouchHelper itemTouchHelper;
    Button submit;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_setting);
        submit= (Button)findViewById(R.id.submit);
        fix_list = (RecyclerView) findViewById(R.id.fix_list);
        fix_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        Fix_ex = new ArrayList<>();
        fixAdapter = new FixAdapter();
        fix_list.setAdapter(fixAdapter);
        fix_list.smoothScrollToPosition(fix_list.getAdapter().getItemCount()+1);
        back = (ImageButton)findViewById(R.id.back);


        //스와이프 아이템 삭제
        ItemTouchHelper.Callback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            int position;

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // 삭제되는 아이템의 포지션을 가져온다
                position = viewHolder.getAdapterPosition();
                String name;
                String money;
                U.getInstance().log("스와이프 포지션: "+position);
                U.getInstance().log("아이템 개수: "+item_cnt);
                if(position == (item_cnt-1)){

                    return;
                } else if((fix_list.findViewHolderForLayoutPosition(position)) instanceof FixViewHolder){
                    FixViewHolder fixViewHolder = (FixViewHolder) fix_list.findViewHolderForLayoutPosition(position);
                    name = fixViewHolder.fix_name.getText().toString();
                    money = fixViewHolder.fix_money.getText().toString();
                    if((!name.equals("")) && (!money.equals("")) && Fix_ex.size()!=0){
                        for(int i=0; i<Fix_ex.size(); i++){
                            if(Fix_ex.get(i).getF_ex_record().equals(name)){
                                Fix_ex.remove(i);
                            }
                        }
                    }
                    // 데이터의 해당 포지션을 삭제한다
                    item_cnt--;
                    // 아답타에게 알린다
                    fixAdapter.notifyItemRemoved(position);
                }

            }
        };
        simpleItemTouchCallback.isItemViewSwipeEnabled();
        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(fix_list);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FixSettingActivity.this.finish();
            }
        });

        //확인 버튼 눌를시 서버로 고정리스트 전송
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //리싸이클러뷰에 만들어진 뷰들에서 버튼뷰가아닌 고정지출입력 뷰만 확인해서 내부에 값이 입력된 경우만 배열에 추가
                int childCount = fix_list.getChildCount();
                String name;
                String money;
                int sum=0;
                for(int i = 0; i < childCount-1; i++){
                    if((fix_list.findViewHolderForLayoutPosition(i)) instanceof FixViewHolder){
                        FixViewHolder fixViewHolder = (FixViewHolder) fix_list.findViewHolderForLayoutPosition(i);
                        name = fixViewHolder.fix_name.getText().toString();
                        money = fixViewHolder.fix_money.getText().toString();
                        if((!name.equals("")) && (!money.equals(""))){
                            Fix_ex.add(new FixModel(name,Integer.parseInt(money)));
                            sum+=Integer.parseInt(money);
                        }
                    }
                }
                U.getInstance().setFix(FixSettingActivity.this, sum);
                U.getInstance().log("총고정 지출 액 : "+sum);


                //추가된 아이템이 1개이상일경우 서버로 리스트 전송 후 다음 화면으로
                if(Fix_ex.size()>0){
                    Req_Fix req_fix = new Req_Fix(U.getInstance().getEmail(FixSettingActivity.this),Fix_ex);
                    Call<Res> res = SNet.getInstance().getAllFactoryIm().pushFix(req_fix);
                    res.enqueue(new Callback<Res>() {
                        @Override
                        public void onResponse(Call<Res> call, Response<Res> response) {
                            if (response.isSuccessful()) {
                                if (response.body() != null) {
                                    if(response.body().getResult()==1){
                                        U.getInstance().log( "고정지출"+response.body().toString());
                                        Toast.makeText(FixSettingActivity.this,response.body().getMsg(),Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(FixSettingActivity.this, PurposeSetActivity.class);
                                        startActivity(intent);
                                    }else{
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
                        }

                        @Override
                        public void onFailure(Call<Res> call, Throwable t) {
                            U.getInstance().log("통신실패3" + t.getLocalizedMessage());
                        }
                    });
                }
                //추가된 고정지출이 하나도 없을 경우 서버로 전송하지 않고 바로 다음 화면으로
                else{
                    Intent intent = new Intent(FixSettingActivity.this, PurposeSetActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    //배열에 중복입력 방지를 위하여 다음화면으로 넘어간후 다시 돌와왔을때.
    @Override
    protected void onResume() {
        super.onResume();
        Fix_ex.clear();
    }

    //고정 지출 입력 뷰홀더=
    public class FixViewHolder extends RecyclerView.ViewHolder{
        EditText fix_name, fix_money;
        Button del;
        public FixViewHolder(View itemView) {
            super(itemView);
            fix_name = (EditText) itemView.findViewById(R.id.fix_name);
            fix_money = (EditText) itemView.findViewById(R.id.fix_money);
            del = (Button)itemView.findViewById(R.id.del);
        }
    }



    //adapter
    class FixAdapter extends RecyclerView.Adapter<FixViewHolder> {
        @Override
        public FixViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(FixSettingActivity.this).inflate(R.layout.add_btn, parent, false);
            final View view1 = LayoutInflater.from(FixSettingActivity.this).inflate(R.layout.cell_fix_layout, parent, false);
            switch (viewType) {
                //고정지출입력 뷰
                case 1:

                    view1.findViewById(R.id.del).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });

                    return new FixViewHolder(view1);
                //버튼뷰
                case 0:
                    view.findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            item_cnt++;
                            fixAdapter.notifyItemInserted(item_cnt - 1);
                        }
                    });
                    return new FixViewHolder(view);
                default:
                    return new FixViewHolder(view);
            }
        }
        @Override
        public void onBindViewHolder(FixViewHolder holder, int position) {}
        @Override
        public int getItemViewType(int position) {
            //리싸이클러의 제일 마지막뷰는 무조건 버튼 뷰 -> 0번
            if (position == item_cnt - 1) {
                return 0;
            }
            return 1;
        }
        @Override
        public int getItemCount() {
            return item_cnt;
        }
    }




}
