package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.CORBA.General.TestTimeStampsPackage.PeriodicalTestParameters;
import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.General.Filter.ObjectResourceFilter;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;
import com.syrus.AMFICOM.Client.Survey.Result.UI.TestDisplayModel;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.measurement.TemporalPattern;

import java.io.IOException;
import java.io.Serializable;

import java.util.*;

public class Test extends ObjectResource implements Serializable {

	public transient static final String		TYPE					= "test";

	private static final long					serialVersionUID		= 01L;
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

	private TemporalPattern						temporalPattern;
	private transient TestArgumentSet			testArgumentSet;
	private String								testArgumentSetId		= "";
	private transient TestSetup					testSetup;
	private String								testSetupId				= "";
	private String								testTypeId				= "";

	/**
	 * @deprecated moved to TimeStamp_dep
	 */
	private transient TestTimeStamps			time_stamps;

	private transient ClientTest_Transferable	transferable;
	private String								userId					= "";

	public Test(ClientTest_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public Test(String id) {
		this.id = id;
		this.changed = true;
		this.time_stamps = new TestTimeStamps();
		this.time_stamps._default();
		this.elementaryTestAlarms = new ElementaryTestAlarm[0];
		this.transferable = new ClientTest_Transferable();
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
		return this.analysis;
	}

	/**
	 * @return Returns the analysisId.
	 */
	public String getAnalysisId() {
		return this.analysisId;
	}

	/**
	 * @deprecated
	 */

	public Class getChildClass(String key) {
		if (key.equals("elementaryresult")) return Result.class;
		return ObjectResource.class;
	}

	/**
	 * @deprecated
	 */

	public Enumeration getChildren(String key) {
		Vector vec = new Vector();
		if (key.equals("elementaryresult")) {
			for (int i = 0; i < resultIds.length; i++)
				vec.add(Pool.get("result", resultIds[i]));
		}
		return vec.elements();
	}

	/**
	 * @deprecated
	 */
	public Enumeration getChildTypes() {
		Vector vec = new Vector();
		vec.add("elementaryresult");
		return vec.elements();
	}

	/**
	 * @return Returns the deleted.
	 */
	public long getDeleted() {
		return this.deleted;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return this.description;
	}

	public String getDomainId() {
		return ConstStorage.SYS_DOMAIN;
	}

	/**
	 * @return Returns the duration.
	 */
	public long getDuration() {
		return this.duration;
	}

	/**
	 * @return Returns the elementaryTestAlarms.
	 */
	public ElementaryTestAlarm[] getElementaryTestAlarms() {
		return this.elementaryTestAlarms;
	}

	/**
	 * @return Returns the evaluationId.
	 */
	public String getEvaluationId() {
		return this.evaluationId;
	}

	/**
	 * @return Returns the evalution.
	 */
	public Evaluation getEvalution() {
		return this.evalution;
	}

	public String getId() {
		return this.id;
	}

	/**
	 * @return Returns the kisId.
	 */
	public String getKisId() {
		return this.kisId;
	}

	public ObjectResourceModel getModel() {
		return new TestModel(this);
	}

	public long getModified() {
		return this.modified;
	}

	/**
	 * @return Returns the monitoredElementId.
	 */
	public String getMonitoredElementId() {
		return this.monitoredElementId;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @return Returns the requestId.
	 */
	public String getRequestId() {
		return this.requestId;
	}

	/**
	 * @return Returns the requestId.
	 */
	public String[] getResultIds() {
		return this.resultIds;
	}

	/**
	 * @return Returns the returnType.
	 */
	public TestReturnType getReturnType() {
		return this.returnType;
	}

	/**
	 * @return Returns the startTime.
	 */
	public long getStartTime() {
		return this.start_time;
	}

	/**
	 * @return Returns the status.
	 */
	public TestStatus getStatus() {
		return this.status;
	}

	/**
	 * @return Returns the temporalPattern.
	 */
	public TemporalPattern getTemporalPattern() {
		return this.temporalPattern;
	}

	/**
	 * @return Returns the temporalType.
	 */
	public TestTemporalType getTemporalType() {
		return this.temporal_type;
	}

	/**
	 * @return Returns the testArgumentSet.
	 */
	public TestArgumentSet getTestArgumentSet() {
		return this.testArgumentSet;
	}

	/**
	 * @return Returns the testAargumentSetId.
	 */
	public String getTestArgumentSetId() {
		return this.testArgumentSetId;
	}

	/**
	 * @return Returns the testSetup.
	 */
	public TestSetup getTestSetup() {
		return this.testSetup;
	}

	/**
	 * @return Returns the testSetupId.
	 */
	public String getTestSetupId() {
		return this.testSetupId;
	}

	/**
	 * @return Returns the testTypeId.
	 */
	public String getTestTypeId() {
		return this.testTypeId;
	}

	/**
	 * @return Returns the timeStamps.
	 */
	public TestTimeStamps getTimeStamps() {
		return this.time_stamps;
	}

	public Object getTransferable() {
		return this.transferable;
	}

	public String getTyp() {
		return TYPE;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return this.userId;
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
		new_test.testArgumentSetId = this.testArgumentSetId;
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
		this.id = this.transferable.id;
		this.analysisId = this.transferable.analysis_id;
		this.evaluationId = this.transferable.evaluation_id;
		this.testTypeId = this.transferable.test_type_id;
		this.returnType = this.transferable.return_type;
		this.start_time = this.transferable.start_time;
		this.deleted = this.transferable.deleted;
		this.kisId = this.transferable.kis_id;
		this.name = this.transferable.name;
		this.modified = this.transferable.modified;
		this.requestId = this.transferable.request_id;
		this.monitoredElementId = this.transferable.monitored_element_id;
		this.temporal_type = this.transferable.temporal_type;
		this.time_stamps = this.transferable.time_stamps;
		this.status = this.transferable.status;

		this.resultIds = this.transferable.result_ids;
		this.elementaryTestAlarms = this.transferable.elementary_test_alarms;

		this.testArgumentSetId = this.transferable.test_argument_set_id;
		this.testSetupId = this.transferable.test_setup_id;
		this.duration = this.transferable.duration;
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
	 * @param temporalPattern
	 *            The temporalPattern to set.
	 */
	public void setTemporalPattern(TemporalPattern temporalPattern) {
		this.temporalPattern = temporalPattern;
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
		this.transferable.id = this.id;
		this.transferable.analysis_id = this.analysisId;
		this.transferable.evaluation_id = this.evaluationId;
		this.transferable.test_type_id = this.testTypeId;
		this.transferable.start_time = this.start_time;
		this.transferable.deleted = 0;
		this.transferable.return_type = this.returnType;
		this.transferable.kis_id = this.kisId;
		this.transferable.name = this.name;
		this.transferable.modified = this.modified;
		this.transferable.request_id = this.requestId;
		this.transferable.monitored_element_id = this.monitoredElementId;
		this.transferable.temporal_type = this.temporal_type;
		this.transferable.status = TestStatus.TEST_STATUS_SCHEDULED;
		this.transferable.time_stamps = this.time_stamps;
		//		transferable.arguments = new
		// ClientParameter_Transferable[this.arguments.size()];
		if ((this.testArgumentSetId == null)
				|| this.testArgumentSetId.length() == 0) {
			if ((this.testSetup == null) && (this.testSetupId != null)
					&& (this.testSetupId.length() > 0))
					this.testSetup = (TestSetup) Pool.get(TestSetup.typ,
							this.testSetupId);
			if (this.testSetup != null)
					this.testArgumentSetId = this.testSetup
							.getTestArgumentSetId();
		}
		this.transferable.test_argument_set_id = this.testArgumentSetId;

		this.transferable.elementary_test_alarms = new ElementaryTestAlarm[0];
		this.transferable.result_ids = new String[0];
		this.transferable.test_setup_id = this.testSetupId;
		this.transferable.duration = this.duration;
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
		this.evalution = (Evaluation) Pool.get(Evaluation.typ,
				this.evaluationId);

		/**
		 * @todo fix new Identifier
		 */
		if (this.temporalPattern == null)
				this.temporalPattern = new TemporalPattern(new Identifier(
						TemporalPattern.TYPE));
		this.temporalPattern.setStartPeriod(this.start_time);
		this.temporalPattern.setEndPeriod(this.start_time + this.duration);

		/**
		 * todo: this is ONLY for backward compatibility
		 */
		{

			if (this.temporal_type
					.equals(TestTemporalType.TEST_TEMPORAL_TYPE_ONETIME)) {
				this.temporalPattern
						.setType(TemporalPattern.TIMESTAMPTYPE_ONETIME);
			} else if (this.temporal_type
					.equals(TestTemporalType.TEST_TEMPORAL_TYPE_PERIODICAL)) {
				this.temporalPattern
						.setType(TemporalPattern.TIMESTAMPTYPE_PERIODIC);

				//				long start = this.start_time;
				//				long end = this.time_stamps.ptpars().end_time;
				//				long interval = this.time_stamps.ptpars().dt;
				//				long min = interval / (60 * 1000);
				//				long hour = interval / (60 * 60 * 1000);
				//				if ((min > 0) && (min < 60)) {
				//					timeStamp.setPeriodStart(start);
				//					timeStamp.setPeriodEnd(end);
				//					timeStamp.setPeriod(Calendar.MINUTE, (int) min);
				//					timeStamp.addTestDate(Calendar.MINUTE, 0);
				//					timeStamp.addTestTime(0, 0, 0);
				//				}
				//				if ((hour > 0) && (hour < 24)) {
				//					timeStamp.setPeriodStart(start);
				//					timeStamp.setPeriodEnd(end);
				//					timeStamp.setPeriod(Calendar.HOUR, (int) hour);
				//					timeStamp.addTestDate(Calendar.HOUR, 0);
				//					timeStamp.addTestTime(0, 0, 0);
				//				}

			} else if (this.temporal_type
					.equals(TestTemporalType.TEST_TEMPORAL_TYPE_TIMETABLE)) {
				/**
				 * @todo is there will be TIMETABLE ???
				 */
			}

		}

	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.testTypeId = (String) in.readObject();
		int ttt = in.readInt();
		this.temporal_type = TestTemporalType.from_int(ttt);
		//		temporal_type = (TestTemporalType )in.readObject();
		int trt = in.readInt();
		this.returnType = TestReturnType.from_int(trt);
		//		returnType = (TestReturnType )in.readObject();
		this.start_time = in.readLong();

		//		time_stamps = (TestTimeStamps )in.readObject();
		this.time_stamps = new TestTimeStamps();
		if (this.temporal_type.value() == TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL) {
			PeriodicalTestParameters ptp = new PeriodicalTestParameters();
			ptp.dt = in.readLong();
			ptp.end_time = in.readLong();
			this.time_stamps.ptpars(ptp);
		}
		if (this.temporal_type.value() == TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE) {
			long[] l = (long[]) in.readObject();
			this.time_stamps.ti(l);
		}

		this.kisId = (String) in.readObject();
		this.name = (String) in.readObject();
		this.analysisId = (String) in.readObject();
		this.evaluationId = (String) in.readObject();
		this.modified = in.readLong();
		this.requestId = (String) in.readObject();
		this.userId = (String) in.readObject();
		this.deleted = in.readLong();
		this.description = (String) in.readObject();
		this.duration = in.readLong();
		this.testSetupId = (String) in.readObject();
		this.monitoredElementId = (String) in.readObject();
		int ts = in.readInt();
		this.status = TestStatus.from_int(ts);
		//		status = (TestStatus )in.readObject();
		Object obj = in.readObject();
		this.resultIds = (String[]) obj;
		//		Object obj2 = in.readObject();

		//		elementaryTestAlarms = (ElementaryTestAlarm[] )obj2;
		int len = in.readInt();
		this.elementaryTestAlarms = new ElementaryTestAlarm[len];
		for (int i = 0; i < len; i++) {
			this.elementaryTestAlarms[i] = new ElementaryTestAlarm();
			this.elementaryTestAlarms[i].elementary_start_time = in.readLong();
			this.elementaryTestAlarms[i].alarm_id = (String) in.readObject();
			this.elementaryTestAlarms[i].alarm_name = (String) in.readObject();
		}

		this.testArgumentSetId = (String) in.readObject();

		this.transferable = new ClientTest_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.testTypeId);

		out.writeInt(this.temporal_type.value());

		out.writeInt(this.returnType.value());

		out.writeLong(this.start_time);
		//		out.writeObject(time_stamps);

		if (this.temporal_type.value() == TestTemporalType._TEST_TEMPORAL_TYPE_PERIODICAL) {
			PeriodicalTestParameters ptp = this.time_stamps.ptpars();
			out.writeLong(ptp.dt);
			out.writeLong(ptp.end_time);
		}
		if (this.temporal_type.value() == TestTemporalType._TEST_TEMPORAL_TYPE_TIMETABLE)
				out.writeObject(this.time_stamps.ti());

		out.writeObject(this.kisId);
		out.writeObject(this.name);
		out.writeObject(this.analysisId);
		out.writeObject(this.evaluationId);
		out.writeLong(this.modified);
		out.writeObject(this.requestId);
		out.writeObject(this.userId);
		out.writeLong(this.deleted);
		out.writeObject(this.description);
		out.writeLong(this.duration);
		out.writeObject(this.testSetupId);
		out.writeObject(this.monitoredElementId);
		out.writeInt(this.status.value());
		Object obj = this.resultIds;
		out.writeObject(obj);

		//		Object obj2 = elementaryTestAlarms;
		//		out.writeObject(obj2);
		out.writeInt(this.elementaryTestAlarms.length);
		for (int i = 0; i < this.elementaryTestAlarms.length; i++) {
			out.writeLong(this.elementaryTestAlarms[i].elementary_start_time);
			out.writeObject(this.elementaryTestAlarms[i].alarm_id);
			out.writeObject(this.elementaryTestAlarms[i].alarm_name);
		}

		out.writeObject(this.testArgumentSetId);
	}
}

class TestTimeSorter extends ObjectResourceSorter {

	String[][]	sorted_columns	= new String[][] { { "time", "long"}};

	public long getLong(ObjectResource or, String column) {
		Test test = (Test) or;
		return test.getStartTime();
	}

	public String[][] getSortedColumns() {
		return this.sorted_columns;
	}

	public String getString(ObjectResource or, String column) {
		return "";
	}
}