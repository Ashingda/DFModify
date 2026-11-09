import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.nio.file.Path;

public class SBN {

	public List<SpriteObject> spriteObject = new ArrayList<>();
	public String dialogue;
	int ID;
	public boolean isModify;
	public int objectIndex;

	//Constructor
	public SBN(int id, FileStream fileStream) {
		ID = id;
		List<Integer> offsets = new ArrayList<>();
		List<Integer> lengths = new ArrayList<>();
		int elementCount;
		long startPos = fileStream.GetPosition();

		fileStream.ReadByte(3);
		int magic = fileStream.ReadByte();
		fileStream.SetPosition(startPos);

		if (magic > 0) {
			//multi-sprite, probably a General
			elementCount = fileStream.ReadInt();

			for (int i = 0; i < elementCount; i++) {
				offsets.add(fileStream.ReadInt());
				lengths.add(fileStream.ReadInt()); // discard length, read sequentially
			}
		} else {
			//single sprite, probably a Troop
			elementCount = 1;
			offsets.add(0);
		}

		for (int i = 0; i < elementCount; i++) {
			fileStream.SetPosition(startPos, offsets.get(i));
			int a = fileStream.ReadByte();
			int b = fileStream.ReadByte();
			fileStream.SetPosition(startPos, offsets.get(i));

			//Sprite
			if (i < elementCount - 2) { spriteObject.add(new SpriteObject(fileStream, 0)); }
			//Screen
			if (i == elementCount - 2 && a != 66 && b != 71) { spriteObject.add(new SpriteObject(fileStream, 1)); }
			//Dialogue
			if (i == elementCount - 1) { dialogue = fileStream.ReadString(lengths.get(i)); }
		}
	}


	//Object
	public static class SpriteObject {
		public int type;
		public int ID;
		public int maxWidth;
		public int maxHeight;
		public int mirror;
		int offsetToFrameSizes;
		int offsetToPixelData;
		int palCount;
		int frameCount;
		public List<SpritePalette> spritePalette = new ArrayList<>();
		public List<SpriteFrame> spriteFrame = new ArrayList<>();
		public int frameIndex;
		public int unknown;
		int mode;

		public SpriteObject(FileStream fileStream, int m) {
			mode = m;
			if (mode == 0) { HandleSprite(fileStream); }
			if (mode == 1) { HandleScreen(fileStream); }
		}

		void HandleSprite(FileStream fileStream) {
			long startPos = fileStream.GetPosition();
			fileStream.ReadByte(2);
			type = fileStream.ReadByte();
			ID = fileStream.ReadByte();
			fileStream.ReadByte(3);
			maxWidth = fileStream.ReadByte();
			fileStream.ReadByte(2);
			mirror = maxHeight = fileStream.ReadByte();
			maxHeight = fileStream.ReadByte();
			offsetToFrameSizes = fileStream.ReadInt() + 4;
			offsetToPixelData = fileStream.ReadInt();
			palCount = (offsetToFrameSizes - 0x4C) / 32;

			//Load Palettes
			fileStream.SetPosition(startPos, 0x4C);
			for (int i = 0; i < palCount; i++) { spritePalette.add(new SpritePalette(fileStream)); }

			//Frame Count
			fileStream.SetPosition(startPos, offsetToFrameSizes);
			frameCount = fileStream.ReadShort();
			unknown = fileStream.ReadShort();

			//Load Frames
			for (int i = 0; i < frameCount; i++) {
				spriteFrame.add(new SpriteFrame(fileStream, 0));
			}

			//Load Sprite
			for (int i = 0; i < frameCount; i++) {
				fileStream.SetPosition(startPos, offsetToFrameSizes + offsetToPixelData + spriteFrame.get(i).dataOffset);
				spriteFrame.get(i).ReadSprite(fileStream);
				spriteFrame.get(i).GenerateImage(spritePalette);
			}
		}

		void HandleScreen(FileStream fileStream) {
			spritePalette.add(new SpritePalette(fileStream));
			spriteFrame.add(new SpriteFrame(fileStream, 1));
			spriteFrame.get(0).ReadScreen(fileStream, spritePalette);
		}

		public void RefreshAllFrame()
		{
			if (mode == 0) {
				for (int i = 0; i < frameCount; i++) {
					spriteFrame.get(i).GenerateImage(spritePalette);
				}
			}
			if (mode == 1) {
				spriteFrame.get(0).GenerateImage(spritePalette);
			}
		}

