/*
 * $Id: ApplicationModel.java,v 1.3 2005/06/06 14:52:47 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client.model;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.util.Log;

/**
 * Модель приложения описывает действия, которые пользователь (оператор) может
 * производить с системой
 * 
 * @author $Author: bob $
 * @version $Revision: 1.3 $, $Date: 2005/06/06 14:52:47 $
 * @module generalclient_v1
 */
public class ApplicationModel {

	/**
	 * список элементов модели
	 */
	private Map				appications			= new Hashtable();

	/**
	 * список объектов, получающих информацию об изменениях в модели
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
	 * добавить в модель элемент с указанием всех членов
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
	 * добавить в модель элемент с установкой флагов по умолчанию
	 */
	public void add(final String name,
					Command command) {
		if (command != null) {
			this.appications.put(name, new ApplicationEntry(name, command));
		}
	}

	/**
	 * добавить в модель элемент с установкой флагов и команды по умолчанию
	 */
	public void add(final String name) {
		this.appications.put(name, new ApplicationEntry(name));
	}

	/**
	 * получить количество элементов в модели
	 */
	public int getCount() {
		return this.appications.size();
	}

	/**
	 * установить флаг "выбран" для элемента в boolean
	 */
	public void setSelected(final String name,
							final boolean selected) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.selected = selected;
		}
	}

	/**
	 * установить флаг "разрешенный" для элемента в boolean
	 */
	public void setEnabled(	final String name,
							boolean enable) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.accessible = enable;
		}
	}

	/**
	 * установить флаг "видимый" для элемента в boolean
	 */
	public void setVisible(	final String name,
							final boolean visible) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.visible = visible;
		}
	}

	/**
	 * установить флаг "установленный" для элемента в boolean
	 */
	public void setInstalled(	final String name,
								final boolean installed) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.installed = installed;
		}
	}

	/**
	 * установить флаг "доступный" для элемента в boolean
	 */
	public void setAccessible(	final String name,
								final boolean accessible) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.accessible = accessible;
		}
	}

	/**
	 * установить флаг "установленный" для элемента в boolean
	 */
	public void setUsable(	final String name,
							final boolean usable) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.usable = usable;
		}
	}

	/**
	 * получить флаг "видимый"
	 */
	public boolean isVisible(final String name) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		return ((entry != null) && entry.visible && entry.installed);
	}

	/**
	 * получить флаг "доступный"
	 */
	public boolean isAccessible(final String name) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		return ((entry != null) && entry.accessible);
	}

	/**
	 * получить флаг "выбранный"
	 */
	public boolean isSelected(final String name) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		return ((entry != null) && entry.selected);
	}

	/**
	 * получить флаг "установленный"
	 */
	public boolean isUsable(final String name) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		return ((entry != null) && entry.usable);
	}

	/**
	 * получить флаг "разрешенный"
	 */
	public boolean isEnabled(final String name) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		return ((entry != null) && entry.usable && entry.accessible);
	}

	/**
	 * установить флаг "разрешенный" в boolean для всех элементов
	 */
	public void setAllItemsEnabled(final boolean enabled) {
		setAllItemsAccessible(enabled);
	}

	/**
	 * установить флаг "доступный" в boolean для всех элементов
	 */
	public void setAllItemsAccessible(final boolean accessible) {
		for (Iterator iterator = this.appications.keySet().iterator(); iterator.hasNext();) {
			String key = (String)iterator.next();
			ApplicationEntry applicationEntry = (ApplicationEntry) this.appications.get(key);
			applicationEntry.accessible = accessible;
		}
	}

	/**
	 * установить флаг "видимый" в boolean для всех элементов
	 */
	public void setAllItemsVisible(final boolean visible) {
		for (Iterator iterator = this.appications.keySet().iterator(); iterator.hasNext();) {
			String key = (String)iterator.next();
			ApplicationEntry applicationEntry = (ApplicationEntry) this.appications.get(key);
			applicationEntry.visible = visible;
		}
	}

	/**
	 * установить флаг "установленный" в boolean для всех элементов
	 */
	public void setAllItemsUsable(final boolean usable) {
		for (Iterator iterator = this.appications.keySet().iterator(); iterator.hasNext();) {
			String key = (String)iterator.next();
			ApplicationEntry applicationEntry = (ApplicationEntry) this.appications.get(key);
			applicationEntry.usable = usable;
		}
	}

	/**
	 * установить флаг "выбранный" в boolean для всех элементов
	 */
	public void setAllItemsSelected(final boolean selected) {
		for (Iterator iterator = this.appications.keySet().iterator(); iterator.hasNext();) {
			String key = (String)iterator.next();
			ApplicationEntry applicationEntry = (ApplicationEntry) this.appications.get(key);
			applicationEntry.selected = selected;
		}
	}

	/**
	 * установить команду для элемента
	 */
	public void setCommand(	final String name,
							final Command command) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		if (entry != null) {
			entry.command = command;
		}
	}

	/**
	 * получить связанную с элементом команду
	 */
	public Command getCommand(final String name) {
		ApplicationEntry entry = (ApplicationEntry) this.appications.get(name);
		return (entry == null) ? VoidCommand.VOID_COMMAND : entry.command;
	}

	/**
	 * добавить Слушателя - объект, получающий уведомление об изменениях модели
	 */
	public void addListener(ApplicationModelListener listener) {
		this.listenerList.add(listener);
	}

	/**
	 * удалить Слушателя
	 */
	public void removeListener(ApplicationModelListener listener) {
		this.listenerList.remove(listener);
	}

	/**
	 * проинформировать слушателей о том, что изменилось состояние всех
	 * элементов модели
	 */
	public void fireModelChanged() {
		fireModelChanged("");
	}

	/**
	 * проинформировать слушателей о том, что изменилось состояние элемента
	 * 
	 * @param elementName -
	 *            имя элемента
	 */
	public void fireModelChanged(String elementName) {
		for (Iterator iterator = this.listenerList.iterator(); iterator.hasNext();) {
			ApplicationModelListener applicationModelListener = (ApplicationModelListener) iterator.next();
			applicationModelListener.modelChanged(elementName);
		}
	}

	/**
	 * проинформировать слушателей о том, что изменилось состояние элементов
	 * 
	 * @param elementNames -
	 *            массив имен элементов
	 */
	public void fireModelChanged(String[] elementNames) {
		for (Iterator iterator = this.listenerList.iterator(); iterator.hasNext();) {
			ApplicationModelListener applicationModelListener = (ApplicationModelListener) iterator.next();
			applicationModelListener.modelChanged(elementNames);
		}
	}

	/**
	 * запись об элементе модели включает имя элемента, связанную с ним команду
	 * и флаги видимости и доступности команды пользователю. конструктора без
	 * параметров нет, так как элемент определяется идентификатором
	 * 
	 * @author $Author: bob $
	 * @version $Revision: 1.3 $, $Date: 2005/06/06 14:52:47 $
	 * @module generalclient_v1
	 */
	class ApplicationEntry {

		/**
		 * выделен ли пункт (для пункта меню - отмечен галочкой, для западающей
		 * кнопки - западание
		 */
		boolean	selected	= false;

		/**
		 * виден ли пункт в текущей инсталлированной конфигурации
		 */
		boolean	installed	= true;

		/**
		 * виден ли пункт в текущем контексте
		 */
		boolean	visible		= true;

		/**
		 * доступен ли пункт в текущей инсталлированной конфигурации
		 */
		boolean	usable		= true;

		/**
		 * доступен ли пункт в текущем контексте
		 */
		boolean	accessible	= true;

		/**
		 * идентификатор элемента меню
		 */
		String	name;

		/**
		 * комманда, ассоциируемая с данным пунктом
		 */
		Command	command;

		/**
		 * конструктор с указанием имени элемента и связанной с ним команды. по
		 * умолчанию логические поля элемента устанавливаются следующим образом:
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
		 * по умолчанию с элементом модели связана пустая команда
		 * 
		 * @param name
		 */
		public ApplicationEntry(final String name) {
			this.name = name;
		}

		/**
		 * конструктор с инициализацией всех членов класса
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
