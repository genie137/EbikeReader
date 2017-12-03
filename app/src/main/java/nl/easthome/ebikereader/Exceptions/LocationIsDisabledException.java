package nl.easthome.ebikereader.Exceptions;
/**
 * Created by jorisoosterhuis on 03/12/2017.
 */

public class LocationIsDisabledException extends Exception {

    public LocationIsDisabledException() {
        super("Location is disabled in settings.");
    }
}
