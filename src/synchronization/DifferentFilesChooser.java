package synchronization;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

// TODO: Auto-generated Javadoc
/**
 * The Class DifferentFilesChooser.
 * Used for console comunication with user
 */
public class DifferentFilesChooser {
	
	
	/** The Different files. */
	private List<FileToCopyToken> DifferentFiles;
	
	/**
	 * Instantiates a new different files chooser.
	 *
	 * @param df the df
	 */
	public DifferentFilesChooser(List<FileToCopyToken> df) {
		this.DifferentFiles=df;
	}
	
	/**
	 * Console ask.
	 */
	public void consoleAsk()
	{
		for (Iterator<FileToCopyToken> iterator = DifferentFiles.iterator(); iterator.hasNext();) {
			FileToCopyToken fileToCopyToken = iterator.next();
			
			System.out.println("File 1 (Source):");
			
			System.out.println(fileToCopyToken.source.getAbsolutePath());
			System.out.println("Length : "+fileToCopyToken.source.length());
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			System.out.println("Last Modified : "+sdf.format(fileToCopyToken.source.lastModified()));
			System.out.println();
			System.out.println("File 2(Target):");
			System.out.println(fileToCopyToken.target.getAbsolutePath());
			System.out.println("Length : "+fileToCopyToken.target.length());
			System.out.println("Last Modified : "+sdf.format(fileToCopyToken.target.lastModified()));
			System.out.println();
			System.out.println("(d)ont synchronize, Copy (s)ource to target, Copy (t)arget to source");
			
			Scanner in = new Scanner(System.in);
			String s= in.nextLine();
			switch (s) {
			case "d":
				fileToCopyToken.dontWrite();
				break;
			case "s":
				break;
			case "t":
				fileToCopyToken.swapSourceTarget();
				break;

			default:
				break;
			}
			in.close();
			
			
			
		}
	}
	
	

}
