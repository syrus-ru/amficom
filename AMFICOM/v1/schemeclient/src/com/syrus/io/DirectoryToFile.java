package com.syrus.io;

//import java.io.*;
//import java.util.*;
//
//import com.syrus.AMFICOM.Client.Resource.*;
//import com.syrus.AMFICOM.Client.Resource.ISMDirectory.MeasurementPortType;
//import com.syrus.AMFICOM.Client.Resource.General.Characteristic;
//import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
//import com.syrus.AMFICOM.Client.Resource.Scheme.*;
//import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class DirectoryToFile
{
}
/*
	public static final String typeDir = "resources/types/";
	public static final String eqType = "equipment";
	public static final String linkType = "link";
	public static final String cableLinkType = "cablelink";
	public static final String portType = "port";
	public static final String cablePortType = "cableport";
	public static final String testportType = "testport";
	public static final String accessportType = "accessport";
	public static final String characteristicType = "characteristic";
	public static final String ext = ".txt";

	public static final String protoDir = "resources/proto/";
	public static final String proto_schemeDir = "resources/proto/protoCell/";
	public static final String proto_ugoDir = "resources/proto/proto_ugo/";
	public static final String protoImageDir = "resources/proto/img/";
	public static final String protoType = "equipment";
	public static final String mapProto_name = "SchemeProtoGroup";
	public static final String mapProto_group_name = "SchemeProtoGroup";

	public static final String schemeDir = "resources/scheme/";
	public static final String schemeCellDir = "resources/scheme/schemeCell/";
	public static final String scheme_ugoDir = "resources/scheme/scheme_ugo/";
	public static final String scheme_name = "scheme";
	public static final String schemeElement_name = "elements";
	public static final String schemeElementCellDir = "resources/scheme/elementsCell/";
	public static final String schemeElement_ugoDir = "resources/scheme/elements_ugo/";
	public static final String schemePath_name = "path";

	public static final String linksDir = "resources/links/";
	public static final String portsDir = "resources/ports/";
	public static final String devicesDir = "resources/devices/";

	private DirectoryToFile()
	{
	}

	public static void readAll()
	{
		System.out.println("Reading types...");
		readTypes();
		System.out.println("Reading ports...");
		readPorts();
		System.out.println("Reading cableports...");
		readCablePorts();
		System.out.println("Reading links...");
		readLinks();
		System.out.println("Reading cablelinks...");
		readCableLinks();
		System.out.println("Reading devices...");
		readDevices();
		System.out.println("Reading protos...");
		readProtoElements();
		System.out.println("Reading mapprotos...");
		readMapProto();
		System.out.println("Reading SchemeProtoGroups...");
		readSchemeProtoGroups();
		System.out.println("Reading paths...");
		readSchemePaths();
		System.out.println("Reading elements...");
		readSchemeElements();
		System.out.println("Reading schemes...");
		readScheme();
	}

	public static void writeAll()
	{
		/* кажися это лишнее
		Map schemes = Pool.getMap(Scheme.typ);
		for (Enumeration en = schemes.elements(); en.hasMoreElements();)
		{
			Scheme scheme = (Scheme)en.nextElement();
			for (Iterator it = scheme.getAllLinks().iterator(); it.hasNext();)
			{
				SchemeLink link = (SchemeLink)it.next();
				Pool.put(SchemeLink.typ, link.getId(), link);
			}
			for (Iterator it = scheme.getAllCableLinks().iterator(); it.hasNext();)
			{
				SchemeCableLink link = (SchemeCableLink)it.next();
				Pool.put(SchemeCableLink.typ, link.getId(), link);
			}
			for (Iterator it = scheme.elements.iterator(); it.hasNext();)
			{
				SchemeElement se = (SchemeElement)it.next();
				if (se.schemeId.equals("") && !se.elementIds.isEmpty())
				{
					for (Iterator it2 = se.getAllChilds().iterator(); it2.hasNext();)
					{
						SchemeElement sel = (SchemeElement)it2.next();
						Pool.put(SchemeElement.typ, sel.getId(), sel);
					}
				}
			}
		}
/
		System.out.println("Writing types...");
		writeTypes();
		System.out.println("Writing devices...");
		writeDevices();
		System.out.println("Writing ports...");
		writePorts();
		System.out.println("Writing cableports...");
		writeCablePorts();
		System.out.println("Writing links...");
		writeLinks();
		System.out.println("Writing cableLinks...");
		writeCableLinks();
		System.out.println("Writing protos...");
		writeProtoElements();
		System.out.println("Writing mapprotos...");
		writeSchemeProtoGroups();
		System.out.println("Writing SchemeProtoGroups...");
		writeSchemeProtoGroups();
		System.out.println("Writing paths...");
		writeSchemePaths();
		System.out.println("Writing elements...");
		writeSchemeElements();
		System.out.println("Writing schemes...");
		writeScheme();
	}

	protected static void writeTypes()
	{
		new File (typeDir).mkdirs();
		Map Map;

		// *******************************************************************
		// ************************   EQUIPMENT  *****************************
		// *******************************************************************
		if (Pool.getMap(EquipmentType.typ) != null)
		{
			Map eqtClasses = new HashMap();
			for(Iterator it = Pool.getMap(EquipmentType.typ).values().iterator(); it.hasNext();)
			{
				EquipmentType eqt = (EquipmentType )it.next();
				eqtClasses.put(eqt.eqClass, eqt.eqClass);
			}
			for(Iterator eqtClassesEl = eqtClasses.values().iterator(); eqtClassesEl.hasNext(); )
			{
				String eqtClass = (String)eqtClassesEl.next();

				List vec = new ArrayList();
				Map hash = new HashMap();
				for(Iterator it = Pool.getMap(EquipmentType.typ).values().iterator(); it.hasNext();)
				{
					EquipmentType eqt = (EquipmentType)it.next();
					if (eqt.eqClass.equals(eqtClass))
						vec.add(eqt);
				}
					try
					{
						File f = new File(typeDir + eqType + eqtClass + ext);
						if (f.exists())
							f.delete();
						f.createNewFile();
						int counter = 0;

						FileOutputStream fos = new FileOutputStream (f);
						OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
						PrintWriter pw = new PrintWriter(osw, true);

						pw.println ("AMFICOM component description file");
						pw.println ("@type " + EquipmentType.typ);
						pw.println();

						for (Iterator it = vec.iterator(); it.hasNext();)
						{
							EquipmentType eqt = (EquipmentType)it.next();
							pw.println ("@name " + eqt.getName());
							pw.println ("@id " + eqt.getId());
							pw.println ("@eqClass " + eqt.eqClass);
							pw.println ("@description " + eqt.description);
							pw.println ("@imageId " + eqt.imageId);
							pw.println ("@manufacturer " + eqt.manufacturer);
							pw.println ("@manufacturerCode " + eqt.manufacturerCode);
							pw.println ("@characteristics ");
							for (Iterator cit = eqt.characteristics.values().iterator(); cit.hasNext();)
							{
								Characteristic ch = (Characteristic)cit.next();
								pw.println(ch.type_id + " " + ch.value);
							}
							pw.println ("@end of characteristics");
							pw.println();
							counter++;
						}
						pw.close();
						osw.close();
						fos.close();
						System.out.println(counter + " EquipmentTypes of " +  eqtClass + " class written");
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
			}
		}

		// *******************************************************************
		// ************************   PORTS  *********************************
		// *******************************************************************
		if (Pool.getMap(PortType.typ) != null)
		{
			try
			{
				File f = new File(typeDir + portType + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);

				pw.println ("AMFICOM component description file");
				pw.println ("@type " + PortType.typ);
				pw.println();

				for(Iterator it = Pool.getMap(PortType.typ).values().iterator(); it.hasNext();)
				{
					PortType pt = (PortType)it.next();
					pw.println ("@name " + pt.getName());
					pw.println ("@id " + pt.getId());
					pw.println ("@pClass " + pt.pClass);
					pw.println ("@description " + pt.description);
					pw.println ("@interfaceId " + pt.interfaceId);
					pw.println ("@characteristics ");
					for (Iterator cit = pt.characteristics.values().iterator(); cit.hasNext();)
					{
						Characteristic ch = (Characteristic)cit.next();
						pw.println(ch.type_id + " " + ch.value);
					}
					pw.println ("@end of characteristics");
					pw.println();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " PortTypes written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		// *******************************************************************
		// ************************   TESTPORTS  *****************************
		// *******************************************************************
		if (Pool.getMap(TestPortType.typ) != null)
		{
			try
			{
				File f = new File(typeDir + testportType + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);
				pw.println ("AMFICOM component description file");
				pw.println ("@type " + TestPortType.typ);
				pw.println();

				for(Iterator it = Pool.getMap(TestPortType.typ).values().iterator(); it.hasNext();)
				{
					TestPortType tpt = (TestPortType)it.next();
					pw.println ("@name " + tpt.getName());
					pw.println ("@id " + tpt.getId());
					pw.println ("@description " + tpt.description);
					pw.println ("@portTypeId " + tpt.portTypeId);
					pw.println ("@characteristics ");
					for (Iterator cit = tpt.characteristics.values().iterator(); cit.hasNext();)
					{
						Characteristic ch = (Characteristic)cit.next();
						pw.println(ch.type_id + " " + ch.value);
					}
					pw.println ("@end of characteristics");
					pw.println();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " TestPortTypes written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		// *******************************************************************
		// ************************   ACCESSPORTS  ***************************
		// *******************************************************************
		if (Pool.getMap(AccessPortType.typ) != null)
		{
			try
			{
				File f = new File(typeDir + accessportType + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);
				pw.println ("AMFICOM component description file");
				pw.println ("@type " + AccessPortType.typ);
				pw.println();

				for(Iterator it = Pool.getMap(AccessPortType.typ).values().iterator(); it.hasNext();)
				{
					AccessPortType apt = (AccessPortType)it.next();
					pw.println ("@name " + apt.getName());
					pw.println ("@id " + apt.getId());
					pw.println ("@description " + apt.description);
					pw.println ("@accessType " + apt.accessType);
					pw.println ("@characteristics ");
					for (Iterator cit = apt.characteristics.values().iterator(); cit.hasNext();)
					{
						Characteristic ch = (Characteristic)cit.next();
						pw.println(ch.type_id + " " + ch.value);
					}
					pw.println ("@end of characteristics");
					pw.println ("@testTypeIds = ");
					for (Iterator tit = apt.testTypeIds.iterator(); tit.hasNext();)
						pw.println((String)tit.next());
					pw.println ("@end of testTypeIds");
					pw.println();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " AccessPortTypes written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
		// *******************************************************************
		// ************************   LINKS  *********************************
		// *******************************************************************
		if (Pool.getMap(LinkType.typ) != null)
		{
			try
			{
				File f = new File(typeDir + linkType + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);
				pw.println ("AMFICOM component description file");
				pw.println ("@type " + LinkType.typ);
				pw.println();

				for(Iterator it = Pool.getMap(LinkType.typ).values().iterator(); it.hasNext();)
				{
					LinkType lt = (LinkType)it.next();
					pw.println ("@name " + lt.getName());
					pw.println ("@id " + lt.getId());
					pw.println ("@description " + lt.description);
					pw.println ("@imageId " + lt.imageId);
					pw.println ("@linkClass " + lt.linkClass);
					pw.println ("@manufacturer " + lt.manufacturer);
					pw.println ("@manufacturerCode " + lt.manufacturerCode);
					pw.println ("@characteristics ");
					for (Iterator cit = lt.characteristics.values().iterator(); cit.hasNext();)
					{
						Characteristic ch = (Characteristic)cit.next();
						pw.println(ch.type_id + " " + ch.value);
					}
					pw.println ("@end of characteristics");
					pw.println();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " LinkTypes written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		// *******************************************************************
		// *********************   CABLELINKS  *******************************
		// *******************************************************************
		if (Pool.getMap(CableLinkType.typ) != null)
		{
			try
			{
				File f = new File(typeDir + cableLinkType + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);
				pw.println ("AMFICOM component description file");
				pw.println ("@type " + CableLinkType.typ);
				pw.println();

				for(Iterator it = Pool.getMap(CableLinkType.typ).values().iterator(); it.hasNext();)
				{
					CableLinkType lt = (CableLinkType)it.next();
					pw.println ("@name " + lt.getName());
					pw.println ("@id " + lt.getId());
					pw.println ("@description " + lt.description);
					pw.println ("@imageId " + lt.imageId);
					pw.println ("@linkClass " + lt.linkClass);
					pw.println ("@manufacturer " + lt.manufacturer);
					pw.println ("@manufacturerCode " + lt.manufacturerCode);
					pw.println ("@characteristics ");
					for (Iterator cit = lt.characteristics.values().iterator(); cit.hasNext();)
					{
						Characteristic ch = (Characteristic)cit.next();
						pw.println(ch.type_id + " " + ch.value);
					}
					pw.println ("@end of characteristics");
					pw.println();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " CableLinkTypes written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}


		// *******************************************************************
		// ************************   CHARACTERISTICS ************************
		// *******************************************************************
		if (Pool.getMap(CharacteristicType.typ) != null)
		{
			Map chtClasses = new HashMap();
			for(Iterator it = Pool.getMap(CharacteristicType.typ).values().iterator(); it.hasNext();)
			{
				CharacteristicType cht = (CharacteristicType)it.next();
				chtClasses.put(cht.chClass, cht.chClass);
			}

			for(Iterator chtClassesEl = chtClasses.values().iterator(); chtClassesEl.hasNext(); )
			{
				String chtClass = (String)chtClassesEl.next();

				List vec = new ArrayList();
				Map hash = new HashMap();
				for(Iterator it = Pool.getMap(CharacteristicType.typ).values().iterator(); it.hasNext();)
				{
					CharacteristicType cht = (CharacteristicType)it.next();
					if (cht.chClass.equals(chtClass))
						vec.add(cht);
				}
					try
					{
						File f = new File(typeDir + characteristicType + chtClass + ext);
						if (f.exists())
							f.delete();
						f.createNewFile();
						int counter = 0;

						FileOutputStream fos = new FileOutputStream (f);
						OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
						PrintWriter pw = new PrintWriter(osw, true);
						pw.println ("AMFICOM component description file");
						pw.println ("@type " + CharacteristicType.typ);
						pw.println();

						for (Iterator it = vec.iterator(); it.hasNext();)
						//for(Enumeration en = Pool.getMap(CharacteristicType.typ).elements(); en.hasMoreElements();)
						{
							CharacteristicType ch = (CharacteristicType)it.next();
							pw.println ("@name " + ch.getName());
							pw.println ("@id " + ch.getId());
							pw.println ("@description " + ch.description);
							pw.println ("@chClass " + ch.chClass);
							pw.println ("@valueTypeId " + ch.valueTypeId);
							pw.println();
							counter++;
						}
						pw.close();
						osw.close();
						fos.close();
						System.out.println(counter + " CharacteristicTypes of " + chtClass + " class written");
					}
					catch (Exception ex)
					{
						ex.printStackTrace();
					}
				}

		}
	}

	protected static void writeDevices()
	{
		SchemeDevice device;
		new File (devicesDir).mkdirs();

		if (Pool.getMap(SchemeDevice.typ) != null)
		{
			try
			{
				File f = new File(devicesDir + eqType + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);

				pw.println ("AMFICOM component description file");
				pw.println ("@type " + SchemeDevice.typ);
				pw.println();

				for(Iterator it = Pool.getMap(SchemeDevice.typ).values().iterator(); it.hasNext();)
				{
					device = (SchemeDevice)it.next();
					pw.println ("@name " + device.getName());
					pw.println ("@id " + device.getId());
					pw.println ("@ports ");
					for (Iterator pit = device.ports.iterator(); pit.hasNext();)
						pw.println (((SchemePort)pit.next()).getId());
//						SchemePort port = (SchemePort)pit.next();
//						Pool.put(SchemePort.typ, port.getId(), port);
					pw.println ("@end of ports");
					pw.println ("@cableports ");
					for (Iterator pit = device.cableports.iterator(); pit.hasNext();)
						pw.println (((SchemeCablePort)pit.next()).getId());
					pw.println ("@end of cableports");
					pw.println ();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " SchemeDevices written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	protected static void writePorts()
	{
		SchemePort port;
		new File (portsDir).mkdirs();

		if (Pool.getMap(SchemePort.typ) != null)
		{
			try
			{
				File f = new File(portsDir + portType + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);

				pw.println ("AMFICOM component description file");
				pw.println ("@type " + SchemePort.typ);
				pw.println();

				for(Iterator it = Pool.getMap(SchemePort.typ).values().iterator(); it.hasNext();)
				{
					port = (SchemePort)it.next();
					pw.println ("@name " + port.getName());
					pw.println ("@id " + port.getId());
					pw.println ("@portTypeId " + port.portTypeId);
					pw.println ("@linkId " + port.linkId);
					pw.println ("@deviceId " + port.deviceId);
					pw.println ("@accessPortTypeId " + port.measurementPortTypeId);
					pw.println ("@directionType " + port.directionType);
					pw.println ();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " SchemePorts written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	protected static void writeCablePorts()
	{
		SchemeCablePort port;
		new File (portsDir).mkdirs();

		if (Pool.getMap(SchemeCablePort.typ) != null)
		{
			try
			{
				File f = new File(portsDir + cablePortType + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);

				pw.println ("AMFICOM component description file");
				pw.println ("@type " + SchemeCablePort.typ);
				pw.println();

				for(Iterator it = Pool.getMap(SchemeCablePort.typ).values().iterator(); it.hasNext();)
				{
					port = (SchemeCablePort)it.next();
					pw.println ("@name " + port.getName());
					pw.println ("@id " + port.getId());
					pw.println ("@cablePortTypeId " + port.cablePortTypeId);
					pw.println ("@cableLinkId " + port.cableLinkId);
					pw.println ("@deviceId " + port.deviceId);
					pw.println ("@accessPortTypeId " + port.measurementPortTypeId);
					pw.println ("@directionType " + port.directionType);
					pw.println ();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " SchemeCablePorts written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	protected static void writeLinks()
	{
		SchemeLink link;
		new File (linksDir).mkdirs();

		if (Pool.getMap(SchemeLink.typ) != null)
		{
			try
			{
				File f = new File(linksDir + linkType + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);

				pw.println ("AMFICOM component description file");
				pw.println ("@type " + SchemeLink.typ);
				pw.println();

				for(Iterator it = Pool.getMap(SchemeLink.typ).values().iterator(); it.hasNext();)
				{
					link = (SchemeLink)it.next();
					pw.println ("@name " + link.getName());
					pw.println ("@id " + link.getId());
					pw.println ("@linkTypeId " + link.linkTypeId);
					pw.println ("@sourcePortId " + link.sourcePortId);
					pw.println ("@targetPortId " + link.targetPortId);
					pw.println ("@opticalLength " + String.valueOf(link.opticalLength));
					pw.println ("@physicalLength " + String.valueOf(link.physicalLength));
					pw.println ();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " SchemeLinks written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	protected static void writeCableLinks()
	{
		SchemeCableLink link;
		new File (linksDir).mkdirs();

		if (Pool.getMap(SchemeCableLink.typ) != null)
		{
			try
			{
				File f = new File(linksDir + cableLinkType + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);

				pw.println ("AMFICOM component description file");
				pw.println ("@type " + SchemeCableLink.typ);
				pw.println();

				for(Iterator it = Pool.getMap(SchemeCableLink.typ).values().iterator(); it.hasNext();)
				{
					link = (SchemeCableLink)it.next();
					pw.println ("@name " + link.getName());
					pw.println ("@id " + link.getId());
					pw.println ("@cableLinkTypeId " + link.cableLinkTypeId);
					pw.println ("@sourcePortId " + link.sourcePortId);
					pw.println ("@targetPortId " + link.targetPortId);
					pw.println ("@opticalLength " + String.valueOf(link.opticalLength));
					pw.println ("@physicalLength " + String.valueOf(link.physicalLength));
					pw.println ("@cableThreads ");
					for (Iterator tit = link.cableThreads.iterator(); tit.hasNext();)
					{
						SchemeCableThread thread = (SchemeCableThread)tit.next();
						pw.println (thread.getId());
						pw.println (thread.getName());
						pw.println (thread.linkTypeId);
					}
					pw.println ("@end of cableThreads");
					pw.println ();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " SchemeCableLinks written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	protected static void writeProtoElements()
	{
		ProtoElement proto;
		EquipmentType type;
		new File (protoDir).mkdirs();
		new File (proto_schemeDir).mkdirs();
		new File (proto_ugoDir).mkdirs();

		if (Pool.getMap(ProtoElement.typ) != null)
		{
			Map protoClasses = new HashMap();
			for(Iterator it = Pool.getMap(ProtoElement.typ).values().iterator(); it.hasNext();)
			{
				proto = (ProtoElement)it.next();
				type = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipmentTypeId);
				protoClasses.put(type.eqClass, type.eqClass);
			}
			for(Iterator protoClassesEl = protoClasses.values().iterator(); protoClassesEl.hasNext(); )
			{
				String protoClass = (String)protoClassesEl.next();

				List vec = new ArrayList();
				for(Iterator it = Pool.getMap(ProtoElement.typ).values().iterator(); it.hasNext();)
				{
					proto = (ProtoElement)it.next();
					type = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipmentTypeId);
					if (type.eqClass.equals(protoClass))
						vec.add(proto);
				}
				try
				{
					File f = new File(protoDir + protoType + protoClass + ext);
					if (f.exists())
						f.delete();
					f.createNewFile();
					int counter = 0;

					FileOutputStream fos = new FileOutputStream (f);
					OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
					PrintWriter pw = new PrintWriter(osw, true);

					pw.println ("AMFICOM component description file");
					pw.println ("@type " + ProtoElement.typ);
					pw.println();

					for (Iterator pit = vec.iterator(); pit.hasNext();)
					{
						proto = (ProtoElement)pit.next();
						pw.println ("@name " + proto.getName());
						pw.println ("@id " + proto.getId());
						pw.println ("@equipmentTypeId " + proto.equipmentTypeId);
						pw.println ("@devices ");
						for (Iterator it = proto.devices.iterator(); it.hasNext();)
							pw.println (((SchemeDevice)it.next()).getId());
						pw.println ("@end of devices");
						pw.println ("@links ");
						for (Iterator it = proto.links.iterator(); it.hasNext();)
							pw.println (((SchemeLink)it.next()).getId());
						pw.println ("@end of links");
						pw.println ("@protoelements ");
						for (Iterator it = proto.protoelementIds.iterator(); it.hasNext();)
							pw.println (it.next());
						pw.println ("@end of protoelements");
						if (new File(proto_schemeDir+proto.getId()).exists())
							pw.println ("@schemecell " + proto_schemeDir+proto.getId());
						else if (writeVisualElement(proto_schemeDir+proto.getId(), proto.schemecell))
							pw.println ("@schemecell " + proto_schemeDir+proto.getId());
						if (new File(proto_ugoDir+proto.getId()).exists())
							pw.println ("@ugocell " + proto_ugoDir+proto.getId());
						else if (writeVisualElement(proto_ugoDir+proto.getId(), proto.ugo))
							pw.println ("@ugocell " + proto_ugoDir+proto.getId());

						pw.println();
						counter++;
					}
					pw.close();
					osw.close();
					fos.close();
					System.out.println(counter + " ProtoElements of " + protoClass + " class written");
				}
				catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}

	protected static void writeSchemeProtoGroups()
	{
	/*	SchemeProtoGroup mapProto;
		new File (protoDir).mkdirs();
		new File (protoImageDir).mkdirs();

		if (Pool.getMap(SchemeProtoGroup.typ) != null)
		{
			try
			{
				File f = new File(protoDir + mapProto_name + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);

				pw.println ("AMFICOM component description file");
				pw.println ("@type " + SchemeProtoGroup.typ);
				pw.println();

				for(Iterator it = Pool.getMap(SchemeProtoGroup.typ).values().iterator(); it.hasNext();)
				{
					mapProto = (SchemeProtoGroup)it.next();
					pw.println ("@id " + mapProto.getId());
					pw.println ("@name " + mapProto.getName());
					pw.println ("@description " + mapProto.description);
					pw.println ("@symbolId " + mapProto.getImageID());
					pw.println ("@peClass " + mapProto.peClass);
					pw.println ("@peIs_kis " + mapProto.peIs_kis);
					pw.println ("@is_visual " + mapProto.is_visual);
					pw.println ("@isTopological " + mapProto.isTopological);

					ImageResource imr = ImageCatalogue.get(mapProto.getImageID());
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ObjectOutputStream out = new ObjectOutputStream(baos);
					out.writeObject(imr);
					writeVisualElement(protoImageDir + mapProto.getId(), baos.toByteArray());

					pw.println ("@protoelements ");
					for (Iterator pit = mapProto.peIds.iterator(); pit.hasNext();)
						pw.println ((String)pit.next());
					pw.println ("@end of protoelements");
					pw.println ();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " SchemeProtoGroups written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}/
	}
/*
	protected static void writeSchemeProtoGroups()
	{
		SchemeProtoGroup mapProto;
		new File (protoDir).mkdirs();
		new File (protoImageDir).mkdirs();

		if (Pool.getMap(SchemeProtoGroup.typ) != null)
		{
			try
			{
				File f = new File(protoDir + mapProto_group_name + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);

				pw.println ("AMFICOM component description file");
				pw.println ("@type " + SchemeProtoGroup.typ);
				pw.println();

				for(Iterator it = Pool.getMap(SchemeProtoGroup.typ).values().iterator(); it.hasNext();)
				{
					mapProto = (SchemeProtoGroup)it.next();
					pw.println ("@id " + mapProto.getId());
					pw.println ("@name " + mapProto.getName());
					pw.println ("@parentId " + mapProto.parentId);
					pw.println ("@groups ");
					for (Iterator git = mapProto.groupIds.iterator(); git.hasNext();)
						pw.println ((String)git.next());
					pw.println ("@end of groups");
					pw.println ("@mapprotos ");
					for (Iterator mit = mapProto.mapprotoIds.iterator(); mit.hasNext();)
						pw.println ((String)mit.next());
					pw.println ("@end of mapprotos");

					pw.println ();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " SchemeProtoGroups written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
/
	protected static void writeScheme()
	{
		Scheme scheme;
		new File (schemeDir).mkdirs();
		new File (schemeCellDir).mkdirs();
		new File (scheme_ugoDir).mkdirs();

		if (Pool.getMap(Scheme.typ) != null)
		{
			try
			{
				File f = new File(schemeDir + scheme_name + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);

				pw.println ("AMFICOM component description file");
				pw.println ("@type " + Scheme.typ);
				pw.println();

				for(Iterator sit = Pool.getMap(Scheme.typ).values().iterator(); sit.hasNext();)
				{
					scheme = (Scheme)sit.next();
					pw.println ("@id " + scheme.getId());
					pw.println ("@name " + scheme.getName());
					pw.println ("@created " + String.valueOf(scheme.created));
					pw.println ("@createdBy " + scheme.createdBy);
					pw.println ("@description " + scheme.description);
					pw.println ("@domainId " + scheme.domainId);
					pw.println ("@ownerId " + scheme.ownerId);
					pw.println ("@solution " + scheme.solution.getId());
					pw.println ("@cablelinks ");
					for (Iterator it = scheme.cablelinks.iterator(); it.hasNext();)
						pw.println (((SchemeCableLink)it.next()).getId());
					pw.println ("@end of cablelinks");
					pw.println ("@links ");
					for (Iterator it = scheme.links.iterator(); it.hasNext();)
						pw.println (((SchemeLink)it.next()).getId());
					pw.println ("@end of links");
					pw.println ("@elements ");
					for (Iterator it = scheme.elements.iterator(); it.hasNext();)
						pw.println (((SchemeElement)it.next()).getId());
					pw.println ("@end of elements");
//					pw.println ("@paths ");
//					for (Iterator it = scheme.paths.iterator(); it.hasNext();)
//						pw.println (((SchemePath)it.next()).getId());
//					pw.println ("@end of paths");
					pw.println ("@clones ");
					for (Iterator it = scheme.clones.keySet().iterator(); it.hasNext();)
					{
						String id = (String)it.next();
						pw.println (id + " " + (String)scheme.clones.get(id));
					}
					pw.println ("@end of clones");
		/*if (new File(schemeCellDir+scheme.getId()).exists())
		 pw.println ("@schemecell " + schemeCellDir+scheme.getId());
		else/
					pw.println ("@width " + String.valueOf(scheme.width));
		pw.println ("@height " + String.valueOf(scheme.height));
		pw.println ("@schemeType " + scheme.schemeType);

		if (writeVisualElement(schemeCellDir+scheme.getId(), scheme.schemecell))
			pw.println ("@schemecell " + schemeCellDir+scheme.getId());
		if (writeVisualElement(scheme_ugoDir+scheme.getId(), scheme.ugo))
			pw.println ("@ugocell " + scheme_ugoDir+scheme.getId());



		pw.println ();
		counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " Schemes written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	protected static void writeSchemeElements()
	{
		SchemeElement el;
		new File (schemeElementCellDir).mkdirs();
		new File (schemeElement_ugoDir).mkdirs();

		if (Pool.getMap(SchemeElement.typ) != null)
		{
			try
			{
				File f = new File(schemeDir + schemeElement_name + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);

				pw.println ("AMFICOM component description file");
				pw.println ("@type " + SchemeElement.typ);
				pw.println();

				for(Iterator sit = Pool.getMap(SchemeElement.typ).values().iterator(); sit.hasNext();)
				{
					el = (SchemeElement)sit.next();
					pw.println ("@id " + el.getId());
					pw.println ("@name " + el.getName());
					//   pw.println ("@equipmentId " + el.equipmentId);
					pw.println ("@protoElementId " + el.protoElementId);

					pw.println ("@devices ");
					for (Iterator it = el.devices.iterator(); it.hasNext();)
						pw.println (((SchemeDevice)it.next()).getId());
					pw.println ("@end of devices");
					pw.println ("@elements ");
					//for (Enumeration e = el.elementIds.elements(); e.hasMoreElements();)
					for (Iterator it = el.getChildElements().iterator(); it.hasNext();)
						pw.println (((SchemeElement)it.next()).getId());
					pw.println ("@end of elements");
					pw.println ("@links ");
					for (Iterator it = el.links.iterator(); it.hasNext();)
						pw.println (((SchemeLink)it.next()).getId());
					pw.println ("@end of links");
/*				if (new File(schemeElementDir+el.getId()).exists())
		 pw.println ("@schemecell " + schemeElementDir + el.getId());
		else /
					if (writeVisualElement(schemeElementCellDir+el.getId(), el.schemecell))
						pw.println ("@schemecell " + schemeElementCellDir+el.getId());
					if (writeVisualElement(schemeElement_ugoDir+el.getId(), el.ugo))
						pw.println ("@ugocell " + schemeElement_ugoDir+el.getId());

					pw.println ();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " SchemeElements written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	protected static void writeSchemePaths()
	{
		SchemePath path;
		new File (schemeDir).mkdirs();

		if (Pool.getMap(SchemePath.typ) != null)
		{
			try
			{
				File f = new File(schemeDir + schemePath_name + ext);
				if (f.exists())
					f.delete();
				f.createNewFile();
				int counter = 0;

				FileOutputStream fos = new FileOutputStream (f);
				OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-16");
				PrintWriter pw = new PrintWriter(osw, true);

				pw.println ("AMFICOM component description file");
				pw.println ("@type " + SchemePath.typ);
				pw.println();

				for(Iterator it = Pool.getMap(SchemePath.typ).values().iterator(); it.hasNext();)
				{
					path = (SchemePath)it.next();
					pw.println ("@id " + path.getId());
					pw.println ("@name " + path.getName());
					pw.println ("@startDeviceId " + path.startDeviceId);
					pw.println ("@endDeviceId " + path.endDeviceId);
					pw.println ("@typeId " + path.typeId);
					pw.println ("@pathelements ");
					for (Iterator lit = path.links.iterator(); lit.hasNext();)
					{
						PathElement pe = (PathElement)lit.next();
						pw.println (String.valueOf(pe.n));
						pw.println (String.valueOf(pe.isCable));
						pw.println (pe.linkId);
						pw.println (pe.threadId);
						pw.println (pe.startPortId);
						pw.println (pe.endPortId);
					}
					pw.println ("@end of pathelements");
					pw.println ();
					counter++;
				}
				pw.close();
				osw.close();
				fos.close();
				System.out.println(counter + " SchemePaths written");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	static protected boolean writeVisualElement(String name, byte[] b)
	{
		try
		{
			OutputStream out = createOutputStream(name);
			out.write(b);
			out.flush();
			out.close();
			return true;
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
	}

	static protected byte[] readVisualElement(String name)
	{
		Object obj;
		try
		{
			InputStream in = createInputStream(name);
			byte[] b = new byte[(int)(new File(name).length())];

			in.read(b);
			in.close();
			return b;
		}
		catch (Exception ex)
		{
			System.err.println("Error reading " + ex.getMessage());
			return new byte[0];
		}
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////  Чтение из файлов
	///////////////////////////////////////////////////////////////////////////
	protected static void readTypes()
	{
		File[] files = new File(typeDir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Map h = new HashMap();
			String filename = files[i].getName();
			System.out.println("loading..." + filename);
			if (filename.startsWith(eqType) && !files[i].isDirectory())
			{
				h = Pool.getMap(EquipmentType.typ);
				if (h == null)
				{
					h = new HashMap();
					Pool.putMap(EquipmentType.typ, h);
				}
				h.putAll(loadEquipmentTypes(files[i]));
			}
			else if (filename.startsWith(linkType) && !files[i].isDirectory())
			{
				Pool.putMap(LinkType.typ, loadLinkTypes(files[i]));
			}
			else if (filename.startsWith(cableLinkType) && !files[i].isDirectory())
			{
				Pool.putMap(CableLinkType.typ, loadCableLinkTypes(files[i]));
			}
			else if (filename.startsWith(portType) && !files[i].isDirectory())
			{
				Pool.putMap(PortType.typ, loadPortTypes(files[i]));
			}
			else if (filename.startsWith(cablePortType) && !files[i].isDirectory())
			{
				Pool.putMap(CablePortType.typ, loadCablePortTypes(files[i]));
			}
			else if (filename.startsWith(testportType) && !files[i].isDirectory())
			{
				Pool.putMap(TestPortType.typ, loadTestPortTypes(files[i]));
			}
			else if (filename.startsWith(accessportType) && !files[i].isDirectory())
			{
				Pool.putMap(AccessPortType.typ, loadAccessPortTypes(files[i]));
			}
			else if (filename.startsWith(characteristicType) && !files[i].isDirectory())
			{
				h = Pool.getMap(CharacteristicType.typ);
				if (h == null)
				{
					h = new HashMap();
					Pool.putMap(CharacteristicType.typ, h);
				}
				h.putAll(loadCharacteristicTypes(files[i]));
			}
		}
	}

	static protected Map loadEquipmentTypes(File file)
	{
		Map h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(EquipmentType.typ))
				return null;

			EquipmentType eqt = new EquipmentType();
			h = new HashMap();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					eqt = new EquipmentType();
				else if (s[0].equals("@name"))
					eqt.name = s[1];
				else if (s[0].equals("@id"))
				{
					eqt.id = s[1];
					h.put(eqt.id, eqt);
				}
				else if (s[0].equals("@description"))
					eqt.description = s[1];
				else if (s[0].equals("@eqClass"))
					eqt.eqClass = s[1];
				else if (s[0].equals("@imageId"))
					eqt.imageId = s[1];
				else if (s[0].equals("@manufacturer"))
					eqt.manufacturer = s[1];
				else if (s[0].equals("@manufacturerCode"))
					eqt.manufacturerCode = s[1];
				else if (s[0].equals("@characteristics"))
				{
					eqt.characteristics = new HashMap();
					String[] ch = analyseString(isr.readASCIIString());
					while (!ch[0].startsWith("@end"))
					{
//						eqt.characteristics.put(ch[0], ch[1]);
//						ch = analyseString(isr.readASCIIString());
						Characteristic chr = new Characteristic(
								"id" + System.currentTimeMillis(),
								"",
								"",
								"",
								ch[0],
								ch[1],
								"");
						eqt.characteristics.put(chr.type_id, chr);
						ch = analyseString(isr.readASCIIString());
					}
				}
			}
			isr.close();
			fis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return h;
	}

	static protected Map loadLinkTypes(File file)
	{
		Map h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(LinkType.typ))
				return null;

			LinkType lt = new LinkType();
			h = new HashMap();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					lt = new LinkType();
				else if (s[0].equals("@name"))
					lt.name = s[1];
				else if (s[0].equals("@id"))
				{
					lt.id = s[1];
					h.put(lt.id, lt);
				}
				else if (s[0].equals("@description"))
					lt.description = s[1];
				else if (s[0].equals("@imageId"))
					lt.imageId = s[1];
				else if (s[0].equals("@linkClass"))
					lt.linkClass = s[1];
				else if (s[0].equals("@manufacturer"))
					lt.manufacturer = s[1];
				else if (s[0].equals("@manufacturerCode"))
					lt.manufacturerCode = s[1];
				else if (s[0].equals("@characteristics"))
				{
					lt.characteristics = new HashMap();
					String[] ch = analyseString(isr.readASCIIString());
					while (!ch[0].startsWith("@end"))
					{
//						lt.characteristics.put(ch[0], ch[1]);
//						ch = analyseString(isr.readASCIIString());
						Characteristic chr = new Characteristic(
								"id" + System.currentTimeMillis(),
								"",
								"",
								"",
								ch[0],
								ch[1],
								"");
						lt.characteristics.put(ch[0], chr);
						ch = analyseString(isr.readASCIIString());
					}
				}
			}
			isr.close();
			fis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return h;
	}

	static protected Map loadCableLinkTypes(File file)
	{
		Map h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(CableLinkType.typ))
				return null;

			CableLinkType lt = new CableLinkType();
			h = new HashMap();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					lt = new CableLinkType();
				else if (s[0].equals("@name"))
					lt.name = s[1];
				else if (s[0].equals("@id"))
				{
					lt.id = s[1];
					h.put(lt.id, lt);
				}
				else if (s[0].equals("@description"))
					lt.description = s[1];
				else if (s[0].equals("@imageId"))
					lt.imageId = s[1];
				else if (s[0].equals("@linkClass"))
					lt.linkClass = s[1];
				else if (s[0].equals("@manufacturer"))
					lt.manufacturer = s[1];
				else if (s[0].equals("@manufacturerCode"))
					lt.manufacturerCode = s[1];
				else if (s[0].equals("@characteristics"))
				{
					lt.characteristics = new HashMap();
					String[] ch = analyseString(isr.readASCIIString());
					while (!ch[0].startsWith("@end"))
					{
//						lt.characteristics.put(ch[0], ch[1]);
//						ch = analyseString(isr.readASCIIString());
						Characteristic chr = new Characteristic(
								"id" + System.currentTimeMillis(),
								"",
								"",
								"",
								ch[0],
								ch[1],
								"");
						lt.characteristics.put(ch[0], chr);
						ch = analyseString(isr.readASCIIString());
					}
				}
			}
			isr.close();
			fis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return h;
	}


	static protected Map loadPortTypes(File file)
	{
		Map h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(PortType.typ))
				return null;

			PortType pt = new PortType();
			h = new HashMap();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					pt = new PortType();
				else if (s[0].equals("@name"))
					pt.name = s[1];
				else if (s[0].equals("@id"))
				{
					pt.id = s[1];
					h.put(pt.id, pt);
				}
				else if (s[0].equals("@pClass"))
					pt.pClass = s[1];
				else if (s[0].equals("@description"))
					pt.description = s[1];
				else if (s[0].equals("@interfaceId"))
					pt.interfaceId = s[1];
				else if (s[0].equals("@characteristics"))
				{
					pt.characteristics = new HashMap();
					String[] ch = analyseString(isr.readASCIIString());
					while (!ch[0].startsWith("@end"))
					{
//						pt.characteristics.put(ch[0], ch[1]);
//						ch = analyseString(isr.readASCIIString());
						Characteristic chr = new Characteristic(
								"id" + System.currentTimeMillis(),
								"",
								"",
								"",
								ch[0],
								ch[1],
								"");
						pt.characteristics.put(ch[0], chr);
						ch = analyseString(isr.readASCIIString());
					}
				}
			}
			isr.close();
			fis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return h;
	}

	static protected Map loadCablePortTypes(File file)
	{
		Map h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(CablePortType.typ))
				return null;

			CablePortType cpt = new CablePortType();
			h = new HashMap();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					cpt = new CablePortType();
				else if (s[0].equals("@name"))
					cpt.name = s[1];
				else if (s[0].equals("@id"))
				{
					cpt.id = s[1];
					h.put(cpt.id, cpt);
				}
				else if (s[0].equals("@description"))
					cpt.description = s[1];
				else if (s[0].equals("@interfaceId"))
					cpt.interfaceId = s[1];
				else if (s[0].equals("@characteristics"))
				{
					cpt.characteristics = new HashMap();
					String[] ch = analyseString(isr.readASCIIString());
					while (!ch[0].startsWith("@end"))
					{
//						cpt.characteristics.put(ch[0], ch[1]);
//						ch = analyseString(isr.readASCIIString());
						Characteristic chr = new Characteristic(
								"id" + System.currentTimeMillis(),
								"",
								"",
								"",
								ch[0],
								ch[1],
								"");
						cpt.characteristics.put(ch[0], chr);
						ch = analyseString(isr.readASCIIString());
					}
				}
			}
			isr.close();
			fis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return h;
	}

	static protected Map loadTestPortTypes(File file)
	{
		Map h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(TestPortType.typ))
				return null;

			TestPortType tpt = new TestPortType();
			h = new HashMap();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					tpt = new TestPortType();
				else if (s[0].equals("@name"))
					tpt.name = s[1];
				else if (s[0].equals("@id"))
				{
					tpt.id = s[1];
					h.put(tpt.id, tpt);
				}
				else if (s[0].equals("@description"))
					tpt.description = s[1];
				else if (s[0].equals("@portTypeId"))
					tpt.portTypeId = s[1];
				else if (s[0].equals("@characteristics"))
				{
					tpt.characteristics = new HashMap();
					String[] ch = analyseString(isr.readASCIIString());
					while (!ch[0].startsWith("@end"))
					{
//						tpt.characteristics.put(ch[0], ch[1]);
//						ch = analyseString(isr.readASCIIString());
						Characteristic chr = new Characteristic(
								"id" + System.currentTimeMillis(),
								"",
								"",
								"",
								ch[0],
								ch[1],
								"");
						tpt.characteristics.put(ch[0], chr);
						ch = analyseString(isr.readASCIIString());
					}
				}
			}
			isr.close();
			fis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return h;
	}

	static protected Map loadAccessPortTypes(File file)
	{
		Map h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(AccessPortType.typ))
				return null;

			AccessPortType apt = new AccessPortType();
			h = new HashMap();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					apt = new AccessPortType();
				else if (s[0].equals("@name"))
					apt.name = s[1];
				else if (s[0].equals("@id"))
				{
					apt.id = s[1];
					h.put(apt.id, apt);
				}
				else if (s[0].equals("@description"))
					apt.description = s[1];
				else if (s[0].equals("@accessType"))
					apt.accessType = s[1];
				else if (s[0].equals("@characteristics"))
				{
					apt.characteristics = new HashMap();
					String[] ch = analyseString(isr.readASCIIString());
					while (!ch[0].startsWith("@end"))
					{
//						apt.characteristics.put(ch[0], ch[1]);
//						ch = analyseString(isr.readASCIIString());
						Characteristic chr = new Characteristic(
								"id" + System.currentTimeMillis(),
								"",
								"",
								"",
								ch[0],
								ch[1],
								"");
						apt.characteristics.put(ch[0], chr);
						ch = analyseString(isr.readASCIIString());
					}
				}
			}
			isr.close();
			fis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return h;
	}

	static protected Map loadCharacteristicTypes(File file)
	{
		Map h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(CharacteristicType.typ))
				return null;

			CharacteristicType ch = new CharacteristicType();
			h = new HashMap();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					ch = new CharacteristicType();
				else if (s[0].equals("@name"))
					ch.name = s[1];
				else if (s[0].equals("@id"))
				{
					ch.id = s[1];
					h.put(ch.id, ch);
				}
				else if (s[0].equals("@description"))
					ch.description = s[1];
				else if (s[0].equals("@chClass"))
					ch.chClass = s[1];
				else if (s[0].equals("@valueTypeId"))
					ch.valueTypeId = s[1];
			}
			isr.close();
			fis.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return h;
	}

	protected static void readPorts()
	{
		File[] files = new File(portsDir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Map h = new HashMap();
			String filename = files[i].getName();
			if (filename.startsWith(portType) && !files[i].isDirectory())
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(SchemePort.typ))
						return;

					SchemePort port = new SchemePort("");
					h = new HashMap();
					while (isr.ready())
					{
						String s[] = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							port = new SchemePort("");
						else if (s[0].equals("@name"))
							port.name = s[1];
						else if (s[0].equals("@id"))
						{
							port.id = s[1];
							h.put(port.id, port);
						}
						else if (s[0].equals("@portTypeId"))
							port.portTypeId = s[1];
						else if (s[0].equals("@linkId"))
							port.linkId = s[1];
						else if (s[0].equals("@deviceId"))
							port.deviceId = s[1];
						else if (s[0].equals("@accessPortTypeId"))
							port.measurementPortTypeId = s[1];
						else if (s[0].equals("@directionType"))
							port.directionType = s[1];
					}
					isr.close();
					fis.close();
					Map old = Pool.getMap(SchemePort.typ);
					if (old == null)
						Pool.putMap(SchemePort.typ, h);
					else
						old.putAll(h);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
	}

	protected static void readCablePorts()
	{
		File[] files = new File(portsDir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Map h = new HashMap();
			String filename = files[i].getName();
			if (filename.startsWith(cablePortType) && !files[i].isDirectory())
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(SchemeCablePort.typ))
						return;

					SchemeCablePort port = new SchemeCablePort("");
					h = new HashMap();
					while (isr.ready())
					{
						String s[] = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							port = new SchemeCablePort("");
						else if (s[0].equals("@name"))
							port.name = s[1];
						else if (s[0].equals("@id"))
						{
							port.id = s[1];
							h.put(port.id, port);
						}
						else if (s[0].equals("@cablePortTypeId"))
							port.cablePortTypeId = s[1];
						else if (s[0].equals("@cableLinkId"))
							port.cableLinkId = s[1];
						else if (s[0].equals("@deviceId"))
							port.deviceId = s[1];
						else if (s[0].equals("@accessPortTypeId"))
							port.measurementPortTypeId = s[1];
						else if (s[0].equals("@directionType"))
							port.directionType = s[1];
					}
					isr.close();
					fis.close();
					Map old = Pool.getMap(SchemeCablePort.typ);
					if (old == null)
						Pool.putMap(SchemeCablePort.typ, h);
					else
						old.putAll(h);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
	}

	protected static void readLinks()
	{
		File[] files = new File(linksDir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Map h = new HashMap();
			String filename = files[i].getName();
			if (filename.startsWith(linkType) && !files[i].isDirectory())
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(SchemeLink.typ))
						return;

					SchemeLink link = new SchemeLink("");
					h = new HashMap();
					while (isr.ready())
					{
						String s[] = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							link = new SchemeLink("");
						else if (s[0].equals("@name"))
							link.name = s[1];
						else if (s[0].equals("@id"))
						{
							link.id = s[1];
							h.put(link.id, link);
						}
						else if (s[0].equals("@linkTypeId"))
							link.linkTypeId = s[1];
						else if (s[0].equals("@sourcePortId"))
							link.sourcePortId = s[1];
						else if (s[0].equals("@targetPortId"))
							link.targetPortId = s[1];
						else if (s[0].equals("@opticalLength"))
							link.opticalLength = Double.parseDouble(s[1]);
						else if (s[0].equals("@physicalLength"))
							link.physicalLength = Double.parseDouble(s[1]);
					}
					isr.close();
					fis.close();
					Map old = Pool.getMap(SchemeLink.typ);
					if (old == null)
						Pool.putMap(SchemeLink.typ, h);
					else
						old.putAll(h);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
	}

	protected static void readCableLinks()
	{
		File[] files = new File(linksDir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Map h = new HashMap();
			String filename = files[i].getName();
			if (filename.startsWith(cableLinkType) && !files[i].isDirectory())
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(SchemeCableLink.typ))
						return;

					SchemeCableLink link = new SchemeCableLink("");
					h = new HashMap();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							link = new SchemeCableLink("");
						else if (s[0].equals("@name"))
							link.setName(s[1]);
						else if (s[0].equals("@id"))
						{
							link.id = s[1];
							h.put(link.id, link);
						}
						else if (s[0].equals("@cableLinkTypeId"))
							link.cableLinkTypeId = s[1];
						else if (s[0].equals("@sourcePortId"))
							link.sourcePortId = s[1];
						else if (s[0].equals("@targetPortId"))
							link.targetPortId = s[1];
						else if (s[0].equals("@opticalLength"))
							link.opticalLength = Double.parseDouble(s[1]);
						else if (s[0].equals("@physicalLength"))
							link.physicalLength = Double.parseDouble(s[1]);
						else if (s[0].equals("@cableThreads"))
						{
							link.cableThreads = new ArrayList();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								SchemeCableThread thread = new SchemeCableThread("");
								thread.id = s[0];
								s = analyseString(isr.readASCIIString());
								thread.name = s[0];
								s = analyseString(isr.readASCIIString());
								thread.linkTypeId = s[0];
								link.cableThreads.add(thread);
								s = analyseString(isr.readASCIIString());
							}
						}
					}
					isr.close();
					fis.close();
					Map old = Pool.getMap(SchemeCableLink.typ);
					if (old == null)
						Pool.putMap(SchemeCableLink.typ, h);
					else
						old.putAll(h);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
	}

	protected static void readDevices()
	{
		File[] files = new File(devicesDir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Map h = new HashMap();
			String filename = files[i].getName();
			if (filename.startsWith(eqType) && !files[i].isDirectory())
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(SchemeDevice.typ))
						return;

					SchemeDevice device = new SchemeDevice("");
					h = new HashMap();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							device = new SchemeDevice("");
						else if (s[0].equals("@name"))
							device.name = s[1];
						else if (s[0].equals("@id"))
						{
							device.id = s[1];
							h.put(device.id, device);
						}
						else if (s[0].equals("@ports"))
						{
							device.ports = new ArrayList();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								SchemePort port = (SchemePort)Pool.get(SchemePort.typ, s[0]);
								if (port == null)
									System.out.println("Error while reading device " + device.getId() + ". Port " + s[0]+ " not found ");
								device.ports.add(port);
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@cableports"))
						{
							device.cableports = new ArrayList();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								SchemeCablePort port = (SchemeCablePort)Pool.get(SchemeCablePort.typ, s[0]);
								if (port == null)
									System.out.println("Error while reading device " + device.getId() + ". Cableport " + s[0]+ " not found ");
								device.cableports.add(port);
								s = analyseString(isr.readASCIIString());
							}
						}
					}
					isr.close();
					fis.close();
					Map old = Pool.getMap(SchemeDevice.typ);
					if (old == null)
						Pool.putMap(SchemeDevice.typ, h);
					else
						old.putAll(h);

				}
				catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
	}

	protected static void readProtoElements()
	{
		File[] files = new File(protoDir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Map h = new HashMap();
			String filename = files[i].getName();
			if (filename.startsWith(protoType) && !files[i].isDirectory())
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(ProtoElement.typ))
						return;

					ProtoElement proto = new ProtoElement("");
					h = new HashMap();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							proto = new ProtoElement("");
						else if (s[0].equals("@name"))
							proto.name = s[1];
						else if (s[0].equals("@id"))
						{
							proto.id = s[1];
							h.put(proto.id, proto);
						}
						else if (s[0].equals("@equipmentTypeId"))
							proto.equipmentTypeId = s[1];
						else if (s[0].equals("@devices"))
						{
							proto.devices = new ArrayList();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								SchemeDevice dev = (SchemeDevice)Pool.get(SchemeDevice.typ, s[0]);
								if (dev == null)
									System.out.println("Error while reading proto " + proto.getId() + ". Device " + s[0]+ " not found ");
								proto.devices.add(dev);
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@links"))
						{
							proto.links = new ArrayList();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								SchemeLink link = (SchemeLink)Pool.get(SchemeLink.typ, s[0]);
								if (link == null)
									System.out.println("Error while reading proto " + proto.getId() + ". Link " + s[0]+ " not found ");
								proto.links.add(link);
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@protoelements"))
						{
							proto.protoelementIds = new ArrayList();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								proto.protoelementIds.add(s[0]);
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@schemecell"))
						{
							proto.schemecell = readVisualElement(s[1]);
						}
						else if (s[0].equals("@ugocell"))
						{
							proto.ugo = readVisualElement(s[1]);
						}
					}

					isr.close();
					fis.close();
					Map old = Pool.getMap(ProtoElement.typ);
					if (old == null)
						Pool.putMap(ProtoElement.typ, h);
					else
						old.putAll(h);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
	}

	protected static void readMapProto()
	{
		File[] files = new File(protoDir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Map h = new HashMap();
			String filename = files[i].getName();
			if (filename.startsWith(mapProto_name) && !files[i].isDirectory())
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(MapProtoElement.typ))
						return;

					MapProtoElement mapproto = new MapProtoElement();
					h = new HashMap();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							mapproto = new MapProtoElement();
						else if (s[0].equals("@name"))
							mapproto.name = s[1];
						else if (s[0].equals("@id"))
						{
							mapproto.id = s[1];
							h.put(mapproto.id, mapproto);
						}
						else if (s[0].equals("@description"))
							mapproto.description = s[1];
						else if (s[0].equals("@symbolId"))
						{
							try
							{
								byte[] b = readVisualElement(protoImageDir + mapproto.getId());
								ByteArrayInputStream bais = new ByteArrayInputStream(b);
								ObjectInputStream in = new ObjectInputStream(bais);
								ImageResource imr = null;
								imr = (ImageResource)in.readObject();
								if (imr != null)
									ImageCatalogue.add(imr.getId(), imr);
							}
							catch (Exception ex)
							{
								ex.printStackTrace();
							}

							mapproto.setImageID(s[1]);
						}
						else if (s[0].equals("@peClass"))
							mapproto.peClass = s[1];
						else if (s[0].equals("@peIs_kis"))
							mapproto.peIs_kis = Boolean.valueOf(s[1]).booleanValue();
						else if (s[0].equals("@is_visual"))
							mapproto.is_visual = Boolean.valueOf(s[1]).booleanValue();
						else if (s[0].equals("@isTopological"))
							mapproto.isTopological = Boolean.valueOf(s[1]).booleanValue();

						else if (s[0].equals("@protoelements"))
						{
							mapproto.peIds = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								mapproto.peIds.add(s[0]);
								s = analyseString(isr.readASCIIString());
							}
						}
					}
					isr.close();
					fis.close();
					Map old = Pool.getMap(MapProtoElement.typ);
					if (old == null)
						Pool.putMap(MapProtoElement.typ, h);
					else
						old.putAll(h);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
	}

	protected static void readMapProtoGroups()
	{
		File[] files = new File(protoDir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Map h = new HashMap();
			String filename = files[i].getName();
			if (filename.startsWith(mapProto_group_name) && !files[i].isDirectory())
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(MapProtoGroup.typ))
						return;

					MapProtoGroup mapproto = new MapProtoGroup();
					h = new HashMap();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							mapproto = new MapProtoGroup();
						else if (s[0].equals("@name"))
							mapproto.name = s[1];
						else if (s[0].equals("@id"))
						{
							mapproto.id = s[1];
							h.put(mapproto.id, mapproto);
						}
						else if (s[0].equals("@parentId"))
							mapproto.parentId = s[1];
						else if (s[0].equals("@groups"))
						{
							mapproto.groupIds = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								mapproto.groupIds.add(s[0]);
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@mapprotos"))
						{
							mapproto.mapprotoIds = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								mapproto.mapprotoIds.add(s[0]);
								s = analyseString(isr.readASCIIString());
							}
						}
					}
					isr.close();
					fis.close();
					Map old = Pool.getMap(MapProtoGroup.typ);
					if (old == null)
						Pool.putMap(MapProtoGroup.typ, h);
					else
						old.putAll(h);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
	}

	protected static void readScheme()
	{
		File[] files = new File(schemeDir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Map h = new HashMap();
			String filename = files[i].getName();
			if (filename.startsWith(scheme_name) && !files[i].isDirectory())
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(Scheme.typ))
						return;

					Scheme scheme = new Scheme();
					h = new HashMap();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							scheme = new Scheme();
						else if (s[0].equals("@name"))
							scheme.name = s[1];
						else if (s[0].equals("@id"))
						{
							scheme.id = s[1];
							h.put(scheme.id, scheme);
						}
						else if (s[0].equals("@created"))
							scheme.created = Long.parseLong(s[1]);
						else if (s[0].equals("@createdBy"))
							scheme.createdBy = s[1];
						else if (s[0].equals("@description"))
							scheme.description = s[1];
						else if (s[0].equals("@domainId"))
							scheme.domainId = s[1];
						else if (s[0].equals("@ownerId"))
							scheme.ownerId = s[1];
						else if (s[0].equals("@solution"))
							scheme.solution = (SolutionCompact)Pool.get(SolutionCompact.typ, s[1]);
						else if (s[0].equals("@cablelinks"))
						{
							scheme.cablelinks = new ArrayList();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								SchemeCableLink link = (SchemeCableLink)Pool.get(SchemeCableLink.typ, s[0]);
								if (link == null)
									System.out.println("Error while reading scheme " + scheme.getId() + ". Cablelink " + s[0]+ " not found ");
								else
									scheme.cablelinks.add(link);
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@links"))
						{
							scheme.links = new ArrayList();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								SchemeLink link = (SchemeLink)Pool.get(SchemeLink.typ, s[0]);
								if (link == null)
									System.out.println("Error while reading scheme " + scheme.getId() + ". Link " + s[0]+ " not found ");
								else
									scheme.links.add(link);
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@elements"))
						{
							scheme.elements = new ArrayList();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								SchemeElement el = (SchemeElement)Pool.get(SchemeElement.typ, s[0]);
								if (el == null)
									System.out.println("Error while reading scheme " + scheme.getId() + ". Element " + s[0]+ " not found ");
								scheme.elements.add(el);
								s = analyseString(isr.readASCIIString());
							}
						}
//						else if (s[0].equals("@paths"))
//						{
//							scheme.paths = new ArrayList();
//							s = analyseString(isr.readASCIIString());
//							while (!s[0].startsWith("@end"))
//							{
//								SchemePath path = (SchemePath)Pool.get(SchemePath.typ, s[0]);
//								if (path == null)
//									System.out.println("Error while reading scheme " + scheme.getId() + ". Path " + s[0]+ " not found ");
//								scheme.paths.add(path);
//								s = analyseString(isr.readASCIIString());
//							}
//						}
						else if (s[0].equals("@clones"))
						{
							scheme.clones = new HashMap();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								scheme.clones.put(s[0], s[1]);
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@width"))
							scheme.width = Integer.parseInt(s[1]);
						else if (s[0].equals("@height"))
							scheme.height = Integer.parseInt(s[1]);
						else if (s[0].equals("@schemeType"))
							scheme.schemeType = s[1];
						else if (s[0].equals("@schemecell"))
						{
							scheme.schemecell = readVisualElement(s[1]);
						}
						else if (s[0].equals("@ugocell"))
						{
							scheme.ugo = readVisualElement(s[1]);
						}
					}
					isr.close();
					fis.close();
					Map old = Pool.getMap(Scheme.typ);
					if (old == null)
						Pool.putMap(Scheme.typ, h);
					else
						old.putAll(h);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
	}

	protected static void readSchemeElements()
	{
		File[] files = new File(schemeDir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Map h = new HashMap();
			String filename = files[i].getName();
			if (filename.startsWith(schemeElement_name) && !files[i].isDirectory())
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(SchemeElement.typ))
						return;

					SchemeElement se = new SchemeElement("");
					h = new HashMap();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							se = new SchemeElement("");
						else if (s[0].equals("@name"))
							se.name = s[1];
						else if (s[0].equals("@id"))
						{
							se.id = s[1];
							h.put(se.id, se);
						}
						else if (s[0].equals("@equipmentId"))
							se.equipmentId = s[1];
						else if (s[0].equals("@protoElementId"))
							se.protoElementId = s[1];

						else if (s[0].equals("@devices"))
						{
							se.devices = new ArrayList();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								SchemeDevice dev = (SchemeDevice)Pool.get(SchemeDevice.typ, s[0]);
								if (dev == null)
									System.out.println("Error while reading element  " + se.getId() + ". Device " + s[0]+ " not found ");
								se.devices.add(dev);
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@elements"))
						{
							se.elementIds = new ArrayList();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								se.elementIds.add(s[0]);
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@links"))
						{
							se.links = new ArrayList();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								SchemeLink link = (SchemeLink)Pool.get(SchemeLink.typ, s[0]);
								if (link == null)
									System.out.println("Error while reading element  " + se.getId() + ". Link " + s[0]+ " not found ");
								se.links.add(link);
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@schemecell"))
						{
							se.schemecell = readVisualElement(s[1]);
						}
						else if (s[0].equals("@ugocell"))
						{
							se.ugo = readVisualElement(s[1]);
						}
					}
					isr.close();
					fis.close();
					Map old = Pool.getMap(SchemeElement.typ);
					if (old == null)
						Pool.putMap(SchemeElement.typ, h);
					else
						old.putAll(h);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
	}

	protected static void readSchemePaths()
	{
		File[] files = new File(schemeDir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Map h = new HashMap();
			String filename = files[i].getName();
			if (filename.startsWith(schemePath_name) && !files[i].isDirectory())
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(SchemePath.typ))
						return;

					SchemePath path = new SchemePath("");
					h = new HashMap();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							path = new SchemePath("");
						else if (s[0].equals("@name"))
							path.name = s[1];
						else if (s[0].equals("@id"))
						{
							path.id = s[1];
							h.put(path.id, path);
						}
						else if (s[0].equals("@startDeviceId"))
							path.startDeviceId = s[1];
						else if (s[0].equals("@endDeviceId"))
							path.endDeviceId = s[1];
						else if (s[0].equals("@typeId"))
							path.typeId = s[1];
						else if (s[0].equals("@pathelements"))
						{
							path.links = new ArrayList();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								PathElement pe = new PathElement();
								pe.n = Integer.parseInt(s[0]);
								s = analyseString(isr.readASCIIString());
								pe.isCable = Boolean.valueOf(s[0]).booleanValue();
								s = analyseString(isr.readASCIIString());
								pe.linkId = s[0];
								s = analyseString(isr.readASCIIString());
								pe.threadId = s[0];
								s = analyseString(isr.readASCIIString());
//								pe.startPortId = s[0];
								s = analyseString(isr.readASCIIString());
//								pe.endPortId = s[0];
								path.links.add(pe);
								s = analyseString(isr.readASCIIString());
							}
						}
					}
					isr.close();
					fis.close();
					Map old = Pool.getMap(SchemePath.typ);
					if (old == null)
						Pool.putMap(SchemePath.typ, h);
					else
						old.putAll(h);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
			}
		}
	}

	static String getType (IntelStreamReader isr) throws IOException
	{
		if (!isr.readASCIIString().equals("AMFICOM component description file"))
			return "";
		String[] s = analyseString (isr.readASCIIString());
		if (s[0].equals("@type"))
			return s[1];
		else
			return "";
	}

	static String[] analyseString (String s)
	{
		int n = s.indexOf(" ");
		if (n == -1)
			return new String[] {s, ""};
		String s1 = s.substring(0, n);
		String s2 = s.substring(n + 1, s.length());
		return new String[] {s1, s2};
	}

	static InputStream createInputStream(String filename) throws Exception
	{
		InputStream f = new FileInputStream(filename);
		return f;
	}

	static OutputStream createOutputStream(String filename) throws Exception
	{
		File file = new File(filename);
		if (file.exists())
			file.delete();
		file.createNewFile();

		OutputStream f = new FileOutputStream(filename);
		return f;
	}
}

*/