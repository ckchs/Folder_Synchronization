package synchronization;

import java.io.File;
import java.io.IOException;
import java.util.*;

import synchronization.FileToCopyToken.FTCTtype;

// TODO: Auto-generated Javadoc
/**
 * The Class FolderCompareToken.
 */
public class FolderCompareToken {
	
	/** The ftc. */
	public List<FileToCopyToken> FTC;

	/**
	 * Generate List of differences between source and target
	 *
	 * @param f1 the f 1
	 * @param f2 the f 2
	 * @param recursive the recursive
	 * @param depth the depth
	 */
	FolderCompareToken(File f1, File f2, boolean recursive, int depth) {
		FTC = new ArrayList<FileToCopyToken>();
		if (!f1.exists() && !f2.exists()) {
			return;
		}
		if (!f1.exists()) {
			ArrayList<File> s2 = new ArrayList<File>(Arrays.asList(f2.listFiles()));
			for (Iterator<File> iterator = s2.iterator(); iterator.hasNext();) {
				File file = iterator.next();
				if (file.isDirectory()) {
					if (recursive || depth > 0) {
						String source = f1.getAbsolutePath() + "\\" + file.getName();
						merge(new FolderCompareToken(new File(source), file, recursive, depth - 1));
					}
				}
				if (file.isFile()) {
					String targetPath = f1.getAbsolutePath() + "\\" + file.getName();
					FTC.add(new FileToCopyToken(file, new File(targetPath), FTCTtype.OnlyInTarget));
				}

			}
			return;
		}
		if (!f2.exists()) {
			ArrayList<File> s1 = new ArrayList<File>(Arrays.asList(f1.listFiles()));
			for (Iterator<File> iterator = s1.iterator(); iterator.hasNext();) {
				File file = iterator.next();
				if (file.isDirectory()) {
					if (recursive || depth > 0) {
						String source = f2.getAbsolutePath() + "\\" + file.getName();
						merge(new FolderCompareToken(file, new File(source), recursive, depth - 1));
					}
				}
				if (file.isFile()) {
					String targetPath = f2.getAbsolutePath() + "\\" + file.getName();
					FTC.add(new FileToCopyToken(file, new File(targetPath), FTCTtype.OnlyInSource));
				}

			}
			return;
		}

		ArrayList<File> sourceFiles = new ArrayList<File>(Arrays.asList(f1.listFiles()));
		sourceFiles.removeIf(f -> !f.isFile());
		ArrayList<File> sourceFolders = new ArrayList<File>(Arrays.asList(f1.listFiles()));
		sourceFolders.removeIf(f -> !f.isDirectory());
		ArrayList<File> targetFiles = new ArrayList<File>(Arrays.asList(f2.listFiles()));
		targetFiles.removeIf(f -> !f.isFile());
		ArrayList<File> targetFolders = new ArrayList<File>(Arrays.asList(f2.listFiles()));
		targetFolders.removeIf(f -> !f.isDirectory());
		File_Comparator fc = new File_Comparator();
		sourceFiles.sort(fc);
		targetFiles.sort(fc);
		ArrayList<Pair<File, File>> sameFiles = new ArrayList<Pair<File, File>>();

		int j = 0;
		int k = 0;
		while (j < sourceFiles.size() && k < targetFiles.size()) {
			int compare = fc.compare(sourceFiles.get(j), targetFiles.get(k));
			if (compare == 0) {
				int advcompare = fc.advancedCompare(sourceFiles.get(j), targetFiles.get(k));
				if (advcompare == 0) {
					sameFiles.add(new Pair<File, File>(sourceFiles.get(j), targetFiles.get(k)));
				} else {
					String targetPath = f2.getAbsolutePath() + "\\" + sourceFiles.get(j).getName();
					FTC.add(new FileToCopyToken(sourceFiles.get(j), new File(targetPath),
							FTCTtype.DifferentSourceAndTarget));
				}
				j++;
				k++;

			} else {
				if (compare > 0) {
					String targetPath = f1.getAbsolutePath() + "\\" + targetFiles.get(k).getName();
					FTC.add(new FileToCopyToken(targetFiles.get(k), new File(targetPath), FTCTtype.OnlyInTarget));
					k++;

				} else {
					String targetPath = f2.getAbsolutePath() + "\\" + sourceFiles.get(j).getName();
					FTC.add(new FileToCopyToken(sourceFiles.get(j), new File(targetPath), FTCTtype.OnlyInSource));
					j++;
				}
			}
		}
		while (j < sourceFiles.size()) {
			String targetPath = f2.getAbsolutePath() + "\\" + sourceFiles.get(j).getName();
			FTC.add(new FileToCopyToken(sourceFiles.get(j), new File(targetPath), FTCTtype.OnlyInSource));
			j++;
		}
		while (k < targetFiles.size()) {
			String targetPath = f1.getAbsolutePath() + "\\" + targetFiles.get(k).getName();
			FTC.add(new FileToCopyToken(targetFiles.get(k), new File(targetPath), FTCTtype.OnlyInTarget));
			k++;
		}

		if (recursive || depth > 0) {
			j = 0;
			k = 0;
			sourceFolders.sort(fc);
			targetFolders.sort(fc);
			while (j < sourceFolders.size() && k < targetFolders.size()) {
				int compare = fc.compare(sourceFolders.get(j), targetFolders.get(k));
				if (compare == 0) {
					merge(new FolderCompareToken(sourceFolders.get(j), targetFolders.get(k), recursive, depth - 1));
					j++;
					k++;
				} else if (compare > 0) {
					File target = targetFolders.get(k);
					String source = f1.getAbsolutePath() + "\\" + target.getName();
					merge(new FolderCompareToken(new File(source), target, recursive, depth - 1));
					k++;
				} else {
					File source = sourceFolders.get(j);
					String target = f2.getAbsolutePath() + "\\" + source.getName();
					merge(new FolderCompareToken(source, new File(target), recursive, depth - 1));
					j++;
				}

			}
			while (j < sourceFolders.size()) {
				File source = sourceFolders.get(j);
				String target = f2.getAbsolutePath() + "\\" + source.getName();
				merge(new FolderCompareToken(source, new File(target), recursive, depth - 1));
				j++;
			}
			while (k < targetFolders.size()) {
				File target = targetFolders.get(k);
				String source = f1.getAbsolutePath() + "\\" + target.getName();
				merge(new FolderCompareToken(new File(source), target, recursive, depth - 1));
				k++;
			}

		}
	}

