/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.codec;

import org.anarres.ipmi.protocol.client.session.IpmiContext;
import org.anarres.ipmi.protocol.client.session.IpmiSessionManager;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class IpmiPacketDecoderTest {

    // A trivial enough test suite to make Jenkins pass.
    @Test
    public void testSomeMethod() {
        IpmiContext context = new IpmiSessionManager();
        IpmiPacketDecoder decoder = new IpmiPacketDecoder(context);
        assertNotNull(decoder);
    }
}