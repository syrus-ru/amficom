/*
 * $Id: AdminDbInterfaceLoadTestCase.java,v 1.1.2.1 2004/10/18 15:31:42 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.server.object;

import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.server.SqlConstants;
import com.syrus.AMFICOM.server.prefs.JdbcConnectionManager;
import java.sql.*;
import java.util.*;
import javax.sql.DataSource;
import junit.extensions.TestSetup;
import junit.framework.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1.2.1 $, $Date: 2004/10/18 15:31:42 $
 * @module server_v1
 */
public class AdminDbInterfaceLoadTestCase extends TestCase implements SqlConstants {
	private static DataSource dataSource;

	public AdminDbInterfaceLoadTestCase(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSetup(new TestSuite(AdminDbInterfaceLoadTestCase.class)) {
			protected void setUp() {
				AdminDbInterfaceLoadTestCase.dataSource = JdbcConnectionManager.getDataSource();
			}
		};
	}

	public static void main (String args[]) {
//		TestSuite suite = new TestSuite();
//		if (args.length != 0)
//			for (int i = 0; i < args.length; i++)
//				suite.addTest(new AdminDbInterfaceLoadTestCase(args[i]));
//		else
//			suite.addTest(AdminDbInterfaceLoadTestCase.suite());
//		junit.textui.TestRunner.run(suite); 
		
		junit.awtui.TestRunner.run(AdminDbInterfaceLoadTestCase.class);
//		junit.swingui.TestRunner.run(AdminDbInterfaceLoadTestCase.class);
//		junit.textui.TestRunner.run(suite);
	}

	public void testLoadServers1() throws SQLException {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			StringBuffer sql = new StringBuffer(KEYWORD_SELECT);
			sql.append(COLUMN_ID);
			sql.append(KEYWORD_FROM);
			sql.append(TABLE_SERVERS);
			ResultSet resultSet = stmt.executeQuery(sql.toString());
			Collection ids = new LinkedList();
			while (resultSet.next())
				ids.add(resultSet.getString(COLUMN_ID));
			AdminDbInterfaceLoad.loadServers(conn, new ServerSeq_TransferableHolder(), (String[]) ids.toArray(new String[ids.size()]));
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	public void testLoadServers2() throws SQLException {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			AdminDbInterfaceLoad.loadServers(conn, new ServerSeq_TransferableHolder());
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	public void testLoadClients1() throws SQLException {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			StringBuffer sql = new StringBuffer(KEYWORD_SELECT);
			sql.append(COLUMN_ID);
			sql.append(KEYWORD_FROM);
			sql.append(TABLE_CLIENTS);
			ResultSet resultSet = stmt.executeQuery(sql.toString());
			Collection ids = new LinkedList();
			while (resultSet.next())
				ids.add(resultSet.getString(COLUMN_ID));
			AdminDbInterfaceLoad.loadClients(conn, new ClientSeq_TransferableHolder(), (String[]) ids.toArray(new String[ids.size()]));
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	public void testLoadClients2() throws SQLException {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			AdminDbInterfaceLoad.loadClients(conn, new ClientSeq_TransferableHolder());
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	public void testLoadAgents1() throws SQLException {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			StringBuffer sql = new StringBuffer(KEYWORD_SELECT);
			sql.append(COLUMN_ID);
			sql.append(KEYWORD_FROM);
			sql.append(TABLE_AGENTS);
			ResultSet resultSet = stmt.executeQuery(sql.toString());
			Collection ids = new LinkedList();
			while (resultSet.next())
				ids.add(resultSet.getString(COLUMN_ID));
			AdminDbInterfaceLoad.loadAgents(conn, new AgentSeq_TransferableHolder(), (String[]) ids.toArray(new String[ids.size()]));
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	public void testLoadAgents2() throws SQLException {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			AdminDbInterfaceLoad.loadAgents(conn, new AgentSeq_TransferableHolder());
		} finally {
			if (conn != null)
				conn.close();
		}
	}
}
