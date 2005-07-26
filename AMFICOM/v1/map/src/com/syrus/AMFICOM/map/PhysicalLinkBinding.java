/**
 * $Id: PhysicalLinkBinding.java,v 1.9 2005/07/26 10:46:07 arseniy Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.map;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * ������ �������� ������� � �������. ����������� ������������� �������.
 * �������� ����� ������ �������, ������� �������� �� ������� �������,
 * � ������� ���������� ������� �� ������ �������.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.9 $, $Date: 2005/07/26 10:46:07 $
 * @module map_v1
 */
public final class PhysicalLinkBinding {
	/** ����� �������� ������� � ������ */
	private List<Object>[][] bindingMap = null;

	/** ������ �������, ����������� �� ������� ������� */
	private ArrayList<Object> bindObjects = new ArrayList<Object>();

	/** ����������� ������� (������� ����) */
	private IntDimension dimension = null;

	/** ������� ��������� ���� ������ ����. */
	protected boolean topToBottom = true;

	/** ������� ��������� ����� �������. */
	protected boolean leftToRight = true;

	/** ������� ��������� ������� �� �����������, ����� �� ���������. */
	protected boolean horizontalVertical = true;


	/**
	 * �����������.
	 * 
	 * @param bindingDimension
	 *        ����������� ��������
	 */
	public PhysicalLinkBinding(final IntDimension bindingDimension) {
		setDimension(bindingDimension);
	}

	/**
	 * �������� ������ � �������.
	 * 
	 * @param object
	 *        ������ (com.syrus.AMFICOM.scheme.corba.SchemeCableLink)
	 */
	public void add(final Object object) {
		final int index = this.bindObjects.indexOf(object);
		if (index == -1)
			this.bindObjects.add(object);
	}
	
	/**
	 * ������� ������ �� �������.
	 * @param object ������ (com.syrus.AMFICOM.scheme.corba.SchemeCableLink)
	 */
	public void remove(final Object object) {
		if (object != null) {
			unbind(object);
			this.bindObjects.remove(object);
		}
	}

	/**
	 * ������� ��� ������ �� �������.
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
	 * �������� ������ �������.
	 * 
	 * @return ������ �������
	 */
	public List<Object> getBindObjects() {
		return (List) this.bindObjects.clone();
	}
	
	/**
	 * �������� ����������� ������� ��������� ������� �� ������ �������.
	 * @return �����������
	 */
	public IntDimension getDimension() {
		return this.dimension;
	}
	
	/**
	 * ���������� ����������� ������� ��������� ������� �� ������ �������.
	 * @param dimension ����������
	 */
	public void setDimension(final IntDimension dimension) {
		this.dimension = dimension;

		// ��������� ����� ������� ���������
		final List<Object>[][] bindingMap2 = new List[dimension.getWidth()][dimension.getHeight()];

		for (int i = 0; i < bindingMap2.length; i++) {
			for (int j = 0; j < bindingMap2[i].length; j++) {
				bindingMap2[i][j] = new LinkedList<Object>();
			}
		}
		
		// ���������� �������� �� ������ �������
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
	 * �������� ���������� ����� � �������. ������������� �� ����������� �
	 * ������������ � �������� ��������� � ������������ ��������� �� ����������� �
	 * ��������� ({@link #horizontalVertical}, {@link #leftToRight},
	 * {@link #topToBottom})
	 * 
	 * @param ii
	 *        ���������� �� �����������
	 * @param jj
	 *        ���������� �� ���������
	 * @return ���������� ����� �������� ���������
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
	 * ������� ��������� ������ �� �����.
	 * 
	 * @param object
	 *        ������
	 * @param i
	 *        ���������� �� �����������
	 * @param j
	 *        ���������� �� ���������
	 */
	public void bind(final Object object, final int i, final int j) {
		this.unbind(object);
		this.bindingMap[i][j].add(object);
	}
	
	/**
	 * ������ �������� ������ � ���������� ����� � �������.
	 * @param object ������
	 */
	public void unbind(final Object object) {
		final IntPoint binding = getBinding(object);
		if (binding != null)
			this.bindingMap[binding.x][binding.y].remove(object);
	}

	/**
	 * �������� ������ �������, ���������� �� �����.
	 * @param i ���������� �� �����������
	 * @param j ���������� �� ���������
	 * @return ������ ����������� �� �������� ���������� �������
	 */
	public List getBound(final int i, final int j) {
		return this.bindingMap[i][j];
	}

	/**
	 * ���������, ���������� �� ����� ����������� ������ � �������.
	 * 
	 * @param object
	 *        ������
	 * @return <code>true</code>, ���� ����� ������ ����������,
	 *         <code>false</code> �����
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
	 * G������� ���������� �����, �� ������� �������� ������.
	 * 
	 * @param object
	 *        ������
	 * @return ���������� ����������� ������, ��� <code>null</code>, ���� �����
	 *         ������ �� ������
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
	 * �������� ����������� ��������� �� ���������.
	 */
	public void flipTopToBottom() {
		this.topToBottom = !this.topToBottom;
	}

	/**
	 * �������� ����������� ��������� �� ���������.
	 * 
	 * @return <code>true</code> ��� ��������� ������ ����, �����
	 *         <code>false</code>
	 */
	public boolean isTopToBottom() {
		return this.topToBottom;
	}

	/**
	 * �������� ����������� ��������� �� �����������.
	 */
	public void flipLeftToRight() {
		this.leftToRight = !this.leftToRight;
	}


	/**
	 * �������� ����������� ��������� �� �����������.
	 * 
	 * @return <code>true</code> ��� ��������� ����� �������, �����
	 *         <code>false</code>
	 */
	public boolean isLeftToRight() {
		return this.leftToRight;
	}

	/**
	 * �������� ������� ���������.
	 */
	public void flipHorizontalVertical() {
		this.horizontalVertical = !this.horizontalVertical;
	}

	/**
	 * �������� ������� ���������.
	 * @return <code>true</code> ��� ��������� ������� �� �����������,
	 * ����� �� ���������, ����� <code>false</code>
	 */
	public boolean isHorizontalVertical() {
		return this.horizontalVertical;
	}
}
