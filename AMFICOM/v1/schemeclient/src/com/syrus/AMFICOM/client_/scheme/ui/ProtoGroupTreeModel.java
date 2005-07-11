/*-
 * $Id: ProtoGroupTreeModel.java,v 1.1 2005/07/11 12:31:39 stas Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.CommonUIUtilities;
import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.VisualManagerFactory;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.filter.UI.FiltrableIconedNode;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.ChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;

/**
 * @author $Author: stas $
 * @version $Revision: 1.1 $, $Date: 2005/07/11 12:31:39 $
 * @module schemeclient_v1
 */

public class ProtoGroupTreeModel implements ChildrenFactory, VisualManagerFactory {
	ApplicationContext aContext;
	private FiltrableIconedNode root;
	
	public ProtoGroupTreeModel(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	public Item getRoot() {
		if (root == null) {
			root = new FiltrableIconedNode();
			root.setChildrenFactory(this);
			root.setObject(SchemeResourceKeys.SCHEME_PROTO_GROUP);
			root.setName(LangModelScheme.getString(SchemeResourceKeys.SCHEME_PROTO_GROUP));
			root.setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
			root.setDefaultCondition(new LinkedIdsCondition(Identifier.VOID_IDENTIFIER, ObjectEntities.SCHEMEPROTOGROUP_CODE));
		}
		return root;
	}
	
	public VisualManager getVisualManager(Item node) {
		Object object = node.getObject();
		if (object instanceof String) {
			String s = (String)object;
			if (s.equals(SchemeResourceKeys.SCHEME_PROTO_GROUP))
				return SchemeProtoGroupPropertiesManager.getInstance(aContext);
			if (s.equals(SchemeResourceKeys.SCHEME_PROTO_ELEMENT))
				return SchemeProtoElementPropertiesManager.getInstance(aContext);
			// for any other strings return null Manager
			return null;
		}
		if (object instanceof SchemeProtoGroup)
			return SchemeProtoGroupPropertiesManager.getInstance(aContext);
		if (object instanceof SchemeProtoElement)
			return SchemeProtoElementPropertiesManager.getInstance(aContext);
		throw new UnsupportedOperationException("Unknown object " + object); //$NON-NLS-1$
	}
	

	public void populate(Item node) {
				
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(SchemeResourceKeys.SCHEME_PROTO_GROUP)) {
				createProtoGroup((FiltrableIconedNode)node);
			} else if (s.equals(SchemeResourceKeys.SCHEME_PROTO_ELEMENT)) {
				addToProtoElement(node);
			} 
		} else if (node.getObject() instanceof SchemeProtoGroup) {
			createProtoGroup((FiltrableIconedNode)node);
		}
	}

	private void createProtoGroup(FiltrableIconedNode node) {
		Collection contents = CommonUIUtilities.getChildObjects(node);
		try {
			StorableObjectCondition condition = node.getResultingCondition();
			Collection groups = StorableObjectPool.getStorableObjectsByCondition(condition, true);
		
			Collection toAdd = CommonUIUtilities.getObjectsToAdd(groups, contents);
			Collection toRemove = CommonUIUtilities.getItemsToRemove(groups, node.getChildren());
			for (Iterator it = toRemove.iterator(); it.hasNext();) {
				Item child = (Item)it.next();
				child.setParent(null);
			}
			for (Iterator it = toAdd.iterator(); it.hasNext();) {
				SchemeProtoGroup protoGroup = (SchemeProtoGroup)it.next();
				FiltrableIconedNode child = new FiltrableIconedNode();
				child.setChildrenFactory(this);
				child.setObject(protoGroup);
				child.setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
				StorableObjectCondition condition2;
				
				if (!protoGroup.getSchemeProtoGroups().isEmpty())
					condition2 = new LinkedIdsCondition(protoGroup.getId(), ObjectEntities.SCHEMEPROTOGROUP_CODE);
				else
					condition2 = new LinkedIdsCondition(protoGroup.getId(), ObjectEntities.SCHEMEPROTOELEMENT_CODE);
				child.setDefaultCondition(condition2);
				node.addChild(child);
			}
		} 
		catch (ApplicationException ex) {
			ex.printStackTrace();
		}
	}

	
	private void addToProtoElement(Item node) {
		Collection contents = CommonUIUtilities.getChildObjects(node);
		SchemeProtoElement proto = (SchemeProtoElement)node.getObject();

		Collection children = new LinkedList();
		if (!proto.getSchemeProtoElements().isEmpty())
			children.add(SchemeResourceKeys.SCHEME_PROTO_ELEMENT);
		if (!proto.getSchemeLinks().isEmpty())
			children.add(SchemeResourceKeys.SCHEME_LINK);
		if (!proto.getSchemeDevices().isEmpty())
			children.add(SchemeResourceKeys.SCHEME_DEVICE);
		
		Collection toAdd = CommonUIUtilities.getObjectsToAdd(children, contents);
		Collection toRemove = CommonUIUtilities.getItemsToRemove(children, node.getChildren());
		
		for (Iterator it = toRemove.iterator(); it.hasNext();) {
			Item child = (Item)it.next();
			child.setParent(null);
		}
		if (toAdd.contains(SchemeResourceKeys.SCHEME_PROTO_ELEMENT)) {
			FiltrableIconedNode child = new FiltrableIconedNode();
			child.setChildrenFactory(this);
			child.setObject(SchemeResourceKeys.SCHEME_PROTO_ELEMENT);
			child.setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
			StorableObjectCondition condition2 = new LinkedIdsCondition(proto.getId(), ObjectEntities.SCHEMEPROTOELEMENT_CODE);
			child.setDefaultCondition(condition2);
			node.addChild(child);
		}
		if (toAdd.contains(SchemeResourceKeys.SCHEME_DEVICE)) {
			FiltrableIconedNode child = new FiltrableIconedNode();
			child.setChildrenFactory(this);
			child.setObject(SchemeResourceKeys.SCHEME_DEVICE);
			StorableObjectCondition condition2 = new LinkedIdsCondition(proto.getId(), ObjectEntities.SCHEMEDEVICE_CODE);
			child.setDefaultCondition(condition2);
			node.addChild(child);
		}
		if (toAdd.contains(SchemeResourceKeys.SCHEME_LINK)) {
			FiltrableIconedNode child = new FiltrableIconedNode();
			child.setChildrenFactory(this);
			child.setObject(SchemeResourceKeys.SCHEME_LINK);
			StorableObjectCondition condition2 = new LinkedIdsCondition(proto.getId(), ObjectEntities.SCHEMEDEVICE_CODE);
			child.setDefaultCondition(condition2);
			node.addChild(child);
		}
		
		for (Iterator it = toAdd.iterator(); it.hasNext();) {
			String objToAdd = (String)it.next();
			FiltrableIconedNode child = new FiltrableIconedNode();
			child.setChildrenFactory(this);
			child.setObject(objToAdd);
			child.setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
			StorableObjectCondition condition2 = ((FiltrableIconedNode)node).getDefaultCondition();
			child.setDefaultCondition(condition2);
			node.addChild(child);
		}
	}
}
