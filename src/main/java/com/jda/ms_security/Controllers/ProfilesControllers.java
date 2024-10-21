package com.jda.ms_security.Controllers;

import com.jda.ms_security.Models.Profile;
import com.jda.ms_security.Models.User;
import com.jda.ms_security.Repositories.ProfileRepository;
import com.jda.ms_security.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/profiles")

public class ProfilesControllers {

    @Autowired
    ProfileRepository theProfileRepository;
    @Autowired
    UserRepository theUserRepository;

    @GetMapping("")
    public List<Profile> find (){
        return this.theProfileRepository.findAll();
    }

    @GetMapping("{id}")
    public Profile findById(@PathVariable String id){
        Profile theProfile= this.theProfileRepository.findById(id).orElse( null);
        return theProfile;
    }

    @PostMapping
    public Profile create(@RequestBody Profile newProfile){
        return this.theProfileRepository.save(newProfile);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable String id){
        Profile theProfile = this.theProfileRepository.findById(id).orElse(null);
        if (theProfile!= null){
            this.theProfileRepository.delete(theProfile);
        }
    }

    @PutMapping("{id}")
    public Profile update(@PathVariable String id, @RequestBody Profile newProfile){
        Profile actualProfile = this.theProfileRepository.findById(id).orElse(null);
        if (actualProfile != null){
            actualProfile.setNumber(newProfile.getNumber());
            actualProfile.setPhoto(newProfile.getPhoto());
            this.theProfileRepository.save(actualProfile);
            return actualProfile;
        }

        else {
            return null;
        }
    }

    @PostMapping("{profileId}/user/{userId}")
    public Profile matchProfile(@PathVariable String ProfileId, @PathVariable String userId){
        Profile theProfile = this.theProfileRepository.findById(ProfileId).orElse(null);
        User theUser = this.theUserRepository.findById(userId).orElse(null);
        if (theProfile!=null && theUser!= null){
            theProfile.setUser(theUser);
            this.theProfileRepository.save(theProfile);
            return theProfile;
        }else{
            return  null;
        }
    }

}

