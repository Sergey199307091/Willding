package com.example.willding;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import pl.droidsonroids.gif.GifImageView;
import ru.ok.android.sdk.Odnoklassniki;
import ru.ok.android.sdk.OkListener;
import ru.ok.android.sdk.util.OkAuthType;
import ru.ok.android.sdk.util.OkScope;

public class SignIn extends AppCompatActivity {
    private LocationManager locationManager;
    private SignInButton btnSignInButton;
    private GoogleSignInClient mGoogleSignInClient;
    private int distance;
//    private GifImageView gifImageView;
    private Location lastLocation;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private Odnoklassniki ok;
//private ImageView ivBackgroundLoading;
private TextView tvPoints;

    private static final String SIGNIN = "signin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        init();
     //   loginVerificationGoogle();//проверка вхошел ли пользователь в google акаунт
//loginVerificationFacebook();
//        ok=Odnoklassniki.createInstance(this, "512001205070","CPEEGHKGDIHBABABA");
//        Odnoklassniki.getInstance();
//        ok.requestAuthorization(this, REDIRECT_URI, OkAuthType.ANY, OkScope.VALUABLE_ACCESS, OkScope.LONG_ACCESS_TOKEN);
    }

    public void init() {
//        gifImageView=findViewById(R.id.gifLoading);
//ivBackgroundLoading=findViewById(R.id.ivBackgroundLoading);
        tvPoints=findViewById(R.id.tvPoints);
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        btnSignInButton = findViewById(R.id.btnSignGoogle);
        //tvSpeed = findViewById(R.id.tvSpeed);
        //   locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);//подключаем сервис по месту расположения
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        btnSignInButton.setOnClickListener(new View.OnClickListener() {//слушатель нажатия на кнопке входа через акаунт Google;
            @Override
            public void onClick(View v) {
//                gifImageView.setVisibility(View.VISIBLE);
//                ivBackgroundLoading.setVisibility(View.VISIBLE);
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();//осуществляем выбор акаунта Google
                startActivityForResult(signInIntent, 100);//возвращаем результат

            }
        });

//loginButton.setReadPermissions("public_profile");
loginButton.setReadPermissions("public_profile");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    AccessTokenTracker accessToken = new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if (currentAccessToken == null) {

            } else {
                loaduserProfile(currentAccessToken);
            }
        }

    private void loaduserProfile(AccessToken newAccesToken) {//получение данных акаунта Facebook и переход на другое активити
        GraphRequest request = GraphRequest.newMeRequest(newAccesToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (object != null) {
                    try {
                       String id = object.getString("id");
                      String name = object.getString("first_name");
                   //     String email = object.getString("email");
//new MySQLQuery().postQueryExample(id,name,email,"0");
Intent intent = new Intent(SignIn.this,Walking.class);
intent.putExtra(SIGNIN,"facebook");
intent.putExtra("id",id);
startActivity(intent);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        Bundle paraments = new Bundle();
        paraments.putString("fields","id,first_name");
        request.setParameters(paraments);
        request.executeAsync();
    }



};

        @Override
        protected void onActivityResult(int requestCode, int resultCode,  Intent data) {//получаем результат по requestCode
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 100) {
                com.google.android.gms.tasks.Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);
            }
        }

    private void handleSignInResult(com.google.android.gms.tasks.Task<GoogleSignInAccount> task) {//здесь получаем name,Email,photo все данные акаунта google
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            if(account!=null){
                new MySQLQuery().postQueryExample(account.getId(),account.getDisplayName(),account.getEmail(),"0");
                Intent intent = new Intent(SignIn.this,Walking.class);
                intent.putExtra(SIGNIN,"google");
                intent.putExtra("id",account.getId());
                startActivity(intent);
//                gifImageView.setVisibility(View.GONE);
//                ivBackgroundLoading.setVisibility(View.GONE);
            }

        } catch (ApiException e) {

        }
    }
public void loginVerificationGoogle(){//проверка вхошел ли пользователь в google акаунт
    GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    if (account != null) {
        Intent intent = new Intent(SignIn.this, Walking.class);
        intent.putExtra(SIGNIN,"google");
        intent.putExtra("id",account.getId());
        startActivity(intent);
//        gifImageView.setVisibility(View.GONE);
//        ivBackgroundLoading.setVisibility(View.GONE);

    }
}
public void loginVerificationFacebook(){
    AccessToken accessToken = AccessToken.getCurrentAccessToken();
    boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
    if(isLoggedIn){
       LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile"));
     Intent intent = new Intent(SignIn.this,Walking.class);
     String id = accessToken.getUserId();
     intent.putExtra(SIGNIN,"facebook");
     intent.putExtra("id",accessToken.getUserId());
     startActivity(intent);
    }
}
}

//    private LocationListener locationListener = new LocationListener() {
//
//        @Override
//        public void onLocationChanged(@NonNull Location location) {
//            UpdateDistance(location);
//        }
//
//
//    };
//
//
//    private void checkPremisson() {//проверка разрешения на месторасположение если версия андроид выше 6 API 23 и если нет разрешений
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 100);//выводим aдertdialog запрос
//        } else {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2, 1, locationListener);//провайдер , время обновления  и дистанция обновления и слушатель запрос месторасположения
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {//получаем ответ запроса
//
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {//проверяем ответ и если ответ пложительный то выполняем это условие
//            checkPremisson();
//
//        } else {
//            Toast.makeText(this, "No GPS premission", Toast.LENGTH_SHORT).show();//если нет,выводит соoбщение
//
//        }
//
//    }
//    private void UpdateDistance(Location location) {
//
//     if(location.hasSpeed() && location!=null){
//distance+=lastLocation.distanceTo(location);
//     }
//     lastLocation=location;
//     tvDistance.setText(String.valueOf(distance));
//     tvSpeed.setText(String.valueOf(location.getSpeed()));
//    }
