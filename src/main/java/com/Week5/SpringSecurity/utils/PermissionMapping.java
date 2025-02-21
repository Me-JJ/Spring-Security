package com.Week5.SpringSecurity.utils;

import com.Week5.SpringSecurity.entities.enums.Permission;
import com.Week5.SpringSecurity.entities.enums.Permission.*;
import com.Week5.SpringSecurity.entities.enums.Role;
import com.Week5.SpringSecurity.entities.enums.Role.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.Week5.SpringSecurity.entities.enums.Permission.*;

public class PermissionMapping
{
    private static final Map<Role, Set<Permission>> permissionMapping = Map.of(
            Role.USER,Set.of(Permission.USER_VIEW,Permission.POST_VIEW),
            Role.CREATOR,Set.of(POST_CREATE,USER_UPDATE,POST_UPDATE),
            Role.ADMIN,Set.of(POST_CREATE,POST_VIEW,POST_DELETE,POST_UPDATE,USER_VIEW,USER_CREATE,USER_DELETE,USER_UPDATE)
    );

    public static Set<SimpleGrantedAuthority> getAuthoritiesByRole(Role role)
    {
        return permissionMapping.get(role).stream().map(x -> new SimpleGrantedAuthority(x.name())).collect(Collectors.toSet());
    }

}
