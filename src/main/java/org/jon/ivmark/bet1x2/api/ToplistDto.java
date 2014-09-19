package org.jon.ivmark.bet1x2.api;

import java.util.List;

public class ToplistDto {

    public Metadata metadata;
    public List<Entry> entries;

    public static class Metadata {
        public List<String> rounds;
    }

    public static class Entry {
        public String position;
        public String username;
        public List<Result> rounds;
        public Result total;

    }

    public static class Result {
        public int correct;
    }
}
