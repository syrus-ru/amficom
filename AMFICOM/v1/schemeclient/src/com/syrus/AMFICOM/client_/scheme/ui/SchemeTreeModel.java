/*
 * $Id: SchemeTreeModel.java,v 1.25 2005/07/11 12:31:40 stas Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

/**
 * @author $Author: stas $
 * @version $Revision: 1.25 $, $Date: 2005/07/11 12:31:40 $
 * @module schemeclient_v1
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.UI.tree.VisualManagerFactory;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.configuration.ui.ConfigurationTreeModel;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.corba.IdlSchemePackage.Kind;

public class SchemeTreeModel implements ChildrenFactory, VisualManagerFactory {
	ApplicationContext aContext;
	ConfigurationTreeModel configurationTreeModel;
	ProtoGroupTreeModel protoTreeModel;

	private static Kind[] schemeTypes = new Kind[] { Kind.NETWORK, Kind.BUILDING,
			Kind.CABLE_SUBNETWORK };

	private static String[] schemeTypeNames = new String[] {
			LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE_NETWORK),
			LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE_BUILDING),
			LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE_CABLE) };

	public SchemeTreeModel(ApplicationContext aContext) {
		this.aContext = aContext;
	}

	public VisualManager getVisualManager(Item node) {
		Object object = node.getObject();
		if (object instanceof String) {
			String s = (String)object;
			/*if (s.equals(Constants.SCHEME))
				return SchemeController.getInstance();
			if (s.equals(Constants.SCHEME_ELEMENT))
				return SchemeElementController.getInstance();
			if (s.equals(Constants.SCHEME_LINK))
				return SchemeLinkController.getInstance();
			if (s.equals(Constants.SCHEME_CABLELINK))
				return SchemeCableLinkController.getInstance();
			if (s.equals(Constants.SCHEME_PATH))
				return SchemePathController.getInstance();
			if (s.equals(Constants.SCHEME_PROTO_GROUP))
				return null;*/
			/**
			 * @todo write SchemeProtoGroupController return
			 *       SchemeProtoGroupController.getInstance();
			 */
			if (s.equals(SchemeResourceKeys.SCHEME_TYPE))
				return null;
			// for any other strings return null Manager
			return null;
		}
		if (object instanceof Kind)
				return null;
//		if (object instanceof Kind)
//			return SchemeController.getInstance();
//		if (object instanceof SchemeProtoGroup) {
//			if (!((SchemeProtoGroup) object).getSchemeProtoGroups().isEmpty())
//				return null;
			/**
			 * @todo write SchemeProtoGroupController return
			 *       SchemeProtoGroupController.getInstance();
			 */
//			else
//				return null;
			/**
			 * @todo write SchemeProtoElementController return
			 *       SchemeProtoElementController.getInstance();
			 */
