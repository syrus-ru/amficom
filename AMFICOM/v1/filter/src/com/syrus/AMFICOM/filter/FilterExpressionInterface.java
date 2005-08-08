/*
 * $Id: FilterExpressionInterface.java,v 1.5 2005/08/08 11:37:22 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.io.*;
import java.util.List;

/**
 * @version $Revision: 1.5 $, $Date: 2005/08/08 11:37:22 $
 * @module filter
 */
public interface FilterExpressionInterface
{
	String NUMERIC_EXPRESSION = "numeric";
	String STRING_EXPRESSION = "string";
	String RANGE_EXPRESSION = "range";
	String TIME_EXPRESSION = "time";
	String LIST_EXPRESSION = "list";

	String getName();
	String getColumnName();
	String getId();
	List getVec();
	int getListID();
	boolean isTemplate();

	void setName(String n);
	void setColumnName(String n);
	void setId(String i);
	void setVec(List v);
	void setListID(int l);
	void setTemplate (boolean newValue);

	void writeObject(ObjectOutputStream out)
		throws IOException;
	void readObject(ObjectInputStream in)
		throws IOException, ClassNotFoundException;

	Object clone();
}
