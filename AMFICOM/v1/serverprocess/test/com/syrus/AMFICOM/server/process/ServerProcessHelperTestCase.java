/*
 * $Id: ServerProcessHelperTestCase.java,v 1.2 2004/09/14 14:01:22 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.server.process;

import com.syrus.AMFICOM.corba.portable.alarm.MessageImpl;
import com.syrus.AMFICOM.corba.portable.common.*;
import com.syrus.AMFICOM.server.prefs.JdbcConnectionManager;
import java.io.PrintWriter;
import java.sql.*;
import javax.sql.DataSource;
import junit.framework.*;
import sqlj.runtime.ref.DefaultContext;

/**
 * @version $Revision: 1.2 $, $Date: 2004/09/14 14:01:22 $
 * @author $Author: bass $
 * @module serverprocess-test
 */
public class ServerProcessHelperTestCase extends TestCase {
	private static final DataSource DATA_SOURCE = JdbcConnectionManager.getDataSource();

	private static final String SYS_USER = "sys";

	public ServerProcessHelperTestCase(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(ServerProcessHelperTestCase.class);
	}

	public static void main(java.lang.String[] args) {
		junit.awtui.TestRunner.run(ServerProcessHelperTestCase.class);
//		junit.swingui.TestRunner.run(ServerProcessHelperTestCase.class);
//		junit.textui.TestRunner.run(ServerProcessHelperTestCase.class);
	}

	public void testGetAlarmReceivers() throws SQLException {
		Identifier ids[] = ServerProcessHelper.getAlarmReceivers();
		for (int i = 0; i < ids.length; i++)
			System.out.println(ids[i]);
	}

	public void testGetLoggedUsers() throws SQLException {
		Identifier ids[] = ServerProcessHelper.getLoggedUsers();
		for (int i = 0; i < ids.length; i++)
			System.out.println(ids[i]);
	}

	public void testGetIORsForAlarmReceiver() throws SQLException {
		String iors[] = ServerProcessHelper.getIORsForAlarmReceiver(new IdentifierImpl(SYS_USER));
		for (int i = 0; i < iors.length; i++)
			System.out.println(iors[i]);
	}

	public void testGetIORsForLoggedUser() throws SQLException {
		String iors[] = ServerProcessHelper.getIORsForLoggedUser(new IdentifierImpl(SYS_USER));
		for (int i = 0; i < iors.length; i++)
			System.out.println(iors[i]);
	}

	public void testLoadMessages() throws SQLException {
		Connection conn = DefaultContext.getDefaultContext().getConnection();
		conn.createStatement().executeUpdate("UPDATE amficom.alertings SET alerted = NULL");
		conn.commit();
		AlertingMessageHolder alertingMessageHolders[];
		System.out.println("LOADING MESSAGES FOR ALL USERS");
		alertingMessageHolders = ServerProcessHelper.loadMessages(null);
		for (int i = 0; i < alertingMessageHolders.length; i++) {
			System.out.println("Message: " + alertingMessageHolders[i].getMessage());
			System.out.println("Alerting Type Id: " + alertingMessageHolders[i].getAlertingTypeId());
			System.out.println("User Id: " + alertingMessageHolders[i].getUserId());
		}
		System.out.println("LOADING MESSAGES FOR USER SYS");
		alertingMessageHolders = ServerProcessHelper.loadMessages(new IdentifierImpl(SYS_USER));
		for (int i = 0; i < alertingMessageHolders.length; i++) {
			System.out.println("Message: " + alertingMessageHolders[i].getMessage());
			System.out.println("Alerting Type Id: " + alertingMessageHolders[i].getAlertingTypeId());
			System.out.println("User Id: " + alertingMessageHolders[i].getUserId());
		}
		System.out.println("LOADING MESSAGES FOR AN UNKNOWN USER");
		alertingMessageHolders = ServerProcessHelper.loadMessages(new IdentifierImpl("Полный пердимонокль"));
		for (int i = 0; i < alertingMessageHolders.length; i++) {
			System.out.println("Message: " + alertingMessageHolders[i].getMessage());
			System.out.println("Alerting Type Id: " + alertingMessageHolders[i].getAlertingTypeId());
			System.out.println("User Id: " + alertingMessageHolders[i].getUserId());
		}
	}

	public void testSetAlerted() throws SQLException {
		ResultSet resultSet = null;
		Statement stmt = null;
		try {
			stmt = DefaultContext.getDefaultContext().getConnection().createStatement();
			resultSet = stmt.executeQuery("SELECT id AS alertingId FROM amficom.alertings WHERE alerted IS NULL");
			while (resultSet.next()) {
				ServerProcessHelper.setAlerted(new IdentifierImpl(resultSet.getString("alertingId")), new PrintWriter(System.out));
			}
		} finally {
			try {
				try {
					resultSet.close();
				} finally {
					stmt.close();
				}
			} catch (NullPointerException npe) {
				;
			}
		}
	}

	public void testSendEMailMessage() throws SQLException, MessageDeliveryFailedException {
		MessageImpl message = new MessageImpl();
		message.setAlertingId(new IdentifierImpl("a1"));
		message.setMessageTypeId(new IdentifierImpl("critical"));
		message.setEventId(new IdentifierImpl("sysev1"));
		message.setEventDate(System.currentTimeMillis());
		message.setEventSourceName("Sample RTU");
		message.setEventSourceDescription("Sample RTU Description");
		message.setTransmissionPathName("Sample Transmission Path");
		message.setTransmissionPathDescription("Sample Transmission Path Description");
		message.setText("We're screwed!.. We all gonna die!!!");
		ServerProcessHelper.sendEMailMessage(message, new IdentifierImpl(SYS_USER), new PrintWriter(System.out));
	}
}
