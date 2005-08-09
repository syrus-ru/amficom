/*
 * $Id: SchemeSampleData.java,v 1.25 2005/08/09 13:23:26 krupenn Exp $
 *
 * Copyright ї 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableThreadType;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.corba.IdlAbstractLinkTypePackage.LinkTypeSort;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.scheme.corba.IdlAbstractSchemePortPackage.IdlDirectionType;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.IdlKind;

/**
 * this class is used to create two sample instances
 * of Scheme whithout graphical components
 * @author $Author: krupenn $
 * @version $Revision: 1.25 $
 * @module generalclient_v1
 */
public final class SchemeSampleData {

	private static boolean loaded = false;

	public static Scheme scheme1;
	public static SchemeElement scheme1element0;
	public static SchemeElement scheme1element1;
	public static SchemeElement scheme1element2;
	public static SchemeElement scheme1element3;
	public static SchemeElement scheme1element4;
	public static SchemeLink scheme1link0;
	public static SchemeCableLink scheme1clink0;
	public static SchemeCableLink scheme1clink1;
	public static SchemeCableLink scheme1clink2;
	public static SchemeCableLink scheme1clink3;
	public static SchemeCableLink scheme1clink4;
	public static SchemePath scheme1path0;

	public static Scheme scheme2;
	public static SchemeElement scheme2element1;
	public static SchemeLink scheme2link0;
	public static SchemeCableLink scheme2clink0;
	public static SchemePath scheme2path0;
	
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
		EquipmentType equipmentType = EquipmentType.createInstance(userId, "sample", "desc", "name", "manu", "code"); 
		LinkType lt = LinkType.createInstance(userId, "1", "2", "3", LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "8", "9", IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGERESOURCE_CODE));
		CableLinkType clt = CableLinkType.createInstance(userId, "4", "5", "6", LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "8", "7", IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGERESOURCE_CODE));
		CableThreadType ctt = CableThreadType.createInstance(userId, "test", "7", "CTT", 0, lt, clt); 
		
		scheme1 = Scheme.createInstance(userId, "Схемка (бол)", IdlKind.NETWORK, domainId);
		scheme1element0 = SchemeElement.createInstance(userId, "Один элемент", scheme1);
		scheme1element0.setDescription("Описалово");
		scheme1element0.setEquipmentType(equipmentType);

		scheme1element1 = SchemeElement.createInstance(userId, "Другой элемент", scheme1);
		scheme1element1.setDescription("Описалово");
		scheme1element1.setEquipmentType(equipmentType);

		scheme1element2 = SchemeElement.createInstance(userId, "Третий элемент", scheme1);
		scheme1element2.setDescription("Описалово");
		scheme1element2.setEquipmentType(equipmentType);

		scheme1element3 = SchemeElement.createInstance(userId, "Опять элемент", scheme1);
		scheme1element3.setDescription("Описалово");
		scheme1element3.setEquipmentType(equipmentType);

		scheme1element4 = SchemeElement.createInstance(userId, "Совсем элемент", scheme1);
		scheme1element4.setDescription("Описалово");
		scheme1element4.setEquipmentType(equipmentType);

		SchemeDevice scheme1dev00 = SchemeDevice.createInstance(userId, "Девайс1", scheme1element0);
		SchemeDevice scheme1dev10 = SchemeDevice.createInstance(userId, "Девайс2", scheme1element1);
		SchemeDevice scheme1dev20 = SchemeDevice.createInstance(userId, "Девайс3", scheme1element2);
		SchemeDevice scheme1dev30 = SchemeDevice.createInstance(userId, "Девайс4", scheme1element3);
		SchemeDevice scheme1dev40 = SchemeDevice.createInstance(userId, "Девайс5", scheme1element4);
		
		SchemePort scheme1dev00port00 = SchemePort.createInstance(userId, "Порт раз", IdlDirectionType._OUT, scheme1dev00);
		SchemePort scheme1dev00port01 = SchemePort.createInstance(userId, "Порт два", IdlDirectionType._IN, scheme1dev00);

		SchemeCablePort scheme1dev00cport00 = SchemeCablePort.createInstance(userId, "Каборт раз", IdlDirectionType._IN, scheme1dev00);
		SchemeCablePort scheme1dev10cport10 = SchemeCablePort.createInstance(userId, "Каборт два", IdlDirectionType._OUT, scheme1dev10);
		SchemeCablePort scheme1dev10cport11 = SchemeCablePort.createInstance(userId, "Каборт три", IdlDirectionType._OUT, scheme1dev10);
		SchemeCablePort scheme1dev20cport20 = SchemeCablePort.createInstance(userId, "Каборт четыре", IdlDirectionType._OUT, scheme1dev20);
		SchemeCablePort scheme1dev20cport21 = SchemeCablePort.createInstance(userId, "Каборт пять", IdlDirectionType._OUT, scheme1dev20);
		SchemeCablePort scheme1dev20cport22 = SchemeCablePort.createInstance(userId, "Каборт шесть", IdlDirectionType._OUT, scheme1dev20);
		SchemeCablePort scheme1dev30cport30 = SchemeCablePort.createInstance(userId, "Каборт сем", IdlDirectionType._OUT, scheme1dev30);
		SchemeCablePort scheme1dev30cport31 = SchemeCablePort.createInstance(userId, "Каборт восем", IdlDirectionType._OUT, scheme1dev30);
		SchemeCablePort scheme1dev40cport40 = SchemeCablePort.createInstance(userId, "Каборт дэвят", IdlDirectionType._OUT, scheme1dev40);
		SchemeCablePort scheme1dev40cport41 = SchemeCablePort.createInstance(userId, "Каборт дэсят", IdlDirectionType._OUT, scheme1dev40);

		scheme1link0 = SchemeLink.createInstance(userId, "Патчкорд", scheme1);
		scheme1link0.setOpticalLength(3.0D);
		scheme1link0.setPhysicalLength(3.0D);
		scheme1link0.setSourceAbstractSchemePort(scheme1dev00port01);
		scheme1link0.setTargetAbstractSchemePort(scheme1dev00port00);

		scheme1clink0 = SchemeCableLink.createInstance(userId, "Кабелёк1", scheme1);
		scheme1clink0.setOpticalLength(1000.0D);
		scheme1clink0.setPhysicalLength(1000.0D);
		scheme1clink0.setSourceAbstractSchemePort(scheme1dev00cport00);
		scheme1clink0.setTargetAbstractSchemePort(scheme1dev10cport10);
		SchemeCableThread scheme1clink0th = SchemeCableThread.createInstance(userId, "1", "", ctt, null, null, null, scheme1clink0);

		scheme1clink1 = SchemeCableLink.createInstance(userId, "Кабелёк2", scheme1);
		scheme1clink1.setOpticalLength(1000.0D);
		scheme1clink1.setPhysicalLength(1000.0D);
		scheme1clink1.setSourceAbstractSchemePort(scheme1dev10cport11);
		scheme1clink1.setTargetAbstractSchemePort(scheme1dev20cport20);
		SchemeCableThread scheme1clink1th = SchemeCableThread.createInstance(userId, "1", "", ctt, null, null, null, scheme1clink1);

		scheme1clink2 = SchemeCableLink.createInstance(userId, "Кабелёк3", scheme1);
		scheme1clink2.setOpticalLength(1000.0D);
		scheme1clink2.setPhysicalLength(1000.0D);
		scheme1clink2.setSourceAbstractSchemePort(scheme1dev20cport21);
		scheme1clink2.setTargetAbstractSchemePort(scheme1dev30cport30);
		SchemeCableThread scheme1clink2th = SchemeCableThread.createInstance(userId, "1", "", ctt, null, null, null, scheme1clink2);

		scheme1clink3 = SchemeCableLink.createInstance(userId, "Кабелёк4", scheme1);
		scheme1clink3.setOpticalLength(1000.0D);
		scheme1clink3.setPhysicalLength(1000.0D);
		scheme1clink3.setSourceAbstractSchemePort(scheme1dev30cport31);
		scheme1clink3.setTargetAbstractSchemePort(scheme1dev40cport40);
		SchemeCableThread.createInstance(userId, "1", "", ctt, null, null, null, scheme1clink3);

		scheme1clink4 = SchemeCableLink.createInstance(userId, "Кабелёк5", scheme1);
		scheme1clink4.setOpticalLength(1000.0D);
		scheme1clink4.setPhysicalLength(1000.0D);
		scheme1clink4.setSourceAbstractSchemePort(scheme1dev40cport41);
		scheme1clink4.setTargetAbstractSchemePort(scheme1dev20cport22);
		SchemeCableThread.createInstance(userId, "1", "", ctt, null, null, null, scheme1clink4);

		SchemeMonitoringSolution solution = SchemeMonitoringSolution.createInstance(userId, "Сольюшн", scheme1);
		scheme1path0 = SchemePath.createInstance(userId, "Путяра измерений", solution);

		PathElement scheme1pelstart = PathElement.createInstance(userId, scheme1path0, null, scheme1dev00port00);

		PathElement scheme1pel0 = PathElement.createInstance(userId, scheme1path0, scheme1link0);
		SchemeUtils.setOpticalLength(scheme1pel0, 3.0);
		SchemeUtils.setPhysicalLength(scheme1pel0, 3.0);
		
		PathElement scheme1pel1 = PathElement.createInstance(userId, scheme1path0, scheme1dev00port00, scheme1dev00cport00);
		
		PathElement scheme1pel2 = PathElement.createInstance(userId, scheme1path0, scheme1clink0th);
		SchemeUtils.setOpticalLength(scheme1pel2, 1000.0);
		SchemeUtils.setPhysicalLength(scheme1pel2, 1000.0);
		
		PathElement scheme1pel3 = PathElement.createInstance(userId, scheme1path0, scheme1dev10cport10, scheme1dev10cport11);
		
		PathElement scheme1pel4 = PathElement.createInstance(userId, scheme1path0, scheme1clink1th);
		SchemeUtils.setOpticalLength(scheme1pel4, 1000.0);
		SchemeUtils.setPhysicalLength(scheme1pel4, 1000.0);
		
		PathElement scheme1pel5 = PathElement.createInstance(userId, scheme1path0, scheme1dev20cport20, scheme1dev20cport21);
		
		PathElement scheme1pel6 = PathElement.createInstance(userId, scheme1path0, scheme1clink2th);
		SchemeUtils.setOpticalLength(scheme1pel6, 1000.0);
		SchemeUtils.setPhysicalLength(scheme1pel6, 1000.0);

		PathElement scheme1pelend = PathElement.createInstance(userId, scheme1path0, scheme1dev20cport21, null);

		scheme1.setHeight(100);
		scheme1.setLabel("lab1");
		scheme1.setWidth(100);
	}

	private static void scheme2(Identifier userId, Identifier domainId) throws ApplicationException
	{
		EquipmentType equipmentType = EquipmentType.createInstance(userId, "samle2", "desc2", "name2", "manu2", "code2"); 
		LinkType lt = LinkType.createInstance(userId, "6", "5", "4", LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "2", "3", IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGERESOURCE_CODE));
		CableLinkType clt = CableLinkType.createInstance(userId, "3", "2", "1", LinkTypeSort.LINKTYPESORT_OPTICAL_FIBER, "4", "5", IdentifierPool.getGeneratedIdentifier(ObjectEntities.IMAGERESOURCE_CODE));
		CableThreadType ctt = CableThreadType.createInstance(userId, "test2", "", "CTT2", 0, lt, clt); 

		scheme2 = Scheme.createInstance(userId, "Схемочка (мал)", IdlKind.NETWORK, domainId);
		SchemeElement scheme2el0 = SchemeElement.createInstance(userId, "Один элемент", scheme2);
		scheme2el0.setDescription("Описалово");
		scheme2el0.setEquipmentType(equipmentType);

		scheme2element1 = SchemeElement.createInstance(userId, "Другой элемент", scheme2);
		scheme2element1.setDescription("Описалово");
		scheme2element1.setEquipmentType(equipmentType);

		SchemeDevice scheme2dev00 = SchemeDevice.createInstance(userId, "Девайс1", scheme2el0);
		SchemeDevice scheme2dev10 = SchemeDevice.createInstance(userId, "Девайс2", scheme2element1);

		SchemePort scheme2dev00port00 = SchemePort.createInstance(userId, "Порт раз", IdlDirectionType._OUT, scheme2dev00);
		SchemePort scheme2dev10port10 = SchemePort.createInstance(userId, "Порт два", IdlDirectionType._IN, scheme2dev10);

		SchemeCablePort scheme2dev00cport00 = SchemeCablePort.createInstance(userId, "Каборт раз", IdlDirectionType._IN, scheme2dev00);
		SchemeCablePort scheme2dev10cport10 = SchemeCablePort.createInstance(userId, "Каборт два", IdlDirectionType._OUT, scheme2dev10);

		scheme2link0 = SchemeLink.createInstance(userId, "Патчкорд");
		scheme2link0.setOpticalLength(1000.0D);
		scheme2link0.setPhysicalLength(1000.0D);
		scheme2link0.setSourceAbstractSchemePort(scheme2dev10port10);
		scheme2link0.setTargetAbstractSchemePort(scheme2dev00port00);

		scheme2clink0 = SchemeCableLink.createInstance(userId, "Кабелёк", scheme2);
		scheme2clink0.setOpticalLength(1000.0D);
		scheme2clink0.setPhysicalLength(1000.0D);
		scheme2clink0.setSourceAbstractSchemePort(scheme2dev10cport10);
		scheme2clink0.setTargetAbstractSchemePort(scheme2dev00cport00);
		SchemeCableThread scheme2clink0th = SchemeCableThread.createInstance(userId, "1", "", ctt, null, null, null, scheme2clink0);

		SchemeMonitoringSolution solution = SchemeMonitoringSolution.createInstance(userId, "Сольюшн", scheme2);
		scheme2path0 = SchemePath.createInstance(userId, "Путяра измерений", solution);
		PathElement scheme2pelstart = PathElement.createInstance(userId, scheme2path0, null, scheme2dev10port10);
		PathElement scheme2pel0 = PathElement.createInstance(userId, scheme2path0, scheme2link0);
		PathElement scheme2pel1 = PathElement.createInstance(userId, scheme2path0, scheme2dev10port10, scheme2dev10cport10);
		PathElement scheme2pel2 = PathElement.createInstance(userId, scheme2path0, scheme2clink0th);
		PathElement scheme2pelend = PathElement.createInstance(userId, scheme2path0, scheme2dev10cport10, null);

		scheme2.setHeight(100);
		scheme2.setLabel("lab1");
		scheme2.setWidth(100);
	}
}
