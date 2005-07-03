package com.syrus.impexp.unicablemap;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UniCableMapLink {
	static String LINK_QUERY = "select " +
			"UN_CHILD, " +
			"UN_PARENT, " +
			"MOD, " +
			"MSK, " +
			"SYS, " +
			"LORD " +
		"from LINK ";

	static String BY_PARENT_QUERY = "select " +
			"UN_CHILD, " +
			"UN_PARENT, " +
			"MOD, " +
			"MSK, " +
			"SYS, " +
			"LORD " +
		"from LINK " +
		"where UN_PARENT = ?";

	static String BY_PARENT_TYPED_QUERY = "select " +
			"UN_CHILD, " +
			"UN_PARENT, " +
			"MOD, " +
			"MSK, " +
			"SYS, " +
			"LORD " +
		"from LINK " +
		"where UN_PARENT = ? " +
		"and MOD = ?";

	static String BY_CHILD_QUERY = "select " +
			"UN_CHILD, " +
			"UN_PARENT, " +
			"MOD, " +
			"MSK, " +
			"SYS, " +
			"LORD " +
		"from LINK " +
		"where UN_CHILD = ?";

	static String BY_CHILD_TYPED_QUERY = "select " +
			"UN_CHILD, " +
			"UN_PARENT, " +
			"MOD, " +
			"MSK, " +
			"SYS, " +
			"LORD " +
		"from LINK " +
		"where UN_CHILD = ? " +
		"and MOD = ?";

	public static PreparedStatement linkStatement;
	public static PreparedStatement byParentStatement;
	public static PreparedStatement byParentTypedStatement;
	public static PreparedStatement byChildStatement;
	public static PreparedStatement byChildTypedStatement;

	public static void init(Connection connection) 
			throws SQLException {
		linkStatement = connection.prepareStatement(LINK_QUERY);
		byParentStatement = connection.prepareStatement(BY_PARENT_QUERY);
		byParentTypedStatement = connection.prepareStatement(BY_PARENT_TYPED_QUERY);
		byChildStatement = connection.prepareStatement(BY_CHILD_QUERY);
		byChildTypedStatement = connection.prepareStatement(BY_CHILD_TYPED_QUERY);
	}

	public UniCableMapObject child;
	public UniCableMapObject parent;
	public UniCableMapLinkType mod;
	public int msk;
	public int sys;
	public int lord;
	
	public UniCableMapLink() {
		// empty
	}
}
