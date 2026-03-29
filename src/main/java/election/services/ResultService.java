package election.services;

import election.data.models.Candidate;
import election.data.repositories.CandidateRepository;
import election.data.repositories.VoteRepository;
import election.exceptions.*;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ResultService {

    private final VoteRepository voteRepository;
    private final CandidateRepository candidateRepository;

    public ResultService(VoteRepository voteRepository, CandidateRepository candidateRepository) {
        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
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
}