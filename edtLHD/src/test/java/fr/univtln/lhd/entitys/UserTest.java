package fr.univtln.lhd.entitys;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    public User getInstanceOfUser(){
        return Admin.of("Name","FirstName","Name.Firstname@emal.com","St");
    }

    public  User getAnotherInstanceOfUser(){
        return Admin.of("Name2","FirstName2","Name2.First2name@emal.com","St");
    }

    @Test
    void EqualIsReflexive(){
        User user1 = this.getInstanceOfUser();
        Assertions.assertEquals(user1,user1);
    }

    @Test
    void HashCodeIsReflexive(){
        User user1 = this.getInstanceOfUser();
        Assertions.assertEquals(user1.hashCode(),user1.hashCode());
    }

    @Test
    void HashCodeIsReflexive2(){
        User user1 = this.getInstanceOfUser();
        User user2 = this.getAnotherInstanceOfUser();
        Assertions.assertNotEquals(user1.hashCode(),user2.hashCode());
    }

    @Test
    void EqualIsReflexive2(){
        User user1 = this.getInstanceOfUser();
        User user2 = this.getAnotherInstanceOfUser();
        Assertions.assertNotEquals(user1,user2);
    }

    void EqualIsSymmetric(){
        User user1 = this.getInstanceOfUser();
        User user2 = this.getInstanceOfUser();
        Assertions.assertTrue(user1.equals(user2)&&user2.equals(user1));
    }

    @Test
    void HashCodeIsSymmetric(){
        User user1 = this.getInstanceOfUser();
        User user2 = this.getInstanceOfUser();
        Assertions.assertTrue(user1.hashCode()==user2.hashCode()&&user2.hashCode()==user1.hashCode());
    }

    @Test
    void HashCodeIsSymmetric2(){
        User user1 = this.getInstanceOfUser();
        User user2 = this.getAnotherInstanceOfUser();
        Assertions.assertFalse(user1.hashCode()==user2.hashCode()&&user2.hashCode()==user1.hashCode());
    }

    @Test
    void EqualIsSymmetric2(){
        User user1 = this.getInstanceOfUser();
        User user2 = this.getAnotherInstanceOfUser();
        Assertions.assertFalse(user1.equals(user2)&&user2.equals(user1));
    }

    @Test
  void EqualIsTransitive(){
        User user1 = this.getInstanceOfUser();
        User user2 = this.getInstanceOfUser();
        User user3 = this.getInstanceOfUser();
        Assertions.assertTrue(user1.equals(user2)&&user2.equals(user3)&&user1.equals(user3));
    }
    void EqualIsTransitive2(){
        User user1 = this.getInstanceOfUser();
        User user2 = this.getAnotherInstanceOfUser();
        User user3 = this.getInstanceOfUser();
        Assertions.assertFalse(user1.equals(user2)&&user2.equals(user3)&&user1.equals(user3));
    }

    @Test
    void HashCodeIsTransitive(){
        User user1 = this.getInstanceOfUser();
        User user2 = this.getInstanceOfUser();
        User user3 = this.getInstanceOfUser();
        Assertions.assertTrue(user1.hashCode()==user2.hashCode()&&
                user2.hashCode()==user3.hashCode()&&
                user1.hashCode()==user3.hashCode());
    }

    @Test
    void HashCodeIsTransitive2(){
        User user1 = this.getInstanceOfUser();
        User user2 = this.getAnotherInstanceOfUser();
        User user3 = this.getInstanceOfUser();
        Assertions.assertFalse(user1.hashCode()==user2.hashCode()&&
                user2.hashCode()==user3.hashCode()&&
                user1.hashCode()==user3.hashCode());
    }
}