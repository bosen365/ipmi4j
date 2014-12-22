/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 13.6 pages 133-134, column 1.
 *
 * @author shevek
 */
public class Ipmi15SessionWrapper implements IpmiSessionWrapper {

    private IpmiHeaderAuthenticationType authenticationType = IpmiHeaderAuthenticationType.NONE;
    private int ipmiSessionSequenceNumber;
    private int ipmiSessionId;
    private byte[] ipmiMessageAuthenticationCode;   // 16 bytes

    @Override
    public int getIpmiSessionId() {
        return ipmiSessionId;
    }

    @Override
    public int getIpmiSessionSequenceNumber() {
        return ipmiSessionSequenceNumber;
    }

    @Override
    public int getWireLength(IpmiHeader header, IpmiSessionData data) {
        return 1 + 4 + 4 + (authenticationType != IpmiHeaderAuthenticationType.NONE ? 16 : 0)
                + header.getWireLength()
                + data.getWireLength();
    }

    @Override
    public void toWire(ByteBuffer buffer, IpmiHeader header, IpmiSessionData data) {
        // Page 133
        buffer.put(authenticationType.getCode());
        buffer.putInt(ipmiSessionSequenceNumber);
        buffer.putInt(ipmiSessionId);
        if (authenticationType != IpmiHeaderAuthenticationType.NONE)
            buffer.put(ipmiMessageAuthenticationCode);
        // Page 134
        // 1 byte payload length

        header.toWire(buffer);
        data.toWire(buffer);
    }

    @Override
    public void fromWire(ByteBuffer buffer, IpmiHeader header, IpmiSessionData data) {
        authenticationType = Code.fromBuffer(IpmiHeaderAuthenticationType.class, buffer);
        ipmiSessionSequenceNumber = buffer.getInt();
        ipmiSessionId = buffer.getInt();
        if (authenticationType != IpmiHeaderAuthenticationType.NONE)
            ipmiMessageAuthenticationCode = AbstractWireable.readBytes(buffer, 16);
        else
            ipmiMessageAuthenticationCode = null;

        header.fromWire(buffer);
        data.fromWire(buffer);
    }
}