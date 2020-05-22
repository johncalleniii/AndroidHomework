package johnallen.com.hwk3_paybill;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FragmentDetail extends Fragment {
    private EditText email;
    private EditText amountDue;
    private EditText first;
    private EditText last;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mCurrentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Create the view
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        //Instantiate Controls
        email = v.findViewById(R.id.etEmail);
        amountDue = v.findViewById(R.id.etAmountDue);
        first = v.findViewById(R.id.etFirst);
        last = v.findViewById(R.id.etLast);
        //Instantiate Database stuff
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        //Fill in controls
        email.setText(mCurrentUser.getEmail());
        try {
            db.collection("Hwk3Accounts")
                    .whereEqualTo("email",mCurrentUser.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                amountDue.setText(document.get("amountDue").toString());
                                first.setText((String) document.get("first"));
                                last.setText((String) document.get("last"));
                            }
                            else{
                                Log.w("MYDEBUG","Error getting documents.",task.getException());
                            }
                        }
                    });
        }
        catch (Exception e){ Toast.makeText(getContext(),"Something's wrong", Toast.LENGTH_LONG).show(); }


        return v;
    }
}
