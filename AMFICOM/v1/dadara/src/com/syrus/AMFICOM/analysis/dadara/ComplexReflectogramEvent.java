/*
 * $Id: ComplexReflectogramEvent.java,v 1.13 2005/04/29 09:57:53 saa Exp $
 * 
 * Copyright � Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.analysis.dadara;

import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;

/**
 * @author $Author: saa $
 * @version $Revision: 1.13 $, $Date: 2005/04/29 09:57:53 $
 * @module dadara
 * 
 * ����� ������������ ��� �������� ����������� ���������� �
 * ������������������� ������� -
 * ����� ����������� {������, �����, ���} �������� �����
 * ������������������� ��������� - ������, ��������� � ��.,
 * ����������� ��� ����������� ���������� � ������� ������������.
 */
public class ComplexReflectogramEvent implements SimpleReflectogramEvent
{
	private int begin;
	private int end;
	private int type;

	private double aLet;
	private double mLoss;

	private double asympY0;
	private double asympY1;

	public int getBegin() { return begin; }
	public int getEnd() { return end; }
	public int getEventType() { return type; }

    public double getLength() { return end - begin; }

    /**
     * @return ��������� ��������, �.�. yMax - asympY0
     */
    public double getALet() { return aLet; }

    /**
     * @return ������ �� �������.
     * ��� ���. ������� ��� (asympY0 - asympY1);
     * ��� ��������� - �� ��, �� ����������������� � ������ ��������
     * �������� �������. 
     */
	public double getMLoss() { return mLoss; }
    
    /**
     * @return
     * ��� ���. ������� - �� �������� ������������� � ������ �������;
     * ��� ������� ���� - �������� ������������� ���������� ��������� �������
     *   �� ������ ������� ����;
     * ��� ������ ������� - �������� �.�. � ������ �������.
     */
	public double getAsympY0() { return asympY0; }

    /**
     * @return
     * ��� ���. ������� - �� �������� ������������� � ����� �������;
     * ��� ����� ������� - � ��������, �� ����������;
     * ��� ������ ������� - �������� �.�. � ����� �������.
     */
    public double getAsympY1() { return asympY1; }
	
    /**
     * @return type == LOSS || type == GAIN
     */
	protected boolean isSplice()
	{
		return type == LOSS || type == GAIN;
	}

    private void setALet(ModelTrace mt) {
        if (type == SimpleReflectogramEvent.LINEAR)
            aLet = 0;
        else {
            int N = end - begin + 1;
            double []yArr = mt.getYArray(begin, N);
            aLet = ReflectogramMath.getArrayMax(yArr) - asympY0;
        }
    }

	// ����� ������ private, �.�. �� ������� ������ �� � ������������� ����.
	// ��������, ��� ������ ����� ���� ����� �������� mloss, � aLet - ������
    // �� ����������������
	private ComplexReflectogramEvent(SimpleReflectogramEvent se, ModelTrace mt)
	{
		begin = se.getBegin();
		end = se.getEnd();
		type = se.getEventType();
		if (type == SimpleReflectogramEvent.LINEAR)
		{
            int N = end - begin + 1;
            double []yArr = mt.getYArray(begin, N);
			ModelFunction lin = ModelFunction.createLinearFrom0(yArr);
			asympY0 = lin.fun(begin);
			asympY1 = lin.fun(end);
			mLoss = asympY0 - asympY1;
		}
		else
		{
			asympY0 = mt.getY(begin);
			asympY1 = mt.getY(end);
			mLoss = asympY0 - asympY1;
		}
	}

	public static ComplexReflectogramEvent[] createEvents(SimpleReflectogramEvent[] se, ModelTrace mt)
	{
		ComplexReflectogramEvent[] ret = new ComplexReflectogramEvent[se.length];

		// ������� �������
		for (int i = 0; i < ret.length; i++)
			ret[i] = new ComplexReflectogramEvent(se[i], mt);

		// ������������ mloss ������ � ����������� (�������� ������� ������ ������)
		for (int i = 0; i < ret.length; i++)
		{
			if (ret[i].isSplice()
					|| ret[i].getEventType()
					== SimpleReflectogramEvent.CONNECTOR)
			{
				// ����� ������� (�� ������ ��� ����) ������ ������� ��������
				// �������, ������� �� ����� ������ ���� ������
				int linCount = 0;
				double linAtt = 0;
				if (i > 0 && ret[i - 1].getLength() > ret[i].getLength())
				{
					linCount++;
					linAtt += ret[i - 1].mLoss / ret[i - 1].getLength();
				}
				if (i < ret.length - 1 && ret[i + 1].getLength() > ret[i].getLength())
				{
					linCount++;
					linAtt += ret[i + 1].mLoss / ret[i + 1].getLength();
				}
				if (linCount > 0)
					linAtt /= linCount;
				ret[i].mLoss -= linAtt * ret[i].getLength();
			}
		}

        // ������������ asympY0 ��� �.�.
        if (se[0].getEventType() == SimpleReflectogramEvent.DEADZONE)
        {
            for (int j = 1; j < se.length; j++) {
                if (se[j].getEventType() == SimpleReflectogramEvent.LINEAR) {
                    double po;
                    int x1 = se[j].getBegin() + 1;
                    int x2 = se[j].getEnd() - 1;
                    if (x1 >= x2)
                    {
                        po = mt.getY(x1);
                    }
                    else
                    {
                        double y1 = mt.getY(x1);
                        double y2 = mt.getY(x2);
                        po = (x1 * y2 - x2 * y1) / (x1 - x2);
                    }
                    ret[0].asympY0 = po;
                    break;
                }
            }
        }

        // ������������� aLet
        for (int i = 0; i < ret.length; i++)
            ret[i].setALet(mt);

        return ret;
	}
}
