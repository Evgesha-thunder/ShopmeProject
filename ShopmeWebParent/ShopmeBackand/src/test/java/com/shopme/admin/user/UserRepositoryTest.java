package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;
import static org.assertj.core.api.InstanceOfAssertFactories.predicate;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;

import javax.persistence.Table;
import java.util.List;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class UserRepositoryTest {

    @Autowired
    UserRepository repo;

    @Autowired
    private TestEntityManager entityManager;


    @Test
    public void testCreateNewUserWithOneRole(){
        Role roleAdmin = entityManager.find(Role.class, 1);
        User userNamHM = new User("name@codejava.net","nam2020", "Nam", "Ha Minh");
        userNamHM.addRole(roleAdmin);

        User savedUser = repo.save(userNamHM);
        assertThat(savedUser.getId()).isGreaterThan(0);

}

    @Test
    public void testCreateUserWithTwoRoles(){
        User userRavi = new User("ravi@gmail.com","ravi2020","Ravi", "Kumar");
        Role roleEditor = new Role(3);
        Role roleAssistant = new Role(5);

        userRavi.addRole(roleEditor);
        userRavi.addRole(roleAssistant);

        User savedUser = repo.save(userRavi);

        assertThat(savedUser.getId()).isGreaterThan(0);

    }

    @Test
    public void testListUsers(){
        Iterable<User> listUsers = repo.findAll();
        listUsers.forEach(u -> System.out.println(u));

    }

    @Test
    public void testGetUserByid(){
        User userNam = repo.findById(1).get();
        System.out.println(userNam);
        assertThat(userNam).isNotNull();
    }

    @Test
    public void testUpdateUserDetails(){
        User userNam = repo.findById(1).get();
        userNam.setEnabled(true);
        userNam.setEmail("corova@mail.ru");
        repo.save(userNam);
    }

    @Test
    public void testUpdateUserRoles(){
        User userRavi = repo.findById(2).get();
        Role roleEditor = new Role(3);
        Role roleSalesPerson = new Role(2);
        userRavi.getRoles().remove(roleEditor);
        userRavi.addRole(roleSalesPerson);

        repo.save(userRavi);
    }

    @Test
    public void testDeleteUser(){
        Integer userId = 2;
        repo.deleteById(userId);
    }

    @Test
    public void testGetUserByEmail(){
        String email =  "bulish2015@yandex.ru";
     User user = repo.getUserByEmail(email);
     assertThat(user).isNotNull();

    }
    @Test
    public void testCountById(){
        Integer id = 1;
        Long countById = repo.countById(id);
        assertThat(countById).isNotNull().isGreaterThan(0);
    }

    @Test
    public void testDisableUser(){
        Integer id = 17;
        repo.updateEnabledStatus(id, false);
    }

    @Test
    public void testEnableUser(){
        Integer id = 17;
        repo.updateEnabledStatus(id, true);
    }

    @Test
    public void testListFirstPage(){
        int pageNumber = 1;
        int pageSize = 4;

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<User> page = repo.findAll(pageable);

        List<User> listUsers = page.getContent();
        listUsers.forEach(System.out::println);
        assertThat(listUsers.size()).isEqualTo(pageSize);
    }

}
