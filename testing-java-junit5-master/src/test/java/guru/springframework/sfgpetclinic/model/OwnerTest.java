package guru.springframework.sfgpetclinic.model;

import guru.springframework.sfgpetclinic.CustomArgsProvider;
import guru.springframework.sfgpetclinic.ModelTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

class OwnerTest implements ModelTests {

    @Test
    void dependentAssertions(){
        Owner owner = new Owner(1L,"Vatsal","Dasika");
        owner.setCity("Key West");
        owner.setTelephone("1234561234");

        assertAll("Properties Test",
                () -> assertAll("Person Prppeties",
                            () -> assertEquals("Vatsal" , owner.getFirstName(),"FirstName did not match"),
                            () -> assertEquals("Dasika",owner.getLastName())),
                () -> assertAll("ownere Properties",
                            () -> assertEquals("Key West",owner.getCity(), "City did not match"),
                        ()-> assertEquals("1234561234",owner.getTelephone()))
                );

        assertThat(owner.getCity(),is("Key West"));

    }

    @DisplayName("Value Source Test ")
    @ParameterizedTest(name = "{displayName} - {index} -   {arguments}")
    @ValueSource(strings = {"spring" , "framework"})
    void testValueSource(String val) {
        System.out.println(val);
    }

    @DisplayName("Enum Source Test ")
    @ParameterizedTest(name = "{displayName} - {index} -   {arguments}")
    @EnumSource(OwnerType.class)
    void enumTest(OwnerType ownerType){
        System.out.println(ownerType);

    }

    @DisplayName("CSV Input Source Test ")
    @ParameterizedTest(name = "{displayName} - [{index}] -   {arguments}")
    @CsvSource({
            "FL, 1, 1",
            "OH, 2, 2",
            "MI, 1, 1",
    })
    void csvInputTest(String stateName, int val1, int val2 ){
        System.out.println(stateName + "=" + val1 + ":" +val2);

    }

    @DisplayName("CSV From File Test ")
    @ParameterizedTest(name = "{displayName} - [{index}] -   {arguments}")
    @CsvFileSource(resources = "/input.csv", numLinesToSkip = 1)
    void csvFromFileTest(String stateName, int val1, int val2 ){
        System.out.println(stateName + "=" + val1 + ":" +val2);

    }

    @DisplayName("Method Provider Test ")
    @ParameterizedTest(name = "{displayName} - [{index}] -   {arguments}")
    @MethodSource("getargs")
    void fromMethodTest(String stateName, int val1, int val2 ){
        System.out.println(stateName + "=" + val1 + ":" +val2);
    }

    static Stream<Arguments> getargs(){
        return Stream.of(
                Arguments.of("FL",1,1),
                Arguments.of("OH",1,1));

    }

    @DisplayName("Custom Provider Test ")
    @ParameterizedTest(name = "{displayName} - [{index}] -   {arguments}")
    @ArgumentsSource(CustomArgsProvider.class)
    void fromCustomProviderTest(String stateName, int val1, int val2 ){
        System.out.println(stateName + "=" + val1 + ":" +val2);
    }
}