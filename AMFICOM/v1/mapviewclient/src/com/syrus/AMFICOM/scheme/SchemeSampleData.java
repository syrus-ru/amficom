/*
 * $Id: SchemeSampleData.java,v 1.19 2005/06/24 14:13:39 bass Exp $
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

import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.DirectionType;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.Kind;

/**
 * this class is used to create two sample instances
 * of Scheme whithout graphical components
 * @author $Author: bass $
 * @version $Revision: 1.19 $
 * @module generalclient_v1
 */
public final class SchemeSampleData {

	private static boolean loaded = false;
	
	private SchemeSampleData() {
		// empty
	}

	public static void populate(Identifier userId, Identifier domainId) throws ApplicationException {
		if(!loaded)
		{
			scheme1(userId, domainId);
			scheme2(userId, domainId);
			loaded = true;
		}
	}

	private static void scheme1(Identifier userId, Identifier domainId) throws ApplicationException
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

		SchemeElement el0 = SchemeElement.createInstance(userId, "Один элемент", scheme);
		el0.setDescription("Описалово");

		SchemeElement el1 = SchemeElement.createInstance(userId, "Другой элемент", scheme);
		el1.setDescription("Описалово");

		SchemeElement el2 = SchemeElement.createInstance(userId, "Третий элемент", scheme);
		el2.setDescription("Описалово");

		SchemeElement el3 = SchemeElement.createInstance(userId, "Опять элемент", scheme);
		el3.setDescription("Описалово");

		SchemeElement el4 = SchemeElement.createInstance(userId, "Совсем элемент", scheme);
		el4.setDescription("Описалово");

		SchemeDevice dev00 = SchemeDevice.createInstance(userId, "Девайс1", el0);
		dev0.add(dev00);

		SchemeDevice dev10 = SchemeDevice.createInstance(userId, "Девайс2", el1);
		dev1.add(dev10);
		
		SchemeDevice dev20 = SchemeDevice.createInstance(userId, "Девайс3", el2);
		dev2.add(dev20);
		
		SchemeDevice dev30 = SchemeDevice.createInstance(userId, "Девайс4", el3);
		dev3.add(dev30);
		
		SchemeDevice dev40 = SchemeDevice.createInstance(userId, "Девайс5", el4);
		dev4.add(dev40);
		
		SchemePort port00 = SchemePort.createInstance(userId, "Порт раз", DirectionType._OUT, dev00);

		SchemePort port01 = SchemePort.createInstance(userId, "Порт два", DirectionType._IN, dev00);

		port0.add(port00);
		port0.add(port01);

		SchemeCablePort cport00 = SchemeCablePort.createInstance(userId, "Каборт раз", DirectionType._IN, dev00);
		cport0.add(cport00);

		SchemeCablePort cport10 = SchemeCablePort.createInstance(userId, "Каборт два", DirectionType._OUT, dev10);

		SchemeCablePort cport11 = SchemeCablePort.createInstance(userId, "Каборт три", DirectionType._OUT, dev10);

		cport1.add(cport10);
		cport1.add(cport11);

		SchemeCablePort cport20 = SchemeCablePort.createInstance(userId, "Каборт четыре", DirectionType._OUT, dev20);

		SchemeCablePort cport21 = SchemeCablePort.createInstance(userId, "Каборт пять", DirectionType._OUT, dev20);

		SchemeCablePort cport22 = SchemeCablePort.createInstance(userId, "Каборт шесть", DirectionType._OUT, dev20);

		cport2.add(cport20);
		cport2.add(cport21);
		cport2.add(cport22);

		SchemeCablePort cport30 = SchemeCablePort.createInstance(userId, "Каборт сем", DirectionType._OUT, dev30);

		SchemeCablePort cport31 = SchemeCablePort.createInstance(userId, "Каборт восем", DirectionType._OUT, dev30);

		cport3.add(cport30);
		cport3.add(cport31);

		SchemeCablePort cport40 = SchemeCablePort.createInstance(userId, "Каборт дэвят", DirectionType._OUT, dev40);

