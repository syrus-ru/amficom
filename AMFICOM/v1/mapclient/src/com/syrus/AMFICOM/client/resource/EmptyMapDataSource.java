package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

import oracle.aurora.jndi.sess_iiop.ServiceCtx;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
//import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.General.*;

public class EmptyMapDataSource
		extends EmptyDataSource
		implements DataSourceInterface
{
	protected EmptyMapDataSource()
	{
	}

	public EmptyMapDataSource(SessionInterface si)
	{
		super(si);
	}

	public void LoadProtoElements()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		ImageCatalogue.add(
				"well",
				new ImageResource("well", "well", "images/well.gif"));
		ImageCatalogue.add(
				"wins",
				new ImageResource("wins", "wins", "images/wins.gif"));
		ImageCatalogue.add(
				"pc",
				new ImageResource("pc", "pc", "images/pc.gif"));
		ImageCatalogue.add(
				"net",
				new ImageResource("net", "net", "images/net.gif"));
/*
		ImageCatalogue.add(
				"node",
				new ImageResource("node", "node", "images/node.gif"));
		ImageCatalogue.add(
				"void",
				new ImageResource("void", "void", "images/void.gif"));
		ImageCatalogue.add(
				"cable",
				new ImageResource("cable", "cable", "images/linkmode.gif"));
		ImageCatalogue.add(
				"path",
				new ImageResource("path", "path", "images/pathmode.gif"));
*/
/*
	    MapEquipmentProtoElement pcElement = new MapEquipmentProtoElement(
			"user_9",
			"pc",
			"pc",
			"",
			"pc",
			"pc",
			"pc",
			true,
			"",
			"");
	    pcElement.setImageID("pc");
		Pool.put("mapequipmentproto", pcElement.getId(), pcElement);
//		Pool.putName("mapequipmentproto", pcElement.getId(), pcElement.getName());

	    MapKISProtoElement winsElement = new MapKISProtoElement(
			"user_9",
			"wins",
			"wins",
			"",
			"wins",
			"wins",
			"wins",
			true,
			"",
			"");
	    winsElement.setImageID("wins");
		Pool.put("mapkisproto", winsElement.getId(), winsElement);
//		Pool.putName("mapkisproto", winsElement.getId(), winsElement.getName());

	    MapEquipmentProtoElement wellElement = new MapEquipmentProtoElement(
			"user_9",
			"well",
			"well",
			"",
			"well",
			"well",
			"well",
			true,
			"",
			"");
	    wellElement.setImageID("well");
		Pool.put("mapequipmentproto", wellElement.getId(), wellElement);
//		Pool.putName("mapequipmentproto", wellElement.getId(), wellElement.getName());

	    MapEquipmentProtoElement netElement = new MapEquipmentProtoElement(
			"user_9",
			"net",
			"net",
			"",
			"net",
			"net",
			"net",
			true,
			"",
			"");
	    netElement.setImageID("net");
		Pool.put("mapequipmentproto", netElement.getId(), netElement);
//		Pool.putName("mapequipmentproto", netElement.getId(), netElement.getName());

	    MapPhysicalLinkProtoElement linkElement = new MapPhysicalLinkProtoElement(
			"link",
			null,
			null,
			null);
	    linkElement.name = "Link!";
		Pool.put("maplinkproto", linkElement.getId(), linkElement);
//		Pool.putName("maplinkproto", linkElement.getId(), linkElement.getName());
*/
	}

	public void LoadMapDescriptors()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
