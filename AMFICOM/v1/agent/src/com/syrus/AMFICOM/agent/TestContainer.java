package com.syrus.AMFICOM.agent;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.io.IOException;
import java.sql.Timestamp;
import com.syrus.AMFICOM.CORBA.KIS.Test_Transferable;
import com.syrus.AMFICOM.CORBA.KIS.Result_Transferable;
import com.syrus.AMFICOM.CORBA.General.TestTemporalType;
import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.CORBA.General.TestTimeStamps;
import com.syrus.AMFICOM.CORBA.KIS.AgentIdentity_Transferable;
import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.CORBA.General.AlarmLevel;
import com.syrus.AMFICOM.agent.AnalysisManager;
import com.syrus.AMFICOM.agent.EvaluationManager;
import com.syrus.util.Log;

//sortArguments
import com.syrus.AMFICOM.CORBA.KIS.Parameter_Transferable;

public class TestContainer extends Agent {
  private Test_Transferable tt;
  private final String taskFileName;
  private boolean taskFileConnected;
  private int taskFileHandle;
  private List resultQueue;
	private boolean aborted;

  public TestContainer(Test_Transferable tt, String kis_id) {
    this.tt = tt;
    this.taskFileName = /*"\\\\.\\pipe\\" + */"taskChannel" + kis_id;
    this.taskFileHandle = 0;
    this.taskFileConnected = false;
		this.resultQueue = Collections.synchronizedList(new LinkedList());
  }

  public void run() {
    switch (this.tt.temporal_type.value()) {
      case TestTemporalType._TEST_TEMPORAL_TYPE_ONETIME:
        this.processOnetime();      
      break;
      case TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL:
        this.processPeriodical();
      break;
      case TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE:
        this.processTimetable();
      break;
    }
//    testContainerTable.remove(this.tt.id);
  }

  private void processOnetime() {
    LinkedList testList = new LinkedList();
    testList.add(this.tt);
    this.processElementaryTestList(testList);
  }
  
  private void processPeriodical() {
    long dt = this.tt.time_stamps.ptpars().dt;
    long end_time = this.tt.time_stamps.ptpars().end_time;
    LinkedList testList = new LinkedList();
    for(int i = 0; i <= (int)((end_time - this.tt.start_time)/dt); i++) {
      Log.debugMessage("processPeriodical() | Adding elementary whith start_time: " + (new Timestamp(this.tt.start_time + i*dt)).toString(), Log.DEBUGLEVEL05);
      testList.add(new Test_Transferable(this.tt.id, this.tt.test_type_id, this.tt.test_request_id, TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME, this.tt.start_time + i*dt, new TestTimeStamps(), TestStatus.TEST_STATUS_PROCESSING, this.tt.kis_id, this.tt.monitored_element_id, this.tt.local_address, this.tt.parameters, this.tt.analysis, this.tt.evaluation, this.tt.return_type));
    }
    this.processElementaryTestList(testList);
  }

  private void processTimetable() {
    LinkedList testList = new LinkedList();
    testList.add(new Test_Transferable(this.tt.id, this.tt.test_type_id, this.tt.test_request_id, TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME, this.tt.start_time, new TestTimeStamps(), TestStatus.TEST_STATUS_PROCESSING, this.tt.kis_id, this.tt.monitored_element_id, this.tt.local_address, this.tt.parameters, this.tt.analysis, this.tt.evaluation, this.tt.return_type));
    long[] ti = this.tt.time_stamps.ti();
    for(int i = 0; i < ti.length; i++) 
      testList.add(new Test_Transferable(this.tt.id, this.tt.test_type_id, this.tt.test_request_id, TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME, ti[i], new TestTimeStamps(), TestStatus.TEST_STATUS_PROCESSING, this.tt.kis_id, this.tt.monitored_element_id, this.tt.local_address, this.tt.parameters, this.tt.analysis, this.tt.evaluation, this.tt.return_type));
    this.processElementaryTestList(testList);
  }

