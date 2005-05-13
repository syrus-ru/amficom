/*
 * $Id: ObjectResourceComboBox.java,v 1.9 2005/05/13 19:05:47 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.general.StorableObject;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.9 $, $Date: 2005/05/13 19:05:47 $
 * @module generalclient_v1
 */
public class ObjectResourceComboBox extends AComboBox
		implements PropertyRenderer, PropertyEditor, ActionListener
{
	static public final String _DEFAULT_COL_ID = "name";
	static public final String _ID_COL_ID = "id";

	public List vec = new ArrayList();

	private String obj_id = "";
	private String type = "";
	private String col_id = "name";
	private int shownull = 0;
	private Map objs = new HashMap();
	private StubResource stubResource = new StubResource();

	private boolean doRestrict = false;
	private String domain_id = "";

	class ComboBoxRenderer extends DefaultListCellRenderer
	{
		ObjectResourceComboBox parent;

		public ComboBoxRenderer(ObjectResourceComboBox parent)
		{
			this.parent = parent;
		}

		public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus)
		{
			if(value instanceof StorableObject)
			{
				StorableObject or = (StorableObject)value;
				String text = "";
				if(parent.col_id.equals(ObjectResourceComboBox._DEFAULT_COL_ID))
					text = or.getName();
				else
				if(parent.col_id.equals(ObjectResourceComboBox._ID_COL_ID))
					text = or.getId();
				else
				{
//					ObjectResourceModel mod = or.getModel();
//					text = mod.getColumnValue(parent.col_id);
//					text = or.getColumnValue(parent.col_id);
				}
				JComponent c = (JComponent )super.getListCellRendererComponent(
												list, text, index, isSelected, cellHasFocus);
				c.setToolTipText(text);
				return c;
			}
			else
				return super.getListCellRendererComponent(
												list, value, index, isSelected, cellHasFocus);
		}

	}

	public ObjectResourceComboBox()
	{
		super();

		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));

		this.setBounds(new Rectangle(0, 0, 20, 20));
		this.addActionListener(this);

		ObjectResourceComboBox.ComboBoxRenderer renderer = new ObjectResourceComboBox.ComboBoxRenderer(this);
		renderer.setSize(this.getWidth(), this.getHeight());
		renderer.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		this.setRenderer(renderer);
		this.setMaximumRowCount(5);
	}

	public ObjectResourceComboBox(Map objs, String col_id, boolean shownull)
	{
		super();
		this.objs = objs;
		this.col_id = col_id;

		if(objs != null)
		{
			for(Iterator it = objs.values().iterator(); it.hasNext();)
			{
				StorableObject or = (StorableObject)it.next();
				vec.add(or);
			}
		}

		if(shownull)
			vec.add(stubResource);

		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));

		this.setBounds(new Rectangle(0, 0, 20, 20));
		this.addActionListener(this);

		ObjectResourceComboBox.ComboBoxRenderer renderer = new ObjectResourceComboBox.ComboBoxRenderer(this);
		renderer.setSize(this.getWidth(), this.getHeight());
		renderer.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		this.setRenderer(renderer);
		this.setMaximumRowCount(5);
	}

	public ObjectResourceComboBox(Map objs, String col_id, boolean shownull, Object obj)
	{
		this(objs, col_id, shownull);
		setSelected(obj);
	}

	public ObjectResourceComboBox(String type, String col_id, boolean shownull)
	{
		this.type = type;
		this.col_id = col_id;

		objs = Pool.getMap(type);
		if(objs != null)
		{
			for(Iterator it = objs.values().iterator(); it.hasNext();)
			{
				StorableObject or = (StorableObject)it.next();
				vec.add(or);
			}
		}

		if(shownull)
			vec.add(stubResource);

		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));

		this.setBounds(new Rectangle(0, 0, 20, 20));
		this.addActionListener(this);

		ObjectResourceComboBox.ComboBoxRenderer renderer = new ObjectResourceComboBox.ComboBoxRenderer(this);
		renderer.setSize(this.getWidth(), this.getHeight());
		renderer.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
		this.setRenderer(renderer);
		this.setMaximumRowCount(5);
	}

	public ObjectResourceComboBox(String type, String col_id, boolean shownull, Object obj)
	{
		this(type, col_id, shownull);
		setSelected(obj);
	}

	public ObjectResourceComboBox(Map objs, Object obj)
	{
		this(objs, _DEFAULT_COL_ID, false, obj);
	}

	public ObjectResourceComboBox(Map objs)
	{
		this(objs, _DEFAULT_COL_ID, false);
	}

	public ObjectResourceComboBox(String type, Object obj)
	{
		this(type, _DEFAULT_COL_ID, false, obj);
	}

	public ObjectResourceComboBox(String type)
	{
		this(type, _DEFAULT_COL_ID, false);
	}

	public ObjectResourceComboBox(String type, boolean shownull)
	{
		this(type, _DEFAULT_COL_ID, shownull);
	}

	public void setContents(Collection coll, boolean shownull)
	{
		vec = new ArrayList();

		if(coll != null)
		{
			for(Iterator it = coll.iterator(); it.hasNext();)
			{
				StorableObject or = (StorableObject)it.next();
				vec.add(or);
			}
		}

		if(shownull)
			vec.add(stubResource);

		if(doRestrict)
			restrictContents();

		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));
	}

	public void setContents(Iterator it, boolean shownull)
	{
		vec = new ArrayList();
		if(it != null)
			for(; it.hasNext();)
			{
				StorableObject or = (StorableObject )it.next();
				vec.add(or);
			}

		if(shownull)
			vec.add(stubResource);

		if(doRestrict)
			restrictContents();

		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));
	}

	public void setContents(Map objs, boolean shownull)
	{
		vec = new ArrayList();

		if(objs != null)
		{
			for(Iterator it = objs.values().iterator(); it.hasNext();)
			{
				StorableObject or = (StorableObject)it.next();
				vec.add(or);
			}
		}

		if(shownull)
			vec.add(stubResource);

		if(doRestrict)
			restrictContents();

		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));
	}

	public void setContents(String type, boolean shownull)
	{
		Map objs = Pool.getMap(type);
		setContents(objs, shownull);

		if(doRestrict)
			restrictContents();

		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));
	}

	public void add(StorableObject or)
	{
		vec.add(or);
		if(doRestrict)
			restrictContents();

		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));
	}

	public void add(Map h)
	{
		add(h.values());
	}

	public void add(ArrayList v)
	{
		add(v);
	}

	public void add(Collection coll)
	{
		if(coll != null)
		{
			for(Iterator it = coll.iterator(); it.hasNext();)
			{
				StorableObject or = (StorableObject)it.next();
				vec.add(or);
			}
		}

		if(doRestrict)
			restrictContents();

		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));
	}

	public void removeAll()
	{
		vec.clear();
		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));
	}

	public void remove(StorableObject or)
	{
		vec.remove(or);
		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));
	}

	public void remove(Object[] objs)
	{
		for(int i = 0; i < objs.length; i++)
			vec.remove((StorableObject )objs[i]);
		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));
	}

	public void remove(String obj_id)
	{
		for(ListIterator it = vec.listIterator(); it.hasNext();)
		{
			StorableObject or = (StorableObject)it.next();
			if(or.getId().equals(obj_id))
				vec.remove(or);
		}

		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));
	}

	public Object getSelected()
	{
		return getSelectedId();
	}

	public String getSelectedId()
	{
		StorableObject or = (StorableObject )this.getSelectedItem();
		if(or == null)
			return null;
		obj_id = or.getId();
		return obj_id;
	}

	public StorableObject getSelectedObjectResource()
	{
		StorableObject or = (StorableObject )this.getSelectedItem();
		return or;
	}

	public void setSelected(Object obj)
	{
		if(obj == null)
		{
			this.setSelectedItem(stubResource);
			return;
		}
		if(obj instanceof StorableObject)
		{
			this.setSelectedItem(obj);
			return;
		}
		else
		if(obj instanceof String)
		{
			obj_id = (String)obj;

			boolean found = false;

			for(ListIterator it = vec.listIterator(); it.hasNext();)
			{
				StorableObject or = (StorableObject)it.next();
				if(or.getId().equals(obj_id))
				{
					this.setSelectedItem(or);
					found = true;
					break;
				}
			}

			if(!found)
				this.setSelectedItem(stubResource);
		}
	}

	public void actionPerformed(ActionEvent e)
	{
//		if(e.getModifiers() == ActionEvent.ACTION_PERFORMED)
		{
			StorableObject or = (StorableObject )this.getSelectedItem();
			String text = "";
			if(or == null)
				text = "";
			else
			if(col_id.equals(_DEFAULT_COL_ID))
				text = or.getName();
			else
			if(col_id.equals(_ID_COL_ID))
				text = or.getId();
			else
			{
//				ObjectResourceModel mod = or.getModel();
//				text = mod.getColumnValue(col_id);
			}
			this.setToolTipText(text);
		}
	}

	public void restrictToDomain(boolean bool)
	{
		doRestrict = bool;
		if(doRestrict)
			restrictContents();
		setModel(new DefaultComboBoxModel(vec.toArray(new StorableObject[vec.size()])));
	}

	public void restrictContents()
	{
		List vec_to_remove = new ArrayList();

		for(ListIterator it = vec.listIterator(); it.hasNext();)
		{
			StorableObject or = (StorableObject)it.next();
			if(!or.getDomainId().equals(domain_id))
				if(!or.equals(stubResource))
					vec_to_remove.add(or);
		}

		for(ListIterator it = vec_to_remove.listIterator(); it.hasNext();)
		{
			StorableObject or = (StorableObject)it.next();
			vec.remove(or);
		}
	}

	public void setDomainId(String domain_id)
	{
		this.domain_id = domain_id;
	}

	public String getDomainId()
	{
		return domain_id;
	}
}
