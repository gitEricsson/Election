package services;
 
import election.data.models.Election;
import election.data.models.Option;
import election.data.repositories.ElectionRepository;
import election.exceptions.*;
import election.dtos.requests.ElectionRequest;
import election.dtos.responses.ElectionResponse;
import election.services.ElectionService;
import election.services.OptionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
 
@ExtendWith(MockitoExtension.class)
class ElectionServiceTest {
 
    @Mock
    private ElectionRepository electionRepository;
 
    @Mock
    private OptionService optionService;
 
    @InjectMocks
    private ElectionService electionService;
 
    @Test
    @DisplayName("Should create election successfully with options")
    void shouldCreateElectionSuccessfully() {
        ElectionRequest request = new ElectionRequest();
        request.setTitle("Presidential");
        request.setDescription("National Election");
        request.setCreatorId("creator123");
        request.setStartDate(LocalDate.now());
        request.setEndDate(LocalDate.now().plusDays(1));
        request.setOptions(Arrays.asList("Option A", "Option B"));
        
        Election savedElection = new Election("Presidential", "National Election", "creator123", LocalDate.now(), LocalDate.now().plusDays(1));
        savedElection.setId("1");
        
        when(electionRepository.save(any(Election.class))).thenReturn(savedElection);
 
        ElectionResponse createdElection = electionService.createElection(request);
 
        assertNotNull(createdElection);
        assertEquals("1", createdElection.getId());
        verify(optionService, times(2)).addOption(eq("1"), any(Option.class));
    }
 
    @Test
    @DisplayName("Should start election successfully by creator")
    void shouldStartElectionSuccessfully() {
        Election election = new Election();
        election.setCreatorId("creator123");
        election.setEndDate(LocalDate.now().plusDays(1));
        when(electionRepository.findById("1")).thenReturn(Optional.of(election));
 
        electionService.startElection("1", "creator123");
 
        assertTrue(election.isStarted());
        verify(electionRepository).save(election);
    }
 
    @Test
    @DisplayName("Should fail to start election if user is not creator")
    void shouldFailToStartElectionIfUserIsNotCreator() {
        Election election = new Election();
        election.setCreatorId("creator123");
        when(electionRepository.findById("1")).thenReturn(Optional.of(election));
 
        assertThrows(UnauthorizedAccessException.class, () -> electionService.startElection("1", "wrongUser"));
    }
 
    @Test
    @DisplayName("Should fail to start election if already started")
    void shouldFailToStartElectionIfAlreadyStarted() {
        Election election = new Election();
        election.setCreatorId("creator123");
        election.setStarted(true);
        when(electionRepository.findById("1")).thenReturn(Optional.of(election));
 
        assertThrows(ElectionAlreadyStartedException.class, () -> electionService.startElection("1", "creator123"));
    }
 
    @Test
    @DisplayName("Should end election successfully by creator")
    void shouldEndElectionSuccessfully() {
        Election election = new Election();
        election.setCreatorId("creator123");
        election.setStarted(true);
        when(electionRepository.findById("1")).thenReturn(Optional.of(election));
 
        electionService.endElection("1", "creator123");
 
        assertTrue(election.isEnded());
        verify(electionRepository).save(election);
    }
 
    @Test
    @DisplayName("Should delete election successfully by creator")
    void shouldDeleteElectionSuccessfully() {
        Election election = new Election();
        election.setCreatorId("creator123");
        when(electionRepository.findById("1")).thenReturn(Optional.of(election));
 
        electionService.deleteElection("1", "creator123");
 
        verify(electionRepository).deleteById("1");
    }
}