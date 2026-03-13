import java.util.*;

public class HashTableSystemDesignProblems {



    static class TokenBucket {

        int tokens;
        int maxTokens;
        long lastRefill;

        TokenBucket(int maxTokens) {
            this.tokens = maxTokens;
            this.maxTokens = maxTokens;
            this.lastRefill = System.currentTimeMillis();
        }

        synchronized boolean allowRequest() {

            long now = System.currentTimeMillis();

            if (now - lastRefill > 3600000) {
                tokens = maxTokens;
                lastRefill = now;
            }

            if (tokens > 0) {
                tokens--;
                return true;
            }

            return false;
        }
    }

    static class RateLimiter {

        HashMap<String, TokenBucket> clients = new HashMap<>();

        boolean check(String clientId) {

            clients.putIfAbsent(clientId,
                    new TokenBucket(1000));

            return clients.get(clientId)
                    .allowRequest();
        }
    }



    static class Autocomplete {

        HashMap<String, Integer> frequency = new HashMap<>();

        void addQuery(String query) {
            frequency.put(query,
                    frequency.getOrDefault(query,0)+1);
        }

        List<String> search(String prefix) {

            PriorityQueue<String> pq =
                    new PriorityQueue<>(
                            (a,b)->frequency.get(b)-frequency.get(a)
                    );

            for(String q:frequency.keySet()){

                if(q.startsWith(prefix)){
                    pq.add(q);
                }
            }

            List<String> res=new ArrayList<>();

            for(int i=0;i<10 && !pq.isEmpty();i++){
                res.add(pq.poll());
            }

            return res;
        }
    }


    static class ParkingLot {

        String[] table;
        int size;

        ParkingLot(int size){
            this.size=size;
            table=new String[size];
        }

        int hash(String plate){
            return Math.abs(plate.hashCode())%size;
        }

        int parkVehicle(String plate){

            int index=hash(plate);

            while(table[index]!=null){
                index=(index+1)%size;
            }

            table[index]=plate;

            return index;
        }
    }



    static class Transaction{

        int id;
        int amount;

        Transaction(int id,int amount){
            this.id=id;
            this.amount=amount;
        }
    }

    static class FraudDetector{

        List<int[]> twoSum(List<Transaction> txns,int target){

            HashMap<Integer,Transaction> map=new HashMap<>();

            List<int[]> result=new ArrayList<>();

            for(Transaction t:txns){

                int comp=target-t.amount;

                if(map.containsKey(comp)){
                    result.add(new int[]{
                            map.get(comp).id,t.id
                    });
                }

                map.put(t.amount,t);
            }

            return result;
        }
    }



    static class MultiLevelCache{

        LinkedHashMap<String,String> L1=
                new LinkedHashMap<>(10000,0.75f,true);

        HashMap<String,String> L2=new HashMap<>();

        String getVideo(String id){

            if(L1.containsKey(id)){
                return "L1 HIT";
            }

            if(L2.containsKey(id)){

                L1.put(id,L2.get(id));

                return "L2 HIT";
            }

            String data="VideoData";

            L2.put(id,data);

            return "DB HIT";
        }
    }


    public static void main(String[] args) {

        RateLimiter limiter=new RateLimiter();

        System.out.println(
                limiter.check("client1")
        );

        Autocomplete ac=new Autocomplete();

        ac.addQuery("java tutorial");
        ac.addQuery("javascript");

        System.out.println(ac.search("jav"));
    }
}