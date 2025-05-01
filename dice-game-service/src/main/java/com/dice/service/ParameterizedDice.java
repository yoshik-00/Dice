package com.dice.service;

import java.io.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dice.domain.Dice;

public class ParameterizedDice implements Dice {
    private final Queue<Integer> queue;

    public ParameterizedDice(InputStream is) {
        queue = new ArrayDeque<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            br.lines().forEachOrdered(s ->
                    queue.addAll(Stream.of(s.split(",")).map(Integer::parseInt).collect(Collectors.toList())));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public ArrayList<Integer> roll(int n) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            result.add(queue.poll());
        }
        return result;
    }
}
