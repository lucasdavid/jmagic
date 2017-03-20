package hearthstone.core.actions;

import java.util.HashMap;

/**
 *
 * @author ldavid
 */
@Deprecated
public class ActionFactory {

    private static ActionFactory factoryInstance;
    private final HashMap<String, Action> actions;

    private ActionFactory() {
        actions = new HashMap<>();

        actions.put("DrawAction", new DrawAction());
        actions.put("PassAction", new PassAction());
    }

    private static ActionFactory factory() {
        if (factoryInstance == null) {
            factoryInstance = new ActionFactory();
        }

        return factoryInstance;
    }

    public static Action get(String actionName) {
        return factory().actions.get(actionName);
    }
}
