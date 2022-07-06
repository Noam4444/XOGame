package com.example.xoproj;

import static android.provider.Telephony.Mms.Part.TEXT;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private  static final String KEY_playerX ="key_name1", KEY_playerO ="key_name2";
    private String text1,text2;
    Button Aboutbtn, Startbtn;
    EditText PlayerX, PlayerO;
    RadioButton Humen,Bot;
    TextView Oplayer;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Aboutbtn =findViewById(R.id.Aboutbtn);
        Startbtn =findViewById(R.id.Startbtn);
        PlayerX = findViewById(R.id.PlayerX);
        PlayerO = findViewById(R.id.PlayerO);
        Oplayer =findViewById(R.id.Oplayer);
        Humen=findViewById(R.id.Humen);
        Bot=findViewById(R.id.Bot);
        mp = MediaPlayer.create(this, R.raw.soundstart);

    Intent intent = getIntent();
        if( intent.getExtras()!= null) {
            PlayerX.setText(intent.getStringExtra(KEY_playerX));
            PlayerO.setText(intent.getStringExtra(KEY_playerO));
        }
        Humen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    PlayerO.setVisibility(View.VISIBLE);
                    Oplayer.setVisibility(View.VISIBLE);
                    Bot.setChecked(false);
                }
            }
        });
        Bot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {//בודק אם לחצן השחקן מוך בוט לחוץ אם כן השם השני נעלם
                if (isChecked){
                    PlayerO.setVisibility(View.INVISIBLE);
                    Oplayer.setVisibility(View.INVISIBLE);
                    Humen.setChecked(false);
                }
            }
        });
        Startbtn.setOnClickListener(new View.OnClickListener() {//אירוע התחלת משחק מפנה לאיזה דף שצריך
            @Override
            public void onClick(View view) {
                mp.start();
                if(Humen.isChecked()){
                    if (PlayerX.getText().toString().trim().length()>0&& PlayerO.getText().toString().trim().length()>0){
                    Intent intent=new Intent(MainActivity.this,XOgame.class);
                    intent.putExtra("key_name1", PlayerX.getText().toString());
                    intent.putExtra("key_name2", PlayerO.getText().toString());
                    savedata();

                    startActivity(intent);
                    }
                    else {
                        Toast.makeText( MainActivity.this,"X Player or O Player null",Toast.LENGTH_LONG).show();
                    }
                }
                if (Bot.isChecked()){
                    if (PlayerX.getText().toString().trim().length()>0) {
                        Intent intent = new Intent(MainActivity.this, XOgamebot.class);
                        intent.putExtra("key_name1", PlayerX.getText().toString());
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText( MainActivity.this,"X Player null",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        loaddata();
        updateviews();
        Aboutbtn.setOnClickListener(new View.OnClickListener() {//לחיצה על הכפתור מציגה את יוצרי המשחק
            @Override
            public void onClick(View v) {
                if(Aboutbtn ==v)
                Toast.makeText( MainActivity.this,"Gabi Avramov and Noam Zargari",Toast.LENGTH_LONG).show();
            }
        });
    }
    public  void savedata(){//שמירת שמות השחקנים
        SharedPreferences sharedPreferences1=getSharedPreferences(KEY_playerX,MODE_PRIVATE);
        SharedPreferences sharedPreferences2=getSharedPreferences(KEY_playerO,MODE_PRIVATE);
        SharedPreferences.Editor editor1=sharedPreferences1.edit();
        SharedPreferences.Editor editor2=sharedPreferences2.edit();
        editor1.putString(TEXT, PlayerX.getText().toString());
        editor2.putString(TEXT, PlayerO.getText().toString());
        editor1.apply();
        editor2.apply();
        Toast.makeText(this,"Data saved",Toast.LENGTH_SHORT).show();
    }
    public void loaddata(){//טעינת שמות השחקנים שנשמרו מהמשחק הקודם
        SharedPreferences sharedPreferences1=getSharedPreferences(KEY_playerX,MODE_PRIVATE);
        SharedPreferences sharedPreferences2=getSharedPreferences(KEY_playerO,MODE_PRIVATE);
        text1=sharedPreferences1.getString(TEXT,"");
        text2=sharedPreferences2.getString(TEXT,"");
    }
    public void updateviews(){//עדכון שמות חדשים
        PlayerX.setText(text1);
        PlayerO.setText(text2);
    }
    public void clear(View view) {//כפתור ניקוי שמות
        PlayerX.setText("");
        PlayerO.setText("");
        savedata();
    }
}