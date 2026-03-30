package election.services;

import election.data.models.Candidate;
import election.data.models.Election;
import election.data.repositories.CandidateRepository;
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
    private final CandidateRepository candidateRepository;
    private final ElectionRepository electionRepository;

    public ResultService(VoteRepository voteRepository, CandidateRepository candidateRepository, ElectionRepository electionRepository) {
        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
        this.electionRepository = electionRepository;
    }

    public Map<Candidate, Integer> getVoteCounts(Long electionId) {
        List<Candidate> candidates = candidateRepository.findByElectionId(electionId);
        Map<Candidate, Integer> voteCounts = new HashMap<>();

        for (Candidate candidate : candidates) {
            int count = voteRepository.countByElectionIdAndCandidateId(electionId, candidate.getId());
            voteCounts.put(candidate, count);
        }
        return voteCounts;
    }

    public Candidate getWinner(Long electionId) {
        Map<Candidate, Integer> counts = getVoteCounts(electionId);

        int maxVotes = -1;
        Candidate winner = null;
        boolean isTie = false;

        for (Map.Entry<Candidate, Integer> entry : counts.entrySet()) {
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

    public ElectionResultResponse getElectionResults(Long electionId) {
        Election election = electionRepository.findById(electionId).orElseThrow(ElectionNotFoundException::new);
        Map<Candidate, Integer> counts = getVoteCounts(electionId);

        Map<String, Integer> candidateVotes = new HashMap<>();
        for (Map.Entry<Candidate, Integer> entry : counts.entrySet()) {
            candidateVotes.put(entry.getKey().getName(), entry.getValue());
        }

        String winnerName;
        try {
            Candidate winner = getWinner(electionId);
            winnerName = winner.getName();
        } catch (TieException e) {
            winnerName = "Tie";
        }

        return new ElectionResultResponse(election.getTitle(), candidateVotes, winnerName);
    }
}