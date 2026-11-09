import java.awt.image.BufferedImage;
import java.io.*;

public class DataBase {
	public Scenario[] scenario = new Scenario[8];
	public SBN[] sbn = new SBN[171];

	//Options
	public boolean isRandomSeedCorrect;
	public boolean isUnlockAllCharacter;
	public boolean isDisableSkullChildren;

	//File
	FileStream fileStream;
	public boolean isFileOpen;

	//Pointers
	boolean isLoadPointers;
	int[] itemNamePointers;
	int[] characterNamePointers;
	int[] skillNamePointers;
	int[] expTable;

	//Offsets
	long[] ReadyPointer = new long[8];
	long[] ReadyString = new long[8];


	enum EnumFile {
		f0,
		AREA_MAP,
		BODY001_GRP,
		BODY002_GRP,
		BODY003_GRP,
		BODY004_GRP,
		BODY005_GRP,
		BODY006_GRP,
		BODY007_GRP,
		BODY008_GRP,
		BODY009_GRP,
		BODY010_GRP,
		BODY011_GRP,
		BODY012_GRP,
		BODY013_GRP,
		BODY014_GRP,
		BODY015_GRP,
		BODY016_GRP,
		BODY017_GRP,
		BODY018_GRP,
		BODY019_GRP,
		BODY020_GRP,
		BODY021_GRP,
		BODY022_GRP,
		BODY023_GRP,
		BODY024_GRP,
		BODY025_GRP,
		BODY026_GRP,
		BODY027_GRP,
		BODY028_GRP,
		BODY029_GRP,
		BODY030_GRP,
		BODY031_GRP,
		BODY032_GRP,
		BODY033_GRP,
		BODY034_GRP,
		BODY035_GRP,
		BODY036_GRP,
		BODY037_GRP,
		BODY038_GRP,
		BODY039_GRP,
		BODY040_GRP,
		BODY041_GRP,
		BODY042_GRP,
		BODY043_GRP,
		BODY044_GRP,
		BODY045_GRP,
		BODY046_GRP,
		BODY047_GRP,
		BODY048_GRP,
		BODY049_GRP,
		BODY050_GRP,
		BODY051_GRP,
		BODY052_GRP,
		BODY053_GRP,
		BODY054_GRP,
		BODY055_GRP,
		BODY056_GRP,
		BODY057_GRP,
		BODY058_GRP,
		BODY059_GRP,
		BODY060_GRP,
		BODY061_GRP,
		BODY062_GRP,
		BODY063_GRP,
		BODY064_GRP,
		BODY065_GRP,
		BODY066_GRP,
		BODY067_GRP,
		BODY068_GRP,
		BODY069_GRP,
		BODY070_GRP,
		BODY071_GRP,
		BODY072_GRP,
		BODY073_GRP,
		BODY074_GRP,
		BODY075_GRP,
		BODY076_GRP,
		BODY077_GRP,
		BODY078_GRP,
		BODY079_GRP,
		BODY080_GRP,
		BODY081_GRP,
		BODY082_GRP,
		BODY083_GRP,
		BODY084_GRP,
		BODY085_GRP,
		BODY086_GRP,
		BODY087_GRP,
		BODY088_GRP,
		BODY089_GRP,
		BODY090_GRP,
		BODY091_GRP,
		BODY092_GRP,
		BODY093_GRP,
		BODY094_GRP,
		BODY095_GRP,
		BODY096_GRP,
		BODY097_GRP,
		BODY098_GRP,
		BODY099_GRP,
		BODY100_GRP,
		BODY101_GRP,
		BODY102_GRP,
		BODY103_GRP,
		BODY104_GRP,
		BODY105_GRP,
		BODY106_GRP,
		BODY107_GRP,
		BODY108_GRP,
		BODY109_GRP,
		BODY110_GRP,
		BODY111_GRP,
		BODY112_GRP,
		BODY113_GRP,
		BODY114_GRP,
		BODY115_GRP,
		BODY116_GRP,
		BODY117_GRP,
		BODY118_GRP,
		BODY119_GRP,
		BODY120_GRP,
		BODY121_GRP,
		BODY122_GRP,
		BODY123_GRP,
		BODY124_GRP,
		BODY125_GRP,
		BODY126_GRP,
		BODY128_GRP,
		BODY129_GRP,
		BODY130_GRP,
		BODY131_GRP,
		BODY132_GRP,
		BODY133_GRP,
		BODY134_GRP,
		BODY135_GRP,
		BODY136_GRP,
		BODY137_GRP,
		BODY138_GRP,
		BODY139_GRP,
		BODY140_GRP,
		BODY142_GRP,
		BODY143_GRP,
		BODY144_GRP,
		BODY145_GRP,
		BODY146_GRP,
		BODY147_GRP,
		BODY148_GRP,
		BODY149_GRP,
		BODY150_GRP,
		BODY151_GRP,
		BODY152_GRP,
		BODY153_GRP,
		BODY154_GRP,
		BODY155_GRP,
		BODY156_GRP,
		BODY157_GRP,
		BODY158_GRP,
		BODY159_GRP,
		BODY160_GRP,
		BODY161_GRP,
		BODY162_GRP,
		BODY163_GRP,
		BODY164_GRP,
		BODY165_GRP,
		BODY166_GRP,
		BODY167_GRP,
		BODY168_GRP,
		BODY169_GRP,
		BODY170_GRP,
		BODY171_GRP,
		BODY172_GRP,
		BODY173_GRP,
		BODY174_GRP,
		BODY175_GRP,
		BODY176_GRP,
		BODY177_GRP,
		BODY178_GRP,
		BODY179_GRP,
		CDDA1,
		BOSS_CPK,
		ENDING_CPK,
		KATO_CPK,
		MAD_CPK,
		OPNING03_CPK,
		OPNING1_CPK,
		OUTTAKES_CPK,
		ST0A_CPK,
		ST1A_CPK,
		ST2A_CPK,
		ST3A_CPK,
		ST4A_CPK,
		ST5A_CPK,
		ST6A_CPK,
		ST7A_CPK,
		WDLOGO_CPK,
		DISC_ABS_TXT,
		DISC_BIB_TXT,
		DISC_CPY_TXT,
		EV001_BMP,
		EV002_BMP,
		EV003_BMP,
		EV004_BMP,
		EV005_BMP,
		EV006_BMP,
		EV007_BMP,
		EV008_BMP,
		EV009_BMP,
		EV010_BMP,
		EV011_BMP,
		EV012_BMP,
		EV013_BMP,
		EV014_BMP,
		EV015_BMP,
		EV016_BMP,
		EV017_BMP,
		EV018_BMP,
		EV019_BMP,
		EV020_BMP,
		EV021_BMP,
		EV022_BMP,
		EV023_BMP,
		EV024_BMP,
		EV025_BMP,
		EV026_BMP,
		EV027_BMP,
		EV028_BMP,
		EV029_BMP,
		EV030_BMP,
		EV031_BMP,
		EV032_BMP,
		EV033_BMP,
		EV034_BMP,
		EV035_BMP,
		EV036_BMP,
		EV037_BMP,
		EV038_BMP,
		EV039_BMP,
		EV040_BMP,
		EV041_BMP,
		EV042_BMP,
		EV043_BMP,
		EV044_BMP,
		EV045_BMP,
		EV046_BMP,
		EV047_BMP,
		EV048_BMP,
		EV049_BMP,
		EV050_BMP,
		EV051_BMP,
		EV052_BMP,
		EV053_BMP,
		EV054_BMP,
		EV055_BMP,
		EV056_BMP,
		EV057_BMP,
		EV058_BMP,
		EV059_BMP,
		EV060_BMP,
		EV061_BMP,
		EV062_BMP,
		EV063_BMP,
		EV064_BMP,
		EV065_BMP,
		EV066_BMP,
		EV067_BMP,
		EV068_BMP,
		EV069_BMP,
		EV070_BMP,
		EV071_BMP,
		EV072_BMP,
		EV073_BMP,
		EV074_BMP,
		EV075_BMP,
		EV076_BMP,
		EV077_BMP,
		EV078_BMP,
		EV079_BMP,
		EV080_BMP,
		EV081_BMP,
		EV082_BMP,
		EV083_BMP,
		EV084_BMP,
		EV085_BMP,
		EV086_BMP,
		EV087_BMP,
		EV088_BMP,
		EV089_BMP,
		EV090_BMP,
		EV091_BMP,
		EV092_BMP,
		EV093_BMP,
		EV094_BMP,
		EV095_BMP,
		EV096_BMP,
		EV097_BMP,
		EV098_BMP,
		EV099_BMP,
		EV100_BMP,
		EV101_BMP,
		EV102_BMP,
		EV103_BMP,
		EV104_BMP,
		EV105_BMP,
		EV106_BMP,
		EV107_BMP,
		EV108_BMP,
		EV109_BMP,
		EV110_BMP,
		EV111_BMP,
		EV112_BMP,
		EV113_BMP,
		EV114_BMP,
		EV115_BMP,
		EV116_BMP,
		EV117_BMP,
		EV118_BMP,
		EV119_BMP,
		EV120_BMP,
		EV121_BMP,
		EV122_BMP,
		EV123_BMP,
		EV124_BMP,
		EV125_BMP,
		EV126_BMP,
		EV127_BMP,
		EV128_BMP,
		EV129_BMP,
		EV130_BMP,
		EV131_BMP,
		EV132_BMP,
		EV133_BMP,
		EV134_BMP,
		EV135_BMP,
		EV136_BMP,
		EV137_BMP,
		EV138_BMP,
		EV139_BMP,
		EV140_BMP,
		EV141_BMP,
		EV142_BMP,
		EV143_BMP,
		EV144_BMP,
		EV145_BMP,
		EV146_BMP,
		EV147_BMP,
		EV148_BMP,
		EV149_BMP,
		EV150_BMP,
		EV151_BMP,
		EV152_BMP,
		EV153_BMP,
		EV154_BMP,
		FACEFONT_GRP,
		BG_DG2,
		BR_NAME_GRP,
		BR_VDP0_GRP,
		BR_VDP1_GRP,
		BR_VDP2_GRP,
		BR_VDP3_GRP,
		BR_VDP4_GRP,
		BR_VDP5_GRP,
		BR_VDP6_GRP,
		BR_VDP7_GRP,
		CLUT_GRP,
		CPK_BIN,
		DF_BIN,
		DF10100_SBN,
		DF10200_SBN,
		DF10300_SBN,
		DF10400_SBN,
		DF10500_SBN,
		DF10600_SBN,
		DF10700_SBN,
		DF10800_SBN,
		DF10900_SBN,
		DF11000_SBN,
		DF11100_SBN,
		DF11300_SBN,
		DF2000_SBN,
		DF2001_SBN,
		DF2002_SBN,
		DF2003_SBN,
		DF2004_SBN,
		DF2005_SBN,
		DF2006_SBN,
		DF2007_SBN,
		DF2008_SBN,
		DF2009_SBN,
		DF2010_SBN,
		DF2011_SBN,
		DF2012_SBN,
		DF2013_SBN,
		DF2014_SBN,
		DF2015_SBN,
		DF2016_SBN,
		DF2017_SBN,
		DF2018_SBN,
		DF2019_SBN,
		DF2020_SBN,
		DF2021_SBN,
		DF2022_SBN,
		DF2023_SBN,
		DF2024_SBN,
		DF2025_SBN,
		DF2026_SBN,
		DF2027_SBN,
		DF2028_SBN,
		DF2029_SBN,
		DF2030_SBN,
		DF2031_SBN,
		DF2032_SBN,
		DF2033_SBN,
		DF2034_SBN,
		DF2035_SBN,
		DF2036_SBN,
		DF2037_SBN,
		DF2038_SBN,
		DF2039_SBN,
		DF2040_SBN,
		DF2041_SBN,
		DF2042_SBN,
		DF2043_SBN,
		DF2044_SBN,
		DF2045_SBN,
		DF2046_SBN,
		DF2047_SBN,
		DF2048_SBN,
		DF2049_SBN,
		DF2050_SBN,
		DF2051_SBN,
		DF2052_SBN,
		DF2053_SBN,
		DF2054_SBN,
		DF2055_SBN,
		DF2056_SBN,
		DF2057_SBN,
		DF2058_SBN,
		DF2059_SBN,
		DF2060_SBN,
		DF2061_SBN,
		DF2062_SBN,
		DF2063_SBN,
		DF2064_SBN,
		DF2065_SBN,
		DF2066_SBN,
		DF2067_SBN,
		DF2068_SBN,
		DF2069_SBN,
		DF2070_SBN,
		DF2071_SBN,
		DF2072_SBN,
		DF2073_SBN,
		DF2074_SBN,
		DF2075_SBN,
		DF2076_SBN,
		DF2077_SBN,
		DF2078_SBN,
		DF2079_SBN,
		DF2080_SBN,
		DF2081_SBN,
		DF2082_SBN,
		DF2083_SBN,
		DF2084_SBN,
		DF2085_SBN,
		DF2086_SBN,
		DF2087_SBN,
		DF2088_SBN,
		DF2089_SBN,
		DF2090_SBN,
		DF2091_SBN,
		DF2092_SBN,
		DF2093_SBN,
		DF2094_SBN,
		DF2095_SBN,
		DF2096_SBN,
		DF2097_SBN,
		DF2098_SBN,
		DF2099_SBN,
		DF2100_SBN,
		DF2101_SBN,
		DF2102_SBN,
		DF2103_SBN,
		DF2104_SBN,
		DF2105_SBN,
		DF2106_SBN,
		DF2107_SBN,
		DF2108_SBN,
		DF2109_SBN,
		DF2110_SBN,
		DF2111_SBN,
		DF2112_SBN,
		DF2113_SBN,
		DF2114_SBN,
		DF2115_SBN,
		DF2116_SBN,
		DF2117_SBN,
		DF2118_SBN,
		DF2119_SBN,
		DF2120_SBN,
		DF2121_SBN,
		DF2122_SBN,
		DF2123_SBN,
		DF2124_SBN,
		DF2126_SBN,
		DF2127_SBN,
		DF2128_SBN,
		DF2129_SBN,
		DF2130_SBN,
		DF2131_SBN,
		DF2132_SBN,
		DF2133_SBN,
		DF2134_SBN,
		DF2135_SBN,
		DF2136_SBN,
		DF2137_SBN,
		DF2138_SBN,
		DF2139_SBN,
		DF2140_SBN,
		DF2141_SBN,
		DF2142_SBN,
		DF2143_SBN,
		DF2144_SBN,
		DF2145_SBN,
		DF2146_SBN,
		DF2147_SBN,
		DF2148_SBN,
		DF2149_SBN,
		DF2150_SBN,
		DF2151_SBN,
		DF2152_SBN,
		DF2153_SBN,
		DF2154_SBN,
		DF2155_SBN,
		DF2156_SBN,
		DF2157_SBN,
		DF2158_SBN,
		DF2159_SBN,
		DF2160_SBN,
		DF2161_SBN,
		DF2162_SBN,
		DF2163_SBN,
		DF2164_SBN,
		DF2165_SBN,
		DF2166_SBN,
		DF2167_SBN,
		DF2168_SBN,
		DF2169_SBN,
		DF2170_SBN,
		DF2171_SBN,
		DF2172_SBN,
		DF2173_SBN,
		DF2174_SBN,
		DF2175_SBN,
		DF2176_SBN,
		DF2177_SBN,
		DF2178_SBN,
		DF2179_SBN,
		DFWORK0_BIN,
		DFWORK1_BIN,
		DFWORK2_BIN,
		DFWORK3_BIN,
		DFWORK4_BIN,
		DFWORK5_BIN,
		DFWORK6_BIN,
		DFWORK7_BIN,
		FACE111_BMP,
		FACE111_DG2,
		FACEFONT_GRP2,
		FIELD0_BIN,
		FIELD1_BIN,
		FIELD2_BIN,
		FIELD3_BIN,
		FIELD4_BIN,
		FIELD5_BIN,
		FIELD6_BIN,
		FIELD7_BIN,
		FLD001_BIN,
		FLD002_BIN,
		FLD003_BIN,
		FLD004_BIN,
		FLD005_BIN,
		FLD006_BIN,
		FLD007_BIN,
		FLD008_BIN,
		FLD009_BIN,
		FLD010_BIN,
		FLD011_BIN,
		FLD012_BIN,
		FLD013_BIN,
		FLD014_BIN,
		FLD015_BIN,
		FLD016_BIN,
		FLD017_BIN,
		FLD018_BIN,
		FLD019_BIN,
		FLD020_BIN,
		FLD021_BIN,
		FLD022_BIN,
		FLD023_BIN,
		FLD024_BIN,
		FLD025_BIN,
		FLD026_BIN,
		FLD027_BIN,
		FLD028_BIN,
		FLD029_BIN,
		FLD030_BIN,
		FLD031_BIN,
		FLVRAM_BIN,
		GMOVER_BIN,
		ICON_GRP,
		ITEM_GRP,
		LOAD_BMP,
		MAP_BMP,
		MEET00_BMP,
		MEET01_BMP,
		MEET02_BMP,
		MEET03_BMP,
		MT_VDP0_GRP,
		MT_VDP1_GRP,
		MT_VDP2_GRP,
		MT_VDP3_GRP,
		MT_VDP4_GRP,
		MT_VDP5_GRP,
		MT_VDP6_GRP,
		MT_VDP7_GRP,
		PANEL_GRP,
		READY_BIN,
		READY_BMP,
		READY_GRP,
		READY0_BIN,
		READY1_BIN,
		READY2_BIN,
		READY3_BIN,
		READY4_BIN,
		READY5_BIN,
		READY6_BIN,
		READY7_BIN,
		RE_OPEN_BIN,
		SAVEBG_BMP,
		SAVEBG_PAL,
		SIRO_GRP,
		STRING_GRP,
		TEN_BMP,
		WIN_GRP,
		SDDRVS_TSK,
		s06_LNK,
		s07_LNK,
		s08_LNK,
		s09_LNK,
		s10_LNK,
		s11_LNK,
		s12_LNK,
		s13_LNK,
		s14_LNK,
		s15_LNK,
		s16_LNK,
		s18_LNK,
		s19_LNK,
		s20_LNK,
		s21_LNK,
		s22_LNK,
		s23_LNK,
		s24_LNK,
		s25_LNK,
		s26_LNK,
		s27_LNK,
		s28_LNK,
		s29_LNK,
		s30_LNK,
		s31_LNK,
		s32_LNK,
		s33_LNK,
		BB00_AI,
		BB01_AI,
		BB02_AI,
		BB03_AI,
		BB04_AI,
		BB05_AI,
		BB06_AI,
		BB07_AI,
		BB08_AI,
		BB09_AI,
		E00_AI,
		E01_AI,
		E03_AI,
		E04_AI,
		E05_AI,
		E06_AI,
		E11_AI,
		E12_AI,
		E14_AI,
		E15_AI,
		E16_AI,
		E17_AI,
		E19_AI,
		E20_AI,
		E21_AI,
		E22_AI,
		E23_AI,
		E24_AI,
		E25_AI,
		E26_AI,
		E27_AI,
		E28_AI,
		E29_AI,
		E32_AI,
		E34_AI,
		E35_AI,
		E36_AI,
		E38_AI,
		E39_AI,
		E40_AI,
		E41_AI,
		E43_AI,
		F00_AI,
		F01_AI,
		F02_AI,
		F03_AI,
		F04_AI,
		F05_AI,
		F06_AI,
		F07_AI,
		F08_AI,
		F09_AI,
		KANSEI_AI,
		S01_AI,
		S02_AI,
		S03_AI,
		TETTAI_AI,
		X12_AI,
		X18_AI,
		X19_AI,
		X21_AI,
	}

