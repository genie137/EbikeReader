package nl.easthome.ebikereader.Exceptions;

import com.karumi.dexter.listener.PermissionDeniedResponse;
public class PermissionPermanentlyDeniedException extends Exception {
    public PermissionDeniedResponse permission;

    public PermissionPermanentlyDeniedException(PermissionDeniedResponse permission) {
        this.permission = permission;
    }

    public PermissionPermanentlyDeniedException(PermissionDeniedResponse permission, String message) {
        super(message);
        this.permission = permission;
    }

    public PermissionDeniedResponse getPermission() {
        return permission;
    }
}
