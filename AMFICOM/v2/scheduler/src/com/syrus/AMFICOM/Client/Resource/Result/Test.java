package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.CORBA.General.TestTimeStampsPackage.PeriodicalTestParameters;
import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.Client.Survey.Result.UI.TestDisplayModel;

import java.io.IOException;
import java.io.Serializable;

import java.util.*;

public class Test extends ObjectResource implements Serializable {

	private static final long					serialVersionUID		= 01L;

	public transient static final String		typ						= "test";
	private transient Analysis					analysis;
	private String								analysisId				= "";
	private long								deleted					= 0;
	private String								description				= "";

	/**
	 * @deprecated moved to TimeStamp_dep
	 */
	private long								duration				= 3000;
	private transient ElementaryTestAlarm[]		elementaryTestAlarms	= new ElementaryTestAlarm[0];
	private String								evaluationId			= "";
	private transient Evaluation				evalution;

	private String								id						= "";
	private String								kisId					= "";
	private long								modified				= 0;
	private String								monitoredElementId		= "";
	private String								name					= "";
	private String								requestId				= "";
	private String[]							resultIds				= new String[0];
	private transient TestReturnType			returnType;

	/**
	 * @deprecated moved to TimeStamp_dep
	 */
	private long								start_time				= 0;
	private transient TestStatus				status;

	/**
	 * @deprecated moved to TimeStamp_dep
	 */
	private transient TestTemporalType			temporal_type;
	private String								testArgumentSetId		= "";
	private String								testSetupId				= "";
	private String								testTypeId				= "";
	private transient TestArgumentSet			testArgumentSet;
	private transient TestSetup					testSetup;

	/**
	 * @deprecated moved to TimeStamp_dep
	 */
	private transient TestTimeStamps			time_stamps;

	private transient TimeStamp_dep				timeStamp;

	private transient ClientTest_Transferable	transferable;
	private String								userId					= "";

