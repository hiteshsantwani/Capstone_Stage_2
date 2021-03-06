package com.mindsparkk.ExpertTravel.Activity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.mindsparkk.ExpertTravel.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Hitesh on 28/06/15.
 */
public class SignUpActivity extends AppCompatActivity {

    EditText usernameTxt, nameTxt;
    EditText passwordTxt;
    EditText emailTxt;
    String username, name, email, pass, selected_gender = "";
    Button back, done;
    TextView signuptitle, alreadyRegistered;
    ImageView male, female, cross;
    RelativeLayout firstLayout;
    TextView gender;
    ViewSwitcher switcher;
    ProgressDialog d;
    View v1;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_layout);

        nameTxt = (EditText) findViewById(R.id.nameTxt);
        usernameTxt = (EditText) findViewById(R.id.usernameTxt);
        emailTxt = (EditText) findViewById(R.id.emailTxt);
        passwordTxt = (EditText) findViewById(R.id.passwordTxt);

        switcher = (ViewSwitcher) findViewById(R.id.viewSwitcher);
        signuptitle = (TextView) findViewById(R.id.signup_title);
        male = (ImageView) findViewById(R.id.male);
        female = (ImageView) findViewById(R.id.female);
        back = (Button) findViewById(R.id.back);
        done = (Button) findViewById(R.id.done);
        v1 = (View) findViewById(R.id.viewDivider);
        gender = (TextView) findViewById(R.id.gender);
        alreadyRegistered = (TextView) findViewById(R.id.alreadyRegistered);

        firstLayout = (RelativeLayout) findViewById(R.id.firstPage);

        d = new ProgressDialog(this);
        d.setIndeterminate(true);
        d.setMessage(getString(R.string.creatingAccountTag));
        d.setCanceledOnTouchOutside(false);

        //to go to previous view.........................................
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AnimationUtils();
                switcher.setAnimation(AnimationUtils.makeInAnimation
                        (getBaseContext(), true));
                switcher.showPrevious();
                signuptitle.setText(R.string.signUpMessageTag);
            }
        });

        //for selection of gender....................................................................
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setVisibility(View.GONE);
                v1.setVisibility(View.GONE);
                gender.setText(R.string.iAmMaleTag);
                selected_gender = "Male";
                nextView();

                emailTxt.setText(getAccounts());
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setVisibility(View.GONE);
                v1.setVisibility(View.GONE);
                gender.setText(R.string.iAmFemaleTag);
                selected_gender = "Female";
                nextView();

                emailTxt.setText(getAccounts());
            }
        });
        //end of gender selection....................................................................

        //to send data to parse on sign up button....................................
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDataToParse();
            }
        });

        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public String getAccounts() {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+

        Account[] accounts = AccountManager.get(this).getAccounts();
        String email = "";

        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                email = account.name;
            }
        }

        return email;
    }

    public void nextView() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new AnimationUtils();
                switcher.setAnimation(AnimationUtils.makeInAnimation
                        (getBaseContext(), true));
                switcher.showNext();
                signuptitle.setText(R.string.oneStepMsg);
            }
        }, 600);
    }

    public void setDataToParse() {

        if (usernameTxt.getText().length() != 0 && emailTxt.getText().length() != 0 && passwordTxt.getText().length() != 0 && nameTxt.getText().length() != 0) {
            d.show();

            name = nameTxt.getText().toString();
            email = emailTxt.getText().toString();
            pass = passwordTxt.getText().toString();
            username = usernameTxt.getText().toString();

            if (!isValidEmail(email)) {
                d.dismiss();
                emailTxt.setError(getString(R.string.validEmailMsg));
            } else {

                ParseUser user = new ParseUser();
                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(pass);
                user.put("name", name);
                user.put("gender", selected_gender);

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            d.dismiss();
                            Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                            // Log.d("done", "signed in ");
                        } else {
                            d.dismiss();
                            if (e.getCode() == 203) {
                                emailTxt.setError(getString(R.string.emailRegisteredMsg));
                                d.dismiss();
                            }
                            if (e.getCode() == 202) {
                                usernameTxt.setError(getString(R.string.usernameAlreadyTaken));
                                d.dismiss();
                            }
                            // Log.d("error", e.getMessage() + e.getCode());
                        }
                    }
                });
            }
        } else {
            if (usernameTxt.getText().length() == 0) {
                usernameTxt.setError(getString(R.string.mandatoryFieldTag));
            }
            if (nameTxt.getText().length() == 0) {
                nameTxt.setError(getString(R.string.mandatoryFieldTag));
            }
            if (passwordTxt.getText().length() == 0) {
                passwordTxt.setError(getString(R.string.mandatoryFieldTag));
            }
            if (emailTxt.getText().length() == 0) {
                emailTxt.setError(getString(R.string.mandatoryFieldTag));
            }
        }
    }

    private boolean isValidEmail(String email) {

        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
}
