package info5.sar.task1.Impl;

import info5.sar.task1.Broker;
import info5.sar.task1.Task;

public class TaskImpl extends Task{
	
	private Broker _broker;
	private Runnable _runable;

	public TaskImpl(Broker b, Runnable r) {
		super(b, r);
		this._broker = b;
		this._runable = r;
	}

	@Override
	public Broker getBroker() {
		return this._broker;
	}

	@Override
	public void run() {
		this._runable.run();
	}

}
