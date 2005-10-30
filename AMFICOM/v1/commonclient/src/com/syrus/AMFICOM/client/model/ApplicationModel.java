/*
 * $Id: ApplicationModel.java,v 1.13 2005/10/30 15:20:24 bass Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.client.model;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.syrus.util.Log;

/**
 * ������ ���������� ��������� ��������, ������� ������������ (��������) �����
 * ����������� � ��������
 * 
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2005/10/30 15:20:24 $
 * @module commonclient
 */
public class ApplicationModel {

	/**
	 * ������ ��������� ������
	 */
	private Map<String, ApplicationEntry> appications = new Hashtable<String, ApplicationEntry>();

	/**
	 * ������ ��������, ���������� ���������� �� ���������� � ������
	 */
	private List<ApplicationModelListener> listenerList = new LinkedList<ApplicationModelListener>();

	private static ApplicationModel instance;

	private static final String MENU = "Menu";

	public static final String MENU_SESSION = MENU + ".Session";

	public static final String MENU_SESSION_NEW = MENU_SESSION + ".New";

	public static final String MENU_SESSION_CLOSE = MENU_SESSION + ".Close";

	public static final String MENU_SESSION_OPTIONS = MENU_SESSION + ".Options";

	public static final String MENU_SESSION_CHANGE_PASSWORD = MENU_SESSION + ".ChangePassword";

	public static final String MENU_VIEW = MENU + ".View";

	public static final String MENU_VIEW_ARRANGE = MENU_VIEW + ".WindowArrange";

	public static final String MENU_EXIT = MENU + ".Exit";

	public static final String MENU_HELP = MENU + ".Help";

	public static final String MENU_HELP_ABOUT = MENU_HELP + ".About";

	/**
	 * To obtain a shared instance, use {@link #getInstance()}.
	 * 
	 * @see #getInstance()
	 */
	protected ApplicationModel() {
		// nothing
	}

	public static ApplicationModel getInstance() {
		if (instance == null) {
			synchronized (ApplicationModel.class) {
				if (instance == null) {
					instance = new ApplicationModel();
				}
			}
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
			assert Log.debugMessage("name: " + name + " , command is null ", Level.WARNING);
			this.add(name, VoidCommand.VOID_COMMAND, installed, visible, usable, accessible, selected);
		} else {
			this.appications.put(name, new ApplicationEntry(name, command, installed, visible, usable, accessible, selected));
		}
	}

