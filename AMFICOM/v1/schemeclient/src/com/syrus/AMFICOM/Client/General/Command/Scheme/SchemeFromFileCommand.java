package com.syrus.AMFICOM.Client.General.Command.Scheme;

import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;
import com.syrus.io.DirectoryToFile;

public class SchemeFromFileCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	ApplicationContext aContext;

	FileOutputStream fos;
	PrintWriter pw;

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
//		DirectoryToFile.readAll();

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
		dataSource.SaveMapProtoGroups(createSchemeProtoGroupsIdsList());

		if (Pool.getMap(Scheme.typ) != null)
			for (Iterator it = Pool.getMap(Scheme.typ).keySet().iterator(); it.hasNext();)
				dataSource.SaveScheme((String)it.next());

			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
				"Схемы успешно проимпортированы на сервер",
				"Сообщение",
				JOptionPane.INFORMATION_MESSAGE);
	}

	String[] createEqtIdsList()
	{
		Map m = Pool.getMap(EquipmentType.typ);
		if (m != null)
			return (String[])m.keySet().toArray(new String[m.size()]);
		return new String[0];
	}

	public Map getTopLevelElements()
	{
		Map ht = new HashMap();
		ArrayList ids = new ArrayList();

		if (Pool.getMap(ProtoElement.typ) != null)
			for (Iterator it = Pool.getMap(ProtoElement.typ).values().iterator(); it.hasNext();)
			{
				ProtoElement element = (ProtoElement)it.next();
				for (Iterator iit = element.protoelementIds.iterator(); iit.hasNext();)
					ids.add((String)iit.next());
				ht.put(element.getId(), element);
			}

		for (Iterator it = ht.values().iterator(); it.hasNext();)
		{
			ProtoElement element = (ProtoElement)it.next();
			if(ids.contains(element.getId()))
				ht.remove(element.getId());
		}
		return ht;
	}

	ArrayList get_proto_ids (ArrayList ids, ProtoElement proto)
	{
		for (Iterator it = proto.protoelementIds.iterator(); it.hasNext();)
		{
			ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, (String)it.next());
			get_proto_ids(ids, p);
		}
		ids.add(proto.getId());
		return ids;
	}

	String[] createSchemeProtoIdsList()
	{
		ArrayList proto_ids = new ArrayList();
		Map top_protos = getTopLevelElements();

		for (Iterator it = top_protos.values().iterator(); it.hasNext();)
			get_proto_ids(proto_ids, (ProtoElement)it.next());

		return (String[])proto_ids.toArray(new String[proto_ids.size()]);
	}

	String[] createSchemeProtoGroupsIdsList()
	{
		Map m = Pool.getMap(SchemeProtoGroup.typ);
		if (m != null)
			return (String[])m.keySet().toArray(new String[m.size()]);
		return new String[0];

	}

	String[] createLinkTypesList()
	{
		Map m = Pool.getMap(LinkType.typ);
		if (m != null)
			return (String[])m.keySet().toArray(new String[m.size()]);
		return new String[0];
	}

	String[] createCableLinkTypesList()
	{
		Map m = Pool.getMap(CableLinkType.typ);
		if (m != null)
			return (String[])m.keySet().toArray(new String[m.size()]);
		return new String[0];

	}

	String[] createPortTypesList()
	{
		Map m = Pool.getMap(PortType.typ);
		if (m != null)
			return (String[])m.keySet().toArray(new String[m.size()]);
		return new String[0];
	}

	String[] createCablePortTypesList()
	{
		Map m = Pool.getMap(CablePortType.typ);
		if (m != null)
			return (String[])m.keySet().toArray(new String[m.size()]);
		return new String[0];
	}
}


