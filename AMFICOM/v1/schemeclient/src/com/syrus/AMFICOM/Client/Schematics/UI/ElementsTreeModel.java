package com.syrus.AMFICOM.Client.Schematics.UI;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Lang.LangModelConfig;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceCatalogActionModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeModel;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceTreeNode;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.configuration.CableLinkType;
import com.syrus.AMFICOM.configuration.CableLinkTypeController;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.LinkType;
import com.syrus.AMFICOM.configuration.LinkTypeController;
import com.syrus.AMFICOM.configuration.MeasurementPortType;
import com.syrus.AMFICOM.configuration.MeasurementPortTypeController;
import com.syrus.AMFICOM.configuration.PortType;
import com.syrus.AMFICOM.configuration.PortTypeController;
import com.syrus.AMFICOM.configuration.TransmissionPathType;
import com.syrus.AMFICOM.configuration.TransmissionPathTypeController;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.EquivalentCondition;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeController;
import com.syrus.AMFICOM.scheme.corba.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.corba.SchemeProtoGroup;


public class ElementsTreeModel extends ObjectResourceTreeModel
{
	ApplicationContext aContext;

	public ElementsTreeModel(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public ObjectResourceTreeNode getRoot()
	{
		return new ObjectResourceTreeNode ("root", "Компоненты сети", true,
																			 new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif")));
	}

	public ImageIcon getNodeIcon(ObjectResourceTreeNode node)
	{
		return null;
	}

	public Color getNodeTextColor(ObjectResourceTreeNode node)
	{
		return null;
	}

	public void nodeAfterSelected(ObjectResourceTreeNode node)
	{
	}

	public void nodeBeforeExpanded(ObjectResourceTreeNode node)
	{
	}

	public ObjectResourceCatalogActionModel getNodeActionModel(ObjectResourceTreeNode node)
	{
//		if(node.getObject() instanceof String)
//		{
	/*		String s = (String )node.getObject();
			if(s.equals(LinkType.typ) ||
							s.equals(CableLinkType.typ) ||
							s.equals(PortType.typ) ||
							s.equals(CablePortType.typ) ||
							s.equals(MeasurementPortType.typ) ||
							s.equals(TransmissionPathType.typ))
				return new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.PANEL,
				ObjectResourceCatalogActionModel.ADD_BUTTON,
				ObjectResourceCatalogActionModel.SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.PROPS_BUTTON,
				ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
		}*/
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("LinkType") ||
					s.equals("CableLinkType.typ") ||
					s.equals("PortType.typ") ||
					s.equals("CablePortType.typ") ||
					s.equals("MeasurementPortType.typ") ||
					s.equals("TransmissionPathType.typ"))
				return new ObjectResourceCatalogActionModel(
						ObjectResourceCatalogActionModel.NO_PANEL,
						ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
						ObjectResourceCatalogActionModel.NO_SAVE_BUTTON,
						ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
						ObjectResourceCatalogActionModel.NO_PROPS_BUTTON,
						ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
		}
		return new ObjectResourceCatalogActionModel(
				ObjectResourceCatalogActionModel.NO_PANEL,
				ObjectResourceCatalogActionModel.NO_ADD_BUTTON,
				ObjectResourceCatalogActionModel.NO_SAVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_REMOVE_BUTTON,
				ObjectResourceCatalogActionModel.NO_PROPS_BUTTON,
				ObjectResourceCatalogActionModel.NO_CANCEL_BUTTON);
	}

	public Class getNodeChildClass(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("SchemeProtoGroup"))
				return SchemeProtoGroup.class;
			if(s.equals("LinkType"))
				return LinkType.class;
			if(s.equals("CableLinkType"))
				return CableLinkType.class;
			if(s.equals("PortType"))
				return PortType.class;
//			if(s.equals(KISType.typ))
//				return KISType.class;
			if(s.equals("TransmissionPathType"))
				return TransmissionPathType.class;
			if(s.equals("MeasurementPortType"))
				return MeasurementPortType.class;
			if(s.equals("MeasurementType"))
				return MeasurementType.class;
		}
		else if (node.getObject() instanceof SchemeProtoGroup)
		{
			if (((SchemeProtoGroup)node.getObject()).schemeProtoGroups().length != 0)
				return SchemeProtoGroup.class;
			return SchemeProtoElement.class;
		}
		else if (node.getObject() instanceof SchemeProtoGroup)
			return SchemeProtoElement.class;
		return null;
	}

	public ObjectResourceController getNodeChildController(ObjectResourceTreeNode node)
	{
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("SchemeProtoGroup"))
				return null;
			/**
			 * @todo write SchemeProtoGroupController
			 * return SchemeProtoGroupController.getInstance();
			 */
			if(s.equals("LinkType"))
				return LinkTypeController.getInstance();
			if(s.equals("CableLinkType"))
				return CableLinkTypeController.getInstance();
			if(s.equals("PortType"))
				return PortTypeController.getInstance();
			if(s.equals("TransmissionPathType"))
				return TransmissionPathTypeController.getInstance();
			if(s.equals("MeasurementType"))
				return MeasurementTypeController.getInstance();
			if(s.equals("MeasurementPortType"))
				return MeasurementPortTypeController.getInstance();
		}
		else if (node.getObject() instanceof SchemeProtoGroup)
		{
			if (((SchemeProtoGroup)node.getObject()).schemeProtoGroups().length != 0)
				return null;
			/**
			 * @todo write SchemeProtoGroupController
			 * return SchemeProtoGroupController.getInstance();
			 */
			else
				return null;
			/**
			 * @todo write SchemeProtoElementController
			 * return SchemeProtoElementController.getInstance();
			 */
		}
		return null;
	}


	public List getChildNodes(ObjectResourceTreeNode node)
	{
		List vec = new ArrayList();
		if(node.getObject() instanceof String)
		{
			String s = (String )node.getObject();
			if(s.equals("root"))
			{
				vec.add(new ObjectResourceTreeNode("configure", LangModelConfig.getString("label_configuration"), true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				vec.add(new ObjectResourceTreeNode ("SchemeProtoGroup", "Компоненты сети", true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
			}
			else if(s.equals("configure"))
			{
				vec.add(new ObjectResourceTreeNode("netdirectory", LangModelConfig.getString("menuNetDirText"), true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				vec.add(new ObjectResourceTreeNode("jdirectory", LangModelConfig.getString("menuJDirText"), true,
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
			} 
			else if(s.equals("netdirectory"))
			{
//				vec.add(new ObjectResourceTreeNode(EquipmentType.typ, LangModelConfig.getString("menuNetDirEquipmentText"), true));
				vec.add(new ObjectResourceTreeNode("LinkType", LangModelConfig.getString("menuNetDirLinkText"), true));
				vec.add(new ObjectResourceTreeNode("CableLinkType", LangModelConfig.getString("menuNetDirCableText"), true));
				vec.add(new ObjectResourceTreeNode("PortType", LangModelConfig.getString("menuNetDirPortText"), true));
//				vec.add(new ObjectResourceTreeNode("CablePortType", LangModelConfig.getString("menuNetDirCablePortText"), true));
			}
			else if(s.equals("jdirectory"))
			{
//				vec.add(new ObjectResourceTreeNode(KISType.typ, LangModelConfig.getString("menuJDirKISText"), true));
				vec.add(new ObjectResourceTreeNode("MeasurementType", LangModelConfig.getString("MeasurementType"), true));
				vec.add(new ObjectResourceTreeNode("MeasurementPortType", LangModelConfig.getString("menuJDirAccessPointText"), true));
//				vec.add(new ObjectResourceTreeNode("TransmissionPathType", LangModelConfig.getString("menuJDirPathText"), true));
			}
			else if(s.equals("LinkType"))
			{
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINKTYPE_ENTITY_CODE);
					Collection linkTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = linkTypes.iterator(); it.hasNext(); ) {
						LinkType type = (LinkType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(type, type.getName(), true, true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if(s.equals("CableLinkType"))
			{
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.CABLELINKTYPE_ENTITY_CODE);
					Collection linkTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = linkTypes.iterator(); it.hasNext(); ) {
						CableLinkType type = (CableLinkType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(type, type.getName(), true, true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}

			}
			else if(s.equals("PortType"))
			{
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.PORTTYPE_ENTITY_CODE);
					Collection portTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = portTypes.iterator(); it.hasNext(); ) {
						PortType type = (PortType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(type, type.getName(), true, true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if(s.equals("TransmissionPathType"))
			{
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE);
					Collection pathTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = pathTypes.iterator(); it.hasNext(); ) {
						TransmissionPathType type = (TransmissionPathType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(type, type.getName(), true, true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if(s.equals("MeasurementPortType"))
			{
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
					Collection pathTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = pathTypes.iterator(); it.hasNext(); ) {
						MeasurementPortType type = (MeasurementPortType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(type, type.getName(), true, true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if(s.equals("MeasurementType"))
			{
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.MEASUREMENTTYPE_ENTITY_CODE);
					Collection measurementTypes = MeasurementStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = measurementTypes.iterator(); it.hasNext(); ) {
						MeasurementType type = (MeasurementType)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(type, type.getDescription(), true, true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("SchemeProtoGroup"))
			{
				try {
					Identifier domain_id = new Identifier(((RISDSessionInfo)aContext.getSessionInterface()).
							getAccessIdentifier().domain_id);
					LinkedIdsCondition condition = new LinkedIdsCondition(domain_id,
							ObjectEntities.SCHEME_PROTO_GROUP_ENTITY_CODE);
					Collection groups = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = groups.iterator(); it.hasNext(); ) {
						SchemeProtoGroup group = (SchemeProtoGroup)it.next();
						ObjectResourceTreeNode n = new ObjectResourceTreeNode(group, group.name(), true, true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
		}
		else
		{
			if(node.getObject() instanceof SchemeProtoGroup)
			{
				SchemeProtoGroup parent_group = (SchemeProtoGroup)node.getObject();
				for (int i = 0; i < parent_group.schemeProtoGroups().length; i++)
				{
					SchemeProtoGroup map_group = parent_group.schemeProtoGroups()[i];
					ImageIcon icon;
					if (map_group.symbol() == null)
						icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
					else
						icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(
								map_group.symbolImpl().getImage()).
																 getScaledInstance(16, 16, Image.SCALE_SMOOTH));

					vec.add(new ObjectResourceTreeNode(map_group, map_group.name(), true, icon,
							map_group.schemeProtoGroups().length == 0 && map_group.schemeProtoElements().length == 0));
				}
				if (vec.isEmpty())
				{
					for (int i = 0; i < parent_group.schemeProtoElements().length; i++)
					{
						SchemeProtoElement proto = parent_group.schemeProtoElements()[i];
//						proto.scheme_proto_group = parent_group;
						vec.add(new ObjectResourceTreeNode(proto, proto.name().length() == 0 ? "Без названия" : proto.name(), true, true));
					}
				}
			}
		}
		return vec;
	}
}
