
package com.syrus.AMFICOM.client.resource;

import com.syrus.AMFICOM.CORBA.General.TestStatus;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Object.ObjectPermissionAttributes;

public class TestResource implements ObjectResource {

	public static final String	TYPE	= "TestResource";

	private boolean			changed	= false;
	private String			id	= "";
	private String			name	= "";
	private TestStatus		status	= TestStatus.TEST_STATUS_SCHEDULED;
	private long			time;

	public TestResource(String id) {
		this.id = id;
	}

	public String getDomainId() {
		throw new UnsupportedOperationException();
	}

	public String getId() {
		return this.id;
	}

	public ObjectResourceModel getModel() {
		throw new UnsupportedOperationException();
	}

	public long getModified() {
		return 0;
	}

	public String getName() {

		return this.name;
	}

	public ObjectPermissionAttributes getPermissionAttributes() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return Returns the status.
	 */
	public TestStatus getStatus() {
		return this.status;
	}

	/**
	 * @return Returns the time.
	 */
	public long getTime() {
		return this.time;
	}

	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	public String getTyp() {
		return TYPE;
	}

	public boolean isChanged() {
		return this.changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public void setLocalFromTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param name
	 *                The name to set.
	 */
	public void setName(String name) {
		this.changed = true;
		this.name = name;
	}

	/**
	 * @param status
	 *                The status to set.
	 */
	public void setStatus(TestStatus status) {
		this.changed = true;
		this.status = status;
	}

	/**
	 * @param time
	 *                The time to set.
	 */
	public void setTime(long time) {
		this.changed = true;
		this.time = time;
	}

	public void setTransferableFromLocal() {
		throw new UnsupportedOperationException();
	}

	public void updateLocalFromTransferable() {
		throw new UnsupportedOperationException();
	}
}