		public void RefreshFrame(int i)
		{
			if (mode == 0) {
				spriteFrame.get(i).GenerateImage(spritePalette);
			}
			if (mode == 1) {
				spriteFrame.get(0).GenerateImage(spritePalette);
			}
		}
	}


	//Palette
	public static class SpritePalette {
		public byte[] data;
		public long position;
		public boolean isModify;
		public Color[] color = new Color[16];

		public SpritePalette(FileStream fileStream) {
			position = fileStream.GetPosition();
			data = fileStream.ReadByte(32);
			GenerateColor();
		}

		public void GenerateColor() {
			for (int i = 0; i < 16; i++) {
				int c = (data[i * 2] << 8) | (data[i * 2 + 1] & 0xFF);

				int r = remap((c & 0x1F));
				int g = remap(((c >> 5) & 0x1F));
				int b = remap(((c >> 10) & 0x1F));
				int a = 0xFF;

				color[i] = new Color(r, g, b, a);
			}
			color[0] = new Color(0, 0, 0, 0);
		}

		public void SetData(byte[] inputData) {
			for (int i = 2; i < data.length; i++) {
				data[i] = inputData[i];
			}
		}

		int remap(int c) {
			c = (int)(c * 8.225f);
			if (c < 0) {c = 0;}
			if (c > 255) {c = 255;}
			return c;
		}
	}


	//Frame
	public static class SpriteFrame {
		public byte[] data;
		public long position;
		public boolean isModify;

		public int width;
		public int height;

		public int paletteMode;
		public int paletteSection;
		public int paletteValue;

		public BufferedImage image;
		public int dataOffset;
		public int byteLength;

		public SpriteFrame(FileStream fileStream, int mode) {
			if (mode == 0) {
				width = fileStream.ReadShort();
				height = fileStream.ReadShort();

				fileStream.ReadByte();
				paletteMode = fileStream.ReadByte();
				paletteSection = fileStream.ReadByte();
				paletteValue = fileStream.ReadByte();
				dataOffset = fileStream.ReadInt();
				byteLength = fileStream.ReadInt();
			}
			else if (mode == 1) {
				width = 512;
				height = 256;
				byteLength = width * height;
			}
		}

		public void ReadSprite(FileStream fileStream) {
			position = fileStream.GetPosition();
			data = fileStream.ReadByte(byteLength);
		}

		public void GenerateImage(List<SpritePalette> spritePalette) {
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			if (paletteMode == 16) {
				int d = -1;
				for (int y = height - 1; y > -1; y--) {
					for (int x = 0; x < width; x += 1) {
						d++;
						int pal = (data[d] >> 4) & 0xF;
						image.setRGB(x, y, spritePalette.get(pal).color[data[d] & 0xF].getRGB());
					}
				}
			} else {
				int d = -1;
				for (int y = 0; y < height; y++) {
					for (int x = 0; x < width; x += 2) {
						d++;
						int pal = (paletteSection & 0xF) * 8 + ((paletteValue >> 4) & 0xF) / 2;
						image.setRGB(x, y, spritePalette.get(pal).color[(data[d] >> 4) & 0xF].getRGB());
						image.setRGB(x + 1, y, spritePalette.get(pal).color[data[d] & 0xF].getRGB());
					}
				}
			}
		}

		public void ReadScreen(FileStream fileStream, List<SpritePalette> spritePalette) {
			byte[] data = fileStream.ReadByte(byteLength);
			image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

			int d = -1;
			for (int y = height - 1; y > -1; y--)
			{
				for (int x = 0; x < width; x += 2) {
					d++;
					int pal = (paletteSection & 0xF) * 8 + ((paletteValue >> 4) & 0xF) / 2;
					image.setRGB(x, y, spritePalette.get(pal).color[(data[d] >> 4) & 0xF].getRGB());
					image.setRGB(x + 1, y, spritePalette.get(pal).color[data[d] & 0xF].getRGB());
				}
			}
		}
	}



