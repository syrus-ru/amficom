//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Класс содержит описание схемы сети, которая состоит из     * //
// *           набора элементов, отображаемых на экране                   * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 22 jan 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\Resource\Scheme\Scheme.java                            * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.swing.JOptionPane;
import com.syrus.AMFICOM.Client.General.Model.Environment;

import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;

import com.syrus.AMFICOM.CORBA.Scheme.SchemeCableLink_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.SchemeElement_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.SchemeLink_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.SchemePath_Transferable;
import com.syrus.AMFICOM.CORBA.Scheme.Scheme_Transferable;

public class Scheme extends ObjectResource implements Serializable
{
	public static final String typ = "scheme";
	private static final long serialVersionUID = 01L;
	private Scheme_Transferable transferable;

	public static final String NETWORK = "network";
	public static final String CABLESUBNETWORK = "cablesubnetwork";
	public static final String BUILDING = "building";
	public static final String FLOOR = "floor";
	public static final String ROOM = "room";
	public static final String RACK = "rack";
	public static final String BAY = "bay";
	public static final String CARDCAGE = "cardcage";

	public String id = "";
	public String name = "";
	public String scheme_type = "network";

	public Vector elements_to_register = new Vector();
	public Vector elements = new Vector();
	public Vector cablelinks = new Vector();
	public Vector links = new Vector();
	public Vector paths = new Vector();

	public long created = 0;
	public long modified = 0;
	public String created_by = "";
	public String modified_by = "";

	public String owner_id = "";
	public String description = "";
	public String domain_id = "";
	public String symbol_id = "";
	public String label = "";

	public int width = 840;
	public int height = 1190;

	public Serializable serializable_cell;
	public byte[] schemecell;
	public Serializable serializable_ugo;
	public byte[] ugo;
	public Hashtable clones = new Hashtable();

	public Scheme()
	{
		transferable = new Scheme_Transferable();
	}

	public Scheme(Scheme_Transferable transferable)
	{
		this.transferable = transferable;
		setLocalFromTransferable();
	}

	public String getTyp()
	{
		return typ;
	}

	public String getName()
	{
		return name;
	}

	public String getId()
	{
		return id;
	}

	public Object getTransferable()
	{
		return transferable;
	}

	public long getModified()
	{
		return modified;
	}

	public String getDomainId()
	{
		return domain_id;
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String cloned_id = (String)Pool.get("clonedids", id);
		if (cloned_id != null)
		{
			Scheme cloned = (Scheme)Pool.get(Scheme.typ, cloned_id);
			if (cloned == null)
				System.err.println("Scheme.clone() id not found: " + cloned_id);
			else
				return cloned;
		}

		Scheme scheme = new Scheme();

		scheme.id = dataSource.GetUId(Scheme.typ);
		scheme.name = name;
		scheme.scheme_type = scheme_type;

		scheme.elements = new Vector(elements.size());
		for (int i = 0; i < elements.size(); i++)
			scheme.elements.add(((SchemeElement)elements.get(i)).clone(dataSource));
		scheme.cablelinks = new Vector(cablelinks.size());
		for (int i = 0; i < cablelinks.size(); i++)
			scheme.cablelinks.add(((SchemeCableLink)cablelinks.get(i)).clone(dataSource));
		scheme.links = new Vector(links.size());
		for (int i = 0; i < links.size(); i++)
			scheme.links.add(((SchemeLink)links.get(i)).clone(dataSource));

		scheme.paths = new Vector();
		//scheme.paths = new Vector(paths.size());
		//	for (int i = 0; i < paths.size(); i++)
		//		scheme.paths.add(((SchemePath)paths.get(i)).clone(dataSource));

		scheme.created = 0;
		scheme.modified = 0;
		scheme.created_by = dataSource.getSession().getUserId();
		scheme.modified_by = dataSource.getSession().getUserId();

		scheme.owner_id = dataSource.getSession().getUserId();
		scheme.description = description;
		scheme.domain_id = dataSource.getSession().getDomainId();
		scheme.symbol_id = symbol_id;
		scheme.label = label;

		if (schemecell != null)
		{
			scheme.schemecell = new byte[schemecell.length];
			for (int i = 0; i < schemecell.length; i++)
				scheme.schemecell[i] = schemecell[i];
		}
		if (ugo != null)
		{
			scheme.ugo = new byte[ugo.length];
			for (int i = 0; i < ugo.length; i++)
				scheme.ugo[i] = ugo[i];
		}

		scheme.unpack();
		Pool.put(Scheme.typ, scheme.getId(), scheme);
		Pool.put("clonedids", id, scheme.getId());

		Hashtable ht = Pool.getHash("clonedids");
		scheme.clones.put(id, scheme.getId());
		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			String id = ((SchemeElement)it.next()).getId();
			scheme.clones.put(id, ht.get(id));
		}
		for (Iterator it = cablelinks.iterator(); it.hasNext();)
		{
			String id = ((SchemeCableLink)it.next()).getId();
			scheme.clones.put(id, ht.get(id));
		}
//		for (Iterator it = paths.iterator(); it.hasNext();)
//		{
//			String id = ((SchemePath)it.next()).getId();
//			scheme.clones.put(id, ht.get(id));
//		}

