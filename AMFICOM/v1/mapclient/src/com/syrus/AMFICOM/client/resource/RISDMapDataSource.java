package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Constants;
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
import com.syrus.AMFICOM.CORBA.Map.MapPipePathElementSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Map.MapPipePathElement_Transferable;
import com.syrus.AMFICOM.CORBA.Map.MapSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Map.MapSiteElementSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Map.MapSiteElement_Transferable;
import com.syrus.AMFICOM.CORBA.Map.Map_Transferable;
import com.syrus.AMFICOM.CORBA.Resource.ImageResourceSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Resource.ImageResource_Transferable;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.Map.Map;
import com.syrus.AMFICOM.Client.Resource.Map.MapLinkProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapMarkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalLinkElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPhysicalNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapPipePathElement;
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
			ecode = ((RISDSessionInfo )getSession()).ci.getServer().LoadStatedAttributeTypes(
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

		Vector loadedObjects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.getServer().GetMapProtoElements(si.accessIdentity, ih, peh, lh);
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
			loadedObjects.add(proto);
	    }

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " map proto link(s) fetched");
	    for (i = 0; i < count; i++)
		{
			link = new MapLinkProtoElement(links[i]);
			Pool.put("maplinkproto", link.getId(), link);
//			Pool.putName("maplinkproto", link.getId(), link.getName());
			loadedObjects.add(link);
	    }

		// update loaded objects
		count = loadedObjects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loadedObjects.get(i);
			or.updateLocalFromTransferable();
		}
	}
