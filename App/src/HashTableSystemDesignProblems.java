import java.util.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.PriorityQueue;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Random;

public class HashTableSystemDesignProblems {

    /*
    =========================
    PROBLEM 1
    Username Availability
    =========================
    */

    static class UsernameChecker {

        HashMap<String, Integer> users = new HashMap<>();
        HashMap<String, Integer> attempts = new HashMap<>();

        boolean checkAvailability(String username) {
            attempts.put(username, attempts.getOrDefault(username, 0) + 1);
            return !users.containsKey(username);
        }

        List<String> suggestAlternatives(String username) {

            List<String> suggestions = new ArrayList<>();

            for (int i = 1; i <= 3; i++) {
                suggestions.add(username + i);
            }

            suggestions.add(username.replace("_", "."));

            return suggestions;
        }

        String getMostAttempted() {

            String result = "";
            int max = 0;

            for (String user : attempts.keySet()) {
                if (attempts.get(user) > max) {
                    max = attempts.get(user);
                    result = user;
                }
            }

            return result;
        }
    }


    /*
    =========================
    PROBLEM 2
    Flash Sale Inventory
    =========================
    */

    static class InventoryManager {

        HashMap<String, Integer> stock = new HashMap<>();
        HashMap<String, Queue<Integer>> waiting = new HashMap<>();

        void addProduct(String productId, int count) {
            stock.put(productId, count);
            waiting.put(productId, new LinkedList<>());
        }

        synchronized String purchaseItem(String productId, int userId) {

            int current = stock.getOrDefault(productId, 0);

            if (current > 0) {

                stock.put(productId, current - 1);

                return "Success, remaining: " + (current - 1);

            } else {

                waiting.get(productId).add(userId);

                return "Added to waiting list position "
                        + waiting.get(productId).size();
            }
        }

        int checkStock(String productId) {
            return stock.getOrDefault(productId, 0);
        }
    }


    /*
    =========================
    PROBLEM 3
    DNS Cache with TTL
    =========================
    */

    static class DNSCache {

        static class Entry {
            String ip;
            long expiry;

            Entry(String ip, long ttl) {
                this.ip = ip;
                this.expiry = System.currentTimeMillis() + ttl;
            }
        }

        HashMap<String, Entry> cache = new HashMap<>();

        String resolve(String domain) {

            if (cache.containsKey(domain)) {

                Entry entry = cache.get(domain);

                if (System.currentTimeMillis() < entry.expiry) {
                    return "Cache HIT -> " + entry.ip;
                }

                cache.remove(domain);
            }

            String ip = queryUpstream(domain);

            cache.put(domain, new Entry(ip, 5000));

            return "Cache MISS -> " + ip;
        }

        String queryUpstream(String domain) {
            return "172.217.14." + new Random().nextInt(200);
        }
    }


    /*
    =========================
    PROBLEM 4
    Plagiarism Detector
    =========================
    */

    static class PlagiarismDetector {

        HashMap<String, Set<String>> index = new HashMap<>();

        void addDocument(String docId, String text) {

            String[] words = text.split(" ");

            for (int i = 0; i < words.length - 4; i++) {

                String gram = words[i] + " " +
                        words[i+1] + " " +
                        words[i+2] + " " +
                        words[i+3] + " " +
                        words[i+4];

                index.putIfAbsent(gram, new HashSet<>());

                index.get(gram).add(docId);
            }
        }

        Map<String, Integer> analyze(String text) {

            Map<String, Integer> result = new HashMap<>();

            String[] words = text.split(" ");

            for (int i = 0; i < words.length - 4; i++) {

                String gram = words[i] + " " +
                        words[i+1] + " " +
                        words[i+2] + " " +
                        words[i+3] + " " +
                        words[i+4];

                if (index.containsKey(gram)) {

                    for (String doc : index.get(gram)) {

                        result.put(doc,
                                result.getOrDefault(doc, 0) + 1);
                    }
                }
            }

            return result;
        }
    }


    /*
    =========================
    PROBLEM 5
    Real Time Analytics
    =========================
    */

    static class Analytics {

        HashMap<String, Integer> pageViews = new HashMap<>();
        HashMap<String, Set<String>> uniqueVisitors = new HashMap<>();
        HashMap<String, Integer> sources = new HashMap<>();

        void processEvent(String url, String userId, String source) {

            pageViews.put(url,
                    pageViews.getOrDefault(url, 0) + 1);

            uniqueVisitors.putIfAbsent(url, new HashSet<>());
            uniqueVisitors.get(url).add(userId);

            sources.put(source,
                    sources.getOrDefault(source, 0) + 1);
        }

        void getTopPages() {

            pageViews.entrySet()
                    .stream()
                    .sorted((a,b)->b.getValue()-a.getValue())
                    .limit(10)
                    .forEach(System.out::println);
        }
    }


    public static void main(String[] args) {

        UsernameChecker checker = new UsernameChecker();

        System.out.println(
                checker.checkAvailability("john_doe")
        );

        InventoryManager manager = new InventoryManager();

        manager.addProduct("IPHONE15", 2);

        System.out.println(
                manager.purchaseItem("IPHONE15", 101)
        );
    }
}