	long[] fileOffset;


	//long lowRamReady = 0x2A2C60;

	public DataBase() {

		//FileStream
		fileStream = new FileStream();

		fileOffset = Array.filePos;

		//Scenario
		for (int i = 0; i < scenario.length; i++) {
			scenario[i] = new Scenario();
		}

		//Pointer
		ReadyPointer[0] = 0x3A7B8;
		ReadyPointer[1] = 0x38174;
		ReadyPointer[2] = 0x39FF8;
		ReadyPointer[3] = 0x3A180;
		ReadyPointer[4] = 0x396D8;
		ReadyPointer[5] = 0x3A140;
		ReadyPointer[6] = 0x39E04;
		ReadyPointer[7] = 0x3A288;

		//String
		ReadyString[0] = 0x4C30C;
		ReadyString[1] = 0x49CB4;
		ReadyString[2] = 0x4BB58;
		ReadyString[3] = 0x4BCD0;
		ReadyString[4] = 0x4B218;
		ReadyString[5] = 0x4BC90;
		ReadyString[6] = 0x4B968;
		ReadyString[7] = 0x4BDF0;
	}

	public boolean OpenFile(File file) {
		//Stream
		isFileOpen = fileStream.openFile(file);

		//Load
		if (isFileOpen) {
			//Load Pointer
			LoadPointers();

			//Load READY
			LoadREADY();

			//Load DFWORK
			LoadDFWORK();

			//Load Sprite
			LoadSprite(0);

			//Load Gameplay
			LoadGameplay();
		}

		return isFileOpen;
	}

