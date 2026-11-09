import javax.swing.*;
import java.io.*;
import java.awt.*;

public class Main extends JFrame {
	//Database
	static DataBase dataBase = new DataBase();
	MenuTab menuTab = new MenuTab();

	//Tab
	JTabbedPane tabbedPane;

	//Tab Panels
	JPanel characterTab;
	JPanel spriteTab;
	JPanel gameplayTab;

	//File
	File fileSaturnRom;

	//MenuItem
	JMenuItem[] menuItem;
	enum EnumMenuItem { OpenRom, SaveRom, OpenPatch, SavePatch,}



	public Main() {
		//JFrame
		setTitle("DFHack");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1024, 768);
		setLocationRelativeTo(null);

		//MainMenu
		CreateMainMenu();

		//Tab
		tabbedPane = new JTabbedPane();
		{
			characterTab = menuTab.CreateCharacterTab();
			spriteTab = menuTab.CreateSpriteTab();
			gameplayTab =  menuTab.CreateGameplayTab();

			//Add
			tabbedPane.addTab("Character", characterTab);
			tabbedPane.addTab("Sprite", spriteTab);
			tabbedPane.addTab("Gameplay", gameplayTab);
		}

		//Disable Tab
		SetTabbedPaneEnabled(false);

		setLayout(new BorderLayout());
		add(tabbedPane);

		//Make the frame visible
		setVisible(true);
	}

	public void SetTabbedPaneEnabled(boolean isEnabled) {

		tabbedPane.setEnabledAt(0, isEnabled);
		tabbedPane.setEnabledAt(1, isEnabled);
		tabbedPane.setEnabledAt(2, isEnabled);
		menuTab.SetPanelEnabled(isEnabled);
	}

	void CreateMainMenu() {
		//Menu bar
		JMenuBar menuBar = new JMenuBar();
		{
			//Initialize
			menuItem = new JMenuItem[EnumMenuItem.values().length];
			for (int i = 0; i < menuItem.length; i++ ) {
				menuItem[i]	= new JMenuItem();
			}

			//Saturn Rom
			JMenu patchMenu = new JMenu("Saturn ROM");
			{
				//Open
				JMenuItem menu = menuItem[EnumMenuItem.OpenRom.ordinal()];
				menu.setText("Open Saturn ROM");
				menu.addActionListener(e -> OpenRom());
				menu.setEnabled(true);
				patchMenu.add(menu);

				//Save
				menu = menuItem[EnumMenuItem.SaveRom.ordinal()];
				menu.setText("Save Saturn ROM");
				menu.addActionListener(e -> SaveRom());
				menu.setEnabled(false);
				patchMenu.add(menu);

				//Set
				menuBar.add(patchMenu);
			}

			//Patch
			JMenu fileMenu = new JMenu("Patch");
			{
				//Open
				JMenuItem menu = menuItem[EnumMenuItem.OpenPatch.ordinal()];
				menu.setText("Open Patch File");
				menu.addActionListener(e -> OpenPatch());
				menu.setEnabled(false);
				fileMenu.add(menu);

				//Save
				menu = menuItem[EnumMenuItem.SavePatch.ordinal()];
				menu.setText("Save Patch File");
				menu.addActionListener(e -> SavePatch());
				menu.setEnabled(false);
				fileMenu.add(menu);

				//Set
				menuBar.add(fileMenu);
			}

			//Set
			setJMenuBar(menuBar);
		}
	}


	private void OpenRom() {
		//Open
		FileDialog fileDialog = new FileDialog(this, "Open Saturn ROM", FileDialog.LOAD);
		fileDialog.setFile("*.bin;*.iso");
		fileDialog.setVisible(true);

		//File
		fileSaturnRom = new File(fileDialog.getDirectory(), fileDialog.getFile());

		boolean isFileOpen = dataBase.OpenFile(fileSaturnRom);

		//Database
		if (!isFileOpen) {
			JOptionPane.showMessageDialog(
					this,
					"File is not a Dragon Force ROM",
					"Error",
					JOptionPane.INFORMATION_MESSAGE
			);
		}

		if (!isFileOpen) {return;}

		//MenuName
		menuTab.UpdateMenuList();

		//Data
		menuTab.LoadMenuData();

		//Enable
		SetTabbedPaneEnabled(true);
		menuItem[EnumMenuItem.SaveRom.ordinal()].setEnabled(true);
		menuItem[EnumMenuItem.OpenPatch.ordinal()].setEnabled(true);
		menuItem[EnumMenuItem.SavePatch.ordinal()].setEnabled(true);
	}

	private void SaveRom() {
		//Update data first
		menuTab.SetMenuData();

		//Save data after
		dataBase.SaveFile(fileSaturnRom);

		//Database
		JOptionPane.showMessageDialog(
				this,
				"Changes has been saved.",
				"Saved",
				JOptionPane.INFORMATION_MESSAGE
		);
	}

	private void OpenPatch() {
		FileDialog fileDialog = new FileDialog(this, "Open Patch", FileDialog.LOAD);
		fileDialog.setFile("*.patch");
		fileDialog.setVisible(true);
	}

	private void SavePatch() {
		FileDialog fileDialog = new FileDialog(this, "Save Patch", FileDialog.SAVE);
		fileDialog.setFile("*.patch");
		fileDialog.setVisible(true);
	}




	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new Main().setVisible(true));
	}

}
