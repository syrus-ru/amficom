package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.CORBA.General.ElementAttributeTypeSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.General.ElementAttributeType_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapLinkProtoElementSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Map.MapLinkProtoElement_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapMarkElementSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Map.MapMarkElement_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapNodeLinkElementSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Map.MapNodeLinkElement_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapNodeProtoElementSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Map.MapNodeProtoElement_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapPhysicalLinkElementSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Map.MapPhysicalLinkElement_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapPhysicalNodeElementSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Map.MapPhysicalNodeElement_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Map.MapSiteElementSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Map.MapSiteElement_Transferable;
import com.syrus.AMFICOM.CORBA.Map.Map_Transferable;
import com.syrus.AMFICOM.CORBA.Resource.ImageResourceSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Resource.ImageResource_Transferable;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceImage;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.ImageCatalogue;
import com.syrus.AMFICOM.Client.Resource.ImageResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.RISDConfigDataSource;

import com.syrus.AMFICOM.Client.Resource.General.ElementAttributeType;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapSiteNodeElement;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class RISDMapDataSource
		extends RISDConfigDataSource
		implements DataSourceInterface
{
	protected RISDMapDataSource()
	{
	}

	public RISDMapDataSource(SessionInterface si)
	{
		super(si);
	}
/*
	public void LoadAttributeTypes(String[] ids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		int i;
		int ecode = 0;
		int count;

		ElementAttributeTypeSeq_TransferableHolder ath = new ElementAttributeTypeSeq_TransferableHolder();
		ElementAttributeType_Transferable atypes[];
		ElementAttributeType atype;

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.LoadStatedAttributeTypes(
					((RISDSessionInfo )getSession()).accessIdentity, 
					ids, 
					ath);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting attribute types: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadAttributeTypes! status = " + ecode);
			return;
		}

		atypes = ath.value;
		count = atypes.length;
		System.out.println("...Done! " + count + " attribute type(s) fetched");
			for (i = 0; i < count; i++)
		{
			atype = new ElementAttributeType(atypes[i]);
			Pool.put(ElementAttributeType.typ, atype.getId(), atype);
			}
	}
*/
/*
	public void LoadMapProtoElements()
	{
		if(this.getSession() == null)
			return;
		if(!this.getSession().isOpened())
			return;

		System.out.println("LoadProtoElements:");

		int i;
		int ecode = 0;
		int count;
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;

		MapNodeProtoElementSeq_TransferableHolder peh = new MapProtoElementSeq_TransferableHolder();
		MapNodeProtoElement_Transferable protos[];
		MapNodeProtoElement proto;
		
		MapLinkProtoElementSeq_TransferableHolder lh = new MapLinkProtoElementSeq_TransferableHolder();
		MapLinkProtoElement_Transferable links[];
		MapLinkProtoElement link;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.GetMapProtoElements(si.accessIdentity, ih, peh, lh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting map proto elements: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetProtoElements! status = " + ecode);
			return;
		}

		Vector imvec = new Vector();
	    for (i = 0; i < peh.value.length; i++)
			imvec.add(peh.value[i].symbolId);
		String[] imids = new String[imvec.size()];
		imvec.copyInto(imids);
		new DataSourceImage(this).LoadImages(imids);

		protos = peh.value;
		count = protos.length;
		System.out.println("...Done! " + count + " map proto element(s) fetched");
	    for (i = 0; i < count; i++)
		{
			proto = new MapNodeProtoElement(protos[i]);
			Pool.put("mapprotoelement", proto.getId(), proto);
			loaded_objects.add(proto);
	    }

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " map proto link(s) fetched");
	    for (i = 0; i < count; i++)
		{
			link = new MapLinkProtoElement(links[i]);
			Pool.put("maplinkproto", link.getId(), link);
//			Pool.putName("maplinkproto", link.getId(), link.getName());
			loaded_objects.add(link);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}
*/
	public void LoadMapProtoElements(String[] eids, String[] lids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("LoadProtoElements:");

		int i;
		int ecode = 0;
		int count;
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;

		MapNodeProtoElementSeq_TransferableHolder peh = new MapNodeProtoElementSeq_TransferableHolder();
		MapNodeProtoElement_Transferable protos[];
		MapNodeProtoElement proto;
		
		MapLinkProtoElementSeq_TransferableHolder lh = new MapLinkProtoElementSeq_TransferableHolder();
		MapLinkProtoElement_Transferable links[];
		MapLinkProtoElement link;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.GetStatedMapProtoElements(
					((RISDSessionInfo )getSession()).accessIdentity, 
					eids, 
					lids, 
					ih, 
					peh, 
					lh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting map proto elements: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetProtoElements! status = " + ecode);
			return;
		}

		Vector imvec = new Vector();
	    for (i = 0; i < peh.value.length; i++)
			imvec.add(peh.value[i].symbolId);
		String[] imids = new String[imvec.size()];
		imvec.copyInto(imids);
		new DataSourceImage(this).LoadImages(imids);

		protos = peh.value;
		count = protos.length;
		System.out.println("...Done! " + count + " map proto element(s) fetched");
	    for (i = 0; i < count; i++)
		{
			proto = new MapNodeProtoElement(protos[i]);
			Pool.put(MapNodeProtoElement.typ, proto.getId(), proto);
			loaded_objects.add(proto);
	    }

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " map proto link(s) fetched");
	    for (i = 0; i < count; i++)
		{
			link = new MapLinkProtoElement(links[i]);
			Pool.put(MapLinkProtoElement.typ, link.getId(), link);
			loaded_objects.add(link);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void SaveMapProtoElements(String[] nids, String[] lids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		int ecode;

		Vector image_vec = new Vector();
		ImageResource_Transferable images[];

		MapNodeProtoElement_Transferable mpes[] = new MapNodeProtoElement_Transferable[nids.length];
		for(int i = 0; i < nids.length; i++)
		{
			MapNodeProtoElement mpe = (MapNodeProtoElement )Pool.get(MapNodeProtoElement.typ, nids[i]);
			image_vec.add(ImageCatalogue.get(mpe.getImageId()));
			mpe.setTransferableFromLocal();
			mpes[i] = (MapNodeProtoElement_Transferable )mpe.getTransferable();
		}

		MapLinkProtoElement_Transferable mles[] = new MapLinkProtoElement_Transferable[lids.length];
		for(int i = 0; i < lids.length; i++)
		{
			MapLinkProtoElement mle = (MapLinkProtoElement )Pool.get(MapLinkProtoElement.typ, lids[i]);
			mle.setTransferableFromLocal();
			mles[i] = (MapLinkProtoElement_Transferable )mle.getTransferable();
		}

		int i = 0;
		images = new ImageResource_Transferable[image_vec.size()];
		for(Enumeration enum = image_vec.elements(); enum.hasMoreElements(); i++)
		{
			ObjectResource os = (ObjectResource )enum.nextElement();
			if(os != null)
			{
				os.setTransferableFromLocal();
				images[i] = (ImageResource_Transferable )os.getTransferable();
			}
		}

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.SaveMapProtoElements(
					((RISDSessionInfo )getSession()).accessIdentity, 
					images,
					mpes,
					mles);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving LinkTypes: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveLinkTypes! status = " + ecode);
			return;
		}
	}
	
	public void RemoveMapProtoElements(String[] nids, String[] lids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		int ecode;
		String []gids = new String[0];
		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.RemoveMapProtoElements(
					((RISDSessionInfo )getSession()).accessIdentity,
					nids, 
					lids);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing MapProtos: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveMapProtoElements! status = " + ecode);
			return;
		}
	}
