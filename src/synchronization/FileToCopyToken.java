package synchronization;

import java.io.*;

public class FileToCopyToken {
	public File source;
	public File target;
	boolean write=true;
	public FTCTtype type;
	public FileToCopyToken(File Source,File Target,FTCTtype Type) {
		this.source=Source;
		this.target=Target;
		this.type=Type;
	}

	public enum FTCTtype
	{
		OnlyInSource,OnlyInTarget,DifferentSourceAndTarget
	}
	
	public void CopyThis() throws IOException
	{
		if (!write) {
			return;
		}
		byte[] b = new byte[File_sync.COPY_BUFFER];
		target.getParentFile().mkdirs();
		target.createNewFile();
		
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
	public void dontWrite()
	{
		write=false;
	}
	public void swapSourceTarget()
	{
		File temp = source;
		source=target;
		target=temp;
	}
	
	
}

