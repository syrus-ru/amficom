package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Map.MapProtoElement;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.EquipmentType;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.Resource.SchemeDirectory.ProtoElement;

public class SaveComponentDialog extends JDialog
{
	ProtoElement proto;
	DataSourceInterface dataSource;

	ProtoElementPropsPanel componentPanel;
	ApplicationContext aContext;

	public SaveComponentDialog(ApplicationContext aContext)
	{
		super(Environment.getActiveWindow());
		this.aContext = aContext;
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		setTitle("Сохранение компонента");
		//setResizable(false);
		setModal(true);

		this.addWindowListener(new java.awt.event.WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				cancelButton_actionPerformed();
			}
		});

		Dimension frameSize = new Dimension (400, 165);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(frameSize);
		setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);

		getContentPane().setLayout(new BorderLayout());
		// COMPONENT
		componentPanel = new ProtoElementPropsPanel(aContext);
		this.getContentPane().add(componentPanel, BorderLayout.CENTER);

		// BUTTONS
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());

		JButton okButton = new JButton("OK");
		JButton cancelButton = new JButton("Отмена");
		okButton.setPreferredSize(new Dimension(80, 25));
		cancelButton.setPreferredSize(new Dimension(80, 25));
		buttonPanel.add(okButton, FlowLayout.LEFT);
		buttonPanel.add(cancelButton, FlowLayout.CENTER);
		okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				this_okButtonActionPerformed();
			}
		});
		cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed (ActionEvent ev)
			{
				cancelButton_actionPerformed();
			}
		});
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	public void init(ProtoElement proto, DataSourceInterface dataSource)
	{
		this.proto = proto;
		this.dataSource = dataSource;

		componentPanel.init(proto, dataSource, true);
		//groupPanel.init(proto, dataSource);

		setVisible(true);
	}

	void cancelButton_actionPerformed()
	{
		componentPanel.undo();
		dispose();
	}

	void this_okButtonActionPerformed()
	{
		MapProtoElement map_proto = componentPanel.getMapProtoElement();
		if (map_proto == null)
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Не задана группа компонентов.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}
		if (!MiscUtil.validName(componentPanel.getProtoName()))
		{
			JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Некорректное название компонента.", "Ошибка", JOptionPane.OK_OPTION);
			return;
		}

		//ComponentSaveCommand.saveTypes(aContext.getDataSourceInterface(), false);

		if (!map_proto.pe_ids.contains(proto.getId()))
			map_proto.pe_ids.add(proto.getId());
		proto.map_proto = map_proto;

		proto.name = componentPanel.getProtoName();
		EquipmentType eqt = (EquipmentType)Pool.get(EquipmentType.typ, proto.equipment_type_id);
		eqt.name = proto.getName();

		proto.domain_id = aContext.getSessionInterface().getDomainId();
		String[] proto_ids = (String[])createProtoIdsList(proto).toArray(new String[0]);

		for (int i = 0; i < proto_ids.length; i++)
		{
			ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, proto_ids[i]);
			p.pack();
		}
		dataSource.SaveSchemeProtos(proto_ids);

		Pool.put(MapProtoElement.typ, map_proto.getId(), map_proto);
		dataSource.SaveMapProtoElements(new String[] {map_proto.getId()});

		String[] eqtype_ids = (String[])createEqTypesList(proto).toArray(new String[0]);
		dataSource.SaveEquipmentTypes(eqtype_ids);

		Hashtable l_ids = createLinkIdsList(proto);
		if (!l_ids.isEmpty())
		{
			String[] link_ids = new String[l_ids.size()];
			int i = 0;
			for (Enumeration enum = l_ids.keys(); enum.hasMoreElements();)
				link_ids[i++] = (String)enum.nextElement();
			dataSource.SaveLinkTypes(link_ids);
		}

		Hashtable p_ids = createPortIdsList(proto);
		if (!p_ids.isEmpty())
		{
			String[] port_ids = new String[p_ids.size()];
			int i = 0;
			for (Enumeration enum = p_ids.keys(); enum.hasMoreElements();)
				port_ids[i++] = (String)enum.nextElement();
			dataSource.SavePortTypes(port_ids);
		}

		aContext.getDispatcher().notify(new TreeListSelectionEvent(map_proto.getTyp(), TreeListSelectionEvent.REFRESH_EVENT));
		dispose();

		JOptionPane.showMessageDialog(
				Environment.getActiveWindow(),
				"Элемент "+ proto.getName() +" успешно сохранен",
				"Сообщение",
						JOptionPane.INFORMATION_MESSAGE);
	}

	ArrayList createProtoIdsList(ProtoElement proto)
	{
		ArrayList proto_ids = new ArrayList();
		for (Iterator it = proto.protoelement_ids.iterator(); it.hasNext();)
		{
			ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, (String)it.next());
			proto_ids.addAll(createProtoIdsList(p));
		}
		proto_ids.add(proto.getId());
		return proto_ids;
	}

	ArrayList createEqTypesList(ProtoElement proto)
	{
		ArrayList eq_ids = new ArrayList();
		for (Iterator it = proto.protoelement_ids.iterator(); it.hasNext();)
		{
			ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, (String)it.next());
			eq_ids.addAll(createEqTypesList(p));
		}
		eq_ids.add(proto.equipment_type_id);
		return eq_ids;
	}

	Hashtable createLinkIdsList(ProtoElement proto)
	{
		Hashtable l_ids = new Hashtable();
		for (Iterator it = proto.protoelement_ids.iterator(); it.hasNext();)
		{
			ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, (String)it.next());
			l_ids.putAll(createLinkIdsList(p));
		}
		for (Iterator it = proto.links.iterator(); it.hasNext();)
		{
			SchemeLink link = (SchemeLink)it.next();
			l_ids.put(link.link_type_id, link.link_type_id);
		}
		return l_ids;
	}

	Hashtable createPortIdsList(ProtoElement proto)
	{
		Hashtable p_ids = new Hashtable();
		for (Iterator it = proto.protoelement_ids.iterator(); it.hasNext();)
		{
			ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, (String)it.next());
			p_ids.putAll(createPortIdsList(p));
		}
		for (Iterator it = proto.devices.iterator(); it.hasNext();)
		{
			SchemeDevice dev = (SchemeDevice)it.next();
			for (Iterator pit = dev.ports.iterator(); pit.hasNext();)
			{
				SchemePort port = (SchemePort)pit.next();
				p_ids.put(port.port_type_id, port.port_type_id);
			}
		}
		return p_ids;
	}

	Hashtable createCablePortIdsList(ProtoElement proto)
	{
		Hashtable p_ids = new Hashtable();
		for (Iterator it = proto.protoelement_ids.iterator(); it.hasNext();)
		{
			ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, (String)it.next());
			p_ids.putAll(createCablePortIdsList(p));
		}
		for (Iterator it = proto.devices.iterator(); it.hasNext();)
		{
			SchemeDevice dev = (SchemeDevice)it.next();
			for (Iterator pit = dev.cableports.iterator(); pit.hasNext();)
			{
				SchemeCablePort port = (SchemeCablePort)pit.next();
				p_ids.put(port.cable_port_type_id, port.cable_port_type_id);
			}
		}
		return p_ids;
	}
}
