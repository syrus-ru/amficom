package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.ResultSet_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Survey.General.ConstStorage;

import java.io.IOException;
import java.io.Serializable;

import java.util.Date;

public class ResultSet extends ObjectResource implements Serializable {

	private static final long		serialVersionUID	= 01L;

	public static final String		typ					= "resultset";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public boolean					active				= false;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String					comments			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long						created				= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String					domain_id			= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long						end_time			= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String					id					= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long						modified			= 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String					name				= "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long						start_time			= 0;

	private ResultSet_Transferable	transferable;

	public ResultSet() {
		this.transferable = new ResultSet_Transferable();
	}

	public ResultSet(ResultSet_Transferable transferable) {
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel() {
		return new com.syrus.AMFICOM.Client.Survey.Result.UI.ResultSetDisplayModel();
	}

	public static ObjectResourceSorter getDefaultSorter() {
		return new ResultSetTimeSorter();
	}

	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return this.comments;
	}

	/**
	 * @return Returns the created.
	 */
	public long getCreated() {
		return this.created;
	}

	/**
	 * @return Returns the domainId.
	 */
	public String getDomainId() {
		return this.domain_id;
	}

	/**
	 * @return Returns the endTime.
	 */
	public long getEndTime() {
		return this.end_time;
	}

	public String getId() {
		return this.id;
	}

	public ObjectResourceModel getModel() {
		return new ResultSetModel(this);
	}

	/**
	 * @return Returns the modified.
	 */
	public long getModified() {
		return this.modified;
	}

	public String getName() {
		return this.name;
	}

	/**
	 * @return Returns the startTime.
	 */
	public long getStartTime() {
		return this.start_time;
	}

	public Object getTransferable() {
		return this.transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @return Returns the active.
	 */
	public boolean isActive() {
		return this.active;
	}

	/**
	 * @param active
	 *            The active to set.
	 */
	public void setActive(boolean active) {
		this.changed = true;
		this.active = active;
	}

	/**
	 * @param comments
	 *            The comments to set.
	 */
	public void setComments(String comments) {
		this.changed = true;
		this.comments = comments;
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
	 * @param domainId
	 *            The domainId to set.
	 */
	public void setDomainId(String domainId) {
		this.changed = true;
		this.domain_id = domainId;
	}

	/**
	 * @param endTime
	 *            The endTime to set.
	 */
	public void setEndTime(long endTime) {
		this.changed = true;
		this.end_time = endTime;
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

		this.domain_id = this.transferable.domain_id;
		this.comments = this.transferable.comments;
		this.active = this.transferable.active;

		this.created = this.transferable.created;
		this.modified = this.transferable.modified;
		this.start_time = this.transferable.start_time;
		this.end_time = this.transferable.end_time;

		this.name = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(
				this.start_time))
				+ " - "
				+ ConstStorage.SIMPLE_DATE_FORMAT
						.format(new Date(this.end_time));
		this.changed = false;
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
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.changed = true;
		this.name = name;
	}

	/**
	 * @param startTime
	 *            The startTime to set.
	 */
	public void setStartTime(long startTime) {
		this.changed = true;
		this.start_time = startTime;
	}

	public void setTransferableFromLocal() {
		this.transferable.id = this.id;
		this.transferable.name = this.name;

		this.transferable.domain_id = this.domain_id;
		this.transferable.comments = this.comments;
		this.transferable.active = this.active;

		this.transferable.created = this.created;
		this.transferable.modified = this.modified;
		this.transferable.start_time = this.start_time;
		this.transferable.end_time = this.end_time;
		this.changed = false;

	}

	public void updateLocalFromTransferable() {
		this.changed = false;
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		this.id = (String) in.readObject();
		this.name = (String) in.readObject();
		this.domain_id = (String) in.readObject();
		this.comments = (String) in.readObject();
		this.active = in.readBoolean();

		this.created = in.readLong();
		this.modified = in.readLong();
		this.start_time = in.readLong();
		this.end_time = in.readLong();

		this.transferable = new ResultSet_Transferable();
		updateLocalFromTransferable();
		this.changed = false;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(this.id);
		out.writeObject(this.name);
		out.writeObject(this.domain_id);
		out.writeObject(this.comments);
		out.writeBoolean(this.active);

		out.writeLong(this.created);
		out.writeLong(this.modified);
		out.writeLong(this.start_time);
		out.writeLong(this.end_time);
		this.changed = false;
	}
}

class ResultSetTimeSorter extends ObjectResourceSorter {

	String[][]	sorted_columns	= new String[][] { { "time", "long"}};

	public long getLong(ObjectResource or, String column) {
		ResultSet rs = (ResultSet) or;
		return rs.start_time;
	}

	public String[][] getSortedColumns() {
		return this.sorted_columns;
	}

	public String getString(ObjectResource or, String column) {
		return "";
	}
}

