package com.syrus.AMFICOM.Client.General.UI;


import com.syrus.AMFICOM.Client.Resource.ObjectResource;
import com.syrus.AMFICOM.Client.Resource.ObjectResourceModel;
import com.syrus.AMFICOM.Client.Resource.Pool;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;


public class ObjectResourceListBox extends JList
		implements PropertyRenderer, PropertyEditor
{
	static public final String _DEFAULT_COL_ID = "name";
	static public final String _ID_COL_ID = "id";

	public List vec = new ArrayList();
	String obj_id = "";
	String type = "";
	String col_id = "name";
	Map objs = new HashMap();

	boolean doRestrict = false;
	String domain_id = "";

    class MyListBoxRenderer extends DefaultListCellRenderer
    {
		ObjectResourceListBox parent;

        public MyListBoxRenderer(ObjectResourceListBox parent)
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
				if(parent.col_id.equals(ObjectResourceListBox._DEFAULT_COL_ID))
					text = or.getName();
				else
				if(parent.col_id.equals(ObjectResourceListBox._ID_COL_ID))
					text = or.getId();
				else
				{
					ObjectResourceModel mod = or.getModel();
					text = mod.getColumnValue(parent.col_id);
//					text = or.getColumnValue(parent.col_id);
				}
				return super.getListCellRendererComponent(
                        list, text, index, isSelected, cellHasFocus);
			}
			else
				return super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
        }

    }

	public ObjectResourceListBox()
	{
		super();

		setListData(vec);

		this.setBounds(new Rectangle(0, 0, 20, 20));

        ObjectResourceListBox.MyListBoxRenderer renderer = new ObjectResourceListBox.MyListBoxRenderer(this);
        renderer.setSize(this.getWidth(), this.getHeight());
        renderer.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.setCellRenderer(renderer);
//        this.setMaximumRowCount(5);
	}

	public ObjectResourceListBox(Map objs, String col_id)
	{
		super();
		this.objs = objs;
		this.col_id = col_id;

		if(objs != null)
    {
      Iterator it = objs.values().iterator();
			for(;it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				vec.add(or);
			}
    }
//        setModel(new DefaultComboBoxModel(vec));
		setListData(vec);

		this.setBounds(new Rectangle(0, 0, 20, 20));

        ObjectResourceListBox.MyListBoxRenderer renderer = new ObjectResourceListBox.MyListBoxRenderer(this);
        renderer.setSize(this.getWidth(), this.getHeight());
        renderer.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.setCellRenderer(renderer);
//        this.setMaximumRowCount(5);
	}

	public ObjectResourceListBox(Map objs, String col_id, Object obj)
	{
		this(objs, col_id);
		setSelected(obj);
	}
	
	public ObjectResourceListBox(String type, String col_id)
	{
		this.type = type;
		this.col_id = col_id;

		objs = Pool.getMap(type);
		if(objs != null)
    {
      Iterator it = objs.values().iterator();
			for(;it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				vec.add(or);
			}
    }
//        setModel(new DefaultComboBoxModel(vec));
		setListData(vec);

		this.setBounds(new Rectangle(0, 0, 20, 20));

        ObjectResourceListBox.MyListBoxRenderer renderer = new ObjectResourceListBox.MyListBoxRenderer(this);
        renderer.setSize(this.getWidth(), this.getHeight());
        renderer.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.setCellRenderer(renderer);
//        this.setMaximumRowCount(5);
	}
	
	public ObjectResourceListBox(String type, String col_id, Object obj)
	{
		this(type, col_id);
		setSelected(obj);
	}
	
	public ObjectResourceListBox(Map objs, Object obj)
	{
		this(objs, _DEFAULT_COL_ID, obj);
	}
	
	public ObjectResourceListBox(Map objs)
	{
		this(objs, _DEFAULT_COL_ID);
	}

	public ObjectResourceListBox(String type, Object obj)
	{
		this(type, _DEFAULT_COL_ID, obj);
	}
	
	public ObjectResourceListBox(String type)
	{
		this(type, _DEFAULT_COL_ID);
	}

	public void setContents(Collection enum)
	{
		vec = new ArrayList();
		if(enum != null)
    {
      Iterator it = enum.iterator();
			for(;it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				vec.add(or);
			}
    }
//        setModel(new DefaultComboBoxModel(vec));
		if(doRestrict)
			restrictContents();
		setListData(vec);
	}

	public void setContents(Map objs)
	{
		vec = new ArrayList();
		if(objs != null)
    {
      Iterator it = objs.values().iterator();
			for(;it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				vec.add(or);
			}
    }
//        setModel(new DefaultListModel(vec));
		if(doRestrict)
			restrictContents();
		setListData(vec);

	}

	public void setContents(String type)
	{
		Map objs = Pool.getMap(type);
		setContents(objs);
	}

	public void removeAll()
	{
		vec.clear();
		setListData(vec);
	}
	
	public void remove(ObjectResource or)
	{
		vec.remove(or);
		setListData(vec);
	}

	public void remove(Object[] objs)
	{
		for(int i = 0; i < objs.length; i++)
			vec.remove((ObjectResource )objs[i]);
		setListData(vec);
	}

	public void remove(String obj_id)
	{
		for(int i = 0; i < vec.size(); i++)
		{
			ObjectResource or = (ObjectResource )vec.get(i);
			if(or.getId().equals(obj_id))
				vec.remove(or);
		}
		setListData(vec);
	}

	public void add(ObjectResource or)
	{
		vec.add(or);
		if(doRestrict)
			restrictContents();
		setListData(vec);
	}
	
	public void add(Map h)
	{
		add(h);
	}

	public void add(List v)
	{
		add(v);
	}

	public void add(Collection e)
	{
		if(e != null)
    {
      Iterator it = e.iterator();
			for(; it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				vec.add(or);
			}
    }
		if(doRestrict)
			restrictContents();
		setListData(vec);
	}

	public Object getSelected()
	{
		ObjectResource or = (ObjectResource )super.getSelectedValue();
		if(or == null)
			return null;
		obj_id = or.getId();
		return obj_id;
	}
	
	public ObjectResource getSelectedObjectResource()
	{
		ObjectResource or = (ObjectResource )super.getSelectedValue();
		return or;
	}
	
	public void setSelected(Object obj)
	{
		if(obj == null)
		{
			return;
		}
		if(obj instanceof ObjectResource)
		{
			this.setSelectedValue(obj, true);
			return;
		}
		else
		if(obj instanceof String)
		{
			obj_id = (String)obj;
      Iterator it = vec.listIterator();
			for(;it.hasNext();)
			{
				ObjectResource or = (ObjectResource )it.next();
				if(or.getId().equals(obj_id))
				{
					this.setSelectedValue(or, true);
					break;
				}
			}
		}
	}

	public void deselect(Object obj)
	{
		ObjectResource or = null;
		if(obj == null)
		{
			return;
		}
		if(obj instanceof String)
		{
			obj_id = (String )obj;
      
      Iterator it = vec.listIterator();
			for(;it.hasNext();)
			{
				ObjectResource or1 = (ObjectResource )it.next();
				if(or1.getId().equals(obj_id))
				{
					or = or1;
					break;
				}
			}
		}
		if(obj instanceof ObjectResource)
		{
			or = (ObjectResource )obj;
		}
		if(or != null)
		{
			int index = 0;
      Iterator it = vec.listIterator();
			for(;it.hasNext();)
			{
				ObjectResource or2 = (ObjectResource )it.next();
				if(or2.equals(or))
				{
					this.getSelectionModel().removeSelectionInterval(index, index);
					break;
				}
				index++;
			}
		}
	}

	public void restrictToDomain(boolean bool)
	{
		doRestrict = bool;
		if(doRestrict)
			restrictContents();
		setListData(vec);
	}

	public void restrictContents()
	{
		List vec_to_remove = new ArrayList();

    Iterator it = vec.listIterator();
    for(;it.hasNext();)
    {
      ObjectResource or = (ObjectResource )it.next();
			if(!or.getDomainId().equals(domain_id))
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
  
  public void setListData(List data)
  {
    super.setListData(data.toArray());
  }
}
