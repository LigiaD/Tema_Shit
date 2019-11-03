package com.tema1.main;

import com.tema1.goods.GoodsFactory;
import com.tema1.goods.GoodsType;

import java.util.*;

import static java.util.stream.Collectors.toMap;

public class Card {
    private Map<Integer, Integer> cards;
    private Map<Integer, Integer> buffer;
    private int id;
//    private Integer assetType;

    Card() {
        cards = new HashMap<>();
        buffer = new HashMap<>();
//        assetType = new Integer();
    }

    Card(Map<Integer, Integer> allCards) {
        cards = new HashMap<>(allCards);
    }

    public void setCards(Map<Integer, Integer> cards) {
        this.cards = cards;
    }

    public Map<Integer, Integer> getCards() {
        return cards;
    }

    public int getId() {
        Map.Entry<Integer, Integer> map = cards.entrySet().iterator().next();
       return map.getKey();

    }

    public void setId(int id) {
        this.id = id;
    }

    public int countIllegals() {
        int i = 0;
        for(Map.Entry<Integer, Integer> val : cards.entrySet()) {
            if (GoodsFactory.getInstance().getGoodsById(val.getKey()).getType().equals(GoodsType.Illegal))
                i++;
        }
//        System.out.println(i);
        return i;
    }

    public void removeIllegals() {
        for(Iterator<Map.Entry<Integer, Integer>> it = cards.entrySet().iterator(); it.hasNext();) {
            Map.Entry<Integer, Integer> val = it.next();
            if (GoodsFactory.getInstance().getGoodsById(val.getKey()).getType().equals(GoodsType.Illegal)) {
                buffer.put(val.getKey(),val.getValue());
                it.remove();
            }
        }
    }

    public void setBuffer(Map<Integer, Integer> buffer) {
        this.buffer = buffer;
    }

    public Map<Integer, Integer> getBuffer() {
        return buffer;
    }

    public void getMostProfit() {
        Map<Integer, Integer> ret = new HashMap<>();
        int profit = 0;
        int id = 0;
        int value = 0;
        for (Map.Entry<Integer, Integer> val : cards.entrySet()) {
            if (GoodsFactory.getInstance().getGoodsById(val.getKey()).getProfit() > profit) {
                id = val.getKey();
                value = val.getValue();
                profit = GoodsFactory.getInstance().getGoodsById(val.getKey()).getProfit();
            }
            if (GoodsFactory.getInstance().getGoodsById(val.getKey()).getProfit() == profit
                    && id < val.getKey()) {
                id = val.getKey();
                value = val.getValue();
                profit = GoodsFactory.getInstance().getGoodsById(val.getKey()).getProfit();
            }
        }
        ret.put(id, value);
//        setAssetType(id);
        cards = ret;
//        System.out.println(cards);
//        System.out.println(setAssetType(id));
    }

    public void mostFrequent(){
        int max_count = 0;
        Map<Integer, Integer> res = new HashMap<>();
        Map<Integer, Integer> sortedByFrequency = this.cards
                .entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(
                        toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                LinkedHashMap::new));

        for(Map.Entry<Integer, Integer> val : sortedByFrequency.entrySet())
        {
            if (max_count <= val.getValue())
            {
                res.put(val.getKey(), val.getValue());
                max_count = val.getValue();
            }
        }

        cards = res;
    }

    public void addIllegal(String strategy) {
        int profit = 0;
        int id = 0;
        int value = 0;

        for (Map.Entry<Integer, Integer> val : buffer.entrySet()) {
            if (GoodsFactory.getInstance().getGoodsById(val.getKey()).getType().equals(GoodsType.Illegal)){
                if (GoodsFactory.getInstance().getGoodsById(val.getKey()).getProfit() > profit) {
                    profit = GoodsFactory.getInstance().getGoodsById(val.getKey()).getProfit();
                    id = val.getKey();
                    value = val.getValue();
                }
            }
        }
        if(strategy.equals("greedy")) {
            cards.put(id, 1);
        }
        if(strategy.equals("bribed")) {
            cards.put(id, value);
        }

    }

    public int bagSize(){
        int size = 0;
        for(Map.Entry<Integer, Integer> card : this.cards.entrySet()) {
            size += card.getValue();
        }
        return size;
    }
}