/*
 * $Id: ApplicationModel.java,v 1.8 2005/03/16 13:40:57 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.*;
import java.util.*;

/**
 * ������ ���������� ��������� ��������, ������� ������������ (��������) 
 * ����� ����������� � ��������
 * 
 * @author $Author: bass $
 * @version $Revision: 1.8 $, $Date: 2005/03/16 13:40:57 $
 * @module generalclient_v1
 */
public class ApplicationModel
{
	protected SessionInterface session = null;

	protected DataSourceInterface dataSource = null;

	/**
	 * ������ ��������� ������
	 */
	private Hashtable appHash = new Hashtable();
	
	/**
	 * ������ ��������, ���������� ���������� �� ���������� � ������
	 */
	private ApplicationModelListenerList listenerList
		= new ApplicationModelListenerList();

	private static ApplicationModel instance = null;

	/**
	 * To obtain a shared instance, use {@link #getInstance()}.
	 * @see #getInstance()
	 */
	protected ApplicationModel()
	{
	}

	public static ApplicationModel getInstance()
	{
		if (instance == null)
			synchronized (ApplicationModel.class)
			{
				if (instance == null)
					instance = new ApplicationModel();
			}
		return instance;
	}

	/**
	 * �������� � ������ ������� � ��������� ���� ������
	 */
	public void add(final String name, Command command, final boolean installed, final boolean visible, final boolean usable, final boolean accessible, final boolean selected)
	{
		if (command == null)
			add(name, new VoidCommand(), installed, visible, usable, accessible, selected);
		else
			appHash.put(name, new ApplicationEntry(name, command, installed, visible, usable, accessible, selected));
	}

	/**
	 * �������� � ������ ������� � ���������� ������ �� ���������
	 */
	public void add(final String name, Command command)
	{
		if (command == null)
			add(name, new VoidCommand());
		else
			appHash.put(name, new ApplicationEntry(name, command));
	}

	/**
	 * �������� � ������ ������� � ���������� ������ � ������� �� ���������
	 */
	public void add(final String name)
	{
		appHash.put(name, new ApplicationEntry(name));
	}

	/**
	 * �������� ���������� ��������� � ������
	 */
	public int getCount()
	{
		return appHash.size();
	}

	/**
	 * ���������� ���� "������" ��� ��������
	 * @deprecated use {@link #setSelected(String, boolean) setSelected(name, true)}
	 */
	public void select(final String name)
	{
		setSelected(name, true);
	}

	/**
	 * ����� ���� "������" ��� ��������
	 * @deprecated use {@link #setSelected(String, boolean) setSelected(name, false)}
	 */
	public void deselect(final String name)
	{
		setSelected(name, false);
	}

	/**
	 * ���������� ���� "������" ��� �������� � bool
	 */
	public void setSelected(final String name, final boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		if (entry != null)
			entry.selected = bool;
	}

	/**
	 * ���������� ���� "�����������" ��� ��������
	 * @deprecated use {@link #setEnabled(String, boolean) setEnabled(name, true)}
	 */
	public void enable(final String name)
	{
		setEnabled(name, true);
	}

	/**
	 * ����� ���� "�����������" ��� ��������
	 * @deprecated use {@link #setEnabled(String, boolean) setEnabled(name, false)}
	 */
	public void disable(final String name)
	{
		setEnabled(name, false);
	}

