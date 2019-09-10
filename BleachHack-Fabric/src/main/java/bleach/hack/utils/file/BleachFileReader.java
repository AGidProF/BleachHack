package bleach.hack.utils.file;

import java.util.List;

import bleach.hack.gui.clickgui.ModuleWindow;
import bleach.hack.gui.clickgui.SettingBase;
import bleach.hack.gui.clickgui.SettingMode;
import bleach.hack.gui.clickgui.SettingSlider;
import bleach.hack.gui.clickgui.SettingToggle;
import bleach.hack.module.Module;
import bleach.hack.module.ModuleManager;
import bleach.hack.module.mods.ClickGui;

public class BleachFileReader {

	private static BleachFileMang fileMang = new BleachFileMang();
	
	public static void saveModules() {
		fileMang.createEmptyFile("modules.txt");
		
		String lines = "";
		for(Module m: ModuleManager.getModules()) {
			if(m.getName() == "ClickGui" || m.getName() == "Freecam") continue;
			lines += m.getName() + ":" + m.isToggled() + "\n";
		}
		
		fileMang.appendFile(lines, "modules.txt");
	}
	
	public static void readModules() {
		List<String> lines = fileMang.readFileLines("modules.txt");
		
		for(Module m: ModuleManager.getModules()) {
			for(String s: lines) {
				String[] line = s.split(":");
				try {
					if(line[0].contains(m.getName()) && line[1].contains("true")) {
						m.toggle();
						break;
					}
				}catch(Exception e) {}
			}
		}
	}
	
	public static void saveSettings() {
		fileMang.createEmptyFile("settings.txt");
		
		String lines = "";
		for(Module m: ModuleManager.getModules()) {
			String line = m.getName();
			int count = 0;
			
			for(SettingBase set: m.getSettings()) {
				if(set instanceof SettingSlider) line += ":" + m.getSettings().get(count).toSlider().getValue();
				if(set instanceof SettingMode) line += ":" + m.getSettings().get(count).toMode().mode;
				if(set instanceof SettingToggle) line += ":" + m.getSettings().get(count).toToggle().state;
				count++;
			}
			lines += line + "\n";
		}
		
		fileMang.appendFile(lines, "settings.txt");
	}
	
	public static void readSettings() {
		List<String> lines = fileMang.readFileLines("settings.txt");
		
		for(Module m: ModuleManager.getModules()) {
			for(String s: lines) {
				String[] line = s.split(":");
				if(!line[0].startsWith(m.getName())) continue;
				int count = 0;
				
				for(SettingBase set: m.getSettings()) {
					try {
						if(set instanceof SettingSlider) {
							m.getSettings().get(count).toSlider().value = Double.parseDouble(line[count+1]);}
						if(set instanceof SettingMode) {
							m.getSettings().get(count).toMode().mode = Integer.parseInt(line[count+1]);}
						if(set instanceof SettingToggle) {
							m.getSettings().get(count).toToggle().state = Boolean.parseBoolean(line[count+1]);}
					}catch(Exception e) {}
					count++;
				}
			}
		}
	}
	
	public static void saveBinds() {
		fileMang.createEmptyFile("binds.txt");
		
		String lines = "";
		for(Module m: ModuleManager.getModules()) {
			lines += m.getName() + ":" + m.getKey() + "\n";
		}
		
		fileMang.appendFile(lines, "binds.txt");
	}
	
	public static void readBinds() {
		List<String> lines = fileMang.readFileLines("binds.txt");
		
		for(Module m: ModuleManager.getModules()) {
			for(String s: lines) {
				String[] line = s.split(":");
				if(!line[0].startsWith(m.getName())) continue;
				try { m.setKey(Integer.parseInt(line[line.length - 1])); }catch(Exception e) {}
			}
		}
	}
	
	public static void saveClickGui() {
		fileMang.createEmptyFile("clickgui.txt");
		
		String text = "";
		for(ModuleWindow w: ClickGui.clickGui.tabs) text += w.getPos()[0] + ":" + w.getPos()[1] + "\n";
		
		fileMang.appendFile(text, "clickgui.txt");
	}
	
	public static void readClickGui() {
		List<String> lines = fileMang.readFileLines("clickgui.txt");
		
		try {
			int c = 0;
			for(ModuleWindow w: ClickGui.clickGui.tabs) {
				w.setPos(Integer.parseInt(lines.get(c).split(":")[0]), Integer.parseInt(lines.get(c).split(":")[1]));
				c++;
			}
		}catch(Exception e) {}
	}
}