/*	
	public void SaveMapProtoGroups(String[] nids, String[] lids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		int ecode;

		Vector image_vec = new Vector();
		ImageResource_Transferable images[] = new ImageResource_Transferable[0];;
		MapProtoElement_Transferable mpes[] = new MapProtoElement_Transferable[0];
		MapProtoGroup_Transferable mpgs[] = new MapProtoGroup_Transferable[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			MapProtoGroup mpg = (MapProtoGroup )Pool.get(MapProtoGroup.typ, ids[i]);
			mpg.setTransferableFromLocal();
			mpgs[i] = (MapProtoGroup_Transferable )mpg.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveMapProtoElements(
					si.accessIdentity, 
					images,
					mpgs,
					mpes);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving LinkTypes: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveLinkTypes! status = " + ecode);
			return;
		}
	}
	
	public void RemoveMapProtoGroups(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		String []pids = new String[0];
		try
		{
			ecode = si.ci.server.RemoveMapProtoElements(si.accessIdentity, ids, pids);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing MapProtos: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveMapProtoElements! status = " + ecode);
			return;
		}
	}
*/	

	public void LoadMaps(String[] ids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("LoadMaps:");

		int i;
		int ecode = 0;
		int count;
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;
		MapSeq_TransferableHolder mh = new MapSeq_TransferableHolder();
		Map_Transferable maps[];
		Map mc;
		MapSiteElementSeq_TransferableHolder sh = new MapSiteElementSeq_TransferableHolder();
		MapSiteElement_Transferable sites[];
		MapSiteNodeElement site;

		MapMarkElementSeq_TransferableHolder mrh = new MapMarkElementSeq_TransferableHolder();
		MapMarkElement_Transferable marks[];
		MapMarkElement mark;

		MapPhysicalNodeElementSeq_TransferableHolder nh = new MapPhysicalNodeElementSeq_TransferableHolder();
		MapPhysicalNodeElement_Transferable nodes[];
		MapPhysicalNodeElement node;

		MapNodeLinkElementSeq_TransferableHolder nlh = new MapNodeLinkElementSeq_TransferableHolder();
		MapNodeLinkElement_Transferable nodelinks[];
		MapNodeLinkElement nodelink;

		MapPhysicalLinkElementSeq_TransferableHolder lh = new MapPhysicalLinkElementSeq_TransferableHolder();
		MapPhysicalLinkElement_Transferable links[];
		MapPhysicalLinkElement link;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.GetStatedMaps(
					((RISDSessionInfo )getSession()).accessIdentity, 
					ids, 
					ih, 
					mh, 
					sh, 
					mrh, 
					nh, 
					nlh, 
					lh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting maps: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetMaps! status = " + ecode);
			return;
		}

		Vector imvec = new Vector();
	    for (i = 0; i < sh.value.length; i++)
			imvec.add(sh.value[i].symbolId);
		String[] imids = new String[imvec.size()];
		imvec.copyInto(imids);
		new DataSourceImage(this).LoadImages(imids);

		maps = mh.value;
		count = maps.length;
		System.out.println("...Done! " + count + " map(s) fetched");
	    for (i = 0; i < count; i++)
		{
			mc = new Map(maps[i]);
			Pool.put(Map.typ, mc.getId(), mc);
			loaded_objects.add(mc);
	    }

		sites = sh.value;
		count = sites.length;
		System.out.println("...Done! " + count + " site(s) fetched");
	    for (i = 0; i < count; i++)
		{
			site = new MapSiteNodeElement(sites[i]);
			Pool.put(MapSiteNodeElement.typ, site.getId(), site);
			loaded_objects.add(site);
	    }

		marks = mrh.value;
		count = marks.length;
		System.out.println("...Done! " + count + " mark(s) fetched");
	    for (i = 0; i < count; i++)
		{
			mark = new MapMarkElement(marks[i]);
			Pool.put(MapMarkElement.typ, mark.getId(), mark);
			loaded_objects.add(mark);
	    }

		nodes = nh.value;
		count = nodes.length;
		System.out.println("...Done! " + count + " node(s) fetched");
	    for (i = 0; i < count; i++)
		{
			node = new MapPhysicalNodeElement(nodes[i]);
			Pool.put(MapPhysicalNodeElement.typ, node.getId(), node);
			loaded_objects.add(node);
	    }

		nodelinks = nlh.value;
		count = nodelinks.length;
		System.out.println("...Done! " + count + " nodelink(s) fetched");
	    for (i = 0; i < count; i++)
		{
			nodelink = new MapNodeLinkElement(nodelinks[i]);
			Pool.put(MapNodeLinkElement.typ, nodelink.getId(), nodelink);
			loaded_objects.add(nodelink);
	    }

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " link(s) fetched");
	    for (i = 0; i < count; i++)
		{
			link = new MapPhysicalLinkElement(links[i]);
			Pool.put(MapPhysicalLinkElement.typ, link.getId(), link);
			loaded_objects.add(link);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void SaveMaps(String[] mc_ids)
	{
		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("SaveMap:");

		int i;
		int ecode = 0;
		int count;
		ObjectResource os;

		Hashtable image_vec = new Hashtable();
		ArrayList site_vec = new ArrayList();
		ArrayList node_vec = new ArrayList();
		ArrayList mark_vec = new ArrayList();
		ArrayList nodelink_vec = new ArrayList();
		ArrayList link_vec = new ArrayList();
		ArrayList vec;

		ImageResource_Transferable images[];
		Map_Transferable maps[];
		MapSiteElement_Transferable sites[];
		MapMarkElement_Transferable marks[];
		MapPhysicalNodeElement_Transferable nodes[];
		MapNodeLinkElement_Transferable nodelinks[];
		MapPhysicalLinkElement_Transferable links[];

		ImageResource image;
		Map map;
		MapSiteNodeElement site;
		MapMarkElement mark;
		MapPhysicalNodeElement node;
		MapNodeLinkElement nodelink;
		MapPhysicalLinkElement link;

		maps = new Map_Transferable[mc_ids.length];
		for(i = 0; i < mc_ids.length; i++)
		{
			Map mc = (Map )Pool.get(Map.typ, mc_ids[i]);

			mc.setTransferableFromLocal();
			maps[i] = (Map_Transferable )mc.getTransferable();

			for(Iterator it = mc.getNodes().iterator(); it.hasNext();)
			{
				os = (ObjectResource )it.next();
				os.setTransferableFromLocal();
				if(os.getTyp().equals(MapPhysicalNodeElement.typ))
					node_vec.add(os.getTransferable());
				if(os.getTyp().equals(MapSiteNodeElement.typ))
					site_vec.add(os.getTransferable());
				if(os.getTyp().equals(MapMarkElement.typ))
					mark_vec.add(os.getTransferable());
			}

			for(Iterator it = mc.getNodeLinks().iterator(); it.hasNext();)
			{
				os = (ObjectResource )it.next();
				os.setTransferableFromLocal();
				nodelink_vec.add(os.getTransferable());
			}

			for(Iterator it = mc.getPhysicalLinks().iterator(); it.hasNext();)
			{
				os = (ObjectResource )it.next();
				os.setTransferableFromLocal();
				link_vec.add(os.getTransferable());
			}
		}
	
		nodes = (MapPhysicalNodeElement_Transferable [])
			node_vec.toArray(new MapPhysicalNodeElement_Transferable[0]);
		sites = (MapSiteElement_Transferable [])
			site_vec.toArray(new MapSiteElement_Transferable[0]);
		marks = (MapMarkElement_Transferable [])
			mark_vec.toArray(new MapMarkElement_Transferable[0]);
		nodelinks = (MapNodeLinkElement_Transferable [])
			node_vec.toArray(new MapNodeLinkElement_Transferable[0]);
		links = (MapPhysicalLinkElement_Transferable [])
			node_vec.toArray(new MapPhysicalLinkElement_Transferable[0]);

		count = image_vec.size();
		images = new ImageResource_Transferable[image_vec.size()];
		i = 0;
		for(Enumeration enum = image_vec.elements(); enum.hasMoreElements(); i++)
		{
			os = (ObjectResource )enum.nextElement();
			os.setTransferableFromLocal();
			images[i] = (ImageResource_Transferable )os.getTransferable();
		}

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.SaveMaps(
					((RISDSessionInfo )getSession()).accessIdentity,
					images,
					maps,
					sites,
					marks,
					nodes,
					nodelinks,
					links);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving map: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveMap! status = " + ecode);
			return;
		}
	}

	public void RemoveMap(String mc_id)
	{
		Map mc = (Map )Pool.get(Map.typ, mc_id);

		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("RemoveMap:");

		int ecode = 0;

		String[] maps = new String[1];
		maps[0] = mc.getId();
		String[] leer = new String[0];

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.RemoveMaps(
					((RISDSessionInfo )getSession()).accessIdentity,
					maps,
					leer,
					leer,
					leer,
					leer,
					leer);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing map: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveMap! status = " + ecode);
			return;
		}
	}

	public void RemoveFromMap(String mc_id)
	{
		Map mc = (Map )Pool.get(Map.typ, mc_id);

		if(getSession() == null)
			return;
		if(!getSession().isOpened())
			return;

		System.out.println("RemoveFromMap:");
	
		int i;
		int ecode = 0;
		int count;
		ObjectResource os;

		ArrayList site_vec = new ArrayList();
		ArrayList node_vec = new ArrayList();
		ArrayList mark_vec = new ArrayList();
		ArrayList nodelink_vec = new ArrayList();
		ArrayList link_vec = new ArrayList();
		List vec;

		String maps[];
		String sites[];
		String marks[];
		String nodes[];
		String nodelinks[];
		String links[];

		Map map;
		MapSiteNodeElement site;
		MapMarkElement mark;
		MapPhysicalNodeElement node;
		MapNodeLinkElement nodelink;
		MapPhysicalLinkElement link;

		maps = new String[0];

		vec = mc.getRemovedElements();
		for(Iterator it = vec.iterator(); it.hasNext();)
		{
			os = (ObjectResource )it.next();
			if(os.getTyp().equals(MapPhysicalNodeElement.typ))
				node_vec.add(os.getId());
			if(os.getTyp().equals(MapSiteNodeElement.typ))
				site_vec.add(os.getId());
			if(os.getTyp().equals(MapMarkElement.typ))
				mark_vec.add(os.getId());
			if(os.getTyp().equals(MapNodeLinkElement.typ))
				nodelink_vec.add(os.getId());
			if(os.getTyp().equals(MapPhysicalLinkElement.typ))
				link_vec.add(os.getId());
		}
		nodes = (String[] )node_vec.toArray(new String[node_vec.size()]);
		sites = (String[] )site_vec.toArray(new String[site_vec.size()]);
		marks = (String[] )mark_vec.toArray(new String[mark_vec.size()]);
		nodelinks = (String[] )nodelink_vec.toArray(new String[nodelink_vec.size()]);
		links = (String[] )link_vec.toArray(new String[link_vec.size()]);

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.server.RemoveMaps(
					((RISDSessionInfo )getSession()).accessIdentity,
					maps,
					sites,
					marks,
					nodes,
					nodelinks,
					links);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing from map: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveFromMap! status = " + ecode);
			return;
		}
	}


}
