package synchronization;

import java.io.*;
import java.text.SimpleDateFormat;
import synchronization.FolderCompareToken.typeOfWrite;

public class ArgumentParser {
	public static final int COPY_BUFFER = 1024 * 1024 * 8;
	public static final int COMPARE_BUFFER = 1024 * 8;

	static File Source;
	static File Target;
	static boolean recursive = false;
	static int recursive_depth = 0;
	static boolean realTimeSync=false;
	static boolean backUp=false;
	static String typeStr;
	static typeOfWrite type;
	static long time;
	static SimpleDateFormat sdt=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
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
				case "-rts":
					realTimeSync=true;
					break;
				case "-time":
					i++;
					time=Long.parseLong(args[i]);
				case "-backup":
					backUp=true;
					break;
				case "-type":
					i++;
					typeStr=args[i];
				case "-format":
					i++;
					sdt=new SimpleDateFormat(args[i]);
				default:
					break;

				}
			}
			type=stringToType(typeStr);
			
			
			
		} catch (Exception e) {
			System.out.println("argument parsing error");
		}

		try {
			if (realTimeSync) {
					if (type==typeOfWrite.badType) {
						type=typeOfWrite.BothWayDiffsToTarget;
					}
					new RealTimeSync(Source, Target, recursive, recursive_depth, time, type);
					return;
				}
			if (backUp) {
				if (type==typeOfWrite.badType) {
					type=typeOfWrite.SourceToTargetWithDiffs;
				}
				new BackUp(Source, Target, recursive, recursive_depth, time, type, sdt);
				return;
			}
			if (type==typeOfWrite.badType) {
				type=typeOfWrite.BothWayDiffsToTarget;
			}
			FolderCompareToken fct= new FolderCompareToken(Source, Target, recursive, recursive_depth);
			fct.write(type);
			
			
			
		} catch (Exception e) {
			System.out.print(e.toString());
		}

	}
	
	private static typeOfWrite stringToType(String s)
	{
		switch (s) {
		case "bwad":
			return typeOfWrite.BothWaysAskDiff;
		case "bwdts":
			return typeOfWrite.BothWayDiffsToSource;
		case "bwdtt":
			return typeOfWrite.BothWayDiffsToTarget;
		case "bwnd":
			return typeOfWrite.BothWayNoDiffs;
		case "sttad":
			return typeOfWrite.SourceToTargetAskDiffs;
		case "sttnd":
			return typeOfWrite.SourceToTargetNoDiffs;
		case "sttwd":
			return typeOfWrite.SourceToTargetWithDiffs;
		case "ttsad":
			return typeOfWrite.TargetToSourceAskDiffs;
		case "ttsnd":
			return typeOfWrite.TargetToSourceNoDiffs;
		case "ttswd":
			return typeOfWrite.TargetToSourceWithDiffs;
			

		default:
			return typeOfWrite.badType;
		}
	}

}
