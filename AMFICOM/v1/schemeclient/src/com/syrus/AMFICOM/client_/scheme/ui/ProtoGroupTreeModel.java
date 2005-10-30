/*-
 * $Id: ProtoGroupTreeModel.java,v 1.11 2005/10/30 15:20:54 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.swing.UIManager;

import com.syrus.AMFICOM.client.UI.VisualManager;
import com.syrus.AMFICOM.client.UI.tree.PopulatableIconedNode;
import com.syrus.AMFICOM.client.UI.tree.VisualManagerFactory;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client_.scheme.SchemeObjectsFactory;
import com.syrus.AMFICOM.filter.UI.FiltrableIconedNode;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.logic.AbstractChildrenFactory;
import com.syrus.AMFICOM.logic.Item;
import com.syrus.AMFICOM.resource.LangModelScheme;
import com.syrus.AMFICOM.resource.SchemeResourceKeys;
import com.syrus.AMFICOM.scheme.SchemeProtoElement;
import com.syrus.AMFICOM.scheme.SchemeProtoGroup;
import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/10/30 15:20:54 $
 * @module schemeclient
 */

public class ProtoGroupTreeModel extends AbstractChildrenFactory implements VisualManagerFactory {
	ApplicationContext aContext;
	private PopulatableIconedNode root;
	
