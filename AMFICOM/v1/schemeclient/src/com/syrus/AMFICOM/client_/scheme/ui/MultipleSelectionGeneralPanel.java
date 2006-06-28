/*-
 * $Id: MultipleSelectionGeneralPanel.java,v 1.2 2006/06/06 12:41:55 stas Exp $
 *
 * Copyright ¿ 2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client_.scheme.ui;

import java.awt.BorderLayout;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.syrus.AMFICOM.client.UI.DefaultStorableObjectEditor;
import com.syrus.AMFICOM.client.UI.WrapperedList;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.client.resource.I18N;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Namable;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.util.PropertyChangeException;
import com.syrus.util.Wrapper;

public class MultipleSelectionGeneralPanel extends DefaultStorableObjectEditor<StorableObject> {
	ApplicationContext aContext;
	Set<StorableObject> objects;
	WrapperedList<Namable> list = new WrapperedList<Namable>(
			NameableWrapper.getInstance(), 
			NameableWrapper.COLUMN_NAME, 
			NameableWrapper.COLUMN_NAME);
	JPanel panel = new JPanel();
	
	protected MultipleSelectionGeneralPanel() {
		this.panel.setLayout(new BorderLayout());
		
		JLabel objectsLabel = new JLabel(I18N.getString("selected_objects"));
		this.panel.add(objectsLabel, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(this.list);
		this.panel.add(scrollPane, BorderLayout.CENTER);
	}
	
	public void setContext(final ApplicationContext aContext) {
		this.aContext = aContext;
	}
	
	public JComponent getGUI() {
		return this.panel;
	}

	@Override
	public void setObjects(Set<StorableObject> objects) {
		this.objects = objects;
		
		Collection<Namable> nameables = new HashSet<Namable>();
		for (StorableObject so : objects) {
			if (so instanceof Namable) {
				nameables.add((Namable)so);
			}
		}
		this.list.getModel().setElements(nameables);
	}
	
	public void setObject(StorableObject object) {
		final HashSet<StorableObject> hashSet = new HashSet<StorableObject>();
		hashSet.add(object);
		setObjects(hashSet);
	}

	public StorableObject getObject() {
		return this.objects.iterator().next();
	}

	@Override
	public Set<StorableObject> getObjects() {
		return this.objects;
	}
}

class NameableWrapper implements Wrapper<Namable> {
	public static final String COLUMN_NAME = "name";
	
	private static NameableWrapper instance;
	
	private final List<String> keys;
	
	public static NameableWrapper getInstance() {
		if (instance == null) {
			instance = new NameableWrapper();
		}
		return instance;
	}
	
	private NameableWrapper() {
		this.keys = Collections.unmodifiableList(Arrays.asList(new String[] {
				COLUMN_NAME
		}
		));
	}
	
	public List<String> getKeys() {
		return this.keys;
	}
	
	public String getName(String key) {
		return key;
	}
	
	public Class< ? > getPropertyClass(String key) {
		if (key.equals(COLUMN_NAME)) {
			return String.class;
		}
		return null;
	}
	
	public Object getPropertyValue(String key) {
		return key;
	}
	
	public void setPropertyValue(String key, Object objectKey, Object objectValue) {
	}
	
	public Object getValue(Namable object, String key) {
		if (key.equals(COLUMN_NAME)) {
			return object.getName();
		}
		return null;
	}
	
	public boolean isEditable(String key) {
		return false;
	}
	
	public void setValue(Namable object, String key, Object value) throws PropertyChangeException {
	}
}

