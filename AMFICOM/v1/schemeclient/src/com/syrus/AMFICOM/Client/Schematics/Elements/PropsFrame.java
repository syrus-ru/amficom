package com.syrus.AMFICOM.Client.Schematics.Elements;

import java.util.*;
import java.util.List;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.Configure.UI.CharacteristicsPanel;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelSchematics;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.configuration.corba.*;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.scheme.corba.*;

public class PropsFrame extends JInternalFrame implements OperationListener
{
	public boolean can_be_editable = true;
	boolean editableProperty = false;

	ApplicationContext aContext;
	Dispatcher dispatcher;
	CharacteristicsPanel cPanel;

	CharacteristicTypeSort[] sorts = new CharacteristicTypeSort[] {
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPTICAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_OPERATIONAL,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_INTERFACE,
			CharacteristicTypeSort.CHARACTERISTICTYPESORT_ELECTRICAL
	};

	List attributes;
	List characteristics;
	Identifier attributedId;
	Identifier characterizedId;
	CharacteristicSort attributedSort;
	CharacteristicSort characterizedSort;
	Characterized characterizedObj;
	Characterizable attributedObj;

	boolean attributesEditable;
	boolean characteristicsEditable;

	public PropsFrame(ApplicationContext aContext, boolean can_be_editable)
	{
		super();
		this.aContext = aContext;
		this.can_be_editable = can_be_editable;

		init_module(aContext.getDispatcher());
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception
	{
		setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));
		setTitle(LangModelSchematics.getString("characteristicsTitle"));

		cPanel = new CharacteristicsPanel();

		this.setDefaultCloseOperation(JInternalFrame.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setMinimumSize(new Dimension(200, 150));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().add(cPanel);
		scrollPane.getViewport().setBackground(Color.white);
		scrollPane.setAutoscrolls(true);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}

	void init_module(Dispatcher dispatcher)
	{
		dispatcher.register(this, TreeDataSelectionEvent.type);
		dispatcher.register(this, SchemeNavigateEvent.type);

		this.dispatcher.register(this, TreeDataSelectionEvent.type);
	}

	public void operationPerformed(OperationEvent ae)
	{
		if (ae.getActionCommand().equals(SchemeNavigateEvent.type))
		{
			SchemeNavigateEvent ev = (SchemeNavigateEvent)ae;
			editableProperty = ev.isEditable;
//			setPropsEditable(ev.isEditable);
			Object[] objs = (Object[])ev.getSource();

			if (ev.SCHEME_ALL_DESELECTED) {
				showNoSelection();
				return;
			}

			if (ev.SCHEME_ELEMENT_SELECTED)
			{
				SchemeElement element = (SchemeElement)objs[0];
				setSchemeElementSelected(element);
			}
			else if (ev.SCHEME_PROTO_ELEMENT_SELECTED)
			{
				SchemeProtoElement element = (SchemeProtoElement)objs[0];
				setSchemeProtoSelected(element);
			}
			else if (ev.SCHEME_PORT_SELECTED)
			{
				SchemePort port = (SchemePort)objs[0];
				attributes = port.characteristicsImpl().getValue();
				attributedId = new Identifier(port.id().transferable());
				attributedObj = port;
				attributedSort = CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEPORT;
				attributesEditable = true;

				if (port.port() != null)
				{
					characteristics = port.portImpl().getCharacteristics();
					characterizedId = port.portImpl().getId();
					characterizedObj = port.portImpl();
					characterizedSort = CharacteristicSort.CHARACTERISTIC_SORT_PORT;
					characteristicsEditable = true;
				}
				else
				{
					if (port.portTypeImpl() != null)
					{
						characteristics = port.portTypeImpl().getCharacteristics();
						characterizedId = port.portTypeImpl().getId();
						characterizedObj = port.portTypeImpl();
						characterizedSort = CharacteristicSort.CHARACTERISTIC_SORT_PORTTYPE;
						characteristicsEditable = false;
					}
				}
			}
			else if (ev.SCHEME_CABLE_PORT_SELECTED)
			{
				SchemeCablePort port = (SchemeCablePort)objs[0];
				attributes = port.characteristicsImpl().getValue();
				attributedId = new Identifier(port.id().transferable());
				attributedObj = port;
				attributedSort = CharacteristicSort.CHARACTERISTIC_SORT_SCHEMECABLEPORT;
				attributesEditable = true;

				if (port.port() != null)
				{
					characteristics = port.portImpl().getCharacteristics();
					characterizedId = port.portImpl().getId();
					characterizedObj = port.portImpl();
					characterizedSort = CharacteristicSort.CHARACTERISTIC_SORT_CABLEPORT;
					characteristicsEditable = true;
				}
				else
				{
					if (port.portTypeImpl() != null)
					{
						characteristics = port.portTypeImpl().getCharacteristics();
						characterizedId = port.portTypeImpl().getId();
						characterizedObj = port.portTypeImpl();
						characterizedSort = CharacteristicSort.CHARACTERISTIC_SORT_PORTTYPE;
						characteristicsEditable = false;
					}
				}
			}
			else if (ev.SCHEME_LINK_SELECTED)
			{
				SchemeLink link = (SchemeLink)objs[0];
				attributes = link.characteristicsImpl().getValue();
				attributedId = new Identifier(link.id().transferable());
				attributedObj = link;
				attributedSort = CharacteristicSort.CHARACTERISTIC_SORT_SCHEMELINK;
				attributesEditable = true;

				if (link.link() != null)
				{
					characteristics = link.linkImpl().getCharacteristics();
					characterizedId = link.linkImpl().getId();
					characterizedObj = link.linkImpl();
					characterizedSort = CharacteristicSort.CHARACTERISTIC_SORT_LINK;
					characteristicsEditable = true;
				}
				else
				{
					if (link.linkTypeImpl() != null)
					{
						characteristics = link.linkTypeImpl().getCharacteristics();
						characterizedId = link.linkTypeImpl().getId();
						characterizedObj = link.linkTypeImpl();
						characterizedSort = CharacteristicSort.CHARACTERISTIC_SORT_LINKTYPE;
						characteristicsEditable = false;
					}
				}
			}
			else if (ev.SCHEME_CABLE_LINK_SELECTED)
			{
				SchemeCableLink link = (SchemeCableLink)objs[0];
				attributes = link.characteristicsImpl().getValue();
				attributedId = new Identifier(link.id().transferable());
				attributedObj = link;
				attributedSort = CharacteristicSort.CHARACTERISTIC_SORT_SCHEMECABLELINK;
				attributesEditable = true;

				if (link.link() != null)
				{
					characteristics = link.linkImpl().getCharacteristics();
					characterizedId = link.linkImpl().getId();
					characterizedObj = link.linkImpl();
					characterizedSort = CharacteristicSort.CHARACTERISTIC_SORT_CABLELINK;
					characteristicsEditable = true;
				}
				else
				{
					if (link.cableLinkTypeImpl() != null)
					{
						characteristics = link.cableLinkTypeImpl().getCharacteristics();
						characterizedId = link.cableLinkTypeImpl().getId();
						characterizedObj = link.cableLinkTypeImpl();
						characterizedSort = CharacteristicSort.CHARACTERISTIC_SORT_CABLELINKTYPE;
						characteristicsEditable = false;
					}
				}
			}
			else if (ev.SCHEME_PATH_SELECTED)
			{
				showNoSelection();
			}
			else
				showNoSelection();

			showSelection();
		}
		else if (ae.getActionCommand().equals(TreeDataSelectionEvent.type))
		{
			TreeDataSelectionEvent ev = (TreeDataSelectionEvent) ae;
			if (ev.getDataClass() == null)
				return;
			if (ev.getDataClass().equals(SchemeProtoElement.class))
			{
				if (ev.getSelectionNumber() != -1)
				{
					SchemeProtoElement element = (SchemeProtoElement)ev.getList().get(ev.getSelectionNumber());
					setSchemeProtoSelected(element);
				}
				else
					showNoSelection();
			}
			else if (ev.getDataClass().equals(SchemeElement.class))
			{
				if (ev.getSelectionNumber() != -1)
				{
					SchemeElement element = (SchemeElement)ev.getList().get(ev.getSelectionNumber());
					setSchemeElementSelected(element);
				}
				else
					showNoSelection();
			}
			else
				showNoSelection();

			showSelection();
		}
	}

