package fr.univtln.lhd.entitys;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdminTest {

    private Admin getInstanceOfAdmin(){
        Admin admin = Admin.of("Name","FirstName","Name.Firstname@emal.com","St");
        return admin;
    }

    @Test
    public void shouldReturnNotNull(){
        Admin admin = this.getInstanceOfAdmin();
        assertNotNull(admin);
    }
    @Test
    void UFRshoudlBeSt() {
        Admin admin = this.getInstanceOfAdmin();
        assertEquals(admin.getUFR(),"St");
    }
}