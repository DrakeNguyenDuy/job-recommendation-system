package test;

import java.io.File;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.lenskit.data.dao.DataAccessObject;
import org.lenskit.data.dao.ItemDAO;
import org.lenskit.data.dao.Query;
import org.lenskit.data.dao.file.DelimitedColumnEntityFormat;
import org.lenskit.data.dao.file.StaticDataSource;
import org.lenskit.data.dao.file.TextEntitySource;
import org.lenskit.data.entities.CommonAttributes;
import org.lenskit.data.entities.Entity;
import org.lenskit.data.entities.EntityType;
import org.lenskit.util.io.LineStream;
import org.lenskit.util.io.ObjectStream;
import org.lenskit.util.io.ObjectStreams;

import com.google.common.collect.ImmutableSet;

import anotations.UserFile;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongSet;

public class ItemDaoImpl implements ItemDAO {
	private File input;
	private Long2ObjectMap<List<String>> itemFeatures; // item id -> list all features of that item
	private Set<String> featureAlls;

	@Inject
	public ItemDaoImpl(@UserFile File file) {
		// TODO Auto-generated constructor stub
		input = file;
	}

	public void loadCache() {
		// if it null then will load it
		if (itemFeatures == null) {
			synchronized (this) {
				if (itemFeatures == null) {
					itemFeatures = new Long2ObjectOpenHashMap<List<String>>();
					ImmutableSet<String> set;
					TextEntitySource es = new TextEntitySource("item");
					es.setFile(input.toPath());
					DelimitedColumnEntityFormat format = new DelimitedColumnEntityFormat();
					format.setDelimiter(",");
					format.setHeader(true);
					format.addColumn(CommonAttributes.ITEM_ID);
					format.setEntityType(EntityType.forName("item"));
					es.setFormat(format);
					StaticDataSource sds = new StaticDataSource("haa");
					sds.addSource(es);
					DataAccessObject dao = sds.get();
					Query<Entity> q = dao.query(EntityType.forName("item"));
					
				}
			}
		}
	}

	@Override
	public LongSet getItemIds() {
		// TODO Auto-generated method stub
		return null;
	}

}
