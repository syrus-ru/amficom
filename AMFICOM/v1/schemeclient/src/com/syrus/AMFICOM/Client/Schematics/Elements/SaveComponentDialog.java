package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.*;
import java.util.List;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Event.TreeListSelectionEvent;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.MiscUtil;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.*;

public class SaveComponentDialog extends JDialog
{
	SchemeProtoElement proto;

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

	public void init(SchemeProtoElement proto)
	{
		this.proto = proto;

		componentPanel.init(proto, true);
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
		SchemeProtoGroup scheme_proto = componentPanel.getSchemeProtoGroup();
		if (scheme_proto == null)
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

		List protos = Arrays.asList(scheme_proto.schemeProtoElements());
		if (!protos.contains(proto))
			protos.add(proto);
		proto.parent(scheme_proto);

		proto.name(componentPanel.getProtoName());
		EquipmentType eqt = proto.equipmentTypeImpl();

		try {
			SchemeStorableObjectPool.putStorableObject(proto);
			ConfigurationStorableObjectPool.putStorableObject(eqt);
			aContext.getDispatcher().notify(new TreeListSelectionEvent(scheme_proto, TreeListSelectionEvent.REFRESH_EVENT));
			dispose();

			JOptionPane.showMessageDialog(
					Environment.getActiveWindow(),
					"Элемент "+ proto.name() +" успешно сохранен",
					"Сообщение",
							JOptionPane.INFORMATION_MESSAGE);
		}
		catch (IllegalObjectEntityException ex) {
			ex.printStackTrace();
		}
	}

	/*ArrayList createProtoIdsList(SchemeProtoElement proto)
	{
		ArrayList proto_ids = new ArrayList();
		SchemeProtoElement[] p = proto.protoElements();
		for (int i = 0; i < p.length; i++)
			proto_ids.addAll(createProtoIdsList(p[i]));

		proto_ids.add(proto.id());
		return proto_ids;
	}

	ArrayList createEqTypesList(ProtoElement proto)
	{
		ArrayList eq_ids = new ArrayList();
		for (Iterator it = proto.protoelementIds.iterator(); it.hasNext();)
		{
			ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, (String)it.next());
			eq_ids.addAll(createEqTypesList(p));
		}
		eq_ids.add(proto.equipmentTypeId);
		return eq_ids;
	}

	Map createLinkIdsList(ProtoElement proto)
	{
		Map l_ids = new HashMap();
		for (Iterator it = proto.protoelementIds.iterator(); it.hasNext();)
		{
			ProtoElement p = (ProtoElement)Pool.get(ProtoElement.typ, (String)it.next());
			l_ids.putAll(createLinkIdsList(p));
		}
		for (Iterator it = proto.links.iterator(); it.hasNext();)
		{
			SchemeLink link = (SchemeLink)it.next();
			l_ids.put(link.linkTypeId, link.linkTypeId);
		}
		return l_ids;
	}

	Map createPortIdsList(ProtoElement proto)
	{
		Map p_ids = new HashMap();
		for (Iterator it = proto.protoelementIds.iterator(); it.hasNext();)
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
				p_ids.put(port.portTypeId, port.portTypeId);
			}
		}
		return p_ids;
	}

	Map createCablePortIdsList(ProtoElement proto)
	{
		Map p_ids = new HashMap();
		for (Iterator it = proto.protoelementIds.iterator(); it.hasNext();)
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
				p_ids.put(port.cablePortTypeId, port.cablePortTypeId);
			}
		}
		return p_ids;
	}*/
}
