package bookstore;

import javax.swing.SwingUtilities;

public class jframe {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new mainframe();
			}
		});
	}

}
