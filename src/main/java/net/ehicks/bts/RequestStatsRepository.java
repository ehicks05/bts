package net.ehicks.bts;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestStatsRepository
{
    private HashOperations hashOperations;
    private RedisTemplate<String, RequestStats> redisTemplate;

    public RequestStatsRepository(RedisTemplate<String, RequestStats> redisTemplate)
    {
        this.redisTemplate = redisTemplate;
        this.hashOperations = this.redisTemplate.opsForHash();
    }

    public void save(RequestStats requestStats)
    {
        hashOperations.put("REQUEST_STATS", requestStats.getRequestId(), requestStats);

        Long size = hashOperations.size("REQUEST_STATS");
        int remove = size.intValue() - 100;
        if (remove > 0)
        {
            List<RequestStats> requestStatses = findAll().stream()
                    .sorted(Comparator.comparing(RequestStats::getRequestStart))
                    .collect(Collectors.toList())
                    .subList(0, remove);
            requestStatses.forEach(requestStats1 -> hashOperations.delete("REQUEST_STATS", requestStats1.getRequestId()));
        }
    }

    public List<RequestStats> findAll()
    {
        return hashOperations.values("REQUEST_STATS");
    }

    public RequestStats findById(String requestId)
    {
        return (RequestStats) hashOperations.get("REQUEST_STATS", requestId);
    }

    public void update(RequestStats requestStats)
    {
        save(requestStats);
    }

    public void delete(String requestId)
    {
        hashOperations.delete("REQUEST_STATS", requestId);
    }
}