		return scheme;
	}

	public Enumeration getChildTypes()
	{
		Vector vec = new Vector();
		vec.add("elements");
		vec.add("cablelinks");
		vec.add("paths");
		return vec.elements();
	}

	public Class getChildClass(String key)
	{
		if(key.equals("elements"))
		{
			return SchemeElement.class;
		}
		else if(key.equals("cablelinks"))
		{
			return SchemeCableLink.class;
		}
		else if(key.equals("paths"))
		{
			return SchemePath.class;
		}
		return ObjectResource.class;
	}

	public Enumeration getChildren(String key)
	{
		if(key.equals("elements"))
		{
			return elements.elements();
		}
		else if(key.equals("cablelinks"))
		{
			return cablelinks.elements();
		}
		else if(key.equals("paths"))
		{
			return paths.elements();
		}
		return new Vector().elements();
	}

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		scheme_type = transferable.scheme_type;
		created = transferable.created;
		modified = transferable.modified;
		modified_by = transferable.modified_by;
		created_by = transferable.created_by;

		owner_id = transferable.owner_id;
		domain_id = transferable.domain_id;
		symbol_id = transferable.symbol_id;
		label = transferable.label;
		description = transferable.description;

		elements = new Vector();
		cablelinks = new Vector();
		links = new Vector();
		paths = new Vector();

		elements_to_register = new Vector();
		Vector transferable_element_ids = new Vector();
		for (int i = 0; i < transferable.element_ids.length; i++)
			transferable_element_ids.add(transferable.element_ids[i]);
		for (int i = 0; i < transferable.elements.length; i++)
		{
			SchemeElement element = new SchemeElement(transferable.elements[i]);
			if (transferable_element_ids.contains(element.getId()))
				elements.add(element);
			else
				elements_to_register.add(element);
		}
		for (int i = 0; i < transferable.cable_links.length; i++)
			cablelinks.add(new SchemeCableLink(transferable.cable_links[i]));
		for (int i = 0; i < transferable.links.length; i++)
			links.add(new SchemeLink(transferable.links[i]));
		for (int i = 0; i < transferable.paths.length; i++)
			paths.add(new SchemePath(transferable.paths[i]));

		width = (transferable.width != 0 ? transferable.width : 840);
		height = (transferable.height != 0 ? transferable.height : 1190);
		if (transferable.clonez.length == 0)
			clones = new Hashtable();
		else
		{
			try
			{
				ByteArrayInputStream bais = new ByteArrayInputStream(transferable.clonez);
				ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(bais));
				clones = (Hashtable)in.readObject();
				in.close();
			}
			catch (Exception e)
			{
				clones = new Hashtable();
			}
		}

		schemecell = transferable.schemecell;
		ugo = transferable.ugocell;
	}

	public void setTransferableFromLocal()
	{
		modified = System.currentTimeMillis();

		transferable.id = id;
		transferable.name = name;
		transferable.scheme_type = scheme_type;
		transferable.created = created;
		transferable.modified = modified;
		transferable.created_by = created_by;
		transferable.modified_by = modified_by;
		transferable.modified_by = modified_by;

		transferable.owner_id = owner_id;
		transferable.domain_id = domain_id;
		transferable.symbol_id = symbol_id;
		transferable.label = label;
		transferable.description = description;

		Vector all_elements = new Vector();
		for (int i = 0; i < elements.size(); i++)
		{
			all_elements.addAll(getChildElements((SchemeElement)elements.get(i)));
			all_elements.add(elements.get(i));
		}

		transferable.elements = new SchemeElement_Transferable[all_elements.size()];

		for (int i=0; i<transferable.elements.length; i++)
		{
			SchemeElement element = (SchemeElement)all_elements.get(i);
			element.setTransferableFromLocal();
			transferable.elements[i] = (SchemeElement_Transferable)element.getTransferable();
		}
		transferable.element_ids = new String[elements.size()];
		for (int i=0; i<transferable.element_ids.length; i++)
			transferable.element_ids[i] = ((SchemeElement)elements.get(i)).getId();

		transferable.cable_links = new SchemeCableLink_Transferable[cablelinks.size()];
		transferable.links = new SchemeLink_Transferable[links.size()];
		transferable.paths = new SchemePath_Transferable[paths.size()];
		for (int i=0; i<transferable.cable_links.length; i++)
		{
			SchemeCableLink cablelink = (SchemeCableLink)cablelinks.get(i);
			cablelink.setTransferableFromLocal();
			transferable.cable_links[i] = (SchemeCableLink_Transferable)cablelink.getTransferable();
		}
		for (int i=0; i<transferable.links.length; i++)
		{
			SchemeLink link = (SchemeLink)links.get(i);
			link.setTransferableFromLocal();
			transferable.links[i] = (SchemeLink_Transferable)link.getTransferable();
		}
		for (int i=0; i<transferable.paths.length; i++)
		{
			SchemePath path = (SchemePath)paths.get(i);
			path.setTransferableFromLocal();
			transferable.paths[i] = (SchemePath_Transferable)path.getTransferable();
		}

		transferable.width = width;
		transferable.height = height;
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(new GZIPOutputStream(baos));
			out.writeObject(clones);
			out.flush();
			out.close();
			transferable.clonez = baos.toByteArray();
		}
		catch (IOException e)
		{
			transferable.clonez = new byte[0];
		}

		transferable.ugocell = ugo;
		transferable.schemecell = schemecell;
	}

	public void updateLocalFromTransferable()
	{
		/*Vector all_elements = new Vector();
		for (int i = 0; i < elements.size(); i++)
		{
			all_elements.addAll(getChildElements((SchemeElement)elements.get(i)));
			all_elements.add(elements.get(i));
		}*/
		for (int i = 0; i < elements_to_register.size(); i++)
		{
			SchemeElement el = (SchemeElement)elements_to_register.get(i);
			Pool.put(SchemeElement.typ, el.getId(), el);
			el.updateLocalFromTransferable();
		}
		for (int i = 0; i < elements.size(); i++)
		{
			SchemeElement el = (SchemeElement)elements.get(i);
			Pool.put(SchemeElement.typ, el.getId(), el);
			el.updateLocalFromTransferable();
		}
		for (int i = 0; i < cablelinks.size(); i++)
		{
			SchemeCableLink link = (SchemeCableLink)cablelinks.get(i);
			Pool.put(SchemeCableLink.typ, link.getId(), link);
			link.updateLocalFromTransferable();
		}
		for (int i = 0; i < links.size(); i++)
		{
			SchemeLink link = (SchemeLink)links.get(i);
			Pool.put(SchemeLink.typ, link.getId(), link);
			link.updateLocalFromTransferable();
		}
		for (int i = 0; i < paths.size(); i++)
		{
			SchemePath path = (SchemePath)paths.get(i);
			Pool.put(SchemePath.typ, path.getId(), path);
			path.updateLocalFromTransferable();
		}
	}

	public synchronized boolean pack()
	{
//		System.out.println("packing schemecell...");
		schemecell = pack(serializable_cell);
//		System.out.println("packing done!");
//		System.out.println("packing schemeugo...");
		ugo = pack(serializable_ugo);
//		System.out.println("packing done!");

		int i = 0;
//		System.out.println("trying to unpack...");
		while (unpack(schemecell) == null)
		{
			System.out.println();
//			System.out.println("fail! retry...");
			schemecell = pack(serializable_cell);
			i++;
			if (i == 3)
				return false;
		}
//		System.out.println("all success!");

		i = 0;
		while (unpack(ugo) == null)
		{
			ugo = pack(serializable_ugo);
			i++;
			if (i == 3)
				return false;
		}

		return true;
	}

	private synchronized byte[] pack(Serializable cell)
	{
		if (cell == null)
			return null;
		try
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream zos = new GZIPOutputStream(baos);
			ObjectOutputStream out = new ObjectOutputStream(zos);

			out.writeObject(cell);

			out.flush();
			baos.flush();
			out.close();
			zos.close();
			baos.close();
			byte[] b = baos.toByteArray();
			return b;
		}
		catch (StackOverflowError e)
		{
			System.err.println("Error packing scheme: " + e.toString() + "retry...");
			return null;
		}
		catch (Exception e)
		{
			System.err.println("Exception packing scheme: " + e.toString() + "retry...");
			return null;
		}
	}
