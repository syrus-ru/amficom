/*
 * $Id: SchemeSampleData.java,v 1.4 2005/04/26 16:29:48 krupenn Exp $
 *
 * Copyright ї 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.AMFICOM.scheme.corba.Scheme_TransferablePackage.Kind;

/**
 * this class is used to create two sample instances
 * of Scheme whithout graphical components
 * @author $Author: krupenn $
 * @version $Revision: 1.4 $
 * @module generalclient_v1
 */
public final class SchemeSampleData {

	private static boolean loaded = false;
	
	private SchemeSampleData() {
		// empty
	}

	public static void populate(Identifier userId, Identifier domainId) throws DatabaseException, IllegalObjectEntityException {
		if(!loaded)
		{
			scheme1(userId, domainId);
			scheme2(userId, domainId);
			loaded = true;
		}
	}

	private static void scheme1(Identifier userId, Identifier domainId) throws CreateObjectException, IllegalObjectEntityException
	{
		Set el = new HashSet();
		Set link = new HashSet();
		Set port0 = new HashSet();
		Set cport0 = new HashSet();
		Set cport1 = new HashSet();
		Set cport2 = new HashSet();
		Set cport3 = new HashSet();
		Set cport4 = new HashSet();
		Set clink = new HashSet();
		Set dev0 = new HashSet();
		Set dev1 = new HashSet();
		Set dev2 = new HashSet();
		Set dev3 = new HashSet();
		Set dev4 = new HashSet();
		Set path = new HashSet();
		SortedSet pel = new TreeSet();

		Scheme scheme = Scheme.createInstance(userId, "Схемка", Kind.NETWORK, domainId);

		SchemeDevice dev00 = SchemeDevice.createInstance(userId, "Девайс1");
		dev0.add(dev00);

		SchemeDevice dev10 = SchemeDevice.createInstance(userId, "Девайс2");
		dev1.add(dev10);
		
		SchemeDevice dev20 = SchemeDevice.createInstance(userId, "Девайс3");
		dev2.add(dev20);
		
		SchemeDevice dev30 = SchemeDevice.createInstance(userId, "Девайс4");
		dev3.add(dev30);
		
		SchemeDevice dev40 = SchemeDevice.createInstance(userId, "Девайс5");
		dev4.add(dev40);
		
		SchemePort port00 = SchemePort.createInstance(userId, "Порт раз", AbstractSchemePortDirectionType._OUT, dev00);
//		port00.setSchemeLink(link0);

		SchemePort port01 = SchemePort.createInstance(userId, "Порт два", AbstractSchemePortDirectionType._IN, dev00);
//		port01.setSchemeLink(link0);

		port0.add(port00);
		port0.add(port01);

//		cport00.setSchemeCableLink(clink0);
		SchemeCablePort cport00 = SchemeCablePort.createInstance(userId, "Каборт раз", AbstractSchemePortDirectionType._IN, dev00);
		cport0.add(cport00);

//		cport10.setSchemeCableLink(clink0);
		SchemeCablePort cport10 = SchemeCablePort.createInstance(userId, "Каборт два", AbstractSchemePortDirectionType._OUT, dev10);

//		cport11.setSchemeCableLink(clink1);
		SchemeCablePort cport11 = SchemeCablePort.createInstance(userId, "Каборт три", AbstractSchemePortDirectionType._OUT, dev10);

		cport1.add(cport10);
		cport1.add(cport11);

//		cport20.setSchemeCableLink(clink1);
		SchemeCablePort cport20 = SchemeCablePort.createInstance(userId, "Каборт четыре", AbstractSchemePortDirectionType._OUT, dev20);

//		cport21.setSchemeCableLink(clink2);
		SchemeCablePort cport21 = SchemeCablePort.createInstance(userId, "Каборт пять", AbstractSchemePortDirectionType._OUT, dev20);

//		cport22.setSchemeCableLink(clink4);
		SchemeCablePort cport22 = SchemeCablePort.createInstance(userId, "Каборт шесть", AbstractSchemePortDirectionType._OUT, dev20);

		cport2.add(cport20);
		cport2.add(cport21);
		cport2.add(cport22);

//		cport30.setSchemeCableLink(clink2);
		SchemeCablePort cport30 = SchemeCablePort.createInstance(userId, "Каборт сем", AbstractSchemePortDirectionType._OUT, dev30);

//		cport31.setSchemeCableLink(clink3);
		SchemeCablePort cport31 = SchemeCablePort.createInstance(userId, "Каборт восем", AbstractSchemePortDirectionType._OUT, dev30);

		cport3.add(cport30);
		cport3.add(cport31);

//		cport40.setSchemeCableLink(clink3);
		SchemeCablePort cport40 = SchemeCablePort.createInstance(userId, "Каборт дэвят", AbstractSchemePortDirectionType._OUT, dev40);

//		cport41.setSchemeCableLink(clink4);
		SchemeCablePort cport41 = SchemeCablePort.createInstance(userId, "Каборт дэсят", AbstractSchemePortDirectionType._OUT, dev40);

		cport4.add(cport40);
		cport4.add(cport41);

		dev00.setSchemePorts(port0);
		dev00.setSchemeCablePorts(cport0);
		dev10.setSchemeCablePorts(cport1);
		dev20.setSchemeCablePorts(cport2);
		dev30.setSchemeCablePorts(cport3);
		dev40.setSchemeCablePorts(cport4);

		SchemeElement el0 = SchemeElement.createInstance(userId, "Один элемент", scheme);
		el0.setDescription("Описалово");
		el0.setSchemeDevices(dev0);

		SchemeElement el1 = SchemeElement.createInstance(userId, "Другой элемент", scheme);
		el1.setDescription("Описалово");
		el1.setSchemeDevices(dev1);

		SchemeElement el2 = SchemeElement.createInstance(userId, "Третий элемент", scheme);
		el2.setDescription("Описалово");
		el2.setSchemeDevices(dev2);

		SchemeElement el3 = SchemeElement.createInstance(userId, "Опять элемент", scheme);
		el3.setDescription("Описалово");
		el3.setSchemeDevices(dev3);

		SchemeElement el4 = SchemeElement.createInstance(userId, "Совсем элемент", scheme);
		el4.setDescription("Описалово");
		el4.setSchemeDevices(dev4);

		el.add(el0);
		el.add(el1);
		el.add(el2);
		el.add(el3);
		el.add(el4);

		SchemeLink link0 = SchemeLink.createInstance(userId, "Патчкорд", scheme);
		link0.setOpticalLength(3.0D);
		link0.setPhysicalLength(3.0D);
		link0.setSourceSchemePort(port01);
		link0.setTargetSchemePort(port00);

		link.add(link0);

		SchemeCableLink clink0 = SchemeCableLink.createInstance(userId, "Кабелёк1", scheme);
		clink0.setOpticalLength(1000.0D);
		clink0.setPhysicalLength(1000.0D);
		clink0.setSourceSchemeCablePort(cport10);
		clink0.setTargetSchemeCablePort(cport00);
		SchemeCableThread clink0th = SchemeCableThread.createInstance(userId, "1", "", null, null, null, null, clink0);
		clink0.setSchemeCableThreads(Collections.singleton(clink0th));

		SchemeCableLink clink1 = SchemeCableLink.createInstance(userId, "Кабелёк2", scheme);
		clink1.setOpticalLength(1000.0D);
		clink1.setPhysicalLength(1000.0D);
		clink1.setSourceSchemeCablePort(cport11);
		clink1.setTargetSchemeCablePort(cport20);
		SchemeCableThread clink1th = SchemeCableThread.createInstance(userId, "1", "", null, null, null, null, clink1);
		clink1.setSchemeCableThreads(Collections.singleton(clink1th));

		SchemeCableLink clink2 = SchemeCableLink.createInstance(userId, "Кабелёк3", scheme);
		clink2.setOpticalLength(1000.0D);
		clink2.setPhysicalLength(1000.0D);
		clink2.setSourceSchemeCablePort(cport21);
		clink2.setTargetSchemeCablePort(cport30);
		SchemeCableThread clink2th = SchemeCableThread.createInstance(userId, "1", "", null, null, null, null, clink2);
		clink2.setSchemeCableThreads(Collections.singleton(clink2th));

		SchemeCableLink clink3 = SchemeCableLink.createInstance(userId, "Кабелёк4", scheme);
		clink3.setOpticalLength(1000.0D);
		clink3.setPhysicalLength(1000.0D);
		clink3.setSourceSchemeCablePort(cport31);
		clink3.setTargetSchemeCablePort(cport40);
		SchemeCableThread clink3th = SchemeCableThread.createInstance(userId, "1", "", null, null, null, null, clink3);
		clink3.setSchemeCableThreads(Collections.singleton(clink3th));

		SchemeCableLink clink4 = SchemeCableLink.createInstance(userId, "Кабелёк5", scheme);
		clink4.setOpticalLength(1000.0D);
		clink4.setPhysicalLength(1000.0D);
		clink4.setSourceSchemeCablePort(cport41);
		clink4.setTargetSchemeCablePort(cport22);
		SchemeCableThread clink4th = SchemeCableThread.createInstance(userId, "1", "", null, null, null, null, clink4);
		clink4.setSchemeCableThreads(Collections.singleton(clink4th));

		clink.add(clink0);
		clink.add(clink1);
		clink.add(clink2);
		clink.add(clink3);
		clink.add(clink4);

		SchemePath path0 = SchemePath.createInstance(userId, "Путяра измерений", "", null, el0, el3, null);
		path0.setScheme(scheme);

		path.add(path0);

		PathElement pel0 = PathElement.createInstance(userId, path0, link0);
		pel0.setStartAbstractSchemePort(port01);
		pel0.setEndAbstractSchemePort(port00);
		pel0.setAbstractSchemeElement(link0);
		SchemeUtils.setOpticalLength(pel0, 3.0);
		SchemeUtils.setPhysicalLength(pel0, 3.0);
		
		PathElement pel1 = PathElement.createInstance(userId, path0, port00, cport00);
		pel1.setAbstractSchemeElement(el0);
		
		PathElement pel2 = PathElement.createInstance(userId, path0, clink0th);
		pel2.setStartAbstractSchemePort(cport00);
		pel2.setEndAbstractSchemePort(cport10);
		pel2.setAbstractSchemeElement(clink0);
		SchemeUtils.setOpticalLength(pel2, 1000.0);
		SchemeUtils.setPhysicalLength(pel2, 1000.0);
		
		PathElement pel3 = PathElement.createInstance(userId, path0, cport10, cport11);
		pel3.setAbstractSchemeElement(el1);
		
		PathElement pel4 = PathElement.createInstance(userId, path0, clink1th);
		pel4.setStartAbstractSchemePort(cport11);
		pel4.setEndAbstractSchemePort(cport20);
		pel4.setAbstractSchemeElement(clink1);
		SchemeUtils.setOpticalLength(pel4, 1000.0);
		SchemeUtils.setPhysicalLength(pel4, 1000.0);
		
		PathElement pel5 = PathElement.createInstance(userId, path0, cport20, cport21);
		pel5.setAbstractSchemeElement(el2);
		
		PathElement pel6 = PathElement.createInstance(userId, path0, clink2th);
		pel6.setStartAbstractSchemePort(cport21);
		pel6.setEndAbstractSchemePort(cport30);
		pel6.setAbstractSchemeElement(clink2);
		SchemeUtils.setOpticalLength(pel6, 1000.0);
		SchemeUtils.setPhysicalLength(pel6, 1000.0);

		pel.add(pel0);
		pel.add(pel1);
		pel.add(pel2);
		pel.add(pel3);
		pel.add(pel4);
		pel.add(pel5);
		pel.add(pel6);

		path0.setPathElements(pel);

		scheme.setSchemeCableLinks(clink);
		scheme.setSchemeLinks(link);
		scheme.getCurrentSchemeMonitoringSolution().setSchemePaths(path);
		scheme.setSchemeElements(el);
		scheme.setHeight(100);
		scheme.setLabel("lab1");
		scheme.setWidth(100);
		scheme.setDomainId(domainId);

		SchemeStorableObjectPool.putStorableObject(scheme);
	}

