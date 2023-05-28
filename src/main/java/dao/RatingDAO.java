package dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.grouplens.lenskit.data.text.DelimitedColumnEventFormat;
import org.grouplens.lenskit.data.text.Field;
import org.grouplens.lenskit.data.text.TextEventDAO;
import org.lenskit.data.dao.EventCollectionDAO;
import org.lenskit.data.dao.EventDAO;
import org.lenskit.data.dao.SortOrder;
import org.lenskit.data.dao.file.DelimitedColumnEntityFormat;
import org.lenskit.data.dao.file.TextEntitySource;
import org.lenskit.data.entities.Entity;
import org.lenskit.data.entities.TypedName;
import org.lenskit.data.events.Event;
import org.lenskit.util.io.ObjectStream;
import org.lenskit.util.io.ObjectStreams;

import com.google.common.collect.ImmutableSet;

import anotations.RatingFile;
import contants.AttributesContants;
import contants.SystemConstants;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class RatingDAO extends AbstractDao implements EventDAO {
	private transient volatile EventCollectionDAO eventCache;
	private File file;

	@Inject
	public RatingDAO(@RatingFile File file) {
		// TODO Auto-generated constructor stub
		this.file = file;
	}

	private void loadEvent() {
		if (eventCache == null) {
			synchronized (this) {
				if (eventCache == null) {
					DelimitedColumnEventFormat dcef = DelimitedColumnEventFormat.create("like");// type event is
					// rating, like and
					// like-batch
					dcef.setDelimiter(SystemConstants.COMMA);
					dcef.setHeaderLines(0);
//					dcef.setFields(dcef.getFields());
					TextEventDAO dao = new TextEventDAO(this.file, dcef);
					ObjectStream<Event> e = dao.streamEvents();
					ArrayList<Event> e1 = ObjectStreams.makeList(dao.streamEvents());
					eventCache = (EventCollectionDAO) EventCollectionDAO
							.create(ObjectStreams.makeList(dao.streamEvents()));
				}
			}
		}
	}

	@Override
	public ObjectStream<Event> streamEvents() {
		loadEvent();
		return eventCache.streamEvents();
	}

	@Override
	public <E extends Event> ObjectStream<E> streamEvents(Class<E> type) {
		loadEvent();
		return eventCache.streamEvents(type);
	}

	@Override
	public <E extends Event> ObjectStream<E> streamEvents(Class<E> type, SortOrder order) {
		loadEvent();
		return eventCache.streamEvents(type, order);
	}

}
