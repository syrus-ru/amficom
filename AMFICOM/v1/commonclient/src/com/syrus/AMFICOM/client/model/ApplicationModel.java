/*
 * $Id: ApplicationModel.java,v 1.13 2005/10/30 15:20:24 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.client.model;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.syrus.util.Log;

/**
 * Модель приложения описывает действия, которые пользователь (оператор) может
 * производить с системой
 * 
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2005/10/30 15:20:24 $
 * @module commonclient
 */
public class ApplicationModel {

	/**
	 * список элементов модели
	 */
	private Map<String, ApplicationEntry> appications = new Hashtable<String, ApplicationEntry>();

	/**
	 * список объектов, получающих информацию об изменениях в модели
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
			assert Log.debugMessage("name: " + name + " , command is null ", Level.WARNING);
			this.add(name, VoidCommand.VOID_COMMAND, installed, visible, usable, accessible, selected);
		} else {
			this.appications.put(name, new ApplicationEntry(name, command, installed, visible, usable, accessible, selected));
		}
	}

	/**
	 * добавить в модель элемент с установкой флагов по умолчанию
	 */
	public void add(final String name, final Command command) {
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
	public void setSelected(final String name, final boolean selected) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.selected = selected;
		}
	}

	/**
	 * установить флаг "разрешенный" для элемента в boolean
	 */
	public void setEnabled(final String name, boolean enable) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.accessible = enable;
		}
	}

	/**
	 * установить флаг "видимый" для элемента в boolean
	 */
	public void setVisible(final String name, final boolean visible) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.visible = visible;
		}
	}

	/**
	 * установить флаг "установленный" для элемента в boolean
	 */
	public void setInstalled(final String name, final boolean installed) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.installed = installed;
		}
	}

	/**
	 * установить флаг "доступный" для элемента в boolean
	 */
	public void setAccessible(final String name, final boolean accessible) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.accessible = accessible;
		}
	}

	/**
	 * установить флаг "установленный" для элемента в boolean
	 */
	public void setUsable(final String name, final boolean usable) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.usable = usable;
		}
	}

	/**
	 * получить флаг "видимый"
	 */
	public boolean isVisible(final String name) {
		final ApplicationEntry entry = this.appications.get(name);
		return ((entry != null) && entry.visible && entry.installed);
	}

	/**
	 * получить флаг "доступный"
	 */
	public boolean isAccessible(final String name) {
		final ApplicationEntry entry = this.appications.get(name);
		return ((entry != null) && entry.accessible);
	}

	/**
	 * получить флаг "выбранный"
	 */
	public boolean isSelected(final String name) {
		final ApplicationEntry entry = this.appications.get(name);
		return ((entry != null) && entry.selected);
	}

	/**
	 * получить флаг "установленный"
	 */
	public boolean isUsable(final String name) {
		final ApplicationEntry entry = this.appications.get(name);
		return ((entry != null) && entry.usable);
	}

	/**
	 * получить флаг "разрешенный"
	 */
	public boolean isEnabled(final String name) {
		final ApplicationEntry entry = this.appications.get(name);
		return ((entry != null) && entry.usable && entry.accessible);
	}

	/**
	 * установить флаг "разрешенный" в boolean для всех элементов
	 */
	public void setAllItemsEnabled(final boolean enabled) {
		this.setAllItemsAccessible(enabled);
	}

	/**
	 * установить флаг "доступный" в boolean для всех элементов
	 */
	public void setAllItemsAccessible(final boolean accessible) {
		for (final String key : this.appications.keySet()) {
			final ApplicationEntry applicationEntry = this.appications.get(key);
			applicationEntry.accessible = accessible;
		}
	}

	/**
	 * установить флаг "видимый" в boolean для всех элементов
	 */
	public void setAllItemsVisible(final boolean visible) {
		for (final String key : this.appications.keySet()) {
			final ApplicationEntry applicationEntry = this.appications.get(key);
			applicationEntry.visible = visible;
		}
	}

	/**
	 * установить флаг "установленный" в boolean для всех элементов
	 */
	public void setAllItemsUsable(final boolean usable) {
		for (final String key : this.appications.keySet()) {
			final ApplicationEntry applicationEntry = this.appications.get(key);
			applicationEntry.usable = usable;
		}
	}

	/**
	 * установить флаг "выбранный" в boolean для всех элементов
	 */
	public void setAllItemsSelected(final boolean selected) {
		for (final String key : this.appications.keySet()) {
			final ApplicationEntry applicationEntry = this.appications.get(key);
			applicationEntry.selected = selected;
		}
	}

	/**
	 * установить команду для элемента
	 */
	public void setCommand(final String name, final Command command) {
		final ApplicationEntry entry = this.appications.get(name);
		if (entry != null) {
			entry.command = command;
		}
	}

	/**
	 * получить связанную с элементом команду
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
	 * добавить Слушателя - объект, получающий уведомление об изменениях модели
	 */
	public void addListener(final ApplicationModelListener listener) {
		this.listenerList.add(listener);
	}

	/**
	 * удалить Слушателя
	 */
	public void removeListener(final ApplicationModelListener listener) {
		this.listenerList.remove(listener);
	}

	/**
	 * проинформировать слушателей о том, что изменилось состояние всех
	 * элементов модели
	 */
	public void fireModelChanged() {
		this.fireModelChanged("");
	}

	/**
	 * проинформировать слушателей о том, что изменилось состояние элемента
	 * 
	 * @param elementName -
	 *            имя элемента
	 */
	public void fireModelChanged(final String elementName) {
		for (final ApplicationModelListener applicationModelListener : this.listenerList) {
			applicationModelListener.modelChanged(elementName);
		}
	}

	/**
	 * проинформировать слушателей о том, что изменилось состояние элементов
	 * 
	 * @param elementNames -
	 *            массив имен элементов
	 */
	public void fireModelChanged(final String[] elementNames) {
		for (final ApplicationModelListener applicationModelListener : this.listenerList) {
			applicationModelListener.modelChanged(elementNames);
		}
	}

	/**
	 * запись об элементе модели включает имя элемента, связанную с ним команду
	 * и флаги видимости и доступности команды пользователю. конструктора без
	 * параметров нет, так как элемент определяется идентификатором
	 * 
	 * @author $Author: bass $
	 * @version $Revision: 1.13 $, $Date: 2005/10/30 15:20:24 $
	 * @module commonclient
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
		public ApplicationEntry(final String name, final Command command) {
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