/*
	private byte[] packNew(Serializable cell)
	{
		if (cell == null)
			return null;
		try
		{
//			System.out.println("new creating baos...");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			GZIPOutputStream zos = new GZIPOutputStream(baos);
			ObjectOutputStream oos = new ObjectOutputStream(zos);

			Vector v = (Vector)cell;
			Object[] obj = (Object[])v.get(0);

//			System.out.println("writing size...");
			oos.writeInt(obj.length);

//			System.out.println("writing objects...");
			for (int i = 0; i < obj.length; i++)
			{
//				System.out.println(i + ": " + obj[i] + ";");
				oos.writeObject(obj[i]);
			}
//			System.out.println();
//			System.out.println("writing object (1)...");
			oos.writeObject(v.get(1));
//			System.out.println("writing object (2)...");
			oos.writeObject(v.get(2));

			oos.flush();
			zos.flush();
			baos.flush();

			oos.close();
			zos.close();
			baos.close();
			byte[] b = baos.toByteArray();
			return b;
		}
		catch (Exception e)
		{
			System.err.println("Error packing sheme: " + e.toString());
			return null;
		}
		catch (StackOverflowError e)
		{
			System.err.println("Error packing sheme: " + e.toString());
			//e.printStackTrace();
			return null;
		}
	}*/

	public synchronized void unpack()
	{
		if (serializable_cell == null && schemecell != null && schemecell.length != 0)
			serializable_cell = unpack(schemecell);
		if (serializable_ugo == null && ugo != null && ugo.length != 0)
			serializable_ugo = unpack(ugo);
	}

	private synchronized Serializable unpack(byte[] b)
	{
		if (b == null)
			return null;
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(b);
			GZIPInputStream zis = new GZIPInputStream(bais);
			ObjectInputStream in = new ObjectInputStream(zis);

			Object obj = in.readObject();
			Serializable serializable = (Serializable)obj;

			in.close();
			zis.close();
			bais.close();
			return serializable;
		}
		catch (Exception e)
		{
			System.err.println("Exception unpacking scheme: " + e.toString());
			return null;
		}
		catch (StackOverflowError e)
		{
			System.err.println("Error unpacking scheme: " + e.toString());
			return null;
		}
	}
