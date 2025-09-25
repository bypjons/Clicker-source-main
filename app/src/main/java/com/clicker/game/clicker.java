package com.clicker.game;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class clicker extends AppCompatActivity {

    private int score = 0;
    private int clickValue = 1;
    private int upgradeCost = 10;
    private int autoClickCost = 50;
    private boolean autoClickEnabled = false;
    private int autoClickLevel = 0;

    private TextView scoreTextView;
    private Button clickButton;
    private Button upgradeButton;
    private Button autoClickButton;
    private TextView autoClickStatus;

    private Handler autoClickHandler = new Handler();
    private Runnable autoClickRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ciicker);

        scoreTextView = findViewById(R.id.scoreTextView);
        clickButton = findViewById(R.id.clickButton);
        upgradeButton = findViewById(R.id.upgradeButton);
        autoClickButton = findViewById(R.id.autoClickButton);
        autoClickStatus = findViewById(R.id.autoClickStatus);

        updateScoreDisplay();

        clickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score += clickValue;
                updateScoreDisplay();
            }
        });

        upgradeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (score >= upgradeCost) {
                    score -= upgradeCost;
                    clickValue++;
                    upgradeCost = (int)(upgradeCost * 1.5);
                    updateScoreDisplay();
                    updateUpgradeButtonText();
                }
            }
        });

        autoClickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (score >= autoClickCost) {
                    score -= autoClickCost;
                    autoClickLevel++;
                    autoClickCost = (int)(autoClickCost * 2);

                    if (!autoClickEnabled) {
                        autoClickEnabled = true;
                        startAutoClick();
                    }

                    updateScoreDisplay();
                    updateAutoClickButtonText();
                }
            }
        });

        setupAutoClicker();
    }

    private void updateScoreDisplay() {
        scoreTextView.setText("Очки: " + score);

        upgradeButton.setEnabled(score >= upgradeCost);
        autoClickButton.setEnabled(score >= autoClickCost);
    }

    private void updateUpgradeButtonText() {
        upgradeButton.setText("Улучшение: +" + clickValue + " за клик (Стоимость: " + upgradeCost + ")");
    }

    private void updateAutoClickButtonText() {
        autoClickButton.setText("Автоклик Ур. " + autoClickLevel + " (Стоимость: " + autoClickCost + ")");
        autoClickStatus.setText("Автокликер: уровень " + autoClickLevel + " (+" + autoClickLevel + "/сек)");
    }

    private void setupAutoClicker() {
        autoClickRunnable = new Runnable() {
            @Override
            public void run() {
                if (autoClickEnabled) {
                    score += autoClickLevel;
                    updateScoreDisplay();
                }
                autoClickHandler.postDelayed(this, 1000);
            }
        };
    }

    private void startAutoClick() {
        autoClickHandler.postDelayed(autoClickRunnable, 1000);
        updateAutoClickButtonText();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        autoClickHandler.removeCallbacks(autoClickRunnable);
    }
}