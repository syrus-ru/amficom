/*
 * $Id: TaskReceiver.java,v 1.4 2004/07/21 08:18:10 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.agent;

import java.io.IOException;
import java.sql.Timestamp;
import com.syrus.AMFICOM.CORBA.General.TestTimeStamps;
import com.syrus.AMFICOM.CORBA.General.TestTemporalType;
import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.CORBA.General.TestReturnType;
import com.syrus.AMFICOM.CORBA.General.AMFICOMRemoteException;
import com.syrus.AMFICOM.CORBA.KIS.AgentIdentity_Transferable;
import com.syrus.AMFICOM.CORBA.KIS.Test_Transferable;
import com.syrus.AMFICOM.CORBA.KIS.Analysis_Transferable;
import com.syrus.AMFICOM.CORBA.KIS.Evaluation_Transferable;
import com.syrus.AMFICOM.CORBA.KIS.Etalon_Transferable;
import com.syrus.AMFICOM.CORBA.KIS.Parameter_Transferable;
import com.syrus.util.ByteArray;
import com.syrus.util.Log;

// For queryTests_Stub()

/**
 * @version $Revision: 1.4 $, $Date: 2004/07/21 08:18:10 $
 * @author $Author: arseniy $
 * @module agent_v1
 */
public class TaskReceiver extends Agent  {
	public void run() {

//Query
		AgentIdentity_Transferable agentIdT = new AgentIdentity_Transferable(agentId, "hz");
    Test_Transferable[] tests;
		String[] atids;
    while(true) {
      tests = null;
			atids = null;

      try {
        tests = amficomkis.queryTests(agentIdT, risdtimewait);
        //tests = queryTests_Stub();
      }
      catch (AMFICOMRemoteException e) {
        Log.errorMessage("Exception while quering tests: "  + e.message);
				continue;
      }
			catch (Exception ex) {
        Log.errorMessage("Exception while quering tests");
				Log.errorException(ex);
				continue;
      }
      if (tests != null) {
        Log.debugMessage("tests.length == " + tests.length, Log.DEBUGLEVEL05);
				synchronized (taskQueue) {
					for (int i = 0; i < tests.length; i++)
						insertTest(tests[i]);
				}
      }
      else
        Log.debugMessage("tests == NULL", Log.DEBUGLEVEL05);

			try {
				atids = amficomkis.queryAbortedTests(agentIdT);
			}
			catch (AMFICOMRemoteException e) {
        Log.errorMessage("Exception while quering aborted tests: "  + e.message);
				continue;
      }
			catch (Exception ex) {
				Log.errorMessage("Exception while quering aborted tests");
				Log.errorException(ex);
				continue;
			}
			TestContainer testContainer;
			if (atids != null && atids.length > 0) {
				for (int i = 0; i < atids.length; i++) {
					Log.debugMessage("------- Aborting test: '" + atids[i] + "' -------", Log.DEBUGLEVEL03);
					synchronized (testContainers) {
						testContainer = ((TestContainer)testContainers.get(atids[i]));
					}
					if (testContainer != null)
						testContainer.abort();
					else {
						Log.debugMessage("TaskReceiver.run | WARNING: Cannot find testcontainer for test '" + atids[i] + "'!", Log.DEBUGLEVEL05);
						synchronized (taskQueue) {
							for (int j = 0; j < taskQueue.size(); j++) {
								if (((Test_Transferable)taskQueue.get(j)).id.equals(atids[i])) {
									taskQueue.remove(j);
									Log.debugMessage("TaskReceiver.run | Test '" + atids[i] + "' removed from taskQueue", Log.DEBUGLEVEL05);
								}
							}
						}//synchronized (taskQueue)
					}//else if (testContainer != null)
				}//for
			}//if (atids != null && atids.length > 0)
      
// Try to sleep
      try { sleep(risdtimewait); }
      catch(InterruptedException ie) { System.out.println("Interrupted!"); }
    }
  }

  private static void insertTest(Test_Transferable test) {
    Log.debugMessage("Adding to taskQueue test with start_time = " + test.start_time + ", " + (new Timestamp(test.start_time)).toString() + ", test_id = '" + test.id + "', parameters - " + test.parameters.length, Log.DEBUGLEVEL05);
    if (taskQueue.isEmpty())
      taskQueue.add(test);
    else {
      if (test.start_time >= ((Test_Transferable)taskQueue.get(taskQueue.size() - 1)).start_time)
        taskQueue.add(test);
      else
        for (int j = 0; j < taskQueue.size(); j++) {
          if (test.start_time <= ((Test_Transferable)taskQueue.get(j)).start_time) {
            taskQueue.add(j, test);
            break;
          }
        }
    }
    printTastQueue();
  }

  private static void printTastQueue() {
    for (int i = 0; i < taskQueue.size(); i++) {
      long t = ((Test_Transferable)taskQueue.get(i)).start_time;
      Log.debugMessage("start_time(" + i + ") = " + t + ", " + (new Timestamp(t)).toString(), Log.DEBUGLEVEL05);
    }
  }

  private static Test_Transferable[] queryTestsStub() throws AMFICOMRemoteException {
    System.out.println("---------------- query ----------------");
    Test_Transferable[] tests = new Test_Transferable[1];
    TestTimeStamps tts = new TestTimeStamps();
    tts._default();
    Parameter_Transferable[] parameters = new Parameter_Transferable[6];
    try {
      parameters[0] = new Parameter_Transferable("ref_wvlen", "ttal6", (new ByteArray((int)1550)).getBytes()); 
      parameters[1] = new Parameter_Transferable("ref_trclen", "ttal1", (new ByteArray((double)65.535)).getBytes());
      parameters[2] = new Parameter_Transferable("ref_res", "ttal4", (new ByteArray((double)4)).getBytes());
      parameters[3] = new Parameter_Transferable("ref_pulswd", "ttal9", (new ByteArray((long)1000)).getBytes());
      parameters[4] = new Parameter_Transferable("ref_ior", "ttal5", (new ByteArray((double)1.467)).getBytes());
      parameters[5] = new Parameter_Transferable("ref_scans", "ttal2", (new ByteArray((double)4000)).getBytes());
    }
    catch (IOException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    Analysis_Transferable analysis
      = new Analysis_Transferable("ani3",
                                                              "dadara",
                                                              new Parameter_Transferable[0]);
    Evaluation_Transferable evaluation
      = new Evaluation_Transferable(null,
                                                                null,
                                                                new Parameter_Transferable[0],
                                                                new Etalon_Transferable(null,
                                                                                                                    null,
                                                                                                                    new Parameter_Transferable[0]));
    
    tests[0] = new Test_Transferable("test1",
                                     "trace_and_analyse",
                                     "treq1",
                                     TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME,
                                     System.currentTimeMillis() + 10*1000,
                                     tts,
                                     TestStatus.TEST_STATUS_SCHEDULED,
                                     "kis1",
                                     "mone2",
                                     "ME-199058001202-20030205125200-00000",
                                     parameters,
                                     analysis,
                                     evaluation,
                                     TestReturnType.TEST_RETURN_TYPE_WHOLE);
		return tests;
	}
}
