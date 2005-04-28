/*
 * $Id: LoginProcessor.java,v 1.2 2005/04/28 14:11:52 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.leserver;

import com.syrus.AMFICOM.general.SleepButWorkThread;
import com.syrus.util.ApplicationProperties;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.2 $, $Date: 2005/04/28 14:11:52 $
 * @author $Author: arseniy $
 * @module leserver_v1
 */
final class LoginProcessor extends SleepButWorkThread {
	public static final String KEY_LOGIN_PROCESSOR_TICK_TIME = "LoginProcessorTickTime";
	public static final String KEY_LOGIN_PROCESSOR_MAX_FALLS = "LoginProcessorMaxFalls";

	public static final int LOGIN_PROCESSOR_TICK_TIME = 5;	//sec

	private boolean running;

	public LoginProcessor() {
		super(ApplicationProperties.getInt(KEY_LOGIN_PROCESSOR_TICK_TIME, LOGIN_PROCESSOR_TICK_TIME) * 1000,
				ApplicationProperties.getInt(KEY_LOGIN_PROCESSOR_MAX_FALLS, MAX_FALLS));
		this.running = true;

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				LoginProcessor.this.shutdown();
			}
		});
	}

	public void run() {
		while (this.running) {
			//TODO Implement

			try {
				sleep(super.initialTimeToSleep);
			}
			catch (InterruptedException ie) {
				Log.errorException(ie);
			}
		}
	}

	protected void processFall() {
		switch (super.fallCode) {
			case FALL_CODE_NO_ERROR:
				break;
			default:
				Log.errorMessage("processError | Unknown error code: " + super.fallCode);
		}
	}

	protected void shutdown() {
		this.running = false;
	}

}
