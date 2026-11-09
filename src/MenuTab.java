import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

import java.awt.image.BufferedImage;
import javax.swing.JPanel;

public class MenuTab {
	//Select
	int menuScenarioIndex;
	int menuCharacterIndex;
	int menuSpriteIndex;

	//Menu
	JTextField[] menuField;
	JLabel[] menuLabel;

	//DropBox
	JComboBox<String> menuSenarioDropBox;
	JComboBox<String> menuItemDropBox;
	JCheckBox[] menuCheckBox;

	//Scroll
	JScrollPane menuCharacterScroll;
	JScrollPane menuSpriteScroll;

	//Sprite
	JLabel menuSprite;

	//Button
	JButton[] menuButton;
	JButton[] paletteButton;

	//Name
	JList<String> menuCharacterList;
	JList<String> menuSpriteList;

	//Unility
	GridBagConstraints breakLine;
	Color colorHeader;

	//Enum
	public enum EnumMenu {
		name, face, life, loyalty, SBN,
		level, HP, MP,
		STR, INT, CMD, DEF,
		resSword, resIce, resFire, resLight, resDark,
		item,
		Soldier, Cavalry, Archer, Samurai, Mage, Monk, Beast, Harpy, Dragon, Zombie,
		troopType, troopAmount,
		skillName0, skillName1, skillName2,
		skillMP0, skillMP1, skillMP2,
		skillRate0, skillRate1, skillRate2,
		skillDmg0, skillDmg1, skillDmg2,
		skillPow0, skillPow1, skillPow2,
		Senario,
		Object, Frame,
	}

	//CheckBox
	public enum EnumCheckBox {
		RandomizeSeed, UnlockAllCharacter, DisableSkullChildren,
	}

	//Button
	public enum EnumButton {
		Clone,
		ObjectRight, ObjectLeft, FrameRight, FrameLeft, Export, ExportAll,
		Import, ImportAll, Palette, Refresh, Test,
	}


	//Initialize
	public MenuTab() {
		//Field
		int count = EnumMenu.values().length;
		menuField = new JTextField[count];
		menuLabel = new JLabel[count];
		for (int i = 0; i < count; i++) {
			menuField[i] = new JTextField(5);
			menuLabel[i] = new JLabel();
			menuLabel[i].setFont(new Font("Courier New", Font.BOLD, 12));
		}

		//Button
		count = EnumButton.values().length;
		menuButton = new JButton[count];
		for (int i = 0; i < count; i++) {
			menuButton[i] = new JButton();
		}

		//Option
		count = EnumCheckBox.values().length;
		menuCheckBox = new JCheckBox[count];
		for (int i = 0; i < count; i++) {
			menuCheckBox[i] = new JCheckBox();
		}

		//Utility
		breakLine = new GridBagConstraints();
		breakLine.gridwidth = GridBagConstraints.REMAINDER;
		colorHeader = new Color(.7f,.7f,.7f);
	}

	public void UpdateMenuList() {
		menuCharacterList.setListData(Main.dataBase.GetCharacterNameList(menuScenarioIndex));
		menuSpriteList.setListData(Main.dataBase.GetSpriteNameList(menuScenarioIndex));
	}

	public void SetPanelEnabled(boolean isEnabled) {
		for (int i = 0; i < menuField.length; i++)
		{
			//Field
			menuField[i].setEnabled(isEnabled);
			//Label
			menuLabel[i].setEnabled(isEnabled);
		}
		//Scenario
		menuSenarioDropBox.setEnabled(isEnabled);
		//Button
		for (int i = 0; i < menuButton.length; i++)
		{
			menuButton[i].setEnabled(isEnabled);
		}
		//Item
		menuItemDropBox.setEnabled(isEnabled);
		//Name
		menuField[EnumMenu.name.ordinal()].setEnabled(false);
	}