	void setSchemeProtoSelected(SchemeProtoElement element)
	{
		attributes = element.characteristicsImpl().getValue();
		attributedId = new Identifier(element.id().transferable());
		attributedObj = element;
		attributedSort = CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEPROTOELEMENT;
		attributesEditable = true;

		if (element.equipmentType() != null) {
			characteristics = element.equipmentTypeImpl().getCharacteristics();
			characterizedId = element.equipmentTypeImpl().getId();
			characterizedObj = element.equipmentTypeImpl();
			characterizedSort = CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENTTYPE;
			characteristicsEditable = false;
		}
	}

	void setSchemeElementSelected(SchemeElement element)
	{
		attributes = element.characteristicsImpl().getValue();
		attributedId = new Identifier(element.id().transferable());
		attributedObj = element;
		attributedSort = CharacteristicSort.CHARACTERISTIC_SORT_SCHEMEELEMENT;
		attributesEditable = true;

		if (element.equipment() != null)
		{
			characteristics = element.equipmentImpl().getCharacteristics();
			characterizedId = element.equipmentImpl().getId();
			characterizedObj = element.equipmentImpl();
			characterizedSort = CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENT;
			characteristicsEditable = true;
		}
		else
		{
			if (element.equipmentType() != null)
			{
				characteristics = element.equipmentTypeImpl().getCharacteristics();
				characterizedId = element.equipmentTypeImpl().getId();
				characterizedObj = element.equipmentTypeImpl();
				characterizedSort = CharacteristicSort.CHARACTERISTIC_SORT_EQUIPMENTTYPE;
				characteristicsEditable = false;
			}
		}
	}

	void showSelection()
	{
		for (int i = 0; i < sorts.length; i++)
			cPanel.setTypeSortMapping(
					sorts[i],
					characterizedSort,
					characterizedObj,
					characterizedId,
					characteristicsEditable);
		cPanel.setTypeSortMapping(
				CharacteristicTypeSort.CHARACTERISTICTYPESORT_VISUAL,
				attributedSort,
				attributedObj,
				attributedId,
				attributesEditable);

		cPanel.addCharacteristics(characteristics, characterizedId);
		cPanel.addCharacteristics(attributes, attributedId);
	}

	void showNoSelection()
	{
		cPanel.clear();
	}

	void removeCharacterisric(List characteristics, String name)
	{
		for (Iterator it = characteristics.iterator(); it.hasNext();)
		{
			Characteristic ch = (Characteristic)it.next();
			CharacteristicType t = (CharacteristicType)ch.getType();
			if (t.getDependencies().equals(name))
			{
				it.remove();
				break;
			}
		}
	}

	void setCharacteristic(List characteristics, String name, String value)
	{
		for (Iterator it = characteristics.iterator(); it.hasNext();)
		{
			Characteristic attr = (Characteristic)it.next();
			CharacteristicType at = (CharacteristicType)attr.getType();
			if (at.getDescription().equals(name))
			{
				attr.setValue(value);
				break;
			}
		}
	}
}
