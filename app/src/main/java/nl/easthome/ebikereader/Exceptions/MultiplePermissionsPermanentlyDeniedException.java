package nl.easthome.ebikereader.Exceptions;

import com.karumi.dexter.listener.PermissionDeniedResponse;

import java.util.List;
public class MultiplePermissionsPermanentlyDeniedException extends Exception {
    public List<PermissionDeniedResponse> permissions;

    public MultiplePermissionsPermanentlyDeniedException(List<PermissionDeniedResponse> permissions) {
        this.permissions = permissions;
    }

    public MultiplePermissionsPermanentlyDeniedException(List<PermissionDeniedResponse> permissions, String message) {
        super(message);
        this.permissions = permissions;
    }

    public List<PermissionDeniedResponse> getPermission() {
        return permissions;
    }
}
