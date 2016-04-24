package de.htw.ds.sync;

import java.util.Arrays;

public class VectorMathMultiThreaded {
	static private final int PROCESSOR_COUNT = Runtime.getRuntime().availableProcessors();

	static public double[] add (final double[] leftOperand, final double[] rightOperand) {
		if (leftOperand.length != rightOperand.length) throw new IllegalArgumentException();
		final double[] result = new double[leftOperand.length];
		for (int index = 0; index < leftOperand.length; ++index) {
			result[index] = leftOperand[index] + rightOperand[index];
		}
		return result;
	}
	
	static public double[][] mux (final double[] leftOperand, final double[] rightOperand) {
		final double[][] result = new double[leftOperand.length][rightOperand.length];
		for (int leftIndex = 0; leftIndex < leftOperand.length; ++leftIndex) {
			for (int rightIndex = 0; rightIndex < rightOperand.length; ++rightIndex) {
				result[leftIndex][rightIndex] = leftOperand[leftIndex] * rightOperand[rightIndex];
			}
		}
		return result;
	}

	static public void main (final String[] args) {
		final int dimension = args.length == 0 ? 10 : Integer.parseInt(args[0]);

		final double[] a = new double[dimension], b = new double[dimension];
		for (int index = 0; index < dimension; ++index) {
			a[index] = index + 1.0;
			b[index] = index + 2.0;
		}
		System.out.format("Computation is performed on %s processor core(s):\n", PROCESSOR_COUNT);

		final long timestamp0 = System.currentTimeMillis();
		final double[] sum = add(a, b);
		final long timestamp1 = System.currentTimeMillis();
		System.out.format("a + b took %sms to compute.\n", timestamp1 - timestamp0);

		final long timestamp2 = System.currentTimeMillis();
		final double[][] mux = mux(a, b);
		final long timestamp3 = System.currentTimeMillis();
		System.out.format("a x b took %sms to compute.\n", timestamp3 - timestamp2);

		if (dimension <= 100) {
			System.out.print("a = ");
			System.out.println(Arrays.toString(a));
			System.out.print("b = ");
			System.out.println(Arrays.toString(b));
			System.out.print("a + b = ");
			System.out.println(Arrays.toString(sum));
			System.out.print("a x b = [");
			for (int index = 0; index < mux.length; ++index) {
				System.out.print(Arrays.toString(mux[index]));
			}
			System.out.println("]");
		}
	}
	
}
