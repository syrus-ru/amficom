/*
 * $Id: FilterExpressionInterface.java,v 1.2 2004/06/08 15:31:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.filter;

import java.io.*;
import java.util.Vector;

/**
 * @version $Revision: 1.2 $, $Date: 2004/06/08 15:31:57 $
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
	String getId();
	Vector getVec();
	int getListID();

	void setName(String n);
	void setId(String i);
	void setVec(Vector v);
	void setListID(int l);

	void writeObject(ObjectOutputStream out)
		throws IOException;
	void readObject(ObjectInputStream in)
		throws IOException, ClassNotFoundException;
}
