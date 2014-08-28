package io.loli.sc.system;


/**
 * 运行任务的Runnable，在新线程中执行task.run()方法
 * 
 * @author choco
 * 
 */
public class TaskRunnable extends Thread {
	private HotKeyTask task;
	private int index;

	TaskRunnable(int index, HotKeyTask task) {
		super();
		this.task = task;
		this.index = index;
	}

	@Override
	public void run() {
		task.run(index);
	}

}