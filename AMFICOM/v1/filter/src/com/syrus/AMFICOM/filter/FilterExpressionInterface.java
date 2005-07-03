/*
 * $Id: FilterExpressionInterface.java,v 1.4 2004/08/24 12:52:08 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.io.*;
import java.util.List;

/**
 * @version $Revision: 1.4 $, $Date: 2004/08/24 12:52:08 $
 * @module filter_v1
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
