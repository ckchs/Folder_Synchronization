package synchronization;

import java.io.*;
import java.util.Arrays;

public class File_sync {
	public static final int COPY_BUFFER = 1024 * 1024 * 8;
	public static final int COMPARE_BUFFER = 1024 * 8;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File f1 = new File("C:\\zapoctak\\a.txt");
		File f2 = new File("C:\\zapoctak\\a.txt");
		File f3 = new File("C:\\zapoctak\\sub1\\a.txt");
		File f4 = new File("C:\\zapoctak\\sub2\\a.txt");
		try {
			boolean f1f2 = compareFiles(f3, f4, true);
			if (f1f2) {
				System.out.println("spravne");
			} else {
				System.out.println("nespravne");
			}

		} catch (Exception e) {
		}

	}

	/**
	 * Compare two files. If neither of them isn't file return false.
	 * 
	 * @param f1
	 *            file1
	 * @param f2
	 *            file2
	 * @param detailed
	 *            Compare also contents of files
	 * @return Whether they have equal name, last modified and size.
	 */
	public static boolean compareFiles(File f1, File f2, boolean compareContent) throws FileNotFoundException {

		if (!f1.isFile() || !f2.isFile())
			return false;
		if (!f1.getName().equals(f2.getName()))
			return false;
		if (f1.length() != f2.length())
			return false;
		if (f1.lastModified() != f2.lastModified())
			return false;
		if (compareContent) {
			try (InputStream is1 = new FileInputStream(f1); InputStream is2 = new FileInputStream(f2)) {
				byte[] buffer1 = new byte[COMPARE_BUFFER];
				byte[] buffer2 = new byte[COMPARE_BUFFER];
				int len = 0;
				do {

					len = is1.read(buffer1);
					is2.read(buffer2);
					if (!Arrays.equals(buffer1, buffer2))
						return false;
				} while (len != -1);
			} catch (IOException e) {
				System.out.println("nastala chyba");
				return false;
			}

		}

		return true;
	}

}
