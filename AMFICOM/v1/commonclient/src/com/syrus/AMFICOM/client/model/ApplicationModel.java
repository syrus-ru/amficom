/*
 * $Id: ApplicationModel.java,v 1.3 2005/06/06 14:52:47 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.client.model;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.util.Log;

/**
 * ������ ���������� ��������� ��������, ������� ������������ (��������) �����
 * ����������� � ��������
 * 
 * @author $Author: bob $
 * @version $Revision: 1.3 $, $Date: 2005/06/06 14:52:47 $
 * @module generalclient_v1
 */
public class ApplicationModel {

	/**
	 * ������ ��������� ������
	 */
	private Map				appications			= new Hashtable();

	/**
	 * ������ ��������, ���������� ���������� �� ���������� � ������
	 */
	private List					listenerList	= new LinkedList();

	private static ApplicationModel	instance;

	public static final String	MENU_SESSION					= "menuSession";

	public static final String	MENU_SESSION_NEW				= "menuSessionNew";

	public static final String	MENU_SESSION_CLOSE				= "menuSessionClose";

	public static final String	MENU_SESSION_OPTIONS			= "menuSessionOptions";

	public static final String	MENU_SESSION_CONNECTION			= "menuSessionConnection";

	public static final String	MENU_SESSION_CHANGE_PASSWORD	= "menuSessionChangePassword";

	public static final String	MENU_SESSION_DOMAIN				= "menuSessionDomain";

	public static final String	MENU_VIEW_ARRANGE				= "menuViewArrange";

	public static final String	MENU_EXIT						= "menuExit";

	public static final String	MENU_HELP						= "menuHelp";

	public static final String	MENU_HELP_ABOUT					= "menuHelpAbout";

	/**
	 * To obtain a shared instance, use {@link #getInstance()}.
	 * 
	 * @see #getInstance()
	 */
	protected ApplicationModel() {
		// nothing
	}

	public static ApplicationModel getInstance() {
		if (instance == null)
			synchronized (ApplicationModel.class) {
				if (instance == null)
					instance = new ApplicationModel();
			}
		return instance;
	}

	/**
	 * �������� � ������ ������� � ��������� ���� ������
	 */
	public void add(final String name,
					final Command command,
					final boolean installed,
					final boolean visible,
					final boolean usable,
					final boolean accessible,
					final boolean selected) {
		if (command == null) {
			Log.debugMessage("ApplicationModel.add | name: " + name + " , command is null ", Log.WARNING);
			this.add(name, VoidCommand.VOID_COMMAND, installed, visible, usable, accessible, selected);
		} else {
			this.appications.put(name,
				new ApplicationEntry(name, command, installed, visible, usable, accessible, selected));
		}
	}

	/**
	 * �������� � ������ ������� � ���������� ������ �� ���������
	 */
	public void add(final String name,
					Command command) {
		if (command != null) {
			this.appications.put(name, new ApplicationEntry(name, command));
		}
	}

	/**
	 * �������� � ������ ������� � ���������� ������ � ������� �� ���������
	 */
	public void add(final String name) {
		this.appications.put(name, new ApplicationEntry(name));
	}

	/**
	 * �������� ���������� ��������� � ������
	 */
	public int getCount() {
		return this.appications.size();
	}

