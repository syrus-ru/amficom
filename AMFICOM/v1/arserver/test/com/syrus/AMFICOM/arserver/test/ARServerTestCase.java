/*
 * $Id: ARServerTestCase.java,v 1.2 2005/02/01 15:35:57 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.arserver.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.List;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.omg.CORBA.ORB;
import org.omg.CORBA.UserException;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import com.syrus.AMFICOM.arserver.ARServerResourceObjectLoader;
import com.syrus.AMFICOM.arserver.corba.ARServer;
import com.syrus.AMFICOM.arserver.corba.ARServerHelper;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort;
import com.syrus.util.ClientLRUMap;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/01 15:35:57 $
 * @author $Author: max $
 * @module arserver_v1
 */
public class ARServerTestCase extends TestCase {

	private static ARServer							server;
	private static AccessIdentifier_Transferable	accessIdentifier_Transferable;   
    private int j;        
    
	public ARServerTestCase(String name) {
		super(name);
	}
    
    public static void main(java.lang.String[] args) {
		Class clazz = ARServerTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}
    
    public static Test suite() {
		return suiteWrapper(ARServerTestCase.class);		
	}
    
    private static Test suiteWrapper(Class clazz) {
		TestSuite suite = new TestSuite(clazz);
		TestSetup wrapper = new TestSetup(suite) {

			protected void setUp() {
				oneTimeSetUp();
			}

			protected void tearDown() {
				oneTimeTearDown();
			}
		};
		return wrapper;
	}
    
    static void oneTimeSetUp() {
		try {
			ORB orb = JavaSoftORBUtil.getInstance().getORB();
			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootPOA.the_POAManager().activate();
			NamingContextExt rootNamingCtx = NamingContextExtHelper.narrow(orb
					.resolve_initial_references("NameService"));
			
			final String hostName = InetAddress.getLocalHost().getCanonicalHostName()
					.replaceAll("\\.", "_");
			
			server = ARServerHelper.narrow(rootNamingCtx.resolve_str(hostName + "/ARServer"));
			
			ResourceStorableObjectPool.init(new ARServerResourceObjectLoader(1000l),
					ClientLRUMap.class, 200);
			
			IdentifierPool.init(server);
			
			System.out.println("server reference have got : \n" + server.toString());
			
			accessIdentifier_Transferable = new AccessIdentifier_Transferable();
			
			Identifier id = new Identifier("Null_0");
			
			accessIdentifier_Transferable.domain_id = (Identifier_Transferable)(
					new Identifier("Domain_19")).getTransferable();
			accessIdentifier_Transferable.user_id = (Identifier_Transferable)(
					new Identifier("Users_56")).getTransferable();
			accessIdentifier_Transferable.session_id = (Identifier_Transferable) id.getTransferable();
									
		} catch (UnknownHostException e) {			
			System.out.println("UnknownHostException: " + e.getMessage());
			e.printStackTrace();
		} catch (UserException e) {
			System.out.println("UnknownHostException: " + e.getMessage());
			e.printStackTrace();
		}
    }
    
    static void oneTimeTearDown() {
        // empty;
	}
    
    public void _testRecieveBlob() throws AMFICOMRemoteException {
    	Identifier id = new Identifier("ImageResource_1721");
    	
    	ImageResource_Transferable testTrans = server.transmitImageResource((Identifier_Transferable)id.getTransferable(), accessIdentifier_Transferable);
    	    	
    	int sort = testTrans.data.discriminator().value();
		
    	if(sort == ImageResourceSort._FILE) {
			FileImageResource test = new FileImageResource(testTrans);
			System.out.println(test.getFileName());
		} else if(sort == ImageResourceSort._SCHEME) {			
			SchemeImageResource test = new SchemeImageResource(testTrans);
			byte[] image = test.getImage();
			for (int i = 0; i < image.length; i++) {
				System.out.println(image[i]);				
			}
		} else if(sort == ImageResourceSort._BITMAP) {
			BitmapImageResource test = new BitmapImageResource(testTrans);
			byte[] image = test.getImage();
			for (int i = 0; i < image.length; i++) {
				System.out.println(image[i]);				
			}
		} else {
			assertFalse("wrong sort" + sort, true);
    	}
    }
    
