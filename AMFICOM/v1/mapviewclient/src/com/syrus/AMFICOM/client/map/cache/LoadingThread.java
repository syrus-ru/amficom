/**
 * $Id: LoadingThread.java,v 1.2 2005/06/21 12:36:26 peskovsky Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
 */
package com.syrus.AMFICOM.client.map.cache;

import java.awt.Image;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.syrus.AMFICOM.client.map.Logger;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.map.TopologicalImageQuery;

public class LoadingThread extends Thread {
    private MapImageLoader mapImageLoader = null;

    /**
     * ������� �������� �� ��������� ����������� � �������
     */
    private List requestQueue = Collections.synchronizedList(new LinkedList());

    /**
     * ������, �������������� � ������� ������
     */
    private TopologicalImageQuery requestCurrentlyProcessed = null;

    /**
     * ���������� true ��� ���������� ������
     */
    private boolean toCancelLoading = false;

    /**
     * ������ � ������� �������� ���������� � ��������� ����.
     */
    private boolean[] layerVisibilities = null;

    /**
     * ������ � ������� �������� ���������� � ��������� �������� ��
     * ��������������� �����.
     */
    private boolean[] labelVisibilities = null;

    class State {
        public static final int STATE_IDLE = 0;

        public static final int STATE_RENDERING = 1;

        private int value;

        /**
         * @return Returns the value.
         */
        public int getValue() {
            return this.value;
        }

        /**
         * @param value
         *            The value to set.
         */
        public void setValue(int value) {
            this.value = value;
        }
    }

    private State state = null;

    public LoadingThread(MapImageLoader mapImageLoader)
            throws MapDataException, MapConnectionException {
        this.mapImageLoader = mapImageLoader;

        int layerCount = this.mapImageLoader.getMapConnection().getLayers()
                .size();
        this.layerVisibilities = new boolean[layerCount];
        this.labelVisibilities = new boolean[layerCount];

        this.state = new State();
        this.state.setValue(State.STATE_IDLE);
    }

    public void run() {
        while (!this.toCancelLoading) {
            // �������� - ��� ������ �������
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (this.state) {
                Iterator rqIt = this.requestQueue.iterator();
                if (rqIt.hasNext()) {
                    this.requestCurrentlyProcessed = (TopologicalImageQuery) rqIt
                            .next();
                    rqIt.remove();
                    this.state.setValue(State.STATE_RENDERING);
                } else
                    continue;
            }

            // �������� ������ �� ���������
            Image imageForRequest = null;
            try {
                Logger.log(" TIC - loadingThread - run - processing request ("
                        + this.requestCurrentlyProcessed + ")");

                this.setQueryLayerVisibilities(this.requestCurrentlyProcessed);

                imageForRequest = this.mapImageLoader
                        .renderMapImage(this.requestCurrentlyProcessed);

                if (imageForRequest == null) {
                    synchronized (this.state) {
                        // ���� ��������� ��� ����������
                        this.add(this.requestCurrentlyProcessed);
                        this.state.setValue(State.STATE_IDLE);
                        this.requestCurrentlyProcessed = null;

                        continue;
                    }
                }
            } catch (MapConnectionException e1) {
                e1.printStackTrace();
                this.add(this.requestCurrentlyProcessed);
            } catch (MapDataException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                this.add(this.requestCurrentlyProcessed);
            }

            synchronized (this.state) {
                // ��������� ������� ��������. ������ ������������������ ����,
                // �����
                // ������ ���� ��� ������������� ������� ��������� �������� ��
                // ����� ������
                this.requestCurrentlyProcessed.setImage(imageForRequest);
                this.requestCurrentlyProcessed
                        .setPriority(TopologicalImageQuery.PRIORITY_ALREADY_LOADED);

                Logger
                        .log(" TIC - loadingThread - run - image loaded for request ("
                                + this.requestCurrentlyProcessed + ")");

                this.state.setValue(State.STATE_IDLE);
                this.requestCurrentlyProcessed = null;
            }
        }
    }

    /**
     * ������������� ��������� ��������� ���� � �������� �� ��� ��� ��������� ������� 
     * @param query ������
     * @throws MapDataException
     * @throws MapConnectionException
     */
    private void setQueryLayerVisibilities(TopologicalImageQuery query)
    	throws MapDataException,MapConnectionException {
		Logger.log(" TIC - loadingThread - run - visibilities are set.");
		Iterator layersIt = this.mapImageLoader.getMapConnection().getLayers()
				.iterator();
		int index = 0;
		for (; layersIt.hasNext();) {
			SpatialLayer spL = (SpatialLayer) layersIt.next();

			// ��������� ���� ������� �� ����, ����� �� ��� ������ ������ � �����
			// �� �� ��� ������� �������� �� �������
			this.layerVisibilities[index] = spL.isVisible()
					&& spL.isVisibleAtScale(query.getTopoScale());
			// �� �� ����� ��� ��������
			this.labelVisibilities[index] = spL.isLabelVisible()
					&& spL.isVisibleAtScale(query.getTopoScale());
			index++;
		}

		query.setLayerVisibilities(this.layerVisibilities);		
		query.setLabelVisibilities(this.labelVisibilities);
		
		Logger.log(" TIC - loadingThread - run - visibilities are set.");
	}
    
