package services;

class ElectionServiceTest {

    @Test
    void shouldCreateElectionSuccessfully() {}

    @Test
    void shouldFailToCreateElectionWithInvalidDates() {}

    @Test
    void shouldStartElectionSuccessfully() {}

    @Test
    void shouldFailToStartElectionIfAlreadyStarted() {}

    @Test
    void shouldFailToStartElectionIfEndDatePassed() {}

    @Test
    void shouldEndElectionSuccessfully() {}

    @Test
    void shouldFailToEndElectionIfNotStarted() {}

    @Test
    void shouldFailToEndElectionIfAlreadyEnded() {}

    @Test
    void shouldReturnCorrectElectionResults() {}

    @Test
    void shouldReturnEmptyResultsIfNoVotesCast() {}
}