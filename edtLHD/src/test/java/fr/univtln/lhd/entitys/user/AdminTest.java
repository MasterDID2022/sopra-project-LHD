package fr.univtln.lhd.entitys.user;

import fr.univtln.lhd.model.entitys.user.Admin;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class AdminTest {

    public Admin getInstanceOfAdmin(){
        return Admin.of("Name","FirstName","Name.Firstname@emal.com","St");
    }

    public Admin getAnotherInstanceOfAdmin(){
        return Admin.of("Name2","FirstName","Name.Firstname@emal.com","St");
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