//		}
//		if (object instanceof Scheme)
//			return SchemeController.getInstance();
//		if (object instanceof SchemeElement)
//			return SchemeElementController.getInstance();
		throw new UnsupportedOperationException("Unknown object " + object); //$NON-NLS-1$
	}
	
	Collection getChildObjects(Item node) {
		Collection childObjects = new ArrayList(node.getChildren().size());
		for (Iterator it = node.getChildren().iterator(); it.hasNext();)
			childObjects.add(((Item)it.next()).getObject());
		return childObjects;
	}
	

	

	public void populate(Item node) {
		Collection contents = getChildObjects(node);
		
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(SchemeResourceKeys.ROOT)) {
				createRoot(node, contents);
			} 
			else if (s.equals(SchemeResourceKeys.SCHEME_TYPE)) {
				for (int i = 0; i < schemeTypes.length; i++) {
					if (!contents.contains(schemeTypes[i]))
						node.addChild(new PopulatableIconedNode(this, schemeTypes[i], schemeTypeNames[i], UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG)));
				}
			} 
			/*else if (s.equals(Constants.SCHEME_PROTO_GROUP)) {
				try {
					Identifier domainId = new Identifier(((RISDSessionInfo) aContext
							.getSessionInterface()).getAccessIdentifier().domain_id);
					LinkedIdsCondition condition = new LinkedIdsCondition(domainId,
							ObjectEntities.SCHEMEPROTOGROUP_CODE);
					Set groups = SchemeStorableObjectPool.getStorableObjectsByCondition(
							condition, true);

					for (Iterator it = groups.iterator(); it.hasNext();) {
						SchemeProtoGroup group = (SchemeProtoGroup) it.next();
						if (!contents.contains(group))
							node.addChild(new PopulatableIconedNode(this, group));
					}
				} 
				catch (ApplicationException ex) {
					ex.printStackTrace();
				}
			} 
			else if (s.equals(Constants.SCHEME)) {
				Scheme parent = (Scheme) ((Item) node.getParent()).getObject();
				List ds = new LinkedList();
				for (int i = 0; i < parent.getSchemeElementsAsArray().length; i++) {
					SchemeElement el = parent.getSchemeElementsAsArray()[i];
					if (el.getScheme() != null)
						ds.add(el.getScheme());
				}
				if (ds.size() > 0) {
					for (Iterator it = ds.iterator(); it.hasNext();) {
						Scheme sch = (Scheme) it.next();
						if (!contents.contains(sch))
							node.addChild(new PopulatableIconedNode(this, sch));
					}
				}
			} 
			else if (s.equals(Constants.SCHEME_ELEMENT)) {
				Object parent = ((Item) node.getParent()).getObject();
				List ds = new LinkedList();
				if (parent instanceof Scheme) {
					Scheme scheme = (Scheme) parent;
					for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) {
						SchemeElement element = scheme.getSchemeElementsAsArray()[i];
						if (element.getScheme() == null)
							ds.add(element);
					}
				} 
				else if (parent instanceof SchemeElement) {
					SchemeElement el = (SchemeElement) parent;
					ds.addAll(Arrays.asList(el.getSchemeElementsAsArray()));
				}
				if (ds.size() > 0) {
					for (Iterator it = ds.iterator(); it.hasNext();) {
						SchemeElement element = (SchemeElement) it.next();
						if (element.getSchemeLinksAsArray().length != 0	|| element.getSchemeElementsAsArray().length != 0) {
							if (!contents.contains(element))
								node.addChild(new PopulatableIconedNode(this, element, element.getName(), true));
						} else {
							if (!contents.contains(element))
								node.addChild(new PopulatableIconedNode(this, element, element.getName(), false));
						}
					}
				}
			} 
			else if (s.equals(Constants.SCHEME_LINK)) {
				Object parent = ((Item) node.getParent()).getObject();
				if (parent instanceof Scheme) {
					Scheme scheme = (Scheme) parent;
					for (int i = 0; i < scheme.getSchemeLinksAsArray().length; i++) {
						SchemeLink link = scheme.getSchemeLinksAsArray()[i];
						if (!contents.contains(link))
							node.addChild(new PopulatableIconedNode(this, link, link.getName(), false));
					}
				} 
				else if (parent instanceof SchemeElement) {
					SchemeElement el = (SchemeElement) parent;
					for (int i = 0; i < el.getSchemeLinksAsArray().length; i++) {
						SchemeLink link = el.getSchemeLinksAsArray()[i];
						if (!contents.contains(link))
							node.addChild(new PopulatableIconedNode(this, link, link.getName(), false));
					}
				}
			} 
			else if (s.equals(Constants.SCHEME_CABLELINK)) {
				Scheme parent = (Scheme) ((Item) node.getParent()).getObject();
				for (int i = 0; i < parent.getSchemeCableLinksAsArray().length; i++) {
					SchemeCableLink link = parent.getSchemeCableLinksAsArray()[i];
					if (!contents.contains(link))
						node.addChild(new PopulatableIconedNode(this, link, link.getName(), false));
				}
			} 
			else if (s.equals(Constants.SCHEME_PATH)) {
				Scheme parent = (Scheme) ((Item) node.getParent()).getObject();
				for (int i = 0; i < parent.getCurrentSchemeMonitoringSolution().getSchemePathsAsArray().length; i++) {
					SchemePath path = parent.getCurrentSchemeMonitoringSolution().getSchemePathsAsArray()[i];
					if (!contents.contains(path))
						node.addChild(new PopulatableIconedNode(this, path, path.getName(), false));
				}
			}
		} 
		else {
			if (node.getObject() instanceof Kind) {
				Kind type = (Kind) node.getObject();
				TypicalCondition condition = new TypicalCondition(String.valueOf(type
						.value()), OperationSort.OPERATION_EQUALS,
						ObjectEntities.SCHEME_CODE,
						com.syrus.AMFICOM.scheme.SchemeController.COLUMN_TYPE);
				try {
					Set schemes = SchemeStorableObjectPool
							.getStorableObjectsByCondition(condition, true);

					for (Iterator it = schemes.iterator(); it.hasNext();) {
						Scheme sc = (Scheme) it.next();
						if (!contents.contains(sc))
							node.addChild(new PopulatableIconedNode(this, sc));
					}
				} 
				catch (ApplicationException ex1) {
					ex1.printStackTrace();
				}
			}
			if (node.getObject() instanceof SchemeProtoGroup) {
				SchemeProtoGroup parent_group = (SchemeProtoGroup) node.getObject();
				for (final Iterator schemeProtoGroupIterator = parent_group.getSchemeProtoGroups().iterator(); schemeProtoGroupIterator.hasNext();) {
					final SchemeProtoGroup map_group = (SchemeProtoGroup) schemeProtoGroupIterator.next();
					ImageIcon icon;
					if (map_group.getSymbol() == null) {
						icon = new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/folder.gif"));
					} else {
						icon = new ImageIcon(Toolkit.getDefaultToolkit().createImage(
								map_group.getSymbol().getImage()).getScaledInstance(16, 16,
								Image.SCALE_SMOOTH));
					}
					if (!contents.contains(map_group))
						node.addChild(new PopulatableIconedNode(this, map_group, map_group.getName(), !map_group.getSchemeProtoGroups().isEmpty() || !map_group.getSchemeProtoElements().isEmpty()));
				}
				if (parent_group.getSchemeProtoGroups().isEmpty()) {
					for (final Iterator schemeProtoElementIterator = parent_group.getSchemeProtoElements().iterator(); schemeProtoElementIterator.hasNext();) {
						final SchemeProtoElement schemeProtoElement = (SchemeProtoElement) schemeProtoElementIterator.next();
						// schemeProtoElement.parent(parent_group);
						if (! contents.contains(schemeProtoElement))
							node.addChild(new PopulatableIconedNode(this, schemeProtoElement, schemeProtoElement.getName(), false));
					}
				}
			} 
			else if (node.getObject() instanceof Scheme) {
				Scheme s = (Scheme) node.getObject();
				if (s.getSchemeElementsAsArray().length != 0) {
					boolean has_schemes = false;
					boolean has_elements = false;
					for (int i = 0; i < s.getSchemeElementsAsArray().length; i++) {
						SchemeElement el = s.getSchemeElementsAsArray()[i];
						if (el.getScheme() == null) {
							has_elements = true;
							break;
						}
					}
					for (int i = 0; i < s.getSchemeElementsAsArray().length; i++) {
						SchemeElement el = s.getSchemeElementsAsArray()[i];
						if (el.getScheme() != null) {
							has_schemes = true;
							break;
						}
					}
					if (has_schemes) {
						if (!contents.contains(Constants.SCHEME))
							node.addChild(new PopulatableIconedNode(this, Constants.SCHEME));
					}
					if (has_elements) {
						if (!contents.contains(Constants.SCHEME_ELEMENT))
							node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_ELEMENT));
					}
				}
				if (s.getSchemeLinksAsArray().length != 0) {
					if (!contents.contains(Constants.SCHEME_LINK))
						node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_LINK));
				}
				if (s.getSchemeCableLinksAsArray().length != 0) {
					if (!contents.contains(Constants.SCHEME_CABLELINK))
						node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_CABLELINK));
				}
				if (s.getCurrentSchemeMonitoringSolution().getSchemePathsAsArray().length != 0) {
					if (!contents.contains(Constants.SCHEME_PATH))
						node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_PATH));
				}
			} 
			else if (node.getObject() instanceof SchemeElement) {
				SchemeElement schel = (SchemeElement) node.getObject();
				if (schel.getScheme() != null) {
					Scheme scheme = schel.getScheme();
					for (int i = 0; i < scheme.getSchemeElementsAsArray().length; i++) {
						SchemeElement element = scheme.getSchemeElementsAsArray()[i];
						if (element.getScheme() == null) {
							if (!contents.contains(element))
								node.addChild(new PopulatableIconedNode(this, element, element.getName(), (element.getSchemeLinksAsArray().length != 0 || element.getSchemeElementsAsArray().length != 0)));
						} 
						else {
							if (!contents.contains(element))
								node.addChild(new PopulatableIconedNode(this, element));
						}
					}
				} 
				else {
					if (schel.getSchemeElementsAsArray().length != 0) {
						if (!contents.contains(Constants.SCHEME_ELEMENT))
							node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_ELEMENT));
					}
					if (schel.getSchemeLinksAsArray().length != 0) {
						if (!contents.contains(Constants.SCHEME_LINK))
							node.addChild(new PopulatableIconedNode(this, Constants.SCHEME_LINK));
					}
				}
			}*/
		}
	}
	
	private void createRoot(Item node, Collection contents) {
		if (!contents.contains(SchemeResourceKeys.CONFIGURATION)) {
			if (configurationTreeModel == null) {
				configurationTreeModel = new ConfigurationTreeModel(aContext);
			}
			node.addChild(configurationTreeModel.getRoot());
		}
		if (!contents.contains(SchemeResourceKeys.SCHEME_TYPE))
			node.addChild(new PopulatableIconedNode(this, SchemeResourceKeys.SCHEME_TYPE, LangModelScheme.getString(SchemeResourceKeys.SCHEME_TYPE), UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG)));
		if (!contents.contains(SchemeResourceKeys.SCHEME_PROTO_GROUP)) {
			if (protoTreeModel == null) {
				protoTreeModel = new ProtoGroupTreeModel(aContext);
			}
			node.addChild(protoTreeModel.getRoot());
		}
	}
}
