/*
 * $Id: Agent.java,v 1.2 2004/06/21 14:56:29 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.agent;

import com.syrus.AMFICOM.RISDConnection;
import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.General.AMFICOMRemoteException;
import com.syrus.AMFICOM.CORBA.KIS.*;
import com.syrus.AMFICOM.server.measurement.Result;
import com.syrus.util.*;
import java.sql.Timestamp;
import java.util.*;

/**
 * @version $Revision: 1.2 $, $Date: 2004/06/21 14:56:29 $
 * @author $Author: bass $
 * @module agent_v1
 */
public class Agent extends Thread  {
	private static final long KISTIMEWAIT = 1000;
	private static final long RISDTIMEWAIT = 1000;
	static final long MEASURETIME = 10*60*1000;
	private static final String AGENT_ID = "agent-1";
	private static final String SERVICEURL = "sess_iiop://research:2481:research";
	private static final String USERNAME = "amficom";
	private static final String PASSWORD = "amficom";
	private static final String SERVEROBJECTNAME="/AMFICOM/AMFICOMKIS";
	static long kistimewait;
	static long risdtimewait;
	static long measuretime;
	static String agent_id;
	static List taskQueue;
	static List reportQueue;
	static Hashtable testContainers;
	static AMFICOMKIS amficomkis;
	private static String[] kis_ids;

  public static void main(String[] args) {
		//Initialize application
		Application.init("agent");
		System.out.println("Setting values from file " + ApplicationProperties.getFileName());

		kistimewait = (ApplicationProperties.getInt("kistimewait", (int)KISTIMEWAIT/1000)) * 1000;
		risdtimewait = (ApplicationProperties.getInt("risdtimewait", (int)RISDTIMEWAIT/1000)) * 1000;
		measuretime = (ApplicationProperties.getInt("measuretime", (int)MEASURETIME/1000)) * 1000;
		agent_id = ApplicationProperties.getString("agent_id", AGENT_ID);

		String serverObjectName = ApplicationProperties.getString("ServerObjectName", SERVEROBJECTNAME);
		RISDConnection risdconnection = new RISDConnection(ApplicationProperties.getString("serviceURL", SERVICEURL),
																												ApplicationProperties.getString("username", USERNAME),
																												ApplicationProperties.getString("password", PASSWORD));

		amficomkis = (AMFICOMKIS)risdconnection.getServerObject(serverObjectName);
    if(amficomkis == null) {
      Log.errorMessage("Unable to get server object -- exiting!");
      System.exit(-1);
    }

    taskQueue = Collections.synchronizedList(new LinkedList());
    reportQueue = Collections.synchronizedList(new LinkedList());
    testContainers = new Hashtable(Collections.synchronizedMap(new Hashtable()));

    try {
      kis_ids = amficomkis.getKISIdentities(new AgentIdentity_Transferable(agent_id, "hz"));
			//kis_ids = getKISIdentities_Stub();
    }
    catch (AMFICOMRemoteException e) {
			Log.errorMessage("AMFICOMRemoteException: " + e.message);
    }

    Agent agent = new Agent();
    agent.start();
    TaskReceiver taskReceiver = new TaskReceiver();
    taskReceiver.start();
    ReportReceiver reportReceiver;
    for (int i = 0; i < kis_ids.length; i++) {
      reportReceiver = new ReportReceiver(kis_ids[i]);
      reportReceiver.start();
    }
  }

  public void run() {
    Test_Transferable tt;
		Result_Transferable result;
		int retcode;		
		AgentIdentity_Transferable agentId = new AgentIdentity_Transferable(agent_id, "hz");
    String kis_id;
    TestContainer testContainer;
    while (true) {
			synchronized (taskQueue) {
	      if (!taskQueue.isEmpty()) {
		      if (((Test_Transferable)taskQueue.get(0)).start_time <= System.currentTimeMillis()) {
			      tt = (Test_Transferable)taskQueue.remove(0);
				    Log.debugMessage("Removed from taskQueue id: " + tt.id, Log.DEBUGLEVEL03);
					  kis_id = findKISIDForTest(tt);
						if (kis_id != null) {
							testContainer = new TestContainer(tt, kis_id);
							synchronized (testContainers) {
								testContainers.put(tt.id, testContainer);
							}
		          testContainer.start();
			      }
				    else {
					    Log.errorMessage("ERROR: KIS " + tt.kis_id + " isn't registered for this Agent");
						}
	        }
		      else
			      Log.debugMessage("curtime: " + new Timestamp(System.currentTimeMillis()).toString(), Log.DEBUGLEVEL05);
				    try { sleep(kistimewait); }
					  catch(InterruptedException ie) { System.out.println("Interrupted!"); }
				}//if !taskQueue.isEmpty()
				else {
					try { sleep(risdtimewait); }
	        catch(InterruptedException ie) { System.out.println("Interrupted!"); }
		    }//else if !taskQueue.isEmpty()
			}//synchronized (taskQueue)


			synchronized (reportQueue) {
				if (!reportQueue.isEmpty()) {
					result = (Result_Transferable)reportQueue.get(0);
			    retcode = Constants.ERROR_EMPTY;
				  try{
					  retcode = amficomkis.reportResult(agentId, result);
						//retcode = reportResult_Stub(result);
	        }
		      catch (AMFICOMRemoteException e) {
			      Log.errorMessage("Exception while reporting result: " + e.message);
						continue;
				  }
					catch (Exception ex) {
						Log.errorException(ex);
						continue;
					}
					switch (retcode) { 
						case Constants.ERROR_NO_ERROR:
							Log.debugMessage("Successfully saved result", Log.DEBUGLEVEL03);
	            reportQueue.remove(result);
		          break;
			      case Constants.ERROR_SAVING:
				      Log.errorMessage("ERROR: Cannot save result");
					    break;
						case Constants.ERROR_UPDATING:
							Log.errorMessage("ERROR: Cannot update tests or testrequests");
	            break;
		      }
				}
			}//synchronized (reportQueue)
    }//while
  }

  private static String findKISIDForTest(Test_Transferable tt) {
    int i = 0;
    while (i < kis_ids.length && !tt.kis_id.equals(kis_ids[i]))
      i++;
    if (i < kis_ids.length)
      return kis_ids[i];
    else
      return null;
  }

	private static String[] getKISIdentities_Stub() throws AMFICOMRemoteException {
		String[] str = new String[1];
		str[0] = "kis1";
		return str;
	}

	private static int reportResult_Stub(Result_Transferable result) throws AMFICOMRemoteException {
    System.out.println("---------------- report ----------------");
    if (result.result_type.equals(Result.RESULT_TYPE_TEST)) {
      System.out.println("test_id == '" + result.action_id + "'");
    }
    else
      if (result.result_type.equals(Result.RESULT_TYPE_ANALYSIS)) {
        System.out.println("Analysis id: '" + result.action_id + "'");
      }
      else
        if (result.result_type.equals(Result.RESULT_TYPE_EVALUATION)) {
          System.out.println("Evaluation id: '" + result.action_id + "'");
        }
        else
          System.out.println("REPORT | Unknown type of result: '" + result.result_type + "'");

    for (int i = 0; i < result.parameters.length; i++) {
      System.out.println("parameter[" + i + "].name == '" + result.parameters[i].name + "'");
      System.out.println("  length of value == " + result.parameters[i].value.length);
    }

    return Constants.ERROR_NO_ERROR;
  }
}
