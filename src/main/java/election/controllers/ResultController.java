package election.controllers;

import election.data.models.Candidate;
import election.dtos.responses.ElectionResultResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import election.services.ResultService;

@RestController
@RequestMapping("/api/winner")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    @GetMapping("/{electionId}")
    public Candidate getWinner(@PathVariable Long electionId) {
        return resultService.getWinner(electionId);
    }

    @GetMapping("/{electionId}/results")
    public ElectionResultResponse getElectionResults(@PathVariable Long electionId) {
        return resultService.getElectionResults(electionId);
    }
}