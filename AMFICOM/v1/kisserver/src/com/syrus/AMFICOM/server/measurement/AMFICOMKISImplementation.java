package com.syrus.AMFICOM.server.measurement;

import java.sql.SQLException;
import sqlj.runtime.ref.DefaultContext;
import com.syrus.AMFICOM.CORBA._AMFICOMKISImplBase;
import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.CORBA.General.AMFICOMRemoteException;
import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.CORBA.KIS.AgentIdentity_Transferable;
import com.syrus.AMFICOM.CORBA.KIS.Test_Transferable;
import com.syrus.AMFICOM.CORBA.KIS.Result_Transferable;
import com.syrus.AMFICOM.CORBA.General.AlarmLevel;
import com.syrus.AMFICOM.server.event.Event;
import com.syrus.AMFICOM.server.event.EventSource;
/*import com.syrus.util.database.Server;*/
import com.syrus.util.Log;
import com.syrus.util.database.DatabaseConnection;

public class AMFICOMKISImplementation extends _AMFICOMKISImplBase {

	static {/*
		Server.init("amficomkis");*/
		
    DefaultContext dc = DefaultContext.getDefaultContext();
    try {
      dc.getConnection().setAutoCommit(false);
    }
    catch (SQLException e) {
      Log.errorException(e);
    }
    DefaultContext.setDefaultContext(dc);

		DatabaseConnection.setConnection(dc.getConnection());
	}

  public Test_Transferable[] queryTests(AgentIdentity_Transferable agentid,
																	 long interval)
                                    throws AMFICOMRemoteException {
//Check access rights
    if (!this.checkRigths(agentid)) {
      Log.errorMessage("AMFICOMKIS.query | ERROR: Authentication failed!");
      throw new AMFICOMRemoteException(Constants.ERROR_INSUFFICIENT_PRIVILEGES, "AMFICOMKIS.query | ERROR: Authentication failed!");
    }
//Find tests
    Test[] tests;
    try {
      tests = Test.retrieveScheduledTests(agentid.agent_id, interval);
    }
    catch (SQLException e) {
      String mesg = "AMFICOMKIS | Exception while retrieving scheduled tests: " + e.getMessage();
      Log.errorMessage(mesg);
      Log.errorException(e);
      throw new AMFICOMRemoteException(Constants.ERROR_LOADING, mesg);
    }
    Test_Transferable[] tts = new Test_Transferable[tests.length];
    try {
      for (int i = 0; i < tts.length; i++) {
				tests[i].updateStatus(TestStatus._TEST_STATUS_PROCESSING);
				tests[i].setModified();
        tts[i] = tests[i].getTransferable();
			}
    }
    catch (SQLException e) {
      String mesg = "AMFICOMKIS | Exception while getting transferable test" + e.getMessage();
      Log.errorMessage(mesg);
      Log.errorException(e);
      throw new AMFICOMRemoteException(Constants.ERROR_LOADING, mesg);
    }
		if (tts.length > 0)
			Log.debugMessage("AMFICOMKIS | Retrieved " + tts.length + " scheduled tests", Log.DEBUGLEVEL03);
    return tts;
  }

	public String[] queryAbortedTests(AgentIdentity_Transferable agentid)
                                    throws AMFICOMRemoteException {
		if (!this.checkRigths(agentid)) {
      Log.errorMessage("AMFICOMKIS.query | ERROR: Authentication failed!");
      throw new AMFICOMRemoteException(Constants.ERROR_INSUFFICIENT_PRIVILEGES, "AMFICOMKIS.query | ERROR: Authentication failed!");
    }

		Test[] tests;
    try {
      tests = Test.retrieveAbortedTests(agentid.agent_id);
    }
    catch (SQLException e) {
			String mesg = "AMFICOMKIS | Exception while retrieving cancelled tests: " + e.getMessage();
			Log.errorMessage(mesg);
      Log.errorException(e);
      throw new AMFICOMRemoteException(Constants.ERROR_LOADING, mesg);
    }
		String[] ids = new String[tests.length];
		try {
			for (int i = 0; i < ids.length; i++) {
				tests[i].updateStatus(TestStatus._TEST_STATUS_ABORTED);
				ids[i] = tests[i].getId();
			}
		}
		catch (SQLException e) {
			String mesg = "AMFICOMKIS | Exception while updating statuses of cancelled tests: " + e.getMessage();
      Log.errorMessage(mesg);
      Log.errorException(e);
      throw new AMFICOMRemoteException(Constants.ERROR_LOADING, mesg);
    }
		return ids;
	}