    private void add(TopologicalImageQuery requestToAdd) {
        // ���� ������ ������ � ����������� ����, ��� � ������ �������
        ListIterator lIt = this.requestQueue.listIterator();
        for (; lIt.hasNext();) {
            TopologicalImageQuery curRequest = (TopologicalImageQuery) lIt
                    .next();
            if (requestToAdd.getPriority() < curRequest.getPriority()) {
                // ��������� ����� ���� ��������
                lIt.previous();
                lIt.add(requestToAdd);
                return;
            }
        }

        // �� ����� - ��������� � ����� �������
        lIt.add(requestToAdd);
    }

    private void stopRendering() throws MapConnectionException,
            MapDataException {
        this.mapImageLoader.stopRendering();
    }

    /**
     * ��������� ������ � �������, �������� ��� ���� ������� �� ����������
     * 
     * @param requestToAdd
     *            ������
     * @throws MapDataException
     * @throws MapConnectionException
     */
    public void addRequest(TopologicalImageQuery requestToAdd)
            throws MapConnectionException, MapDataException {
        Logger.log(" TIC - loadingThread - addRequest - adding request ("
                + requestToAdd + ")");

        add(requestToAdd);

        synchronized (this.state) {
            if ((this.state.getValue() == State.STATE_RENDERING)
                    && (this.requestCurrentlyProcessed.getPriority() > requestToAdd
                            .getPriority()))
                stopRendering();
        }
    }

    public boolean removeRequest(TopologicalImageQuery request)
            throws MapConnectionException, MapDataException {
        synchronized (this.state) {
            if ((this.state.getValue() == State.STATE_RENDERING)
                    && (this.requestCurrentlyProcessed == request)) {
                // ���� �� ������� ������, ������� ��� � ��������� - �� ��� ���
                // ��� � �������.
                // ������� ������ ������������� ���������.
                this.stopRendering();
                return true;
            }
            // ������� ������ �� �������
            else if (this.requestQueue.remove(request))
                return true;
        }
        return false;
    }

    /**
     * ������ ��������� �������������� ������� � �������
     * 
     * @param request
     *            ������
     * @param newPriority
     *            ����� ���������
     * @throws MapDataException
     * @throws MapConnectionException
     */
    public void changeRequestPriority(TopologicalImageQuery request,
            int newPriority) throws MapConnectionException, MapDataException {
        if (request.getPriority() == newPriority) {
            Logger
                    .log(" TIC - loadingThread - changeRequestPriority - the request ("
                            + request
                            + ") already has the priority "
                            + newPriority + ". Exiting.");
            return;
        }

        Logger
                .log(" TIC - loadingThread - changeRequestPriority - changing request's ("
                        + request + ") priority for " + newPriority);

        synchronized (this.state) {
            if ((this.state.getValue() == State.STATE_RENDERING)
                    && (this.requestCurrentlyProcessed == request))
            // ���� ������ ��� � ���������, ���������:
            // ���� ��� ��������� ��� ���� ������ ����������, �� �� �����
            // ���������
            // ����� ������������ - ������ �� ������, ����� �������������
            // ���������
            {
                this.requestCurrentlyProcessed.setPriority(newPriority);

                Iterator rqIt = this.requestQueue.iterator();
                if (!rqIt.hasNext())
                    // ������� �����
                    return;

                TopologicalImageQuery firstElement = (TopologicalImageQuery) rqIt
                        .next();
                if (firstElement.getPriority() < newPriority) {
                    // ������ ������� ����� ����� ��������� ����� �������,
                    // ��� ����� ��������� ��� ��������������� ������� -
                    // ������������� ���������.
                    this.stopRendering();
                    // � ��������������� ������ ������ ������� � �������
                    this.add(this.requestCurrentlyProcessed);
                }
            } else if (this.requestQueue.remove(request)) {
                // ���� ������ ���������� � �������
                // ����� ��� ����� ���������
                request.setPriority(newPriority);

                // ����� ������ ������ � �������
                this.add(request);
            }
        }
    }

    /**
     * ������ ��������� ���� �������� �� �������, ����� �������� � �����������
     * EXPRESS, �� ����� ������
     */
    public void setTheLowestPriorityForAll() {
        Logger
                .log(" TIC - loadingThread - setTheLowestPriorityForAll - entering");

        Iterator it = this.requestQueue.iterator();
        for (; it.hasNext();) {
            TopologicalImageQuery curRequest = (TopologicalImageQuery) it
                    .next();
            if (curRequest.getPriority() > TopologicalImageQuery.PRIORITY_EXPRESS)
                curRequest
                        .setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_LOW);
        }

        synchronized (this.state) {
            if (this.state.getValue() == State.STATE_RENDERING) {
                if (this.requestCurrentlyProcessed.getPriority() > TopologicalImageQuery.PRIORITY_EXPRESS)
                    this.requestCurrentlyProcessed
                            .setPriority(TopologicalImageQuery.PRIORITY_BACKGROUND_LOW);
            }
        }
        Logger.log(" TIC - loadingThread - setTheLowestPriorityForAll - done");
    }

    public void clearQueue() throws MapConnectionException, MapDataException {
        this.requestQueue.clear();
        synchronized (this.state) {
            if (this.state.getValue() == State.STATE_RENDERING)
                this.stopRendering();
        }
    }

    /**
     * ��������� ������ ������ ���������.
     *
     */
    public void cancel() {
        this.toCancelLoading = true;
    }
}