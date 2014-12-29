/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.common;

import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public abstract class AbstractWireable implements Wireable {

    protected abstract void toWireUnchecked(@Nonnull ByteBuffer buffer);

    @Override
    public void toWire(@Nonnull ByteBuffer buffer) {
        Preconditions.checkNotNull(buffer, "ByteBuffer was null.");
        int start = buffer.position();
        toWireUnchecked(buffer);

        int expectedLength = getWireLength();
        int actualLength = buffer.position() - start;
        if (actualLength != expectedLength)
            throw new IllegalStateException("Object should serialize to " + expectedLength + " bytes, but generated " + actualLength + ": " + this);
    }

    protected abstract void fromWireUnchecked(@Nonnull ByteBuffer buffer);

    @Override
    public void fromWire(ByteBuffer buffer) {
        Preconditions.checkNotNull(buffer, "ByteBuffer was null.");
        int start = buffer.position();
        fromWireUnchecked(buffer);

        int expectedLength = getWireLength();
        int actualLength = buffer.position() - start;
        if (actualLength != expectedLength)
            throw new IllegalStateException("Object should deserialize to " + expectedLength + " bytes, but generated " + actualLength + ": " + this);
    }

    /** Reads an array of bytes from the wire and returns them. */
    @Nonnull
    public static byte[] readBytes(@Nonnull ByteBuffer buffer, @Nonnegative int length) {
        byte[] data = new byte[length];
        buffer.get(data);
        return data;
    }

    // TODO: Take a 'description' message too.
    /** Reads a 4-byte signed int from the wire, and asserts it equal to the given expected value. */
    public static void assertWireInt(@Nonnull ByteBuffer buffer, int expectValue, String description) {
        int actualValue = buffer.getInt();
        if (actualValue != expectValue)
            throw new IllegalArgumentException("In " + description + ": "
                    + "Expected 0x" + Integer.toHexString(expectValue)
                    + " but got 0x" + Integer.toHexString(actualValue));
    }

    /** Reads a 2-byte unsigned char from the wire, and asserts it equal to the given expected value. */
    public static void assertWireChar(@Nonnull ByteBuffer buffer, char expectValue, String description) {
        short actualValue = buffer.get();
        if (actualValue != expectValue)
            throw new IllegalArgumentException("In " + description + ": "
                    + "Expected 0x" + Integer.toHexString(expectValue)
                    + " but got 0x" + Integer.toHexString(actualValue));
    }

    /** Reads a byte from the wire, and asserts it equal to the given expected value. */
    public static void assertWireByte(@Nonnull ByteBuffer buffer, byte expectValue, @Nonnull String description) {
        byte actualValue = buffer.get();
        if (actualValue != expectValue)
            throw new IllegalArgumentException("In " + description + ": "
                    + "Expected 0x" + UnsignedBytes.toString(expectValue, 16)
                    + " but got 0x" + UnsignedBytes.toString(actualValue, 16));
    }

    /** Reads a number of bytes from the wire, and asserts them equal to the given expected values. */
    public static void assertWireBytes(@Nonnull ByteBuffer buffer, @Nonnull int... expectValues) {
        for (int expectValue : expectValues)
            assertWireByte(buffer, (byte) (expectValue & 0xFF), "data byte at offset " + buffer.position());
    }

    /** Reads a number of zero bytes from the buffer. */
    public static void assertWireBytesZero(@Nonnull ByteBuffer buffer, @Nonnegative int count) {
        for (int i = 0; i < count; i++)
            assertWireByte(buffer, (byte) 0, "data byte at offset " + buffer.position());
    }

    protected int assertMask(int value, int nbits) {
        if (Integer.numberOfLeadingZeros(value) < Integer.SIZE - nbits)
            throw new IllegalArgumentException("Too many bits in " + Integer.toHexString(value) + "; expected max " + nbits);
        return value;
    }
}