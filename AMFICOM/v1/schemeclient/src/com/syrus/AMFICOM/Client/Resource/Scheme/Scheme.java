package com.syrus.AMFICOM.Client.Resource.Scheme;

import java.io.*;
import java.util.*;
import java.util.zip.*;

import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceDisplayModel;
import com.syrus.AMFICOM.Client.Resource.*;

public class Scheme extends StubResource implements Serializable
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
	public String schemeType = Scheme.NETWORK;

	public Collection elementsTo_register;
	public Collection elements;
	public Collection cablelinks;
	public Collection links;
//	public Collection paths;
	public SolutionCompact solution;

//	public String path_conditionsId = "";

	public long created = 0;
	public long modified = 0;
	public String createdBy = "";
	public String modifiedBy = "";

	public String ownerId = "";
	public String description = "";
	public String domainId = "";
	public String symbolId = "";
	public String label = "";

	public int width = 840;
	public int height = 1190;

	public Serializable serializable_cell;
	public byte[] schemecell;
	public Serializable serializable_ugo;
	public byte[] ugo;
	public Map clones = new HashMap();

	public Scheme()
	{
		elementsTo_register = new ArrayList();
		elements = new ArrayList();
		cablelinks = new ArrayList();
		links = new ArrayList();
//		paths = new ArrayList();
		solution = new SolutionCompact();

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
		return domainId;
	}

	public Object clone(DataSourceInterface dataSource)
	{
		String clonedId = (String)Pool.get("clonedids", id);
		if (clonedId != null)
		{
			Scheme cloned = (Scheme)Pool.get(Scheme.typ, clonedId);
			if (cloned == null)
				System.err.println("Scheme.clone() id not found: " + clonedId);
			else
				return cloned;
		}

		Scheme scheme = new Scheme();

		scheme.id = dataSource.GetUId(Scheme.typ);
		scheme.name = name;
		scheme.schemeType = schemeType;

		for (Iterator it = elements.iterator(); it.hasNext();)
			scheme.elements.add(((SchemeElement)it.next()).clone(dataSource));
		for (Iterator it = cablelinks.iterator(); it.hasNext();)
			scheme.cablelinks.add(((SchemeCableLink)it.next()).clone(dataSource));
		for (Iterator it = links.iterator(); it.hasNext();)
			scheme.links.add(((SchemeLink)it.next()).clone(dataSource));

		//scheme.paths = new Vector(paths.size());
		//	for (int i = 0; i < paths.size(); i++)
		//		scheme.paths.add(((SchemePath)paths.get(i)).clone(dataSource));

		scheme.created = 0;
		scheme.modified = 0;
		scheme.createdBy = dataSource.getSession().getUserId();
		scheme.modifiedBy = dataSource.getSession().getUserId();

		for (Iterator it = elementsTo_register.iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement)it.next();
			String clonedSeId = (String)Pool.get("clonedids", se.getId());
			SchemeElement clonedSe = (SchemeElement)Pool.get(SchemeElement.typ, clonedSeId);
			scheme.elementsTo_register.add(clonedSe);
		}

		scheme.ownerId = dataSource.getSession().getUserId();
		scheme.description = description;
		scheme.domainId = dataSource.getSession().getDomainId();
		scheme.symbolId = symbolId;
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

		Map ht = Pool.getMap("clonedids");
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
		for (Iterator it = elementsTo_register.iterator(); it.hasNext();)
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

	public void setLocalFromTransferable()
	{
		id = transferable.id;
		name = transferable.name;
		schemeType = transferable.schemeType;
		created = transferable.created;
		modified = transferable.modified;
		modifiedBy = transferable.modifiedBy;
		createdBy = transferable.createdBy;

		ownerId = transferable.ownerId;
		domainId = transferable.domainId;
		symbolId = transferable.symbolId;
		label = transferable.label;
		description = transferable.description;

		elements = new ArrayList();
		elementsTo_register = new ArrayList();
		cablelinks = new ArrayList(transferable.cableLinks.length);
		links = new ArrayList(transferable.links.length);
//		paths = new ArrayList(transferable.paths.length);
		solution = new SolutionCompact(transferable.solution);

		ArrayList transferable_elementIds = new ArrayList(transferable.elementIds.length);

		for (int i = 0; i < transferable.elementIds.length; i++)
			transferable_elementIds.add(transferable.elementIds[i]);
		for (int i = 0; i < transferable.elements.length; i++)
		{
			SchemeElement element = new SchemeElement(transferable.elements[i]);
			if (transferable_elementIds.contains(element.getId()))
				elements.add(element);
			else
				elementsTo_register.add(element);
		}
		for (int i = 0; i < transferable.cableLinks.length; i++)
			cablelinks.add(new SchemeCableLink(transferable.cableLinks[i]));
		for (int i = 0; i < transferable.links.length; i++)
			links.add(new SchemeLink(transferable.links[i]));
//		for (int i = 0; i < transferable.paths.length; i++)
//			paths.add(new SchemePath(transferable.paths[i]));

		width = (transferable.width != 0 ? transferable.width : 840);
		height = (transferable.height != 0 ? transferable.height : 1190);
		if (transferable.clonez.length == 0)
			clones = new HashMap();
		else
		{
			try
			{
				ByteArrayInputStream bais = new ByteArrayInputStream(transferable.clonez);
				ObjectInputStream in = new ObjectInputStream(new GZIPInputStream(bais));
				clones = (Map)in.readObject();
				in.close();
			}
			catch (Exception e)
			{
				clones = new HashMap();
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
		transferable.schemeType = schemeType;
		transferable.created = created;
		transferable.modified = modified;
		transferable.createdBy = createdBy;
		transferable.modifiedBy = modifiedBy;
		transferable.modifiedBy = modifiedBy;

		transferable.ownerId = ownerId;
		transferable.domainId = domainId;
		transferable.symbolId = symbolId;
		transferable.label = label;
		transferable.description = description;

		LinkedHashSet all_elements = new LinkedHashSet();
		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement se = (SchemeElement)it.next();
			for (Iterator it2 = se.getAllChilds().iterator(); it2.hasNext();)
				all_elements.add(it2.next());
			all_elements.add(se);
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
		transferable.elementIds = new String[elements.size()];
		for (Iterator it = elements.iterator(); it.hasNext();)
			transferable.elementIds[counter++] = ((SchemeElement)it.next()).getId();

		transferable.cableLinks = new SchemeCableLink_Transferable[cablelinks.size()];
		transferable.links = new SchemeLink_Transferable[links.size()];
//		transferable.paths = new SchemePath_Transferable[paths.size()];
		solution.setTransferableFromLocal();
		transferable.solution = (SchemeMonitoringSolution_Transferable)solution.getTransferable();

		counter = 0;
		for (Iterator it = cablelinks.iterator(); it.hasNext();)
		{
			SchemeCableLink cablelink = (SchemeCableLink)it.next();
			cablelink.setTransferableFromLocal();
			transferable.cableLinks[counter++] = (SchemeCableLink_Transferable)cablelink.getTransferable();
		}
		counter = 0;
		for (Iterator it = links.iterator(); it.hasNext();)
		{
			SchemeLink link = (SchemeLink)it.next();
			link.setTransferableFromLocal();
			transferable.links[counter++] = (SchemeLink_Transferable)link.getTransferable();
		}
//		counter = 0;
//		for (Iterator it = paths.iterator(); it.hasNext();)
//		{
//			SchemePath path = (SchemePath)it.next();
//			path.setTransferableFromLocal();
//			transferable.paths[counter++] = (SchemePath_Transferable)path.getTransferable();
//		}

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
		for(Iterator it = elementsTo_register.iterator(); it.hasNext();)
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
//		for(Iterator it = paths.iterator(); it.hasNext();)
//		{
//			SchemePath path = (SchemePath)it.next();
//			Pool.put(SchemePath.typ, path.getId(), path);
//			path.updateLocalFromTransferable();
//		}
		solution.updateLocalFromTransferable();
	}

	public String getPropertyPaneClassName()
	{
		return "";
	}

	public synchronized boolean pack()
	{
		schemecell = pack(serializable_cell);
		ugo = pack(serializable_ugo);

		int i = 0;
		while (unpack(schemecell) == null)
		{
			System.out.println();
			schemecell = pack(serializable_cell);
			i++;
			if (i == 3)
				return false;
		}

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
			System.err.println(e.toString() + " packing scheme: retry...");
			return null;
		}
		catch (Exception e)
		{
			System.err.println(e.toString() + " packing scheme: retry...");
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
		catch (StackOverflowError e)
		{
			System.err.println("Error unpacking scheme: " + e.toString());
			return null;
		}
		catch (Exception e)
		{
			System.err.println("Exception unpacking scheme: " + e.toString());
			return null;
		}

	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException
	{
		out.writeObject(id);
		out.writeObject(name);
		out.writeObject(schemeType);
		out.writeLong(created);
		out.writeLong(modified);
		out.writeObject(createdBy);
		out.writeObject(modifiedBy);
		out.writeObject(ownerId);
		out.writeObject(description);
		out.writeObject(domainId);
		out.writeObject(symbolId);
		out.writeObject(label);

		out.writeObject(elements);
		out.writeObject(elementsTo_register);
		out.writeObject(cablelinks);
		out.writeObject(links);
//		out.writeObject(paths);
		out.writeObject(solution);

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
		schemeType = (String )in.readObject();
		created = in.readLong();
		modified = in.readLong();
		createdBy = (String )in.readObject();
		modifiedBy = (String )in.readObject();
		ownerId = (String )in.readObject();
		description = (String )in.readObject();
		domainId = (String )in.readObject();
		symbolId = (String )in.readObject();
		label = (String )in.readObject();

		elements = (Collection )in.readObject();
		elementsTo_register = (Collection )in.readObject();
		cablelinks = (Collection )in.readObject();
		links = (Collection )in.readObject();
//		paths = (Collection )in.readObject();
		solution = (SolutionCompact)in.readObject();

		width = in.readInt();
		height = in.readInt();

		schemecell = (byte[] )in.readObject();
		ugo = (byte[] )in.readObject();
		clones = (Map )in.readObject();

		transferable = new Scheme_Transferable();
		updateLocalFromTransferable();
	}

	Iterator getChildElements(SchemeElement element)
	{
		HashSet v = new HashSet();
		for(Iterator it = element.elementIds.iterator(); it.hasNext();)
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
			if (el.getInternalSchemeId().length() != 0)
			{
				Scheme scheme = el.getInternalScheme();
				for (Iterator inner = scheme.getAllLinks().iterator(); inner.hasNext();)
					ht.add(inner.next());
			}
		}
		return ht;
	}
/*
	public Scheme getSchemeBySchemeElement(String scheme_elementId)
	{
		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (el.getId().equals(scheme_elementId))
				return this;
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (el.getInternalSchemeId().length() == 0)
			{
				for (Iterator it2 = el.getChildElements().iterator(); it2.hasNext();)
				{
					SchemeElement el2 = (SchemeElement)it2.next();
					if (el2.getId().equals(scheme_elementId))
						return this;
				}
			}
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (el.getInternalSchemeId().length() != 0)
			{
				Scheme sch = el.getInternalScheme();
				Scheme found = sch.getSchemeBySchemeElement(scheme_elementId);
				if (found != null)
					return found;
			}
		}
		return null;
	}*/

	public Scheme getSchemeByLink(String linkId)
	{
		for (Iterator it = links.iterator(); it.hasNext();)
		{
			SchemeLink l = (SchemeLink)it.next();
			if (l.getId().equals(linkId))
				return this;
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (el.getInternalSchemeId().length() == 0)
			{
				for (Iterator it2 = el.getAllElementsLinks().iterator(); it2.hasNext();)
				{
					SchemeLink l = (SchemeLink)it2.next();
					if (l.getId().equals(linkId))
						return this;
				}
			}
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (el.getInternalSchemeId().length() != 0)
			{
				Scheme sch = el.getInternalScheme();
				Scheme found = sch.getSchemeByLink (linkId);
				if (found != null)
					return found;
			}
		}
		return null;
	}

	public Scheme getSchemeByCableLink(String cableLinkId)
	{
		for (Iterator it = cablelinks.iterator(); it.hasNext();)
		{
			SchemeCableLink l = (SchemeCableLink)it.next();
			if (l.getId().equals(cableLinkId))
				return this;
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (el.getInternalSchemeId().length() != 0)
			{
				Scheme sch = el.getInternalScheme();
				Scheme found = sch.getSchemeByCableLink (cableLinkId);
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
			if (el.getInternalSchemeId().length() == 0)
			{
				for (Iterator it2 = el.getAllElementsLinks().iterator(); it2.hasNext();)
					ht.add(it2.next());
			}
			else
			{
				Scheme scheme = el.getInternalScheme();
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
			if (el.getInternalSchemeId().length() != 0)
			{
				Scheme scheme = el.getInternalScheme();
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
		return solution.paths;
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
		return solution.paths;
	}

	// return all top level elements at scheme and at inner schemes
	public Collection getTopologicalElements()
	{
		HashSet ht = new HashSet();
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (el.getInternalSchemeId().length() == 0)
				ht.add(el);
			else
			{
				Scheme scheme = el.getInternalScheme();
				if(scheme.schemeType.equals(Scheme.CABLESUBNETWORK))
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
		ht.addAll(cablelinks);

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (el.getInternalSchemeId().length() != 0)
			{
				Scheme scheme = (Scheme)el.getInternalScheme();
				if(scheme.schemeType.equals(Scheme.CABLESUBNETWORK))
				{
					for (Iterator inner = scheme.getTopologicalCableLinks().iterator(); inner.hasNext();)
						ht.add(inner.next());
				}
			}
		}
		return ht;
	}

	public Collection getTopologicalPaths()
	{
		HashSet ht = new HashSet();
		ht.addAll(solution.paths);

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			if (el.getInternalSchemeId().length() != 0)
			{
				Scheme scheme = (Scheme)el.getInternalScheme();
				if(scheme.schemeType.equals(Scheme.CABLESUBNETWORK))
				{
					for (Iterator inner = scheme.getTopologicalPaths().iterator(); inner.hasNext();)
						ht.add(inner.next());
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
			if (el.getInternalSchemeId().length() == 0)
				ht.add(el);
			else
			{
				Scheme scheme = (Scheme)el.getInternalScheme();
				for (Iterator inner = scheme.getTopLevelElements().iterator(); inner.hasNext();)
					ht.add(inner.next());
			}
		}
		return ht;
	}

	public SchemeElement getSchemeElement(String elementId)
	{
		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.getId().equals(elementId))
				return element;
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.getInternalSchemeId().length() != 0)
			{
				Scheme innerScheme = element.getInternalScheme();
				SchemeElement el = innerScheme.getSchemeElement(elementId);
				if (el != null)
					return el;
			}
			else
			{
				for (Iterator it2 = element.getAllChilds().iterator(); it2.hasNext();)
				{
					SchemeElement se = (SchemeElement)it2.next();
					if (se.getId().equals(elementId))
						return se;
				}
			}
		}
		return null;
	}

	public SchemeElement getSchemeElementByCablePort(String portId)
	{
		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			el = el.getSchemeElementByCablePort(portId);
			if (el != null)
				return el;
		}
		return null;
	}

	public SchemeElement getSchemeElementByPort(String portId)
	{
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			el = el.getSchemeElementByPort(portId);
			if (el != null)
				return el;
		}
		return null;
	}

	public boolean isSchemeContainsLink(String linkId)
	{
		for (Iterator it = links.iterator(); it.hasNext();)
		{
			SchemeLink sl = (SchemeLink)it.next();
			if (sl.linkId.equals(linkId))
				return true;
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.getInternalSchemeId().length() == 0)
			{
				for (Iterator it2 = element.getAllElementsLinks().iterator(); it2.hasNext();)
				{
					SchemeLink sl = (SchemeLink)it2.next();
					if (sl.linkId.equals(linkId))
						return true;
				}
			}
			else
			{
				Scheme inner = element.getInternalScheme();
				return inner.isSchemeContainsLink(linkId);
			}
		}
		return false;
	}

	public boolean isSchemeContainsCableLink(String cableLinkId)
	{
		for (Iterator it = cablelinks.iterator(); it.hasNext();)
		{
			SchemeCableLink sl = (SchemeCableLink)it.next();
			if (sl.cableLinkId.equals(cableLinkId))
				return true;
		}

		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.getInternalSchemeId().length() != 0)
			{
				Scheme inner = element.getInternalScheme();
				return inner.isSchemeContainsCableLink(cableLinkId);
			}
		}
		return false;
	}

	public SchemeElement getSchemeElementByDevice(String deviceId)
	{
		for (Iterator it = elements.iterator(); it.hasNext();)
		{
			SchemeElement el = (SchemeElement)it.next();
			el = el.getSchemeElementByDevice(deviceId);
			if (el != null)
				return el;
		}
		return null;
	}

	public SchemeElement getTopLevelNonSchemeElement(String seId)
	{
		SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, seId);
		return getTopLevelNonSchemeElement(se);
	}

	//find top level element at this scheme for element se
	public SchemeElement getTopLevelNonSchemeElement(SchemeElement se)
	{
		SchemeElement element = getTopLevelElement(se);
		if (element.getInternalSchemeId().length() != 0)
		{
			Scheme inner = element.getInternalScheme();
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
			if (element.getInternalSchemeId().length() == 0)
			{
				for (Iterator it2 = element.getAllChilds().iterator(); it2.hasNext();)
				{
					SchemeElement innerSe = (SchemeElement)it2.next();
					if (innerSe.getId().equals(se.getId()))
						return element;
				}
			}
		}

		for (Iterator it = elements.iterator(); it.hasNext();)  // Search inner schemes
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.getInternalSchemeId().length() != 0)
			{
				Scheme inner = element.getInternalScheme();
				SchemeElement innerSe = inner.getTopLevelElement(se);
				if (innerSe != null)
				{
					if(inner.schemeType.equals(Scheme.CABLESUBNETWORK))
						return innerSe;
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
			if (element.getInternalSchemeId().length() == 0)
			{
				for (Iterator it2 = element.getAllChilds().iterator(); it2.hasNext();)
				{
					SchemeElement innerSe = (SchemeElement)it2.next();
					if (innerSe.getId().equals(se.getId()))
						return element;
				}
			}
		}

		for (Iterator it = elements.iterator(); it.hasNext();)  // Search inner schemes
		{
			SchemeElement element = (SchemeElement)it.next();
			if (element.getInternalSchemeId().length() != 0)
			{
				Scheme inner = element.getInternalScheme();
				SchemeElement innerSe = inner.getTopLevelElement(se);
				if (innerSe != null)
					return element;
			}
		}
		return null;
	}

	public SchemeElement getTopLevelElement(String seId)
	{
		SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, seId);
		return getTopLevelElement(se);
	}

	public SchemeElement getTopologicalElement(String seId)
	{
		SchemeElement se = (SchemeElement )Pool.get(SchemeElement.typ, seId);
		return getTopologicalElement(se);
	}
}