  private void processElementaryTestList(LinkedList testList) {
    Test_Transferable test_i;
		String measurement_id;
		Result_Transferable result;
    int n_tests = testList.size();
		int n_reports = 0;
    long deadtime = ((Test_Transferable)testList.get(n_tests-1)).start_time + measuretime;
		this.aborted = false;
    while(n_reports < n_tests && System.currentTimeMillis() <= deadtime && !this.aborted) {
			Log.debugMessage("----------- Elementary tests : " + n_tests + ", reports: " + n_reports + " ---------", Log.DEBUGLEVEL05);
			if (!testList.isEmpty()) {
			  test_i = (Test_Transferable)testList.get(0);
		    if (test_i.start_time <= System.currentTimeMillis()) {
					Log.debugMessage("Pushung", Log.DEBUGLEVEL05);

//---------******* DON't FORGET REMOVE IT NAHREN!!! *******---------
sortArguments(test_i);
//------------------------------------------------------------------

	        if (!this.taskFileConnected) {
					  this.taskFileHandle = Transceiver.call(this.taskFileName);
				    if(this.taskFileHandle != 0) {
			        Log.debugMessage("TestContainer.processElementaryTestList | Task channel connected", Log.DEBUGLEVEL05);
		          this.taskFileConnected = true;
	          }
						else {
					    Log.debugMessage("TestContainer.processElementaryTestList | Task channel NOT connected", Log.DEBUGLEVEL05);
				      this.taskFileConnected = false;
			        this.taskFileHandle = 0;
		        }
	        }
					else {
				    String[] par_names = new String[test_i.parameters.length];
			      byte[][] par_values = new byte[test_i.parameters.length][];
		        for (int i = 0; i < par_names.length; i++) {
	            par_names[i] = test_i.parameters[i].name;
							par_values[i] = test_i.parameters[i].value;
						}
						measurement_id = test_i.id + "*" + Long.toString(test_i.start_time);
					  if (Transceiver.push1(this.taskFileHandle, this.taskFileName,
																	measurement_id,
																	test_i.test_type_id,
																	test_i.local_address,
																	par_names,
																	par_values)) {
				      Log.debugMessage("TestContainer.processElementaryTestList | Task is written", Log.DEBUGLEVEL03);
			        testList.remove(test_i);
		        }
	          else {
						  Log.debugMessage("TestContainer.processElementaryTestList | Task is NOT written", Log.DEBUGLEVEL03);
					    this.taskFileConnected = false;
				      this.taskFileHandle = 0;
			      }
		      }
	      }
			}//!testList.isEmpty()

			synchronized (this.resultQueue) {
				if (!this.resultQueue.isEmpty()) {
					result = (Result_Transferable)this.resultQueue.remove(0);
//regard return_type here -- do later !!!
					synchronized (reportQueue) {
						reportQueue.add(result);
					}
					n_reports ++;
//----------------------------------------------------
//Process analysis and/or evaluation
				  Result_Transferable analysisResult = null;
				  if (this.tt.analysis.id != null && this.tt.evaluation.id != null) {
					  AnalysisManager analysisManager = AnalysisManager.getAnalysisManager(this.tt.analysis.analysis_type_id);
						if (analysisManager != null) {
							analysisResult = analysisManager.analyse(this.tt.analysis, result, this.tt.evaluation.etalon);
							synchronized (reportQueue) {
								reportQueue.add(analysisResult);
							}
						}
					  else
						  Log.errorMessage("analysisManager == null");
			    }
		      if (this.tt.evaluation.id != null) {
						EvaluationManager evaluationManager = EvaluationManager.getEvaluationManager(this.tt.evaluation.evaluation_type_id);
					  if (evaluationManager != null) {
							try {
								Result_Transferable evaluationResult = evaluationManager.evaluate(tt.evaluation, analysisResult, result);
								synchronized (reportQueue) {
									reportQueue.add(evaluationResult);
								}
							}
							catch (OutOfMemoryError om) {
								System.out.println(om.getMessage());
								om.printStackTrace();
							}
							catch (Exception e) {
								Log.errorMessage("Exception while evaluating test '" + this.tt.id + "'");
								Log.errorException(e);
								continue;
							}
				    }
			      else
			        Log.errorMessage("evaluationManager == null");
					}
//----------------------------------------------------
				}//if (!this.resultQueue.isEmpty())
			}//synchronized (this.resultQueue)

      try {
        sleep(kistimewait);
      }
      catch (InterruptedException ex) {}
    }//while
		synchronized (testContainers) {
			testContainers.remove(this.tt.id);
		}
    Log.debugMessage("+ Exiting: testList size == " + testList.size() + ", resultQueue size == " + this.resultQueue.size() + ", reports: " + n_reports + " +", Log.DEBUGLEVEL03);
  }

	public void addResult(Result_Transferable result) {
		synchronized (this.resultQueue) {
			this.resultQueue.add(result);
		}
	}

	public String getMeasurementTypeId() {
		return this.tt.test_type_id;
	}

	public void abort() {
		this.taskFileConnected = false;
    this.taskFileHandle = 0;
		synchronized (this.resultQueue) {
			this.resultQueue.clear();
		}
		synchronized (testContainers) {
			testContainers.remove(this.tt.id);
		}
		this.aborted = true;
	}

	private static void sortArguments(Test_Transferable test_i) {
		if (test_i.test_type_id.equals("trace_and_analyse")) {
			Parameter_Transferable[] pars = new Parameter_Transferable[test_i.parameters.length];
			for (int i = 0; i < test_i.parameters.length; i++)
				if (test_i.parameters[i].name.equals("ref_wvlen"))
					pars[0] = test_i.parameters[i];
			for (int i = 0; i < test_i.parameters.length; i++)
				if (test_i.parameters[i].name.equals("ref_trclen"))
					pars[1] = test_i.parameters[i];
			for (int i = 0; i < test_i.parameters.length; i++)
				if (test_i.parameters[i].name.equals("ref_res"))
					pars[2] = test_i.parameters[i];
			for (int i = 0; i < test_i.parameters.length; i++)
				if (test_i.parameters[i].name.equals("ref_pulswd"))
					pars[3] = test_i.parameters[i];
			for (int i = 0; i < test_i.parameters.length; i++)
				if (test_i.parameters[i].name.equals("ref_ior"))
					pars[4] = test_i.parameters[i];
			for (int i = 0; i < test_i.parameters.length; i++)
				if (test_i.parameters[i].name.equals("ref_scans"))
					pars[5] = test_i.parameters[i];
			test_i.parameters = pars;
		}
	}
}