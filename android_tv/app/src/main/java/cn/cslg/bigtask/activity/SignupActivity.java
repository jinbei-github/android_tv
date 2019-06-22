package cn.cslg.bigtask.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import cn.cslg.bigtask.R;
import cn.cslg.bigtask.db.User;

public class SignupActivity extends AppCompatActivity {
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mLoginFormView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setText("注册");
        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
    }

    /**
     * 注册
     */
    private void attemptLogin() {
        String usermail = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        List<User> users = DataSupport.findAll(User.class);
        for(User user: users){
            if (usermail.equals(user.getUsername())){
                Toast.makeText(SignupActivity.this,"已有该用户",Toast.LENGTH_SHORT).show();
                return;
            }
        }
        User user = new User();
        user.setUsername(usermail);
        user.setPassword(password);
        user.save();
        Toast.makeText(SignupActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
        finish();


    }
}
