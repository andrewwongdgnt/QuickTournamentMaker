package com.dgnt.quickTournamentMaker.test;


import android.support.v4.util.Pair;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    static int lca(int[] match, int[] base, int[] p, int a, int b) {
        boolean[] used = new boolean[match.length];
        while (true) {
            a = base[a];
            used[a] = true;
            if (match[a] == -1) break;
            a = p[match[a]];
        }
        while (true) {
            b = base[b];
            if (used[b]) return b;
            b = p[match[b]];
        }
    }

    static void markPath(int[] match, int[] base, boolean[] blossom, int[] p, int v, int b, int children) {
        for (; base[v] != b; v = p[match[v]]) {
            blossom[base[v]] = blossom[base[match[v]]] = true;
            p[v] = children;
            children = match[v];
        }
    }

    static int findPath(List<Integer>[] graph, int[] match, int[] p, int root) {
        int n = graph.length;
        boolean[] used = new boolean[n];
        Arrays.fill(p, -1);
        int[] base = new int[n];
        for (int i = 0; i < n; ++i)
            base[i] = i;

        used[root] = true;
        int qh = 0;
        int qt = 0;
        int[] q = new int[n];
        q[qt++] = root;
        while (qh < qt) {
            int v = q[qh++];

            for (int to : graph[v]) {
                if (base[v] == base[to] || match[v] == to) continue;
                if (to == root || match[to] != -1 && p[match[to]] != -1) {
                    int curbase = lca(match, base, p, v, to);
                    boolean[] blossom = new boolean[n];
                    markPath(match, base, blossom, p, v, curbase, to);
                    markPath(match, base, blossom, p, to, curbase, v);
                    for (int i = 0; i < n; ++i)
                        if (blossom[base[i]]) {
                            base[i] = curbase;
                            if (!used[i]) {
                                used[i] = true;
                                q[qt++] = i;
                            }
                        }
                } else if (p[to] == -1) {
                    p[to] = v;
                    if (match[to] == -1)
                        return to;
                    to = match[to];
                    used[to] = true;
                    q[qt++] = to;
                }
            }
        }
        return -1;
    }

    public static int maxMatching(List<Integer>[] graph) {
        int n = graph.length;
        int[] match = new int[n];
        Arrays.fill(match, -1);
        int[] p = new int[n];
        for (int i = 0; i < n; ++i) {
            if (match[i] == -1) {
                int v = findPath(graph, match, p, i);
                while (v != -1) {
                    int pv = p[v];
                    int ppv = match[pv];
                    match[v] = pv;
                    match[pv] = v;
                    v = ppv;
                }
            }
        }

        int matches = 0;
        for (int i = 0; i < n; ++i)
            if (match[i] != -1)
                ++matches;
        return matches / 2;
    }
    @Test
    public void addition_isCorrect() throws Exception {
//        int n = 4;
//        List<Integer>[] g = new List[n];
//        for (int i = 0; i < n; i++) {
//            g[i] = new ArrayList<>();
//        }
//        g[0].add(1);
//        g[1].add(0);
//        g[1].add(2);
//        g[2].add(1);
//        g[2].add(3);
//        g[3].add(2);
//        g[0].add(3);
//        g[3].add(0);
//        int maxMatching = maxMatching(g);
//        System.out.println(maxMatching);

//        HashSet<Integer> numbers = new HashSet<>();
//        numbers.add(1);
//        numbers.add(2);
//        numbers.add(3);
//        numbers.add(4);
//        numbers.add(5);
//        numbers.add(6);
//        compute(numbers, new ArrayList<Pair<Integer,Integer>>());

        final Set<String> matchHistory = new HashSet<String>();

        long startTime = System.nanoTime();

        List<String> list = Arrays.asList("1", "2","3","4","5","6","7","8");

        final List<Pair<String,String>> pairList = getProperPairing(list,matchHistory);

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println(TimeUnit.MILLISECONDS.convert(duration, TimeUnit.NANOSECONDS));


        for (final Pair<String, String> pair : pairList) {
            final String participant1 = pair.first;
            final String participant2 = pair.second;

            matchHistory.add(getPairKey(participant1, participant2));
        }



    }
    private static String getPairKey(final String participant1, final String participant2) {
        if (participant1.compareTo(participant2) <= 0)
            return participant1 + participant2;
        else
            return participant2 + participant1;
    }
    private static List<Pair<String, String>> getProperPairing(final List<String> rankedParticipantList, final Set<String> matchUpHistory) {

        if (rankedParticipantList.size() == 2) {
            final String participant1 = rankedParticipantList.get(0);
            final String participant2 = rankedParticipantList.get(1);
            if (!matchUpHistory.contains(getPairKey(participant1, participant2))) {
                final Pair pair = new Pair(participant1, participant2);
                final List<Pair<String, String>> pairList = new ArrayList<>();
                pairList.add(pair);
                return pairList;
            } else {
                return null;
            }
        } else if (rankedParticipantList.size() > 2 && rankedParticipantList.size() % 2 == 0) {

            final String participant1 = rankedParticipantList.get(0);
            for (int i = 1; i<rankedParticipantList.size(); i++){

                final String participant2 = rankedParticipantList.get(i);

                if (!matchUpHistory.contains(getPairKey(participant1, participant2))) {
                    final List<String> subRankedParticipantList = new ArrayList<>();
                    for (int j = 1; j < rankedParticipantList.size(); j++) {
                        if (j != i)
                            subRankedParticipantList.add(rankedParticipantList.get(j));
                    }

                    final List<Pair<String, String>> subProperPairing = getProperPairing(subRankedParticipantList, matchUpHistory);
                    if (subProperPairing != null) {
                        final Pair pair = new Pair(participant1, participant2);

                        subProperPairing.add(0, pair);
                        return subProperPairing;
                    }
                }
            }
            return null;


        } else {
            return null;
        }
    }
    private static void compute(Set<Integer> set,
                                List<List<Integer>> currentResults,
                                List<List<List<Integer>>> results)
    {
//        System.out.println(results);
        if (set.size() < 2)
        {
            results.add(new ArrayList<List<Integer>>(currentResults));
            return;
        }
        List<Integer> list = new ArrayList<Integer>(set);
        Integer first = list.remove(0);
        for (int i=0; i<list.size(); i++)
        {
            Integer second = list.get(i);
            Set<Integer> nextSet = new LinkedHashSet<Integer>(list);
            nextSet.remove(second);

            List<Integer> pair = Arrays.asList(first, second);
            currentResults.add(pair);
            compute(nextSet, currentResults, results);
            currentResults.remove(pair);
        }
    }

