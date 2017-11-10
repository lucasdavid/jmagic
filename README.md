# JMagic: the Gathering

[![Build Status](https://travis-ci.org/lucasdavid/jmagic.svg?branch=master)](https://travis-ci.org/lucasdavid/jmagic?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/lucasdavid/jmagic/badge.svg?branch=master)](https://coveralls.io/github/lucasdavid/jmagic?branch=master)

A Java implementation for an environment which can play the game
Magic: The Gathering.


## Introduction

This code is licenced under MIT License. Check the attached [LICENSE](LICENSE)
file.

This code was developed using Java 9 SDK. Please refer to the proper
documentation.


## Start Up

You can have a random game by executing the `Runner` class.
Tweak its attributes to personalize your game instance. For instance:

 * Altering `Runner.N_CARDS` from `40` to `80` will result in all players
   starting the game with 80 cards on their decks.
 * Removing `AttachAction.class` entry from the `LooseOnIllegalActionAttempt`
   instantiation in `Runner.OBSERVERS` will prevent all players from attaching
   cards to others.

For more flexible behavior, check the constructors available at
`Game` class.

### Basic Mechanics

A `Game` represents a Game of org.games.jorg.games.jmagic.org.games.jmagic. It contains the current state
of the world (an instance of `State`), the players in the
game and observers (instances of `Observer`), which are
responsible for keeping the game consistent.

At every iteration, the `Game` object will retrieve who's the active `Player`
in the current game state and ask them for an `Action` that can modify its
`State`.

#### Players

Like in a real match, players are required to do ALL the work. That includes
drawing, passing their turn and computing the damage. These actions can be
performed by simply instantiating an object of `Action`
and returning it to the Game object, which will pass it to its observers for
validation and, finally, commit the action.

To play a game using **your very own** player, simply create a class extending
from `Player` and implement the method
`Action YourPlayerClass#act(State state)`. Be aware tough: this game requires
your player to handle ALL actions, including drawing, passing, computing.

##### Basic Players

If you want to make your life a little bit easier when creating your own
player, just extend one of the classes in `org.games.jorg.games.jmagic.org.games.jmagic.players` package and override
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
            return new DeclareBlockersAction(blockers);
        }

        // Don't know how to handle this state. Delegate to `RandomPlayer`.
        return super.act(state);
    }
}
```

##### Complete Player

If you want to handle ALL cases, you can simply extend `Player`.

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
in [org.games.jorg.games.jmagic.org.games.jmagic.players](src/org.games.jorg.games.jmagic.magic/players) package for more insight.


## Docs

This section describes the intertwining of the elements in this code.

### Actions

An `Action` is the entity capable of modifying the match's current `State`.
An action's implementation of the abstract method
`State Action#update(State state)` will determine how the state is altered.

At every iteration of the `Game`, an action is requested from the active
`Player` by calling `Player#act(State state)`. The active `Player` therefore
must implement this contract and act accordingly to the current state of the
game, described by its parameter `State state`.

For more information on how actions work, check out the many examples in
[org.games.jorg.games.jmagic.org.games.jmagic.actions](src/org.games.jorg.games.jmagic.magic/actions).

### Observers

`Player` are not the only entity that can execute actions over a game state.
Twice every iteration (before the active player's action and after it), the
game will deliver the current state to each observer, which in turn will modify
it at will.

Passing a `List` of `Observers` to a `Game` construtor is a way to add
constrains to that game. For example:

```Java
Game game = new Game(players, playersCards, playerActTimeout, List.of(
        new LooseIfDrawingFromEmptyDeck(),
        new LooseOnActTimeout(3000)
));
```

This game will disqualify players that attempt to draw from an empty deck
**and** the ones that failed to return with an Action in less than 3 seconds.

For more information on how observers work, check out the many examples at
[org.games.jorg.games.jmagic.org.games.jmagic.observers](src/org.games.jorg.games.jmagic.magic/observers).

### Validation

Not all actions are valid all the time. For instance, you cannot
draw a card outside your own drawing phase unless you have a spell that
explicitly allows you to do so. That's when `ValidationRule` becomes
interesting: by sub-classing it and defining an implementation for
`ValidationRule#onValidate(State state)`, we can create an rule that checks
whether or not the current state is valid. Furthermore, we can add to error
messages to `ValidationRule#errors` to better inform users why that state is
invalid.

Let's illustrate the concept with an example. Say we create a "rule that
asserts that the game is in a given turn-step". The implementation is quite easy:

```Java
public class TurnsStepIs extends ValidationRule {

    private final TurnSteps step;

    public TurnsStepIs(TurnSteps step) {
        this.step = step;
    }

    @Override
    public void onValidate(State state) {
        if (state.step != this.step) {
            errors.add(String.format("Incorrect turn's step (expected: %s, actual: %s)",
                this.step.name(), state.step.name()));
        }
    }
}
```

We can now use `TurnStepIs` rule when defining our `DrawAction`:

```Java
public final class DrawAction extends Action {

    @Override
    public State update(State state) {
        // Logic to draw card...
    }

    @Override
    public ValidationRule validationRules() {
        return new TurnsStepIs(TurnSteps.DRAW),
    }
}
```

Before applying every action given by a player, the `Game` instance will use
an observer to check if that action is valid
(by simply checking `action.validationRules().validate(state).isValid()`).
If it's not, the player will either automatically pass or be disqualified.

In the example above, `DrawAction` will only be valid during DRAW turn steps!

Finally, you can compose rules using a few connectives in
[org.games.jorg.games.jmagic.org.games.jmagic.infrastructure.validation.basic](src/org.games.jorg.games.jmagic.magic/infrastructure/validation/basic),
such as `And`, `Or` and `Not`:

```Java
import static Connectives.*;

public final class DrawAction extends Action {

    // Rest of DrawAction's code...

    @Override
    public ValidationRule validationRules() {
        return And(
            new HasCardsInTheirDeck(),
            new TurnsStepIs(TurnSteps.DRAW),
            new ActiveAndTurnsPlayersAreTheSame(),
            new HasNotAlreadyDrawnInThisTurn());
    }
}
```

Notice that these connectives are merely sub-classes of `ValidationRule`, and
the static methods in Connectives class are justs aliases to the construction
of a connective object.