*/
	public void loadMapProtoElements(String[] eids, String[] lids)
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

		MapNodeProtoElementSeq_TransferableHolder peh = new MapNodeProtoElementSeq_TransferableHolder();
		MapNodeProtoElement_Transferable protos[];
		MapNodeProtoElement proto;
		
		MapLinkProtoElementSeq_TransferableHolder lh = new MapLinkProtoElementSeq_TransferableHolder();
		MapLinkProtoElement_Transferable links[];
		MapLinkProtoElement link;

		Vector loadedObjects = new Vector();
		ObjectResource or;

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.getServer().GetStatedMapProtoElements(
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
			loadedObjects.add(proto);
	    }

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " map proto link(s) fetched");
	    for (i = 0; i < count; i++)
		{
			link = new MapLinkProtoElement(links[i]);
			Pool.put(MapLinkProtoElement.typ, link.getId(), link);
			loadedObjects.add(link);
	    }

		// update loaded objects
		count = loadedObjects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loadedObjects.get(i);
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

		Vector imageVec = new Vector();
		ImageResource_Transferable images[];

		MapNodeProtoElement_Transferable mpes[] = new MapNodeProtoElement_Transferable[nids.length];
		for(int i = 0; i < nids.length; i++)
		{
			MapNodeProtoElement mpe = (MapNodeProtoElement )Pool.get(MapNodeProtoElement.typ, nids[i]);
			imageVec.add(ImageCatalogue.get(mpe.getImageId()));
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
		images = new ImageResource_Transferable[imageVec.size()];
		for(Enumeration enum = imageVec.elements(); enum.hasMoreElements(); i++)
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
			ecode = ((RISDSessionInfo )getSession()).ci.getServer().SaveMapProtoElements(
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
		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.getServer().RemoveMapProtoElements(
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

		Vector imageVec = new Vector();
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
			ecode = si.ci.getServer().SaveMapProtoElements(
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
			ecode = si.ci.getServer().RemoveMapProtoElements(si.accessIdentity, ids, pids);
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

	public void loadMaps(String[] ids)
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

		MapPipePathElementSeq_TransferableHolder ch = new MapPipePathElementSeq_TransferableHolder();
		MapPipePathElement_Transferable collectors[];
		MapPipePathElement  collector;

		Vector loadedObjects = new Vector();
		ObjectResource or;

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.getServer().GetStatedMaps(
					((RISDSessionInfo )getSession()).accessIdentity, 
					ids, 
					ih, 
					mh, 
					sh, 
					mrh, 
					nh, 
					nlh, 
					lh,
					ch);
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
			loadedObjects.add(mc);
	    }

		sites = sh.value;
		count = sites.length;
		System.out.println("...Done! " + count + " site(s) fetched");
	    for (i = 0; i < count; i++)
		{
			site = new MapSiteNodeElement(sites[i]);
			Pool.put(MapSiteNodeElement.typ, site.getId(), site);
			loadedObjects.add(site);
	    }

		marks = mrh.value;
		count = marks.length;
		System.out.println("...Done! " + count + " mark(s) fetched");
	    for (i = 0; i < count; i++)
		{
			mark = new MapMarkElement(marks[i]);
			Pool.put(MapMarkElement.typ, mark.getId(), mark);
			loadedObjects.add(mark);
	    }

		nodes = nh.value;
		count = nodes.length;
		System.out.println("...Done! " + count + " node(s) fetched");
	    for (i = 0; i < count; i++)
		{
			node = new MapPhysicalNodeElement(nodes[i]);
			Pool.put(MapPhysicalNodeElement.typ, node.getId(), node);
			loadedObjects.add(node);
	    }

		nodelinks = nlh.value;
		count = nodelinks.length;
		System.out.println("...Done! " + count + " nodelink(s) fetched");
	    for (i = 0; i < count; i++)
		{
			nodelink = new MapNodeLinkElement(nodelinks[i]);
			Pool.put(MapNodeLinkElement.typ, nodelink.getId(), nodelink);
			loadedObjects.add(nodelink);
	    }

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " link(s) fetched");
	    for (i = 0; i < count; i++)
		{
			link = new MapPhysicalLinkElement(links[i]);
			Pool.put(MapPhysicalLinkElement.typ, link.getId(), link);
			loadedObjects.add(link);
	    }

		collectors = ch.value;
		count = collectors.length;
		System.out.println("...Done! " + count + " collector(s) fetched");
	    for (i = 0; i < count; i++)
		{
			collector = new MapPipePathElement(collectors[i]);
			Pool.put(MapPipePathElement.typ, collector.getId(), collector);
			loadedObjects.add(collector);
	    }

		// update loaded objects
		count = loadedObjects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loadedObjects.get(i);
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
		ObjectResource os;

		Hashtable imageVec = new Hashtable();
		ArrayList siteVec = new ArrayList();
		ArrayList nodeVec = new ArrayList();
		ArrayList markVec = new ArrayList();
		ArrayList nodelinkVec = new ArrayList();
		ArrayList linkVec = new ArrayList();
		ArrayList collectorVec = new ArrayList();

		ImageResource_Transferable images[];
		Map_Transferable maps[];
		MapSiteElement_Transferable sites[];
		MapMarkElement_Transferable marks[];
		MapPhysicalNodeElement_Transferable nodes[];
		MapNodeLinkElement_Transferable nodelinks[];
		MapPhysicalLinkElement_Transferable links[];
		MapPipePathElement_Transferable collectors[];

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
					nodeVec.add(os.getTransferable());
				if(os.getTyp().equals(MapSiteNodeElement.typ))
					siteVec.add(os.getTransferable());
				if(os.getTyp().equals(MapMarkElement.typ))
					markVec.add(os.getTransferable());
			}

			for(Iterator it = mc.getNodeLinks().iterator(); it.hasNext();)
			{
				os = (ObjectResource )it.next();
				os.setTransferableFromLocal();
				nodelinkVec.add(os.getTransferable());
			}

			for(Iterator it = mc.getPhysicalLinks().iterator(); it.hasNext();)
			{
				os = (ObjectResource )it.next();
				os.setTransferableFromLocal();
				linkVec.add(os.getTransferable());
			}

			for(Iterator it = mc.getCollectors().iterator(); it.hasNext();)
			{
				os = (ObjectResource )it.next();
				os.setTransferableFromLocal();
				collectorVec.add(os.getTransferable());
			}
		}
	
		nodes = (MapPhysicalNodeElement_Transferable [])
			nodeVec.toArray(new MapPhysicalNodeElement_Transferable [nodeVec.size()]);
		sites = (MapSiteElement_Transferable [])
			siteVec.toArray(new MapSiteElement_Transferable [siteVec.size()]);
		marks = (MapMarkElement_Transferable [])
			markVec.toArray(new MapMarkElement_Transferable [markVec.size()]);
		nodelinks = (MapNodeLinkElement_Transferable [])
			nodelinkVec.toArray(new MapNodeLinkElement_Transferable [nodelinkVec.size()]);
		links = (MapPhysicalLinkElement_Transferable [])
			linkVec.toArray(new MapPhysicalLinkElement_Transferable [linkVec.size()]);
		collectors = (MapPipePathElement_Transferable [])
			collectorVec.toArray(new MapPipePathElement_Transferable [collectorVec.size()]);

		images = new ImageResource_Transferable[imageVec.size()];
		i = 0;
		for(Enumeration enum = imageVec.elements(); enum.hasMoreElements(); i++)
		{
			os = (ObjectResource )enum.nextElement();
			os.setTransferableFromLocal();
			images[i] = (ImageResource_Transferable )os.getTransferable();
		}

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.getServer().SaveMaps(
					((RISDSessionInfo )getSession()).accessIdentity,
					images,
					maps,
					sites,
					marks,
					nodes,
					nodelinks,
					links,
					collectors);
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
			ecode = ((RISDSessionInfo )getSession()).ci.getServer().RemoveMaps(
					((RISDSessionInfo )getSession()).accessIdentity,
					maps,
					leer,
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
	
		int ecode = 0;
		ObjectResource os;

		ArrayList siteVec = new ArrayList();
		ArrayList nodeVec = new ArrayList();
		ArrayList markVec = new ArrayList();
		ArrayList nodelinkVec = new ArrayList();
		ArrayList linkVec = new ArrayList();
		ArrayList collectorVec = new ArrayList();
		List vec;

		String maps[];
		String sites[];
		String marks[];
		String nodes[];
		String nodelinks[];
		String links[];
		String collectors[];

		maps = new String[0];

		vec = mc.getRemovedElements();
		for(Iterator it = vec.iterator(); it.hasNext();)
		{
			os = (ObjectResource )it.next();
			if(os.getTyp().equals(MapPhysicalNodeElement.typ))
				nodeVec.add(os.getId());
			if(os.getTyp().equals(MapSiteNodeElement.typ))
				siteVec.add(os.getId());
			if(os.getTyp().equals(MapMarkElement.typ))
				markVec.add(os.getId());
			if(os.getTyp().equals(MapNodeLinkElement.typ))
				nodelinkVec.add(os.getId());
			if(os.getTyp().equals(MapPhysicalLinkElement.typ))
				linkVec.add(os.getId());
			if(os.getTyp().equals(MapPipePathElement.typ))
				collectorVec.add(os.getId());
		}
		nodes = (String[] )nodeVec.toArray(new String[nodeVec.size()]);
		sites = (String[] )siteVec.toArray(new String[siteVec.size()]);
		marks = (String[] )markVec.toArray(new String[markVec.size()]);
		nodelinks = (String[] )nodelinkVec.toArray(new String[nodelinkVec.size()]);
		links = (String[] )linkVec.toArray(new String[linkVec.size()]);
		collectors = (String[] )collectorVec.toArray(new String[collectorVec.size()]);

		try
		{
			ecode = ((RISDSessionInfo )getSession()).ci.getServer().RemoveMaps(
					((RISDSessionInfo )getSession()).accessIdentity,
					maps,
					sites,
					marks,
					nodes,
					nodelinks,
					links,
					collectors);
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
