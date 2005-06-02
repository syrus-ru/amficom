/*
 * $Id: LRUIteratorTestCase.java,v 1.2 2005/06/02 14:31:02 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.util.Iterator;

import junit.framework.TestCase;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ParameterType;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectResizableLRUMap;
import com.syrus.AMFICOM.general.corba.DataType;
import com.syrus.util.LRUMap;


/**
 * @version $Revision: 1.2 $, $Date: 2005/06/02 14:31:02 $
 * @author $Author: arseniy $
 * @module tools/test
 */
public class LRUIteratorTestCase extends TestCase {

	public static void main(String[] args) {
		junit.textui.TestRunner.run(LRUIteratorTestCase.class);
	}

	public void testLRUMapItarator(){
		System.out.println("testLRUMapItarator | ");
		LRUMap map = new LRUMap();
		dosmth(map);
	}
	
	public void testClientLRUMapItarator(){
		System.out.println("testClientLRUMapItarator | ");
		StorableObjectResizableLRUMap map = new StorableObjectResizableLRUMap();
		dosmth(map);		
	}
	
	private void dosmth(LRUMap map){		
		Identifier id = new Identifier("ParameterType_1");
		Identifier creatorId = new Identifier("User_1");
		try {
			ParameterType parameterType = ParameterType.createInstance(creatorId, "test", "parameterType", "name", DataType.DATA_TYPE_BOOLEAN);
			map.put(parameterType.getId(), parameterType);
		}
		catch (CreateObjectException e) {
			e.printStackTrace();
		}
		
		for (Iterator it = map.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			System.out.println(storableObject.getId());
			
		}	
	}
	
	
	protected void setUp() throws Exception {
		System.out.println("Next test begin...");
		super.setUp();
	}
	
	protected void tearDown() throws Exception {
		System.out.println("Next test end...");
		System.out.println();
		super.tearDown();
	}
}
