package com.syrus.AMFICOM.Client.Resource;

import com.syrus.AMFICOM.CORBA.Constants;
import com.syrus.AMFICOM.CORBA.General.CharacteristicTypeSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.General.CharacteristicType_Transferable;
import com.syrus.AMFICOM.CORBA.General.ElementAttributeTypeSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.General.ElementAttributeType_Transferable;
import com.syrus.AMFICOM.CORBA.ISMDirectory.KISTypeSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.ISMDirectory.KISType_Transferable;
import com.syrus.AMFICOM.CORBA.ISMDirectory.MeasurementPortTypeSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.ISMDirectory.MeasurementPortType_Transferable;
import com.syrus.AMFICOM.CORBA.ISMDirectory.TransmissionPathTypeSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.ISMDirectory.TransmissionPathType_Transferable;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.CableLinkTypeSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.CableLinkType_Transferable;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.CablePortTypeSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.CablePortType_Transferable;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.EquipmentTypeSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.EquipmentType_Transferable;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.LinkTypeSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.LinkType_Transferable;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.PortTypeSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.PortType_Transferable;
import com.syrus.AMFICOM.CORBA.Resource.ImageResourceSeq_TransferableHolder;
import com.syrus.AMFICOM.CORBA.Resource.ImageResource_Transferable;
import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.General.CharacteristicType;
import com.syrus.AMFICOM.Client.Resource.General.ElementAttributeType;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.KISType;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.MeasurementPortType;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.TransmissionPathType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CableLinkType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CablePortType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.PortType;