/*
	private Serializable unpackNew(byte[] b)
	{
		if (b == null)
			return null;
		try
		{
			ByteArrayInputStream bais = new ByteArrayInputStream(b);
			GZIPInputStream zis = new GZIPInputStream(bais);
			ObjectInputStream in = new ObjectInputStream(zis);

			Vector v = new Vector();
			int length = in.readInt();
			Object[] obj = new Object[length];
			for (int i = 0; i < length; i++)
				obj[i] = in.readObject();
			v.add(obj);
			v.add(in.readObject());
			v.add(in.readObject());
			Serializable serializable = (Serializable)v;


			in.close();
			zis.close();
			bais.close();

			return serializable;
		}
		catch (Exception e)
		{
			System.err.println("Exception unpacking sheme: " + e.toString());
			return null;
		}
		catch (StackOverflowError e)
		{
			System.err.println("Error unpacking sheme: " + e.toString());
			//e.printStackTrace();
			return null;
		}
	}*/

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(scheme_type);
		out.writeLong(created);
		out.writeLong(modified);
		out.writeObject(created_by);
		out.writeObject(modified_by);
		out.writeObject(owner_id);
		out.writeObject(description);
		out.writeObject(domain_id);
		out.writeObject(symbol_id);
		out.writeObject(label);

		out.writeObject(elements);
		out.writeObject(elements_to_register);
		out.writeObject(cablelinks);
		out.writeObject(links);
		out.writeObject(paths);

		out.writeInt(width);
		out.writeInt(height);

		out.writeObject(schemecell);
		out.writeObject(ugo);
		out.writeObject(clones);
	}

	private void readObject(java.io.ObjectInputStream in)
			throws IOException, ClassNotFoundException
	{
		id = (String )in.readObject();
		name = (String )in.readObject();
		scheme_type = (String )in.readObject();
		created = in.readLong();
		modified = in.readLong();
		created_by = (String )in.readObject();
		modified_by = (String )in.readObject();
		owner_id = (String )in.readObject();
		description = (String )in.readObject();
		domain_id = (String )in.readObject();
		symbol_id = (String )in.readObject();
		label = (String )in.readObject();

		elements = (Vector )in.readObject();
		elements_to_register = (Vector )in.readObject();
		cablelinks = (Vector )in.readObject();
		links = (Vector )in.readObject();
		paths = (Vector )in.readObject();

		width = in.readInt();
		height = in.readInt();

		schemecell = (byte[] )in.readObject();
		ugo = (byte[] )in.readObject();
		clones = (Hashtable )in.readObject();

		transferable = new Scheme_Transferable();
		updateLocalFromTransferable();
	}

	Vector getChildElements(SchemeElement element)
	{
		Vector v = new Vector();
		for (int i = 0; i < element.element_ids.size(); i++)
		{
			SchemeElement child = (SchemeElement)Pool.get(SchemeElement.typ, (String)element.element_ids.get(i));
			v.addAll(getChildElements(child));
			v.add(child);
		}
		return v;
	}

	public ObjectResourceModel getModel()
	{
		return new SchemeModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new SchemeDisplayModel();
	}

