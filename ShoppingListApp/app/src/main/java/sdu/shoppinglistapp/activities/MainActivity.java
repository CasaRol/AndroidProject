package sdu.shoppinglistapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import sdu.shoppinglistapp.R;
import sdu.shoppinglistapp.business.ShopItem;
import sdu.shoppinglistapp.business.ShopList;
import sdu.shoppinglistapp.business.User;
import sdu.shoppinglistapp.persistence.DbHandler;

import static sdu.shoppinglistapp.activities.LoginActivity.mAuth;

public class MainActivity extends AppCompatActivity {
    private User user = null;
    private DbHandler dbh = DbHandler.getInstance();

    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        logout = findViewById(R.id.btn_logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });

        // FOR TESTING PURPOSES:
        ArrayList<ShopList> slist = new ArrayList<>();

        ArrayList<ShopItem> ilist = new ArrayList<>();
        ilist.add(new ShopItem("thisisanitem", false, "ThisisaScreenName", ""));



        //slist.add(new ShopList(0, "thisisalist", 109850923, ilist, map));
        //user = new User("Patrick", "email", "testuserid", slist);

        /*
        HashMap<String, String> map = new HashMap<>();
        map.put("XuiWQTZXer531rw2Ay0A", "Patrick");
        new ShopList("8LULHyPzkyNmRGguELTU", "Indkøbsliste", 0, ilist, map);

         */


        // END OF TESTING PURPOSES

        Log.d("MyTag", "onCreate: Main found");
    }

    @Override
    protected void onResume() {
        super.onResume();

//        // checks if the user is logged in (i.e. not null), and sends them to login if no User object is found. Otherwise redirects to the shoppingActivity.
//        if (user == null) {
//            Log.d("***DEBUG", "onResume: User was NULL, going to login activity");
//            Intent logIntent = new Intent(this, LoginActivity.class);
//            startActivity(logIntent);
//        } else {
//            Log.d("***DEBUG", "onResume: User was: " + user);
//            Intent shopIntent = new Intent(this, ShoppingActivity.class);
//            shopIntent.putExtra("User", user);
//            startActivity(shopIntent);
//        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
    }

//    private void updateUI(FirebaseUser currentUser) {
//        if (currentUser == null) {
//            Toast.makeText(this, "Please sign in", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(this, LoginActivity.class));
//        }
//    }

    // use to set the user after login
    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return this.user;
    }
}
