package bphc.com.nirmaan;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bphc.com.nirmaan.app.Constants;
import bphc.com.nirmaan.app.LoginPrefs;
import bphc.com.nirmaan.object.LoginSet;
import bphc.com.nirmaan.service.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    Button submit,skip;
    TextInputLayout input_username,input_password;
    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (LoginPrefs.getSuccessPref(this)==1){
            startActivity(new Intent(this, LandingActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK));
            finish();
        }

        skip = (Button) findViewById(R.id.skip);
        submit = (Button) findViewById(R.id.sign_submit);

        input_username = (TextInputLayout) findViewById(R.id.input_sign_username);
        input_password = (TextInputLayout) findViewById(R.id.input_sign_password);

        username = (EditText) findViewById(R.id.sign_username);
        password = (EditText) findViewById(R.id.sign_password);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<LoginSet> call = ApiManager.getInstance().getService().authenticateUser(username.getText().toString(), password.getText().toString());
                call.enqueue(new Callback<LoginSet>() {
                    @Override
                    public void onResponse(Call<LoginSet> call, Response<LoginSet> response) {
                        Toast.makeText(getApplicationContext(),response.body().getSuccess()+"",Toast.LENGTH_LONG).show();
                        Log.e("Login",response.body().getSuccess()+"");
                        if (response.body().getSuccess()==1){
                            LoginPrefs.setPrefs(getApplicationContext(),
                                    username.getText().toString(),
                                    password.getText().toString(),
                                    response.body().getSuccess(),
                                    response.body().getPrivilege());
                            startActivity(new Intent(LoginActivity.this,LandingActivity.class).putExtra(Constants.login_privilege,
                                    LoginPrefs.getPrivilagePref(getApplicationContext())).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginSet> call, Throwable t) {
                        Toast.makeText(getApplicationContext(),t.toString()+"",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,LandingActivity.class));
            }
        });
    }
}
