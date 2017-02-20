package net.sharksystem.sharknet.pki;

/**
 * Created by j4rvis on 2/14/17.
 */
public class PKIDataHolder {
    private static PKIDataHolder ourInstance = new PKIDataHolder();
    private PKICertificateHolder holder = null;

    private PKIDataHolder() {
    }

    public static PKIDataHolder getInstance() {
        return ourInstance;
    }

    public PKICertificateHolder getHolder() {
        return holder;
    }

    public void setHolder(PKICertificateHolder holder) {
        this.holder = holder;
    }
}
