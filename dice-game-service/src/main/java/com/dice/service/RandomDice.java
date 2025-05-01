package com.dice.service;


import java.util.ArrayList;
import java.util.Random;

import com.dice.domain.Dice;

public class RandomDice implements Dice {
  private Random random;
    
  public RandomDice() {
      this.random = new Random();
  }

	public ArrayList<Integer> roll(int n) {
    ArrayList<Integer> results = new ArrayList<>();
        
    for (int i = 0; i < n; i++) {
      int value = random.nextInt(6) + 1;
      results.add(value);
    }
    
    return results;
	}
}
