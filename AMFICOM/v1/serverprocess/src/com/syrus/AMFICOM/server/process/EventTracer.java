/*
 * $Id: EventTracer.java,v 1.1 2004/06/22 09:57:10 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process;

import com.syrus.AMFICOM.CORBA.General.EventStatus;
import com.syrus.AMFICOM.server.event.*;
import com.syrus.AMFICOM.server.measurement.*;
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;
import java.sql.SQLException;
import java.util.*;
import sqlj.runtime.ref.DefaultContext;

/**
 * @version $Revision: 1.1 $, $Date: 2004/06/22 09:57:10 $
 * @module serverprocess
 */
public final class EventTracer extends Thread {
	/**
	 * @todo Default JDBC connection is stored in DatabaseConnection. This
	 *       procedure should be performed only once (on server process
	 *       startup), but all JDBC-based aplications must be aware of it.
	 */
	static {
		DatabaseConnection.setConnection(DefaultContext.getDefaultContext().getConnection());
	}

	private volatile boolean running = true;

	public void stopRunning() {
		running = false;
	}

	public void run() {
		LinkedList llev;
		while (running) {
			llev = Event.getGeneratedEvents();
			if (llev != null) {
				Iterator iterator = llev.iterator();
				while (iterator.hasNext())
					try {
						processEvent((Event) (iterator.next()));
					} catch (SQLException e) {
						Log.errorException(e);
					}
			}
			try {
				sleep(1000);
			} catch (InterruptedException ie) {
				System.out.println("EventProcessor | Interrupted!");
			}
		}
	}

	private void processEvent(Event event) throws SQLException {
		String rule = (new EventSource(event.getSourceId())).getRule(event.getTypeId());
		RuleParser rp = new RuleParser(rule);
		String action = rp.getAction();
		if (action == null) {
			Log.errorMessage("action for event " + event.getId() + "is null");
			return;
		}
		Log.debugMessage("action: " + action, Log.DEBUGLEVEL03);
		if (action.equals("GENERATE_ALARM")) {
			Alarm alarm = new Alarm(event, rp.getActionParameter(0));
			Alerting.generateAlertings(event);
			try {
				Result result = new Result(event.getDescriptor());
				if (result.getResultType().equals(Result.RESULT_TYPE_EVALUATION)) {
					Test test = Test.retrieveTestForEvaluation(result.getAction().getId());
					test.setModified();
				} else
					Log.debugMessage("Result " + result.getId() + " not of type " + Result.RESULT_TYPE_EVALUATION, Log.DEBUGLEVEL05);
			} catch (Exception e) {
				Log.errorMessage("Cannot find test for alarm " + alarm.getId());
				Log.errorException(e);
			}
//			Event alarmevent = new Event(alarm.getId(), event.getDescription(), EventSource.getIdByObjectId("server1"), "alarmgeneratedevent");
		}
		event.updateStatus(EventStatus._EVENT_STATUS_PROCESSED);
	}
}
