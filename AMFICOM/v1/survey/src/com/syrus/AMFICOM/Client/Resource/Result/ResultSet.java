package com.syrus.AMFICOM.Client.Resource.Result;

import com.syrus.AMFICOM.CORBA.Survey.ResultSet_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceSorter;

import java.io.IOException;
import java.io.Serializable;

import java.text.SimpleDateFormat;

import java.util.Date;

public class ResultSet extends ObjectResource implements Serializable
{
	private static final long serialVersionUID = 01L;
	
	public static final String typ = "resultset";

	private ResultSet_Transferable transferable;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String id = "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String name = "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String domain_id = "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public String comments = "";
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public boolean active = false;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long created = 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long modified = 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long start_time = 0;
	/**
	 * @deprecated use setter/getter pair to access this field
	 */
	public long end_time = 0;

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm:ss");

	public ResultSet()
	{
		transferable = new ResultSet_Transferable();
	}

	public ResultSet(ResultSet_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public String getTyp()
	{
		return typ;
	}

	public String getDomainId()
	{
		return domain_id;
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;

		domain_id = transferable.domain_id;
		comments = transferable.comments;
		active = transferable.active;

		created = transferable.created;
		modified = transferable.modified;
		start_time = transferable.start_time;
		end_time = transferable.end_time;

		name = sdf.format(new Date(start_time)) + " - " + sdf.format(new Date(end_time));
	}

	public void setTransferableFromLocal()
	{
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

	public void updateLocalFromTransferable()
	{
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
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

	private void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		domain_id = (String )in.readObject();
		comments = (String )in.readObject();
		active = in.readBoolean();

		created = in.readLong();
		modified = in.readLong();
		start_time = in.readLong();
		end_time = in.readLong();

		transferable = new ResultSet_Transferable();
		updateLocalFromTransferable();
	}

	public static ObjectResourceSorter getDefaultSorter()
	{
		return new ResultSetTimeSorter();
	}
	
	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new com.syrus.AMFICOM.Client.Survey.Result.UI.ResultSetDisplayModel();
	}
	
	public ObjectResourceModel getModel()
	{
		return new ResultSetModel(this);
	}
	
	/**
	 * @return Returns the active.
	 */
	public boolean isActive() {
		return active;
	}
	/**
	 * @param active The active to set.
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	/**
	 * @return Returns the comments.
	 */
	public String getComments() {
		return comments;
	}
	/**
	 * @param comments The comments to set.
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}
	/**
	 * @return Returns the created.
	 */
	public long getCreated() {
		return created;
	}
	/**
	 * @param created The created to set.
	 */
	public void setCreated(long created) {
		this.created = created;
	}
	/**
	 * @return Returns the domain_id.
	 */
	public String getDomain_id() {
		return domain_id;
	}
	/**
	 * @param domain_id The domain_id to set.
	 */
	public void setDomain_id(String domain_id) {
		this.domain_id = domain_id;
	}
	/**
	 * @return Returns the end_time.
	 */
	public long getEnd_time() {
		return end_time;
	}
	/**
	 * @param end_time The end_time to set.
	 */
	public void setEnd_time(long end_time) {
		this.end_time = end_time;
	}
	/**
	 * @return Returns the modified.
	 */
	public long getModified() {
		return modified;
	}
	/**
	 * @param modified The modified to set.
	 */
	public void setModified(long modified) {
		this.modified = modified;
	}
	/**
	 * @return Returns the start_time.
	 */
	public long getStart_time() {
		return start_time;
	}
	/**
	 * @param start_time The start_time to set.
	 */
	public void setStart_time(long start_time) {
		this.start_time = start_time;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(String id) {
		this.id = id;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
}

class ResultSetTimeSorter extends ObjectResourceSorter
{
	String[][] sorted_columns = new String[][]{
		{"time", "long"}
		};

	public String[][] getSortedColumns()
	{
		return sorted_columns;
	}
	
	public String getString(ObjectResource or, String column)
	{
		return "";
	}

	public long getLong(ObjectResource or, String column)
	{
		ResultSet rs = (ResultSet )or;
		return rs.start_time;
	}
}
