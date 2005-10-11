/*-
 * $Id: EvaluationPerEventResult.java,v 1.1 2005/10/11 16:42:01 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.analysis.dadara;

/**
 * ќписывает общие (дл€ всй р/г) результаты сравнени€ с эталоном.
 * ¬езде предполагаетс€ нумераци€ событий при сравнении с эталоном,
 * соответствующа€ сравниваемой р/г.
 * <p>
 * FIXME: возможно, нумерациу событий по р/г надо переделать на нумерацию по эталону
 * @author $Author: saa $
 * @version $Revision: 1.1 $, $Date: 2005/10/11 16:42:01 $
 * @module
 */
public interface EvaluationPerEventResult {
	/**
	 * ¬озвращает число событий результата анализа
	 * @return число событий результата анализа
	 */
	int getNEvents();

	/**
	 * ¬озвращает true, если Q- и K-параметры дл€ данного событи€ доступны.
	 * @param i номер данного событи€
	 * @return true, если Q- и K-параметры дл€ данного событи€ доступны.
	 * @throws IndexOutOfBoundsException, если
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>  
	 */
	boolean hasQK(int i);
	/**
	 * ¬озвращает Q-параметр дл€ данного событи€.
	 * @param i номер данного событи€
	 * @return Q-параметр дл€ данного событи€.
	 * @throws IndexOutOfBoundsException, если
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>  
	 * @throws IllegalStateException, если {@link #hasQK}(i) == false
	 */
	double getQ(int i);
	/**
	 * ¬озвращает K-параметр дл€ данного событи€.
	 * @param i номер данного событи€
	 * @return K-параметр дл€ данного событи€.
	 * @throws IndexOutOfBoundsException, если
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>  
	 * @throws IllegalStateException, если {@link #hasQK}(i) == false
	 */
	double getK(int i);
}
