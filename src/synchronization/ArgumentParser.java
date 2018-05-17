package synchronization;

import java.io.*;
import java.text.SimpleDateFormat;
import synchronization.FolderCompareToken.typeOfWrite;

// TODO: Auto-generated Javadoc
/**
 * The Class ArgumentParser.
 */
public class ArgumentParser {
	
	/** The Constant COPY_BUFFER. */
	public static final int COPY_BUFFER = 1024 * 1024 * 8;


	/** The Source. */
	static File Source;
	
	/** The Target. */
	static File Target;
	
	/** The recursive. */
	static boolean recursive = false;
	
	/** The recursive depth. */
	static int recursive_depth = 0;
	
	/** The real time sync. */
	static boolean realTimeSync=false;
	
	/** The back up. */
	static boolean backUp=false;
	
	/** The type str. */
	static String typeStr="bwnd";
	
	/** The type. */
	static typeOfWrite type;
	
	/** The time. */
	static long time=60;
	
	/** The sdt. */
	static SimpleDateFormat sdt=new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
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
					break;
				case "-backup":
					backUp=true;
					break;
				case "-type":
					i++;
					typeStr=args[i];
					break;
				case "-format":
					i++;
					sdt=new SimpleDateFormat(args[i]);
					break;
				case "-d":
					i++;
					recursive_depth=Integer.parseInt(args[i]);
					break;
				case "-r":
					recursive=true;
					break;
				default:
					throw new Exception("wrong argument");
					
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
	
	/**
	 * String to type.
	 *
	 * @param string parameter to parse
	 * @return parsed type
	 */
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
