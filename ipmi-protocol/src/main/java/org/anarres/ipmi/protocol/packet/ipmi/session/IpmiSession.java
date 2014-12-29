/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.session;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAuthenticationAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiConfidentialityAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiIntegrityAlgorithm;

/**
 *
 * @author shevek
 */
public class IpmiSession {

    private final int id;
    private AtomicInteger encryptedSequenceNumber = new AtomicInteger(0);
    private AtomicInteger unencryptedSequenceNumber = new AtomicInteger(0);
    private IpmiAuthenticationAlgorithm authenticationAlgorithm;
    private IpmiConfidentialityAlgorithm confidentialityAlgorithm;
    private Object confidentialityAlgorithmState;
    private IpmiIntegrityAlgorithm integrityAlgorithm;

    public IpmiSession(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int nextUnencryptedSequenceNumber() {
        return unencryptedSequenceNumber.getAndIncrement();
    }

    public int nextEncryptedSequenceNumber() {
        return encryptedSequenceNumber.getAndIncrement();
    }

    @Nonnull
    public byte[] newRandomSeed(int length) {
        SecureRandom r = new SecureRandom();
        return r.generateSeed(length);
    }

    public IpmiAuthenticationAlgorithm getAuthenticationAlgorithm() {
        return authenticationAlgorithm;
    }

    public void setAuthenticationAlgorithm(IpmiAuthenticationAlgorithm authenticationAlgorithm) {
        this.authenticationAlgorithm = authenticationAlgorithm;
    }

    public IpmiConfidentialityAlgorithm getConfidentialityAlgorithm() {
        return confidentialityAlgorithm;
    }

    public void setConfidentialityAlgorithm(IpmiConfidentialityAlgorithm confidentialityAlgorithm) {
        this.confidentialityAlgorithm = confidentialityAlgorithm;
    }

    public Object getConfidentialityAlgorithmState() {
        return confidentialityAlgorithmState;
    }

    public void setConfidentialityAlgorithmState(Object confidentialityAlgorithmState) {
        this.confidentialityAlgorithmState = confidentialityAlgorithmState;
    }

    public IpmiIntegrityAlgorithm getIntegrityAlgorithm() {
        return integrityAlgorithm;
    }

    public void setIntegrityAlgorithm(IpmiIntegrityAlgorithm integrityAlgorithm) {
        this.integrityAlgorithm = integrityAlgorithm;
    }
}