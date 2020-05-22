package johnallen.com.hwk3_paybill;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FragmentOverview extends Fragment {
    private Button details;
    private Button pay;
    private EditText email;
    private EditText balance;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mCurrentUser;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        //Create the view
        View v = inflater.inflate(R.layout.fragment_overview, container, false);
        //Create the control variables so they can be interacted with
        email = v.findViewById(R.id.editTextEmail);
        balance = v.findViewById(R.id.editTextAmountDue);
        details = v.findViewById(R.id.buttonDetails);
        pay = v.findViewById(R.id.buttonPay);
        //Get currently logged in user
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        //Fill in the editTexts with the data
        email.setText(mCurrentUser.getEmail());
        queryDatabase();
        //Button Handlers
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivityDetail.class));
            }
        });

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActivityPayBill.class));
            }
        });

        return v;
    }
    @Override
    public void onStart()
    {
        super.onStart();
    queryDatabase();
    }

    public void queryDatabase()
    {
        try {
            db.collection("Hwk3Accounts")
                    .whereEqualTo("email",mCurrentUser.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                balance.setText(document.get("amountDue").toString());
                            }
                            else{
                                Log.w("MYDEBUG","Error getting documents.",task.getException());
                            }
                        }
                    });
        }
        catch (Exception e){ Toast.makeText(getContext(),"Something's wrong", Toast.LENGTH_LONG).show(); }
    }

}
