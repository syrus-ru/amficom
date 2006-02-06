/*-
 * $Id: ImageToByte.java,v 1.1 2005/07/04 11:03:12 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.io;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * @author Maxim Selivanov
 * @author $Author: max $
 * @version $Revision: 1.1 $, $Date: 2005/07/04 11:03:12 $
 * @module mshserver_v1
 */

public class ImageToByte {
	
	static public byte[] toByteArray(BufferedImage image) {
	    
		WritableRaster raster = image.getRaster();
		int width = image.getWidth();
		int height = image.getHeight();
		int imageLenght = width * height; 
		byte[] qwe = new byte[imageLenght + 8];
		qwe = (byte[]) raster.getDataElements(0, 0, width ,height , qwe);
		
		insertInt(qwe, width, imageLenght);
		insertInt(qwe, height, imageLenght + 4);
		
		return qwe;
	}	
	
	static public BufferedImage byteToImqge(byte[] qwe) {
		int width = restoreInt(qwe, qwe.length - 8);
		int height = restoreInt(qwe, qwe.length - 4);
		BufferedImage im = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_INDEXED);
		im.getRaster().setDataElements(0,0, width, height, qwe);
		return im;
	}
	
	private static void insertInt(byte[] qwe, int integer, int startIndex) {
		qwe[startIndex] = (byte)((integer >> 24) & 0xff);
		qwe[startIndex + 1] = (byte)((integer >> 16) & 0xff);
		qwe[startIndex + 2] = (byte)((integer >> 8) & 0xff); 
		qwe[startIndex + 3] = (byte)(integer & 0xff);
	}

	private static int restoreInt(byte[] qwe, int startIndex) {
		return ((qwe[startIndex] & 0xff) << 24) | ((qwe[startIndex + 1] & 0xff) << 16) | ((qwe[startIndex + 2] & 0xff) << 8) | ((qwe[startIndex + 3] & 0xff) << 0);
	}
}
