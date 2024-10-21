package com.jda.ms_security.Controllers;

import com.jda.ms_security.Models.Permission;
import com.jda.ms_security.Models.Role;
import com.jda.ms_security.Models.User;
import com.jda.ms_security.Models.RolePermission;
import com.jda.ms_security.Repositories.*;
import com.jda.ms_security.Repositories.RolePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/role_permission")

public class RolePermissionController {
    @Autowired
    UserRepository theUserRepository;

    @Autowired
    RoleRepository theRoleRepository;

    @Autowired
    PermissionRepository thePermissionRepository;

    @Autowired
    RolePermissionRepository theRolePermissionRepository;


    @PostMapping("role/{roleId}/permission/{permissionId}")
    public RolePermission create(@PathVariable String roleId, @PathVariable String permissionId){
        Role theRole = this.theRoleRepository.findById (roleId).orElse(null);
        Permission thePermission = this.thePermissionRepository.findById(permissionId).orElse(null);

        if (theRole!=null && thePermission!= null){
            RolePermission newRolePermission = new RolePermission();
            newRolePermission.setRole(theRole);
            newRolePermission.setPermission(thePermission);
            this.theRolePermissionRepository.save(newRolePermission);
            return newRolePermission;
        }else{
            return  null;
        }

    }

   /* @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        User theUser = this.theUserRepository.findById(id).orElse(null);
        if (theUser!= null){
         this.theUserRepository.delete(theUser);
        }
    }*/ //Buscar la manera de borrar un rol o un permiso?

    @GetMapping("role/{roleId}")
    public List<RolePermission> getPermissionsByRole(@PathVariable String roleId){
        return this.theRolePermissionRepository.getPermissionsByRole(roleId);
    }
     //Implementar el inverso.
}
