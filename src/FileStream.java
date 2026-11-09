import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class FileStream {
	private static final int SECTOR_SIZE = 2352;
	private static final int USER_DATA_OFFSET = 16; // Skip 12 byte sync + 4 byte header
	private static final int USER_DATA_SIZE = 2048;
	int[] sectorOffset = new int[2];

	private RandomAccessFile randomAccessFile;

	int[] stringMap;
	boolean isFileOpen;

	byte[] identify = {0x44, 0x52, 0x41, 0x47, 0x4F, 0x4E, 0x20, 0x46, 0x4F, 0x52, 0x43, 0x45};

	public Boolean openFile(File file) {
		if (isFileOpen) { closeFile(); }

		isFileOpen = false;

		try {
			randomAccessFile = new RandomAccessFile(file, "rw");

			//Check if file is correct
			SetPosition(0x70);
			byte[] content = ReadByte(0xc);

			if (Arrays.equals(content, identify)) {
				isFileOpen = true;
				System.err.println("Open!");
			}

		} catch (IOException e) {
			System.err.println("Open file error: " + e.getMessage());
		}
		return isFileOpen;
	}

	public void WriteByte(int newValue) {
		try {
			//Write 0x00
			randomAccessFile.writeByte(newValue);
		} catch (IOException e) {
			System.err.println("Write error: " + e.getMessage());
		}
	}

	public void WriteByte(long pos, int newValue) {
		SetPosition(pos);
		try {
			//Write 0x00
			randomAccessFile.writeByte(newValue);
		} catch (IOException e) {
			System.err.println("Write error: " + e.getMessage());
		}
	}

	public void WriteByte(byte[] data) {
		try {
			for (int i = 0; i < data.length; i++)
			{
				randomAccessFile.writeByte(data[i]);
				UpdateSector(1);
			}

		} catch (IOException e) {
			System.err.println("Write error: " + e.getMessage());
		}
	}

	public void WriteShort(int newValue) {
		try {
			//Write 0x0000
			randomAccessFile.writeShort(newValue);
		} catch (IOException e) {
			System.err.println("Write error: " + e.getMessage());
		}
	}

	public void WriteInt(int newValue) {
		try {
			//Write 0x00000000
			randomAccessFile.writeInt(newValue);
		} catch (IOException e) {
			System.err.println("Write error: " + e.getMessage());
		}
	}

	public void WriteInt(long pos, int newValue) {
		SetPosition(pos);
		try {
			//Write 0x00000000
			randomAccessFile.writeInt(newValue);
		} catch (IOException e) {
			System.err.println("Write error: " + e.getMessage());
		}
	}

	public int ReadByte() {
		try {
			//Read 0x00
			int read = randomAccessFile.readUnsignedByte();
			UpdateSector(1);
			return read;
		} catch (IOException e) {
			System.err.println("ReadByte error: " + e.getMessage());
		}
		return 0;
	}

	public byte[] ReadByte(int count) {
		byte[] bytes = new byte[count];

		try {
			//Read 0x00
			for (int i = 0; i < count; i++) {
				bytes[i] = (byte)randomAccessFile.readUnsignedByte();
				UpdateSector(1);
			}
		} catch (IOException e) {
			System.err.println("ReadByte error: " + e.getMessage());
		}
		return bytes;
	}

	public int ReadShort() {
		try {
			//Read 0x0000
			int read = randomAccessFile.readUnsignedShort();
			UpdateSector(2);
			return read;
		} catch (IOException e) {
			System.err.println("ReadByte error: " + e.getMessage());
		}
		return 0;
	}

	public int ReadInt() {
		try {
			//Read 0x00000000
			int read = randomAccessFile.readUnsignedShort();
			UpdateSector(2);
			read = (read << 16) | (randomAccessFile.readUnsignedShort() & 0xFFFF);
			UpdateSector(2);
			return read;
		} catch (IOException e) {
			System.err.println("ReadByte error: " + e.getMessage());
		}
		return 0;
	}

	public String ReadString(int length) {
		StringBuilder string = new StringBuilder();
		try {
			for (int x = 0; x < length; x++) {
				//Read
				int readByte = randomAccessFile.readUnsignedByte();

				//Byte is not Zero
				if (readByte != 0) {
					string.append((char)readByte);
				} else {
					break;
				}

				//Sector
				UpdateSector(1);
			}
		} catch (IOException e) {
			System.err.println("ReadByte error: " + e.getMessage());
		}

		return string.toString();
	}

	public void SetPosition(long pos) {
		sectorOffset = calculateFromRawOffset(pos);
		long filePosition = calculateSectorPointer(sectorOffset);

		try {
			randomAccessFile.seek(filePosition);
		} catch (IOException e) {
			System.err.println("Position error: " + e.getMessage());
		}
	}

	public void SetPosition(long pos, long offset) {
		sectorOffset = calculateFromRawOffset(pos);
		offset += sectorOffset[1];

		for (int i = 0; i < 3; i++) {
			if (offset >= USER_DATA_SIZE) {
				sectorOffset[0]++;
				offset -= USER_DATA_SIZE;
				i = 0;
			}
		}
		sectorOffset[1] = (int)offset;

		long filePosition = calculateSectorPointer(sectorOffset);

		try {
			randomAccessFile.seek(filePosition);
		} catch (IOException e) {
			System.err.println("Position error: " + e.getMessage());
		}
	}

	public long GetPosition() {
		try {
			return randomAccessFile.getFilePointer();
		} catch (IOException e) {
			System.err.println("Position error: " + e.getMessage());
		}
		return 0;
	}

	public long GetPosition(long pos, long offset) {
		sectorOffset = calculateFromRawOffset(pos);
		offset += sectorOffset[1];

		for (int i = 0; i < 3; i++) {
			if (offset >= USER_DATA_SIZE) {
				sectorOffset[0]++;
				offset -= USER_DATA_SIZE;
				i = 0;
			}
		}
		sectorOffset[1] = (int)offset;

		return calculateSectorPointer(sectorOffset);
	}

	public void SkipShort(int count) {
		SkipByte(count * 2);
	}

	public void SkipByte(int count) {
		sectorOffset[1] += count;

		for (int i = 0; i < 3; i++) {
			if (sectorOffset[1]>= USER_DATA_SIZE) {
				sectorOffset[0]++;
				sectorOffset[1] -= USER_DATA_SIZE;
				i = 0;
			}
		}
		long filePosition = calculateSectorPointer(sectorOffset);

		try {
			randomAccessFile.seek(filePosition);
		} catch (IOException e) {
			System.err.println("Position error: " + e.getMessage());
		}
	}

	public String[] ReadStringTable(long length) {
		StringBuilder string = new StringBuilder();
		List<String> list = new ArrayList<>();

		try {
			boolean isBool = false;

			//Read
			for (int x = 0; x < length; x++) {
				//Byte
				int readByte = randomAccessFile.readUnsignedByte();

				//Byte is a String
				if (readByte != 0) {
					//Build String
					string.append((char)readByte);
					isBool = true;
				} else {
					if (isBool) {
						//Add to List
						list.add(string.toString());

						//Reset String
						string.setLength(0);
						isBool = false;
					}
				}

				UpdateSector(1);
			}
		} catch (IOException e) {
			System.err.println("ReadByte error: " + e.getMessage());
		}

		return list.toArray(String[]::new);
	}

	public int[] ReadAttributeTable(int count) {
		int[] table = new int[count];

		try {
			for (int i = 0; i < count; i++) {
				table[i] = randomAccessFile.readUnsignedShort();
				UpdateSector(2);
			}
		} catch (IOException e) {
			System.err.println("ReadByte error: " + e.getMessage());
		}
		return table;
	}

	public void WriteAttributeTable(int[] table) {
		try {
			for (int i = 0; i < table.length; i++) {
				randomAccessFile.writeShort(table[i]);
				UpdateSector(2);
			}
		} catch (IOException e) {
			System.err.println("Error: " + e.getMessage());
		}
	}


	public void SetStringMap(long start, long offset, long length){
		//Position
		SetPosition(start, offset);

		//Define
		stringMap = new int[(int)length];
		int i = 0;
		boolean isBool = false;

		try {
			//Read
			for (int x = 0; x < length; x++) {
				//Byte
				int readByte = randomAccessFile.readUnsignedByte();

				//Byte is a String
				if (readByte != 0) {
					stringMap[x] = i;
					isBool = true;
				} else {
					if (isBool) { i++; }
					isBool = false;
				}

				//Sector
				UpdateSector(1);
			}
		} catch (IOException e) {
			System.err.println("ReadByte error: " + e.getMessage());
		}
	}

	public int[] ReadPointerTable(int count) {
		int[] table = new int[count];

		long start = 0;
		int p;

		try {
			for (int i = 0; i < count; i++) {
				p = randomAccessFile.readUnsignedShort();
				UpdateSector(2);
				p = (p << 16) | (randomAccessFile.readUnsignedShort() & 0xFFFF);
				UpdateSector(2);
				if (i == 0) {start = p;}
				p -= start;

				table[i] = stringMap[p];
			}
		} catch (IOException e) {
			System.err.println("ReadByte error: " + e.getMessage());
		}
		return table;
	}


	public void UpdateSector(int byteSize) {
		sectorOffset[1] += byteSize;
		if (sectorOffset[1]>= USER_DATA_SIZE) {
			sectorOffset[0] ++;
			sectorOffset[1] = 0;

			//Position to the next sector
			long filePosition = calculateSectorPointer(sectorOffset);
			try {
				randomAccessFile.seek(filePosition);
			} catch (IOException e) {
				System.err.println("Position error: " + e.getMessage());
			}
		}
	}

	public void PrintPosition() {
		long filePosition = calculateSectorPointer(sectorOffset);
		System.err.println("Position: " + Long.toHexString(filePosition));
	}

	public void PrintPosition(long pos) {
		System.err.println("Position: " + Long.toHexString(pos));
	}

	private static long calculateSectorPointer(int[] sectorOffset) {
		return ((long) sectorOffset[0] * SECTOR_SIZE) + USER_DATA_OFFSET + sectorOffset[1];
	}

	public static int[] calculateFromRawOffset(long rawDataOffset) {
		rawDataOffset -= USER_DATA_OFFSET;

		int[] sector = new int[2];
		sector[0] = (int) (rawDataOffset / SECTOR_SIZE);
		sector[1] = (int) (rawDataOffset - sector[0] * SECTOR_SIZE);
		return sector;
	}


	public void closeFile() {
		if (randomAccessFile != null) {
			try {
				randomAccessFile.close();
				System.out.println("Shared file closed safely.");
			} catch (IOException e) {
				System.err.println("Error closing file: " + e.getMessage());
			} finally {
				randomAccessFile = null;
				isFileOpen = false;
			}
		}
	}
}
