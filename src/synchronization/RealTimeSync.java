package synchronization;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import synchronization.FolderCompareToken.typeOfWrite;

// TODO: Auto-generated Javadoc
/**
 * The Class RealTimeSync.
 * 
 */
public class RealTimeSync {
	
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
	
	

	/**
	 * Run scheduled service with synchronisation.
	 *
	 * @param Source the source
	 * @param Target the target
	 * @param recursive the recursive
	 * @param recursive_depth the recursive depth
	 * @param time the time
	 * @param type the type
	 */
	public RealTimeSync(File Source, File Target, boolean recursive, int recursive_depth, long time, typeOfWrite type) {
		this.Source = Source;
		this.Target = Target;
		this.recursive = recursive;
		this.recursive_depth = recursive_depth;
		this.time = time;
		this.type = type;
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
			FolderCompareToken fct = new FolderCompareToken(Source, Target, recursive, recursive_depth);
			fct.write(type);
		}
	};

}
