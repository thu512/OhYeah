package layout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.changjoo.ohyeah.R;


public class FirstFragment extends Fragment
{
    ProgressBar pb1;
    TextView textView;
    public FirstFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_first, container, false);
        pb1 = (ProgressBar)layout.findViewById(R.id.pb1);
        pb1.setProgress(25);
        textView = (TextView) layout.findViewById(R.id.textView);
        return layout;
    }
}

