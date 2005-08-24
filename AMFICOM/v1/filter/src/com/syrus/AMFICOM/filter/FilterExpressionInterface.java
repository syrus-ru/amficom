/*-
 * $Id: FilterExpressionInterface.java,v 1.7 2005/08/24 15:00:29 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.filter;

import java.io.*;
import java.util.List;

/**
 * @version $Revision: 1.7 $, $Date: 2005/08/24 15:00:29 $
 * @module filter
 */
public interface FilterExpressionInterface {
	String NUMERIC_EXPRESSION = "numeric";
	String STRING_EXPRESSION = "string";
	String RANGE_EXPRESSION = "range";
	String TIME_EXPRESSION = "time";
	String LIST_EXPRESSION = "list";

	String getName();

	String getColumnName();

	String getId();

	List<Object> getVec();

	int getListID();

	boolean isTemplate();

	void setName(String n);

	void setColumnName(String n);

	void setId(String i);

	void setVec(List<Object> v);

	void setListID(int l);

	void setTemplate(boolean newValue);

	void writeObject(ObjectOutputStream out) throws IOException;

	void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException;

	Object clone();
}
