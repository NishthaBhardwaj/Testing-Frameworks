package guru.springframework.sfgpetclinic.model;

import guru.springframework.sfgpetclinic.ModelTests;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest implements ModelTests {

    @Test
    void groupAssertions() {
        //given BDD
        Person person = new Person(1L,"Nishtha","Bhardwaj");

        //then
        assertAll("Test Props Set",
                () -> assertEquals("Nishtha", person.getFirstName()),
                ()-> assertEquals("Bhardwaj", person.getLastName()));
    }

    @Test
    void groupAssertionsMsg() {
        //given BDD
        Person person = new Person(1L,"Nishtha","Bhardwaj");

        //then
        assertAll("Test Props Set",
                () -> assertEquals("Nishtha",person.getFirstName(),"firstName failed"),
                ()-> assertEquals("Bhardwaj", person.getLastName(), "lastName failed"));
    }


}