	public void SaveFile(File file) {
		//Save
		if (isFileOpen) {

			//Save Scenario
			SaveDFWORK();

			//Save Sprite
			SaveSprite();

			//Save Gameplay
			SaveGameplay();
		}
	}

	public void LoadPointers() {
		if (isLoadPointers) {return;}
		isLoadPointers = true;

		//Ready
		int i = 0;
		String string =   "READY" + i + "_BIN";
		EnumFile index = EnumFile.valueOf(string);
		long READY = fileOffset[index.ordinal()];

		//Item Name Pointers
		{
			//String
			fileStream.SetStringMap(READY, ReadyString[0], 0x3C8);
			//Pointer
			fileStream.SetPosition(READY, ReadyPointer[0]);
			itemNamePointers = fileStream.ReadPointerTable(73);
		}

		//Character Name Pointers
		{
			//String
			fileStream.SetStringMap(READY, ReadyString[0] + 0x3C8, 0x538);
			//Pointer
			fileStream.SetPosition(READY, ReadyPointer[0] + 0x124);
			characterNamePointers = fileStream.ReadPointerTable(171);
		}

		//Skill Name Pointers
		{
			//String
			fileStream.SetStringMap(READY, ReadyString[0] + 0x1048, 0x260);
			//Pointer
			fileStream.SetPosition(READY, ReadyPointer[0] + 0xA30);
			skillNamePointers = fileStream.ReadPointerTable(77);
		}

		//Exp Table
		fileStream.SetPosition(READY, ReadyPointer[0] + 0x7C8);
		{
			expTable = fileStream.ReadAttributeTable(0x66);
		}
	}


