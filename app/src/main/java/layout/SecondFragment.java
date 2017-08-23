package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.changjoo.ohyeah.R;
import com.changjoo.ohyeah.utill.U;


public class SecondFragment extends Fragment
{
    ProgressBar pb2;
    public SecondFragment()
    {
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        U.getInstance().log("한달");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_second, container, false);
        pb2 = (ProgressBar)layout.findViewById(R.id.pb2);
        pb2.setProgress(75);
        return layout;
    }
}