	//**********************************************************************************************
	// Save
	//**********************************************************************************************
	public void Save(FileStream fileStream) {
		if (!isModify) {return;}
		int objectCount = GetObjectCount();
		for (int o = 0; o < objectCount; o++)
		{
			//Frame Data
			int frameCount = GetFrameCount(o);
			for (int i = 0; i < frameCount; i++) {
				//Modified
				if (spriteObject.get(o).spriteFrame.get(i).isModify) {
					fileStream.SetPosition(spriteObject.get(o).spriteFrame.get(i).position);
					fileStream.WriteByte(spriteObject.get(o).spriteFrame.get(i).data);
				}
			}

			//Palette Data
			int paletteCount = spriteObject.get(o).spritePalette.size();
			for (int i = 0; i < paletteCount; i++) {
				//Modified
				if (spriteObject.get(o).spritePalette.get(i).isModify) {
					fileStream.SetPosition(spriteObject.get(o).spritePalette.get(i).position);
					fileStream.WriteByte(spriteObject.get(o).spritePalette.get(i).data);
				}
			}


		}
	}







	//**********************************************************************************************
	// Functions
	//**********************************************************************************************


	//Object
	public int GetObjectCount() {
		return spriteObject.size();
	}
	public void SetObjectIndex(int i) {
		i = objectIndex + i;
		if (i < 0) {i = 0;}
		if (i > spriteObject.size()-1) {i = spriteObject.size()-1;}
		objectIndex = i;
	}
	public String GetObjectText() {
		return "Object " + objectIndex + "/" + (spriteObject.size()-1);
	}
	public void Refresh() {
		spriteObject.get(objectIndex).RefreshAllFrame();
	}


	//Frame
	public int GetFrameCount(int objectIndex) {
		return spriteObject.get(objectIndex).spriteFrame.size();
	}
	public void SetFrameIndex(int i) {
		i = spriteObject.get(objectIndex).frameIndex + i;
		if (i < 0) {i = 0;}
		if (i > spriteObject.get(objectIndex).spriteFrame.size()-1) {
			i = spriteObject.get(objectIndex).spriteFrame.size()-1;
		}
		spriteObject.get(objectIndex).frameIndex = i;
	}
	public String GetFrameText() {
		return "Frame " + spriteObject.get(objectIndex).frameIndex + "/" + (spriteObject.get(objectIndex).spriteFrame.size()-1);
	}
	public void SetFrameData(int o, int f, byte[] data) {
		spriteObject.get(o).spriteFrame.get(f).data = data;
		spriteObject.get(o).spriteFrame.get(f).isModify = true;
		isModify = true;
	}
	public BufferedImage GetFrameImage() {
		int frameIndex = spriteObject.get(objectIndex).frameIndex;
		return spriteObject.get(objectIndex).spriteFrame.get(frameIndex).image;
	}
	public BufferedImage GetFrameImage(int o, int f) {
		return spriteObject.get(o).spriteFrame.get(f).image;
	}
	public void SetFrameRefresh(int o, int f) {
		spriteObject.get(o).RefreshFrame(f);
	}
	public void SetFrameRefreshAll(int o) {
		spriteObject.get(o).RefreshAllFrame();
	}

	//Palette
	public byte[] GetPaletteData() {
		int frameIndex = spriteObject.get(objectIndex).frameIndex;
		int paletteSection = spriteObject.get(objectIndex).spriteFrame.get(frameIndex).paletteSection;
		int paletteValue = spriteObject.get(objectIndex).spriteFrame.get(frameIndex).paletteValue;
		int pal = (paletteSection & 0xF) * 8 + ((paletteValue >> 4) & 0xF) / 2;
		return spriteObject.get(objectIndex).spritePalette.get(pal).data;
	}
	public byte[] GetPaletteData(int o, int f) {
		int paletteSection = spriteObject.get(o).spriteFrame.get(f).paletteSection;
		int paletteValue = spriteObject.get(o).spriteFrame.get(f).paletteValue;
		int pal = (paletteSection & 0xF) * 8 + ((paletteValue >> 4) & 0xF) / 2;
		return spriteObject.get(o).spritePalette.get(pal).data;
	}
	public void SetPaletteData(int o, int f, byte[] data) {
		int paletteSection = spriteObject.get(o).spriteFrame.get(f).paletteSection;
		int paletteValue = spriteObject.get(o).spriteFrame.get(f).paletteValue;
		int pal = (paletteSection & 0xF) * 8 + ((paletteValue >> 4) & 0xF) / 2;
		spriteObject.get(o).spritePalette.get(pal).SetData(data);
		spriteObject.get(o).spritePalette.get(pal).GenerateColor();
		spriteObject.get(o).spritePalette.get(pal).isModify = true;
		spriteObject.get(o).RefreshAllFrame();
		ImportFrame();
	}
	public Color[] GetPaletteColor() {
		int frameIndex = spriteObject.get(objectIndex).frameIndex;
		int paletteSection = spriteObject.get(objectIndex).spriteFrame.get(frameIndex).paletteSection;
		int paletteValue = spriteObject.get(objectIndex).spriteFrame.get(frameIndex).paletteValue;
		int pal = (paletteSection & 0xF) * 8 + ((paletteValue >> 4) & 0xF) / 2;
		return spriteObject.get(objectIndex).spritePalette.get(pal).color;
	}









