package com.syrus.impexp.unicablemap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UniCableMapObject {
	public static final String OBJECT_QUERY = "select " +
			"UN, " +
			"TYP, " +
			"TEXT, " +
			"BUF, " +
			"MSK, " +
			"ORD, " +
			"DX, " +
			"DY, " +
			"X0, " +
			"Y0, " +
			"X1, " +
			"Y1, " +
			"STATE, " +
			"ELID " +
		"from OBJECT";
	public static PreparedStatement objectStatement;

	public static final String UN_OBJECT_QUERY = "select " +
			"UN, " +
			"TYP, " +
			"TEXT, " +
			"BUF, " +
			"MSK, " +
			"ORD, " +
			"DX, " +
			"DY, " +
			"X0, " +
			"Y0, " +
			"X1, " +
			"Y1, " +
			"STATE, " +
			"ELID " +
		"from OBJECT " +
		"where UN = ? ";
	public static PreparedStatement unObjectStatement;

	public int un;
	public UniCableMapType typ;
	public String text;
	public UniCableMapBuf buf;
	public int msk;
	public int ord;
	public double dx;
	public double dy;
	public double x0;
	public double y0;
	public double x1;
	public double y1;
	public int state;
	public String elid;

	public static void init(Connection connection) 
			throws SQLException {
		objectStatement = connection.prepareStatement(OBJECT_QUERY);
		unObjectStatement = connection.prepareStatement(UN_OBJECT_QUERY);
	}

	public UniCableMapObject() {
	}

	public String toString()
	{
		return text + " | [un]"
			+ un + " | [typ]"
			+ typ.un + " (" + typ.text + ") | ";/*
			+ msk + " | "
			+ ord + " | "
			+ dx + " | "
			+ dy + " | "
			+ x0 + " | "
			+ y0 + " | "
			+ x1 + " | "
			+ y1 + " | "
			+ state + " | "
			+ elid;*/
	}
}