	public Test(ClientTest_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Test(String id) {
		this.id = id;
		this.changed = true;
		time_stamps = new TestTimeStamps();
		time_stamps._default();
		elementaryTestAlarms = new ElementaryTestAlarm[0];
		transferable = new ClientTest_Transferable();
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel() {
		return new TestDisplayModel();
	}

	public static ObjectResourceSorter getDefaultSorter() {
		return new TestTimeSorter();
	}

	public static ObjectResourceFilter getFilter() {
		return new com.syrus.AMFICOM.Client.Schedule.Filter.TestFilter();
	}

	public static ObjectResourceSorter getSorter() {
		return new com.syrus.AMFICOM.Client.Schedule.Sorter.TestSorter();
	}

	/**
	 * @return Returns the analysis.
	 */
	public Analysis getAnalysis() {
		return analysis;
	}

	/**
	 * @return Returns the analysisId.
	 */
	public String getAnalysisId() {
		return analysisId;
	}

	public Class getChildClass(String key) {
		if (key.equals("elementaryresult")) return Result.class;
		return ObjectResource.class;
	}

	public Enumeration getChildren(String key) {
		Vector vec = new Vector();
		if (key.equals("elementaryresult")) {
			for (int i = 0; i < resultIds.length; i++)
				vec.add(Pool.get("result", resultIds[i]));
		}
		return vec.elements();
	}

	public Enumeration getChildTypes() {
		Vector vec = new Vector();
		vec.add("elementaryresult");
		return vec.elements();
	}

	/**
	 * @return Returns the deleted.
	 */
	public long getDeleted() {
		return deleted;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	public String getDomainId() {
		return ConstStorage.SYS_DOMAIN;
	}

	/**
	 * @return Returns the duration.
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @return Returns the elementaryTestAlarms.
	 */
	public ElementaryTestAlarm[] getElementaryTestAlarms() {
		return elementaryTestAlarms;
	}

	/**
	 * @return Returns the evaluationId.
	 */
	public String getEvaluationId() {
		return evaluationId;
	}

	/**
	 * @return Returns the evalution.
	 */
	public Evaluation getEvalution() {
		return evalution;
	}

	public String getId() {
		return id;
	}

	/**
	 * @return Returns the kisId.
	 */
	public String getKisId() {
		return kisId;
	}

	public ObjectResourceModel getModel() {
		return new TestModel(this);
	}

	public long getModified() {
		return modified;
	}

	/**
	 * @return Returns the monitoredElementId.
	 */
	public String getMonitoredElementId() {
		return monitoredElementId;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return Returns the requestId.
	 */
	public String getRequestId() {
		return requestId;
	}

	/**
	 * @return Returns the requestId.
	 */
	public String[] getResultIds() {
		return resultIds;
	}

	/**
	 * @return Returns the returnType.
	 */
	public TestReturnType getReturnType() {
		return returnType;
	}

	/**
	 * @return Returns the startTime.
	 */
	public long getStartTime() {
		return start_time;
	}

	/**
	 * @return Returns the status.
	 */
	public TestStatus getStatus() {
		return status;
	}

	/**
	 * @return Returns the temporalType.
	 */
	public TestTemporalType getTemporalType() {
		return temporal_type;
	}

	/**
	 * @return Returns the testArgumentSet.
	 */
	public TestArgumentSet getTestArgumentSet() {
		return testArgumentSet;
	}

	/**
	 * @return Returns the testAargumentSetId.
	 */
	public String getTestArgumentSetId() {
		return testArgumentSetId;
	}

	/**
	 * @return Returns the testSetup.
	 */
	public TestSetup getTestSetup() {
		return testSetup;
	}

	/**
	 * @return Returns the testSetupId.
	 */
	public String getTestSetupId() {
		return testSetupId;
	}

	/**
	 * @return Returns the testTypeId.
	 */
	public String getTestTypeId() {
		return testTypeId;
	}

	/**
	 * @return Returns the timeStamp.
	 */
	public TimeStamp_dep getTimeStamp() {
		return timeStamp;
	}

	/**
	 * @return Returns the timeStamps.
	 */
	public TestTimeStamps getTimeStamps() {
		return time_stamps;
	}

	public Object getTransferable() {
		return transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}

	public Test myclone() {
		Test new_test = new Test(this.id);
		new_test.testTypeId = this.testTypeId;
		new_test.start_time = this.start_time;
		new_test.deleted = this.deleted;
		new_test.kisId = this.kisId;
		new_test.analysisId = this.analysisId;
		new_test.evaluationId = this.evaluationId;
		new_test.name = this.name;
		new_test.modified = this.modified;
		new_test.requestId = this.requestId;
		new_test.monitoredElementId = this.monitoredElementId;
		new_test.temporal_type = this.temporal_type;
		new_test.returnType = this.returnType;
		new_test.status = this.status;
		new_test.time_stamps = this.time_stamps;
		//		new_test.arguments = this.arguments;
		new_test.testArgumentSetId = testArgumentSetId;
		new_test.elementaryTestAlarms = this.elementaryTestAlarms;
		new_test.resultIds = this.resultIds;
		new_test.description = this.description;
		new_test.transferable = this.transferable;
		new_test.userId = this.userId;
		new_test.testSetupId = this.testSetupId;
		new_test.duration = this.duration;

		return new_test;
	}

	/**
	 * @param analysis
	 *            The analysis to set.
	 */
	public void setAnalysis(Analysis analysis) {
		this.changed = true;
		this.analysis = analysis;
	}

	/**
	 * @param analysisId
	 *            The analysisId to set.
	 */
	public void setAnalysisId(String analysisId) {
		this.changed = true;
		this.analysisId = analysisId;
	}

	/**
	 * @param deleted
	 *            The deleted to set.
	 */
	public void setDeleted(long deleted) {
		this.changed = true;
		this.deleted = deleted;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.changed = true;
		this.description = description;
	}

	/**
	 * @param duration
	 *            The duration to set.
	 */
	public void setDuration(long duration) {
		this.changed = true;
		this.duration = duration;
	}

	/**
	 * @param elementaryTestAlarms
	 *            The elementaryTestAlarms to set.
	 */
	public void setElementaryTestAlarms(
			ElementaryTestAlarm[] elementaryTestAlarms) {
		this.changed = true;
		this.elementaryTestAlarms = elementaryTestAlarms;
	}

	/**
	 * @param evaluationId
	 *            The evaluationId to set.
	 */
	public void setEvaluationId(String evaluationId) {
		this.changed = true;
		this.evaluationId = evaluationId;
	}

	/**
	 * @param evalution
	 *            The evalution to set.
	 */
	public void setEvalution(Evaluation evalution) {
		this.changed = true;
		this.evalution = evalution;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.changed = true;
		this.id = id;
	}

	/**
	 * @param kisId
	 *            The kisId to set.
	 */
	public void setKisId(String kisId) {
		this.changed = true;
		this.kisId = kisId;
	}

	public void setLocalFromTransferable() {
		this.id = transferable.id;
		this.analysisId = transferable.analysis_id;
		this.evaluationId = transferable.evaluation_id;
		this.testTypeId = transferable.test_type_id;
		this.returnType = transferable.return_type;
		this.start_time = transferable.start_time;
		this.deleted = transferable.deleted;
		this.kisId = transferable.kis_id;
		this.name = transferable.name;
		this.modified = transferable.modified;
		this.requestId = transferable.request_id;
		this.monitoredElementId = transferable.monitored_element_id;
		this.temporal_type = transferable.temporal_type;
		this.time_stamps = transferable.time_stamps;
		this.status = transferable.status;

		resultIds = transferable.result_ids;
		this.elementaryTestAlarms = transferable.elementary_test_alarms;

		this.testArgumentSetId = transferable.test_argument_set_id;
		this.testSetupId = transferable.test_setup_id;
		this.duration = transferable.duration;
		this.changed = false;
		//System.out.println("testID:" + id + " setLocalFromTransferable()");
		//		arguments = new Vector();
		//		for (int i=0; i<transferable.arguments.length; i++)
		//			arguments.add(new Parameter(transferable.arguments[i]));

	}

	/**
	 * @param modified
	 *            The modified to set.
	 */
	public void setModified(long modified) {
		this.changed = true;
		this.modified = modified;
	}

	/**
	 * @param monitoredElementId
	 *            The monitoredElementId to set.
	 */
	public void setMonitoredElementId(String monitoredElementId) {
		this.changed = true;
		this.monitoredElementId = monitoredElementId;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.changed = true;
		this.name = name;
	}

	/**
	 * @param requestId
	 *            The requestId to set.
	 */
	public void setRequestId(String requestId) {
		this.changed = true;
		this.requestId = requestId;
	}

	/**
	 * @param resultIds
	 *            The resultIds to set.
	 */
	public void setResultIds(String[] resultIds) {
		this.changed = true;
		this.resultIds = resultIds;
	}

	/**
	 * @param returnType
	 *            The returnType to set.
	 */
	public void setReturnType(TestReturnType returnType) {
		this.changed = true;
		this.returnType = returnType;
	}

	/**
	 * @param startTime
	 *            The startTime to set.
	 */
	public void setStartTime(long startTime) {
		this.changed = true;
		this.start_time = startTime;
	}

	/**
	 * @param status
	 *            The status to set.
	 */
	public void setStatus(TestStatus status) {
		this.changed = true;
		this.status = status;
	}

	/**
	 * @param temporalType
	 *            The temporalType to set.
	 */
	public void setTemporalType(TestTemporalType temporalType) {
		this.changed = true;
		this.temporal_type = temporalType;
	}

	/**
	 * @param testArgumentSet
	 *            The testArgumentSet to set.
	 */
	public void setTestArgumentSet(TestArgumentSet testArgumentSet) {
		this.changed = true;
		this.testArgumentSet = testArgumentSet;
	}

	/**
	 * @param testAargumentSetId
	 *            The testAargumentSetId to set.
	 */
	public void setTestArgumentSetId(String testAargumentSetId) {
		this.changed = true;
		this.testArgumentSetId = testAargumentSetId;
	}

	/**
	 * @param testSetup
	 *            The testSetup to set.
	 */
	public void setTestSetup(TestSetup testSetup) {
		this.changed = true;
		this.testSetup = testSetup;
	}

	/**
	 * @param testSetupId
	 *            The testSetupId to set.
	 */
	public void setTestSetupId(String testSetupId) {
		this.changed = true;
		this.testSetupId = testSetupId;
	}

	/**
	 * @param testTypeId
	 *            The testTypeId to set.
	 */
	public void setTestTypeId(String testTypeId) {
		this.changed = true;
		this.testTypeId = testTypeId;
	}

	/**
	 * @param timeStamp
	 *            The timeStamp to set.
	 */
	public void setTimeStamp(TimeStamp_dep timeStamp) {
		this.changed = true;
		this.timeStamp = timeStamp;

		if (this.timeStamp.getType() == TimeStamp_dep.TIMESTAMPTYPE_ONETIME) {
			this.temporal_type = TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME;
			this.start_time = this.timeStamp.getPeriodStart();
			//			tempTest.duration = receiveTimeStamp.getPeriodEnd() -
			// receiveTimeStamp.getPeriodStart();
			this.duration = 0;
			timeStamp.setPeriodEnd(timeStamp.getPeriodStart());

		} else if (this.timeStamp.getType() == TimeStamp_dep.TIMESTAMPTYPE_PERIODIC) {
			this.temporal_type = TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL;
			this.start_time = this.timeStamp.getPeriodStart();
			/**
			 * @TODO periodical time
			 */
			Time period = this.timeStamp.getPeriod();
			long interval = 0;
			int scale = period.getScale();
			int value = period.getValue();
			switch (scale) {
				case Calendar.MINUTE:
					interval = 1000 * 60 * value;
					break;
				case Calendar.HOUR:
					interval = 1000 * 60 * 60 * value;
					break;

			/**
			 * @todo other period scales
			 */

			}
			PeriodicalTestParameters ptp = new PeriodicalTestParameters();
			ptp.dt = interval;
			ptp.end_time = this.timeStamp.getPeriodEnd();
			this.time_stamps.ptpars(ptp);
		}

	}

	/**
	 * @param timeStamps
	 *            The timeStamps to set.
	 */
	public void setTimeStamps(TestTimeStamps timeStamps) {
		this.changed = true;
		this.time_stamps = timeStamps;
	}

	/**
	 * @param transferable
	 *            The transferable to set.
	 */
	public void setTransferable(ClientTest_Transferable transferable) {
		this.changed = true;
		this.transferable = transferable;
	}

	public void setTransferableFromLocal() {
		//System.out.println("setTransferableFromLocal");
		transferable.id = this.id;
		transferable.analysis_id = this.analysisId;
		transferable.evaluation_id = this.evaluationId;
		transferable.test_type_id = this.testTypeId;
		transferable.start_time = this.start_time;
		transferable.deleted = 0;
		transferable.return_type = this.returnType;
		transferable.kis_id = this.kisId;
		transferable.name = this.name;
		transferable.modified = this.modified;
		transferable.request_id = this.requestId;
		transferable.monitored_element_id = this.monitoredElementId;
		transferable.temporal_type = this.temporal_type;
		transferable.status = TestStatus.TEST_STATUS_SCHEDULED;
		transferable.time_stamps = this.time_stamps;
		//		transferable.arguments = new
		// ClientParameter_Transferable[this.arguments.size()];
		if ((testArgumentSetId == null) || testArgumentSetId.length() == 0) {
			if ((testSetup == null) && (testSetupId != null)
					&& (testSetupId.length() > 0))
					testSetup = (TestSetup) Pool
							.get(TestSetup.typ, testSetupId);
			if (testSetup != null)
					testArgumentSetId = testSetup.getTestArgumentSetId();
		}
		transferable.test_argument_set_id = testArgumentSetId;

		transferable.elementary_test_alarms = new ElementaryTestAlarm[0];
		transferable.result_ids = new String[0];
		transferable.test_setup_id = this.testSetupId;
		transferable.duration = this.duration;
		this.changed = false;
		/*
		 * for (int i=0; i <transferable.arguments.length; i++) { Parameter
		 * argument = (Parameter)arguments.get(i);
		 * argument.setTransferableFromLocal(); transferable.arguments[i] =
		 * (ClientParameter_Transferable)argument.getTransferable(); }
		 */
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(String userId) {
		this.changed = true;
		this.userId = userId;
	}

	public void updateLocalFromTransferable() {
		this.analysis = (Analysis) Pool.get(Analysis.typ, this.analysisId);
		this.evalution = (Evaluation) Pool.get(Evaluation.typ, evaluationId);

		if (timeStamp == null) timeStamp = new TimeStamp_dep();
		timeStamp.setPeriodStart(this.start_time);
		timeStamp.setPeriodEnd(this.start_time + this.duration);

		/**
		 * todo: this is ONLY for backward compatibility
		 */
		{

			if (this.temporal_type
					.equals(TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME)) {
				timeStamp.setType(TimeStamp_dep.TIMESTAMPTYPE_ONETIME);
			} else if (this.temporal_type
					.equals(TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL)) {
				timeStamp.setType(TimeStamp_dep.TIMESTAMPTYPE_PERIODIC);

				long start = this.start_time;
				long end = this.time_stamps.ptpars().end_time;
				long interval = this.time_stamps.ptpars().dt;
				long min = interval / (60 * 1000);
				long hour = interval / (60 * 60 * 1000);
				if ((min > 0) && (min < 60)) {
					timeStamp.setPeriodStart(start);
					timeStamp.setPeriodEnd(end);
					timeStamp.setPeriod(Calendar.MINUTE, (int) min);
					timeStamp.addTestDate(Calendar.MINUTE, 0);
					timeStamp.addTestTime(0, 0, 0);
				}
				if ((hour > 0) && (hour < 24)) {
					timeStamp.setPeriodStart(start);
					timeStamp.setPeriodEnd(end);
					timeStamp.setPeriod(Calendar.HOUR, (int) hour);
					timeStamp.addTestDate(Calendar.HOUR, 0);
					timeStamp.addTestTime(0, 0, 0);
				}
				//ElementaryTest et = new ElementaryTest(this, start);
				//et.count = iii;
				//iii++;
				//vec.addElement(et);
				//				while (start + temp_time <= end) {
				//					et = new ElementaryTest(this, start + temp_time);
				//					et.count = iii;
				//					iii++;
				//					//vec.addElement(et);
				//					temp_time += interval;
				//				}
			} else if (this.temporal_type
					.equals(TestTemporalType.TEST_TEMPORAL_TYPE_TIMETABLE)) {
				//				long start = this.start_time;
				//				long time_massiv[] = this.time_stamps.ti();
				//				int iii = 1;
				//				ElementaryTest et = new ElementaryTest(this, start);
				//				et.count = iii;
				//				iii++;
				//				//vec.addElement(et);
				//				for (int j = 0; j < time_massiv.length; j++) {
				//					et = new ElementaryTest(this, time_massiv[j]);
				//					et.count = iii;
				//					iii++;
				//					//vec.addElement(et);
				//				}
			}

		}

	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		id = (String) in.readObject();
		testTypeId = (String) in.readObject();
		int ttt = in.readInt();
		temporal_type = TestTemporalType.from_int(ttt);
		//		temporal_type = (TestTemporalType )in.readObject();
		int trt = in.readInt();
		returnType = TestReturnType.from_int(trt);
		//		returnType = (TestReturnType )in.readObject();
		start_time = in.readLong();

		//		time_stamps = (TestTimeStamps )in.readObject();
		time_stamps = new TestTimeStamps();
		if (temporal_type.value() == TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL) {
			PeriodicalTestParameters ptp = new PeriodicalTestParameters();
			ptp.dt = in.readLong();
			ptp.end_time = in.readLong();
			time_stamps.ptpars(ptp);
		}
		if (temporal_type.value() == TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE) {
			long[] l = (long[]) in.readObject();
			time_stamps.ti(l);
		}

		kisId = (String) in.readObject();
		name = (String) in.readObject();
		analysisId = (String) in.readObject();
		evaluationId = (String) in.readObject();
		modified = in.readLong();
		requestId = (String) in.readObject();
		userId = (String) in.readObject();
		deleted = in.readLong();
		description = (String) in.readObject();
		duration = in.readLong();
		testSetupId = (String) in.readObject();
		monitoredElementId = (String) in.readObject();
		int ts = in.readInt();
		status = TestStatus.from_int(ts);
		//		status = (TestStatus )in.readObject();
		Object obj = in.readObject();
		resultIds = (String[]) obj;
		//		Object obj2 = in.readObject();

		//		elementaryTestAlarms = (ElementaryTestAlarm[] )obj2;
		int len = in.readInt();
		elementaryTestAlarms = new ElementaryTestAlarm[len];
		for (int i = 0; i < len; i++) {
			elementaryTestAlarms[i] = new ElementaryTestAlarm();
			elementaryTestAlarms[i].elementary_start_time = in.readLong();
			elementaryTestAlarms[i].alarm_id = (String) in.readObject();
			elementaryTestAlarms[i].alarm_name = (String) in.readObject();
		}

		testArgumentSetId = (String) in.readObject();

		transferable = new ClientTest_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(id);
		out.writeObject(testTypeId);

		out.writeInt(temporal_type.value());

		out.writeInt(returnType.value());

		out.writeLong(start_time);
		//		out.writeObject(time_stamps);

		if (temporal_type.value() == TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL) {
			PeriodicalTestParameters ptp = time_stamps.ptpars();
			out.writeLong(ptp.dt);
			out.writeLong(ptp.end_time);
		}
		if (temporal_type.value() == TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE)
				out.writeObject(time_stamps.ti());

		out.writeObject(kisId);
		out.writeObject(name);
		out.writeObject(analysisId);
		out.writeObject(evaluationId);
		out.writeLong(modified);
		out.writeObject(requestId);
		out.writeObject(userId);
		out.writeLong(deleted);
		out.writeObject(description);
		out.writeLong(duration);
		out.writeObject(testSetupId);
		out.writeObject(monitoredElementId);
		out.writeInt(status.value());
		Object obj = resultIds;
		out.writeObject(obj);

		//		Object obj2 = elementaryTestAlarms;
		//		out.writeObject(obj2);
		out.writeInt(elementaryTestAlarms.length);
		for (int i = 0; i < elementaryTestAlarms.length; i++) {
			out.writeLong(elementaryTestAlarms[i].elementary_start_time);
			out.writeObject(elementaryTestAlarms[i].alarm_id);
			out.writeObject(elementaryTestAlarms[i].alarm_name);
		}

		out.writeObject(testArgumentSetId);
	}
}

class TestTimeSorter extends ObjectResourceSorter {

	String[][]	sorted_columns	= new String[][] { { "time", "long"}};

	public long getLong(ObjectResource or, String column) {
		Test test = (Test) or;
		return test.getStartTime();
	}

	public String[][] getSortedColumns() {
		return sorted_columns;
	}

	public String getString(ObjectResource or, String column) {
		return "";
	}
}