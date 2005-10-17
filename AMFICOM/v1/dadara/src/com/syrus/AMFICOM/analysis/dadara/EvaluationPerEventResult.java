/*-
 * $Id: EvaluationPerEventResult.java,v 1.2 2005/10/17 13:45:11 saa Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/10/17 13:45:11 $
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
	 * ќбъ€сн€ет причину, по которой Q- и K-параметры дл€ данного
	 * событи€ недоступны.
	 * ¬озвращает true, если причиной €вл€етс€ изменение на рефлектограмме,
	 * false, если к данному событию Q и K не определены по другой причине
	 * (например, Q и K дл€ этих событий не определимо).
	 * @param i номер данного событи€
	 * @throws IllegalStateException, если Q и K дл€ данного событи€
	 * доступны, т.е.
	 * <tt>{@link #hasQK}(i) != false</tt>
	 * @return true, если причиной €вл€етс€ изменение на рефлектограмме,
	 * false, если к данному событию Q и K не определены по другой причине
	 * (например, Q и K дл€ этих событий не определимо).
	 */
	boolean isModified(int i);

	/**
	 * ¬озвращает Q-параметр дл€ данного событи€.
	 * @param i номер данного событи€
	 * @return Q-параметр дл€ данного событи€.
	 * @throws IndexOutOfBoundsException, если
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>  
	 * @throws IllegalStateException, если <tt>{@link #hasQK}(i) == false</tt>
	 */
	double getQ(int i);

	/**
	 * ¬озвращает K-параметр дл€ данного событи€.
	 * @param i номер данного событи€
	 * @return K-параметр дл€ данного событи€.
	 * @throws IndexOutOfBoundsException, если
	 *   <tt> i &lt; 0 || i &gt;= {@link #getNEvents()} </tt>  
	 * @throws IllegalStateException, если <tt>{@link #hasQK}(i) == false</tt>
	 */
	double getK(int i);
}
