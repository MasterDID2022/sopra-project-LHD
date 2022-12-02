package fr.univtln.lhd.entitys;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    private Admin getInstanceOfAdmin(){
        return Admin.of("Name","FirstName","Name.Firstname@emal.com","St");
    }

    @Test
    void shouldReturnNotNull(){
        Admin admin = this.getInstanceOfAdmin();
        assertNotNull(admin);
    }
    @Test
    void facultyShouldBeSt() {
        Admin admin = this.getInstanceOfAdmin();
        assertEquals("St",admin.getFaculty());
    }
}