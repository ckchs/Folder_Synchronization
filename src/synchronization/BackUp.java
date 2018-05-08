package synchronization;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import synchronization.FolderCompareToken.typeOfWrite;

public class BackUp {
	File Source;
	File Target;
	boolean recursive = false;
	int recursive_depth = 0;
	long time;
	typeOfWrite type;
	SimpleDateFormat sdt;

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

	}

	Runnable runSynchronization = new Runnable() {
		public void run() {
			Calendar cal= Calendar.getInstance();
			File NewTarget =new File( Target.getAbsolutePath() + "\\" + sdt.format(cal.getTime()));
			FolderCompareToken fct = new FolderCompareToken(Source, NewTarget, recursive, recursive_depth);
			fct.write(type);
		}
	};

}