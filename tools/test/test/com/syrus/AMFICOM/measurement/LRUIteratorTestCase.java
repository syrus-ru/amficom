/*
 * $Id: LRUIteratorTestCase.java,v 1.1 2004/11/11 12:15:57 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package test.com.syrus.AMFICOM.measurement;

import java.util.Iterator;

import junit.framework.TestCase;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.util.ClientLRUMap;
import com.syrus.util.LRUMap;


/**
 * @version $Revision: 1.1 $, $Date: 2004/11/11 12:15:57 $
 * @author $Author: bob $
 * @module module
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
		ClientLRUMap map = new ClientLRUMap();
		dosmth(map);		
	}
	
	private void dosmth(LRUMap map){		
		Identifier id = new Identifier("ParameterType_1");
		Identifier creatorId = new Identifier("User_1");
		ParameterType parameterType = ParameterType.createInstance(id , creatorId, "test", "parameterType", "name");
		map.put(parameterType.getId(), parameterType);
		
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
