package layout;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gogolab.ohyeah.R;
import com.gogolab.ohyeah.ui.ModifyPurposeActivity;
import com.gogolab.ohyeah.utill.U;

import me.grantland.widget.AutofitTextView;


public class FirstFragment extends Fragment
{
    ProgressBar pb1;
    ProgressBar ratio_saving_pb;
    AutofitTextView daily_budget;
    TextView ratio_saving;
    Button goal_item;


    int first_budget;
    int daily_budget_rr;
    double ratio_saving_rr;
    int goal_item_rr;

    public FirstFragment() {
    }



    public static FirstFragment create(int first_budget, double daily_budget_rr, double ratio_saving_rr, int goal_item_rr) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt("first_budget", first_budget);
        args.putInt("daily_budget_rr", (int)daily_budget_rr);
        args.putDouble("ratio_saving_rr", ratio_saving_rr);
        args.putInt("goal_item_rr", goal_item_rr);
        U.getInstance().log("플래그먼트 create"+daily_budget_rr+"/"+goal_item_rr+"/"+ratio_saving_rr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        first_budget = getArguments().getInt("first_budget");
        daily_budget_rr = getArguments().getInt("daily_budget_rr");
        ratio_saving_rr = getArguments().getDouble("ratio_saving_rr");
        goal_item_rr = getArguments().getInt("goal_item_rr");
        U.getInstance().log("플래그먼트 oncreate"+daily_budget_rr+"/"+goal_item_rr+"/"+ratio_saving_rr);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        U.getInstance().log("하루");
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_first, container, false);
        pb1 = (ProgressBar)layout.findViewById(R.id.pb1);
        ratio_saving_pb = (ProgressBar)layout.findViewById(R.id.ratio_saving_pb);
        daily_budget = (AutofitTextView) layout.findViewById(R.id.daily_budget);
        ratio_saving = (TextView) layout.findViewById(R.id.ratio_saving);
        goal_item = (Button)layout.findViewById(R.id.goal_item);


        U.getInstance().log("플래그먼트"+daily_budget_rr+"/"+goal_item_rr+"/"+ratio_saving_rr);
        U.getInstance().log("하루 퍼센트: " + (int)(((double)daily_budget_rr/(double)first_budget) *100.0));
        if(daily_budget_rr<0){
            Resources res = getResources();
            Drawable draw=res.getDrawable(R.drawable.progress_warn);
            Drawable draw2=res.getDrawable(R.drawable.progress2_warn);
            pb1.setProgressDrawable(draw);
            ratio_saving_pb.setProgressDrawable(draw2);

        }
        daily_budget.setText(U.getInstance().toNumFormat(""+daily_budget_rr));
        ratio_saving.setText(""+(int) ratio_saving_rr+" %");

        Handler handler = new Handler() {};
        handler.postDelayed(new Runnable() {

            public void run() {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    pb1.setProgress((int)(((double)daily_budget_rr/(double)first_budget) *100.0),true);
                    ratio_saving_pb.setProgress((int) ratio_saving_rr,true);
                }else{
                    pb1.setProgress((int)(((double)daily_budget_rr/(double)first_budget) *100.0));
                    ratio_saving_pb.setProgress((int) ratio_saving_rr);
                }
            }
        },1000);

        switch (goal_item_rr){
            case 1:
                goal_item.setBackgroundResource(R.drawable.purpose_house);
                break;
            case 2:
                goal_item.setBackgroundResource(R.drawable.purpose_car);
                break;
            case 3:
                goal_item.setBackgroundResource(R.drawable.purpose_travel);
                break;
            case 4:
                goal_item.setBackgroundResource(R.drawable.purpose_gift);
                break;
            case 5:
                goal_item.setBackgroundResource(R.drawable.purpose_heart);
                break;
            case 6:
                goal_item.setBackgroundResource(R.drawable.purpose_house);
                break;
            default:
                goal_item.setBackgroundResource(R.drawable.purpose_house);
                break;
        }

        goal_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ModifyPurposeActivity.class);
                startActivity(intent);
            }
        });

        return layout;
    }
}