/*
	public Hashtable getTopLevelElements()
	{
		Hashtable ht = new Hashtable();
		Vector ids = new Vector();

		for (int i = 0; i < elements.size(); i++)
		{
			SchemeElement element = (SchemeElement)elements.get(i);
			for (int j = 0; j < element.element_ids.size(); j++)
				ids.add((String )element.element_ids.get(j));
			ht.put(element.getId(), element);
		}

		for (int i = 0; i < elements.size(); i++)
		{
			SchemeElement element = (SchemeElement)elements.get(i);
			if(ids.contains(element.getId()))
				ht.remove(element.getId());
		}
		return ht;
	}
*/

	// return all links at scheme including inner schemes
	public Enumeration getAllSchemeLinks()
	{
		Hashtable ht = new Hashtable();
		for (int i = 0; i < links.size(); i++)
		{
			SchemeLink l = (SchemeLink)links.get(i);
			ht.put(l.getId(), l);
		}

		for (int i = 0; i < elements.size(); i++)
		{
			SchemeElement el = (SchemeElement)elements.get(i);
			if (!el.scheme_id.equals(""))
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				for (Enumeration inner = scheme.getAllLinks(); inner.hasMoreElements();)
				{
					SchemeLink l = (SchemeLink)inner.nextElement();
					ht.put(l.getId(), l);
				}
			}
		}
		return ht.elements();
	}

	// return all links at scheme including inner schemes and links in elements
	public Enumeration getAllLinks ()
	{
		Hashtable ht = new Hashtable();
		for (int i = 0; i < links.size(); i++)
		{
			SchemeLink l = (SchemeLink)links.get(i);
			ht.put(l.getId(), l);
		}

		for (int i = 0; i < elements.size(); i++)
		{
			SchemeElement el = (SchemeElement)elements.get(i);
			if (el.scheme_id.equals(""))
			{
				for (Enumeration e = el.getAllElementsLinks(); e.hasMoreElements();)
				{
					SchemeLink l = (SchemeLink)e.nextElement();
					ht.put(l.getId(), l);
				}
			}
			else
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				for (Enumeration inner = scheme.getAllLinks(); inner.hasMoreElements();)
				{
					SchemeLink l = (SchemeLink)inner.nextElement();
					ht.put(l.getId(), l);
				}
			}
		}
		return ht.elements();
	}

	// return all cablelinks at scheme including inner schemes
	public Enumeration getAllCableLinks ()
	{
		Hashtable ht = new Hashtable();
		for (int i = 0; i < cablelinks.size(); i++)
		{
			SchemeCableLink l = (SchemeCableLink)cablelinks.get(i);
			ht.put(l.getId(), l);
		}

		for (int i = 0; i < elements.size(); i++)
		{
			SchemeElement el = (SchemeElement)elements.get(i);
			if (!el.scheme_id.equals(""))
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				for (Enumeration inner = scheme.getAllCableLinks(); inner.hasMoreElements();)
				{
					SchemeCableLink l = (SchemeCableLink)inner.nextElement();
					ht.put(l.getId(), l);
				}
			}
		}
		return ht.elements();
	}

	public Enumeration getTopLevelElements()
	{
		return elements.elements();
	}

	public Enumeration getTopLevelCableLinks()
	{
		return cablelinks.elements();
	}

	public Enumeration getTopLevelPaths()
	{
		return paths.elements();
	}

	public Vector getTopologicalElements1()
	{
		return elements;
	}

	public Vector getTopologicalCableLinks1()
	{
		return cablelinks;
	}

	public Vector getTopologicalPaths1()
	{
		return paths;
	}

	// return all top level elements at scheme and at inner schemes
	public Vector getTopologicalElements()
	{
		Vector ht = new Vector();
		for (int i = 0; i < elements.size(); i++)
		{
			SchemeElement el = (SchemeElement)elements.get(i);
			if (el.scheme_id.equals(""))
				ht.add(el);
			else
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				if(scheme.scheme_type.equals(Scheme.CABLESUBNETWORK))
				{
					for (Enumeration inner = scheme.getTopologicalElements().elements(); inner.hasMoreElements();)
					{
						SchemeElement se = (SchemeElement)inner.nextElement();
						ht.add(se);
					}
				}
				else
					ht.add(el);
			}
		}
		return ht;
	}

	// return all top level elements at scheme and at inner cable links
	public Vector getTopologicalCableLinks()
	{
		Vector ht = new Vector();
		for (int i = 0; i < cablelinks.size(); i++)
		{
			SchemeElement el = (SchemeElement )elements.get(i);
			ht.add(el);
		}
		for (int i = 0; i < elements.size(); i++)
		{
			SchemeElement el = (SchemeElement)elements.get(i);
			if (el.scheme_id != null && !el.scheme_id.equals(""))
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				if(scheme.scheme_type.equals(Scheme.CABLESUBNETWORK))
				{
					for (Enumeration inner = scheme.getTopologicalCableLinks().elements(); inner.hasMoreElements();)
					{
						SchemeElement se = (SchemeElement)inner.nextElement();
						ht.add(se);
					}
				}
			}
		}
		return ht;
	}

	public Vector getTopologicalPaths()
	{
		Vector ht = new Vector();
		for (int i = 0; i < paths.size(); i++)
		{
			SchemeElement el = (SchemeElement )elements.get(i);
			ht.add(el);
		}
		for (int i = 0; i < elements.size(); i++)
		{
			SchemeElement el = (SchemeElement)elements.get(i);
			if (el.scheme_id != null && !el.scheme_id.equals(""))
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				if(scheme.scheme_type.equals(Scheme.CABLESUBNETWORK))
				{
					for (Enumeration inner = scheme.getTopologicalPaths().elements(); inner.hasMoreElements();)
					{
						SchemeElement se = (SchemeElement)inner.nextElement();
						ht.add(se);
					}
				}
			}
		}
		return ht;
	}

	// return all top level elements at scheme and at inner schemes
	public Enumeration getAllTopElements()
	{
		Hashtable ht = new Hashtable();
		for (int i = 0; i < elements.size(); i++)
		{
			SchemeElement el = (SchemeElement)elements.get(i);
			if (el.scheme_id.equals(""))
				ht.put(el.getId(), el);
			else
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				for (Enumeration inner = scheme.getTopLevelElements(); inner.hasMoreElements();)
				{
					SchemeElement se = (SchemeElement)inner.nextElement();
					ht.put(se.getId(), se);
				}
			}
		}
		return ht.elements();
	}

	public SchemeElement getSchemeElement(String element_id)
	{
		return getSchemeElement(this, element_id);
	}

	protected SchemeElement getSchemeElement(Scheme scheme, String element_id)
	{
		for (int i = 0; i < scheme.elements.size(); i++)
		{
			SchemeElement element = (SchemeElement)scheme.elements.get(i);
			if (element.getId().equals(element_id))
				return element;
		}

		for (int i = 0; i < scheme.elements.size(); i++)
		{
			SchemeElement element = (SchemeElement)scheme.elements.get(i);
			if (!element.scheme_id.equals(""))
			{
				Scheme inner_scheme = (Scheme)Pool.get(Scheme.typ, element.scheme_id);
				SchemeElement el = getSchemeElement(inner_scheme, element_id);
				if (el != null)
					return el;
			}
			else
			{
				for (Enumeration e = element.getAllChilds(); e.hasMoreElements();)
				{
					SchemeElement se = (SchemeElement)e.nextElement();
					if (se.getId().equals(element_id))
						return se;
				}
			}
		}
		return null;
	}

	public SchemeElement getSchemeElementByCablePort(String port_id)
	{
		return getSchemeElementByCablePort(this, port_id);
	}

	protected SchemeElement getSchemeElementByCablePort(Scheme scheme, String port_id)
	{
		for (int i = 0; i < scheme.elements.size(); i++)
		{
			SchemeElement element = getSchemeElementByCablePort((SchemeElement)scheme.elements.get(i), port_id);
			if (element != null)
				return element;
		}
		return null;
	}

	protected SchemeElement getSchemeElementByCablePort(SchemeElement element, String port_id)
	{
		for(int j = 0; j < element.devices.size(); j++)
		{
			SchemeDevice sd = (SchemeDevice )element.devices.get(j);
			for(int k = 0; k < sd.cableports.size(); k++)
				if(((SchemeCablePort )sd.cableports.get(k)).getId().equals(port_id))
					return element;
		}

		for(int j = 0; j < element.element_ids.size(); j++)			// Search inner elements
		{
			SchemeElement child_element = (SchemeElement)Pool.get(SchemeElement.typ, (String)element.element_ids.get(j));
			SchemeElement el = getSchemeElementByCablePort(child_element, port_id);
			if (el != null)
				return el;
		}

		if (!element.scheme_id.equals(""))// Search inner schemes
		{
			Scheme child_scheme = (Scheme)Pool.get(Scheme.typ, element.scheme_id);
			SchemeElement el = getSchemeElementByCablePort(child_scheme, port_id);
			if (el != null)
				return el;
		}

		return null;
	}

	public SchemeElement getSchemeElementByPort(String port_id)
	{
		return getSchemeElementByPort(this, port_id);
	}

	protected SchemeElement getSchemeElementByPort(Scheme scheme, String port_id)
	{
		for (int i = 0; i < scheme.elements.size(); i++)
		{
			SchemeElement element = getSchemeElementByPort((SchemeElement)scheme.elements.get(i), port_id);
			if (element != null)
				return element;
		}
		return null;
	}

	protected SchemeElement getSchemeElementByPort(SchemeElement element, String port_id)
	{
		for(int j = 0; j < element.devices.size(); j++)
		{
			SchemeDevice sd = (SchemeDevice )element.devices.get(j);
			for(int k = 0; k < sd.ports.size(); k++)
				if(((SchemePort )sd.ports.get(k)).getId().equals(port_id))
					return element;
		}

		for(int j = 0; j < element.element_ids.size(); j++)			// Search inner elements
		{
			SchemeElement child_element = (SchemeElement)Pool.get(SchemeElement.typ, (String)element.element_ids.get(j));

			SchemeElement el = getSchemeElementByPort(child_element, port_id);
			if (el != null)
				return el;
		}

		if (!element.scheme_id.equals(""))     // Search inner schemes
		{
			Scheme child_scheme = (Scheme)Pool.get(Scheme.typ, element.scheme_id);
			SchemeElement el = getSchemeElementByPort(child_scheme, port_id);
			if (el != null)
				return el;
		}

		return null;
	}

	public boolean isSchemeContainsLink(String link_id)
	{
		return isSchemeContainsLink(this, link_id);
	}

	protected boolean isSchemeContainsLink(Scheme scheme, String link_id)
	{
		for (int i = 0; i < scheme.links.size(); i++)
		{
			SchemeLink sl = (SchemeLink)scheme.links.get(i);
			if (sl.link_id.equals(link_id))
				return true;
		}

		for (int i = 0; i < scheme.elements.size(); i++)
		{
			SchemeElement element = (SchemeElement)scheme.elements.get(i);
			if (element.scheme_id.equals(""))
			{
				for (Enumeration en = element.getAllElementsLinks(); en.hasMoreElements();)
				{
					SchemeLink sl = (SchemeLink)en.nextElement();
					if (sl.link_id.equals(link_id))
						return true;
				}
			}
			else
				return isSchemeContainsLink((Scheme)Pool.get(Scheme.typ, element.scheme_id), link_id);
		}
		return false;
	}

	public boolean isSchemeContainsCableLink(String cable_link_id)
	{
		return isSchemeContainsCableLink(this, cable_link_id);
	}

	protected boolean isSchemeContainsCableLink(Scheme scheme, String cable_link_id)
	{
		for (int i = 0; i < scheme.cablelinks.size(); i++)
		{
			SchemeCableLink sl = (SchemeCableLink)scheme.cablelinks.get(i);
			if (sl.cable_link_id.equals(cable_link_id))
				return true;
		}

		for (int i = 0; i < scheme.elements.size(); i++)
		{
			SchemeElement element = (SchemeElement)scheme.elements.get(i);
			if (!element.scheme_id.equals(""))
				return isSchemeContainsLink((Scheme)Pool.get(Scheme.typ, element.scheme_id), cable_link_id);
		}
		return false;
	}



