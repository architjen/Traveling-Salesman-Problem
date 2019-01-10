import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class RandomGenerator {
	double rangeMin = -100.0;
	double rangeMax = 100.0;

	public RandomGenerator() {
	}

	public RandomGenerator(double rangeMin, double rangeMax) {
		this.rangeMin = rangeMin;
		this.rangeMax = rangeMax;
	}

	public void generateInstanceFile(String fileName, String address, int number) throws IOException {
		String filePath = address + "/" + fileName;
		FileWriter fw = new FileWriter(filePath);

		String newLine = System.getProperty("line.separator");
		fw.write("NAME: " + fileName);
		fw.write(newLine);
		for (int i = 1; i < number + 1; i++) {
			String newXY = generateXY();
			fw.write(" " + i + " " + newXY + newLine);
		}
		fw.write("EOF");
		fw.close();
	}

	public String generateXY() {
		Random r = new Random();
		double X = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		X = Math.round(X * 100.0) / 100.0;
		double Y = rangeMin + (rangeMax - rangeMin) * r.nextDouble();
		Y = Math.round(Y * 100.0) / 100.0;
		String xy = X + "  " + Y;
		return xy;
	}

	public static int MAX_PARAM_NUM = 3;
	public static void main(String[] args) {
		String help = "Please enter:"
				+ "java RandomGenerator.jar <file location> <file name with no extention> <Number of nodes> +\n";
		if (args == null || args.length < MAX_PARAM_NUM) {
			System.out.println(help);
			return;
		}

		String loc = args[0];
		String fileName = args[1];
		String numOfVertices = args[2];
		int vertices = 0;
		try {

			vertices = Integer.parseInt(numOfVertices);
			if (vertices < 0) {
				System.out.println(help);
				return;
			}
			RandomGenerator generator = new RandomGenerator();
			generator.generateInstanceFile(fileName, loc, vertices);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(help);
			return;
		}

	}

}
