/**
 * $Id: PhysicalLinkBinding.java,v 1.9 2005/07/26 10:46:07 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Объект привязки кабелей к тоннелю. Принадлежит определенному тоннелю.
 * включает всебя список кабелей, которые проходят по данному тоннелю,
 * и матрицу пролегания кабелей по трубам тоннеля.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.9 $, $Date: 2005/07/26 10:46:07 $
 * @module map_v1
 */
public final class PhysicalLinkBinding {
	/** карта привязки кабелей к трубам */
	private List<Object>[][] bindingMap = null;

	/** список кабелей, проложенных по данному тоннелю */
	private ArrayList<Object> bindObjects = new ArrayList<Object>();

	/** размерность тоннеля (матрица труб) */
	private IntDimension dimension = null;

	/** порядок нумерации труб сверху вниз. */
	protected boolean topToBottom = true;

	/** порядок нумерации слева направо. */
	protected boolean leftToRight = true;

	/** порядок нумерации сначала по горизонтали, затем по вертикали. */
	protected boolean horizontalVertical = true;


	/**
	 * Конструктор.
	 * 
	 * @param bindingDimension
	 *        размерность привязки
	 */
	public PhysicalLinkBinding(final IntDimension bindingDimension) {
		setDimension(bindingDimension);
	}

	/**
	 * Добавить кабель в тоннель.
	 * 
	 * @param object
	 *        кабель (com.syrus.AMFICOM.scheme.corba.SchemeCableLink)
	 */
	public void add(final Object object) {
		final int index = this.bindObjects.indexOf(object);
		if (index == -1)
			this.bindObjects.add(object);
	}
	
	/**
	 * Удалить кабель из тоннеля.
	 * @param object кабель (com.syrus.AMFICOM.scheme.corba.SchemeCableLink)
	 */
	public void remove(final Object object) {
		if (object != null) {
			unbind(object);
			this.bindObjects.remove(object);
		}
	}

	/**
	 * Удалить все кабели из тоннеля.
	 */
	public void clear() {
		this.bindObjects.clear();

		for (int i = 0; i < this.bindingMap.length; i++) {
			for (int j = 0; j < this.bindingMap[i].length; j++) {
				this.bindingMap[i][j] = new LinkedList<Object>();
			}
		}
	}
	
	/**
	 * Получить список кабелей.
	 * 
	 * @return список кабелей
	 */
	public List<Object> getBindObjects() {
		return (List) this.bindObjects.clone();
	}
	
	/**
	 * Получить размерность матрицы прокладки кабелей по трубам тоннеля.
	 * @return размерность
	 */
	public IntDimension getDimension() {
		return this.dimension;
	}
	
	/**
	 * установить размерность матрицы прокладки кабелей по трубам тоннеля.
	 * @param dimension рамерность
	 */
	public void setDimension(final IntDimension dimension) {
		this.dimension = dimension;

		// создается новая матрица прокладки
		final List<Object>[][] bindingMap2 = new List[dimension.getWidth()][dimension.getHeight()];

		for (int i = 0; i < bindingMap2.length; i++) {
			for (int j = 0; j < bindingMap2[i].length; j++) {
				bindingMap2[i][j] = new LinkedList<Object>();
			}
		}
		
		// копируются привязки из старой матрицы
		if (this.bindingMap != null) {
			final int mini = Math.min(this.bindingMap.length, bindingMap2.length);

			for (int i = 0; i < mini; i++) {
				final int minj = Math.min(this.bindingMap[i].length, bindingMap2[i].length);
				for (int j = 0; j < minj; j++) {
					bindingMap2[i][j].addAll(this.bindingMap[i][j]);
				}
			}
		}
		this.bindingMap = bindingMap2;
	}
	
