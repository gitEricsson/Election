package services;
 
import election.data.models.Vote;
import election.data.models.Election;
import election.data.models.Option;
import election.data.repositories.VoteRepository;
import election.data.repositories.ElectionRepository;
import election.data.repositories.OptionRepository;
import election.exceptions.*;
import election.dtos.requests.VoteRequest;
import election.services.VoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
 
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
    private OptionRepository optionRepository;
 
    @InjectMocks
    private VoteService voteService;
 
    @Test
    @DisplayName("Should cast vote successfully")
    void shouldCastVoteSuccessfully() {
        Election election = new Election();
        election.setStarted(true);
        Option option = new Option();
        option.setElectionId("1");
        VoteRequest request = new VoteRequest();
        request.setUserId("1");
        request.setElectionId("1");
        request.setOptionId("1");
        Vote vote = new Vote("1", "1", "1");
 
        when(electionRepository.findById("1")).thenReturn(Optional.of(election));
        when(voteRepository.existsByUserIdAndElectionId("1", "1")).thenReturn(false);
        when(optionRepository.findById("1")).thenReturn(Optional.of(option));
        when(voteRepository.save(any(Vote.class))).thenReturn(vote);
 
        Vote castVote = voteService.castVote(request);
 
        assertNotNull(castVote);
        verify(voteRepository).save(any(Vote.class));
    }
 
    @Test
    @DisplayName("Should fail if election does not exist")
    void shouldFailIfElectionDoesNotExist() {
        VoteRequest request = new VoteRequest();
        request.setUserId("1");
        request.setElectionId("99");
        request.setOptionId("1");
        when(electionRepository.findById("99")).thenReturn(Optional.empty());
 
        assertThrows(ElectionNotFoundException.class, () -> voteService.castVote(request));
    }
 
    @Test
    @DisplayName("Should fail if election not started")
    void shouldFailIfElectionNotStarted() {
        Election election = new Election();
        election.setStarted(false);
        VoteRequest request = new VoteRequest();
        request.setUserId("1");
        request.setElectionId("2");
        request.setOptionId("1");
        when(electionRepository.findById("2")).thenReturn(Optional.of(election));
 
        assertThrows(ElectionNotStartedException.class, () -> voteService.castVote(request));
    }
 
    @Test
    @DisplayName("Should fail if election ended")
    void shouldFailIfElectionEnded() {
        Election election = new Election();
        election.setStarted(true);
        election.setEnded(true);
        VoteRequest request = new VoteRequest();
        request.setUserId("1");
        request.setElectionId("3");
        request.setOptionId("1");
        when(electionRepository.findById("3")).thenReturn(Optional.of(election));
 
        assertThrows(ElectionEndedException.class, () -> voteService.castVote(request));
    }
 
    @Test
    @DisplayName("Should fail if user already voted")
    void shouldFailIfUserAlreadyVoted() {
        Election election = new Election();
        election.setStarted(true);
        VoteRequest request = new VoteRequest();
        request.setUserId("1");
        request.setElectionId("1");
        request.setOptionId("1");
        when(electionRepository.findById("1")).thenReturn(Optional.of(election));
        when(voteRepository.existsByUserIdAndElectionId("1", "1")).thenReturn(true);
 
        assertThrows(DuplicateVoteException.class, () -> voteService.castVote(request));
    }
 
    @Test
    @DisplayName("Should fail if option does not exist")
    void shouldFailIfOptionDoesNotExist() {
        Election election = new Election();
        election.setStarted(true);
        VoteRequest request = new VoteRequest();
        request.setUserId("1");
        request.setElectionId("1");
        request.setOptionId("99");
        when(electionRepository.findById("1")).thenReturn(Optional.of(election));
        when(voteRepository.existsByUserIdAndElectionId("1", "1")).thenReturn(false);
        when(optionRepository.findById("99")).thenReturn(Optional.empty());
 
        assertThrows(OptionNotFoundException.class, () -> voteService.castVote(request));
    }
 
    @Test
    @DisplayName("Should fail if option not in election")
    void shouldFailIfOptionNotInElection() {
        Election election = new Election();
        election.setStarted(true);
        Option option = new Option();
        option.setElectionId("2");
        VoteRequest request = new VoteRequest();
        request.setUserId("1");
        request.setElectionId("1");
        request.setOptionId("4");
 
        when(electionRepository.findById("1")).thenReturn(Optional.of(election));
        when(voteRepository.existsByUserIdAndElectionId("1", "1")).thenReturn(false);
        when(optionRepository.findById("4")).thenReturn(Optional.of(option));
 
        assertThrows(InvalidOptionForElectionException.class, () -> voteService.castVote(request));
    }
 
    @Test
    @DisplayName("Should increment vote count correctly")
    void shouldIncrementVoteCountCorrectly() {
        when(voteRepository.countByElectionIdAndOptionId("1", "1")).thenReturn(1);
 
        int voteCount = voteService.getVoteCount("1", "1");
 
        assertEquals(1, voteCount);
    }
}