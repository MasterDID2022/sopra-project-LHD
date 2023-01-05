package fr.univtln.lhd.model.entities.slots;

import fr.univtln.lhd.exceptions.IdException;
import fr.univtln.lhd.model.entities.users.Professor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.threeten.extra.Interval;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SlotTest {
    private Slot slot = InitgetInstanceOfSlot();
    private Slot slot2 = InitgetInstanceOfSlot();


    public Classroom getInstanceOfClassroom () {
        return Classroom.getInstance("Name");
    }

    public Subject getInstanceOfSubject () {
        return Subject.getInstance("name",70);
    }

    public Group getInstanceOfGroup () {
        return Group.getInstance("Name");
    }
    public Professor getInstanceOfProfessor(){return Professor.of("Name","fname","email@lhd.org","test");}
    public List<Professor> getListOfProfessor(){return  List.of(getInstanceOfProfessor());}
    public List<Group> getListOfGroup () {return List.of(getInstanceOfGroup());}

    public Slot getInstanceOfSlot () {
        return slot;
    }

    private Slot InitgetInstanceOfSlot () {
        return Slot.getInstance(Slot.SlotType.CM, getInstanceOfClassroom(), getInstanceOfSubject(), getListOfGroup(), getListOfProfessor(),Interval.of(Instant.now(), Instant.now().plusSeconds(10)));
    }

    public Slot getAnotherInstanceOfSlot () {
        return Slot.getInstance(Slot.SlotType.TD, getInstanceOfClassroom(), getInstanceOfSubject(), getListOfGroup(), getListOfProfessor(),Interval.of(Instant.now(), Instant.now().plusSeconds(10)));
    }

    public Slot getInstanceOfSlotWithMemo () {
        return Slot.getInstance(Slot.SlotType.CM, getInstanceOfClassroom(), getInstanceOfSubject(), getListOfGroup(),getListOfProfessor(),Interval.of(Instant.now(), Instant.now().plusSeconds(10)), "memo");
    }

    @Test
    void ShouldBeSettable () {
        Slot slot = getInstanceOfSlot();
        slot.setMemo("memo");
        assertEquals("memo", slot.getMemo().orElse(null)); // if optional is empty, null wil make the test fail.
    }

    @Test
    void testInstanceNotNull () {
        Slot slot = getInstanceOfSlot();
        assertNotNull(slot);
    }

    @Test
    void testInstanceNotNullWithMemo () {
        Slot slot = getInstanceOfSlotWithMemo();
        assertNotNull(slot);
    }

    @Test
    void ShouldThrowAnIdException(){
        Slot slot = this.getInstanceOfSlot();
        IdException thrown = Assertions.assertThrows(
                IdException.class,() ->slot.setId(-3),"should throw id error"
        );
        Assertions.assertFalse(thrown.getMessage().contains("should throw id error"));
    }


    @Test
    void ShouldAddZeroToFormat(){
        Instant instant = Instant.ofEpochSecond(1672297200);
        Interval timerangeTest = Interval.of(instant, Duration.ofHours(1));
        Slot slot = getInstanceOfSlot();
        slot.setTimeRange(timerangeTest);
        if (ZoneId.systemDefault().equals(ZoneId.of("Europe/Paris"))){
            Assertions.assertEquals("08:00 - 09:00",slot.getDisplayTimeInterval());
        }
        else {//Because The C.I is not on the same TimeZone
            Assertions.assertEquals("07:00 - 08:00",slot.getDisplayTimeInterval());
        }
    }

    @Test
    void ShouldNotAddZeroToFormat(){
        Instant instant = Instant.ofEpochSecond(1672250420);
        Interval timerangeTest = Interval.of(instant, Duration.ofHours(1));
        Slot slot = getInstanceOfSlot();
        slot.setTimeRange(timerangeTest);
        if (ZoneId.systemDefault().equals(ZoneId.of("Europe/Paris"))){
            Assertions.assertEquals("19:00 - 20:00",slot.getDisplayTimeInterval());
        }
        else {//Because The C.I is not on the same TimeZone
            Assertions.assertEquals("18:00 - 19:00",slot.getDisplayTimeInterval());
        }
    }

    @Test
    void EqualIsReflexive(){
        Slot slot1 = this.getInstanceOfSlot();
        Assertions.assertEquals(slot1,slot1);
    }

    @Test
    void HashCodeIsReflexive(){
        Slot slot1 = this.getInstanceOfSlot();
        Assertions.assertEquals(slot1.hashCode(),slot1.hashCode());
    }

    @Test
    void ShouldGetClassroom(){
        Assertions.assertEquals(getInstanceOfClassroom(),
                getInstanceOfSlot().getClassroom());
    }

    @Test
    void ShouldGetID(){
        Assertions.assertEquals(-1,
                getInstanceOfSlot().getId());
    }

    @Test
    void ShouldGetType(){
        Assertions.assertEquals(Slot.SlotType.CM,
                getInstanceOfSlot().getType());
    }

    @Test
    void ShouldGetGroup(){
        Assertions.assertEquals(getInstanceOfGroup(),
                getInstanceOfSlot().getGroup().get(0));
    }

    @Test
    void ShouldGetInterval(){
        Interval newInterval = Interval.of(Instant.now(),Duration.ofHours(1));
        Slot newSlot = Slot.getInstance(Slot.SlotType.CM,getInstanceOfClassroom(),getInstanceOfSubject(),
                getListOfGroup(),getListOfProfessor(),newInterval);
        Assertions.assertEquals(newInterval,
                newSlot.getTimeRange());
    }

    @Test
    void ShouldGetProfessor(){
        Assertions.assertEquals(getListOfProfessor(),
                getAnotherInstanceOfSlot().getProfessors());
    }


    @Test
    void differentClassShouldNotBeEqual(){
        Group different = Group.getInstance("Name");
        Slot slot = this.getAnotherInstanceOfSlot();
        Assertions.assertNotEquals(slot,different);

    }

    @Test
    void EqualIsReflexive2(){
        Slot slot1 = this.getInstanceOfSlot();
        Slot slot2 = this.getAnotherInstanceOfSlot();
        Assertions.assertNotEquals(slot1,slot2);
    }

    @Test
    void EqualIsSymmetric(){
        Slot slot1 = this.getInstanceOfSlot();
        Slot slot2 = this.getInstanceOfSlot();
        Assertions.assertTrue(slot1.equals(slot2)&&slot2.equals(slot1));
    }

    @Test
    void HashCodeIsSymmetric(){
        Slot slot1 = this.getInstanceOfSlot();
        Slot slot2 = this.getInstanceOfSlot();
        Assertions.assertTrue(slot1.hashCode()==slot2.hashCode()&&slot2.hashCode()==slot1.hashCode());
    }

    @Test
    void HashCodeIsSymmetric2(){
        Slot slot1 = this.getInstanceOfSlot();
        Slot slot2 = this.getAnotherInstanceOfSlot();
        Assertions.assertFalse(slot1.hashCode()==slot2.hashCode()&&slot2.hashCode()==slot1.hashCode());
    }

    @Test
    void EqualIsSymmetric2(){
        Slot slot1 = this.getInstanceOfSlot();
        Slot slot2 = this.getAnotherInstanceOfSlot();
        Assertions.assertFalse(slot1.equals(slot2)&&slot2.equals(slot1));
    }

    @Test
    void EqualIsTransitive(){
        Slot slot1 = this.getInstanceOfSlot();
        Slot slot2 = this.getInstanceOfSlot();
        Slot slot3 = this.getInstanceOfSlot();
        Assertions.assertTrue(slot1.equals(slot2)&&slot2.equals(slot3)&&slot1.equals(slot3));
    }
    @Test
    void EqualIsTransitive2(){
        Slot slot1 = this.getInstanceOfSlot();
        Slot slot2 = this.getAnotherInstanceOfSlot();
        Slot slot3 = this.getInstanceOfSlot();
        Assertions.assertFalse(slot1.equals(slot2)&&slot2.equals(slot3)&&slot1.equals(slot3));
    }

    @Test
    void HashCodeIsTransitive(){
        Slot testWithSameTime = this.getInstanceOfSlot();
        Slot slot1 = testWithSameTime;
        Slot slot2 = testWithSameTime;
        Slot slot3 = testWithSameTime;
        Assertions.assertTrue(slot1.hashCode()==slot2.hashCode()&&
                slot2.hashCode()==slot3.hashCode()&&
                slot1.hashCode()==slot3.hashCode());
    }

    @Test
    void HashCodeIsTransitive2(){
        Slot slot1 = this.getInstanceOfSlot();
        Slot slot2 = this.getAnotherInstanceOfSlot();
        Slot slot3 = this.getInstanceOfSlot();
        Assertions.assertFalse(slot1.hashCode()==slot2.hashCode()&&
                slot2.hashCode()==slot3.hashCode()&&
                slot1.hashCode()==slot3.hashCode());
    }

    @Test
    void ShoudlReturnToSTringOfSlot(){
        Instant instant = Instant.ofEpochSecond(1672297200);
        Interval timerangeTest = Interval.of(instant, Duration.ofHours(1));
        Slot slot = getInstanceOfSlot();
        slot.setTimeRange(timerangeTest);
        Assertions.assertEquals("Slot(id=-1, type=CM, classroom=Name, subject=name, group=[Name], professors=[Name FNAME], timeRange=2022-12-29T07:00:00Z/2022-12-29T08:00:00Z, memo=Optional.empty)",
                slot.toString());
    }


}