# JMagic: the Gathering

A Java implementation for an environment which can play the game
Magic: The Gathering.

## Introduction

This code was developed using Java 9 SDK. Please refer to the proper
documentation.

## Start Up

You can have a random game by executing the `magic.Runner` class.
Tweak its attributes to personalize your game instance. For instance:

 * Altering `Runner.N_CARDS` from `40` to `80` will result in all players
   starting the game with 80 cards on their decks.
 * Removing `AttachAction.class` entry from the `LooseOnIllegalActionAttempt`
   instantiation in `Runner.OBSERVERS` will prevent all players from attaching
   cards to others.
   
For more flexible behavior, check the constructors available at
`magic.core.Game` class.

### Basic Mechanics

A `magic.core.Game` represents a Game of magic. It contains the current state
of the world (an instance of `magic.core.states.State`), the players in the
game and observers (instances of `magic.core.observers.Observer`), which are
responsible for keeping the game consistent.

At every iteration, the `Game` object will retrieve who's the active `Player`
in the current game state and ask them for an `Action` that can modify its
`State`.

#### Players

Like in a real match, players are required to do ALL the work. That includes
drawing, passing their turn and computing the damage. These actions can be
performed by simply instantiating an object of `magic.core.actions.Action`
and returning it to the Game object, which will pass it to its observers for
validation and, finally, commit the action.

To play a game using **your very own** player, simply create a class extending
from `magic.core.Player` and implement the method
`Action YourPlayerClass#act(State state)`. Be aware tough: this game requires
your player to handle ALL actions, including drawing, passing, computing.

##### Basic Players

If you want to make your life a little bit easier when creating your own
player, just extend one of the classes in `magic.players` package and override
the `Player#act(State state)` method. With that, handle the game states you
want and delegate the rest to the superior player. E.x.:

```Java
class MySimplePlayer extends RandomPlayer {
    @Override
    public Action act(State state) {
        if (state.step == TurnSteps.DECLARE_BLOCKERS
        && !(state.actionThatLedToThisState instanceof DeclareBlockersAction)) {
            Map<Creature, Creature> blockers;
            // Some fancy computation to declare blockers s.t. the number of
            // creatures killed is minimized...
            return new magic.core.actions.DeclareBlockersAction(blockers);
        }

        // Don't know how to handle this state. Delegate to `RandomPlayer`.
        return super.act(state);
    }
}
```

##### Complete Player

If you want to handle ALL cases, you can simply extend `magic.core.Player`.

Suppose `State current` is the current state of the game, `MyPlayer dylan` is
the current turn's player and it's his drawing phase. The Game will ask
`dylan` for an `Action` by calling the `dylan.act(current)` method.

An implementation that would correctly draw a card from dylan's deck is this:
```Java
class MyCompletePlayer extends Player {
    @Override
    public Action act(State state) {
        if (state.turnsPlayerState().player.equals(this)
            && state.step == TurnSteps.DRAW
            && new HasNotAlreadyDrawnInThisTurn().isValid(state)) {
            return new DrawAction();
        }
        
        // Don't know what to do. Advance game.
        return AdvanceGameAction();
    }
}
```

*Explanation:* although we know for a fact that IT IS Dylan's drawing phase,
`Player#act(State state)` is called in many other stages of the game. Dylan
must, therefore, check if:
 
 1. He is the active and turn's player (the player that has the upkeep)
 2. This is the drawing phase 
 3. He didn't draw already.
 
If all of these conditions are true, he will return an instance of
`DrawAction`, which will be read by the Game object and finally modify
the game state. Otherwise, Dylan won't know what to do and will simply
request the game to advance with `AdvanceGameAction`.

**Note:** although this implementation correctly draws from the deck,
it does not cover many other important actions. Read the implementations
in `magic.players` package for more insight.
