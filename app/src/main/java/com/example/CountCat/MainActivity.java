package com.CountCat;

import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.CountCat.R;

import java.util.concurrent.TimeUnit;

public class MainActivity extends Activity implements View.OnClickListener{

    int score;
    int scoreB;


    private Button plusbutton,plusbuttonB;
    private Button minusbutton,minusbuttonB;

    NumberPicker numPicker,numPickerB;

    private long timeCountInMilliSeconds = 1 * 60000;

    private enum TimerStatus {
        STARTED,
        STOPPED
    }

    private TimerStatus timerStatus = TimerStatus.STOPPED;

    private ProgressBar progressBarCircle;
    private EditText editTextMinute;
    private TextView textViewTime;
    private ImageView imageViewReset;
    private ImageView imageViewStartStop;
    private CountDownTimer countDownTimer;
    private long milliLeft = 1*60000;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayForTeamA(score);
        displayForTeamB(scoreB);

        findViews();
        initViews();

        // method call to initialize the views
        initViews_times();
        // method call to initialize the listeners
        initListeners();

}

    private void findViews(){
        numPicker = (NumberPicker)findViewById(R.id.numPicker);
        plusbutton = (Button)findViewById(R.id.plusbutton);
        minusbutton = (Button)findViewById(R.id.minusbutton);

        numPickerB = (NumberPicker)findViewById(R.id.numPickerB);
        plusbuttonB = (Button)findViewById(R.id.plusbuttonB);
        minusbuttonB = (Button)findViewById(R.id.minusbuttonB);

    }

    private void initViews(){
        numPicker.setMaxValue(10);
        numPicker.setMinValue(1);

        numPickerB.setMaxValue(10);
        numPickerB.setMinValue(1);

        plusbutton.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                score = score + numPicker.getValue();
                displayForTeamA(score);
                //textView1.setText(numPicker.getValue() + "");
            }
        });

        minusbutton.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                score = score - numPicker.getValue();
                displayForTeamA(score);
                //textView1.setText(numPicker.getValue() + "");
            }
        });

        plusbuttonB.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                scoreB = scoreB + numPickerB.getValue();
                displayForTeamB(scoreB);
                //textView1.setText(numPicker.getValue() + "");
            }
        });

        minusbuttonB.setOnClickListener(new OnClickListener(){
            public void onClick(View v) {
                scoreB = scoreB - numPickerB.getValue();
                displayForTeamB(scoreB);
                //textView1.setText(numPicker.getValue() + "");
            }
        });

    }

    /**
     * スタートをセットし、CountDownTimerをインスタンス
     * カウントダウン中に、onTick毎にtextViewに時間を表示
     * 一時停止でCOuntDownTimerをキャンセル
     * 再スタートでtextViewの残り時間で新しくCountDownTimerをインスタンス
     */



    /**
     * method to initialize the views
     * タイマーviewの初期化
     */
    private void initViews_times() {
        progressBarCircle = (ProgressBar) findViewById(R.id.progressBarCircle);
        editTextMinute = (EditText) findViewById(R.id.editTextMinute);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        imageViewReset = (ImageView) findViewById(R.id.imageViewReset);
        imageViewStartStop = (ImageView) findViewById(R.id.imageViewStartStop);
    }

    /**
     * method to initialize the click listeners
     * クリックリスナーの初期化
     */
    private void initListeners() {
        imageViewReset.setOnClickListener(this);
        imageViewStartStop.setOnClickListener(this);
    }

    /**
     * implemented method to listen clicks
     *
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //リセットボタンが押された時
            case R.id.imageViewReset:
                reset();
                break;
            //スタート、ストップボタンを押された時
            case R.id.imageViewStartStop:
                startStop();
                break;
        }
    }

    /**
     * method to reset count down timer
     * カウントダウンをリセット
     *
     */
    private void reset() {
        stopCountDownTimer();
        startCountDownTimer();
    }

    /**
     * method to start and stop count down timer
     * カウントダウンを開始する
     */
    private void startStop() {
        if (timerStatus == TimerStatus.STOPPED) {

            // call to initialize the timer values
            //タイマー値を初期化
            setTimerValues(milliLeft);

            // call to initialize the progress bar values
            //プログレスバー値を初期化
            setProgressBarValues();

            // showing the reset icon
            //リセットアイコンの表示
            imageViewReset.setVisibility(View.VISIBLE);

            // changing play icon to stop icon
            //再生アイコンを停止アイコンに変更
            imageViewStartStop.setImageResource(R.drawable.ic_baseline_stop_24);

            // making edit text not editable
            //編集テキストを編集不可にする
            editTextMinute.setEnabled(false);

            // changing the timer status to started
            //タイマーステータスを開始に変更する
            timerStatus = TimerStatus.STARTED;

            // call to start the count down timer
            //カウントダウンタイマーを開始する
            startCountDownTimer();

        } else {

            // hiding the reset icon
            //リセットアイコンを非表示にする
            imageViewReset.setVisibility(View.GONE);

            // changing stop icon to start icon
            //停止アイコンを開始アイコンに変更
            imageViewStartStop.setImageResource(R.drawable.ic_baseline_arrow_right_24);

            // making edit text editable
            //編集テキストを編集可能にする
            editTextMinute.setEnabled(true);

            // changing the timer status to stopped
            //タイマーステータスを停止に変更する
            timerStatus = TimerStatus.STOPPED;
            stopCountDownTimer();

        }

    }

    /**
     * method to initialize the values for count down timer
     * カウントダウンタイマーの値を初期化する
     */
    private void setTimerValues(long timer) {
        int time = 0;

        //再生トリガーが0→１に変化した場合(カウントダウンが完了するかリセットが押されることで再生トリガーが1→0になる)
        //リセットがおされた場合,

        //if(R.id.imageViewReset)

        if (!editTextMinute.getText().toString().isEmpty()) {
            // fetching value from edit text and type cast to integer
            //編集テキストから値をフェッチ＆整数にキャスト
            time = Integer.parseInt(editTextMinute.getText().toString().trim());
        } else {
            // toast message to fill edit text
            //編集テキストを埋めるためのメッセージ
            Toast.makeText(getApplicationContext(), getString(R.string.message_minutes), Toast.LENGTH_LONG).show();
        }
        // assigning values after converting to milliseconds
        //ミリ秒に変換した後に値を割り当てる
        timeCountInMilliSeconds = time * 60 * 1000;

        //ストップボタン→再生ボタンを押した場合
        //if(R.id.imageViewStartStop)
        //timeCountInMilliSeconds = Integer.parseInt(timer.getText().toString().trim()) * 60 * 1000;

    }

    /**
     * method to start count down timer
     * カウントダウンを開始する
     */
    private void startCountDownTimer() {

        countDownTimer = new CountDownTimer(timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                //テキストビューのタイマーをミリ秒に変換して残り時間を表示
                textViewTime.setText(hmsTimeFormatter(millisUntilFinished));

                //プログレスバーに残り時間の分だけ表示させる
                progressBarCircle.setProgress((int) (millisUntilFinished / 1000));

                //残り時間をmilliLeftに格納する
                milliLeft = millisUntilFinished;


            }

            @Override
            public void onFinish() {

                textViewTime.setText(hmsTimeFormatter(timeCountInMilliSeconds));

                // call to initialize the progress bar values
                //プログレスバー値を初期化する
                setProgressBarValues();

                // hiding the reset icon
                //リセットアイコンを非表示する
                imageViewReset.setVisibility(View.GONE);

                // changing stop icon to start icon
                //停止アイコンを開始アイコンに変更
                imageViewStartStop.setImageResource(R.drawable.ic_baseline_arrow_right_24);

                // making edit text editable
                //編集テキストを編集可能にする
                editTextMinute.setEnabled(true);

                // changing the timer status to stopped
                //タイマーステータスを停止する
                timerStatus = TimerStatus.STOPPED;
            }

        }.start();
        countDownTimer.start();
    }

    /**
     * method to stop count down timer
     * カウントダウンタイマーを停止する
     */
    private void stopCountDownTimer() {

        countDownTimer.cancel();

    }

    /**
     * method to set circular progress bar values
     * 循環プログレスバー値を設定する
     */
    private void setProgressBarValues() {

        progressBarCircle.setMax((int) timeCountInMilliSeconds / 1000);
        progressBarCircle.setProgress((int) timeCountInMilliSeconds / 1000);
    }


    /**
     * method to convert millisecond to time format
     *
     * @param milliSeconds
     * @return HH:mm:ss time formatted string
     * ミリ秒をHH:mm:ssに時間変換します
     */
    private String hmsTimeFormatter(long milliSeconds) {

        String hms = String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toHours(milliSeconds),
                TimeUnit.MILLISECONDS.toMinutes(milliSeconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(milliSeconds)),
                TimeUnit.MILLISECONDS.toSeconds(milliSeconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(milliSeconds)));

        return hms;

    }


    /**
     * Displays the given score for Team A.
     */
    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Displays the given score for Team B.
     */
    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     *
     * All Reset
     */
    public void resetScore(View v){

        score = 0;
        scoreB = 0;
        displayForTeamA(score);
        displayForTeamB(scoreB);
    }

}
