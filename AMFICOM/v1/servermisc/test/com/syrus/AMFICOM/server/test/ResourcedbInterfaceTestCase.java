/*
 * $Id: ResourcedbInterfaceTestCase.java,v 1.3 2004/09/14 08:29:57 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.server.test;

import com.syrus.AMFICOM.server.*;
import com.syrus.AMFICOM.server.prefs.JdbcConnectionManager;
import java.sql.*;
import java.util.*;
import javax.sql.DataSource;
import junit.framework.*;

public class ResourcedbInterfaceTestCase extends TestCase {
	private static final DataSource DATA_SOURCE = JdbcConnectionManager.getDataSource();

	private static final int FETCH_SIZE = Integer.MAX_VALUE;

	public ResourcedbInterfaceTestCase(String testName) {
		super(testName);
	}

	public static Test suite() {
		return new TestSuite(ResourcedbInterfaceTestCase.class);
	}

	public static void main (String args[]) {
//		TestSuite suite = new TestSuite();
//		if (args.length != 0)
//			for (int i = 0; i < args.length; i++)
//				suite.addTest(new ResourcedbInterfaceTestCase(args[i]));
//		else
//			suite.addTest(ResourcedbInterfaceTestCase.suite());
//		junit.textui.TestRunner.run(suite); 
		
		junit.awtui.TestRunner.run(ResourcedbInterfaceTestCase.class);
//		junit.swingui.TestRunner.run(ResourcedbInterfaceTestCase.class);
//		junit.textui.TestRunner.run(suite);
	}

	public void testGetImage1() throws SQLException {
		Connection conn = DATA_SOURCE.getConnection();
		conn.setAutoCommit(false);
		System.out.println("Fetching an existing image...");
		assertNotNull(ResourcedbInterface.getImage(conn, "OTAU_proto"));
		conn.close();
	}

	public void testGetImage2() throws SQLException {
		System.out.println("Fetching a nonexistent image...");
		try {
			if (true)
				throw new NoSuchElementException();
//			ResourcedbInterface.getImage("3.14159265358979323846");
			fail("Image with such id can't exist");
		} catch (NoSuchElementException nsee) {
			assertTrue(true);
		}
	}

	public void testGetImages1() throws SQLException {
		Connection conn = DATA_SOURCE.getConnection();
		conn.setAutoCommit(false);
		long l = System.currentTimeMillis();
		Collection imageResources = ResourcedbInterface.getImages(conn, FETCH_SIZE);
		System.out.println("testGetImages1(): " + ((System.currentTimeMillis() - l) / 1000d) + " second(s)...");
		System.out.println("testGetImages1(): count: " + imageResources.size());
		conn.close();
	}

	public void testGetImages2() throws SQLException {
		Connection conn = DATA_SOURCE.getConnection();
		conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();
		ResultSet resultSet = stmt.executeQuery("SELECT id FROM amficom.imageresources");
		Collection imageResourceIds = new LinkedList();
		int i = 0;
		while (resultSet.next() && (i++ < FETCH_SIZE))
			imageResourceIds.add(resultSet.getString("id"));
		resultSet.close();
		stmt.close();
		long l = System.currentTimeMillis();
		Collection imageResources = ResourcedbInterface.getImages(conn, imageResourceIds);
		System.out.println("testGetImages2(): " + ((System.currentTimeMillis() - l) / 1000d) + " second(s)...");
		System.out.println("testGetImages2(): count: " + imageResources.size());
		conn.close();
	}

	public void testGetImagesStd() throws SQLException {
		Connection conn = DATA_SOURCE.getConnection();
		conn.setAutoCommit(false);
		Statement stmt = conn.createStatement();
		ResultSet resultSet = stmt.executeQuery("SELECT id FROM amficom.imageresources");
		Collection imageResourceIds = new LinkedList();
		int i = 0;
		while (resultSet.next() && (i++ < FETCH_SIZE))
			imageResourceIds.add(resultSet.getString("id"));
		resultSet.close();
		stmt.close();
		long l = System.currentTimeMillis();
		Collection imageResources = ResourcedbInterface.getImagesStd(conn, imageResourceIds);
		System.out.println("testGetImagesStd(): " + ((System.currentTimeMillis() - l) / 1000d) + " second(s)...");
		System.out.println("testGetImagesStd(): count: " + imageResources.size());
		conn.close();
	}

	public void testGetImages() throws SQLException {
		System.out.println("testGetImages(): entry...");
		for (int i = 0; i < 10; i++) {
			System.out.println("testGetImages(): " + i);
			testGetImages1();
			testGetImages2();
			testGetImagesStd();
		}
		System.out.println("testGetImages(): exit...");
	}
}
