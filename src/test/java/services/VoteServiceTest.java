package services;

import election.data.models.Vote;
import election.data.models.Election;
import election.data.models.Candidate;
import election.data.repositories.VoteRepository;
import election.data.repositories.ElectionRepository;
import election.data.repositories.CandidateRepository;
import election.exceptions.*;
import election.services.VoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private ElectionRepository electionRepository;

    @Mock
    private CandidateRepository candidateRepository;

    @InjectMocks
    private VoteService voteService;

    @Test
    @DisplayName("Should cast vote successfully")
    void shouldCastVoteSuccessfully() {
        Election election = new Election();
        election.setStarted(true);
        Candidate candidate = new Candidate();
        candidate.setElectionId(1L);
        Vote vote = new Vote(1L, 1L, 1L);

        when(electionRepository.findById(1L)).thenReturn(Optional.of(election));
        when(voteRepository.existsByUserIdAndElectionId(1L, 1L)).thenReturn(false);
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(voteRepository.save(any(Vote.class))).thenReturn(vote);

        Vote castVote = voteService.castVote(vote);

        assertNotNull(castVote);
        verify(voteRepository).save(any(Vote.class));
    }

    @Test
    @DisplayName("Should fail if election does not exist")
    void shouldFailIfElectionDoesNotExist() {
        Vote vote = new Vote(1L, 99L, 1L);
        when(electionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ElectionNotFoundException.class, () -> voteService.castVote(vote));
    }

    @Test
    @DisplayName("Should fail if election not started")
    void shouldFailIfElectionNotStarted() {
        Election election = new Election();
        election.setStarted(false);
        Vote vote = new Vote(1L, 2L, 1L);
        when(electionRepository.findById(2L)).thenReturn(Optional.of(election));

        assertThrows(ElectionNotStartedException.class, () -> voteService.castVote(vote));
    }

    @Test
    @DisplayName("Should fail if election ended")
    void shouldFailIfElectionEnded() {
        Election election = new Election();
        election.setStarted(true);
        election.setEnded(true);
        Vote vote = new Vote(1L, 3L, 1L);
        when(electionRepository.findById(3L)).thenReturn(Optional.of(election));

        assertThrows(ElectionEndedException.class, () -> voteService.castVote(vote));
    }

    @Test
    @DisplayName("Should fail if user already voted")
    void shouldFailIfUserAlreadyVoted() {
        Election election = new Election();
        election.setStarted(true);
        Vote vote = new Vote(1L, 1L, 1L);
        when(electionRepository.findById(1L)).thenReturn(Optional.of(election));
        when(voteRepository.existsByUserIdAndElectionId(1L, 1L)).thenReturn(true);

        assertThrows(DuplicateVoteException.class, () -> voteService.castVote(vote));
    }

    @Test
    @DisplayName("Should fail if candidate does not exist")
    void shouldFailIfCandidateDoesNotExist() {
        Election election = new Election();
        election.setStarted(true);
        Vote vote = new Vote(1L, 1L, 99L);
        when(electionRepository.findById(1L)).thenReturn(Optional.of(election));
        when(voteRepository.existsByUserIdAndElectionId(1L, 1L)).thenReturn(false);
        when(candidateRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(CandidateNotFoundException.class, () -> voteService.castVote(vote));
    }

    @Test
    @DisplayName("Should fail if candidate not in election")
    void shouldFailIfCandidateNotInElection() {
        Election election = new Election();
        election.setStarted(true);
        Candidate candidate = new Candidate();
        candidate.setElectionId(2L);
        Vote vote = new Vote(1L, 1L, 4L);

        when(electionRepository.findById(1L)).thenReturn(Optional.of(election));
        when(voteRepository.existsByUserIdAndElectionId(1L, 1L)).thenReturn(false);
        when(candidateRepository.findById(4L)).thenReturn(Optional.of(candidate));

        assertThrows(InvalidCandidateForElectionException.class, () -> voteService.castVote(vote));
    }

    @Test
    @DisplayName("Should record vote with correct timestamp")
    void shouldRecordVoteWithCorrectTimestamp() {
        Election election = new Election();
        election.setStarted(true);
        Candidate candidate = new Candidate();
        candidate.setElectionId(1L);
        Vote vote = new Vote(1L, 1L, 1L);
        vote.setTimestamp(LocalDateTime.now());

        when(electionRepository.findById(1L)).thenReturn(Optional.of(election));
        when(voteRepository.existsByUserIdAndElectionId(1L, 1L)).thenReturn(false);
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidate));
        when(voteRepository.save(any(Vote.class))).thenReturn(vote);

        Vote castVote = voteService.castVote(vote);

        assertNotNull(castVote.getTimestamp());
    }

    @Test
    @DisplayName("Should increment vote count correctly")
    void shouldIncrementVoteCountCorrectly() {
        when(voteRepository.countByElectionIdAndCandidateId(1L, 1L)).thenReturn(1);

        int voteCount = voteService.getVoteCount(1L, 1L);

        assertEquals(1, voteCount);
    }
}