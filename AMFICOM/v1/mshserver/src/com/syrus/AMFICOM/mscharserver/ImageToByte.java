/*-
 * $Id: ImageToByte.java,v 1.4 2005/09/21 15:14:28 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.mscharserver;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * @author Maxim Selivanov
 * @author $Author: arseniy $
 * @version $Revision: 1.4 $, $Date: 2005/09/21 15:14:28 $
 * @module mscharserver
 */

final class ImageToByte {
	private ImageToByte() {
		assert false : "Singleton";
	}

	static byte[] toByteArray(final BufferedImage image) {
		final WritableRaster raster = image.getRaster();
		final int width = image.getWidth();
		final int height = image.getHeight();
		final int imageLenght = width * height;
		byte[] qwe = new byte[imageLenght + 8];
		qwe = (byte[]) raster.getDataElements(0, 0, width, height, qwe);

		insertInt(qwe, width, imageLenght);
		insertInt(qwe, height, imageLenght + 4);

		return qwe;
	}

	static BufferedImage byteToImqge(final byte[] qwe) {
		final int width = restoreInt(qwe, qwe.length - 8);
		final int height = restoreInt(qwe, qwe.length - 4);
		final BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);
		im.getRaster().setDataElements(0, 0, width, height, qwe);
		return im;
	}

	private static void insertInt(final byte[] qwe, final int integer, final int startIndex) {
		qwe[startIndex] = (byte) ((integer >> 24) & 0xff);
		qwe[startIndex + 1] = (byte) ((integer >> 16) & 0xff);
		qwe[startIndex + 2] = (byte) ((integer >> 8) & 0xff);
		qwe[startIndex + 3] = (byte) (integer & 0xff);
	}

	private static int restoreInt(final byte[] qwe, final int startIndex) {
		return ((qwe[startIndex] & 0xff) << 24)
				| ((qwe[startIndex + 1] & 0xff) << 16)
				| ((qwe[startIndex + 2] & 0xff) << 8)
				| ((qwe[startIndex + 3] & 0xff) << 0);
	}
}