import java.util.Vector;

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

	public void LoadCharacteristicTypes(
			String[] cht_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		CharacteristicTypeSeq_TransferableHolder chth = new CharacteristicTypeSeq_TransferableHolder();
		CharacteristicType_Transferable characteristictypes[];
		CharacteristicType characteristictype;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.LoadStatedCharacteristicTypes(si.accessIdentity, cht_ids, chth);
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

		characteristictypes = chth.value;
		count = characteristictypes.length;
		System.out.println("...Done! " + count + " characteristictype(s) fetched");
			for (i = 0; i < count; i++)
		{
			characteristictype = new CharacteristicType(characteristictypes[i]);
			Pool.put(CharacteristicType.typ, characteristictype.getId(), characteristictype);
			loaded_objects.add(characteristictype);
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
			String[] pt_ids,
			String[] eqt_ids,
			String[] lt_ids,
			String[] cpt_ids,
			String[] clt_ids)
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

		CablePortTypeSeq_TransferableHolder cpth = new CablePortTypeSeq_TransferableHolder();
		CablePortType_Transferable cporttypes[];
		CablePortType cporttype;
		CableLinkTypeSeq_TransferableHolder clth = new CableLinkTypeSeq_TransferableHolder();
		CableLinkType_Transferable clinktypes[];
		CableLinkType clinktype;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.LoadStatedNetDirectory(si.accessIdentity, pt_ids, eqt_ids, lt_ids, cpt_ids, clt_ids, pth, eth, lth, cpth, clth);
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
			Pool.put(PortType.typ, porttype.getId(), porttype);
			loaded_objects.add(porttype);
			}

		equipmenttypes = eth.value;
		count = equipmenttypes.length;
		System.out.println("...Done! " + count + " equipmenttype(s) fetched");
			for (i = 0; i < count; i++)
		{
			equipmenttype = new EquipmentType(equipmenttypes[i]);
			Pool.put(EquipmentType.typ, equipmenttype.getId(), equipmenttype);
			loaded_objects.add(equipmenttype);
			}

		linktypes = lth.value;
		count = linktypes.length;
		System.out.println("...Done! " + count + " linktype(s) fetched");
			for (i = 0; i < count; i++)
		{
			linktype = new LinkType(linktypes[i]);
			Pool.put(LinkType.typ, linktype.getId(), linktype);
			loaded_objects.add(linktype);
			}

		cporttypes = cpth.value;
		count = cporttypes.length;
		System.out.println("...Done! " + count + " cableporttype(s) fetched");
			for (i = 0; i < count; i++)
		{
			cporttype = new CablePortType(cporttypes[i]);
			Pool.put(CablePortType.typ, cporttype.getId(), cporttype);
			loaded_objects.add(cporttype);
			}

		clinktypes = clth.value;
		count = clinktypes.length;
		System.out.println("...Done! " + count + " cablelinktype(s) fetched");
			for (i = 0; i < count; i++)
		{
			clinktype = new CableLinkType(clinktypes[i]);
			Pool.put(CableLinkType.typ, clinktype.getId(), clinktype);
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
		String[] kt_ids,
		String[] mpt_ids,
		String[] pt_ids)
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

		KISTypeSeq_TransferableHolder kth = new KISTypeSeq_TransferableHolder();
		KISType_Transferable kistypes[];
		KISType kistype;
		MeasurementPortTypeSeq_TransferableHolder mpth = new MeasurementPortTypeSeq_TransferableHolder();
		MeasurementPortType_Transferable measurementporttypes[];
		MeasurementPortType measurementporttype;
		TransmissionPathTypeSeq_TransferableHolder pth = new TransmissionPathTypeSeq_TransferableHolder();
		TransmissionPathType_Transferable pathtypes[];
		TransmissionPathType pathtype;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.LoadStatedISMDirectory(si.accessIdentity, kt_ids, mpt_ids, pt_ids, kth, mpth, pth);
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
			Pool.put(KISType.typ, kistype.getId(), kistype);
			loaded_objects.add(kistype);
			}

		measurementporttypes = mpth.value;
		count = measurementporttypes.length;
		System.out.println("...Done! " + count + " accessporttype(s) fetched");
			for (i = 0; i < count; i++)
		{
			measurementporttype = new MeasurementPortType(measurementporttypes[i]);
			Pool.put(MeasurementPortType.typ, measurementporttype.getId(), measurementporttype);
			loaded_objects.add(measurementporttype);
			}

		pathtypes = pth.value;
		count = pathtypes.length;
		System.out.println("...Done! " + count + " pathtype(s) fetched");
			for (i = 0; i < count; i++)
		{
			pathtype = new TransmissionPathType(pathtypes[i]);
			Pool.put(TransmissionPathType.typ, pathtype.getId(), pathtype);
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

	public void SaveCharacteristicTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		CharacteristicType_Transferable cts[] = new CharacteristicType_Transferable[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			CharacteristicType ct = (CharacteristicType )Pool.get(CharacteristicType.typ, ids[i]);
			ct.setTransferableFromLocal();
			cts[i] = (CharacteristicType_Transferable )ct.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveCharacteristicTypes(
					si.accessIdentity,
					cts);
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

	public void RemoveCharacteristicTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		try
		{
//			ecode = si.ci.server.RemoveCharacteristicTypes(
//					si.accessIdentity,
//					ids);
				ecode = 0;
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

	private static final String[] empty = new String[0];

	public void RemoveEquipmentTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;

		try
		{
			ecode = si.ci.server.RemoveNetDirectory(
					si.accessIdentity,
					empty,
					ids,
					empty,
					empty,
					empty);
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

	public void RemovePortTypes(String[] ids)
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
			ecode = si.ci.server.RemoveNetDirectory(
					si.accessIdentity,
					ids,
					empty,
					empty,
					empty,
					empty);
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

	public void RemoveCablePortTypes(String[] ids)
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
			ecode = si.ci.server.RemoveNetDirectory(
					si.accessIdentity,
					empty,
					empty,
					empty,
					ids,
					empty);
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

	public void RemoveLinkTypes(String[] ids)
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
			ecode = si.ci.server.RemoveNetDirectory(
					si.accessIdentity,
					empty,
					empty,
					ids,
					empty,
					empty);
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

	public void RemoveCableLinkTypes(String[] ids)
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
			ecode = si.ci.server.RemoveNetDirectory(
					si.accessIdentity,
					empty,
					empty,
					empty,
					empty,
					ids);
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

	public void SaveKISTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		KISType_Transferable kts[] = new KISType_Transferable[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			KISType kqt = (KISType )Pool.get(KISType.typ, ids[i]);
			kqt.setTransferableFromLocal();
			kts[i] = (KISType_Transferable )kqt.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveISMDirectory(
					si.accessIdentity,
					kts,
					new MeasurementPortType_Transferable[0],
					new TransmissionPathType_Transferable[0]);
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

	public void SaveMeasurementPortTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		MeasurementPortType_Transferable mpts[] = new MeasurementPortType_Transferable[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			MeasurementPortType mpqt = (MeasurementPortType )Pool.get(MeasurementPortType.typ, ids[i]);
			mpqt.setTransferableFromLocal();
			mpts[i] = (MeasurementPortType_Transferable )mpqt.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveISMDirectory(
					si.accessIdentity,
					new KISType_Transferable[0],
					mpts,
					new TransmissionPathType_Transferable[0]);
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

	public void SaveTransmissionPathTypes(String[] ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode;
		TransmissionPathType_Transferable tpts[] = new TransmissionPathType_Transferable[ids.length];
		for(int i = 0; i < ids.length; i++)
		{
			TransmissionPathType tpt = (TransmissionPathType )Pool.get(TransmissionPathType.typ, ids[i]);
			tpt.setTransferableFromLocal();
			tpts[i] = (TransmissionPathType_Transferable )tpt.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveISMDirectory(
					si.accessIdentity,
					new KISType_Transferable[0],
					new MeasurementPortType_Transferable[0],
					tpts);
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

}
