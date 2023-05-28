package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.lenskit.data.dao.ItemDAO;
import org.lenskit.data.dao.file.DelimitedColumnEntityFormat;
import org.lenskit.data.dao.file.TextEntitySource;
import org.lenskit.data.entities.Entity;
import org.lenskit.data.entities.TypedName;
import org.lenskit.util.io.ObjectStream;

import com.google.common.collect.ImmutableSet;

import anotations.ItemFile;
import contants.AttributesContants;
import contants.IndexConstants;
import contants.SystemConstants;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;

public class ItemDao extends AbstractDao implements ItemDAO {
	private File file;
	private transient volatile Long2ObjectMap<List<String>> featuresCache;// id item map to a list feature of item
	private transient volatile Set<String> allFeatures;

	@Inject
	public ItemDao(@ItemFile File file) {
		// TODO Auto-generated constructor stub
		this.file = file;
	}

	private void loadFeaturesCache() {
		if (featuresCache == null) {// nếu chưa có dữ liệu trong bộ đệm chứa tất cả các đặc trưng
			synchronized (this) {// đồng bộ tiếng trình
				if (featuresCache == null) {// nếu vẫn null thì thưc hiện load
					featuresCache = new Long2ObjectOpenHashMap<List<String>>();
					TypedName<?> typeNames[] = { AttributesContants.ENTITY_ID, AttributesContants.NAME,
							AttributesContants.SKILLS, AttributesContants.ADDRESS, AttributesContants.FEATURE4,
							AttributesContants.FEATURE5, AttributesContants.FEATURE6, AttributesContants.FEATURE7,
							AttributesContants.FEATURE8, AttributesContants.FEATURE9, AttributesContants.FEATURE10,
							AttributesContants.FEATURE11, AttributesContants.FEATURE12, AttributesContants.FEATURE13,
							AttributesContants.FEATURE14, AttributesContants.FEATURE15, AttributesContants.FEATURE16,
							AttributesContants.FEATURE17, AttributesContants.FEATURE18, AttributesContants.FEATURE19,
							AttributesContants.FEATURE20 };
					String[] columnGets = { SystemConstants.ID, SystemConstants.NAME, SystemConstants.SKILLS,
							SystemConstants.ADDRESS };
					ImmutableSet.Builder<String> featuresSetBuilder = ImmutableSet.builder();
					List<String[]> lines = getObjectStream(typeNames, file, columnGets);
					for (String[] strings : lines) {
						long jobID = Long.parseLong(strings[0]);
						List<String> features = featuresCache.get(jobID);
						if (features == null) {// nếu tại vị trí đó chưa có dữ liệu thì khởi tạo array list, dành cho
												// lần lặp đầu tiên
							features = new ArrayList<String>();
							featuresCache.put(jobID, features);
						}
						for (int i = 2; i < strings.length; i++) {
							// begin is 2 beacause we will skip id and name
							// analys value in each column, value in each cell with format:
							// value1;value2;value3
							String[] analysh = strings[i].split(SystemConstants.SEMICOMMA);
							for (String value : analysh) {
								// add value to features
								features.add(value);
								// add value to set all features
								featuresSetBuilder.add(value);
							}
						}
					}
					allFeatures = featuresSetBuilder.build();
				}
			}
		}
	}

	// get all id of item
	@Override
	public LongSet getItemIds() {
		loadFeaturesCache();
		return featuresCache.keySet();
	}

	// get all features
	public Set<String> getAllFeatures() {
		loadFeaturesCache();
		return allFeatures;
	}

	// get feature by id item
	public List<String> getFeatureByIdItem(long idItem) {
		loadFeaturesCache();
		return featuresCache.get(idItem);
	}
	
	//get map all item
	public Long2ObjectMap<List<String>> getMapAllItem(){
		return featuresCache;
	}
}