	/**
	 * Получить порядковый номер в тоннеле. Высчитывается по координатам в
	 * соответствии с порядком нумерации и направлением нумерации по горизонтали и
	 * вертикали ({@link #horizontalVertical}, {@link #leftToRight},
	 * {@link #topToBottom})
	 * 
	 * @param ii
	 *        координата по горизонтали
	 * @param jj
	 *        координата по вертикали
	 * @return порядковый номер сквозной нумерации
	 */
	public int getSequenceNumber(final int ii, final int jj) {
		final int sequenceNumber = -1;
		final int m = getDimension().getWidth();
		final int n = getDimension().getHeight();
		final int counter = 1;
		final int limit = n * m;

		final int istart = this.isLeftToRight() ? 0 : m - 1;
		final int jstart = this.isTopToBottom() ? 0 : n - 1;

		final int iend = m - 1 - istart;
		final int jend = n - 1 - jstart;

		int iincrement = this.isLeftToRight() ? 1 : -1;
		int jincrement = this.isTopToBottom() ? 1 : -1;

		int i = istart;
		int j = jstart;

		while (true) {
			if (i == ii && j == jj)
				break;

			if (counter > limit)
				break;

			if (this.isHorizontalVertical()) {
				if (i == iend) {
					i = istart;
					j += jincrement;
				} else
					i += iincrement;
			} else {
				if (j == jend) {
					j = jstart;
					i += iincrement;
				} else
					j += jincrement;
			}
		}
		return sequenceNumber;
	}
	
	/**
	 * Указать прокладку кабеля по трубе.
	 * 
	 * @param object
	 *        кабель
	 * @param i
	 *        координата по горизонтали
	 * @param j
	 *        координата по вертикали
	 */
	public void bind(final Object object, final int i, final int j) {
		this.unbind(object);
		this.bindingMap[i][j].add(object);
	}
	
	/**
	 * Убрать привязку кабеля к конкретной трубе в тоннеле.
	 * @param object кабель
	 */
	public void unbind(final Object object) {
		final IntPoint binding = getBinding(object);
		if (binding != null)
			this.bindingMap[binding.x][binding.y].remove(object);
	}

	/**
	 * Получить список кабелей, проходящих по трубе.
	 * @param i координата по горизонтали
	 * @param j координата по вертикали
	 * @return список привязанных по заданной координате кабелей
	 */
	public List getBound(final int i, final int j) {
		return this.bindingMap[i][j];
	}

	/**
	 * Проверить, определено ли место прохождения кабеля в тоннеле.
	 * 
	 * @param object
	 *        кабель
	 * @return <code>true</code>, если место кабеля определено,
	 *         <code>false</code> иначе
	 */
	public boolean isBound(final Object object) {
		final int index = this.bindObjects.indexOf(object);
		if (index == -1) {
			return false;
		}
		for (int i = 0; i < this.bindingMap.length; i++) {
			for (int j = 0; j < this.bindingMap[i].length; j++) {
				if (this.bindingMap[i][j].contains(object)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Gолучить координаты трубы, по которой проходит кабель.
	 * 
	 * @param object
	 *        кабель
	 * @return координаты прохождения кабеля, или <code>null</code>, если место
	 *         кабеля не задано
	 */
	public IntPoint getBinding(final Object object) {
		final int index = this.bindObjects.indexOf(object);
		if (index == -1) {
			return null;
		}
		for (int i = 0; i < this.bindingMap.length; i++) {
			for (int j = 0; j < this.bindingMap[i].length; j++) {
				if (this.bindingMap[i][j].contains(object)) {
					return new IntPoint(i, j);
				}
			}
		}
		return null;
	}

	/**
	 * Поменять направление нумерации по вертикали.
	 */
	public void flipTopToBottom() {
		this.topToBottom = !this.topToBottom;
	}

	/**
	 * Получить направление нумерации по вертикали.
	 * 
	 * @return <code>true</code> при нумерации сверху вниз, иначе
	 *         <code>false</code>
	 */
	public boolean isTopToBottom() {
		return this.topToBottom;
	}

	/**
	 * Поменять направление нумерации по горизонтали.
	 */
	public void flipLeftToRight() {
		this.leftToRight = !this.leftToRight;
	}


	/**
	 * Получить направление нумерации по горизонтали.
	 * 
	 * @return <code>true</code> при нумерации слева направо, иначе
	 *         <code>false</code>
	 */
	public boolean isLeftToRight() {
		return this.leftToRight;
	}

	/**
	 * Поменять порядок нумерации.
	 */
	public void flipHorizontalVertical() {
		this.horizontalVertical = !this.horizontalVertical;
	}

	/**
	 * Получить порядок нумерации.
	 * @return <code>true</code> при нумерации сначала по горизонтали,
	 * потом по вертикали, иначе <code>false</code>
	 */
	public boolean isHorizontalVertical() {
		return this.horizontalVertical;
	}
}