	public void LoadREADY() {
		for (int i = 0; i < scenario.length; i++) {
			String string =   "READY" + i + "_BIN";
			EnumFile index = EnumFile.valueOf(string);
			long READY = fileOffset[index.ordinal()];

			//Item
			fileStream.SetPosition(READY, ReadyString[i]);
			{
				scenario[i].itemName = fileStream.ReadStringTable(0x3C8);
				scenario[i].characterName = fileStream.ReadStringTable(0x538);
				scenario[i].buildingName = fileStream.ReadStringTable(0x26C);
				scenario[i].locationName = fileStream.ReadStringTable(0x16c);
				scenario[i].jobName = fileStream.ReadStringTable(0x318);
				fileStream.SkipByte(0x58);
				scenario[i].skillName = fileStream.ReadStringTable(0x260);
			}
		}
	}

	public void LoadDFWORK() {
		for (int i = 0; i < scenario.length; i++) {
			String string =   "DFWORK" + i + "_BIN";
			EnumFile index = EnumFile.valueOf(string);
			long DFWORK = fileOffset[index.ordinal()];

			//Status
			fileStream.SetPosition(DFWORK, 0x30);
			{
				fileStream.SkipShort(171); //status
				scenario[i].owner = fileStream.ReadAttributeTable(171);
				fileStream.SkipShort(171); //class
				scenario[i].level = fileStream.ReadAttributeTable(171);
				scenario[i].job = fileStream.ReadAttributeTable(171);

				scenario[i].location = fileStream.ReadAttributeTable(171);
				scenario[i].troopType = fileStream.ReadAttributeTable(171);
				scenario[i].troopAmount = fileStream.ReadAttributeTable(171);
				fileStream.SkipShort(171); //action
				fileStream.SkipShort(171); //armyAI
				scenario[i].life = fileStream.ReadAttributeTable(171);

				fileStream.SkipShort(171); //hp
				scenario[i].HP = fileStream.ReadAttributeTable(171);
				fileStream.SkipShort(171); //mp
				scenario[i].MP = fileStream.ReadAttributeTable(171);
				scenario[i].STR = fileStream.ReadAttributeTable(171);
				scenario[i].DEF = fileStream.ReadAttributeTable(171);
				scenario[i].ESC = fileStream.ReadAttributeTable(171);
				scenario[i].CMD = fileStream.ReadAttributeTable(171);
				scenario[i].INT = fileStream.ReadAttributeTable(171);
				scenario[i].loyalty = fileStream.ReadAttributeTable(171);

				scenario[i].item = fileStream.ReadAttributeTable(171);
				scenario[i].face = fileStream.ReadAttributeTable(171);
			}

			//Medals
			fileStream.SetPosition(DFWORK, 0x2442);
			{
				scenario[i].medal0 = fileStream.ReadAttributeTable(171);
				scenario[i].medal1 = fileStream.ReadAttributeTable(171);
				scenario[i].medal2 = fileStream.ReadAttributeTable(171);
				scenario[i].medal3 = fileStream.ReadAttributeTable(171);
				scenario[i].medal4 = fileStream.ReadAttributeTable(171);
				scenario[i].EXP = fileStream.ReadAttributeTable(171);
			}

			//Resist
			fileStream.SetPosition(DFWORK, 0x3C4E);
			{
				scenario[i].resSword = fileStream.ReadAttributeTable(171);
				scenario[i].resIce = fileStream.ReadAttributeTable(171);
				scenario[i].resFire = fileStream.ReadAttributeTable(171);
				scenario[i].resLight = fileStream.ReadAttributeTable(171);
				scenario[i].resDark = fileStream.ReadAttributeTable(171);
				scenario[i].sex = fileStream.ReadAttributeTable(171);
				scenario[i].SBN = fileStream.ReadAttributeTable(171);
				scenario[i].spellTier = fileStream.ReadAttributeTable(171);
				scenario[i].formation = fileStream.ReadAttributeTable(171);
				scenario[i].AllowRecruit = fileStream.ReadAttributeTable(171);
			}

			//Skills
			fileStream.SetPosition(DFWORK, 0x8712);
			{
				//Skill1
				scenario[i].skillType0 = fileStream.ReadAttributeTable(180);
				scenario[i].skillTarget0 = fileStream.ReadAttributeTable(180);
				fileStream.SkipShort(180); //Text
				scenario[i].skillMP0 = fileStream.ReadAttributeTable(180);
				scenario[i].skillRate0 = fileStream.ReadAttributeTable(180);
				fileStream.SkipShort(180); //Unknown
				scenario[i].skillDmg0 = fileStream.ReadAttributeTable(180);
				fileStream.SkipShort(180); //Accuracy
				fileStream.SkipShort(180); //Unknown
				scenario[i].skillPow0 = fileStream.ReadAttributeTable(180);

				//Skill2
				scenario[i].skillType1 = fileStream.ReadAttributeTable(180);
				scenario[i].skillTarget1 = fileStream.ReadAttributeTable(180);
				fileStream.SkipShort(180); //Text
				scenario[i].skillMP1 = fileStream.ReadAttributeTable(180);
				scenario[i].skillRate1 = fileStream.ReadAttributeTable(180);
				fileStream.SkipShort(180); //Unknown
				scenario[i].skillDmg1 = fileStream.ReadAttributeTable(180);
				fileStream.SkipShort(180); //Accuracy
				fileStream.SkipShort(180); //Unknown
				scenario[i].skillPow1 = fileStream.ReadAttributeTable(180);

				//Skill3
				scenario[i].skillType2 = fileStream.ReadAttributeTable(180);
				scenario[i].skillTarget2 = fileStream.ReadAttributeTable(180);
				fileStream.SkipShort(180); //Text
				scenario[i].skillMP2 = fileStream.ReadAttributeTable(180);
				scenario[i].skillRate2 = fileStream.ReadAttributeTable(180);
				fileStream.SkipShort(180); //Unknown
				scenario[i].skillDmg2 = fileStream.ReadAttributeTable(180);
				fileStream.SkipShort(180); //Accuracy
				fileStream.SkipShort(180); //Unknown
				scenario[i].skillPow2 = fileStream.ReadAttributeTable(180);
			}
		}
	}

