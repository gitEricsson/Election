package services;

import election.data.models.Candidate;
import election.data.models.Election;
import election.data.repositories.CandidateRepository;
import election.data.repositories.ElectionRepository;
import election.data.repositories.VoteRepository;
import election.dtos.responses.ElectionResultResponse;
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
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResultServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private ElectionRepository electionRepository;

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

        List<Candidate> t = new java.util.ArrayList<>();
        t.add(candidateOne);
        t.add(candidateTwo);
        when(candidateRepository.findByElectionId(1L)).thenReturn(t);
        when(voteRepository.countByElectionIdAndCandidateId(1L, 1L)).thenReturn(15);
        when(voteRepository.countByElectionIdAndCandidateId(1L, 2L)).thenReturn(15);

        assertThrows(TieException.class, () -> resultService.getWinner(1L));
    }

    @Test
    @DisplayName("Should return election results")
    void shouldReturnElectionResults() {
        Candidate candidateOne = new Candidate("Jane");
        candidateOne.setId(1L);
        Candidate candidateTwo = new Candidate("John");
        candidateTwo.setId(2L);
        
        Election election = new Election();
        election.setTitle("My Election");

        List<Candidate> listOfCandidates = new java.util.ArrayList<>();
        listOfCandidates.add(candidateOne);
        listOfCandidates.add(candidateTwo);
        
        when(electionRepository.findById(1L)).thenReturn(Optional.of(election));
        when(candidateRepository.findByElectionId(1L)).thenReturn(listOfCandidates);
        when(voteRepository.countByElectionIdAndCandidateId(1L, 1L)).thenReturn(20);
        when(voteRepository.countByElectionIdAndCandidateId(1L, 2L)).thenReturn(10);

        ElectionResultResponse response = resultService.getElectionResults(1L);

        assertEquals("My Election", response.getElectionTitle());
        assertEquals("Jane", response.getWinnerName());
        assertEquals(20, response.getCandidateVotes().get("Jane"));
        assertEquals(10, response.getCandidateVotes().get("John"));
    }
}