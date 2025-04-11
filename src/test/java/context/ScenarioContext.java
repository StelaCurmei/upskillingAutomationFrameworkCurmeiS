package context;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private static ScenarioContext instance;
    private final Map<ContextKey, String> scenarioContext;

    // Private constructor to prevent instantiation
    private ScenarioContext() {
        scenarioContext = new HashMap<>();
    }

    // Returns the singleton instance
    public static ScenarioContext getInstance() {
        if (instance == null) {
            instance = new ScenarioContext();
        }
        return instance;
    }

    public static void setScenarioContext(ContextKey key, String value) {
        getInstance().scenarioContext.put(key, value);
    }

    public static String getScenarioContext(ContextKey key) {
        return getInstance().scenarioContext.get(key);
    }
}
