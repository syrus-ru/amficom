//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * ����������� ��������� ������������ � ����������                      * //
// *                                                                      * //
// * ������: ������� - ������� ������������������� ��������������������   * //
// *         ����������������� �������� � ���������� �����������          * //
// *                                                                      * //
// *         ���������� ��������������� ������� �����������               * //
// *                                                                      * //
// * ��������: ������ ���������� ��������� ��������, ������� ������������ * //
// *         (��������) ����� ����������� � ��������                      * //
// *                                                                      * //
// * ���: Java 1.2.2                                                      * //
// *                                                                      * //
// * �����: ����������� �.�.                                              * //
// *                                                                      * //
// * ������: 0.1                                                          * //
// * ��: 16 jul 2002                                                      * //
// * ������������: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        General\Model\ApplicationModel.java                           * //
// *                                                                      * //
// * ����� ����������: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * ����������: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * ������: ����������                                                   * //
// *                                                                      * //
// * ���������:                                                           * //
// *  ���         ����   �����      �����������                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * ��������:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.EmptyDataSource;
import com.syrus.AMFICOM.Client.Resource.RISDDataSource;

import java.util.Enumeration;
import java.util.Hashtable;

// ������ ����������
public class ApplicationModel
{
	// ������ �� �������� ������ �������� ��� ��������, ��������� � ��� ������� �
	// ����� ��������� � ����������� ������� ������������.
	// ������������ ��� ���������� ���, ��� ��� ������� ������������ ���������������
	class ApplicationEntry
	{
		boolean selected = false;	// ������� �� ����� (��� ������ ���� - �������
									// ��������, ��� ���������� ������ - ���������
		boolean installed = true;	// ����� �� ����� � ������� ����������������
									// ������������
		boolean visible = true;		// ����� �� ����� � ������� ���������
		boolean usable = true;		// �������� �� ����� � ������� ����������������
									// ������������
		boolean accessible = true;	// �������� �� ����� � ������� ���������
		String name;				// ������������� �������� ����
		Command command = new VoidCommand();
									// ��������, ������������� � ������ �������

		// ����������� � ��������� ����� �������� � ��������� � ��� �������.
		// �� ��������� ���������� ���� �������� ��������������� ��������� �������:
		//  selected = false
		//  visible = true
		//  usable = true
		//  accessible = true
		public ApplicationEntry(String name, Command command)
		{
			this.name = name;
			this.command = command;
		}

		// �� ��������� � ��������� ������ ������� ������ �������
		public ApplicationEntry(String name)
		{
			this.name = name;
		}

		// ����������� � �������������� ���� ������ ������
		public ApplicationEntry(
				String name,
				Command command,
				boolean installed,
				boolean visible,
				boolean usable,
				boolean accessible,
				boolean selected)
		{
			this.name = name;
			this.installed = installed;
			this.visible = visible;
			this.usable = usable;
			this.accessible = accessible;
			this.selected = selected;
			this.command = command;
		}

		// ��� �������� ������ ������� ������� ����� ���� ������ �������
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

		// ������������ ������� �������� �������� ������ �������
		public Object clone()
		{
			return new ApplicationEntry(this);
		}
	}

///////////////////////////////////////////////////////////////////////////////
// ���� � ������ ������ ApplicationModel
///////////////////////////////////////////////////////////////////////////////
	private Hashtable appHash = new Hashtable();	// ������ ��������� ������
//	private com.syrus.AMFICOM.Client.Test.Hashtable appHash = new com.syrus.AMFICOM.Client.Test.Hashtable();	// ������ ��������� ������
	private ApplicationModelListenerList listenerList =
			new ApplicationModelListenerList();		// ������ ��������, �������-
													// ��� ���������� ��
													// ���������� � ������

	// ����������� ��� ���������� - ���������������� ������ ��������� �
	// ������ ��������, ����������� ����������� �� ���������� � ������
	public ApplicationModel()
	{
		// nothing
	}

	// ��� ������������ ������ ����������� ��� �������� � �������, �����������
	// ���������� �� ���������� � ������
	public ApplicationModel(ApplicationModel aModel)
	{
		
			// ���������� ��������� ��������� ������
		for (int i = aModel.listenerList.getListenerCount() - 1; i >= 0; i--)
		{
			listenerList.add(
					aModel.listenerList.getListenerClass(i),
					aModel.listenerList.getListener(i));
		}
			// ���������������� ������ ���������
		appHash = new Hashtable();
//		appHash = new com.syrus.AMFICOM.Client.Test.Hashtable();
			// ���������� �������� ������, � ���, ����� � ������ ������ ��������
			// ���������� ����������
		for(Enumeration e = aModel.appHash.keys(); e.hasMoreElements();)
		{
			String key = (String )e.nextElement();
			ApplicationEntry entry = (ApplicationEntry )aModel.appHash.get(key);
			appHash.put(key, entry.clone());
		}
	}

	// �������� � ������ ������� � ��������� ���� ������
	public void add(
			String name,
			Command command,
			boolean installed,
			boolean visible,
			boolean usable,
			boolean accessible,
			boolean selected)
	{
		if(command == null)
			command = new VoidCommand();
		ApplicationEntry entry = new ApplicationEntry(
				name,
				command,
				installed,
				visible,
				usable,
				accessible,
				selected);
		appHash.put(name, entry);
	}

	// �������� � ������ ������� � ���������� ������ �� ���������
	public void add(String name, Command command)
	{
		if(command == null)
			command = new VoidCommand();
		ApplicationEntry entry = new ApplicationEntry(name, command);
		appHash.put(name, entry);
	}

	// �������� � ������ ������� � ���������� ������ � ������� �� ���������
	public void add(String name)
	{
		ApplicationEntry entry = new ApplicationEntry(name);
		appHash.put(name, entry);
	}

