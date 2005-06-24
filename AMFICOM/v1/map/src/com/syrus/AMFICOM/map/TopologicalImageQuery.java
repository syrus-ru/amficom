/*-
 * $Id: TopologicalImageQuery.java,v 1.5 2005/06/24 10:41:46 bass Exp $
 *
 * Copyright ї 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.map;

import java.awt.Image;

import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.map.corba.IdlTopologicalImageQuery;

/**
 * Класс-запрос для обращения к серверу топографических данных через пул
 * 
 * @author $Author: bass $
 * @version $Revision: 1.5 $, $Date: 2005/06/24 10:41:46 $
 * @module mapinfo_v1
 */
public final class TopologicalImageQuery {

	/**
	 * Для участков изображения, добавляемых в кэш "на всякий случай", до
	 * запроса пользователя и перерисовываемых в фоновом режиме
	 */
	private static final int PRIORITY_BACKGROUND = 20;

	public static final int PRIORITY_BACKGROUND_HIGH = TopologicalImageQuery.PRIORITY_BACKGROUND + 1;

	public static final int PRIORITY_BACKGROUND_MIDDLE = TopologicalImageQuery.PRIORITY_BACKGROUND + 2;

	public static final int PRIORITY_BACKGROUND_LOW = TopologicalImageQuery.PRIORITY_BACKGROUND + 3;

	/**
	 * Для участков изображения, которые требуется отобразить по текущему
	 * запросу пользователя (самый высокий приоритет)
	 */
	public static final int PRIORITY_EXPRESS = 10;

	/**
	 * Для участков изображения, уже подгруженных, которые требуется только
	 * перерисовать
	 */
	public static final int PRIORITY_ALREADY_LOADED = 0;

	/**
	 * Приоритет запроса
	 */
	private transient int priority = TopologicalImageQuery.PRIORITY_BACKGROUND;

	/**
	 * 
	 * Ширина изображения карты в пикселях
	 * 
	 */

	private int mapImageWidth;

	/**
	 * 
	 * Высота изображения карты в пикселях
	 * 
	 */

	private int mapImageHeight;

	/**
	 * 
	 * Координата Х топологического центра в сферических координатах
	 * 
	 */

	private double topoCenterX;

	/**
	 * 
	 * Координата Y топологического центра в сферических координатах
	 * 
	 */

	private double topoCenterY;

	private transient DoublePoint topoCenter = null;

	/**
	 * 
	 * Массштаб
	 * 
	 */

	private double topoScale;

	/**
	 * 
	 * Видимость слоёв
	 * 
	 */

	private boolean[] layerVisibilities;

	/**
	 * 
	 * Видимость надписей на слоях
	 * 
	 */

	private boolean[] labelVisibilities;

	/**
	 * 
	 * Идентификатор пользователя
	 * 
	 */

	private long userID;

	/**
	 * Время последнего использования
	 */
	private transient long lastUsed = 0;

	/**
	 * Отображаемое изображение
	 */
	private transient Image image = null;

	/**
	 * 
	 * @param mapImageWidth
	 * 
	 * @param mapImageHeight
	 * 
	 * @param topoCenterX
	 * 
	 * @param topoCenterY
	 * 
	 * @param topoScale
	 * 
	 * @param layerVisibilities
	 * 
	 * @param labelVisibilities
	 * 
	 * @param userID
	 * 
	 */

	public TopologicalImageQuery(int mapImageWidth, int mapImageHeight,

	double topoCenterX, double topoCenterY, double topoScale,

	boolean[] layerVisibilities, boolean[] labelVisibilities,

	long userID) {

		this.mapImageWidth = mapImageWidth;

		this.mapImageHeight = mapImageHeight;

		this.topoCenterX = topoCenterX;

		this.topoCenterY = topoCenterY;

		this.topoScale = topoScale;

		this.layerVisibilities = layerVisibilities;

		this.labelVisibilities = labelVisibilities;

		this.userID = userID;

		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;

	}

	public TopologicalImageQuery(IdlTopologicalImageQuery tit) {

		this.mapImageWidth = tit.mapImageWidth;

		this.mapImageHeight = tit.mapImageHeight;

		this.topoCenterX = tit.topoCenterX;

		this.topoCenterY = tit.topoCenterY;

		this.topoScale = tit.topoScale;

		this.layerVisibilities = tit.layerVisibilities;

		this.labelVisibilities = tit.labelVisibilities;

		this.userID = tit.userId;

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
	 * 
	 */
	public IdlTopologicalImageQuery getTransferable() {
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		return new IdlTopologicalImageQuery(
				this.mapImageWidth, this.mapImageHeight,
				this.topoCenterX, this.topoCenterY,
				this.topoScale, this.layerVisibilities,
				this.labelVisibilities, this.userID);
	}

	/**
	 * <p>
	 * <b>Clients must never explicitly call this method. </b>
	 * </p>
	 */
	protected boolean isValid() {
		return this.mapImageWidth != 0 && this.mapImageHeight != 0
				&& this.userID != 0;
	}

	public boolean[] getLabelVisibilities() {

		return this.labelVisibilities;

	}

	public void setLabelVisibilities(boolean[] labelVisibilities) {

		this.labelVisibilities = labelVisibilities;

	}

	public boolean[] getLayerVisibilities() {

		return this.layerVisibilities;

	}

	public void setLayerVisibilities(boolean[] layerVisibilities) {

		this.layerVisibilities = layerVisibilities;

	}

	public int getMapImageHeight() {

		return this.mapImageHeight;

	}

	public void setMapImageHeight(int mapImageHeight) {

		this.mapImageHeight = mapImageHeight;

	}

	public int getMapImageWidth() {

		return this.mapImageWidth;

	}

	public void setMapImageWidth(int mapImageWidth) {

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
	public void setTopoCenterX(double topoCenterX) {
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
	public void setTopoCenterY(double topoCenterY) {
		this.topoCenterY = topoCenterY;
	}

	public double getTopoScale() {

		return this.topoScale;

	}

	public void setTopoScale(double topoScale) {

		this.topoScale = topoScale;

	}

	public long getUserID() {

		return this.userID;

	}

	public void setUserID(long userID) {

		this.userID = userID;

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

	public void setLastUsed(long lastUsed) {
		this.lastUsed = lastUsed;
	}

	@Override
	public String toString() {
		String resultString = "priority (" + this.priority + "), "
				+ "topo scale (" + this.topoScale + "), " + "topo center ("
				+ this.topoCenterX + ":" + this.topoCenterY + ")";

		return resultString;
	}

	/**
	 * Для сортировки запросов по убыванию по времени 
	 */
	public int compareTo(final Object o) {
		return this.compareTo((TopologicalImageQuery) o);
	}

	public int compareTo(final TopologicalImageQuery that) {
		return this.lastUsed <= that.lastUsed ? this.lastUsed < that.lastUsed ? 1 : 0 : -1;
	}

	public Image getImage() {
		return this.image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public DoublePoint getTopoCenter() {
		return this.topoCenter;
	}

	public void setTopoCenter(DoublePoint topoCenter) {
		this.topoCenter = topoCenter;

		this.topoCenterX = topoCenter.getX();
		this.topoCenterY = topoCenter.getY();
	}
}
