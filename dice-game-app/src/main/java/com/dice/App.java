package com.dice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.dice.domain.Dice;
import com.dice.domain.Result;
import com.dice.service.ParameterizedDice;
import com.dice.service.Player;
import com.dice.service.RandomDice;

import java.util.Objects;

public class App {
	public static void main(String[] args) {
		Player player = new Player(dice(args));
		List<Result> results = player.play();

		if (Objects.nonNull(results)) {
			results.forEach(System.out::println);
			System.out.println("Final Score: " + results.get(results.size() - 1).getScore());
		}
	}

	/*
	 * プログラムが起動し、mainメソッドが呼ばれる
		コマンドライン引数に基づいてdiceメソッドが適切なサイコロオブジェクトを生成

		引数なし：ランダムサイコロ
		ファイル指定あり：ファイルの内容に基づくイカサマサイコロ


		プレイヤーが生成され、ゲームが実行される
		結果が表示される（各ラウンドの詳細と最終スコア）

		このコードは、異なるサイコロの実装を切り替えるためのファクトリーパターンとストリームAPIを使用したファイル読み込み
	 */
	private static Dice dice(String[] args) {
		if (args.length > 0 && !args[0].trim().isEmpty()) {
			//コマンドライン引数がある場合とない場合で処理を分岐
			//try-with-resourcesブロック（リソースを自動的にクローズ）
			try (Stream<String> stream = Files.lines(Paths.get(args[0].trim()))) {
				String s = stream.collect(Collectors.joining(System.lineSeparator())).trim();
				if (s.isEmpty()) {
					return new RandomDice();
				} else {
					return new ParameterizedDice(new ByteArrayInputStream(s.getBytes()));
				}
			} catch (IOException e) {
				//捕捉するが具体的な例外処理は実行しない.
				//具体的な回復処理（例えばファイルが見つからない場合に別のファイルを試すなど）は行わず、単に例外の種類を変換して再スローしています。
				throw new UncheckedIOException(e);
			}
		} else {
			//コマンドライン引数がない場合はRandomDiceを返す
			return new RandomDice();
		}
	}
}
