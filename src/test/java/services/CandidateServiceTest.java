package services;

import election.data.models.Candidate;
import election.data.repositories.CandidateRepository;
import election.data.repositories.ElectionRepository;
import election.exceptions.*;
import election.services.CandidateService;
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
class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private ElectionRepository electionRepository;

    @InjectMocks
    private CandidateService candidateService;

    @Test
    @DisplayName("Should add candidate successfully")
    void shouldAddCandidateSuccessfully() {
        Candidate candidate = new Candidate("Jane Doe");
        when(electionRepository.existsById(1L)).thenReturn(true);
        when(candidateRepository.existsByElectionIdAndName(1L, "Jane Doe")).thenReturn(false);
        when(candidateRepository.save(any(Candidate.class))).thenReturn(candidate);

        Candidate savedCandidate = candidateService.addCandidate(1L, candidate);

        assertNotNull(savedCandidate);
        verify(candidateRepository).save(candidate);
    }

    @Test
    @DisplayName("Should fail to add candidate to non-existent election")
    void shouldFailToAddCandidateToNonExistentElection() {
        Candidate candidate = new Candidate("Jane Doe");
        when(electionRepository.existsById(99L)).thenReturn(false);

        assertThrows(ElectionNotFoundException.class, () -> candidateService.addCandidate(99L, candidate));
    }

    @Test
    @DisplayName("Should fail to add duplicate candidate to same election")
    void shouldFailToAddDuplicateCandidateToSameElection() {
        Candidate candidate = new Candidate("Jane Doe");
        when(electionRepository.existsById(1L)).thenReturn(true);
        when(candidateRepository.existsByElectionIdAndName(1L, "Jane Doe")).thenReturn(true);

        assertThrows(DuplicateCandidateException.class, () -> candidateService.addCandidate(1L, candidate));
    }

    @Test
    @DisplayName("Should retrieve candidates by election")
    void shouldRetrieveCandidatesByElection() {
        List<Candidate> candidateslist = new java.util.ArrayList<>();
        candidateslist.add(new Candidate("Jane Doe"));
        when(candidateRepository.findByElectionId(1L)).thenReturn(candidateslist);

        List<Candidate> candidates = candidateService.getCandidatesByElection(1L);

        assertFalse(candidates.isEmpty());
    }

    @Test
    @DisplayName("Should return empty list if no candidates exist")
    void shouldReturnEmptyListIfNoCandidatesExist() {
        List<Candidate> emptyCandidateslist = new java.util.ArrayList<>();
        when(candidateRepository.findByElectionId(1L)).thenReturn(emptyCandidateslist);

        List<Candidate> candidates = candidateService.getCandidatesByElection(1L);

        assertTrue(candidates.isEmpty());
    }
}