package com.tema1.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class Game {
    private int rounds;
    private List<Player> players = new ArrayList<Player>();
    private List<Integer> AllCards;

    public Game() {
        rounds = -1;
        players = null;
        AllCards = null;
    }

    public Game(int rounds, List<String> Strategy, List<Integer> cards) {
        this.rounds = rounds;
        this.AllCards = cards;
        for (int i = 0; i < Strategy.size(); i++) {
            players.add( new Player(Strategy.get(i),i) );
        }
//        System.out.println(players.toString());
    }

    public int getRounds() {
        return rounds;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Integer> getAllCards() {
        return AllCards;
    }
    private void clearPlayersBag() {
        for(Player player : this.players){
            player.clearBag();
        }
    }
    private void verify(Player player, Player sheriff){
        int penalty = 0;
        if(player.isLying()){
            penalty = player.getPlayerPenalty();
            player.addToPocket(-penalty);
            sheriff.addToPocket(penalty);
            AllCards.addAll(player.confiscCards());
        }
        if(!(player.isLying())){
            penalty = player.getPlayerPenalty();
            player.addToPocket(penalty);
            sheriff.addToPocket(-penalty);
        }
    }

    private void inspectAll(Player sheriff){

        if(sheriff.getStrategy().equals("greedy")) {

            for (Player player : players) {
                if (player.isSheriff())
                    continue;

                if (player.getStrategy().equals("bribed")) {
                    if ((player.getCards().countIllegals() == 1) ||
                            (player.getCards().countIllegals() == 2)) {
                        sheriff.addToPocket(5);
                        player.addToPocket(-5);
                        continue ;
                    }
                    if (player.getCards().countIllegals() > 2) {
                        sheriff.addToPocket(10);
                        player.addToPocket(-10);

                    }

                } else {
                    verify(player, sheriff);
                }
            }
        }

        if(sheriff.getStrategy().equals("basic")){
                for (Player player : players) {
                    if (player.isSheriff())
                        continue;
                    verify(player,sheriff);
                }
        }

        if(sheriff.getStrategy().equals("bribed")){
            int index = sheriff.getPlayerId();
            if(index == 0){
                verify(players.get(players.size()), sheriff);
                verify(players.get(index + 1), sheriff);
                return ;
            }
            if(index == players.size()){
                verify(players.get(index - 1), sheriff);
                verify(players.get(0), sheriff);
                return ;
            }
            if(index > 0){
                verify(players.get(index - 1), sheriff);
                verify(players.get(index + 1), sheriff);
            }

        }
    }

    public void Inspection(){
        Player sheriff = new Player();
        for (Player player : this.players) {
            if (player.isSheriff())
                sheriff = player;
        }
        inspectAll(sheriff);
    }

    public final void startGame() {
        for (int round = 1; round <= this.rounds; ++round) {
            for (int subRound = 0; subRound < this.players.size(); ++subRound) {
                this.players.get(subRound).setSheriff(true);
                for (Player player : this.players) {
                    if (player.getSheriff())
                        continue;
//                    System.out.println("Runda : " + round + "\t subrunda : "+ subRound);
                    player.setPlayerCards(this.AllCards);
                    player.setBag(round);
//                    System.out.println(player.toString());
                    System.out.println(player.getCards());

                }
                this.players.get(subRound).setSheriff(false);
                clearPlayersBag();
            }
        }
    }
}