package com.shopme.admin.user;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private RoleRepository roleRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<User> listAll(){
        return (List<User>) userRepo.findAll();
    }

    public List<Role> listRoles(){
      return (List<Role>) roleRepo.findAll();
    }

    public void save(User user) {
        boolean isUpdatingUser = (user.getId() != null);

        if (isUpdatingUser) {
            User existingUser = userRepo.findById(user.getId()).get();

            if (user.getPassword().isEmpty()) {
                user.setPassword(existingUser.getPassword());
            } else {
                encodePassword(user);
            }
        }else {
            encodePassword(user);
        }
        userRepo.save(user);
    }

    public void encodePassword(User user){
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

    }

    public boolean isEmailUnique(Integer id,String email){
        User userByEmail = userRepo.getUserByEmail(email);
        if (userByEmail == null) return true;
        boolean isCreatingNew = (id == null);

        if (isCreatingNew){
            if (userByEmail != null) return false;
        }else {
            if (userByEmail.getId() != id){
                return false;
            }
        }
        return true;
    }

    public User get(Integer id) throws UserNotFoundException {
        try {
            return userRepo.findById(id).get();
        }catch (NoSuchElementException ex){
            throw new UserNotFoundException("Could not found any users with Id " + id);
        }
    }

    public void delete(Integer id) throws UserNotFoundException {
        Long countById = userRepo.countById(id);
        if (countById == null || countById == 0){
            throw new UserNotFoundException("Could not find any user with id " + id);
        }

        userRepo.deleteById(id);
    }

    public void updateUserEnabledStatus(Integer id, Boolean enabled){
        userRepo.updateEnabledStatus(id, enabled);
    }
}