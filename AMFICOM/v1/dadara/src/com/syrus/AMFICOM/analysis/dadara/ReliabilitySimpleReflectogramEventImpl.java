/*-
 * $Id: ReliabilitySimpleReflectogramEventImpl.java,v 1.1 2005/04/14 12:03:17 saa Exp $
 * 
 * Copyright � 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/04/14 12:03:17 $
 * @module
 */
public class ReliabilitySimpleReflectogramEventImpl
extends SimpleReflectogramEventImpl
implements ReliabilitySimpleReflectogramEvents {

    double reliability;

    protected ReliabilitySimpleReflectogramEventImpl()
    { // for native use
    }

    /**
     * @param begin ������ �������
     * @param end ����� �������
     * @param eventType ��� ������� {@link SimpleReflectogramEvent}
     * @param reliability ������������� ���������� �������� ������ (0-1) ���� -1, ���� ������������� �� ����������
     */
    public ReliabilitySimpleReflectogramEventImpl(int begin, int end,
            int eventType, double reliability) {
        super(begin, end, eventType);
        this.reliability = reliability;
    }

    public double getReliability() {
        if (hasReliability())
            return reliability;
        else
            throw new IllegalArgumentException("getReliability() requested on event that has no probability");
    }

    public boolean hasReliability() {
        return reliability >= 0;
    }

}
