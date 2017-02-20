package net.sharksystem.sharknet.pki;

import net.sharkfw.knowledgeBase.PeerSemanticTag;
import net.sharkfw.knowledgeBase.SharkCSAlgebra;
import net.sharkfw.security.SharkCertificate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by j4rvis on 2/20/17.
 */

public class PKICertificateHolder {

    private List<SharkCertificate> certificates = new ArrayList<>();
    private PeerSemanticTag owner;

    public boolean addCertificate(SharkCertificate certificate){
        if (this.owner == null){
            this.owner = certificate.getOwner();
        }
        if (SharkCSAlgebra.identical(this.owner, certificate.getOwner())){
            this.certificates.add(certificate);
            return true;
        }
        return false;
    }

    public List<SharkCertificate> getCertificates() {
        return certificates;
    }

    public PeerSemanticTag getOwner() {
        return owner;
    }


}
