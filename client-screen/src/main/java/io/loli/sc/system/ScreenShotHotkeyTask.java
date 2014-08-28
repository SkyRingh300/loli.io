package io.loli.sc.system;

import io.loli.sc.ui.swing.SCLauncher;

public class ScreenShotHotkeyTask implements HotKeyTask {
	public void run(int index) {
		switch (index) {
		case 0:
			SCLauncher.launch("select");
			break;
		case 1:
			SCLauncher.launch("full");
			break;
		}
	}
}
