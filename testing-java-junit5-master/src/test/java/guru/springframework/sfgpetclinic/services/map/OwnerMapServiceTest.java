package guru.springframework.sfgpetclinic.services.map;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.PetTypeService;
import org.junit.jupiter.api.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Owner Map service test")
class OwnerMapServiceTest {

    PetTypeService petTypeService;
    PetService petService;
    OwnerMapService PetMapService;


    @BeforeEach
    void setUp() {
        petTypeService = new PetTypeMapService();
        petService = new PetMapService();
        PetMapService = new OwnerMapService(petTypeService,petService);

    }

    @Test
    @DisplayName("Verify Zero owner")
    void ownerAreZero(){
        int ownerCount = PetMapService.findAll().size();
        assertThat(ownerCount).isZero();
    }

    @DisplayName("Pet Type -")
    @Nested
    class TestCreatePetTypes {
        @BeforeEach
        void setUp() {
            PetType petType = new PetType(1L,"Dog");
            PetType petType2 = new PetType(1L,"Cat");
            petTypeService.save(petType);
            petTypeService.save(petType2);
        }

        @Test
        void testPetCount(){
            int petTypeCount = petTypeService.findAll().size();
            assertThat(petTypeCount).isNotZero().isEqualTo(1);
        }
    }

    @DisplayName("verify still zero owner")
    @Test
    void ownersAreStillZero(){
        int ownerCount = PetMapService.findAll().size();
        assertThat(ownerCount).isZero();

    }
}