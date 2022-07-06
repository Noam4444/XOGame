package com.example.xoproj;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class XOgame extends AppCompatActivity implements  View.OnClickListener{
Button btNew,btBack;
TextView PlayerX,PlayerO, tvshow,winX,winO;
private Button[][] buttons = new Button[3][3];//הלוח שבנוי ממערך דו מימדי 3 על 3
    private boolean player1Turn = true;//משתנה בוליאני שבודק אם שחקן אחד משחק
    private int roundCount;//משתנה סופר כמה כפתורים נלחצו
    private int countwinX,countwinO;//משתנים לספירת נצחונות
    Switch Music;//כפתור מוזיקה
    MediaPlayer mpbtn,mpmusic;//משתנה אחד שמחזיק את המוזיקה שמפעילים אותה בכפתור, משתנה שמחזיק את הצלילי רקע שכל פעם לוחצים על כפתור במהלך המשחק


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xogame);
        PlayerX =findViewById(R.id.PlayerX);//הצגת שם שחקן ראשון
        PlayerO=findViewById(R.id.PlayerO);//הצגת שם שחקן שני
        btNew=findViewById(R.id.btNew);//כפתור למשחק חדש
        Music=findViewById(R.id.Music);
        winX=findViewById(R.id.winX);
        winO=findViewById(R.id.winO);
        btBack=findViewById(R.id.btBack);//כפתור חזרה לדף הראשי
        tvshow =findViewById(R.id.tvshow);//מציג של מי התור X או O
        mpbtn = MediaPlayer.create(this, R.raw.sample3);//משתנה לרעשי רקע שמתחיל משחק, ובלחיצה על כפתור
        mpmusic = MediaPlayer.create(this, R.raw.omer);//משתנה למוסיקה

        Intent intent = getIntent();
        if( intent.getExtras()!= null) {
            PlayerX.setText(intent.getStringExtra("key_name1"));//מעביר את השם הראשון של השחקן למסך משחק
            PlayerO.setText(intent.getStringExtra("key_name2"));//מעביר את השם השני של השחקן למסך משחק
        }
        for (int i = 0; i < 3; i++) {// עובר על הכפתורים לפי ה איידי שיצרנו באקסמל שיהיה תואם למשתנים i ןj
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;//משתנה סטירנג משום שהאיידי הוא מסוג סטרינג
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].animate().rotation(buttons[i][j].getRotation() - 1440).start();//נותן סיבוב לכל הכפתורים בתחילת משחק
            }
        }
        countwinX=0;//אתחול משתנים לספירת נצחונות
        countwinO=0;
        winX.setText(Integer.toString(countwinX));
        winO.setText(Integer.toString(countwinO));
    }
    @Override
    public void onClick(View v) {
        if (!((Button) v).getText().toString().equals("")) {     //בדיקה אם הכפתור פנוי ללחיצה
            return;
        }

        if (player1Turn) {//תור של שחקן X
            mpbtn.start();//  רעש רקע יופעל
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j]==v){//אם הכפתור במיקום המטריצה נבחר

                        buttons[i][j].animate().rotation(buttons[i][j].getRotation() - 180).start();// סיבוב של הכפתור שנלחץ
                    }
                }
            }
            ((Button) v).setText("X");//הכפתור יהיה לחוץ על X
            tvshow.setText("O");//הצגת תור הבא יהיה התור של O
        } else {// תור של שחקן O
            mpbtn.start();//רעש רקע
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (buttons[i][j]==v){
                        buttons[i][j].animate().rotation(buttons[i][j].getRotation() - -180).start();//סיבוב של כפתור שנלחץ
                    }
                }
            }
            ((Button) v).setText("O");//הכפתור יהיה לחוץ על O
            tvshow.setText("X");//הצגת תור הבא יהיה התור של X
        }
         roundCount++;//נלחץ כפתור נקדם את המונה
        if (checkForWin()) {//קריאה לפונקציה לבדיקת ניצחון
            if (player1Turn) { //אם זה בתור של השחקן הראשון
                player1Wins(); //אז אם הפונקציה בודקת ניצחון החזירה אמת וזה בתור של השחקן הראשון אז נפעיל את הפונקציה של השחקן הראשון שמנצח
            } else {
                player2Wins();//אם יש ניצחון וזה לא השחקן הראשון נפעיל את הפונקציה של השחקן השני שהוא ניצח
            }
        } else if (roundCount == 9) { //נבדוק האם המונה שלנו הגיע ל9 למספר גודל הלוח ואם שני השחקנים לא ניצחו אז יש תיקו
            draw();// נפעיל את הפונקציה שקוראת לתיקו
        } else {
            player1Turn = !player1Turn; // אם אחרי כל הבדיקות וגם המונה לא הגיע ל9 נמשיך לשחק והמצב של השחקן הראשון ישתנה בכל תור
        }
    }
    private boolean checkForWin() {// בדיקת ניצחון לפי מעבר על הכפתורים הלחוצים בסדר חוקי המשחק
        String[][] SignEq = new String[3][3];//המרה למערך דו ממדי סטרינג לצורך השוואת סימנים
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                SignEq[i][j] = buttons[i][j].getText().toString(); //סריקת הלוח איפה הכפתורים נלחצו
            }
        }
        for (int i = 0; i < 3; i++) { //בדיקת כל  ניצחון של שורה בכל המטריצה  אם יש ניצחון
            if (SignEq[i][0].equals(SignEq[i][1])
                    && SignEq[i][0].equals(SignEq[i][2])
                    && !SignEq[i][0].equals("")) {
                //צביעת המיקומים שבהם יש ניצחון
                buttons[i][0].setBackgroundColor(Color.parseColor("#EF0404"));
                buttons[i][1].setBackgroundColor(Color.parseColor("#EF0404"));
                buttons[i][2].setBackgroundColor(Color.parseColor("#EF0404"));
                return true;//החזרה של שלשה מנצחת
            }
        }
        for (int i = 0; i < 3; i++) { //בדיקת ניצחון של תור בכל המטריצה  אם יש ניצחון
            if (SignEq[0][i].equals(SignEq[1][i])
                    && SignEq[0][i].equals(SignEq[2][i])
                    && !SignEq[0][i].equals("")) {
                buttons[0][i].setBackgroundColor(Color.parseColor("#EF0404"));
                buttons[1][i].setBackgroundColor(Color.parseColor("#EF0404"));
                buttons[2][i].setBackgroundColor(Color.parseColor("#EF0404"));
                return true;
            }
        }
        if (SignEq[0][0].equals(SignEq[1][1]) // בדיקת ניצחון של  אלכסון מצד שמאל
                && SignEq[0][0].equals(SignEq[2][2])
                && !SignEq[0][0].equals("")) {
            buttons[0][0].setBackgroundColor(Color.parseColor("#EF0404"));
            buttons[1][1].setBackgroundColor(Color.parseColor("#EF0404"));
            buttons[2][2].setBackgroundColor(Color.parseColor("#EF0404"));

            return true;
        }
        if (SignEq[0][2].equals(SignEq[1][1]) //בדיקת ניצחון של  אלכסון מצד ימין
                && SignEq[0][2].equals(SignEq[2][0])
                && !SignEq[0][2].equals("")) {
            buttons[0][2].setBackgroundColor(Color.parseColor("#EF0404"));
            buttons[1][1].setBackgroundColor(Color.parseColor("#EF0404"));
            buttons[2][0].setBackgroundColor(Color.parseColor("#EF0404"));
            return true;
        }
        return false;
    }
    private void player1Wins() { //ניצחון של שחקן ראשון

        AlertDialog.Builder builder = new AlertDialog.Builder(XOgame.this);//יצירת הדיאלוג
        builder.setTitle("X Win"); //הכותרת שהמתשמשים יקבלו
        builder.setMessage("Are you want play again?"); //השאלה האם להמשיך לשחק
        builder.setCancelable(false); // אין אפשרות לצאת מהדיאלוג
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() { // אם נלחץ כן נעשה משחק חדש
            @Override
            public void onClick(DialogInterface dialog, int which) {
                winX.setText(Integer.toString(countwinX=countwinX+1));//מעלה ניצחון ב 1 אם שחקן X ניצח
                resetBoard();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() { // אם יבחר לא יעבור למסך ראשי
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mpmusic.stop();
                Intent intent=new Intent(XOgame.this,MainActivity.class);
                startActivity(intent);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);//מזיז את ההודעה ללמטה
        dialog.show();
    }
    private void player2Wins() {//ניצחון של שחקן שני כמו הראשון למעלה אותן פקודות
        AlertDialog.Builder builder = new AlertDialog.Builder(XOgame.this);
        builder.setTitle("O Win");
        builder.setMessage("Are you want play again? ");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                winO.setText(Integer.toString(countwinO=countwinO+1));//מעלה ניצחון ב 1 אם שחקן O ניצח
                resetBoard();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mpmusic.stop();
                Intent intent=new Intent(XOgame.this,MainActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }
    private void draw() {// אם תיקו אותן פקודות למעלה

        AlertDialog.Builder builder = new AlertDialog.Builder(XOgame.this);
        builder.setTitle("Draw");
        builder.setMessage("Play again? ");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetBoard();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mpmusic.stop();
                Intent intent=new Intent(XOgame.this,MainActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();
    }
    private void resetBoard() {//פונקציה לאיפוס לוח המשחק
        //איפוס הכפתורים של המטריצה למשחק חדש באמצעות שתי לולאות שעוברות על המטריצה
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setBackgroundColor(Color.parseColor("#03DAC5"));// איפוס הצבע רקע




            }
        }

        player1Turn = true; // איפוס השחקן הראשון
        roundCount=0;// איפוס הקאונטר
    }

    public void New(View view) {//   פונקציה למשחק חדש בדומה לריסטרט בורד
        AlertDialog.Builder builder = new AlertDialog.Builder(XOgame.this);
        builder.setTitle("Are you want new game? ");
        builder.setCancelable(false);
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            //במידה וענה שרוצה משחק חדש נאפס את המשחק
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //איפוס הכפתורים של המטריצה למשחק חדש באמצעות שתי לולאות שעוברות על המטריצה
                resetBoard();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            // אם ענה שלא  הדיאלוג נסגר ונשאר באותו מצב במשחק
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void back(View view) {//מעבר חזרה למסך ראשי
        AlertDialog.Builder builder = new AlertDialog.Builder(XOgame.this);
        builder.setTitle("Are you want back to main?");
        builder.setMessage(" ");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            //אם ענה שכן נחזיר אותו חזרה למסך ראשי
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mpmusic.stop();
                Intent intent=new Intent(XOgame.this,MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            //אם ענה שלא נסגור דיאלוג ונשאיר את המשחק באותו מצב
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void music(View view) {//פונקציה למוסיקה
        if (Music.isChecked()){// אם הכפתור לחוץ
            mpmusic.start();// נפעיל את המוזיקה
        }
        else {
            mpmusic.pause();// נעצור אותה
        }
    }
}