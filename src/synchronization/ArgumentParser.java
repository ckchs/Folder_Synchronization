package synchronization;

import java.io.*;

import synchronization.FileToCopyToken.FTCTtype;

public class ArgumentParser {
	public static final int COPY_BUFFER = 1024 * 1024 * 8;
	public static final int COMPARE_BUFFER = 1024 * 8;

	static File Source;
	static File Target;
	static boolean recursive = false;
	static int recursive_depth = 0;
	static boolean forced = false;
	static boolean askingWhich = false;

	public static void main(String[] args) {
		try {
			for (int i = 0; i < args.length; i++) {
				switch (args[i]) {
				case "-s":
					Source = new File(args[i + 1]);
					i++;
					break;
				case "-t":
					Target = new File(args[i + 1]);
					i++;
					break;
				case "-f":
					forced = true;
					break;
				case "-aw":
					askingWhich = true;
					break;
				case "-r":
					recursive = true;
					break;
				case "-rd":
					i++;
					recursive_depth = Integer.parseInt(args[i]);
					break;
				default:
					break;

				}
			}
		} catch (Exception e) {
			System.out.println("argument parsing error");
		}

		try {

		} catch (Exception e) {
			System.out.print(e.toString());
		}

	}

}