	/**
	 * ���������� ���� "������" ��� �������� � boolean
	 */
	public void setSelected(final String name,
							final boolean selected) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.selected = selected;
		}
	}

	/**
	 * ���������� ���� "�����������" ��� �������� � boolean
	 */
	public void setEnabled(	final String name,
							boolean enable) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.accessible = enable;
		}
	}

	/**
	 * ���������� ���� "�������" ��� �������� � boolean
	 */
	public void setVisible(	final String name,
							final boolean visible) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.visible = visible;
		}
	}

	/**
	 * ���������� ���� "�������������" ��� �������� � boolean
	 */
	public void setInstalled(	final String name,
								final boolean installed) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.installed = installed;
		}
	}

	/**
	 * ���������� ���� "���������" ��� �������� � boolean
	 */
	public void setAccessible(	final String name,
								final boolean accessible) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.accessible = accessible;
		}
	}

	/**
	 * ���������� ���� "�������������" ��� �������� � boolean
	 */
	public void setUsable(	final String name,
							final boolean usable) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.usable = usable;
		}
	}

	/**
	 * �������� ���� "�������"
	 */
	public boolean isVisible(final String name) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		return ((entry != null) && entry.visible && entry.installed);
	}

	/**
	 * �������� ���� "���������"
	 */
	public boolean isAccessible(final String name) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		return ((entry != null) && entry.accessible);
	}

	/**
	 * �������� ���� "���������"
	 */
	public boolean isSelected(final String name) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		return ((entry != null) && entry.selected);
	}

	/**
	 * �������� ���� "�������������"
	 */
	public boolean isUsable(final String name) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		return ((entry != null) && entry.usable);
	}

	/**
	 * �������� ���� "�����������"
	 */
	public boolean isEnabled(final String name) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		return ((entry != null) && entry.usable && entry.accessible);
	}

	/**
	 * ���������� ���� "�����������" � boolean ��� ���� ���������
	 */
	public void setAllItemsEnabled(final boolean enabled) {
		setAllItemsAccessible(enabled);
	}

	/**
	 * ���������� ���� "���������" � boolean ��� ���� ���������
	 */
	public void setAllItemsAccessible(final boolean accessible) {
		for (Iterator iterator = this.appications.keySet().iterator(); iterator.hasNext();) {
			String key = (String)iterator.next();
			ApplicationEntry applicationEntry = (ApplicationEntry) this.appications.get(key);
			applicationEntry.accessible = accessible;
		}
	}

	/**
	 * ���������� ���� "�������" � boolean ��� ���� ���������
	 */
	public void setAllItemsVisible(final boolean visible) {
		for (Iterator iterator = this.appications.keySet().iterator(); iterator.hasNext();) {
			String key = (String)iterator.next();
			ApplicationEntry applicationEntry = (ApplicationEntry) this.appications.get(key);
			applicationEntry.visible = visible;
		}
	}

	/**
	 * ���������� ���� "�������������" � boolean ��� ���� ���������
	 */
	public void setAllItemsUsable(final boolean usable) {
		for (Iterator iterator = this.appications.keySet().iterator(); iterator.hasNext();) {
			String key = (String)iterator.next();
			ApplicationEntry applicationEntry = (ApplicationEntry) this.appications.get(key);
			applicationEntry.usable = usable;
		}
	}

	/**
	 * ���������� ���� "���������" � boolean ��� ���� ���������
	 */
	public void setAllItemsSelected(final boolean selected) {
		for (Iterator iterator = this.appications.keySet().iterator(); iterator.hasNext();) {
			String key = (String)iterator.next();
			ApplicationEntry applicationEntry = (ApplicationEntry) this.appications.get(key);
			applicationEntry.selected = selected;
		}
	}

	/**
	 * ���������� ������� ��� ��������
	 */
	public void setCommand(	final String name,
							final Command command) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.command = command;
		}
	}

	/**
	 * �������� ��������� � ��������� �������
	 */
	public Command getCommand(final String name) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		return (entry == null) ? VoidCommand.VOID_COMMAND : entry.command;
	}

	/**
	 * �������� ��������� - ������, ���������� ����������� �� ���������� ������
	 */
	public void addListener(ApplicationModelListener listener) {
		this.listenerList.add(listener);
	}

	/**
	 * ������� ���������
	 */
	public void removeListener(ApplicationModelListener listener) {
		this.listenerList.remove(listener);
	}

	/**
	 * ���������������� ���������� � ���, ��� ���������� ��������� ����
	 * ��������� ������
	 */
	public void fireModelChanged() {
		fireModelChanged("");
	}

	/**
	 * ���������������� ���������� � ���, ��� ���������� ��������� ��������
	 * 
	 * @param elementName -
	 *            ��� ��������
	 */
	public void fireModelChanged(String elementName) {
		for (Iterator iterator = this.listenerList.iterator(); iterator.hasNext();) {
			ApplicationModelListener applicationModelListener = (ApplicationModelListener) iterator.next();
			applicationModelListener.modelChanged(elementName);
		}
	}

	/**
	 * ���������������� ���������� � ���, ��� ���������� ��������� ���������
	 * 
	 * @param elementNames -
	 *            ������ ���� ���������
	 */
	public void fireModelChanged(String[] elementNames) {
		for (Iterator iterator = this.listenerList.iterator(); iterator.hasNext();) {
			ApplicationModelListener applicationModelListener = (ApplicationModelListener) iterator.next();
			applicationModelListener.modelChanged(elementNames);
		}
	}

	/**
	 * ������ �� �������� ������ �������� ��� ��������, ��������� � ��� �������
	 * � ����� ��������� � ����������� ������� ������������. ������������ ���
	 * ���������� ���, ��� ��� ������� ������������ ���������������
	 * 
	 * @author $Author: bob $
	 * @version $Revision: 1.3 $, $Date: 2005/06/06 14:52:47 $
	 * @module generalclient_v1
	 */
	class ApplicationEntry {

		/**
		 * ������� �� ����� (��� ������ ���� - ������� ��������, ��� ����������
		 * ������ - ���������
		 */
		boolean	selected	= false;

		/**
		 * ����� �� ����� � ������� ���������������� ������������
		 */
		boolean	installed	= true;

		/**
		 * ����� �� ����� � ������� ���������
		 */
		boolean	visible		= true;

		/**
		 * �������� �� ����� � ������� ���������������� ������������
		 */
		boolean	usable		= true;

		/**
		 * �������� �� ����� � ������� ���������
		 */
		boolean	accessible	= true;

		/**
		 * ������������� �������� ����
		 */
		String	name;

		/**
		 * ��������, ������������� � ������ �������
		 */
		Command	command;

		/**
		 * ����������� � ��������� ����� �������� � ��������� � ��� �������. ��
		 * ��������� ���������� ���� �������� ��������������� ��������� �������:
		 * selected = false visible = true usable = true accessible = true
		 * 
		 * @param name
		 * @param command
		 */
		public ApplicationEntry(final String name, Command command) {
			this.name = name;
			this.command = command;
		}

		/**
		 * �� ��������� � ��������� ������ ������� ������ �������
		 * 
		 * @param name
		 */
		public ApplicationEntry(final String name) {
			this.name = name;
		}

		/**
		 * ����������� � �������������� ���� ������ ������
		 * 
		 * @param name
		 * @param command
		 * @param installed
		 * @param visible
		 * @param usable
		 * @param accessible
		 * @param selected
		 */
		public ApplicationEntry(final String name,
				Command command,
				final boolean installed,
				final boolean visible,
				final boolean usable,
				final boolean accessible,
				final boolean selected) {
			this.name = name;
			this.installed = installed;
			this.visible = visible;
			this.usable = usable;
			this.accessible = accessible;
			this.selected = selected;
			this.command = command;
		}

	}
}