/*
		int i;
		int ecode = 0;
		int count;
		MapContextSeq_TransferableHolder mh = new MapContextSeq_TransferableHolder();
		MapContext_Transferable maps[];
		MapContext mc;

		try
		{
			ecode = si.ci.server.GetMapDescriptors(si.accessIdentity, mh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting map descriptors: " + ex.getMessage());
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetMapDescriptors! status = " + ecode);
			return;
		}

		maps = mh.value;
		count = maps.length;
		System.out.println("...Done! " + count + " map(s) fetched");

	    for (i = 0; i < count; i++)
		{
			mc = new MapContext(maps[i]);
			Pool.put("mapcontext", mc.getId(), mc);
			Pool.putName("mapcontext", mc.getId(), mc.getName());
	    }
*/
	}

	public void LoadMaps()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;
		MapContextSeq_TransferableHolder mh = new MapContextSeq_TransferableHolder();
		MapContext_Transferable maps[];
		MapContext mc;
		MapElementSeq_TransferableHolder eh = new MapElementSeq_TransferableHolder();
		MapElement_Transferable equipments[];
		MapEquipmentNodeElement equipment;
		MapKISElementSeq_TransferableHolder kh = new MapKISElementSeq_TransferableHolder();
		MapKISElement_Transferable kiss[];

		MapPhysicalNodeElementSeq_TransferableHolder nh = new MapPhysicalNodeElementSeq_TransferableHolder();
		MapPhysicalNodeElement_Transferable nodes[];
		MapPhysicalNodeElement node;
		MapNodeLinkElementSeq_TransferableHolder nlh = new MapNodeLinkElementSeq_TransferableHolder();
		MapNodeLinkElement_Transferable nodelinks[];
		MapNodeLinkElement nodelink;
		MapPhysicalLinkElementSeq_TransferableHolder lh = new MapPhysicalLinkElementSeq_TransferableHolder();
		MapPhysicalLinkElement_Transferable links[];
		MapPhysicalLinkElement link;
		MapPathElementSeq_TransferableHolder ph = new MapPathElementSeq_TransferableHolder();
		MapPathElement_Transferable paths[];
		MapTransmissionPathElement path;

		Vector loaded_objects = new Vector();
		ObjectResource or;
