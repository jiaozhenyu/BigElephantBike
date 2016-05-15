package com.example.administrator.bigelephantbike;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class ValidateIdActivity extends Activity {

    private Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_id);

        submit =(Button)findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(ValidateIdActivity.this,MipcaActivityCapture.class);
                intent.putExtra("name","id");
                startActivity(intent);
            }
        });
    }

}
