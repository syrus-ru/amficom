//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Список получателей уведомления об изменении модели         * //
// *         приложения                                                   * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 0.1                                                          * //
// * От: 16 jul 2002                                                      * //
// * Расположение: ISM\prog\java\AMFICOMMain\com\syrus\AMFICOM\Client\    * //
// *        General\Model\ApplicationModelListenerList.java               * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.Client.General.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * A class which holds a list of EventListeners.  A single instance
 * can be used to hold all listeners (of all types) for the instance
 * using the lsit.  It is the responsiblity of the class using the
 * EventListenerList to provide type-safe API (preferably conforming
 * to the JavaBeans spec) and methods which dispatch event notification
 * methods to appropriate Event Listeners on the list.
 *
 * The main benefits which this class provides are that it is relatively
 * cheap in the case of no listeners, and provides serialization for
 * eventlistener lists in a single place, as well as a degree of MT safety
 * (when used correctly).
 *
 * Usage example:
 *    Say one is defining a class which sends out FooEvents, and wantds
 * to allow users of the class to register FooListeners and receive
 * notification when FooEvents occur.  The following should be added
 * to the class definition:
   <pre>
   EventListenerList listenrList = new EventListnerList();
   FooEvent fooEvent = null;

   public void addFooListener(FooListener l) {
	   listenerList.add(FooListener.class, l);
   }

   public void removeFooListener(FooListener l) {
	   listenerList.remove(FooListener.class, l);
   }


	// Notify all listeners that have registered interest for
	// notification on this event type.  The event instance
	// is lazily created using the parameters passed into
	// the fire method.

	protected void firefooXXX() {
	// Guaranteed to return a non-null array
	Object[] listeners = listenerList.getListenerList();
	// Process the listeners last to first, notifying
	// those that are interested in this event
	for (int i = listeners.length-2; i>=0; i-=2) {
	    if (listeners[i]==FooListener.class) {
		// Lazily create the event:
		if (fooEvent == null)
		    fooEvent = new FooEvent(this);
		((FooListener)listeners[i+1]).fooXXX(fooEvent);
	    }	       
	}
    }	
   </pre>
 * foo should be changed to the appropriate name, and Method to the
 * appropriate method name (one fire method should exist for each
 * notification method in the FooListener interface).
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with 
 * future Swing releases.  The current serialization support is appropriate
 * for short term storage or RMI between applications running the same
 * version of Swing.  A future release of Swing will provide support for
 * long term persistence.
 *
 * @version 1.23 10/01/98
 * @author Georges Saab
 * @author Hans Muller
 * @author James Gosling
 */
public class ApplicationModelListenerList implements Serializable
{
	/* A null array to be shared by all empty listener lists*/
	private final static Object[] NULL_ARRAY = new Object[0];
	/* The list of ListenerType - Listener pairs */
	protected transient Object[] listenerList = NULL_ARRAY;
	protected transient Object[] listenerClassList = NULL_ARRAY;

	/**
	 * This passes back the event listener list as an array
	 * of ListenerType - listener pairs.  Note that for
	 * performance reasons, this implementation passes back
	 * the actual data structure in which the listner data
	 * is stored internally!
	 * This method is guaranteed to pass back a non-null
	 * array, so that no null-checking is required in
	 * fire methods.  A zero-length array of Object should
	 * be returned if there are currently no listeners.
	 *
	 * WARNING!!! Absolutely NO modification of
	 * the data contained in this array should be made -- if
	 * any such manipulation is necessary, it should be done
	 * on a copy of the array returned rather than the array
	 * itself.
	 */
	public Object[] getListenerList()
	{
		return listenerList;
	}

	public Object[] getListenerClassList()
	{
		return listenerClassList;
	}

	/**
	 * Return the total number of listeners for this listenerlist
	 */
	public int getListenerCount()
	{
		return listenerList.length;
	}

