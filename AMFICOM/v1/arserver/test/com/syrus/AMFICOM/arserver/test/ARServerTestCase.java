/*
 * $Id: ARServerTestCase.java,v 1.1 2005/01/21 06:56:10 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.arserver.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.resource.BitmapImageResource;
import com.syrus.AMFICOM.resource.FileImageResource;
import com.syrus.AMFICOM.resource.ResourceStorableObjectPool;
import com.syrus.AMFICOM.resource.SchemeImageResource;
import com.syrus.AMFICOM.resource.corba.ImageResource_Transferable;
import com.syrus.util.ClientLRUMap;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * @version $Revision: 1.1 $, $Date: 2005/01/21 06:56:10 $
 * @author $Author: max $
 * @module arserver_v1
 */
public class ARServerTestCase extends TestCase {

	private static ARServer							server;
	private static AccessIdentifier_Transferable	accessIdentifier_Transferable;   
    private static Identifier 						userId = new Identifier("Users_56");
    
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
			
			//accessIdentifier_Transferable.domain_id = server.reverseLookupDomainName("Системный домен");
			//accessIdentifier_Transferable.user_id = server.reverseLookupUserLogin("sys");
			accessIdentifier_Transferable.session_id = (Identifier_Transferable) id.getTransferable();
									
		} catch (UnknownHostException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		} catch (UserException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
    }
    
    static void oneTimeTearDown() {
        // empty;
	}
    
    public void _testRecieveImages() throws AMFICOMRemoteException, CreateObjectException {
        
        //      Checking recieveAnalysiss and transmitAnalysiss methods
    	byte[] data = {1,2,3};
    	    	
    	Identifier id1 = IdentifierPool.generateId(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE);
    	Identifier id2 = IdentifierPool.generateId(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE);
    	Identifier id3 = IdentifierPool.generateId(ObjectEntities.IMAGE_RESOURCE_ENTITY_CODE);
    	
    	Identifier_Transferable[] idTranss = {(Identifier_Transferable)id1.getTransferable(),
    			(Identifier_Transferable)id2.getTransferable(),
    			(Identifier_Transferable)id3.getTransferable()};
    	
    	FileImageResource	res1 = FileImageResource.createInstance(id1, "TestImajeRes.jpg");            
        SchemeImageResource res2 = SchemeImageResource.createInstance(id2);
        BitmapImageResource res3 = BitmapImageResource.createInstance(id3, "", data); 
        
        ImageResource_Transferable res1Trans = (ImageResource_Transferable)res1.getTransferable();
        ImageResource_Transferable res2Trans = (ImageResource_Transferable)res2.getTransferable();
        ImageResource_Transferable res3Trans = (ImageResource_Transferable)res3.getTransferable();
        ImageResource_Transferable[] imgTranss = {res1Trans, res2Trans, res3Trans};
        
        server.receiveImageResources(imgTranss, false, accessIdentifier_Transferable);
        
        ImageResource_Transferable[] imgTranss2 = server.transmitImageResources(idTranss, accessIdentifier_Transferable);
        
        System.out.println("number " + imgTranss2.length);          
    }
}
