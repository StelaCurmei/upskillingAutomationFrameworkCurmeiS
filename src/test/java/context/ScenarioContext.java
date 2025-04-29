package context;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private static ScenarioContext instance;
    private final Map<ContextKey, Object> scenarioContext; // Changed from String to Object

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

    public static void setScenarioContext(ContextKey key, Object value) {
        getInstance().scenarioContext.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getScenarioContext(ContextKey key) {
        return (T) getInstance().scenarioContext.get(key);
    }
}
