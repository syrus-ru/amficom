package com.syrus.AMFICOM.Client.Resource;

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

public class RISDDirectoryDataSource
		extends RISDObjectDataSource
		implements DataSourceInterface
{
	protected RISDDirectoryDataSource()
	{
	}

	public RISDDirectoryDataSource(SessionInterface si)
	{
		super(si);
	}

	public void LoadNetDirectory()
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

		PortTypeSeq_TransferableHolder pth = new PortTypeSeq_TransferableHolder();
		PortType_Transferable porttypes[];
		PortType porttype;
		EquipmentTypeSeq_TransferableHolder eth = new EquipmentTypeSeq_TransferableHolder();
		EquipmentType_Transferable equipmenttypes[];
		EquipmentType equipmenttype;
		LinkTypeSeq_TransferableHolder lth = new LinkTypeSeq_TransferableHolder();
		LinkType_Transferable linktypes[];
		LinkType linktype;
		TestPortTypeSeq_TransferableHolder tpth = new TestPortTypeSeq_TransferableHolder();
		TestPortType_Transferable testporttypes[];
		TestPortType testporttype;
		CharacteristicTypeSeq_TransferableHolder chth = new CharacteristicTypeSeq_TransferableHolder();
		CharacteristicType_Transferable characteristictypes[];
		CharacteristicType characteristictype;

		CablePortTypeSeq_TransferableHolder cpth = new CablePortTypeSeq_TransferableHolder();
		CablePortType_Transferable cporttypes[];
		CablePortType cporttype;
		CableLinkTypeSeq_TransferableHolder clth = new CableLinkTypeSeq_TransferableHolder();
		CableLinkType_Transferable clinktypes[];
		CableLinkType clinktype;

		PortSeq_TransferableHolder ph = new PortSeq_TransferableHolder();
		Port_Transferable ports[];
		Port port;
		EquipmentSeq_TransferableHolder eh = new EquipmentSeq_TransferableHolder();
		Equipment_Transferable equipments[];
		Equipment equipment;
		LinkSeq_TransferableHolder lh = new LinkSeq_TransferableHolder();
		Link_Transferable links[];
		Link link;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
//			ecode = si.ci.server.LoadNetDirectory(si.accessIdentity, pth, eth, lth, tpth, chth, ph, eh, lh);
			ecode = si.ci.server.LoadNetDirectory(si.accessIdentity, pth, eth, lth, tpth, chth, cpth, clth);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting network directory: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadNetDirectory! status = " + ecode);
			return;
		}

		porttypes = pth.value;
		count = porttypes.length;
		System.out.println("...Done! " + count + " porttype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			porttype = new PortType(porttypes[i]);
			Pool.put("porttype", porttype.getId(), porttype);
//			Pool.putName("porttype", porttype.getId(), porttype.getName());
			loaded_objects.add(porttype);
	    }

		equipmenttypes = eth.value;
		count = equipmenttypes.length;
		System.out.println("...Done! " + count + " equipmenttype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			equipmenttype = new EquipmentType(equipmenttypes[i]);
			Pool.put("equipmenttype", equipmenttype.getId(), equipmenttype);
//			Pool.putName("equipmenttype", equipmenttype.getId(), equipmenttype.getName());
			loaded_objects.add(equipmenttype);
	    }

		linktypes = lth.value;
		count = linktypes.length;
		System.out.println("...Done! " + count + " linktype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			linktype = new LinkType(linktypes[i]);
			Pool.put("linktype", linktype.getId(), linktype);
//			Pool.putName("linktype", linktype.getId(), linktype.getName());
			loaded_objects.add(linktype);
	    }

		testporttypes = tpth.value;
		count = testporttypes.length;
		System.out.println("...Done! " + count + " testporttype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			testporttype = new TestPortType(testporttypes[i]);
			Pool.put("testporttype", testporttype.getId(), testporttype);
//			Pool.putName("testporttype", testporttype.getId(), testporttype.getName());
			loaded_objects.add(testporttype);
	    }

		characteristictypes = chth.value;
		count = characteristictypes.length;
		System.out.println("...Done! " + count + " characteristictype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			characteristictype = new CharacteristicType(characteristictypes[i]);
			Pool.put("characteristictype", characteristictype.getId(), characteristictype);
//			Pool.putName("porttype", porttype.getId(), porttype.getName());
			loaded_objects.add(characteristictype);
	    }

		cporttypes = cpth.value;
		count = cporttypes.length;
		System.out.println("...Done! " + count + " cableporttype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			cporttype = new CablePortType(cporttypes[i]);
			Pool.put("cableporttype", cporttype.getId(), cporttype);
//			Pool.putName("porttype", porttype.getId(), porttype.getName());
			loaded_objects.add(cporttype);
	    }

		clinktypes = clth.value;
		count = clinktypes.length;
		System.out.println("...Done! " + count + " cablelinktype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			clinktype = new CableLinkType(clinktypes[i]);
			Pool.put("cablelinktype", clinktype.getId(), clinktype);
//			Pool.putName("linktype", linktype.getId(), linktype.getName());
			loaded_objects.add(clinktype);
	    }

