package com.syrus.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Hashtable;
import java.util.Vector;

import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.AccessPortType;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.Network.Characteristic;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CableLinkType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CablePortType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CharacteristicType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.PortType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.TestPortType;
import com.syrus.AMFICOM.Client.Resource.Scheme.PathElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCablePort;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeCableThread;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeDevice;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemeLink;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePath;
import com.syrus.AMFICOM.Client.Resource.Scheme.SchemePort;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class CopyDirectoryToFile
{
	public static final String type_dir = "resources/types/";
	public static final String eq_type = "equipment";
	public static final String link_type = "link";
	public static final String cable_link_type = "cablelink";
	public static final String port_type = "port";
	public static final String cable_port_type = "cableport";
	public static final String testport_type = "testport";
	public static final String accessport_type = "accessport";
	public static final String characteristic_type = "characteristic";
	public static final String ext = ".txt";

	public static final String proto_dir = "resources/proto/";
	public static final String proto_scheme_dir = "resources/proto/img/";
	public static final String proto_type = "equipment";
	public static final String map_proto_name = "mapproto";

	public static final String scheme_dir = "resources/scheme/";
	public static final String scheme_scheme_dir = "resources/scheme/img/";
	public static final String scheme_name = "scheme";
	public static final String scheme_element_name = "elements";
	public static final String scheme_path_name = "path";

	public static final String links_dir = "resources/links/";
	public static final String ports_dir = "resources/ports/";
	public static final String devices_dir = "resources/devices/";

	public static void readAll(DataSourceInterface dataSource)
	{
		readTypes(dataSource);
		readPorts(dataSource);
		readCablePorts(dataSource);
		readLinks(dataSource);
		readCableLinks(dataSource);
		readDevices(dataSource);
		readProtoElements(dataSource);
		readMapProto(dataSource);
		readSchemePaths(dataSource);
		readSchemeElements(dataSource);
		readScheme(dataSource);
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
			return null;
		}
	}

	///////////////////////////////////////////////////////////////////////////
	///////////////  Чтение из файлов
	///////////////////////////////////////////////////////////////////////////
	protected static void readTypes(DataSourceInterface dataSource)
	{
		File[] files = new File(type_dir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Hashtable h = new Hashtable();
			String filename = files[i].getName();
			System.out.println("loading..." + filename);
			if (filename.startsWith(eq_type))
			{
				h = Pool.getHash(EquipmentType.typ);
				if (h == null)
				{
					h = new Hashtable();
					Pool.putHash(EquipmentType.typ, h);
				}
				h.putAll(loadEquipmentTypes(files[i], dataSource));
			}
			else if (filename.startsWith(link_type))
			{
				Pool.putHash(LinkType.typ, loadLinkTypes(files[i], dataSource));
			}
			else if (filename.startsWith(port_type))
			{
				Pool.putHash(PortType.typ, loadPortTypes(files[i], dataSource));
			}
			else if (filename.startsWith(cable_port_type))
			{
				Pool.putHash(CablePortType.typ, loadCablePortTypes(files[i], dataSource));
			}
			else if (filename.startsWith(testport_type))
			{
				Pool.putHash(TestPortType.typ, loadTestPortTypes(files[i], dataSource));
			}
			else if (filename.startsWith(accessport_type))
			{
				Pool.putHash(AccessPortType.typ, loadAccessPortTypes(files[i], dataSource));
			}
			else if (filename.startsWith(characteristic_type))
			{
				h = Pool.getHash(CharacteristicType.typ);
				if (h == null)
				{
					h = new Hashtable();
					Pool.putHash(CharacteristicType.typ, h);
				}
				h.putAll(loadCharacteristicTypes(files[i], dataSource));
			}
		}
	}

	static protected Hashtable loadEquipmentTypes(File file, DataSourceInterface dataSource)
	{
		Hashtable h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(EquipmentType.typ))
				return null;

			EquipmentType eqt = new EquipmentType();
			h = new Hashtable();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					eqt = new EquipmentType();
				else if (s[0].equals("@name"))
					eqt.name = s[1];
				else if (s[0].equals("@id"))
				{
					String new_id = (String)Pool.get("cl_ids", s[1]);
					if (new_id == null)
					{
						new_id = dataSource.GetUId(EquipmentType.typ);
						Pool.put("cl_ids", s[1], new_id);
					}
					eqt.id = new_id;
					h.put(eqt.id, eqt);
				}
				else if (s[0].equals("@codename"))
					eqt.codename = s[1];
				else if (s[0].equals("@description"))
					eqt.description = s[1];
				else if (s[0].equals("@eq_class"))
					eqt.eq_class = s[1];
				else if (s[0].equals("@image_id"))
					eqt.image_id = s[1];
				else if (s[0].equals("@is_holder"))
					eqt.is_holder = Boolean.valueOf(s[1]).booleanValue();
				else if (s[0].equals("@manufacturer"))
					eqt.manufacturer = s[1];
				else if (s[0].equals("@manufacturer_code"))
					eqt.manufacturer_code = s[1];
				else if (s[0].equals("@characteristics"))
				{
					eqt.characteristics = new Hashtable();
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

	static protected Hashtable loadLinkTypes(File file, DataSourceInterface dataSource)
	{
		Hashtable h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(LinkType.typ))
				return null;

			LinkType lt = new LinkType();
			h = new Hashtable();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					lt = new LinkType();
				else if (s[0].equals("@name"))
					lt.name = s[1];
				else if (s[0].equals("@id"))
				{
					String new_id = (String)Pool.get("cl_ids", s[1]);
					if (new_id == null)
					{
						new_id = dataSource.GetUId(LinkType.typ);
						Pool.put("cl_ids", s[1], new_id);
					}
					lt.id = new_id;
					h.put(lt.id, lt);
				}
				else if (s[0].equals("@codename"))
					lt.codename = s[1];
				else if (s[0].equals("@description"))
					lt.description = s[1];
				else if (s[0].equals("@image_id"))
					lt.image_id = s[1];
				else if (s[0].equals("@is_holder"))
					lt.is_holder = Boolean.valueOf(s[1]).booleanValue();
				else if (s[0].equals("@link_class"))
					lt.link_class = s[1];
				else if (s[0].equals("@manufacturer"))
					lt.manufacturer = s[1];
				else if (s[0].equals("@manufacturer_code"))
					lt.manufacturer_code = s[1];
				else if (s[0].equals("@standard"))
					lt.standard = s[1];
				else if (s[0].equals("@year"))
					lt.year = s[1];
				else if (s[0].equals("@characteristics"))
				{
					lt.characteristics = new Hashtable();
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

	static protected Hashtable loadPortTypes(File file, DataSourceInterface dataSource)
	{
		Hashtable h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(PortType.typ))
				return null;

			PortType pt = new PortType();
			h = new Hashtable();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					pt = new PortType();
				else if (s[0].equals("@name"))
					pt.name = s[1];
				else if (s[0].equals("@id"))
				{
					String new_id = (String)Pool.get("cl_ids", s[1]);
					if (new_id == null)
					{
						new_id = dataSource.GetUId(PortType.typ);
						Pool.put("cl_ids", s[1], new_id);
					}
					pt.id = new_id;
					h.put(pt.id, pt);
				}
				else if (s[0].equals("@p_class"))
					pt.p_class = s[1];
				else if (s[0].equals("@codename"))
					pt.codename = s[1];
				else if (s[0].equals("@description"))
					pt.description = s[1];
				else if (s[0].equals("@body"))
					pt.body = s[1];
				else if (s[0].equals("@interface_id"))
					pt.interface_id = s[1];
				else if (s[0].equals("@standard"))
					pt.standard = s[1];
				else if (s[0].equals("@characteristics"))
				{
					pt.characteristics = new Hashtable();
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

	static protected Hashtable loadCablePortTypes(File file, DataSourceInterface dataSource)
	{
		Hashtable h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(CablePortType.typ))
				return null;

			CablePortType cpt = new CablePortType();
			h = new Hashtable();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					cpt = new CablePortType();
				else if (s[0].equals("@name"))
					cpt.name = s[1];
				else if (s[0].equals("@id"))
				{
					String new_id = (String)Pool.get("cl_ids", s[1]);
					if (new_id == null)
					{
						new_id = dataSource.GetUId(CablePortType.typ);
						Pool.put("cl_ids", s[1], new_id);
					}
					cpt.id = new_id;
					h.put(cpt.id, cpt);
				}
				else if (s[0].equals("@codename"))
					cpt.codename = s[1];
				else if (s[0].equals("@description"))
					cpt.description = s[1];
				else if (s[0].equals("@body"))
					cpt.body = s[1];
				else if (s[0].equals("@interface_id"))
					cpt.interface_id = s[1];
				else if (s[0].equals("@standard"))
					cpt.standard = s[1];
				else if (s[0].equals("@characteristics"))
				{
					cpt.characteristics = new Hashtable();
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

	static protected Hashtable loadTestPortTypes(File file, DataSourceInterface dataSource)
	{
		Hashtable h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(TestPortType.typ))
				return null;

			TestPortType tpt = new TestPortType();
			h = new Hashtable();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					tpt = new TestPortType();
				else if (s[0].equals("@name"))
					tpt.name = s[1];
				else if (s[0].equals("@id"))
				{
					String new_id = (String)Pool.get("cl_ids", s[1]);
					if (new_id == null)
					{
						new_id = dataSource.GetUId(TestPortType.typ);
						Pool.put("cl_ids", s[1], new_id);
					}
					tpt.id = new_id;
					h.put(tpt.id, tpt);
				}
				else if (s[0].equals("@description"))
					tpt.description = s[1];
				else if (s[0].equals("@port_type_id"))
					tpt.port_type_id = s[1];
				else if (s[0].equals("@characteristics"))
				{
					tpt.characteristics = new Hashtable();
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

	static protected Hashtable loadAccessPortTypes(File file, DataSourceInterface dataSource)
	{
		Hashtable h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(AccessPortType.typ))
				return null;

			AccessPortType apt = new AccessPortType();
			h = new Hashtable();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					apt = new AccessPortType();
				else if (s[0].equals("@name"))
					apt.name = s[1];
				else if (s[0].equals("@id"))
				{
					String new_id = (String)Pool.get("cl_ids", s[1]);
					if (new_id == null)
					{
						new_id = dataSource.GetUId(EquipmentType.typ);
						Pool.put("cl_ids", s[1], new_id);
					}
					apt.id = new_id;
					h.put(apt.id, apt);
				}
				else if (s[0].equals("@description"))
					apt.description = s[1];
				else if (s[0].equals("@access_type"))
					apt.access_type = s[1];
				else if (s[0].equals("@characteristics"))
				{
					apt.characteristics = new Hashtable();
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

	static protected Hashtable loadCharacteristicTypes(File file, DataSourceInterface dataSource)
	{
		Hashtable h;
		try
		{
			FileInputStream fis = new FileInputStream(file);
			IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

			if (!getType(isr).equals(CharacteristicType.typ))
				return null;

			CharacteristicType ch = new CharacteristicType();
			h = new Hashtable();
			while (isr.ready())
			{
				String s[] = analyseString(isr.readASCIIString());
				if (s[0].equals(""))
					ch = new CharacteristicType();
				else if (s[0].equals("@name"))
					ch.name = s[1];
				else if (s[0].equals("@id"))
				{
					String new_id = (String)Pool.get("cl_ids", s[1]);
					if (new_id == null)
					{
						new_id = dataSource.GetUId(CharacteristicType.typ);
						Pool.put("cl_ids", s[1], new_id);
					}
					ch.id = new_id;
					h.put(ch.id, ch);
				}
				else if (s[0].equals("@description"))
					ch.description = s[1];
				else if (s[0].equals("@ch_class"))
					ch.ch_class = s[1];
				else if (s[0].equals("@value_type_id"))
					ch.value_type_id = s[1];
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

	protected static void readPorts(DataSourceInterface dataSource)
	{
		File[] files = new File(ports_dir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Hashtable h = new Hashtable();
			String filename = files[i].getName();
			if (filename.startsWith(port_type))
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(SchemePort.typ))
						return;

					SchemePort port = new SchemePort("");
					h = new Hashtable();
					while (isr.ready())
					{
						String s[] = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							port = new SchemePort("");
						else if (s[0].equals("@name"))
							port.name = s[1];
						else if (s[0].equals("@id"))
						{
							String new_id = (String)Pool.get("cl_ids", s[1]);
							if (new_id == null)
							{
								new_id = dataSource.GetUId(SchemePort.typ);
								Pool.put("cl_ids", s[1], new_id);
							}
							port.id = new_id;
							h.put(port.id, port);
						}
						else if (s[0].equals("@port_type_id"))
							port.port_type_id = s[1];
						else if (s[0].equals("@link_id"))
							port.link_id = s[1];
						else if (s[0].equals("@device_id"))
							port.device_id = s[1];
						else if (s[0].equals("@access_port_type_id"))
							port.access_port_type_id = s[1];
					}
					isr.close();
					fis.close();
					Hashtable old = Pool.getHash(SchemePort.typ);
					if (old == null)
						Pool.putHash(SchemePort.typ, h);
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

	protected static void readCablePorts(DataSourceInterface dataSource)
	{
		File[] files = new File(ports_dir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Hashtable h = new Hashtable();
			String filename = files[i].getName();
			if (filename.startsWith(cable_port_type))
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(SchemeCablePort.typ))
						return;

					SchemeCablePort port = new SchemeCablePort("");
					h = new Hashtable();
					while (isr.ready())
					{
						String s[] = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							port = new SchemeCablePort("");
						else if (s[0].equals("@name"))
							port.name = s[1];
						else if (s[0].equals("@id"))
						{
							String new_id = (String)Pool.get("cl_ids", s[1]);
							if (new_id == null)
							{
								new_id = dataSource.GetUId(SchemeCablePort.typ);
								Pool.put("cl_ids", s[1], new_id);
							}
							port.id = new_id;
							h.put(port.id, port);
						}
						else if (s[0].equals("@cable_port_type_id"))
							port.cable_port_type_id = s[1];
						else if (s[0].equals("@cable_link_id"))
							port.cable_link_id = s[1];
						else if (s[0].equals("@device_id"))
							port.device_id = s[1];
					}
					isr.close();
					fis.close();
					Hashtable old = Pool.getHash(SchemeCablePort.typ);
					if (old == null)
						Pool.putHash(SchemeCablePort.typ, h);
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

	protected static void readLinks(DataSourceInterface dataSource)
	{
		File[] files = new File(links_dir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Hashtable h = new Hashtable();
			String filename = files[i].getName();
			if (filename.startsWith(link_type))
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(SchemeLink.typ))
						return;

					SchemeLink link = new SchemeLink("");
					h = new Hashtable();
					while (isr.ready())
					{
						String s[] = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							link = new SchemeLink("");
						else if (s[0].equals("@name"))
							link.name = s[1];
						else if (s[0].equals("@id"))
						{
							String new_id = (String)Pool.get("cl_ids", s[1]);
							if (new_id == null)
							{
								new_id = dataSource.GetUId(SchemeLink.typ);
								Pool.put("cl_ids", s[1], new_id);
							}
							link.id = new_id;
							h.put(link.id, link);
						}
						else if (s[0].equals("@link_type_id"))
							link.link_type_id = s[1];
						else if (s[0].equals("@source_port_id"))
							link.source_port_id = s[1];
						else if (s[0].equals("@target_port_id"))
							link.target_port_id = s[1];
					}
					isr.close();
					fis.close();
					Hashtable old = Pool.getHash(SchemeLink.typ);
					if (old == null)
						Pool.putHash(SchemeLink.typ, h);
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

	protected static void readCableLinks(DataSourceInterface dataSource)
	{
		File[] files = new File(links_dir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Hashtable h = new Hashtable();
			String filename = files[i].getName();
			if (filename.startsWith(cable_link_type))
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(CableLinkType.typ))
						return;

					SchemeCableLink link = new SchemeCableLink("");
					h = new Hashtable();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							link = new SchemeCableLink("");
						else if (s[0].equals("@name"))
							link.name = s[1];
						else if (s[0].equals("@id"))
						{
							String new_id = (String)Pool.get("cl_ids", s[1]);
							if (new_id == null)
							{
								new_id = dataSource.GetUId(SchemeCableLink.typ);
								Pool.put("cl_ids", s[1], new_id);
							}
							link.id = new_id;
							h.put(link.id, link);
						}
						else if (s[0].equals("@cable_link_type_id"))
							link.cable_link_type_id = s[1];
						else if (s[0].equals("@source_port_id"))
							link.source_port_id = s[1];
						else if (s[0].equals("@target_port_id"))
							link.target_port_id = s[1];
						else if (s[0].equals("@cable_threads"))
						{
							link.cable_threads = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								link.cable_threads.add((SchemeCableThread)Pool.get(SchemeCableThread.typ, s[0]));
								s = analyseString(isr.readASCIIString());
							}
						}
					}
					isr.close();
					fis.close();
					Hashtable old = Pool.getHash(SchemeCableLink.typ);
					if (old == null)
						Pool.putHash(SchemeCableLink.typ, h);
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

	protected static void readDevices(DataSourceInterface dataSource)
	{
		File[] files = new File(devices_dir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Hashtable h = new Hashtable();
			String filename = files[i].getName();
			if (filename.startsWith(eq_type))
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(SchemeDevice.typ))
						return;

					SchemeDevice device = new SchemeDevice("");
					h = new Hashtable();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							device = new SchemeDevice("");
						else if (s[0].equals("@name"))
							device.name = s[1];
						else if (s[0].equals("@id"))
						{
							String new_id = (String)Pool.get("cl_ids", s[1]);
							if (new_id == null)
							{
								new_id = dataSource.GetUId(SchemeDevice.typ);
								Pool.put("cl_ids", s[1], new_id);
							}
							device.id = new_id;
							h.put(device.id, device);
						}
						else if (s[0].equals("@ports"))
						{
							device.ports = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								device.ports.add((SchemePort)Pool.get(SchemePort.typ, s[0]));
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@cableports"))
						{
							device.cableports = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								device.cableports.add((SchemeCablePort)Pool.get(SchemeCablePort.typ, s[0]));
								s = analyseString(isr.readASCIIString());
							}
						}
					}
					isr.close();
					fis.close();
					Hashtable old = Pool.getHash(SchemeDevice.typ);
					if (old == null)
						Pool.putHash(SchemeDevice.typ, h);
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

	protected static void readProtoElements(DataSourceInterface dataSource)
	{
		File[] files = new File(proto_dir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Hashtable h = new Hashtable();
			String filename = files[i].getName();
			if (filename.startsWith(proto_type))
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(ProtoElement.typ))
						return;

					ProtoElement proto = new ProtoElement("");
					h = new Hashtable();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							proto = new ProtoElement("");
						else if (s[0].equals("@name"))
							proto.name = s[1];
						else if (s[0].equals("@id"))
						{
							String new_id = (String)Pool.get("cl_ids", s[1]);
							if (new_id == null)
							{
								new_id = dataSource.GetUId(ProtoElement.typ);
								Pool.put("cl_ids", s[1], new_id);
							}
							proto.id = new_id;
							h.put(proto.id, proto);
						}
						else if (s[0].equals("@equipment_type_id"))
							proto.equipment_type_id = s[1];
						else if (s[0].equals("@devices"))
						{
							proto.devices = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								proto.devices.add((SchemeDevice)Pool.get(SchemeDevice.typ, s[0]));
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@links"))
						{
							proto.links = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								proto.links.add((SchemeLink)Pool.get(SchemeLink.typ, s[0]));
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@protoelements"))
						{
							proto.protoelement_ids = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								proto.protoelement_ids.add(s[0]);
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@schemecell"))
						{
							proto.schemecell = readVisualElement(s[1]);
						}
					}

					isr.close();
					fis.close();
					Hashtable old = Pool.getHash(ProtoElement.typ);
					if (old == null)
						Pool.putHash(ProtoElement.typ, h);
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

	protected static void readMapProto(DataSourceInterface dataSource)
	{
		File[] files = new File(proto_dir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Hashtable h = new Hashtable();
			String filename = files[i].getName();
			if (filename.startsWith(map_proto_name))
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(MapProtoElement.typ))
						return;

					MapProtoElement mapproto = new MapProtoElement();
					h = new Hashtable();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							mapproto = new MapProtoElement();
						else if (s[0].equals("@name"))
							mapproto.name = s[1];
						else if (s[0].equals("@id"))
						{
							String new_id = (String)Pool.get("cl_ids", s[1]);
							if (new_id == null)
							{
								new_id = dataSource.GetUId(MapProtoElement.typ);
								Pool.put("cl_ids", s[1], new_id);
							}
							mapproto.id = new_id;
							h.put(mapproto.id, mapproto);
						}
						else if (s[0].equals("@description"))
							mapproto.description = s[1];
						else if (s[0].equals("@symbol_id"))
							mapproto.setImageID(s[1]);
						else if (s[0].equals("@pe_class"))
							mapproto.pe_class = s[1];
						else if (s[0].equals("@pe_is_kis"))
							mapproto.pe_is_kis = Boolean.valueOf(s[1]).booleanValue();
						else if (s[0].equals("@is_visual"))
						{
							mapproto.is_visual = Boolean.valueOf(s[1]).booleanValue();
						}
						else if (s[0].equals("@protoelements"))
						{
							mapproto.pe_ids = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								mapproto.pe_ids.add(s[0]);
								s = analyseString(isr.readASCIIString());
							}
						}
					}
					isr.close();
					fis.close();
					Hashtable old = Pool.getHash(MapProtoElement.typ);
					if (old == null)
						Pool.putHash(MapProtoElement.typ, h);
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

	protected static void readScheme(DataSourceInterface dataSource)
	{
		File[] files = new File(scheme_dir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Hashtable h = new Hashtable();
			String filename = files[i].getName();
			if (filename.startsWith(scheme_name))
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(Scheme.typ))
						return;

					Scheme scheme = new Scheme();
					h = new Hashtable();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							scheme = new Scheme();
						else if (s[0].equals("@name"))
							scheme.name = s[1];
						else if (s[0].equals("@id"))
						{
							String new_id = (String)Pool.get("cl_ids", s[1]);
							if (new_id == null)
							{
								new_id = dataSource.GetUId(Scheme.typ);
								Pool.put("cl_ids", s[1], new_id);
							}
							scheme.id = new_id;
							h.put(scheme.id, scheme);
						}
						else if (s[0].equals("@created"))
							scheme.created = Long.parseLong(s[1]);
						else if (s[0].equals("@created_by"))
							scheme.created_by = s[1];
						else if (s[0].equals("@description"))
							scheme.description = s[1];
						else if (s[0].equals("@domain_id"))
							scheme.domain_id = s[1];
						else if (s[0].equals("@owner_id"))
							scheme.owner_id = s[1];
						else if (s[0].equals("@cablelinks"))
						{
							scheme.cablelinks = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								scheme.cablelinks.add((SchemeCableLink)Pool.get(SchemeCableLink.typ, s[0]));
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@elements"))
						{
							scheme.elements = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								scheme.elements.add((SchemeElement)Pool.get(SchemeElement.typ, s[0]));
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@paths"))
						{
							scheme.paths = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								scheme.paths.add((SchemePath)Pool.get(SchemePath.typ, s[0]));
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@schemecell"))
						{
							scheme.schemecell = readVisualElement(s[1]);
						}
					}
					isr.close();
					fis.close();
					Hashtable old = Pool.getHash(Scheme.typ);
					if (old == null)
						Pool.putHash(Scheme.typ, h);
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

	protected static void readSchemeElements(DataSourceInterface dataSource)
	{
		File[] files = new File(scheme_dir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Hashtable h = new Hashtable();
			String filename = files[i].getName();
			if (filename.startsWith(scheme_element_name))
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(SchemeElement.typ))
						return;

					SchemeElement se = new SchemeElement("");
					h = new Hashtable();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							se = new SchemeElement("");
						else if (s[0].equals("@name"))
							se.name = s[1];
						else if (s[0].equals("@id"))
						{
							String new_id = (String)Pool.get("cl_ids", s[1]);
							if (new_id == null)
							{
								new_id = dataSource.GetUId(SchemeElement.typ);
								Pool.put("cl_ids", s[1], new_id);
							}
							se.id = new_id;
							h.put(se.id, se);
						}
						else if (s[0].equals("@equipment_id"))
							se.equipment_id = s[1];
						else if (s[0].equals("@proto_element_id"))
							se.proto_element_id = s[1];

						else if (s[0].equals("@devices"))
						{
							se.devices = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								se.devices.add((SchemeDevice)Pool.get(SchemeDevice.typ, s[0]));
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@elements"))
						{
							se.element_ids = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								se.element_ids.add(s[0]);
								s = analyseString(isr.readASCIIString());
							}
						}
						else if (s[0].equals("@links"))
						{
							se.links = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								se.links.add((SchemeLink)Pool.get(SchemeLink.typ, s[0]));
								s = analyseString(isr.readASCIIString());
							}
						}
/*						else if (s[0].equals("@schemecell"))
						{
							se.schemecell = readVisualElement(s[1]);
						}*/
					}
					isr.close();
					fis.close();
					Hashtable old = Pool.getHash(SchemeElement.typ);
					if (old == null)
						Pool.putHash(SchemeElement.typ, h);
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

	protected static void readSchemePaths(DataSourceInterface dataSource)
	{
		File[] files = new File(scheme_dir).listFiles();
		if (files == null)
			return;
		for (int i = 0; i < files.length; i++)
		{
			Hashtable h = new Hashtable();
			String filename = files[i].getName();
			if (filename.startsWith(scheme_path_name))
			{
				try
				{
					FileInputStream fis = new FileInputStream(files[i]);
					IntelStreamReader isr = new IntelStreamReader(fis, "UTF-16");

					if (!getType(isr).equals(SchemePath.typ))
						return;

					SchemePath path = new SchemePath("");
					h = new Hashtable();
					while (isr.ready())
					{
						String[] s = analyseString(isr.readASCIIString());
						if (s[0].equals(""))
							path = new SchemePath("");
						else if (s[0].equals("@name"))
							path.name = s[1];
						else if (s[0].equals("@id"))
						{
							String new_id = (String)Pool.get("cl_ids", s[1]);
							if (new_id == null)
							{
								new_id = dataSource.GetUId(SchemePath.typ);
								Pool.put("cl_ids", s[1], new_id);
							}
							path.id = new_id;
							h.put(path.id, path);
						}
						else if (s[0].equals("@start_device_id"))
							path.start_device_id = s[1];
						else if (s[0].equals("@end_device_id"))
							path.end_device_id = s[1];
						else if (s[0].equals("@type_id"))
							path.type_id = s[1];
						else if (s[0].equals("@links"))
						{
							path.links = new Vector();
							s = analyseString(isr.readASCIIString());
							while (!s[0].startsWith("@end"))
							{
								PathElement pe = new PathElement();
								pe.n = Integer.parseInt(s[0]);
								s = analyseString(isr.readASCIIString());
								pe.is_cable = Boolean.valueOf(s[0]).booleanValue();
								s = analyseString(isr.readASCIIString());
								pe.link_id = s[0];
								s = analyseString(isr.readASCIIString());
								pe.thread_id = s[0];
								path.links.add(pe);
								s = analyseString(isr.readASCIIString());
							}
						}
					}
					isr.close();
					fis.close();
					Hashtable old = Pool.getHash(SchemePath.typ);
					if (old == null)
						Pool.putHash(SchemePath.typ, h);
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