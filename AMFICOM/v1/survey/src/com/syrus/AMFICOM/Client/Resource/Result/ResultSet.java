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
		transferable = new ResultSet_Transferable();
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
		return comments;
	}

	/**
	 * @return Returns the created.
	 */
	public long getCreated() {
		return created;
	}

	/**
	 * @return Returns the domainId.
	 */
	public String getDomainId() {
		return domain_id;
	}

	/**
	 * @return Returns the endTime.
	 */
	public long getEndTime() {
		return end_time;
	}

	public String getId() {
		return id;
	}

	public ObjectResourceModel getModel() {
		return new ResultSetModel(this);
	}

	/**
	 * @return Returns the modified.
	 */
	public long getModified() {
		return modified;
	}

	public String getName() {
		return name;
	}

	/**
	 * @return Returns the start_time.
	 */
	public long getStartTime() {
		return start_time;
	}

	public Object getTransferable() {
		return transferable;
	}

	public String getTyp() {
		return typ;
	}

	/**
	 * @return Returns the active.
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active
	 *            The active to set.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	/**
	 * @param comments
	 *            The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @param created
	 *            The created to set.
	 */
	public void setCreated(long created) {
		this.created = created;
	}

	/**
	 * @param domainId
	 *            The domainId to set.
	 */
	public void setDomainId(String domainId) {
		this.domain_id = domainId;
	}

	/**
	 * @param endTime
	 *            The endTime to set.
	 */
	public void setEndTime(long endTime) {
		this.end_time = endTime;
	}

	/**
	 * @param id
	 *            The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}

	public void setLocalFromTransferable() {
		id = transferable.id;
		name = transferable.name;

		domain_id = transferable.domain_id;
		comments = transferable.comments;
		active = transferable.active;

		created = transferable.created;
		modified = transferable.modified;
		start_time = transferable.start_time;
		end_time = transferable.end_time;

		name = ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(start_time)) + " - "
				+ ConstStorage.SIMPLE_DATE_FORMAT.format(new Date(end_time));
	}

	/**
	 * @param modified
	 *            The modified to set.
	 */
	public void setModified(long modified) {
		this.modified = modified;
	}

	/**
	 * @param name
	 *            The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param start_time
	 *            The start_time to set.
	 */
	public void setStartTime(long start_time) {
		this.start_time = start_time;
	}

	public void setTransferableFromLocal() {
		transferable.id = id;
		transferable.name = name;

		transferable.domain_id = domain_id;
		transferable.comments = comments;
		transferable.active = active;

		transferable.created = created;
		transferable.modified = modified;
		transferable.start_time = start_time;
		transferable.end_time = end_time;

	}

	public void updateLocalFromTransferable() {
		// nothing to do
	}

	private void readObject(java.io.ObjectInputStream in) throws IOException,
			ClassNotFoundException {
		id = (String) in.readObject();
		name = (String) in.readObject();
		domain_id = (String) in.readObject();
		comments = (String) in.readObject();
		active = in.readBoolean();

		created = in.readLong();
		modified = in.readLong();
		start_time = in.readLong();
		end_time = in.readLong();

		transferable = new ResultSet_Transferable();
		updateLocalFromTransferable();
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(domain_id);
		out.writeObject(comments);
		out.writeBoolean(active);

		out.writeLong(created);
		out.writeLong(modified);
		out.writeLong(start_time);
		out.writeLong(end_time);
	}
}

class ResultSetTimeSorter extends ObjectResourceSorter {

	String[][]	sorted_columns	= new String[][] { { "time", "long"}};

	public long getLong(ObjectResource or, String column) {
		ResultSet rs = (ResultSet) or;
		return rs.start_time;
	}

	public String[][] getSortedColumns() {
		return sorted_columns;
	}

	public String getString(ObjectResource or, String column) {
		return "";
	}
}