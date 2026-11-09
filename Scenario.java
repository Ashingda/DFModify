public class Scenario {

	//Strings
	public String[] itemName;
	public String[] characterName;
	public String[] buildingName;
	public String[] locationName;
	public String[] jobName;
	public String[] skillName;

	//Tables
	//public int[] status;
	public int[] owner;
	//public int[] class;
	public int[] level;
	public int[] job;

	public int[] location;
	public int[] troopType;
	public int[] troopAmount;
	//public int[] action;
	//public int[] armyAI;
	public int[] life;

	//public int[] hp;
	public int[] HP;
	//public int[] mp;
	public int[] MP;
	public int[] STR;
	public int[] DEF;
	public int[] ESC;
	public int[] CMD;
	public int[] INT;
	public int[] loyalty;

	public int[] item;
	public int[] face;

	//public int[] win;
	//public int[] loss;
	//public int[] won;
	//public int[] lost;
	public int[] medal0;
	public int[] medal1;
	public int[] medal2;
	public int[] medal3;
	public int[] medal4;
	public int[] EXP;

	public int[] resSword;
	public int[] resIce;
	public int[] resFire;
	public int[] resLight;
	public int[] resDark;

	public int[] sex;
	public int[] SBN;
	public int[] spellTier;
	public int[] formation;
	public int[] AllowRecruit;

	public int[] skillType0;
	public int[] skillTarget0;
	public int[] skillMP0;
	public int[] skillRate0;
	public int[] skillDmg0;
	public int[] skillPow0;

	public int[] skillType1;
	public int[] skillTarget1;
	public int[] skillMP1;
	public int[] skillRate1;
	public int[] skillDmg1;
	public int[] skillPow1;

	public int[] skillType2;
	public int[] skillTarget2;
	public int[] skillMP2;
	public int[] skillRate2;
	public int[] skillDmg2;
	public int[] skillPow2;

	public int GetMedal(int c, int troop) {
		int troopGroup = troop / 2;
		int troopByte = troop - troopGroup *2;
		int value = 0;

		//Medal
		if (troopGroup == 0) { value = medal0[c]; }
		if (troopGroup == 1) { value = medal1[c]; }
		if (troopGroup == 2) { value = medal2[c]; }
		if (troopGroup == 3) { value = medal3[c]; }
		if (troopGroup == 4) { value = medal4[c]; }

		//Split
		if (troopByte == 0) {
			//High
			value = (value >> 8) & 0xFF;
		} else {
			//Low
			value = value & 0xFF;
		}

		//Cap
		if (value == 15) { value = 0; }

		return value;
	}
}