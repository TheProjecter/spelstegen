package spelstegen.server;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import spelstegen.client.Player;

/**
 * This is the main class of the console administration tool.
 * 
 * @author Henrik Segesten
 */
public class ConsoleTool {
	
	private StorageInterface storage = new MySQLStorageImpl();

	public static void main(String[] args) {
		ConsoleTool ct = new ConsoleTool();
		if (args.length == 0) {
			System.out.println("Välkommen till administrationsverktyget för spelstegen!");
			System.out.println("");
			System.out.println("");
			System.out.println("Ange ett av följande alternativ och ge som argument till verktyget:");
			System.out.println("");
			System.out.println("");
			System.out.println("1 - Lägg till spelare");
		}
		if (args.length == 1) {
			if (Integer.parseInt(args[0].trim()) == 1) {
				ct.addPlayer();
			}
		}
	}
	
	
	private void addPlayer() {
		String name;
		String email;
		String nickName;
		String password;
		Scanner sc = new Scanner(System.in);
		System.out.println("Skriv in ditt namn");
		name = sc.nextLine();
		System.out.println("Skriv in din email");
		email = sc.nextLine();
		System.out.println("Skriv in ditt lösenord");
		String temp = sc.nextLine();
		System.out.println("Skriv in ditt lösenord igen");
		password = sc.nextLine();
		if (password.equals(temp)) {
			password = getMd5Digest(temp);
		}
		System.out.println("Skriv in ditt smeknamn");
		nickName = sc.nextLine();
		Player p = new Player(name, email);
		p.setEncryptedPassword(password);
		p.setNickName(nickName);
		storage.addPlayer(p);
	}
	
	static String getMd5Digest(String input) {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1,messageDigest);
            return pad(number.toString(16),32,'0');
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }
	
	private static String pad(String s, int length, char pad) {
		StringBuffer buffer = new StringBuffer(s);
		while (buffer.length() < length) {
			buffer.insert(0, pad);
		}
		return buffer.toString();
	}
	
}
