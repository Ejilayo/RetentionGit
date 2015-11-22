package ca.Ranti;

import java.util.EventListener;

public interface DateChangeListener extends EventListener {
	public void dateChanged(DateChangeEvent e);
}
