package com.java.java_proj.util.security;

import com.java.java_proj.entities.enums.PermissionAccessType;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(
            Authentication auth, Object targetDomainObject, Object permission) {
        if ((auth == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }
        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();

        return hasPrivilege(auth, targetType, permission.toString().toUpperCase());
    }

    @Override
    public boolean hasPermission(
            Authentication auth, Serializable targetId, String targetType, Object permission) {
        if ((auth == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }

        return hasPrivilege(auth, targetId.toString() + "_" + targetType.toUpperCase(),
                permission.toString().toUpperCase());
    }

    private boolean hasPrivilege(Authentication auth, String targetField, String permission) {

        for (GrantedAuthority grantedAuth : auth.getAuthorities()) {
            // target field equal and enum have higher value than required
            if (grantedAuth.getAuthority().startsWith(targetField) &&
                    PermissionAccessType.valueOf(grantedAuth.getAuthority().split("_")[2]).getValue()
                            >= PermissionAccessType.valueOf(permission).getValue()) {
                return true;
            }
        }
        
        return false;
    }
}