    public void _testSingleInsertUpdateDelete() throws AMFICOMRemoteException, CreateObjectException {
        
        // some preporations. Important: it is necessarily to change "j" variable every time you start this test
    	// , otherwise an exception will arise. Value must be unique
    	this.j = 1160;
    	Identifier creatorId = new Identifier(accessIdentifier_Transferable.user_id);
    	byte[] image = { 0, 12, 32, 56 ,-23 ,15, -45};
    	byte[] image2 = { 10, 112, 2, 6 ,-23 ,105, -5};
        List data = new LinkedList();
    	data.add(new String("bla bla bla"));
    	List data2 = new LinkedList();
    	data2.add(new String("bla bla bla bla bla"));
    	String s = "TEST";
        
    	
    	// Creation new objects, Cautions: attribute "codename" must be unique
    	FileImageResource fir1 = FileImageResource.createInstance(creatorId, s + (++this.j));
    	SchemeImageResource sir1 = SchemeImageResource.createInstance(creatorId);
        BitmapImageResource bir1 = BitmapImageResource.createInstance(creatorId, s + (++this.j), image);
        
    	// filling attributes
        sir1.setData(data);        
        
        // Getting transferables
        ImageResource_Transferable fir1Trans = (ImageResource_Transferable) fir1.getTransferable();
        ImageResource_Transferable sir1Trans = (ImageResource_Transferable) sir1.getTransferable();
        ImageResource_Transferable bir1Trans = (ImageResource_Transferable) bir1.getTransferable();
        
        // First test. Creation
        server.receiveImageResource(fir1Trans, true, accessIdentifier_Transferable);
        server.receiveImageResource(sir1Trans, true, accessIdentifier_Transferable);
        server.receiveImageResource(bir1Trans, true, accessIdentifier_Transferable);
        
        // Receiving from server
        ImageResource_Transferable testfir1Trans = server.transmitImageResource(
        		(Identifier_Transferable)fir1.getId().getTransferable(), 
        		accessIdentifier_Transferable);
        ImageResource_Transferable testsir1Trans = server.transmitImageResource(
        		(Identifier_Transferable)sir1.getId().getTransferable(), 
        		accessIdentifier_Transferable);
        ImageResource_Transferable testbir1Trans = server.transmitImageResource(
        		(Identifier_Transferable)bir1.getId().getTransferable(), 
        		accessIdentifier_Transferable);
        
        FileImageResource testfir1 = new FileImageResource(testfir1Trans);
        SchemeImageResource testsir1 = new SchemeImageResource(testsir1Trans);
        BitmapImageResource testbir1 = new BitmapImageResource(testbir1Trans);
        
        // Comparing
        assertEquals(fir1.getCodename(), testfir1.getCodename());
        assertEquals(fir1.getId(), testfir1.getId());
        
        assertEquals(sir1.getId(), testsir1.getId());
        System.out.println("Checking data");
        for (int i = 0; i < sir1.getImage().length; i++) {
			System.out.print(sir1.getImage()[i]);
		}
        System.out.println("");
        for (int i = 0; i < testsir1.getImage().length; i++) {
			System.out.print(testsir1.getImage()[i]);
		}
        System.out.println("");
        
        assertEquals(bir1.getCodename(), testbir1.getCodename());
        assertEquals(bir1.getId(), testbir1.getId());
        System.out.println("Checking data");
        for (int i = 0; i < bir1.getImage().length; i++) {
			System.out.print(bir1.getImage()[i]);
		}
        System.out.println("");
        for (int i = 0; i < testbir1.getImage().length; i++) {
			System.out.print(testbir1.getImage()[i]);
		}
        
        // Second Test. Updating
        // Making some changes
        fir1.setFileName(s + s + this.j);
        sir1.setData(data2);
		bir1.setImage(image2);
        
		// Getting transferables
        fir1Trans = (ImageResource_Transferable) fir1.getTransferable();
        sir1Trans = (ImageResource_Transferable) sir1.getTransferable();
        bir1Trans = (ImageResource_Transferable) bir1.getTransferable();
        
        // Sending to server
		server.receiveImageResource(fir1Trans, true, accessIdentifier_Transferable);
        server.receiveImageResource(sir1Trans, true, accessIdentifier_Transferable);
        server.receiveImageResource(bir1Trans, true, accessIdentifier_Transferable);
		
		// Receiving from server
        testfir1Trans = server.transmitImageResource(
				(Identifier_Transferable)fir1.getId().getTransferable(), 
				accessIdentifier_Transferable);
		testsir1Trans = server.transmitImageResource(
				(Identifier_Transferable)sir1.getId().getTransferable(), 
				accessIdentifier_Transferable);
		testbir1Trans = server.transmitImageResource(
				(Identifier_Transferable)bir1.getId().getTransferable(), 
				accessIdentifier_Transferable);
                
        testfir1 = new FileImageResource(testfir1Trans);
        testsir1 = new SchemeImageResource(testsir1Trans);
        testbir1 = new BitmapImageResource(testbir1Trans);
        
        // Comparing
        assertEquals(fir1.getCodename(), testfir1.getCodename());
        assertEquals(fir1.getId(), testfir1.getId());
        
        assertEquals(sir1.getId(), testsir1.getId());
        System.out.println("Checking data");
        for (int i = 0; i < sir1.getImage().length; i++) {
			System.out.print(sir1.getImage()[i]);
		}
        System.out.println("");
        for (int i = 0; i < testsir1.getImage().length; i++) {
			System.out.print(testsir1.getImage()[i]);
		}
        System.out.println("");
        
        assertEquals(bir1.getCodename(), testbir1.getCodename());
        assertEquals(bir1.getId(), testbir1.getId());
        System.out.println("Checking data");
        for (int i = 0; i < bir1.getImage().length; i++) {
			System.out.print(bir1.getImage()[i]);
		}
        System.out.println("");
        for (int i = 0; i < testbir1.getImage().length; i++) {
			System.out.print(testbir1.getImage()[i]);
		}
        // Third test. Deleting
        // Removing from server db
        server.delete((Identifier_Transferable)fir1.getId().getTransferable(), 
				accessIdentifier_Transferable);
		server.delete((Identifier_Transferable)sir1.getId().getTransferable(), 
				accessIdentifier_Transferable);
		server.delete((Identifier_Transferable)bir1.getId().getTransferable(), 
				accessIdentifier_Transferable);
		// Trying to load from server
		
		// Clearing 
		testfir1Trans = null;
		testsir1Trans = null;
		testbir1Trans = null;
		// if we are realy deleted entitys we must have here exceptions 
		try {
			testfir1Trans = server.transmitImageResource(
					(Identifier_Transferable)fir1.getId().getTransferable(), 
					accessIdentifier_Transferable);
		} catch (AMFICOMRemoteException e){
			// if we here we are right
			System.out.println("excecptions has been thrown");
		}
		try {
			testsir1Trans = server.transmitImageResource(
					(Identifier_Transferable)sir1.getId().getTransferable(), 
					accessIdentifier_Transferable);				
		} catch (AMFICOMRemoteException e){
			// if we here we are right
			System.out.println("excecptions has been thrown");
		}
		try {
			testbir1Trans = server.transmitImageResource(
					(Identifier_Transferable)bir1.getId().getTransferable(), 
					accessIdentifier_Transferable);		
		} catch (AMFICOMRemoteException e){
			// if we here we are right
			System.out.println("excecptions has been thrown");
		}
		assertFalse("transferables must be null", !(testfir1Trans == null) 
				&& !(testsir1Trans == null) 
				&& !(testbir1Trans == null));
    }
    
