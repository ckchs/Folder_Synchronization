package synchronization;

import java.io.File;
import java.io.IOException;
import java.util.*;

import synchronization.FileToCopyToken.FTCTtype;

public class FolderCompareToken {
	public List<FileToCopyToken> FTC;
	public List<FileToCopyToken> DifferentFiles;
	
	
	FolderCompareToken(File f1, File f2, boolean recursive, int depth)
	{
		FTC=new ArrayList<FileToCopyToken>();
		if (!f1.exists() && !f2.exists()) {
			return;
		}
		if (!f1.exists()) {
			ArrayList<File> s2 = new ArrayList<File>(Arrays.asList(f2.listFiles()));
			for (Iterator<File> iterator = s2.iterator(); iterator.hasNext();) {
				File file = iterator.next();
				if (file.isDirectory()) {
					if (recursive && depth>0)
					{
						String source = f1.getAbsolutePath()+"\\"+file.getName();
						merge(new FolderCompareToken(new File(source), file, true, depth-1));
					}
				}
				if (file.isFile()) {
					String targetPath=f1.getAbsolutePath()+"\\"+file.getName();
					FTC.add(new FileToCopyToken(file, new File(targetPath ),FTCTtype.OnlyInTarget));
				}
				
			}
			return;
		}
		if (!f2.exists()) {
			ArrayList<File> s1 = new ArrayList<File>(Arrays.asList(f1.listFiles()));
			for (Iterator<File> iterator = s1.iterator(); iterator.hasNext();) {
				File file = iterator.next();
				if (file.isDirectory()) {
					if (recursive && depth>0)
					{
						String source = f2.getAbsolutePath()+"\\"+file.getName();
						merge(new FolderCompareToken(file, new File(source), true, depth-1));
					}
				}
				if (file.isFile()) {
					String targetPath=f2.getAbsolutePath()+"\\"+file.getName();
					FTC.add(new FileToCopyToken(file, new File(targetPath ),FTCTtype.OnlyInSource));
				}
				
			}
			return;
		}
		
		
		
		ArrayList<File> sourceFiles = new ArrayList<File>(Arrays.asList(f1.listFiles()));
		sourceFiles.removeIf( f -> !f.isFile());
		ArrayList<File> sourceFolders= new ArrayList<File>(Arrays.asList(f1.listFiles()));
		sourceFolders.removeIf( f -> !f.isDirectory());
		ArrayList<File> targetFiles = new ArrayList<File>(Arrays.asList(f2.listFiles()));
		targetFiles.removeIf( f -> !f.isFile());
		ArrayList<File> targetFolders = new ArrayList<File>(Arrays.asList(f2.listFiles()));
		targetFolders.removeIf( f -> !f.isDirectory());
		File_Comparator fc = new File_Comparator();
		sourceFiles.sort(fc);
		targetFiles.sort(fc);
		ArrayList<Pair<File,File>> sameFiles = new ArrayList<Pair<File,File>>();
		
		int j = 0;
		int k = 0;
		while (j < sourceFiles.size() && k < targetFiles.size()) {
			int compare = fc.compare(sourceFiles.get(j), targetFiles.get(k));
			if (compare==0) {
				int advcompare = fc.advancedCompare(sourceFiles.get(j), targetFiles.get(k));
				if(advcompare==0)
				{
				sameFiles.add(new Pair<File,File>(sourceFiles.get(j),targetFiles.get(k)));
				}
				else {
					String targetPath=f2.getAbsolutePath()+"\\"+sourceFiles.get(j).getName();
					FTC.add(new FileToCopyToken(sourceFiles.get(j), new File(targetPath ), FTCTtype.DifferentSourceAndTarget));
				}
				j++;
				k++;
				
			}
			else
			{
				if(compare>0)
				{
					String targetPath=f1.getAbsolutePath()+"\\"+targetFiles.get(k).getName();
					FTC.add(new FileToCopyToken(targetFiles.get(k), new File(targetPath ),FTCTtype.OnlyInTarget));
					k++;
					
				}
				else 
				{
					String targetPath=f2.getAbsolutePath()+"\\"+sourceFiles.get(j).getName();
					FTC.add(new FileToCopyToken(sourceFiles.get(j), new File(targetPath ),FTCTtype.OnlyInSource));
					j++;
				}
			}
		}
		while (j < sourceFiles.size()) {
			String targetPath=f2.getAbsolutePath()+"\\"+sourceFiles.get(j).getName();
			FTC.add(new FileToCopyToken(sourceFiles.get(j), new File(targetPath ),FTCTtype.OnlyInSource));
			j++;
		}
		while (k < targetFiles.size()) {
			String targetPath=f1.getAbsolutePath()+"\\"+targetFiles.get(k).getName();
			FTC.add(new FileToCopyToken(targetFiles.get(k), new File(targetPath ),FTCTtype.OnlyInTarget));
			k++;
		}
		
		
		
		if (recursive && depth>0) {
			j=0;
			k=0;
			sourceFolders.sort(fc);
			targetFolders.sort(fc);
			while (j<sourceFolders.size() && k<targetFolders.size()) {
				int compare = fc.compare(sourceFolders.get(j), targetFolders.get(k));
				if (compare==0) {
					merge(new FolderCompareToken(sourceFolders.get(j), targetFolders.get(k),true,depth-1));
					j++;k++;
				}
				else if (compare>0) {
					File target = targetFolders.get(k);
					String source = f1.getAbsolutePath()+"\\"+target.getName();
					merge(new FolderCompareToken(new File(source), target, true, depth-1));
					k++;
				} else {
					File source = sourceFolders.get(j);
					String target = f2.getAbsolutePath()+"\\"+source.getName();
					merge(new FolderCompareToken(source, new File(target), true, depth-1));
					j++;
				}
				
			}
			while (j<sourceFolders.size()) {
				File source = sourceFolders.get(j);
				String target = f2.getAbsolutePath()+"\\"+source.getName();
				merge(new FolderCompareToken(source, new File(target), true, depth-1));
				j++;
			}
			while (k<targetFolders.size()) {
				File target = targetFolders.get(k);
				String source = f1.getAbsolutePath()+"\\"+target.getName();
				merge(new FolderCompareToken(new File(source), target, true, depth-1));
				k++;
			}
			
		}
	}
	
	void merge(FolderCompareToken ftc)
	{
		while (!ftc.FTC.isEmpty()) {
			FTC.add(ftc.FTC.remove(ftc.FTC.size()-1));
		}
	}
	
	public void write(FileCheck fc)
	{
		for (Iterator<FileToCopyToken> iterator = FTC.iterator(); iterator.hasNext();) {
			FileToCopyToken fileToCopyToken = iterator.next();
			if (fc.test(fileToCopyToken)) {
				try {
					fileToCopyToken.CopyThis();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	
	public List<FileToCopyToken> getDifferentTokens()
	{
		DifferentFiles = new ArrayList<FileToCopyToken>();
		for (Iterator<FileToCopyToken> iterator = FTC.iterator(); iterator.hasNext();) {
			FileToCopyToken fileToCopyToken = iterator.next();
			if (fileToCopyToken.type==FTCTtype.DifferentSourceAndTarget) {
				DifferentFiles.add(fileToCopyToken);
			}
		}
		
		return DifferentFiles;
	}
	
	interface FileCheck
	{
		boolean test(FileToCopyToken ftct);
	}
	
	
	

}
