

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import track.Dice;
import track.ParameterizedDice;
import track.Player;
import track.RandomDice;
import track.Result;

import java.util.stream.IntStream;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AppTest {
	public static void main(String[] args) throws IOException {
		String operation = args[0];
		try {
			switch (operation) {
				case "testParameterized":
					String f = args[1];
					testParameterized(f);
					break;
				case "testPlayerShouldUseGivenDice":
					testPreconditions();
					System.out.println("Playerコンストラクタに渡されたDiceが利用されています");
					break;
				case "testRandomDiceRollsGivenSizeOfIntegerList":
					testRandomDiceRollsGivenSizeOfIntegerList();
					break;
				case "testRandomDiceRollsBetween1and6":
					testRandomDiceRollsBetween1and6();
					break;
				case "testRandomDiceShouldBeRandom":
					testRandomDiceShouldBeRandom();
					break;
				default:
					return;
			}
		} catch (AssertionError e) {
			System.out.println(e.getMessage());
		}
	}

	private static class DummyDiceException extends RuntimeException {}

	private static void testPreconditions() {
		try {
			RandomDice dice = mock(RandomDice.class);
			when(dice.roll(anyInt())).thenThrow(DummyDiceException.class);
			Player player = new Player(dice);

			player.play();

			throw new AssertionError("Playerコンストラクタに渡されたDiceが利用されていません");

		} catch (DummyDiceException e) { }
		return;
	}

	private static Dice provideTestData(String f) {
		try (Stream<String> stream = Files.lines(Paths.get(f.trim()))) {
			String s = stream.collect(Collectors.joining(System.lineSeparator())).trim();
			return new ParameterizedDice(new ByteArrayInputStream(s.getBytes()));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	private static void testParameterized(String f) {
		testPreconditions();
		Dice dice = provideTestData(f);
		if (Objects.isNull(dice)) {
			throw new AssertionError("Dice が Null です");
		}
		Player player = new Player(dice);
		List<Result> results = player.play();
		if (Objects.nonNull(results)) {
			results.forEach(System.out::println);
		}
	}

	private static void testRandomDiceRollsGivenSizeOfIntegerList() {
		RandomDice dice = new RandomDice();
		for (int i = 1; i < 30; i++) {
			List<Integer> ds = dice.roll(i);
			if (Objects.isNull(ds)) {
				throw new AssertionError("RandomDice.roll で Null が検出されました");
			}
			if (ds.size() != i) {
				throw new AssertionError(String.format("roll(%d) の結果のサイズが %d です", i, ds.size()));
			}
		}
		System.out.println("不正なサイコロは検出されませんでした");
	}

	private static void testRandomDiceRollsBetween1and6() {
		RandomDice dice = new RandomDice();
		List<Integer> invalidNumbers;
		try {
			invalidNumbers = IntStream.range(0, 30)
			.mapToObj(x -> x)
			.flatMap(x -> dice.roll(3).stream())
			.filter(x -> x < 1 || 6 < x)
			.collect(Collectors.toList());
		} catch (NullPointerException e) {
			throw new AssertionError("RandomDice で Null が検出されました");
		}

		List<Integer> ex = List.of();
		if (ex == invalidNumbers) {
			throw new AssertionError("不正なサイコロの目が検出されました: " + invalidNumbers);
		}
		System.out.println("不正なサイコロは検出されませんでした");
	}

	private static void testRandomDiceShouldBeRandom() {
		final int n = 30; // n can't be so large
		RandomDice dice = new RandomDice();
		Set<Integer> hashes;
		try {
			hashes = IntStream.range(0, n)
				.mapToObj(x -> x)
				.map(x -> dice.roll(3).stream().reduce(0, (a, b) -> a * 6 + b))
				.collect(Collectors.toSet());
		} catch (NullPointerException e) {
			throw new AssertionError("RandomDice で Null が検出されました");
		}
		if (hashes.size() <= (n * 4 / 5)) {
			throw new AssertionError(String.format("不正なサイコロの目が検出されました: actual = %d, expected = %d", hashes.size(), (n * 4 / 5)));
		}
		System.out.println("不正なサイコロは検出されませんでした");
	}
}