	// �������� ���������� ��������� � ������
	public int getCount()
	{
		return appHash.size();
	}

	// ���������� ���� "������" ��� ��������
	public void select(String name)
	{
		setSelected(name, true);
	}

	// ����� ���� "������" ��� ��������
	public void deselect(String name)
	{
		setSelected(name, false);
	}

	// ���������� ���� "������" ��� �������� � bool
	public void setSelected(String name, boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		entry.selected = bool;
	}

	// ���������� ���� "�����������" ��� ��������
	public void enable(String name)
	{
		setEnabled(name, true);
	}

	// ����� ���� "�����������" ��� ��������
	public void disable(String name)
	{
		setEnabled(name, false);
	}

	// ���������� ���� "�����������" ��� �������� � bool
	public void setEnabled(String name, boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		entry.accessible = bool;
	}

	// ���������� ���� "�������" ��� ��������
	public void show(String name)
	{
		setVisible(name, true);
	}

	// ����� ���� "�������" ��� ��������
	public void hide(String name)
	{
		setVisible(name, false);
	}

	// ���������� ���� "�������" ��� �������� � bool
	public void setVisible(String name, boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		entry.visible = bool;
	}

	// ���������� ���� "�������������" ��� �������� � bool
	public void setInstalled(String name, boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		entry.installed = bool;
	}

	// ���������� ���� "���������" ��� �������� � bool
	public void setAccessible(String name, boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		entry.accessible = bool;
	}

	// ���������� ���� "�������������" ��� �������� � bool
	public void setUsable(String name, boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		entry.usable = bool;
	}

	// �������� ���� "�������"
	public boolean isVisible(String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return false;
		return entry.visible && entry.installed;
	}

	// �������� ���� "���������"
	public boolean isAccessible(String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return false;
		return entry.accessible;
	}

	// �������� ���� "���������"
	public boolean isSelected(String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return false;
		return entry.selected;
	}

	// �������� ���� "�������������"
	public boolean isUsable(String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return false;
		return entry.usable;
	}

	// �������� ���� "�����������"
	public boolean isEnabled(String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return false;
		return entry.usable && entry.accessible;
	}

	// ���������� ���� "�����������" � bool ��� ���� ���������
	public void setAllItemsEnabled(boolean bool)
	{
		setAllItemsAccessible(bool);
	}

	// ���������� ���� "���������" � bool ��� ���� ���������
	public void setAllItemsAccessible(boolean bool)
	{
		ApplicationEntry entry;
		Enumeration enum = appHash.elements();
		for(;enum.hasMoreElements();)
		{
			entry = (ApplicationEntry) enum.nextElement();
			entry.accessible = bool;
		}
	}

	// ���������� ���� "�������" � bool ��� ���� ���������
	public void setAllItemsVisible(boolean bool)
	{
		ApplicationEntry entry;
		Enumeration enum = appHash.elements();
		for(;enum.hasMoreElements();)
		{
			entry = (ApplicationEntry) enum.nextElement();
			entry.visible = bool;
		}
	}

	// ���������� ���� "�������������" � bool ��� ���� ���������
	public void setAllItemsUsable(boolean bool)
	{
		ApplicationEntry entry;
		Enumeration enum = appHash.elements();
		for(;enum.hasMoreElements();)
		{
			entry = (ApplicationEntry) enum.nextElement();
			entry.usable = bool;
		}
	}

	// ���������� ���� "���������" � bool ��� ���� ���������
	public void setAllItemsSelected(boolean bool)
	{
		ApplicationEntry entry;
		Enumeration enum = appHash.elements();
		for(;enum.hasMoreElements();)
		{
			entry = (ApplicationEntry) enum.nextElement();
			entry.selected = bool;
		}
	}

	// ���������� ������� ��� ��������
	public void setCommand(String name, Command command)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		if(command == null)
			command = new VoidCommand();
		entry.command = command;
	}

	// �������� ��������� � ��������� �������
	public Command getCommand(String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return new VoidCommand();
		return entry.command;
	}

	// �������� ��������� - ������, ���������� ����������� �� ���������� ������
	public void addListener(ApplicationModelListener l)
	{
		listenerList.add(ApplicationModelListener.class, l);
	}

	// ������� ���������
	public void removeListener(ApplicationModelListener l)
	{
		listenerList.remove(ApplicationModelListener.class, l);
	}

	// ���������������� ���������� � ���, ��� ���������� ��������� ��������
	// s - ��� ��������
	public void fireModelChanged(String e)
	{
		fireModelChanged(new String [] { e });
	}

	// ���������������� ���������� � ���, ��� ���������� ��������� ���������
	// e - ������ ���� ���������
	public void fireModelChanged(String e[])
	{
		int i;
		// ������������ ��������� ������ ����������
		Object[] listeners = listenerList.getListenerList();
		Object[] listenerclasses = listenerList.getListenerClassList();
		// ���� �� ���� ���������� � ������� ����������� �� ����������
		// ��������� e � ������
		for (i = listeners.length - 1; i >= 0; i--)
		{
			if (listenerclasses[i] == ApplicationModelListener.class)
			{
				((ApplicationModelListener )listeners[i]).modelChanged(e);
			}
		}
	}

	public DataSourceInterface getDataSource(SessionInterface si)
	{
		String connection = Environment.getConnectionType();
		if(connection.equals("RISD"))
			return new RISDDataSource(si);
		else
		if(connection.equals("Empty"))
			return new EmptyDataSource(si);
		return null;
	}

	// ������������ ������
	public Object clone()
	{
		return new ApplicationModel(this);
	}
}