//    public void compute(HashSet<Integer> numbers, ArrayList<Pair<Integer,Integer>> pairs) {
//        if (numbers.size() <= 1) {
//            System.out.println(pairs);
//        } else {
//            for (Integer number1 : numbers) {
//                for (Integer number2 : numbers) {
//                    if (number1 != number2) {
//                        HashSet<Integer> possibleNumbers = new HashSet<Integer>(numbers);
//                        ArrayList<Pair<Integer,Integer>> allPairs = new ArrayList<Pair<Integer,Integer>>(pairs);
//
//                        possibleNumbers.remove(number1);
//                        possibleNumbers.remove(number2);
//                        allPairs.add(new ImmutablePair<>(number1, number2));
//
//                        compute(possibleNumbers, allPairs);
//                    }
//                }
//            }
//        }
//    }
//
//    public Set<List<Pair>> compute2(List<Integer> numbers) {
//        if(numbers.size() < 3) {
//            // Base case
//            List<Pair> list = new ArrayList<>();
//            list.add(new Pair(numbers));
//            Set<List<Pair>> result = new HashSet<>();
//            result.add(list);
//            return result;
//        } else {
//            HashSet<ArrayList<Pair>> result = new HashSet<>();
//            // We take each pair that contains the 1st element
//            for(int i = 1; i < numbers.size(); i++) {
//                Pair first = new Pair(numbers.get(0), numbers.get(i));
//                // This is the input for next level of recursion
//                // Our numbers list w/o the current pair
//                List<Integer> nextStep = new ArrayList<>(numbers);
//                nextStep.remove(i);
//                nextStep.remove(0);
//                Set<List<Pair>> intermediate = null;
//                if(nextStep.size() % 2 == 0) {
//                    intermediate = compute(nextStep);
//                } else {
//                    intermediate = compute(numbers).addAll( firstElementSingle(numbers) ),compute( nextStep );
//                }
//                for(List<Pair> list : intermediate ) {
//                    // We add the current pair at the beginning
//                    list.add(0, first);
//                }
//                result.addAll(intermediate);
//            }
//            return result;
//        }
//    }
//
//    private Set<List<Integer>> firstElementSingle(List<Integer> numbers) {
//        Set<List<Integer>> result  = compute(numbers.subList(1,numbers.size()) );
//        for(List<Integer> list : result) {
//            list.add(numbers.get(0));
//        }
//        return result;
//    }

}