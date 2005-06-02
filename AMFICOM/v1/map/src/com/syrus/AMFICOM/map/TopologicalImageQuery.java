/*
 * $Id: TopologicalImageQuery.java,v 1.2 2005/06/02 09:42:28 max Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.map;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.map.corba.TopologicalImageQuery_Transferable;

/**
 * Класс-запрос для обращения к серверу топографических данных через пул
 * @author $Author: max $
 * @version $Revision: 1.2 $, $Date: 2005/06/02 09:42:28 $
 * @module mapinfo_v1
 */
public class TopologicalImageQuery {
    /**
     * Ширина изображения карты в пикселях
     */
    private int mapImageWidth;
    /**
     * Высота изображения карты в пикселях
     */
    private int mapImageHeight;

    /**
     * Координата Х топологического центра в сферических координатах
     */
    private double topoCenterX;
    /**
     * Координата Y топологического центра в сферических координатах
     */
    private double topoCenterY;
    /**
     * Массштаб
     */
    private double topoScale;
    
    /**
     * Видимость слоёв
     */
    private boolean[] layerVisibilities;
    /**
     * Видимость надписей на слоях
     */
    private boolean[] labelVisibilities;
    
    /**
     * Идентификатор пользователя
     */
    private long userID;
    
    private String commandName;    

    /**
     * @param mapImageWidth
     * @param mapImageHeight
     * @param topoCenterX
     * @param topoCenterY
     * @param topoScale
     * @param layerVisibilities
     * @param labelVisibilities
     * @param userID
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
    
    public TopologicalImageQuery(TopologicalImageQuery_Transferable tit) {
		
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
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
    public TopologicalImageQuery_Transferable getTransferable() {
		
		assert this.isValid() : ErrorMessages.OBJECT_STATE_ILLEGAL;
		
		return new TopologicalImageQuery_Transferable(
				this.mapImageWidth,
				this.mapImageHeight,
				this.topoCenterX,
				this.topoCenterY,
				this.topoScale,
				this.layerVisibilities,
				this.labelVisibilities,
				this.userID);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected boolean isValid() {
		return this.mapImageWidth != 0 && this.mapImageHeight != 0 && this.userID != 0;
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
    public void setTopoCenterX(double topoCenterX) {
        this.topoCenterX = topoCenterX;
    }
    public double getTopoCenterY() {
        return this.topoCenterY;
    }
    public void setTopoCenterY(double topoCenterY) {
        this.topoCenterY = topoCenterY;
    }
    public double getTopoScale() {
        return this.topoScale;
    }
    public void setTopoScale(double topoScale) {
        this.topoScale = topoScale;
    }
    public String getCommandName() {
        return this.commandName;
    }
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }
    public long getUserID() {
        return this.userID;
    }
    public void setUserID(long userID) {
        this.userID = userID;
    }
}
