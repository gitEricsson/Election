package election.controllers;

import election.dtos.requests.VoteRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import election.services.VoteService;

@RestController
@RequestMapping("/api/votes")
@RequiredArgsConstructor
public class VoteController {

    private final VoteService voteService;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String castVote(@RequestBody VoteRequest request) {
        voteService.castVote(request);
        return "Vote cast successfully";
    }
}
