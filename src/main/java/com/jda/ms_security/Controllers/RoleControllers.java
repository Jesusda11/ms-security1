package com.jda.ms_security.Controllers;

import com.jda.ms_security.Models.Role;
import com.jda.ms_security.Repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("api/roles")

public class RoleControllers {
    @Autowired
    RoleRepository theRoleRepository;

    @GetMapping("")
    public List<Role> find (){
        return this.theRoleRepository.findAll();
    }

    @GetMapping("{id}")
    public Role findById(@PathVariable String id){
        Role theRole= this.theRoleRepository.findById(id).orElse( null);
        return theRole;
    }

    @PostMapping
    public Role create(@RequestBody Role newRole){
        return this.theRoleRepository.save(newRole);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Role theRole = this.theRoleRepository.findById(id).orElse(null);
        if (theRole!= null){
            this.theRoleRepository.delete(theRole);
        }
    }

    @PutMapping("{id}")
    public Role update(@PathVariable String id, @RequestBody Role newRole){
        Role actualRole = this.theRoleRepository.findById(id).orElse(null);
        if (actualRole != null){
            actualRole.setName(newRole.getName());
            actualRole.setDescription(newRole.getDescription());
            this.theRoleRepository.save(actualRole);
            return actualRole;
        }

        else {
            return null;
        }
    }
}
