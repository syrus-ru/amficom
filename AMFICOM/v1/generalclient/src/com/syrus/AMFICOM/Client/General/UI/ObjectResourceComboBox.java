package com.syrus.AMFICOM.Client.General.UI;


import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;
import com.syrus.AMFICOM.Client.Resource.StubResource;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;

public class ObjectResourceComboBox extends AComboBox
		implements PropertyRenderer, PropertyEditor, ActionListener
{
	static public final String _DEFAULT_COL_ID = "name";
	static public final String _ID_COL_ID = "id";

	public Vector vec = new Vector();
	String obj_id = "";
	String type = "";
	String col_id = "name";
	int shownull = 0;
	Hashtable objs = new Hashtable();
	StubResource stubResource = new StubResource();

	boolean doRestrict = false;
	String domain_id = "";

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
			if(value instanceof ObjectResource)
			{
				ObjectResource or = (ObjectResource)value;
				String text = "";
				if(parent.col_id.equals(ObjectResourceComboBox._DEFAULT_COL_ID))
					text = or.getName();
				else
				if(parent.col_id.equals(ObjectResourceComboBox._ID_COL_ID))
					text = or.getId();
				else
				{
					ObjectResourceModel mod = or.getModel();
					text = mod.getColumnValue(parent.col_id);
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

        setModel(new DefaultComboBoxModel(vec));

		this.setBounds(new Rectangle(0, 0, 20, 20));
		this.addActionListener(this);

        ObjectResourceComboBox.ComboBoxRenderer renderer = new ObjectResourceComboBox.ComboBoxRenderer(this);
        renderer.setSize(this.getWidth(), this.getHeight());
        renderer.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.setRenderer(renderer);
        this.setMaximumRowCount(5);
	}

	public ObjectResourceComboBox(Hashtable objs, String col_id, boolean shownull)
	{
		super();
		this.objs = objs;
		this.col_id = col_id;

		if(objs != null)
			for(Enumeration enum = objs.elements(); enum.hasMoreElements();)
			{
				ObjectResource or = (ObjectResource)enum.nextElement();
				vec.addElement(or);
			}
		if(shownull)
			vec.add(stubResource);

        setModel(new DefaultComboBoxModel(vec));

		this.setBounds(new Rectangle(0, 0, 20, 20));
		this.addActionListener(this);

        ObjectResourceComboBox.ComboBoxRenderer renderer = new ObjectResourceComboBox.ComboBoxRenderer(this);
        renderer.setSize(this.getWidth(), this.getHeight());
        renderer.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.setRenderer(renderer);
        this.setMaximumRowCount(5);
	}

	public ObjectResourceComboBox(Hashtable objs, String col_id, boolean shownull, Object obj)
	{
		this(objs, col_id, shownull);
		setSelected(obj);
	}
	
	public ObjectResourceComboBox(String type, String col_id, boolean shownull)
	{
		this.type = type;
		this.col_id = col_id;

		objs = Pool.getHash(type);
		if(objs != null)
			for(Enumeration enum = objs.elements(); enum.hasMoreElements();)
			{
				ObjectResource or = (ObjectResource)enum.nextElement();
				vec.addElement(or);
			}

		if(shownull)
			vec.add(stubResource);

        setModel(new DefaultComboBoxModel(vec));

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
	
	public ObjectResourceComboBox(Hashtable objs, Object obj)
	{
		this(objs, _DEFAULT_COL_ID, false, obj);
	}
	
	public ObjectResourceComboBox(Hashtable objs)
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

	public void setContents(Enumeration enum, boolean shownull)
	{
		vec = new Vector();
		if(enum != null)
			for(; enum.hasMoreElements();)
			{
				ObjectResource or = (ObjectResource )enum.nextElement();
				vec.addElement(or);
			}
		if(shownull)
			vec.add(stubResource);

		if(doRestrict)
			restrictContents();
        setModel(new DefaultComboBoxModel(vec));
	}

	public void setContents(Hashtable objs, boolean shownull)
	{
		vec = new Vector();
		if(objs != null)
			for(Enumeration enum = objs.elements(); enum.hasMoreElements();)
			{
				ObjectResource or = (ObjectResource )enum.nextElement();
				vec.addElement(or);
			}
		if(shownull)
			vec.add(stubResource);

		if(doRestrict)
			restrictContents();
        setModel(new DefaultComboBoxModel(vec));

	}

	public void setContents(String type, boolean shownull)
	{
		Hashtable objs = Pool.getHash(type);
		setContents(objs, shownull);

		if(doRestrict)
			restrictContents();
        setModel(new DefaultComboBoxModel(vec));
	}

	public void add(ObjectResource or)
	{
		vec.add(or);
		if(doRestrict)
			restrictContents();
        setModel(new DefaultComboBoxModel(vec));
	}
	
	public void add(Hashtable h)
	{
		add(h.elements());
	}

	public void add(Vector v)
	{
		add(v.elements());
	}

	public void add(Enumeration e)
	{
		if(e != null)
			for(; e.hasMoreElements();)
			{
				ObjectResource or = (ObjectResource )e.nextElement();
				vec.addElement(or);
			}
		if(doRestrict)
			restrictContents();
        setModel(new DefaultComboBoxModel(vec));
	}

	public void removeAll()
	{
		vec.removeAllElements();
        setModel(new DefaultComboBoxModel(vec));
	}
	
	public void remove(ObjectResource or)
	{
		vec.remove(or);
        setModel(new DefaultComboBoxModel(vec));
	}

	public void remove(Object[] objs)
	{
		for(int i = 0; i < objs.length; i++)
			vec.remove((ObjectResource )objs[i]);
        setModel(new DefaultComboBoxModel(vec));
	}

	public void remove(String obj_id)
	{
		for(int i = 0; i < vec.size(); i++)
		{
			ObjectResource or = (ObjectResource )vec.get(i);
			if(or.getId().equals(obj_id))
				vec.remove(or);
		}
        setModel(new DefaultComboBoxModel(vec));
	}

	public Object getSelected()
	{
		return getSelectedId();
	}
	
	public String getSelectedId()
	{
		ObjectResource or = (ObjectResource )this.getSelectedItem();
		if(or == null)
			return null;
		obj_id = or.getId();
		return obj_id;
	}
	
	public ObjectResource getSelectedObjectResource()
	{
		ObjectResource or = (ObjectResource )this.getSelectedItem();
		return or;
	}
	
	public void setSelected(Object obj)
	{
		if(obj == null)
		{
			this.setSelectedItem(stubResource);
			return;
		}
		if(obj instanceof ObjectResource)
		{
			this.setSelectedItem(obj);
			return;
		}
		else
		if(obj instanceof String)
		{
			obj_id = (String)obj;

			boolean found = false;

			for(Enumeration enum = vec.elements(); enum.hasMoreElements();)
			{
				ObjectResource or = (ObjectResource )enum.nextElement();
				if(or.getId().equals(obj_id))
				{
					this.setSelectedItem(or);
					found = true;
					break;
				}
			}
			if(!found)
				this.setSelectedItem(stubResource);
/*
			Object or = Pool.get(type, obj_id);
			if(or != null)
				this.setSelectedItem(or);
			else
				this.setSelectedItem(stubResource);
*/
		}
	}

	public void actionPerformed(ActionEvent e)
	{
//		if(e.getModifiers() == ActionEvent.ACTION_PERFORMED)
		{
			ObjectResource or = (ObjectResource )this.getSelectedItem();
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
				ObjectResourceModel mod = or.getModel();
				text = mod.getColumnValue(col_id);
			}
			this.setToolTipText(text);
		}
	}

	public void restrictToDomain(boolean bool)
	{
		doRestrict = bool;
		if(doRestrict)
			restrictContents();
        setModel(new DefaultComboBoxModel(vec));
	}

	public void restrictContents()
	{
		Vector vec_to_remove = new Vector();
		for(Enumeration enum = vec.elements(); enum.hasMoreElements();)
		{
			ObjectResource or = (ObjectResource )enum.nextElement();
			if(!or.getDomainId().equals(domain_id))
				if(!or.equals(stubResource))
					vec_to_remove.add(or);
		}
		for(int i = 0; i < vec_to_remove.size(); i++)
			vec.remove((ObjectResource )vec_to_remove.get(i));
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