/*
		ports = ph.value;
		count = ports.length;
		System.out.println("...Done! " + count + " port(s) fetched");
	    for (i = 0; i < count; i++)
		{
			port = new Port(ports[i]);
			Pool.put("port", port.getId(), port);
//			Pool.putName("port", port.getId(), port.getName());
			loaded_objects.add(port);
	    }

		equipments = eh.value;
		count = equipments.length;
		System.out.println("...Done! " + count + " equipment(s) fetched");
	    for (i = 0; i < count; i++)
		{
			equipment = new Equipment(equipments[i]);
			Pool.put("equipment", equipment.getId(), equipment);
//			Pool.putName("equipment", equipment.getId(), equipment.getName());
			loaded_objects.add(equipment);
	    }

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " link(s) fetched");
	    for (i = 0; i < count; i++)
		{
			link = new Link(links[i]);
			Pool.put("link", link.getId(), link);
//			Pool.putName("link", link.getId(), link.getName());
			loaded_objects.add(link);
	    }
*/
		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void LoadISMDirectory()
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

		EquipmentTypeSeq_TransferableHolder kth = new EquipmentTypeSeq_TransferableHolder();
		EquipmentType_Transferable kistypes[];
		KISType kistype;
		AccessPortTypeSeq_TransferableHolder apth = new AccessPortTypeSeq_TransferableHolder();
		AccessPortType_Transferable accessporttypes[];
		AccessPortType accessporttype;
		TransmissionPathTypeSeq_TransferableHolder pth = new TransmissionPathTypeSeq_TransferableHolder();
		TransmissionPathType_Transferable pathtypes[];
		TransmissionPathType pathtype;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
