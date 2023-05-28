package test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.lenskit.LenskitConfiguration;
import org.lenskit.data.dao.DataAccessObject;
import org.lenskit.data.dao.EventCollectionDAO;
import org.lenskit.data.dao.EventDAO;
import org.lenskit.data.dao.file.DelimitedColumnEntityFormat;
import org.lenskit.data.dao.file.StaticDataSource;
import org.lenskit.data.dao.file.TextEntitySource;
import org.lenskit.data.entities.CommonAttributes;
import org.lenskit.data.entities.Entity;
import org.lenskit.data.entities.EntityType;
import org.lenskit.data.entities.TypedName;
import org.lenskit.data.events.Event;
import org.lenskit.data.ratings.RatingSummary;
import org.lenskit.util.collections.LongUtils;
import org.lenskit.util.io.ObjectStream;
import org.lenskit.util.io.ObjectStreamIterator;
import org.lenskit.util.io.ObjectStreams;

import com.google.common.collect.ImmutableList;

import contants.AttributesContants;
import contants.SystemConstants;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;

public class Main {
	private transient volatile EventCollectionDAO collectionDAO;

	private LenskitConfiguration configuration() {
		LenskitConfiguration config = new LenskitConfiguration();
		// configure the rating data source
//		config.bind(EventDAO.class). to(RatingDAO.class);
//		ObjectStreams os = ObjectStreams.makeList(null)
		return config;
	}

	public void s() {

		TextEntitySource es = new TextEntitySource();
		es.setFile(new File("data/ratings.csv").toPath());
		DelimitedColumnEntityFormat format = new DelimitedColumnEntityFormat();
		format.setDelimiter(",");
		format.setHeader(true);
		format.setEntityType(EntityType.forName("rating"));
		format.addColumn(CommonAttributes.ENTITY_ID);
//		format.addColumn(CommonAttributes.ITEM_ID);
//		format.addColumn(CommonAttributes.RATING);
		es.setFormat(format);
		StaticDataSource sds = new StaticDataSource();
		sds.addSource(es);
		DataAccessObject dao = sds.get();
//		ObjectStream<Entity> os = dao.streamEntities(EntityType.forName("rating"));
//		Collection<Entity> list = ObjectStreams.makeList(os);
//		collectionDAO = (EventCollectionDAO) EventCollectionDAO.create((Collection<? extends Event>) list);
//		System.out.println(collectionDAO);
	}

	public void s1() {
		TypedName<?> typeNames[] = { AttributesContants.ENTITY_ID, AttributesContants.NAME, AttributesContants.SKILLS,
				AttributesContants.ADDRESS, AttributesContants.FEATURE4, AttributesContants.FEATURE5,
				AttributesContants.FEATURE6, AttributesContants.FEATURE7, AttributesContants.FEATURE8,
				AttributesContants.FEATURE9, AttributesContants.FEATURE10, AttributesContants.FEATURE11,
				AttributesContants.FEATURE12, AttributesContants.FEATURE13, AttributesContants.FEATURE14,
				AttributesContants.FEATURE15, AttributesContants.FEATURE16, AttributesContants.FEATURE17,
				AttributesContants.FEATURE18, AttributesContants.FEATURE19, AttributesContants.FEATURE20 };
		TextEntitySource es = new TextEntitySource();
		DelimitedColumnEntityFormat format = new DelimitedColumnEntityFormat();
		format.setDelimiter(",");
		format.setHeader(true);
//		format.addColumn(CommonAttributes.USER_ID);
//		format.addColumn(CommonAttributes.ITEM_ID);
//		format.addColumn(CommonAttributes.RATING);
		format.addColumns(typeNames);
		es.setFormat(format);
		es.setFile(new File("data/job.csv").toPath());
		try {
			ObjectStream<Entity> ose = es.openStream();
			ObjectStreamIterator<Entity> sss = (ObjectStreamIterator<Entity>) ose.iterator();
			for (Entity entity : ose) {
//				System.out.println(Long.parseLong((String) entity.get(SystemConstants.ID)));
				System.out.println(entity.get("id").toString() instanceof String);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Main m = new Main();
//		m.s1();
		long[] key = { 0L, 1L };
		MutableSparseVector temp = MutableSparseVector.create(key);
		temp.clear();
//		System.out.println(temp.get(1L));
//		System.out.println(temp);

//		long[] i1 = { 11, 14, 24, 38, 63, 85, 105, 107, 114, 121, 134, 141, 146, 153, 187, 194, 238, 243, 268, 274, 275,
//				280, 393, 414, 462, 557, 558, 585, 601, 629, 641, 664, 672, 680, 786, 788, 812, 854, 1572, 1637, 1891,
//				1892, 1900, 2501, 2502, 3049, 4327, 5503, 7443, 8467, 9331, 9741, 9802, 9806, 10020, 36657, 36658 };
//		long[] i2 = { 607, 161, 807, 278, 180, 22, 12, 98, 954, 671, 1422, 36955, 809, 808, 597, 120, 857, 604, 640,
//				2164, 1894, 155, 550, 862, 603, 1597, 2024, 752, 329, 8587, 424, 955, 581, 8358, 745, 272, 122, 197, 13,
//				602, 77, 453, 568 };
//		LongSet ls1 = new LongOpenHashSet();
//		LongSet ls2 = new LongOpenHashSet();
//		for (long l : i1) {
//			ls1.add(l);
//		}
//		for (long l : i2) {
//			ls2.add(l);
//		}
//		System.out.println(ls1.size());
//		LongSet ls = LongUtils.setDifference(ls1, ls2);
//		System.out.println(ls.size());
		
//		 Long2ObjectMap<ImmutableList.Builder<Event>> table =
//                 new Long2ObjectOpenHashMap<ImmutableList.Builder<Event>>();
//		 table.get
	}
}
