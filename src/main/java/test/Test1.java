package test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

import org.lenskit.LenskitConfiguration;
import org.lenskit.LenskitRecommender;
import org.lenskit.LenskitRecommenderEngine;
import org.lenskit.api.ItemScorer;
import org.lenskit.data.dao.DataAccessObject;
import org.lenskit.data.dao.file.StaticDataSource;
import org.lenskit.data.entities.EntityType;
import org.lenskit.knn.item.ItemItemScorer;

import it.unimi.dsi.fastutil.longs.LongSet;

public class Test1 {
	public static void main(String[] args) throws IOException {
		//prepare source data
		StaticDataSource sds = StaticDataSource.load(Paths.get("data/test.yml"));
		DataAccessObject dao = sds.get();
		LongSet ls = dao.getEntityIds(EntityType.forName("user"));
		//configuration the Lenskit
		LenskitConfiguration config = new LenskitConfiguration();
		config.bind(ItemScorer.class).to(ItemItemScorer.class);
//		config.s
		Set<EntityType> set = dao.getEntityTypes();
		System.out.println(set);
		
	}
}
