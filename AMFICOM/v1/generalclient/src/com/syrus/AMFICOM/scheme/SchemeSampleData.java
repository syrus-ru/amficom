/*
 * $Id: SchemeSampleData.java,v 1.5 2005/04/19 09:01:50 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.scheme.corba.AbstractSchemePortDirectionType;
import com.syrus.AMFICOM.scheme.corba.Scheme_TransferablePackage.Kind;

/**
 * this class is used to create two sample instances
 * of Scheme whithout graphical components
 * @author $Author: bass $
 * @version $Revision: 1.5 $
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
		SchemeCableThread clink0th = SchemeCableThread.createInstance(userId);
		SchemeCableThread clink1th = SchemeCableThread.createInstance(userId);
		SchemeCableThread clink2th = SchemeCableThread.createInstance(userId);
		SchemeCableThread clink3th = SchemeCableThread.createInstance(userId);
		SchemeCableThread clink4th = SchemeCableThread.createInstance(userId);
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

		Scheme scheme = Scheme.createInstance(userId, domainId, "������", "");
		dev00.setSchemePorts(port0);
		dev00.setSchemeCablePorts(cport0);
		dev00.setName("������1");

		dev10.setSchemeCablePorts(cport1);
		dev10.setName("������2");
		
		dev20.setSchemeCablePorts(cport2);
		dev20.setName("������3");
		
		dev30.setSchemeCablePorts(cport3);
		dev30.setName("������4");
		
		dev40.setSchemeCablePorts(cport4);
		dev40.setName("������5");
		
//		port00.setSchemeDevice(dev00);
		port00.setDirectionType(AbstractSchemePortDirectionType._OUT);
		port00.setName("���� ���");
//		port00.setSchemeLink(link0);

//		port01.setSchemeDevice(dev00);
		port01.setDirectionType(AbstractSchemePortDirectionType._IN);
		port01.setName("���� ���");
//		port01.setSchemeLink(link0);

//		cport00.setSchemeCableLink(clink0);
//		cport00.setSchemeDevice(dev00);
		cport00.setDirectionType(AbstractSchemePortDirectionType._IN);
		cport00.setName("������ ���");

//		cport10.setSchemeCableLink(clink0);
//		cport10.setSchemeDevice(dev10);
		cport10.setDirectionType(AbstractSchemePortDirectionType._OUT);
		cport10.setName("������ ���");

//		cport11.setSchemeCableLink(clink1);
//		cport11.setSchemeDevice(dev10);
		cport11.setDirectionType(AbstractSchemePortDirectionType._OUT);
		cport11.setName("������ ���");

//		cport20.setSchemeCableLink(clink1);
//		cport20.setSchemeDevice(dev20);
		cport20.setDirectionType(AbstractSchemePortDirectionType._OUT);
		cport20.setName("������ ������");

//		cport21.setSchemeCableLink(clink2);
//		cport21.setSchemeDevice(dev20);
		cport21.setDirectionType(AbstractSchemePortDirectionType._OUT);
		cport21.setName("������ ����");

//		cport22.setSchemeCableLink(clink4);
//		cport22.setSchemeDevice(dev20);
		cport22.setDirectionType(AbstractSchemePortDirectionType._OUT);
		cport22.setName("������ �����");

//		cport30.setSchemeCableLink(clink2);
//		cport30.setSchemeDevice(dev30);
		cport30.setDirectionType(AbstractSchemePortDirectionType._OUT);
		cport30.setName("������ ���");

//		cport31.setSchemeCableLink(clink3);
//		cport31.setSchemeDevice(dev30);
		cport31.setDirectionType(AbstractSchemePortDirectionType._OUT);
		cport31.setName("������ �����");

//		cport40.setSchemeCableLink(clink3);
//		cport40.setSchemeDevice(dev40);
		cport40.setDirectionType(AbstractSchemePortDirectionType._OUT);
		cport40.setName("������ �����");

//		cport41.setSchemeCableLink(clink4);
//		cport41.setSchemeDevice(dev40);
		cport41.setDirectionType(AbstractSchemePortDirectionType._OUT);
		cport41.setName("������ �����");

		el0.setDescription("���������");
		el0.setSchemeDevices(dev0);
		el0.setName("���� �������");
		el0.setScheme(scheme);

		el1.setDescription("���������");
		el1.setSchemeDevices(dev1);
		el1.setName("������ �������");
		el1.setScheme(scheme);

		el2.setDescription("���������");
		el2.setSchemeDevices(dev2);
		el2.setName("������ �������");
		el2.setScheme(scheme);

		el3.setDescription("���������");
		el3.setSchemeDevices(dev3);
		el3.setName("����� �������");
		el3.setScheme(scheme);

		el4.setDescription("���������");
		el4.setSchemeDevices(dev4);
		el4.setName("������ �������");
		el4.setScheme(scheme);

		link0.setName("��������");
		link0.setOpticalLength(3.0D);
		link0.setPhysicalLength(3.0D);
		link0.setSourceSchemePort(port01);
		link0.setTargetSchemePort(port00);
//		link0.setScheme(scheme);

		clink0.setName("������1");
		clink0.setOpticalLength(1000.0D);
		clink0.setPhysicalLength(1000.0D);
		clink0.setSourceSchemeCablePort(cport10);
		clink0.setTargetSchemeCablePort(cport00);
		clink0.setSchemeCableThreads(Collections.singleton(clink0th));

		clink1.setName("������2");
		clink1.setOpticalLength(1000.0D);
		clink1.setPhysicalLength(1000.0D);
		clink1.setSourceSchemeCablePort(cport11);
		clink1.setTargetSchemeCablePort(cport20);
		clink1.setSchemeCableThreads(Collections.singleton(clink1th));

		clink2.setName("������3");
		clink2.setOpticalLength(1000.0D);
		clink2.setPhysicalLength(1000.0D);
		clink2.setSourceSchemeCablePort(cport21);
		clink2.setTargetSchemeCablePort(cport30);
		clink2.setSchemeCableThreads(Collections.singleton(clink2th));

		clink3.setName("������4");
		clink3.setOpticalLength(1000.0D);
		clink3.setPhysicalLength(1000.0D);
		clink3.setSourceSchemeCablePort(cport31);
		clink3.setTargetSchemeCablePort(cport40);
		clink3.setSchemeCableThreads(Collections.singleton(clink3th));

		clink4.setName("������5");
		clink4.setOpticalLength(1000.0D);
		clink4.setPhysicalLength(1000.0D);
		clink4.setSourceSchemeCablePort(cport41);
		clink4.setTargetSchemeCablePort(cport22);
		clink4.setSchemeCableThreads(Collections.singleton(clink4th));

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

		path0.setStartSchemeElement(el0);
		path0.setEndSchemeElement(el3);
		path0.setPathElements(pel);
		path0.setName("������ ���������");
		path0.setScheme(scheme);

		scheme.setSchemeCableLinks(clink);
		scheme.setSchemeLinks(link);
		scheme.getCurrentSchemeMonitoringSolution().setSchemePaths(path);
		scheme.setSchemeElements(el);
		scheme.setHeight(100);
		scheme.setLabel("lab1");
		scheme.setKind(Kind.NETWORK);
		scheme.setWidth(100);
		scheme.setDomainId(domainId);

		SchemeStorableObjectPool.putStorableObject(scheme);
	}

	private static void scheme2(Identifier userId, Identifier domainId) throws CreateObjectException, IllegalObjectEntityException
	{
		Scheme scheme = Scheme.createInstance(userId, domainId, "��������", "");
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
		SchemeCableThread clink0th = SchemeCableThread.createInstance(userId);
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

//		port00.setSchemeDevice(dev00);
		port00.setDirectionType(AbstractSchemePortDirectionType._OUT);
		port00.setName("���� ���");
//		port00.setSchemeLink(link0);

//		port10.setSchemeDevice(dev10);
		port10.setDirectionType(AbstractSchemePortDirectionType._IN);
		port10.setName("���� ���");
//		port10.setSchemeLink(link0);

//		cport00.setSchemeCableLink(clink0);
//		cport00.setSchemeDevice(dev00);
		cport00.setDirectionType(AbstractSchemePortDirectionType._IN);
		cport00.setName("������ ���");

//		cport10.setSchemeCableLink(clink0);
//		cport10.setSchemeDevice(dev10);
		cport10.setDirectionType(AbstractSchemePortDirectionType._OUT);
		cport10.setName("������ ���");

		dev00.setSchemePorts(port0);
		dev00.setSchemeCablePorts(cport0);
		dev00.setName("������1");

		dev10.setSchemePorts(port1);
		dev10.setSchemeCablePorts(cport1);
		dev10.setName("������2");
		
		el0.setDescription("���������");
		el0.setSchemeDevices(dev0);
		el0.setName("���� �������");
		el0.setScheme(scheme);

		el1.setDescription("���������");
		el1.setSchemeDevices(dev1);
		el1.setName("������ �������");
		el1.setScheme(scheme);

		link0.setName("��������");
		link0.setOpticalLength(1000.0D);
		link0.setPhysicalLength(1000.0D);
		link0.setSourceSchemePort(port10);
		link0.setTargetSchemePort(port00);

		clink0.setName("������");
		clink0.setOpticalLength(1000.0D);
		clink0.setPhysicalLength(1000.0D);
		clink0.setSourceSchemeCablePort(cport10);
		clink0.setTargetSchemeCablePort(cport00);
		clink0.setSchemeCableThreads(Collections.singleton(clink0th));

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

		path0.setEndSchemeElement(el0);
		path0.setPathElements(pel);
		path0.setName("������ ���������");
		path0.setStartSchemeElement(el1);
		path0.setScheme(scheme);

		scheme.setSchemeCableLinks(clink);
		scheme.setSchemeLinks(link);
		scheme.getCurrentSchemeMonitoringSolution().setSchemePaths(path);
		scheme.setSchemeElements(el);
		scheme.setHeight(100);
		scheme.setLabel("lab1");
		scheme.setKind(Kind.NETWORK);
		scheme.setWidth(100);
		scheme.setDomainId(domainId);

		SchemeStorableObjectPool.putStorableObject(scheme);
	}
}
