package synchronization;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import synchronization.FolderCompareToken.typeOfWrite;

public class RealTimeSync {
	File Source;
	File Target;
	boolean recursive = false;
	int recursive_depth = 0;
	long time;
	typeOfWrite type;

	public RealTimeSync(File Source, File Target, boolean recursive, int recursive_depth, long time, typeOfWrite type) {
		this.Source = Source;
		this.Target = Target;
		this.recursive = recursive;
		this.recursive_depth = recursive_depth;
		this.time = time;
		this.type = type;
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(runSynchronization, 0, time, TimeUnit.SECONDS);

	}

	Runnable runSynchronization = new Runnable() {
		public void run() {
			FolderCompareToken fct = new FolderCompareToken(Source, Target, recursive, recursive_depth);
			fct.write(type);
		}
	};

}