	/**
	 * Merge.
	 * Used when recursion return is merged into actual list 
	 * @param ftc the ftc
	 */
	void merge(FolderCompareToken ftc) {
		while (!ftc.FTC.isEmpty()) {
			FTC.add(ftc.FTC.remove(ftc.FTC.size() - 1));
		}
	}

	/**
	 * Write.
	 * Tell each token to write. Which write and which dont depends on type of write
	 * @param type the type
	 */
	public void write(typeOfWrite type) {

		if (type == typeOfWrite.BothWaysAskDiff || type == typeOfWrite.SourceToTargetAskDiffs
				|| type == typeOfWrite.TargetToSourceAskDiffs) {
			List<FileToCopyToken> different = getDifferentTokens();
			DifferentFilesChooser dfc = new DifferentFilesChooser(different);
			dfc.consoleAsk();
		}

		for (Iterator<FileToCopyToken> iterator = FTC.iterator(); iterator.hasNext();) {
			FileToCopyToken fileToCopyToken = iterator.next();

			try {
				switch (type) {
				case BothWayDiffsToSource:
					if (fileToCopyToken.type == FTCTtype.DifferentSourceAndTarget) {
						fileToCopyToken.swapSourceTarget();
					}
					fileToCopyToken.CopyThis();
					break;
				case BothWayDiffsToTarget:
					fileToCopyToken.CopyThis();
					break;
				case BothWayNoDiffs:
					if (fileToCopyToken.type != FTCTtype.DifferentSourceAndTarget)
						fileToCopyToken.CopyThis();
					break;
				case BothWaysAskDiff:
					fileToCopyToken.CopyThis();
					break;
				case SourceToTargetAskDiffs:
					if (fileToCopyToken.type != FTCTtype.OnlyInTarget) {
						fileToCopyToken.CopyThis();
					}
					break;
				case SourceToTargetNoDiffs:
					if (fileToCopyToken.type == FTCTtype.OnlyInSource) {
						fileToCopyToken.CopyThis();
					}
					break;
				case SourceToTargetWithDiffs:
					if (fileToCopyToken.type != FTCTtype.OnlyInTarget) {
						fileToCopyToken.CopyThis();
					}
					break;
				case TargetToSourceAskDiffs:
					if (fileToCopyToken.type != FTCTtype.OnlyInSource) {
						fileToCopyToken.CopyThis();
					}
					break;
				case TargetToSourceNoDiffs:
					if (fileToCopyToken.type == FTCTtype.OnlyInTarget) {
						fileToCopyToken.CopyThis();
					}
					break;
				case TargetToSourceWithDiffs:
					if (fileToCopyToken.type == FTCTtype.DifferentSourceAndTarget) {
						fileToCopyToken.swapSourceTarget();
					}
					if (fileToCopyToken.type != FTCTtype.OnlyInSource)
						fileToCopyToken.CopyThis();

					break;
				default:
					break;
				}

				fileToCopyToken.CopyThis();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			}
		}

	}

	/**
	 * Gets the different tokens.
	 *
	 * @return Tokens that are both in source and target but are different
	 */
	private List<FileToCopyToken> getDifferentTokens() {
		List<FileToCopyToken> DifferentFiles = new ArrayList<FileToCopyToken>();
		for (Iterator<FileToCopyToken> iterator = FTC.iterator(); iterator.hasNext();) {
			FileToCopyToken fileToCopyToken = iterator.next();
			if (fileToCopyToken.type == FTCTtype.DifferentSourceAndTarget) {
				DifferentFiles.add(fileToCopyToken);
			}
		}

		return DifferentFiles;
	}

	/**
	 * The Enum typeOfWrite.
	 */
	enum typeOfWrite {
		
		/** The bad type. */
		badType, 
 /** The Both ways ask diff. */
 BothWaysAskDiff, 
 /** The Both way diffs to target. */
 BothWayDiffsToTarget, 
 /** The Both way diffs to source. */
 BothWayDiffsToSource, 
 /** The Both way no diffs. */
 BothWayNoDiffs, 
 /** The Source to target with diffs. */
 SourceToTargetWithDiffs, 
 /** The Source to target ask diffs. */
 SourceToTargetAskDiffs, 
 /** The Source to target no diffs. */
 SourceToTargetNoDiffs, 
 /** The Target to source with diffs. */
 TargetToSourceWithDiffs, 
 /** The Target to source ask diffs. */
 TargetToSourceAskDiffs, 
 /** The Target to source no diffs. */
 TargetToSourceNoDiffs
	}


}
