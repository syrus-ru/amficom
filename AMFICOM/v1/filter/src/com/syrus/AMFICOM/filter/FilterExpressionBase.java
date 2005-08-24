/*-
 * $Id: FilterExpressionBase.java,v 1.8 2005/08/24 15:00:29 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @version $Revision: 1.8 $, $Date: 2005/08/24 15:00:29 $
 * @module filter
 */
public class FilterExpressionBase implements FilterExpressionInterface {

	/**
	 * Value: {@value}
	 */
	public static final String TYP = "filterexpression";

	protected String colName = "";
	protected String colId = "";

	protected String visualName = "";

	protected List<Object> vec = new ArrayList<Object>();
	protected boolean isTemplate = false;

	protected int listID = 0;

	public String getName() {
		return this.visualName;
	}

	public String getId() {
		return this.colId;
	}

	public List<Object> getVec() {
		return this.vec;
	}

	public int getListID() {
		return this.listID;
	}

	public boolean isTemplate() {
		return this.isTemplate;
	}

	public void setName(final String n) {
		this.visualName = n;
	}

	public void setColumnName(final String n) {
		this.colName = n;
	}

	public String getColumnName() {
		return this.colName;
	}

	public void setId(final String i) {
		this.colId = i;
	}

	public void setVec(final List<Object> v) {
		this.vec = v;
	}

	public void setListID(final int l) {
		this.listID = l;
	}

	public void setTemplate(final boolean ifT) {
		this.isTemplate = ifT;
	}

	public void writeObject(final ObjectOutputStream out) throws IOException {
		/*
		 * ???: what's the need of creating one more object *reference* before
		 * serialization?
		 */

		out.writeObject(this.visualName);
		out.writeObject(this.colName);
		out.writeObject(this.colId);
		// Vector vec1 = vec;
		// String type = (String )vec.get(0);
		// if (type.equals(LIST_EXPRESSION))
		// {
		// TreeModelClone tree = (TreeModelClone )vec.get(1);
		// vec1.setElementAt(tree.getHash(), 1);
		// }
		// out.writeObject(vec1);
		out.writeObject(this.vec);

		out.writeInt(this.listID);
	}

	public void readObject(final ObjectInputStream in) throws IOException, ClassNotFoundException {
		this.visualName = (String) in.readObject();
		this.colName = (String) in.readObject();
		this.colId = (String) in.readObject();
		this.vec = (List) in.readObject();
		// Vector vec1 = (Vector )in.readObject();
		// String type = (String )vec1.get(0);
		// if (type.equals(LIST_EXPRESSION))
		// {
		// Hashtable h = (Hashtable )vec.get(1);
		// vec1.setElementAt(tree.getHash(), 1);
		// }
		/*
		 * ???: the same: assignment WILL NOT happen if readObject() fails.
		 */
		// vec = vec1;
		this.listID = in.readInt();
	}

	@Override
	public Object clone() {
		final FilterExpressionBase fe = new FilterExpressionBase();
		fe.setName(getName());
		fe.setColumnName(getColumnName());
		fe.setId(getId());

		final List<Object> cloneList = new ArrayList<Object>();
		cloneList.addAll(this.getVec());
		fe.setVec(cloneList);

		fe.setListID(getListID());

		return fe;
	}

}
