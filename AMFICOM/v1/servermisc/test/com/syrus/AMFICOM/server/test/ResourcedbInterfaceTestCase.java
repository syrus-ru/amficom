/*
 * $Id: ResourcedbInterfaceTestCase.java,v 1.1 2004/08/20 13:54:21 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.server.test;

import com.syrus.AMFICOM.server.*;
import com.syrus.AMFICOM.server.prefs.JDBCConnectionManager;
import java.sql.*;
import java.util.*;
import junit.framework.*;

public class ResourcedbInterfaceTestCase extends TestCase {
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

	protected void setUp() throws Exception {
		Class.forName(JDBCConnectionManager.class.getName());
	}

	public void testGetImage1() throws SQLException {
		System.out.println("Fetching an existing image...");
		assertNotNull(ResourcedbInterface.getImage("OTAU_proto"));
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

	public void testLoadImage1() {
		System.out.println("Fetching an existing image via loadImage()...");
		assertNotNull(ResourcedbInterface.loadImage("OTAU_proto"));
	}

	public void testLoadImage2() {
		System.out.println("Fetching a nonexistent image via loadImage() (an exception stack trace will be dumped)...");
		assertNull(ResourcedbInterface.loadImage("3.14159265358979323846"));
	}

	public void testGetImages1() throws SQLException {
		long l = System.currentTimeMillis();
		Collection imageResources = ResourcedbInterface.getImages(FETCH_SIZE);
		System.out.println("testGetImages1(): " + ((System.currentTimeMillis() - l) / 1000d) + " second(s)...");
		System.out.println("testGetImages1(): count: " + imageResources.size());
	}

	public void testGetImages2() throws SQLException {
		Connection conn = JDBCConnectionManager.getConn();
		Statement stmt = conn.createStatement();
		ResultSet resultSet = stmt.executeQuery("SELECT id FROM amficom.imageresources");
		Collection imageResourceIds = new LinkedList();
		int i = 0;
		while (resultSet.next() && (i++ < FETCH_SIZE))
			imageResourceIds.add(resultSet.getString("id"));
		resultSet.close();
		stmt.close();
		long l = System.currentTimeMillis();
		Collection imageResources = ResourcedbInterface.getImages(imageResourceIds);
		System.out.println("testGetImages2(): " + ((System.currentTimeMillis() - l) / 1000d) + " second(s)...");
		System.out.println("testGetImages2(): count: " + imageResources.size());
	}

	public void testGetImagesStd() throws SQLException {
		Connection conn = JDBCConnectionManager.getConn();
		Statement stmt = conn.createStatement();
		ResultSet resultSet = stmt.executeQuery("SELECT id FROM amficom.imageresources");
		Collection imageResourceIds = new LinkedList();
		int i = 0;
		while (resultSet.next() && (i++ < FETCH_SIZE))
			imageResourceIds.add(resultSet.getString("id"));
		resultSet.close();
		stmt.close();
		long l = System.currentTimeMillis();
		Collection imageResources = ResourcedbInterface.getImagesStd(imageResourceIds);
		System.out.println("testGetImagesStd(): " + ((System.currentTimeMillis() - l) / 1000d) + " second(s)...");
		System.out.println("testGetImagesStd(): count: " + imageResources.size());
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
