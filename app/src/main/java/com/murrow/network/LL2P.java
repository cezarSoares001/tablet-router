package com.murrow.network;

import java.util.Arrays;

/**
 * Created by Corbin Murrow on 1/27/2016.
 */
public class LL2P
{
    private Integer dstAddr;
    private Integer srcAddr;
    private Integer type;
    private byte[] payload;
    private CRC16 crc;

    public LL2P(String dstAddr, String srcAddr, String type, String payload)
    {
        this.dstAddr = Integer.valueOf(dstAddr, 16);
        this.srcAddr = Integer.valueOf(srcAddr, 16);
        this.type = Integer.valueOf(type,16);

        this.payload = payload.getBytes();
    }

    public LL2P(byte[] info)
    {
        String tmp = Arrays.toString(info);

        tmp.toString();
    }

    public LL2P()
    {
        new LL2P("000", "000", "00", null);
    }
}
