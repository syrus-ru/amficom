package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.Scheme.MapProtoGroup;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CableLinkType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.CablePortType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.LinkType;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.PortType;
import com.syrus.AMFICOM.Client.Resource.Scheme.Scheme;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

import com.syrus.io.DirectoryToFile;

public class SchemeFromFileCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;

	FileOutputStream fos;
	PrintWriter pw;

	Hashtable idl = new Hashtable();

	public SchemeFromFileCommand()
	{
	}

	public SchemeFromFileCommand(Dispatcher dispatcher, ApplicationContext aContext)
	{
		this.dispatcher = dispatcher;
		this.aContext = aContext;
	}

	public Object clone()
	{
		return new SchemeFromFileCommand(dispatcher, aContext);
	}

	public void execute()
	{
		DirectoryToFile.readAll();
		dispatcher.notify(new TreeListSelectionEvent("",  TreeListSelectionEvent.REFRESH_EVENT));

		int res = JOptionPane.showConfirmDialog(
				Environment.getActiveWindow(),
				"Вы действительно хотите сохранить на сервер данные из файла?",
				"Подтверждение",
				JOptionPane.YES_NO_CANCEL_OPTION);
		if (res != JOptionPane.YES_OPTION)
			return;

		System.out.println("Saving data to server...");

		DataSourceInterface dataSource = aContext.getDataSourceInterface();

		dataSource.SaveEquipmentTypes(createEqtIdsList());
		dataSource.SaveSchemeProtos(createSchemeProtoIdsList());
		dataSource.SaveLinkTypes(createLinkTypesList());
		dataSource.SaveCableLinkTypes(createCableLinkTypesList());
		dataSource.SavePortTypes(createPortTypesList());
		dataSource.SaveCablePortTypes(createCablePortTypesList());
		dataSource.SaveMapProtoElements(createMapProtoIdsList());
		dataSource.SaveMapProtoGroups(createMapProtoGroupsIdsList());

		if (Pool.getHash(Scheme.typ) != null)
			for (Enumeration enum = Pool.getHash(Scheme.typ).keys(); enum.hasMoreElements();)
			{
				String id = (String)enum.nextElement();
				Scheme sch = (Scheme)Pool.get(Scheme.typ, id);
				dataSource.SaveScheme(id);
			}

			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
				"Схемы успешно проимпортированы на сервер",
				"Сообщение",
				JOptionPane.INFORMATION_MESSAGE);
	}

	String[] createEqtIdsList()
	{
		ArrayList eqt_ids = new ArrayList();
		int size = 0;

		if (Pool.getHash(EquipmentType.typ) != null)
			for (Enumeration enum = Pool.getHash(EquipmentType.typ).keys(); enum.hasMoreElements();)
			{
				eqt_ids.add(enum.nextElement());
				size++;
			}
		return (String[])eqt_ids.toArray(new String[size]);
	}

	public Hashtable getTopLevelElements()
	{
		Hashtable ht = new Hashtable();
		ArrayList ids = new ArrayList();

		if (Pool.getHash(ProtoElement.typ) != null)
			for (Enumeration enum = Pool.getHash(ProtoElement.typ).elements(); enum.hasMoreElements();)
			{
				ProtoElement element = (ProtoElement)enum.nextElement();
				for (int j = 0; j < element.protoelement_ids.size(); j++)
					ids.add((String )element.protoelement_ids.get(j));
				ht.put(element.getId(), element);
			}

		for (Enumeration enum = ht.elements(); enum.hasMoreElements();)
		{
			ProtoElement element = (ProtoElement)enum.nextElement();
			if(ids.contains(element.getId()))
				ht.remove(element.getId());
		}
		return ht;
	}

	ArrayList get_proto_ids (ArrayList ids, ProtoElement proto)
	{
		for (int i = 0; i < proto.protoelement_ids.size(); i++)
		{
			ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, (String)proto.protoelement_ids.get(i));
			get_proto_ids(ids, p);
		}
		ids.add(proto.getId());
		return ids;
	}

	String[] createSchemeProtoIdsList()
	{
		ArrayList proto_ids = new ArrayList();
		Hashtable top_protos = getTopLevelElements();

		for (Enumeration enum = top_protos.elements(); enum.hasMoreElements();)
			get_proto_ids(proto_ids, (ProtoElement)enum.nextElement());

		return (String[])proto_ids.toArray(new String[proto_ids.size()]);
	}

	String[] createMapProtoIdsList()
	{
		ArrayList mps = new ArrayList();

		if (Pool.getHash(MapProtoElement.typ) != null)
			for (Enumeration enum = Pool.getHash(MapProtoElement.typ).keys(); enum.hasMoreElements();)
				mps.add(enum.nextElement());

		return (String[])mps.toArray(new String[mps.size()]);
	}

	String[] createMapProtoGroupsIdsList()
	{
		ArrayList mps = new ArrayList();

		if (Pool.getHash(MapProtoGroup.typ) != null)
			for (Enumeration enum = Pool.getHash(MapProtoGroup.typ).keys(); enum.hasMoreElements();)
				mps.add(enum.nextElement());

		return (String[])mps.toArray(new String[mps.size()]);
	}

	String[] createLinkTypesList()
	{
		ArrayList lts = new ArrayList();
		int size = 0;

		if (Pool.getHash(LinkType.typ) != null)
			for (Enumeration enum = Pool.getHash(LinkType.typ).keys(); enum.hasMoreElements();)
			{
				lts.add(enum.nextElement());
				size++;
			}
		return (String[])lts.toArray(new String[size]);
	}

	String[] createCableLinkTypesList()
	{
		ArrayList lts = new ArrayList();
		int size = 0;

		if (Pool.getHash(CableLinkType.typ) != null)
			for (Enumeration enum = Pool.getHash(CableLinkType.typ).keys(); enum.hasMoreElements();)
			{
				lts.add(enum.nextElement());
				size++;
			}
		return (String[])lts.toArray(new String[size]);
	}

	String[] createPortTypesList()
	{
		ArrayList pts = new ArrayList();
		int size = 0;

		if (Pool.getHash(PortType.typ) != null)
			for (Enumeration enum = Pool.getHash(PortType.typ).keys(); enum.hasMoreElements();)
			{
				pts.add(enum.nextElement());
				size++;
			}
		return (String[])pts.toArray(new String[size]);
	}

	String[] createCablePortTypesList()
	{
		ArrayList pts = new ArrayList();
		int size = 0;

		if (Pool.getHash(CablePortType.typ) != null)
			for (Enumeration enum = Pool.getHash(CablePortType.typ).keys(); enum.hasMoreElements();)
			{
				pts.add(enum.nextElement());
				size++;
			}
		return (String[])pts.toArray(new String[size]);
	}
}


