/*
 * $Id: ApplicationModel.java,v 1.7 2004/09/27 09:58:44 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.*;
import java.util.*;

/**
 * Модель приложения описывает действия, которые пользователь (оператор) 
 * может производить с системой
 * 
 * @author $Author: bass $
 * @version $Revision: 1.7 $, $Date: 2004/09/27 09:58:44 $
 * @module generalclient_v1
 */
public class ApplicationModel
{
	protected SessionInterface session = null;

	protected DataSourceInterface dataSource = null;

	/**
	 * список элементов модели
	 */
	private Hashtable appHash = new Hashtable();
	
	/**
	 * список объектов, получающих информацию об изменениях в модели
	 */
	private ApplicationModelListenerList listenerList
		= new ApplicationModelListenerList();

	private static ApplicationModel instance = null;

	/**
	 * @deprecated This constructor shouldn't be used directly, and in the
	 *             future its visibility will be narrowed to
	 *             <code>protected</code> to allow it to be invoked by
	 *             descendants only. To obtain a shared instance, use
	 *             {@link #getInstance()} instead.
	 * @see #getInstance()
	 */
	public ApplicationModel()
	{
	}

	/**
	 * при дублировании модели дублируются все элементы и объекты, принимающие
	 * информацию об изменениях в модели
	 * 
	 * @param aModel
	 * @deprecated Used only in {@link #clone()}.
	 */
	public ApplicationModel(ApplicationModel aModel)
	{
		// копируются Слушатели изменения модели
		for (int i = aModel.listenerList.getListenerCount() - 1; i >= 0; i--)
			listenerList.add(aModel.listenerList.getListenerClass(i), aModel.listenerList.getListener(i));

		// инициализируется список элементов
		appHash = new Hashtable();

		// копируются элементы модели, с тем, чтобы в каждой модели элементы
		// изменялись независимо
		for (Enumeration e = aModel.appHash.keys(); e.hasMoreElements();)
		{
			String key = (String )e.nextElement();
			appHash.put(key, ((ApplicationEntry) (aModel.appHash.get(key))).clone());
		}
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
	 * добавить в модель элемент с указанием всех членов
	 */
	public void add(final String name, Command command, final boolean installed, final boolean visible, final boolean usable, final boolean accessible, final boolean selected)
	{
		if (command == null)
			add(name, new VoidCommand(), installed, visible, usable, accessible, selected);
		else
			appHash.put(name, new ApplicationEntry(name, command, installed, visible, usable, accessible, selected));
	}

	/**
	 * добавить в модель элемент с установкой флагов по умолчанию
	 */
	public void add(final String name, Command command)
	{
		if (command == null)
			add(name, new VoidCommand());
		else
			appHash.put(name, new ApplicationEntry(name, command));
	}

	/**
	 * добавить в модель элемент с установкой флагов и команды по умолчанию
	 */
	public void add(final String name)
	{
		appHash.put(name, new ApplicationEntry(name));
	}

	/**
	 * получить количество элементов в модели
	 */
	public int getCount()
	{
		return appHash.size();
	}

	/**
	 * установить флаг "выбран" для элемента
	 * @deprecated use {@link #setSelected(String, boolean) setSelected(name, true)}
	 */
	public void select(final String name)
	{
		setSelected(name, true);
	}

	/**
	 * снять флаг "выбран" для элемента
	 * @deprecated use {@link #setSelected(String, boolean) setSelected(name, false)}
	 */
	public void deselect(final String name)
	{
		setSelected(name, false);
	}

	/**
	 * установить флаг "выбран" для элемента в bool
	 */
	public void setSelected(final String name, final boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		if (entry != null)
			entry.selected = bool;
	}

	/**
	 * установить флаг "разрешенный" для элемента
	 * @deprecated use {@link #setEnabled(String, boolean) setEnabled(name, true)}
	 */
	public void enable(final String name)
	{
		setEnabled(name, true);
	}

	/**
	 * снять флаг "разрешенный" для элемента
	 * @deprecated use {@link #setEnabled(String, boolean) setEnabled(name, false)}
	 */
	public void disable(final String name)
	{
		setEnabled(name, false);
	}

	/**
	 * установить флаг "разрешенный" для элемента в bool
	 */
	public void setEnabled(final String name, boolean accessible)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		if (entry != null)
			entry.accessible = accessible;
	}

	/**
	 * установить флаг "видимый" для элемента
	 * @deprecated use {@see #setVisible(String, boolean) setVisible(name, true)}
	 */
	public void show(final String name)
	{
		setVisible(name, true);
	}

	/**
	 * снять флаг "видимый" для элемента
	 * @deprecated use {@link #setVisible(String, boolean) setVisible(name, false)}
	 */
	public void hide(final String name)
	{
		setVisible(name, false);
	}

