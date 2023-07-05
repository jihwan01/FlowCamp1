package com.example.flowcamp1.ui.notifications;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.flowcamp1.R;
import com.example.flowcamp1.databinding.FragmentNotificationsBinding;

import java.util.*;

public class NotificationsFragment extends Fragment {

    Random random = new Random();

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    private long score;
    private int UP = 0;
    private int LEFT = 1;
    private int RIGHT = 2;
    private int DOWN = 3;
    private int X = -1;
    public int numNonZero;
    public boolean change;
    public boolean isNew;
    private FragmentNotificationsBinding binding;
    GestureDetectorCompat mDetector;
    private TextView scoreTextView;
    private TextView highScoreTextView;
    private TextView gameOverTextView;
    private TextView[][] cellTextViewMatrix;
    private int[][] cellValueMatrix;
    private HashMap<Integer, Integer> colorMap = new HashMap<Integer, Integer>();
    private TextView cell1;
    private TextView cell2;
    private TextView cell3;
    private TextView cell4;
    private TextView cell5;
    private TextView cell6;
    private TextView cell7;
    private TextView cell8;
    private TextView cell9;
    private TextView cell10;
    private TextView cell11;
    private TextView cell12;
    private TextView cell13;
    private TextView cell14;
    private TextView cell15;
    private TextView cell16;
    Button resetButton;
    Animation scaleAnim;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textNotifications;
//        notificationsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        scoreTextView = binding.score;
        highScoreTextView = binding.highScore;

