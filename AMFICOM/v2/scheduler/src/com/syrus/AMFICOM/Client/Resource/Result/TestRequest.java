package com.syrus.AMFICOM.Client.Resource.Result;

import java.util.*;

import com.syrus.AMFICOM.CORBA.General.*;
import com.syrus.AMFICOM.CORBA.Survey.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

public class TestRequest extends ObjectResource {

	public static final String				TYP				= "testrequest";
	private long							completionTime	= 0;
	private long							created			= 0;
	private long							deleted			= 0;
	private String							id				= "";
	private String							name			= "";
	private TestRequestStatus				status			= TestRequestStatus.TEST_REQUEST_STATUS_SHEDULED;
	private String							statusText		= "";
	private List							testIds			= new ArrayList();
	private ClientTestRequest_Transferable	transferable;
	private String							userId			= "";

	public TestRequest(ClientTestRequest_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public TestRequest(String id) {
		this.id = id;
		this.transferable = new ClientTestRequest_Transferable();
	}

	public void addTest(Test test) {
		System.out.println(getClass().getName() + "\taddTest:" + test.getId());
		this.testIds.add(test.getId());
	}

	/**
	 * @return Returns the completionTime.
	 */
	public long getCompletionTime() {
		return this.completionTime;
	}

	/**
	 * @return Returns the created.
	 */
	public long getCreated() {
		return this.created;
	}

	/**
	 * @return Returns the deleted.
	 */
	public long getDeleted() {
		return this.deleted;
	}

	public String getDomainId() {
		return ConstStorage.SYS_DOMAIN;
	}

	public String getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @return Returns the status.
	 */
	public TestRequestStatus getStatus() {
		return this.status;
	}

	/**
	 * @return Returns the statusText.
	 */
	public String getStatusText() {
		return this.statusText;
	}

	/**
	 * @return Returns the testIds.
	 */
	public List getTestIds() {
		return this.testIds;
	}

	public Object getTransferable() {
		return this.transferable;
	}

	public String getTyp() {
		return TYP;
	}

	/**
	 * @return Returns the userId.
	 */
	public String getUserId() {
		return userId;
	}

	public void removeTest(Test test) {
		this.testIds.remove(test.getId());
		if (this.testIds.isEmpty())
			this.changed = false;
	}

	/**
	 * @param completionTime
	 *            The completionTime to set.
	 */
	public void setCompletionTime(long completionTime) {
		this.changed = true;
		this.completionTime = completionTime;
	}

	/**
	 * @param created
	 *            The created to set.
	 */
	public void setCreated(long created) {
		this.changed = true;
		this.created = created;
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
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.changed = true;
		this.id = id;
	}

	public void setLocalFromTransferable() {
		this.id = this.transferable.id;
		this.name = this.transferable.name;
		this.created = this.transferable.created;
		this.userId = this.transferable.user_id;
		this.deleted = this.transferable.deleted;
		this.status = this.transferable.status;
		this.completionTime = this.transferable.completion_time;
		this.statusText = "";

		this.testIds.clear();
		for (int i = 0; i < this.transferable.test_ids.length; i++) {
			this.testIds.add(this.transferable.test_ids[i]);
		}
		this.changed = false;
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
	 * @param status
	 *            The status to set.
	 */
	public void setStatus(TestRequestStatus status) {
		this.changed = true;
		this.status = status;
	}

	/**
	 * @param statusText
	 *            The statusText to set.
	 */
	public void setStatusText(String status_text) {
		this.changed = true;
		this.statusText = status_text;
	}

	/**
	 * @param testIds
	 *            The testIds to set.
	 */
	public void setTestIds(List testIds) {
		this.changed = true;
		this.testIds = testIds;
	}

	public void setTransferableFromLocal() {
		/*
		 * public java.lang.String id; public java.lang.String name; public int
		 * status; public long created; public java.lang.String userId; public
		 * long deleted; public long completionTime; public java.lang.String
		 * statusText; public java.lang.String[] testIds;
		 */
		this.transferable.id = this.id;
		this.transferable.name = this.name;
		this.transferable.status = this.status;
		this.transferable.created = this.created;
		this.transferable.user_id = this.userId;
		this.transferable.deleted = this.deleted;
		this.transferable.completion_time = this.completionTime;
		this.transferable.status_text = "";

		this.transferable.test_ids = new String[this.testIds.size()];
		int i = 0;
		for (Iterator it = this.testIds.iterator(); it.hasNext();)
			this.transferable.test_ids[i++] = (String) it.next();
		this.changed = false;
	}

	/**
	 * @param userId
	 *            The userId to set.
	 */
	public void setUserId(String userId) {
		this.changed = true;
		this.userId = userId;
	}

	/*
	 * public void addParameter(Parameter parameter) {
	 * parameters.add(parameter); }
	 * 
	 * public void addArgument(Parameter argument) { arguments.add(argument); }
	 */
	public void updateLocalFromTransferable() {
		// here must be updateing local from tranferable
		// but it don't
	}
}