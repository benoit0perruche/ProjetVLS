package visualization;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class GuiHelper {
	public static void showOnFrame(JComponent component, JComponent legend, String frameName) {
		JFrame frame = new JFrame(frameName);
		WindowAdapter wa = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		frame.addWindowListener(wa);
		frame.getContentPane().add(component);
		frame.getContentPane().add(legend, BorderLayout.EAST);
		frame.pack();
		frame.setVisible(true);
	}
}
