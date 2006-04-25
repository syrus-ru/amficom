/*-
 * $Id: TestSerialization.java,v 1.2 2006/04/25 10:16:32 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * @version $Revision: 1.2 $, $Date: 2006/04/25 10:16:32 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module test
 */
public final class TestSerialization {
	//	 ***********************************************************************************
	public static void main(String[] args) throws Exception {
		final TestSerialization ts = new TestSerialization();
		ts.testInstanceObjest();
		ts.testClassObjest();
	}

	// ***********************************************************************************
	void testInstanceObjest() throws Exception {

		// Serialize output an Instance Object
		final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("t1.tmp"));
		out.writeObject(new MySerializabe());
		out.flush();

		// ATTENTION: Changed the static and transient static value after storage
		MySerializabe.si = 10000;
		MySerializabe.tsi = 100000;

		// Read back Serialized Instance Object
		final ObjectInputStream in = new ObjectInputStream(new FileInputStream("t1.tmp"));
		final MySerializabe mys = (MySerializabe) in.readObject();
		in.close();

		// .............................................................
		// Show the results
		System.out.println("Output from testInstanceObjest():\n");

		// instant values are serialized
		System.out.println("instance variable str          : " + mys.str);
		System.out.println("instance variable i            : " + mys.i);

		// static values are not serialized for an instant object!
		// The new value is picked up, not the old ones!
		System.out.println("static variable sstr           : " + MySerializabe.sstr);
		System.out.println("static variable si             : " + MySerializabe.si);

		// transient values are not serialized
		System.out.println("transient variable tstr        : " + mys.tstr);
		System.out.println("transient variable ti          : " + mys.ti);

		// transient static values are not serialized 
		// The new value is picked up, not the old ones!
		System.out.println("transient static variable tsstr: " + MySerializabe.tsstr);
		System.out.println("transient static variable tsi  : " + MySerializabe.tsi);
	}

	// ***********************************************************************************
	void testClassObjest() throws Exception {

		// Serialize output Class Object
		final Class c = Class.forName("com.syrus.util.MySerializabe");
		final ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("t2.tmp"));
		out.writeObject(c);
		out.flush();

		// ATTENTION: Changed the static and transient static value after storage
		MySerializabe.si = 11111;
		MySerializabe.tsi = 111111;

		// Read back Serialized Class Object
		final ObjectInputStream in = new ObjectInputStream(new FileInputStream("t2.tmp"));
		final Class cls = (Class) in.readObject();
		in.close();

		// .............................................................
		// Show the results
		System.out.println("\nOutput from testClassObjest():\n");

		// Only field with static modifier is legal to get value back
		// The new value is picked up, not the old ones!
		// which means they are both not serialized.
		// The conclusion: static and tansient static does not make any differece

		System.out.println("static variable sstr           : " + cls.getDeclaredField("sstr").get(cls));
		System.out.println("static variable si             : " + cls.getDeclaredField("si").getInt(cls));

		System.out.println("transient static variable tsstr: " + cls.getDeclaredField("tsstr").get(cls));
		System.out.println("transient static variable tsi  : " + cls.getDeclaredField("tsi").getInt(cls));

		// all other fields will cause exceptions
		// uncomment this code and try it out!
		// System.out.println(cls.getDeclaredField("i").get(cls));

		// .............................................................
		// Obviously the serialzed Class object know all fields in itself
		System.out.println("\n  Print some reflections from Class MySerializabe:");
		System.out.println("     " + cls);
		System.out.println("     " + cls.getDeclaredField("str"));
		System.out.println("     " + cls.getDeclaredField("tstr"));
		System.out.println("     " + cls.getDeclaredField("sstr"));
		System.out.println("     " + cls.getDeclaredField("tsstr"));
		System.out.println("     " + cls.getDeclaredFields());
	}
}

//	 ***********************************************************************************
class MySerializabe implements Serializable {
	private static final long serialVersionUID = 2474203340371331596L;

	String str = "STRING";
	static String sstr = "STATIC STRING";
	transient String tstr = "TRANSIENT STRING";
	transient static String tsstr = "TRANSIENT STATIC STRING";
	
	int i = 1;
	static int si = 10;
	transient int ti = 100;
	transient static int tsi = 1000;
}