//			ecode = si.ci.server.LoadISMDirectory(si.accessIdentity, kth, apth, ph, aph);
			ecode = si.ci.server.LoadISMDirectory(si.accessIdentity, kth, apth, pth);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting ISM directory: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadISMDirectory! status = " + ecode);
			return;
		}

		kistypes = kth.value;
		count = kistypes.length;
		System.out.println("...Done! " + count + " kistype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			kistype = new KISType(kistypes[i]);
			Pool.put("kistype", kistype.getId(), kistype);
//			Pool.putName("kistype", kistype.getId(), kistype.getName());
			loaded_objects.add(kistype);
	    }

		accessporttypes = apth.value;
		count = accessporttypes.length;
		System.out.println("...Done! " + count + " accessporttype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			accessporttype = new AccessPortType(accessporttypes[i]);
			Pool.put("accessporttype", accessporttype.getId(), accessporttype);
//			Pool.putName("accessporttype", accessporttype.getId(), accessporttype.getName());
			loaded_objects.add(accessporttype);
	    }

		pathtypes = pth.value;
		count = pathtypes.length;
		System.out.println("...Done! " + count + " port(s) fetched");
	    for (i = 0; i < count; i++)
		{
			pathtype = new TransmissionPathType(pathtypes[i]);
			Pool.put("pathtype", pathtype.getId(), pathtype);
//			Pool.putName("port", port.getId(), port.getName());
			loaded_objects.add(pathtype);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void LoadNetDirectory(
			Vector pt_ids, 
			Vector eqt_ids,
			Vector lt_ids,
			Vector cht_ids,
			Vector cpt_ids,
			Vector clt_ids)
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

		PortTypeSeq_TransferableHolder pth = new PortTypeSeq_TransferableHolder();
		PortType_Transferable porttypes[];
		PortType porttype;
		EquipmentTypeSeq_TransferableHolder eth = new EquipmentTypeSeq_TransferableHolder();
		EquipmentType_Transferable equipmenttypes[];
		EquipmentType equipmenttype;
		LinkTypeSeq_TransferableHolder lth = new LinkTypeSeq_TransferableHolder();
		LinkType_Transferable linktypes[];
		LinkType linktype;
		TestPortTypeSeq_TransferableHolder tpth = new TestPortTypeSeq_TransferableHolder();
		TestPortType_Transferable testporttypes[];
		TestPortType testporttype;
		CharacteristicTypeSeq_TransferableHolder chth = new CharacteristicTypeSeq_TransferableHolder();
		CharacteristicType_Transferable characteristictypes[];
		CharacteristicType characteristictype;

		CablePortTypeSeq_TransferableHolder cpth = new CablePortTypeSeq_TransferableHolder();
		CablePortType_Transferable cporttypes[];
		CablePortType cporttype;
		CableLinkTypeSeq_TransferableHolder clth = new CableLinkTypeSeq_TransferableHolder();
		CableLinkType_Transferable clinktypes[];
		CableLinkType clinktype;

		PortSeq_TransferableHolder ph = new PortSeq_TransferableHolder();
		Port_Transferable ports[];
		Port port;
		EquipmentSeq_TransferableHolder eh = new EquipmentSeq_TransferableHolder();
		Equipment_Transferable equipments[];
		Equipment equipment;
		LinkSeq_TransferableHolder lh = new LinkSeq_TransferableHolder();
		Link_Transferable links[];
		Link link;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		String[] ptid_s = new String[pt_ids.size()];
		pt_ids.copyInto(ptid_s);
		String[] eqtid_s = new String[eqt_ids.size()];
		eqt_ids.copyInto(eqtid_s);
		String[] ltid_s = new String[lt_ids.size()];
		lt_ids.copyInto(ltid_s);
		String[] chtid_s = new String[cht_ids.size()];
		cht_ids.copyInto(chtid_s);
		String[] cptid_s = new String[cpt_ids.size()];
		cpt_ids.copyInto(cptid_s);
		String[] cltid_s = new String[clt_ids.size()];
		clt_ids.copyInto(cltid_s);
		try
		{
			ecode = si.ci.server.LoadStatedNetDirectory(si.accessIdentity, ptid_s, eqtid_s, ltid_s, chtid_s, cptid_s, cltid_s, pth, eth, lth, tpth, chth, cpth, clth);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting network directory: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadNetDirectory! status = " + ecode);
			return;
		}

		porttypes = pth.value;
		count = porttypes.length;
		System.out.println("...Done! " + count + " porttype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			porttype = new PortType(porttypes[i]);
			Pool.put("porttype", porttype.getId(), porttype);
			loaded_objects.add(porttype);
	    }

		equipmenttypes = eth.value;
		count = equipmenttypes.length;
		System.out.println("...Done! " + count + " equipmenttype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			equipmenttype = new EquipmentType(equipmenttypes[i]);
			Pool.put("equipmenttype", equipmenttype.getId(), equipmenttype);
			loaded_objects.add(equipmenttype);
	    }

		linktypes = lth.value;
		count = linktypes.length;
		System.out.println("...Done! " + count + " linktype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			linktype = new LinkType(linktypes[i]);
			Pool.put("linktype", linktype.getId(), linktype);
			loaded_objects.add(linktype);
	    }

		testporttypes = tpth.value;
		count = testporttypes.length;
		System.out.println("...Done! " + count + " testporttype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			testporttype = new TestPortType(testporttypes[i]);
			Pool.put("testporttype", testporttype.getId(), testporttype);
			loaded_objects.add(testporttype);
	    }

		characteristictypes = chth.value;
		count = characteristictypes.length;
		System.out.println("...Done! " + count + " characteristictype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			characteristictype = new CharacteristicType(characteristictypes[i]);
			Pool.put("characteristictype", characteristictype.getId(), characteristictype);
			loaded_objects.add(characteristictype);
	    }

		cporttypes = cpth.value;
		count = cporttypes.length;
		System.out.println("...Done! " + count + " cableporttype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			cporttype = new CablePortType(cporttypes[i]);
			Pool.put("cableporttype", cporttype.getId(), cporttype);
			loaded_objects.add(cporttype);
	    }

		clinktypes = clth.value;
		count = clinktypes.length;
		System.out.println("...Done! " + count + " cablelinktype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			clinktype = new CableLinkType(clinktypes[i]);
			Pool.put("cablelinktype", clinktype.getId(), clinktype);
			loaded_objects.add(clinktype);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}
	
	public void LoadISMDirectory(
		Vector kt_ids,
		Vector apt_ids,
		Vector pt_ids)
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

		EquipmentTypeSeq_TransferableHolder kth = new EquipmentTypeSeq_TransferableHolder();
		EquipmentType_Transferable kistypes[];
		KISType kistype;
		AccessPortTypeSeq_TransferableHolder apth = new AccessPortTypeSeq_TransferableHolder();
		AccessPortType_Transferable accessporttypes[];
		AccessPortType accessporttype;
		TransmissionPathTypeSeq_TransferableHolder pth = new TransmissionPathTypeSeq_TransferableHolder();
		TransmissionPathType_Transferable pathtypes[];
		TransmissionPathType pathtype;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		String[] id_s = new String[apt_ids.size()];
		apt_ids.copyInto(id_s);
		String[] kid_s = new String[kt_ids.size()];
		kt_ids.copyInto(kid_s);
		String[] pid_s = new String[pt_ids.size()];
		pt_ids.copyInto(pid_s);
		try
		{
			ecode = si.ci.server.LoadStatedISMDirectory(si.accessIdentity, kid_s, id_s, pid_s, kth, apth, pth);
//			ecode = si.ci.server.LoadISMDirectory(si.accessIdentity, apth);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting ISM directory: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadISMDirectory! status = " + ecode);
			return;
		}

		kistypes = kth.value;
		count = kistypes.length;
		System.out.println("...Done! " + count + " kistype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			kistype = new KISType(kistypes[i]);
			Pool.put("kistype", kistype.getId(), kistype);
//			Pool.putName("kistype", kistype.getId(), kistype.getName());
			loaded_objects.add(kistype);
	    }

		accessporttypes = apth.value;
		count = accessporttypes.length;
		System.out.println("...Done! " + count + " accessporttype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			accessporttype = new AccessPortType(accessporttypes[i]);
			Pool.put("accessporttype", accessporttype.getId(), accessporttype);
//			Pool.putName("accessporttype", accessporttype.getId(), accessporttype.getName());
			loaded_objects.add(accessporttype);
	    }

		pathtypes = pth.value;
		count = pathtypes.length;
		System.out.println("...Done! " + count + " pathtype(s) fetched");
	    for (i = 0; i < count; i++)
		{
			pathtype = new TransmissionPathType(pathtypes[i]);
			Pool.put("pathtype", pathtype.getId(), pathtype);
//			Pool.putName("port", port.getId(), port.getName());
			loaded_objects.add(pathtype);
	    }

		// update loaded objects
		count = loaded_objects.size();
	    for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void SaveEquipmentTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		EquipmentType_Transferable ets[] = new EquipmentType_Transferable[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			EquipmentType eqt = (EquipmentType )Pool.get(EquipmentType.typ, ids[i]);
			eqt.setTransferableFromLocal();
			ets[i] = (EquipmentType_Transferable )eqt.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveNetDirectory(
					si.accessIdentity, 
					new PortType_Transferable[0],
					ets,
					new LinkType_Transferable[0],
					new TestPortType_Transferable[0],
					new CharacteristicType_Transferable[0],
					new CablePortType_Transferable[0],
					new CableLinkType_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving EquipmentTypes: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveEquipmentTypes! status = " + ecode);
			return;
		}
	}

	public void SavePortTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		PortType_Transferable pts[] = new PortType_Transferable[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			PortType pt = (PortType )Pool.get(PortType.typ, ids[i]);
			pt.setTransferableFromLocal();
			pts[i] = (PortType_Transferable )pt.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveNetDirectory(
					si.accessIdentity, 
					pts,
					new EquipmentType_Transferable[0],
					new LinkType_Transferable[0],
					new TestPortType_Transferable[0],
					new CharacteristicType_Transferable[0],
					new CablePortType_Transferable[0],
					new CableLinkType_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving PortTypes: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SavePortTypes! status = " + ecode);
			return;
		}
	}

	public void SaveCablePortTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		CablePortType_Transferable pts[] = new CablePortType_Transferable[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			CablePortType pt = (CablePortType )Pool.get(CablePortType.typ, ids[i]);
			pt.setTransferableFromLocal();
			pts[i] = (CablePortType_Transferable )pt.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveNetDirectory(
					si.accessIdentity, 
					new PortType_Transferable[0],
					new EquipmentType_Transferable[0],
					new LinkType_Transferable[0],
					new TestPortType_Transferable[0],
					new CharacteristicType_Transferable[0],
					pts,
					new CableLinkType_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving CablePortTypes: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveCablePortTypes! status = " + ecode);
			return;
		}
	}

	public void SaveLinkTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		LinkType_Transferable lts[] = new LinkType_Transferable[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			LinkType lt = (LinkType )Pool.get(LinkType.typ, ids[i]);
			lt.setTransferableFromLocal();
			lts[i] = (LinkType_Transferable )lt.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveNetDirectory(
					si.accessIdentity, 
					new PortType_Transferable[0],
					new EquipmentType_Transferable[0],
					lts,
					new TestPortType_Transferable[0],
					new CharacteristicType_Transferable[0],
					new CablePortType_Transferable[0],
					new CableLinkType_Transferable[0]);
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

	public void SaveCableLinkTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		CableLinkType_Transferable lts[] = new CableLinkType_Transferable[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			CableLinkType lt = (CableLinkType )Pool.get(CableLinkType.typ, ids[i]);
			lt.setTransferableFromLocal();
			lts[i] = (CableLinkType_Transferable )lt.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveNetDirectory(
					si.accessIdentity, 
					new PortType_Transferable[0],
					new EquipmentType_Transferable[0],
					new LinkType_Transferable[0],
					new TestPortType_Transferable[0],
					new CharacteristicType_Transferable[0],
					new CablePortType_Transferable[0],
					lts);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving CableLinkTypes: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveCableLinkTypes! status = " + ecode);
			return;
		}
	}


}