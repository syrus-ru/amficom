/**
 * $Id: ApplicationModel.java,v 1.6 2004/07/28 12:56:22 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */
package com.syrus.AMFICOM.Client.General.Model;

import com.syrus.AMFICOM.Client.General.Command.Command;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.SessionInterface;
import com.syrus.AMFICOM.Client.Resource.DataSourceInterface;
import com.syrus.AMFICOM.Client.Resource.EmptyDataSource;
import com.syrus.AMFICOM.Client.Resource.RISDDataSource;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Модель приложения описывает действия, которые пользователь (оператор) 
 * может производить с системой
 * 
 * 
 * 
 * @version $Revision: 1.6 $, $Date: 2004/07/28 12:56:22 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class ApplicationModel
{
	/**
	 * запись об элементе модели включает имя элемента, связанную с ним команду 
	 * и флаги видимости и доступности команды пользователю.
	 * конструктора без параметров нет, так как элемент определяется 
	 * идентификатором
	 * 
	 * @version $Revision: 1.6 $, $Date: 2004/07/28 12:56:22 $
	 * @module
	 * @author $Author: krupenn $
	 * @see
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
		public ApplicationEntry(String name, Command command)
		{
			this.name = name;
			this.command = command;
		}

		/**
		 * по умолчанию с элементом модели связана пустая команда
		 * @param name
		 */
		public ApplicationEntry(String name)
		{
			this.name = name;
		}

		// 
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

		/**
		 * при создании нового объекта берутся копии всех членов объекта
		 * @param entry
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
		 * дублирование объекта включает создание нового объекта
		 */
		public Object clone()
		{
			return new ApplicationEntry(this);
		}
	}

