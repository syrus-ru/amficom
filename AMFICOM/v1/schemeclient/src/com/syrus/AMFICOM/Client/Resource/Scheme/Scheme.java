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

import java.io.*;
import java.util.*;
import java.util.zip.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;

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
	public String scheme_type = Scheme.NETWORK;

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
		for (Iterator it = elements.iterator(); it.hasNext();)
			scheme.elements.add(((SchemeElement)it.next()).clone(dataSource));
		scheme.cablelinks = new Vector(cablelinks.size());
		for (Iterator it = cablelinks.iterator(); it.hasNext();)
			scheme.cablelinks.add(((SchemeCableLink)it.next()).clone(dataSource));
		scheme.links = new Vector(links.size());
		for (Iterator it = links.iterator(); it.hasNext();)
			scheme.links.add(((SchemeLink)it.next()).clone(dataSource));

		scheme.paths = new Vector();
		//scheme.paths = new Vector(paths.size());
		//	for (int i = 0; i < paths.size(); i++)
		//		scheme.paths.add(((SchemePath)paths.get(i)).clone(dataSource));

		scheme.created = 0;
		scheme.modified = 0;
		scheme.created_by = dataSource.getSession().getUserId();
		scheme.modified_by = dataSource.getSession().getUserId();

		for (Iterator it = elements_to_register.iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement)it.next();
			String cloned_se_id = (String)Pool.get("clonedids", se.getId());
			SchemeElement cloned_se = (SchemeElement)Pool.get(SchemeElement.typ, cloned_se_id);
			scheme.elements_to_register.add(cloned_se);
		}

		scheme.owner_id = dataSource.getSession().getUserId();
		scheme.description = description;
		scheme.domain_id = dataSource.getSession().getDomainId();
		scheme.symbol_id = symbol_id;
		scheme.label = label;

		if (schemecell != null)
		{
			scheme.schemecell = new byte[schemecell.length];
			System.arraycopy(schemecell, 0, scheme.schemecell, 0, schemecell.length);
		}
		if (ugo != null)
		{
			scheme.ugo = new byte[ugo.length];
			System.arraycopy(ugo, 0, scheme.ugo, 0, ugo.length);
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
		for (Iterator it = elements_to_register.iterator(); it.hasNext();)
		{
			String id = ((SchemeElement)it.next()).getId();
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

		HashSet all_elements = new HashSet();
		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement)it.next();
			all_elements.add(se);
			for (Iterator it2 = se.getChildElements().iterator(); it2.hasNext();)
				all_elements.add(it2.next());
		}

		transferable.elements = new SchemeElement_Transferable[all_elements.size()];
		int counter = 0;
		for(Iterator it = all_elements.iterator(); it.hasNext();)
		{
			SchemeElement element = (SchemeElement)it.next();
			element.setTransferableFromLocal();
			transferable.elements[counter++] = (SchemeElement_Transferable)element.getTransferable();
		}
		counter = 0;
		transferable.element_ids = new String[elements.size()];
		for (Iterator it = elements.iterator(); it.hasNext();)
			transferable.element_ids[counter++] = ((SchemeElement)it.next()).getId();

		transferable.cable_links = new SchemeCableLink_Transferable[cablelinks.size()];
		transferable.links = new SchemeLink_Transferable[links.size()];
		transferable.paths = new SchemePath_Transferable[paths.size()];
		counter = 0;
		for (Iterator it = cablelinks.iterator(); it.hasNext();)
		{
			SchemeCableLink cablelink = (SchemeCableLink)it.next();
			cablelink.setTransferableFromLocal();
			transferable.cable_links[counter++] = (SchemeCableLink_Transferable)cablelink.getTransferable();
		}
		counter = 0;
		for (Iterator it = links.iterator(); it.hasNext();)
		{
			SchemeLink link = (SchemeLink)it.next();
			link.setTransferableFromLocal();
			transferable.links[counter++] = (SchemeLink_Transferable)link.getTransferable();
		}
		counter = 0;
		for (Iterator it = paths.iterator(); it.hasNext();)
		{
			SchemePath path = (SchemePath)it.next();
			path.setTransferableFromLocal();
			transferable.paths[counter++] = (SchemePath_Transferable)path.getTransferable();
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
		for(Iterator it = elements_to_register.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			Pool.put(SchemeElement.typ, el.getId(), el);
			el.updateLocalFromTransferable();
		}
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			Pool.put(SchemeElement.typ, el.getId(), el);
			el.updateLocalFromTransferable();
		}
		for(Iterator it = cablelinks.iterator(); it.hasNext();)
		{
			SchemeCableLink link = (SchemeCableLink)it.next();
			Pool.put(SchemeCableLink.typ, link.getId(), link);
			link.updateLocalFromTransferable();
		}
		for(Iterator it = links.iterator(); it.hasNext();)
		{
			SchemeLink link = (SchemeLink)it.next();
			Pool.put(SchemeLink.typ, link.getId(), link);
			link.updateLocalFromTransferable();
		}
		for(Iterator it = paths.iterator(); it.hasNext();)
		{
			SchemePath path = (SchemePath)it.next();
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

	Iterator getChildElements(SchemeElement element)
	{
		HashSet v = new HashSet();
		for(Iterator it = element.element_ids.iterator(); it.hasNext();)
		{
			SchemeElement child = (SchemeElement)Pool.get(SchemeElement.typ, (String)it.next());
			for(Iterator it2 = getChildElements(child); it2.hasNext();)
				v.add(it2.next());
			v.add(child);
		}
		return v.iterator();
	}

	public ObjectResourceModel getModel()
	{
		return new SchemeModel(this);
	}

	public static ObjectResourceDisplayModel getDefaultDisplayModel()
	{
		return new SchemeDisplayModel();
	}

	// return all links at scheme including inner schemes
	public Collection getAllSchemeLinks()
	{
		HashSet ht = new HashSet();
		for (Iterator it = links.iterator(); it.hasNext();)
			ht.add(it.next());

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (!el.scheme_id.equals(""))
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				for (Iterator inner = scheme.getAllLinks().iterator(); inner.hasNext();)
					ht.add(inner.next());
			}
		}
		return ht;
	}

	public Scheme getSchemeByLink(String link_id)
	{
		for (Iterator it = links.iterator(); it.hasNext();)
		{
			SchemeLink l = (SchemeLink)it.next();
			if (l.getId().equals(link_id))
				return this;
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (el.scheme_id.equals(""))
			{
				for (Iterator it2 = el.links.iterator(); it2.hasNext();)
				{
					SchemeLink l = (SchemeLink)it2.next();
					if (l.getId().equals(link_id))
						return this;
				}
			}
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (!el.scheme_id.equals(""))
			{
				Scheme sch = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				Scheme found = sch.getSchemeByLink (link_id);
				if (found != null)
					return found;
			}
		}
		return null;
	}

	public Scheme getSchemeByCableLink(String cable_link_id)
	{
		for (Iterator it = cablelinks.iterator(); it.hasNext();)
		{
			SchemeCableLink l = (SchemeCableLink)it.next();
			if (l.getId().equals(cable_link_id))
				return this;
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (!el.scheme_id.equals(""))
			{
				Scheme sch = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				Scheme found = sch.getSchemeByCableLink (cable_link_id);
				if (found != null)
					return found;
			}
		}
		return null;
	}

	// return all links at scheme including inner schemes and links in elements
	public Collection getAllLinks()
	{
		HashSet ht = new HashSet();
		for (Iterator it = links.iterator(); it.hasNext();)
			ht.add(it.next());

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (el.scheme_id.equals(""))
			{
				for (Iterator it2 = el.getAllElementsLinks().iterator(); it2.hasNext();)
					ht.add(it2.next());
			}
			else
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				for (Iterator inner = scheme.getAllLinks().iterator(); inner.hasNext();)
					ht.add(inner.next());
			}
		}
		return ht;
	}

	// return all cablelinks at scheme including inner schemes
	public Collection getAllCableLinks()
	{
		HashSet ht = new HashSet();
		for (Iterator it = cablelinks.iterator(); it.hasNext();)
			ht.add(it.next());

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (!el.scheme_id.equals(""))
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				for (Iterator inner = scheme.getAllCableLinks().iterator(); inner.hasNext();)
				{
					SchemeCableLink l = (SchemeCableLink)inner.next();
					ht.add(l);
				}
			}
		}
		return ht;
	}

	public Collection getTopLevelElements()
	{
		return elements;
	}

	public Collection getTopLevelCableLinks()
	{
		return cablelinks;
	}

	public Collection getTopLevelPaths()
	{
		return paths;
	}

	public Collection getTopologicalElements1()
	{
		return elements;
	}

	public Collection getTopologicalCableLinks1()
	{
		return cablelinks;
	}

	public Collection getTopologicalPaths1()
	{
		return paths;
	}

	// return all top level elements at scheme and at inner schemes
	public Collection getTopologicalElements()
	{
		HashSet ht = new HashSet();
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (el.scheme_id.equals(""))
				ht.add(el);
			else
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				if(scheme.scheme_type.equals(Scheme.CABLESUBNETWORK))
				{
					for (Iterator inner = scheme.getTopologicalElements().iterator(); inner.hasNext();)
						ht.add(inner.next());
				}
				else
					ht.add(el);
			}
		}
		return ht;
	}

	// return all top level elements at scheme and at inner cable links
	public Collection getTopologicalCableLinks()
	{
		HashSet ht = new HashSet();
		for (int i = 0; i < cablelinks.size(); i++)
		{
			SchemeCableLink scl = (SchemeCableLink )cablelinks.get(i);
			ht.add(scl);
		}
		for (int i = 0; i < elements.size(); i++)
		{
			SchemeElement el = (SchemeElement)elements.get(i);
			if (el.scheme_id != null && !el.scheme_id.equals(""))
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				if(scheme.scheme_type.equals(Scheme.CABLESUBNETWORK))
				{
					for (Iterator inner = scheme.getTopologicalCableLinks().iterator(); inner.hasNext();)
					{
						SchemeCableLink scl = (SchemeCableLink)inner.next();
						ht.add(scl);
					}
				}
			}
		}
		return ht;
	}

	public Collection getTopologicalPaths()
	{
		HashSet ht = new HashSet();
		for (int i = 0; i < paths.size(); i++)
		{
			SchemePath sp = (SchemePath)paths.get(i);
			ht.add(sp);
		}
		for (int i = 0; i < elements.size(); i++)
		{
			SchemeElement el = (SchemeElement)elements.get(i);
			if (el.scheme_id != null && !el.scheme_id.equals(""))
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				if(scheme.scheme_type.equals(Scheme.CABLESUBNETWORK))
				{
					for (Iterator inner = scheme.getTopologicalPaths().iterator(); inner.hasNext();)
					{
						SchemePath sp = (SchemePath)inner.next();
						ht.add(sp);
					}
				}
			}
		}
		return ht;
	}

	// return all top level elements at scheme and at inner schemes
	public Collection getAllTopElements()
	{
		HashSet ht = new HashSet();
		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (el.scheme_id.equals(""))
				ht.add(el);
			else
			{
				Scheme scheme = (Scheme)Pool.get(Scheme.typ, el.scheme_id);
				for (Iterator inner = scheme.getTopLevelElements().iterator(); inner.hasNext();)
					ht.add(inner.next());
			}
		}
		return ht;
	}

	public SchemeElement getSchemeElement(String element_id)
	{
		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.getId().equals(element_id))
				return element;
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.scheme_id.length() != 0)
			{
				Scheme inner_scheme = (Scheme)Pool.get(Scheme.typ, element.scheme_id);
				SchemeElement el = inner_scheme.getSchemeElement(element_id);
				if (el != null)
					return el;
			}
			else
			{
				for (Iterator it2 = element.getAllChilds().iterator(); it2.hasNext();)
				{
					SchemeElement se = (SchemeElement)it2.next();
					if (se.getId().equals(element_id))
						return se;
				}
			}
		}
		return null;
	}

	public SchemeElement getSchemeElementByCablePort(String port_id)
	{
		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			el = el.getSchemeElementByCablePort(port_id);
			if (el != null)
				return el;
		}
		return null;
	}

	public SchemeElement getSchemeElementByPort(String port_id)
	{
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			el = el.getSchemeElementByPort(port_id);
			if (el != null)
				return el;
		}
		return null;
	}

	public boolean isSchemeContainsLink(String link_id)
	{
		for (Iterator it = links.iterator(); it.hasNext();)
		{
			SchemeLink sl = (SchemeLink)it.next();
			if (sl.link_id.equals(link_id))
				return true;
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.scheme_id.equals(""))
			{
				for (Iterator it2 = element.getAllElementsLinks().iterator(); it2.hasNext();)
				{
					SchemeLink sl = (SchemeLink)it2.next();
					if (sl.link_id.equals(link_id))
						return true;
				}
			}
			else
			{
				Scheme inner = (Scheme)Pool.get(Scheme.typ, element.scheme_id);
				return inner.isSchemeContainsLink(link_id);
			}
		}
		return false;
	}

	public boolean isSchemeContainsCableLink(String cable_link_id)
	{
		for (Iterator it = cablelinks.iterator(); it.hasNext();)
		{
			SchemeCableLink sl = (SchemeCableLink)it.next();
			if (sl.cable_link_id.equals(cable_link_id))
				return true;
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement element = (SchemeElement)it.next();
			if (!element.scheme_id.equals(""))
			{
				Scheme inner = (Scheme)Pool.get(Scheme.typ, element.scheme_id);
				return inner.isSchemeContainsCableLink(cable_link_id);
			}
		}
		return false;
	}

	public SchemeElement getSchemeElementByDevice(String device_id)
	{
		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			el = el.getSchemeElementByDevice(device_id);
			if (el != null)
				return el;
		}
		return null;
	}

	public SchemeElement getTopLevelNonSchemeElement(String se_id)
	{
		SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, se_id);
		return getTopLevelNonSchemeElement(se);
	}

	//find top level element at this scheme for element se
	public SchemeElement getTopLevelNonSchemeElement(SchemeElement se)
	{
		SchemeElement element = getTopLevelElement(se);
		if (element.scheme_id.length() != 0)
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
		for(Iterator it = elements.iterator(); it.hasNext();) // Search top elements
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.getId().equals(se.getId()))
				return se;
		}

		for(Iterator it = elements.iterator(); it.hasNext();) // Search inner elements
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.scheme_id.length() == 0)
			{
				for (Iterator it2 = element.getAllChilds().iterator(); it2.hasNext();)
				{
					SchemeElement inner_se = (SchemeElement)it2.next();
					if (inner_se.getId().equals(se.getId()))
						return element;
				}
			}
		}

		for (Iterator it = elements.iterator(); it.hasNext();)  // Search inner schemes
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.scheme_id.length() != 0)
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
		for (Iterator it = elements.iterator(); it.hasNext();) // Search top elements
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.getId().equals(se.getId()))
				return se;
		}

		for (Iterator it = elements.iterator(); it.hasNext();) // Search inner elements
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.scheme_id.length() == 0)
			{
				for (Iterator it2 = element.getAllChilds().iterator(); it2.hasNext();)
				{
					SchemeElement inner_se = (SchemeElement)it2.next();
					if (inner_se.getId().equals(se.getId()))
						return element;
				}
			}
		}

		for (Iterator it = elements.iterator(); it.hasNext();)  // Search inner schemes
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.scheme_id.length() != 0)
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

