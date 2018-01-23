package nl.easthome.antpluslibary.Enums;

/**
 * Enum state that defines how the Ant sensor was added.
 * NEW = The sensor has not been seen saved on the device.
 * EXISTING_AND_MISSING = The sensor is saved, but has not been found by the DeviceSearch.
 * EXISTING_AND_FOUND = The sensor is saved, and found by the DeviceSearch.
 */
public enum AntAddType {
    NEW,
    EXISTING_AND_MISSING,
    EXISTING_AND_FOUND
}
