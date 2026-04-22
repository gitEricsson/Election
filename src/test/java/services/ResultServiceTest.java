package services;

import election.data.models.Option;
import election.data.models.Election;
import election.data.repositories.OptionRepository;
import election.data.repositories.ElectionRepository;
import election.data.repositories.VoteRepository;
import election.dtos.responses.ElectionResultResponse;
import election.exceptions.TieException;
import election.exceptions.ElectionOngoingException;
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
    private OptionRepository optionRepository;

    @Mock
    private ElectionRepository electionRepository;

    @InjectMocks
    private ResultService resultService;

    @Test
    @DisplayName("Should return correct vote counts per option")
    void shouldReturnCorrectVoteCountsPerOption() {
        Option option = new Option("Option 1");
        option.setId("1");
        List<Option> listOfOptions = new java.util.ArrayList<>();
        listOfOptions.add(option);
        when(optionRepository.findByElectionId("1")).thenReturn(listOfOptions);
        when(voteRepository.countByElectionIdAndOptionId("1", "1")).thenReturn(10);

        Map<Option, Integer> voteCounts = resultService.getVoteCounts("1");

        assertEquals(10, voteCounts.get(option));
    }

    @Test
    @DisplayName("Should return winner correctly")
    void shouldReturnWinnerCorrectly() {
        Option optionOne = new Option("Option 1");
        optionOne.setId("1");
        Option optionTwo = new Option("Option 2");
        optionTwo.setId("2");

        Election election = new Election();
        election.setEnded(true);

        List<Option> listOfOptions = new java.util.ArrayList<>();
        listOfOptions.add(optionOne);
        listOfOptions.add(optionTwo);
        when(optionRepository.findByElectionId("1")).thenReturn(listOfOptions);
        when(voteRepository.countByElectionIdAndOptionId("1", "1")).thenReturn(20);
        when(voteRepository.countByElectionIdAndOptionId("1", "2")).thenReturn(10);
        when(electionRepository.findById("1")).thenReturn(Optional.of(election));

        Option winner = resultService.getWinner("1");

        assertEquals(optionOne, winner);
    }

    @Test
    @DisplayName("Should handle tie between options")
    void shouldHandleTieBetweenOptions() {
        Option optionOne = new Option("Option 1");
        optionOne.setId("1");
        Option optionTwo = new Option("Option 2");
        optionTwo.setId("2");

        Election election = new Election();
        election.setEnded(true);

        List<Option> t = new java.util.ArrayList<>();
        t.add(optionOne);
        t.add(optionTwo);
        when(optionRepository.findByElectionId("1")).thenReturn(t);
        when(voteRepository.countByElectionIdAndOptionId("1", "1")).thenReturn(15);
        when(voteRepository.countByElectionIdAndOptionId("1", "2")).thenReturn(15);
        when(electionRepository.findById("1")).thenReturn(Optional.of(election));

        assertThrows(TieException.class, () -> resultService.getWinner("1"));
    }

    @Test
    @DisplayName("Should return election results")
    void shouldReturnElectionResults() {
        Option optionOne = new Option("Option 1");
        optionOne.setId("1");
        Option optionTwo = new Option("Option 2");
        optionTwo.setId("2");
        
        Election election = new Election();
        election.setTitle("My Election");
        election.setEnded(true);

        List<Option> listOfOptions = new java.util.ArrayList<>();
        listOfOptions.add(optionOne);
        listOfOptions.add(optionTwo);
        
        when(electionRepository.findById("1")).thenReturn(Optional.of(election));
        when(optionRepository.findByElectionId("1")).thenReturn(listOfOptions);
        when(voteRepository.countByElectionIdAndOptionId("1", "1")).thenReturn(20);
        when(voteRepository.countByElectionIdAndOptionId("1", "2")).thenReturn(10);

        ElectionResultResponse response = resultService.getElectionResults("1");

        assertEquals("My Election", response.getElectionTitle());
        assertEquals("Option 1", response.getWinnerName());
        assertEquals(20, response.getOptionVotes().get("Option 1"));
        assertEquals(10, response.getOptionVotes().get("Option 2"));
    }

    @Test
    @DisplayName("Should return election results with ongoing status when election is not ended")
    void shouldReturnElectionResultsWithOngoingStatus() {
        Option optionOne = new Option("Option 1");
        optionOne.setId("1");
        Option optionTwo = new Option("Option 2");
        optionTwo.setId("2");
        
        Election election = new Election();
        election.setTitle("My Election");
        election.setEnded(false);

        List<Option> listOfOptions = new java.util.ArrayList<>();
        listOfOptions.add(optionOne);
        listOfOptions.add(optionTwo);
        
        when(electionRepository.findById("1")).thenReturn(Optional.of(election));
        when(optionRepository.findByElectionId("1")).thenReturn(listOfOptions);
        when(voteRepository.countByElectionIdAndOptionId("1", "1")).thenReturn(20);
        when(voteRepository.countByElectionIdAndOptionId("1", "2")).thenReturn(10);

        ElectionResultResponse response = resultService.getElectionResults("1");

        assertEquals("My Election", response.getElectionTitle());
        assertEquals("Election ongoing", response.getWinnerName());
        assertEquals(20, response.getOptionVotes().get("Option 1"));
        assertEquals(10, response.getOptionVotes().get("Option 2"));
    }

    @Test
    @DisplayName("Should throw ElectionOngoingException when getting winner for ongoing election")
    void shouldThrowElectionOngoingExceptionWhenGettingWinnerForOngoingElection() {
        Election election = new Election();
        election.setEnded(false);

        when(electionRepository.findById("1")).thenReturn(Optional.of(election));

        assertThrows(ElectionOngoingException.class, () -> resultService.getWinner("1"));
    }
}