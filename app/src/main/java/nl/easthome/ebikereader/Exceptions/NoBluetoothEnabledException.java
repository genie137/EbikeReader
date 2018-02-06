package nl.easthome.ebikereader.Exceptions;


public class NoBluetoothEnabledException extends Exception {

    public NoBluetoothEnabledException() {
        super("Bluetooth is disabled in settings.");
    }

}
