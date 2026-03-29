package services;

import election.data.models.Election;
import election.data.repositories.ElectionRepository;
import election.exceptions.*;
import election.services.ElectionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ElectionServiceTest {

    @Mock
    private ElectionRepository electionRepository;

    @InjectMocks
    private ElectionService electionService;

    @Test
    @DisplayName("Should create election successfully")
    void shouldCreateElectionSuccessfully() {
        Election election = new Election("Presidential", LocalDate.now(), LocalDate.now().plusDays(1));
        when(electionRepository.save(election)).thenReturn(election);

        Election createdElection = electionService.createElection(election);

        assertNotNull(createdElection);
    }

    @Test
    @DisplayName("Should start election successfully")
    void shouldStartElectionSuccessfully() {
        Election election = new Election();
        election.setEndDate(LocalDate.now().plusDays(1));
        when(electionRepository.findById(1L)).thenReturn(Optional.of(election));

        electionService.startElection(1L);

        assertTrue(election.isStarted());
        verify(electionRepository).save(election);
    }

    @Test
    @DisplayName("Should fail to start election if already started")
    void shouldFailToStartElectionIfAlreadyStarted() {
        Election election = new Election();
        election.setStarted(true);
        when(electionRepository.findById(1L)).thenReturn(Optional.of(election));

        assertThrows(ElectionAlreadyStartedException.class, () -> electionService.startElection(1L));
    }

    @Test
    @DisplayName("Should fail to start election if end date passed")
    void shouldFailToStartElectionIfEndDatePassed() {
        Election election = new Election();
        election.setEndDate(LocalDate.now().minusDays(1));
        when(electionRepository.findById(1L)).thenReturn(Optional.of(election));

        assertThrows(ElectionEndedException.class, () -> electionService.startElection(1L));
    }

    @Test
    @DisplayName("Should end election successfully")
    void shouldEndElectionSuccessfully() {
        Election election = new Election();
        election.setStarted(true);
        when(electionRepository.findById(1L)).thenReturn(Optional.of(election));

        electionService.endElection(1L);

        assertTrue(election.isEnded());
    }
}