/*-
 * $$Id: CableBindingController.java,v 1.36 2006/06/15 06:40:16 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map.props;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;
import com.syrus.AMFICOM.map.AbstractNode;
import com.syrus.AMFICOM.map.PhysicalLink;
import com.syrus.AMFICOM.mapview.CablePath;
import com.syrus.AMFICOM.mapview.UnboundLink;
import com.syrus.AMFICOM.scheme.CableChannelingItem;
import com.syrus.util.Log;
import com.syrus.util.Wrapper;

/**
 * @version $Revision: 1.36 $, $Date: 2006/06/15 06:40:16 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class CableBindingController implements Wrapper<PhysicalLink> {
	public static final String KEY_START_NODE = "startnode"; //$NON-NLS-1$
	public static final String KEY_START_SPARE = "startspare"; //$NON-NLS-1$
	public static final String KEY_LINK = "link"; //$NON-NLS-1$
	public static final String KEY_END_SPARE = "endspare"; //$NON-NLS-1$
	public static final String KEY_END_NODE = "endnode"; //$NON-NLS-1$

	private static CableBindingController instance;

	private List<String> keys;
	private String[] keysArray;
	
	CablePath cablePath;

	private CableBindingController() {
		// empty private constructor
		this.keysArray = new String[] { 
				KEY_START_NODE, 
				KEY_START_SPARE, 
				KEY_LINK, 
				KEY_END_SPARE, 
				KEY_END_NODE };
	
		this.keys = Collections.unmodifiableList(Arrays.asList(this.keysArray));
	}

	public static CableBindingController getInstance() {
		if(instance == null)
			instance = new CableBindingController();
		return instance;
	}

	public List<String> getKeys() {
		return this.keys;
	}

	public String[] getKeysArray() {
		return this.keysArray;
	}

	public String getName(final String key) {
		String name = null;
		if(key.equals(KEY_START_NODE)) {
			name = I18N.getString(MapEditorResourceKeys.LABEL_NODE);
		}
		else if(key.equals(KEY_START_SPARE)) {
			name = I18N.getString(MapEditorResourceKeys.LABEL_START_SPARE);
		}
		else if(key.equals(KEY_LINK)) {
			name = I18N.getString(MapEditorResourceKeys.LABEL_TUNNEL);
		}
		else if(key.equals(KEY_END_SPARE)) {
			name = I18N.getString(MapEditorResourceKeys.LABEL_END_SPARE);
		}
		else if(key.equals(KEY_END_NODE)) {
			name = I18N.getString(MapEditorResourceKeys.LABEL_NODE);
		}
		return name;
	}

	public Object getValue(final PhysicalLink link, final String key) {
		Object result = null;
		try {
			CableChannelingItem cci = this.cablePath.getFirstCCI(link);
			if(key.equals(KEY_START_NODE)) {
				AbstractNode mne = this.cablePath.getStartNode();
				result = (mne == null) ? "" : mne.getName(); //$NON-NLS-1$
			} else if(key.equals(KEY_START_SPARE)) {
				result = (link instanceof UnboundLink) ? "" : String.valueOf(cci.getStartSpare()); //$NON-NLS-1$
			} else if(key.equals(KEY_LINK)) {
				result = (link instanceof UnboundLink) ? "" : link.getName(); //$NON-NLS-1$
			} else if(key.equals(KEY_END_SPARE)) {
				result = (link instanceof UnboundLink) ? "" : String.valueOf(cci.getEndSpare()); //$NON-NLS-1$
			} else if(key.equals(KEY_END_NODE)) {
				AbstractNode mne = this.cablePath.getEndNode();
				result = (mne == null) ? "" : mne.getName(); //$NON-NLS-1$
			}
		} catch(Exception e) {
			Log.errorMessage(e);
		}
		return result;
	}

	public boolean isEditable(final String key) {
		boolean editable = false;
		if(key.equals(KEY_START_SPARE) || key.equals(KEY_END_SPARE)) {
			editable = true;
		}
		return editable;
	}

	public void setValue(PhysicalLink link, final String key, final Object value) {
		try {
			CableChannelingItem cci = this.cablePath.getFirstCCI(link);
			if(key.equals(KEY_START_SPARE)) {
				if(cci.getPhysicalLink() != null)
					cci.setStartSpare(Double.parseDouble((String) value));
			} else if(key.equals(KEY_END_SPARE)) {
				if(cci.getPhysicalLink() != null)
					cci.setEndSpare(Double.parseDouble((String) value));
			}
		} catch(Exception e) {
			Log.errorMessage(e);
		}
	}

	public String getKey(final int index) {
		return this.keys.get(index);
	}

	public Object getPropertyValue(final String key) {
		Object result = ""; //$NON-NLS-1$
		return result;
	}

	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
		//empty
	}

	public Class<?> getPropertyClass(String key) {
		Class<?> clazz = String.class;
		return clazz;
	}

	public void setCablePath(CablePath cablePath) {
		this.cablePath = cablePath;
	}
}