///////////////////////////////////////////////////////////////////////////////
// поля и методы класса ApplicationModel
///////////////////////////////////////////////////////////////////////////////

	/**
	 * список элементов модели
	 */
	private Hashtable appHash = new Hashtable();
	
	/**
	 * список объектов, получающих информацию об изменениях в модели
	 */
	private ApplicationModelListenerList listenerList =
			new ApplicationModelListenerList();

	/**
	 * конструктор без параметров - инициализируются список элементов и
	 * список объектов, принимающих уведомление об изменениях в модели
	 */
	public ApplicationModel()
	{
		// nothing
	}

	/**
	 * при дублировании модели дублируются все элементы и объекты, принимающие
	 * информацию об изменениях в модели
	 * 
	 * @param aModel
	 */
	public ApplicationModel(ApplicationModel aModel)
	{
		// копируются Слушатели изменения модели
		for (int i = aModel.listenerList.getListenerCount() - 1; i >= 0; i--)
		{
			listenerList.add(
					aModel.listenerList.getListenerClass(i),
					aModel.listenerList.getListener(i));
		}

		// инициализируется список элементов
		appHash = new Hashtable();

		// копируются элементы модели, с тем, чтобы в каждой модели элементы
		// изменялись независимо
		for(Enumeration e = aModel.appHash.keys(); e.hasMoreElements();)
		{
			String key = (String )e.nextElement();
			ApplicationEntry entry = (ApplicationEntry )aModel.appHash.get(key);
			appHash.put(key, entry.clone());
		}
	}

	/**
	 * добавить в модель элемент с указанием всех членов
	 */
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

	/**
	 * добавить в модель элемент с установкой флагов по умолчанию
	 */
	public void add(String name, Command command)
	{
		if(command == null)
			command = new VoidCommand();
		ApplicationEntry entry = new ApplicationEntry(name, command);
		appHash.put(name, entry);
	}

	/**
	 * добавить в модель элемент с установкой флагов и команды по умолчанию
	 */
	public void add(String name)
	{
		ApplicationEntry entry = new ApplicationEntry(name);
		appHash.put(name, entry);
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
	public void select(String name)
	{
		setSelected(name, true);
	}

	/**
	 * снять флаг "выбран" для элемента
	 * @deprecated use {@link #setSelected(String, boolean) setSelected(name, false)}
	 */
	public void deselect(String name)
	{
		setSelected(name, false);
	}

	/**
	 * установить флаг "выбран" для элемента в bool
	 */
	public void setSelected(String name, boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		entry.selected = bool;
	}

	/**
	 * установить флаг "разрешенный" для элемента
	 * @deprecated use {@link #setEnabled(String, boolean) setEnabled(name, true)}
	 */
	public void enable(String name)
	{
		setEnabled(name, true);
	}

	/**
	 * снять флаг "разрешенный" для элемента
	 * @deprecated use {@link #setEnabled(String, boolean) setEnabled(name, false)}
	 */
	public void disable(String name)
	{
		setEnabled(name, false);
	}

	/**
	 * установить флаг "разрешенный" для элемента в bool
	 */
	public void setEnabled(String name, boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		entry.accessible = bool;
	}

	/**
	 * установить флаг "видимый" для элемента
	 * @deprecated use {@see #setVisible(String, boolean) setVisible(name, true)}
	 */
	public void show(String name)
	{
		setVisible(name, true);
	}

	/**
	 * снять флаг "видимый" для элемента
	 * @deprecated use {@link #setVisible(String, boolean) setVisible(name, false)}
	 */
	public void hide(String name)
	{
		setVisible(name, false);
	}

	/**
	 * установить флаг "видимый" для элемента в bool
	 */
	public void setVisible(String name, boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		entry.visible = bool;
	}

	/**
	 * установить флаг "установленный" для элемента в bool
	 */
	public void setInstalled(String name, boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		entry.installed = bool;
	}

	/**
	 * установить флаг "доступный" для элемента в bool
	 */
	public void setAccessible(String name, boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		entry.accessible = bool;
	}

	/**
	 * установить флаг "установленный" для элемента в bool
	 */
	public void setUsable(String name, boolean bool)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		entry.usable = bool;
	}

	/**
	 * получить флаг "видимый"
	 */
	public boolean isVisible(String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return false;
		return entry.visible && entry.installed;
	}

	/**
	 * получить флаг "доступный"
	 */
	public boolean isAccessible(String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return false;
		return entry.accessible;
	}

	/**
	 * получить флаг "выбранный"
	 */
	public boolean isSelected(String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return false;
		return entry.selected;
	}

	/**
	 * получить флаг "установленный"
	 */
	public boolean isUsable(String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return false;
		return entry.usable;
	}

	/**
	 * получить флаг "разрешенный"
	 */
	public boolean isEnabled(String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return false;
		return entry.usable && entry.accessible;
	}

	/**
	 * установить флаг "разрешенный" в bool для всех элементов
	 */
	public void setAllItemsEnabled(boolean bool)
	{
		setAllItemsAccessible(bool);
	}

	/**
	 * установить флаг "доступный" в bool для всех элементов
	 */
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

	/**
	 * установить флаг "видимый" в bool для всех элементов
	 */
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

	/**
	 * установить флаг "установленный" в bool для всех элементов
	 */
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

	/**
	 * установить флаг "выбранный" в bool для всех элементов
	 */
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

	/**
	 * установить команду для элемента
	 */
	public void setCommand(String name, Command command)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return;
		if(command == null)
			command = new VoidCommand();
		entry.command = command;
	}

	/**
	 * получить связанную с элементом команду
	 */
	public Command getCommand(String name)
	{
		ApplicationEntry entry = (ApplicationEntry) appHash.get(name);
		if(entry == null)
			return new VoidCommand();
		return entry.command;
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
	// 
	// 
	public void fireModelChanged(String e[])
	{
		int i;

		// возвращается ненулевой список Слушателей
		Object[] listeners = listenerList.getListenerList();
		Object[] listenerclasses = listenerList.getListenerClassList();

		// Идем по всем слушателям и каждого информируем об изменениях
		// элементов e в модели
		for (i = listeners.length - 1; i >= 0; i--)
		{
			if (listenerclasses[i] == ApplicationModelListener.class)
			{
				((ApplicationModelListener )listeners[i]).modelChanged(e);
			}
		}
	}

	/**
	 * 
	 */
	public DataSourceInterface getDataSource(SessionInterface si)
	{
		String connection = Environment.getConnectionType();
		if(connection.equals(Environment.CONNECTION_RISD))
			return new RISDDataSource(si);
		else
		if(connection.equals(Environment.CONNECTION_EMPTY))
			return new EmptyDataSource(si);
		return null;
	}

	/**
	 * дублирование модели
	 */
	public Object clone()
	{
		return new ApplicationModel(this);
	}
}
