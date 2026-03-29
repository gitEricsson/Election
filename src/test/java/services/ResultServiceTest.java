package services;

import election.data.models.Candidate;
import election.data.repositories.CandidateRepository;
import election.data.repositories.VoteRepository;
import election.exceptions.TieException;
import election.services.ResultService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private ResultService resultService;

    @Test
    @DisplayName("Should return correct vote counts per candidate")
    void shouldReturnCorrectVoteCountsPerCandidate() {
        Candidate candidate = new Candidate("Jane");
        candidate.setId(1L);
        List<Candidate> listOfCandidates = new java.util.ArrayList<>();
        listOfCandidates.add(candidate);
        when(candidateRepository.findByElectionId(1L)).thenReturn(listOfCandidates);
        when(voteRepository.countByElectionIdAndCandidateId(1L, 1L)).thenReturn(10);

        Map<Candidate, Integer> voteCounts = resultService.getVoteCounts(1L);

        assertEquals(10, voteCounts.get(candidate));
    }

    @Test
    @DisplayName("Should return winner correctly")
    void shouldReturnWinnerCorrectly() {
        Candidate candidateOne = new Candidate("Jane");
        candidateOne.setId(1L);
        Candidate candidateTwo = new Candidate("John");
        candidateTwo.setId(2L);

        List<Candidate> listOfCandidates = new java.util.ArrayList<>();
        listOfCandidates.add(candidateOne);
        listOfCandidates.add(candidateTwo);
        when(candidateRepository.findByElectionId(1L)).thenReturn(listOfCandidates);
        when(voteRepository.countByElectionIdAndCandidateId(1L, 1L)).thenReturn(20);
        when(voteRepository.countByElectionIdAndCandidateId(1L, 2L)).thenReturn(10);

        Candidate winner = resultService.getWinner(1L);

        assertEquals(candidateOne, winner);
    }

    @Test
    @DisplayName("Should handle tie between candidates")
    void shouldHandleTieBetweenCandidates() {
        Candidate candidateOne = new Candidate("Jane");
        candidateOne.setId(1L);
        Candidate candidateTwo = new Candidate("John");
        candidateTwo.setId(2L);

        List<Candidate> listOfCandidates = new java.util.ArrayList<>();
        listOfCandidates.add(candidateOne);
        listOfCandidates.add(candidateTwo);
        when(candidateRepository.findByElectionId(1L)).thenReturn(listOfCandidates);
        when(voteRepository.countByElectionIdAndCandidateId(1L, 1L)).thenReturn(15);
        when(voteRepository.countByElectionIdAndCandidateId(1L, 2L)).thenReturn(15);

        assertThrows(TieException.class, () -> resultService.getWinner(1L));
    }
}