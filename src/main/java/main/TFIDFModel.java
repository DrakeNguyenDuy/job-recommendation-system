package main;

import java.io.Serializable;
import java.util.Map;
//class này sẽ được dùng để xây dựng model

import org.grouplens.grapht.annotation.DefaultProvider;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.lenskit.inject.Shareable;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;

//LensKit models are annotated with @Shareable so they can be serialized and reused
@Shareable
//This model class will be built by the model builder
@DefaultProvider(TFIDFBuilderModel.class)
public class TFIDFModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Map<String, Long> features = null;
	private Map<Long, MutableSparseVector> itemVectorsMap = null;
	private Map<Long, Double> idToDFMap = null;

	public TFIDFModel(Map<String, Long> features, Map<Long, MutableSparseVector> itemVectorsMap,
			Map<Long, Double> idToDFMap) {
		// TODO Auto-generated constructor stub
		this.features = features;
		this.itemVectorsMap = itemVectorsMap;
		this.idToDFMap = idToDFMap;
	}

	// create a vector contain all features of item
	public MutableSparseVector createVectorFeatures() {
		return MutableSparseVector.create(features.values());
	}

	// get vector item by id
	@SuppressWarnings("static-access")
	public SparseVector getVectorItem(long idItem) {
		SparseVector sv = itemVectorsMap.get(idItem);
		// if sv is null -> not found id item
		return sv == null ? sv.empty() : sv;
	}

	// get map id feature -> name feature
	public Map<String, Long> createMapIDFeaure() {
		return features;
	}

	// compute IDF of feature specific
	public double weightDF(long idFeature) {
		return idToDFMap.get(idFeature);
	}
}
