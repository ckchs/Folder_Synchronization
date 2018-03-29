package synchronization;

import java.io.*;
import java.util.*;

public class File_sync {
	public static final int COPY_BUFFER = 1024 * 1024 * 8;
	public static final int COMPARE_BUFFER = 1024 * 8;

	public static void main(String[] args) {
		File f3 = new File("C:\\zapoctak\\sub1\\a.txt");
		File f4 = new File("C:\\zapoctak\\sub2\\a.txt");
		try {
			int f1f2 = compareFiles(f3, f4, true);
			if (f1f2 == 1) {
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
	 * @return -1: one of files is not file 0: files are not same 1: files are same.
	 */
	public static int compareFiles(File f1, File f2, boolean compareContent) {

		if (!f1.isFile() || !f2.isFile())
			return -1;
		if (!f1.getName().equals(f2.getName()))
			return 0;
		if (f1.length() != f2.length())
			return 0;
		if (f1.lastModified() != f2.lastModified())
			return 0;
		if (compareContent) {
			try (InputStream is1 = new FileInputStream(f1); InputStream is2 = new FileInputStream(f2)) {
				byte[] buffer1 = new byte[COMPARE_BUFFER];
				byte[] buffer2 = new byte[COMPARE_BUFFER];
				int len = 0;
				do {

					len = is1.read(buffer1);
					is2.read(buffer2);
					if (!Arrays.equals(buffer1, buffer2))
						return 0;
				} while (len != -1);
			} catch (IOException e) {
				System.out.println("nastala chyba");
				return 0;
			}

		}

		return 1;
	}

	public static FolderCompareToken compareFolders(File f1, File f2, boolean recursive, int depth) {
		ArrayList<File> sourceFiles = new ArrayList<File>(Arrays.asList(f1.listFiles()));
		Collections.sort(sourceFiles);
		ArrayList<File> targetFiles = new ArrayList<File>(Arrays.asList(f2.listFiles()));
		Collections.sort(targetFiles);
		ArrayList<Pair<File,File>> sameNames = new ArrayList<Pair<File,File>>();
		int j = 0;
		int k = 0;
		while (j < sourceFiles.size() || k < targetFiles.size()) {
			if (sourceFiles.get(j).getName().equals(targetFiles.get(k).getName())) {
				j++;
				k++;
				sameNames.add(new Pair<File,File>(sourceFiles.get(j),targetFiles.get(k)));
			}
			else
			{
				if(sourceFiles.get(j).getName())
					
			}
		}

		return null;
	}

}
