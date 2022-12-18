package fr.univtln.lhd.model.entities.users;

import fr.univtln.lhd.exceptions.IdException;
import lombok.Getter;

import java.util.Objects;

@Getter
public abstract class User {
    private final String name;
    private Long id= (long) -1;
    private final String fname;
    private final String email;

    /**
     * Abstract constructor of user, should only be used by the child of User
     * @param name name of a user
     * @param fname first name of a user
     * @param email email of a user
     */
    protected User(String name, String fname, String email) {
        this.name = name;
        this.fname = fname;
        this.email = email;
    }

    /**
     * Override of Equal
     * the equality depend of the name fistname and email
     * @param o User
     * @return True if equal False elif
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) && Objects.equals(fname, user.fname) && Objects.equals(email, user.email);
    }

    /**
     * Override of hashcode
     * the resulting hashcode depend of the name firstname and email of the User
     * @return hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, fname, email);
    }

    /**
     * Set the id, should only be used by the DAO
     * @param id
     * @throws IdException
     */
    public void setId(long id) throws IdException {
        if (id<0) {
            throw  new IdException();
        }
        this.id = id;
    }

    @Override
    public String toString() {
        return  name +' '+ fname +' '+ email + " ["+id+"] ";
    }
}
