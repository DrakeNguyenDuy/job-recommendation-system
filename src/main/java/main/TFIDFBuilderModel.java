package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Provider;

import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.lenskit.inject.Transient;

import dao.ItemDao;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import utils.ScoreUtils;

public class TFIDFBuilderModel implements Provider<TFIDFModel> {
	private ItemDao itemDao;
	private Map<String, Long> idMapFeature;

	@Inject
	public TFIDFBuilderModel(@Transient ItemDao itemDao) {
		// TODO Auto-generated constructor stub
		this.itemDao = itemDao;
	}

	@Override
	public TFIDFModel get() {
		// Create a map contain id map to each feature
		idMapFeature = createFeaturesIdMap();
		// Tạo một vector đề lưu trữ tần xuất xuất hiện của mỗi đặc trưng đối với tất cả
		// Item
		MutableSparseVector vectorDF = MutableSparseVector.create(idMapFeature.values());
		vectorDF.fill(0.0);
		// Tính tf cho mỗi công việc
		// Và đồng thời tính xây dựng luôn vector DF
		// Áp dụng IDF cho mỗi vector tf và chuẩn hóa đơn vị vector cho nó

		// Tạo map để chứa vector tf của công việc
		// Mỗi id item sẽ được map đến một vector tương ứng
		Map<Long, MutableSparseVector> itemVectors = new HashMap<Long, MutableSparseVector>();
//		// Tạo một vector để lưu trữ tần xuất xuất hiện của đặc trưng (TF) trong mỗi
//		// item
//		MutableSparseVector vectorTFOfItem = MutableSparseVector.create(idMapFeature.values());
		// Tạo một vector để đánh dấu những đặc trưng nào có trong item
		// Nếu có đặc trưng đó thì continue
		// Nếu chưa có đặc trưng đó thì set id với 1
//		MutableSparseVector vectorTFTickFeaturesItem = MutableSparseVector.create(idMapFeature.values());

		// Lấy tập hợp id của tất cả item
		LongSet idAllItems = itemDao.getItemIds();
		for (Long idItem : idAllItems) {
			// Reset vector TF của item
//			vectorTFOfItem.clear();
			// Tạo một vector để lưu trữ tần xuất xuất hiện của đặc trưng (TF) trong mỗi
			// item
			MutableSparseVector vectorTFOfItem = MutableSparseVector.create(idMapFeature.values());
//			vectorTFOfItem.fill(0);
			// Lấy các đặc trưng trong item có theo id item
			List<String> featureOfItem = itemDao.getFeatureByIdItem(idItem);
			for (String feature : featureOfItem) {
				long featureID = idMapFeature.get(feature);// Lấy id của đặc trưng trong map feature->id
				try {
					/*
					 * Đếm số lần xuất hiện của các đặc trưng trong mỗi item. Nếu đã tồn tại giá trị
					 * thì tăng giá trị thêm 1. Nếu chưa có giá trị thì khởi tạo giá trị bằng 1.
					 */
					vectorTFOfItem.set(featureID, vectorTFOfItem.get(featureID) + 1);
				} catch (IllegalArgumentException e) {
					vectorTFOfItem.set(featureID, 1);
					// Thêm giá trị vào vector đánh dấu các đặc trưng có xuất hiện trong item
//					vectorTFTickFeaturesItem.set(featureID, 1);
					// Thêm vào vector DF để đếm số lần xuất hiện trong các văn bản (Công việc)
					vectorDF.set(featureID, vectorDF.get(featureID) + 1);
				}
			}
			// Thêm id item và vector TF của nó vào map để lưu trữ
			itemVectors.put(idItem, vectorTFOfItem);
			// vectorTFOfItem sẽ được reset và bắt đấu các bước trên
		}
		// Tính IDF cho từng đặc trưng
		int numOfItems = idAllItems.size();// Số lượng tất cả item (Công việc)
		for (VectorEntry ve : vectorDF) {
			double df = ve.getValue();// Lấy DF của đặc trưng
			double idf = Math.log(numOfItems / df);// Tính IDF
			if(Double.isInfinite(idf)) {
				vectorDF.set(ve.getKey(), 0);
			}else {
				vectorDF.set(ve.getKey(), idf);// Put lại vào trong vector DF	
			}
		}
		// Tính TF-IDF chỗ mỗi đặc trưng của item (Công việc)
		// Tạo một map để lưu trữ model data
		Map<Long, MutableSparseVector> modelData = new HashMap<Long, MutableSparseVector>();
		Set<Map.Entry<Long, MutableSparseVector>> setVectorTFItem = itemVectors.entrySet();
		for (Map.Entry<Long, MutableSparseVector> veEntry : setVectorTFItem) {
			MutableSparseVector vectorTfItemTemp = veEntry.getValue();
			for (VectorEntry entry : vectorTfItemTemp) {
				long idFeature = entry.getKey();
				int countSpecificFeature = ScoreUtils.countFeatureSpecific(vectorTfItemTemp);
				double tf = entry.getValue() / countSpecificFeature;
				double idf = vectorDF.get(idFeature);
//				System.out.println(idf*tf);
				vectorTfItemTemp.add(idFeature, tf * idf);
			}
			modelData.put(veEntry.getKey(), vectorTfItemTemp);
		}
		Map<Long, Double> idToDFMap = createIDToDFMap();
		return new TFIDFModel(idMapFeature, modelData, idToDFMap);
	}

	// Tạo map feature -> id feature
	private Map<String, Long> createFeaturesIdMap() {
		Set<String> set = itemDao.getAllFeatures();
		Map<String, Long> idToFeature = new HashMap<String, Long>();
		for (String string : set) {
			idToFeature.put(string, idToFeature.size() + 1L);
		}
		return idToFeature;
	}

	// create a map from id item to df value
	@SuppressWarnings("deprecation")
	public Map<Long, Double> createIDToDFMap() {
		Set<String> set = idMapFeature.keySet();
//		Long2ObjectMap<Double> result = new Long2ObjectOpenHashMap<Double>();
		Map<Long, Double> result = new HashMap<Long, Double>();
		Long2ObjectMap<List<String>> mapAllItem = itemDao.getMapAllItem();
		for (String feature : set) {
			Integer count = 0;
			Set<Long> idItemSet = mapAllItem.keySet();
			for (Long idItem : idItemSet) {
				if (mapAllItem.get(idItem).contains(feature))
					count++;
			}
			result.put(idMapFeature.get(feature), (double) (mapAllItem.size() / count));
		}
		return result;
	}
	
//	private Map<Long, MutableSparseVector> normalizerMatrixItem(){
//		
//	}
}
