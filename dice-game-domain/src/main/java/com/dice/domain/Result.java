package com.dice.domain;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Result {
    private final List<Integer> dice;
    private final int score;

    public Result(List<Integer> dice, int score) {
        this.dice = dice;
        this.score = score;
    }

    public List<Integer> getDice() {
        return dice;
    }

    public int getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return score == result.score &&
                Objects.equals(dice, result.dice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dice, score);
    }

    @Override
    public String toString() {
        return String.format(
                "Dice: [%s], Score: %2d",
                dice.stream().map(String::valueOf).collect(Collectors.joining("]-[")),
                score
        );
    }
}
