package layout;

import android.content.Intent;
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

import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.ui.ModifyPurposeActivity;
import com.changjoo.ohyeah.utill.U;

import me.grantland.widget.AutofitTextView;


public class SecondFragment extends Fragment
{
    ProgressBar pb2;
    AutofitTextView month_budget;
    ProgressBar ratio_saving_pb2;
    TextView ratio_saving2;
    Button goal_item2;

    int first_month_budget;
    int daily_budget_month;
    double ratio_saving_rr;
    int goal_item_rr;


    public SecondFragment()
    {
    }

    public static SecondFragment create(int first_month_budget, int daily_budget_month, double ratio_saving_rr, int goal_item_rr) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();

        args.putInt("first_month_budget", first_month_budget);
        args.putInt("daily_budget_month", daily_budget_month);
        args.putDouble("ratio_saving_rr", ratio_saving_rr);
        args.putInt("goal_item_rr", goal_item_rr);
        U.getInstance().log("플래그먼트 create"+daily_budget_month+"/"+goal_item_rr+"/"+ratio_saving_rr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);

        U.getInstance().log("한달");
        first_month_budget = getArguments().getInt("first_month_budget");
        daily_budget_month = getArguments().getInt("daily_budget_month");
        ratio_saving_rr = getArguments().getDouble("ratio_saving_rr");
        goal_item_rr = getArguments().getInt("goal_item_rr");
        U.getInstance().log("두번쩨 플래그먼트 oncreate"+first_month_budget+"/"+daily_budget_month+"/"+goal_item_rr+"/"+ratio_saving_rr);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_second, container, false);
        pb2 = (ProgressBar)layout.findViewById(R.id.pb2);
        ratio_saving_pb2 = (ProgressBar)layout.findViewById(R.id.ratio_saving_pb2);
        month_budget = (AutofitTextView) layout.findViewById(R.id.month_budget);
        ratio_saving2 = (TextView) layout.findViewById(R.id.ratio_saving2);
        goal_item2 = (Button)layout.findViewById(R.id.goal_item2);


        U.getInstance().log("플래그먼트"+daily_budget_month+"/"+goal_item_rr+"/"+ratio_saving_rr);
        pb2.setProgress((int)(((double)daily_budget_month/(double)first_month_budget) *100.0));
        month_budget.setText(U.getInstance().toNumFormat(""+daily_budget_month));
        ratio_saving2.setText(""+(int) ratio_saving_rr+" %");
        ratio_saving_pb2.setProgress((int) ratio_saving_rr);


        Handler handler = new Handler() {};
        handler.postDelayed(new Runnable() {
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    pb2.setProgress((int)(((double)daily_budget_month/(double)first_month_budget) *100.0),true);
                    ratio_saving_pb2.setProgress((int) ratio_saving_rr,true);
                }else{
                    pb2.setProgress((int)(((double)daily_budget_month/(double)first_month_budget) *100.0));
                    ratio_saving_pb2.setProgress((int) ratio_saving_rr);
                }
            }
        },1000);


        switch (goal_item_rr){
            case 1:
                goal_item2.setBackgroundResource(R.drawable.purpose_house);
                break;
            case 2:
                goal_item2.setBackgroundResource(R.drawable.purpose_car);
                break;
            case 3:
                goal_item2.setBackgroundResource(R.drawable.purpose_travel);
                break;
            case 4:
                goal_item2.setBackgroundResource(R.drawable.purpose_gift);
                break;
            case 5:
                goal_item2.setBackgroundResource(R.drawable.purpose_heart);
                break;
            case 6:
                goal_item2.setBackgroundResource(R.drawable.purpose_house);
                break;
            default:
                goal_item2.setBackgroundResource(R.drawable.purpose_house);
                break;
        }
        goal_item2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ModifyPurposeActivity.class);
                startActivity(intent);
            }
        });
        return layout;
    }
}
