/*-
 * $Id: TopologicalImageQuery.java,v 1.14 2005/08/12 14:24:17 arseniy Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.awt.Image;

import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.map.corba.IdlTopologicalImageQuery;
import com.syrus.AMFICOM.resource.DoublePoint;

/**
 * �����-������ ��� ��������� � ������� ��������������� ������ ����� ���
 * 
 * @author $Author: arseniy $
 * @version $Revision: 1.14 $, $Date: 2005/08/12 14:24:17 $
 * @module mapinfo_v1
 */
public final class TopologicalImageQuery implements Comparable<TopologicalImageQuery> {

	/**
	 * ��� �������� �����������, ����������� � ��� "�� ������ ������", ��
	 * ������� ������������ � ���������������� � ������� ������
	 */
	private static final int PRIORITY_BACKGROUND = 20;
	public static final int PRIORITY_BACKGROUND_HIGH = TopologicalImageQuery.PRIORITY_BACKGROUND + 1;
	public static final int PRIORITY_BACKGROUND_MIDDLE = TopologicalImageQuery.PRIORITY_BACKGROUND + 2;
	public static final int PRIORITY_BACKGROUND_LOW = TopologicalImageQuery.PRIORITY_BACKGROUND + 3;

	/**
	 * ��� �������� �����������, ������� ��������� ���������� �� ��������
	 * ������� ������������ (����� ������� ���������)
	 */
	public static final int PRIORITY_EXPRESS = 10;

	/**
	 * ��� �������� �����������, ��� ������������, ������� ��������� ������
	 * ������������
	 */
	public static final int PRIORITY_ALREADY_LOADED = 0;

	/**
	 * ��������� �������
	 */
	private transient int priority = TopologicalImageQuery.PRIORITY_BACKGROUND;

	/**
	 * 
	 * ������ ����������� ����� � ��������
	 * 
	 */
	private int mapImageWidth;

	/**
	 * 
	 * ������ ����������� ����� � ��������
	 * 
	 */
	private int mapImageHeight;

	/**
	 * 
	 * ���������� � ��������������� ������ � ����������� �����������
	 * 
	 */
	private double topoCenterX;

	/**
	 * 
	 * ���������� Y ��������������� ������ � ����������� �����������
	 * 
	 */
	private double topoCenterY;

	private transient DoublePoint topoCenter = null;

	/**
	 * 
	 * ��������
	 * 
	 */
	private double topoScale;

	/**
	 * 
	 * ��������� ����
	 * 
	 */
	private boolean[] layerVisibilities;

	/**
	 * 
	 * ��������� �������� �� �����
	 * 
	 */
	private boolean[] labelVisibilities;

	/**
	 * ����� ���������� �������������
	 */
	private transient long lastUsed = 0;

	/**
	 * ����� �������� �����������
	 */
	private transient long timeCreated = 0;

	/**
	 * ������������ �����������
	 */
	private transient Image image = null;

	/**
	 * @param mapImageWidth
	 * @param mapImageHeight
	 * @param topoCenterX
	 * @param topoCenterY
	 * @param topoScale
	 * @param layerVisibilities
	 * @param labelVisibilities
	 */

	public TopologicalImageQuery(final int mapImageWidth,
			final int mapImageHeight,
			final double topoCenterX,
			final double topoCenterY,
			final double topoScale,
			final boolean[] layerVisibilities,
			final boolean[] labelVisibilities) {
		this.mapImageWidth = mapImageWidth;
		this.mapImageHeight = mapImageHeight;
		this.topoCenterX = topoCenterX;
		this.topoCenterY = topoCenterY;
		this.topoScale = topoScale;
		this.layerVisibilities = layerVisibilities;
		this.labelVisibilities = labelVisibilities;

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

	}

	public TopologicalImageQuery(final IdlTopologicalImageQuery tit) {
		this.mapImageWidth = tit.mapImageWidth;
		this.mapImageHeight = tit.mapImageHeight;
		this.topoCenterX = tit.topoCenterX;
		this.topoCenterY = tit.topoCenterY;
		this.topoScale = tit.topoScale;
		this.layerVisibilities = tit.layerVisibilities;
		this.labelVisibilities = tit.labelVisibilities;

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

	}

	/**
	 * 
	 */
	public TopologicalImageQuery() {
		// empty
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	public IdlTopologicalImageQuery getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return new IdlTopologicalImageQuery(this.mapImageWidth,
				this.mapImageHeight,
				this.topoCenterX,
				this.topoCenterY,
				this.topoScale,
				this.layerVisibilities,
				this.labelVisibilities);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected boolean isValid() {
		return this.mapImageWidth != 0 && this.mapImageHeight != 0;
	}

	public boolean[] getLabelVisibilities() {
		return this.labelVisibilities;
	}

	public void setLabelVisibilities(final boolean[] labelVisibilities) {
		this.labelVisibilities = labelVisibilities;
	}

	public boolean[] getLayerVisibilities() {
		return this.layerVisibilities;
	}

	public void setLayerVisibilities(final boolean[] layerVisibilities) {
		this.layerVisibilities = layerVisibilities;
	}

	public int getMapImageHeight() {
		return this.mapImageHeight;
	}

	public void setMapImageHeight(final int mapImageHeight) {
		this.mapImageHeight = mapImageHeight;
	}

	public int getMapImageWidth() {
		return this.mapImageWidth;
	}

	public void setMapImageWidth(final int mapImageWidth) {
		this.mapImageWidth = mapImageWidth;
	}

	public double getTopoCenterX() {
		return this.topoCenterX;
	}

	/**
	 * @deprecated Use setTopoCenter instead.
	 * @param topoCenterX
	 */
	@Deprecated
	public void setTopoCenterX(final double topoCenterX) {
		this.topoCenterX = topoCenterX;
	}

	public double getTopoCenterY() {
		return this.topoCenterY;
	}

	/**
	 * @deprecated Use setTopoCenter instead.
	 * @param topoCenterY
	 */
	@Deprecated
	public void setTopoCenterY(final double topoCenterY) {
		this.topoCenterY = topoCenterY;
	}

	public double getTopoScale() {
		return this.topoScale;
	}

	public void setTopoScale(final double topoScale) {
		this.topoScale = topoScale;
	}

	public int getPriority() {
		return this.priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public long getLastUsed() {
		return this.lastUsed;
	}

	public void setLastUsed(final long lastUsed) {
		this.lastUsed = lastUsed;
	}

	@Override
	public String toString() {
		final String resultString = "priority (" + this.priority + "), "
				+ "topo scale (" + this.topoScale + "), " + "topo center ("
				+ this.topoCenterX + ":" + this.topoCenterY + ")";
		return resultString;
	}

	/**
	 * ��� ���������� �������� �� �������� �� ������� 
	 */
	public int compareTo(final TopologicalImageQuery that) {
		return this.lastUsed <= that.lastUsed ? this.lastUsed < that.lastUsed ? 1 : 0 : -1;
	}

	public Image getImage() {
		return this.image;
	}

	public void setImage(final Image image) {
		this.image = image;
	}

	public DoublePoint getTopoCenter() {
		return this.topoCenter;
	}

	public void setTopoCenter(final DoublePoint topoCenter) {
		this.topoCenter = topoCenter;
		this.topoCenterX = topoCenter.getX();
		this.topoCenterY = topoCenter.getY();
	}

	public long getTimeCreated() {
		return this.timeCreated;
	}

	public void setTimeCreated(final long timeCreated) {
		this.timeCreated = timeCreated;
	}
}