    public void testMultiplyInsertUpdateDelete() throws AMFICOMRemoteException, CreateObjectException {
        
        // some preporations. Important: it is necassarly to change "j" variable every time you start this test
    	// , otherwise an exception will arise
    	this.j = 1240;
    	Identifier creatorId = new Identifier(accessIdentifier_Transferable.user_id);
    	byte[] image = { 0, 12, 32, 56 ,-23 ,15, -45};
    	byte[] image2 = { 10, 112, 2, 6 ,-23 ,105, -5};
        List data = new LinkedList();
    	data.add(new String("bla bla bla"));
    	List data2 = new LinkedList();
    	data2.add(new String("bla bla bla bla bla"));
    	String s = "TEST";
        
    	
    	// Creation new objects, Cautions: attribute "codename" must be unique
    	FileImageResource fir1 = FileImageResource.createInstance(creatorId, s + (++this.j));
    	SchemeImageResource sir1 = SchemeImageResource.createInstance(creatorId);
        BitmapImageResource bir1 = BitmapImageResource.createInstance(creatorId, s + (++this.j), image);
        
    	// filling attributes
        sir1.setData(data);        
        
        // Getting transferables
        ImageResource_Transferable fir1Trans = (ImageResource_Transferable) fir1.getTransferable();
        ImageResource_Transferable sir1Trans = (ImageResource_Transferable) sir1.getTransferable();
        ImageResource_Transferable bir1Trans = (ImageResource_Transferable) bir1.getTransferable();
        
        ImageResource_Transferable[] trans = {fir1Trans, sir1Trans, bir1Trans};
        
        // First test. Creation
        server.receiveImageResources(trans, true, accessIdentifier_Transferable);        
        
        // Receiving from server
        
        Identifier_Transferable[] idTrans = {(Identifier_Transferable)fir1.getId().getTransferable(),
        		(Identifier_Transferable)sir1.getId().getTransferable(),
        		(Identifier_Transferable)bir1.getId().getTransferable()};
        
        ImageResource_Transferable[] testTrans = server.transmitImageResources(idTrans, 
        		accessIdentifier_Transferable);
        
        FileImageResource testfir1 = null;
        SchemeImageResource testsir1 = null;
        BitmapImageResource testbir1 = null;
        for (int i = 0; i < testTrans.length; i++) {
			ImageResource_Transferable imgTrans = testTrans[i];
			int sort = imgTrans.data.discriminator().value();
			if(sort == ImageResourceSort._FILE)
				testfir1 = new FileImageResource(testTrans[i]);
			else if(sort == ImageResourceSort._SCHEME)
				testsir1 = new SchemeImageResource(testTrans[i]);
			else if(sort == ImageResourceSort._BITMAP)
				testbir1 = new BitmapImageResource(testTrans[i]);
			else 
				assertFalse("wrong sort" + sort, true);			
		}
        
        // Comparing
        assertEquals(fir1.getCodename(), testfir1.getCodename());
        assertEquals(fir1.getId(), testfir1.getId());
        
        assertEquals(sir1.getId(), testsir1.getId());
        System.out.println("Checking data");
        for (int i = 0; i < sir1.getImage().length; i++) {
			System.out.print(sir1.getImage()[i]);
		}
        System.out.println("");
        for (int i = 0; i < testsir1.getImage().length; i++) {
			System.out.print(testsir1.getImage()[i]);
		}
        System.out.println("");
        
        assertEquals(bir1.getCodename(), testbir1.getCodename());
        assertEquals(bir1.getId(), testbir1.getId());
        System.out.println("Checking data");
        for (int i = 0; i < bir1.getImage().length; i++) {
			System.out.print(bir1.getImage()[i]);
		}
        System.out.println("");
        for (int i = 0; i < testbir1.getImage().length; i++) {
			System.out.print(testbir1.getImage()[i]);
		}
        
        // Second Test. Updating
        // Making some changes
        fir1.setFileName(s + s + this.j);
        sir1.setData(data2);
		bir1.setImage(image2);
        
		// Getting transferables
        fir1Trans = (ImageResource_Transferable) fir1.getTransferable();
        sir1Trans = (ImageResource_Transferable) sir1.getTransferable();
        bir1Trans = (ImageResource_Transferable) bir1.getTransferable();
        ImageResource_Transferable[] trans2 = {fir1Trans, sir1Trans, bir1Trans};
        
        // Sending to server
		server.receiveImageResources(trans2, true, accessIdentifier_Transferable);
        	
		// Receiving from server
		testTrans = server.transmitImageResources(idTrans, 
        		accessIdentifier_Transferable);
                
        for (int i = 0; i < testTrans.length; i++) {
			ImageResource_Transferable imgTrans = testTrans[i];
			int sort = imgTrans.data.discriminator().value();
			if(sort == ImageResourceSort._FILE)
				testfir1 = new FileImageResource(testTrans[i]);
			else if(sort == ImageResourceSort._SCHEME)
				testsir1 = new SchemeImageResource(testTrans[i]);
			else if(sort == ImageResourceSort._BITMAP)
				testbir1 = new BitmapImageResource(testTrans[i]);
			else 
				assertFalse("wrong sort" + sort, true);			
		}
                
        // Comparing
        assertEquals(fir1.getCodename(), testfir1.getCodename());
        assertEquals(fir1.getId(), testfir1.getId());
        
        assertEquals(sir1.getId(), testsir1.getId());
        System.out.println("Checking data");
        for (int i = 0; i < sir1.getImage().length; i++) {
			System.out.print(sir1.getImage()[i]);
		}
        System.out.println("");
        for (int i = 0; i < testsir1.getImage().length; i++) {
			System.out.print(testsir1.getImage()[i]);
		}
        System.out.println("");
        
        assertEquals(bir1.getCodename(), testbir1.getCodename());
        assertEquals(bir1.getId(), testbir1.getId());
        System.out.println("Checking data");
        for (int i = 0; i < bir1.getImage().length; i++) {
			System.out.print(bir1.getImage()[i]);
		}
        System.out.println("");
        for (int i = 0; i < testbir1.getImage().length; i++) {
			System.out.print(testbir1.getImage()[i]);
		}
        // Third test. Deleting
        // Removing from server db
        server.deleteList(idTrans, accessIdentifier_Transferable);
		// Trying to load from server
		
		// Clearing 
		ImageResource_Transferable testfir1Trans = null;
		ImageResource_Transferable testsir1Trans = null;
		ImageResource_Transferable testbir1Trans = null;
		// if we are realy deleted entitys we must have here exceptions 
		try {
			testfir1Trans = server.transmitImageResource(
					(Identifier_Transferable)fir1.getId().getTransferable(), 
					accessIdentifier_Transferable);
		} catch (AMFICOMRemoteException e){
			// if we here we are right
			System.out.println("excecptions has been thrown");
		}
		try {
			testsir1Trans = server.transmitImageResource(
					(Identifier_Transferable)sir1.getId().getTransferable(), 
					accessIdentifier_Transferable);				
		} catch (AMFICOMRemoteException e){
			// if we here we are right
			System.out.println("excecptions has been thrown");
		}
		try {
			testbir1Trans = server.transmitImageResource(
					(Identifier_Transferable)bir1.getId().getTransferable(), 
					accessIdentifier_Transferable);		
		} catch (AMFICOMRemoteException e){
			// if we here we are right
			System.out.println("excecptions has been thrown");
		}
		assertFalse("transferables must be null", !(testfir1Trans == null) 
				&& !(testsir1Trans == null) 
				&& !(testbir1Trans == null));
    }
}
