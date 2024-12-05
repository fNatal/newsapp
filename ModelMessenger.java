package models;

public class ModelMessenger {
    private MLModel model;

    public ModelMessenger() {
        this.model = new MLModel();
    }

    public String[] getRecommendations(String preferences) {
        return model.predict(preferences);
    }

    public void updateModel(String behavior) {
        model.learn(behavior);
    }
}
