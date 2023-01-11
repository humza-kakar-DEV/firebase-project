package com.example.firebasepractise;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.firebasepractise.Util.CommunicationInterface;
import com.example.firebasepractise.Util.Constant;
import com.example.firebasepractise.Util.Utils;
import com.example.firebasepractise.databinding.ActivityAuthTypeBinding;
import com.example.firebasepractise.fragment.LoginFragment;
import com.example.firebasepractise.fragment.RegisterFragment;
import com.example.firebasepractise.fragment.SelectRoleFragment;
import com.example.firebasepractise.fragment.UserContentContainerFragment;
import com.example.firebasepractise.fragment.admin.AdminFragmentContainer;
import com.example.firebasepractise.fragment.planner.PlannerFragmentContainer;
import com.example.firebasepractise.model.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Any;
import com.nouman.jazzcashlib.JazzCash;
import com.nouman.jazzcashlib.JazzCashResponse;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AuthType extends AppCompatActivity implements LoginFragment.LoginFragmentInterface
        , RegisterFragment.RegisterFragmentListener {

    private static final int RC_SIGN_IN = 111;
    Button plannerButton, adminButton, userButton;
    private ActivityAuthTypeBinding binding;
    private FrameLayout frameLayout;
    private UserContentContainerFragment venueUserFragment;
    private String role;
    private CommunicationInterface communicationInterface;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private ImageUriInterface imageUriInterface;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount googleSignInAccount;

//    jazz cash variables

    String postData = "";
    private WebView mWebView;

    private final String Jazz_MerchantID      = "MC52757";
    private final String Jazz_Password        = "v3875bxv89";
    private final String Jazz_IntegritySalt   = "uw18485s40";

    private static final String paymentReturnUrl="https://www.youtube.com/";


//    ________________

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthTypeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

//        jazz cash code

        WebView mWebView = (WebView) findViewById(R.id.webView);
        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        mWebView.setWebViewClient(new MyWebViewClient());
        webSettings.setDomStorageEnabled(true);
        mWebView.addJavascriptInterface(new FormDataInterface(), "FORMOUT");

//        Intent intentData = getIntent();
//        String price = intentData.getStringExtra("price");
//        System.out.println("AhmadLogs: price_before : " +price);
//
//        String[] values = price.split("\\.");
//        price = values[0];
        String price = "100";
        System.out.println("AhmadLogs: price : " +price);

        Date Date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String DateString = dateFormat.format(Date);

        // Convert Date to Calendar
        Calendar c = Calendar.getInstance();
        c.setTime(Date);
        c.add(Calendar.HOUR, 1);

        // Convert calendar back to Date
        Date currentDateHourPlusOne = c.getTime();
        String expiryDateString = dateFormat.format(currentDateHourPlusOne);

        String TransactionIdString = "T" + DateString;

        String pp_MerchantID = Jazz_MerchantID;
        String pp_Password = Jazz_Password;
        String IntegritySalt = Jazz_IntegritySalt;
        String pp_ReturnURL = paymentReturnUrl;
        String pp_Amount = price;
        String pp_TxnDateTime = DateString;
        String pp_TxnExpiryDateTime = expiryDateString;
        String pp_TxnRefNo = TransactionIdString;
        String pp_Version = "1.1";
        String pp_TxnType = "";
        String pp_Language = "EN";
        String pp_SubMerchantID = "";
        String pp_BankID = "TBANK";
        String pp_ProductID = "RETL";
        String pp_TxnCurrency = "PKR";
        String pp_BillReference = "billRef";
        String pp_Description = "Description of transaction";
        String pp_SecureHash = "";
        String pp_mpf_1 = "1";
        String pp_mpf_2 = "2";
        String pp_mpf_3 = "3";
        String pp_mpf_4 = "4";
        String pp_mpf_5 = "5";

        String sortedString = "";
        sortedString += IntegritySalt + "&";
        sortedString += pp_Amount + "&";
        sortedString += pp_BankID + "&";
        sortedString += pp_BillReference + "&";
        sortedString += pp_Description + "&";
        sortedString += pp_Language + "&";
        sortedString += pp_MerchantID + "&";
        sortedString += pp_Password + "&";
        sortedString += pp_ProductID + "&";
        sortedString += pp_ReturnURL + "&";
        //sortedString += pp_SubMerchantID + "&";
        sortedString += pp_TxnCurrency + "&";
        sortedString += pp_TxnDateTime + "&";
        sortedString += pp_TxnExpiryDateTime + "&";
        //sortedString += pp_TxnType + "&";
        sortedString += pp_TxnRefNo + "&";
        sortedString += pp_Version + "&";
        sortedString += pp_mpf_1 + "&";
        sortedString += pp_mpf_2 + "&";
        sortedString += pp_mpf_3 + "&";
        sortedString += pp_mpf_4 + "&";
        sortedString += pp_mpf_5;

        pp_SecureHash = php_hash_hmac(sortedString, IntegritySalt);

        try {
            postData += URLEncoder.encode("pp_Version", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Version, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnType", "UTF-8")
                    + "=" + pp_TxnType + "&";
            postData += URLEncoder.encode("pp_Language", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Language, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_MerchantID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_MerchantID, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_SubMerchantID", "UTF-8")
                    + "=" + pp_SubMerchantID + "&";
            postData += URLEncoder.encode("pp_Password", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Password, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_BankID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_BankID, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_ProductID", "UTF-8")
                    + "=" + URLEncoder.encode(pp_ProductID, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnRefNo", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnRefNo, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_Amount", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Amount, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnCurrency", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnCurrency, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnDateTime", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnDateTime, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_BillReference", "UTF-8")
                    + "=" + URLEncoder.encode(pp_BillReference, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_Description", "UTF-8")
                    + "=" + URLEncoder.encode(pp_Description, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_TxnExpiryDateTime", "UTF-8")
                    + "=" + URLEncoder.encode(pp_TxnExpiryDateTime, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_ReturnURL", "UTF-8")
                    + "=" + URLEncoder.encode(pp_ReturnURL, "UTF-8") + "&";
            postData += URLEncoder.encode("pp_SecureHash", "UTF-8")
                    + "=" + pp_SecureHash + "&";
            postData += URLEncoder.encode("ppmpf_1", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_1, "UTF-8") + "&";
            postData += URLEncoder.encode("ppmpf_2", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_2, "UTF-8") + "&";
            postData += URLEncoder.encode("ppmpf_3", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_3, "UTF-8") + "&";
            postData += URLEncoder.encode("ppmpf_4", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_4, "UTF-8") + "&";
            postData += URLEncoder.encode("ppmpf_5", "UTF-8")
                    + "=" + URLEncoder.encode(pp_mpf_5, "UTF-8");

        } catch (UnsupportedEncodingException e) {
//            Log.d(Constant.PLANS_TAG, "onCreate: " + e.getMessage());
            e.printStackTrace();
        }

//        System.out.println("AhmadLogs: postData : " +postData);

        mWebView.postUrl("https://sandbox.jazzcash.com.pk/CustomerPortal/transactionmanagement/merchantform/", postData.getBytes());
//        Log.d(Constant.PLANS_TAG, "onCreate: " + postData);

//        ________________


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Initialize sign in client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        Intent intent = new Intent("com.example.firebasepractise.fragment.LoginFragment");
        intent.putExtra("val", "abc");
        sendBroadcast(intent);

        // drawer layout instance to toggle the menu icon to open
        // drawer and back button to close drawer
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.nav_open, R.string.nav_close);
        // pass the Open and Close toggle for the drawer layout listener
        // to toggle the button
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        // to make the Navigation drawer icon always appear on the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_logout:
                        firebaseAuth.signOut();
                        mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(AuthType.this, "logged out", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        homeFragment();
                        break;
                }
                //This is for closing the drawer after acting on it
                binding.drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        frameLayout = (FrameLayout) findViewById(R.id.frameLayout);
        venueUserFragment = UserContentContainerFragment.newInstance(null, null);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            checkCurrentUserRoleWithUid(0);
        } else if (googleSignInAccount != null) {
//            Toast.makeText(this, "firebase auth user null", Toast.LENGTH_SHORT).show();
            checkGoogleLoggedInUserInFirestoreWithEmail(googleSignInAccount, 0);
        } else {
            homeFragment();
        }
    }

//    jazz cash inner classes

    private class MyWebViewClient extends WebViewClient {
        private final String jsCode ="" + "function parseForm(form){"+
                "var values='';"+
                "for(var i=0 ; i< form.elements.length; i++){"+
                "   values+=form.elements[i].name+'='+form.elements[i].value+'&'"+
                "}"+
                "var url=form.action;"+
                "console.log('parse form fired');"+
                "window.FORMOUT.processFormData(url,values);"+
                "   }"+
                "for(var i=0 ; i< document.forms.length ; i++){"+
                "   parseForm(document.forms[i]);"+
                "};";

        //private static final String DEBUG_TAG = "CustomWebClient";

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if(url.equals(paymentReturnUrl)){
                System.out.println("AhmadLogs: return url cancelling");
                view.stopLoading();
                return;
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //Log.d(DEBUG_TAG, "Url: "+url);
            if(url.equals(paymentReturnUrl)){
                return;
            }
            view.loadUrl("javascript:(function() { " + jsCode + "})()");

            super.onPageFinished(view, url);
        }
    }

    private class FormDataInterface {
        @JavascriptInterface
        public void processFormData(String url, String formData) {
            Intent i = new Intent();

            System.out.println("AhmadLogs: Url:" + url + " form data " + formData);
            if (url.equals(paymentReturnUrl)) {
                String[] values = formData.split("&");
                for (String pair : values) {
                    String[] nameValue = pair.split("=");
                    if (nameValue.length == 2) {
                        System.out.println("AhmadLogs: Name:" + nameValue[0] + " value:" + nameValue[1]);
                        Log.d(Constant.PLANS_TAG, "onCreate: " + nameValue[0] + " " + nameValue[1]);
                        i.putExtra(nameValue[0], nameValue[1]);
                    }
                }

                setResult(RESULT_OK, i);
//                finish();

            }
        }
    }

    public static String php_hash_hmac(String data, String secret) {
        String returnString = "";
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] res = sha256_HMAC.doFinal(data.getBytes());
            returnString = bytesToHex(res);

        }catch (Exception e){
            e.printStackTrace();
        }

        return returnString;
    }

    public static String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789abcdef".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0, v; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }


//    ____________________



    //    home fragment
    public void homeFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(Constant.ROOT_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager.beginTransaction()
                .add(frameLayout.getId(), LoginFragment.newInstance(null, null), Constant.ROOT_FRAGMENT)
                .addToBackStack(Constant.ROOT_FRAGMENT)
                .commit();
    }

    //    creating options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.tab_menu_options, menu);
        return true;
    }

    //    no usage of this options while filtering
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                communicationInterface.fragmentListen(Utils.travelItems);
                return true;
            case R.id.item2:
                communicationInterface.fragmentListen(Utils.cateringItems);
                return true;
            case R.id.item3:
                communicationInterface.fragmentListen(Utils.decorItems);
                return true;
            case R.id.item4:
                communicationInterface.fragmentListen(Utils.djItems);
                return true;
        }
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if ((getSupportFragmentManager().getBackStackEntryCount() == 1)) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void interfaceReference(CommunicationInterface communicationInterface) {
        this.communicationInterface = communicationInterface;
    }

    @Override
    public void setRegisterFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(frameLayout.getId(), RegisterFragment.newInstance(null, null))
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setMainContentFragments() {
        checkCurrentUserRoleWithUid(0);
    }

    //    getting data recursive
    public void checkCurrentUserRoleWithUid(int increment) {
        if (increment > 2) {
            return;
        }
        firebaseFirestore.collection(Utils.roles.get(increment)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    User user = queryDocumentSnapshot.toObject(User.class);
                    if (user.getUserId().equals(firebaseAuth.getCurrentUser().getUid())) {
                        checkingRoleOfUser(user.getType());
                        return;
                    }
                }
            }
        });
        checkCurrentUserRoleWithUid(++increment);
    }

    @Override
    public void fromLoginFragmentCheckUser() {
        checkCurrentUserRoleWithUid(0);
    }

    public interface AuthTypeInterface {
        void updateAdapterListener(String value);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Signed in successfully, show authenticated UI.
                Toast.makeText(this, "email: " + account.getEmail(), Toast.LENGTH_SHORT).show();
                checkGoogleLoggedInUserInFirestoreWithEmail(account, 0);
            } catch (ApiException e) {
                // Signed in unsuccessful update UI
            }
        } else if (requestCode == Constant.UPLOAD_IMAGE) {
            imageUriInterface.imageUri(data.getData());
        }
    }

    public void checkGoogleLoggedInUserInFirestoreWithEmail(GoogleSignInAccount account, int increment) {
        if (increment > 2) {
            Log.d("myStart", "runned");
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(frameLayout.getId(), SelectRoleFragment.newInstance(null, null))
                    .commit();
            return;
        }
        firebaseFirestore.collection(Utils.roles.get(increment)).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    User user = queryDocumentSnapshot.toObject(User.class);
                    if (user.getEmail().equals(account.getEmail())) {
                        checkingRoleOfUser(user.getType());
                        return;
                    }
                }
            }
        });
        checkGoogleLoggedInUserInFirestoreWithEmail(account, ++increment);
    }

    public void checkingRoleOfUser(String role) {
        switch (role) {
            case "PlannerRole":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(frameLayout.getId(), PlannerFragmentContainer.newInstance(null, null))
                        .addToBackStack(null)
                        .commit();
                break;
            case "AdminRole":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(frameLayout.getId(), AdminFragmentContainer.newInstance(null, null))
                        .addToBackStack(null)
                        .commit();
                break;
            case "UserRole":
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(frameLayout.getId(), UserContentContainerFragment.newInstance(null, null))
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    public void imageUriInterfaceReference(ImageUriInterface imageUriInterface) {
        this.imageUriInterface = imageUriInterface;
    }

    public interface ImageUriInterface {
        void imageUri(Uri imageUri);
    }
}
