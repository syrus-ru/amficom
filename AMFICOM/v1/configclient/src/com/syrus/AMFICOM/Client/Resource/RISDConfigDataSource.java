package com.syrus.AMFICOM.Client.Resource;

import java.util.*;

import org.omg.CORBA.*;

import com.syrus.AMFICOM.CORBA.*;
import com.syrus.AMFICOM.CORBA.Admin.*;
import com.syrus.AMFICOM.CORBA.Resource.*;
import com.syrus.AMFICOM.CORBA.NetworkDirectory.*;
import com.syrus.AMFICOM.CORBA.Network.*;
import com.syrus.AMFICOM.CORBA.ISM.*;
import com.syrus.AMFICOM.CORBA.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.ISM.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
//import com.syrus.AMFICOM.Client.Resource.Object.*;
import com.syrus.AMFICOM.Client.General.*;

public class RISDConfigDataSource
		extends RISDDirectoryDataSource
		implements DataSourceInterface
{
	protected RISDConfigDataSource()
	{
		super();
	}

	public RISDConfigDataSource(SessionInterface si)
	{
		super(si);
	}

	public void LoadKISDescriptors()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		LoadISM();
	}

	public void SaveEquipment(String equipment_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		Equipment equipment = (Equipment )Pool.get(Equipment.typ, equipment_id);
		equipment.setTransferableFromLocal();

		Equipment_Transferable equipment_t[] = new Equipment_Transferable[1];
		equipment_t[0] = (Equipment_Transferable )equipment.getTransferable();

		Port_Transferable port_t[] = new Port_Transferable[equipment.ports.size()];
		int counter = 0;
		for(Iterator it = equipment.ports.iterator(); it.hasNext();)
		{
			Port port = (Port)it.next();
			port.setTransferableFromLocal();
			port_t[counter++] = (Port_Transferable )port.getTransferable();
		}

		counter = 0;
		CablePort_Transferable cport_t[] = new CablePort_Transferable[equipment.cports.size()];
		for(Iterator it = equipment.cports.iterator(); it.hasNext();)
		{
			CablePort cport = (CablePort)it.next();
			cport.setTransferableFromLocal();
			cport_t[counter++] = (CablePort_Transferable )cport.getTransferable();
		}

		counter = 0;
		TestPort_Transferable testport_t[] = new TestPort_Transferable[equipment.test_ports.size()];
		for(Iterator it = equipment.test_ports.iterator(); it.hasNext();)
		{
			TestPort testport = (TestPort)it.next();
			testport.setTransferableFromLocal();
			testport_t[counter++] = (TestPort_Transferable )testport.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveNet(
					si.accessIdentity,
					port_t,
					cport_t,
					equipment_t,
					new Link_Transferable[0],
					new CableLink_Transferable[0],
					testport_t);
//					new TestPort_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving Equipment: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveEquipment! status = " + ecode);
			return;
		}
	}

	public void SaveKIS(String kis_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		KIS kis = (KIS )Pool.get(KIS.typ, kis_id);
		kis.setTransferableFromLocal();

		Equipment_Transferable kis_t[] = new Equipment_Transferable[1];
		kis_t[0] = (Equipment_Transferable )kis.getTransferable();

		int counter = 0;
		Port_Transferable port_t[] = new Port_Transferable[kis.ports.size()];
		for(Iterator it = kis.ports.iterator(); it.hasNext();)
		{
			Port port = (Port)it.next();
			port.setTransferableFromLocal();
			port_t[counter++] = (Port_Transferable )port.getTransferable();
		}

		counter = 0;
		CablePort_Transferable cport_t[] = new CablePort_Transferable[kis.cports.size()];
		for(Iterator it = kis.cports.iterator(); it.hasNext();)
		{
			CablePort cport = (CablePort)it.next();
			cport.setTransferableFromLocal();
			cport_t[counter++] = (CablePort_Transferable )cport.getTransferable();
		}

		counter = 0;
		AccessPort_Transferable accessport_t[] = new AccessPort_Transferable[kis.access_ports.size()];
		for(Iterator it = kis.access_ports.iterator(); it.hasNext();)
		{
			AccessPort accessport = (AccessPort)it.next();
			accessport.setTransferableFromLocal();
			accessport_t[counter++] = (AccessPort_Transferable )accessport.getTransferable();
		}

		try
		{
			ecode = si.ci.server.SaveISM(
					si.accessIdentity,
					port_t,
					cport_t,
					kis_t,
					new Link_Transferable[0],
					new CableLink_Transferable[0],
					new MonitoredElement_Transferable[0],
					new TransmissionPath_Transferable[0],
					accessport_t);
//					new AccessPort_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving KIS: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveKIS! status = " + ecode);
			return;
		}
	}

	public void SaveLink(String link_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		Link link = (Link )Pool.get(Link.typ, link_id);
		link.setTransferableFromLocal();

		Link_Transferable link_t[] = new Link_Transferable[1];
		link_t[0] = (Link_Transferable )link.getTransferable();

		try
		{
			ecode = si.ci.server.SaveNet(
					si.accessIdentity,
					new Port_Transferable[0],
					new CablePort_Transferable[0],
					new Equipment_Transferable[0],
					link_t,
					new CableLink_Transferable[0],
					new TestPort_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving Link: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveLink! status = " + ecode);
			return;
		}
	}

	public void SaveCableLink(String clink_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		CableLink link = (CableLink )Pool.get(CableLink.typ, clink_id);
		link.setTransferableFromLocal();

		CableLink_Transferable link_t[] = new CableLink_Transferable[1];
		link_t[0] = (CableLink_Transferable )link.getTransferable();

		try
		{
			ecode = si.ci.server.SaveNet(
					si.accessIdentity,
					new Port_Transferable[0],
					new CablePort_Transferable[0],
					new Equipment_Transferable[0],
					new Link_Transferable[0],
					link_t,
					new TestPort_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving cable Link: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveCableLink! status = " + ecode);
			return;
		}
	}

	public void SavePath(String path_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		TransmissionPath path = (TransmissionPath )Pool.get(TransmissionPath.typ, path_id);
		path.setTransferableFromLocal();

		TransmissionPath_Transferable path_t[] = new TransmissionPath_Transferable[1];
		path_t[0] = (TransmissionPath_Transferable )path.getTransferable();

		try
		{
			ecode = si.ci.server.SaveISM(
					si.accessIdentity,
					new Port_Transferable[0],
					new CablePort_Transferable[0],
					new Equipment_Transferable[0],
					new Link_Transferable[0],
					new CableLink_Transferable[0],
					new MonitoredElement_Transferable[0],
					path_t,
					new AccessPort_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving Path: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SavePath! status = " + ecode);
			return;
		}
	}

	public void SavePort(String port_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		Port port = (Port )Pool.get(Port.typ, port_id);
		port.setTransferableFromLocal();

		Port_Transferable port_t[] = new Port_Transferable[1];
		port_t[0] = (Port_Transferable )port.getTransferable();

		try
		{
			ecode = si.ci.server.SaveNet(
					si.accessIdentity,
					port_t,
					new CablePort_Transferable[0],
					new Equipment_Transferable[0],
					new Link_Transferable[0],
					new CableLink_Transferable[0],
					new TestPort_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving Port: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SavePort! status = " + ecode);
			return;
		}
	}

	public void SaveCablePort(String cport_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		CablePort port = (CablePort )Pool.get(CablePort.typ, cport_id);
		port.setTransferableFromLocal();

		CablePort_Transferable port_t[] = new CablePort_Transferable[1];
		port_t[0] = (CablePort_Transferable )port.getTransferable();

		try
		{
			ecode = si.ci.server.SaveNet(
					si.accessIdentity,
					new Port_Transferable[0],
					port_t,
					new Equipment_Transferable[0],
					new Link_Transferable[0],
					new CableLink_Transferable[0],
					new TestPort_Transferable[0]);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving CablePort: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveCablePort! status = " + ecode);
			return;
		}
	}

	public void SaveAccessPort(String port_id)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int ecode = 0;
		AccessPort port = (AccessPort )Pool.get(AccessPort.typ, port_id);
		port.setTransferableFromLocal();

		AccessPort_Transferable port_t[] = new AccessPort_Transferable[1];
		port_t[0] = (AccessPort_Transferable )port.getTransferable();

		try
		{
			ecode = si.ci.server.SaveISM(
					si.accessIdentity,
					new Port_Transferable[0],
					new CablePort_Transferable[0],
					new Equipment_Transferable[0],
					new Link_Transferable[0],
					new CableLink_Transferable[0],
					new MonitoredElement_Transferable[0],
					new TransmissionPath_Transferable[0],
					port_t);
		}
		catch (Exception ex)
		{
			System.err.print("Error saving AccessPort: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed SaveAccessPort! status = " + ecode);
			return;
		}
	}

	public void RemoveEquipments(String[] equipment_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		String[] leer = new String[0];

		int ecode = 0;
		try
		{
			ecode = si.ci.server.RemoveNet(
					si.accessIdentity,
					leer,//new Port_Transferable[0],
					leer,//new CablePort_Transferable[0],
					equipment_ids,//new Equipment_Transferable[0],
					leer,//new Link_Transferable[0],
					leer//new CableLink_Transferable[0]
			);
		}
		catch (Exception ex)
		{
			System.err.print("Error removingEquipments: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveEquipments! status = " + ecode);
			return;
		}

		for(int i = 0; i < equipment_ids.length; i++)
		{
			Pool.remove(Equipment.typ, equipment_ids[i]);
			Pool.remove("kisequipment", equipment_ids[i]);
		}
	}

	public void RemovePorts(String[] port_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		String[] leer = new String[0];

		int ecode = 0;
		try
		{
			ecode = si.ci.server.RemoveNet(
					si.accessIdentity,
					port_ids,//new Port_Transferable[0],
					leer,//new CablePort_Transferable[0],
					leer,//new Equipment_Transferable[0],
					leer,//new Link_Transferable[0],
					leer//new CableLink_Transferable[0]
			);
		}
		catch (Exception ex)
		{
			System.err.print("Error removingPorts: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemovePorts! status = " + ecode);
			return;
		}

		for(int i = 0; i < port_ids.length; i++)
			Pool.remove(Port.typ, port_ids[i]);
	}

	public void RemoveCablePorts(String[] cport_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		String[] leer = new String[0];

		int ecode = 0;
		try
		{
			ecode = si.ci.server.RemoveNet(
					si.accessIdentity,
					leer,//new Port_Transferable[0],
					cport_ids,//new CablePort_Transferable[0],
					leer,//new Equipment_Transferable[0],
					leer,//new Link_Transferable[0],
					leer//new CableLink_Transferable[0]
			);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveCablePorts! status = " + ecode);
			return;
		}

		for(int i = 0; i < cport_ids.length; i++)
			Pool.remove(CablePort.typ, cport_ids[i]);
	}

	public void RemoveLinks(String[] link_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		String[] leer = new String[0];

		int ecode = 0;
		try
		{
			ecode = si.ci.server.RemoveNet(
					si.accessIdentity,
					leer,//new Port_Transferable[0],
					leer,//new CablePort_Transferable[0],
					leer,//new Equipment_Transferable[0],
					link_ids,//new Link_Transferable[0],
					leer//new CableLink_Transferable[0]
			);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveLinks! status = " + ecode);
			return;
		}

		for(int i = 0; i < link_ids.length; i++)
			Pool.remove(Link.typ, link_ids[i]);
	}

	public void RemoveCableLinks(String[] clink_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		String[] leer = new String[0];

		int ecode = 0;
		try
		{
			ecode = si.ci.server.RemoveNet(
					si.accessIdentity,
					leer,//new Port_Transferable[0],
					leer,//new CablePort_Transferable[0],
					leer,//new Equipment_Transferable[0],
					leer,//new Link_Transferable[0],
					clink_ids//new CableLink_Transferable[0]
			);
		}
		catch (Exception ex)
		{
			System.err.print("Error removingCableLinks: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed RemoveCableLinks! status = " + ecode);
			return;
		}

		for(int i = 0; i < clink_ids.length; i++)
			Pool.remove(CableLink.typ, clink_ids[i]);
	}

	public void RemovePaths(String[] path_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		String[] leer = new String[0];

		int ecode = 0;
		try
		{
			ecode = si.ci.server.RemoveISM(
					si.accessIdentity,
					leer,//new Port_Transferable[0],
					leer,//new CablePort_Transferable[0],
					leer,//new Equipment_Transferable[0],
					leer,//new Link_Transferable[0],
					leer,//new CableLink_Transferable[0]
					leer,//new MonitoredElement_Transferable[0],
					path_ids,//new TransmissionPath_Transferable[0],
					leer//new AccessPort_Transferable[0]);
				);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed Remove! status = " + ecode);
			return;
		}

		for(int i = 0; i < path_ids.length; i++)
			Pool.remove(TransmissionPath.typ, path_ids[i]);
	}

	public void RemoveKISs(String[] kis_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		String[] leer = new String[0];

		int ecode = 0;
		try
		{
			ecode = si.ci.server.RemoveISM(
					si.accessIdentity,
					leer,//new Port_Transferable[0],
					leer,//new CablePort_Transferable[0],
					kis_ids,//new Equipment_Transferable[0],
					leer,//new Link_Transferable[0],
					leer,//new CableLink_Transferable[0]
					leer,//new MonitoredElement_Transferable[0],
					leer,//new TransmissionPath_Transferable[0],
					leer//new AccessPort_Transferable[0]);
				);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed Remove! status = " + ecode);
			return;
		}

		for(int i = 0; i < kis_ids.length; i++)
		{
			Pool.remove(KIS.typ, kis_ids[i]);
			Pool.remove("kisequipment", kis_ids[i]);
		}
	}

	public void RemoveAccessPorts(String[] port_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		String[] leer = new String[0];

		int ecode = 0;
		try
		{
			ecode = si.ci.server.RemoveISM(
					si.accessIdentity,
					leer,//new Port_Transferable[0],
					leer,//new CablePort_Transferable[0],
					leer,//new Equipment_Transferable[0],
					leer,//new Link_Transferable[0],
					leer,//new CableLink_Transferable[0]
					leer,//new MonitoredElement_Transferable[0],
					leer,//new TransmissionPath_Transferable[0],
					port_ids//new AccessPort_Transferable[0]);
				);
		}
		catch (Exception ex)
		{
			System.err.print("Error removing: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed Remove! status = " + ecode);
			return;
		}

		for(int i = 0; i < port_ids.length; i++)
			Pool.remove(Port.typ, port_ids[i]);
	}

	public void LoadNet()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		EquipmentSeq_TransferableHolder eh = new EquipmentSeq_TransferableHolder();
		Equipment_Transferable equipments[];
		Equipment equipment;
		PortSeq_TransferableHolder ph = new PortSeq_TransferableHolder();
		Port_Transferable ports[];
		Port port;
		CablePortSeq_TransferableHolder cph = new CablePortSeq_TransferableHolder();
		CablePort_Transferable cports[];
		CablePort cport;
		LinkSeq_TransferableHolder lh = new LinkSeq_TransferableHolder();
		Link_Transferable links[];
		Link link;
		CableLinkSeq_TransferableHolder clh = new CableLinkSeq_TransferableHolder();
		CableLink_Transferable clinks[];
		CableLink clink;
		TestPortSeq_TransferableHolder tph = new TestPortSeq_TransferableHolder();
		TestPort_Transferable testports[];
		TestPort testport;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.LoadNet(si.accessIdentity, ph, cph, eh, lh, clh, tph);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting network catalogue: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadNet! status = " + ecode);
			return;
		}

		ports = ph.value;
		count = ports.length;
		System.out.println("...Done! " + count + " port(s) fetched");
			for (i = 0; i < count; i++)
		{
			port = new Port(ports[i]);
			Pool.put("port", port.getId(), port);
			loaded_objects.add(port);
			}

		cports = cph.value;
		count = cports.length;
		System.out.println("...Done! " + count + " cable port(s) fetched");
			for (i = 0; i < count; i++)
		{
			cport = new CablePort(cports[i]);
			Pool.put("cableport", cport.getId(), cport);
			loaded_objects.add(cport);
			}

		equipments = eh.value;
		count = equipments.length;
		System.out.println("...Done! " + count + " equipment(s) fetched");
			for (i = 0; i < count; i++)
		{
			equipment = new Equipment(equipments[i]);
			Pool.put("equipment", equipment.getId(), equipment);
			Pool.put("kisequipment", equipment.getId(), equipment);
			loaded_objects.add(equipment);
			}

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " link(s) fetched");
			for (i = 0; i < count; i++)
		{
			link = new Link(links[i]);
			Pool.put("link", link.getId(), link);
			loaded_objects.add(link);
			}

		clinks = clh.value;
		count = clinks.length;
		System.out.println("...Done! " + count + " cable link(s) fetched");
			for (i = 0; i < count; i++)
		{
			clink = new CableLink(clinks[i]);
			Pool.put("cablelink", clink.getId(), clink);
			loaded_objects.add(clink);
			}

		testports = tph.value;
		count = testports.length;
		System.out.println("...Done! " + count + " testport(s) fetched");
			for (i = 0; i < count; i++)
		{
			testport = new TestPort(testports[i]);
			Pool.put("testport", testport.getId(), testport);
			loaded_objects.add(testport);
			}

		// update loaded objects
		count = loaded_objects.size();
			for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void LoadISM()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;
		EquipmentSeq_TransferableHolder kh = new EquipmentSeq_TransferableHolder();
		Equipment_Transferable kiss[];
		KIS kis;
		PortSeq_TransferableHolder ph = new PortSeq_TransferableHolder();
		Port_Transferable ports[];
		Port port;
		CablePortSeq_TransferableHolder cph = new CablePortSeq_TransferableHolder();
		CablePort_Transferable cports[];
		CablePort cport;
		LinkSeq_TransferableHolder lh = new LinkSeq_TransferableHolder();
		Link_Transferable links[];
		Link link;
		CableLinkSeq_TransferableHolder clh = new CableLinkSeq_TransferableHolder();
		CableLink_Transferable clinks[];
		CableLink clink;
		MonitoredElementSeq_TransferableHolder meh = new MonitoredElementSeq_TransferableHolder();
		MonitoredElement_Transferable mes[];
		MonitoredElement me;
		TransmissionPathSeq_TransferableHolder tph = new TransmissionPathSeq_TransferableHolder();
		TransmissionPath_Transferable paths[];
		TransmissionPath path;
		AccessPortSeq_TransferableHolder aph = new AccessPortSeq_TransferableHolder();
		AccessPort_Transferable accessports[];
		AccessPort accessport;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		try
		{
			ecode = si.ci.server.LoadISM(si.accessIdentity, ph, cph, kh, lh, clh, meh, tph, aph);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting ISM catalogue: " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadISM! status = " + ecode);
			return;
		}

		ports = ph.value;
		count = ports.length;
		System.out.println("...Done! " + count + " port(s) fetched");
			for (i = 0; i < count; i++)
		{
			port = new Port(ports[i]);
			Pool.put("port", port.getId(), port);
			loaded_objects.add(port);
			}

		kiss = kh.value;
		count = kiss.length;
		System.out.println("...Done! " + count + " kis(s) fetched");
			for (i = 0; i < count; i++)
		{
			kis = new KIS(kiss[i]);
			Pool.put("kis", kis.getId(), kis);
			Pool.put("kisequipment", kis.getId(), kis);
			loaded_objects.add(kis);
			}

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " link(s) fetched");
			for (i = 0; i < count; i++)
		{
			link = new Link(links[i]);
			Pool.put("link", link.getId(), link);
			loaded_objects.add(link);
			}

		mes = meh.value;
		count = mes.length;
		System.out.println("...Done! " + count + " monitored element(s) fetched");
			for (i = 0; i < count; i++)
		{
			me = new MonitoredElement(mes[i]);
			Pool.put("monitoredelement", me.getId(), me);
			loaded_objects.add(me);
			}

		paths = tph.value;
		count = paths.length;
		System.out.println("...Done! " + count + " path(s) fetched");
			for (i = 0; i < count; i++)
		{
			path = new TransmissionPath(paths[i]);
			Pool.put("path", path.getId(), path);
			loaded_objects.add(path);
			}

		accessports = aph.value;
		count = accessports.length;
		System.out.println("...Done! " + count + " accessport(s) fetched");
			for (i = 0; i < count; i++)
		{
			accessport = new AccessPort(accessports[i]);
			Pool.put("accessport", accessport.getId(), accessport);
			loaded_objects.add(accessport);
			}

		// update loaded objects
		count = loaded_objects.size();
			for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void LoadNet(
			Vector p_ids,
			Vector cp_ids,
			Vector eq_ids,
			Vector l_ids,
			Vector cl_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;

		EquipmentSeq_TransferableHolder eh = new EquipmentSeq_TransferableHolder();
		EquipmentSeq_TransferableHolder leereh = new EquipmentSeq_TransferableHolder();
		Equipment_Transferable equipments[];
		Equipment equipment;
		PortSeq_TransferableHolder ph = new PortSeq_TransferableHolder();
		PortSeq_TransferableHolder leerph = new PortSeq_TransferableHolder();
		Port_Transferable ports[];
		Port port;
		CablePortSeq_TransferableHolder cph = new CablePortSeq_TransferableHolder();
		CablePortSeq_TransferableHolder leercph = new CablePortSeq_TransferableHolder();
		CablePort_Transferable cports[];
		CablePort cport;
		LinkSeq_TransferableHolder lh = new LinkSeq_TransferableHolder();
		LinkSeq_TransferableHolder leerlh = new LinkSeq_TransferableHolder();
		Link_Transferable links[];
		Link link;
		CableLinkSeq_TransferableHolder clh = new CableLinkSeq_TransferableHolder();
		CableLinkSeq_TransferableHolder leerclh = new CableLinkSeq_TransferableHolder();
		CableLink_Transferable clinks[];
		CableLink clink;
		TestPortSeq_TransferableHolder tph = new TestPortSeq_TransferableHolder();
		TestPortSeq_TransferableHolder leertph = new TestPortSeq_TransferableHolder();
		TestPort_Transferable testports[];
		TestPort testport;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		String[] pid_s = new String[p_ids.size()];
		p_ids.copyInto(pid_s);
		String[] cpid_s = new String[cp_ids.size()];
		cp_ids.copyInto(cpid_s);
		String[] eqid_s = new String[eq_ids.size()];
		eq_ids.copyInto(eqid_s);
		String[] lid_s = new String[l_ids.size()];
		l_ids.copyInto(lid_s);
		String[] clid_s = new String[cl_ids.size()];
		cl_ids.copyInto(clid_s);

		String[] leer = new String[0];
		try
		{
			ecode = si.ci.server.LoadStatedNet(si.accessIdentity, pid_s, cpid_s, eqid_s, leer, leer, ph, cph, eh, leerlh, leerclh, tph);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting network : " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadNet! status = " + ecode);
			return;
		}

		try
		{
			ecode = si.ci.server.LoadStatedNet(si.accessIdentity, leer, leer, leer, lid_s, leer, leerph, leercph, leereh, lh, leerclh, leertph);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting network : " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadNet! status = " + ecode);
			return;
		}

		try
		{
			ecode = si.ci.server.LoadStatedNet(si.accessIdentity, leer, leer, leer, leer, clid_s, leerph, leercph, leereh, leerlh, clh, leertph);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting network : " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadNet! status = " + ecode);
			return;
		}

		ports = ph.value;
		count = ports.length;
		System.out.println("...Done! " + count + " port(s) fetched");
			for (i = 0; i < count; i++)
		{
			port = new Port(ports[i]);
			Pool.put("port", port.getId(), port);
			loaded_objects.add(port);
			}

		cports = cph.value;
		count = cports.length;
		System.out.println("...Done! " + count + " cable port(s) fetched");
			for (i = 0; i < count; i++)
		{
			cport = new CablePort(cports[i]);
			Pool.put("cableport", cport.getId(), cport);
			loaded_objects.add(cport);
			}

		equipments = eh.value;
		count = equipments.length;
		System.out.println("...Done! " + count + " equipment(s) fetched");
			for (i = 0; i < count; i++)
		{
			equipment = new Equipment(equipments[i]);
			Pool.put("equipment", equipment.getId(), equipment);
			Pool.put("kisequipment", equipment.getId(), equipment);
			loaded_objects.add(equipment);
			}

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " link(s) fetched");
			for (i = 0; i < count; i++)
		{
			link = new Link(links[i]);
			Pool.put("link", link.getId(), link);
			loaded_objects.add(link);
			}

		clinks = clh.value;
		count = clinks.length;
		System.out.println("...Done! " + count + " cable link(s) fetched");
			for (i = 0; i < count; i++)
		{
			clink = new CableLink(clinks[i]);
			Pool.put("cablelink", clink.getId(), clink);
			loaded_objects.add(clink);
			}

		testports = tph.value;
		count = testports.length;
		System.out.println("...Done! " + count + " testport(s) fetched");
			for (i = 0; i < count; i++)
		{
			testport = new TestPort(testports[i]);
			Pool.put("testport", testport.getId(), testport);
			loaded_objects.add(testport);
			}

		// update loaded objects
		count = loaded_objects.size();
			for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void LoadISM(
		Vector k_ids,
		Vector ap_ids,
		Vector me_ids,
		Vector t_ids)
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;

		int i;
		int ecode = 0;
		int count;

		EquipmentSeq_TransferableHolder kh = new EquipmentSeq_TransferableHolder();
		Equipment_Transferable kiss[];
		KIS kis;
		PortSeq_TransferableHolder ph = new PortSeq_TransferableHolder();
		Port_Transferable ports[];
		Port port;
		CablePortSeq_TransferableHolder cph = new CablePortSeq_TransferableHolder();
		CablePort_Transferable cports[];
		CablePort cport;
		LinkSeq_TransferableHolder lh = new LinkSeq_TransferableHolder();
		Link_Transferable links[];
		Link link;
		CableLinkSeq_TransferableHolder clh = new CableLinkSeq_TransferableHolder();
		CableLink_Transferable clinks[];
		CableLink clink;
		MonitoredElementSeq_TransferableHolder meh = new MonitoredElementSeq_TransferableHolder();
		MonitoredElement_Transferable mes[];
		MonitoredElement me;
		TransmissionPathSeq_TransferableHolder tph = new TransmissionPathSeq_TransferableHolder();
		TransmissionPath_Transferable paths[];
		TransmissionPath path;
		AccessPortSeq_TransferableHolder aph = new AccessPortSeq_TransferableHolder();
		AccessPort_Transferable accessports[];
		AccessPort accessport;

		Vector loaded_objects = new Vector();
		ObjectResource or;

		String[] p_ids = new String[0];
		String[] cp_ids = new String[0];
		String[] l_ids = new String[0];
		String[] cl_ids = new String[0];

		String[] aid_s = new String[ap_ids.size()];
		ap_ids.copyInto(aid_s);
		String[] kid_s = new String[k_ids.size()];
		k_ids.copyInto(kid_s);
		String[] tid_s = new String[t_ids.size()];
		t_ids.copyInto(tid_s);
		String[] mid_s = new String[me_ids.size()];
		me_ids.copyInto(mid_s);
		try
		{
			ecode = si.ci.server.LoadStatedISM(si.accessIdentity,
				p_ids,
				cp_ids,
				kid_s,
				l_ids,
				cl_ids,
				mid_s,
				tid_s,
				aid_s,
				ph,
				cph,
				kh,
				lh,
				clh,
				meh,
				tph,
				aph);
		}
		catch (Exception ex)
		{
			System.err.print("Error getting ISM : " + ex.getMessage());
			ex.printStackTrace();
			return;
		}

		if (ecode != Constants.ERROR_NO_ERROR)
		{
			System.out.println ("Failed LoadISM! status = " + ecode);
			return;
		}

		ports = ph.value;
		count = ports.length;
		System.out.println("...Done! " + count + " port(s) fetched");
			for (i = 0; i < count; i++)
		{
			port = new Port(ports[i]);
			Pool.put("port", port.getId(), port);
			loaded_objects.add(port);
			}

		kiss = kh.value;
		count = kiss.length;
		System.out.println("...Done! " + count + " kis(s) fetched");
			for (i = 0; i < count; i++)
		{
			kis = new KIS(kiss[i]);
			Pool.put("kis", kis.getId(), kis);
			Pool.put("kisequipment", kis.getId(), kis);
			loaded_objects.add(kis);
			}

		links = lh.value;
		count = links.length;
		System.out.println("...Done! " + count + " link(s) fetched");
			for (i = 0; i < count; i++)
		{
			link = new Link(links[i]);
			Pool.put("link", link.getId(), link);
			loaded_objects.add(link);
			}

		mes = meh.value;
		count = mes.length;
		System.out.println("...Done! " + count + " monitored element(s) fetched");
			for (i = 0; i < count; i++)
		{
			me = new MonitoredElement(mes[i]);
			Pool.put("monitoredelement", me.getId(), me);
			loaded_objects.add(me);
			}

		paths = tph.value;
		count = paths.length;
		System.out.println("...Done! " + count + " path(s) fetched");
			for (i = 0; i < count; i++)
		{
			path = new TransmissionPath(paths[i]);
			Pool.put("path", path.getId(), path);
			loaded_objects.add(path);
			}

		accessports = aph.value;
		count = accessports.length;
		System.out.println("...Done! " + count + " accessport(s) fetched");
			for (i = 0; i < count; i++)
		{
			accessport = new AccessPort(accessports[i]);
			Pool.put("accessport", accessport.getId(), accessport);
			loaded_objects.add(accessport);
			}

		// update loaded objects
		count = loaded_objects.size();
			for (i = 0; i < count; i++)
		{
			or = (ObjectResource )loaded_objects.get(i);
			or.updateLocalFromTransferable();
		}
	}

	public void SaveNet()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

	public void SaveISM()
	{
		if(si == null)
			return;
		if(!si.isOpened())
			return;
	}

}