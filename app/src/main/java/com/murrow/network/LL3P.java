package com.murrow.network;

import com.murrow.support.NetworkConstants;
import com.murrow.support.Utilities;

/**
 * Created by Corbin Murrow on 4/5/2016.
 */
public class LL3P
{
    private Integer srcAddr;
    private Integer dstAddr;
    private Integer type;
    private Integer identifier;
    private Integer ttl;
    private Integer checksum;

    byte[] payload;

    public LL3P()
    {
        setSrcAddr(NetworkConstants.MY_LL3P_ADDR);
        setDstAddr("0");
        setType(NetworkConstants.TYPE_LL3P);
        setTTL("255");
        setIdentifier("0");

        payload = Utilities.stringToBytes("0");

        calculateChecksum();
    }

    public LL3P(String srcAddr, String dstAddr, String type, String identifier, String ttl, String payload)
    {
        setSrcAddr(srcAddr);
        setDstAddr(dstAddr);
        setType(type);
        setIdentifier(identifier);
        setTTL(ttl);
        setPayload(Utilities.stringToBytes(payload));

        calculateChecksum();
    }

    public LL3P(byte[] data)
    {
        String tmpData = Utilities.bytesToString(data);

        setDstAddr(tmpData.substring(0, 4));
        setSrcAddr(tmpData.substring(4, 8));
        setType(tmpData.substring(8, 12));
        setIdentifier(tmpData.substring(12, 16));
        setTTL(tmpData.substring(16, 17));

        setChecksum(tmpData.substring(tmpData.length() - 4, tmpData.length()));

        setPayload(Utilities.stringToBytes(tmpData.substring(17, tmpData.length() - 4)));
    }

    public Integer getSrcAddr()
    {
        return srcAddr;
    }

    public void setSrcAddr(String srcAddr)
    {
        this.srcAddr = Integer.valueOf(srcAddr, 16);
    }

    public Integer getDstAddr()
    {
        return dstAddr;
    }

    public void setDstAddr(String dstAddr)
    {
        this.dstAddr = Integer.valueOf(dstAddr, 16);
    }

    public Integer getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = Integer.valueOf(type, 16);
    }

    public Integer getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier(String identifier)
    {
        this.identifier = Integer.valueOf(identifier, 16);
    }

    public Integer getTTL()
    {
        return ttl;
    }

    public void setTTL(String ttl)
    {
        this.ttl = Integer.valueOf(ttl, 16);
    }

    public Integer getChecksum()
    {
        return checksum;
    }

    public void setChecksum(String checksum)
    {
        this.checksum = Integer.valueOf(checksum, 16);
    }

    public byte[] getPayload()
    {
        return payload;
    }

    public void setPayload(byte[] payload)
    {
        this.payload = payload;
    }

    public String getSrcAddrHex()
    {
        return Utilities.padHex(Integer.toHexString(srcAddr), NetworkConstants.LL3P_ADDR_LENGTH);
    }

    public String getDstAddrHex()
    {
        return Utilities.padHex(Integer.toHexString(dstAddr), NetworkConstants.LL3P_ADDR_LENGTH);
    }

    public String getTypeHex()
    {
        return Utilities.padHex(Integer.toHexString(type), NetworkConstants.TYPE_LENGTH);
    }

    public String getIdentifierHex()
    {
        return Utilities.padHex(Integer.toHexString(identifier), 4);
    }

    public String getTTLHex()
    {
        return Utilities.padHex(Integer.toHexString(ttl), 2);
    }

    public String getChecksumHex()
    {
        return Utilities.padHex(Integer.toHexString(checksum), 4);
    }

    public String getPayloadString()
    {
        return Utilities.bytesToString(payload);
    }

    public byte[] getPacketBytes()
    {
        return Utilities.stringToBytes(toString());
    }

    public void decrementTTL()
    {
        ttl--;
    }

    private void calculateChecksum()
    {

    }

    @Override
    public String toString()
    {
        return getSrcAddrHex() + getDstAddrHex() + getTypeHex() + getIdentifierHex() + getTTLHex() + getPayloadString();
    }
}
