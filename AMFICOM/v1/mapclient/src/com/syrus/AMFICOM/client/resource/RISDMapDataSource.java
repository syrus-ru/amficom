package com.syrus.AMFICOM.Client.Resource;

import java.io.*;
import java.util.*;

import oracle.aurora.jndi.sess_iiop.ServiceCtx;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.Map.*;
import com.syrus.AMFICOM.CORBA.Scheme.*;
import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.CORBA.Network.*;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.*;
import com.syrus.AMFICOM.CORBA.ISM.*;
import com.syrus.AMFICOM.CORBA.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
//import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.General.*;

public class RISDMapDataSource
		extends RISDSchemeDataSource
		implements DataSourceInterface
{
	protected RISDMapDataSource()
	{
	}

	public RISDMapDataSource(SessionInterface si)
	{
		super(si);
	}

	public void LoadMapProtoElements()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		System.out.println("LoadProtoElements:");

		int i;
		int ecode = 0;
		int count;
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;
/*
		MapEquipmentElementSeq_TransferableHolder eh = new MapEquipmentElementSeq_TransferableHolder();
		MapEquipmentElement_Transferable equipments[];
		MapEquipmentProtoElement equipment;
		MapKISElementSeq_TransferableHolder kh = new MapKISElementSeq_TransferableHolder();
		MapKISElement_Transferable kiss[];
		MapKISProtoElement kis;
*/
		MapProtoGroupSeq_TransferableHolder pgh = new MapProtoGroupSeq_TransferableHolder();
		MapProtoGroup_Transferable groups[];
		MapProtoGroup group;
		MapProtoElementSeq_TransferableHolder peh = new MapProtoElementSeq_TransferableHolder();
		MapProtoElement_Transferable protos[];
		MapProtoElement proto;
		
		MapLinkProtoElementSeq_TransferableHolder lh = new MapLinkProtoElementSeq_TransferableHolder();
		MapLinkProtoElement_Transferable links[];
		MapPhysicalLinkProtoElement link;
		MapPathProtoElementSeq_TransferableHolder ph = new MapPathProtoElementSeq_TransferableHolder();
		MapPathProtoElement_Transferable paths[];
		MapTransmissionPathProtoElement path;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.GetMapProtoElements(si.accessIdentity, ih, pgh, peh, lh, ph);
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
			imvec.add(peh.value[i].symbol_id);
		String[] imids = new String[imvec.size()];
		imvec.copyInto(imids);
		new DataSourceImage(this).LoadImages(imids);
/*
		images = ih.value;
		count = images.length;
		System.out.println("...Done! " + count + " image(s) fetched");
	    for (i = 0; i < count; i++)
		{
			image = new ImageResource(images[i]);
			ImageCatalogue.add(image.getId(), image);
	    }
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
		ImageCatalogue.add(
				"net",
				new ImageResource("net", "net", "images/net.gif"));

		ImageCatalogue.add(
				"well",
				new ImageResource("well", "well", "images/well.gif"));
		ImageCatalogue.add(
				"wins",
				new ImageResource("wins", "wins", "images/wins.gif"));
		ImageCatalogue.add(
				"pc",
				new ImageResource("pc", "pc", "images/pc.gif"));
*/
		groups = pgh.value;
		count = groups.length;
		System.out.println("...Done! " + count + " map proto group(s) fetched");
	    for (i = 0; i < count; i++)
		{
			group = new MapProtoGroup(groups[i]);
			Pool.put("mapprotogroup", group.getId(), group);
			loaded_objects.add(group);
	    }

		protos = peh.value;
		count = protos.length;
		System.out.println("...Done! " + count + " map proto element(s) fetched");
	    for (i = 0; i < count; i++)
		{
			proto = new MapProtoElement(protos[i]);
			Pool.put("mapprotoelement", proto.getId(), proto);
			loaded_objects.add(proto);
	    }

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " map proto link(s) fetched");
	    for (i = 0; i < count; i++)
		{
			link = new MapPhysicalLinkProtoElement(links[i]);
			Pool.put("maplinkproto", link.getId(), link);
//			Pool.putName("maplinkproto", link.getId(), link.getName());
			loaded_objects.add(link);
	    }

		paths = ph.value;
		count = paths.length;
		System.out.println("...Done! " + count + " map proto path(s) fetched");
	    for (i = 0; i < count; i++)
		{
			path = new MapTransmissionPathProtoElement(paths[i]);
			Pool.put("mappathproto", path.getId(), path);
//			Pool.putName("mappathproto", path.getId(), path.getName());
			loaded_objects.add(path);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void LoadMapProtoElements(Vector gids, Vector eids, Vector lids, Vector pids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		System.out.println("LoadProtoElements:");

		int i;
		int ecode = 0;
		int count;
		ImageResourceSeq_TransferableHolder ih = new ImageResourceSeq_TransferableHolder();
		ImageResource_Transferable images[];
		ImageResource image;

		MapProtoGroupSeq_TransferableHolder pgh = new MapProtoGroupSeq_TransferableHolder();
		MapProtoGroup_Transferable groups[];
		MapProtoGroup group;
		MapProtoElementSeq_TransferableHolder peh = new MapProtoElementSeq_TransferableHolder();
		MapProtoElement_Transferable protos[];
		MapProtoElement proto;
		
		MapLinkProtoElementSeq_TransferableHolder lh = new MapLinkProtoElementSeq_TransferableHolder();
		MapLinkProtoElement_Transferable links[];
		MapPhysicalLinkProtoElement link;
		MapPathProtoElementSeq_TransferableHolder ph = new MapPathProtoElementSeq_TransferableHolder();
		MapPathProtoElement_Transferable paths[];
		MapTransmissionPathProtoElement path;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		String[] gid_s = new String[gids.size()];
		gids.copyInto(gid_s);
		String[] eid_s = new String[eids.size()];
		eids.copyInto(eid_s);
		String[] lid_s = new String[lids.size()];
		lids.copyInto(lid_s);
		String[] pid_s = new String[pids.size()];
		pids.copyInto(pid_s);
		try
		{
			ecode = si.ci.server.GetStatedMapProtoElements(si.accessIdentity, gid_s, eid_s, lid_s, pid_s, ih, pgh, peh, lh, ph);
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
			imvec.add(peh.value[i].symbol_id);
		String[] imids = new String[imvec.size()];
		imvec.copyInto(imids);
		new DataSourceImage(this).LoadImages(imids);
/*
		images = ih.value;
		count = images.length;
		System.out.println("...Done! " + count + " image(s) fetched");
	    for (i = 0; i < count; i++)
		{
			image = new ImageResource(images[i]);
			ImageCatalogue.add(image.getId(), image);

	    }
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
		ImageCatalogue.add(
				"net",
				new ImageResource("net", "net", "images/net.gif"));
*/
		groups = pgh.value;
		count = groups.length;
		System.out.println("...Done! " + count + " map proto group(s) fetched");
	    for (i = 0; i < count; i++)
		{
			group = new MapProtoGroup(groups[i]);
			Pool.put("mapprotogroup", group.getId(), group);
			loaded_objects.add(group);
	    }

		protos = peh.value;
		count = protos.length;
		System.out.println("...Done! " + count + " map proto element(s) fetched");
	    for (i = 0; i < count; i++)
		{
			proto = new MapProtoElement(protos[i]);
			Pool.put("mapprotoelement", proto.getId(), proto);
			loaded_objects.add(proto);
	    }

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " map proto link(s) fetched");
	    for (i = 0; i < count; i++)
		{
			link = new MapPhysicalLinkProtoElement(links[i]);
			Pool.put("maplinkproto", link.getId(), link);
//			Pool.putName("maplinkproto", link.getId(), link.getName());
			loaded_objects.add(link);
	    }

		paths = ph.value;
		count = paths.length;
		System.out.println("...Done! " + count + " map proto path(s) fetched");
	    for (i = 0; i < count; i++)
		{
			path = new MapTransmissionPathProtoElement(paths[i]);
			Pool.put("mappathproto", path.getId(), path);
//			Pool.putName("mappathproto", path.getId(), path.getName());
			loaded_objects.add(path);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void SaveMapProtoElements(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;

		Vector image_vec = new Vector();
		ImageResource_Transferable images[];
		MapProtoGroup_Transferable mpgs[] = new MapProtoGroup_Transferable[0];
		MapProtoElement_Transferable mpes[] = new MapProtoElement_Transferable[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			MapProtoElement mpe = (MapProtoElement )Pool.get(MapProtoElement.typ, ids[i]);
			image_vec.add(ImageCatalogue.get(mpe.getImageID()));
			mpe.setTransferableFromLocal();
			mpes[i] = (MapProtoElement_Transferable )mpe.getTransferable();
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
	
	public void RemoveMapProtoElements(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		String []gids = new String[0];
		try
		{
			ecode = si.ci.server.RemoveMapProtoElements(si.accessIdentity, gids, ids);
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
	
	public void SaveMapProtoGroups(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
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
	
	public void LoadMapDescriptors()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		System.out.println("LoadMapDescriptors:");

		int i;
		int ecode = 0;
		int count;
		MapContextSeq_TransferableHolder mh = new MapContextSeq_TransferableHolder();
		MapContext_Transferable maps[];
		MapContext mc;
/*
		try
		{
			ecode = si.ci.server.GetMapDescriptors(si.accessIdentity, mh);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting map descriptors: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed GetMapDescriptors! status = " + ecode);
			return;
		}
*/
		maps = mh.value;
		count = maps.length;
		System.out.println("...Done! " + count + " map(s) fetched");

	    for (i = 0; i < count; i++)
		{
			mc = new MapContext(maps[i]);
			Pool.put("mapcontext", mc.getId(), mc);
//			Pool.putName("mapcontext", mc.getId(), mc.getName());
	    }
	}

	public void LoadMaps()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		System.out.println("LoadMaps:");

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
		MapElementSeq_TransferableHolder kh = new MapElementSeq_TransferableHolder();
		MapElement_Transferable kiss[];


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
		MapPathElementSeq_TransferableHolder ph = new MapPathElementSeq_TransferableHolder();
		MapPathElement_Transferable paths[];
		MapTransmissionPathElement path;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.GetMaps(si.accessIdentity, ih, mh, eh, kh, mrh, nh, nlh, lh, ph);
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
	    for (i = 0; i < eh.value.length; i++)
			imvec.add(eh.value[i].symbol_id);
	    for (i = 0; i < kh.value.length; i++)
			imvec.add(kh.value[i].symbol_id);
	    for (i = 0; i < mrh.value.length; i++)
			imvec.add(mrh.value[i].symbol_id);
	    for (i = 0; i < nh.value.length; i++)
			imvec.add(nh.value[i].symbol_id);
		String[] imids = new String[imvec.size()];
		imvec.copyInto(imids);
		new DataSourceImage(this).LoadImages(imids);
/*
		images = ih.value;
		count = images.length;
		System.out.println("...Done! " + count + " image(s) fetched");
	    for (i = 0; i < count; i++)
		{
			image = new ImageResource(images[i]);
			ImageCatalogue.add(image.getId(), image);
	    }
*/
		maps = mh.value;
		count = maps.length;
		System.out.println("...Done! " + count + " map(s) fetched");
	    for (i = 0; i < count; i++)
		{
			mc = new MapContext(maps[i]);
			Pool.put("mapcontext", mc.getId(), mc);
//			Pool.putName("mapcontext", mc.getId(), mc.getName());
			loaded_objects.add(mc);
	    }

		equipments = eh.value;
		count = equipments.length;
		System.out.println("...Done! " + count + " equipment(s) fetched");
	    for (i = 0; i < count; i++)
		{
			equipment = new MapEquipmentNodeElement(equipments[i]);
			Pool.put("mapequipmentelement", equipment.getId(), equipment);
//			Pool.putName("mapequipmentelement", equipment.getId(), equipment.getName());
			loaded_objects.add(equipment);
	    }
/*
		kiss = kh.value;
		count = kiss.length;
		System.out.println("...Done! " + count + " kis(s) fetched");
	    for (i = 0; i < count; i++)
		{
			kis = new MapKISNodeElement(kiss[i]);
			Pool.put("mapkiselement", kis.getId(), kis);
//			Pool.putName("mapkiselement", kis.getId(), kis.getName());
			loaded_objects.add(kis);
	    }
*/		

		marks = mrh.value;
		count = marks.length;
		System.out.println("...Done! " + count + " mark(s) fetched");
	    for (i = 0; i < count; i++)
		{
			mark = new MapMarkElement(marks[i]);
			Pool.put("mapmarkelement", mark.getId(), mark);
			loaded_objects.add(mark);
	    }

		nodes = nh.value;
		count = nodes.length;
		System.out.println("...Done! " + count + " node(s) fetched");
	    for (i = 0; i < count; i++)
		{
			node = new MapPhysicalNodeElement(nodes[i]);
			Pool.put("mapnodeelement", node.getId(), node);
//			Pool.putName("mapnodeelement", node.getId(), node.getName());
			loaded_objects.add(node);
	    }

		nodelinks = nlh.value;
		count = nodelinks.length;
		System.out.println("...Done! " + count + " nodelink(s) fetched");
	    for (i = 0; i < count; i++)
		{
			nodelink = new MapNodeLinkElement(nodelinks[i]);
			Pool.put("mapnodelinkelement", nodelink.getId(), nodelink);
//			Pool.putName("mapnodelinkelement", nodelink.getId(), nodelink.getName());
			loaded_objects.add(nodelink);
	    }

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " link(s) fetched");
	    for (i = 0; i < count; i++)
		{
			link = new MapPhysicalLinkElement(links[i]);
			Pool.put("maplinkelement", link.getId(), link);
//			Pool.putName("maplinkelement", link.getId(), link.getName());
			loaded_objects.add(link);
	    }

		paths = ph.value;
		count = paths.length;
		System.out.println("...Done! " + count + " path(s) fetched");
	    for (i = 0; i < count; i++)
		{
			path = new MapTransmissionPathElement(paths[i]);
			Pool.put("mappathelement", path.getId(), path);
//			Pool.putName("mappathelement", path.getId(), path.getName());
			loaded_objects.add(path);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void LoadMaps(Vector ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		for(int i = 0; i < ids.size(); i++)
			LoadMap((String )ids.get(i));
	}

	public void LoadMap(String map_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		System.out.println("LoadMap: " + map_id);

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
		MapElementSeq_TransferableHolder kh = new MapElementSeq_TransferableHolder();
		MapElement_Transferable kiss[];

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
		MapPathElementSeq_TransferableHolder ph = new MapPathElementSeq_TransferableHolder();
		MapPathElement_Transferable paths[];
		MapTransmissionPathElement path;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.GetMap(si.accessIdentity, map_id, ih, mh, eh, kh, mrh, nh, nlh, lh, ph);
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
	    for (i = 0; i < eh.value.length; i++)
			imvec.add(eh.value[i].symbol_id);
	    for (i = 0; i < kh.value.length; i++)
			imvec.add(kh.value[i].symbol_id);
	    for (i = 0; i < mrh.value.length; i++)
			imvec.add(mrh.value[i].symbol_id);
	    for (i = 0; i < nh.value.length; i++)
			imvec.add(nh.value[i].symbol_id);
		String[] imids = new String[imvec.size()];
		imvec.copyInto(imids);
		new DataSourceImage(this).LoadImages(imids);
/*
		images = ih.value;
		count = images.length;
		System.out.println("...Done! " + count + " image(s) fetched");
	    for (i = 0; i < count; i++)
		{
			image = new ImageResource(images[i]);
			ImageCatalogue.add(image.getId(), image);
	    }
*/
		maps = mh.value;
		count = maps.length;
		System.out.println("...Done! " + count + " map(s) fetched");
	    for (i = 0; i < count; i++)
		{
			mc = new MapContext(maps[i]);
			Pool.put("mapcontext", mc.getId(), mc);
			loaded_objects.add(mc);
	    }

		equipments = eh.value;
		count = equipments.length;
		System.out.println("...Done! " + count + " equipment(s) fetched");
	    for (i = 0; i < count; i++)
		{
			SchemeElement se = (SchemeElement )Pool.get(SchemeElement .typ, equipments[i].element_id);

			equipment = new MapEquipmentNodeElement(equipments[i]);
			Pool.put("mapequipmentelement", equipment.getId(), equipment);
			loaded_objects.add(equipment);
	    }
/*
		kiss = kh.value;
		count = kiss.length;
		System.out.println("...Done! " + count + " kis(s) fetched");
	    for (i = 0; i < count; i++)
		{
			kis = new MapKISNodeElement(kiss[i]);
			Pool.put("mapkiselement", kis.getId(), kis);
			loaded_objects.add(kis);
	    }
*/		
		marks = mrh.value;
		count = marks.length;
		System.out.println("...Done! " + count + " mark(s) fetched");
	    for (i = 0; i < count; i++)
		{
			mark = new MapMarkElement(marks[i]);
			Pool.put("mapmarkelement", mark.getId(), mark);
			loaded_objects.add(mark);
	    }

		nodes = nh.value;
		count = nodes.length;
		System.out.println("...Done! " + count + " node(s) fetched");
	    for (i = 0; i < count; i++)
		{
			node = new MapPhysicalNodeElement(nodes[i]);
			Pool.put("mapnodeelement", node.getId(), node);
			loaded_objects.add(node);
	    }

		nodelinks = nlh.value;
		count = nodelinks.length;
		System.out.println("...Done! " + count + " nodelink(s) fetched");
	    for (i = 0; i < count; i++)
		{
			nodelink = new MapNodeLinkElement(nodelinks[i]);
			Pool.put("mapnodelinkelement", nodelink.getId(), nodelink);
			loaded_objects.add(nodelink);
	    }

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " link(s) fetched");
	    for (i = 0; i < count; i++)
		{
			link = new MapPhysicalLinkElement(links[i]);
			Pool.put("maplinkelement", link.getId(), link);
			loaded_objects.add(link);
	    }

		paths = ph.value;
		count = paths.length;
		System.out.println("...Done! " + count + " path(s) fetched");
	    for (i = 0; i < count; i++)
		{
			path = new MapTransmissionPathElement(paths[i]);
			Pool.put("mappathelement", path.getId(), path);
			loaded_objects.add(path);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void SaveMap(String mc_id)
	{
		MapContext mc = (MapContext )Pool.get("mapcontext", mc_id);

		if(si == null)
			return;
		if(!si.isOpened())
			return;

		System.out.println("SaveMap:");

		int i;
		int ecode = 0;
		int count;
		ObjectResource os;

		Hashtable image_vec = new Hashtable();
		ArrayList equipment_vec;
		ArrayList kis_vec;
		ArrayList node_vec;
		ArrayList mark_vec;
		ArrayList vec;

		ImageResource_Transferable images[];
		MapContext_Transferable maps[];
		MapElement_Transferable equipments[];
		MapElement_Transferable kiss[];
		MapMarkElement_Transferable marks[];
		MapPhysicalNodeElement_Transferable nodes[];
		MapNodeLinkElement_Transferable nodelinks[];
		MapPhysicalLinkElement_Transferable links[];
		MapPathElement_Transferable paths[];

		ImageResource image;
		MapContext map;
		MapEquipmentNodeElement equipment;
		MapMarkElement mark;
		MapPhysicalNodeElement node;
		MapNodeLinkElement nodelink;
		MapPhysicalLinkElement link;
		MapTransmissionPathElement path;

		maps = new MapContext_Transferable[1];
		mc.setTransferableFromLocal();
		maps[0] = (MapContext_Transferable )mc.getTransferable();

		node_vec = new ArrayList();
		mark_vec = new ArrayList();
		kis_vec = new ArrayList();
		equipment_vec = new ArrayList();

		for(Iterator it = mc.getNodes().iterator(); it.hasNext();)
		{
			os = (ObjectResource )it.next();
			os.setTransferableFromLocal();
			if(os.getTyp().equals("mapnodeelement"))
				node_vec.add(os.getTransferable());
			if(os.getTyp().equals("mapequipmentelement"))
				equipment_vec.add(os.getTransferable());
			if(os.getTyp().equals("mapmarkelement"))
				mark_vec.add(os.getTransferable());
		}
		nodes = (MapPhysicalNodeElement_Transferable [])
			node_vec.toArray(new MapPhysicalNodeElement_Transferable[0]);
		kiss = (MapElement_Transferable [])
			kis_vec.toArray(new MapElement_Transferable[0]);
		equipments = (MapElement_Transferable [])
			equipment_vec.toArray(new MapElement_Transferable[0]);
		marks = (MapMarkElement_Transferable [])
			mark_vec.toArray(new MapMarkElement_Transferable[0]);

		vec = mc.getNodeLinks();
		count = vec.size();
		nodelinks = new MapNodeLinkElement_Transferable[count];
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource )vec.get(i);
			os.setTransferableFromLocal();
			nodelinks[i] = (MapNodeLinkElement_Transferable )os.getTransferable();
		}

		links = new MapPhysicalLinkElement_Transferable[mc.getPhysicalLinks().size()];
		i = 0;
		for(Iterator it = mc.getPhysicalLinks().iterator(); it.hasNext(); i++)
		{
			os = (ObjectResource )it.next();
			os.setTransferableFromLocal();
			links[i] = (MapPhysicalLinkElement_Transferable )os.getTransferable();
		}
	
		paths = new MapPathElement_Transferable[mc.getTransmissionPath().size()];
		i = 0;
		for(Iterator it = mc.getTransmissionPath().iterator(); it.hasNext(); i++)
		{
			os = (ObjectResource )it.next();
			os.setTransferableFromLocal();
			paths[i] = (MapPathElement_Transferable )os.getTransferable();
		}

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
			ecode = si.ci.server.SaveMaps(
					si.accessIdentity,
					images,
					maps,
					equipments,
					kiss,
					marks,
					nodes,
					nodelinks,
					links,
					paths);
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

	public void SaveMaps()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void ReloadAttributes(String mc_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

//		System.out.println("ReloadAttributes:");

		MapContext mc = (MapContext )Pool.get("mapcontext", mc_id);
		int ecode = 0;
		int count;
		int i;

		String[] maps = new String[1];
		maps[0] = mc.getId();

		ElementAttributeSeq_TransferableHolder ah = new ElementAttributeSeq_TransferableHolder();
		ElementAttribute_Transferable eas[];
		ElementAttribute ea;

		try
		{
			ecode = si.ci.server.ReloadAttributes(
					si.accessIdentity,
					maps,
					ah);
		}
		catch (Exception ex)
		{
			System.err.print("Error ReloadAttributes: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed ReloadAttributes! status = " + ecode);
			return;
		}

		eas = ah.value;
		count = eas.length;
//		System.out.println("...Done! " + count + " element attribute(s) fetched");
	    for (i = 0; i < count; i++)
		{
			ea = new ElementAttribute(eas[i]);
			Pool.put("attribute", ea.getId(), ea);
//			Pool.putName("attribute", ea.getId(), ea.getName());
	    }

	}

	public void RemoveMap(String mc_id)
	{
		MapContext mc = (MapContext )Pool.get("mapcontext", mc_id);

		if(si == null)
			return;
		if(!si.isOpened())
			return;

		System.out.println("RemoveMap:");

		int ecode = 0;

		String[] maps = new String[1];
		maps[0] = mc.getId();
		String[] leer = new String[0];

		try
		{
			ecode = si.ci.server.RemoveMaps(
					si.accessIdentity,
					maps,
					leer,
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
		MapContext mc = (MapContext )Pool.get("mapcontext", mc_id);

		if(si == null)
			return;
		if(!si.isOpened())
			return;

		System.out.println("RemoveFromMap:");
	
		int i;
		int ecode = 0;
		int count;
		ObjectResource os;

		Vector equipment_vec;
		Vector kis_vec;
		Vector node_vec;
		Vector mark_vec;
		Vector nodelink_vec;
		Vector link_vec;
		Vector path_vec;
		LinkedList vec;

		String maps[];
		String equipments[];
		String kiss[];
		String marks[];
		String nodes[];
		String nodelinks[];
		String links[];
		String paths[];

		MapContext map;
		MapEquipmentNodeElement equipment;
		MapMarkElement mark;
		MapPhysicalNodeElement node;
		MapNodeLinkElement nodelink;
		MapPhysicalLinkElement link;
		MapTransmissionPathElement path;

		maps = new String[0];

		node_vec = new Vector();
		mark_vec = new Vector();
		kis_vec = new Vector();
		equipment_vec = new Vector();
		nodelink_vec = new Vector();
		link_vec = new Vector();
		path_vec = new Vector();

		vec = mc.getRemovedElements();
		count = vec.size();
		for(i = 0; i < count; i++)
		{
			os = (ObjectResource )vec.get(i);
			if(os.getTyp().equals("mapnodeelement"))
				node_vec.add(os.getId());
			if(os.getTyp().equals("mapequipmentelement"))
				equipment_vec.add(os.getId());
			if(os.getTyp().equals("mapmarkelement"))
				mark_vec.add(os.getId());
			if(os.getTyp().equals("mapnodelinkelement"))
				nodelink_vec.add(os.getId());
			if(os.getTyp().equals("maplinkelement"))
				link_vec.add(os.getId());
			if(os.getTyp().equals("mappathelement"))
				path_vec.add(os.getId());
		}
		nodes = new String[node_vec.size()];
		node_vec.copyInto(nodes);
		kiss = new String[kis_vec.size()];
		kis_vec.copyInto(kiss);
		equipments = new String[equipment_vec.size()];
		equipment_vec.copyInto(equipments);
		marks = new String[mark_vec.size()];
		mark_vec.copyInto(marks);
		nodelinks = new String[nodelink_vec.size()];
		nodelink_vec.copyInto(nodelinks);
		links = new String[link_vec.size()];
		link_vec.copyInto(links);
		paths = new String[path_vec.size()];
		path_vec.copyInto(paths);

		try
		{
			ecode = si.ci.server.RemoveMaps(
					si.accessIdentity,
					maps,
					equipments,
					kiss,
					marks,
					nodes,
					nodelinks,
					links,
					paths);
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