		SchemeCablePort cport41 = SchemeCablePort.createInstance(userId, "Каборт дэсят", DirectionType._OUT, dev40);

		cport4.add(cport40);
		cport4.add(cport41);

		el.add(el0);
		el.add(el1);
		el.add(el2);
		el.add(el3);
		el.add(el4);

		SchemeLink link0 = SchemeLink.createInstance(userId, "Патчкорд", scheme);
		link0.setOpticalLength(3.0D);
		link0.setPhysicalLength(3.0D);
		link0.setSourceAbstractSchemePort(port01);
		link0.setTargetAbstractSchemePort(port00);

		link.add(link0);

		LinkType lt = LinkType.createInstance(userId, "1", "2", "3", LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "8", "9", IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGERESOURCE_CODE));
		CableLinkType clt = CableLinkType.createInstance(userId, "4", "5", "6", LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "8", "7", IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGERESOURCE_CODE));
		CableThreadType ctt = CableThreadType.createInstance(userId, "test", "7", "CTT", 0, lt, clt); 
		
		SchemeCableLink clink0 = SchemeCableLink.createInstance(userId, "Кабелёк1", scheme);
		clink0.setOpticalLength(1000.0D);
		clink0.setPhysicalLength(1000.0D);
		clink0.setSourceAbstractSchemePort(cport10);
		clink0.setTargetAbstractSchemePort(cport00);
		SchemeCableThread clink0th = SchemeCableThread.createInstance(userId, "1", "", ctt, null, null, null, clink0);

		SchemeCableLink clink1 = SchemeCableLink.createInstance(userId, "Кабелёк2", scheme);
		clink1.setOpticalLength(1000.0D);
		clink1.setPhysicalLength(1000.0D);
		clink1.setSourceAbstractSchemePort(cport11);
		clink1.setTargetAbstractSchemePort(cport20);
		SchemeCableThread clink1th = SchemeCableThread.createInstance(userId, "1", "", ctt, null, null, null, clink1);

		SchemeCableLink clink2 = SchemeCableLink.createInstance(userId, "Кабелёк3", scheme);
		clink2.setOpticalLength(1000.0D);
		clink2.setPhysicalLength(1000.0D);
		clink2.setSourceAbstractSchemePort(cport21);
		clink2.setTargetAbstractSchemePort(cport30);
		SchemeCableThread clink2th = SchemeCableThread.createInstance(userId, "1", "", ctt, null, null, null, clink2);

		SchemeCableLink clink3 = SchemeCableLink.createInstance(userId, "Кабелёк4", scheme);
		clink3.setOpticalLength(1000.0D);
		clink3.setPhysicalLength(1000.0D);
		clink3.setSourceAbstractSchemePort(cport31);
		clink3.setTargetAbstractSchemePort(cport40);
		SchemeCableThread clink3th = SchemeCableThread.createInstance(userId, "1", "", ctt, null, null, null, clink3);

		SchemeCableLink clink4 = SchemeCableLink.createInstance(userId, "Кабелёк5", scheme);
		clink4.setOpticalLength(1000.0D);
		clink4.setPhysicalLength(1000.0D);
		clink4.setSourceAbstractSchemePort(cport41);
		clink4.setTargetAbstractSchemePort(cport22);
		SchemeCableThread clink4th = SchemeCableThread.createInstance(userId, "1", "", ctt, null, null, null, clink4);

		clink.add(clink0);
		clink.add(clink1);
		clink.add(clink2);
		clink.add(clink3);
		clink.add(clink4);

		SchemePath path0 = SchemePath.createInstance(userId, "Путяра измерений", scheme);

		path.add(path0);

		PathElement pel0 = PathElement.createInstance(userId, path0, link0);
		SchemeUtils.setOpticalLength(pel0, 3.0);
		SchemeUtils.setPhysicalLength(pel0, 3.0);
		
		PathElement pel1 = PathElement.createInstance(userId, path0, port00, cport00);
		
		PathElement pel2 = PathElement.createInstance(userId, path0, clink0th);
		SchemeUtils.setOpticalLength(pel2, 1000.0);
		SchemeUtils.setPhysicalLength(pel2, 1000.0);
		
		PathElement pel3 = PathElement.createInstance(userId, path0, cport10, cport11);
		
		PathElement pel4 = PathElement.createInstance(userId, path0, clink1th);
		SchemeUtils.setOpticalLength(pel4, 1000.0);
		SchemeUtils.setPhysicalLength(pel4, 1000.0);
		
		PathElement pel5 = PathElement.createInstance(userId, path0, cport20, cport21);
		
		PathElement pel6 = PathElement.createInstance(userId, path0, clink2th);
		SchemeUtils.setOpticalLength(pel6, 1000.0);
		SchemeUtils.setPhysicalLength(pel6, 1000.0);

		pel.add(pel0);
		pel.add(pel1);
		pel.add(pel2);
		pel.add(pel3);
		pel.add(pel4);
		pel.add(pel5);
		pel.add(pel6);

		scheme.setHeight(100);
		scheme.setLabel("lab1");
		scheme.setWidth(100);
	}

	private static void scheme2(Identifier userId, Identifier domainId) throws ApplicationException
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

		SchemeElement el0 = SchemeElement.createInstance(userId, "Один элемент", scheme);
		el0.setDescription("Описалово");

		SchemeElement el1 = SchemeElement.createInstance(userId, "Другой элемент", scheme);
		el1.setDescription("Описалово");

		SchemeDevice dev00 = SchemeDevice.createInstance(userId, "Девайс1", el0);
		dev0.add(dev00);

		SchemeDevice dev10 = SchemeDevice.createInstance(userId, "Девайс2", el1);
		dev1.add(dev10);

		SchemePort port00 = SchemePort.createInstance(userId, "Порт раз", DirectionType._OUT, dev00);

		port0.add(port00);

		SchemePort port10 = SchemePort.createInstance(userId, "Порт два", DirectionType._IN, dev10);

		port1.add(port10);

		SchemeCablePort cport00 = SchemeCablePort.createInstance(userId, "Каборт раз", DirectionType._IN, dev00);
		cport0.add(cport00);

		SchemeCablePort cport10 = SchemeCablePort.createInstance(userId, "Каборт два", DirectionType._OUT, dev10);
		cport1.add(cport10);

		el.add(el0);
		el.add(el1);

		SchemeLink link0 = SchemeLink.createInstance(userId, "Патчкорд");
		link.add(link0);
		link0.setOpticalLength(1000.0D);
		link0.setPhysicalLength(1000.0D);
		link0.setSourceAbstractSchemePort(port10);
		link0.setTargetAbstractSchemePort(port00);

		LinkType lt = LinkType.createInstance(userId, "6", "5", "4", LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "2", "3", IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGERESOURCE_CODE));
		CableLinkType clt = CableLinkType.createInstance(userId, "3", "2", "1", LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "4", "5", IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGERESOURCE_CODE));
		CableThreadType ctt = CableThreadType.createInstance(userId, "test2", "", "CTT2", 0, lt, clt); 

		SchemeCableLink clink0 = SchemeCableLink.createInstance(userId, "Кабелёк", scheme);
		clink.add(clink0);
		clink0.setOpticalLength(1000.0D);
		clink0.setPhysicalLength(1000.0D);
		clink0.setSourceAbstractSchemePort(cport10);
		clink0.setTargetAbstractSchemePort(cport00);
		SchemeCableThread clink0th = SchemeCableThread.createInstance(userId, "1", "", ctt, null, null, null, clink0);

		SchemePath path0 = SchemePath.createInstance(userId, "Путяра измерений", scheme);

		PathElement pel0 = PathElement.createInstance(userId, path0, link0);
		
		PathElement pel1 = PathElement.createInstance(userId, path0, port10, cport10);
		
		PathElement pel2 = PathElement.createInstance(userId, path0, clink0th);

		pel.add(pel0);
		pel.add(pel1);
		pel.add(pel2);

		path.add(path0);

		scheme.setHeight(100);
		scheme.setLabel("lab1");
		scheme.setWidth(100);
	}
}
