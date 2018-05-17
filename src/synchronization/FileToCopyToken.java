package synchronization;

import java.io.*;

// TODO: Auto-generated Javadoc
/**
 * The Class FileToCopyToken.
 * One object of diff result
 */
public class FileToCopyToken {
	
	/** The source. */
	public File source;
	
	/** The target. */
	public File target;
	
	/** The write. */
	boolean write=true;
	
	/** The type. */
	public FTCTtype type;
	
	/**
	 * Instantiates a new file to copy token.
	 *
	 * @param Source the source
	 * @param Target the target
	 * @param Type the type
	 */
	public FileToCopyToken(File Source,File Target,FTCTtype Type) {
		this.source=Source;
		this.target=Target;
		this.type=Type;
	}

	/**
	 * The Enum FTCTtype.
	 */
	public enum FTCTtype
	{
		
		/** The Only in source. */
		OnlyInSource,
/** The Only in target. */
OnlyInTarget,
/** The Different source and target. */
DifferentSourceAndTarget
	}
	
	/**
	 * Copy this.
	 * Write this token from source to target
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void CopyThis() throws IOException
	{
		if (!write) {
			return;
		}
		byte[] b = new byte[ArgumentParser.COPY_BUFFER];
		target.getParentFile().mkdirs();
		try {
			target.createNewFile();
		} catch (Exception e) {
			//file already exists
		}
		
		
		try(InputStream is = new FileInputStream(source);OutputStream os = new FileOutputStream(target))
		{
			
			
			int length=0;
			while ((length=is.read(b))>0) {
				os.write(b, 0, length);
			}
			length++;
			
		}
		
		
		catch (IOException e) {
			throw e;
		}
		target.setLastModified(source.lastModified());
	}
	
	/**
	 * Dont write.
	 */
	public void dontWrite()
	{
		write=false;
	}
	
	/**
	 * Swap source target.
	 */
	public void swapSourceTarget()
	{
		File temp = source;
		source=target;
		target=temp;
	}
	
	
}

