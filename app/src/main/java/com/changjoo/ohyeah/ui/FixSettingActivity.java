package com.changjoo.ohyeah.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.changjoo.ohyeah.Activity;
import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.model.FixModel;

import java.util.ArrayList;

public class FixSettingActivity extends Activity {
    RecyclerView fix_list;
    ArrayList<FixModel> fixModels;
    FixAdapter fixAdapter;
    int item_cnt = 2;
    // setup swipe to remove item
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


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("FFF",fixModels.toString());
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FixSettingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    //뷰홀더
    class FixViewHolder extends RecyclerView.ViewHolder {
        EditText fix_name, fix_money;

        public FixViewHolder(View itemView) {
            super(itemView);
            fix_name = (EditText) itemView.findViewById(R.id.fix_name);
            fix_money = (EditText) itemView.findViewById(R.id.fix_money);
        }
    }

    class BtnViewHolder extends RecyclerView.ViewHolder {
        Button add_btn;

        public BtnViewHolder(View itemView) {
            super(itemView);
            add_btn = (Button) itemView.findViewById(R.id.add_btn);
        }
    }

    //adapter
    class FixAdapter extends RecyclerView.Adapter<FixViewHolder> {
        @Override
        public FixViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            View view1;
            switch (viewType) {
                case 1:
                    view1 = LayoutInflater.from(FixSettingActivity.this).inflate(R.layout.cell_fix_layout, parent, false);
                    return new FixViewHolder(view1);
                case 0:
                    view = LayoutInflater.from(FixSettingActivity.this).inflate(R.layout.add_btn, parent, false);
                    view.findViewById(R.id.add_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d("FFF", "버튼눌림");
                            item_cnt++;
                            fixAdapter.notifyItemInserted(item_cnt - 1);

                        }
                    });
                    return new FixViewHolder(view);
                default:
                    view = LayoutInflater.from(FixSettingActivity.this).inflate(R.layout.cell_fix_layout, parent, false);
                    return new FixViewHolder(view);
            }
        }


        @Override
        public void onBindViewHolder(FixViewHolder holder, int position) {
            //데이터세팅!!!

        }

        @Override
        public int getItemViewType(int position) {
            Log.d("FFF", " " + fixAdapter.getItemCount() + "position:" + position);
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
