package alarmas;

import javafx.animation.Timeline;
import javafx.scene.control.Label;

public class AlarmaItem {
    private Label label;
    private Timeline timeline;

    public AlarmaItem(Label label, Timeline timeline) {
        this.label = label;
        this.timeline = timeline;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public Timeline getTimeline() {
        return timeline;
    }
}