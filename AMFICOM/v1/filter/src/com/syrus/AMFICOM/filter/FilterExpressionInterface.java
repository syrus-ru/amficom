package com.syrus.AMFICOM.filter;

import java.util.Vector;
import java.io.*;

public interface FilterExpressionInterface 
{
	public static String NUMERIC_EXPRESSION = "numeric";
	public static String STRING_EXPRESSION = "string";
	public static String RANGE_EXPRESSION = "range";
	public static String TIME_EXPRESSION = "time";
	public static String LIST_EXPRESSION = "list";

	public String getName();
	public String getId();
	public Vector getVec();
	public int getListID();

	public void setName(String n);
	public void setId(String i);
	public void setVec(Vector v);
	public void setListID(int l);

	public void writeObject(java.io.ObjectOutputStream out)
		throws IOException;
	public void readObject(java.io.ObjectInputStream in)
		throws IOException, ClassNotFoundException;
}