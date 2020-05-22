package johnallen.com.hwk3_paybill;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ActivityOverview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add( R.id.overviewFragContainer, new FragmentOverview() );
        if (getApplicationContext().getResources().getDisplayMetrics().density >= 2)
            ft.add( R.id.detailFragContainer, new FragmentDetail());
        ft.commit();

    };
}
