package synchronization;

import java.io.File;
import java.util.Comparator;

// TODO: Auto-generated Javadoc
/**
 * The Class File_Comparator.
 */
public class File_Comparator implements Comparator<java.io.File> {

	/**
	 * Instantiates a new file comparator.
	 */
	public File_Comparator() {
	}

	/** 
	 * Compare only names
	 */
	@Override
	public int compare(File o1, File o2) {
		if(!o1.exists())
			return 1;
		if(!o2.exists())
			return -1;
		return o1.getName().compareTo(o2.getName());
		
	}
	
	/**
	 * Compare names, length and lastmodified
	 * 
	 */
	public int advancedCompare(File o1, File o2) {
		if(!o1.exists())
			return 1;
		if(!o2.exists())
			return -1;
		if (o1.isDirectory()&&o2.isDirectory())
		{
			int namecompare = o1.getName().compareTo(o2.getName());
			if(namecompare != 0)
				return namecompare;
			if (o1.length() != o2.length())
				if(o1.length() > o2.length())
				return -1;
				else return 1;
			
			if (o1.lastModified() != o2.lastModified())
				if(o1.lastModified() > o2.lastModified())
				return -1;
				else return 1;
			
			return 0;
			
			
		}
		if(o1.isDirectory()&&o2.isFile())
			return 1;
		if(o2.isDirectory()&&o1.isFile())
			return -1;
		
		if(o1.isFile()&&o2.isFile())
		{
			int namecompare = o1.getName().compareTo(o2.getName());
			if(namecompare != 0)
				return namecompare;
			if (o1.length() != o2.length())
				if(o1.length() > o2.length())
				return -1;
				else return 1;
			
			if (o1.lastModified() != o2.lastModified())
				if(o1.lastModified() > o2.lastModified())
				return -1;
				else return 1;
			
			return 0;
		}
		return 0;
	}

}