	/**
	 * ���������� ���� "�����������" ��� �������� � bool
	 */
	public void setEnabled(final String name, boolean accessible)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		if (entry != null)
			entry.accessible = accessible;
	}

	/**
	 * ���������� ���� "�������" ��� ��������
	 * @deprecated use {@see #setVisible(String, boolean) setVisible(name, true)}
	 */
	public void show(final String name)
	{
		setVisible(name, true);
	}

	/**
	 * ����� ���� "�������" ��� ��������
	 * @deprecated use {@link #setVisible(String, boolean) setVisible(name, false)}
	 */
	public void hide(final String name)
	{
		setVisible(name, false);
	}

	/**
	 * ���������� ���� "�������" ��� �������� � bool
	 */
	public void setVisible(final String name, final boolean visible)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		if (entry != null)
			entry.visible = visible;
	}

	/**
	 * ���������� ���� "�������������" ��� �������� � bool
	 */
	public void setInstalled(final String name, final boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		if (entry != null)
			entry.installed = bool;
	}

	/**
	 * ���������� ���� "���������" ��� �������� � bool
	 */
	public void setAccessible(final String name, final boolean accessible)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		if (entry != null)
			entry.accessible = accessible;
	}

	/**
	 * ���������� ���� "�������������" ��� �������� � bool
	 */
	public void setUsable(final String name, final boolean usable)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		if (entry != null)
			entry.usable = usable;
	}

	/**
	 * �������� ���� "�������"
	 */
	public boolean isVisible(final String name)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		return ((entry != null) && entry.visible && entry.installed);
	}

	/**
	 * �������� ���� "���������"
	 */
	public boolean isAccessible(final String name)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		return ((entry != null) && entry.accessible);
	}

	/**
	 * �������� ���� "���������"
	 */
	public boolean isSelected(final String name)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		return ((entry != null) && entry.selected);
	}

	/**
	 * �������� ���� "�������������"
	 */
	public boolean isUsable(final String name)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		return ((entry != null) && entry.usable);
	}

	/**
	 * �������� ���� "�����������"
	 */
	public boolean isEnabled(final String name)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		return ((entry != null) && entry.usable && entry.accessible);
	}

	/**
	 * ���������� ���� "�����������" � bool ��� ���� ���������
	 */
	public void setAllItemsEnabled(final boolean enabled)
	{
		setAllItemsAccessible(enabled);
	}

	/**
	 * ���������� ���� "���������" � bool ��� ���� ���������
	 */
	public void setAllItemsAccessible(final boolean accessible)
	{
		for(Enumeration e = appHash.elements(); e.hasMoreElements();)
			((ApplicationEntry) (e.nextElement())).accessible = accessible;
	}

	/**
	 * ���������� ���� "�������" � bool ��� ���� ���������
	 */
	public void setAllItemsVisible(final boolean visible)
	{
		for (Enumeration e = appHash.elements(); e.hasMoreElements();)
			((ApplicationEntry) (e.nextElement())).visible = visible;
	}

	/**
	 * ���������� ���� "�������������" � bool ��� ���� ���������
	 */
	public void setAllItemsUsable(final boolean usable)
	{
		for (Enumeration e = appHash.elements(); e.hasMoreElements();)
			((ApplicationEntry) (e.nextElement())).usable = usable;
	}

	/**
	 * ���������� ���� "���������" � bool ��� ���� ���������
	 */
	public void setAllItemsSelected(final boolean selected)
	{
		for (Enumeration e = appHash.elements(); e.hasMoreElements();)
			((ApplicationEntry) (e.nextElement())).selected = selected;
	}

	/**
	 * ���������� ������� ��� ��������
	 */
	public void setCommand(final String name, final Command command)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		if (entry == null)
			return;
		if (command == null)
			setCommand(name, new VoidCommand());
		else
			entry.command = command;
	}

	/**
	 * �������� ��������� � ��������� �������
	 */
	public Command getCommand(final String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		return ((entry == null) ? (new VoidCommand()) : entry.command);
	}

	/**
	 * �������� ��������� - ������, ���������� ����������� �� ���������� ������
	 */
	public void addListener(ApplicationModelListener l)
	{
		listenerList.add(ApplicationModelListener.class, l);
	}

	/**
	 * ������� ���������
	 */
	public void removeListener(ApplicationModelListener l)
	{
		listenerList.remove(ApplicationModelListener.class, l);
	}

	/**
	 * ���������������� ���������� � ���, ��� ���������� ��������� 
	 * ���� ��������� ������
	 */
	public void fireModelChanged()
	{
		fireModelChanged(new String [] { "" });
	}

	/**
	 * ���������������� ���������� � ���, ��� ���������� ��������� ��������
	 * @param s - ��� ��������
	 */
	public void fireModelChanged(String e)
	{
		fireModelChanged(new String [] { e });
	}

	/**
	 * ���������������� ���������� � ���, ��� ���������� ��������� ���������
	 * @param e - ������ ���� ���������
	 */
	public void fireModelChanged(String e[])
	{
		// ������������ ��������� ������ ����������
		Object[] listeners = listenerList.getListenerList();
		Object[] listenerclasses = listenerList.getListenerClassList();

		// ���� �� ���� ���������� � ������� ����������� �� ����������
		// ��������� e � ������
		for (int i = listeners.length - 1; i >= 0; i--)
			if (listenerclasses[i] == ApplicationModelListener.class)
				((ApplicationModelListener )listeners[i]).modelChanged(e);
	}

	public DataSourceInterface getDataSource(final SessionInterface session)
	{
		if ((this.session == null) || (!this.session.equals(session)))
			synchronized (this)
			{
				if ((this.session == null) || (!this.session.equals(session)))
				{
					this.session = session;
					this.dataSource = new RISDDataSource(this.session);
				}
			}
		return this.dataSource;
	}

	/**
	 * ������ �� �������� ������ �������� ��� ��������, ��������� � ��� ������� 
	 * � ����� ��������� � ����������� ������� ������������.
	 * ������������ ��� ���������� ���, ��� ��� ������� ������������ 
	 * ���������������
	 * 
	 * @author $Author: bass $
	 * @version $Revision: 1.8 $, $Date: 2005/03/16 13:40:57 $
	 * @module generalclient_v1
	 */
	class ApplicationEntry
	{
		/**
		 * ������� �� ����� (��� ������ ���� - ������� ��������, ��� 
		 * ���������� ������ - ���������
		 */
		boolean selected = false;

		/**
		 * ����� �� ����� � ������� ���������������� ������������
		 */
		boolean installed = true;

		/**
		 * ����� �� ����� � ������� ���������
		 */
		boolean visible = true;
		
		/**
		 * �������� �� ����� � ������� ���������������� ������������
		 */
		boolean usable = true;

		/**
		 * �������� �� ����� � ������� ���������
		 */
		boolean accessible = true;
		
		/**
		 * ������������� �������� ����
		 */
		String name;

		/**
		 * ��������, ������������� � ������ �������
		 */
		Command command = new VoidCommand();

		/**
		 * ����������� � ��������� ����� �������� � ��������� � ��� �������.
		 * �� ��������� ���������� ���� �������� ��������������� ��������� 
		 * �������:
		 * selected = false
		 * visible = true
		 * usable = true
		 * accessible = true
		 * 
		 * @param name
		 * @param command
		 */
		public ApplicationEntry(final String name, Command command)
		{
			this.name = name;
			this.command = command;
		}

		/**
		 * �� ��������� � ��������� ������ ������� ������ �������
		 * @param name
		 */
		public ApplicationEntry(final String name)
		{
			this.name = name;
		}

		/**
		 * ����������� � �������������� ���� ������ ������
		 * @param name
		 * @param command
		 * @param installed
		 * @param visible
		 * @param usable
		 * @param accessible
		 * @param selected
		 */
		public ApplicationEntry(final String name, Command command, final boolean installed, final boolean visible, final boolean usable, final boolean accessible, final boolean selected)
		{
			this.name = name;
			this.installed = installed;
			this.visible = visible;
			this.usable = usable;
			this.accessible = accessible;
			this.selected = selected;
			this.command = command;
		}

		/**
		 * @deprecated
		 */
		public ApplicationEntry(ApplicationEntry entry)
		{
			this.name = entry.name;
			this.installed = entry.installed;
			this.visible = entry.visible;
			this.usable = entry.usable;
			this.accessible = entry.accessible;
			this.selected = entry.selected;
			this.command = (Command )entry.command.clone();
		}

		/**
		 * @deprecated
		 */
		public Object clone()
		{
			return new ApplicationEntry(this);
		}
	}
}
