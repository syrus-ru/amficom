/*
 * $Id: ByteArrayConverter.java,v 1.3 2004/12/08 13:51:03 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.io;

import java.io.IOException;

/**
 * @version $Revision: 1.3 $, $Date: 2004/12/08 13:51:03 $
 * @author $Author: bass $
 * @module util
 */
public class ByteArrayConverter
{
  private byte[] b;

  public ByteArrayConverter(byte[] array)
  {
    b = array;
  }

  public final short readIShort(int i) throws IOException
  {
    return (short)(b[i+1] << 8 | b[i] & 0xff);
  }

  public final int readIUnsignedShort(int i) throws IOException
  {
    return (b[i+1] & 0xff) << 8 | b[i] & 0xff;
  }

  public final int readIInt(int i) throws IOException
  {
    return (b[i+3] & 0xff) << 24 | (b[i+2] & 0xff) << 16 |
            (b[i+1] & 0xff) << 8 | b[i] & 0xff;
  }

  public final long readIUnsignedInt(int i) throws IOException
  {
    return (long)(b[i+3] & 0xff) << 24 | (long)(b[i+2] & 0xff) << 16 |
            (long)(b[i+1] & 0xff) << 8 | b[i] & 0xff;

  }

  public final double readIDouble(int i) throws IOException
  {
    return Double.longBitsToDouble ((long)(b[i+7] & 0xff) << 56 | (long)(b[i+6] & 0xff) << 48 |
    (long)(b[i+5] & 0xff) << 40 | (long)(b[i+4] & 0xff) << 32 | (long)(b[i+3] & 0xff) << 24 |
    (long)(b[i+2] & 0xff) << 16 | (long)(b[i+1] & 0xff) <<  8 | b[i] & 0xff);
  }

  public final char readIChar(int i) throws IOException
  {
    byte nul = 0x00;
    return (char)(nul << 8 | b[i] & 0xff);
  }

  public final String readIString(int i) throws IOException
  {
    byte nul = 0x00;
    String s = "";

    while (true)
    {
      if (b[i] == 0x00)
        break;
      s += (char)(nul << 8 | b[i] & 0xff);
      i++;
    }
    return  s;
  }
}
