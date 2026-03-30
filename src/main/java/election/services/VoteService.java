package election.services;

import election.data.models.Vote;
import election.data.models.Election;
import election.data.models.Candidate;
import election.data.repositories.VoteRepository;
import election.data.repositories.ElectionRepository;
import election.data.repositories.CandidateRepository;
import election.dtos.requests.VoteRequest;
import election.exceptions.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;

    public VoteService(VoteRepository voteRepository, ElectionRepository electionRepository, CandidateRepository candidateRepository) {
        this.voteRepository = voteRepository;
        this.electionRepository = electionRepository;
        this.candidateRepository = candidateRepository;
    }

    public Vote castVote(VoteRequest request) {
        Vote vote = new Vote(request.getUserId(), request.getElectionId(), request.getCandidateId());
        Election election = electionRepository.findById(vote.getElectionId()).orElseThrow(ElectionNotFoundException::new);

        if (!election.isStarted()) {
            throw new ElectionNotStartedException("Election not started");
        }
        if (election.isEnded()) {
            throw new ElectionEndedException("Election ended");
        }
        if (voteRepository.existsByUserIdAndElectionId(vote.getUserId(), vote.getElectionId())) {
            throw new DuplicateVoteException("Duplicate vote");
        }

        Candidate candidate = candidateRepository.findById(vote.getCandidateId()).orElseThrow(CandidateNotFoundException::new);

        if (!candidate.getElectionId().equals(vote.getElectionId())) {
            throw new InvalidCandidateForElectionException("Invalid candidate");
        }

        vote.setTimestamp(LocalDateTime.now());
        return voteRepository.save(vote);
    }

    public int getVoteCount(Long electionId, Long candidateId) {
        return voteRepository.countByElectionIdAndCandidateId(electionId, candidateId);
    }
}