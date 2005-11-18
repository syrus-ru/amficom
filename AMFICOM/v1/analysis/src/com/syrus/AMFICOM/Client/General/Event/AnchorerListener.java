/*-
 * $Id: AnchorerListener.java,v 1.1 2005/11/18 10:28:38 saa Exp $
 * 
 * Copyright © 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.Client.General.Event;

/**
 * Подписчик на изменения в привязчике Heap.anchorer.
 * В момент вызова методов этого интерфейса Heap.anchorer будет
 * находиться в состоянии, согласованном с эталоном.
 * @author $Author: saa $
 * @author saa
 * @version $Revision: 1.1 $, $Date: 2005/11/18 10:28:38 $
 * @module analysis
 */
public interface AnchorerListener {
	/**
	 * Вызывается при создании, изменении или удалении привязки.
	 * В случае, если привязка создается или изменяется,
	 * гарантировано, что на момент вызова этого метода
	 * эталон существует, а привязчик
	 * находится в состоянии, согласованном с эталоном.
	 */
	void anchorerChanged();
}
