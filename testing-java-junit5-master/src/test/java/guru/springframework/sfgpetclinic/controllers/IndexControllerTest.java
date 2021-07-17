package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.ControllerTests;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class IndexControllerTest implements ControllerTests {

    IndexController controller;

    @BeforeEach
    void setUp() {
        controller = new IndexController();
    }

    @DisplayName("Test Proper View name is returned for index page")
    @Test
    void index() {
        assertEquals("index",controller.index(),"wrong view returned");
        assertEquals("index",controller.index(),"wrong view returned");
        assertEquals("index",controller.index(),() -> "Another expensive message"+
                "Make me only if you have to");

        assertThat(controller.index()).isEqualTo("index")
        .contains("in");
    }

    @Test
    @DisplayName("Test Exception")
    void oupsHandler() {

        assertThrows(ValueNotFoundException.class,() -> {
            controller.oupsHandler();
                });

//        assertTrue("notimplemented".equals(controller.oupsHandler()), () -> "This is some expensive" +
//                "Message to build" +
//                "for my test");

    }

    @Test
    @Disabled("Demo of the timeout")
    void testTimeout(){
        assertTimeout(Duration.ofMillis(100),() ->{
            Thread.sleep(5000);
            System.out.println("I got here");
        });
    }

    @Test
    @Disabled("Demo of the timeout")
    void testTimeoutPrompt(){
        assertTimeoutPreemptively(Duration.ofMillis(100),() ->{
            Thread.sleep(5000);
            System.out.println("I got here 232345674");
        });
    }

    @Disabled
    @Test
    void testAssumptionTrue() {
        assumeTrue("Nishtha".equalsIgnoreCase(System.getenv("NISHTHA_RUNTIME")));

    }

    @Test
    void testAssumptionTrueAssumptionsIsTure() {
        assumeTrue("Nishtha".equalsIgnoreCase("Nishtha"));

    }

    @Test
    @EnabledOnOs(OS.MAC)
    void testMeOnMacOS(){

    }
    @Test
    @EnabledOnOs(OS.WINDOWS)
    void testMeOnWindowsOS(){

    }
    @Test
    @EnabledOnJre(JRE.JAVA_8)
    void testMeOnJava8(){

    }
    @Test

    @EnabledOnJre(JRE.JAVA_11)
    void testMeOnJava11(){

    }
    @Test
    @EnabledIfEnvironmentVariable(named = "USER",matches = "Nishtha")
    void testIfUserJT(){

    }
    @Test
    @EnabledIfEnvironmentVariable(named = "USER",matches = "fred")
    void testIfUserFred(){

    }
}