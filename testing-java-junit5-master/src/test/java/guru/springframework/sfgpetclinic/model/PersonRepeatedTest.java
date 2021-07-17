package guru.springframework.sfgpetclinic.model;

import guru.springframework.sfgpetclinic.ModelRepeatedTests;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.TestInfo;

public class PersonRepeatedTest implements ModelRepeatedTests {

    @RepeatedTest(value = 5, name = "{displayName}: {currentRepetition}"  )
    @DisplayName("My Repeated Test")
    void myRepeatedTest(){
        //todo -- impl

    }

    @RepeatedTest(5)
    void myRepeatedTestWithId(TestInfo testInfo, RepetitionInfo repetitionInfo){
        System.out.println(testInfo.getDisplayName() + ":" +  testInfo.getTags() + ": "+
                repetitionInfo.getTotalRepetitions());

    }

    @RepeatedTest(value = 5, name = "{displayName} |  {currentRepetition}"  )
    @DisplayName("My Assignment Repeated Test")
    void myAssignmentRepeated(){
        //todo -- impl

    }
}
