package com.syrus.AMFICOM.Client.Schematics.UI;

import java.awt.*;
import java.util.*;
import java.util.List;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.General.RISDSessionInfo;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.client_.general.ui_.tree.*;
import com.syrus.AMFICOM.client_.resource.ObjectResourceController;
import com.syrus.AMFICOM.configuration.*;
import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.AMFICOM.measurement.*;
import com.syrus.AMFICOM.scheme.SchemeStorableObjectPool;
import com.syrus.AMFICOM.scheme.corba.*;
import com.syrus.AMFICOM.scheme.corba.SchemePackage.Type;

public class SchemeTreeModel implements TreeDataModel
{
	ApplicationContext aContext;
	StorableObjectTreeNode root = new StorableObjectTreeNode("root", "Сеть",
			new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif")));

	private static Type[] schemeTypes = new Type[] {
		Type.NETWORK,
		Type.BUILDING,
		Type.CABLE_SUBNETWORK
	};

	public SchemeTreeModel(ApplicationContext aContext)
	{
		this.aContext = aContext;
	}

	public StorableObjectTreeNode getRoot()
	{
		return root;
	}

	public Color getNodeTextColor(StorableObjectTreeNode node)
	{
		return null;
	}

	public Class getNodeChildClass(StorableObjectTreeNode node)
	{
		if(node.getUserObject() instanceof String)
		{
			String s = (String )node.getUserObject();
			if(s.equals("SchemeProtoGroup"))
				return SchemeProtoGroup.class;
			if(s.equals("Scheme"))
				return Scheme.class;
			if(s.equals("SchemeElement"))
				return SchemeElement.class;
			if(s.equals("SchemeLink"))
				return SchemeLink.class;
			if(s.equals("SchemeCableLink"))
				return SchemeCableLink.class;
			if(s.equals("SchemePath"))
				return SchemePath.class;
			if(s.equals("SchemeProtoGroup"))
				return SchemeProtoGroup.class;
			if(s.equals("LinkType"))
				return LinkType.class;
			if(s.equals("CableLinkType"))
				return CableLinkType.class;
			if(s.equals("PortType"))
				return PortType.class;
			if(s.equals("TransmissionPathType"))
				return TransmissionPathType.class;
			if(s.equals("MeasurementPortType"))
				return MeasurementPortType.class;
			if(s.equals("schemeTypes"))
				return Type.class;
			if(s.equals("MeasurementType"))
				return MeasurementType.class;
		}
		else if (node.getUserObject() instanceof Type)
			return Scheme.class;
		else if (node.getUserObject() instanceof SchemeProtoGroup)
		{
			if (((SchemeProtoGroup)node.getUserObject()).schemeProtoGroups().length != 0)
				return SchemeProtoGroup.class;
			return SchemeProtoElement.class;
		}
		else if (node.getUserObject() instanceof Scheme)
			return Scheme.class;
		else if (node.getUserObject() instanceof SchemeElement)
			return SchemeElement.class;
		return null;
	}

	public ObjectResourceController getNodeChildController(StorableObjectTreeNode node)
	{
		if(node.getUserObject() instanceof String)
		{
			String s = (String )node.getUserObject();
			if(s.equals("SchemeProtoGroup"))
				return null;
			/**
			 * @todo write SchemeProtoGroupController
			 * return SchemeProtoGroupController.getInstance();
			 */
			if(s.equals("Scheme"))
				return SchemeController.getInstance();
			if(s.equals("SchemeElement"))
				return null;
			/**
			 * @todo write SchemeElementController
			 * return SchemeElementController.getInstance();
			 */
			if(s.equals("SchemeLink"))
				return null;
			/**
			 * @todo write SchemeLinkController
			 * return SchemeLinkController.getInstance();
			 */
			if(s.equals("SchemeCableLink"))
				return null;
			/**
			 * @todo write SchemeCableLinkController
			 * return SchemeCableLinkController.getInstance();
			 */
			if(s.equals("SchemePath"))
				return null;
			/**
			 * @todo write SchemePathController
			 * return SchemePathController.getInstance();
			 */
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
			if(s.equals("MeasurementPortType"))
				return MeasurementPortTypeController.getInstance();
			if(s.equals("MeasurementType"))
				return MeasurementTypeController.getInstance();
			if(s.equals("schemeTypes"))
				return null;
		}
		else if (node.getUserObject() instanceof Type)
			return SchemeController.getInstance();
		else if (node.getUserObject() instanceof SchemeProtoGroup)
		{
			if (((SchemeProtoGroup)node.getUserObject()).schemeProtoGroups().length != 0)
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
		else if (node.getUserObject() instanceof Scheme)
			return SchemeController.getInstance();
		else if (node.getUserObject() instanceof SchemeElement)
			return null;
			/**
			 * @todo write SchemeElementController
			 * return SchemeElementController.getInstance();
			 */
		return null;
	}

	public List getChildNodes(StorableObjectTreeNode node)
	{
		List vec = new ArrayList();
		if(node.getUserObject() instanceof String)
		{
			String s = (String )node.getUserObject();
			if(s.equals("root"))
			{
				vec.add(new StorableObjectTreeNode("configure", LangModelConfig.getString("label_configuration"),
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				vec.add(new StorableObjectTreeNode ("SchemeProtoGroup", "Компоненты сети", 
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				StorableObjectTreeNode sch = new StorableObjectTreeNode ("schemeTypes", "Схемы", 
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif")));
			
				vec.add(sch);
//				registerSearchableNode(Scheme.typ, sch);
			}
			else if(s.equals("configure"))
			{
				vec.add(new StorableObjectTreeNode("netdirectory", LangModelConfig.getString("menuNetDirText"),
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				vec.add(new StorableObjectTreeNode("jdirectory", LangModelConfig.getString("menuJDirText"),
						new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
			}
			else if(s.equals("netdirectory"))
			{
				vec.add(new StorableObjectTreeNode("LinkType", LangModelConfig.getString("menuNetDirLinkText")));
				vec.add(new StorableObjectTreeNode("CableLinkType", LangModelConfig.getString("menuNetDirCableText")));
				vec.add(new StorableObjectTreeNode("PortType", LangModelConfig.getString("menuNetDirPortText")));
			}
			else if(s.equals("jdirectory"))
			{
				vec.add(new StorableObjectTreeNode("MeasurementType", LangModelConfig.getString("MeasurementType")));
				vec.add(new StorableObjectTreeNode("MeasurementPortType", LangModelConfig.getString("menuJDirAccessPointText")));
//				vec.add(new StorableObjectTreeNode("TransmissionPathType", LangModelConfig.getString("menuJDirPathText"), true));
			}
			else if(s.equals("schemeTypes"))
			{
				for (int i = 0; i < schemeTypes.length; i++)
				{
					String type;
					switch (schemeTypes[i].value()) {
						case Type._NETWORK:
							type = LangModelSchematics.getString("NETWORK");
							break;
						case Type._CABLE_SUBNETWORK:
							type = LangModelSchematics.getString("CABLE_SUBNETWORK");
							break;
						default:
							type = LangModelSchematics.getString("BUILDING");
					}

					vec.add(new StorableObjectTreeNode(schemeTypes[i], type,
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"))));
				}
			}
			else if (s.equals("LinkType")) {
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.LINKTYPE_ENTITY_CODE);

					Collection linkTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = linkTypes.iterator(); it.hasNext(); ) {
						LinkType type = (LinkType)it.next();
						StorableObjectTreeNode n = new StorableObjectTreeNode(type, type.getName(), true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("CableLinkType"))
			{
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.CABLELINKTYPE_ENTITY_CODE);

					Collection linkTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = linkTypes.iterator(); it.hasNext(); ) {
						CableLinkType type = (CableLinkType)it.next();
						StorableObjectTreeNode n = new StorableObjectTreeNode(type, type.getName(), true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("PortType")) {
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.PORTTYPE_ENTITY_CODE);
					Collection portTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = portTypes.iterator(); it.hasNext(); ) {
						PortType type = (PortType)it.next();
						StorableObjectTreeNode n = new StorableObjectTreeNode(type, type.getName(), true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("TransmissionPathType")) {
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.TRANSPATHTYPE_ENTITY_CODE);
					Collection pathTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = pathTypes.iterator(); it.hasNext(); ) {
						TransmissionPathType type = (TransmissionPathType)it.next();
						StorableObjectTreeNode n = new StorableObjectTreeNode(type, type.getName(), true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("MeasurementPortType")) {
				try {
					EquivalentCondition condition = new EquivalentCondition(ObjectEntities.MEASUREMENTPORTTYPE_ENTITY_CODE);
					Collection pathTypes = ConfigurationStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = pathTypes.iterator(); it.hasNext(); ) {
						MeasurementPortType type = (MeasurementPortType)it.next();
						StorableObjectTreeNode n = new StorableObjectTreeNode(type, type.getName(), true);
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
						StorableObjectTreeNode n = new StorableObjectTreeNode(type, type.getDescription(), true);
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
					List groups = SchemeStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = groups.iterator(); it.hasNext(); ) {
						SchemeProtoGroup group = (SchemeProtoGroup)it.next();
						StorableObjectTreeNode n = new StorableObjectTreeNode(group, group.name(), true);
						vec.add(n);
					}
				}
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			}
			else if (s.equals("Scheme"))
			{
				Scheme parent = (Scheme)((StorableObjectTreeNode)node.getParent()).getUserObject();
				List ds = new LinkedList();
				for (int i = 0; i < parent.schemeElements().length; i++)
				{
					SchemeElement el = parent.schemeElements()[i];
					if (el.internalScheme() != null)
						ds.add(el.internalScheme());
				}
				if (ds.size() > 0)
				{
					for(Iterator it = ds.iterator(); it.hasNext();)
					{
						Scheme sch = (Scheme)it.next();
						vec.add(new StorableObjectTreeNode(sch, sch.name(), 
							new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")), false));
					}
				}
			}
			else if (s.equals("SchemeElement"))
			{
				Object parent = ((StorableObjectTreeNode)node.getParent()).getUserObject();
				List ds = new LinkedList();
				if (parent instanceof Scheme)
				{
					Scheme scheme = (Scheme)parent;
					for (int i = 0; i < scheme.schemeElements().length; i++)
					{
						SchemeElement element = scheme.schemeElements()[i];
						if (element.internalScheme() == null)
							ds.add(element);
					}
				}
				else if (parent instanceof SchemeElement)
				{
					SchemeElement el = (SchemeElement)parent;
					ds.addAll(Arrays.asList(el.schemeElements()));
				}
				if (ds.size() > 0)
				{
					for(Iterator it = ds.iterator(); it.hasNext();)
					{
						SchemeElement element = (SchemeElement)it.next();
						if (element.schemeLinks().length != 0 || element.schemeElements().length != 0)
							vec.add(new StorableObjectTreeNode(element, element.name(), false));
						else
							vec.add(new StorableObjectTreeNode(element, element.name(), true));
					}
				}
			}
			else if (s.equals("SchemeLink"))
			{
				Object parent = ((StorableObjectTreeNode)node.getParent()).getUserObject();
				if (parent instanceof Scheme)
				{
					Scheme scheme = (Scheme)parent;
					for(int i = 0; i < scheme.schemeLinks().length; i++)
					{
						SchemeLink link = scheme.schemeLinks()[i];
						vec.add(new StorableObjectTreeNode(link, link.name(), true));
					}
				}
				else if (parent instanceof SchemeElement)
				{
					SchemeElement el = (SchemeElement)parent;
					for(int i = 0; i < el.schemeLinks().length; i++)
					{
						SchemeLink link = el.schemeLinks()[i];
						vec.add(new StorableObjectTreeNode(link, link.name(), true));
					}
				}
			}
			else if (s.equals("SchemeCableLink"))
			{
				Scheme parent = (Scheme)((StorableObjectTreeNode)node.getParent()).getUserObject();
				for(int i = 0; i < parent.schemeCableLinks().length; i++)
				{
					SchemeCableLink link = parent.schemeCableLinks()[i];
					vec.add(new StorableObjectTreeNode(link, link.name(), true));
				}
			}
			else if (s.equals("SchemePath"))
			{
				Scheme parent = (Scheme)((StorableObjectTreeNode)node.getParent()).getUserObject();
				for(int i = 0; i < parent.schemeMonitoringSolution().schemePaths().length; i++)
				{
					SchemePath path = parent.schemeMonitoringSolution().schemePaths()[i];
					vec.add(new StorableObjectTreeNode(path, path.name(), true));
				}
			}
		}
		else
		{
			if(node.getUserObject() instanceof Type)
			{
				Type type = (Type)node.getUserObject();
				TypicalCondition condition = new TypicalCondition(
						String.valueOf(type.value()),
						OperationSort.OPERATION_EQUALS,
						ObjectEntities.SCHEME_ENTITY_CODE,
						com.syrus.AMFICOM.scheme.SchemeController.COLUMN_TYPE);
				try {
					List schemes = SchemeStorableObjectPool.getStorableObjectsByCondition(condition, true);

					for (Iterator it = schemes.iterator(); it.hasNext(); ) {
						Scheme sc = (Scheme)it.next();
						vec.add(new StorableObjectTreeNode(sc, sc.name(),
								new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif"))));
					}
				}
				catch (ApplicationException ex1) {
					ex1.printStackTrace();
				}
			}


			if(node.getUserObject() instanceof SchemeProtoGroup)
			{
				SchemeProtoGroup parent_group = (SchemeProtoGroup)node.getUserObject();
				for (int i = 0; i < parent_group.schemeProtoGroups().length; i++)
				{
					SchemeProtoGroup map_group = parent_group.schemeProtoGroups()[i];
					ImageIcon icon;
					if (map_group.symbol() == null)
						icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
					else
						icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(map_group.symbolImpl().getImage())
																 .getScaledInstance(16, 16, Image.SCALE_SMOOTH));

					vec.add(new StorableObjectTreeNode(map_group, map_group.name(), icon,
							map_group.schemeProtoGroups().length == 0 && map_group.schemeProtoElements().length == 0));
				}
				if (vec.isEmpty())
				{
					for (int i = 0; i < parent_group.schemeProtoElements().length; i++)
					{
						SchemeProtoElement proto = parent_group.schemeProtoElements()[i];
//						proto.scheme_proto_group = parent_group;
						vec.add(new StorableObjectTreeNode(proto, proto.name().length() == 0 ? "Без названия" : proto.name(), true));
					}
				}
			}
			else if(node.getUserObject() instanceof Scheme)
			{
				Scheme s = (Scheme)node.getUserObject();
				if (s.schemeElements().length != 0)
				{
					boolean has_schemes = false;
					boolean has_elements = false;
					for (int i = 0; i < s.schemeElements().length; i++)
					{
						SchemeElement el = s.schemeElements()[i];
						if (el.internalScheme() == null)
						{
							has_elements = true;
							break;
						}
					}
					for (int i = 0; i < s.schemeElements().length; i++)
					{
						SchemeElement el = s.schemeElements()[i];
						if (el.internalScheme() != null)
						{
							has_schemes = true;
							break;
						}
					}
					if (has_schemes)
						vec.add(new StorableObjectTreeNode("Scheme", "Вложенные схемы", true));
					if (has_elements)
						vec.add(new StorableObjectTreeNode("SchemeElement", "Узлы", true));
				}
				if (s.schemeLinks().length != 0)
					vec.add(new StorableObjectTreeNode("SchemeLink", "Линии", true));
				if (s.schemeCableLinks().length != 0)
					vec.add(new StorableObjectTreeNode("SchemeCableLink", "Кабели", true));
				if (s.schemeMonitoringSolution().schemePaths().length != 0)
					vec.add(new StorableObjectTreeNode("SchemePath", "Пути", true));
			}
			else if(node.getUserObject() instanceof SchemeElement)
			{
				SchemeElement schel = (SchemeElement)node.getUserObject();
				if (schel.internalScheme() != null)
				{
					Scheme scheme = schel.internalScheme();
					for (int i = 0; i < scheme.schemeElements().length; i++)
					{
						SchemeElement element = scheme.schemeElements()[i];
						if (element.internalScheme() == null)
						{
							if (element.schemeLinks().length != 0 || element.schemeElements().length != 0)
								vec.add(new StorableObjectTreeNode(element, element.name(), false));
							else
								vec.add(new StorableObjectTreeNode(element, element.name(), true));
						}
						else
							vec.add(new StorableObjectTreeNode(element, element.internalScheme().name(), 
									new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/scheme.gif")), false));
					}
				}
				else
				{
					if (schel.schemeElements().length != 0)
						vec.add(new StorableObjectTreeNode("SchemeElement", "Вложенные элементы", true));
				 if (schel.schemeLinks().length != 0)
						vec.add(new StorableObjectTreeNode("SchemeLink", "Линии", true));
				}
			}
		}
		return vec;
	}
}
