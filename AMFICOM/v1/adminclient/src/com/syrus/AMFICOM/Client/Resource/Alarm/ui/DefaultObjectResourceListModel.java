package com.syrus.AMFICOM.Client.Resource.Alarm.ui;

import java.util.*;
import javax.swing.AbstractListModel;

/**
 * A re-implementation of {@link javax.swing.DefaultListModel} using
 * {@link java.util.ArrayList} instead of {@link java.util.Vector}
 *
 * @version $Revision: 1.1 $, $Date: 2004/06/24 10:53:57 $
 * @author $Author: bass $
 */
final class DefaultObjectResourceListModel extends AbstractListModel {
	private ArrayList delegate = new ArrayList();

	private static final long serialVersionUID = -5793411647208771030L;

	public int getSize() {
		synchronized (delegate) {
			return delegate.size();
		}
	}

	public Object getElementAt(int index) {
		synchronized (delegate) {
			return delegate.get(index);
		}
	}

	public void copyInto(Object anArray[]) {
		synchronized (delegate) {
			delegate.toArray(anArray);
		}
	}

	public void trimToSize() {
		synchronized (delegate) {
			delegate.trimToSize();
		}
	}

	public void ensureCapacity(int minCapacity) {
		synchronized (delegate) {
			delegate.ensureCapacity(minCapacity);
		}
	}

	public void setSize(int newSize) {
		synchronized (delegate) {
			int oldSize = delegate.size();
			if (oldSize < newSize) {
				delegate.ensureCapacity(newSize);
				fireIntervalAdded(this, oldSize, newSize - 1);
			} else if (oldSize > newSize) {
				for (int i = (oldSize - 1); i > (newSize - 1); i --)
					delegate.remove(i);
				fireIntervalRemoved(this, newSize, oldSize - 1);
			}
		}
	}

	public int capacity() {
		throw new NoSuchMethodError();
	}

	public int size() {
		synchronized (delegate) {
			return delegate.size();
		}
	}

	public boolean isEmpty() {
		synchronized (delegate) {
			return delegate.isEmpty();
		}
	}

	public Enumeration elements() {
		return new Enumeration() {
			int count = 0;

			public boolean hasMoreElements() {
				synchronized (delegate) {
					return count < delegate.size();
				}
			}

			public Object nextElement() {
				synchronized (delegate) {
					if (count < delegate.size())
						return delegate.get(count ++);
				}
				throw new NoSuchElementException("ArrayList Enumeration");
			}
		};
	}

	public boolean contains(Object elem) {
		synchronized (delegate) {
			return delegate.contains(elem);
		}
	}

	public int indexOf(Object elem) {
		synchronized (delegate) {
			return delegate.indexOf(elem);
		}
	}

	public int indexOf(Object elem, int index) {
		synchronized (delegate) {
			int size = delegate.size();
			if (elem == null)
				for (int i = index; i < size; i ++) {
					if (delegate.get(i) == null)
						return i;
				}
			else
				for (int i = index; i < size; i ++)
					if (elem.equals(delegate.get(i)))
						return i;
			return - 1;
		}
	}

	public int lastIndexOf(Object elem) {
		synchronized (delegate) {
			return delegate.lastIndexOf(elem);
		}
	}

	public int lastIndexOf(Object elem, int index) {
		synchronized (delegate) {
			int size = delegate.size();
			if (index >= size)
				throw new IndexOutOfBoundsException(index + " >= " + size);
			if (elem == null)
				for (int i = index; i >= 0; i --) {
					if (delegate.get(i) == null)
						return i;
				}
			else
				for (int i = index; i >= 0; i--)
					if (elem.equals(delegate.get(i)))
						return i;
			return - 1;
		}
	}

	public Object elementAt(int index) {
		synchronized (delegate) {
			return delegate.get(index);
		}
	}

	public Object firstElement() {
		synchronized (delegate) {
			if (delegate.size() == 0)
				throw new NoSuchElementException();
			return delegate.get(0);
		}
	}

	public Object lastElement() {
		synchronized (delegate) {
			int size = delegate.size();
			if (size == 0)
				throw new NoSuchElementException();
			return delegate.get(size - 1);
		}
	}

	public void setElementAt(Object obj, int index) {
		synchronized (delegate) {
			delegate.set(index, obj);
		}
		fireContentsChanged(this, index, index);
	}

	public void removeElementAt(int index) {
		synchronized (delegate) {
			delegate.remove(index);
		}
		fireIntervalRemoved(this, index, index);
	}

	public void insertElementAt(Object obj, int index) {
		synchronized (delegate) {
			delegate.add(index, obj);
		}
		fireIntervalAdded(this, index, index);
	}

	public void addElement(Object obj) {
		int index;
		synchronized (delegate) {
			index = delegate.size();
			delegate.add(obj);
		}
		fireIntervalAdded(this, index, index);
	}

	public boolean removeElement(Object obj) {
		int index = indexOf(obj);
		boolean rv;
		synchronized (delegate) {
			rv = delegate.remove(obj);
		}
		if (index >= 0)
			fireIntervalRemoved(this, index, index);
		return rv;
	}

	public void removeAllElements() {
		int index1;
		synchronized (delegate) {
			index1 = delegate.size() - 1;
			delegate.clear();
		}
		if (index1 >= 0)
			fireIntervalRemoved(this, 0, index1);
	}

	public String toString() {
		synchronized (delegate) {
			return delegate.toString();
		}
	}

	public Object[] toArray() {
		Object[] rv;
		synchronized (delegate) {
			rv = new Object[delegate.size()];
			delegate.toArray(rv);
		}
		return rv;
	}

	public Object get(int index) {
		synchronized (delegate) {
			return delegate.get(index);
		}
	}

	public Object set(int index, Object element) {
		Object rv;
		synchronized (delegate) {
			rv = delegate.get(index);
			delegate.set(index, element);
		}
		fireContentsChanged(this, index, index);
		return rv;
	}

	public void add(int index, Object element) {
		synchronized (delegate) {
			delegate.add(index, element);
		}
		fireIntervalAdded(this, index, index);
	}

	public Object remove(int index) {
		Object rv;
		synchronized (delegate) {
			rv = delegate.get(index);
			delegate.remove(index);
		}
		fireIntervalRemoved(this, index, index);
		return rv;
	}

	public void clear() {
		int index1;
		synchronized (delegate) {
			index1 = delegate.size() - 1;
			delegate.clear();
		}
		if (index1 >= 0)
			fireIntervalRemoved(this, 0, index1);
	}

	public void removeRange(int fromIndex, int toIndex) {
		synchronized (delegate) {
			for(int i = toIndex; i >= fromIndex; i --)
					delegate.remove(i);
		}
		fireIntervalRemoved(this, fromIndex, toIndex);
	}
}
