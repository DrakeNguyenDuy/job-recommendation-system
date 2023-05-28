package utils;

import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;

public class ScoreUtils {
	// count feature in user specific
	public static int countFeatureSpecific(SparseVector vector) {
		int count = 0;
		for (VectorEntry vectorEntry : vector) {
			if (vectorEntry.getValue() == 1)
				++count;
		}
		return count;
	}
}
