/**
 * $Id: MapRemoveWrapper.java,v 1.8.2.1 2006/05/18 17:50:00 bass Exp $
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.map;

import java.util.HashSet;
import java.util.Set;

import javax.swing.JOptionPane;

import com.syrus.AMFICOM.client.UI.dialogs.DefaultStorableObjectRemoveWrapper;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.map.Map;
import com.syrus.AMFICOM.mapview.MapView;
import com.syrus.AMFICOM.scheme.Scheme;
import com.syrus.AMFICOM.scheme.SchemeCableLink;
import com.syrus.util.Log;

public class MapRemoveWrapper extends DefaultStorableObjectRemoveWrapper {

	static MapRemoveWrapper instance;
	
	protected MapRemoveWrapper() {
		super();
	}
	
	public static MapRemoveWrapper getInstance() {
		if(instance == null) {
			instance = new MapRemoveWrapper();
		}
		return instance;
	}
	
	@Override
	public boolean remove(Object object) {
		try {
//* TODO uncomment when conditions are working at CB server!!!
			Map map = (Map) object;
			LinkedIdsCondition condition = new LinkedIdsCondition(map.getId(), ObjectEntities.MAP_CODE);
			Set<Map> maps = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if(!maps.isEmpty()) {
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(), 
						I18N.getString(MapEditorResourceKeys.ERROR_LINKED_OBJECTS_EXIST_CANNOT_REMOVE) + " - " + maps.iterator().next().getName(), 
						I18N.getString(MapEditorResourceKeys.ERROR), 
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			condition = new LinkedIdsCondition(map.getId(), ObjectEntities.MAPVIEW_CODE);
			Set<MapView> mapViews = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if(!mapViews.isEmpty()) {
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(), 
						I18N.getString(MapEditorResourceKeys.ERROR_LINKED_OBJECTS_EXIST_CANNOT_REMOVE) + " - " + mapViews.iterator().next().getName(), 
						I18N.getString(MapEditorResourceKeys.ERROR), 
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			condition = new LinkedIdsCondition(Identifier.createIdentifiers(map.getSiteNodes()), ObjectEntities.MAP_CODE);
			maps = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if(!maps.isEmpty()) {
				JOptionPane.showMessageDialog(
						Environment.getActiveWindow(), 
						I18N.getString(MapEditorResourceKeys.ERROR_LINKED_OBJECTS_EXIST_CANNOT_REMOVE) + " - " + maps.iterator().next().getName(), 
						I18N.getString(MapEditorResourceKeys.ERROR), 
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
			condition = new LinkedIdsCondition(map.getId(), ObjectEntities.SCHEME_CODE);
			Set<Scheme> schemes = StorableObjectPool.getStorableObjectsByCondition(condition, true);
			if(!schemes.isEmpty()) {
				// отвязываем схемы
				// и стираем все CableChannelingItems на отвязанных схемах
				Set<Identifiable> toDelete = new HashSet<Identifiable>();
				for (Scheme scheme : schemes) {
					 for (SchemeCableLink scl : scheme.getSchemeCableLinks()) {
						 toDelete.addAll(scl.getPathMembers());
					 }
				}
				StorableObjectPool.delete(toDelete);
				StorableObjectPool.flush(toDelete, LoginManager.getUserId(), false);
				StorableObjectPool.flush(schemes, LoginManager.getUserId(), false);
//				JOptionPane.showMessageDialog(
//						Environment.getActiveWindow(), 
//						I18N.getString(MapEditorResourceKeys.ERROR_LINKED_OBJECTS_EXIST_CANNOT_REMOVE) + " - " + schemes.iterator().next().getName(), 
//						I18N.getString(MapEditorResourceKeys.ERROR), 
//						JOptionPane.ERROR_MESSAGE);
//				return false;
			}
			return super.remove(object);
		} catch(ApplicationException e) {
			Log.errorMessage(e);
		}
		return false;
	}

}