	public void LoadGameplay() {

		//Load RandomSeed
		fileStream.SetPosition(Array.randomSeedOffset[0]);
		int readInt = fileStream.ReadInt();
		isRandomSeedCorrect = (readInt == Array.randomSeedCorrect);

		//Load UnlockAllCharacter
		fileStream.SetPosition(0x1BEA8);
		readInt = fileStream.ReadInt();
		isUnlockAllCharacter = (readInt == 0x0006);

		//Load DisableSkullChildren
		fileStream.SetPosition(0x2448FB1F);
		readInt = fileStream.ReadByte();
		isDisableSkullChildren = (readInt == 0x00);
	}


	//Sprite
	public void LoadSprite(int i) {
		fileStream.SetPosition(Array.filePos[EnumFile.DF2000_SBN.ordinal() + i]);
		sbn[i] = new SBN(i, fileStream);
	}
	public void SaveSprite() {
		for (int i = 0; i < sbn.length; i++) {
			if (sbn[i] != null) {
				sbn[i].Save(fileStream);
			}
		}
	}
	public BufferedImage GetSpriteImage(int i)
	{
		if (sbn[i] == null) { LoadSprite(i); }
		return sbn[i].GetFrameImage();
	}


	public void SaveDFWORK() {
		for (int i = 0; i < scenario.length; i++) {
			String string =   "DFWORK" + i + "_BIN";
			EnumFile index = EnumFile.valueOf(string);
			long DFWORK = fileOffset[index.ordinal()];

			//Position Owner
			fileStream.SetPosition(DFWORK, 0x30);
			{
				fileStream.SkipShort(171); //Status
				fileStream.WriteAttributeTable(scenario[i].owner);
				fileStream.SkipShort(171); //Class
				fileStream.WriteAttributeTable(scenario[i].level);
				fileStream.WriteAttributeTable(scenario[i].job);

				fileStream.WriteAttributeTable(scenario[i].location);
				fileStream.WriteAttributeTable(scenario[i].troopType);
				fileStream.WriteAttributeTable(scenario[i].troopAmount);
				fileStream.SkipShort(171); //action
				fileStream.SkipShort(171); //armyAI
				fileStream.WriteAttributeTable(scenario[i].life);


				fileStream.WriteAttributeTable(scenario[i].HP); //Current HP
				fileStream.WriteAttributeTable(scenario[i].HP); //Max HP
				fileStream.WriteAttributeTable(scenario[i].MP); //Current MP
				fileStream.WriteAttributeTable(scenario[i].MP); //Max MP
				fileStream.WriteAttributeTable(scenario[i].STR);
				fileStream.WriteAttributeTable(scenario[i].DEF);
				fileStream.WriteAttributeTable(scenario[i].ESC);
				fileStream.WriteAttributeTable(scenario[i].CMD);
				fileStream.WriteAttributeTable(scenario[i].INT);
				fileStream.WriteAttributeTable(scenario[i].loyalty);

				fileStream.WriteAttributeTable(scenario[i].item);
				fileStream.WriteAttributeTable(scenario[i].face);
			}

			//Position Medals
			fileStream.SetPosition(DFWORK, 0x2442);
			{
				fileStream.WriteAttributeTable(scenario[i].medal0);
				fileStream.WriteAttributeTable(scenario[i].medal1);
				fileStream.WriteAttributeTable(scenario[i].medal2);
				fileStream.WriteAttributeTable(scenario[i].medal3);
				fileStream.WriteAttributeTable(scenario[i].medal4);
				fileStream.WriteAttributeTable(scenario[i].EXP);
			}

			//Position Resist
			fileStream.SetPosition(DFWORK, 0x3C4E);
			{
				fileStream.WriteAttributeTable(scenario[i].resSword);
				fileStream.WriteAttributeTable(scenario[i].resIce);
				fileStream.WriteAttributeTable(scenario[i].resFire);
				fileStream.WriteAttributeTable(scenario[i].resLight);
				fileStream.WriteAttributeTable(scenario[i].resDark);
				fileStream.WriteAttributeTable(scenario[i].sex);
				fileStream.WriteAttributeTable(scenario[i].SBN);
				fileStream.WriteAttributeTable(scenario[i].spellTier);
				fileStream.WriteAttributeTable(scenario[i].formation);
				fileStream.WriteAttributeTable(scenario[i].AllowRecruit);
			}

			//Position Skills
			fileStream.SetPosition(DFWORK, 0x8712);
			{
				//Skill 0
				fileStream.WriteAttributeTable(scenario[i].skillType0);
				fileStream.WriteAttributeTable(scenario[i].skillTarget0);
				fileStream.SkipShort(180); //Text
				fileStream.WriteAttributeTable(scenario[i].skillMP0);
				fileStream.WriteAttributeTable(scenario[i].skillRate0);
				fileStream.SkipShort(180); //Unknown
				fileStream.WriteAttributeTable(scenario[i].skillDmg0);
				fileStream.SkipShort(180); //Accuracy
				fileStream.SkipShort(180); //Unknown
				fileStream.WriteAttributeTable(scenario[i].skillPow0);

				//Skill 1
				fileStream.WriteAttributeTable(scenario[i].skillType1);
				fileStream.WriteAttributeTable(scenario[i].skillTarget1);
				fileStream.SkipShort(180); //Text
				fileStream.WriteAttributeTable(scenario[i].skillMP1);
				fileStream.WriteAttributeTable(scenario[i].skillRate1);
				fileStream.SkipShort(180); //Unknown
				fileStream.WriteAttributeTable(scenario[i].skillDmg1);
				fileStream.SkipShort(180); //Accuracy
				fileStream.SkipShort(180); //Unknown
				fileStream.WriteAttributeTable(scenario[i].skillPow1);

				//Skill 2
				fileStream.WriteAttributeTable(scenario[i].skillType2);
				fileStream.WriteAttributeTable(scenario[i].skillTarget2);
				fileStream.SkipShort(180); //Text
				fileStream.WriteAttributeTable(scenario[i].skillMP2);
				fileStream.WriteAttributeTable(scenario[i].skillRate2);
				fileStream.SkipShort(180); //Unknown
				fileStream.WriteAttributeTable(scenario[i].skillDmg2);
				fileStream.SkipShort(180); //Accuracy
				fileStream.SkipShort(180); //Unknown
				fileStream.WriteAttributeTable(scenario[i].skillPow2);
			}
		}
	}