	/**
	 * �������� � ������ ������� � ���������� ������ �� ���������
	 */
	public void add(final String name, final Command command) {
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
	public void setSelected(final String name, final boolean selected) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.selected = selected;
		}
	}

	/**
	 * ���������� ���� "�����������" ��� �������� � boolean
	 */
	public void setEnabled(final String name, boolean enable) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.accessible = enable;
		}
	}

	/**
	 * ���������� ���� "�������" ��� �������� � boolean
	 */
	public void setVisible(final String name, final boolean visible) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.visible = visible;
		}
	}

	/**
	 * ���������� ���� "�������������" ��� �������� � boolean
	 */
	public void setInstalled(final String name, final boolean installed) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.installed = installed;
		}
	}

	/**
	 * ���������� ���� "���������" ��� �������� � boolean
	 */
	public void setAccessible(final String name, final boolean accessible) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.accessible = accessible;
		}
	}

	/**
	 * ���������� ���� "�������������" ��� �������� � boolean
	 */
	public void setUsable(final String name, final boolean usable) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.usable = usable;
		}
	}

	/**
	 * �������� ���� "�������"
	 */
	public boolean isVisible(final String name) {
		final ApplicationEntry entry = this.appications.get(name);
		return ((entry != null) && entry.visible && entry.installed);
	}

	/**
	 * �������� ���� "���������"
	 */
	public boolean isAccessible(final String name) {
		final ApplicationEntry entry = this.appications.get(name);
		return ((entry != null) && entry.accessible);
	}

	/**
	 * �������� ���� "���������"
	 */
	public boolean isSelected(final String name) {
		final ApplicationEntry entry = this.appications.get(name);
		return ((entry != null) && entry.selected);
	}

	/**
	 * �������� ���� "�������������"
	 */
	public boolean isUsable(final String name) {
		final ApplicationEntry entry = this.appications.get(name);
		return ((entry != null) && entry.usable);
	}

	/**
	 * �������� ���� "�����������"
	 */
	public boolean isEnabled(final String name) {
		final ApplicationEntry entry = this.appications.get(name);
		return ((entry != null) && entry.usable && entry.accessible);
	}

	/**
	 * ���������� ���� "�����������" � boolean ��� ���� ���������
	 */
	public void setAllItemsEnabled(final boolean enabled) {
		this.setAllItemsAccessible(enabled);
	}

	/**
	 * ���������� ���� "���������" � boolean ��� ���� ���������
	 */
	public void setAllItemsAccessible(final boolean accessible) {
		for (final String key : this.appications.keySet()) {
			final ApplicationEntry applicationEntry = this.appications.get(key);
			applicationEntry.accessible = accessible;
		}
	}

	/**
	 * ���������� ���� "�������" � boolean ��� ���� ���������
	 */
	public void setAllItemsVisible(final boolean visible) {
		for (final String key : this.appications.keySet()) {
			final ApplicationEntry applicationEntry = this.appications.get(key);
			applicationEntry.visible = visible;
		}
	}

	/**
	 * ���������� ���� "�������������" � boolean ��� ���� ���������
	 */
	public void setAllItemsUsable(final boolean usable) {
		for (final String key : this.appications.keySet()) {
			final ApplicationEntry applicationEntry = this.appications.get(key);
			applicationEntry.usable = usable;
		}
	}

	/**
	 * ���������� ���� "���������" � boolean ��� ���� ���������
	 */
	public void setAllItemsSelected(final boolean selected) {
		for (final String key : this.appications.keySet()) {
			final ApplicationEntry applicationEntry = this.appications.get(key);
			applicationEntry.selected = selected;
		}
	}

	/**
	 * ���������� ������� ��� ��������
	 */
	public void setCommand(final String name, final Command command) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.command = command;
		}
	}

	/**
	 * �������� ��������� � ��������� �������
	 */
	public Command getCommand(final String name) {
		try {
			final ApplicationEntry entry = this.appications.get(name);
			if (entry == null) {
				throw new NoSuchMethodException("Command '" + name + "' not found in ApplicationModel");
			}
			return entry.command;
		} catch (NoSuchMethodException e) {
			assert Log.errorMessage(e);
			return VoidCommand.VOID_COMMAND;
		}
	}

	/**
	 * �������� ��������� - ������, ���������� ����������� �� ���������� ������
	 */
	public void addListener(final ApplicationModelListener listener) {
		this.listenerList.add(listener);
	}

	/**
	 * ������� ���������
	 */
	public void removeListener(final ApplicationModelListener listener) {
		this.listenerList.remove(listener);
	}

	/**
	 * ���������������� ���������� � ���, ��� ���������� ��������� ����
	 * ��������� ������
	 */
	public void fireModelChanged() {
		this.fireModelChanged("");
	}

	/**
	 * ���������������� ���������� � ���, ��� ���������� ��������� ��������
	 * 
	 * @param elementName -
	 *            ��� ��������
	 */
	public void fireModelChanged(final String elementName) {
		for (final ApplicationModelListener applicationModelListener : this.listenerList) {
			applicationModelListener.modelChanged(elementName);
		}
	}

	/**
	 * ���������������� ���������� � ���, ��� ���������� ��������� ���������
	 * 
	 * @param elementNames -
	 *            ������ ���� ���������
	 */
	public void fireModelChanged(final String[] elementNames) {
		for (final ApplicationModelListener applicationModelListener : this.listenerList) {
			applicationModelListener.modelChanged(elementNames);
		}
	}

	/**
	 * ������ �� �������� ������ �������� ��� ��������, ��������� � ��� �������
	 * � ����� ��������� � ����������� ������� ������������. ������������ ���
	 * ���������� ���, ��� ��� ������� ������������ ���������������
	 * 
	 * @author $Author: bass $
	 * @version $Revision: 1.13 $, $Date: 2005/10/30 15:20:24 $
	 * @module commonclient
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
		public ApplicationEntry(final String name, final Command command) {
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
				final Command command,
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