	private static void scheme2(Identifier userId, Identifier domainId) throws CreateObjectException, IllegalObjectEntityException
	{
		Set el = new HashSet();
		Set link = new HashSet();
		Set port0 = new HashSet();
		Set port1 = new HashSet();
		Set cport0 = new HashSet();
		Set cport1 = new HashSet();
		Set clink = new HashSet();
		Set dev0 = new HashSet();
		Set dev1 = new HashSet();
		Set path = new HashSet();
		SortedSet pel = new TreeSet();

		Scheme scheme = Scheme.createInstance(userId, "Схемочка", Kind.NETWORK, domainId);

		SchemeDevice dev00 = SchemeDevice.createInstance(userId, "Девайс1");
		dev0.add(dev00);

		SchemeDevice dev10 = SchemeDevice.createInstance(userId, "Девайс2");
		dev1.add(dev10);

//		port00.setSchemeLink(link0);
		SchemePort port00 = SchemePort.createInstance(userId, "Порт раз", AbstractSchemePortDirectionType._OUT, dev00);

		port0.add(port00);

//		port10.setSchemeLink(link0);
		SchemePort port10 = SchemePort.createInstance(userId, "Порт два", AbstractSchemePortDirectionType._IN, dev10);

		port1.add(port10);

//		cport00.setSchemeCableLink(clink0);
		SchemeCablePort cport00 = SchemeCablePort.createInstance(userId, "Каборт раз", AbstractSchemePortDirectionType._IN, dev00);
		cport0.add(cport00);

//		cport10.setSchemeCableLink(clink0);
		SchemeCablePort cport10 = SchemeCablePort.createInstance(userId, "Каборт два", AbstractSchemePortDirectionType._OUT, dev10);
		cport1.add(cport10);

		dev00.setSchemePorts(port0);
		dev00.setSchemeCablePorts(cport0);
		dev10.setSchemePorts(port1);
		dev10.setSchemeCablePorts(cport1);
		
		SchemeElement el0 = SchemeElement.createInstance(userId, "Один элемент", scheme);
		el0.setDescription("Описалово");
		el0.setSchemeDevices(dev0);

		SchemeElement el1 = SchemeElement.createInstance(userId, "Другой элемент", scheme);
		el1.setDescription("Описалово");
		el1.setSchemeDevices(dev1);

		el.add(el0);
		el.add(el1);

		SchemeLink link0 = SchemeLink.createInstance(userId, "Патчкорд");
		link.add(link0);
		link0.setOpticalLength(1000.0D);
		link0.setPhysicalLength(1000.0D);
		link0.setSourceSchemePort(port10);
		link0.setTargetSchemePort(port00);

		SchemeCableLink clink0 = SchemeCableLink.createInstance(userId, "Кабелёк", scheme);
		clink.add(clink0);
		clink0.setOpticalLength(1000.0D);
		clink0.setPhysicalLength(1000.0D);
		clink0.setSourceSchemeCablePort(cport10);
		clink0.setTargetSchemeCablePort(cport00);
		SchemeCableThread clink0th = SchemeCableThread.createInstance(userId, "1", "", null, null, null, null, clink0);
		clink0.setSchemeCableThreads(Collections.singleton(clink0th));

		SchemePath path0 = SchemePath.createInstance(userId, "Путяра измерений", "", null, el1, el0, null);
		path0.setScheme(scheme);

		PathElement pel0 = PathElement.createInstance(userId, path0, link0);
		pel0.setStartAbstractSchemePort(port00);
		pel0.setEndAbstractSchemePort(port10);
		pel0.setAbstractSchemeElement(link0);
		
		PathElement pel1 = PathElement.createInstance(userId, path0, port10, cport10);
		pel1.setAbstractSchemeElement(el1);
		
		PathElement pel2 = PathElement.createInstance(userId, path0, clink0th);
		pel2.setStartAbstractSchemePort(cport10);
		pel2.setEndAbstractSchemePort(cport00);
		pel2.setAbstractSchemeElement(clink0);

		pel.add(pel0);
		pel.add(pel1);
		pel.add(pel2);

		path0.setPathElements(pel);

		path.add(path0);

		scheme.setSchemeCableLinks(clink);
		scheme.setSchemeLinks(link);
		scheme.getCurrentSchemeMonitoringSolution().setSchemePaths(path);
		scheme.setSchemeElements(el);
		scheme.setHeight(100);
		scheme.setLabel("lab1");
		scheme.setWidth(100);
		scheme.setDomainId(domainId);

		SchemeStorableObjectPool.putStorableObject(scheme);
	}
}
