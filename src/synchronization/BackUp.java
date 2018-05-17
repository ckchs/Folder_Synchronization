package synchronization;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import synchronization.FolderCompareToken.typeOfWrite;

// TODO: Auto-generated Javadoc
/**
 * The Class BackUp.
 */
public class BackUp {
	
	/** The Source. */
	File Source;
	
	/** The Target. */
	File Target;
	
	/** The recursive. */
	boolean recursive = false;
	
	/** The recursive depth. */
	int recursive_depth = 0;
	
	/** The time. */
	long time;
	
	/** The type. */
	typeOfWrite type;
	
	/** The SimpleDateFormat. */
	SimpleDateFormat sdt;

	/**
	 * Run scheduled service with synchronisation.
	 *
	 * @param Source the source
	 * @param Target the target
	 * @param recursive the recursive
	 * @param recursive_depth the recursive depth
	 * @param time the time
	 * @param type the type
	 * @param sdt the sdt
	 */
	public BackUp(File Source, File Target, boolean recursive, int recursive_depth, long time, typeOfWrite type,SimpleDateFormat sdt) {
		this.Source = Source;
		this.Target = Target;
		this.recursive = recursive;
		this.recursive_depth = recursive_depth;
		this.time = time;
		this.type = type;
		this.sdt=sdt;
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(runSynchronization, 0, time, TimeUnit.SECONDS);
		System.out.println("Write anything to stop synchronisation");
		Scanner in = new Scanner(System.in);
		String s= in.nextLine();
		executor.shutdown();
	}

	/** Method to be executed every n seconds. */
	Runnable runSynchronization = new Runnable() {
		public void run() {
			Calendar cal= Calendar.getInstance();
			File NewTarget =new File( Target.getAbsolutePath() + "\\" + sdt.format(cal.getTime()));
			FolderCompareToken fct = new FolderCompareToken(Source, NewTarget, recursive, recursive_depth);
			fct.write(type);
		}
	};

}