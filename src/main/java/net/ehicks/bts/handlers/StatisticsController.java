package net.ehicks.bts.handlers;

import net.ehicks.bts.RequestStatsRepository;
import net.ehicks.bts.RequestStats;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticsController
{
    private RequestStatsRepository requestStatsRepository;

    public StatisticsController(RequestStatsRepository requestStatsRepository)
    {
        this.requestStatsRepository = requestStatsRepository;
    }

    @GetMapping("/api/ajaxGetRequestStats/{requestId}")
    public RequestStats getRequestStats(@PathVariable String requestId)
    {
        return requestStatsRepository.findById(requestId);
    }
}