	//Gameplay
	public void SaveGameplay() {

		//Save RandomSeed
		for (int i = 0; i < 8; i++)
		{
			fileStream.SetPosition(Array.randomSeedOffset[i]);
			if (isRandomSeedCorrect) {
				fileStream.WriteInt(Array.randomSeedCorrect);
			} else {
				fileStream.WriteInt(Array.randomSeedOriginal[i]);
			}
		}

		//Save UnlockAllCharacter
		int flag = isUnlockAllCharacter ? 0x0006 : 0x82DC;
		fileStream.WriteInt(0x1BEA8, flag);

		//Save DisableSkullChildren
		flag = isDisableSkullChildren ? 0x00 : 0x01;
		fileStream.WriteByte(0x2448FB1F, flag);
		fileStream.WriteByte(0x2456CC4F, flag);
		fileStream.WriteByte(0x246453FF, flag);
		fileStream.WriteByte(0x2472378F, flag);
		fileStream.WriteByte(0x248008BF, flag);
		fileStream.WriteByte(0x248DD0BF, flag);
		fileStream.WriteByte(0x249B8F8F, flag);
		fileStream.WriteByte(0x24A9731F, flag);
	}













	//Menu Functions
	public String GetItemName(int s, int c) {
		int i = scenario[s].item[c];
		i = itemNamePointers[i];
		if (i < 0 || i > itemNamePointers.length) { return "----"; }
		return scenario[s].itemName[i];
	}

