package com.udacity.webcrawler;

import com.udacity.webcrawler.parser.PageParser;
import com.udacity.webcrawler.parser.PageParserFactory;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.RecursiveTask;
import java.util.regex.Pattern;

public class InternalCrawler extends RecursiveTask<Boolean> {
    private String url;
    private Instant deadline;
    private int maxDepth;
    private Map<String, Integer> counts;
    private Set<String> visitedUrls;
    private Clock clock;
    private PageParserFactory parserFactory;

    private List<Pattern> ignoredUrls;

    public InternalCrawler(String url, Instant deadline, int maxDepth, Map<String, Integer> counts, Set<String> visitedUrls, Clock clock, PageParserFactory parserFactory, List<Pattern> ignoredUrls) {
        this.url = url;
        this.deadline = deadline;
        this.maxDepth = maxDepth;
        this.counts = counts;
        this.visitedUrls = visitedUrls;
        this.clock = clock;
        this.parserFactory = parserFactory;
        this.ignoredUrls = ignoredUrls;
    }

    @Override
    protected Boolean compute() {
        if (maxDepth == 0 || clock.instant().isAfter(deadline)) {
            return false;
        }
        for (Pattern pattern : ignoredUrls) {
            if (pattern.matcher(url).matches()) {
                return false;
            }
        }
        if (visitedUrls.contains(url)) {
            return false;
        }
        visitedUrls.add(url);
        PageParser.Result result = parserFactory.get(url).parse();
        for (Map.Entry<String, Integer> e : result.getWordCounts().entrySet()) {
             counts.compute(e.getKey(), (k, v) -> (v == null) ? e.getValue() : e.getValue()+v);
        }
        List<InternalCrawler> subtasks = new ArrayList<>();
        for (String link : result.getLinks()) {
            subtasks.add(new InternalCrawler(link, deadline, maxDepth - 1, counts, visitedUrls, clock, parserFactory, ignoredUrls));
        }
        invokeAll(subtasks);
        return true;
    }
}