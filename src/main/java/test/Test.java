package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.text.StrTokenizer;
import org.grouplens.lenskit.data.text.DelimitedColumnEventFormat;
import org.grouplens.lenskit.data.text.EventTypeDefinition;
import org.lenskit.data.dao.DataAccessObject;
import org.lenskit.data.dao.EntityCollectionDAO;
import org.lenskit.data.dao.EntityQuery;
import org.lenskit.data.dao.EventCollectionDAO;
import org.lenskit.data.dao.file.DelimitedColumnEntityFormat;
import org.lenskit.data.dao.file.EntitySource.Layout;
import org.lenskit.data.dao.file.StaticDataSource;
import org.lenskit.data.dao.file.TextEntitySource;
import org.lenskit.data.entities.AttributeSet;
import org.lenskit.data.entities.CommonAttributes;
import org.lenskit.data.entities.Entity;
import org.lenskit.data.entities.EntityType;
import org.lenskit.data.entities.TypedName;
import org.lenskit.data.events.Event;
import org.lenskit.data.ratings.EntityCountRatingVectorPDAO;
import org.lenskit.util.io.LineStream;
import org.lenskit.util.io.ObjectStream;
import org.lenskit.util.io.ObjectStreams;

import com.google.common.collect.ImmutableSet;

import it.unimi.dsi.fastutil.longs.LongSet;

public class Test {
	public static void main(String[] args) throws IOException {
		TextEntitySource es = new TextEntitySource("user");
		es.setFile(new File("data/user.csv").toPath());
		DelimitedColumnEntityFormat format = new DelimitedColumnEntityFormat();
//		DelimitedColumnEventFormat
		format.setDelimiter(",");
		format.setHeader(true);
		format.setEntityType(EntityType.forName("user"));
		format.addColumn(CommonAttributes.USER_ID);
		format.addColumn(CommonAttributes.NAME);

//		format.addColumn(CommonAttributes.ENTITY_ID);
		es.setFormat(format);
//		Map<String, Object> map = es.getMetadata();
//		System.out.println(map.values());
//		System.out.println("đá");
		StaticDataSource sds = new StaticDataSource("haa");
//		sds.load(new File("data/test.yml").toPath());
		sds.addSource(es);
//		DataAccessObject accessObject = sds.getSources().size();
//		LongSet longSets = accessObject.getEntityIds(EntityType.forName("item"));
//		System.out.println(longSets);
//		System.out.println(sds.getSources());
//		Layout layout = es.getLayout();
//		System.out.println(layout);
//		AttributeSet attributeSet = layout.getAttributes();
//		System.out.println(attributeSet.size());
//		TypedName<?> s= attributeSet.getAttribute(2);
//		System.out.println(s.getName());
		DataAccessObject dao = sds.get();
		System.out.println(dao);
//		LongSet longSet = dao.getEntityIds(EntityType.forName("items"));
//		System.out.println(sds.getSources());
//		BufferedReader bf = new BufferedReader(new FileReader(new File("data\\rating.csv")));
//		LineStream ls = new LineStream(bf);
//		ObjectStream<Entity> os = dao.streamEntities(EntityType.forName("user")); 
////		st.
//		List<Entity> os1 = ObjectStreams.makeList(os);
//		EntityCountRatingVectorPDAO ecd = new EntityCountRatingVectorPDAO(dao, EntityType.forName("user"));
//		ObjectStream<List<String>> s = ls.tokenize(null)\
//		System.out.println(ecd.userRatingVector(4L));
		ImmutableSet.Builder<String> s= ImmutableSet.builder();
	}
}