	public String GetCharacterName(int s, int c) {
		int i = characterNamePointers[c];
		if (i < 0 || i > characterNamePointers.length) { return "----"; }
		return scenario[s].characterName[i];
	}

	public String[] GetCharacterNameList(int s) {
		String[] list = new String[171];

		for (int i = 0; i < list.length; i++)
		{
			int id = characterNamePointers[i];
			if (id > -1 && id < characterNamePointers.length) {
				list[i] = i + " " + scenario[s].characterName[id];
			}
		}

		return list;
	}

	public String[] GetSpriteNameList(int s) {
		String[] list = new String[171];

		for (int i = 0; i < list.length; i++)
		{
			int id = characterNamePointers[i];
			if (id > -1 && id < characterNamePointers.length) {
				list[i] = "SBN " + String.format("%03d", i) + " " + scenario[s].characterName[id];
			}
		}

		return list;
	}

	public String GetSkillName(int s, int c, int index) {
		int i = 0;

		if (index == 0) { i = scenario[s].skillType0[c]; }
		else if (index == 1) { i = scenario[s].skillType1[c]; }
		else if (index == 2) { i = scenario[s].skillType2[c]; }

		if (i < 0 || i > skillNamePointers.length) { return "----"; }

		i = skillNamePointers[i];

		return scenario[s].skillName[i];
	}

}

