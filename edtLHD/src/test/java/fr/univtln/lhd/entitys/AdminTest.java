package fr.univtln.lhd.entitys;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import  org.junit.jupiter.api.Test;


public class AdminTest {

    public Admin getInstanceOfAdmin(){
        return Admin.of("Name","FirstName","Name.Firstname@emal.com","St");
    }

    @Test
    void shouldReturnNotNull(){
        Admin admin = this.getInstanceOfAdmin();
        Assertions.assertNotNull(admin);
    }
    @Test
    void facultyShouldBeSt() {
        Admin admin = this.getInstanceOfAdmin();
        Assertions.assertEquals("St",admin.getFaculty());
    }
}