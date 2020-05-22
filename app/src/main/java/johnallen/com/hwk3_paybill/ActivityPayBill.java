package johnallen.com.hwk3_paybill;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class ActivityPayBill extends AppCompatActivity {
    private int dueInt;
    private EditText amountDue;
    private EditText payAmount;
    private Button pay;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private FirebaseUser mCurrentUser;
    private String mDocumentID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bill);
        //Instantiate Buttons
        amountDue = findViewById(R.id.etAmountDue);
        payAmount = findViewById(R.id.etPayAmount);
        pay = findViewById(R.id.buttonPay);
        //Get currently logged in user
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();
        try {
            db.collection("Hwk3Accounts")
                    .whereEqualTo("email",mCurrentUser.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);
                                mDocumentID = document.getId();
                                amountDue.setText( document.get("amountDue").toString());
                                dueInt = Integer.parseInt(amountDue.getText().toString());
                            }
                            else{
                                Log.w("MYDEBUG","Error getting documents.",task.getException());
                            }
                        }
                    });
        }
        catch (Exception e){ Toast.makeText(getApplicationContext(),"Something's wrong", Toast.LENGTH_LONG).show(); }

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    dueInt -= Integer.parseInt(payAmount.getText().toString());
                    db.collection("Hwk3Accounts")
                            .document(mDocumentID)
                            .update("amountDue", dueInt)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("MYDEBUG", "Update successful");
                                        Toast.makeText(getApplicationContext(),"Payment Complete", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                    else {
                                        Log.d("MYDEBUG", "Update FAILED!!!");
                                        Toast.makeText(getApplicationContext(),"Payment Failed", Toast.LENGTH_LONG).show();
                                        finish();}
                                }
                            });
                }
                catch (Exception e) {Toast.makeText(getApplicationContext(),"Pay Is Broken", Toast.LENGTH_LONG).show();}
            }
        });


    }
}
