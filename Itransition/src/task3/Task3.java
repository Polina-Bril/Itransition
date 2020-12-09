package task3;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;

public class Task3 {

	public static void main(String[] args) {
		if (checkArguments(args)) {
			SecureRandom random = new SecureRandom();
			byte key[] = new byte[16];
			random.nextBytes(key);
			String hmacKey = bytesToHex(key);
			int computerMove = random.nextInt(args.length) + 1;
			String hmac = getHmac(key, computerMove);
			System.out.println("HMAC: " + hmac);
			printMenu(args);
			int playerMove = playerChoose(args);
			if (playerMove != 0) {
				System.out.println("Your move: " + args[playerMove - 1]);
				System.out.println("Computer's move: " + args[computerMove - 1]);
				printGameResult(playerMove, computerMove, args);
				System.out.println("HMAC key: " + hmacKey);
			}
		}
	}

	public static boolean checkArguments(String[] args) {
		HashSet<String> setArgs = new HashSet<>(Arrays.asList(args));
		if (setArgs.size() < args.length) {
			System.out.println("Duplicated arguments are not allowed");
			return false;
		} else if (args.length % 2 == 0) {
			System.out.println(
					"Wrong number of arguments! There should be an ODD (3, 5, 7, etc.) number of arguments not less than three");
			return false;
		} else if (args.length < 3) {
			System.out.println(
					"Not enough arguments! There should be an odd number of arguments not less than THREE(3, 5, 7, etc.)");
			return false;
		} else {
			return true;
		}
	}

	public static String getHmac(byte[] key, int computerMove) {
		byte b = (byte) computerMove;
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA3-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		messageDigest.update(key);
		messageDigest.update(b);
		byte[] hmac = messageDigest.digest();
		return bytesToHex(hmac);
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}

	private static void printMenu(String[] args) {
		System.out.println("Available moves:");
		int n = 1;
		for (String s : args) {
			System.out.println(n + " - " + s);
			n++;
		}
		System.out.println("0 - Exit");
	}

	private static int playerChoose(String[] args) {
		System.out.print("Enter your move: ");
		Scanner sc = new Scanner(System.in);
		int playerMove = sc.nextInt();
		while (playerMove < 0 || playerMove > args.length) {
			System.out.println("Wrong choice! Try again!");
			System.out.print("Enter your move: ");
			playerMove = sc.nextInt();
		}
		sc.close();
		return playerMove;
	}

	private static void printGameResult(int playerMove, int computerMove, String[] args) {
		int result = playerMove - computerMove;
		if (result == 0) {
			System.out.println("Draw! Nobody won!");
		} else if ((result >= 1 && result <= args.length / 2)
				|| (result > -args.length && result <= -args.length / 2)) {
			System.out.println("You win!");
		} else {
			System.out.println("You lose! Try again!");
		}
	}
}