	public ProtoGroupTreeModel(ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	public static final Object getRootObject() {
		return SchemeResourceKeys.SCHEME_PROTO_GROUP;
	}
	
	public Item getRoot() {
		if (this.root == null) {
			this.root = new PopulatableIconedNode();
			this.root.setChildrenFactory(this);
			this.root.setObject(SchemeResourceKeys.SCHEME_PROTO_GROUP);
			this.root.setName(LangModelScheme.getString(SchemeResourceKeys.SCHEME_PROTO_GROUP));
			this.root.setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
		}
		return this.root;
	}
	
	public VisualManager getVisualManager(Item node) {
		Object object = node.getObject();
		if (object instanceof String) {
			String s = (String)object;
			if (s.equals(SchemeResourceKeys.SCHEME_PROTO_GROUP))
				return SchemeProtoGroupPropertiesManager.getInstance(this.aContext);
			if (s.equals(SchemeResourceKeys.SCHEME_PROTO_ELEMENT))
				return SchemeProtoElementPropertiesManager.getInstance(this.aContext);
			// for any other strings return null Manager
			return null;
		}
		if (object instanceof SchemeProtoGroup)
			return SchemeProtoGroupPropertiesManager.getInstance(this.aContext);
		if (object instanceof SchemeProtoElement)
			return SchemeProtoElementPropertiesManager.getInstance(this.aContext);
		throw new UnsupportedOperationException("Unknown object " + object); //$NON-NLS-1$
	}
	

	public void populate(Item node) {
				
		if (node.getObject() instanceof String) {
			String s = (String) node.getObject();
			if (s.equals(SchemeResourceKeys.SCHEME_PROTO_GROUP)) {
				addToProtoGroup(node);
			} else if (s.equals(SchemeResourceKeys.SCHEME_PROTO_ELEMENT)) {
//				addToProtoElement(node);
			} 
		} else if (node.getObject() instanceof SchemeProtoGroup) {
			addToProtoGroup(node);
		} else if (node.getObject() instanceof SchemeProtoElement) {
//			addToProtoElement(node);
		}
	}

	private void addToProtoGroup(Item node) {
		Collection<Object> contents = super.getChildObjects(node);
		try {
			// first add ProtoGroups (always)
			Identifier parentId = (node.equals(this.root) ? Identifier.VOID_IDENTIFIER : ((SchemeProtoGroup)node.getObject()).getId());
			StorableObjectCondition condition1 = new LinkedIdsCondition(parentId, ObjectEntities.SCHEMEPROTOGROUP_CODE); 
			Collection<StorableObject> groups = StorableObjectPool.getStorableObjectsByCondition(condition1, true);
			groups.remove(SchemeObjectsFactory.stubProtoGroup);

			final Collection<StorableObject> children;
			//	next add ProtoElements according to FilteredCondition
			if (node instanceof FiltrableIconedNode) {
				FiltrableIconedNode filtrableNode = (FiltrableIconedNode)node;
				StorableObjectCondition condition2 = filtrableNode.getResultingCondition();
				Collection<SchemeProtoElement> protos = StorableObjectPool.getStorableObjectsByCondition(condition2, true);
				children = new ArrayList<StorableObject>(groups.size() + protos.size());
				children.addAll(groups);
				children.addAll(protos);
			} else {
				children = groups;
			}

			Collection toAdd = super.getObjectsToAdd(children, contents);
			Collection toRemove = super.getItemsToRemove(children, node.getChildren());
			for (Iterator it = toRemove.iterator(); it.hasNext();) {
				Item child = (Item)it.next();
				child.setParent(null);
			}
			for (Iterator it = toAdd.iterator(); it.hasNext();) {
				Object childObject = it.next();
				if (childObject instanceof SchemeProtoGroup) {
					SchemeProtoGroup protoGroup = (SchemeProtoGroup)childObject;
					FiltrableIconedNode child = new FiltrableIconedNode();
					child.setChildrenFactory(this);
					child.setObject(protoGroup);
					child.setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
					child.setDefaultCondition(new LinkedIdsCondition(protoGroup.getId(), ObjectEntities.SCHEMEPROTOELEMENT_CODE));
					node.addChild(child);					
				} else {
					SchemeProtoElement protoElement = (SchemeProtoElement)childObject;
					FiltrableIconedNode child = new FiltrableIconedNode();
					child.setChildrenFactory(this);
					child.setObject(protoElement);
					child.setIcon(null);
					child.setDefaultCondition(new LinkedIdsCondition(protoElement.getId(), ObjectEntities.SCHEMEPROTOELEMENT_CODE));
					node.addChild(child);
					
					child.setCanHaveChildren(false);
				}
			}
		} 
		catch (ApplicationException ex) {
			assert Log.errorMessage(ex);
		}
	}

	
	/*private void addToProtoElement(Item node) {
		Collection contents = CommonUIUtilities.getChildObjects(node);
		SchemeProtoElement proto = (SchemeProtoElement)node.getObject();

		Collection children = new LinkedList();
		if (!proto.getSchemeProtoElements().isEmpty())
			children.add(SchemeResourceKeys.SCHEME_PROTO_ELEMENT);
		if (!proto.getSchemeLinks().isEmpty())
			children.add(SchemeResourceKeys.SCHEME_LINK);
		if (!proto.getSchemeDevices().isEmpty())
			children.add(SchemeResourceKeys.SCHEME_DEVICE);

		if (!proto.getSchemePortsRecursively().isEmpty())
			children.add(SchemeResourceKeys.SCHEME_PORT);
		if (!proto.getSchemeCablePortsRecursively().isEmpty())
			children.add(SchemeResourceKeys.SCHEME_CABLE_PORT);
		
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
			child.setName(LangModelScheme.getString(SchemeResourceKeys.SCHEME_PROTO_ELEMENT));
			child.setIcon(UIManager.getIcon(SchemeResourceKeys.ICON_CATALOG));
			StorableObjectCondition condition2 = new LinkedIdsCondition(proto.getId(), ObjectEntities.SCHEMEPROTOELEMENT_CODE);
			child.setDefaultCondition(condition2);
			node.addChild(child);
		}
		if (toAdd.contains(SchemeResourceKeys.SCHEME_LINK)) {
			FiltrableIconedNode child = new FiltrableIconedNode();
			child.setChildrenFactory(this);
			child.setObject(SchemeResourceKeys.SCHEME_LINK);
			child.setName(LangModelScheme.getString(SchemeResourceKeys.SCHEME_LINK));
			StorableObjectCondition condition2 = new LinkedIdsCondition(proto.getId(), ObjectEntities.SCHEMELINK_CODE);
			child.setDefaultCondition(condition2);
			node.addChild(child);
		}
		if (toAdd.contains(SchemeResourceKeys.SCHEME_LINK)) {
			FiltrableIconedNode child = new FiltrableIconedNode();
			child.setChildrenFactory(this);
			child.setObject(SchemeResourceKeys.SCHEME_LINK);
			child.setName(LangModelScheme.getString(SchemeResourceKeys.SCHEME_LINK));
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
	}*/
}