	public JPanel CreateCharacterTab() {
		// Create the container panel for this specific tab using BorderLayout
		JPanel tabPanel = new JPanel(new BorderLayout());

		//Names
		menuCharacterList = new JList<>(new String[171]);
		{
			menuCharacterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			menuCharacterScroll = new JScrollPane(menuCharacterList);
			menuCharacterScroll.setPreferredSize(new Dimension(150, 0));

			// Add selection listener to update the text area
			menuCharacterList.addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					if (!e.getValueIsAdjusting()) {
						SetMenuData();
						menuCharacterIndex = menuCharacterList.getSelectedIndex();
						menuSpriteIndex = menuCharacterIndex;
						menuSpriteList.setSelectedIndex(menuCharacterIndex);
						LoadMenuData();
					}
				}
			});

			//Set
			tabPanel.add(menuCharacterScroll, BorderLayout.WEST);
		}

		//Utility
		int sizeH = 20;

		//Contents
		JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		{
			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

			JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
			{
				panelTop.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

				//Senario
				menuLabel[EnumMenu.Senario.ordinal()].setText("Scenario");
				panelTop.add(menuLabel[EnumMenu.Senario.ordinal()]);

				//Dropbox
				String[] options = { "0 Wein", "1 Goldark", "2 Teiris", "3 Mikhal", "4 Reinhart", "5 Leon", "6 Gongos", "7 Junon", };
				menuSenarioDropBox = new JComboBox<>(options);
				menuSenarioDropBox.addActionListener(e -> {
					SetMenuData();
					menuScenarioIndex = menuSenarioDropBox.getSelectedIndex();
					LoadMenuData();
				});
				panelTop.add(menuSenarioDropBox);

				//Clone
				JButton button = menuButton[EnumButton.Clone.ordinal()];
				button.setText("Clone to all Senario");
				button.addActionListener(e -> {
					System.out.println("Button was clicked!");
				});

				panelTop.add(menuButton[EnumButton.Clone.ordinal()]);

				contentPanel.add(panelTop, breakLine);
			}


			JPanel panelBottom = new JPanel(new FlowLayout(FlowLayout.LEFT));
			{
				//Left
				JPanel panelLeft = new JPanel();
				{
					panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));

					//Status
					JPanel status = new JPanel();
					{
						JPanel panel = new JPanel(new GridBagLayout());
						{

							panel.setPreferredSize(new Dimension(280, 9 * sizeH + 5));
							panel.add(CreateLabel("Character", 80, colorHeader), breakLine);
							panel.add(CreateMenuField("Name", EnumMenu.name), breakLine);
							panel.add(CreateMenuField("Face", EnumMenu.face), breakLine);
							panel.add(CreateMenuField("LV", EnumMenu.level), breakLine);
							panel.add(CreateMenuField("HP", EnumMenu.HP), breakLine);
							panel.add(CreateMenuField("MP", EnumMenu.MP), breakLine);
							panel.add(CreateMenuField("Life", EnumMenu.life), breakLine);
							panel.add(CreateMenuField("Loyalty", EnumMenu.loyalty), breakLine);
							status.add(panel, breakLine);
						}
						panelLeft.add(new JScrollPane(status), breakLine);
					}


					//Attribute
					JPanel attribute = new JPanel();
					{
						JPanel panel = new JPanel(new GridBagLayout());
						{
							panel.setPreferredSize(new Dimension(280, 5 * sizeH + 5));
							panel.add(CreateLabel("Stats", 80, colorHeader), breakLine);
							panel.add(CreateMenuField("STR", EnumMenu.STR), breakLine);
							panel.add(CreateMenuField("INT", EnumMenu.INT), breakLine);
							panel.add(CreateMenuField("CMD", EnumMenu.CMD), breakLine);
							panel.add(CreateMenuField("DEF", EnumMenu.DEF), breakLine);
							attribute.add(panel);
						}
						panelLeft.add(new JScrollPane(attribute));
					}

					//Resist
					JPanel resist = new JPanel();
					{
						JPanel panel = new JPanel(new GridBagLayout());
						{
							panel.setPreferredSize(new Dimension(280, 6 * sizeH + 5));
							panel.add(CreateLabel("Resist", 80, colorHeader), breakLine);
							panel.add(CreateMenuField("Sword", EnumMenu.resSword), breakLine);
							panel.add(CreateMenuField("Ice", EnumMenu.resIce), breakLine);
							panel.add(CreateMenuField("Fire", EnumMenu.resFire), breakLine);
							panel.add(CreateMenuField("Light", EnumMenu.resLight), breakLine);
							panel.add(CreateMenuField("Dark", EnumMenu.resDark), breakLine);
							resist.add(panel);
						}
						panelLeft.add(new JScrollPane(resist));
					}

					//Item
					JPanel item = new JPanel();
					{
						JPanel panel = new JPanel(new GridBagLayout());
						{
							panel.setPreferredSize(new Dimension(280, 2 * sizeH + 5));
							panel.add(CreateLabel("Item", 80, colorHeader), breakLine);
							item.add(panel);

							//Dropbox
							String[] options = Array.ItemList;
							menuItemDropBox = new JComboBox<>(options);
							menuItemDropBox.addActionListener(e -> {
								SetMenuData();
								menuSpriteIndex = menuItemDropBox.getSelectedIndex();
								LoadMenuData();
							});
							panel.add(menuItemDropBox);

						}
						panelLeft.add(new JScrollPane(item));
					}

					//Set
					panelBottom.add(panelLeft);
				}

				//Right
				JPanel panelRight = new JPanel();
				{
					panelRight.setLayout(new BoxLayout(panelRight, BoxLayout.Y_AXIS));

					//Troop
					JPanel medal = new JPanel();
					{
						JPanel panel = new JPanel(new GridBagLayout());
						{
							panel.setPreferredSize(new Dimension(180, 11 * sizeH + 5));
							panel.add(CreateLabel("Medals", 80, colorHeader), breakLine);
							panel.add(CreateMenuField("Soldier", EnumMenu.Soldier), breakLine);
							panel.add(CreateMenuField("Cavalry", EnumMenu.Cavalry), breakLine);
							panel.add(CreateMenuField("Archer", EnumMenu.Archer), breakLine);
							panel.add(CreateMenuField("Samurai", EnumMenu.Samurai), breakLine);
							panel.add(CreateMenuField("Mage", EnumMenu.Mage), breakLine);
							panel.add(CreateMenuField("Monk", EnumMenu.Monk), breakLine);
							panel.add(CreateMenuField("Beast", EnumMenu.Beast), breakLine);
							panel.add(CreateMenuField("Harpy", EnumMenu.Harpy), breakLine);
							panel.add(CreateMenuField("Dragon", EnumMenu.Dragon), breakLine);
							panel.add(CreateMenuField("Zombie", EnumMenu.Zombie), breakLine);
							medal.add(panel);
						}
						panelRight.add(new JScrollPane(medal));
					}

					//Troop
					JPanel troop = new JPanel();
					{
						JPanel panel = new JPanel(new GridBagLayout());
						{
							panel.setPreferredSize(new Dimension(250, 3 * sizeH + 5));

							panel.add(CreateLabel("Troop", 80, colorHeader), breakLine);
							panel.add(CreateMenuField("Type", EnumMenu.troopType), breakLine);
							panel.add(CreateMenuField("Amount", EnumMenu.troopAmount), breakLine);

							troop.add(panel);
						}
						panelRight.add(new JScrollPane(troop));
					}


					//Skill
					JPanel skill = new JPanel();
					{
						skill.setPreferredSize(new Dimension(410, 4 * sizeH + 15));

						JPanel panel = new JPanel();
						{
							JPanel skillName = new JPanel(new GridBagLayout());
							{
								skillName.add(CreateHeader("Skills", 130, colorHeader), breakLine);
								skillName.add(CreateLabel(EnumMenu.skillName0, 130), breakLine);
								skillName.add(CreateLabel(EnumMenu.skillName1, 130), breakLine);
								skillName.add(CreateLabel(EnumMenu.skillName2, 130), breakLine);
								panel.add(skillName);
							}

							JPanel skillStat = new JPanel(new GridBagLayout());
							{
								skillStat.add(CreateHeader("Mp", "Damage", "Pow", 60, colorHeader), breakLine);
								skillStat.add(CreateSkillField(EnumMenu.skillMP0, EnumMenu.skillDmg0, EnumMenu.skillPow0), breakLine);
								skillStat.add(CreateSkillField(EnumMenu.skillMP1, EnumMenu.skillDmg1, EnumMenu.skillPow1), breakLine);
								skillStat.add(CreateSkillField(EnumMenu.skillMP2, EnumMenu.skillDmg2, EnumMenu.skillPow2), breakLine);
								panel.add(skillStat);
							}
							skill.add(panel);
						}
						panelRight.add(new JScrollPane(skill));
					}

					//Extra
					JPanel extra = new JPanel();
					{
						JPanel panel = new JPanel(new GridBagLayout());
						{
							panel.setPreferredSize(new Dimension(180, 2 * sizeH + 5));
							panel.add(CreateLabel("Sprite", 80, colorHeader), breakLine);
							panel.add(CreateMenuField("SBN", EnumMenu.SBN), breakLine);
							extra.add(panel);
						}
						panelRight.add(new JScrollPane(extra));
					}

					panelBottom.add(panelRight);
				}

				contentPanel.add(panelBottom, breakLine);
			}
			tabPanel.add(new JScrollPane(contentPanel));
		}

		return tabPanel;
	}

	public JPanel CreateSpriteTab() {
		JPanel tabPanel = new JPanel(new BorderLayout());
		{
			//Names
			menuSpriteList = new JList<>(new String[171]);
			{
				menuSpriteList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				menuSpriteScroll = new JScrollPane(menuSpriteList);
				menuSpriteScroll.setPreferredSize(new Dimension(150, 0));

				// Add selection listener to update the text area
				menuSpriteList.addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						if (!e.getValueIsAdjusting()) {
							SetMenuData();
							menuSpriteIndex = menuSpriteList.getSelectedIndex();
							menuCharacterIndex = menuSpriteIndex;
							menuCharacterList.setSelectedIndex(menuSpriteIndex);
							LoadMenuData();
						}
					}
				});

				menuCharacterList.setSelectionMode(menuSpriteList.getSelectionMode());

				//Set
				tabPanel.add(menuSpriteScroll, BorderLayout.WEST);
			}



			//Sprite Panel
			JPanel panel = new JPanel(new GridBagLayout());
			{
				//Object
				JPanel objectPanel = new JPanel();
				{
					//Left
					JButton buttonL = menuButton[EnumButton.ObjectLeft.ordinal()];
					buttonL.setText("<");
					buttonL.addActionListener(e -> {
						Main.dataBase.sbn[menuSpriteIndex].SetObjectIndex(-1);
						UpdateSpriteMenu();
					});
					objectPanel.add(buttonL);

					//Label
					JLabel label = menuLabel[EnumMenu.Object.ordinal()];
					label.setText( "Object " + "0" + "/" + "0");
					label.setPreferredSize(new Dimension(100, 24));
					label.setHorizontalAlignment(SwingConstants.CENTER);
					objectPanel.add(new JScrollPane(label));

					//Right
					JButton buttonR = menuButton[EnumButton.ObjectRight.ordinal()];
					buttonR.setText(">");
					buttonR.addActionListener(e -> {
						Main.dataBase.sbn[menuSpriteIndex].SetObjectIndex(1);
						UpdateSpriteMenu();
					});
					objectPanel.add(buttonR);

					panel.add(objectPanel, breakLine);
				}

				//Frame
				JPanel framePanel = new JPanel();
				{
					//Left
					JButton buttonL = menuButton[EnumButton.FrameLeft.ordinal()];
					buttonL.setText("<");
					buttonL.addActionListener(e -> {
						Main.dataBase.sbn[menuSpriteIndex].SetFrameIndex(-1);
						UpdateSpriteMenu();
					});
					framePanel.add(buttonL);

					//Lablel
					JLabel label = menuLabel[EnumMenu.Frame.ordinal()];
					label.setText( "Frame " + "0" + "/" + "0");
					label.setPreferredSize(new Dimension(100, 24));
					label.setHorizontalAlignment(SwingConstants.CENTER);
					framePanel.add(new JScrollPane(label));

					//Right
					JButton buttonR = menuButton[EnumButton.FrameRight.ordinal()];
					buttonR.setText(">");
					buttonR.addActionListener(e -> {
						Main.dataBase.sbn[menuSpriteIndex].SetFrameIndex(1);
						UpdateSpriteMenu();
					});
					framePanel.add(buttonR);

					panel.add(framePanel, breakLine);
				}

				//Palette View
				JPanel paletteViewPanel = new JPanel();
				{
					// List of palette colors for the swatches
					paletteButton = new JButton[16];

					for (int i = 0; i < 16; i++) {
						paletteButton[i] = new JButton();
						JButton button = paletteButton[i];
						button.setBackground(Color.BLACK);
						button.setPreferredSize(new Dimension(12, 12));
						button.setOpaque(true);
						button.setBorderPainted(false);

						// Change drawing color when clicked
						button.addActionListener(e -> {});
						paletteViewPanel.add(button);
					}
					panel.add( new JScrollPane(paletteViewPanel), breakLine);
				}



				//Sprite
				JPanel spritePanel = new JPanel();
				{
					BufferedImage menuImage = new BufferedImage(255, 255, BufferedImage.TYPE_INT_ARGB);
					ImageIcon icon = new ImageIcon(menuImage);
					menuSprite = new JLabel(icon);
					menuSprite.setPreferredSize(new Dimension(255, 255));
					spritePanel.add(menuSprite);

					panel.add(new JScrollPane(spritePanel), breakLine);
				}

				//Export
				JPanel exportPanel = new JPanel();
				{
					JButton button = menuButton[EnumButton.Export.ordinal()];
					button.setText("Export Frame");
					button.setPreferredSize(new Dimension(150, 24));
					button.addActionListener(e -> {
						Main.dataBase.sbn[menuSpriteIndex].ExportFrame();
					});
					exportPanel.add(button);

					panel.add(exportPanel);
				}

				//ExportAll
				JPanel exportAllPanel = new JPanel();
				{
					JButton button = menuButton[EnumButton.ExportAll.ordinal()];
					button.setText("Export All Frames");
					button.setPreferredSize(new Dimension(150, 24));
					button.addActionListener(e -> {
						Main.dataBase.sbn[menuSpriteIndex].ExportAllFrame();
					});
					exportAllPanel.add(button);

					panel.add(exportAllPanel, breakLine);
				}

				//Import
				JPanel importPanel = new JPanel();
				{
					JButton button = menuButton[EnumButton.Import.ordinal()];
					button.setText("Import Frame");
					button.setPreferredSize(new Dimension(150, 24));
					button.addActionListener(e -> {
						Main.dataBase.sbn[menuSpriteIndex].ImportFrame();
						UpdateSpriteMenu();
					});
					importPanel.add(button);

					panel.add(importPanel);
				}

				//ImportAll
				JPanel importAllPanel = new JPanel();
				{
					JButton button = menuButton[EnumButton.ImportAll.ordinal()];
					button.setText("Import All Frames");
					button.setPreferredSize(new Dimension(150, 24));
					button.addActionListener(e -> {
						Main.dataBase.sbn[menuSpriteIndex].ImportAllFrame();
						UpdateSpriteMenu();
					});
					importAllPanel.add(button);

					panel.add(importAllPanel, breakLine);
				}

				//Palette
				JPanel palettePanel = new JPanel();
				{
					JButton button = menuButton[EnumButton.Palette.ordinal()];
					button.setText("Import with Palette");
					button.setPreferredSize(new Dimension(150, 24));
					button.addActionListener(e -> {
						Main.dataBase.sbn[menuSpriteIndex].ImportWithPalette();
						UpdateSpriteMenu();
					});
					palettePanel.add(button);

					panel.add(palettePanel, breakLine);
				}

				//Refresh
				JPanel refreshPanel = new JPanel();
				{
					JButton button = menuButton[EnumButton.Refresh.ordinal()];
					button.setText("Refresh");
					button.setPreferredSize(new Dimension(150, 24));
					button.addActionListener(e -> {
						Main.dataBase.sbn[menuSpriteIndex].Refresh();
						UpdateSpriteMenu();
					});
					refreshPanel.add(button);

					panel.add(refreshPanel, breakLine);
				}

			}
			tabPanel.add( new JScrollPane(panel));
		}
		return tabPanel;
	}

	public JPanel CreateGameplayTab() {
		JPanel tabPanel = new JPanel();
		{
			JPanel panel = new JPanel(new GridBagLayout());
			{
				//Randomize Seed
				menuCheckBox[EnumCheckBox.RandomizeSeed.ordinal()].setText("Correctly Randomize level up stats, including MP and INT.");
				panel.add(menuCheckBox[EnumCheckBox.RandomizeSeed.ordinal()], breakLine);

				//UnlockAllCharcter
				menuCheckBox[EnumCheckBox.UnlockAllCharacter.ordinal()].setText("Unlock all characters at start of new game.");
				panel.add(menuCheckBox[EnumCheckBox.UnlockAllCharacter.ordinal()], breakLine);

				//DisableSkullChildren
				menuCheckBox[EnumCheckBox.DisableSkullChildren.ordinal()].setText("Disable the Skull Children completely.");
				panel.add(menuCheckBox[EnumCheckBox.DisableSkullChildren.ordinal()], breakLine);
			}
			tabPanel.add(panel, breakLine);
		}
		return tabPanel;
	}

	JLabel CreateLabel(EnumMenu enumMenu, int width) {
		JLabel label = menuLabel[enumMenu.ordinal()];
		label.setText(enumMenu.toString());
		label.setPreferredSize(new Dimension(width, 20));
		return label;
	}

	JPanel CreateLabel(String text, int width, Color color) {
		JPanel panel = new JPanel(new GridLayout(1, 0, 10, 0));
		{
			JLabel label = new JLabel(text, SwingConstants.CENTER);
			label.setPreferredSize(new Dimension(width, 15));
			//label.
			label.setFont(new Font("Courier New", Font.BOLD, 12));
			label.setForeground(color);
			panel.add(label);
		}
		return panel;
	}

	JPanel CreateHeader(String text0, int width, Color color) {
		JPanel panel = new JPanel(new GridLayout(1, 0, 10, 0));
		{
			panel.add(CreateLabel(text0, width, color));
		}
		return panel;
	}

	JPanel CreateHeader(String text0, String text1, String text2, int width ,Color color) {
		JPanel panel = new JPanel(new GridLayout(1, 0, 10, 0));
		{
			panel.add(CreateLabel(text0, width, color));
			panel.add(CreateLabel(text1, width, color));
			panel.add(CreateLabel(text2, width, color));
		}
		return panel;
	}

	JPanel CreateMenuField(EnumMenu enumMenu) {
		JPanel panel = new JPanel(new GridLayout(1, 0, 10, 0));
		{
			int index = enumMenu.ordinal();
			//Add Field
			panel.add(menuField[index]);
		}
		return panel;
	}

	JPanel CreateMenuField(String text, EnumMenu enumMenu) {
		JPanel panel = new JPanel(new GridLayout(1, 0, 10, 0));
		{
			//Add Label
			JLabel label = menuLabel[enumMenu.ordinal()];
			label.setText(text);
			panel.add(label);

			//Add Field
			panel.add(menuField[enumMenu.ordinal()]);
		}
		return panel;
	}

	JPanel CreateSkillField(EnumMenu mp, EnumMenu dmg, EnumMenu pow ){
		JPanel panel = new JPanel(new GridLayout(1, 0, 10, 0));
		{
			panel.add(menuField[mp.ordinal()]);
			panel.add(menuField[dmg.ordinal()]);
			panel.add(menuField[pow.ordinal()]);
		}
		return panel;
	}

	public void LoadMenuData() {
		DataBase dataBase = Main.dataBase;
		if (!dataBase.isFileOpen) {return;}

		int s = menuScenarioIndex;
		int c = menuCharacterIndex;

		menuField[EnumMenu.name.ordinal()].setText(dataBase.GetCharacterName(s, c));
		menuField[EnumMenu.face.ordinal()].setText("" + dataBase.scenario[s].face[c]);
		menuField[EnumMenu.level.ordinal()].setText("" + (dataBase.scenario[s].level[c] + 1));
		menuField[EnumMenu.HP.ordinal()].setText("" + dataBase.scenario[s].HP[c]);
		menuField[EnumMenu.MP.ordinal()].setText("" + dataBase.scenario[s].MP[c]);

		menuField[EnumMenu.life.ordinal()].setText("" + dataBase.scenario[s].life[c]);
		menuField[EnumMenu.loyalty.ordinal()].setText("" + dataBase.scenario[s].loyalty[c]);

		menuField[EnumMenu.STR.ordinal()].setText("" + dataBase.scenario[s].STR[c]);
		menuField[EnumMenu.INT.ordinal()].setText("" + dataBase.scenario[s].INT[c]);
		menuField[EnumMenu.CMD.ordinal()].setText("" + dataBase.scenario[s].CMD[c]);
		menuField[EnumMenu.DEF.ordinal()].setText("" + dataBase.scenario[s].DEF[c]);

		menuField[EnumMenu.resSword.ordinal()].setText("" + dataBase.scenario[s].resSword[c]);
		menuField[EnumMenu.resIce.ordinal()].setText("" + dataBase.scenario[s].resIce[c]);
		menuField[EnumMenu.resFire.ordinal()].setText("" + dataBase.scenario[s].resFire[c]);
		menuField[EnumMenu.resLight.ordinal()].setText("" + dataBase.scenario[s].resLight[c]);
		menuField[EnumMenu.resDark.ordinal()].setText("" + dataBase.scenario[s].resDark[c]);

		menuLabel[EnumMenu.item.ordinal()].setText(dataBase.GetItemName(s, c));
		menuField[EnumMenu.item.ordinal()].setText("" + dataBase.scenario[s].item[c]);

		menuField[EnumMenu.Soldier.ordinal()].setText("" + dataBase.scenario[s].GetMedal(c, 0));
		menuField[EnumMenu.Cavalry.ordinal()].setText("" + dataBase.scenario[s].GetMedal(c, 1));
		menuField[EnumMenu.Mage.ordinal()].setText("" + dataBase.scenario[s].GetMedal(c, 2));
		menuField[EnumMenu.Samurai.ordinal()].setText("" + dataBase.scenario[s].GetMedal(c, 3));
		menuField[EnumMenu.Archer.ordinal()].setText("" + dataBase.scenario[s].GetMedal(c, 4));
		menuField[EnumMenu.Monk.ordinal()].setText("" + dataBase.scenario[s].GetMedal(c, 5));
		menuField[EnumMenu.Harpy.ordinal()].setText("" + dataBase.scenario[s].GetMedal(c, 6));
		menuField[EnumMenu.Beast.ordinal()].setText("" + dataBase.scenario[s].GetMedal(c, 7));
		menuField[EnumMenu.Dragon.ordinal()].setText("" + dataBase.scenario[s].GetMedal(c, 8));
		menuField[EnumMenu.Zombie.ordinal()].setText("" + dataBase.scenario[s].GetMedal(c, 9));

		menuField[EnumMenu.troopType.ordinal()].setText("" + dataBase.scenario[s].troopType[c]);
		menuField[EnumMenu.troopAmount.ordinal()].setText("" + dataBase.scenario[s].troopAmount[c]);

		//Skills
		menuLabel[EnumMenu.skillName0.ordinal()].setText(dataBase.GetSkillName(s, c, 0));
		menuLabel[EnumMenu.skillName1.ordinal()].setText(dataBase.GetSkillName(s, c, 1));
		menuLabel[EnumMenu.skillName2.ordinal()].setText(dataBase.GetSkillName(s, c, 2));

		menuField[EnumMenu.skillMP0.ordinal()].setText("" + dataBase.scenario[s].skillMP0[c]);
		menuField[EnumMenu.skillMP1.ordinal()].setText("" + dataBase.scenario[s].skillMP1[c]);
		menuField[EnumMenu.skillMP2.ordinal()].setText("" + dataBase.scenario[s].skillMP2[c]);

		menuField[EnumMenu.skillRate0.ordinal()].setText("" + dataBase.scenario[s].skillRate0[c]);
		menuField[EnumMenu.skillRate1.ordinal()].setText("" + dataBase.scenario[s].skillRate1[c]);
		menuField[EnumMenu.skillRate2.ordinal()].setText("" + dataBase.scenario[s].skillRate2[c]);

		menuField[EnumMenu.skillDmg0.ordinal()].setText("" + dataBase.scenario[s].skillDmg0[c]);
		menuField[EnumMenu.skillDmg1.ordinal()].setText("" + dataBase.scenario[s].skillDmg1[c]);
		menuField[EnumMenu.skillDmg2.ordinal()].setText("" + dataBase.scenario[s].skillDmg2[c]);

		menuField[EnumMenu.skillPow0.ordinal()].setText("" + dataBase.scenario[s].skillPow0[c]);
		menuField[EnumMenu.skillPow1.ordinal()].setText("" + dataBase.scenario[s].skillPow1[c]);
		menuField[EnumMenu.skillPow2.ordinal()].setText("" + dataBase.scenario[s].skillPow2[c]);

		menuField[EnumMenu.SBN.ordinal()].setText("" + dataBase.scenario[s].SBN[c]);

		//Options
		menuCheckBox[EnumCheckBox.RandomizeSeed.ordinal()].setSelected(dataBase.isRandomSeedCorrect);
		menuCheckBox[EnumCheckBox.UnlockAllCharacter.ordinal()].setSelected(dataBase.isUnlockAllCharacter);
		menuCheckBox[EnumCheckBox.DisableSkullChildren.ordinal()].setSelected(dataBase.isDisableSkullChildren);

		//Sprite
		UpdateSpriteMenu();
	}

	public void SetMenuData() {
		DataBase dataBase = Main.dataBase;
		if (!dataBase.isFileOpen) {return;}

		int s = menuScenarioIndex;
		int c = menuCharacterIndex;

		dataBase.scenario[s].face[c] = Integer.parseInt(menuField[EnumMenu.face.ordinal()].getText());
		dataBase.scenario[s].level[c] = Clamp(Integer.parseInt(menuField[EnumMenu.level.ordinal()].getText()) - 1, 0, 49);
		dataBase.scenario[s].HP[c] = Clamp(Integer.parseInt(menuField[EnumMenu.HP.ordinal()].getText()), 0, 255);
		dataBase.scenario[s].MP[c] = Clamp(Integer.parseInt(menuField[EnumMenu.MP.ordinal()].getText()), 0, 255);

		dataBase.scenario[s].life[c] = Integer.parseInt(menuField[EnumMenu.life.ordinal()].getText());
		dataBase.scenario[s].loyalty[c] = Integer.parseInt(menuField[EnumMenu.loyalty.ordinal()].getText());

		dataBase.scenario[s].STR[c] = Integer.parseInt(menuField[EnumMenu.STR.ordinal()].getText());
		dataBase.scenario[s].INT[c] = Integer.parseInt(menuField[EnumMenu.INT.ordinal()].getText());
		dataBase.scenario[s].CMD[c] = Integer.parseInt(menuField[EnumMenu.CMD.ordinal()].getText());
		dataBase.scenario[s].DEF[c] = Integer.parseInt(menuField[EnumMenu.DEF.ordinal()].getText());

		dataBase.scenario[s].resSword[c] = Integer.parseInt(menuField[EnumMenu.resSword.ordinal()].getText());
		dataBase.scenario[s].resIce[c] = Integer.parseInt(menuField[EnumMenu.resIce.ordinal()].getText());
		dataBase.scenario[s].resFire[c] = Integer.parseInt(menuField[EnumMenu.resFire.ordinal()].getText());
		dataBase.scenario[s].resLight[c] = Integer.parseInt(menuField[EnumMenu.resLight.ordinal()].getText());
		dataBase.scenario[s].resDark[c] = Integer.parseInt(menuField[EnumMenu.resDark.ordinal()].getText());

		dataBase.scenario[s].item[c] = Integer.parseInt(menuField[EnumMenu.item.ordinal()].getText());

		int Soldier = Integer.parseInt(menuField[EnumMenu.Soldier.ordinal()].getText());
		int Cavalry = Integer.parseInt(menuField[EnumMenu.Cavalry.ordinal()].getText());
		int Mage = Integer.parseInt(menuField[EnumMenu.Mage.ordinal()].getText());
		int Samurai = Integer.parseInt(menuField[EnumMenu.Samurai.ordinal()].getText());
		int Archer = Integer.parseInt(menuField[EnumMenu.Archer.ordinal()].getText());
		int Monk = Integer.parseInt(menuField[EnumMenu.Monk.ordinal()].getText());
		int Harpy = Integer.parseInt(menuField[EnumMenu.Harpy.ordinal()].getText());
		int Beast = Integer.parseInt(menuField[EnumMenu.Beast.ordinal()].getText());
		int Dragon = Integer.parseInt(menuField[EnumMenu.Dragon.ordinal()].getText());
		int Zombie = Integer.parseInt(menuField[EnumMenu.Zombie.ordinal()].getText());

		int level = dataBase.scenario[s].level[c];
		dataBase.scenario[s].EXP[c] = dataBase.expTable[level];

		dataBase.scenario[s].medal0[c] = SetMedal(Soldier, Cavalry);
		dataBase.scenario[s].medal1[c] = SetMedal(Mage, Samurai);
		dataBase.scenario[s].medal2[c] = SetMedal(Archer, Monk);
		dataBase.scenario[s].medal3[c] = SetMedal(Harpy, Beast);
		dataBase.scenario[s].medal4[c] = SetMedal(Dragon, Zombie);

		dataBase.scenario[s].troopType[c] = Integer.parseInt(menuField[EnumMenu.troopType.ordinal()].getText());
		dataBase.scenario[s].troopAmount[c] = Integer.parseInt(menuField[EnumMenu.troopAmount.ordinal()].getText());

		dataBase.scenario[s].skillMP0[c] = Integer.parseInt(menuField[EnumMenu.skillMP0.ordinal()].getText());
		dataBase.scenario[s].skillMP1[c] = Integer.parseInt(menuField[EnumMenu.skillMP1.ordinal()].getText());
		dataBase.scenario[s].skillMP2[c] = Integer.parseInt(menuField[EnumMenu.skillMP2.ordinal()].getText());

		dataBase.scenario[s].skillRate0[c] = Integer.parseInt(menuField[EnumMenu.skillRate0.ordinal()].getText());
		dataBase.scenario[s].skillRate1[c] = Integer.parseInt(menuField[EnumMenu.skillRate1.ordinal()].getText());
		dataBase.scenario[s].skillRate2[c] = Integer.parseInt(menuField[EnumMenu.skillRate2.ordinal()].getText());

		dataBase.scenario[s].skillDmg0[c] = Integer.parseInt(menuField[EnumMenu.skillDmg0.ordinal()].getText());
		dataBase.scenario[s].skillDmg1[c] = Integer.parseInt(menuField[EnumMenu.skillDmg1.ordinal()].getText());
		dataBase.scenario[s].skillDmg2[c] = Integer.parseInt(menuField[EnumMenu.skillDmg2.ordinal()].getText());

		dataBase.scenario[s].skillPow0[c] = Integer.parseInt(menuField[EnumMenu.skillPow0.ordinal()].getText());
		dataBase.scenario[s].skillPow1[c] = Integer.parseInt(menuField[EnumMenu.skillPow1.ordinal()].getText());
		dataBase.scenario[s].skillPow2[c] = Integer.parseInt(menuField[EnumMenu.skillPow2.ordinal()].getText());

		dataBase.scenario[s].SBN[c] = Integer.parseInt(menuField[EnumMenu.SBN.ordinal()].getText());

		//Options
		dataBase.isRandomSeedCorrect = menuCheckBox[EnumCheckBox.RandomizeSeed.ordinal()].isSelected();
		dataBase.isUnlockAllCharacter = menuCheckBox[EnumCheckBox.UnlockAllCharacter.ordinal()].isSelected();
		dataBase.isDisableSkullChildren = menuCheckBox[EnumCheckBox.DisableSkullChildren.ordinal()].isSelected();
	}

	public void UpdateSpriteMenu() {
		DataBase dataBase = Main.dataBase;
		if (!dataBase.isFileOpen) {return;}

		int s = menuSpriteIndex;

		if (dataBase.sbn[s] == null) {
			dataBase.LoadSprite(s);
		}

		//Image
		ImageIcon icon = new ImageIcon(dataBase.GetSpriteImage(s));
		menuSprite.setIcon(icon);

		//Palette
		Color[] color = dataBase.sbn[s].GetPaletteColor();
		for (int i = 0; i < color.length; i++) {
			paletteButton[i].setBackground(color[i]);
		}

		//Object Index
		menuLabel[EnumMenu.Object.ordinal()].setText(dataBase.sbn[s].GetObjectText());

		//Frame Index
		menuLabel[EnumMenu.Frame.ordinal()].setText(dataBase.sbn[s].GetFrameText());
	}

	int Clamp(int i, int min, int max) {
		if (i < min) {return min;}
		else if (i > max) {return max;}
		return i;
	}

	public int SetMedal(int ihigh, int ilow ) {

		byte high = (byte) ihigh;
		byte low = (byte) ilow;

		if (high == 0) {high = 15;}
		if (low == 0) {low = 15;}

		//Value
		return ((high & 0xFF) << 8) | (low & 0xFF);
	}

}
