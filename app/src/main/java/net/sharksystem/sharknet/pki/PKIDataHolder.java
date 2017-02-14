package net.sharksystem.sharknet.pki;

import net.sharkfw.security.SharkCertificate;
import net.sharkfw.security.SharkPublicKey;

/**
 * Created by j4rvis on 2/14/17.
 */
public class PKIDataHolder {
    private static PKIDataHolder ourInstance = new PKIDataHolder();

    public static PKIDataHolder getInstance() {
        return ourInstance;
    }

    private PKIDataHolder() {
    }

    private SharkCertificate certificate = null;
    private SharkPublicKey publicKey = null;

    public SharkCertificate getCertificate() {
        return certificate;
    }

    public void setCertificate(SharkCertificate certificate) {
        this.certificate = certificate;
    }

    public SharkPublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(SharkPublicKey publicKey) {
        this.publicKey = publicKey;
    }
}