	//Export
	public void ExportFrame() {
		int o = objectIndex;
		int f = spriteObject.get(o).frameIndex;

		ExportImage(o, f);
	}
	public void ExportAllFrame() {
		int o = objectIndex;
		int count = GetFrameCount(o);

		for (int f = 0; f < count; f++) {
			ExportImage(o, f);
		}
	}
	public void ExportImage(int o, int f) {
		String exportPath = "sprites/" + String.format("%03d", ID);
		String fileName = String.format("%02d", o) + String.format("%02d", f) + ".png";
		File file = new File(exportPath + "/" + fileName);

		try {
			Files.createDirectories(Paths.get(exportPath));
			boolean success = ImageIO.write(spriteObject.get(o).spriteFrame.get(f).image, "png", file);
			if (success) {
				System.out.println("Image successfully saved to: " + file.getAbsolutePath());
			}
		} catch (IOException e) {
			System.err.println("An error occurred while saving the file: " + e.getMessage());
		}
	}


	//Import
	public void ImportFrame() {
		int o = objectIndex;
		int f = spriteObject.get(o).frameIndex;

		ImportImage(o, f);
	}
	public void ImportAllFrame() {
		int o = objectIndex;
		int count = GetFrameCount(o);

		for (int f = 0; f < count; f++) {
			ImportImage(o, f);
		}
	}
	public void ImportImage(int o, int f) {
		String exportPath = "sprites/" + String.format("%03d", ID);
		String fileName = String.format("%02d", o) + String.format("%02d", f) + ".png";
		File file = new File(exportPath + "/" + fileName);

		try {
			Path filePath = Paths.get(exportPath, fileName);
			if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
				SetFrameData(o, f, CreateFrame(0, f, ImageIO.read(file)));
				SetFrameRefresh(o, f);
			}
		} catch (IOException e) {
			System.err.println("An error occurred while Importing Image: " + e.getMessage());
		}
	}






	//Palette
	public void ImportWithPalette() {
		int o = objectIndex;
		int f = spriteObject.get(o).frameIndex;
		String exportPath = "Export/" + String.format("%03d", ID);
		String fileName = String.format("%02d", o) + String.format("%02d", f) + ".png";
		File file = new File(exportPath + "/" + fileName);

		try {
			Path filePath = Paths.get(exportPath, fileName);
			if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
				BufferedImage image = ImageIO.read(file);
				byte[] paletteData = CreatePalette(image);
				SetPaletteData(o, f, paletteData);
				SetFrameData(o, f, CreateFrame(o, f, image));
			}
		} catch (IOException e) {
			System.err.println("An error occurred while Importing Image: " + e.getMessage());
		}
	}






	//Create Palette
	byte[] CreatePalette(BufferedImage image) {

		//Image
		int width = image.getWidth();
		int height = image.getHeight();

		//Scan image and collect unique visible colors
		List<Integer> palette = new ArrayList<>();
		palette.add(0);

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int argb = image.getRGB(x, y);
				int alpha = (argb >>> 24) & 0xFF;

				// Only grab colors from visible pixels
				if (alpha > 128) {
					int rgb = argb & 0x00FFFFFF; // Strip alpha channel

					if (!palette.contains(rgb)) {
						palette.add(rgb);
					}
				}
			}
		}

		//Format and pack colors into standard Saturn BGR555 format (32 bytes total)
		byte[] data = new byte[16 * 2];
		for (int i = 0; i < 16; i++) {
			short saturnColor = 0;

			if (i < palette.size() && i > 0) {
				int rgb = palette.get(i);
				int red   = (rgb >> 16) & 0xFF;
				int green = (rgb >> 8)  & 0xFF;
				int blue  =  rgb         & 0xFF;

				// Scale down 8-bit channels to 5-bit
				int r5 = red   >> 3;
				int g5 = green >> 3;
				int b5 = blue  >> 3;

				if (r5 == 0 && g5 == 0 && b5 == 0) {
					r5 = 1; // Elevate to the minimum safe visible color threshold
					g5 = 1;
					b5 = 1;
				}

				// Pack into Saturn BGR555: Bit 15 set to 1 means visible color
				saturnColor = (short) ((b5 << 10) | (g5 << 5) | r5);
			}

			// Write palette entries as Big-Endian (SH-2 CPU standard)
			data[i * 2]     = (byte) ((saturnColor >> 8) & 0xFF);
			data[i * 2 + 1] = (byte) (saturnColor & 0xFF);
		}
		return data;
	}


	//Create Frame
	byte[] CreateFrame(int o, int f, BufferedImage image) {

		int[] referencePalette = new int[16];
		byte[] palBuffer = GetPaletteData(o, f);

		// Parse Big-Endian BGR555 colors into standard RGB ints
		for (int i = 0; i < 16; i++) {
			int highByte = palBuffer[i * 2] & 0xFF;
			int lowByte  = palBuffer[i * 2 + 1] & 0xFF;
			int bgr555   = (highByte << 8) | lowByte;

			referencePalette[i] = decodeSaturnBGR555(bgr555);
		}

		int width = image.getWidth();
		int height = image.getHeight();

		if (width % 2 != 0) {
			throw new IllegalArgumentException("Error: Sprite width must be even for 4-bit nibble packing.");
		}

		int[] rawIndices = new int[width * height];
		int pixelCounter = 0;

		// 3. Map pixels directly against loaded palette values
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int argb = image.getRGB(x, y);
				int alpha = (argb >>> 24) & 0xFF;

				if (alpha <= 128) {
					rawIndices[pixelCounter++] = 0; // Index 0 is strictly transparent
				} else {
					int targetRgb = argb & 0x00FFFFFF;
					int matchedIndex = -1;
					int minDistance = Integer.MAX_VALUE;

					// Skip index 0 during color lookup since it's transparent
					for (int i = 1; i < 16; i++) {
						int palRgb = referencePalette[i];

						// Extract color components for distance check
						int r1 = (targetRgb >> 16) & 0xFF;
						int g1 = (targetRgb >> 8)  & 0xFF;
						int b1 =  targetRgb         & 0xFF;

						int r2 = (palRgb >> 16) & 0xFF;
						int g2 = (palRgb >> 8)  & 0xFF;
						int b2 =  palRgb         & 0xFF;

						// Euclidean distance math to find closest structural match
						int distance = ((r1 - r2) * (r1 - r2)) +
								((g1 - g2) * (g1 - g2)) +
								((b1 - b2) * (b1 - b2));

						if (distance < minDistance) {
							minDistance = distance;
							matchedIndex = i;
						}
					}
					rawIndices[pixelCounter++] = (matchedIndex != -1) ? matchedIndex : 1;
				}
			}
		}

		//Pack the matched indexes 2-per-byte (4-bit chunks)
		byte[] packedBytes = new byte[(width * height) / 2];
		for (int i = 0; i < packedBytes.length; i++) {
			int leftPixel  = rawIndices[i * 2];
			int rightPixel = rawIndices[i * 2 + 1];

			packedBytes[i] = (byte) ((leftPixel << 4) | (rightPixel & 0x0F));
		}

		return packedBytes;
	}



	private static int decodeSaturnBGR555(int bgr555) {
		int b5 = (bgr555 >> 10) & 0x1F;
		int g5 = (bgr555 >> 5)  & 0x1F;
		int r5 =  bgr555         & 0x1F;

		// Scale 5-bit depth up to standard 8-bit depth
		int r8 = (r5 << 3) | (r5 >> 2);
		int g8 = (g5 << 3) | (g5 >> 2);
		int b8 = (b5 << 3) | (b5 >> 2);

		return (r8 << 16) | (g8 << 8) | b8;
	}

}
