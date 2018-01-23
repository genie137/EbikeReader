package nl.easthome.antpluslibary.Enums;

/**
 * Shows the state of ANT on the device.
 */
public enum AntSupportState {
    ANT_NO_CHIP_OR_USB,
    ANT_SUPPORTED_BUT_NO_RADIO,
    ANT_SUPPORTED_BUT_NO_PLUGIN,
    ANT_SUPPORTED_BUT_NO_RADIO_AND_PLUGIN,
    ANT_SUPPORTED
}
