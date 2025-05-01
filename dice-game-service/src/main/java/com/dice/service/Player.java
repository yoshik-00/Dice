package com.dice.service;


import java.util.List;

import com.dice.domain.Dice;
import com.dice.domain.Result;

import java.util.ArrayList;


public class Player {
  private Dice dice;

	public Player(Dice dice) {
    this.dice = dice;
	}


  /*
   * List<Result> results = new ArrayList<>()：結果を格納するリストを初期化
   * int currentScore = 0：現在のスコアを0に初期化
   * for (int round = 0; round < 3; round++)：3ラウンドのループを開始
   */
  public List<Result> play() {
    List<Result> results = new ArrayList<>();
    int currentScore = 0;

    for (int round = 0; round < 3; round++) {
      //３個のサイコロで固定
      ArrayList<Integer> diceValues = dice.roll(3);

      //サイコロの目から得点を計算
      int roundScore = calculateRoundScore(diceValues);
      currentScore += roundScore;

      results.add(new Result(diceValues, currentScore));

      // 2, 4, 6のゾロ目の場合
      if (hasSpecialDice(diceValues)) {
        //スコアを2倍にする
        currentScore *= 2;
        //最後の結果を更新
        results.set(results.size() - 1, new Result(diceValues, currentScore));

        boolean continueSpecial = true;

        //特殊ケースの追加処理
        /*
         * 追加のサイコロを振る
          追加の得点を計算して加算
          特殊ケースが再度発生した場合：

          スコアを2倍にする
          結果を追加してループを継続


          通常のケースの場合：

          結果を追加
          ループを終了（continueSpecial = false）
         */
        while (continueSpecial) {
          ArrayList<Integer> newDiceValues = dice.roll(3);

          int additionalScore = calculateRoundScore(newDiceValues);
          currentScore += additionalScore;

          if (hasSpecialDice(newDiceValues)) {
            currentScore *= 2;
            results.add(new Result(newDiceValues, currentScore));
          } else {
            results.add(new Result(newDiceValues, currentScore));
            continueSpecial = false;
          }
        }

      }
    }
    return results;
  }
  
  // サイコロの目から得点を計算

  private int calculateRoundScore(List<Integer> diceValues) {
    int score = 0;

    for (int value : diceValues) {
      switch (value) {
        case 1:
          score += 1;
          break;
        case 3:
          score += 2;
          break;
        case 5:
          score += 4;
          break;
      }
    }
    return score;
  }

  //特殊ケースかいなか（2, 4, 6のゾロ目）
  /*
   * allTwo：すべてのサイコロが2かチェック
    allFour：すべてのサイコロが4かチェック
    allSix：すべてのサイコロが6かチェック
   */
  private boolean hasSpecialDice(List<Integer> diceValues) {
    boolean allTwo = true;
    for (int value : diceValues) {
      if (value != 2) {
          allTwo = false;
          break;
      }
    }    
    boolean allFour = true;
    for (int value : diceValues) {
      if (value != 4) {
        allFour = false;
        break;
      }
    }    
    boolean allSix = true;
    for (int value : diceValues) {
      if (value != 6) {
        allSix = false;
        break;
      }
      
    }    

  return allTwo || allFour || allSix;  
}
  
}
