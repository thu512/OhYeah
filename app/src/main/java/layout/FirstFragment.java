package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.utill.U;


public class FirstFragment extends Fragment
{
    ProgressBar pb1;
    ProgressBar ratio_saving_pb;
    TextView daily_budget;
    TextView ratio_saving;
    Button goal_item;


    int first_budget;
    int daily_budget_rr;
    double ratio_saving_rr;
    int goal_item_rr;

    public FirstFragment() {
    }



    public static FirstFragment create(int first_budget, int daily_budget_rr, double ratio_saving_rr, int goal_item_rr) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putInt("first_budget", first_budget);
        args.putInt("daily_budget_rr", daily_budget_rr);
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

        daily_budget = (TextView) layout.findViewById(R.id.daily_budget);
        ratio_saving = (TextView) layout.findViewById(R.id.ratio_saving);
        goal_item = (Button)layout.findViewById(R.id.goal_item);


        U.getInstance().log("플래그먼트"+daily_budget_rr+"/"+goal_item_rr+"/"+ratio_saving_rr);
        pb1.setProgress((int)(((double)daily_budget_rr/(double)first_budget) *100.0));
        daily_budget.setText(""+daily_budget_rr);
        ratio_saving.setText(""+(int) ratio_saving_rr);
        ratio_saving_pb.setProgress((int) ratio_saving_rr);
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

        return layout;
    }
}

