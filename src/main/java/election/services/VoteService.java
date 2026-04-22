package election.services;

import election.data.models.Option;
import election.data.models.Vote;
import election.data.models.Election;
import election.data.repositories.OptionRepository;
import election.data.repositories.VoteRepository;
import election.data.repositories.ElectionRepository;
import election.dtos.requests.VoteRequest;
import election.exceptions.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final ElectionRepository electionRepository;
    private final OptionRepository optionRepository;

    public VoteService(VoteRepository voteRepository, ElectionRepository electionRepository, OptionRepository optionRepository) {
        this.voteRepository = voteRepository;
        this.electionRepository = electionRepository;
        this.optionRepository = optionRepository;
    }

    public Vote castVote(VoteRequest request) {
        Vote vote = new Vote(request.getUserId(), request.getElectionId(), request.getOptionId());
        Election election = electionRepository.findById(vote.getElectionId()).orElseThrow(ElectionNotFoundException::new);

        if (!election.isStarted()) {
            throw new ElectionNotStartedException("Election has not started");
        }
        if (election.isEnded()) {
            throw new ElectionEndedException("Election has ended");
        }
        if (voteRepository.existsByUserIdAndElectionId(vote.getUserId(), vote.getElectionId())) {
            throw new DuplicateVoteException("User has already voted");
        }

        Option option = optionRepository.findById(vote.getOptionId()).orElseThrow(OptionNotFoundException::new);

        if (!option.getElectionId().equals(vote.getElectionId())) {
            throw new InvalidOptionForElectionException("Invalid option");
        }

        vote.setTimestamp(LocalDateTime.now());
        return voteRepository.save(vote);
    }

    public int getVoteCount(String electionId, String optionId) {
        return voteRepository.countByElectionIdAndOptionId(electionId, optionId);
    }
}