package mapManagementFloorAndMode;

/**
 * Created by MZ on 4/26/17.
 */
import javafx.scene.control.TextField;

public class PersistentPromptTextField extends TextField {
    PersistentPromptTextField(String text, String prompt) {
        super(text);
        setPromptText(prompt);
        getStyleClass().add("persistent-prompt");
        refreshPromptVisibility();

        textProperty().addListener(observable -> refreshPromptVisibility());
    }

    private void refreshPromptVisibility() {
        final String text = getText();
        if (isEmptyString(text)) {
            getStyleClass().remove("no-prompt");
        } else {
            if (!getStyleClass().contains("no-prompt")) {
                getStyleClass().add("no-prompt");
            }
        }
    }

    private boolean isEmptyString(String text) {
        return text == null || text.isEmpty();
    }
}