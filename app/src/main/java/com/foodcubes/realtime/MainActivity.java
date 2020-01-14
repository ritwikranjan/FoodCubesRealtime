package com.foodcubes.realtime;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

        private static final int RC_SIGN_IN = 101;
        TextView userName;
        FirebaseAuth mFirebaseAuth;
        FirebaseUser mFirebaseUser;
        FirebaseAuth.AuthStateListener mFirebaseAuthStateListener;
        FloatingActionButton fab;
        DatabaseReference mDatabaseReference;
        FirebaseDatabase mFirebaseDatabase;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            FirebaseApp.initializeApp(this);
            mFirebaseAuth = FirebaseAuth.getInstance();
            mFirebaseUser = mFirebaseAuth.getCurrentUser();
            startSignInFlow();
            fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent dataEntryActivity = new Intent(MainActivity.this, DataEntry.class);
                    dataEntryActivity.putExtra("Uid",mFirebaseUser.getUid());
                    startActivity(dataEntryActivity);
                }
            });
        }

        private void startSignInFlow() {
           //
            mFirebaseAuthStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = mFirebaseAuth.getCurrentUser();
                    if (user != null) {
                        Toast.makeText(MainActivity.this, "User is signed in", Toast.LENGTH_SHORT).show();
                        //onSignedInInitialize(user.getDisplayName( ), user.getPhotoUrl( ));
                    } else {
                        //onSignOutCleanUp( );
                       /* AuthUI.IdpConfig phoneConfigWithDefaultNumber = new AuthUI.IdpConfig.PhoneBuilder()
                                .setDefaultCountryIso("in")
                                .build();*/
                        List<AuthUI.IdpConfig> providers = Arrays.asList(
                                new AuthUI.IdpConfig.GoogleBuilder().build(),
                                new AuthUI.IdpConfig.EmailBuilder().build()
                                //phoneConfigWithDefaultNumber
                        );
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setIsSmartLockEnabled(false)
                                        .setAvailableProviders(providers)
                                        .build(),
                                RC_SIGN_IN);

                    }
                }
            };
            mFirebaseAuth.addAuthStateListener(mFirebaseAuthStateListener);

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == RC_SIGN_IN) {
                IdpResponse response = IdpResponse.fromResultIntent(data);


                if (resultCode == RESULT_OK) {
                    // Successfully signed in
                    mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    afterSignedIn(mFirebaseUser);
                    // ...
                } else {
                    Toast.makeText(MainActivity.this, response.getError().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        private void afterSignedIn(final FirebaseUser user) {
            if(user!=null){
                mDatabaseReference = mFirebaseDatabase.getReference(user.getUid());
            //    userName.setText(user.getDisplayName());
            } else{
                //TODO implement phone sign in
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_signOut) {
                AuthUI.getInstance().signOut(this);
                return true;
            }

            return super.onOptionsItemSelected(item);
        }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
