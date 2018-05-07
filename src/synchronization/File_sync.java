package synchronization;

import java.io.*;

public class File_sync {
	public static final int COPY_BUFFER = 1024 * 1024 * 8;
	public static final int COMPARE_BUFFER = 1024 * 8;

	public static void main(String[] args) {
		File f1 = new File("TestFiles\\folder1");
		File f2 = new File("TestFiles\\folder2");
		try {
			FolderCompareToken f= new FolderCompareToken(f1,f2,true,1);
			DifferentFilesChooser dfc = new DifferentFilesChooser(f.getDifferentTokens());
			dfc.consoleAsk();
			f.write(a -> { return true; });
		} catch (Exception e) {
			System.out.print(e.toString());
		}

	}
	

}