	/**
	 * Return the total number of listeners of the supplied type
	 * for this listenerlist.
	 */
	public int getListenerCount(Class t)
	{
		int i;
		int count = 0;
		Object[] lList = listenerClassList;

		for (i = 0; i < lList.length; i++)
		{
			if (t == (Class)lList[i])
				count++;
		}
		return count;
	}
	/**
	 * Add the listener as a listener of the specified type.
	 * @param t the type of the listener to be added
	 * @param l the listener to be added
	 */
	public synchronized void add(Class t, ApplicationModelListener l)
	{
		if (l==null)
		{
			// In an ideal world, we would do an assertion here
			// to help developers know they are probably doing
			// something wrong
			return;
		}
		if (!t.isInstance(l))
		{
			throw new IllegalArgumentException("Listener " + l +
					 " is not of type " + t);
		}
		if (listenerList == NULL_ARRAY)
		{
			// if this is the first listener added,
			// initialize the lists
			listenerList = new Object[] { l };
			listenerClassList = new Object[] { t };
		}
		else
		{
			// Otherwise copy the array and add the new listener
			int i = listenerList.length;
			Object[] tmp = new Object[i + 1];
			Object[] tmp2 = new Object[i + 1];
			System.arraycopy(listenerList, 0, tmp, 0, i);
			System.arraycopy(listenerClassList, 0, tmp2, 0, i);

			tmp[i] = l;
			tmp2[i] = t;

			listenerList = tmp;
			listenerClassList = tmp2;
		}
	}

	/**
	 * Remove the listener as a listener of the specified type.
	 * @param t the type of the listener to be removed
	 * @param l the listener to be removed
	 */
	public synchronized void remove(Class t, ApplicationModelListener l)
	{
		int index;
		int i;
		if (l == null)
		{
			// In an ideal world, we would do an assertion here
			// to help developers know they are probably doing
			// something wrong
			return;
		}
		if (!t.isInstance(l))
		{
			throw new IllegalArgumentException("Listener " + l +
					 " is not of type " + t);
		}
		index = -1;
		// Is l on the list?
		for (i = listenerList.length - 1; i >= 0; i--)
		{
			if ((listenerClassList[i] == t) &&
				(listenerList[i].equals(l)) )
			{
				index = i;
				break;
			}
		}

		// If so,  remove it
		if (index != -1)
		{
			Object[] tmp = new Object[listenerList.length - 1];
			Object[] tmp2 = new Object[listenerClassList.length - 1];
			// Copy the list up to index
			System.arraycopy(listenerList, 0, tmp, 0, index);
			System.arraycopy(listenerClassList, 0, tmp2, 0, index);
			// Copy from two past the index, up to
			// the end of tmp (which is two elements
			// shorter than the old list)
			if (index < tmp.length)
			{
				System.arraycopy(listenerList, index + 1, tmp, index,
					 tmp.length - index);
				System.arraycopy(listenerClassList, index + 1, tmp2, index,
					 tmp2.length - index);
			}
			// set the listener array to the new array or null
			listenerList = (tmp.length == 0) ? NULL_ARRAY : tmp;
			listenerClassList = (tmp2.length == 0) ? NULL_ARRAY : tmp2;
		}
	}

	// Serialization support.
	private void writeObject(ObjectOutputStream s) throws IOException
	{
		int i;
		Object[] lList = listenerList;
		Object[] lList2 = listenerClassList;
		s.defaultWriteObject();

		// Save the non-null event listeners:
		for (i = 0; i < lList.length; i++)
		{
			Class t = (Class )lList2[i];
			ApplicationModelListener l = (ApplicationModelListener )lList[i];
			if ((l != null) && (l instanceof Serializable))
			{
				s.writeObject(t.getName());
				s.writeObject(l);
			}
		}

		s.writeObject(null);
	}

	private void readObject(ObjectInputStream s)
			throws IOException, ClassNotFoundException
	{
		listenerList = NULL_ARRAY;
		s.defaultReadObject();
		Object listenerTypeOrNull;

		while (null != (listenerTypeOrNull = s.readObject()))
		{
			ApplicationModelListener l = (ApplicationModelListener )s.readObject();
			add(Class.forName((String )listenerTypeOrNull), l);
		}
	}

	public String toString()
	{
		int i;
		Object[] lList = listenerList;
		Object[] lList2 = listenerClassList;
		String s = "ApplicationModelListenerList: ";
		s += lList.length + " listeners: ";
		for (i = 0 ; i <= lList.length - 1; i++)
		{
			s += " type " + ((Class)lList2[i]).getName();
			s += " listener " + lList[i];
		}
		return s;
	}

	public Class getListenerClass(int index)
	{
		return (Class )listenerClassList[index];
	}

	public ApplicationModelListener getListener(int index)
	{
		return (ApplicationModelListener )listenerList[index];
	}
}
