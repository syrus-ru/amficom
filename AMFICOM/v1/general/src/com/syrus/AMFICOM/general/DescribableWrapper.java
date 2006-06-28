/*-
* $Id: DescribableWrapper.java,v 1.2 2006/03/13 15:54:27 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import java.util.Collections;
import java.util.List;

import com.syrus.util.PropertyChangeException;
import com.syrus.util.Wrapper;


/**
 * @version $Revision: 1.2 $, $Date: 2006/03/13 15:54:27 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module general
 */
public final class DescribableWrapper implements Wrapper<Describable> {

	public static final String COLUMN_DESCRIPTION = "description";
	
	private static DescribableWrapper	instance;
	
	private List<String>	keys;

	private DescribableWrapper() {
		this.keys = Collections.unmodifiableList(Collections.singletonList(COLUMN_DESCRIPTION));
	}
	
	public static synchronized DescribableWrapper getInstance() {
		if (instance == null) {
			instance = new DescribableWrapper();
		}
		return instance;
	}
	
	public List<String> getKeys() {
		return this.keys;
	}
	
	public String getName(final String key) {
		if (key == COLUMN_DESCRIPTION) {
			return LangModelGeneral.getString("DescribableWrapper.Text.Description");
		}
		return null;
	}
	
	public Class<?> getPropertyClass(final String key) {
		if (key == COLUMN_DESCRIPTION) {
			return String.class;
		}
		return null;
	}
	
	public Object getPropertyValue(final String key) {
		return null;
	}	

	public Object getValue(	final Describable describable,
	                       	final String key) {
		if (describable != null && key == COLUMN_DESCRIPTION) {			
			return describable.getDescription();
		} 
		return null;
	}
	
	public boolean isEditable(final String key) {
		return false;
	}
	
	public void setPropertyValue(	final String key,
			final Object objectKey,
			final Object objectValue) {
		// nothing		
	}	

	public void setValue(	final Describable describable,
		final String key,
		final Object value) 
	throws PropertyChangeException {
		if (describable != null && key == COLUMN_DESCRIPTION) {
			describable.setDescription((String) value);
		}		
	}
}

