package fr.univtln.lhd.model.entities.slots;

import fr.univtln.lhd.exceptions.IdException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

/**
 * Class defining a Subject
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Subject {

    private long id;

    private final String name;
    private final float hourCountMax; //max number of hours dedicated to this subject

    /**
     * Factory for a Subject
     *
     * @param name         name of the subject
     * @param hourCountMax max number of hours dedicated to this subject
     * @return an instance of Subject
     */
    public static Subject getInstance(String name, float hourCountMax) {
        return getInstance(-1, name, hourCountMax);
    }

    /**
     * Factory for a Subject
     *
     * @param id           long identifier for a Classroom (default -1)
     * @param name         name of the subject
     * @param hourCountMax max number of hours dedicated to this subject
     * @return an instance of Subject
     */
    public static Subject getInstance(long id, String name, float hourCountMax) {
        return new Subject(id, name, hourCountMax);
    }

    /**
     * Set the id, should only be used by the DAO
     * @param id
     * @throws IdException
     */
    public void setId(long id) throws IdException {
        if (id < 0) {
            throw new IdException("Id Error");
        }
        this.id=id;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hourCountMax=" + hourCountMax +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return id == subject.id && Float.compare(subject.hourCountMax, hourCountMax) == 0 && Objects.equals(name, subject.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hourCountMax);
    }
}