/*
		try
		{
			ecode = si.ci.server.GetMaps(si.accessIdentity, ih, mh, eh, kh, nh, nlh, lh, ph);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting maps: " + ex.getMessage());
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetMaps! status = " + ecode);
			return;
		}

		images = ih.value;
		count = images.length;
		System.out.println("...Done! " + count + " image(s) fetched");
	    for (i = 0; i < count; i++)
		{
			image = new ImageResource(images[i]);
			ImageCatalogue.add(image.getId(), image);
	    }

		maps = mh.value;
		count = maps.length;
		System.out.println("...Done! " + count + " map(s) fetched");
	    for (i = 0; i < count; i++)
		{
			mc = new MapContext(maps[i]);
			Pool.put("mapcontext", mc.getId(), mc);
			Pool.putName("mapcontext", mc.getId(), mc.getName());
			loaded_objects.add(mc);
	    }

		equipments = eh.value;
		count = equipments.length;
		System.out.println("...Done! " + count + " equipment(s) fetched");
	    for (i = 0; i < count; i++)
		{
			equipment = new MapEquipmentNodeElement(equipments[i]);
			Pool.put("mapequipmentelement", equipment.getId(), equipment);
			Pool.putName("mapequipmentelement", equipment.getId(), equipment.getName());
			loaded_objects.add(equipment);
	    }

		kiss = kh.value;
		count = kiss.length;
		System.out.println("...Done! " + count + " kis(s) fetched");
	    for (i = 0; i < count; i++)
		{
			kis = new MapKISNodeElement(kiss[i]);
			Pool.put("mapkiselement", kis.getId(), kis);
			Pool.putName("mapkiselement", kis.getId(), kis.getName());
			loaded_objects.add(kis);
	    }

		nodes = nh.value;
		count = nodes.length;
		System.out.println("...Done! " + count + " node(s) fetched");
	    for (i = 0; i < count; i++)
		{
			node = new MapPhysicalNodeElement(nodes[i]);
			Pool.put("mapnodeelement", node.getId(), node);
			Pool.putName("mapnodeelement", node.getId(), node.getName());
			loaded_objects.add(node);
	    }

		nodelinks = nlh.value;
		count = nodelinks.length;
		System.out.println("...Done! " + count + " nodelink(s) fetched");
	    for (i = 0; i < count; i++)
		{
			nodelink = new MapNodeLinkElement(nodelinks[i]);
			Pool.put("mapnodelinkelement", nodelink.getId(), nodelink);
			Pool.putName("mapnodelinkelement", nodelink.getId(), nodelink.getName());
			loaded_objects.add(nodelink);
	    }

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " link(s) fetched");
	    for (i = 0; i < count; i++)
		{
			link = new MapPhysicalLinkElement(links[i]);
			Pool.put("maplinkelement", link.getId(), link);
			Pool.putName("maplinkelement", link.getId(), link.getName());
			loaded_objects.add(link);
	    }

		paths = ph.value;
		count = paths.length;
		System.out.println("...Done! " + count + " path(s) fetched");
	    for (i = 0; i < count; i++)
		{
			path = new MapTransmissionPathElement(paths[i]);
			Pool.put("mappathelement", path.getId(), path);
			Pool.putName("mappathelement", path.getId(), path.getName());
			loaded_objects.add(path);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
*/
	}

	public void SaveMap(String mc_id)
	{
		MapContext mc = (MapContext )Pool.get("mapcontext", mc_id);

		if(si == null)
			return;
		if(!si.isOpened())
			return;
/*
		int i;
		int ecode = 0;
		int count;
		ObjectResource os;

		Hashtable image_vec = new Hashtable();
		Vector equipment_vec;
		Vector kis_vec;
		Vector node_vec;
		Vector vec;

		ImageResource_Transferable images[];
		MapContext_Transferable maps[];
		MapElement_Transferable equipments[];
		MapKISElement_Transferable kiss[];
		MapPhysicalNodeElement_Transferable nodes[];
		MapNodeLinkElement_Transferable nodelinks[];
		MapPhysicalLinkElement_Transferable links[];
		MapPathElement_Transferable paths[];

		ImageResource image;
		MapContext map;
		MapEquipmentNodeElement equipment;
		MapKISNodeElement kis;
		MapPhysicalNodeElement node;
		MapNodeLinkElement nodelink;
		MapPhysicalLinkElement link;
		MapTransmissionPathElement path;

		maps = new MapContext_Transferable[1];
		mc.setTransferableFromLocal();
		maps[0] = (MapContext_Transferable )mc.getTransferable();

		node_vec = new Vector();
		kis_vec = new Vector();
		equipment_vec = new Vector();

		vec = mc.getNodes();
		count = vec.size();
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource )vec.get(i);
			os.setTransferableFromLocal();
			if(os.getTyp().equals("mapnodeelement"))
			{
//				node = (MapPhysicalNodeElement )os;
				node_vec.add(os.getTransferable());
//				image_vec
			}
			if(os.getTyp().equals("mapkiselement"))
			{
				kis_vec.add(os.getTransferable());
			}
			if(os.getTyp().equals("mapequipmentelement"))
			{
				equipment_vec.add(os.getTransferable());
			}
		}
		nodes = new MapPhysicalNodeElement_Transferable[node_vec.size()];
		node_vec.copyInto(nodes);
		kiss = new MapKISElement_Transferable[kis_vec.size()];
		kis_vec.copyInto(kiss);
		equipments = new MapElement_Transferable[equipment_vec.size()];
		equipment_vec.copyInto(equipments);

		vec = mc.getNodeLinks();
		count = vec.size();
		nodelinks = new MapNodeLinkElement_Transferable[count];
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource )vec.get(i);
			os.setTransferableFromLocal();
			nodelinks[i] = (MapNodeLinkElement_Transferable )os.getTransferable();
		}

		vec = mc.getPhysicalLinks();
		count = vec.size();
		links = new MapPhysicalLinkElement_Transferable[count];
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource )vec.get(i);
			os.setTransferableFromLocal();
			links[i] = (MapPhysicalLinkElement_Transferable )os.getTransferable();
		}

		vec = mc.getTransmissionPath();
		count = vec.size();
		paths = new MapPathElement_Transferable[count];
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource )vec.get(i);
			os.setTransferableFromLocal();
			paths[i] = (MapPathElement_Transferable )os.getTransferable();
		}

		count = image_vec.size();
		images = new ImageResource_Transferable[image_vec.size()];
		for(Enumeration enum = image_vec.elements(); enum.hasMoreElements();)
		{
			os = (ObjectResource )enum.nextElement();
			os.setTransferableFromLocal();
			images[i] = (ImageResource_Transferable )os.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveMaps(
					si.accessIdentity,
					images,
					maps,
					equipments,
					kiss,
					nodes,
					nodelinks,
					links,
					paths);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving map: " + ex.getMessage());
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveMap! status = " + ecode);
			return;
		}
*/
	}

	public void SaveMaps()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

}