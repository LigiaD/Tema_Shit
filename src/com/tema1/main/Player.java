package com.tema1.main;

import com.tema1.goods.GoodsFactory;

import java.util.*;
import java.util.Collections;
import java.util.Map;


public class Player {
    private String strategy;
    private boolean isSheriff;
    private Card cards = new Card();
    private Card buffer = new Card();
    private int playerId;
    private int pocket;
    private int assetType;

    public Player() {
        strategy = null;
        isSheriff = false;
        pocket = 80;
    }



    public Player(String strategy, Map<Integer, Integer> playerCards, int playerId) {
        this.strategy = strategy;
        this.cards = new Card(playerCards);
        this.buffer = new Card(playerCards);
        isSheriff = false;
        pocket = 80;
        this.playerId = playerId;
    }

    public Player(String strategy, int id) {
        this.strategy = strategy;
        this.playerId = id;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public boolean isSheriff() {
        return isSheriff;
    }

    public final boolean getSheriff() {
        return this.isSheriff;
    }


    public void setSheriff(boolean sheriff) {
        isSheriff = sheriff;
    }

    public void setPlayerCards(List<Integer> gameCards) {
        for (int card : gameCards) {
            if (cards.bagSize() == 10)
                break;
            this.cards.getCards().put(card, Collections.frequency(gameCards.subList(0, 10), card));
            buffer.getCards().putAll(cards.getCards());
        }

        System.out.println(cards.getCards());
        gameCards.subList(0, 10).clear();

    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public void clearBag() {
        cards.getCards().clear();
        buffer.getCards().clear();
    }


    public void basicSetBag() {
        if(this.cards.countIllegals() == cards.getCards().size()) {
            cards.getMostProfit();
            assetType = 0;
        }
        if(this.cards.countIllegals() > 0 || this.cards.countIllegals() == 0) {
            cards.removeIllegals();
            cards.mostFrequent();
            cards.getMostProfit();
            assetType = cards.getId();
        }
    }

    public void setBag(int round) {


        if (strategy.equals("basic")) {
            basicSetBag();
            return;
        }
        if (strategy.equals("greedy")) {
//            cards.setBuffer(cards.getCards());
            if(round % 2 == 0) {
                basicSetBag();
//                System.out.println("SHIT");
//                System.out.println(buffer.isAllIllegals());
                if(buffer.countIllegals() > 0){

                    if(cards.bagSize() < 8) {
                        cards.addIllegal(strategy);
                    }
                    return ;
                }

            }
            basicSetBag();
            return;
        }
        if (strategy.equals("bribed")) {
            if(pocket <= 5){
                basicSetBag();
                return ;
            }
            if(cards.countIllegals() == 0) {
                basicSetBag();
                return ;
            }
            if(cards.countIllegals() == 10){
                //TODO while bagSize <= 8 && Penalty < pocket
                cards.addIllegal(strategy);
                assetType = 0;

                return ;
            }

            if(cards.countIllegals() < 10) {
                cards.addIllegal(strategy);
                if (cards.bagSize() < 8) {
                    cards.getMostProfit();
                }
                assetType = 0;
            }


        }

    }
    public boolean isLying(){
        for (Map.Entry<Integer, Integer> card : cards.getCards().entrySet()){
            if(card.getKey() == assetType)
                return false;
//            if(card.getKey() != assetType)
//                return true;
        }
        return true;
    }
    public int getPlayerPenalty(){
        int penalty = 0;
        if(isLying()){
            for (Map.Entry<Integer, Integer> card : cards.getCards().entrySet()) {
                if (card.getKey() != assetType)
                    penalty += card.getValue()
                            * GoodsFactory.getInstance().getGoodsById(card.getKey()).getPenalty();
            }

        }
        if(!(isLying())){
            for (Map.Entry<Integer, Integer> card : cards.getCards().entrySet()) {
                penalty += card.getValue()
                        * GoodsFactory.getInstance().getGoodsById(card.getKey()).getPenalty();

            }
        }

        return penalty;
    }

    public void addToPocket(int coins){
        pocket += coins;
    }
    public List<Integer> confiscCards() {
        List<Integer> confiscatedCards = new ArrayList<Integer>();
        for (Map.Entry<Integer, Integer> card : cards.getCards().entrySet()){
            if(card.getKey() != assetType)
               for(int i = 0; i < card.getValue(); i++){
                   confiscatedCards.add(card.getKey());
               }
        }
        return confiscatedCards;
    }

    public void addKingQueenBonus(){



    }

    public Card getCards() {
        return cards;
    }

    public int getPocket() {
        return pocket;
    }



    @Override
    public String toString() {
        String cards = this.cards.getCards().toString();
        return "" + strategy + "\t:"+ playerId + "\t:" + cards;

    }
}