/*
	public SchemeElement getSchemeElementByPort(String port_id)
	{
		for (int i = 0; i < elements.size(); i++)
		{
			SchemeElement element = (SchemeElement)elements.get(i);
			for(int j = 0; j < element.devices.size(); j++)
			{
				SchemeDevice sd = (SchemeDevice )element.devices.get(j);
				for(int k = 0; k < sd.ports.size(); k++)
					if(((SchemePort )sd.ports.get(k)).getId().equals(port_id))
						return element;
			}
		}
		return null;
	}*/

	public SchemeElement getSchemeElementByDevice(String device_id)
	{
		return getSchemeElementByDevice(this, device_id);
	}

	protected SchemeElement getSchemeElementByDevice(Scheme scheme, String device_id)
	{
		for (int i = 0; i < scheme.elements.size(); i++)
		{
			SchemeElement element = getSchemeElementByDevice((SchemeElement)scheme.elements.get(i), device_id);
			if (element != null)
				return element;
		}
		return null;
	}

	protected SchemeElement getSchemeElementByDevice(SchemeElement element, String device_id)
	{
		for(int j = 0; j < element.devices.size(); j++)
		{
			SchemeDevice sd = (SchemeDevice )element.devices.get(j);
			if(sd.getId().equals(device_id))
				return element;
		}

		for(int j = 0; j < element.element_ids.size(); j++)
		{
			SchemeElement child_element = (SchemeElement)Pool.get(SchemeElement.typ, (String)element.element_ids.get(j));
			// Search inner elements
			SchemeElement el = getSchemeElementByDevice(child_element, device_id);
			if (el != null)
				return el;
		}

		if (!element.scheme_id.equals(""))// Search inner schemes
		{
			Scheme child_scheme = (Scheme)Pool.get(Scheme.typ, element.scheme_id);
			SchemeElement el = getSchemeElementByDevice(child_scheme, device_id);
			if (el != null)
				return el;
		}

		return null;
	}

	//find top level element at this scheme for element se
	public SchemeElement getTopLevelNonSchemeElement(SchemeElement se)
	{
		SchemeElement element = getTopLevelElement(se);
		if (!element.scheme_id.equals(""))
		{
			Scheme inner = (Scheme)Pool.get(Scheme.typ, element.scheme_id);
			return inner.getTopLevelNonSchemeElement(se);
		}
		else
			return element;
	}


	//find top level topological element at this scheme for element se
	public SchemeElement getTopologicalElement(SchemeElement se)
	{
		for (int i = 0; i < elements.size(); i++) // Search top elements
		{
			SchemeElement element = (SchemeElement)elements.get(i);
			if (element.getId().equals(se.getId()))
				return se;
		}

		for (int i = 0; i < elements.size(); i++) // Search inner elements
		{
			SchemeElement element = (SchemeElement)elements.get(i);
			if (element.scheme_id.equals(""))
			{
				for (Enumeration e = element.getAllChilds(); e.hasMoreElements();)
				{
					SchemeElement inner_se = (SchemeElement)e.nextElement();
					if (inner_se.getId().equals(se.getId()))
						return element;
				}
			}
		}

		for (int i = 0; i < elements.size(); i++)  // Search inner schemes
		{
			SchemeElement element = (SchemeElement)elements.get(i);
			if (!element.scheme_id.equals(""))
			{
				Scheme inner = (Scheme)Pool.get(Scheme.typ, element.scheme_id);
				SchemeElement inner_se = inner.getTopLevelElement(se);
				if (inner_se != null)
				{
					if(inner.scheme_type.equals(Scheme.CABLESUBNETWORK))
						return inner_se;
					else
						return element;
				}
			}
		}

		return null;
	}

	//find top level element at this scheme for element se
	public SchemeElement getTopLevelElement(SchemeElement se)
	{
		for (int i = 0; i < elements.size(); i++) // Search top elements
		{
			SchemeElement element = (SchemeElement)elements.get(i);
			if (element.getId().equals(se.getId()))
				return se;
		}

		for (int i = 0; i < elements.size(); i++) // Search inner elements
		{
			SchemeElement element = (SchemeElement)elements.get(i);
			if (element.scheme_id.equals(""))
			{
				for (Enumeration e = element.getAllChilds(); e.hasMoreElements();)
				{
					SchemeElement inner_se = (SchemeElement)e.nextElement();
					if (inner_se.getId().equals(se.getId()))
						return element;
				}
			}
		}

		for (int i = 0; i < elements.size(); i++)  // Search inner schemes
		{
			SchemeElement element = (SchemeElement)elements.get(i);
			if (!element.scheme_id.equals(""))
			{
				Scheme inner = (Scheme)Pool.get(Scheme.typ, element.scheme_id);
				SchemeElement inner_se = inner.getTopLevelElement(se);
				if (inner_se != null)
					return element;
			}
		}

		return null;
	}

	public SchemeElement getTopLevelElement(String se_id)
	{
		SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, se_id);
		return getTopLevelElement(se);
	}

	public SchemeElement getTopologicalElement(String se_id)
	{
		SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, se_id);
		return getTopologicalElement(se);
	}
}