package pl.czak.retronix.gwt;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import pl.czak.retronix.models.Direction;

public class Application implements EntryPoint {
  @Override
  public void onModuleLoad() {
    Direction dir = Direction.randomDiagonal();
    RootPanel.get().add(new Label("Random direction: " + dir.toString()));
  }
}
