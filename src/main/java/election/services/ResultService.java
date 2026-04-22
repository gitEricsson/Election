package election.services;

import election.data.models.Option;
import election.data.models.Election;
import election.data.repositories.OptionRepository;
import election.data.repositories.ElectionRepository;
import election.data.repositories.VoteRepository;
import election.dtos.responses.ElectionResultResponse;
import election.exceptions.*;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResultService {

    private final VoteRepository voteRepository;
    private final OptionRepository optionRepository;
    private final ElectionRepository electionRepository;

    public ResultService(VoteRepository voteRepository, OptionRepository optionRepository, ElectionRepository electionRepository) {
        this.voteRepository = voteRepository;
        this.optionRepository = optionRepository;
        this.electionRepository = electionRepository;
    }

    public Map<Option, Integer> getVoteCounts(String electionId) {
        List<Option> options = optionRepository.findByElectionId(electionId);
        Map<Option, Integer> voteCounts = new HashMap<>();

        for (Option option : options) {
            int count = voteRepository.countByElectionIdAndOptionId(electionId, option.getId());
            voteCounts.put(option, count);
        }
        return voteCounts;
    }

    public Option getWinner(String electionId) {
        Election election = electionRepository.findById(electionId).orElseThrow(ElectionNotFoundException::new);
        if (!election.isEnded()) {
            throw new ElectionOngoingException("Election is still ongoing");
        }
        Map<Option, Integer> counts = getVoteCounts(electionId);

        int maxVotes = -1;
        Option winner = null;
        boolean isTie = false;

        for (Map.Entry<Option, Integer> entry : counts.entrySet()) {
            int votes = entry.getValue();

            if (votes > maxVotes) {
                maxVotes = votes;
                winner = entry.getKey();
                isTie = false;
                continue;
            }

            if (votes == maxVotes) {
                isTie = true;
            }
        }

        if (isTie) {
            throw new TieException("It was a Tie");
        }

        return winner;
    }

    public ElectionResultResponse getElectionResults(String electionId) {
        Election election = electionRepository.findById(electionId).orElseThrow(ElectionNotFoundException::new);
        Map<Option, Integer> counts = getVoteCounts(electionId);

        Map<String, Integer> optionVotes = new HashMap<>();
        for (Map.Entry<Option, Integer> entry : counts.entrySet()) {
            optionVotes.put(entry.getKey().getName(), entry.getValue());
        }

        String winnerName;
        if (election.isEnded()) {
            try {
                Option winner = getWinner(electionId);
                winnerName = (winner != null) ? winner.getName() : "No winner yet";
            } catch (TieException e) {
                winnerName = "Tie";
            }
        } else {
            winnerName = "Election ongoing";
        }

        return new ElectionResultResponse(election.getTitle(), optionVotes, winnerName);
    }
}