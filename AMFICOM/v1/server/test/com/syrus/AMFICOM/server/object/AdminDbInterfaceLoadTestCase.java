/*
 * $Id: AdminDbInterfaceLoadTestCase.java,v 1.1.2.2 2004/10/19 10:31:54 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.server.object;

import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.server.SqlConstants;
import com.syrus.AMFICOM.server.prefs.JdbcConnectionManager;
import java.sql.*;
import java.util.*;
import javax.sql.DataSource;
import junit.extensions.TestSetup;
import junit.framework.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1.2.2 $, $Date: 2004/10/19 10:31:54 $
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

	public void testLokupDomainName() throws SQLException {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(KEYWORD_SELECT + COLUMN_ID + KEYWORD_FROM + TABLE_DOMAINS);
			while (resultSet.next()) {
				String id = resultSet.getString(COLUMN_ID);
				String domainName = AdminDbInterfaceLoad.lookupDomainName(conn, new Identifier_Transferable(id));
				System.err.println(id + '\t' + domainName + '\t' + AdminDbInterfaceLoad.reverseLookupDomainName(conn, domainName).identifier_string);
			}
			resultSet.close();
			stmt.close();
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	public void testLokupUserLogin() throws SQLException {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(KEYWORD_SELECT + COLUMN_ID + KEYWORD_FROM + TABLE_USERS);
			while (resultSet.next()) {
				String id = resultSet.getString(COLUMN_ID);
				String userLogin = AdminDbInterfaceLoad.lookupUserLogin(conn, new Identifier_Transferable(id));
				System.err.println(id + '\t' + userLogin + '\t' + AdminDbInterfaceLoad.reverseLookupUserLogin(conn, userLogin).identifier_string);
			}
			resultSet.close();
			stmt.close();
		} finally {
			if (conn != null)
				conn.close();
		}
	}

	public void testLokupUserName() throws SQLException {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(KEYWORD_SELECT + COLUMN_ID + KEYWORD_FROM + TABLE_USERS);
			while (resultSet.next()) {
				String id = resultSet.getString(COLUMN_ID);
				String userName = AdminDbInterfaceLoad.lookupUserName(conn, new Identifier_Transferable(id));
				System.err.println(id + '\t' + userName + '\t' + AdminDbInterfaceLoad.reverseLookupUserName(conn, userName).identifier_string);
			}
			resultSet.close();
			stmt.close();
		} finally {
			if (conn != null)
				conn.close();
		}
	}
}
