package info5.sar.Channel.Impl;

import info5.sar.Channel.Abstract.Broker;
import info5.sar.Channel.Abstract.Task;

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