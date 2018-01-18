package nl.easthome.ebikereader.Exceptions;

public class LocationIsDisabledException extends Exception {

    public LocationIsDisabledException() {
        super("Location is disabled in settings.");
    }
}
