package services;
 
import election.data.models.Option;
import election.data.repositories.OptionRepository;
import election.data.repositories.ElectionRepository;
import election.exceptions.*;
import election.services.OptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
class OptionServiceTest {
 
    @Mock
    private OptionRepository optionRepository;
 
    @Mock
    private ElectionRepository electionRepository;
 
    @InjectMocks
    private OptionService optionService;
 
    @Test
    @DisplayName("Should add option successfully")
    void shouldAddOptionSuccessfully() {
        Option option = new Option("Option 1");
        when(electionRepository.existsById("1")).thenReturn(true);
        when(optionRepository.existsByElectionIdAndName("1", "Option 1")).thenReturn(false);
        when(optionRepository.save(any(Option.class))).thenReturn(option);
 
        Option savedOption = optionService.addOption("1", option);
 
        assertNotNull(savedOption);
        verify(optionRepository).save(option);
    }
 
    @Test
    @DisplayName("Should fail to add option to non-existent election")
    void shouldFailToAddOptionToNonExistentElection() {
        Option option = new Option("Option 1");
        when(electionRepository.existsById("99")).thenReturn(false);
 
        assertThrows(ElectionNotFoundException.class, () -> optionService.addOption("99", option));
    }
 
    @Test
    @DisplayName("Should fail to add duplicate option to same election")
    void shouldFailToAddDuplicateOptionToSameElection() {
        Option option = new Option("Option 1");
        when(electionRepository.existsById("1")).thenReturn(true);
        when(optionRepository.existsByElectionIdAndName("1", "Option 1")).thenReturn(true);
 
        assertThrows(DuplicateOptionException.class, () -> optionService.addOption("1", option));
    }
 
    @Test
    @DisplayName("Should retrieve options by election")
    void shouldRetrieveOptionsByElection() {
        List<Option> optionsList = new java.util.ArrayList<>();
        optionsList.add(new Option("Option 1"));
        when(optionRepository.findByElectionId("1")).thenReturn(optionsList);
 
        List<Option> options = optionService.getOptionsByElection("1");
 
        assertFalse(options.isEmpty());
    }
}