	/**
	 * установить флаг "видимый" для элемента в bool
	 */
	public void setVisible(final String name, final boolean visible)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		if (entry != null)
			entry.visible = visible;
	}

	/**
	 * установить флаг "установленный" для элемента в bool
	 */
	public void setInstalled(final String name, final boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		if (entry != null)
			entry.installed = bool;
	}

	/**
	 * установить флаг "доступный" для элемента в bool
	 */
	public void setAccessible(final String name, final boolean accessible)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		if (entry != null)
			entry.accessible = accessible;
	}

	/**
	 * установить флаг "установленный" для элемента в bool
	 */
	public void setUsable(final String name, final boolean usable)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		if (entry != null)
			entry.usable = usable;
	}

	/**
	 * получить флаг "видимый"
	 */
	public boolean isVisible(final String name)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		return ((entry != null) && entry.visible && entry.installed);
	}

	/**
	 * получить флаг "доступный"
	 */
	public boolean isAccessible(final String name)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		return ((entry != null) && entry.accessible);
	}

	/**
	 * получить флаг "выбранный"
	 */
	public boolean isSelected(final String name)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		return ((entry != null) && entry.selected);
	}

	/**
	 * получить флаг "установленный"
	 */
	public boolean isUsable(final String name)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		return ((entry != null) && entry.usable);
	}

	/**
	 * получить флаг "разрешенный"
	 */
	public boolean isEnabled(final String name)
	{
		ApplicationEntry entry = (ApplicationEntry) (appHash.get(name));
		return ((entry != null) && entry.usable && entry.accessible);
	}

	/**
	 * установить флаг "разрешенный" в bool для всех элементов
	 */
	public void setAllItemsEnabled(final boolean enabled)
	{
		setAllItemsAccessible(enabled);
	}

	/**
	 * установить флаг "доступный" в bool для всех элементов
	 */
	public void setAllItemsAccessible(final boolean accessible)
	{
		for(Enumeration e = appHash.elements(); e.hasMoreElements();)
			((ApplicationEntry) (e.nextElement())).accessible = accessible;
	}

	/**
	 * установить флаг "видимый" в bool для всех элементов
	 */
	public void setAllItemsVisible(final boolean visible)
	{
		for (Enumeration e = appHash.elements(); e.hasMoreElements();)
			((ApplicationEntry) (e.nextElement())).visible = visible;
	}

	/**
	 * установить флаг "установленный" в bool для всех элементов
	 */
	public void setAllItemsUsable(final boolean usable)
	{
		for (Enumeration e = appHash.elements(); e.hasMoreElements();)
			((ApplicationEntry) (e.nextElement())).usable = usable;
	}

	/**
	 * установить флаг "выбранный" в bool для всех элементов
	 */
	public void setAllItemsSelected(final boolean selected)
	{
		for (Enumeration e = appHash.elements(); e.hasMoreElements();)
			((ApplicationEntry) (e.nextElement())).selected = selected;
	}

	/**
	 * установить команду для элемента
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
	 * получить связанную с элементом команду
	 */
	public Command getCommand(final String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		return ((entry == null) ? (new VoidCommand()) : entry.command);
	}

	/**
	 * добавить Слушателя - объект, получающий уведомление об изменениях модели
	 */
	public void addListener(ApplicationModelListener l)
	{
		listenerList.add(ApplicationModelListener.class, l);
	}

	/**
	 * удалить Слушателя
	 */
	public void removeListener(ApplicationModelListener l)
	{
		listenerList.remove(ApplicationModelListener.class, l);
	}

	/**
	 * проинформировать слушателей о том, что изменилось состояние 
	 * всех элементов модели
	 */
	public void fireModelChanged()
	{
		fireModelChanged(new String [] { "" });
	}

	/**
	 * проинформировать слушателей о том, что изменилось состояние элемента
	 * @param s - имя элемента
	 */
	public void fireModelChanged(String e)
	{
		fireModelChanged(new String [] { e });
	}

	/**
	 * проинформировать слушателей о том, что изменилось состояние элементов
	 * @param e - массив имен элементов
	 */
	public void fireModelChanged(String e[])
	{
		// возвращается ненулевой список Слушателей
		Object[] listeners = listenerList.getListenerList();
		Object[] listenerclasses = listenerList.getListenerClassList();

		// Идем по всем слушателям и каждого информируем об изменениях
		// элементов e в модели
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
	 * @deprecated Incorrectly implemented and never used.
	 */
	public Object clone()
	{
		return new ApplicationModel(this);
	}

	/**
	 * запись об элементе модели включает имя элемента, связанную с ним команду 
	 * и флаги видимости и доступности команды пользователю.
	 * конструктора без параметров нет, так как элемент определяется 
	 * идентификатором
	 * 
	 * @author $Author: bass $
	 * @version $Revision: 1.7 $, $Date: 2004/09/27 09:58:44 $
	 * @module generalclient_v1
	 */
	class ApplicationEntry
	{
		/**
		 * выделен ли пункт (для пункта меню - отмечен галочкой, для 
		 * западающей кнопки - западание
		 */
		boolean selected = false;

		/**
		 * виден ли пункт в текущей инсталлированной конфигурации
		 */
		boolean installed = true;

		/**
		 * виден ли пункт в текущем контексте
		 */
		boolean visible = true;
		
		/**
		 * доступен ли пункт в текущей инсталлированной конфигурации
		 */
		boolean usable = true;

		/**
		 * доступен ли пункт в текущем контексте
		 */
		boolean accessible = true;
		
		/**
		 * идентификатор элемента меню
		 */
		String name;

		/**
		 * комманда, ассоциируемая с данным пунктом
		 */
		Command command = new VoidCommand();

		/**
		 * конструктор с указанием имени элемента и связанной с ним команды.
		 * по умолчанию логические поля элемента устанавливаются следующим 
		 * образом:
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
		 * по умолчанию с элементом модели связана пустая команда
		 * @param name
		 */
		public ApplicationEntry(final String name)
		{
			this.name = name;
		}

		/**
		 * конструктор с инициализацией всех членов класса
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
