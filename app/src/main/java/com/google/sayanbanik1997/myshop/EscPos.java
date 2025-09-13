package com.google.sayanbanik1997.myshop;


// EscPos.java

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

public class EscPos {
    public static final byte[] INIT = new byte[]{0x1B, 0x40};                 // ESC @
    public static final byte[] LF   = new byte[]{0x0A};                        // line feed
    public static final byte[] ALIGN_LEFT   = new byte[]{0x1B, 0x61, 0x00};    // ESC a 0
    public static final byte[] ALIGN_CENTER = new byte[]{0x1B, 0x61, 0x01};    // ESC a 1
    public static final byte[] ALIGN_RIGHT  = new byte[]{0x1B, 0x61, 0x02};    // ESC a 2
    public static final byte[] BOLD_ON      = new byte[]{0x1B, 0x45, 0x01};    // ESC E 1
    public static final byte[] BOLD_OFF     = new byte[]{0x1B, 0x45, 0x00};    // ESC E 0
    public static final byte[] CUT_FULL     = new byte[]{0x1D, 0x56, 0x00};    // GS V 0 (if supported)

    private final OutputStream os;
    private final Charset cs;

    public EscPos(OutputStream os, String charsetName) {
        this.os = os;
        this.cs = Charset.forName(charsetName); // e.g. "UTF-8" or a printer code page like "GBK"
    }

    public EscPos write(byte[] bytes) throws IOException { os.write(bytes); return this; }
    public EscPos text(String s) throws IOException { os.write(s.getBytes(cs)); return this; }
    public EscPos nl() throws IOException { os.write(LF); return this; }
    public void flush() throws IOException { os.flush(); }
}


