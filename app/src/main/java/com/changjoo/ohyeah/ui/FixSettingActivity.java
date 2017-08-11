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

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.model.FixModel;
import com.changjoo.ohyeah.utill.U;

import java.util.ArrayList;

public class FixSettingActivity extends Activity {
    RecyclerView fix_list;
    ArrayList<FixModel> fixModels;
    FixAdapter fixAdapter;
    int item_cnt = 4; //고정지출 입력 리스트 기본 개수
    ItemTouchHelper itemTouchHelper;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix_setting);
        submit= (Button)findViewById(R.id.submit);
        fix_list = (RecyclerView) findViewById(R.id.fix_list);
        fix_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        fixModels = new ArrayList<>();
        fixAdapter = new FixAdapter();
        fix_list.setAdapter(fixAdapter);
        fix_list.smoothScrollToPosition(fix_list.getAdapter().getItemCount()+1);

        //스와이프 아이템 삭제
        ItemTouchHelper.Callback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // 삭제되는 아이템의 포지션을 가져온다
                final int position = viewHolder.getAdapterPosition();
                // 데이터의 해당 포지션을 삭제한다
                item_cnt--;
                // 아답타에게 알린다
                fixAdapter.notifyItemRemoved(position);
            }
        };
        itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(fix_list);


        //확인 버튼 눌를시 서버로 고정리스트 전송
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //리싸이클러뷰에 만들어진 뷰들에서 버튼뷰가아닌 고정지출입력 뷰만 확인해서 내부에 값이 입력된 경우만 배열에 추가
                int childCount = fix_list.getChildCount();
                String name;
                String money;
                for(int i = 0; i < childCount-1; i++){
                    if((fix_list.findViewHolderForLayoutPosition(i)) instanceof FixViewHolder){
                        FixViewHolder fixViewHolder = (FixViewHolder) fix_list.findViewHolderForLayoutPosition(i);
                        name = fixViewHolder.fix_name.getText().toString();
                        money = fixViewHolder.fix_money.getText().toString();
                        if((!name.equals("")) && (!money.equals(""))){
                            fixModels.add(new FixModel(name,Integer.parseInt(money)));
                        }
                    }
                }
                U.getInstance().log("배열 길이 :"+fixModels.size()+""+fixModels.toString());

                //추가된 아이템이 1개이상일경우 서버로 리스트 전송 후 다음 화면으로
                if(fixModels.size()>0){

                }
                //추가된 고정지출이 하나도 없을 경우 서버로 전송하지 않고 바로 다음 화면으로
                else{
                    Intent intent = new Intent(FixSettingActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    //고정 지출 입력 뷰홀더
    public class FixViewHolder extends RecyclerView.ViewHolder {
        EditText fix_name, fix_money;

        public FixViewHolder(View itemView) {
            super(itemView);
            fix_name = (EditText) itemView.findViewById(R.id.fix_name);
            fix_money = (EditText) itemView.findViewById(R.id.fix_money);
        }
    }

    //고정지출 입력 뷰 추가하는 버튼 뷰홀더
    class BtnViewHolder extends RecyclerView.ViewHolder {
        Button add_btn;
        public BtnViewHolder(View itemView) {
            super(itemView);
            add_btn = (Button) itemView.findViewById(R.id.add_btn);
            add_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
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