  public int reportResult(AgentIdentity_Transferable agentid,
                    Result_Transferable rest)
                      throws AMFICOMRemoteException {
    if (!this.checkRigths(agentid)) {
      String mesg = "AMFICOMKIS.report | ERROR: Authentication failed!";
      Log.errorMessage(mesg);
      throw new AMFICOMRemoteException(Constants.ERROR_INSUFFICIENT_PRIVILEGES, mesg);
    }

		Test test;
		try {
			test = new Test(rest.test_id);
		}
		catch (Exception e) {
      String mesg = "AMFICOMKIS | Exception while retrieving test for id '" + rest.test_id + "': " + e.getMessage();
      Log.errorMessage(mesg);
      Log.errorException(e);
      throw new AMFICOMRemoteException(Constants.ERROR_LOADING, mesg);
    }

		Result result;
    try {
      result = new Result(rest);
      if (rest.result_type.equals(Result.RESULT_TYPE_TEST)) {
        if (!result.setMyTestStatusCompleted());
					test.setModified();
				test.setMyRequestStatusCompleted();
			}
			else
				if (rest.result_type.equals(Result.RESULT_TYPE_ANALYSIS)) {
					Analysis analysis = new Analysis(rest.action_id);
					analysis.setModified();
				}
				else
					if (rest.result_type.equals(Result.RESULT_TYPE_EVALUATION)) {
						Evaluation evaluation = new Evaluation(rest.action_id);
						evaluation.setModified();
					}
					else
						if (rest.result_type.equals(Result.RESULT_TYPE_MODELING)) {
							Modeling modeling = new Modeling(rest.action_id);
							modeling.setModified();
						}
						else
							Log.errorMessage("AMFICOMKIS | ERROR: Unknown type of result: '" + rest.result_type + "'");
    }
    catch (Exception e) {
      String mesg = "AMFICOMKIS | Exception while saving result: " + e.getMessage();
      Log.errorMessage(mesg);
      Log.errorException(e);
      throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
    }

		if (rest.result_type.equals(Result.RESULT_TYPE_EVALUATION)
										&& rest.alarmLevel.value() != AlarmLevel._ALARM_LEVEL_NONE) {
			try {
				String event_type;
				switch (rest.alarmLevel.value()) {
					case AlarmLevel._ALARM_LEVEL_SOFT:
						event_type = "testwarningevent";
						break;
					case AlarmLevel._ALARM_LEVEL_HARD:
						event_type = "testalarmevent";
						break;
					default:
						throw new Exception("AMFICOMKIS | ERROR: Unknown alarm level: " + rest.alarmLevel.value());
				}
				
				new Event(result.getId(),
					"On KIS",
					EventSource.getIdByObjectId(test.getKISId()),
					event_type);
			}
			catch (Exception e) {
				String mesg = "AMFICOMKIS.reportResult | ERROR: Cannot generate warnig/alarm event: " + e.getMessage();
			  Log.errorMessage(mesg);
				Log.errorException(e);
				throw new AMFICOMRemoteException(Constants.ERROR_SAVING, mesg);
			}
		}

    return Constants.ERROR_NO_ERROR;
  }

  public String[] getKISIdentities(AgentIdentity_Transferable agentid)
                                    throws AMFICOMRemoteException {
    if (!this.checkRigths(agentid)) {
      Log.errorMessage("AMFICOMKIS.getKISIdentities | ERROR: Authentication failed!");
      throw new AMFICOMRemoteException(Constants.ERROR_INSUFFICIENT_PRIVILEGES, "AMFICOMKIS.getKISIdentities | ERROR: Authentication failed!");
    }

    String[] kisids = AgentSettings.getKISIdentities(agentid.agent_id);
    if (kisids == null) {
			String mesg = "AMFICOMKIS.query | ERROR: Failed to load KIS identities";
      Log.errorMessage(mesg);
      throw new AMFICOMRemoteException(Constants.ERROR_LOADING, mesg);
    }
    return kisids;
  }

  private boolean checkRigths(AgentIdentity_Transferable agentid) {
  //Dodelat'!!!
//-----------
/*
org.omg.CORBA.ORB orb = this._orb();
System.out.println(orb.getClass().getName());
try {

String[] ir = orb.list_initial_services();
for (int i = 0; i < ir.length; i++)
	System.out.println(ir[i]);

	org.omg.CORBA.Object obj = orb.resolve_initial_references("URLNamingResolver");
}
catch (Exception e) {
	System.out.println("Cannot resolve ir: " + e.getMessage());
	e.printStackTrace();
	return false;
}
*/
//-----------
    return true;
  }
}