        sharedPref = getActivity().getSharedPreferences("2048", Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        gameOverTextView = binding.gameOver;
        gameOverTextView.setText("game over");
        gameOverTextView.setBackgroundColor(Color.argb(100, 0, 0, 0));

        resetButton = binding.button;

        cell1 = binding.cell1;
        cell2 = binding.cell2;
        cell3 = binding.cell3;
        cell4 = binding.cell4;
        cell5 = binding.cell5;
        cell6 = binding.cell6;
        cell7 = binding.cell7;
        cell8 = binding.cell8;
        cell9 = binding.cell9;
        cell10 = binding.cell10;
        cell11 = binding.cell11;
        cell12 = binding.cell12;
        cell13 = binding.cell13;
        cell14 = binding.cell14;
        cell15 = binding.cell15;
        cell16 = binding.cell16;

        colorMap.put(0, Color.LTGRAY);
        colorMap.put(2, Color.WHITE);
        colorMap.put(4, Color.rgb(255,248,220));
        colorMap.put(8, Color.rgb(255,218,185));
        colorMap.put(16, Color.rgb(255,160,122));
        colorMap.put(32, Color.rgb(255,127,80));
        colorMap.put(64, Color.rgb(255,99,71));
        colorMap.put(128, Color.rgb(255,255,162));
        colorMap.put(256, Color.rgb(255,255,108));
        colorMap.put(512, Color.rgb(255,255,54));
        colorMap.put(1024, Color.rgb(25,246,18));
        colorMap.put(2048, Color.rgb(255,228,0));

        scaleAnim = AnimationUtils.loadAnimation(root.getContext(), R.anim.scale);

        mDetector = new GestureDetectorCompat(getActivity(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(@NonNull MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent motionEvent) {
            }

            @Override
            public boolean onFling(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
                double x = motionEvent.getX();
                double y = motionEvent.getY();
                double x1 = motionEvent1.getX();
                double y1 = motionEvent1.getY();

                int direction = GestureDirection(x, y, x1, y1);

                switch(direction){
                    case 0:
                        clearAllAnim();
                        GestureUp();
                        break;
                    case 1:
                        clearAllAnim();
                        GestureLeft();
                        break;
                    case 2:
                        clearAllAnim();
                        GestureRight();
                        break;
                    case 3:
                        clearAllAnim();
                        GestureDown();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        root.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDetector.onTouchEvent(motionEvent);
                return true;
            }
        });

        InitScore();
        InitHighScore();

        cellTextViewMatrix = new TextView[][]{
                {cell1, cell2, cell3, cell4},
                {cell5, cell6, cell7, cell8},
                {cell9, cell10, cell11, cell12},
                {cell13, cell14, cell15, cell16}
        };

        cellValueMatrix = new int[][]{
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
        };

        for(int i=0; i<16; i++){
            cellValueMatrix[i/4][i%4] = sharedPref.getInt("cell"+(i+1), 0);
            editor.putInt("cell"+(i+1), cellValueMatrix[i/4][i%4]);
        }

        isNew = sharedPref.getBoolean("new", true);
        if(isNew){
            InitBoard();
            editor.putBoolean("new", false);
            editor.apply();
        }
        UpdateBoard();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putLong("score", 0);
                editor.apply();
                InitScore();

                for(int i=0; i<4; i++){
                    for(int j=0; j<4; j++){
                        cellValueMatrix[i][j] = 0;
                    }
                }

                gameOverTextView.setVisibility(gameOverTextView.GONE);
                InitBoard();
                UpdateBoard();
            }
        });

        return root;
    }

    private void InitScore() {
        score = sharedPref.getLong("score", 0);
        scoreTextView.setText("Score: "+score);
    }

    private void InitHighScore() {
        highScoreTextView.setText("High Score: "+sharedPref.getLong("highScore", 0));
    }

    private void InitBoard() {
        for(int i=0; i<2; i++){
            int r = random.nextInt(4);
            int c = random.nextInt(4);

            while(cellValueMatrix[r][c] != 0){
                r = random.nextInt(4);
                c = random.nextInt(4);
            }

            cellValueMatrix[r][c] = 2;
        }
    }

    private void UpdateBoard() {
        for(int i=0; i<16; i++){
            editor.putInt("cell"+(i+1), cellValueMatrix[i/4][i%4]);
        }
        editor.apply();
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                cellTextViewMatrix[i][j].setText(""+cellValueMatrix[i][j]);
                if(colorMap.containsKey(cellValueMatrix[i][j])){
                    cellTextViewMatrix[i][j].setBackgroundColor(colorMap.get(cellValueMatrix[i][j]));
                }
                else{
                    cellTextViewMatrix[i][j].setBackgroundColor(Color.rgb(255,228,0));
                }

                if(cellValueMatrix[i][j] == 0){
                    cellTextViewMatrix[i][j].setTextColor(colorMap.get(0));
                } else if (cellValueMatrix[i][j] > 8 && cellValueMatrix[i][j] < 128) {
                    cellTextViewMatrix[i][j].setTextColor(Color.WHITE);
                }
                else{
                    cellTextViewMatrix[i][j].setTextColor(Color.BLACK);
                }
            }
        }
    }

    private int GestureDirection(double x, double y, double x1, double y1){
        if(Math.abs(x-x1)>Math.abs(y-y1)){
            if(x>x1){
                return LEFT;
            }
            else{
                return RIGHT;
            }
        } else if (Math.abs(x-x1)<Math.abs(y-y1)) {
            if(y>y1){
                return UP;
            }
            else{
                return DOWN;
            }
        }
        else{
            return X;
        }
    }

    public void GestureUp(){
//        Toast.makeText(getActivity(), "up", Toast.LENGTH_LONG).show();
        change = false;
        for(int j=0; j<4; j++){
            numNonZero = 0;
            for(int i=0; i<4; i++){
                if(cellValueMatrix[i][j] != 0){
                    if(numNonZero != i){
                        cellValueMatrix[numNonZero][j] = cellValueMatrix[i][j];
                        cellValueMatrix[i][j] = 0;
                        change = true;
                    }
                    numNonZero++;
                }
            }

            if(cellValueMatrix[0][j] == cellValueMatrix[1][j]){

                cellValueMatrix[0][j] *= 2;
                score += cellValueMatrix[0][j];

                if(cellValueMatrix[0][j] != 0){
                    change = true;
                }

                if(cellValueMatrix[2][j] == cellValueMatrix[3][j]){
                    cellValueMatrix[1][j] = cellValueMatrix[2][j] * 2;
                    score += cellValueMatrix[1][j];

                    cellValueMatrix[2][j] = 0;
                    cellValueMatrix[3][j] = 0;
                }
                else{
                    cellValueMatrix[1][j] = cellValueMatrix[2][j];
                    cellValueMatrix[2][j] = cellValueMatrix[3][j];
                    cellValueMatrix[3][j] = 0;
                }
            } else if (cellValueMatrix[1][j] == cellValueMatrix[2][j]) {
                cellValueMatrix[1][j] *= 2;
                score += cellValueMatrix[1][j];

                if(cellValueMatrix[1][j] != 0){
                    change = true;
                }
                cellValueMatrix[2][j] = cellValueMatrix[3][j];
                cellValueMatrix[3][j] = 0;
            } else if (cellValueMatrix[2][j] == cellValueMatrix[3][j]) {
                cellValueMatrix[2][j] *= 2;
                score += cellValueMatrix[2][j];

                if(cellValueMatrix[2][j] != 0){
                    change = true;
                }
                cellValueMatrix[3][j] = 0;
            }
        }
        if(change){
            RandomCreate();
            UpdateBoard();
            setScore();
            if(isOver()){
                gameOverTextView.setVisibility(gameOverTextView.VISIBLE);
            }
        }
    }

    public void GestureLeft(){
//        Toast.makeText(getActivity(), "left", Toast.LENGTH_LONG).show();
        change = false;
        for(int i=0; i<4; i++){
            numNonZero = 0;
            for(int j=0; j<4; j++){
                if(cellValueMatrix[i][j] != 0){
                    if(numNonZero != j){
                        cellValueMatrix[i][numNonZero] = cellValueMatrix[i][j];
                        cellValueMatrix[i][j] = 0;
                        change = true;
                    }
                    numNonZero++;
                }
            }

            if(cellValueMatrix[i][0] == cellValueMatrix[i][1]){

                cellValueMatrix[i][0] *= 2;
                score += cellValueMatrix[i][0];

                if(cellValueMatrix[i][0] != 0){
                    change = true;
                }

                if(cellValueMatrix[i][2] == cellValueMatrix[i][3]){
                    cellValueMatrix[i][1] = cellValueMatrix[i][2] * 2;
                    score += cellValueMatrix[i][1];

                    cellValueMatrix[i][2] = 0;
                    cellValueMatrix[i][3] = 0;
                }
                else{
                    cellValueMatrix[i][1] = cellValueMatrix[i][2];
                    cellValueMatrix[i][2] = cellValueMatrix[i][3];
                    cellValueMatrix[i][3] = 0;
                }
            } else if (cellValueMatrix[i][1] == cellValueMatrix[i][2]) {
                cellValueMatrix[i][1] *= 2;
                score += cellValueMatrix[i][1];

                if(cellValueMatrix[i][1] != 0){
                    change = true;
                }
                cellValueMatrix[i][2] = cellValueMatrix[i][3];
                cellValueMatrix[i][3] = 0;
            } else if (cellValueMatrix[i][2] == cellValueMatrix[i][3]) {
                cellValueMatrix[i][2] *= 2;
                score += cellValueMatrix[i][2];

                if(cellValueMatrix[i][2] != 0){
                    change = true;
                }
                cellValueMatrix[i][3] = 0;
            }
        }
        if(change){
            RandomCreate();
            UpdateBoard();
            setScore();
            if(isOver()){
                gameOverTextView.setVisibility(gameOverTextView.VISIBLE);
            }
        }
    }

    public void GestureRight(){
//        Toast.makeText(getActivity(), "right", Toast.LENGTH_LONG).show();
        change = false;
        for(int i=0; i<4; i++){
            numNonZero = 0;
            for(int j=3; j>=0; j--){
                if(cellValueMatrix[i][j] != 0){
                    if(3-numNonZero != j){
                        cellValueMatrix[i][3-numNonZero] = cellValueMatrix[i][j];
                        cellValueMatrix[i][j] = 0;
                        change = true;
                    }
                    numNonZero++;
                }
            }

            if(cellValueMatrix[i][3] == cellValueMatrix[i][2]){

                cellValueMatrix[i][3] *= 2;
                score += cellValueMatrix[i][3];

                if(cellValueMatrix[i][3] != 0){
                    change = true;
                }

                if(cellValueMatrix[i][1] == cellValueMatrix[i][0]){
                    cellValueMatrix[i][2] = cellValueMatrix[i][1] * 2;
                    score += cellValueMatrix[i][2];

                    cellValueMatrix[i][1] = 0;
                    cellValueMatrix[i][0] = 0;
                }
                else{
                    cellValueMatrix[i][2] = cellValueMatrix[i][1];
                    cellValueMatrix[i][1] = cellValueMatrix[i][0];
                    cellValueMatrix[i][0] = 0;
                }
            } else if (cellValueMatrix[i][2] == cellValueMatrix[i][1]) {
                cellValueMatrix[i][2] *= 2;
                score += cellValueMatrix[i][2];

                if(cellValueMatrix[i][2] != 0){
                    change = true;
                }
                cellValueMatrix[i][1] = cellValueMatrix[i][0];
                cellValueMatrix[i][0] = 0;
            } else if (cellValueMatrix[i][1] == cellValueMatrix[i][0]) {
                cellValueMatrix[i][1] *= 2;
                score += cellValueMatrix[i][1];

                if(cellValueMatrix[i][1] != 0){
                    change = true;
                }
                cellValueMatrix[i][0] = 0;
            }
        }
        if(change){
            RandomCreate();
            UpdateBoard();
            setScore();
            if(isOver()){
                gameOverTextView.setVisibility(gameOverTextView.VISIBLE);
            }
        }
    }

    public void GestureDown(){
//        Toast.makeText(getActivity(), "down", Toast.LENGTH_LONG).show();
        change = false;
        for(int j=0; j<4; j++){
            numNonZero = 0;
            for(int i=3; i>=0; i--){
                if(cellValueMatrix[i][j] != 0){
                    if(3-numNonZero != i){
                        cellValueMatrix[3-numNonZero][j] = cellValueMatrix[i][j];
                        cellValueMatrix[i][j] = 0;
                        change = true;
                    }
                    numNonZero++;
                }
            }

            if(cellValueMatrix[3][j] == cellValueMatrix[2][j]){

                cellValueMatrix[3][j] *= 2;
                score += cellValueMatrix[3][j];

                if(cellValueMatrix[3][j] != 0){
                    change = true;
                }

                if(cellValueMatrix[1][j] == cellValueMatrix[0][j]){
                    cellValueMatrix[2][j] = cellValueMatrix[1][j] * 2;
                    score += cellValueMatrix[2][j];

                    cellValueMatrix[1][j] = 0;
                    cellValueMatrix[0][j] = 0;
                }
                else{
                    cellValueMatrix[2][j] = cellValueMatrix[1][j];
                    cellValueMatrix[1][j] = cellValueMatrix[0][j];
                    cellValueMatrix[0][j] = 0;
                }
            } else if (cellValueMatrix[2][j] == cellValueMatrix[1][j]) {
                cellValueMatrix[2][j] *= 2;
                score += cellValueMatrix[2][j];

                if(cellValueMatrix[2][j] != 0){
                    change = true;
                }
                cellValueMatrix[1][j] = cellValueMatrix[0][j];
                cellValueMatrix[0][j] = 0;
            } else if (cellValueMatrix[1][j] == cellValueMatrix[0][j]) {
                cellValueMatrix[1][j] *= 2;
                score += cellValueMatrix[1][j];

                if(cellValueMatrix[1][j] != 0){
                    change = true;
                }
                cellValueMatrix[0][j] = 0;
            }
        }
        if(change){
            RandomCreate();
            UpdateBoard();
            setScore();
            if(isOver()){
                gameOverTextView.setVisibility(gameOverTextView.VISIBLE);
            }
        }
    }

    public void RandomCreate(){
        int r = random.nextInt(4);
        int c = random.nextInt(4);
        int v = random.nextInt(10);

        while(cellValueMatrix[r][c] != 0){
            r = random.nextInt(4);
            c = random.nextInt(4);
        }

        if(v == 0){
            cellValueMatrix[r][c] = 4;
        }
        else{
            cellValueMatrix[r][c] = 2;
        }
        cellTextViewMatrix[r][c].startAnimation(scaleAnim);
    }

    public void clearAllAnim(){
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                cellTextViewMatrix[i][j].clearAnimation();
            }
        }
    }

    public boolean isOver(){
        for(int i=0; i<4; i++){
            for(int j=0; j<4; j++){
                if(cellValueMatrix[i][j] == 0){
                    return false;
                }
            }
        }
        for(int i=0; i<4; i++){
            for(int j=0; j<3; j++){
                if(cellValueMatrix[i][j] == cellValueMatrix[i][j+1]){
                    return false;
                }
            }
        }
        for(int i=0; i<3; i++){
            for(int j=0; j<4; j++){
                if(cellValueMatrix[i][j] == cellValueMatrix[i+1][j]){
                    return false;
                }
            }
        }
        return true;
    }

    public void setScore() {
        editor.putLong("score", score);
        editor.apply();
        scoreTextView.setText("Score: "+score);
        if(score > sharedPref.getLong("highScore", 0)){
            editor.putLong("highScore", score);
            editor.apply();
            highScoreTextView.setText("High Score: "+sharedPref.getLong("highScore", 0));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}