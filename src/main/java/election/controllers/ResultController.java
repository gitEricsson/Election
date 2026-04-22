package election.controllers;

import election.data.models.Option;
import election.dtos.responses.ElectionResultResponse;
import election.exceptions.ElectionOngoingException;
import election.exceptions.ElectionNotFoundException;
import election.exceptions.TieException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import election.services.ResultService;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/results")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService resultService;

    @GetMapping("/{electionId}")
    public ElectionResultResponse getElectionResults(@PathVariable("electionId") String electionId) {
        return resultService.getElectionResults(electionId);
    }

    @GetMapping("/{electionId}/winner")
    public Option getWinner(@PathVariable("electionId") String electionId) {
        return resultService.getWinner(electionId);
    }

    @ExceptionHandler(ElectionOngoingException.class)
    public ResponseEntity<Map<String, String>> handleElectionOngoing(ElectionOngoingException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(ElectionNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleElectionNotFound(ElectionNotFoundException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TieException.class)
    public ResponseEntity<Map<String, String>> handleTie(TieException e) {
        Map<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }
}