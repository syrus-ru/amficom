/*
 * $Id: SchemeSampleData.java,v 1.3 2005/04/15 18:04:08 bass Exp $
 *
 * Copyright ї 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.AMFICOM.scheme.corba.SchemeKind;
import com.syrus.AMFICOM.scheme.corba.PathElement_TransferablePackage.DataPackage.Kind;

/**
 * this class is used to create two sample instances
 * of Scheme whithout graphical components
 * @author $Author: bass $
 * @version $Revision: 1.3 $
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
		SchemeElement el0 = SchemeElement.createInstance(userId);
		el.add(el0);
		SchemeElement el1 = SchemeElement.createInstance(userId);
		el.add(el1);
		SchemeElement el2 = SchemeElement.createInstance(userId);
		el.add(el2);
		SchemeElement el3 = SchemeElement.createInstance(userId);
		el.add(el3);
		SchemeElement el4 = SchemeElement.createInstance(userId);
		el.add(el4);
		Set link = new HashSet();
		SchemeLink link0 = SchemeLink.createInstance(userId);
		link.add(link0);
		Set port0 = new HashSet();
		SchemePort port00 = SchemePort.createInstance(userId);
		port0.add(port00);
		SchemePort port01 = SchemePort.createInstance(userId);
		port0.add(port01);
		Set cport0 = new HashSet();
		SchemeCablePort cport00 = SchemeCablePort.createInstance(userId);
		cport0.add(cport00);
		Set cport1 = new HashSet();
		SchemeCablePort cport10 = SchemeCablePort.createInstance(userId);
		cport1.add(cport10);
		SchemeCablePort cport11 = SchemeCablePort.createInstance(userId);
		cport1.add(cport11);
		Set cport2 = new HashSet();
		SchemeCablePort cport20 = SchemeCablePort.createInstance(userId);
		cport2.add(cport20);
		SchemeCablePort cport21 = SchemeCablePort.createInstance(userId);
		cport2.add(cport21);
		SchemeCablePort cport22 = SchemeCablePort.createInstance(userId);
		cport2.add(cport22);
		Set cport3 = new HashSet();
		SchemeCablePort cport30 = SchemeCablePort.createInstance(userId);
		cport3.add(cport30);
		SchemeCablePort cport31 = SchemeCablePort.createInstance(userId);
		cport3.add(cport31);
		Set cport4 = new HashSet();
		SchemeCablePort cport40 = SchemeCablePort.createInstance(userId);
		cport4.add(cport40);
		SchemeCablePort cport41 = SchemeCablePort.createInstance(userId);
		cport4.add(cport41);
		Set clink = new HashSet();
		SchemeCableLink clink0 = SchemeCableLink.createInstance(userId);
		clink.add(clink0);
		SchemeCableLink clink1 = SchemeCableLink.createInstance(userId);
		clink.add(clink1);
		SchemeCableLink clink2 = SchemeCableLink.createInstance(userId);
		clink.add(clink2);
		SchemeCableLink clink3 = SchemeCableLink.createInstance(userId);
		clink.add(clink3);
		SchemeCableLink clink4 = SchemeCableLink.createInstance(userId);
		clink.add(clink4);
		Set dev0 = new HashSet();
		SchemeDevice dev00 = SchemeDevice.createInstance(userId);
		dev0.add(dev00);
		Set dev1 = new HashSet();
		SchemeDevice dev10 = SchemeDevice.createInstance(userId);
		dev1.add(dev10);
		Set dev2 = new HashSet();
		SchemeDevice dev20 = SchemeDevice.createInstance(userId);
		dev2.add(dev20);
		Set dev3 = new HashSet();
		SchemeDevice dev30 = SchemeDevice.createInstance(userId);
		dev3.add(dev30);
		Set dev4 = new HashSet();
		SchemeDevice dev40 = SchemeDevice.createInstance(userId);
		dev4.add(dev40);
		Set path = new HashSet();
		SchemePath path0 = SchemePath.createInstance(userId);
		path.add(path0);
		SortedSet pel = new TreeSet();
		PathElement pel0 = PathElement.createInstance(userId, Kind.SCHEME_LINK);
		pel.add(pel0);
		PathElement pel1 = PathElement.createInstance(userId, Kind.SCHEME_ELEMENT);
		pel.add(pel1);
		PathElement pel2 = PathElement.createInstance(userId, Kind.SCHEME_CABLE_LINK);
		pel.add(pel2);
		PathElement pel3 = PathElement.createInstance(userId, Kind.SCHEME_ELEMENT);
		pel.add(pel3);
		PathElement pel4 = PathElement.createInstance(userId, Kind.SCHEME_CABLE_LINK);
		pel.add(pel4);
		PathElement pel5 = PathElement.createInstance(userId, Kind.SCHEME_ELEMENT);
		pel.add(pel5);
		PathElement pel6 = PathElement.createInstance(userId, Kind.SCHEME_CABLE_LINK);
		pel.add(pel6);

		Scheme scheme = Scheme.createInstance(userId, domainId, "Схемка", "");
		dev00.setSchemePorts(port0);
		dev00.setSchemeCablePorts(cport0);
		dev00.setName("Девайс1");

		dev10.setSchemeCablePorts(cport1);
		dev10.setName("Девайс2");
		
		dev20.setSchemeCablePorts(cport2);
		dev20.setName("Девайс3");
		
		dev30.setSchemeCablePorts(cport3);
		dev30.setName("Девайс4");
		
		dev40.setSchemeCablePorts(cport4);
		dev40.setName("Девайс5");
		
//		port00.setSchemeDevice(dev00);
		port00.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._OUT);
		port00.setName("Порт раз");
//		port00.setSchemeLink(link0);

//		port01.setSchemeDevice(dev00);
		port01.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._IN);
		port01.setName("Порт два");
//		port01.setSchemeLink(link0);

//		cport00.setSchemeCableLink(clink0);
//		cport00.setSchemeDevice(dev00);
		cport00.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._IN);
		cport00.setName("Каборт раз");

//		cport10.setSchemeCableLink(clink0);
//		cport10.setSchemeDevice(dev10);
		cport10.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._OUT);
		cport10.setName("Каборт два");

//		cport11.setSchemeCableLink(clink1);
//		cport11.setSchemeDevice(dev10);
		cport11.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._OUT);
		cport11.setName("Каборт три");

//		cport20.setSchemeCableLink(clink1);
//		cport20.setSchemeDevice(dev20);
		cport20.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._OUT);
		cport20.setName("Каборт четыре");

//		cport21.setSchemeCableLink(clink2);
//		cport21.setSchemeDevice(dev20);
		cport21.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._OUT);
		cport21.setName("Каборт пять");

//		cport22.setSchemeCableLink(clink4);
//		cport22.setSchemeDevice(dev20);
		cport22.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._OUT);
		cport22.setName("Каборт шесть");

//		cport30.setSchemeCableLink(clink2);
//		cport30.setSchemeDevice(dev30);
		cport30.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._OUT);
		cport30.setName("Каборт сем");

//		cport31.setSchemeCableLink(clink3);
//		cport31.setSchemeDevice(dev30);
		cport31.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._OUT);
		cport31.setName("Каборт восем");

//		cport40.setSchemeCableLink(clink3);
//		cport40.setSchemeDevice(dev40);
		cport40.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._OUT);
		cport40.setName("Каборт дэвят");

//		cport41.setSchemeCableLink(clink4);
//		cport41.setSchemeDevice(dev40);
		cport41.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._OUT);
		cport41.setName("Каборт дэсят");

		el0.setDescription("Описалово");
		el0.setSchemeDevices(dev0);
		el0.setName("Один элемент");
		el0.setScheme(scheme);

		el1.setDescription("Описалово");
		el1.setSchemeDevices(dev1);
		el1.setName("Другой элемент");
		el1.setScheme(scheme);

		el2.setDescription("Описалово");
		el2.setSchemeDevices(dev2);
		el2.setName("Третий элемент");
		el2.setScheme(scheme);

		el3.setDescription("Описалово");
		el3.setSchemeDevices(dev3);
		el3.setName("Опять элемент");
		el3.setScheme(scheme);

		el4.setDescription("Описалово");
		el4.setSchemeDevices(dev4);
		el4.setName("Совсем элемент");
		el4.setScheme(scheme);

		link0.setName("Патчкорд");
		link0.setOpticalLength(3.0D);
		link0.setPhysicalLength(3.0D);
		link0.setSourceSchemePort(port01);
		link0.setTargetSchemePort(port00);
//		link0.setScheme(scheme);

		clink0.setName("Кабелёк1");
		clink0.setOpticalLength(1000.0D);
		clink0.setPhysicalLength(1000.0D);
		clink0.setSourceSchemeCablePort(cport10);
		clink0.setTargetSchemeCablePort(cport00);
//		clink0.setScheme(scheme);

		clink1.setName("Кабелёк2");
		clink1.setOpticalLength(1000.0D);
		clink1.setPhysicalLength(1000.0D);
		clink1.setSourceSchemeCablePort(cport11);
		clink1.setTargetSchemeCablePort(cport20);
//		clink1.setScheme(scheme);

		clink2.setName("Кабелёк3");
		clink2.setOpticalLength(1000.0D);
		clink2.setPhysicalLength(1000.0D);
		clink2.setSourceSchemeCablePort(cport21);
		clink2.setTargetSchemeCablePort(cport30);
//		clink2.setScheme(scheme);

		clink3.setName("Кабелёк4");
		clink3.setOpticalLength(1000.0D);
		clink3.setPhysicalLength(1000.0D);
		clink3.setSourceSchemeCablePort(cport31);
		clink3.setTargetSchemeCablePort(cport40);
//		clink3.setScheme(scheme);

		clink4.setName("Кабелёк5");
		clink4.setOpticalLength(1000.0D);
		clink4.setPhysicalLength(1000.0D);
		clink4.setSourceSchemeCablePort(cport41);
		clink4.setTargetSchemeCablePort(cport22);
//		clink4.setScheme(scheme);

		pel0.setStartAbstractSchemePort(port01);
		pel0.setEndAbstractSchemePort(port00);
//		pel0.setScheme(scheme);
		pel0.setAbstractSchemeElement(link0);
		SchemeUtils.setOpticalLength(pel0, 3.0);
		SchemeUtils.setPhysicalLength(pel0, 3.0);
		
//		pel1.setScheme(scheme);
		pel1.setAbstractSchemeElement(el0);
		
		pel2.setStartAbstractSchemePort(cport00);
		pel2.setEndAbstractSchemePort(cport10);
//		pel2.setScheme(scheme);
		pel2.setAbstractSchemeElement(clink0);
		SchemeUtils.setOpticalLength(pel2, 1000.0);
		SchemeUtils.setPhysicalLength(pel2, 1000.0);
		
//		pel3.setScheme(scheme);
		pel3.setAbstractSchemeElement(el1);
		
		pel4.setStartAbstractSchemePort(cport11);
		pel4.setEndAbstractSchemePort(cport20);
//		pel4.setScheme(scheme);
		pel4.setAbstractSchemeElement(clink1);
		SchemeUtils.setOpticalLength(pel4, 1000.0);
		SchemeUtils.setPhysicalLength(pel4, 1000.0);
		
//		pel5.setScheme(scheme);
		pel5.setAbstractSchemeElement(el2);
		
		pel6.setStartAbstractSchemePort(cport21);
		pel6.setEndAbstractSchemePort(cport30);
//		pel6.setScheme(scheme);
		pel6.setAbstractSchemeElement(clink2);
		SchemeUtils.setOpticalLength(pel6, 1000.0);
		SchemeUtils.setPhysicalLength(pel6, 1000.0);
		
		path0.setStartSchemeElement(el0);
		path0.setEndSchemeElement(el3);
		path0.setPathElements(pel);
		path0.setName("Путяра измерений");
		path0.setScheme(scheme);

		scheme.setSchemeCableLinks(clink);
		scheme.setSchemeLinks(link);
		scheme.getCurrentSchemeMonitoringSolution().setSchemePaths(path);
		scheme.setSchemeElements(el);
		scheme.setHeight(100);
		scheme.setLabel("lab1");
		scheme.setSchemeKind(SchemeKind.NETWORK);
		scheme.setWidth(100);
		scheme.setDomainId(domainId);

		SchemeStorableObjectPool.putStorableObject(scheme);
	}

	private static void scheme2(Identifier userId, Identifier domainId) throws CreateObjectException, IllegalObjectEntityException
	{
		Scheme scheme = Scheme.createInstance(userId, domainId, "Схемочка", "");
		Set el = new HashSet();
		SchemeElement el0 = SchemeElement.createInstance(userId);
		el.add(el0);
		SchemeElement el1 = SchemeElement.createInstance(userId);
		el.add(el1);
		Set link = new HashSet();
		SchemeLink link0 = SchemeLink.createInstance(userId);
		link.add(link0);
		Set port0 = new HashSet();
		SchemePort port00 = SchemePort.createInstance(userId);
		port0.add(port00);
		Set port1 = new HashSet();
		SchemePort port10 = SchemePort.createInstance(userId);
		port1.add(port10);
		Set cport0 = new HashSet();
		SchemeCablePort cport00 = SchemeCablePort.createInstance(userId);
		cport0.add(cport00);
		Set cport1 = new HashSet();
		SchemeCablePort cport10 = SchemeCablePort.createInstance(userId);
		cport1.add(cport10);
		Set clink = new HashSet();
		SchemeCableLink clink0 = SchemeCableLink.createInstance(userId);
		clink.add(clink0);
		Set dev0 = new HashSet();
		SchemeDevice dev00 = SchemeDevice.createInstance(userId);
		dev0.add(dev00);
		Set dev1 = new HashSet();
		SchemeDevice dev10 = SchemeDevice.createInstance(userId);
		dev1.add(dev10);
		Set path = new HashSet();
		SchemePath path0 = SchemePath.createInstance(userId);
		path.add(path0);
		SortedSet pel = new TreeSet();
		PathElement pel0 = PathElement.createInstance(userId, Kind.SCHEME_LINK);
		pel.add(pel0);
		PathElement pel1 = PathElement.createInstance(userId, Kind.SCHEME_ELEMENT);
		pel.add(pel1);
		PathElement pel2 = PathElement.createInstance(userId, Kind.SCHEME_CABLE_LINK);
		pel.add(pel2);

//		port00.setSchemeDevice(dev00);
		port00.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._OUT);
		port00.setName("Порт раз");
//		port00.setSchemeLink(link0);

//		port10.setSchemeDevice(dev10);
		port10.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._IN);
		port10.setName("Порт два");
//		port10.setSchemeLink(link0);

//		cport00.setSchemeCableLink(clink0);
//		cport00.setSchemeDevice(dev00);
		cport00.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._IN);
		cport00.setName("Каборт раз");

//		cport10.setSchemeCableLink(clink0);
//		cport10.setSchemeDevice(dev10);
		cport10.setAbstractSchemePortDirectionType(AbstractSchemePortDirectionType._OUT);
		cport10.setName("Каборт два");

		dev00.setSchemePorts(port0);
		dev00.setSchemeCablePorts(cport0);
		dev00.setName("Девайс1");

		dev10.setSchemePorts(port1);
		dev10.setSchemeCablePorts(cport1);
		dev10.setName("Девайс2");
		
		el0.setDescription("Описалово");
		el0.setSchemeDevices(dev0);
		el0.setName("Один элемент");
		el0.setScheme(scheme);

		el1.setDescription("Описалово");
		el1.setSchemeDevices(dev1);
		el1.setName("Другой элемент");
		el1.setScheme(scheme);

		link0.setName("Патчкорд");
		link0.setOpticalLength(1000.0D);
		link0.setPhysicalLength(1000.0D);
		link0.setSourceSchemePort(port10);
		link0.setTargetSchemePort(port00);
//		link0.setScheme(scheme);

		clink0.setName("Кабелёк");
		clink0.setOpticalLength(1000.0D);
		clink0.setPhysicalLength(1000.0D);
		clink0.setSourceSchemeCablePort(cport10);
		clink0.setTargetSchemeCablePort(cport00);
//		clink0.setScheme(scheme);

		pel0.setEndAbstractSchemePort(port10);
//		pel0.setScheme(scheme);
		pel0.setAbstractSchemeElement(link0);
		pel0.setStartAbstractSchemePort(port00);
		
//		pel1.setScheme(scheme);
		pel1.setAbstractSchemeElement(el1);
		
		pel2.setEndAbstractSchemePort(cport00);
//		pel2.setScheme(scheme);
		pel2.setAbstractSchemeElement(clink0);
		pel2.setStartAbstractSchemePort(cport10);
		
		path0.setEndSchemeElement(el0);
		path0.setPathElements(pel);
		path0.setName("Путяра измерений");
		path0.setStartSchemeElement(el1);
		path0.setScheme(scheme);

		scheme.setSchemeCableLinks(clink);
		scheme.setSchemeLinks(link);
		scheme.getCurrentSchemeMonitoringSolution().setSchemePaths(path);
		scheme.setSchemeElements(el);
		scheme.setHeight(100);
		scheme.setLabel("lab1");
		scheme.setSchemeKind(SchemeKind.NETWORK);
		scheme.setWidth(100);
		scheme.setDomainId(domainId);

		SchemeStorableObjectPool.putStorableObject(scheme);
	}
}
