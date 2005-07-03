import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
/*-
 * $Id: ToleranceSimpleReflectogramEvent.java,v 1.1 2005/06/01 07:20:19 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

public class ToleranceSimpleReflectogramEvent implements
        SimpleReflectogramEvent {

    private int begin;
    private int end;
    private int eventType;
    private int newLevel;
    private int lossLevel;
    private int otherType; // other possible event type

    private int beginMin;
    private int beginMax;
    private int endMin;
    private int endMax;

    public int getBegin() {
        return begin;
    }
    public int getEnd() {
        return end;
    }
    public boolean hasBeginMin() {
        return beginMin <= begin;
    }
    public boolean hasBeginMax() {
        return beginMax >= begin;
    }
    public boolean hasEndMin() {
        return endMin <= end; 
    }
    public boolean hasEndMax() {
        return endMax >= end; 
    }
    public int getBeginMax() {
        return beginMax;
    }
    public int getBeginMin() {
        return beginMin;
    }
    public int getEndMax() {
        return endMax;
    }
    public int getEndMin() {
        return endMin;
    }
    public void setBeginMax(int beginMax) {
        this.beginMax = beginMax;
    }
    public void setBeginMin(int beginMin) {
        this.beginMin = beginMin;
    }
    public void setEndMax(int endMax) {
        this.endMax = endMax;
    }
    public void setEndMin(int endMin) {
        this.endMin = endMin;
    }
    public int getEventType() {
        return eventType;
    }
    public int getOtherType() {
        return otherType;
    }
    public int getLossLevel() {
        return lossLevel;
    }
    public int getNewLevel() {
        return newLevel;
    }
    public ToleranceSimpleReflectogramEvent(int begin, int end, int type,
            int level, int level2) {
        this.begin = begin;
        this.end = end;
        this.eventType = type;
        this.newLevel = level;
        this.lossLevel = level2;
        this.otherType = eventType;
        // set (begin|end)(Min|Max) to undefined
        this.beginMin = begin + 1;
        this.beginMax = begin - 1;
        this.endMin = end + 1;
        this.endMax = end - 1;
    }
    public void setOtherType(int t) {
        otherType = t;
    }
}
